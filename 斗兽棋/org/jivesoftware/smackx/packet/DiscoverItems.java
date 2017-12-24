package org.jivesoftware.smackx.packet;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import org.jivesoftware.smack.packet.IQ;

public class DiscoverItems
  extends IQ
{
  private final List<Item> items = new CopyOnWriteArrayList();
  private String node;
  
  public void addItem(Item paramItem)
  {
    synchronized (this.items)
    {
      this.items.add(paramItem);
      return;
    }
  }
  
  public String getChildElementXML()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("<query xmlns=\"http://jabber.org/protocol/disco#items\"");
    if (getNode() != null)
    {
      localStringBuilder.append(" node=\"");
      localStringBuilder.append(getNode());
      localStringBuilder.append("\"");
    }
    localStringBuilder.append(">");
    synchronized (this.items)
    {
      Iterator localIterator = this.items.iterator();
      if (localIterator.hasNext()) {
        localStringBuilder.append(((Item)localIterator.next()).toXML());
      }
    }
    localObject.append("</query>");
    return localObject.toString();
  }
  
  public Iterator<Item> getItems()
  {
    synchronized (this.items)
    {
      Iterator localIterator = Collections.unmodifiableList(this.items).iterator();
      return localIterator;
    }
  }
  
  public String getNode()
  {
    return this.node;
  }
  
  public void setNode(String paramString)
  {
    this.node = paramString;
  }
  
  public static class Item
  {
    public static final String REMOVE_ACTION = "remove";
    public static final String UPDATE_ACTION = "update";
    private String action;
    private String entityID;
    private String name;
    private String node;
    
    public Item(String paramString)
    {
      this.entityID = paramString;
    }
    
    public String getAction()
    {
      return this.action;
    }
    
    public String getEntityID()
    {
      return this.entityID;
    }
    
    public String getName()
    {
      return this.name;
    }
    
    public String getNode()
    {
      return this.node;
    }
    
    public void setAction(String paramString)
    {
      this.action = paramString;
    }
    
    public void setName(String paramString)
    {
      this.name = paramString;
    }
    
    public void setNode(String paramString)
    {
      this.node = paramString;
    }
    
    public String toXML()
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("<item jid=\"").append(this.entityID).append("\"");
      if (this.name != null) {
        localStringBuilder.append(" name=\"").append(this.name).append("\"");
      }
      if (this.node != null) {
        localStringBuilder.append(" node=\"").append(this.node).append("\"");
      }
      if (this.action != null) {
        localStringBuilder.append(" action=\"").append(this.action).append("\"");
      }
      localStringBuilder.append("/>");
      return localStringBuilder.toString();
    }
  }
}


/* Location:           C:\Users\Administrator\Documents\ds\反编译\AndroidKiller_v1.3.1\projects\Jungledsq_ppc\ProjectSrc\smali\
 * Qualified Name:     org.jivesoftware.smackx.packet.DiscoverItems
 * JD-Core Version:    0.7.0.1
 */