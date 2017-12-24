package org.jivesoftware.smackx.commands;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import org.jivesoftware.smack.ConnectionCreationListener;
import org.jivesoftware.smack.ConnectionListener;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.filter.PacketTypeFilter;
import org.jivesoftware.smack.packet.IQ.Type;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.packet.XMPPError;
import org.jivesoftware.smack.packet.XMPPError.Condition;
import org.jivesoftware.smack.packet.XMPPError.Type;
import org.jivesoftware.smack.util.StringUtils;
import org.jivesoftware.smackx.Form;
import org.jivesoftware.smackx.NodeInformationProvider;
import org.jivesoftware.smackx.ServiceDiscoveryManager;
import org.jivesoftware.smackx.packet.AdHocCommandData;
import org.jivesoftware.smackx.packet.AdHocCommandData.SpecificError;
import org.jivesoftware.smackx.packet.DiscoverInfo.Identity;
import org.jivesoftware.smackx.packet.DiscoverItems;
import org.jivesoftware.smackx.packet.DiscoverItems.Item;

public class AdHocCommandManager
{
  private static final String DISCO_NAMESPACE = "http://jabber.org/protocol/commands";
  private static final int SESSION_TIMEOUT = 120;
  private static final String discoNode = "http://jabber.org/protocol/commands";
  private static Map<XMPPConnection, AdHocCommandManager> instances = new ConcurrentHashMap();
  private Map<String, AdHocCommandInfo> commands = new ConcurrentHashMap();
  private XMPPConnection connection;
  private Map<String, LocalCommand> executingCommands = new ConcurrentHashMap();
  private Thread sessionsSweeper;
  
  static
  {
    XMPPConnection.addConnectionCreationListener(new ConnectionCreationListener()
    {
      public void connectionCreated(XMPPConnection paramAnonymousXMPPConnection)
      {
        new AdHocCommandManager(paramAnonymousXMPPConnection, null);
      }
    });
  }
  
  private AdHocCommandManager(XMPPConnection paramXMPPConnection)
  {
    this.connection = paramXMPPConnection;
    init();
  }
  
  public static AdHocCommandManager getAddHocCommandsManager(XMPPConnection paramXMPPConnection)
  {
    return (AdHocCommandManager)instances.get(paramXMPPConnection);
  }
  
  private Collection<AdHocCommandInfo> getRegisteredCommands()
  {
    return this.commands.values();
  }
  
  private void init()
  {
    instances.put(this.connection, this);
    this.connection.addConnectionListener(new ConnectionListener()
    {
      public void connectionClosed()
      {
        AdHocCommandManager.instances.remove(AdHocCommandManager.this.connection);
      }
      
      public void connectionClosedOnError(Exception paramAnonymousException)
      {
        AdHocCommandManager.instances.remove(AdHocCommandManager.this.connection);
      }
      
      public void reconnectingIn(int paramAnonymousInt) {}
      
      public void reconnectionFailed(Exception paramAnonymousException) {}
      
      public void reconnectionSuccessful()
      {
        AdHocCommandManager.instances.put(AdHocCommandManager.this.connection, AdHocCommandManager.this);
      }
    });
    ServiceDiscoveryManager.getInstanceFor(this.connection).addFeature("http://jabber.org/protocol/commands");
    ServiceDiscoveryManager.getInstanceFor(this.connection).setNodeInformationProvider("http://jabber.org/protocol/commands", new NodeInformationProvider()
    {
      public List<String> getNodeFeatures()
      {
        return null;
      }
      
      public List<DiscoverInfo.Identity> getNodeIdentities()
      {
        return null;
      }
      
      public List<DiscoverItems.Item> getNodeItems()
      {
        ArrayList localArrayList = new ArrayList();
        Iterator localIterator = AdHocCommandManager.this.getRegisteredCommands().iterator();
        while (localIterator.hasNext())
        {
          AdHocCommandManager.AdHocCommandInfo localAdHocCommandInfo = (AdHocCommandManager.AdHocCommandInfo)localIterator.next();
          DiscoverItems.Item localItem = new DiscoverItems.Item(localAdHocCommandInfo.getOwnerJID());
          localItem.setName(localAdHocCommandInfo.getName());
          localItem.setNode(localAdHocCommandInfo.getNode());
          localArrayList.add(localItem);
        }
        return localArrayList;
      }
    });
    PacketListener local6 = new PacketListener()
    {
      public void processPacket(Packet paramAnonymousPacket)
      {
        paramAnonymousPacket = (AdHocCommandData)paramAnonymousPacket;
        AdHocCommandManager.this.processAdHocCommand(paramAnonymousPacket);
      }
    };
    PacketTypeFilter localPacketTypeFilter = new PacketTypeFilter(AdHocCommandData.class);
    this.connection.addPacketListener(local6, localPacketTypeFilter);
    this.sessionsSweeper = new Thread(new Runnable()
    {
      public void run()
      {
        for (;;)
        {
          Iterator localIterator = AdHocCommandManager.this.executingCommands.keySet().iterator();
          while (localIterator.hasNext())
          {
            String str = (String)localIterator.next();
            LocalCommand localLocalCommand = (LocalCommand)AdHocCommandManager.this.executingCommands.get(str);
            if (localLocalCommand != null)
            {
              long l = localLocalCommand.getCreationDate();
              if (System.currentTimeMillis() - l > 240000L) {
                AdHocCommandManager.this.executingCommands.remove(str);
              }
            }
          }
          try
          {
            Thread.sleep(1000L);
          }
          catch (InterruptedException localInterruptedException) {}
        }
      }
    });
    this.sessionsSweeper.setDaemon(true);
  }
  
