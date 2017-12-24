package org.jivesoftware.smackx.packet;

import org.jivesoftware.smack.packet.IQ;

public class PEPPubSub
  extends IQ
{
  PEPItem item;
  
  public PEPPubSub(PEPItem paramPEPItem)
  {
    this.item = paramPEPItem;
  }
  
  public String getChildElementXML()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("<").append(getElementName()).append(" xmlns=\"").append(getNamespace()).append("\">");
    localStringBuilder.append("<publish node=\"").append(this.item.getNode()).append("\">");
    localStringBuilder.append(this.item.toXML());
    localStringBuilder.append("</publish>");
    localStringBuilder.append("</").append(getElementName()).append(">");
    return localStringBuilder.toString();
  }
  
  public String getElementName()
  {
    return "pubsub";
  }
  
  public String getNamespace()
  {
    return "http://jabber.org/protocol/pubsub";
  }
}


/* Location:           C:\Users\Administrator\Documents\ds\反编译\AndroidKiller_v1.3.1\projects\Jungledsq_ppc\ProjectSrc\smali\
 * Qualified Name:     org.jivesoftware.smackx.packet.PEPPubSub
 * JD-Core Version:    0.7.0.1
 */