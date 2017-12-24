package org.jivesoftware.smackx.provider;

import org.jivesoftware.smack.packet.PacketExtension;
import org.jivesoftware.smack.provider.PacketExtensionProvider;
import org.jivesoftware.smackx.packet.MessageEvent;
import org.xmlpull.v1.XmlPullParser;

public class MessageEventProvider
  implements PacketExtensionProvider
{
  public PacketExtension parseExtension(XmlPullParser paramXmlPullParser)
    throws Exception
  {
    MessageEvent localMessageEvent = new MessageEvent();
    int i = 0;
    while (i == 0)
    {
      int j = paramXmlPullParser.next();
      if (j == 2)
      {
        if (paramXmlPullParser.getName().equals("id")) {
          localMessageEvent.setPacketID(paramXmlPullParser.nextText());
        }
        if (paramXmlPullParser.getName().equals("composing")) {
          localMessageEvent.setComposing(true);
        }
        if (paramXmlPullParser.getName().equals("delivered")) {
          localMessageEvent.setDelivered(true);
        }
        if (paramXmlPullParser.getName().equals("displayed")) {
          localMessageEvent.setDisplayed(true);
        }
        if (paramXmlPullParser.getName().equals("offline")) {
          localMessageEvent.setOffline(true);
        }
      }
      else if ((j == 3) && (paramXmlPullParser.getName().equals("x")))
      {
        i = 1;
      }
    }
    return localMessageEvent;
  }
}


/* Location:           C:\Users\Administrator\Documents\ds\反编译\AndroidKiller_v1.3.1\projects\Jungledsq_ppc\ProjectSrc\smali\
 * Qualified Name:     org.jivesoftware.smackx.provider.MessageEventProvider
 * JD-Core Version:    0.7.0.1
 */