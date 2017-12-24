package org.jivesoftware.smackx.workgroup.agent;

import java.util.Collection;
import org.jivesoftware.smack.PacketCollector;
import org.jivesoftware.smack.SmackConfiguration;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.filter.PacketIDFilter;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.packet.IQ.Type;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smackx.workgroup.packet.AgentInfo;
import org.jivesoftware.smackx.workgroup.packet.AgentWorkgroups;

public class Agent
{
  private XMPPConnection connection;
  private String workgroupJID;
  
  Agent(XMPPConnection paramXMPPConnection, String paramString)
  {
    this.connection = paramXMPPConnection;
    this.workgroupJID = paramString;
  }
  
  public static Collection getWorkgroups(String paramString1, String paramString2, XMPPConnection paramXMPPConnection)
    throws XMPPException
  {
    paramString2 = new AgentWorkgroups(paramString2);
    paramString2.setTo(paramString1);
    paramString1 = paramXMPPConnection.createPacketCollector(new PacketIDFilter(paramString2.getPacketID()));
    paramXMPPConnection.sendPacket(paramString2);
    paramString2 = (AgentWorkgroups)paramString1.nextResult(SmackConfiguration.getPacketReplyTimeout());
    paramString1.cancel();
    if (paramString2 == null) {
      throw new XMPPException("No response from server on status set.");
    }
    if (paramString2.getError() != null) {
      throw new XMPPException(paramString2.getError());
    }
    return paramString2.getWorkgroups();
  }
  
  public String getName()
    throws XMPPException
  {
    AgentInfo localAgentInfo = new AgentInfo();
    localAgentInfo.setType(IQ.Type.GET);
    localAgentInfo.setTo(this.workgroupJID);
    localAgentInfo.setFrom(getUser());
    PacketCollector localPacketCollector = this.connection.createPacketCollector(new PacketIDFilter(localAgentInfo.getPacketID()));
    this.connection.sendPacket(localAgentInfo);
    localAgentInfo = (AgentInfo)localPacketCollector.nextResult(SmackConfiguration.getPacketReplyTimeout());
    localPacketCollector.cancel();
    if (localAgentInfo == null) {
      throw new XMPPException("No response from server on status set.");
    }
    if (localAgentInfo.getError() != null) {
      throw new XMPPException(localAgentInfo.getError());
    }
    return localAgentInfo.getName();
  }
  
  public String getUser()
  {
    return this.connection.getUser();
  }
  
  public void setName(String paramString)
    throws XMPPException
  {
    Object localObject = new AgentInfo();
    ((AgentInfo)localObject).setType(IQ.Type.SET);
    ((AgentInfo)localObject).setTo(this.workgroupJID);
    ((AgentInfo)localObject).setFrom(getUser());
    ((AgentInfo)localObject).setName(paramString);
    paramString = this.connection.createPacketCollector(new PacketIDFilter(((AgentInfo)localObject).getPacketID()));
    this.connection.sendPacket((Packet)localObject);
    localObject = (IQ)paramString.nextResult(SmackConfiguration.getPacketReplyTimeout());
    paramString.cancel();
    if (localObject == null) {
      throw new XMPPException("No response from server on status set.");
    }
    if (((IQ)localObject).getError() != null) {
      throw new XMPPException(((IQ)localObject).getError());
    }
  }
}


/* Location:           C:\Users\Administrator\Documents\ds\反编译\AndroidKiller_v1.3.1\projects\Jungledsq_ppc\ProjectSrc\smali\
 * Qualified Name:     org.jivesoftware.smackx.workgroup.agent.Agent
 * JD-Core Version:    0.7.0.1
 */