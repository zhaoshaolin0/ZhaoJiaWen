package org.jivesoftware.smackx.filetransfer;

import java.io.InputStream;
import java.io.OutputStream;
import org.jivesoftware.smack.PacketCollector;
import org.jivesoftware.smack.SmackConfiguration;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.packet.IQ.Type;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.packet.XMPPError;
import org.jivesoftware.smackx.FormField;
import org.jivesoftware.smackx.packet.DataForm;
import org.jivesoftware.smackx.packet.StreamInitiation;

public abstract class StreamNegotiator
{
  public abstract void cleanup();
  
  public IQ createError(String paramString1, String paramString2, String paramString3, XMPPError paramXMPPError)
  {
    paramString1 = FileTransferNegotiator.createIQ(paramString3, paramString2, paramString1, IQ.Type.ERROR);
    paramString1.setError(paramXMPPError);
    return paramString1;
  }
  
  public abstract InputStream createIncomingStream(StreamInitiation paramStreamInitiation)
    throws XMPPException;
  
  public StreamInitiation createInitiationAccept(StreamInitiation paramStreamInitiation, String[] paramArrayOfString)
  {
    StreamInitiation localStreamInitiation = new StreamInitiation();
    localStreamInitiation.setTo(paramStreamInitiation.getFrom());
    localStreamInitiation.setFrom(paramStreamInitiation.getTo());
    localStreamInitiation.setType(IQ.Type.RESULT);
    localStreamInitiation.setPacketID(paramStreamInitiation.getPacketID());
    paramStreamInitiation = new DataForm("submit");
    FormField localFormField = new FormField("stream-method");
    int j = paramArrayOfString.length;
    int i = 0;
    while (i < j)
    {
      localFormField.addValue(paramArrayOfString[i]);
      i += 1;
    }
    paramStreamInitiation.addField(localFormField);
    localStreamInitiation.setFeatureNegotiationForm(paramStreamInitiation);
    return localStreamInitiation;
  }
  
  public abstract OutputStream createOutgoingStream(String paramString1, String paramString2, String paramString3)
    throws XMPPException;
  
  public abstract PacketFilter getInitiationPacketFilter(String paramString1, String paramString2);
  
  public abstract String[] getNamespaces();
  
  Packet initiateIncomingStream(XMPPConnection paramXMPPConnection, StreamInitiation paramStreamInitiation)
    throws XMPPException
  {
    StreamInitiation localStreamInitiation = createInitiationAccept(paramStreamInitiation, getNamespaces());
    paramStreamInitiation = paramXMPPConnection.createPacketCollector(getInitiationPacketFilter(paramStreamInitiation.getFrom(), paramStreamInitiation.getSessionID()));
    paramXMPPConnection.sendPacket(localStreamInitiation);
    paramXMPPConnection = paramStreamInitiation.nextResult(SmackConfiguration.getPacketReplyTimeout());
    paramStreamInitiation.cancel();
    if (paramXMPPConnection == null) {
      throw new XMPPException("No response from file transfer initiator");
    }
    return paramXMPPConnection;
  }
  
  abstract InputStream negotiateIncomingStream(Packet paramPacket)
    throws XMPPException;
}


/* Location:           C:\Users\Administrator\Documents\ds\反编译\AndroidKiller_v1.3.1\projects\Jungledsq_ppc\ProjectSrc\smali\
 * Qualified Name:     org.jivesoftware.smackx.filetransfer.StreamNegotiator
 * JD-Core Version:    0.7.0.1
 */