  private LocalCommand newInstanceOfCmd(String paramString1, String paramString2)
    throws XMPPException
  {
    paramString1 = (AdHocCommandInfo)this.commands.get(paramString1);
    try
    {
      LocalCommand localLocalCommand = paramString1.getCommandInstance();
      localLocalCommand.setSessionID(paramString2);
      localLocalCommand.setName(paramString1.getName());
      localLocalCommand.setNode(paramString1.getNode());
      return localLocalCommand;
    }
    catch (InstantiationException paramString1)
    {
      paramString1.printStackTrace();
      throw new XMPPException(new XMPPError(XMPPError.Condition.interna_server_error));
    }
    catch (IllegalAccessException paramString1)
    {
      paramString1.printStackTrace();
      throw new XMPPException(new XMPPError(XMPPError.Condition.interna_server_error));
    }
  }
  
  private void processAdHocCommand(AdHocCommandData paramAdHocCommandData)
  {
    if (paramAdHocCommandData.getType() != IQ.Type.SET) {
      return;
    }
    AdHocCommandData localAdHocCommandData = new AdHocCommandData();
    localAdHocCommandData.setTo(paramAdHocCommandData.getFrom());
    localAdHocCommandData.setPacketID(paramAdHocCommandData.getPacketID());
    localAdHocCommandData.setNode(paramAdHocCommandData.getNode());
    localAdHocCommandData.setId(paramAdHocCommandData.getTo());
    String str = paramAdHocCommandData.getSessionID();
    Object localObject2 = paramAdHocCommandData.getNode();
    Object localObject1;
    if (str == null)
    {
      if (!this.commands.containsKey(localObject2))
      {
        respondError(localAdHocCommandData, XMPPError.Condition.item_not_found);
        return;
      }
      localObject1 = StringUtils.randomString(15);
      try
      {
        localObject2 = newInstanceOfCmd((String)localObject2, (String)localObject1);
        localAdHocCommandData.setType(IQ.Type.RESULT);
        ((LocalCommand)localObject2).setData(localAdHocCommandData);
        if (!((LocalCommand)localObject2).hasPermission(paramAdHocCommandData.getFrom()))
        {
          respondError(localAdHocCommandData, XMPPError.Condition.forbidden);
          return;
        }
      }
      catch (XMPPException paramAdHocCommandData)
      {
        localObject2 = paramAdHocCommandData.getXMPPError();
        if (XMPPError.Type.CANCEL.equals(((XMPPError)localObject2).getType()))
        {
          localAdHocCommandData.setStatus(AdHocCommand.Status.canceled);
          this.executingCommands.remove(localObject1);
        }
        respondError(localAdHocCommandData, (XMPPError)localObject2);
        paramAdHocCommandData.printStackTrace();
        return;
      }
      paramAdHocCommandData = paramAdHocCommandData.getAction();
      if ((paramAdHocCommandData != null) && (paramAdHocCommandData.equals(AdHocCommand.Action.unknown)))
      {
        respondError(localAdHocCommandData, XMPPError.Condition.bad_request, AdHocCommand.SpecificErrorCondition.malformedAction);
        return;
      }
      if ((paramAdHocCommandData != null) && (!paramAdHocCommandData.equals(AdHocCommand.Action.execute)))
      {
        respondError(localAdHocCommandData, XMPPError.Condition.bad_request, AdHocCommand.SpecificErrorCondition.badAction);
        return;
      }
      ((LocalCommand)localObject2).incrementStage();
      ((LocalCommand)localObject2).execute();
      if (((LocalCommand)localObject2).isLastStage()) {
        localAdHocCommandData.setStatus(AdHocCommand.Status.completed);
      }
      for (;;)
      {
        this.connection.sendPacket(localAdHocCommandData);
        return;
        localAdHocCommandData.setStatus(AdHocCommand.Status.executing);
        this.executingCommands.put(localObject1, localObject2);
        if (!this.sessionsSweeper.isAlive()) {
          this.sessionsSweeper.start();
        }
      }
    }
    LocalCommand localLocalCommand = (LocalCommand)this.executingCommands.get(str);
    if (localLocalCommand == null)
    {
      respondError(localAdHocCommandData, XMPPError.Condition.bad_request, AdHocCommand.SpecificErrorCondition.badSessionid);
      return;
    }
    long l = localLocalCommand.getCreationDate();
    if (System.currentTimeMillis() - l > 120000L)
    {
      this.executingCommands.remove(str);
      respondError(localAdHocCommandData, XMPPError.Condition.not_allowed, AdHocCommand.SpecificErrorCondition.sessionExpired);
      return;
    }
    try
    {
      localObject2 = paramAdHocCommandData.getAction();
      if ((localObject2 != null) && (((AdHocCommand.Action)localObject2).equals(AdHocCommand.Action.unknown)))
      {
        respondError(localAdHocCommandData, XMPPError.Condition.bad_request, AdHocCommand.SpecificErrorCondition.malformedAction);
        return;
      }
    }
    finally {}
    if (localObject2 != null)
    {
      localObject1 = localObject2;
      if (!AdHocCommand.Action.execute.equals(localObject2)) {}
    }
    else
    {
      localObject1 = localLocalCommand.getExecuteAction();
    }
    if (!localLocalCommand.isValidAction((AdHocCommand.Action)localObject1))
    {
      respondError(localAdHocCommandData, XMPPError.Condition.bad_request, AdHocCommand.SpecificErrorCondition.badAction);
      return;
    }
    try
    {
      localAdHocCommandData.setType(IQ.Type.RESULT);
      localLocalCommand.setData(localAdHocCommandData);
      if (AdHocCommand.Action.next.equals(localObject1))
      {
        localLocalCommand.incrementStage();
        localLocalCommand.next(new Form(paramAdHocCommandData.getForm()));
        if (localLocalCommand.isLastStage()) {
          localAdHocCommandData.setStatus(AdHocCommand.Status.completed);
        }
        for (;;)
        {
          this.connection.sendPacket(localAdHocCommandData);
          return;
          localAdHocCommandData.setStatus(AdHocCommand.Status.executing);
        }
      }
    }
    catch (XMPPException paramAdHocCommandData)
    {
      for (;;)
      {
        localObject1 = paramAdHocCommandData.getXMPPError();
        if (XMPPError.Type.CANCEL.equals(((XMPPError)localObject1).getType()))
        {
          localAdHocCommandData.setStatus(AdHocCommand.Status.canceled);
          this.executingCommands.remove(str);
        }
        respondError(localAdHocCommandData, (XMPPError)localObject1);
        paramAdHocCommandData.printStackTrace();
        continue;
        if (AdHocCommand.Action.complete.equals(localObject1))
        {
          localLocalCommand.incrementStage();
          localLocalCommand.complete(new Form(paramAdHocCommandData.getForm()));
          localAdHocCommandData.setStatus(AdHocCommand.Status.completed);
          this.executingCommands.remove(str);
        }
        else if (AdHocCommand.Action.prev.equals(localObject1))
        {
          localLocalCommand.decrementStage();
          localLocalCommand.prev();
        }
        else if (AdHocCommand.Action.cancel.equals(localObject1))
        {
          localLocalCommand.cancel();
          localAdHocCommandData.setStatus(AdHocCommand.Status.canceled);
          this.executingCommands.remove(str);
        }
      }
    }
  }
  
