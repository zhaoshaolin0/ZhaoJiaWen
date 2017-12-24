package org.jivesoftware.smackx;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.filter.PacketExtensionFilter;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smackx.packet.MessageEvent;

public class MessageEventManager
{
  private XMPPConnection con;
  private List messageEventNotificationListeners = new ArrayList();
  private List messageEventRequestListeners = new ArrayList();
  private PacketFilter packetFilter = new PacketExtensionFilter("x", "jabber:x:event");
  private PacketListener packetListener;
  
  public MessageEventManager(XMPPConnection paramXMPPConnection)
  {
    this.con = paramXMPPConnection;
    init();
  }
  
  public static void addNotificationsRequests(Message paramMessage, boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3, boolean paramBoolean4)
  {
    MessageEvent localMessageEvent = new MessageEvent();
    localMessageEvent.setOffline(paramBoolean1);
    localMessageEvent.setDelivered(paramBoolean2);
    localMessageEvent.setDisplayed(paramBoolean3);
    localMessageEvent.setComposing(paramBoolean4);
    paramMessage.addExtension(localMessageEvent);
  }
  
  private void fireMessageEventNotificationListeners(String paramString1, String paramString2, String paramString3)
  {
    MessageEventNotificationListener[] arrayOfMessageEventNotificationListener;
    synchronized (this.messageEventNotificationListeners)
    {
      arrayOfMessageEventNotificationListener = new MessageEventNotificationListener[this.messageEventNotificationListeners.size()];
      this.messageEventNotificationListeners.toArray(arrayOfMessageEventNotificationListener);
    }
    try
    {
      paramString3 = MessageEventNotificationListener.class.getDeclaredMethod(paramString3, new Class[] { String.class, String.class });
      int i = 0;
      while (i < arrayOfMessageEventNotificationListener.length)
      {
        paramString3.invoke(arrayOfMessageEventNotificationListener[i], new Object[] { paramString1, paramString2 });
        i += 1;
        continue;
        paramString1 = finally;
        throw paramString1;
      }
    }
    catch (NoSuchMethodException paramString1)
    {
      paramString1.printStackTrace();
      return;
    }
    catch (InvocationTargetException paramString1)
    {
      paramString1.printStackTrace();
      return;
    }
    catch (IllegalAccessException paramString1)
    {
      paramString1.printStackTrace();
    }
  }
  
  private void fireMessageEventRequestListeners(String paramString1, String paramString2, String paramString3)
  {
    MessageEventRequestListener[] arrayOfMessageEventRequestListener;
    synchronized (this.messageEventRequestListeners)
    {
      arrayOfMessageEventRequestListener = new MessageEventRequestListener[this.messageEventRequestListeners.size()];
      this.messageEventRequestListeners.toArray(arrayOfMessageEventRequestListener);
    }
    try
    {
      paramString3 = MessageEventRequestListener.class.getDeclaredMethod(paramString3, new Class[] { String.class, String.class, MessageEventManager.class });
      int i = 0;
      while (i < arrayOfMessageEventRequestListener.length)
      {
        paramString3.invoke(arrayOfMessageEventRequestListener[i], new Object[] { paramString1, paramString2, this });
        i += 1;
        continue;
        paramString1 = finally;
        throw paramString1;
      }
    }
    catch (NoSuchMethodException paramString1)
    {
      paramString1.printStackTrace();
      return;
    }
    catch (InvocationTargetException paramString1)
    {
      paramString1.printStackTrace();
      return;
    }
    catch (IllegalAccessException paramString1)
    {
      paramString1.printStackTrace();
    }
  }
  
