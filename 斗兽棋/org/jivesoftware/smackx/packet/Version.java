package org.jivesoftware.smackx.packet;

import org.jivesoftware.smack.packet.IQ;

public class Version
  extends IQ
{
  private String name;
  private String os;
  private String version;
  
  public String getChildElementXML()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("<query xmlns=\"jabber:iq:version\">");
    if (this.name != null) {
      localStringBuilder.append("<name>").append(this.name).append("</name>");
    }
    if (this.version != null) {
      localStringBuilder.append("<version>").append(this.version).append("</version>");
    }
    if (this.os != null) {
      localStringBuilder.append("<os>").append(this.os).append("</os>");
    }
    localStringBuilder.append("</query>");
    return localStringBuilder.toString();
  }
  
  public String getName()
  {
    return this.name;
  }
  
  public String getOs()
  {
    return this.os;
  }
  
  public String getVersion()
  {
    return this.version;
  }
  
  public void setName(String paramString)
  {
    this.name = paramString;
  }
  
  public void setOs(String paramString)
  {
    this.os = paramString;
  }
  
  public void setVersion(String paramString)
  {
    this.version = paramString;
  }
}


/* Location:           C:\Users\Administrator\Documents\ds\反编译\AndroidKiller_v1.3.1\projects\Jungledsq_ppc\ProjectSrc\smali\
 * Qualified Name:     org.jivesoftware.smackx.packet.Version
 * JD-Core Version:    0.7.0.1
 */