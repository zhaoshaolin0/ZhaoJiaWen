package org.jivesoftware.smackx.workgroup.settings;

import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.provider.IQProvider;
import org.jivesoftware.smackx.workgroup.util.ModelUtil;
import org.xmlpull.v1.XmlPullParser;

public class WorkgroupProperties
  extends IQ
{
  public static final String ELEMENT_NAME = "workgroup-properties";
  public static final String NAMESPACE = "http://jivesoftware.com/protocol/workgroup";
  private boolean authRequired;
  private String email;
  private String fullName;
  private String jid;
  
  public String getChildElementXML()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("<").append("workgroup-properties").append(" xmlns=");
    localStringBuilder.append('"');
    localStringBuilder.append("http://jivesoftware.com/protocol/workgroup");
    localStringBuilder.append('"');
    if (ModelUtil.hasLength(getJid())) {
      localStringBuilder.append("jid=\"" + getJid() + "\" ");
    }
    localStringBuilder.append("></").append("workgroup-properties").append("> ");
    return localStringBuilder.toString();
  }
  
  public String getEmail()
  {
    return this.email;
  }
  
  public String getFullName()
  {
    return this.fullName;
  }
  
  public String getJid()
  {
    return this.jid;
  }
  
  public boolean isAuthRequired()
  {
    return this.authRequired;
  }
  
  public void setAuthRequired(boolean paramBoolean)
  {
    this.authRequired = paramBoolean;
  }
  
  public void setEmail(String paramString)
  {
    this.email = paramString;
  }
  
  public void setFullName(String paramString)
  {
    this.fullName = paramString;
  }
  
  public void setJid(String paramString)
  {
    this.jid = paramString;
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
      WorkgroupProperties localWorkgroupProperties = new WorkgroupProperties();
      int i = 0;
      while (i == 0)
      {
        int j = paramXmlPullParser.next();
        if ((j == 2) && ("authRequired".equals(paramXmlPullParser.getName()))) {
          localWorkgroupProperties.setAuthRequired(new Boolean(paramXmlPullParser.nextText()).booleanValue());
        } else if ((j == 2) && ("email".equals(paramXmlPullParser.getName()))) {
          localWorkgroupProperties.setEmail(paramXmlPullParser.nextText());
        } else if ((j == 2) && ("name".equals(paramXmlPullParser.getName()))) {
          localWorkgroupProperties.setFullName(paramXmlPullParser.nextText());
        } else if ((j == 3) && ("workgroup-properties".equals(paramXmlPullParser.getName()))) {
          i = 1;
        }
      }
      return localWorkgroupProperties;
    }
  }
}


/* Location:           C:\Users\Administrator\Documents\ds\反编译\AndroidKiller_v1.3.1\projects\Jungledsq_ppc\ProjectSrc\smali\
 * Qualified Name:     org.jivesoftware.smackx.workgroup.settings.WorkgroupProperties
 * JD-Core Version:    0.7.0.1
 */