  private void respondError(AdHocCommandData paramAdHocCommandData, XMPPError.Condition paramCondition)
  {
    respondError(paramAdHocCommandData, new XMPPError(paramCondition));
  }
  
  private void respondError(AdHocCommandData paramAdHocCommandData, XMPPError.Condition paramCondition, AdHocCommand.SpecificErrorCondition paramSpecificErrorCondition)
  {
    paramCondition = new XMPPError(paramCondition);
    paramCondition.addExtension(new AdHocCommandData.SpecificError(paramSpecificErrorCondition));
    respondError(paramAdHocCommandData, paramCondition);
  }
  
  private void respondError(AdHocCommandData paramAdHocCommandData, XMPPError paramXMPPError)
  {
    paramAdHocCommandData.setType(IQ.Type.ERROR);
    paramAdHocCommandData.setError(paramXMPPError);
    this.connection.sendPacket(paramAdHocCommandData);
  }
  
  public DiscoverItems discoverCommands(String paramString)
    throws XMPPException
  {
    return ServiceDiscoveryManager.getInstanceFor(this.connection).discoverItems(paramString, "http://jabber.org/protocol/commands");
  }
  
  public RemoteCommand getRemoteCommand(String paramString1, String paramString2)
  {
    return new RemoteCommand(this.connection, paramString2, paramString1);
  }
  
