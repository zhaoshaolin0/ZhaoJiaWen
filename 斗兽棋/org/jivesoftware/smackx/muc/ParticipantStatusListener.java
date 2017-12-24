package org.jivesoftware.smackx.muc;

public abstract interface ParticipantStatusListener
{
  public abstract void adminGranted(String paramString);
  
  public abstract void adminRevoked(String paramString);
  
  public abstract void banned(String paramString1, String paramString2, String paramString3);
  
  public abstract void joined(String paramString);
  
  public abstract void kicked(String paramString1, String paramString2, String paramString3);
  
  public abstract void left(String paramString);
  
  public abstract void membershipGranted(String paramString);
  
  public abstract void membershipRevoked(String paramString);
  
  public abstract void moderatorGranted(String paramString);
  
  public abstract void moderatorRevoked(String paramString);
  
  public abstract void nicknameChanged(String paramString1, String paramString2);
  
  public abstract void ownershipGranted(String paramString);
  
  public abstract void ownershipRevoked(String paramString);
  
  public abstract void voiceGranted(String paramString);
  
  public abstract void voiceRevoked(String paramString);
}


/* Location:           C:\Users\Administrator\Documents\ds\反编译\AndroidKiller_v1.3.1\projects\Jungledsq_ppc\ProjectSrc\smali\
 * Qualified Name:     org.jivesoftware.smackx.muc.ParticipantStatusListener
 * JD-Core Version:    0.7.0.1
 */