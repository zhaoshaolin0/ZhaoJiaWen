package org.jivesoftware.smackx;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.jivesoftware.smack.ConnectionCreationListener;
import org.jivesoftware.smack.ConnectionListener;
import org.jivesoftware.smack.PacketCollector;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.SmackConfiguration;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.filter.PacketIDFilter;
import org.jivesoftware.smack.filter.PacketTypeFilter;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.packet.IQ.Type;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.packet.XMPPError;
import org.jivesoftware.smack.packet.XMPPError.Condition;
import org.jivesoftware.smackx.packet.DataForm;
import org.jivesoftware.smackx.packet.DiscoverInfo;
import org.jivesoftware.smackx.packet.DiscoverInfo.Identity;
import org.jivesoftware.smackx.packet.DiscoverItems;
import org.jivesoftware.smackx.packet.DiscoverItems.Item;

public class ServiceDiscoveryManager
{
  private static String identityName = "Smack";
  private static String identityType = "pc";
  private static Map<XMPPConnection, ServiceDiscoveryManager> instances = new ConcurrentHashMap();
  private XMPPConnection connection;
  private DataForm extendedInfo = null;
  private final List<String> features = new ArrayList();
  private Map<String, NodeInformationProvider> nodeInformationProviders = new ConcurrentHashMap();
  
  static
  {
    XMPPConnection.addConnectionCreationListener(new ConnectionCreationListener()
    {
      public void connectionCreated(XMPPConnection paramAnonymousXMPPConnection)
      {
        new ServiceDiscoveryManager(paramAnonymousXMPPConnection);
      }
    });
  }
  
  public ServiceDiscoveryManager(XMPPConnection paramXMPPConnection)
  {
    this.connection = paramXMPPConnection;
    init();
  }
  
  public static String getIdentityName()
  {
    return identityName;
  }
  
  public static String getIdentityType()
  {
    return identityType;
  }
  
  public static ServiceDiscoveryManager getInstanceFor(XMPPConnection paramXMPPConnection)
  {
    return (ServiceDiscoveryManager)instances.get(paramXMPPConnection);
  }
  
  private NodeInformationProvider getNodeInformationProvider(String paramString)
  {
    if (paramString == null) {
      return null;
    }
    return (NodeInformationProvider)this.nodeInformationProviders.get(paramString);
  }
  
