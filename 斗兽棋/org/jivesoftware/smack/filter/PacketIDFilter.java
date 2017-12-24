package org.jivesoftware.smack.filter;

import org.jivesoftware.smack.packet.Packet;

public class PacketIDFilter
  implements PacketFilter
{
  private String packetID;
  
  public PacketIDFilter(String paramString)
  {
    if (paramString == null) {
      throw new IllegalArgumentException("Packet ID cannot be null.");
    }
    this.packetID = paramString;
  }
  
  public boolean accept(Packet paramPacket)
  {
    return this.packetID.equals(paramPacket.getPacketID());
  }
  
  public String toString()
  {
    return "PacketIDFilter by id: " + this.packetID;
  }
}


/* Location:           C:\Users\Administrator\Documents\ds\反编译\AndroidKiller_v1.3.1\projects\Jungledsq_ppc\ProjectSrc\smali\
 * Qualified Name:     org.jivesoftware.smack.filter.PacketIDFilter
 * JD-Core Version:    0.7.0.1
 */