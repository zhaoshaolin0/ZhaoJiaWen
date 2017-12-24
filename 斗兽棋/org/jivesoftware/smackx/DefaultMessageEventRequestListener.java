package org.jivesoftware.smackx;

public class DefaultMessageEventRequestListener
  implements MessageEventRequestListener
{
  public void composingNotificationRequested(String paramString1, String paramString2, MessageEventManager paramMessageEventManager) {}
  
  public void deliveredNotificationRequested(String paramString1, String paramString2, MessageEventManager paramMessageEventManager)
  {
    paramMessageEventManager.sendDeliveredNotification(paramString1, paramString2);
  }
  
  public void displayedNotificationRequested(String paramString1, String paramString2, MessageEventManager paramMessageEventManager) {}
  
  public void offlineNotificationRequested(String paramString1, String paramString2, MessageEventManager paramMessageEventManager) {}
}


/* Location:           C:\Users\Administrator\Documents\ds\反编译\AndroidKiller_v1.3.1\projects\Jungledsq_ppc\ProjectSrc\smali\
 * Qualified Name:     org.jivesoftware.smackx.DefaultMessageEventRequestListener
 * JD-Core Version:    0.7.0.1
 */