package org.jivesoftware.smackx.workgroup.packet;

import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import org.jivesoftware.smack.packet.PacketExtension;
import org.jivesoftware.smack.provider.PacketExtensionProvider;
import org.jivesoftware.smackx.workgroup.QueueUser;
import org.xmlpull.v1.XmlPullParser;

public class QueueDetails
  implements PacketExtension
{
  private static final SimpleDateFormat DATE_FORMATTER = new SimpleDateFormat("yyyyMMdd'T'HH:mm:ss");
  public static final String ELEMENT_NAME = "notify-queue-details";
  public static final String NAMESPACE = "http://jabber.org/protocol/workgroup";
  private Set users = new HashSet();
  
  private void addUser(QueueUser paramQueueUser)
  {
    synchronized (this.users)
    {
      this.users.add(paramQueueUser);
      return;
    }
  }
  
  public String getElementName()
  {
    return "notify-queue-details";
  }
  
  public String getNamespace()
  {
    return "http://jabber.org/protocol/workgroup";
  }
  
  public int getUserCount()
  {
    return this.users.size();
  }
  
  public Set getUsers()
  {
    synchronized (this.users)
    {
      Set localSet2 = this.users;
      return localSet2;
    }
  }
  
  public String toXML()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("<").append("notify-queue-details").append(" xmlns=\"").append("http://jabber.org/protocol/workgroup").append("\">");
    synchronized (this.users)
    {
      Iterator localIterator = this.users.iterator();
      if (localIterator.hasNext())
      {
        QueueUser localQueueUser = (QueueUser)localIterator.next();
        int i = localQueueUser.getQueuePosition();
        int j = localQueueUser.getEstimatedRemainingTime();
        Date localDate = localQueueUser.getQueueJoinTimestamp();
        localStringBuilder.append("<user jid=\"").append(localQueueUser.getUserID()).append("\">");
        if (i != -1) {
          localStringBuilder.append("<position>").append(i).append("</position>");
        }
        if (j != -1) {
          localStringBuilder.append("<time>").append(j).append("</time>");
        }
        if (localDate != null)
        {
          localStringBuilder.append("<join-time>");
          localStringBuilder.append(DATE_FORMATTER.format(localDate));
          localStringBuilder.append("</join-time>");
        }
        localStringBuilder.append("</user>");
      }
    }
    localObject.append("</").append("notify-queue-details").append(">");
    return localObject.toString();
  }
  
  public static class Provider
    implements PacketExtensionProvider
  {
    public PacketExtension parseExtension(XmlPullParser paramXmlPullParser)
      throws Exception
    {
      QueueDetails localQueueDetails = new QueueDetails(null);
      int j = paramXmlPullParser.getEventType();
      if ((j != 3) && ("notify-queue-details".equals(paramXmlPullParser.getName()))) {
        for (int i = paramXmlPullParser.next();; i = paramXmlPullParser.next())
        {
          j = i;
          if (i != 2) {
            break;
          }
          j = i;
          if (!"user".equals(paramXmlPullParser.getName())) {
            break;
          }
          j = -1;
          i = -1;
          Object localObject1 = null;
          String str = paramXmlPullParser.getAttributeValue("", "jid");
          if (str == null) {}
          int n = paramXmlPullParser.next();
          if ((n != 3) || (!"user".equals(paramXmlPullParser.getName())))
          {
            int k;
            int m;
            Object localObject2;
            if ("position".equals(paramXmlPullParser.getName()))
            {
              k = Integer.parseInt(paramXmlPullParser.nextText());
              m = i;
              localObject2 = localObject1;
            }
            for (;;)
            {
              int i1 = paramXmlPullParser.next();
              n = i1;
              localObject1 = localObject2;
              j = k;
              i = m;
              if (i1 == 3) {
                break;
              }
              n = i1;
              localObject1 = localObject2;
              j = k;
              i = m;
              break;
              if ("time".equals(paramXmlPullParser.getName()))
              {
                m = Integer.parseInt(paramXmlPullParser.nextText());
                localObject2 = localObject1;
                k = j;
              }
              else if ("join-time".equals(paramXmlPullParser.getName()))
              {
                localObject2 = QueueDetails.DATE_FORMATTER.parse(paramXmlPullParser.nextText());
                k = j;
                m = i;
              }
              else
              {
                localObject2 = localObject1;
                k = j;
                m = i;
                if (paramXmlPullParser.getName().equals("waitTime"))
                {
                  localObject2 = QueueDetails.DATE_FORMATTER.parse(paramXmlPullParser.nextText());
                  System.out.println(localObject2);
                  localObject2 = localObject1;
                  k = j;
                  m = i;
                }
              }
            }
          }
          localQueueDetails.addUser(new QueueUser(str, j, i, localObject1));
        }
      }
      return localQueueDetails;
    }
  }
}


/* Location:           C:\Users\Administrator\Documents\ds\反编译\AndroidKiller_v1.3.1\projects\Jungledsq_ppc\ProjectSrc\smali\
 * Qualified Name:     org.jivesoftware.smackx.workgroup.packet.QueueDetails
 * JD-Core Version:    0.7.0.1
 */