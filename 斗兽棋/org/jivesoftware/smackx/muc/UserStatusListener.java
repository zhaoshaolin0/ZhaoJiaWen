package org.jivesoftware.smackx.muc;

public abstract interface UserStatusListener
{
  public abstract void adminGranted();
  
  public abstract void adminRevoked();
  
  public abstract void banned(String paramString1, String paramString2);
  
  public abstract void kicked(String paramString1, String paramString2);
  
  public abstract void membershipGranted();
  
  public abstract void membershipRevoked();
  
  public abstract void moderatorGranted();
  
  public abstract void moderatorRevoked();
  
  public abstract void ownershipGranted();
  
  public abstract void ownershipRevoked();
  
  public abstract void voiceGranted();
  
  public abstract void voiceRevoked();
}


/* Location:           C:\Users\Administrator\Documents\ds\反编译\AndroidKiller_v1.3.1\projects\Jungledsq_ppc\ProjectSrc\smali\
 * Qualified Name:     org.jivesoftware.smackx.muc.UserStatusListener
 * JD-Core Version:    0.7.0.1
 */