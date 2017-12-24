package org.jivesoftware.smackx.packet;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import org.jivesoftware.smack.packet.IQ;

public class MUCAdmin
  extends IQ
{
  private List items = new ArrayList();
  
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
    localStringBuilder.append("<query xmlns=\"http://jabber.org/protocol/muc#admin\">");
    List localList = this.items;
    int i = 0;
    try
    {
      while (i < this.items.size())
      {
        localStringBuilder.append(((Item)this.items.get(i)).toXML());
        i += 1;
      }
      localStringBuilder.append(getExtensionsXML());
      localStringBuilder.append("</query>");
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
  
  public static class Item
  {
    private String actor;
    private String affiliation;
    private String jid;
    private String nick;
    private String reason;
    private String role;
    
    public Item(String paramString1, String paramString2)
    {
      this.affiliation = paramString1;
      this.role = paramString2;
    }
    
    public String getActor()
    {
      return this.actor;
    }
    
    public String getAffiliation()
    {
      return this.affiliation;
    }
    
    public String getJid()
    {
      return this.jid;
    }
    
    public String getNick()
    {
      return this.nick;
    }
    
    public String getReason()
    {
      return this.reason;
    }
    
    public String getRole()
    {
      return this.role;
    }
    
    public void setActor(String paramString)
    {
      this.actor = paramString;
    }
    
    public void setJid(String paramString)
    {
      this.jid = paramString;
    }
    
    public void setNick(String paramString)
    {
      this.nick = paramString;
    }
    
    public void setReason(String paramString)
    {
      this.reason = paramString;
    }
    
    public String toXML()
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("<item");
      if (getAffiliation() != null) {
        localStringBuilder.append(" affiliation=\"").append(getAffiliation()).append("\"");
      }
      if (getJid() != null) {
        localStringBuilder.append(" jid=\"").append(getJid()).append("\"");
      }
      if (getNick() != null) {
        localStringBuilder.append(" nick=\"").append(getNick()).append("\"");
      }
      if (getRole() != null) {
        localStringBuilder.append(" role=\"").append(getRole()).append("\"");
      }
      if ((getReason() == null) && (getActor() == null)) {
        localStringBuilder.append("/>");
      }
      for (;;)
      {
        return localStringBuilder.toString();
        localStringBuilder.append(">");
        if (getReason() != null) {
          localStringBuilder.append("<reason>").append(getReason()).append("</reason>");
        }
        if (getActor() != null) {
          localStringBuilder.append("<actor jid=\"").append(getActor()).append("\"/>");
        }
        localStringBuilder.append("</item>");
      }
    }
  }
}


/* Location:           C:\Users\Administrator\Documents\ds\反编译\AndroidKiller_v1.3.1\projects\Jungledsq_ppc\ProjectSrc\smali\
 * Qualified Name:     org.jivesoftware.smackx.packet.MUCAdmin
 * JD-Core Version:    0.7.0.1
 */