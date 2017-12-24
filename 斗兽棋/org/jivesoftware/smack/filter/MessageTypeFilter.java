package org.jivesoftware.smack.filter;

import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Message.Type;
import org.jivesoftware.smack.packet.Packet;

public class MessageTypeFilter
  implements PacketFilter
{
  private final Message.Type type;
  
  public MessageTypeFilter(Message.Type paramType)
  {
    this.type = paramType;
  }
  
  public boolean accept(Packet paramPacket)
  {
    if (!(paramPacket instanceof Message)) {
      return false;
    }
    return ((Message)paramPacket).getType().equals(this.type);
  }
}


/* Location:           C:\Users\Administrator\Documents\ds\反编译\AndroidKiller_v1.3.1\projects\Jungledsq_ppc\ProjectSrc\smali\
 * Qualified Name:     org.jivesoftware.smack.filter.MessageTypeFilter
 * JD-Core Version:    0.7.0.1
 */