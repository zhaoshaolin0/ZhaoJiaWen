package org.jivesoftware.smackx;

import java.util.List;
import org.jivesoftware.smack.PacketCollector;
import org.jivesoftware.smack.SmackConfiguration;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.filter.PacketIDFilter;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.packet.IQ.Type;
import org.jivesoftware.smackx.packet.SharedGroupsInfo;

public class SharedGroupManager
{
  public static List getSharedGroups(XMPPConnection paramXMPPConnection)
    throws XMPPException
  {
    SharedGroupsInfo localSharedGroupsInfo = new SharedGroupsInfo();
    localSharedGroupsInfo.setType(IQ.Type.GET);
    PacketCollector localPacketCollector = paramXMPPConnection.createPacketCollector(new PacketIDFilter(localSharedGroupsInfo.getPacketID()));
    paramXMPPConnection.sendPacket(localSharedGroupsInfo);
    paramXMPPConnection = (IQ)localPacketCollector.nextResult(SmackConfiguration.getPacketReplyTimeout());
    localPacketCollector.cancel();
    if (paramXMPPConnection == null) {
      throw new XMPPException("No response from the server.");
    }
    if (paramXMPPConnection.getType() == IQ.Type.ERROR) {
      throw new XMPPException(paramXMPPConnection.getError());
    }
    return ((SharedGroupsInfo)paramXMPPConnection).getGroups();
  }
}


/* Location:           C:\Users\Administrator\Documents\ds\反编译\AndroidKiller_v1.3.1\projects\Jungledsq_ppc\ProjectSrc\smali\
 * Qualified Name:     org.jivesoftware.smackx.SharedGroupManager
 * JD-Core Version:    0.7.0.1
 */