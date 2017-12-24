package org.jivesoftware.smackx.workgroup.packet;

import org.jivesoftware.smack.packet.PacketExtension;
import org.jivesoftware.smack.provider.PacketExtensionProvider;
import org.jivesoftware.smackx.workgroup.MetaData;
import org.jivesoftware.smackx.workgroup.util.MetaDataUtils;
import org.xmlpull.v1.XmlPullParser;

public class MetaDataProvider
  implements PacketExtensionProvider
{
  public PacketExtension parseExtension(XmlPullParser paramXmlPullParser)
    throws Exception
  {
    return new MetaData(MetaDataUtils.parseMetaData(paramXmlPullParser));
  }
}


/* Location:           C:\Users\Administrator\Documents\ds\反编译\AndroidKiller_v1.3.1\projects\Jungledsq_ppc\ProjectSrc\smali\
 * Qualified Name:     org.jivesoftware.smackx.workgroup.packet.MetaDataProvider
 * JD-Core Version:    0.7.0.1
 */