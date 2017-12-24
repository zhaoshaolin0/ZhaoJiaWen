package org.jivesoftware.smackx.workgroup.agent;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.filter.PacketTypeFilter;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.packet.Presence.Type;
import org.jivesoftware.smack.util.StringUtils;
import org.jivesoftware.smackx.workgroup.packet.AgentStatus;
import org.jivesoftware.smackx.workgroup.packet.AgentStatusRequest;
import org.jivesoftware.smackx.workgroup.packet.AgentStatusRequest.Item;

public class AgentRoster
{
  private static final int EVENT_AGENT_ADDED = 0;
  private static final int EVENT_AGENT_REMOVED = 1;
  private static final int EVENT_PRESENCE_CHANGED = 2;
  private XMPPConnection connection;
  private List entries;
  private List listeners;
  private Map presenceMap;
  boolean rosterInitialized = false;
  private String workgroupJID;
  
  AgentRoster(XMPPConnection paramXMPPConnection, String paramString)
  {
    this.connection = paramXMPPConnection;
    this.workgroupJID = paramString;
    this.entries = new ArrayList();
    this.listeners = new ArrayList();
    this.presenceMap = new HashMap();
    Object localObject = new PacketTypeFilter(AgentStatusRequest.class);
    paramXMPPConnection.addPacketListener(new AgentStatusListener(null), (PacketFilter)localObject);
    paramXMPPConnection.addPacketListener(new PresencePacketListener(null), new PacketTypeFilter(Presence.class));
    localObject = new AgentStatusRequest();
    ((AgentStatusRequest)localObject).setTo(paramString);
    paramXMPPConnection.sendPacket((Packet)localObject);
  }
  
  private void fireEvent(int paramInt, Object paramObject)
  {
    for (;;)
    {
      AgentRosterListener[] arrayOfAgentRosterListener;
      int i;
      synchronized (this.listeners)
      {
        arrayOfAgentRosterListener = new AgentRosterListener[this.listeners.size()];
        this.listeners.toArray(arrayOfAgentRosterListener);
        i = 0;
        if (i >= arrayOfAgentRosterListener.length) {
          break;
        }
        switch (paramInt)
        {
        default: 
          i += 1;
        }
      }
      arrayOfAgentRosterListener[i].agentAdded((String)paramObject);
      continue;
      arrayOfAgentRosterListener[i].agentRemoved((String)paramObject);
      continue;
      arrayOfAgentRosterListener[i].presenceChanged((Presence)paramObject);
    }
  }
  
  private String getPresenceMapKey(String paramString)
  {
    String str = paramString;
    if (!contains(paramString)) {
      str = StringUtils.parseBareAddress(paramString).toLowerCase();
    }
    return str;
  }
  
  public void addListener(AgentRosterListener paramAgentRosterListener)
  {
    synchronized (this.listeners)
    {
      if (!this.listeners.contains(paramAgentRosterListener))
      {
        this.listeners.add(paramAgentRosterListener);
        Iterator localIterator = getAgents().iterator();
        while (localIterator.hasNext())
        {
          Object localObject = (String)localIterator.next();
          if (this.entries.contains(localObject))
          {
            paramAgentRosterListener.agentAdded((String)localObject);
            localObject = (Map)this.presenceMap.get(localObject);
            if (localObject != null)
            {
              localObject = ((Map)localObject).values().iterator();
              if (((Iterator)localObject).hasNext()) {
                paramAgentRosterListener.presenceChanged((Presence)((Iterator)localObject).next());
              }
            }
          }
        }
      }
    }
  }
  
  public boolean contains(String paramString)
  {
    if (paramString == null) {
      return false;
    }
    synchronized (this.entries)
    {
      Iterator localIterator = this.entries.iterator();
      while (localIterator.hasNext()) {
        if (((String)localIterator.next()).toLowerCase().equals(paramString.toLowerCase())) {
          return true;
        }
      }
      return false;
    }
  }
  
  public int getAgentCount()
  {
    return this.entries.size();
  }
  
  public Set getAgents()
  {
    HashSet localHashSet = new HashSet();
    synchronized (this.entries)
    {
      Iterator localIterator = this.entries.iterator();
      if (localIterator.hasNext()) {
        localHashSet.add(localIterator.next());
      }
    }
    return Collections.unmodifiableSet(localSet);
  }
  
