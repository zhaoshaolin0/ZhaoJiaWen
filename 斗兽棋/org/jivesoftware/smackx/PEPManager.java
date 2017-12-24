package org.jivesoftware.smackx;

import java.util.ArrayList;
import java.util.List;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.filter.PacketExtensionFilter;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.packet.IQ.Type;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smackx.packet.PEPEvent;
import org.jivesoftware.smackx.packet.PEPItem;
import org.jivesoftware.smackx.packet.PEPPubSub;

public class PEPManager
{
  private XMPPConnection connection;
  private PacketFilter packetFilter = new PacketExtensionFilter("event", "http://jabber.org/protocol/pubsub#event");
  private PacketListener packetListener;
  private List<PEPListener> pepListeners = new ArrayList();
  
  public PEPManager(XMPPConnection paramXMPPConnection)
  {
    this.connection = paramXMPPConnection;
    init();
  }
  
  private void firePEPListeners(String paramString, PEPEvent paramPEPEvent)
  {
    synchronized (this.pepListeners)
    {
      PEPListener[] arrayOfPEPListener = new PEPListener[this.pepListeners.size()];
      this.pepListeners.toArray(arrayOfPEPListener);
      int i = 0;
      if (i < arrayOfPEPListener.length)
      {
        arrayOfPEPListener[i].eventReceived(paramString, paramPEPEvent);
        i += 1;
      }
    }
  }
  
  private void init()
  {
    this.packetListener = new PacketListener()
    {
      public void processPacket(Packet paramAnonymousPacket)
      {
        paramAnonymousPacket = (Message)paramAnonymousPacket;
        PEPEvent localPEPEvent = (PEPEvent)paramAnonymousPacket.getExtension("event", "http://jabber.org/protocol/pubsub#event");
        PEPManager.this.firePEPListeners(paramAnonymousPacket.getFrom(), localPEPEvent);
      }
    };
    this.connection.addPacketListener(this.packetListener, this.packetFilter);
  }
  
  public void addPEPListener(PEPListener paramPEPListener)
  {
    synchronized (this.pepListeners)
    {
      if (!this.pepListeners.contains(paramPEPListener)) {
        this.pepListeners.add(paramPEPListener);
      }
      return;
    }
  }
  
  public void destroy()
  {
    if (this.connection != null) {
      this.connection.removePacketListener(this.packetListener);
    }
  }
  
  public void finalize()
  {
    destroy();
  }
  
  public void publish(PEPItem paramPEPItem)
  {
    paramPEPItem = new PEPPubSub(paramPEPItem);
    paramPEPItem.setType(IQ.Type.SET);
    this.connection.sendPacket(paramPEPItem);
  }
  
  public void removePEPListener(PEPListener paramPEPListener)
  {
    synchronized (this.pepListeners)
    {
      this.pepListeners.remove(paramPEPListener);
      return;
    }
  }
}


/* Location:           C:\Users\Administrator\Documents\ds\反编译\AndroidKiller_v1.3.1\projects\Jungledsq_ppc\ProjectSrc\smali\
 * Qualified Name:     org.jivesoftware.smackx.PEPManager
 * JD-Core Version:    0.7.0.1
 */