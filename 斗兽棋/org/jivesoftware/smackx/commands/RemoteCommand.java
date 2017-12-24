package org.jivesoftware.smackx.commands;

import org.jivesoftware.smack.PacketCollector;
import org.jivesoftware.smack.SmackConfiguration;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.filter.PacketIDFilter;
import org.jivesoftware.smack.packet.IQ.Type;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smackx.Form;
import org.jivesoftware.smackx.packet.AdHocCommandData;

public class RemoteCommand
  extends AdHocCommand
{
  private XMPPConnection connection;
  private String jid;
  private String sessionID;
  
  protected RemoteCommand(XMPPConnection paramXMPPConnection, String paramString1, String paramString2)
  {
    this.connection = paramXMPPConnection;
    this.jid = paramString2;
    setNode(paramString1);
  }
  
  private void executeAction(AdHocCommand.Action paramAction)
    throws XMPPException
  {
    executeAction(paramAction, null);
  }
  
  private void executeAction(AdHocCommand.Action paramAction, Form paramForm)
    throws XMPPException
  {
    AdHocCommandData localAdHocCommandData = new AdHocCommandData();
    localAdHocCommandData.setType(IQ.Type.SET);
    localAdHocCommandData.setTo(getOwnerJID());
    localAdHocCommandData.setNode(getNode());
    localAdHocCommandData.setSessionID(this.sessionID);
    localAdHocCommandData.setAction(paramAction);
    if (paramForm != null) {
      localAdHocCommandData.setForm(paramForm.getDataFormToSend());
    }
    paramAction = this.connection.createPacketCollector(new PacketIDFilter(localAdHocCommandData.getPacketID()));
    this.connection.sendPacket(localAdHocCommandData);
    paramForm = paramAction.nextResult(SmackConfiguration.getPacketReplyTimeout());
    paramAction.cancel();
    if (paramForm == null) {
      throw new XMPPException("No response from server on status set.");
    }
    if (paramForm.getError() != null) {
      throw new XMPPException(paramForm.getError());
    }
    paramAction = (AdHocCommandData)paramForm;
    this.sessionID = paramAction.getSessionID();
    super.setData(paramAction);
  }
  
  public void cancel()
    throws XMPPException
  {
    executeAction(AdHocCommand.Action.cancel);
  }
  
  public void complete(Form paramForm)
    throws XMPPException
  {
    executeAction(AdHocCommand.Action.complete, paramForm);
  }
  
  public void execute()
    throws XMPPException
  {
    executeAction(AdHocCommand.Action.execute);
  }
  
  public void execute(Form paramForm)
    throws XMPPException
  {
    executeAction(AdHocCommand.Action.execute, paramForm);
  }
  
  public String getOwnerJID()
  {
    return this.jid;
  }
  
  public void next(Form paramForm)
    throws XMPPException
  {
    executeAction(AdHocCommand.Action.next, paramForm);
  }
  
  public void prev()
    throws XMPPException
  {
    executeAction(AdHocCommand.Action.prev);
  }
}


/* Location:           C:\Users\Administrator\Documents\ds\反编译\AndroidKiller_v1.3.1\projects\Jungledsq_ppc\ProjectSrc\smali\
 * Qualified Name:     org.jivesoftware.smackx.commands.RemoteCommand
 * JD-Core Version:    0.7.0.1
 */