package org.jivesoftware.smackx.filetransfer;

import java.net.URLConnection;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import org.jivesoftware.smack.ConnectionListener;
import org.jivesoftware.smack.PacketCollector;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.filter.PacketIDFilter;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.packet.IQ.Type;
import org.jivesoftware.smack.packet.XMPPError;
import org.jivesoftware.smack.packet.XMPPError.Condition;
import org.jivesoftware.smackx.FormField;
import org.jivesoftware.smackx.FormField.Option;
import org.jivesoftware.smackx.ServiceDiscoveryManager;
import org.jivesoftware.smackx.packet.DataForm;
import org.jivesoftware.smackx.packet.StreamInitiation;
import org.jivesoftware.smackx.packet.StreamInitiation.File;

public class FileTransferNegotiator
{
  public static final String BYTE_STREAM = "http://jabber.org/protocol/bytestreams";
  public static boolean IBB_ONLY = false;
  public static final String INBAND_BYTE_STREAM = "http://jabber.org/protocol/ibb";
  private static final String[] NAMESPACE = { "http://jabber.org/protocol/si/profile/file-transfer", "http://jabber.org/protocol/si", "http://jabber.org/protocol/bytestreams", "http://jabber.org/protocol/ibb" };
  private static final String[] PROTOCOLS = { "http://jabber.org/protocol/bytestreams", "http://jabber.org/protocol/ibb" };
  protected static final String STREAM_DATA_FIELD_NAME = "stream-method";
  private static final String STREAM_INIT_PREFIX = "jsi_";
  private static final Random randomGenerator;
  private static final Map<XMPPConnection, FileTransferNegotiator> transferObject = new ConcurrentHashMap();
  private final Socks5TransferNegotiatorManager byteStreamTransferManager;
  private final XMPPConnection connection;
  private final StreamNegotiator inbandTransferManager;
  
  static
  {
    randomGenerator = new Random();
  }
  
  private FileTransferNegotiator(XMPPConnection paramXMPPConnection)
  {
    configureConnection(paramXMPPConnection);
    this.connection = paramXMPPConnection;
    this.byteStreamTransferManager = new Socks5TransferNegotiatorManager(paramXMPPConnection);
    this.inbandTransferManager = new IBBTransferNegotiator(paramXMPPConnection);
  }
  
  private void cleanup(XMPPConnection paramXMPPConnection)
  {
    if (transferObject.remove(paramXMPPConnection) != null)
    {
      this.byteStreamTransferManager.cleanup();
      this.inbandTransferManager.cleanup();
    }
  }
  
  private void configureConnection(final XMPPConnection paramXMPPConnection)
  {
    paramXMPPConnection.addConnectionListener(new ConnectionListener()
    {
      public void connectionClosed()
      {
        FileTransferNegotiator.this.cleanup(paramXMPPConnection);
      }
      
      public void connectionClosedOnError(Exception paramAnonymousException)
      {
        FileTransferNegotiator.this.cleanup(paramXMPPConnection);
      }
      
      public void reconnectingIn(int paramAnonymousInt) {}
      
      public void reconnectionFailed(Exception paramAnonymousException) {}
      
      public void reconnectionSuccessful() {}
    });
  }
  
  private DataForm createDefaultInitiationForm()
  {
    DataForm localDataForm = new DataForm("form");
    FormField localFormField = new FormField("stream-method");
    localFormField.setType("list-multi");
    if (!IBB_ONLY) {
      localFormField.addOption(new FormField.Option("http://jabber.org/protocol/bytestreams"));
    }
    localFormField.addOption(new FormField.Option("http://jabber.org/protocol/ibb"));
    localDataForm.addField(localFormField);
    return localDataForm;
  }
  
  public static IQ createIQ(String paramString1, String paramString2, String paramString3, IQ.Type paramType)
  {
    IQ local1 = new IQ()
    {
      public String getChildElementXML()
      {
        return null;
      }
    };
    local1.setPacketID(paramString1);
    local1.setTo(paramString2);
    local1.setFrom(paramString3);
    local1.setType(paramType);
    return local1;
  }
  
