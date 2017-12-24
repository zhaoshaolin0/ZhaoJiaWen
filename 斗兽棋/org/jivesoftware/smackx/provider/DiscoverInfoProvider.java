package org.jivesoftware.smackx.provider;

import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.provider.IQProvider;
import org.jivesoftware.smack.util.PacketParserUtils;
import org.jivesoftware.smackx.packet.DiscoverInfo;
import org.jivesoftware.smackx.packet.DiscoverInfo.Identity;
import org.xmlpull.v1.XmlPullParser;

public class DiscoverInfoProvider
  implements IQProvider
{
  public IQ parseIQ(XmlPullParser paramXmlPullParser)
    throws Exception
  {
    DiscoverInfo localDiscoverInfo = new DiscoverInfo();
    int i = 0;
    String str2 = "";
    String str3 = "";
    String str4 = "";
    String str1 = "";
    localDiscoverInfo.setNode(paramXmlPullParser.getAttributeValue("", "node"));
    while (i == 0)
    {
      int j = paramXmlPullParser.next();
      if (j == 2)
      {
        if (paramXmlPullParser.getName().equals("identity"))
        {
          str2 = paramXmlPullParser.getAttributeValue("", "category");
          str3 = paramXmlPullParser.getAttributeValue("", "name");
          str4 = paramXmlPullParser.getAttributeValue("", "type");
        }
        else if (paramXmlPullParser.getName().equals("feature"))
        {
          str1 = paramXmlPullParser.getAttributeValue("", "var");
        }
        else
        {
          localDiscoverInfo.addExtension(PacketParserUtils.parsePacketExtension(paramXmlPullParser.getName(), paramXmlPullParser.getNamespace(), paramXmlPullParser));
        }
      }
      else if (j == 3)
      {
        if (paramXmlPullParser.getName().equals("identity"))
        {
          DiscoverInfo.Identity localIdentity = new DiscoverInfo.Identity(str2, str3);
          localIdentity.setType(str4);
          localDiscoverInfo.addIdentity(localIdentity);
        }
        if (paramXmlPullParser.getName().equals("feature")) {
          localDiscoverInfo.addFeature(str1);
        }
        if (paramXmlPullParser.getName().equals("query")) {
          i = 1;
        }
      }
    }
    return localDiscoverInfo;
  }
}


/* Location:           C:\Users\Administrator\Documents\ds\反编译\AndroidKiller_v1.3.1\projects\Jungledsq_ppc\ProjectSrc\smali\
 * Qualified Name:     org.jivesoftware.smackx.provider.DiscoverInfoProvider
 * JD-Core Version:    0.7.0.1
 */