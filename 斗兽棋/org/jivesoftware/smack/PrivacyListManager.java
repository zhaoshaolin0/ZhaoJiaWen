package org.jivesoftware.smack;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import org.jivesoftware.smack.filter.AndFilter;
import org.jivesoftware.smack.filter.IQTypeFilter;
import org.jivesoftware.smack.filter.PacketExtensionFilter;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.filter.PacketIDFilter;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.packet.IQ.Type;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.packet.Privacy;
import org.jivesoftware.smack.packet.PrivacyItem;

public class PrivacyListManager
{
  private static Map<XMPPConnection, PrivacyListManager> instances = new Hashtable();
  private XMPPConnection connection;
  private final List<PrivacyListListener> listeners = new ArrayList();
  PacketFilter packetFilter = new AndFilter(new PacketFilter[] { new IQTypeFilter(IQ.Type.SET), new PacketExtensionFilter("query", "jabber:iq:privacy") });
  
  static
  {
    XMPPConnection.addConnectionCreationListener(new ConnectionCreationListener()
    {
      public void connectionCreated(XMPPConnection paramAnonymousXMPPConnection)
      {
        new PrivacyListManager(paramAnonymousXMPPConnection, null);
      }
    });
  }
  
  private PrivacyListManager(XMPPConnection paramXMPPConnection)
  {
    this.connection = paramXMPPConnection;
    init();
  }
  
  public static PrivacyListManager getInstanceFor(XMPPConnection paramXMPPConnection)
  {
    return (PrivacyListManager)instances.get(paramXMPPConnection);
  }
  
  private List<PrivacyItem> getPrivacyListItems(String paramString)
    throws XMPPException
  {
    Privacy localPrivacy = new Privacy();
    localPrivacy.setPrivacyList(paramString, new ArrayList());
    return getRequest(localPrivacy).getPrivacyList(paramString);
  }
  
  private Privacy getPrivacyWithListNames()
    throws XMPPException
  {
    return getRequest(new Privacy());
  }
  
  private Privacy getRequest(Privacy paramPrivacy)
    throws XMPPException
  {
    paramPrivacy.setType(IQ.Type.GET);
    paramPrivacy.setFrom(getUser());
    Object localObject = new PacketIDFilter(paramPrivacy.getPacketID());
    localObject = this.connection.createPacketCollector((PacketFilter)localObject);
    this.connection.sendPacket(paramPrivacy);
    paramPrivacy = (Privacy)((PacketCollector)localObject).nextResult(SmackConfiguration.getPacketReplyTimeout());
    ((PacketCollector)localObject).cancel();
    if (paramPrivacy == null) {
      throw new XMPPException("No response from server.");
    }
    if (paramPrivacy.getError() != null) {
      throw new XMPPException(paramPrivacy.getError());
    }
    return paramPrivacy;
  }
  
  private String getUser()
  {
    return this.connection.getUser();
  }
  
  private void init()
  {
    instances.put(this.connection, this);
    this.connection.addConnectionListener(new ConnectionListener()
    {
      public void connectionClosed()
      {
        PrivacyListManager.instances.remove(PrivacyListManager.this.connection);
      }
      
      public void connectionClosedOnError(Exception paramAnonymousException) {}
      
      public void reconnectingIn(int paramAnonymousInt) {}
      
      public void reconnectionFailed(Exception paramAnonymousException) {}
      
      public void reconnectionSuccessful() {}
    });
    this.connection.addPacketListener(new PacketListener()
    {
      public void processPacket(Packet paramAnonymousPacket)
      {
        if ((paramAnonymousPacket == null) || (paramAnonymousPacket.getError() != null)) {
          return;
        }
        Privacy localPrivacy = (Privacy)paramAnonymousPacket;
        for (;;)
        {
          PrivacyListListener localPrivacyListListener;
          Object localObject2;
          String str;
          synchronized (PrivacyListManager.this.listeners)
          {
            Iterator localIterator1 = PrivacyListManager.this.listeners.iterator();
            if (!localIterator1.hasNext()) {
              break;
            }
            localPrivacyListListener = (PrivacyListListener)localIterator1.next();
            Iterator localIterator2 = localPrivacy.getItemLists().entrySet().iterator();
            if (!localIterator2.hasNext()) {
              continue;
            }
            localObject2 = (Map.Entry)localIterator2.next();
            str = (String)((Map.Entry)localObject2).getKey();
            localObject2 = (List)((Map.Entry)localObject2).getValue();
            if (((List)localObject2).isEmpty()) {
              localPrivacyListListener.updatedPrivacyList(str);
            }
          }
          localPrivacyListListener.setPrivacyList(str, (List)localObject2);
        }
        ??? = new IQ()
        {
          public String getChildElementXML()
          {
            return "";
          }
        };
        ((IQ)???).setType(IQ.Type.RESULT);
        ((IQ)???).setFrom(paramAnonymousPacket.getFrom());
        ((IQ)???).setPacketID(paramAnonymousPacket.getPacketID());
        PrivacyListManager.this.connection.sendPacket((Packet)???);
      }
    }, this.packetFilter);
  }
  
