package org.jivesoftware.smack.filter;

import org.jivesoftware.smack.packet.Packet;

public class OrFilter
  implements PacketFilter
{
  private PacketFilter[] filters;
  private int size;
  
  public OrFilter()
  {
    this.size = 0;
    this.filters = new PacketFilter[3];
  }
  
  public OrFilter(PacketFilter paramPacketFilter1, PacketFilter paramPacketFilter2)
  {
    if ((paramPacketFilter1 == null) || (paramPacketFilter2 == null)) {
      throw new IllegalArgumentException("Parameters cannot be null.");
    }
    this.size = 2;
    this.filters = new PacketFilter[2];
    this.filters[0] = paramPacketFilter1;
    this.filters[1] = paramPacketFilter2;
  }
  
  public boolean accept(Packet paramPacket)
  {
    int i = 0;
    while (i < this.size)
    {
      if (this.filters[i].accept(paramPacket)) {
        return true;
      }
      i += 1;
    }
    return false;
  }
  
  public void addFilter(PacketFilter paramPacketFilter)
  {
    if (paramPacketFilter == null) {
      throw new IllegalArgumentException("Parameter cannot be null.");
    }
    if (this.size == this.filters.length)
    {
      PacketFilter[] arrayOfPacketFilter = new PacketFilter[this.filters.length + 2];
      int i = 0;
      while (i < this.filters.length)
      {
        arrayOfPacketFilter[i] = this.filters[i];
        i += 1;
      }
      this.filters = arrayOfPacketFilter;
    }
    this.filters[this.size] = paramPacketFilter;
    this.size += 1;
  }
  
  public String toString()
  {
    return this.filters.toString();
  }
}


/* Location:           C:\Users\Administrator\Documents\ds\反编译\AndroidKiller_v1.3.1\projects\Jungledsq_ppc\ProjectSrc\smali\
 * Qualified Name:     org.jivesoftware.smack.filter.OrFilter
 * JD-Core Version:    0.7.0.1
 */