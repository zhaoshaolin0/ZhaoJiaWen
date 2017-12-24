package org.jivesoftware.smackx.workgroup.agent;

public class UserRequest
  extends OfferContent
{
  private static UserRequest instance = new UserRequest();
  
  public static OfferContent getInstance()
  {
    return instance;
  }
  
  boolean isInvitation()
  {
    return false;
  }
  
  boolean isTransfer()
  {
    return false;
  }
  
  boolean isUserRequest()
  {
    return true;
  }
}


/* Location:           C:\Users\Administrator\Documents\ds\反编译\AndroidKiller_v1.3.1\projects\Jungledsq_ppc\ProjectSrc\smali\
 * Qualified Name:     org.jivesoftware.smackx.workgroup.agent.UserRequest
 * JD-Core Version:    0.7.0.1
 */