  private Packet setRequest(Privacy paramPrivacy)
    throws XMPPException
  {
    paramPrivacy.setType(IQ.Type.SET);
    paramPrivacy.setFrom(getUser());
    Object localObject = new PacketIDFilter(paramPrivacy.getPacketID());
    localObject = this.connection.createPacketCollector((PacketFilter)localObject);
    this.connection.sendPacket(paramPrivacy);
    paramPrivacy = ((PacketCollector)localObject).nextResult(SmackConfiguration.getPacketReplyTimeout());
    ((PacketCollector)localObject).cancel();
    if (paramPrivacy == null) {
      throw new XMPPException("No response from server.");
    }
    if (paramPrivacy.getError() != null) {
      throw new XMPPException(paramPrivacy.getError());
    }
    return paramPrivacy;
  }
  
  public void addListener(PrivacyListListener paramPrivacyListListener)
  {
    synchronized (this.listeners)
    {
      this.listeners.add(paramPrivacyListListener);
      return;
    }
  }
  
  public void createPrivacyList(String paramString, List<PrivacyItem> paramList)
    throws XMPPException
  {
    updatePrivacyList(paramString, paramList);
  }
  
  public void declineActiveList()
    throws XMPPException
  {
    Privacy localPrivacy = new Privacy();
    localPrivacy.setDeclineActiveList(true);
    setRequest(localPrivacy);
  }
  
  public void declineDefaultList()
    throws XMPPException
  {
    Privacy localPrivacy = new Privacy();
    localPrivacy.setDeclineDefaultList(true);
    setRequest(localPrivacy);
  }
  
  public void deletePrivacyList(String paramString)
    throws XMPPException
  {
    Privacy localPrivacy = new Privacy();
    localPrivacy.setPrivacyList(paramString, new ArrayList());
    setRequest(localPrivacy);
  }
  
  public PrivacyList getActiveList()
    throws XMPPException
  {
    Privacy localPrivacy = getPrivacyWithListNames();
    String str = localPrivacy.getActiveName();
    if ((localPrivacy.getActiveName() != null) && (localPrivacy.getDefaultName() != null) && (localPrivacy.getActiveName().equals(localPrivacy.getDefaultName()))) {}
    for (boolean bool = true;; bool = false) {
      return new PrivacyList(true, bool, str, getPrivacyListItems(str));
    }
  }
  
  public PrivacyList getDefaultList()
    throws XMPPException
  {
    Privacy localPrivacy = getPrivacyWithListNames();
    String str = localPrivacy.getDefaultName();
    if ((localPrivacy.getActiveName() != null) && (localPrivacy.getDefaultName() != null) && (localPrivacy.getActiveName().equals(localPrivacy.getDefaultName()))) {}
    for (boolean bool = true;; bool = false) {
      return new PrivacyList(bool, true, str, getPrivacyListItems(str));
    }
  }
  
  public PrivacyList getPrivacyList(String paramString)
    throws XMPPException
  {
    return new PrivacyList(false, false, paramString, getPrivacyListItems(paramString));
  }
  
  public PrivacyList[] getPrivacyLists()
    throws XMPPException
  {
    Privacy localPrivacy = getPrivacyWithListNames();
    Object localObject = localPrivacy.getPrivacyListNames();
    PrivacyList[] arrayOfPrivacyList = new PrivacyList[((Set)localObject).size()];
    int i = 0;
    localObject = ((Set)localObject).iterator();
    while (((Iterator)localObject).hasNext())
    {
      String str = (String)((Iterator)localObject).next();
      arrayOfPrivacyList[i] = new PrivacyList(str.equals(localPrivacy.getActiveName()), str.equals(localPrivacy.getDefaultName()), str, getPrivacyListItems(str));
      i += 1;
    }
    return arrayOfPrivacyList;
  }
  
  public void setActiveListName(String paramString)
    throws XMPPException
  {
    Privacy localPrivacy = new Privacy();
    localPrivacy.setActiveName(paramString);
    setRequest(localPrivacy);
  }
  
  public void setDefaultListName(String paramString)
    throws XMPPException
  {
    Privacy localPrivacy = new Privacy();
    localPrivacy.setDefaultName(paramString);
    setRequest(localPrivacy);
  }
  
  public void updatePrivacyList(String paramString, List<PrivacyItem> paramList)
    throws XMPPException
  {
    Privacy localPrivacy = new Privacy();
    localPrivacy.setPrivacyList(paramString, paramList);
    setRequest(localPrivacy);
  }
}


/* Location:           C:\Users\Administrator\Documents\ds\反编译\AndroidKiller_v1.3.1\projects\Jungledsq_ppc\ProjectSrc\smali\
 * Qualified Name:     org.jivesoftware.smack.PrivacyListManager
 * JD-Core Version:    0.7.0.1
 */