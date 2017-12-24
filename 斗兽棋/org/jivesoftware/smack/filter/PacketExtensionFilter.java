package org.jivesoftware.smack.filter;

import org.jivesoftware.smack.packet.Packet;

public class PacketExtensionFilter
  implements PacketFilter
{
  private String elementName;
  private String namespace;
  
  public PacketExtensionFilter(String paramString)
  {
    this(null, paramString);
  }
  
  public PacketExtensionFilter(String paramString1, String paramString2)
  {
    this.elementName = paramString1;
    this.namespace = paramString2;
  }
  
  public boolean accept(Packet paramPacket)
  {
    return paramPacket.getExtension(this.elementName, this.namespace) != null;
  }
}


/* Location:           C:\Users\Administrator\Documents\ds\反编译\AndroidKiller_v1.3.1\projects\Jungledsq_ppc\ProjectSrc\smali\
 * Qualified Name:     org.jivesoftware.smack.filter.PacketExtensionFilter
 * JD-Core Version:    0.7.0.1
 */