  public void publishCommands(String paramString)
    throws XMPPException
  {
    ServiceDiscoveryManager localServiceDiscoveryManager = ServiceDiscoveryManager.getInstanceFor(this.connection);
    DiscoverItems localDiscoverItems = new DiscoverItems();
    Iterator localIterator = getRegisteredCommands().iterator();
    while (localIterator.hasNext())
    {
      AdHocCommandInfo localAdHocCommandInfo = (AdHocCommandInfo)localIterator.next();
      DiscoverItems.Item localItem = new DiscoverItems.Item(localAdHocCommandInfo.getOwnerJID());
      localItem.setName(localAdHocCommandInfo.getName());
      localItem.setNode(localAdHocCommandInfo.getNode());
      localDiscoverItems.addItem(localItem);
    }
    localServiceDiscoveryManager.publishItems(paramString, "http://jabber.org/protocol/commands", localDiscoverItems);
  }
  
  public void registerCommand(String paramString1, String paramString2, final Class paramClass)
  {
    registerCommand(paramString1, paramString2, new LocalCommandFactory()
    {
      public LocalCommand getInstance()
        throws InstantiationException, IllegalAccessException
      {
        return (LocalCommand)paramClass.newInstance();
      }
    });
  }
  
  public void registerCommand(String paramString1, final String paramString2, LocalCommandFactory paramLocalCommandFactory)
  {
    paramLocalCommandFactory = new AdHocCommandInfo(paramString1, paramString2, this.connection.getUser(), paramLocalCommandFactory);
    this.commands.put(paramString1, paramLocalCommandFactory);
    ServiceDiscoveryManager.getInstanceFor(this.connection).setNodeInformationProvider(paramString1, new NodeInformationProvider()
    {
      public List<String> getNodeFeatures()
      {
        ArrayList localArrayList = new ArrayList();
        localArrayList.add("http://jabber.org/protocol/commands");
        localArrayList.add("jabber:x:data");
        return localArrayList;
      }
      
      public List<DiscoverInfo.Identity> getNodeIdentities()
      {
        ArrayList localArrayList = new ArrayList();
        DiscoverInfo.Identity localIdentity = new DiscoverInfo.Identity("automation", paramString2);
        localIdentity.setType("command-node");
        localArrayList.add(localIdentity);
        return localArrayList;
      }
      
      public List<DiscoverItems.Item> getNodeItems()
      {
        return null;
      }
    });
  }
  
  private static class AdHocCommandInfo
  {
    private LocalCommandFactory factory;
    private String name;
    private String node;
    private String ownerJID;
    
    public AdHocCommandInfo(String paramString1, String paramString2, String paramString3, LocalCommandFactory paramLocalCommandFactory)
    {
      this.node = paramString1;
      this.name = paramString2;
      this.ownerJID = paramString3;
      this.factory = paramLocalCommandFactory;
    }
    
    public LocalCommand getCommandInstance()
      throws InstantiationException, IllegalAccessException
    {
      return this.factory.getInstance();
    }
    
    public String getName()
    {
      return this.name;
    }
    
    public String getNode()
    {
      return this.node;
    }
    
    public String getOwnerJID()
    {
      return this.ownerJID;
    }
  }
}


/* Location:           C:\Users\Administrator\Documents\ds\反编译\AndroidKiller_v1.3.1\projects\Jungledsq_ppc\ProjectSrc\smali\
 * Qualified Name:     org.jivesoftware.smackx.commands.AdHocCommandManager
 * JD-Core Version:    0.7.0.1
 */