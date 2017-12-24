package org.jivesoftware.smackx.workgroup.packet;

import java.util.ArrayList;
import java.util.List;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.provider.IQProvider;
import org.jivesoftware.smack.util.PacketParserUtils;
import org.xmlpull.v1.XmlPullParser;

public class TranscriptProvider
  implements IQProvider
{
  public IQ parseIQ(XmlPullParser paramXmlPullParser)
    throws Exception
  {
    String str = paramXmlPullParser.getAttributeValue("", "sessionID");
    ArrayList localArrayList = new ArrayList();
    int i = 0;
    while (i == 0)
    {
      int j = paramXmlPullParser.next();
      if (j == 2)
      {
        if (paramXmlPullParser.getName().equals("message")) {
          localArrayList.add(PacketParserUtils.parseMessage(paramXmlPullParser));
        } else if (paramXmlPullParser.getName().equals("presence")) {
          localArrayList.add(PacketParserUtils.parsePresence(paramXmlPullParser));
        }
      }
      else if ((j == 3) && (paramXmlPullParser.getName().equals("transcript"))) {
        i = 1;
      }
    }
    return new Transcript(str, localArrayList);
  }
}


/* Location:           C:\Users\Administrator\Documents\ds\反编译\AndroidKiller_v1.3.1\projects\Jungledsq_ppc\ProjectSrc\smali\
 * Qualified Name:     org.jivesoftware.smackx.workgroup.packet.TranscriptProvider
 * JD-Core Version:    0.7.0.1
 */