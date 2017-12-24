package org.jivesoftware.smack;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Message.Type;

public class Chat
{
  private ChatManager chatManager;
  private final Set<MessageListener> listeners = new CopyOnWriteArraySet();
  private String participant;
  private String threadID;
  
  Chat(ChatManager paramChatManager, String paramString1, String paramString2)
  {
    this.chatManager = paramChatManager;
    this.participant = paramString1;
    this.threadID = paramString2;
  }
  
  public void addMessageListener(MessageListener paramMessageListener)
  {
    if (paramMessageListener == null) {
      return;
    }
    this.listeners.add(paramMessageListener);
  }
  
  public PacketCollector createCollector()
  {
    return this.chatManager.createPacketCollector(this);
  }
  
  void deliver(Message paramMessage)
  {
    paramMessage.setThread(this.threadID);
    Iterator localIterator = this.listeners.iterator();
    while (localIterator.hasNext()) {
      ((MessageListener)localIterator.next()).processMessage(this, paramMessage);
    }
  }
  
  public boolean equals(Object paramObject)
  {
    return ((paramObject instanceof Chat)) && (this.threadID.equals(((Chat)paramObject).getThreadID())) && (this.participant.equals(((Chat)paramObject).getParticipant()));
  }
  
  public Collection<MessageListener> getListeners()
  {
    return Collections.unmodifiableCollection(this.listeners);
  }
  
  public String getParticipant()
  {
    return this.participant;
  }
  
  public String getThreadID()
  {
    return this.threadID;
  }
  
  public void removeMessageListener(MessageListener paramMessageListener)
  {
    this.listeners.remove(paramMessageListener);
  }
  
  public void sendMessage(String paramString)
    throws XMPPException
  {
    Message localMessage = new Message(this.participant, Message.Type.chat);
    localMessage.setThread(this.threadID);
    localMessage.setBody(paramString);
    this.chatManager.sendMessage(this, localMessage);
  }
  
  public void sendMessage(Message paramMessage)
    throws XMPPException
  {
    paramMessage.setTo(this.participant);
    paramMessage.setType(Message.Type.chat);
    paramMessage.setThread(this.threadID);
    this.chatManager.sendMessage(this, paramMessage);
  }
}


/* Location:           C:\Users\Administrator\Documents\ds\反编译\AndroidKiller_v1.3.1\projects\Jungledsq_ppc\ProjectSrc\smali\
 * Qualified Name:     org.jivesoftware.smack.Chat
 * JD-Core Version:    0.7.0.1
 */