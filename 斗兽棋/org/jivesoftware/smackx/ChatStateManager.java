package org.jivesoftware.smackx;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.WeakHashMap;
import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.ChatManager;
import org.jivesoftware.smack.ChatManagerListener;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.PacketInterceptor;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.filter.NotFilter;
import org.jivesoftware.smack.filter.PacketExtensionFilter;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.packet.PacketExtension;
import org.jivesoftware.smack.util.collections.ReferenceMap;
import org.jivesoftware.smackx.packet.ChatStateExtension;

public class ChatStateManager
{
  private static final PacketFilter filter = new NotFilter(new PacketExtensionFilter("http://jabber.org/protocol/chatstates"));
  private static final Map<XMPPConnection, ChatStateManager> managers = new WeakHashMap();
  private final Map<Chat, ChatState> chatStates = new ReferenceMap(2, 0);
  private final XMPPConnection connection;
  private final IncomingMessageInterceptor incomingInterceptor = new IncomingMessageInterceptor(null);
  private final OutgoingMessageInterceptor outgoingInterceptor = new OutgoingMessageInterceptor(null);
  
  private ChatStateManager(XMPPConnection paramXMPPConnection)
  {
    this.connection = paramXMPPConnection;
  }
  
  private void fireNewChatState(Chat paramChat, ChatState paramChatState)
  {
    Iterator localIterator = paramChat.getListeners().iterator();
    while (localIterator.hasNext())
    {
      MessageListener localMessageListener = (MessageListener)localIterator.next();
      if ((localMessageListener instanceof ChatStateListener)) {
        ((ChatStateListener)localMessageListener).stateChanged(paramChat, paramChatState);
      }
    }
  }
  
  public static ChatStateManager getInstance(XMPPConnection paramXMPPConnection)
  {
    if (paramXMPPConnection == null) {
      return null;
    }
    synchronized (managers)
    {
      ChatStateManager localChatStateManager2 = (ChatStateManager)managers.get(paramXMPPConnection);
      ChatStateManager localChatStateManager1 = localChatStateManager2;
      if (localChatStateManager2 == null)
      {
        localChatStateManager1 = new ChatStateManager(paramXMPPConnection);
        localChatStateManager1.init();
        managers.put(paramXMPPConnection, localChatStateManager1);
      }
      return localChatStateManager1;
    }
  }
  
  private void init()
  {
    this.connection.getChatManager().addOutgoingMessageInterceptor(this.outgoingInterceptor, filter);
    this.connection.getChatManager().addChatListener(this.incomingInterceptor);
    ServiceDiscoveryManager.getInstanceFor(this.connection).addFeature("http://jabber.org/protocol/chatstates");
  }
  
  private boolean updateChatState(Chat paramChat, ChatState paramChatState)
  {
    if ((ChatState)this.chatStates.get(paramChat) != paramChatState)
    {
      this.chatStates.put(paramChat, paramChatState);
      return true;
    }
    return false;
  }
  
  public boolean equals(Object paramObject)
  {
    if (this == paramObject) {
      return true;
    }
    if ((paramObject == null) || (getClass() != paramObject.getClass())) {
      return false;
    }
    paramObject = (ChatStateManager)paramObject;
    return this.connection.equals(paramObject.connection);
  }
  
  public int hashCode()
  {
    return this.connection.hashCode();
  }
  
  public void setCurrentState(ChatState paramChatState, Chat paramChat)
    throws XMPPException
  {
    if ((paramChat == null) || (paramChatState == null)) {
      throw new IllegalArgumentException("Arguments cannot be null.");
    }
    if (!updateChatState(paramChat, paramChatState)) {
      return;
    }
    Message localMessage = new Message();
    localMessage.addExtension(new ChatStateExtension(paramChatState));
    paramChat.sendMessage(localMessage);
  }
  
  private class IncomingMessageInterceptor
    implements ChatManagerListener, MessageListener
  {
    private IncomingMessageInterceptor() {}
    
    public void chatCreated(Chat paramChat, boolean paramBoolean)
    {
      paramChat.addMessageListener(this);
    }
    
    public void processMessage(Chat paramChat, Message paramMessage)
    {
      paramMessage = paramMessage.getExtension("http://jabber.org/protocol/chatstates");
      if (paramMessage == null) {
        return;
      }
      try
      {
        paramMessage = ChatState.valueOf(paramMessage.getElementName());
        ChatStateManager.this.fireNewChatState(paramChat, paramMessage);
        return;
      }
      catch (Exception paramChat) {}
    }
  }
  
  private class OutgoingMessageInterceptor
    implements PacketInterceptor
  {
    private OutgoingMessageInterceptor() {}
    
    public void interceptPacket(Packet paramPacket)
    {
      paramPacket = (Message)paramPacket;
      Chat localChat = ChatStateManager.this.connection.getChatManager().getThreadChat(paramPacket.getThread());
      if (localChat == null) {}
      while (!ChatStateManager.this.updateChatState(localChat, ChatState.active)) {
        return;
      }
      paramPacket.addExtension(new ChatStateExtension(ChatState.active));
    }
  }
}


/* Location:           C:\Users\Administrator\Documents\ds\反编译\AndroidKiller_v1.3.1\projects\Jungledsq_ppc\ProjectSrc\smali\
 * Qualified Name:     org.jivesoftware.smackx.ChatStateManager
 * JD-Core Version:    0.7.0.1
 */