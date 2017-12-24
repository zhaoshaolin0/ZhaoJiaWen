package org.jivesoftware.smackx.packet;

import org.jivesoftware.smack.packet.PacketExtension;

public class PEPEvent
  implements PacketExtension
{
  PEPItem item;
  
  public PEPEvent() {}
  
  public PEPEvent(PEPItem paramPEPItem)
  {
    this.item = paramPEPItem;
  }
  
  public void addPEPItem(PEPItem paramPEPItem)
  {
    this.item = paramPEPItem;
  }
  
  public String getElementName()
  {
    return "event";
  }
  
  public String getNamespace()
  {
    return "http://jabber.org/protocol/pubsub";
  }
  
  public String toXML()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("<").append(getElementName()).append(" xmlns=\"").append(getNamespace()).append("\">");
    localStringBuilder.append(this.item.toXML());
    localStringBuilder.append("</").append(getElementName()).append(">");
    return localStringBuilder.toString();
  }
}


/* Location:           C:\Users\Administrator\Documents\ds\反编译\AndroidKiller_v1.3.1\projects\Jungledsq_ppc\ProjectSrc\smali\
 * Qualified Name:     org.jivesoftware.smackx.packet.PEPEvent
 * JD-Core Version:    0.7.0.1
 */