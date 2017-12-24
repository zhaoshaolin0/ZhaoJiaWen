package org.jivesoftware.smackx.packet;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import org.jivesoftware.smack.packet.PacketExtension;

public class DelayInformation
  implements PacketExtension
{
  public static SimpleDateFormat NEW_UTC_FORMAT;
  public static SimpleDateFormat UTC_FORMAT = new SimpleDateFormat("yyyyMMdd'T'HH:mm:ss");
  private String from;
  private String reason;
  private Date stamp;
  
  static
  {
    NEW_UTC_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
    UTC_FORMAT.setTimeZone(TimeZone.getTimeZone("GMT+0"));
    NEW_UTC_FORMAT.setTimeZone(TimeZone.getTimeZone("UTC"));
  }
  
  public DelayInformation(Date paramDate)
  {
    this.stamp = paramDate;
  }
  
  public String getElementName()
  {
    return "x";
  }
  
  public String getFrom()
  {
    return this.from;
  }
  
  public String getNamespace()
  {
    return "jabber:x:delay";
  }
  
  public String getReason()
  {
    return this.reason;
  }
  
  public Date getStamp()
  {
    return this.stamp;
  }
  
  public void setFrom(String paramString)
  {
    this.from = paramString;
  }
  
  public void setReason(String paramString)
  {
    this.reason = paramString;
  }
  
  public String toXML()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("<").append(getElementName()).append(" xmlns=\"").append(getNamespace()).append("\"");
    localStringBuilder.append(" stamp=\"");
    synchronized (UTC_FORMAT)
    {
      localStringBuilder.append(UTC_FORMAT.format(this.stamp));
      localStringBuilder.append("\"");
      if ((this.from != null) && (this.from.length() > 0)) {
        localStringBuilder.append(" from=\"").append(this.from).append("\"");
      }
      localStringBuilder.append(">");
      if ((this.reason != null) && (this.reason.length() > 0)) {
        localStringBuilder.append(this.reason);
      }
      localStringBuilder.append("</").append(getElementName()).append(">");
      return localStringBuilder.toString();
    }
  }
}


/* Location:           C:\Users\Administrator\Documents\ds\反编译\AndroidKiller_v1.3.1\projects\Jungledsq_ppc\ProjectSrc\smali\
 * Qualified Name:     org.jivesoftware.smackx.packet.DelayInformation
 * JD-Core Version:    0.7.0.1
 */