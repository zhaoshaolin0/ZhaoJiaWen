package org.jivesoftware.smackx.workgroup.ext.history;

import java.util.HashMap;
import java.util.Map;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.provider.IQProvider;
import org.jivesoftware.smackx.workgroup.util.MetaDataUtils;
import org.xmlpull.v1.XmlPullParser;

public class ChatMetadata
  extends IQ
{
  public static final String ELEMENT_NAME = "chat-metadata";
  public static final String NAMESPACE = "http://jivesoftware.com/protocol/workgroup";
  private Map map = new HashMap();
  private String sessionID;
  
  public String getChildElementXML()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("<").append("chat-metadata").append(" xmlns=\"").append("http://jivesoftware.com/protocol/workgroup").append("\">");
    localStringBuilder.append("<sessionID>").append(getSessionID()).append("</sessionID>");
    localStringBuilder.append("</").append("chat-metadata").append("> ");
    return localStringBuilder.toString();
  }
  
  public Map getMetadata()
  {
    return this.map;
  }
  
  public String getSessionID()
  {
    return this.sessionID;
  }
  
  public void setMetadata(Map paramMap)
  {
    this.map = paramMap;
  }
  
  public void setSessionID(String paramString)
  {
    this.sessionID = paramString;
  }
  
  public static class Provider
    implements IQProvider
  {
    public IQ parseIQ(XmlPullParser paramXmlPullParser)
      throws Exception
    {
      ChatMetadata localChatMetadata = new ChatMetadata();
      int i = 0;
      while (i == 0)
      {
        int j = paramXmlPullParser.next();
        if (j == 2)
        {
          if (paramXmlPullParser.getName().equals("sessionID")) {
            localChatMetadata.setSessionID(paramXmlPullParser.nextText());
          } else if (paramXmlPullParser.getName().equals("metadata")) {
            localChatMetadata.setMetadata(MetaDataUtils.parseMetaData(paramXmlPullParser));
          }
        }
        else if ((j == 3) && (paramXmlPullParser.getName().equals("chat-metadata"))) {
          i = 1;
        }
      }
      return localChatMetadata;
    }
  }
}


/* Location:           C:\Users\Administrator\Documents\ds\反编译\AndroidKiller_v1.3.1\projects\Jungledsq_ppc\ProjectSrc\smali\
 * Qualified Name:     org.jivesoftware.smackx.workgroup.ext.history.ChatMetadata
 * JD-Core Version:    0.7.0.1
 */