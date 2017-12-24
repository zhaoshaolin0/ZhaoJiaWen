package org.jivesoftware.smackx;

import org.jivesoftware.smack.ConnectionCreationListener;
import org.jivesoftware.smack.PacketCollector;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.SmackConfiguration;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.filter.AndFilter;
import org.jivesoftware.smack.filter.IQTypeFilter;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.filter.PacketIDFilter;
import org.jivesoftware.smack.filter.PacketTypeFilter;
import org.jivesoftware.smack.packet.IQ.Type;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smackx.packet.LastActivity;

public class LastActivityManager
{
  private XMPPConnection connection;
  private long lastMessageSent;
  
  static
  {
    XMPPConnection.addConnectionCreationListener(new ConnectionCreationListener()
    {
      public void connectionCreated(XMPPConnection paramAnonymousXMPPConnection)
      {
        new LastActivityManager(paramAnonymousXMPPConnection, null);
      }
    });
  }
  
  private LastActivityManager(XMPPConnection paramXMPPConnection)
  {
    this.connection = paramXMPPConnection;
    paramXMPPConnection.addPacketWriterListener(new PacketListener()
    {
      public void processPacket(Packet paramAnonymousPacket)
      {
        LastActivityManager.this.resetIdleTime();
      }
    }, null);
    paramXMPPConnection.addPacketListener(new PacketListener()new AndFilternew IQTypeFilter
    {
      public void processPacket(Packet paramAnonymousPacket)
      {
        LastActivity localLastActivity = new LastActivity();
        localLastActivity.setType(IQ.Type.RESULT);
        localLastActivity.setTo(paramAnonymousPacket.getFrom());
        localLastActivity.setFrom(paramAnonymousPacket.getTo());
        localLastActivity.setPacketID(paramAnonymousPacket.getPacketID());
        localLastActivity.setLastActivity(LastActivityManager.this.getIdleTime());
        LastActivityManager.this.connection.sendPacket(localLastActivity);
      }
    }, new AndFilter(new PacketFilter[] { new IQTypeFilter(IQ.Type.GET), new PacketTypeFilter(LastActivity.class) }));
  }
  
  private long getIdleTime()
  {
    return (System.currentTimeMillis() - this.lastMessageSent) / 1000L;
  }
  
  public static LastActivity getLastActivity(XMPPConnection paramXMPPConnection, String paramString)
    throws XMPPException
  {
    LastActivity localLastActivity = new LastActivity();
    localLastActivity.setTo(paramString);
    paramString = paramXMPPConnection.createPacketCollector(new PacketIDFilter(localLastActivity.getPacketID()));
    paramXMPPConnection.sendPacket(localLastActivity);
    paramXMPPConnection = (LastActivity)paramString.nextResult(SmackConfiguration.getPacketReplyTimeout());
    paramString.cancel();
    if (paramXMPPConnection == null) {
      throw new XMPPException("No response from server on status set.");
    }
    if (paramXMPPConnection.getError() != null) {
      throw new XMPPException(paramXMPPConnection.getError());
    }
    return paramXMPPConnection;
  }
  
  private void resetIdleTime()
  {
    this.lastMessageSent = System.currentTimeMillis();
  }
}


/* Location:           C:\Users\Administrator\Documents\ds\反编译\AndroidKiller_v1.3.1\projects\Jungledsq_ppc\ProjectSrc\smali\
 * Qualified Name:     org.jivesoftware.smackx.LastActivityManager
 * JD-Core Version:    0.7.0.1
 */