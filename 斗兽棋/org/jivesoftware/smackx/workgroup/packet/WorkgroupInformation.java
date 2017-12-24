package org.jivesoftware.smackx.workgroup.packet;

import org.jivesoftware.smack.packet.PacketExtension;
import org.jivesoftware.smack.provider.PacketExtensionProvider;
import org.xmlpull.v1.XmlPullParser;

public class WorkgroupInformation
  implements PacketExtension
{
  public static final String ELEMENT_NAME = "workgroup";
  public static final String NAMESPACE = "http://jabber.org/protocol/workgroup";
  private String workgroupJID;
  
  public WorkgroupInformation(String paramString)
  {
    this.workgroupJID = paramString;
  }
  
  public String getElementName()
  {
    return "workgroup";
  }
  
  public String getNamespace()
  {
    return "http://jabber.org/protocol/workgroup";
  }
  
  public String getWorkgroupJID()
  {
    return this.workgroupJID;
  }
  
  public String toXML()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append('<').append("workgroup");
    localStringBuilder.append(" jid=\"").append(getWorkgroupJID()).append("\"");
    localStringBuilder.append(" xmlns=\"").append("http://jabber.org/protocol/workgroup").append("\" />");
    return localStringBuilder.toString();
  }
  
  public static class Provider
    implements PacketExtensionProvider
  {
    public PacketExtension parseExtension(XmlPullParser paramXmlPullParser)
      throws Exception
    {
      String str = paramXmlPullParser.getAttributeValue("", "jid");
      paramXmlPullParser.next();
      return new WorkgroupInformation(str);
    }
  }
}


/* Location:           C:\Users\Administrator\Documents\ds\反编译\AndroidKiller_v1.3.1\projects\Jungledsq_ppc\ProjectSrc\smali\
 * Qualified Name:     org.jivesoftware.smackx.workgroup.packet.WorkgroupInformation
 * JD-Core Version:    0.7.0.1
 */