  private void init()
  {
    instances.put(this.connection, this);
    this.connection.addConnectionListener(new ConnectionListener()
    {
      public void connectionClosed()
      {
        ServiceDiscoveryManager.instances.remove(ServiceDiscoveryManager.this.connection);
      }
      
      public void connectionClosedOnError(Exception paramAnonymousException) {}
      
      public void reconnectingIn(int paramAnonymousInt) {}
      
      public void reconnectionFailed(Exception paramAnonymousException) {}
      
      public void reconnectionSuccessful() {}
    });
    PacketTypeFilter localPacketTypeFilter = new PacketTypeFilter(DiscoverItems.class);
    Object localObject = new PacketListener()
    {
      public void processPacket(Packet paramAnonymousPacket)
      {
        Object localObject = (DiscoverItems)paramAnonymousPacket;
        if ((localObject != null) && (((DiscoverItems)localObject).getType() == IQ.Type.GET))
        {
          paramAnonymousPacket = new DiscoverItems();
          paramAnonymousPacket.setType(IQ.Type.RESULT);
          paramAnonymousPacket.setTo(((DiscoverItems)localObject).getFrom());
          paramAnonymousPacket.setPacketID(((DiscoverItems)localObject).getPacketID());
          paramAnonymousPacket.setNode(((DiscoverItems)localObject).getNode());
          NodeInformationProvider localNodeInformationProvider = ServiceDiscoveryManager.this.getNodeInformationProvider(((DiscoverItems)localObject).getNode());
          if (localNodeInformationProvider != null)
          {
            localObject = localNodeInformationProvider.getNodeItems();
            if (localObject != null)
            {
              localObject = ((List)localObject).iterator();
              while (((Iterator)localObject).hasNext()) {
                paramAnonymousPacket.addItem((DiscoverItems.Item)((Iterator)localObject).next());
              }
            }
          }
          else if (((DiscoverItems)localObject).getNode() != null)
          {
            paramAnonymousPacket.setType(IQ.Type.ERROR);
            paramAnonymousPacket.setError(new XMPPError(XMPPError.Condition.item_not_found));
          }
          ServiceDiscoveryManager.this.connection.sendPacket(paramAnonymousPacket);
        }
      }
    };
    this.connection.addPacketListener((PacketListener)localObject, localPacketTypeFilter);
    localPacketTypeFilter = new PacketTypeFilter(DiscoverInfo.class);
    localObject = new PacketListener()
    {
      public void processPacket(Packet paramAnonymousPacket)
      {
        ??? = (DiscoverInfo)paramAnonymousPacket;
        Object localObject2;
        if ((??? != null) && (((DiscoverInfo)???).getType() == IQ.Type.GET))
        {
          paramAnonymousPacket = new DiscoverInfo();
          paramAnonymousPacket.setType(IQ.Type.RESULT);
          paramAnonymousPacket.setTo(((DiscoverInfo)???).getFrom());
          paramAnonymousPacket.setPacketID(((DiscoverInfo)???).getPacketID());
          paramAnonymousPacket.setNode(((DiscoverInfo)???).getNode());
          if (((DiscoverInfo)???).getNode() != null) {
            break label173;
          }
          ??? = new DiscoverInfo.Identity("client", ServiceDiscoveryManager.getIdentityName());
          ((DiscoverInfo.Identity)???).setType(ServiceDiscoveryManager.getIdentityType());
          paramAnonymousPacket.addIdentity((DiscoverInfo.Identity)???);
          synchronized (ServiceDiscoveryManager.this.features)
          {
            localObject2 = ServiceDiscoveryManager.this.getFeatures();
            if (((Iterator)localObject2).hasNext()) {
              paramAnonymousPacket.addFeature((String)((Iterator)localObject2).next());
            }
          }
          if (ServiceDiscoveryManager.this.extendedInfo != null) {
            paramAnonymousPacket.addExtension(ServiceDiscoveryManager.this.extendedInfo);
          }
        }
        for (;;)
        {
          ServiceDiscoveryManager.this.connection.sendPacket(paramAnonymousPacket);
          return;
          label173:
          ??? = ServiceDiscoveryManager.this.getNodeInformationProvider(((DiscoverInfo)???).getNode());
          if (??? != null)
          {
            localObject2 = ((NodeInformationProvider)???).getNodeFeatures();
            if (localObject2 != null)
            {
              localObject2 = ((List)localObject2).iterator();
              while (((Iterator)localObject2).hasNext()) {
                paramAnonymousPacket.addFeature((String)((Iterator)localObject2).next());
              }
            }
            ??? = ((NodeInformationProvider)???).getNodeIdentities();
            if (??? != null)
            {
              ??? = ((List)???).iterator();
              while (((Iterator)???).hasNext()) {
                paramAnonymousPacket.addIdentity((DiscoverInfo.Identity)((Iterator)???).next());
              }
            }
          }
          else
          {
            paramAnonymousPacket.setType(IQ.Type.ERROR);
            paramAnonymousPacket.setError(new XMPPError(XMPPError.Condition.item_not_found));
          }
        }
      }
    };
    this.connection.addPacketListener((PacketListener)localObject, localPacketTypeFilter);
  }
  
  public static void setIdentityName(String paramString)
  {
    identityName = paramString;
  }
  
  public static void setIdentityType(String paramString)
  {
    identityType = paramString;
  }
  
  public void addFeature(String paramString)
  {
    synchronized (this.features)
    {
      this.features.add(paramString);
      return;
    }
  }
  
  public boolean canPublishItems(String paramString)
    throws XMPPException
  {
    return discoverInfo(paramString).containsFeature("http://jabber.org/protocol/disco#publish");
  }
  
  public DiscoverInfo discoverInfo(String paramString)
    throws XMPPException
  {
    return discoverInfo(paramString, null);
  }
  