  public Presence getPresence(String paramString)
  {
    Object localObject1 = getPresenceMapKey(paramString);
    Map localMap = (Map)this.presenceMap.get(localObject1);
    Object localObject2;
    if (localMap == null)
    {
      localObject2 = new Presence(Presence.Type.unavailable);
      ((Presence)localObject2).setFrom(paramString);
    }
    do
    {
      return localObject2;
      Iterator localIterator = localMap.keySet().iterator();
      localObject1 = null;
      while (localIterator.hasNext())
      {
        localObject2 = (Presence)localMap.get(localIterator.next());
        if (localObject1 == null) {
          localObject1 = localObject2;
        } else if (((Presence)localObject2).getPriority() > ((Presence)localObject1).getPriority()) {
          localObject1 = localObject2;
        }
      }
      localObject2 = localObject1;
    } while (localObject1 != null);
    localObject1 = new Presence(Presence.Type.unavailable);
    ((Presence)localObject1).setFrom(paramString);
    return localObject1;
  }
  
  public void reload()
  {
    AgentStatusRequest localAgentStatusRequest = new AgentStatusRequest();
    localAgentStatusRequest.setTo(this.workgroupJID);
    this.connection.sendPacket(localAgentStatusRequest);
  }
  
  public void removeListener(AgentRosterListener paramAgentRosterListener)
  {
    synchronized (this.listeners)
    {
      this.listeners.remove(paramAgentRosterListener);
      return;
    }
  }
  
  private class AgentStatusListener
    implements PacketListener
  {
    private AgentStatusListener() {}
    
    public void processPacket(Packet paramPacket)
    {
      if ((paramPacket instanceof AgentStatusRequest))
      {
        paramPacket = ((AgentStatusRequest)paramPacket).getAgents().iterator();
        while (paramPacket.hasNext())
        {
          Object localObject = (AgentStatusRequest.Item)paramPacket.next();
          String str = ((AgentStatusRequest.Item)localObject).getJID();
          if ("remove".equals(((AgentStatusRequest.Item)localObject).getType()))
          {
            localObject = StringUtils.parseName(StringUtils.parseName(str) + "@" + StringUtils.parseServer(str));
            AgentRoster.this.presenceMap.remove(localObject);
            AgentRoster.this.fireEvent(1, str);
          }
          else
          {
            AgentRoster.this.entries.add(str);
            AgentRoster.this.fireEvent(0, str);
          }
        }
        AgentRoster.this.rosterInitialized = true;
      }
    }
  }
  
  private class PresencePacketListener
    implements PacketListener
  {
    private PresencePacketListener() {}
    
    public void processPacket(Packet paramPacket)
    {
      Presence localPresence = (Presence)paramPacket;
      Object localObject2 = localPresence.getFrom();
      if (localObject2 == null) {
        System.out.println("Presence with no FROM: " + localPresence.toXML());
      }
      String str;
      do
      {
        do
        {
          return;
          str = AgentRoster.this.getPresenceMapKey((String)localObject2);
          if (localPresence.getType() != Presence.Type.available) {
            break;
          }
          ??? = (AgentStatus)localPresence.getExtension("agent-status", "http://jabber.org/protocol/workgroup");
        } while ((??? == null) || (!AgentRoster.this.workgroupJID.equals(((AgentStatus)???).getWorkgroupJID())));
        if (AgentRoster.this.presenceMap.get(str) == null)
        {
          ??? = new HashMap();
          AgentRoster.this.presenceMap.put(str, ???);
        }
        for (;;)
        {
          try
          {
            ((Map)???).put(StringUtils.parseResource((String)localObject2), localPresence);
            synchronized (AgentRoster.this.entries)
            {
              localObject2 = AgentRoster.this.entries.iterator();
              do
              {
                if (!((Iterator)localObject2).hasNext()) {
                  break;
                }
              } while (!((String)((Iterator)localObject2).next()).toLowerCase().equals(StringUtils.parseBareAddress(str).toLowerCase()));
              AgentRoster.this.fireEvent(2, paramPacket);
            }
            ??? = (Map)AgentRoster.this.presenceMap.get(str);
          }
          finally {}
        }
        return;
      } while (localPresence.getType() != Presence.Type.unavailable);
      if (AgentRoster.this.presenceMap.get(str) != null) {}
      synchronized ((Map)AgentRoster.this.presenceMap.get(str))
      {
        ((Map)???).remove(StringUtils.parseResource((String)localObject2));
        if (((Map)???).isEmpty()) {
          AgentRoster.this.presenceMap.remove(str);
        }
        synchronized (AgentRoster.this.entries)
        {
          localObject2 = AgentRoster.this.entries.iterator();
          while (((Iterator)localObject2).hasNext()) {
            if (((String)((Iterator)localObject2).next()).toLowerCase().equals(StringUtils.parseBareAddress(str).toLowerCase())) {
              AgentRoster.this.fireEvent(2, paramPacket);
            }
          }
        }
      }
    }
  }
}


/* Location:           C:\Users\Administrator\Documents\ds\反编译\AndroidKiller_v1.3.1\projects\Jungledsq_ppc\ProjectSrc\smali\
 * Qualified Name:     org.jivesoftware.smackx.workgroup.agent.AgentRoster
 * JD-Core Version:    0.7.0.1
 */