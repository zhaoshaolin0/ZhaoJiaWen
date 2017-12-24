package org.jivesoftware.smackx.workgroup.agent;

import org.jivesoftware.smack.PacketCollector;
import org.jivesoftware.smack.SmackConfiguration;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.filter.PacketIDFilter;
import org.jivesoftware.smackx.workgroup.packet.Transcript;
import org.jivesoftware.smackx.workgroup.packet.Transcripts;

public class TranscriptManager
{
  private XMPPConnection connection;
  
  public TranscriptManager(XMPPConnection paramXMPPConnection)
  {
    this.connection = paramXMPPConnection;
  }
  
  public Transcript getTranscript(String paramString1, String paramString2)
    throws XMPPException
  {
    paramString2 = new Transcript(paramString2);
    paramString2.setTo(paramString1);
    paramString1 = this.connection.createPacketCollector(new PacketIDFilter(paramString2.getPacketID()));
    this.connection.sendPacket(paramString2);
    paramString2 = (Transcript)paramString1.nextResult(SmackConfiguration.getPacketReplyTimeout());
    paramString1.cancel();
    if (paramString2 == null) {
      throw new XMPPException("No response from server on status set.");
    }
    if (paramString2.getError() != null) {
      throw new XMPPException(paramString2.getError());
    }
    return paramString2;
  }
  
  public Transcripts getTranscripts(String paramString1, String paramString2)
    throws XMPPException
  {
    paramString2 = new Transcripts(paramString2);
    paramString2.setTo(paramString1);
    paramString1 = this.connection.createPacketCollector(new PacketIDFilter(paramString2.getPacketID()));
    this.connection.sendPacket(paramString2);
    paramString2 = (Transcripts)paramString1.nextResult(SmackConfiguration.getPacketReplyTimeout());
    paramString1.cancel();
    if (paramString2 == null) {
      throw new XMPPException("No response from server on status set.");
    }
    if (paramString2.getError() != null) {
      throw new XMPPException(paramString2.getError());
    }
    return paramString2;
  }
}


/* Location:           C:\Users\Administrator\Documents\ds\反编译\AndroidKiller_v1.3.1\projects\Jungledsq_ppc\ProjectSrc\smali\
 * Qualified Name:     org.jivesoftware.smackx.workgroup.agent.TranscriptManager
 * JD-Core Version:    0.7.0.1
 */