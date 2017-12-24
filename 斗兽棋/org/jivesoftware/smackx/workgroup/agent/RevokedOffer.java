package org.jivesoftware.smackx.workgroup.agent;

import java.util.Date;

public class RevokedOffer
{
  private String reason;
  private String sessionID;
  private Date timestamp;
  private String userID;
  private String userJID;
  private String workgroupName;
  
  RevokedOffer(String paramString1, String paramString2, String paramString3, String paramString4, String paramString5, Date paramDate)
  {
    this.userJID = paramString1;
    this.userID = paramString2;
    this.workgroupName = paramString3;
    this.sessionID = paramString4;
    this.reason = paramString5;
    this.timestamp = paramDate;
  }
  
  public String getReason()
  {
    return this.reason;
  }
  
  public String getSessionID()
  {
    return this.sessionID;
  }
  
  public Date getTimestamp()
  {
    return this.timestamp;
  }
  
  public String getUserID()
  {
    return this.userID;
  }
  
  public String getUserJID()
  {
    return this.userJID;
  }
  
  public String getWorkgroupName()
  {
    return this.workgroupName;
  }
}


/* Location:           C:\Users\Administrator\Documents\ds\反编译\AndroidKiller_v1.3.1\projects\Jungledsq_ppc\ProjectSrc\smali\
 * Qualified Name:     org.jivesoftware.smackx.workgroup.agent.RevokedOffer
 * JD-Core Version:    0.7.0.1
 */