package org.jivesoftware.smackx.packet;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.provider.IQProvider;
import org.xmlpull.v1.XmlPullParser;

public class OfflineMessageRequest
  extends IQ
{
  private boolean fetch = false;
  private List items = new ArrayList();
  private boolean purge = false;
  
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
    localStringBuilder.append("<offline xmlns=\"http://jabber.org/protocol/offline\">");
    List localList = this.items;
    int i = 0;
    try
    {
      while (i < this.items.size())
      {
        localStringBuilder.append(((Item)this.items.get(i)).toXML());
        i += 1;
      }
      if (this.purge) {
        localStringBuilder.append("<purge/>");
      }
      if (this.fetch) {
        localStringBuilder.append("<fetch/>");
      }
      localStringBuilder.append(getExtensionsXML());
      localStringBuilder.append("</offline>");
      return localStringBuilder.toString();
    }
    finally {}
  }
  
  public Iterator getItems()
  {
    synchronized (this.items)
    {
      Iterator localIterator = Collections.unmodifiableList(new ArrayList(this.items)).iterator();
      return localIterator;
    }
  }
  
  public boolean isFetch()
  {
    return this.fetch;
  }
  
  public boolean isPurge()
  {
    return this.purge;
  }
  
  public void setFetch(boolean paramBoolean)
  {
    this.fetch = paramBoolean;
  }
  
  public void setPurge(boolean paramBoolean)
  {
    this.purge = paramBoolean;
  }
  
  public static class Item
  {
    private String action;
    private String jid;
    private String node;
    
    public Item(String paramString)
    {
      this.node = paramString;
    }
    
    public String getAction()
    {
      return this.action;
    }
    
    public String getJid()
    {
      return this.jid;
    }
    
    public String getNode()
    {
      return this.node;
    }
    
    public void setAction(String paramString)
    {
      this.action = paramString;
    }
    
    public void setJid(String paramString)
    {
      this.jid = paramString;
    }
    
    public String toXML()
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("<item");
      if (getAction() != null) {
        localStringBuilder.append(" action=\"").append(getAction()).append("\"");
      }
      if (getJid() != null) {
        localStringBuilder.append(" jid=\"").append(getJid()).append("\"");
      }
      if (getNode() != null) {
        localStringBuilder.append(" node=\"").append(getNode()).append("\"");
      }
      localStringBuilder.append("/>");
      return localStringBuilder.toString();
    }
  }
  
  public static class Provider
    implements IQProvider
  {
    private OfflineMessageRequest.Item parseItem(XmlPullParser paramXmlPullParser)
      throws Exception
    {
      int i = 0;
      OfflineMessageRequest.Item localItem = new OfflineMessageRequest.Item(paramXmlPullParser.getAttributeValue("", "node"));
      localItem.setAction(paramXmlPullParser.getAttributeValue("", "action"));
      localItem.setJid(paramXmlPullParser.getAttributeValue("", "jid"));
      while (i == 0) {
        if ((paramXmlPullParser.next() == 3) && (paramXmlPullParser.getName().equals("item"))) {
          i = 1;
        }
      }
      return localItem;
    }
    
    public IQ parseIQ(XmlPullParser paramXmlPullParser)
      throws Exception
    {
      OfflineMessageRequest localOfflineMessageRequest = new OfflineMessageRequest();
      int i = 0;
      while (i == 0)
      {
        int j = paramXmlPullParser.next();
        if (j == 2)
        {
          if (paramXmlPullParser.getName().equals("item")) {
            localOfflineMessageRequest.addItem(parseItem(paramXmlPullParser));
          } else if (paramXmlPullParser.getName().equals("purge")) {
            localOfflineMessageRequest.setPurge(true);
          } else if (paramXmlPullParser.getName().equals("fetch")) {
            localOfflineMessageRequest.setFetch(true);
          }
        }
        else if ((j == 3) && (paramXmlPullParser.getName().equals("offline"))) {
          i = 1;
        }
      }
      return localOfflineMessageRequest;
    }
  }
}


/* Location:           C:\Users\Administrator\Documents\ds\反编译\AndroidKiller_v1.3.1\projects\Jungledsq_ppc\ProjectSrc\smali\
 * Qualified Name:     org.jivesoftware.smackx.packet.OfflineMessageRequest
 * JD-Core Version:    0.7.0.1
 */