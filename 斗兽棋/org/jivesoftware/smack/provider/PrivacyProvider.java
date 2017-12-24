package org.jivesoftware.smack.provider;

import java.util.ArrayList;
import org.jivesoftware.smack.packet.DefaultPacketExtension;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.packet.Privacy;
import org.jivesoftware.smack.packet.PrivacyItem;
import org.xmlpull.v1.XmlPullParser;

public class PrivacyProvider
  implements IQProvider
{
  public IQ parseIQ(XmlPullParser paramXmlPullParser)
    throws Exception
  {
    Privacy localPrivacy = new Privacy();
    localPrivacy.addExtension(new DefaultPacketExtension(paramXmlPullParser.getName(), paramXmlPullParser.getNamespace()));
    int i = 0;
    while (i == 0)
    {
      int j = paramXmlPullParser.next();
      if (j == 2)
      {
        String str;
        if (paramXmlPullParser.getName().equals("active"))
        {
          str = paramXmlPullParser.getAttributeValue("", "name");
          if (str == null) {
            localPrivacy.setDeclineActiveList(true);
          } else {
            localPrivacy.setActiveName(str);
          }
        }
        else if (paramXmlPullParser.getName().equals("default"))
        {
          str = paramXmlPullParser.getAttributeValue("", "name");
          if (str == null) {
            localPrivacy.setDeclineDefaultList(true);
          } else {
            localPrivacy.setDefaultName(str);
          }
        }
        else if (paramXmlPullParser.getName().equals("list"))
        {
          parseList(paramXmlPullParser, localPrivacy);
        }
      }
      else if ((j == 3) && (paramXmlPullParser.getName().equals("query")))
      {
        i = 1;
      }
    }
    return localPrivacy;
  }
  
  public PrivacyItem parseItem(XmlPullParser paramXmlPullParser)
    throws Exception
  {
    int i = 0;
    Object localObject = paramXmlPullParser.getAttributeValue("", "action");
    String str1 = paramXmlPullParser.getAttributeValue("", "order");
    String str2 = paramXmlPullParser.getAttributeValue("", "type");
    boolean bool = true;
    if ("allow".equalsIgnoreCase((String)localObject))
    {
      bool = true;
      localObject = new PrivacyItem(str2, bool, Integer.parseInt(str1));
      ((PrivacyItem)localObject).setValue(paramXmlPullParser.getAttributeValue("", "value"));
    }
    for (;;)
    {
      if (i != 0) {
        break label226;
      }
      int j = paramXmlPullParser.next();
      if (j == 2)
      {
        if (paramXmlPullParser.getName().equals("iq")) {
          ((PrivacyItem)localObject).setFilterIQ(true);
        }
        if (paramXmlPullParser.getName().equals("message")) {
          ((PrivacyItem)localObject).setFilterMessage(true);
        }
        if (paramXmlPullParser.getName().equals("presence-in")) {
          ((PrivacyItem)localObject).setFilterPresence_in(true);
        }
        if (!paramXmlPullParser.getName().equals("presence-out")) {
          continue;
        }
        ((PrivacyItem)localObject).setFilterPresence_out(true);
        continue;
        if (!"deny".equalsIgnoreCase((String)localObject)) {
          break;
        }
        bool = false;
        break;
      }
      if ((j == 3) && (paramXmlPullParser.getName().equals("item"))) {
        i = 1;
      }
    }
    label226:
    return localObject;
  }
  
  public void parseList(XmlPullParser paramXmlPullParser, Privacy paramPrivacy)
    throws Exception
  {
    int i = 0;
    String str = paramXmlPullParser.getAttributeValue("", "name");
    ArrayList localArrayList = new ArrayList();
    while (i == 0)
    {
      int j = paramXmlPullParser.next();
      if (j == 2)
      {
        if (paramXmlPullParser.getName().equals("item")) {
          localArrayList.add(parseItem(paramXmlPullParser));
        }
      }
      else if ((j == 3) && (paramXmlPullParser.getName().equals("list"))) {
        i = 1;
      }
    }
    paramPrivacy.setPrivacyList(str, localArrayList);
  }
}


/* Location:           C:\Users\Administrator\Documents\ds\反编译\AndroidKiller_v1.3.1\projects\Jungledsq_ppc\ProjectSrc\smali\
 * Qualified Name:     org.jivesoftware.smack.provider.PrivacyProvider
 * JD-Core Version:    0.7.0.1
 */