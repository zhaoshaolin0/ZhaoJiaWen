package org.jivesoftware.smackx;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.MessageListener;

public abstract interface ChatStateListener
  extends MessageListener
{
  public abstract void stateChanged(Chat paramChat, ChatState paramChatState);
}


/* Location:           C:\Users\Administrator\Documents\ds\反编译\AndroidKiller_v1.3.1\projects\Jungledsq_ppc\ProjectSrc\smali\
 * Qualified Name:     org.jivesoftware.smackx.ChatStateListener
 * JD-Core Version:    0.7.0.1
 */