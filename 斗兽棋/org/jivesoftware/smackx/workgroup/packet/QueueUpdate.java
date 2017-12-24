package org.jivesoftware.smackx.workgroup.packet;

import org.jivesoftware.smack.packet.PacketExtension;
import org.jivesoftware.smack.provider.PacketExtensionProvider;
import org.xmlpull.v1.XmlPullParser;

public class QueueUpdate
  implements PacketExtension
{
  public static final String ELEMENT_NAME = "queue-status";
  public static final String NAMESPACE = "http://jabber.org/protocol/workgroup";
  private int position;
  private int remainingTime;
  
  public QueueUpdate(int paramInt1, int paramInt2)
  {
    this.position = paramInt1;
    this.remainingTime = paramInt2;
  }
  
  public String getElementName()
  {
    return "queue-status";
  }
  
  public String getNamespace()
  {
    return "http://jabber.org/protocol/workgroup";
  }
  
  public int getPosition()
  {
    return this.position;
  }
  
  public int getRemaingTime()
  {
    return this.remainingTime;
  }
  
  public String toXML()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("<queue-status xmlns=\"http://jabber.org/protocol/workgroup\">");
    if (this.position != -1) {
      localStringBuilder.append("<position>").append(this.position).append("</position>");
    }
    if (this.remainingTime != -1) {
      localStringBuilder.append("<time>").append(this.remainingTime).append("</time>");
    }
    localStringBuilder.append("</queue-status>");
    return localStringBuilder.toString();
  }
  
  public static class Provider
    implements PacketExtensionProvider
  {
    public PacketExtension parseExtension(XmlPullParser paramXmlPullParser)
      throws Exception
    {
      int k = 0;
      int j = -1;
      int i = -1;
      for (;;)
      {
        String str;
        if (k == 0)
        {
          paramXmlPullParser.next();
          str = paramXmlPullParser.getName();
          if ((paramXmlPullParser.getEventType() != 2) || (!"position".equals(str))) {}
        }
        int m;
        try
        {
          m = Integer.parseInt(paramXmlPullParser.nextText());
          j = m;
        }
        catch (NumberFormatException localNumberFormatException2) {}
        if ((paramXmlPullParser.getEventType() == 2) && ("time".equals(str))) {}
        try
        {
          m = Integer.parseInt(paramXmlPullParser.nextText());
          i = m;
        }
        catch (NumberFormatException localNumberFormatException1) {}
        if ((paramXmlPullParser.getEventType() == 3) && ("queue-status".equals(str)))
        {
          k = 1;
          continue;
          return new QueueUpdate(j, i);
        }
      }
    }
  }
}


/* Location:           C:\Users\Administrator\Documents\ds\反编译\AndroidKiller_v1.3.1\projects\Jungledsq_ppc\ProjectSrc\smali\
 * Qualified Name:     org.jivesoftware.smackx.workgroup.packet.QueueUpdate
 * JD-Core Version:    0.7.0.1
 */