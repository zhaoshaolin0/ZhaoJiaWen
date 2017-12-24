package org.jivesoftware.smack;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.WeakHashMap;
import java.util.concurrent.CopyOnWriteArraySet;
import org.jivesoftware.smack.filter.AndFilter;
import org.jivesoftware.smack.filter.FromContainsFilter;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.filter.ThreadFilter;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Message.Type;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.util.StringUtils;
import org.jivesoftware.smack.util.collections.ReferenceMap;

public class ChatManager
{
  private static long id = 0L;
  private static String prefix = StringUtils.randomString(5);
  private Set<ChatManagerListener> chatManagerListeners = new CopyOnWriteArraySet();
  private XMPPConnection connection;
  private Map<PacketInterceptor, PacketFilter> interceptors = new WeakHashMap();
  private Map<String, Chat> jidChats = new ReferenceMap(0, 2);
  private Map<String, Chat> threadChats = new ReferenceMap(0, 2);
  
  ChatManager(XMPPConnection paramXMPPConnection)
  {
    this.connection = paramXMPPConnection;
    PacketFilter local1 = new PacketFilter()
    {
      public boolean accept(Packet paramAnonymousPacket)
      {
        if (!(paramAnonymousPacket instanceof Message)) {
          return false;
        }
        paramAnonymousPacket = ((Message)paramAnonymousPacket).getType();
        return (paramAnonymousPacket != Message.Type.groupchat) && (paramAnonymousPacket != Message.Type.headline);
      }
    };
    paramXMPPConnection.addPacketListener(new PacketListener()
    {
      public void processPacket(Packet paramAnonymousPacket)
      {
        Message localMessage = (Message)paramAnonymousPacket;
        if (localMessage.getThread() == null) {
          paramAnonymousPacket = ChatManager.this.getUserChat(StringUtils.parseBareAddress(localMessage.getFrom()));
        }
        for (;;)
        {
          Object localObject = paramAnonymousPacket;
          if (paramAnonymousPacket == null) {
            localObject = ChatManager.this.createChat(localMessage);
          }
          ChatManager.this.deliverMessage((Chat)localObject, localMessage);
          return;
          localObject = ChatManager.this.getThreadChat(localMessage.getThread());
          paramAnonymousPacket = (Packet)localObject;
          if (localObject == null) {
            paramAnonymousPacket = ChatManager.this.getUserChat(StringUtils.parseBareAddress(localMessage.getFrom()));
          }
        }
      }
    }, local1);
  }
  
  private Chat createChat(String paramString1, String paramString2, boolean paramBoolean)
  {
    Chat localChat = new Chat(this, paramString1, paramString2);
    this.threadChats.put(paramString2, localChat);
    this.jidChats.put(paramString1, localChat);
    paramString1 = this.chatManagerListeners.iterator();
    while (paramString1.hasNext()) {
      ((ChatManagerListener)paramString1.next()).chatCreated(localChat, paramBoolean);
    }
    return localChat;
  }
  
  private Chat createChat(Message paramMessage)
  {
    String str2 = paramMessage.getThread();
    String str1 = str2;
    if (str2 == null) {
      str1 = nextID();
    }
    return createChat(paramMessage.getFrom(), str1, false);
  }
  
  private void deliverMessage(Chat paramChat, Message paramMessage)
  {
    paramChat.deliver(paramMessage);
  }
  
  private Chat getUserChat(String paramString)
  {
    return (Chat)this.jidChats.get(paramString);
  }
  
  private static String nextID()
  {
    try
    {
      Object localObject1 = new StringBuilder().append(prefix);
      long l = id;
      id = 1L + l;
      localObject1 = Long.toString(l);
      return localObject1;
    }
    finally
    {
      localObject2 = finally;
      throw localObject2;
    }
  }
  
  public void addChatListener(ChatManagerListener paramChatManagerListener)
  {
    this.chatManagerListeners.add(paramChatManagerListener);
  }
  
  public void addOutgoingMessageInterceptor(PacketInterceptor paramPacketInterceptor)
  {
    addOutgoingMessageInterceptor(paramPacketInterceptor, null);
  }
  
  public void addOutgoingMessageInterceptor(PacketInterceptor paramPacketInterceptor, PacketFilter paramPacketFilter)
  {
    if (paramPacketInterceptor != null) {
      this.interceptors.put(paramPacketInterceptor, paramPacketFilter);
    }
  }
  
  public Chat createChat(String paramString1, String paramString2, MessageListener paramMessageListener)
  {
    String str = paramString2;
    if (paramString2 == null) {
      str = nextID();
    }
    if ((Chat)this.threadChats.get(str) != null) {
      throw new IllegalArgumentException("ThreadID is already used");
    }
    paramString1 = createChat(paramString1, str, true);
    paramString1.addMessageListener(paramMessageListener);
    return paramString1;
  }
  
  public Chat createChat(String paramString, MessageListener paramMessageListener)
  {
    String str;
    do
    {
      str = nextID();
    } while (this.threadChats.get(str) != null);
    return createChat(paramString, str, paramMessageListener);
  }
  
  PacketCollector createPacketCollector(Chat paramChat)
  {
    return this.connection.createPacketCollector(new AndFilter(new PacketFilter[] { new ThreadFilter(paramChat.getThreadID()), new FromContainsFilter(paramChat.getParticipant()) }));
  }
  
  public Collection<ChatManagerListener> getChatListeners()
  {
    return Collections.unmodifiableCollection(this.chatManagerListeners);
  }
  
  public Chat getThreadChat(String paramString)
  {
    return (Chat)this.threadChats.get(paramString);
  }
  
  public void removeChatListener(ChatManagerListener paramChatManagerListener)
  {
    this.chatManagerListeners.remove(paramChatManagerListener);
  }
  
  void sendMessage(Chat paramChat, Message paramMessage)
  {
    paramChat = this.interceptors.entrySet().iterator();
    while (paramChat.hasNext())
    {
      Map.Entry localEntry = (Map.Entry)paramChat.next();
      PacketFilter localPacketFilter = (PacketFilter)localEntry.getValue();
      if ((localPacketFilter != null) && (localPacketFilter.accept(paramMessage))) {
        ((PacketInterceptor)localEntry.getKey()).interceptPacket(paramMessage);
      }
    }
    if (paramMessage.getFrom() == null) {
      paramMessage.setFrom(this.connection.getUser());
    }
    this.connection.sendPacket(paramMessage);
  }
}


/* Location:           C:\Users\Administrator\Documents\ds\反编译\AndroidKiller_v1.3.1\projects\Jungledsq_ppc\ProjectSrc\smali\
 * Qualified Name:     org.jivesoftware.smack.ChatManager
 * JD-Core Version:    0.7.0.1
 */