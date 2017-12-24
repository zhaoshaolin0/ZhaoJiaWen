package org.jivesoftware.smackx.workgroup.settings;

import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.provider.IQProvider;
import org.jivesoftware.smackx.workgroup.util.ModelUtil;
import org.xmlpull.v1.XmlPullParser;

public class OfflineSettings
  extends IQ
{
  public static final String ELEMENT_NAME = "offline-settings";
  public static final String NAMESPACE = "http://jivesoftware.com/protocol/workgroup";
  private String emailAddress;
  private String offlineText;
  private String redirectURL;
  private String subject;
  
  public String getChildElementXML()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("<").append("offline-settings").append(" xmlns=");
    localStringBuilder.append('"');
    localStringBuilder.append("http://jivesoftware.com/protocol/workgroup");
    localStringBuilder.append('"');
    localStringBuilder.append("></").append("offline-settings").append("> ");
    return localStringBuilder.toString();
  }
  
  public String getEmailAddress()
  {
    if (!ModelUtil.hasLength(this.emailAddress)) {
      return "";
    }
    return this.emailAddress;
  }
  
  public String getOfflineText()
  {
    if (!ModelUtil.hasLength(this.offlineText)) {
      return "";
    }
    return this.offlineText;
  }
  
  public String getRedirectURL()
  {
    if (!ModelUtil.hasLength(this.redirectURL)) {
      return "";
    }
    return this.redirectURL;
  }
  
  public String getSubject()
  {
    if (!ModelUtil.hasLength(this.subject)) {
      return "";
    }
    return this.subject;
  }
  
  public boolean isConfigured()
  {
    return (ModelUtil.hasLength(getEmailAddress())) && (ModelUtil.hasLength(getSubject())) && (ModelUtil.hasLength(getOfflineText()));
  }
  
  public boolean redirects()
  {
    return ModelUtil.hasLength(getRedirectURL());
  }
  
  public void setEmailAddress(String paramString)
  {
    this.emailAddress = paramString;
  }
  
  public void setOfflineText(String paramString)
  {
    this.offlineText = paramString;
  }
  
  public void setRedirectURL(String paramString)
  {
    this.redirectURL = paramString;
  }
  
  public void setSubject(String paramString)
  {
    this.subject = paramString;
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
      OfflineSettings localOfflineSettings = new OfflineSettings();
      int i = 0;
      String str2 = null;
      String str1 = null;
      String str3 = null;
      String str4 = null;
      while (i == 0)
      {
        int j = paramXmlPullParser.next();
        if ((j == 2) && ("redirectPage".equals(paramXmlPullParser.getName()))) {
          str2 = paramXmlPullParser.nextText();
        } else if ((j == 2) && ("subject".equals(paramXmlPullParser.getName()))) {
          str1 = paramXmlPullParser.nextText();
        } else if ((j == 2) && ("offlineText".equals(paramXmlPullParser.getName()))) {
          str3 = paramXmlPullParser.nextText();
        } else if ((j == 2) && ("emailAddress".equals(paramXmlPullParser.getName()))) {
          str4 = paramXmlPullParser.nextText();
        } else if ((j == 3) && ("offline-settings".equals(paramXmlPullParser.getName()))) {
          i = 1;
        }
      }
      localOfflineSettings.setEmailAddress(str4);
      localOfflineSettings.setRedirectURL(str2);
      localOfflineSettings.setSubject(str1);
      localOfflineSettings.setOfflineText(str3);
      return localOfflineSettings;
    }
  }
}


/* Location:           C:\Users\Administrator\Documents\ds\反编译\AndroidKiller_v1.3.1\projects\Jungledsq_ppc\ProjectSrc\smali\
 * Qualified Name:     org.jivesoftware.smackx.workgroup.settings.OfflineSettings
 * JD-Core Version:    0.7.0.1
 */