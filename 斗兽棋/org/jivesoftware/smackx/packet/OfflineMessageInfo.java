package org.jivesoftware.smackx.packet;

import org.jivesoftware.smack.packet.PacketExtension;
import org.jivesoftware.smack.provider.PacketExtensionProvider;
import org.xmlpull.v1.XmlPullParser;

public class OfflineMessageInfo
  implements PacketExtension
{
  private String node = null;
  
  public String getElementName()
  {
    return "offline";
  }
  
  public String getNamespace()
  {
    return "http://jabber.org/protocol/offline";
  }
  
  public String getNode()
  {
    return this.node;
  }
  
  public void setNode(String paramString)
  {
    this.node = paramString;
  }
  
  public String toXML()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("<").append(getElementName()).append(" xmlns=\"").append(getNamespace()).append("\">");
    if (getNode() != null) {
      localStringBuilder.append("<item node=\"").append(getNode()).append("\"/>");
    }
    localStringBuilder.append("</").append(getElementName()).append(">");
    return localStringBuilder.toString();
  }
  
  public static class Provider
    implements PacketExtensionProvider
  {
    public PacketExtension parseExtension(XmlPullParser paramXmlPullParser)
      throws Exception
    {
      OfflineMessageInfo localOfflineMessageInfo = new OfflineMessageInfo();
      int i = 0;
      while (i == 0)
      {
        int j = paramXmlPullParser.next();
        if (j == 2)
        {
          if (paramXmlPullParser.getName().equals("item")) {
            localOfflineMessageInfo.setNode(paramXmlPullParser.getAttributeValue("", "node"));
          }
        }
        else if ((j == 3) && (paramXmlPullParser.getName().equals("offline"))) {
          i = 1;
        }
      }
      return localOfflineMessageInfo;
    }
  }
}


/* Location:           C:\Users\Administrator\Documents\ds\反编译\AndroidKiller_v1.3.1\projects\Jungledsq_ppc\ProjectSrc\smali\
 * Qualified Name:     org.jivesoftware.smackx.packet.OfflineMessageInfo
 * JD-Core Version:    0.7.0.1
 */