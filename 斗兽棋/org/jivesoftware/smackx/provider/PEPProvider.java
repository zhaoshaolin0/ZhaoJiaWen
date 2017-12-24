package org.jivesoftware.smackx.provider;

import java.util.HashMap;
import java.util.Map;
import org.jivesoftware.smack.packet.PacketExtension;
import org.jivesoftware.smack.provider.PacketExtensionProvider;
import org.xmlpull.v1.XmlPullParser;

public class PEPProvider
  implements PacketExtensionProvider
{
  Map<String, PacketExtensionProvider> nodeParsers = new HashMap();
  PacketExtension pepItem;
  
  public PacketExtension parseExtension(XmlPullParser paramXmlPullParser)
    throws Exception
  {
    int i = 0;
    while (i == 0)
    {
      int j = paramXmlPullParser.next();
      if (j == 2)
      {
        if ((!paramXmlPullParser.getName().equals("event")) && (paramXmlPullParser.getName().equals("items")))
        {
          Object localObject = paramXmlPullParser.getAttributeValue("", "node");
          localObject = (PacketExtensionProvider)this.nodeParsers.get(localObject);
          if (localObject != null) {
            this.pepItem = ((PacketExtensionProvider)localObject).parseExtension(paramXmlPullParser);
          }
        }
      }
      else if ((j == 3) && (paramXmlPullParser.getName().equals("event"))) {
        i = 1;
      }
    }
    return this.pepItem;
  }
  
  public void registerPEPParserExtension(String paramString, PacketExtensionProvider paramPacketExtensionProvider)
  {
    this.nodeParsers.put(paramString, paramPacketExtensionProvider);
  }
}


/* Location:           C:\Users\Administrator\Documents\ds\反编译\AndroidKiller_v1.3.1\projects\Jungledsq_ppc\ProjectSrc\smali\
 * Qualified Name:     org.jivesoftware.smackx.provider.PEPProvider
 * JD-Core Version:    0.7.0.1
 */