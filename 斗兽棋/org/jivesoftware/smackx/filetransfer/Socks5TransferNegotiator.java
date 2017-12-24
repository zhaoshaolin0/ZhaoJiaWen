package org.jivesoftware.smackx.filetransfer;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Collection;
import java.util.Iterator;
import org.jivesoftware.smack.PacketCollector;
import org.jivesoftware.smack.SmackConfiguration;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.filter.AndFilter;
import org.jivesoftware.smack.filter.FromMatchesFilter;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.filter.PacketIDFilter;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.packet.IQ.Type;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.packet.XMPPError;
import org.jivesoftware.smack.packet.XMPPError.Condition;
import org.jivesoftware.smack.util.StringUtils;
import org.jivesoftware.smackx.packet.Bytestream;
import org.jivesoftware.smackx.packet.Bytestream.Mode;
import org.jivesoftware.smackx.packet.Bytestream.StreamHost;
import org.jivesoftware.smackx.packet.Bytestream.StreamHostUsed;
import org.jivesoftware.smackx.packet.StreamInitiation;

public class Socks5TransferNegotiator
  extends StreamNegotiator
{
  private static final int CONNECT_FAILURE_THRESHOLD = 2;
  protected static final String NAMESPACE = "http://jabber.org/protocol/bytestreams";
  public static boolean isAllowLocalProxyHost = true;
  private final XMPPConnection connection;
  private Socks5TransferNegotiatorManager transferNegotiatorManager;
  
  public Socks5TransferNegotiator(Socks5TransferNegotiatorManager paramSocks5TransferNegotiatorManager, XMPPConnection paramXMPPConnection)
  {
    this.connection = paramXMPPConnection;
    this.transferNegotiatorManager = paramSocks5TransferNegotiatorManager;
  }
  
  private void cleanupListeningSocket()
  {
    this.transferNegotiatorManager.removeTransfer();
  }
  
  private static Bytestream createByteStreamActivate(String paramString1, String paramString2, String paramString3, String paramString4)
  {
    paramString1 = new Bytestream(paramString1);
    paramString1.setMode(null);
    paramString1.setToActivate(paramString4);
    paramString1.setFrom(paramString2);
    paramString1.setTo(paramString3);
    paramString1.setType(IQ.Type.SET);
    return paramString1;
  }
  
  private Bytestream createByteStreamInit(String paramString1, String paramString2, String paramString3, String paramString4, int paramInt)
  {
    Bytestream localBytestream = new Bytestream();
    localBytestream.setTo(paramString2);
    localBytestream.setFrom(paramString1);
    localBytestream.setSessionID(paramString3);
    localBytestream.setType(IQ.Type.SET);
    localBytestream.setMode(Bytestream.Mode.tcp);
    if ((paramString4 != null) && (paramInt > 0)) {
      localBytestream.addStreamHost(paramString1, paramString4, paramInt);
    }
    paramString1 = this.transferNegotiatorManager.getStreamHosts();
    if (paramString1 != null)
    {
      paramString1 = paramString1.iterator();
      while (paramString1.hasNext()) {
        localBytestream.addStreamHost((Bytestream.StreamHost)paramString1.next());
      }
    }
    return localBytestream;
  }
  
  private String createDigest(String paramString1, String paramString2, String paramString3)
  {
    return StringUtils.hash(paramString1 + StringUtils.parseName(paramString2) + "@" + StringUtils.parseServer(paramString2) + "/" + StringUtils.parseResource(paramString2) + StringUtils.parseName(paramString3) + "@" + StringUtils.parseServer(paramString3) + "/" + StringUtils.parseResource(paramString3));
  }
  
  static String createIncomingSocks5Message(InputStream paramInputStream)
    throws IOException
  {
    Object localObject = new byte[5];
    paramInputStream.read((byte[])localObject, 0, 5);
    localObject = new byte[localObject[4]];
    paramInputStream.read((byte[])localObject, 0, localObject.length);
    localObject = new String((byte[])localObject);
    paramInputStream.read();
    paramInputStream.read();
    return localObject;
  }
  
  static byte[] createOutgoingSocks5Message(int paramInt, String paramString)
  {
    paramString = paramString.getBytes();
    byte[] arrayOfByte = new byte[paramString.length + 7];
    arrayOfByte[0] = 5;
    arrayOfByte[1] = ((byte)paramInt);
    arrayOfByte[2] = 0;
    arrayOfByte[3] = 3;
    arrayOfByte[4] = ((byte)paramString.length);
    System.arraycopy(paramString, 0, arrayOfByte, 5, paramString.length);
    arrayOfByte[(arrayOfByte.length - 2)] = 0;
    arrayOfByte[(arrayOfByte.length - 1)] = 0;
    return arrayOfByte;
  }
  
  private Bytestream createUsedHostConfirmation(Bytestream.StreamHost paramStreamHost, String paramString1, String paramString2, String paramString3)
  {
    Bytestream localBytestream = new Bytestream();
    localBytestream.setTo(paramString1);
    localBytestream.setFrom(paramString2);
    localBytestream.setType(IQ.Type.RESULT);
    localBytestream.setPacketID(paramString3);
    localBytestream.setUsedHost(paramStreamHost.getJID());
    return localBytestream;
  }
  
  private String discoverLocalIP()
    throws UnknownHostException
  {
    return InetAddress.getLocalHost().getHostAddress();
  }
  
  private Socks5TransferNegotiatorManager.ProxyProcess establishListeningSocket()
    throws IOException
  {
    return this.transferNegotiatorManager.addTransfer();
  }
  
  private void establishSOCKS5ConnectionToProxy(Socket paramSocket, String paramString)
    throws IOException
  {
    DataOutputStream localDataOutputStream = new DataOutputStream(paramSocket.getOutputStream());
    localDataOutputStream.write(new byte[] { 5, 1, 0 });
    paramSocket = new DataInputStream(paramSocket.getInputStream());
    paramSocket.read(new byte[2]);
    localDataOutputStream.write(createOutgoingSocks5Message(1, paramString));
    createIncomingSocks5Message(paramSocket);
  }
  
  private int getConnectionFailures(String paramString)
  {
    return this.transferNegotiatorManager.getConnectionFailures(paramString);
  }
  
  private void incrementConnectionFailures(String paramString)
  {
    this.transferNegotiatorManager.incrementConnectionFailures(paramString);
  }
  
  private Socket initBytestreamSocket(String paramString1, String paramString2, String paramString3)
    throws Exception
  {
    try
    {
      localProxyProcess = establishListeningSocket();
    }
    catch (IOException localIOException)
    {
      try
      {
        for (;;)
        {
          Socks5TransferNegotiatorManager.ProxyProcess localProxyProcess;
          Object localObject2 = discoverLocalIP();
          if (localProxyProcess == null) {
            break;
          }
          i = localProxyProcess.getPort();
          localObject2 = createByteStreamInit(paramString2, paramString3, paramString1, (String)localObject2, i);
          paramString1 = waitForUsedHostResponse(paramString1, localProxyProcess, createDigest(paramString1, paramString2, paramString3), (Bytestream)localObject2).establishedSocket;
          return paramString1;
          localIOException = localIOException;
          Object localObject1 = null;
        }
      }
      catch (UnknownHostException localUnknownHostException)
      {
        for (;;)
        {
          Object localObject3 = null;
          continue;
          int i = 0;
        }
      }
      finally
      {
        cleanupListeningSocket();
      }
    }
  }
  
  private SelectedHostInfo selectHost(Bytestream paramBytestream)
    throws XMPPException
  {
    Iterator localIterator = paramBytestream.getStreamHosts().iterator();
    Object localObject = null;
    Socket localSocket;
    for (;;)
    {
      localSocket = null;
      Bytestream.StreamHost localStreamHost2;
      String str;
      if (localIterator.hasNext())
      {
        localStreamHost2 = (Bytestream.StreamHost)localIterator.next();
        str = localStreamHost2.getAddress();
        localObject = localStreamHost2;
        if (getConnectionFailures(str) >= 2) {}
      }
      else
      {
        try
        {
          localSocket = new Socket(str, localStreamHost2.getPort());
          localIOException1.printStackTrace();
        }
        catch (IOException localIOException1)
        {
          try
          {
            establishSOCKS5ConnectionToProxy(localSocket, createDigest(paramBytestream.getSessionID(), paramBytestream.getFrom(), paramBytestream.getTo()));
            localObject = localStreamHost2;
            if ((localObject != null) && (localSocket != null) && (localSocket.isConnected())) {
              break;
            }
            throw new XMPPException("Could not establish socket with any provided host", new XMPPError(XMPPError.Condition.no_acceptable, "Could not establish socket with any provided host"));
          }
          catch (IOException localIOException2)
          {
            Bytestream.StreamHost localStreamHost1;
            break label136;
          }
          localIOException1 = localIOException1;
        }
        label136:
        incrementConnectionFailures(str);
        localStreamHost1 = null;
      }
    }
    return new SelectedHostInfo(localStreamHost1, localSocket);
  }
  
  private SelectedHostInfo waitForUsedHostResponse(String paramString1, Socks5TransferNegotiatorManager.ProxyProcess paramProxyProcess, String paramString2, Bytestream paramBytestream)
    throws XMPPException, IOException
  {
    SelectedHostInfo localSelectedHostInfo = new SelectedHostInfo();
    Object localObject1 = this.connection.createPacketCollector(new PacketIDFilter(paramBytestream.getPacketID()));
    this.connection.sendPacket(paramBytestream);
    Object localObject2 = ((PacketCollector)localObject1).nextResult(10000L);
    ((PacketCollector)localObject1).cancel();
    if ((localObject2 != null) && ((localObject2 instanceof Bytestream)))
    {
      localObject1 = (Bytestream)localObject2;
      if (((Bytestream)localObject1).getType().equals(IQ.Type.ERROR)) {
        throw new XMPPException("Remote client returned error, stream hosts expected", ((Bytestream)localObject1).getError());
      }
    }
    else
    {
      throw new XMPPException("Unexpected response from remote user");
    }
    localObject2 = ((Bytestream)localObject1).getUsedHost();
    Bytestream.StreamHost localStreamHost = paramBytestream.getStreamHost(((Bytestream.StreamHostUsed)localObject2).getJID());
    if (localStreamHost == null) {
      throw new XMPPException("Remote user responded with unknown host");
    }
    if (((Bytestream.StreamHostUsed)localObject2).getJID().equals(paramBytestream.getFrom()))
    {
      localSelectedHostInfo.establishedSocket = paramProxyProcess.getSocket(paramString2);
      localSelectedHostInfo.selectedHost = localStreamHost;
      return localSelectedHostInfo;
    }
    localSelectedHostInfo.establishedSocket = new Socket(localStreamHost.getAddress(), localStreamHost.getPort());
    establishSOCKS5ConnectionToProxy(localSelectedHostInfo.establishedSocket, paramString2);
    paramProxyProcess = createByteStreamActivate(paramString1, ((Bytestream)localObject1).getTo(), localStreamHost.getJID(), ((Bytestream)localObject1).getFrom());
    paramString1 = this.connection.createPacketCollector(new PacketIDFilter(paramProxyProcess.getPacketID()));
    this.connection.sendPacket(paramProxyProcess);
    paramProxyProcess = (IQ)paramString1.nextResult(SmackConfiguration.getPacketReplyTimeout());
    paramString1.cancel();
    if (!paramProxyProcess.getType().equals(IQ.Type.RESULT))
    {
      localSelectedHostInfo.establishedSocket.close();
      return null;
    }
    return localSelectedHostInfo;
  }
  
  public void cleanup() {}
  
  public InputStream createIncomingStream(StreamInitiation paramStreamInitiation)
    throws XMPPException
  {
    return negotiateIncomingStream(initiateIncomingStream(this.connection, paramStreamInitiation));
  }
  
  /* Error */
  public OutputStream createOutgoingStream(String paramString1, String paramString2, String paramString3)
    throws XMPPException
  {
    // Byte code:
    //   0: aload_0
    //   1: aload_1
    //   2: aload_2
    //   3: aload_3
    //   4: invokespecial 425	org/jivesoftware/smackx/filetransfer/Socks5TransferNegotiator:initBytestreamSocket	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Ljava/net/Socket;
    //   7: astore_1
    //   8: aload_1
    //   9: ifnull +43 -> 52
    //   12: new 427	java/io/BufferedOutputStream
    //   15: dup
    //   16: aload_1
    //   17: invokevirtual 217	java/net/Socket:getOutputStream	()Ljava/io/OutputStream;
    //   20: invokespecial 428	java/io/BufferedOutputStream:<init>	(Ljava/io/OutputStream;)V
    //   23: astore_1
    //   24: aload_1
    //   25: areturn
    //   26: astore_1
    //   27: new 279	org/jivesoftware/smack/XMPPException
    //   30: dup
    //   31: ldc_w 430
    //   34: aload_1
    //   35: invokespecial 433	org/jivesoftware/smack/XMPPException:<init>	(Ljava/lang/String;Ljava/lang/Throwable;)V
    //   38: athrow
    //   39: astore_1
    //   40: new 279	org/jivesoftware/smack/XMPPException
    //   43: dup
    //   44: ldc_w 435
    //   47: aload_1
    //   48: invokespecial 433	org/jivesoftware/smack/XMPPException:<init>	(Ljava/lang/String;Ljava/lang/Throwable;)V
    //   51: athrow
    //   52: aconst_null
    //   53: areturn
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	54	0	this	Socks5TransferNegotiator
    //   0	54	1	paramString1	String
    //   0	54	2	paramString2	String
    //   0	54	3	paramString3	String
    // Exception table:
    //   from	to	target	type
    //   0	8	26	java/lang/Exception
    //   12	24	39	java/io/IOException
  }
  
  public PacketFilter getInitiationPacketFilter(String paramString1, String paramString2)
  {
    return new AndFilter(new PacketFilter[] { new FromMatchesFilter(paramString1), new BytestreamSIDFilter(paramString2) });
  }
  
  public String[] getNamespaces()
  {
    return new String[] { "http://jabber.org/protocol/bytestreams" };
  }
  
  /* Error */
  InputStream negotiateIncomingStream(Packet paramPacket)
    throws XMPPException
  {
    // Byte code:
    //   0: aload_1
    //   1: checkcast 46	org/jivesoftware/smackx/packet/Bytestream
    //   4: astore_1
    //   5: aload_1
    //   6: invokevirtual 357	org/jivesoftware/smackx/packet/Bytestream:getType	()Lorg/jivesoftware/smack/packet/IQ$Type;
    //   9: getstatic 360	org/jivesoftware/smack/packet/IQ$Type:ERROR	Lorg/jivesoftware/smack/packet/IQ$Type;
    //   12: invokevirtual 366	java/lang/Object:equals	(Ljava/lang/Object;)Z
    //   15: ifeq +15 -> 30
    //   18: new 279	org/jivesoftware/smack/XMPPException
    //   21: dup
    //   22: aload_1
    //   23: invokevirtual 372	org/jivesoftware/smackx/packet/Bytestream:getError	()Lorg/jivesoftware/smack/packet/XMPPError;
    //   26: invokespecial 453	org/jivesoftware/smack/XMPPException:<init>	(Lorg/jivesoftware/smack/packet/XMPPError;)V
    //   29: athrow
    //   30: aload_0
    //   31: aload_1
    //   32: invokespecial 455	org/jivesoftware/smackx/filetransfer/Socks5TransferNegotiator:selectHost	(Lorg/jivesoftware/smackx/packet/Bytestream;)Lorg/jivesoftware/smackx/filetransfer/Socks5TransferNegotiator$SelectedHostInfo;
    //   35: astore_2
    //   36: aload_0
    //   37: aload_2
    //   38: getfield 397	org/jivesoftware/smackx/filetransfer/Socks5TransferNegotiator$SelectedHostInfo:selectedHost	Lorg/jivesoftware/smackx/packet/Bytestream$StreamHost;
    //   41: aload_1
    //   42: invokevirtual 294	org/jivesoftware/smackx/packet/Bytestream:getFrom	()Ljava/lang/String;
    //   45: aload_1
    //   46: invokevirtual 297	org/jivesoftware/smackx/packet/Bytestream:getTo	()Ljava/lang/String;
    //   49: aload_1
    //   50: invokevirtual 331	org/jivesoftware/smackx/packet/Bytestream:getPacketID	()Ljava/lang/String;
    //   53: invokespecial 457	org/jivesoftware/smackx/filetransfer/Socks5TransferNegotiator:createUsedHostConfirmation	(Lorg/jivesoftware/smackx/packet/Bytestream$StreamHost;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Lorg/jivesoftware/smackx/packet/Bytestream;
    //   56: astore_1
    //   57: aload_0
    //   58: getfield 34	org/jivesoftware/smackx/filetransfer/Socks5TransferNegotiator:connection	Lorg/jivesoftware/smack/XMPPConnection;
    //   61: aload_1
    //   62: invokevirtual 342	org/jivesoftware/smack/XMPPConnection:sendPacket	(Lorg/jivesoftware/smack/packet/Packet;)V
    //   65: new 459	java/io/PushbackInputStream
    //   68: dup
    //   69: aload_2
    //   70: getfield 273	org/jivesoftware/smackx/filetransfer/Socks5TransferNegotiator$SelectedHostInfo:establishedSocket	Ljava/net/Socket;
    //   73: invokevirtual 231	java/net/Socket:getInputStream	()Ljava/io/InputStream;
    //   76: invokespecial 460	java/io/PushbackInputStream:<init>	(Ljava/io/InputStream;)V
    //   79: astore_1
    //   80: aload_1
    //   81: aload_1
    //   82: invokevirtual 461	java/io/PushbackInputStream:read	()I
    //   85: invokevirtual 465	java/io/PushbackInputStream:unread	(I)V
    //   88: aload_1
    //   89: areturn
    //   90: astore_2
    //   91: aload_2
    //   92: invokevirtual 468	org/jivesoftware/smack/XMPPException:getXMPPError	()Lorg/jivesoftware/smack/packet/XMPPError;
    //   95: ifnull +32 -> 127
    //   98: aload_0
    //   99: aload_1
    //   100: invokevirtual 297	org/jivesoftware/smackx/packet/Bytestream:getTo	()Ljava/lang/String;
    //   103: aload_1
    //   104: invokevirtual 294	org/jivesoftware/smackx/packet/Bytestream:getFrom	()Ljava/lang/String;
    //   107: aload_1
    //   108: invokevirtual 331	org/jivesoftware/smackx/packet/Bytestream:getPacketID	()Ljava/lang/String;
    //   111: aload_2
    //   112: invokevirtual 468	org/jivesoftware/smack/XMPPException:getXMPPError	()Lorg/jivesoftware/smack/packet/XMPPError;
    //   115: invokespecial 472	org/jivesoftware/smackx/filetransfer/StreamNegotiator:createError	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Lorg/jivesoftware/smack/packet/XMPPError;)Lorg/jivesoftware/smack/packet/IQ;
    //   118: astore_1
    //   119: aload_0
    //   120: getfield 34	org/jivesoftware/smackx/filetransfer/Socks5TransferNegotiator:connection	Lorg/jivesoftware/smack/XMPPConnection;
    //   123: aload_1
    //   124: invokevirtual 342	org/jivesoftware/smack/XMPPConnection:sendPacket	(Lorg/jivesoftware/smack/packet/Packet;)V
    //   127: aload_2
    //   128: athrow
    //   129: astore_1
    //   130: new 279	org/jivesoftware/smack/XMPPException
    //   133: dup
    //   134: ldc_w 474
    //   137: aload_1
    //   138: invokespecial 433	org/jivesoftware/smack/XMPPException:<init>	(Ljava/lang/String;Ljava/lang/Throwable;)V
    //   141: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	142	0	this	Socks5TransferNegotiator
    //   0	142	1	paramPacket	Packet
    //   35	35	2	localSelectedHostInfo	SelectedHostInfo
    //   90	38	2	localXMPPException	XMPPException
    // Exception table:
    //   from	to	target	type
    //   30	36	90	org/jivesoftware/smack/XMPPException
    //   65	88	129	java/io/IOException
  }
  
  private static class BytestreamSIDFilter
    implements PacketFilter
  {
    private String sessionID;
    
    public BytestreamSIDFilter(String paramString)
    {
      if (paramString == null) {
        throw new IllegalArgumentException("StreamID cannot be null");
      }
      this.sessionID = paramString;
    }
    
    public boolean accept(Packet paramPacket)
    {
      if (!Bytestream.class.isInstance(paramPacket)) {
        return false;
      }
      paramPacket = ((Bytestream)paramPacket).getSessionID();
      return (paramPacket != null) && (paramPacket.equals(this.sessionID));
    }
  }
  
  private static class SelectedHostInfo
  {
    protected Socket establishedSocket;
    protected XMPPException exception;
    protected Bytestream.StreamHost selectedHost;
    
    public SelectedHostInfo() {}
    
    SelectedHostInfo(Bytestream.StreamHost paramStreamHost, Socket paramSocket)
    {
      this.selectedHost = paramStreamHost;
      this.establishedSocket = paramSocket;
    }
  }
}


/* Location:           C:\Users\Administrator\Documents\ds\反编译\AndroidKiller_v1.3.1\projects\Jungledsq_ppc\ProjectSrc\smali\
 * Qualified Name:     org.jivesoftware.smackx.filetransfer.Socks5TransferNegotiator
 * JD-Core Version:    0.7.0.1
 */