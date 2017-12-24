package org.jivesoftware.smackx.provider;

import org.jivesoftware.smack.packet.PacketExtension;
import org.jivesoftware.smack.provider.PacketExtensionProvider;
import org.jivesoftware.smackx.packet.MultipleAddresses;
import org.xmlpull.v1.XmlPullParser;

public class MultipleAddressesProvider
  implements PacketExtensionProvider
{
  public PacketExtension parseExtension(XmlPullParser paramXmlPullParser)
    throws Exception
  {
    int i = 0;
    MultipleAddresses localMultipleAddresses = new MultipleAddresses();
    while (i == 0)
    {
      int j = paramXmlPullParser.next();
      if (j == 2)
      {
        if (paramXmlPullParser.getName().equals("address")) {
          localMultipleAddresses.addAddress(paramXmlPullParser.getAttributeValue("", "type"), paramXmlPullParser.getAttributeValue("", "jid"), paramXmlPullParser.getAttributeValue("", "node"), paramXmlPullParser.getAttributeValue("", "desc"), "true".equals(paramXmlPullParser.getAttributeValue("", "delivered")), paramXmlPullParser.getAttributeValue("", "uri"));
        }
      }
      else if ((j == 3) && (paramXmlPullParser.getName().equals(localMultipleAddresses.getElementName()))) {
        i = 1;
      }
    }
    return localMultipleAddresses;
  }
}


/* Location:           C:\Users\Administrator\Documents\ds\反编译\AndroidKiller_v1.3.1\projects\Jungledsq_ppc\ProjectSrc\smali\
 * Qualified Name:     org.jivesoftware.smackx.provider.MultipleAddressesProvider
 * JD-Core Version:    0.7.0.1
 */