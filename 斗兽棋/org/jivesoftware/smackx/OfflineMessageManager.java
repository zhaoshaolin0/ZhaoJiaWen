package org.jivesoftware.smackx;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.jivesoftware.smack.PacketCollector;
import org.jivesoftware.smack.SmackConfiguration;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.filter.AndFilter;
import org.jivesoftware.smack.filter.PacketExtensionFilter;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.filter.PacketIDFilter;
import org.jivesoftware.smack.filter.PacketTypeFilter;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smackx.packet.DiscoverInfo;
import org.jivesoftware.smackx.packet.DiscoverItems;
import org.jivesoftware.smackx.packet.DiscoverItems.Item;
import org.jivesoftware.smackx.packet.OfflineMessageInfo;
import org.jivesoftware.smackx.packet.OfflineMessageRequest;
import org.jivesoftware.smackx.packet.OfflineMessageRequest.Item;

public class OfflineMessageManager
{
  private static final String namespace = "http://jabber.org/protocol/offline";
  private XMPPConnection connection;
  private PacketFilter packetFilter;
  
  public OfflineMessageManager(XMPPConnection paramXMPPConnection)
  {
    this.connection = paramXMPPConnection;
    this.packetFilter = new AndFilter(new PacketFilter[] { new PacketExtensionFilter("offline", "http://jabber.org/protocol/offline"), new PacketTypeFilter(Message.class) });
  }
  
  public void deleteMessages()
    throws XMPPException
  {
    Object localObject1 = new OfflineMessageRequest();
    ((OfflineMessageRequest)localObject1).setPurge(true);
    Object localObject2 = new PacketIDFilter(((OfflineMessageRequest)localObject1).getPacketID());
    localObject2 = this.connection.createPacketCollector((PacketFilter)localObject2);
    this.connection.sendPacket((Packet)localObject1);
    localObject1 = (IQ)((PacketCollector)localObject2).nextResult(SmackConfiguration.getPacketReplyTimeout());
    ((PacketCollector)localObject2).cancel();
    if (localObject1 == null) {
      throw new XMPPException("No response from server.");
    }
    if (((IQ)localObject1).getError() != null) {
      throw new XMPPException(((IQ)localObject1).getError());
    }
  }
  
  public void deleteMessages(List<String> paramList)
    throws XMPPException
  {
    Object localObject = new OfflineMessageRequest();
    paramList = paramList.iterator();
    while (paramList.hasNext())
    {
      OfflineMessageRequest.Item localItem = new OfflineMessageRequest.Item((String)paramList.next());
      localItem.setAction("remove");
      ((OfflineMessageRequest)localObject).addItem(localItem);
    }
    paramList = new PacketIDFilter(((OfflineMessageRequest)localObject).getPacketID());
    paramList = this.connection.createPacketCollector(paramList);
    this.connection.sendPacket((Packet)localObject);
    localObject = (IQ)paramList.nextResult(SmackConfiguration.getPacketReplyTimeout());
    paramList.cancel();
    if (localObject == null) {
      throw new XMPPException("No response from server.");
    }
    if (((IQ)localObject).getError() != null) {
      throw new XMPPException(((IQ)localObject).getError());
    }
  }
  
  public Iterator<OfflineMessageHeader> getHeaders()
    throws XMPPException
  {
    ArrayList localArrayList = new ArrayList();
    Iterator localIterator = ServiceDiscoveryManager.getInstanceFor(this.connection).discoverItems(null, "http://jabber.org/protocol/offline").getItems();
    while (localIterator.hasNext()) {
      localArrayList.add(new OfflineMessageHeader((DiscoverItems.Item)localIterator.next()));
    }
    return localArrayList.iterator();
  }
  
  public int getMessageCount()
    throws XMPPException
  {
    Form localForm = Form.getFormFrom(ServiceDiscoveryManager.getInstanceFor(this.connection).discoverInfo(null, "http://jabber.org/protocol/offline"));
    if (localForm != null) {
      return Integer.parseInt((String)localForm.getField("number_of_messages").getValues().next());
    }
    return 0;
  }
  
