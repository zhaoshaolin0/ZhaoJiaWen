package org.jivesoftware.smack.filter;

import org.jivesoftware.smack.packet.Packet;

public class PacketTypeFilter
  implements PacketFilter
{
  Class packetType;
  
  public PacketTypeFilter(Class paramClass)
  {
    if (!Packet.class.isAssignableFrom(paramClass)) {
      throw new IllegalArgumentException("Packet type must be a sub-class of Packet.");
    }
    this.packetType = paramClass;
  }
  
  public boolean accept(Packet paramPacket)
  {
    return this.packetType.isInstance(paramPacket);
  }
  
  public String toString()
  {
    return "PacketTypeFilter: " + this.packetType.getName();
  }
}


/* Location:           C:\Users\Administrator\Documents\ds\反编译\AndroidKiller_v1.3.1\projects\Jungledsq_ppc\ProjectSrc\smali\
 * Qualified Name:     org.jivesoftware.smack.filter.PacketTypeFilter
 * JD-Core Version:    0.7.0.1
 */