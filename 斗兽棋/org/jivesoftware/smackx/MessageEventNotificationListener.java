package org.jivesoftware.smackx;

public abstract interface MessageEventNotificationListener
{
  public abstract void cancelledNotification(String paramString1, String paramString2);
  
  public abstract void composingNotification(String paramString1, String paramString2);
  
  public abstract void deliveredNotification(String paramString1, String paramString2);
  
  public abstract void displayedNotification(String paramString1, String paramString2);
  
  public abstract void offlineNotification(String paramString1, String paramString2);
}


/* Location:           C:\Users\Administrator\Documents\ds\反编译\AndroidKiller_v1.3.1\projects\Jungledsq_ppc\ProjectSrc\smali\
 * Qualified Name:     org.jivesoftware.smackx.MessageEventNotificationListener
 * JD-Core Version:    0.7.0.1
 */