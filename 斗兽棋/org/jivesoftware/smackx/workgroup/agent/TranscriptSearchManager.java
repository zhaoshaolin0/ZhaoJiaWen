package org.jivesoftware.smackx.workgroup.agent;

import org.jivesoftware.smack.PacketCollector;
import org.jivesoftware.smack.SmackConfiguration;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.filter.PacketIDFilter;
import org.jivesoftware.smack.packet.IQ.Type;
import org.jivesoftware.smackx.Form;
import org.jivesoftware.smackx.ReportedData;
import org.jivesoftware.smackx.workgroup.packet.TranscriptSearch;

public class TranscriptSearchManager
{
  private XMPPConnection connection;
  
  public TranscriptSearchManager(XMPPConnection paramXMPPConnection)
  {
    this.connection = paramXMPPConnection;
  }
  
  public Form getSearchForm(String paramString)
    throws XMPPException
  {
    TranscriptSearch localTranscriptSearch = new TranscriptSearch();
    localTranscriptSearch.setType(IQ.Type.GET);
    localTranscriptSearch.setTo(paramString);
    paramString = this.connection.createPacketCollector(new PacketIDFilter(localTranscriptSearch.getPacketID()));
    this.connection.sendPacket(localTranscriptSearch);
    localTranscriptSearch = (TranscriptSearch)paramString.nextResult(SmackConfiguration.getPacketReplyTimeout());
    paramString.cancel();
    if (localTranscriptSearch == null) {
      throw new XMPPException("No response from server on status set.");
    }
    if (localTranscriptSearch.getError() != null) {
      throw new XMPPException(localTranscriptSearch.getError());
    }
    return Form.getFormFrom(localTranscriptSearch);
  }
  
  public ReportedData submitSearch(String paramString, Form paramForm)
    throws XMPPException
  {
    TranscriptSearch localTranscriptSearch = new TranscriptSearch();
    localTranscriptSearch.setType(IQ.Type.GET);
    localTranscriptSearch.setTo(paramString);
    localTranscriptSearch.addExtension(paramForm.getDataFormToSend());
    paramString = this.connection.createPacketCollector(new PacketIDFilter(localTranscriptSearch.getPacketID()));
    this.connection.sendPacket(localTranscriptSearch);
    paramForm = (TranscriptSearch)paramString.nextResult(SmackConfiguration.getPacketReplyTimeout());
    paramString.cancel();
    if (paramForm == null) {
      throw new XMPPException("No response from server on status set.");
    }
    if (paramForm.getError() != null) {
      throw new XMPPException(paramForm.getError());
    }
    return ReportedData.getReportedDataFrom(paramForm);
  }
}


/* Location:           C:\Users\Administrator\Documents\ds\反编译\AndroidKiller_v1.3.1\projects\Jungledsq_ppc\ProjectSrc\smali\
 * Qualified Name:     org.jivesoftware.smackx.workgroup.agent.TranscriptSearchManager
 * JD-Core Version:    0.7.0.1
 */