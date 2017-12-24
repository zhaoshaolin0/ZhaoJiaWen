package org.jivesoftware.smack.filter;

import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.util.StringUtils;

public class FromMatchesFilter
  implements PacketFilter
{
  private String address;
  private boolean matchBareJID = false;
  
  public FromMatchesFilter(String paramString)
  {
    if (paramString == null) {
      throw new IllegalArgumentException("Parameter cannot be null.");
    }
    this.address = paramString.toLowerCase();
    this.matchBareJID = "".equals(StringUtils.parseResource(paramString));
  }
  
  public boolean accept(Packet paramPacket)
  {
    if (paramPacket.getFrom() == null) {
      return false;
    }
    if (this.matchBareJID) {
      return paramPacket.getFrom().toLowerCase().startsWith(this.address);
    }
    return this.address.equals(paramPacket.getFrom().toLowerCase());
  }
  
  public String toString()
  {
    return "FromMatchesFilter: " + this.address;
  }
}


/* Location:           C:\Users\Administrator\Documents\ds\反编译\AndroidKiller_v1.3.1\projects\Jungledsq_ppc\ProjectSrc\smali\
 * Qualified Name:     org.jivesoftware.smack.filter.FromMatchesFilter
 * JD-Core Version:    0.7.0.1
 */