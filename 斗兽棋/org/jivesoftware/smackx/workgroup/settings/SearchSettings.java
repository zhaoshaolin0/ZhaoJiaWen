package org.jivesoftware.smackx.workgroup.settings;

import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.provider.IQProvider;
import org.jivesoftware.smackx.workgroup.util.ModelUtil;
import org.xmlpull.v1.XmlPullParser;

public class SearchSettings
  extends IQ
{
  public static final String ELEMENT_NAME = "search-settings";
  public static final String NAMESPACE = "http://jivesoftware.com/protocol/workgroup";
  private String forumsLocation;
  private String kbLocation;
  
  public String getChildElementXML()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("<").append("search-settings").append(" xmlns=");
    localStringBuilder.append('"');
    localStringBuilder.append("http://jivesoftware.com/protocol/workgroup");
    localStringBuilder.append('"');
    localStringBuilder.append("></").append("search-settings").append("> ");
    return localStringBuilder.toString();
  }
  
  public String getForumsLocation()
  {
    return this.forumsLocation;
  }
  
  public String getKbLocation()
  {
    return this.kbLocation;
  }
  
  public boolean hasForums()
  {
    return ModelUtil.hasLength(getForumsLocation());
  }
  
  public boolean hasKB()
  {
    return ModelUtil.hasLength(getKbLocation());
  }
  
  public boolean isSearchEnabled()
  {
    return (ModelUtil.hasLength(getForumsLocation())) && (ModelUtil.hasLength(getKbLocation()));
  }
  
  public void setForumsLocation(String paramString)
  {
    this.forumsLocation = paramString;
  }
  
  public void setKbLocation(String paramString)
  {
    this.kbLocation = paramString;
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
      SearchSettings localSearchSettings = new SearchSettings();
      int i = 0;
      String str1 = null;
      String str2 = null;
      while (i == 0)
      {
        int j = paramXmlPullParser.next();
        if ((j == 2) && ("forums".equals(paramXmlPullParser.getName()))) {
          str2 = paramXmlPullParser.nextText();
        } else if ((j == 2) && ("kb".equals(paramXmlPullParser.getName()))) {
          str1 = paramXmlPullParser.nextText();
        } else if ((j == 3) && ("search-settings".equals(paramXmlPullParser.getName()))) {
          i = 1;
        }
      }
      localSearchSettings.setForumsLocation(str2);
      localSearchSettings.setKbLocation(str1);
      return localSearchSettings;
    }
  }
}


/* Location:           C:\Users\Administrator\Documents\ds\反编译\AndroidKiller_v1.3.1\projects\Jungledsq_ppc\ProjectSrc\smali\
 * Qualified Name:     org.jivesoftware.smackx.workgroup.settings.SearchSettings
 * JD-Core Version:    0.7.0.1
 */