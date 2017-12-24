package org.jivesoftware.smack;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import org.jivesoftware.smack.filter.PacketIDFilter;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.packet.IQ.Type;
import org.jivesoftware.smack.packet.RosterPacket;
import org.jivesoftware.smack.packet.RosterPacket.Item;
import org.jivesoftware.smack.util.StringUtils;

public class RosterGroup
{
  private XMPPConnection connection;
  private final List<RosterEntry> entries;
  private String name;
  
  RosterGroup(String paramString, XMPPConnection paramXMPPConnection)
  {
    this.name = paramString;
    this.connection = paramXMPPConnection;
    this.entries = new ArrayList();
  }
  
  public void addEntry(RosterEntry paramRosterEntry)
    throws XMPPException
  {
    Object localObject1 = null;
    synchronized (this.entries)
    {
      if (!this.entries.contains(paramRosterEntry))
      {
        RosterPacket localRosterPacket = new RosterPacket();
        localRosterPacket.setType(IQ.Type.SET);
        localObject1 = RosterEntry.toRosterItem(paramRosterEntry);
        ((RosterPacket.Item)localObject1).addGroupName(getName());
        localRosterPacket.addRosterItem((RosterPacket.Item)localObject1);
        localObject1 = this.connection.createPacketCollector(new PacketIDFilter(localRosterPacket.getPacketID()));
        this.connection.sendPacket(localRosterPacket);
      }
      if (localObject1 == null) {
        return;
      }
      ??? = (IQ)((PacketCollector)localObject1).nextResult(SmackConfiguration.getPacketReplyTimeout());
      ((PacketCollector)localObject1).cancel();
      if (??? == null) {
        throw new XMPPException("No response from the server.");
      }
    }
    if (((IQ)???).getType() == IQ.Type.ERROR) {
      throw new XMPPException(((IQ)???).getError());
    }
    addEntryLocal(paramRosterEntry);
  }
  
  void addEntryLocal(RosterEntry paramRosterEntry)
  {
    synchronized (this.entries)
    {
      this.entries.remove(paramRosterEntry);
      this.entries.add(paramRosterEntry);
      return;
    }
  }
  
  public boolean contains(String paramString)
  {
    return getEntry(paramString) != null;
  }
  
  public boolean contains(RosterEntry paramRosterEntry)
  {
    synchronized (this.entries)
    {
      boolean bool = this.entries.contains(paramRosterEntry);
      return bool;
    }
  }
  
  public Collection<RosterEntry> getEntries()
  {
    synchronized (this.entries)
    {
      List localList2 = Collections.unmodifiableList(new ArrayList(this.entries));
      return localList2;
    }
  }
  
  public RosterEntry getEntry(String arg1)
  {
    if (??? == null) {
      return null;
    }
    String str = StringUtils.parseBareAddress(???).toLowerCase();
    synchronized (this.entries)
    {
      Iterator localIterator = this.entries.iterator();
      while (localIterator.hasNext())
      {
        RosterEntry localRosterEntry = (RosterEntry)localIterator.next();
        if (localRosterEntry.getUser().equals(str)) {
          return localRosterEntry;
        }
      }
      return null;
    }
  }
  
  public int getEntryCount()
  {
    synchronized (this.entries)
    {
      int i = this.entries.size();
      return i;
    }
  }
  
  public String getName()
  {
    return this.name;
  }
  
  public void removeEntry(RosterEntry paramRosterEntry)
    throws XMPPException
  {
    Object localObject1 = null;
    synchronized (this.entries)
    {
      if (this.entries.contains(paramRosterEntry))
      {
        RosterPacket localRosterPacket = new RosterPacket();
        localRosterPacket.setType(IQ.Type.SET);
        localObject1 = RosterEntry.toRosterItem(paramRosterEntry);
        ((RosterPacket.Item)localObject1).removeGroupName(getName());
        localRosterPacket.addRosterItem((RosterPacket.Item)localObject1);
        localObject1 = this.connection.createPacketCollector(new PacketIDFilter(localRosterPacket.getPacketID()));
        this.connection.sendPacket(localRosterPacket);
      }
      if (localObject1 == null) {
        return;
      }
      ??? = (IQ)((PacketCollector)localObject1).nextResult(SmackConfiguration.getPacketReplyTimeout());
      ((PacketCollector)localObject1).cancel();
      if (??? == null) {
        throw new XMPPException("No response from the server.");
      }
    }
    if (((IQ)???).getType() == IQ.Type.ERROR) {
      throw new XMPPException(((IQ)???).getError());
    }
    removeEntryLocal(paramRosterEntry);
  }
  
  void removeEntryLocal(RosterEntry paramRosterEntry)
  {
    synchronized (this.entries)
    {
      if (this.entries.contains(paramRosterEntry)) {
        this.entries.remove(paramRosterEntry);
      }
      return;
    }
  }
  
  public void setName(String paramString)
  {
    synchronized (this.entries)
    {
      Iterator localIterator = this.entries.iterator();
      if (localIterator.hasNext())
      {
        Object localObject = (RosterEntry)localIterator.next();
        RosterPacket localRosterPacket = new RosterPacket();
        localRosterPacket.setType(IQ.Type.SET);
        localObject = RosterEntry.toRosterItem((RosterEntry)localObject);
        ((RosterPacket.Item)localObject).removeGroupName(this.name);
        ((RosterPacket.Item)localObject).addGroupName(paramString);
        localRosterPacket.addRosterItem((RosterPacket.Item)localObject);
        this.connection.sendPacket(localRosterPacket);
      }
    }
  }
}


/* Location:           C:\Users\Administrator\Documents\ds\反编译\AndroidKiller_v1.3.1\projects\Jungledsq_ppc\ProjectSrc\smali\
 * Qualified Name:     org.jivesoftware.smack.RosterGroup
 * JD-Core Version:    0.7.0.1
 */