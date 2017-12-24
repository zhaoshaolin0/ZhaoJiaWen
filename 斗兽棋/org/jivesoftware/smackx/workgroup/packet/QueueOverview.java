package org.jivesoftware.smackx.workgroup.packet;

import java.text.SimpleDateFormat;
import java.util.Date;
import org.jivesoftware.smack.packet.PacketExtension;
import org.jivesoftware.smack.provider.PacketExtensionProvider;
import org.jivesoftware.smackx.workgroup.agent.WorkgroupQueue.Status;
import org.xmlpull.v1.XmlPullParser;

public class QueueOverview
  implements PacketExtension
{
  private static final SimpleDateFormat DATE_FORMATTER = new SimpleDateFormat("yyyyMMdd'T'HH:mm:ss");
  public static String ELEMENT_NAME = "notify-queue";
  public static String NAMESPACE = "http://jabber.org/protocol/workgroup";
  private int averageWaitTime = -1;
  private Date oldestEntry = null;
  private WorkgroupQueue.Status status = null;
  private int userCount = -1;
  
  public int getAverageWaitTime()
  {
    return this.averageWaitTime;
  }
  
  public String getElementName()
  {
    return ELEMENT_NAME;
  }
  
  public String getNamespace()
  {
    return NAMESPACE;
  }
  
  public Date getOldestEntry()
  {
    return this.oldestEntry;
  }
  
  public WorkgroupQueue.Status getStatus()
  {
    return this.status;
  }
  
  public int getUserCount()
  {
    return this.userCount;
  }
  
  void setAverageWaitTime(int paramInt)
  {
    this.averageWaitTime = paramInt;
  }
  
  void setOldestEntry(Date paramDate)
  {
    this.oldestEntry = paramDate;
  }
  
  void setStatus(WorkgroupQueue.Status paramStatus)
  {
    this.status = paramStatus;
  }
  
  void setUserCount(int paramInt)
  {
    this.userCount = paramInt;
  }
  
  public String toXML()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("<").append(ELEMENT_NAME).append(" xmlns=\"").append(NAMESPACE).append("\">");
    if (this.userCount != -1) {
      localStringBuilder.append("<count>").append(this.userCount).append("</count>");
    }
    if (this.oldestEntry != null) {
      localStringBuilder.append("<oldest>").append(DATE_FORMATTER.format(this.oldestEntry)).append("</oldest>");
    }
    if (this.averageWaitTime != -1) {
      localStringBuilder.append("<time>").append(this.averageWaitTime).append("</time>");
    }
    if (this.status != null) {
      localStringBuilder.append("<status>").append(this.status).append("</status>");
    }
    localStringBuilder.append("</").append(ELEMENT_NAME).append(">");
    return localStringBuilder.toString();
  }
  
  public static class Provider
    implements PacketExtensionProvider
  {
    public PacketExtension parseExtension(XmlPullParser paramXmlPullParser)
      throws Exception
    {
      int i = paramXmlPullParser.getEventType();
      QueueOverview localQueueOverview = new QueueOverview();
      if (i != 2) {}
      i = paramXmlPullParser.next();
      if ((i != 3) || (!QueueOverview.ELEMENT_NAME.equals(paramXmlPullParser.getName())))
      {
        if ("count".equals(paramXmlPullParser.getName())) {
          localQueueOverview.setUserCount(Integer.parseInt(paramXmlPullParser.nextText()));
        }
        for (;;)
        {
          int j = paramXmlPullParser.next();
          i = j;
          if (j == 3) {
            break;
          }
          i = j;
          break;
          if ("time".equals(paramXmlPullParser.getName())) {
            localQueueOverview.setAverageWaitTime(Integer.parseInt(paramXmlPullParser.nextText()));
          } else if ("oldest".equals(paramXmlPullParser.getName())) {
            localQueueOverview.setOldestEntry(QueueOverview.DATE_FORMATTER.parse(paramXmlPullParser.nextText()));
          } else if ("status".equals(paramXmlPullParser.getName())) {
            localQueueOverview.setStatus(WorkgroupQueue.Status.fromString(paramXmlPullParser.nextText()));
          }
        }
      }
      if (i != 3) {}
      return localQueueOverview;
    }
  }
}


/* Location:           C:\Users\Administrator\Documents\ds\反编译\AndroidKiller_v1.3.1\projects\Jungledsq_ppc\ProjectSrc\smali\
 * Qualified Name:     org.jivesoftware.smackx.workgroup.packet.QueueOverview
 * JD-Core Version:    0.7.0.1
 */