package org.jivesoftware.smackx.workgroup.packet;

import org.jivesoftware.smack.packet.PacketExtension;
import org.jivesoftware.smack.provider.PacketExtensionProvider;
import org.xmlpull.v1.XmlPullParser;

public class SessionID
  implements PacketExtension
{
  public static final String ELEMENT_NAME = "session";
  public static final String NAMESPACE = "http://jivesoftware.com/protocol/workgroup";
  private String sessionID;
  
  public SessionID(String paramString)
  {
    this.sessionID = paramString;
  }
  
  public String getElementName()
  {
    return "session";
  }
  
  public String getNamespace()
  {
    return "http://jivesoftware.com/protocol/workgroup";
  }
  
  public String getSessionID()
  {
    return this.sessionID;
  }
  
  public String toXML()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("<").append("session").append(" xmlns=\"").append("http://jivesoftware.com/protocol/workgroup").append("\" ");
    localStringBuilder.append("id=\"").append(getSessionID());
    localStringBuilder.append("\"/>");
    return localStringBuilder.toString();
  }
  
  public static class Provider
    implements PacketExtensionProvider
  {
    public PacketExtension parseExtension(XmlPullParser paramXmlPullParser)
      throws Exception
    {
      String str = paramXmlPullParser.getAttributeValue("", "id");
      paramXmlPullParser.next();
      return new SessionID(str);
    }
  }
}


/* Location:           C:\Users\Administrator\Documents\ds\反编译\AndroidKiller_v1.3.1\projects\Jungledsq_ppc\ProjectSrc\smali\
 * Qualified Name:     org.jivesoftware.smackx.workgroup.packet.SessionID
 * JD-Core Version:    0.7.0.1
 */