package org.jivesoftware.smackx.provider;

import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.provider.IQProvider;
import org.jivesoftware.smackx.packet.MUCAdmin;
import org.jivesoftware.smackx.packet.MUCAdmin.Item;
import org.xmlpull.v1.XmlPullParser;

public class MUCAdminProvider
  implements IQProvider
{
  private MUCAdmin.Item parseItem(XmlPullParser paramXmlPullParser)
    throws Exception
  {
    int i = 0;
    MUCAdmin.Item localItem = new MUCAdmin.Item(paramXmlPullParser.getAttributeValue("", "affiliation"), paramXmlPullParser.getAttributeValue("", "role"));
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
  
  public IQ parseIQ(XmlPullParser paramXmlPullParser)
    throws Exception
  {
    MUCAdmin localMUCAdmin = new MUCAdmin();
    int i = 0;
    while (i == 0)
    {
      int j = paramXmlPullParser.next();
      if (j == 2)
      {
        if (paramXmlPullParser.getName().equals("item")) {
          localMUCAdmin.addItem(parseItem(paramXmlPullParser));
        }
      }
      else if ((j == 3) && (paramXmlPullParser.getName().equals("query"))) {
        i = 1;
      }
    }
    return localMUCAdmin;
  }
}


/* Location:           C:\Users\Administrator\Documents\ds\反编译\AndroidKiller_v1.3.1\projects\Jungledsq_ppc\ProjectSrc\smali\
 * Qualified Name:     org.jivesoftware.smackx.provider.MUCAdminProvider
 * JD-Core Version:    0.7.0.1
 */