  public static FileTransferNegotiator getInstanceFor(XMPPConnection paramXMPPConnection)
  {
    if (paramXMPPConnection == null) {
      throw new IllegalArgumentException("Connection cannot be null");
    }
    if (!paramXMPPConnection.isConnected()) {
      return null;
    }
    if (transferObject.containsKey(paramXMPPConnection)) {
      return (FileTransferNegotiator)transferObject.get(paramXMPPConnection);
    }
    FileTransferNegotiator localFileTransferNegotiator = new FileTransferNegotiator(paramXMPPConnection);
    setServiceEnabled(paramXMPPConnection, true);
    transferObject.put(paramXMPPConnection, localFileTransferNegotiator);
    return localFileTransferNegotiator;
  }
  
  private StreamNegotiator getNegotiator(FormField paramFormField)
    throws XMPPException
  {
    int j = 0;
    int i = 0;
    Iterator localIterator = paramFormField.getOptions();
    while (localIterator.hasNext())
    {
      String str = ((FormField.Option)localIterator.next()).getValue();
      if ((str.equals("http://jabber.org/protocol/bytestreams")) && (!IBB_ONLY)) {
        j = 1;
      } else if (str.equals("http://jabber.org/protocol/ibb")) {
        i = 1;
      }
    }
    if ((j == 0) && (i == 0))
    {
      paramFormField = new XMPPError(XMPPError.Condition.bad_request, "No acceptable transfer mechanism");
      throw new XMPPException(paramFormField.getMessage(), paramFormField);
    }
    if ((j != 0) && (i != 0) && (paramFormField.getType().equals("list-multi"))) {
      return new FaultTolerantNegotiator(this.connection, this.byteStreamTransferManager.createNegotiator(), this.inbandTransferManager);
    }
    if (j != 0) {
      return this.byteStreamTransferManager.createNegotiator();
    }
    return this.inbandTransferManager;
  }
  
  private StreamNegotiator getOutgoingNegotiator(FormField paramFormField)
    throws XMPPException
  {
    int j = 0;
    int i = 0;
    paramFormField = paramFormField.getValues();
    while (paramFormField.hasNext())
    {
      String str = (String)paramFormField.next();
      if ((str.equals("http://jabber.org/protocol/bytestreams")) && (!IBB_ONLY)) {
        j = 1;
      } else if (str.equals("http://jabber.org/protocol/ibb")) {
        i = 1;
      }
    }
    if ((j == 0) && (i == 0))
    {
      paramFormField = new XMPPError(XMPPError.Condition.bad_request, "No acceptable transfer mechanism");
      throw new XMPPException(paramFormField.getMessage(), paramFormField);
    }
    if ((j != 0) && (i != 0)) {
      return new FaultTolerantNegotiator(this.connection, this.byteStreamTransferManager.createNegotiator(), this.inbandTransferManager);
    }
    if (j != 0) {
      return this.byteStreamTransferManager.createNegotiator();
    }
    return this.inbandTransferManager;
  }
  
  private FormField getStreamMethodField(DataForm paramDataForm)
  {
    Iterator localIterator = paramDataForm.getFields();
    for (;;)
    {
      paramDataForm = null;
      if (localIterator.hasNext())
      {
        paramDataForm = (FormField)localIterator.next();
        if (!paramDataForm.getVariable().equals("stream-method")) {}
      }
      else
      {
        return paramDataForm;
      }
    }
  }
  
  public static Collection getSupportedProtocols()
  {
    return Collections.unmodifiableList(Arrays.asList(PROTOCOLS));
  }
  
  public static boolean isServiceEnabled(XMPPConnection paramXMPPConnection)
  {
    String[] arrayOfString = NAMESPACE;
    int j = arrayOfString.length;
    int i = 0;
    while (i < j)
    {
      String str = arrayOfString[i];
      if (!ServiceDiscoveryManager.getInstanceFor(paramXMPPConnection).includesFeature(str)) {
        return false;
      }
      i += 1;
    }
    return true;
  }
  
  public static void setServiceEnabled(XMPPConnection paramXMPPConnection, boolean paramBoolean)
  {
    paramXMPPConnection = ServiceDiscoveryManager.getInstanceFor(paramXMPPConnection);
    String[] arrayOfString = NAMESPACE;
    int j = arrayOfString.length;
    int i = 0;
    if (i < j)
    {
      String str = arrayOfString[i];
      if (paramBoolean) {
        paramXMPPConnection.addFeature(str);
      }
      for (;;)
      {
        i += 1;
        break;
        paramXMPPConnection.removeFeature(str);
      }
    }
  }
  
