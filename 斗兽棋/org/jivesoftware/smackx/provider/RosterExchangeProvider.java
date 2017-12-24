package org.jivesoftware.smackx.provider;

import java.util.ArrayList;
import org.jivesoftware.smack.packet.PacketExtension;
import org.jivesoftware.smack.provider.PacketExtensionProvider;
import org.jivesoftware.smackx.RemoteRosterEntry;
import org.jivesoftware.smackx.packet.RosterExchange;
import org.xmlpull.v1.XmlPullParser;

public class RosterExchangeProvider
  implements PacketExtensionProvider
{
  public PacketExtension parseExtension(XmlPullParser paramXmlPullParser)
    throws Exception
  {
    RosterExchange localRosterExchange = new RosterExchange();
    int i = 0;
    Object localObject3 = "";
    Object localObject1 = "";
    Object localObject5 = new ArrayList();
    while (i == 0)
    {
      int j = paramXmlPullParser.next();
      if (j == 2)
      {
        Object localObject6 = localObject5;
        Object localObject4 = localObject3;
        Object localObject2 = localObject1;
        if (paramXmlPullParser.getName().equals("item"))
        {
          localObject6 = new ArrayList();
          localObject4 = paramXmlPullParser.getAttributeValue("", "jid");
          localObject2 = paramXmlPullParser.getAttributeValue("", "name");
        }
        localObject5 = localObject6;
        localObject3 = localObject4;
        localObject1 = localObject2;
        if (paramXmlPullParser.getName().equals("group"))
        {
          ((ArrayList)localObject6).add(paramXmlPullParser.nextText());
          localObject5 = localObject6;
          localObject3 = localObject4;
          localObject1 = localObject2;
        }
      }
      else if (j == 3)
      {
        if (paramXmlPullParser.getName().equals("item")) {
          localRosterExchange.addRosterEntry(new RemoteRosterEntry((String)localObject3, (String)localObject1, (String[])((ArrayList)localObject5).toArray(new String[((ArrayList)localObject5).size()])));
        }
        if (paramXmlPullParser.getName().equals("x")) {
          i = 1;
        }
      }
    }
    return localRosterExchange;
  }
}


/* Location:           C:\Users\Administrator\Documents\ds\反编译\AndroidKiller_v1.3.1\projects\Jungledsq_ppc\ProjectSrc\smali\
 * Qualified Name:     org.jivesoftware.smackx.provider.RosterExchangeProvider
 * JD-Core Version:    0.7.0.1
 */