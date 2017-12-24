package org.jivesoftware.smackx.workgroup;

import java.util.Map;

public class WorkgroupInvitation
{
  protected String groupChatName;
  protected String invitationSender;
  protected String issuingWorkgroupName;
  protected String messageBody;
  protected Map metaData;
  protected String sessionID;
  protected String uniqueID;
  
  public WorkgroupInvitation(String paramString1, String paramString2, String paramString3, String paramString4, String paramString5, String paramString6)
  {
    this(paramString1, paramString2, paramString3, paramString4, paramString5, paramString6, null);
  }
  
  public WorkgroupInvitation(String paramString1, String paramString2, String paramString3, String paramString4, String paramString5, String paramString6, Map paramMap)
  {
    this.uniqueID = paramString1;
    this.sessionID = paramString4;
    this.groupChatName = paramString2;
    this.issuingWorkgroupName = paramString3;
    this.messageBody = paramString5;
    this.invitationSender = paramString6;
    this.metaData = paramMap;
  }
  
  public String getGroupChatName()
  {
    return this.groupChatName;
  }
  
  public String getInvitationSender()
  {
    return this.invitationSender;
  }
  
  public String getMessageBody()
  {
    return this.messageBody;
  }
  
  public Map getMetaData()
  {
    return this.metaData;
  }
  
  public String getSessionID()
  {
    return this.sessionID;
  }
  
  public String getUniqueID()
  {
    return this.uniqueID;
  }
  
  public String getWorkgroupName()
  {
    return this.issuingWorkgroupName;
  }
}


/* Location:           C:\Users\Administrator\Documents\ds\反编译\AndroidKiller_v1.3.1\projects\Jungledsq_ppc\ProjectSrc\smali\
 * Qualified Name:     org.jivesoftware.smackx.workgroup.WorkgroupInvitation
 * JD-Core Version:    0.7.0.1
 */