  private void init()
  {
    this.packetListener = new PacketListener()
    {
      public void processPacket(Packet paramAnonymousPacket)
      {
        paramAnonymousPacket = (Message)paramAnonymousPacket;
        Object localObject = (MessageEvent)paramAnonymousPacket.getExtension("x", "jabber:x:event");
        if (((MessageEvent)localObject).isMessageEventRequest())
        {
          localObject = ((MessageEvent)localObject).getEventTypes();
          while (((Iterator)localObject).hasNext()) {
            MessageEventManager.this.fireMessageEventRequestListeners(paramAnonymousPacket.getFrom(), paramAnonymousPacket.getPacketID(), ((String)((Iterator)localObject).next()).concat("NotificationRequested"));
          }
        }
        Iterator localIterator = ((MessageEvent)localObject).getEventTypes();
        while (localIterator.hasNext()) {
          MessageEventManager.this.fireMessageEventNotificationListeners(paramAnonymousPacket.getFrom(), ((MessageEvent)localObject).getPacketID(), ((String)localIterator.next()).concat("Notification"));
        }
      }
    };
    this.con.addPacketListener(this.packetListener, this.packetFilter);
  }
  
  public void addMessageEventNotificationListener(MessageEventNotificationListener paramMessageEventNotificationListener)
  {
    synchronized (this.messageEventNotificationListeners)
    {
      if (!this.messageEventNotificationListeners.contains(paramMessageEventNotificationListener)) {
        this.messageEventNotificationListeners.add(paramMessageEventNotificationListener);
      }
      return;
    }
  }
  
  public void addMessageEventRequestListener(MessageEventRequestListener paramMessageEventRequestListener)
  {
    synchronized (this.messageEventRequestListeners)
    {
      if (!this.messageEventRequestListeners.contains(paramMessageEventRequestListener)) {
        this.messageEventRequestListeners.add(paramMessageEventRequestListener);
      }
      return;
    }
  }
  
  public void destroy()
  {
    if (this.con != null) {
      this.con.removePacketListener(this.packetListener);
    }
  }
  
  public void finalize()
  {
    destroy();
  }
  
  public void removeMessageEventNotificationListener(MessageEventNotificationListener paramMessageEventNotificationListener)
  {
    synchronized (this.messageEventNotificationListeners)
    {
      this.messageEventNotificationListeners.remove(paramMessageEventNotificationListener);
      return;
    }
  }
  
  public void removeMessageEventRequestListener(MessageEventRequestListener paramMessageEventRequestListener)
  {
    synchronized (this.messageEventRequestListeners)
    {
      this.messageEventRequestListeners.remove(paramMessageEventRequestListener);
      return;
    }
  }
  
  public void sendCancelledNotification(String paramString1, String paramString2)
  {
    paramString1 = new Message(paramString1);
    MessageEvent localMessageEvent = new MessageEvent();
    localMessageEvent.setCancelled(true);
    localMessageEvent.setPacketID(paramString2);
    paramString1.addExtension(localMessageEvent);
    this.con.sendPacket(paramString1);
  }
  
  public void sendComposingNotification(String paramString1, String paramString2)
  {
    paramString1 = new Message(paramString1);
    MessageEvent localMessageEvent = new MessageEvent();
    localMessageEvent.setComposing(true);
    localMessageEvent.setPacketID(paramString2);
    paramString1.addExtension(localMessageEvent);
    this.con.sendPacket(paramString1);
  }
  
  public void sendDeliveredNotification(String paramString1, String paramString2)
  {
    paramString1 = new Message(paramString1);
    MessageEvent localMessageEvent = new MessageEvent();
    localMessageEvent.setDelivered(true);
    localMessageEvent.setPacketID(paramString2);
    paramString1.addExtension(localMessageEvent);
    this.con.sendPacket(paramString1);
  }
  
  public void sendDisplayedNotification(String paramString1, String paramString2)
  {
    paramString1 = new Message(paramString1);
    MessageEvent localMessageEvent = new MessageEvent();
    localMessageEvent.setDisplayed(true);
    localMessageEvent.setPacketID(paramString2);
    paramString1.addExtension(localMessageEvent);
    this.con.sendPacket(paramString1);
  }
}


/* Location:           C:\Users\Administrator\Documents\ds\反编译\AndroidKiller_v1.3.1\projects\Jungledsq_ppc\ProjectSrc\smali\
 * Qualified Name:     org.jivesoftware.smackx.MessageEventManager
 * JD-Core Version:    0.7.0.1
 */