  public Iterator<Message> getMessages()
    throws XMPPException
  {
    ArrayList localArrayList = new ArrayList();
    Object localObject1 = new OfflineMessageRequest();
    ((OfflineMessageRequest)localObject1).setFetch(true);
    Object localObject2 = new PacketIDFilter(((OfflineMessageRequest)localObject1).getPacketID());
    PacketCollector localPacketCollector = this.connection.createPacketCollector((PacketFilter)localObject2);
    localObject2 = this.connection.createPacketCollector(this.packetFilter);
    this.connection.sendPacket((Packet)localObject1);
    localObject1 = (IQ)localPacketCollector.nextResult(SmackConfiguration.getPacketReplyTimeout());
    localPacketCollector.cancel();
    if (localObject1 == null) {
      throw new XMPPException("No response from server.");
    }
    if (((IQ)localObject1).getError() != null) {
      throw new XMPPException(((IQ)localObject1).getError());
    }
    for (localObject1 = (Message)((PacketCollector)localObject2).nextResult(SmackConfiguration.getPacketReplyTimeout()); localObject1 != null; localObject1 = (Message)((PacketCollector)localObject2).nextResult(SmackConfiguration.getPacketReplyTimeout())) {
      localArrayList.add(localObject1);
    }
    ((PacketCollector)localObject2).cancel();
    return localArrayList.iterator();
  }
  
  public Iterator<Message> getMessages(final List<String> paramList)
    throws XMPPException
  {
    ArrayList localArrayList = new ArrayList();
    OfflineMessageRequest localOfflineMessageRequest = new OfflineMessageRequest();
    Object localObject1 = paramList.iterator();
    while (((Iterator)localObject1).hasNext())
    {
      localObject2 = new OfflineMessageRequest.Item((String)((Iterator)localObject1).next());
      ((OfflineMessageRequest.Item)localObject2).setAction("view");
      localOfflineMessageRequest.addItem((OfflineMessageRequest.Item)localObject2);
    }
    localObject1 = new PacketIDFilter(localOfflineMessageRequest.getPacketID());
    Object localObject2 = this.connection.createPacketCollector((PacketFilter)localObject1);
    paramList = new AndFilter(new PacketFilter[] { this.packetFilter, new PacketFilter()
    {
      public boolean accept(Packet paramAnonymousPacket)
      {
        paramAnonymousPacket = (OfflineMessageInfo)paramAnonymousPacket.getExtension("offline", "http://jabber.org/protocol/offline");
        return paramList.contains(paramAnonymousPacket.getNode());
      }
    } });
    localObject1 = this.connection.createPacketCollector(paramList);
    this.connection.sendPacket(localOfflineMessageRequest);
    paramList = (IQ)((PacketCollector)localObject2).nextResult(SmackConfiguration.getPacketReplyTimeout());
    ((PacketCollector)localObject2).cancel();
    if (paramList == null) {
      throw new XMPPException("No response from server.");
    }
    if (paramList.getError() != null) {
      throw new XMPPException(paramList.getError());
    }
    for (paramList = (Message)((PacketCollector)localObject1).nextResult(SmackConfiguration.getPacketReplyTimeout()); paramList != null; paramList = (Message)((PacketCollector)localObject1).nextResult(SmackConfiguration.getPacketReplyTimeout())) {
      localArrayList.add(paramList);
    }
    ((PacketCollector)localObject1).cancel();
    return localArrayList.iterator();
  }
  
  public boolean supportsFlexibleRetrieval()
    throws XMPPException
  {
    return ServiceDiscoveryManager.getInstanceFor(this.connection).discoverInfo(null).containsFeature("http://jabber.org/protocol/offline");
  }
}


/* Location:           C:\Users\Administrator\Documents\ds\反编译\AndroidKiller_v1.3.1\projects\Jungledsq_ppc\ProjectSrc\smali\
 * Qualified Name:     org.jivesoftware.smackx.OfflineMessageManager
 * JD-Core Version:    0.7.0.1
 */