  public String getNextStreamID()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("jsi_");
    localStringBuilder.append(Math.abs(randomGenerator.nextLong()));
    return localStringBuilder.toString();
  }
  
  public StreamNegotiator negotiateOutgoingTransfer(String paramString1, String paramString2, String paramString3, long paramLong, String paramString4, int paramInt)
    throws XMPPException
  {
    StreamInitiation localStreamInitiation = new StreamInitiation();
    localStreamInitiation.setSesssionID(paramString2);
    localStreamInitiation.setMimeType(URLConnection.guessContentTypeFromName(paramString3));
    paramString2 = new StreamInitiation.File(paramString3, paramLong);
    paramString2.setDesc(paramString4);
    localStreamInitiation.setFile(paramString2);
    localStreamInitiation.setFeatureNegotiationForm(createDefaultInitiationForm());
    localStreamInitiation.setFrom(this.connection.getUser());
    localStreamInitiation.setTo(paramString1);
    localStreamInitiation.setType(IQ.Type.SET);
    paramString1 = this.connection.createPacketCollector(new PacketIDFilter(localStreamInitiation.getPacketID()));
    this.connection.sendPacket(localStreamInitiation);
    paramString2 = paramString1.nextResult(paramInt);
    paramString1.cancel();
    if ((paramString2 instanceof IQ))
    {
      paramString1 = (IQ)paramString2;
      if (paramString1.getType().equals(IQ.Type.RESULT)) {
        return getOutgoingNegotiator(getStreamMethodField(((StreamInitiation)paramString2).getFeatureNegotiationForm()));
      }
      if (paramString1.getType().equals(IQ.Type.ERROR)) {
        throw new XMPPException(paramString1.getError());
      }
      throw new XMPPException("File transfer response unreadable");
    }
    return null;
  }
  
  public void rejectStream(StreamInitiation paramStreamInitiation)
  {
    XMPPError localXMPPError = new XMPPError(XMPPError.Condition.forbidden, "Offer Declined");
    paramStreamInitiation = createIQ(paramStreamInitiation.getPacketID(), paramStreamInitiation.getFrom(), paramStreamInitiation.getTo(), IQ.Type.ERROR);
    paramStreamInitiation.setError(localXMPPError);
    this.connection.sendPacket(paramStreamInitiation);
  }
  
  public StreamNegotiator selectStreamNegotiator(FileTransferRequest paramFileTransferRequest)
    throws XMPPException
  {
    paramFileTransferRequest = paramFileTransferRequest.getStreamInitiation();
    Object localObject = getStreamMethodField(paramFileTransferRequest.getFeatureNegotiationForm());
    if (localObject == null)
    {
      localObject = new XMPPError(XMPPError.Condition.bad_request, "No stream methods contained in packet.");
      paramFileTransferRequest = createIQ(paramFileTransferRequest.getPacketID(), paramFileTransferRequest.getFrom(), paramFileTransferRequest.getTo(), IQ.Type.ERROR);
      paramFileTransferRequest.setError((XMPPError)localObject);
      this.connection.sendPacket(paramFileTransferRequest);
      throw new XMPPException("No stream methods contained in packet.", (XMPPError)localObject);
    }
    try
    {
      localObject = getNegotiator((FormField)localObject);
      return localObject;
    }
    catch (XMPPException localXMPPException)
    {
      paramFileTransferRequest = createIQ(paramFileTransferRequest.getPacketID(), paramFileTransferRequest.getFrom(), paramFileTransferRequest.getTo(), IQ.Type.ERROR);
      paramFileTransferRequest.setError(localXMPPException.getXMPPError());
      this.connection.sendPacket(paramFileTransferRequest);
      throw localXMPPException;
    }
  }
}


/* Location:           C:\Users\Administrator\Documents\ds\反编译\AndroidKiller_v1.3.1\projects\Jungledsq_ppc\ProjectSrc\smali\
 * Qualified Name:     org.jivesoftware.smackx.filetransfer.FileTransferNegotiator
 * JD-Core Version:    0.7.0.1
 */