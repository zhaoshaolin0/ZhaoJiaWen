package org.jivesoftware.smackx.provider;

import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.provider.IQProvider;
import org.jivesoftware.smackx.packet.DiscoverItems;
import org.jivesoftware.smackx.packet.DiscoverItems.Item;
import org.xmlpull.v1.XmlPullParser;

public class DiscoverItemsProvider
  implements IQProvider
{
  public IQ parseIQ(XmlPullParser paramXmlPullParser)
    throws Exception
  {
    DiscoverItems localDiscoverItems = new DiscoverItems();
    int i = 0;
    String str2 = "";
    String str3 = "";
    String str1 = "";
    String str4 = "";
    localDiscoverItems.setNode(paramXmlPullParser.getAttributeValue("", "node"));
    while (i == 0)
    {
      int j = paramXmlPullParser.next();
      if ((j == 2) && ("item".equals(paramXmlPullParser.getName())))
      {
        str2 = paramXmlPullParser.getAttributeValue("", "jid");
        str3 = paramXmlPullParser.getAttributeValue("", "name");
        str4 = paramXmlPullParser.getAttributeValue("", "node");
        str1 = paramXmlPullParser.getAttributeValue("", "action");
      }
      else if ((j == 3) && ("item".equals(paramXmlPullParser.getName())))
      {
        DiscoverItems.Item localItem = new DiscoverItems.Item(str2);
        localItem.setName(str3);
        localItem.setNode(str4);
        localItem.setAction(str1);
        localDiscoverItems.addItem(localItem);
      }
      else if ((j == 3) && ("query".equals(paramXmlPullParser.getName())))
      {
        i = 1;
      }
    }
    return localDiscoverItems;
  }
}


/* Location:           C:\Users\Administrator\Documents\ds\反编译\AndroidKiller_v1.3.1\projects\Jungledsq_ppc\ProjectSrc\smali\
 * Qualified Name:     org.jivesoftware.smackx.provider.DiscoverItemsProvider
 * JD-Core Version:    0.7.0.1
 */