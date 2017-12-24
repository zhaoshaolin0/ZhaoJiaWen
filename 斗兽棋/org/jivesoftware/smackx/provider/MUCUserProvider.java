package org.jivesoftware.smackx.provider;

import org.jivesoftware.smack.packet.PacketExtension;
import org.jivesoftware.smack.provider.PacketExtensionProvider;
import org.jivesoftware.smackx.packet.MUCUser;
import org.jivesoftware.smackx.packet.MUCUser.Decline;
import org.jivesoftware.smackx.packet.MUCUser.Destroy;
import org.jivesoftware.smackx.packet.MUCUser.Invite;
import org.jivesoftware.smackx.packet.MUCUser.Item;
import org.jivesoftware.smackx.packet.MUCUser.Status;
import org.xmlpull.v1.XmlPullParser;

public class MUCUserProvider
  implements PacketExtensionProvider
{
  private MUCUser.Decline parseDecline(XmlPullParser paramXmlPullParser)
    throws Exception
  {
    int i = 0;
    MUCUser.Decline localDecline = new MUCUser.Decline();
    localDecline.setFrom(paramXmlPullParser.getAttributeValue("", "from"));
    localDecline.setTo(paramXmlPullParser.getAttributeValue("", "to"));
    while (i == 0)
    {
      int j = paramXmlPullParser.next();
      if (j == 2)
      {
        if (paramXmlPullParser.getName().equals("reason")) {
          localDecline.setReason(paramXmlPullParser.nextText());
        }
      }
      else if ((j == 3) && (paramXmlPullParser.getName().equals("decline"))) {
        i = 1;
      }
    }
    return localDecline;
  }
  
  private MUCUser.Destroy parseDestroy(XmlPullParser paramXmlPullParser)
    throws Exception
  {
    int i = 0;
    MUCUser.Destroy localDestroy = new MUCUser.Destroy();
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
  
  private MUCUser.Invite parseInvite(XmlPullParser paramXmlPullParser)
    throws Exception
  {
    int i = 0;
    MUCUser.Invite localInvite = new MUCUser.Invite();
    localInvite.setFrom(paramXmlPullParser.getAttributeValue("", "from"));
    localInvite.setTo(paramXmlPullParser.getAttributeValue("", "to"));
    while (i == 0)
    {
      int j = paramXmlPullParser.next();
      if (j == 2)
      {
        if (paramXmlPullParser.getName().equals("reason")) {
          localInvite.setReason(paramXmlPullParser.nextText());
        }
      }
      else if ((j == 3) && (paramXmlPullParser.getName().equals("invite"))) {
        i = 1;
      }
    }
    return localInvite;
  }
  
  private MUCUser.Item parseItem(XmlPullParser paramXmlPullParser)
    throws Exception
  {
    int i = 0;
    MUCUser.Item localItem = new MUCUser.Item(paramXmlPullParser.getAttributeValue("", "affiliation"), paramXmlPullParser.getAttributeValue("", "role"));
    localItem.setNick(paramXmlPullParser.getAttributeValue("", "nick"));
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
  
  public PacketExtension parseExtension(XmlPullParser paramXmlPullParser)
    throws Exception
  {
    MUCUser localMUCUser = new MUCUser();
    int i = 0;
    while (i == 0)
    {
      int j = paramXmlPullParser.next();
      if (j == 2)
      {
        if (paramXmlPullParser.getName().equals("invite")) {
          localMUCUser.setInvite(parseInvite(paramXmlPullParser));
        }
        if (paramXmlPullParser.getName().equals("item")) {
          localMUCUser.setItem(parseItem(paramXmlPullParser));
        }
        if (paramXmlPullParser.getName().equals("password")) {
          localMUCUser.setPassword(paramXmlPullParser.nextText());
        }
        if (paramXmlPullParser.getName().equals("status")) {
          localMUCUser.setStatus(new MUCUser.Status(paramXmlPullParser.getAttributeValue("", "code")));
        }
        if (paramXmlPullParser.getName().equals("decline")) {
          localMUCUser.setDecline(parseDecline(paramXmlPullParser));
        }
        if (paramXmlPullParser.getName().equals("destroy")) {
          localMUCUser.setDestroy(parseDestroy(paramXmlPullParser));
        }
      }
      else if ((j == 3) && (paramXmlPullParser.getName().equals("x")))
      {
        i = 1;
      }
    }
    return localMUCUser;
  }
}


/* Location:           C:\Users\Administrator\Documents\ds\反编译\AndroidKiller_v1.3.1\projects\Jungledsq_ppc\ProjectSrc\smali\
 * Qualified Name:     org.jivesoftware.smackx.provider.MUCUserProvider
 * JD-Core Version:    0.7.0.1
 */