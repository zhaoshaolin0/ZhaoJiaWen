package org.jivesoftware.smackx.provider;

import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.provider.IQProvider;
import org.jivesoftware.smackx.packet.Bytestream;
import org.jivesoftware.smackx.packet.Bytestream.Activate;
import org.jivesoftware.smackx.packet.Bytestream.Mode;
import org.jivesoftware.smackx.packet.Bytestream.StreamHost;
import org.jivesoftware.smackx.packet.Bytestream.StreamHostUsed;
import org.xmlpull.v1.XmlPullParser;

public class BytestreamsProvider
  implements IQProvider
{
  public IQ parseIQ(XmlPullParser paramXmlPullParser)
    throws Exception
  {
    int i = 0;
    Bytestream localBytestream = new Bytestream();
    String str4 = paramXmlPullParser.getAttributeValue("", "sid");
    String str5 = paramXmlPullParser.getAttributeValue("", "mode");
    String str1 = null;
    String str2 = null;
    String str3 = null;
    while (i == 0)
    {
      int j = paramXmlPullParser.next();
      String str6 = paramXmlPullParser.getName();
      if (j == 2)
      {
        if (str6.equals(Bytestream.StreamHost.ELEMENTNAME))
        {
          str1 = paramXmlPullParser.getAttributeValue("", "jid");
          str2 = paramXmlPullParser.getAttributeValue("", "host");
          str3 = paramXmlPullParser.getAttributeValue("", "port");
        }
        else if (str6.equals(Bytestream.StreamHostUsed.ELEMENTNAME))
        {
          localBytestream.setUsedHost(paramXmlPullParser.getAttributeValue("", "jid"));
        }
        else if (str6.equals(Bytestream.Activate.ELEMENTNAME))
        {
          localBytestream.setToActivate(paramXmlPullParser.getAttributeValue("", "jid"));
        }
      }
      else if (j == 3)
      {
        if (str6.equals("streamhost"))
        {
          if (str3 == null) {
            localBytestream.addStreamHost(str1, str2);
          }
          for (;;)
          {
            str1 = null;
            str2 = null;
            str3 = null;
            break;
            localBytestream.addStreamHost(str1, str2, Integer.parseInt(str3));
          }
        }
        if (str6.equals("query")) {
          i = 1;
        }
      }
    }
    localBytestream.setMode(Bytestream.Mode.fromName(str5));
    localBytestream.setSessionID(str4);
    return localBytestream;
  }
}


/* Location:           C:\Users\Administrator\Documents\ds\反编译\AndroidKiller_v1.3.1\projects\Jungledsq_ppc\ProjectSrc\smali\
 * Qualified Name:     org.jivesoftware.smackx.provider.BytestreamsProvider
 * JD-Core Version:    0.7.0.1
 */