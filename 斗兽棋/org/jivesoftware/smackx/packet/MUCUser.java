package org.jivesoftware.smackx.packet;

import org.jivesoftware.smack.packet.PacketExtension;

public class MUCUser
  implements PacketExtension
{
  private Decline decline;
  private Destroy destroy;
  private Invite invite;
  private Item item;
  private String password;
  private Status status;
  
  public Decline getDecline()
  {
    return this.decline;
  }
  
  public Destroy getDestroy()
  {
    return this.destroy;
  }
  
  public String getElementName()
  {
    return "x";
  }
  
  public Invite getInvite()
  {
    return this.invite;
  }
  
  public Item getItem()
  {
    return this.item;
  }
  
  public String getNamespace()
  {
    return "http://jabber.org/protocol/muc#user";
  }
  
  public String getPassword()
  {
    return this.password;
  }
  
  public Status getStatus()
  {
    return this.status;
  }
  
  public void setDecline(Decline paramDecline)
  {
    this.decline = paramDecline;
  }
  
  public void setDestroy(Destroy paramDestroy)
  {
    this.destroy = paramDestroy;
  }
  
  public void setInvite(Invite paramInvite)
  {
    this.invite = paramInvite;
  }
  
  public void setItem(Item paramItem)
  {
    this.item = paramItem;
  }
  
  public void setPassword(String paramString)
  {
    this.password = paramString;
  }
  
  public void setStatus(Status paramStatus)
  {
    this.status = paramStatus;
  }
  
  public String toXML()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("<").append(getElementName()).append(" xmlns=\"").append(getNamespace()).append("\">");
    if (getInvite() != null) {
      localStringBuilder.append(getInvite().toXML());
    }
    if (getDecline() != null) {
      localStringBuilder.append(getDecline().toXML());
    }
    if (getItem() != null) {
      localStringBuilder.append(getItem().toXML());
    }
    if (getPassword() != null) {
      localStringBuilder.append("<password>").append(getPassword()).append("</password>");
    }
    if (getStatus() != null) {
      localStringBuilder.append(getStatus().toXML());
    }
    if (getDestroy() != null) {
      localStringBuilder.append(getDestroy().toXML());
    }
    localStringBuilder.append("</").append(getElementName()).append(">");
    return localStringBuilder.toString();
  }
  
  public static class Decline
  {
    private String from;
    private String reason;
    private String to;
    
    public String getFrom()
    {
      return this.from;
    }
    
    public String getReason()
    {
      return this.reason;
    }
    
    public String getTo()
    {
      return this.to;
    }
    
    public void setFrom(String paramString)
    {
      this.from = paramString;
    }
    
    public void setReason(String paramString)
    {
      this.reason = paramString;
    }
    
    public void setTo(String paramString)
    {
      this.to = paramString;
    }
    
    public String toXML()
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("<decline ");
      if (getTo() != null) {
        localStringBuilder.append(" to=\"").append(getTo()).append("\"");
      }
      if (getFrom() != null) {
        localStringBuilder.append(" from=\"").append(getFrom()).append("\"");
      }
      localStringBuilder.append(">");
      if (getReason() != null) {
        localStringBuilder.append("<reason>").append(getReason()).append("</reason>");
      }
      localStringBuilder.append("</decline>");
      return localStringBuilder.toString();
    }
  }
  
  public static class Destroy
  {
    private String jid;
    private String reason;
    
    public String getJid()
    {
      return this.jid;
    }
    
    public String getReason()
    {
      return this.reason;
    }
    
    public void setJid(String paramString)
    {
      this.jid = paramString;
    }
    
    public void setReason(String paramString)
    {
      this.reason = paramString;
    }
    
    public String toXML()
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("<destroy");
      if (getJid() != null) {
        localStringBuilder.append(" jid=\"").append(getJid()).append("\"");
      }
      if (getReason() == null) {
        localStringBuilder.append("/>");
      }
      for (;;)
      {
        return localStringBuilder.toString();
        localStringBuilder.append(">");
        if (getReason() != null) {
          localStringBuilder.append("<reason>").append(getReason()).append("</reason>");
        }
        localStringBuilder.append("</destroy>");
      }
    }
  }
  
  public static class Invite
  {
    private String from;
    private String reason;
    private String to;
    
    public String getFrom()
    {
      return this.from;
    }
    
    public String getReason()
    {
      return this.reason;
    }
    
    public String getTo()
    {
      return this.to;
    }
    
    public void setFrom(String paramString)
    {
      this.from = paramString;
    }
    
    public void setReason(String paramString)
    {
      this.reason = paramString;
    }
    
    public void setTo(String paramString)
    {
      this.to = paramString;
    }
    
    public String toXML()
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("<invite ");
      if (getTo() != null) {
        localStringBuilder.append(" to=\"").append(getTo()).append("\"");
      }
      if (getFrom() != null) {
        localStringBuilder.append(" from=\"").append(getFrom()).append("\"");
      }
      localStringBuilder.append(">");
      if (getReason() != null) {
        localStringBuilder.append("<reason>").append(getReason()).append("</reason>");
      }
      localStringBuilder.append("</invite>");
      return localStringBuilder.toString();
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
      if (this.actor == null) {
        return "";
      }
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
      if (this.reason == null) {
        return "";
      }
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
  
  public static class Status
  {
    private String code;
    
    public Status(String paramString)
    {
      this.code = paramString;
    }
    
    public String getCode()
    {
      return this.code;
    }
    
    public String toXML()
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("<status code=\"").append(getCode()).append("\"/>");
      return localStringBuilder.toString();
    }
  }
}


/* Location:           C:\Users\Administrator\Documents\ds\反编译\AndroidKiller_v1.3.1\projects\Jungledsq_ppc\ProjectSrc\smali\
 * Qualified Name:     org.jivesoftware.smackx.packet.MUCUser
 * JD-Core Version:    0.7.0.1
 */