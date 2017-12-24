package org.jivesoftware.smackx.packet;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import org.jivesoftware.smack.packet.IQ;

public class Time
  extends IQ
{
  private static DateFormat displayFormat = DateFormat.getDateTimeInstance();
  private static SimpleDateFormat utcFormat = new SimpleDateFormat("yyyyMMdd'T'HH:mm:ss");
  private String display = null;
  private String tz = null;
  private String utc = null;
  
  public Time() {}
  
  public Time(Calendar paramCalendar)
  {
    TimeZone localTimeZone = paramCalendar.getTimeZone();
    this.tz = paramCalendar.getTimeZone().getID();
    this.display = displayFormat.format(paramCalendar.getTime());
    this.utc = utcFormat.format(new Date(paramCalendar.getTimeInMillis() - localTimeZone.getOffset(paramCalendar.getTimeInMillis())));
  }
  
  public String getChildElementXML()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("<query xmlns=\"jabber:iq:time\">");
    if (this.utc != null) {
      localStringBuilder.append("<utc>").append(this.utc).append("</utc>");
    }
    if (this.tz != null) {
      localStringBuilder.append("<tz>").append(this.tz).append("</tz>");
    }
    if (this.display != null) {
      localStringBuilder.append("<display>").append(this.display).append("</display>");
    }
    localStringBuilder.append("</query>");
    return localStringBuilder.toString();
  }
  
  public String getDisplay()
  {
    return this.display;
  }
  
  public Date getTime()
  {
    if (this.utc == null) {
      return null;
    }
    Object localObject1 = null;
    try
    {
      Object localObject2 = Calendar.getInstance();
      ((Calendar)localObject2).setTime(new Date(utcFormat.parse(this.utc).getTime() + ((Calendar)localObject2).getTimeZone().getOffset(((Calendar)localObject2).getTimeInMillis())));
      localObject2 = ((Calendar)localObject2).getTime();
      localObject1 = localObject2;
    }
    catch (Exception localException)
    {
      for (;;)
      {
        localException.printStackTrace();
      }
    }
    return localObject1;
  }
  
  public String getTz()
  {
    return this.tz;
  }
  
  public String getUtc()
  {
    return this.utc;
  }
  
  public void setDisplay(String paramString)
  {
    this.display = paramString;
  }
  
  public void setTime(Date paramDate)
  {
    this.utc = utcFormat.format(new Date(paramDate.getTime() - TimeZone.getDefault().getOffset(paramDate.getTime())));
  }
  
  public void setTz(String paramString)
  {
    this.tz = paramString;
  }
  
  public void setUtc(String paramString)
  {
    this.utc = paramString;
  }
}


/* Location:           C:\Users\Administrator\Documents\ds\反编译\AndroidKiller_v1.3.1\projects\Jungledsq_ppc\ProjectSrc\smali\
 * Qualified Name:     org.jivesoftware.smackx.packet.Time
 * JD-Core Version:    0.7.0.1
 */