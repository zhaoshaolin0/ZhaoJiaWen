package org.jivesoftware.smackx;

import org.jivesoftware.smack.packet.PacketExtension;
import org.jivesoftware.smack.provider.PacketExtensionProvider;
import org.xmlpull.v1.XmlPullParser;

public class GroupChatInvitation
  implements PacketExtension
{
  public static final String ELEMENT_NAME = "x";
  public static final String NAMESPACE = "jabber:x:conference";
  private String roomAddress;
  
  public GroupChatInvitation(String paramString)
  {
    this.roomAddress = paramString;
  }
  
  public String getElementName()
  {
    return "x";
  }
  
  public String getNamespace()
  {
    return "jabber:x:conference";
  }
  
  public String getRoomAddress()
  {
    return this.roomAddress;
  }
  
  public String toXML()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("<x xmlns=\"jabber:x:conference\" jid=\"").append(this.roomAddress).append("\"/>");
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
      return new GroupChatInvitation(str);
    }
  }
}


/* Location:           C:\Users\Administrator\Documents\ds\反编译\AndroidKiller_v1.3.1\projects\Jungledsq_ppc\ProjectSrc\smali\
 * Qualified Name:     org.jivesoftware.smackx.GroupChatInvitation
 * JD-Core Version:    0.7.0.1
 */