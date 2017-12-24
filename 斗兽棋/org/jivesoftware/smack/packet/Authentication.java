package org.jivesoftware.smack.packet;

import org.jivesoftware.smack.util.StringUtils;

public class Authentication
  extends IQ
{
  private String digest = null;
  private String password = null;
  private String resource = null;
  private String username = null;
  
  public Authentication()
  {
    setType(IQ.Type.SET);
  }
  
  public String getChildElementXML()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("<query xmlns=\"jabber:iq:auth\">");
    if (this.username != null)
    {
      if (this.username.equals("")) {
        localStringBuilder.append("<username/>");
      }
    }
    else
    {
      if (this.digest != null)
      {
        if (!this.digest.equals("")) {
          break label160;
        }
        localStringBuilder.append("<digest/>");
      }
      label67:
      if ((this.password != null) && (this.digest == null))
      {
        if (!this.password.equals("")) {
          break label182;
        }
        localStringBuilder.append("<password/>");
      }
      label100:
      if (this.resource != null)
      {
        if (!this.resource.equals("")) {
          break label207;
        }
        localStringBuilder.append("<resource/>");
      }
    }
    for (;;)
    {
      localStringBuilder.append("</query>");
      return localStringBuilder.toString();
      localStringBuilder.append("<username>").append(this.username).append("</username>");
      break;
      label160:
      localStringBuilder.append("<digest>").append(this.digest).append("</digest>");
      break label67;
      label182:
      localStringBuilder.append("<password>").append(StringUtils.escapeForXML(this.password)).append("</password>");
      break label100;
      label207:
      localStringBuilder.append("<resource>").append(this.resource).append("</resource>");
    }
  }
  
  public String getDigest()
  {
    return this.digest;
  }
  
  public String getPassword()
  {
    return this.password;
  }
  
  public String getResource()
  {
    return this.resource;
  }
  
  public String getUsername()
  {
    return this.username;
  }
  
  public void setDigest(String paramString)
  {
    this.digest = paramString;
  }
  
  public void setDigest(String paramString1, String paramString2)
  {
    this.digest = StringUtils.hash(paramString1 + paramString2);
  }
  
  public void setPassword(String paramString)
  {
    this.password = paramString;
  }
  
  public void setResource(String paramString)
  {
    this.resource = paramString;
  }
  
  public void setUsername(String paramString)
  {
    this.username = paramString;
  }
}


/* Location:           C:\Users\Administrator\Documents\ds\反编译\AndroidKiller_v1.3.1\projects\Jungledsq_ppc\ProjectSrc\smali\
 * Qualified Name:     org.jivesoftware.smack.packet.Authentication
 * JD-Core Version:    0.7.0.1
 */