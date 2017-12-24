package org.jivesoftware.smackx.workgroup.packet;

import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.provider.IQProvider;
import org.jivesoftware.smack.util.PacketParserUtils;
import org.xmlpull.v1.XmlPullParser;

public class TranscriptSearch
  extends IQ
{
  public static final String ELEMENT_NAME = "transcript-search";
  public static final String NAMESPACE = "http://jivesoftware.com/protocol/workgroup";
  
  public String getChildElementXML()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("<").append("transcript-search").append(" xmlns=\"").append("http://jivesoftware.com/protocol/workgroup").append("\">");
    localStringBuilder.append(getExtensionsXML());
    localStringBuilder.append("</").append("transcript-search").append("> ");
    return localStringBuilder.toString();
  }
  
  public static class Provider
    implements IQProvider
  {
    public IQ parseIQ(XmlPullParser paramXmlPullParser)
      throws Exception
    {
      TranscriptSearch localTranscriptSearch = new TranscriptSearch();
      int i = 0;
      while (i == 0)
      {
        int j = paramXmlPullParser.next();
        if (j == 2) {
          localTranscriptSearch.addExtension(PacketParserUtils.parsePacketExtension(paramXmlPullParser.getName(), paramXmlPullParser.getNamespace(), paramXmlPullParser));
        } else if ((j == 3) && (paramXmlPullParser.getName().equals("transcript-search"))) {
          i = 1;
        }
      }
      return localTranscriptSearch;
    }
  }
}


/* Location:           C:\Users\Administrator\Documents\ds\反编译\AndroidKiller_v1.3.1\projects\Jungledsq_ppc\ProjectSrc\smali\
 * Qualified Name:     org.jivesoftware.smackx.workgroup.packet.TranscriptSearch
 * JD-Core Version:    0.7.0.1
 */