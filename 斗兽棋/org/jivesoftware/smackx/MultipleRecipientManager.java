package org.jivesoftware.smackx;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.util.Cache;
import org.jivesoftware.smack.util.StringUtils;
import org.jivesoftware.smackx.packet.DiscoverInfo;
import org.jivesoftware.smackx.packet.DiscoverItems;
import org.jivesoftware.smackx.packet.DiscoverItems.Item;
import org.jivesoftware.smackx.packet.MultipleAddresses;
import org.jivesoftware.smackx.packet.MultipleAddresses.Address;

public class MultipleRecipientManager
{
  private static Cache services = new Cache(100, 86400000L);
  
  private static String getMultipleRecipienServiceAddress(XMPPConnection paramXMPPConnection)
  {
    String str = paramXMPPConnection.getServiceName();
    Object localObject2 = (String)services.get(str);
    Object localObject1 = localObject2;
    if (localObject2 == null) {}
    synchronized (services)
    {
      Object localObject3 = (String)services.get(str);
      localObject1 = localObject3;
      if (localObject3 == null) {
        localObject2 = localObject3;
      }
      for (;;)
      {
        try
        {
          if (!ServiceDiscoveryManager.getInstanceFor(paramXMPPConnection).discoverInfo(str).containsFeature("http://jabber.org/protocol/address")) {
            continue;
          }
          localObject1 = str;
          localObject2 = localObject1;
          localObject3 = services;
          if (localObject1 != null) {
            continue;
          }
          paramXMPPConnection = "";
          localObject2 = localObject1;
          ((Cache)localObject3).put(str, paramXMPPConnection);
        }
        catch (XMPPException paramXMPPConnection)
        {
          Iterator localIterator;
          boolean bool;
          paramXMPPConnection.printStackTrace();
          localObject1 = localObject2;
          continue;
        }
        if (!"".equals(localObject1)) {
          return localObject1;
        }
        return null;
        localObject2 = localObject3;
        localIterator = ServiceDiscoveryManager.getInstanceFor(paramXMPPConnection).discoverItems(str).getItems();
        localObject1 = localObject3;
        localObject2 = localObject3;
        if (localIterator.hasNext())
        {
          localObject2 = localObject3;
          localObject1 = (DiscoverItems.Item)localIterator.next();
          localObject2 = localObject3;
          bool = ServiceDiscoveryManager.getInstanceFor(paramXMPPConnection).discoverInfo(((DiscoverItems.Item)localObject1).getEntityID(), ((DiscoverItems.Item)localObject1).getNode()).containsFeature("http://jabber.org/protocol/address");
          if (bool)
          {
            localObject1 = str;
            continue;
            paramXMPPConnection = (XMPPConnection)localObject1;
          }
        }
      }
    }
    return localObject1;
  }
  
  public static MultipleRecipientInfo getMultipleRecipientInfo(Packet paramPacket)
  {
    paramPacket = (MultipleAddresses)paramPacket.getExtension("addresses", "http://jabber.org/protocol/address");
    if (paramPacket == null) {
      return null;
    }
    return new MultipleRecipientInfo(paramPacket);
  }
  
  public static void reply(XMPPConnection paramXMPPConnection, Message paramMessage1, Message paramMessage2)
    throws XMPPException
  {
    Object localObject2 = getMultipleRecipientInfo(paramMessage1);
    if (localObject2 == null) {
      throw new XMPPException("Original message does not contain multiple recipient info");
    }
    if (((MultipleRecipientInfo)localObject2).shouldNotReply()) {
      throw new XMPPException("Original message should not be replied");
    }
    if (((MultipleRecipientInfo)localObject2).getReplyRoom() != null) {
      throw new XMPPException("Reply should be sent through a room");
    }
    if (paramMessage1.getThread() != null) {
      paramMessage2.setThread(paramMessage1.getThread());
    }
    Object localObject1 = ((MultipleRecipientInfo)localObject2).getReplyAddress();
    if ((localObject1 != null) && (((MultipleAddresses.Address)localObject1).getJid() != null))
    {
      paramMessage2.setTo(((MultipleAddresses.Address)localObject1).getJid());
      paramXMPPConnection.sendPacket(paramMessage2);
      return;
    }
    localObject1 = new ArrayList();
    ArrayList localArrayList = new ArrayList();
    Iterator localIterator = ((MultipleRecipientInfo)localObject2).getTOAddresses().iterator();
    while (localIterator.hasNext()) {
      ((List)localObject1).add(((MultipleAddresses.Address)localIterator.next()).getJid());
    }
    localObject2 = ((MultipleRecipientInfo)localObject2).getCCAddresses().iterator();
    while (((Iterator)localObject2).hasNext()) {
      localArrayList.add(((MultipleAddresses.Address)((Iterator)localObject2).next()).getJid());
    }
    if ((!((List)localObject1).contains(paramMessage1.getFrom())) && (!localArrayList.contains(paramMessage1.getFrom()))) {
      ((List)localObject1).add(paramMessage1.getFrom());
    }
    paramMessage1 = paramXMPPConnection.getUser();
    if ((!((List)localObject1).remove(paramMessage1)) && (!localArrayList.remove(paramMessage1)))
    {
      paramMessage1 = StringUtils.parseBareAddress(paramMessage1);
      ((List)localObject1).remove(paramMessage1);
      localArrayList.remove(paramMessage1);
    }
    paramMessage1 = getMultipleRecipienServiceAddress(paramXMPPConnection);
    if (paramMessage1 != null)
    {
      sendThroughService(paramXMPPConnection, paramMessage2, (List)localObject1, localArrayList, null, null, null, false, paramMessage1);
      return;
    }
    sendToIndividualRecipients(paramXMPPConnection, paramMessage2, (List)localObject1, localArrayList, null);
  }
  
