package org.jivesoftware.smack.filter;

import org.jivesoftware.smack.packet.Packet;

public class FromContainsFilter
  implements PacketFilter
{
  private String from;
  
  public FromContainsFilter(String paramString)
  {
    if (paramString == null) {
      throw new IllegalArgumentException("Parameter cannot be null.");
    }
    this.from = paramString.toLowerCase();
  }
  
  public boolean accept(Packet paramPacket)
  {
    if (paramPacket.getFrom() == null) {
      return false;
    }
    return paramPacket.getFrom().toLowerCase().indexOf(this.from) != -1;
  }
}


/* Location:           C:\Users\Administrator\Documents\ds\反编译\AndroidKiller_v1.3.1\projects\Jungledsq_ppc\ProjectSrc\smali\
 * Qualified Name:     org.jivesoftware.smack.filter.FromContainsFilter
 * JD-Core Version:    0.7.0.1
 */