  public DiscoverInfo discoverInfo(String paramString1, String paramString2)
    throws XMPPException
  {
    DiscoverInfo localDiscoverInfo = new DiscoverInfo();
    localDiscoverInfo.setType(IQ.Type.GET);
    localDiscoverInfo.setTo(paramString1);
    localDiscoverInfo.setNode(paramString2);
    paramString1 = this.connection.createPacketCollector(new PacketIDFilter(localDiscoverInfo.getPacketID()));
    this.connection.sendPacket(localDiscoverInfo);
    paramString2 = (IQ)paramString1.nextResult(SmackConfiguration.getPacketReplyTimeout());
    paramString1.cancel();
    if (paramString2 == null) {
      throw new XMPPException("No response from the server.");
    }
    if (paramString2.getType() == IQ.Type.ERROR) {
      throw new XMPPException(paramString2.getError());
    }
    return (DiscoverInfo)paramString2;
  }
  
  public DiscoverItems discoverItems(String paramString)
    throws XMPPException
  {
    return discoverItems(paramString, null);
  }
  
  public DiscoverItems discoverItems(String paramString1, String paramString2)
    throws XMPPException
  {
    DiscoverItems localDiscoverItems = new DiscoverItems();
    localDiscoverItems.setType(IQ.Type.GET);
    localDiscoverItems.setTo(paramString1);
    localDiscoverItems.setNode(paramString2);
    paramString1 = this.connection.createPacketCollector(new PacketIDFilter(localDiscoverItems.getPacketID()));
    this.connection.sendPacket(localDiscoverItems);
    paramString2 = (IQ)paramString1.nextResult(SmackConfiguration.getPacketReplyTimeout());
    paramString1.cancel();
    if (paramString2 == null) {
      throw new XMPPException("No response from the server.");
    }
    if (paramString2.getType() == IQ.Type.ERROR) {
      throw new XMPPException(paramString2.getError());
    }
    return (DiscoverItems)paramString2;
  }
  
  public Iterator<String> getFeatures()
  {
    synchronized (this.features)
    {
      Iterator localIterator = Collections.unmodifiableList(new ArrayList(this.features)).iterator();
      return localIterator;
    }
  }
  
  public boolean includesFeature(String paramString)
  {
    synchronized (this.features)
    {
      boolean bool = this.features.contains(paramString);
      return bool;
    }
  }
  
  public void publishItems(String paramString1, String paramString2, DiscoverItems paramDiscoverItems)
    throws XMPPException
  {
    paramDiscoverItems.setType(IQ.Type.SET);
    paramDiscoverItems.setTo(paramString1);
    paramDiscoverItems.setNode(paramString2);
    paramString1 = this.connection.createPacketCollector(new PacketIDFilter(paramDiscoverItems.getPacketID()));
    this.connection.sendPacket(paramDiscoverItems);
    paramString2 = (IQ)paramString1.nextResult(SmackConfiguration.getPacketReplyTimeout());
    paramString1.cancel();
    if (paramString2 == null) {
      throw new XMPPException("No response from the server.");
    }
    if (paramString2.getType() == IQ.Type.ERROR) {
      throw new XMPPException(paramString2.getError());
    }
  }
  
  public void publishItems(String paramString, DiscoverItems paramDiscoverItems)
    throws XMPPException
  {
    publishItems(paramString, null, paramDiscoverItems);
  }
  
  public void removeExtendedInfo()
  {
    this.extendedInfo = null;
  }
  
  public void removeFeature(String paramString)
  {
    synchronized (this.features)
    {
      this.features.remove(paramString);
      return;
    }
  }
  
  public void removeNodeInformationProvider(String paramString)
  {
    this.nodeInformationProviders.remove(paramString);
  }
  
  public void setExtendedInfo(DataForm paramDataForm)
  {
    this.extendedInfo = paramDataForm;
  }
  
  public void setNodeInformationProvider(String paramString, NodeInformationProvider paramNodeInformationProvider)
  {
    this.nodeInformationProviders.put(paramString, paramNodeInformationProvider);
  }
}


/* Location:           C:\Users\Administrator\Documents\ds\反编译\AndroidKiller_v1.3.1\projects\Jungledsq_ppc\ProjectSrc\smali\
 * Qualified Name:     org.jivesoftware.smackx.ServiceDiscoveryManager
 * JD-Core Version:    0.7.0.1
 */