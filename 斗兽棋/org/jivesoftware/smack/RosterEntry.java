package org.jivesoftware.smack;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import org.jivesoftware.smack.packet.IQ.Type;
import org.jivesoftware.smack.packet.RosterPacket;
import org.jivesoftware.smack.packet.RosterPacket.Item;
import org.jivesoftware.smack.packet.RosterPacket.ItemStatus;
import org.jivesoftware.smack.packet.RosterPacket.ItemType;

public class RosterEntry
{
  private XMPPConnection connection;
  private String name;
  private RosterPacket.ItemStatus status;
  private RosterPacket.ItemType type;
  private String user;
  
  RosterEntry(String paramString1, String paramString2, RosterPacket.ItemType paramItemType, RosterPacket.ItemStatus paramItemStatus, XMPPConnection paramXMPPConnection)
  {
    this.user = paramString1;
    this.name = paramString2;
    this.type = paramItemType;
    this.status = paramItemStatus;
    this.connection = paramXMPPConnection;
  }
  
  static RosterPacket.Item toRosterItem(RosterEntry paramRosterEntry)
  {
    RosterPacket.Item localItem = new RosterPacket.Item(paramRosterEntry.getUser(), paramRosterEntry.getName());
    localItem.setItemType(paramRosterEntry.getType());
    localItem.setItemStatus(paramRosterEntry.getStatus());
    paramRosterEntry = paramRosterEntry.getGroups().iterator();
    while (paramRosterEntry.hasNext()) {
      localItem.addGroupName(((RosterGroup)paramRosterEntry.next()).getName());
    }
    return localItem;
  }
  
  public boolean equals(Object paramObject)
  {
    if (this == paramObject) {
      return true;
    }
    if ((paramObject != null) && ((paramObject instanceof RosterEntry))) {
      return this.user.equals(((RosterEntry)paramObject).getUser());
    }
    return false;
  }
  
  public Collection<RosterGroup> getGroups()
  {
    ArrayList localArrayList = new ArrayList();
    Iterator localIterator = this.connection.roster.getGroups().iterator();
    while (localIterator.hasNext())
    {
      RosterGroup localRosterGroup = (RosterGroup)localIterator.next();
      if (localRosterGroup.contains(this)) {
        localArrayList.add(localRosterGroup);
      }
    }
    return Collections.unmodifiableCollection(localArrayList);
  }
  
  public String getName()
  {
    return this.name;
  }
  
  public RosterPacket.ItemStatus getStatus()
  {
    return this.status;
  }
  
  public RosterPacket.ItemType getType()
  {
    return this.type;
  }
  
  public String getUser()
  {
    return this.user;
  }
  
  public void setName(String paramString)
  {
    if ((paramString != null) && (paramString.equals(this.name))) {
      return;
    }
    this.name = paramString;
    paramString = new RosterPacket();
    paramString.setType(IQ.Type.SET);
    paramString.addRosterItem(toRosterItem(this));
    this.connection.sendPacket(paramString);
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    if (this.name != null) {
      localStringBuilder.append(this.name).append(": ");
    }
    localStringBuilder.append(this.user);
    Object localObject = getGroups();
    if (!((Collection)localObject).isEmpty())
    {
      localStringBuilder.append(" [");
      localObject = ((Collection)localObject).iterator();
      localStringBuilder.append(((RosterGroup)((Iterator)localObject).next()).getName());
      while (((Iterator)localObject).hasNext())
      {
        localStringBuilder.append(", ");
        localStringBuilder.append(((RosterGroup)((Iterator)localObject).next()).getName());
      }
      localStringBuilder.append("]");
    }
    return localStringBuilder.toString();
  }
  
  void updateState(String paramString, RosterPacket.ItemType paramItemType, RosterPacket.ItemStatus paramItemStatus)
  {
    this.name = paramString;
    this.type = paramItemType;
    this.status = paramItemStatus;
  }
}


/* Location:           C:\Users\Administrator\Documents\ds\反编译\AndroidKiller_v1.3.1\projects\Jungledsq_ppc\ProjectSrc\smali\
 * Qualified Name:     org.jivesoftware.smack.RosterEntry
 * JD-Core Version:    0.7.0.1
 */