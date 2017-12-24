package org.jivesoftware.smackx.workgroup.agent;

public class InvitationRequest
  extends OfferContent
{
  private String inviter;
  private String reason;
  private String room;
  
  public InvitationRequest(String paramString1, String paramString2, String paramString3)
  {
    this.inviter = paramString1;
    this.room = paramString2;
    this.reason = paramString3;
  }
  
  public String getInviter()
  {
    return this.inviter;
  }
  
  public String getReason()
  {
    return this.reason;
  }
  
  public String getRoom()
  {
    return this.room;
  }
  
  boolean isInvitation()
  {
    return true;
  }
  
  boolean isTransfer()
  {
    return false;
  }
  
  boolean isUserRequest()
  {
    return false;
  }
}


/* Location:           C:\Users\Administrator\Documents\ds\反编译\AndroidKiller_v1.3.1\projects\Jungledsq_ppc\ProjectSrc\smali\
 * Qualified Name:     org.jivesoftware.smackx.workgroup.agent.InvitationRequest
 * JD-Core Version:    0.7.0.1
 */