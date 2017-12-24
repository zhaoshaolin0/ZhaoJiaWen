package org.jivesoftware.smack.filter;

import org.jivesoftware.smack.packet.Packet;

public class ToContainsFilter
  implements PacketFilter
{
  private String to;
  
  public ToContainsFilter(String paramString)
  {
    if (paramString == null) {
      throw new IllegalArgumentException("Parameter cannot be null.");
    }
    this.to = paramString.toLowerCase();
  }
  
  public boolean accept(Packet paramPacket)
  {
    if (paramPacket.getTo() == null) {
      return false;
    }
    return paramPacket.getTo().toLowerCase().indexOf(this.to) != -1;
  }
}


/* Location:           C:\Users\Administrator\Documents\ds\反编译\AndroidKiller_v1.3.1\projects\Jungledsq_ppc\ProjectSrc\smali\
 * Qualified Name:     org.jivesoftware.smack.filter.ToContainsFilter
 * JD-Core Version:    0.7.0.1
 */