  public static void send(XMPPConnection paramXMPPConnection, Packet paramPacket, List paramList1, List paramList2, List paramList3)
    throws XMPPException
  {
    send(paramXMPPConnection, paramPacket, paramList1, paramList2, paramList3, null, null, false);
  }
  
  public static void send(XMPPConnection paramXMPPConnection, Packet paramPacket, List paramList1, List paramList2, List paramList3, String paramString1, String paramString2, boolean paramBoolean)
    throws XMPPException
  {
    String str = getMultipleRecipienServiceAddress(paramXMPPConnection);
    if (str != null)
    {
      sendThroughService(paramXMPPConnection, paramPacket, paramList1, paramList2, paramList3, paramString1, paramString2, paramBoolean, str);
      return;
    }
    if ((paramBoolean) || ((paramString1 != null) && (paramString1.trim().length() > 0)) || ((paramString2 != null) && (paramString2.trim().length() > 0))) {
      throw new XMPPException("Extended Stanza Addressing not supported by server");
    }
    sendToIndividualRecipients(paramXMPPConnection, paramPacket, paramList1, paramList2, paramList3);
  }
  
  private static void sendThroughService(XMPPConnection paramXMPPConnection, Packet paramPacket, List paramList1, List paramList2, List paramList3, String paramString1, String paramString2, boolean paramBoolean, String paramString3)
  {
    MultipleAddresses localMultipleAddresses = new MultipleAddresses();
    if (paramList1 != null)
    {
      paramList1 = paramList1.iterator();
      while (paramList1.hasNext()) {
        localMultipleAddresses.addAddress("to", (String)paramList1.next(), null, null, false, null);
      }
    }
    if (paramList2 != null)
    {
      paramList1 = paramList2.iterator();
      while (paramList1.hasNext()) {
        localMultipleAddresses.addAddress("cc", (String)paramList1.next(), null, null, false, null);
      }
    }
    if (paramList3 != null)
    {
      paramList1 = paramList3.iterator();
      while (paramList1.hasNext()) {
        localMultipleAddresses.addAddress("bcc", (String)paramList1.next(), null, null, false, null);
      }
    }
    if (paramBoolean) {
      localMultipleAddresses.setNoReply();
    }
    for (;;)
    {
      paramPacket.setTo(paramString3);
      paramPacket.addExtension(localMultipleAddresses);
      paramXMPPConnection.sendPacket(paramPacket);
      return;
      if ((paramString1 != null) && (paramString1.trim().length() > 0)) {
        localMultipleAddresses.addAddress("replyto", paramString1, null, null, false, null);
      }
      if ((paramString2 != null) && (paramString2.trim().length() > 0)) {
        localMultipleAddresses.addAddress("replyroom", paramString2, null, null, false, null);
      }
    }
  }
  
  private static void sendToIndividualRecipients(XMPPConnection paramXMPPConnection, Packet paramPacket, List paramList1, List paramList2, List paramList3)
  {
    if (paramList1 != null)
    {
      paramList1 = paramList1.iterator();
      while (paramList1.hasNext())
      {
        paramPacket.setTo((String)paramList1.next());
        paramXMPPConnection.sendPacket(new PacketCopy(paramPacket.toXML()));
      }
    }
    if (paramList2 != null)
    {
      paramList1 = paramList2.iterator();
      while (paramList1.hasNext())
      {
        paramPacket.setTo((String)paramList1.next());
        paramXMPPConnection.sendPacket(new PacketCopy(paramPacket.toXML()));
      }
    }
    if (paramList3 != null)
    {
      paramList1 = paramList3.iterator();
      while (paramList1.hasNext())
      {
        paramPacket.setTo((String)paramList1.next());
        paramXMPPConnection.sendPacket(new PacketCopy(paramPacket.toXML()));
      }
    }
  }
  
  private static class PacketCopy
    extends Packet
  {
    private String text;
    
    public PacketCopy(String paramString)
    {
      this.text = paramString;
    }
    
    public String toXML()
    {
      return this.text;
    }
  }
}


/* Location:           C:\Users\Administrator\Documents\ds\反编译\AndroidKiller_v1.3.1\projects\Jungledsq_ppc\ProjectSrc\smali\
 * Qualified Name:     org.jivesoftware.smackx.MultipleRecipientManager
 * JD-Core Version:    0.7.0.1
 */