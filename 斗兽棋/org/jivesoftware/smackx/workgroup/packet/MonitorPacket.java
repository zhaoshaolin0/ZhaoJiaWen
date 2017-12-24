package org.jivesoftware.smackx.workgroup.packet;

import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.provider.IQProvider;
import org.xmlpull.v1.XmlPullParser;

public class MonitorPacket
  extends IQ
{
  public static final String ELEMENT_NAME = "monitor";
  public static final String NAMESPACE = "http://jivesoftware.com/protocol/workgroup";
  private boolean isMonitor;
  private String sessionID;
  
  public String getChildElementXML()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("<").append("monitor").append(" xmlns=");
    localStringBuilder.append('"');
    localStringBuilder.append("http://jivesoftware.com/protocol/workgroup");
    localStringBuilder.append('"');
    localStringBuilder.append(">");
    if (this.sessionID != null) {
      localStringBuilder.append("<makeOwner sessionID=\"" + this.sessionID + "\"></makeOwner>");
    }
    localStringBuilder.append("</").append("monitor").append("> ");
    return localStringBuilder.toString();
  }
  
  public String getElementName()
  {
    return "monitor";
  }
  
  public String getNamespace()
  {
    return "http://jivesoftware.com/protocol/workgroup";
  }
  
  public String getSessionID()
  {
    return this.sessionID;
  }
  
  public boolean isMonitor()
  {
    return this.isMonitor;
  }
  
  public void setMonitor(boolean paramBoolean)
  {
    this.isMonitor = paramBoolean;
  }
  
  public void setSessionID(String paramString)
  {
    this.sessionID = paramString;
  }
  
  public static class InternalProvider
    implements IQProvider
  {
    public IQ parseIQ(XmlPullParser paramXmlPullParser)
      throws Exception
    {
      if (paramXmlPullParser.getEventType() != 2) {
        throw new IllegalStateException("Parser not in proper position, or bad XML.");
      }
      MonitorPacket localMonitorPacket = new MonitorPacket();
      int i = 0;
      while (i == 0)
      {
        int j = paramXmlPullParser.next();
        if ((j == 2) && ("isMonitor".equals(paramXmlPullParser.getName())))
        {
          if ("false".equalsIgnoreCase(paramXmlPullParser.nextText())) {
            localMonitorPacket.setMonitor(false);
          } else {
            localMonitorPacket.setMonitor(true);
          }
        }
        else if ((j == 3) && ("monitor".equals(paramXmlPullParser.getName()))) {
          i = 1;
        }
      }
      return localMonitorPacket;
    }
  }
}


/* Location:           C:\Users\Administrator\Documents\ds\反编译\AndroidKiller_v1.3.1\projects\Jungledsq_ppc\ProjectSrc\smali\
 * Qualified Name:     org.jivesoftware.smackx.workgroup.packet.MonitorPacket
 * JD-Core Version:    0.7.0.1
 */