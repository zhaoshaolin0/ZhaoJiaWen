package org.jivesoftware.smackx.provider;

import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.provider.IQProvider;
import org.jivesoftware.smack.util.PacketParserUtils;
import org.jivesoftware.smackx.packet.MUCOwner;
import org.jivesoftware.smackx.packet.MUCOwner.Destroy;
import org.jivesoftware.smackx.packet.MUCOwner.Item;
import org.xmlpull.v1.XmlPullParser;

public class MUCOwnerProvider
  implements IQProvider
{
  private MUCOwner.Destroy parseDestroy(XmlPullParser paramXmlPullParser)
    throws Exception
  {
    int i = 0;
    MUCOwner.Destroy localDestroy = new MUCOwner.Destroy();
    localDestroy.setJid(paramXmlPullParser.getAttributeValue("", "jid"));
    while (i == 0)
    {
      int j = paramXmlPullParser.next();
      if (j == 2)
      {
        if (paramXmlPullParser.getName().equals("reason")) {
          localDestroy.setReason(paramXmlPullParser.nextText());
        }
      }
      else if ((j == 3) && (paramXmlPullParser.getName().equals("destroy"))) {
        i = 1;
      }
    }
    return localDestroy;
  }
  
  private MUCOwner.Item parseItem(XmlPullParser paramXmlPullParser)
    throws Exception
  {
    int i = 0;
    MUCOwner.Item localItem = new MUCOwner.Item(paramXmlPullParser.getAttributeValue("", "affiliation"));
    localItem.setNick(paramXmlPullParser.getAttributeValue("", "nick"));
    localItem.setRole(paramXmlPullParser.getAttributeValue("", "role"));
    localItem.setJid(paramXmlPullParser.getAttributeValue("", "jid"));
    while (i == 0)
    {
      int j = paramXmlPullParser.next();
      if (j == 2)
      {
        if (paramXmlPullParser.getName().equals("actor")) {
          localItem.setActor(paramXmlPullParser.getAttributeValue("", "jid"));
        }
        if (paramXmlPullParser.getName().equals("reason")) {
          localItem.setReason(paramXmlPullParser.nextText());
        }
      }
      else if ((j == 3) && (paramXmlPullParser.getName().equals("item")))
      {
        i = 1;
      }
    }
    return localItem;
  }
  
  public IQ parseIQ(XmlPullParser paramXmlPullParser)
    throws Exception
  {
    MUCOwner localMUCOwner = new MUCOwner();
    int i = 0;
    while (i == 0)
    {
      int j = paramXmlPullParser.next();
      if (j == 2)
      {
        if (paramXmlPullParser.getName().equals("item")) {
          localMUCOwner.addItem(parseItem(paramXmlPullParser));
        } else if (paramXmlPullParser.getName().equals("destroy")) {
          localMUCOwner.setDestroy(parseDestroy(paramXmlPullParser));
        } else {
          localMUCOwner.addExtension(PacketParserUtils.parsePacketExtension(paramXmlPullParser.getName(), paramXmlPullParser.getNamespace(), paramXmlPullParser));
        }
      }
      else if ((j == 3) && (paramXmlPullParser.getName().equals("query"))) {
        i = 1;
      }
    }
    return localMUCOwner;
  }
}


/* Location:           C:\Users\Administrator\Documents\ds\反编译\AndroidKiller_v1.3.1\projects\Jungledsq_ppc\ProjectSrc\smali\
 * Qualified Name:     org.jivesoftware.smackx.provider.MUCOwnerProvider
 * JD-Core Version:    0.7.0.1
 */