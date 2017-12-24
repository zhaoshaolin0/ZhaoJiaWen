package org.jivesoftware.smackx.packet;

import org.jivesoftware.smack.packet.PacketExtension;

public abstract class PEPItem
  implements PacketExtension
{
  String id;
  
  public PEPItem(String paramString)
  {
    this.id = paramString;
  }
  
  public String getElementName()
  {
    return "item";
  }
  
  abstract String getItemDetailsXML();
  
  public String getNamespace()
  {
    return "http://jabber.org/protocol/pubsub";
  }
  
  abstract String getNode();
  
  public String toXML()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("<").append(getElementName()).append(" id=\"").append(this.id).append("\">");
    localStringBuilder.append(getItemDetailsXML());
    localStringBuilder.append("</").append(getElementName()).append(">");
    return localStringBuilder.toString();
  }
}


/* Location:           C:\Users\Administrator\Documents\ds\反编译\AndroidKiller_v1.3.1\projects\Jungledsq_ppc\ProjectSrc\smali\
 * Qualified Name:     org.jivesoftware.smackx.packet.PEPItem
 * JD-Core Version:    0.7.0.1
 */