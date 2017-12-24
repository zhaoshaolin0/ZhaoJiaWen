package org.jivesoftware.smackx.packet;

import org.jivesoftware.smack.packet.PacketExtension;
import org.jivesoftware.smack.provider.PacketExtensionProvider;
import org.jivesoftware.smackx.ChatState;
import org.xmlpull.v1.XmlPullParser;

public class ChatStateExtension
  implements PacketExtension
{
  private ChatState state;
  
  public ChatStateExtension(ChatState paramChatState)
  {
    this.state = paramChatState;
  }
  
  public String getElementName()
  {
    return this.state.name();
  }
  
  public String getNamespace()
  {
    return "http://jabber.org/protocol/chatstates";
  }
  
  public String toXML()
  {
    return "<" + getElementName() + " xmlns=\"" + getNamespace() + "\" />";
  }
  
  public static class Provider
    implements PacketExtensionProvider
  {
    public PacketExtension parseExtension(XmlPullParser paramXmlPullParser)
      throws Exception
    {
      try
      {
        paramXmlPullParser = ChatState.valueOf(paramXmlPullParser.getName());
        return new ChatStateExtension(paramXmlPullParser);
      }
      catch (Exception paramXmlPullParser)
      {
        for (;;)
        {
          paramXmlPullParser = ChatState.active;
        }
      }
    }
  }
}


/* Location:           C:\Users\Administrator\Documents\ds\反编译\AndroidKiller_v1.3.1\projects\Jungledsq_ppc\ProjectSrc\smali\
 * Qualified Name:     org.jivesoftware.smackx.packet.ChatStateExtension
 * JD-Core Version:    0.7.0.1
 */