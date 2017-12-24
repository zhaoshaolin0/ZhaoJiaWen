package org.jivesoftware.smackx.provider;

import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.packet.PacketExtension;
import org.jivesoftware.smack.provider.IQProvider;
import org.jivesoftware.smack.provider.PacketExtensionProvider;
import org.jivesoftware.smackx.packet.IBBExtensions.Close;
import org.jivesoftware.smackx.packet.IBBExtensions.Data;
import org.jivesoftware.smackx.packet.IBBExtensions.Open;
import org.xmlpull.v1.XmlPullParser;

public class IBBProviders
{
  public static class Close
    implements IQProvider
  {
    public IQ parseIQ(XmlPullParser paramXmlPullParser)
      throws Exception
    {
      return new IBBExtensions.Close(paramXmlPullParser.getAttributeValue("", "sid"));
    }
  }
  
  public static class Data
    implements PacketExtensionProvider
  {
    public PacketExtension parseExtension(XmlPullParser paramXmlPullParser)
      throws Exception
    {
      return new IBBExtensions.Data(paramXmlPullParser.getAttributeValue("", "sid"), Long.parseLong(paramXmlPullParser.getAttributeValue("", "seq")), paramXmlPullParser.nextText());
    }
  }
  
  public static class Open
    implements IQProvider
  {
    public IQ parseIQ(XmlPullParser paramXmlPullParser)
      throws Exception
    {
      return new IBBExtensions.Open(paramXmlPullParser.getAttributeValue("", "sid"), Integer.parseInt(paramXmlPullParser.getAttributeValue("", "block-size")));
    }
  }
}


/* Location:           C:\Users\Administrator\Documents\ds\反编译\AndroidKiller_v1.3.1\projects\Jungledsq_ppc\ProjectSrc\smali\
 * Qualified Name:     org.jivesoftware.smackx.provider.IBBProviders
 * JD-Core Version:    0.7.0.1
 */