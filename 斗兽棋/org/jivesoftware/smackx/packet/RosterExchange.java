package org.jivesoftware.smackx.packet;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.RosterEntry;
import org.jivesoftware.smack.RosterGroup;
import org.jivesoftware.smack.packet.PacketExtension;
import org.jivesoftware.smackx.RemoteRosterEntry;

public class RosterExchange
  implements PacketExtension
{
  private List remoteRosterEntries = new ArrayList();
  
  public RosterExchange() {}
  
  public RosterExchange(Roster paramRoster)
  {
    paramRoster = paramRoster.getEntries().iterator();
    while (paramRoster.hasNext()) {
      addRosterEntry((RosterEntry)paramRoster.next());
    }
  }
  
  public void addRosterEntry(RosterEntry paramRosterEntry)
  {
    Object localObject = new ArrayList();
    Iterator localIterator = paramRosterEntry.getGroups().iterator();
    while (localIterator.hasNext()) {
      ((List)localObject).add(((RosterGroup)localIterator.next()).getName());
    }
    localObject = (String[])((List)localObject).toArray(new String[((List)localObject).size()]);
    addRosterEntry(new RemoteRosterEntry(paramRosterEntry.getUser(), paramRosterEntry.getName(), (String[])localObject));
  }
  
  public void addRosterEntry(RemoteRosterEntry paramRemoteRosterEntry)
  {
    synchronized (this.remoteRosterEntries)
    {
      this.remoteRosterEntries.add(paramRemoteRosterEntry);
      return;
    }
  }
  
  public String getElementName()
  {
    return "x";
  }
  
  public int getEntryCount()
  {
    return this.remoteRosterEntries.size();
  }
  
  public String getNamespace()
  {
    return "jabber:x:roster";
  }
  
  public Iterator getRosterEntries()
  {
    synchronized (this.remoteRosterEntries)
    {
      Iterator localIterator = Collections.unmodifiableList(new ArrayList(this.remoteRosterEntries)).iterator();
      return localIterator;
    }
  }
  
  public String toXML()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("<").append(getElementName()).append(" xmlns=\"").append(getNamespace()).append("\">");
    Iterator localIterator = getRosterEntries();
    while (localIterator.hasNext()) {
      localStringBuilder.append(((RemoteRosterEntry)localIterator.next()).toXML());
    }
    localStringBuilder.append("</").append(getElementName()).append(">");
    return localStringBuilder.toString();
  }
}


/* Location:           C:\Users\Administrator\Documents\ds\反编译\AndroidKiller_v1.3.1\projects\Jungledsq_ppc\ProjectSrc\smali\
 * Qualified Name:     org.jivesoftware.smackx.packet.RosterExchange
 * JD-Core Version:    0.7.0.1
 */