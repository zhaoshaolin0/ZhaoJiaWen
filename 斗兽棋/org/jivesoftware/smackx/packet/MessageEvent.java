package org.jivesoftware.smackx.packet;

import java.util.ArrayList;
import java.util.Iterator;
import org.jivesoftware.smack.packet.PacketExtension;

public class MessageEvent
  implements PacketExtension
{
  public static final String CANCELLED = "cancelled";
  public static final String COMPOSING = "composing";
  public static final String DELIVERED = "delivered";
  public static final String DISPLAYED = "displayed";
  public static final String OFFLINE = "offline";
  private boolean cancelled = true;
  private boolean composing = false;
  private boolean delivered = false;
  private boolean displayed = false;
  private boolean offline = false;
  private String packetID = null;
  
  public String getElementName()
  {
    return "x";
  }
  
  public Iterator getEventTypes()
  {
    ArrayList localArrayList = new ArrayList();
    if (isDelivered()) {
      localArrayList.add("delivered");
    }
    if ((!isMessageEventRequest()) && (isCancelled())) {
      localArrayList.add("cancelled");
    }
    if (isComposing()) {
      localArrayList.add("composing");
    }
    if (isDisplayed()) {
      localArrayList.add("displayed");
    }
    if (isOffline()) {
      localArrayList.add("offline");
    }
    return localArrayList.iterator();
  }
  
  public String getNamespace()
  {
    return "jabber:x:event";
  }
  
  public String getPacketID()
  {
    return this.packetID;
  }
  
  public boolean isCancelled()
  {
    return this.cancelled;
  }
  
  public boolean isComposing()
  {
    return this.composing;
  }
  
  public boolean isDelivered()
  {
    return this.delivered;
  }
  
  public boolean isDisplayed()
  {
    return this.displayed;
  }
  
  public boolean isMessageEventRequest()
  {
    return this.packetID == null;
  }
  
  public boolean isOffline()
  {
    return this.offline;
  }
  
  public void setCancelled(boolean paramBoolean)
  {
    this.cancelled = paramBoolean;
  }
  
  public void setComposing(boolean paramBoolean)
  {
    this.composing = paramBoolean;
    setCancelled(false);
  }
  
  public void setDelivered(boolean paramBoolean)
  {
    this.delivered = paramBoolean;
    setCancelled(false);
  }
  
  public void setDisplayed(boolean paramBoolean)
  {
    this.displayed = paramBoolean;
    setCancelled(false);
  }
  
  public void setOffline(boolean paramBoolean)
  {
    this.offline = paramBoolean;
    setCancelled(false);
  }
  
  public void setPacketID(String paramString)
  {
    this.packetID = paramString;
  }
  
  public String toXML()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("<").append(getElementName()).append(" xmlns=\"").append(getNamespace()).append("\">");
    if (isOffline()) {
      localStringBuilder.append("<").append("offline").append("/>");
    }
    if (isDelivered()) {
      localStringBuilder.append("<").append("delivered").append("/>");
    }
    if (isDisplayed()) {
      localStringBuilder.append("<").append("displayed").append("/>");
    }
    if (isComposing()) {
      localStringBuilder.append("<").append("composing").append("/>");
    }
    if (getPacketID() != null) {
      localStringBuilder.append("<id>").append(getPacketID()).append("</id>");
    }
    localStringBuilder.append("</").append(getElementName()).append(">");
    return localStringBuilder.toString();
  }
}


/* Location:           C:\Users\Administrator\Documents\ds\反编译\AndroidKiller_v1.3.1\projects\Jungledsq_ppc\ProjectSrc\smali\
 * Qualified Name:     org.jivesoftware.smackx.packet.MessageEvent
 * JD-Core Version:    0.7.0.1
 */