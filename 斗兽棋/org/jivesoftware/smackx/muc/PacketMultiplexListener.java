package org.jivesoftware.smackx.muc;

import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.filter.MessageTypeFilter;
import org.jivesoftware.smack.filter.PacketExtensionFilter;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.filter.PacketTypeFilter;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Message.Type;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.packet.Presence;

class PacketMultiplexListener
  implements PacketListener
{
  private static final PacketFilter DECLINES_FILTER = new PacketExtensionFilter("x", "http://jabber.org/protocol/muc#user");
  private static final PacketFilter MESSAGE_FILTER = new MessageTypeFilter(Message.Type.groupchat);
  private static final PacketFilter PRESENCE_FILTER = new PacketTypeFilter(Presence.class);
  private static final PacketFilter SUBJECT_FILTER = new PacketFilter()
  {
    public boolean accept(Packet paramAnonymousPacket)
    {
      return ((Message)paramAnonymousPacket).getSubject() != null;
    }
  };
  private PacketListener declinesListener;
  private ConnectionDetachedPacketCollector messageCollector;
  private PacketListener presenceListener;
  private PacketListener subjectListener;
  
  public PacketMultiplexListener(ConnectionDetachedPacketCollector paramConnectionDetachedPacketCollector, PacketListener paramPacketListener1, PacketListener paramPacketListener2, PacketListener paramPacketListener3)
  {
    if (paramConnectionDetachedPacketCollector == null) {
      throw new IllegalArgumentException("MessageCollector is null");
    }
    if (paramPacketListener1 == null) {
      throw new IllegalArgumentException("Presence listener is null");
    }
    if (paramPacketListener2 == null) {
      throw new IllegalArgumentException("Subject listener is null");
    }
    if (paramPacketListener3 == null) {
      throw new IllegalArgumentException("Declines listener is null");
    }
    this.messageCollector = paramConnectionDetachedPacketCollector;
    this.presenceListener = paramPacketListener1;
    this.subjectListener = paramPacketListener2;
    this.declinesListener = paramPacketListener3;
  }
  
  public void processPacket(Packet paramPacket)
  {
    if (PRESENCE_FILTER.accept(paramPacket)) {
      this.presenceListener.processPacket(paramPacket);
    }
    do
    {
      do
      {
        return;
        if (!MESSAGE_FILTER.accept(paramPacket)) {
          break;
        }
        this.messageCollector.processPacket(paramPacket);
      } while (!SUBJECT_FILTER.accept(paramPacket));
      this.subjectListener.processPacket(paramPacket);
      return;
    } while (!DECLINES_FILTER.accept(paramPacket));
    this.declinesListener.processPacket(paramPacket);
  }
}


/* Location:           C:\Users\Administrator\Documents\ds\反编译\AndroidKiller_v1.3.1\projects\Jungledsq_ppc\ProjectSrc\smali\
 * Qualified Name:     org.jivesoftware.smackx.muc.PacketMultiplexListener
 * JD-Core Version:    0.7.0.1
 */