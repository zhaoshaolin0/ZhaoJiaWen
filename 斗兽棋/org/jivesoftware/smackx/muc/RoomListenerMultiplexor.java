package org.jivesoftware.smackx.muc;

import java.lang.ref.WeakReference;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.ConcurrentHashMap;
import org.jivesoftware.smack.ConnectionListener;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.util.StringUtils;

class RoomListenerMultiplexor
  implements ConnectionListener
{
  private static final Map<XMPPConnection, WeakReference<RoomListenerMultiplexor>> monitors = new WeakHashMap();
  private XMPPConnection connection;
  private RoomMultiplexFilter filter;
  private RoomMultiplexListener listener;
  
  private RoomListenerMultiplexor(XMPPConnection paramXMPPConnection, RoomMultiplexFilter paramRoomMultiplexFilter, RoomMultiplexListener paramRoomMultiplexListener)
  {
    if (paramXMPPConnection == null) {
      throw new IllegalArgumentException("Connection is null");
    }
    if (paramRoomMultiplexFilter == null) {
      throw new IllegalArgumentException("Filter is null");
    }
    if (paramRoomMultiplexListener == null) {
      throw new IllegalArgumentException("Listener is null");
    }
    this.connection = paramXMPPConnection;
    this.filter = paramRoomMultiplexFilter;
    this.listener = paramRoomMultiplexListener;
  }
  
  private void cancel()
  {
    this.connection.removeConnectionListener(this);
    this.connection.removePacketListener(this.listener);
  }
  
  public static RoomListenerMultiplexor getRoomMultiplexor(XMPPConnection paramXMPPConnection)
  {
    synchronized (monitors)
    {
      if (!monitors.containsKey(paramXMPPConnection))
      {
        RoomListenerMultiplexor localRoomListenerMultiplexor = new RoomListenerMultiplexor(paramXMPPConnection, new RoomMultiplexFilter(null), new RoomMultiplexListener(null));
        localRoomListenerMultiplexor.init();
        monitors.put(paramXMPPConnection, new WeakReference(localRoomListenerMultiplexor));
      }
      paramXMPPConnection = (RoomListenerMultiplexor)((WeakReference)monitors.get(paramXMPPConnection)).get();
      return paramXMPPConnection;
    }
  }
  
  public void addRoom(String paramString, PacketMultiplexListener paramPacketMultiplexListener)
  {
    this.filter.addRoom(paramString);
    this.listener.addRoom(paramString, paramPacketMultiplexListener);
  }
  
  public void connectionClosed()
  {
    cancel();
  }
  
  public void connectionClosedOnError(Exception paramException)
  {
    cancel();
  }
  
  public void init()
  {
    this.connection.addConnectionListener(this);
    this.connection.addPacketListener(this.listener, this.filter);
  }
  
  public void reconnectingIn(int paramInt) {}
  
  public void reconnectionFailed(Exception paramException) {}
  
  public void reconnectionSuccessful() {}
  
  public void removeRoom(String paramString)
  {
    this.filter.removeRoom(paramString);
    this.listener.removeRoom(paramString);
  }
  
  private static class RoomMultiplexFilter
    implements PacketFilter
  {
    private Map<String, String> roomAddressTable = new ConcurrentHashMap();
    
    public boolean accept(Packet paramPacket)
    {
      paramPacket = paramPacket.getFrom();
      if (paramPacket == null) {
        return false;
      }
      return this.roomAddressTable.containsKey(StringUtils.parseBareAddress(paramPacket).toLowerCase());
    }
    
    public void addRoom(String paramString)
    {
      if (paramString == null) {
        return;
      }
      this.roomAddressTable.put(paramString.toLowerCase(), paramString);
    }
    
    public void removeRoom(String paramString)
    {
      if (paramString == null) {
        return;
      }
      this.roomAddressTable.remove(paramString.toLowerCase());
    }
  }
  
  private static class RoomMultiplexListener
    implements PacketListener
  {
    private Map<String, PacketMultiplexListener> roomListenersByAddress = new ConcurrentHashMap();
    
    public void addRoom(String paramString, PacketMultiplexListener paramPacketMultiplexListener)
    {
      if (paramString == null) {
        return;
      }
      this.roomListenersByAddress.put(paramString.toLowerCase(), paramPacketMultiplexListener);
    }
    
    public void processPacket(Packet paramPacket)
    {
      Object localObject = paramPacket.getFrom();
      if (localObject == null) {}
      do
      {
        return;
        localObject = (PacketMultiplexListener)this.roomListenersByAddress.get(StringUtils.parseBareAddress((String)localObject).toLowerCase());
      } while (localObject == null);
      ((PacketMultiplexListener)localObject).processPacket(paramPacket);
    }
    
    public void removeRoom(String paramString)
    {
      if (paramString == null) {
        return;
      }
      this.roomListenersByAddress.remove(paramString.toLowerCase());
    }
  }
}


/* Location:           C:\Users\Administrator\Documents\ds\反编译\AndroidKiller_v1.3.1\projects\Jungledsq_ppc\ProjectSrc\smali\
 * Qualified Name:     org.jivesoftware.smackx.muc.RoomListenerMultiplexor
 * JD-Core Version:    0.7.0.1
 */