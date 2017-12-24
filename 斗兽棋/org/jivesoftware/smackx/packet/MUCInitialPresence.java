package org.jivesoftware.smackx.packet;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import org.jivesoftware.smack.packet.PacketExtension;

public class MUCInitialPresence
  implements PacketExtension
{
  private History history;
  private String password;
  
  public String getElementName()
  {
    return "x";
  }
  
  public History getHistory()
  {
    return this.history;
  }
  
  public String getNamespace()
  {
    return "http://jabber.org/protocol/muc";
  }
  
  public String getPassword()
  {
    return this.password;
  }
  
  public void setHistory(History paramHistory)
  {
    this.history = paramHistory;
  }
  
  public void setPassword(String paramString)
  {
    this.password = paramString;
  }
  
  public String toXML()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("<").append(getElementName()).append(" xmlns=\"").append(getNamespace()).append("\">");
    if (getPassword() != null) {
      localStringBuilder.append("<password>").append(getPassword()).append("</password>");
    }
    if (getHistory() != null) {
      localStringBuilder.append(getHistory().toXML());
    }
    localStringBuilder.append("</").append(getElementName()).append(">");
    return localStringBuilder.toString();
  }
  
  public static class History
  {
    private int maxChars = -1;
    private int maxStanzas = -1;
    private int seconds = -1;
    private Date since;
    
    public int getMaxChars()
    {
      return this.maxChars;
    }
    
    public int getMaxStanzas()
    {
      return this.maxStanzas;
    }
    
    public int getSeconds()
    {
      return this.seconds;
    }
    
    public Date getSince()
    {
      return this.since;
    }
    
    public void setMaxChars(int paramInt)
    {
      this.maxChars = paramInt;
    }
    
    public void setMaxStanzas(int paramInt)
    {
      this.maxStanzas = paramInt;
    }
    
    public void setSeconds(int paramInt)
    {
      this.seconds = paramInt;
    }
    
    public void setSince(Date paramDate)
    {
      this.since = paramDate;
    }
    
    public String toXML()
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("<history");
      if (getMaxChars() != -1) {
        localStringBuilder.append(" maxchars=\"").append(getMaxChars()).append("\"");
      }
      if (getMaxStanzas() != -1) {
        localStringBuilder.append(" maxstanzas=\"").append(getMaxStanzas()).append("\"");
      }
      if (getSeconds() != -1) {
        localStringBuilder.append(" seconds=\"").append(getSeconds()).append("\"");
      }
      if (getSince() != null)
      {
        SimpleDateFormat localSimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        localSimpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        localStringBuilder.append(" since=\"").append(localSimpleDateFormat.format(getSince())).append("\"");
      }
      localStringBuilder.append("/>");
      return localStringBuilder.toString();
    }
  }
}


/* Location:           C:\Users\Administrator\Documents\ds\反编译\AndroidKiller_v1.3.1\projects\Jungledsq_ppc\ProjectSrc\smali\
 * Qualified Name:     org.jivesoftware.smackx.packet.MUCInitialPresence
 * JD-Core Version:    0.7.0.1
 */