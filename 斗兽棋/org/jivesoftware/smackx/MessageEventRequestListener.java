package org.jivesoftware.smackx;

public abstract interface MessageEventRequestListener
{
  public abstract void composingNotificationRequested(String paramString1, String paramString2, MessageEventManager paramMessageEventManager);
  
  public abstract void deliveredNotificationRequested(String paramString1, String paramString2, MessageEventManager paramMessageEventManager);
  
  public abstract void displayedNotificationRequested(String paramString1, String paramString2, MessageEventManager paramMessageEventManager);
  
  public abstract void offlineNotificationRequested(String paramString1, String paramString2, MessageEventManager paramMessageEventManager);
}


/* Location:           C:\Users\Administrator\Documents\ds\反编译\AndroidKiller_v1.3.1\projects\Jungledsq_ppc\ProjectSrc\smali\
 * Qualified Name:     org.jivesoftware.smackx.MessageEventRequestListener
 * JD-Core Version:    0.7.0.1
 */