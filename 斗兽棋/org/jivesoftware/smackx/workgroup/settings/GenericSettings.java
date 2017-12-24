package org.jivesoftware.smackx.workgroup.settings;

import java.util.HashMap;
import java.util.Map;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.provider.IQProvider;
import org.jivesoftware.smackx.workgroup.util.ModelUtil;
import org.xmlpull.v1.XmlPullParser;

public class GenericSettings
  extends IQ
{
  public static final String ELEMENT_NAME = "generic-metadata";
  public static final String NAMESPACE = "http://jivesoftware.com/protocol/workgroup";
  private Map map = new HashMap();
  private String query;
  
  public String getChildElementXML()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("<").append("generic-metadata").append(" xmlns=");
    localStringBuilder.append('"');
    localStringBuilder.append("http://jivesoftware.com/protocol/workgroup");
    localStringBuilder.append('"');
    localStringBuilder.append(">");
    if (ModelUtil.hasLength(getQuery())) {
      localStringBuilder.append("<query>" + getQuery() + "</query>");
    }
    localStringBuilder.append("</").append("generic-metadata").append("> ");
    return localStringBuilder.toString();
  }
  
  public Map getMap()
  {
    return this.map;
  }
  
  public String getQuery()
  {
    return this.query;
  }
  
  public void setMap(Map paramMap)
  {
    this.map = paramMap;
  }
  
  public void setQuery(String paramString)
  {
    this.query = paramString;
  }
  
  public static class InternalProvider
    implements IQProvider
  {
    public IQ parseIQ(XmlPullParser paramXmlPullParser)
      throws Exception
    {
      if (paramXmlPullParser.getEventType() != 2) {
        throw new IllegalStateException("Parser not in proper position, or bad XML.");
      }
      GenericSettings localGenericSettings = new GenericSettings();
      int i = 0;
      while (i == 0)
      {
        int j = paramXmlPullParser.next();
        if ((j == 2) && ("entry".equals(paramXmlPullParser.getName())))
        {
          paramXmlPullParser.next();
          String str1 = paramXmlPullParser.nextText();
          paramXmlPullParser.next();
          String str2 = paramXmlPullParser.nextText();
          localGenericSettings.getMap().put(str1, str2);
        }
        else if ((j == 3) && ("generic-metadata".equals(paramXmlPullParser.getName())))
        {
          i = 1;
        }
      }
      return localGenericSettings;
    }
  }
}


/* Location:           C:\Users\Administrator\Documents\ds\反编译\AndroidKiller_v1.3.1\projects\Jungledsq_ppc\ProjectSrc\smali\
 * Qualified Name:     org.jivesoftware.smackx.workgroup.settings.GenericSettings
 * JD-Core Version:    0.7.0.1
 */