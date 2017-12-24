package org.jivesoftware.smack.filter;

import org.jivesoftware.smack.packet.Packet;

public class NotFilter
  implements PacketFilter
{
  private PacketFilter filter;
  
  public NotFilter(PacketFilter paramPacketFilter)
  {
    if (paramPacketFilter == null) {
      throw new IllegalArgumentException("Parameter cannot be null.");
    }
    this.filter = paramPacketFilter;
  }
  
  public boolean accept(Packet paramPacket)
  {
    return !this.filter.accept(paramPacket);
  }
}


/* Location:           C:\Users\Administrator\Documents\ds\反编译\AndroidKiller_v1.3.1\projects\Jungledsq_ppc\ProjectSrc\smali\
 * Qualified Name:     org.jivesoftware.smack.filter.NotFilter
 * JD-Core Version:    0.7.0.1
 */