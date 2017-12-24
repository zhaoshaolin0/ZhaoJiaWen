package org.jivesoftware.smackx.provider;

import org.jivesoftware.smack.packet.PacketExtension;
import org.jivesoftware.smack.provider.PacketExtensionProvider;
import org.jivesoftware.smack.util.StringUtils;
import org.jivesoftware.smackx.packet.XHTMLExtension;
import org.xmlpull.v1.XmlPullParser;

public class XHTMLExtensionProvider
  implements PacketExtensionProvider
{
  public PacketExtension parseExtension(XmlPullParser paramXmlPullParser)
    throws Exception
  {
    XHTMLExtension localXHTMLExtension = new XHTMLExtension();
    int i = 0;
    StringBuilder localStringBuilder = new StringBuilder();
    int k = paramXmlPullParser.getDepth();
    int j = paramXmlPullParser.getDepth();
    String str = "";
    while (i == 0)
    {
      int m = paramXmlPullParser.next();
      if (m == 2)
      {
        if (paramXmlPullParser.getName().equals("body"))
        {
          localStringBuilder = new StringBuilder();
          j = paramXmlPullParser.getDepth();
        }
        str = paramXmlPullParser.getText();
        localStringBuilder.append(paramXmlPullParser.getText());
      }
      else if (m == 4)
      {
        if (localStringBuilder != null) {
          localStringBuilder.append(StringUtils.escapeForXML(paramXmlPullParser.getText()));
        }
      }
      else if (m == 3)
      {
        if ((paramXmlPullParser.getName().equals("body")) && (paramXmlPullParser.getDepth() <= j))
        {
          localStringBuilder.append(paramXmlPullParser.getText());
          localXHTMLExtension.addBody(localStringBuilder.toString());
        }
        else if ((paramXmlPullParser.getName().equals(localXHTMLExtension.getElementName())) && (paramXmlPullParser.getDepth() <= k))
        {
          i = 1;
        }
        else if (!str.equals(paramXmlPullParser.getText()))
        {
          localStringBuilder.append(paramXmlPullParser.getText());
        }
      }
    }
    return localXHTMLExtension;
  }
}


/* Location:           C:\Users\Administrator\Documents\ds\反编译\AndroidKiller_v1.3.1\projects\Jungledsq_ppc\ProjectSrc\smali\
 * Qualified Name:     org.jivesoftware.smackx.provider.XHTMLExtensionProvider
 * JD-Core Version:    0.7.0.1
 */