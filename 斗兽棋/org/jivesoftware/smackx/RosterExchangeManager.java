package org.jivesoftware.smackx;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.RosterEntry;
import org.jivesoftware.smack.RosterGroup;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.filter.PacketExtensionFilter;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smackx.packet.RosterExchange;

public class RosterExchangeManager
{
  private XMPPConnection con;
  private PacketFilter packetFilter = new PacketExtensionFilter("x", "jabber:x:roster");
  private PacketListener packetListener;
  private List rosterExchangeListeners = new ArrayList();
  
  public RosterExchangeManager(XMPPConnection paramXMPPConnection)
  {
    this.con = paramXMPPConnection;
    init();
  }
  
  private void fireRosterExchangeListeners(String paramString, Iterator paramIterator)
  {
    synchronized (this.rosterExchangeListeners)
    {
      RosterExchangeListener[] arrayOfRosterExchangeListener = new RosterExchangeListener[this.rosterExchangeListeners.size()];
      this.rosterExchangeListeners.toArray(arrayOfRosterExchangeListener);
      int i = 0;
      if (i < arrayOfRosterExchangeListener.length)
      {
        arrayOfRosterExchangeListener[i].entriesReceived(paramString, paramIterator);
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
        RosterExchange localRosterExchange = (RosterExchange)paramAnonymousPacket.getExtension("x", "jabber:x:roster");
        RosterExchangeManager.this.fireRosterExchangeListeners(paramAnonymousPacket.getFrom(), localRosterExchange.getRosterEntries());
      }
    };
    this.con.addPacketListener(this.packetListener, this.packetFilter);
  }
  
  public void addRosterListener(RosterExchangeListener paramRosterExchangeListener)
  {
    synchronized (this.rosterExchangeListeners)
    {
      if (!this.rosterExchangeListeners.contains(paramRosterExchangeListener)) {
        this.rosterExchangeListeners.add(paramRosterExchangeListener);
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
  
  public void removeRosterListener(RosterExchangeListener paramRosterExchangeListener)
  {
    synchronized (this.rosterExchangeListeners)
    {
      this.rosterExchangeListeners.remove(paramRosterExchangeListener);
      return;
    }
  }
  
  public void send(Roster paramRoster, String paramString)
  {
    paramString = new Message(paramString);
    paramString.addExtension(new RosterExchange(paramRoster));
    this.con.sendPacket(paramString);
  }
  
  public void send(RosterEntry paramRosterEntry, String paramString)
  {
    paramString = new Message(paramString);
    RosterExchange localRosterExchange = new RosterExchange();
    localRosterExchange.addRosterEntry(paramRosterEntry);
    paramString.addExtension(localRosterExchange);
    this.con.sendPacket(paramString);
  }
  
  public void send(RosterGroup paramRosterGroup, String paramString)
  {
    paramString = new Message(paramString);
    RosterExchange localRosterExchange = new RosterExchange();
    paramRosterGroup = paramRosterGroup.getEntries().iterator();
    while (paramRosterGroup.hasNext()) {
      localRosterExchange.addRosterEntry((RosterEntry)paramRosterGroup.next());
    }
    paramString.addExtension(localRosterExchange);
    this.con.sendPacket(paramString);
  }
}


/* Location:           C:\Users\Administrator\Documents\ds\反编译\AndroidKiller_v1.3.1\projects\Jungledsq_ppc\ProjectSrc\smali\
 * Qualified Name:     org.jivesoftware.smackx.RosterExchangeManager
 * JD-Core Version:    0.7.0.1
 */