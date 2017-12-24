package org.jivesoftware.smack;

import org.jivesoftware.smack.packet.Message;

public abstract interface MessageListener
{
  public abstract void processMessage(Chat paramChat, Message paramMessage);
}


/* Location:           C:\Users\Administrator\Documents\ds\反编译\AndroidKiller_v1.3.1\projects\Jungledsq_ppc\ProjectSrc\smali\
 * Qualified Name:     org.jivesoftware.smack.MessageListener
 * JD-Core Version:    0.7.0.1
 */