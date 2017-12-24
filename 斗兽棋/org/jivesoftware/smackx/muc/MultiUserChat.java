package org.jivesoftware.smackx.muc;

import java.lang.ref.WeakReference;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.ConcurrentHashMap;
import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.ChatManager;
import org.jivesoftware.smack.ConnectionCreationListener;
import org.jivesoftware.smack.ConnectionListener;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.PacketCollector;
import org.jivesoftware.smack.PacketInterceptor;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.SmackConfiguration;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.filter.AndFilter;
import org.jivesoftware.smack.filter.FromMatchesFilter;
import org.jivesoftware.smack.filter.MessageTypeFilter;
import org.jivesoftware.smack.filter.PacketExtensionFilter;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.filter.PacketIDFilter;
import org.jivesoftware.smack.filter.PacketTypeFilter;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.packet.IQ.Type;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Message.Type;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.packet.Presence.Mode;
import org.jivesoftware.smack.packet.Presence.Type;
import org.jivesoftware.smack.packet.Registration;
import org.jivesoftware.smackx.Form;
import org.jivesoftware.smackx.NodeInformationProvider;
import org.jivesoftware.smackx.ServiceDiscoveryManager;
import org.jivesoftware.smackx.packet.DiscoverInfo;
import org.jivesoftware.smackx.packet.DiscoverInfo.Identity;
import org.jivesoftware.smackx.packet.DiscoverItems;
import org.jivesoftware.smackx.packet.DiscoverItems.Item;
import org.jivesoftware.smackx.packet.MUCAdmin;
import org.jivesoftware.smackx.packet.MUCAdmin.Item;
import org.jivesoftware.smackx.packet.MUCOwner;
import org.jivesoftware.smackx.packet.MUCOwner.Destroy;
import org.jivesoftware.smackx.packet.MUCOwner.Item;
import org.jivesoftware.smackx.packet.MUCUser;
import org.jivesoftware.smackx.packet.MUCUser.Decline;
import org.jivesoftware.smackx.packet.MUCUser.Invite;
import org.jivesoftware.smackx.packet.MUCUser.Item;
import org.jivesoftware.smackx.packet.MUCUser.Status;

public class MultiUserChat
{
  private static final String discoNamespace = "http://jabber.org/protocol/muc";
  private static final String discoNode = "http://jabber.org/protocol/muc#rooms";
  private static Map<XMPPConnection, List<String>> joinedRooms = new WeakHashMap();
  private XMPPConnection connection;
  private List<PacketListener> connectionListeners = new ArrayList();
  private final List<InvitationRejectionListener> invitationRejectionListeners = new ArrayList();
  private boolean joined = false;
  private ConnectionDetachedPacketCollector messageCollector;
  private PacketFilter messageFilter;
  private String nickname = null;
  private Map<String, Presence> occupantsMap = new ConcurrentHashMap();
  private final List<ParticipantStatusListener> participantStatusListeners = new ArrayList();
  private PacketFilter presenceFilter;
  private List<PacketInterceptor> presenceInterceptors = new ArrayList();
  private String room;
  private RoomListenerMultiplexor roomListenerMultiplexor;
  private String subject;
  private final List<SubjectUpdatedListener> subjectUpdatedListeners = new ArrayList();
  private final List<UserStatusListener> userStatusListeners = new ArrayList();
  
  static
  {
    XMPPConnection.addConnectionCreationListener(new ConnectionCreationListener()
    {
      public void connectionCreated(final XMPPConnection paramAnonymousXMPPConnection)
      {
        ServiceDiscoveryManager.getInstanceFor(paramAnonymousXMPPConnection).addFeature("http://jabber.org/protocol/muc");
        ServiceDiscoveryManager.getInstanceFor(paramAnonymousXMPPConnection).setNodeInformationProvider("http://jabber.org/protocol/muc#rooms", new NodeInformationProvider()
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
            Iterator localIterator = MultiUserChat.getJoinedRooms(paramAnonymousXMPPConnection);
            while (localIterator.hasNext()) {
              localArrayList.add(new DiscoverItems.Item((String)localIterator.next()));
            }
            return localArrayList;
          }
        });
      }
    });
  }
  
  public MultiUserChat(XMPPConnection paramXMPPConnection, String paramString)
  {
    this.connection = paramXMPPConnection;
    this.room = paramString.toLowerCase();
    init();
  }
  
  public static void addInvitationListener(XMPPConnection paramXMPPConnection, InvitationListener paramInvitationListener)
  {
    InvitationsMonitor.getInvitationsMonitor(paramXMPPConnection).addInvitationListener(paramInvitationListener);
  }
  
  private void changeAffiliationByAdmin(String paramString1, String paramString2, String paramString3)
    throws XMPPException
  {
    MUCAdmin localMUCAdmin = new MUCAdmin();
    localMUCAdmin.setTo(this.room);
    localMUCAdmin.setType(IQ.Type.SET);
    paramString2 = new MUCAdmin.Item(paramString2, null);
    paramString2.setJid(paramString1);
    paramString2.setReason(paramString3);
    localMUCAdmin.addItem(paramString2);
    paramString1 = new PacketIDFilter(localMUCAdmin.getPacketID());
    paramString1 = this.connection.createPacketCollector(paramString1);
    this.connection.sendPacket(localMUCAdmin);
    paramString2 = (IQ)paramString1.nextResult(SmackConfiguration.getPacketReplyTimeout());
    paramString1.cancel();
    if (paramString2 == null) {
      throw new XMPPException("No response from server.");
    }
    if (paramString2.getError() != null) {
      throw new XMPPException(paramString2.getError());
    }
  }
  
  private void changeAffiliationByAdmin(Collection<String> paramCollection, String paramString)
    throws XMPPException
  {
    MUCAdmin localMUCAdmin = new MUCAdmin();
    localMUCAdmin.setTo(this.room);
    localMUCAdmin.setType(IQ.Type.SET);
    paramCollection = paramCollection.iterator();
    while (paramCollection.hasNext())
    {
      String str = (String)paramCollection.next();
      MUCAdmin.Item localItem = new MUCAdmin.Item(paramString, null);
      localItem.setJid(str);
      localMUCAdmin.addItem(localItem);
    }
    paramCollection = new PacketIDFilter(localMUCAdmin.getPacketID());
    paramCollection = this.connection.createPacketCollector(paramCollection);
    this.connection.sendPacket(localMUCAdmin);
    paramString = (IQ)paramCollection.nextResult(SmackConfiguration.getPacketReplyTimeout());
    paramCollection.cancel();
    if (paramString == null) {
      throw new XMPPException("No response from server.");
    }
    if (paramString.getError() != null) {
      throw new XMPPException(paramString.getError());
    }
  }
  
  private void changeAffiliationByOwner(String paramString1, String paramString2)
    throws XMPPException
  {
    MUCOwner localMUCOwner = new MUCOwner();
    localMUCOwner.setTo(this.room);
    localMUCOwner.setType(IQ.Type.SET);
    paramString2 = new MUCOwner.Item(paramString2);
    paramString2.setJid(paramString1);
    localMUCOwner.addItem(paramString2);
    paramString1 = new PacketIDFilter(localMUCOwner.getPacketID());
    paramString1 = this.connection.createPacketCollector(paramString1);
    this.connection.sendPacket(localMUCOwner);
    paramString2 = (IQ)paramString1.nextResult(SmackConfiguration.getPacketReplyTimeout());
    paramString1.cancel();
    if (paramString2 == null) {
      throw new XMPPException("No response from server.");
    }
    if (paramString2.getError() != null) {
      throw new XMPPException(paramString2.getError());
    }
  }
  
  private void changeAffiliationByOwner(Collection<String> paramCollection, String paramString)
    throws XMPPException
  {
    MUCOwner localMUCOwner = new MUCOwner();
    localMUCOwner.setTo(this.room);
    localMUCOwner.setType(IQ.Type.SET);
    paramCollection = paramCollection.iterator();
    while (paramCollection.hasNext())
    {
      String str = (String)paramCollection.next();
      MUCOwner.Item localItem = new MUCOwner.Item(paramString);
      localItem.setJid(str);
      localMUCOwner.addItem(localItem);
    }
    paramCollection = new PacketIDFilter(localMUCOwner.getPacketID());
    paramCollection = this.connection.createPacketCollector(paramCollection);
    this.connection.sendPacket(localMUCOwner);
    paramString = (IQ)paramCollection.nextResult(SmackConfiguration.getPacketReplyTimeout());
    paramCollection.cancel();
    if (paramString == null) {
      throw new XMPPException("No response from server.");
    }
    if (paramString.getError() != null) {
      throw new XMPPException(paramString.getError());
    }
  }
  
  private void changeRole(String paramString1, String paramString2, String paramString3)
    throws XMPPException
  {
    MUCAdmin localMUCAdmin = new MUCAdmin();
    localMUCAdmin.setTo(this.room);
    localMUCAdmin.setType(IQ.Type.SET);
    paramString2 = new MUCAdmin.Item(null, paramString2);
    paramString2.setNick(paramString1);
    paramString2.setReason(paramString3);
    localMUCAdmin.addItem(paramString2);
    paramString1 = new PacketIDFilter(localMUCAdmin.getPacketID());
    paramString1 = this.connection.createPacketCollector(paramString1);
    this.connection.sendPacket(localMUCAdmin);
    paramString2 = (IQ)paramString1.nextResult(SmackConfiguration.getPacketReplyTimeout());
    paramString1.cancel();
    if (paramString2 == null) {
      throw new XMPPException("No response from server.");
    }
    if (paramString2.getError() != null) {
      throw new XMPPException(paramString2.getError());
    }
  }
  
  private void changeRole(Collection<String> paramCollection, String paramString)
    throws XMPPException
  {
    MUCAdmin localMUCAdmin = new MUCAdmin();
    localMUCAdmin.setTo(this.room);
    localMUCAdmin.setType(IQ.Type.SET);
    paramCollection = paramCollection.iterator();
    while (paramCollection.hasNext())
    {
      String str = (String)paramCollection.next();
      MUCAdmin.Item localItem = new MUCAdmin.Item(null, paramString);
      localItem.setNick(str);
      localMUCAdmin.addItem(localItem);
    }
    paramCollection = new PacketIDFilter(localMUCAdmin.getPacketID());
    paramCollection = this.connection.createPacketCollector(paramCollection);
    this.connection.sendPacket(localMUCAdmin);
    paramString = (IQ)paramCollection.nextResult(SmackConfiguration.getPacketReplyTimeout());
    paramCollection.cancel();
    if (paramString == null) {
      throw new XMPPException("No response from server.");
    }
    if (paramString.getError() != null) {
      throw new XMPPException(paramString.getError());
    }
  }
  
  private void checkAffiliationModifications(String paramString1, String paramString2, boolean paramBoolean, String paramString3)
  {
    if (("owner".equals(paramString1)) && (!"owner".equals(paramString2))) {
      if (paramBoolean)
      {
        fireUserStatusListeners("ownershipRevoked", new Object[0]);
        if (("owner".equals(paramString1)) || (!"owner".equals(paramString2))) {
          break label266;
        }
        if (!paramBoolean) {
          break label240;
        }
        fireUserStatusListeners("ownershipGranted", new Object[0]);
      }
    }
    label240:
    do
    {
      return;
      ArrayList localArrayList = new ArrayList();
      localArrayList.add(paramString3);
      fireParticipantStatusListeners("ownershipRevoked", localArrayList);
      break;
      if (("admin".equals(paramString1)) && (!"admin".equals(paramString2)))
      {
        if (paramBoolean)
        {
          fireUserStatusListeners("adminRevoked", new Object[0]);
          break;
        }
        localArrayList = new ArrayList();
        localArrayList.add(paramString3);
        fireParticipantStatusListeners("adminRevoked", localArrayList);
        break;
      }
      if ((!"member".equals(paramString1)) || ("member".equals(paramString2))) {
        break;
      }
      if (paramBoolean)
      {
        fireUserStatusListeners("membershipRevoked", new Object[0]);
        break;
      }
      localArrayList = new ArrayList();
      localArrayList.add(paramString3);
      fireParticipantStatusListeners("membershipRevoked", localArrayList);
      break;
      paramString1 = new ArrayList();
      paramString1.add(paramString3);
      fireParticipantStatusListeners("ownershipGranted", paramString1);
      return;
      if ((!"admin".equals(paramString1)) && ("admin".equals(paramString2)))
      {
        if (paramBoolean)
        {
          fireUserStatusListeners("adminGranted", new Object[0]);
          return;
        }
        paramString1 = new ArrayList();
        paramString1.add(paramString3);
        fireParticipantStatusListeners("adminGranted", paramString1);
        return;
      }
    } while (("member".equals(paramString1)) || (!"member".equals(paramString2)));
    label266:
    if (paramBoolean)
    {
      fireUserStatusListeners("membershipGranted", new Object[0]);
      return;
    }
    paramString1 = new ArrayList();
    paramString1.add(paramString3);
    fireParticipantStatusListeners("membershipGranted", paramString1);
  }
  
  private void checkPresenceCode(String paramString1, boolean paramBoolean, MUCUser paramMUCUser, String paramString2)
  {
    if ("307".equals(paramString1)) {
      if (paramBoolean)
      {
        this.joined = false;
        fireUserStatusListeners("kicked", new Object[] { paramMUCUser.getItem().getActor(), paramMUCUser.getItem().getReason() });
        this.occupantsMap.clear();
        this.nickname = null;
        userHasLeft();
      }
    }
    do
    {
      do
      {
        return;
        paramString1 = new ArrayList();
        paramString1.add(paramString2);
        paramString1.add(paramMUCUser.getItem().getActor());
        paramString1.add(paramMUCUser.getItem().getReason());
        fireParticipantStatusListeners("kicked", paramString1);
        return;
        if ("301".equals(paramString1))
        {
          if (paramBoolean)
          {
            this.joined = false;
            fireUserStatusListeners("banned", new Object[] { paramMUCUser.getItem().getActor(), paramMUCUser.getItem().getReason() });
            this.occupantsMap.clear();
            this.nickname = null;
            userHasLeft();
            return;
          }
          paramString1 = new ArrayList();
          paramString1.add(paramString2);
          paramString1.add(paramMUCUser.getItem().getActor());
          paramString1.add(paramMUCUser.getItem().getReason());
          fireParticipantStatusListeners("banned", paramString1);
          return;
        }
        if (!"321".equals(paramString1)) {
          break;
        }
      } while (!paramBoolean);
      this.joined = false;
      fireUserStatusListeners("membershipRevoked", new Object[0]);
      this.occupantsMap.clear();
      this.nickname = null;
      userHasLeft();
      return;
    } while (!"303".equals(paramString1));
    paramString1 = new ArrayList();
    paramString1.add(paramString2);
    paramString1.add(paramMUCUser.getItem().getNick());
    fireParticipantStatusListeners("nicknameChanged", paramString1);
  }
  
  private void checkRoleModifications(String paramString1, String paramString2, boolean paramBoolean, String paramString3)
  {
    if ((("visitor".equals(paramString1)) || ("none".equals(paramString1))) && ("participant".equals(paramString2))) {
      if (paramBoolean)
      {
        fireUserStatusListeners("voiceGranted", new Object[0]);
        if (("moderator".equals(paramString1)) || (!"moderator".equals(paramString2))) {
          break label280;
        }
        if (("visitor".equals(paramString1)) || ("none".equals(paramString1)))
        {
          if (!paramBoolean) {
            break label226;
          }
          fireUserStatusListeners("voiceGranted", new Object[0]);
        }
        if (!paramBoolean) {
          break label254;
        }
        fireUserStatusListeners("moderatorGranted", new Object[0]);
      }
    }
    label226:
    label254:
    while ((!"moderator".equals(paramString1)) || ("moderator".equals(paramString2)))
    {
      for (;;)
      {
        return;
        ArrayList localArrayList = new ArrayList();
        localArrayList.add(paramString3);
        fireParticipantStatusListeners("voiceGranted", localArrayList);
        break;
        if ((!"participant".equals(paramString1)) || ((!"visitor".equals(paramString2)) && (!"none".equals(paramString2)))) {
          break;
        }
        if (paramBoolean)
        {
          fireUserStatusListeners("voiceRevoked", new Object[0]);
          break;
        }
        localArrayList = new ArrayList();
        localArrayList.add(paramString3);
        fireParticipantStatusListeners("voiceRevoked", localArrayList);
        break;
        paramString1 = new ArrayList();
        paramString1.add(paramString3);
        fireParticipantStatusListeners("voiceGranted", paramString1);
      }
      paramString1 = new ArrayList();
      paramString1.add(paramString3);
      fireParticipantStatusListeners("moderatorGranted", paramString1);
      return;
    }
    label280:
    if (("visitor".equals(paramString2)) || ("none".equals(paramString2)))
    {
      if (!paramBoolean) {
        break label351;
      }
      fireUserStatusListeners("voiceRevoked", new Object[0]);
    }
    while (paramBoolean)
    {
      fireUserStatusListeners("moderatorRevoked", new Object[0]);
      return;
      label351:
      paramString1 = new ArrayList();
      paramString1.add(paramString3);
      fireParticipantStatusListeners("voiceRevoked", paramString1);
    }
    paramString1 = new ArrayList();
    paramString1.add(paramString3);
    fireParticipantStatusListeners("moderatorRevoked", paramString1);
  }
  
  public static void decline(XMPPConnection paramXMPPConnection, String paramString1, String paramString2, String paramString3)
  {
    paramString1 = new Message(paramString1);
    MUCUser localMUCUser = new MUCUser();
    MUCUser.Decline localDecline = new MUCUser.Decline();
    localDecline.setTo(paramString2);
    localDecline.setReason(paramString3);
    localMUCUser.setDecline(localDecline);
    paramString1.addExtension(localMUCUser);
    paramXMPPConnection.sendPacket(paramString1);
  }
  
  private void fireInvitationRejectionListeners(String paramString1, String paramString2)
  {
    synchronized (this.invitationRejectionListeners)
    {
      InvitationRejectionListener[] arrayOfInvitationRejectionListener = new InvitationRejectionListener[this.invitationRejectionListeners.size()];
      this.invitationRejectionListeners.toArray(arrayOfInvitationRejectionListener);
      int j = arrayOfInvitationRejectionListener.length;
      int i = 0;
      if (i < j)
      {
        arrayOfInvitationRejectionListener[i].invitationDeclined(paramString1, paramString2);
        i += 1;
      }
    }
  }
  
  private void fireParticipantStatusListeners(String paramString, List<String> paramList)
  {
    ParticipantStatusListener[] arrayOfParticipantStatusListener;
    synchronized (this.participantStatusListeners)
    {
      arrayOfParticipantStatusListener = new ParticipantStatusListener[this.participantStatusListeners.size()];
      this.participantStatusListeners.toArray(arrayOfParticipantStatusListener);
    }
    try
    {
      ??? = new Class[paramList.size()];
      int i = 0;
      while (i < paramList.size())
      {
        ???[i] = String.class;
        i += 1;
        continue;
        paramString = finally;
        throw paramString;
      }
      paramString = ParticipantStatusListener.class.getDeclaredMethod(paramString, (Class[])???);
      int j = arrayOfParticipantStatusListener.length;
      i = 0;
      while (i < j)
      {
        paramString.invoke(arrayOfParticipantStatusListener[i], paramList.toArray());
        i += 1;
      }
      return;
    }
    catch (NoSuchMethodException paramString)
    {
      paramString.printStackTrace();
      return;
    }
    catch (InvocationTargetException paramString)
    {
      paramString.printStackTrace();
      return;
    }
    catch (IllegalAccessException paramString)
    {
      paramString.printStackTrace();
    }
  }
  
  private void fireSubjectUpdatedListeners(String paramString1, String paramString2)
  {
    synchronized (this.subjectUpdatedListeners)
    {
      SubjectUpdatedListener[] arrayOfSubjectUpdatedListener = new SubjectUpdatedListener[this.subjectUpdatedListeners.size()];
      this.subjectUpdatedListeners.toArray(arrayOfSubjectUpdatedListener);
      int j = arrayOfSubjectUpdatedListener.length;
      int i = 0;
      if (i < j)
      {
        arrayOfSubjectUpdatedListener[i].subjectUpdated(paramString1, paramString2);
        i += 1;
      }
    }
  }
  
  private void fireUserStatusListeners(String paramString, Object[] paramArrayOfObject)
  {
    UserStatusListener[] arrayOfUserStatusListener;
    int i;
    synchronized (this.userStatusListeners)
    {
      arrayOfUserStatusListener = new UserStatusListener[this.userStatusListeners.size()];
      this.userStatusListeners.toArray(arrayOfUserStatusListener);
      ??? = new Class[paramArrayOfObject.length];
      i = 0;
      if (i < paramArrayOfObject.length)
      {
        ???[i] = paramArrayOfObject[i].getClass();
        i += 1;
      }
    }
    try
    {
      paramString = UserStatusListener.class.getDeclaredMethod(paramString, (Class[])???);
      int j = arrayOfUserStatusListener.length;
      i = 0;
      while (i < j)
      {
        paramString.invoke(arrayOfUserStatusListener[i], paramArrayOfObject);
        i += 1;
      }
      return;
    }
    catch (NoSuchMethodException paramString)
    {
      paramString.printStackTrace();
      return;
    }
    catch (InvocationTargetException paramString)
    {
      paramString.printStackTrace();
      return;
    }
    catch (IllegalAccessException paramString)
    {
      paramString.printStackTrace();
    }
  }
  
  private Collection<Affiliate> getAffiliatesByAdmin(String paramString)
    throws XMPPException
  {
    Object localObject = new MUCAdmin();
    ((MUCAdmin)localObject).setTo(this.room);
    ((MUCAdmin)localObject).setType(IQ.Type.GET);
    ((MUCAdmin)localObject).addItem(new MUCAdmin.Item(paramString, null));
    paramString = new PacketIDFilter(((MUCAdmin)localObject).getPacketID());
    paramString = this.connection.createPacketCollector(paramString);
    this.connection.sendPacket((Packet)localObject);
    localObject = (MUCAdmin)paramString.nextResult(SmackConfiguration.getPacketReplyTimeout());
    paramString.cancel();
    if (localObject == null) {
      throw new XMPPException("No response from server.");
    }
    if (((MUCAdmin)localObject).getError() != null) {
      throw new XMPPException(((MUCAdmin)localObject).getError());
    }
    paramString = new ArrayList();
    localObject = ((MUCAdmin)localObject).getItems();
    while (((Iterator)localObject).hasNext()) {
      paramString.add(new Affiliate((MUCAdmin.Item)((Iterator)localObject).next()));
    }
    return paramString;
  }
  
  private Collection<Affiliate> getAffiliatesByOwner(String paramString)
    throws XMPPException
  {
    Object localObject = new MUCOwner();
    ((MUCOwner)localObject).setTo(this.room);
    ((MUCOwner)localObject).setType(IQ.Type.GET);
    ((MUCOwner)localObject).addItem(new MUCOwner.Item(paramString));
    paramString = new PacketIDFilter(((MUCOwner)localObject).getPacketID());
    paramString = this.connection.createPacketCollector(paramString);
    this.connection.sendPacket((Packet)localObject);
    localObject = (MUCOwner)paramString.nextResult(SmackConfiguration.getPacketReplyTimeout());
    paramString.cancel();
    if (localObject == null) {
      throw new XMPPException("No response from server.");
    }
    if (((MUCOwner)localObject).getError() != null) {
      throw new XMPPException(((MUCOwner)localObject).getError());
    }
    paramString = new ArrayList();
    localObject = ((MUCOwner)localObject).getItems();
    while (((Iterator)localObject).hasNext()) {
      paramString.add(new Affiliate((MUCOwner.Item)((Iterator)localObject).next()));
    }
    return paramString;
  }
  
  public static Collection<HostedRoom> getHostedRooms(XMPPConnection paramXMPPConnection, String paramString)
    throws XMPPException
  {
    ArrayList localArrayList = new ArrayList();
    paramXMPPConnection = ServiceDiscoveryManager.getInstanceFor(paramXMPPConnection).discoverItems(paramString).getItems();
    while (paramXMPPConnection.hasNext()) {
      localArrayList.add(new HostedRoom((DiscoverItems.Item)paramXMPPConnection.next()));
    }
    return localArrayList;
  }
  
  private static Iterator<String> getJoinedRooms(XMPPConnection paramXMPPConnection)
  {
    paramXMPPConnection = (List)joinedRooms.get(paramXMPPConnection);
    if (paramXMPPConnection != null) {
      return paramXMPPConnection.iterator();
    }
    return new ArrayList().iterator();
  }
  
  public static Iterator<String> getJoinedRooms(XMPPConnection paramXMPPConnection, String paramString)
  {
    try
    {
      ArrayList localArrayList = new ArrayList();
      paramXMPPConnection = ServiceDiscoveryManager.getInstanceFor(paramXMPPConnection).discoverItems(paramString, "http://jabber.org/protocol/muc#rooms").getItems();
      while (paramXMPPConnection.hasNext()) {
        localArrayList.add(((DiscoverItems.Item)paramXMPPConnection.next()).getEntityID());
      }
      paramXMPPConnection = localArrayList.iterator();
    }
    catch (XMPPException paramXMPPConnection)
    {
      paramXMPPConnection.printStackTrace();
      return new ArrayList().iterator();
    }
    return paramXMPPConnection;
  }
  
  private MUCUser getMUCUserExtension(Packet paramPacket)
  {
    if (paramPacket != null) {
      return (MUCUser)paramPacket.getExtension("x", "http://jabber.org/protocol/muc#user");
    }
    return null;
  }
  
  private Collection<Occupant> getOccupants(String paramString)
    throws XMPPException
  {
    Object localObject = new MUCAdmin();
    ((MUCAdmin)localObject).setTo(this.room);
    ((MUCAdmin)localObject).setType(IQ.Type.GET);
    ((MUCAdmin)localObject).addItem(new MUCAdmin.Item(null, paramString));
    paramString = new PacketIDFilter(((MUCAdmin)localObject).getPacketID());
    paramString = this.connection.createPacketCollector(paramString);
    this.connection.sendPacket((Packet)localObject);
    localObject = (MUCAdmin)paramString.nextResult(SmackConfiguration.getPacketReplyTimeout());
    paramString.cancel();
    if (localObject == null) {
      throw new XMPPException("No response from server.");
    }
    if (((MUCAdmin)localObject).getError() != null) {
      throw new XMPPException(((MUCAdmin)localObject).getError());
    }
    paramString = new ArrayList();
    localObject = ((MUCAdmin)localObject).getItems();
    while (((Iterator)localObject).hasNext()) {
      paramString.add(new Occupant((MUCAdmin.Item)((Iterator)localObject).next()));
    }
    return paramString;
  }
  
  public static RoomInfo getRoomInfo(XMPPConnection paramXMPPConnection, String paramString)
    throws XMPPException
  {
    return new RoomInfo(ServiceDiscoveryManager.getInstanceFor(paramXMPPConnection).discoverInfo(paramString));
  }
  
  public static Collection<String> getServiceNames(XMPPConnection paramXMPPConnection)
    throws XMPPException
  {
    ArrayList localArrayList = new ArrayList();
    ServiceDiscoveryManager localServiceDiscoveryManager = ServiceDiscoveryManager.getInstanceFor(paramXMPPConnection);
    paramXMPPConnection = localServiceDiscoveryManager.discoverItems(paramXMPPConnection.getServiceName()).getItems();
    while (paramXMPPConnection.hasNext())
    {
      DiscoverItems.Item localItem = (DiscoverItems.Item)paramXMPPConnection.next();
      try
      {
        if (localServiceDiscoveryManager.discoverInfo(localItem.getEntityID()).containsFeature("http://jabber.org/protocol/muc")) {
          localArrayList.add(localItem.getEntityID());
        }
      }
      catch (XMPPException localXMPPException) {}
    }
    return localArrayList;
  }
  
  private void init()
  {
    this.messageFilter = new AndFilter(new PacketFilter[] { new FromMatchesFilter(this.room), new MessageTypeFilter(Message.Type.groupchat) });
    this.messageFilter = new AndFilter(new PacketFilter[] { this.messageFilter, new PacketFilter()
    {
      public boolean accept(Packet paramAnonymousPacket)
      {
        return ((Message)paramAnonymousPacket).getBody() != null;
      }
    } });
    this.presenceFilter = new AndFilter(new PacketFilter[] { new FromMatchesFilter(this.room), new PacketTypeFilter(Presence.class) });
    this.messageCollector = new ConnectionDetachedPacketCollector();
    Object localObject = new PacketListener()
    {
      public void processPacket(Packet paramAnonymousPacket)
      {
        paramAnonymousPacket = (Message)paramAnonymousPacket;
        MultiUserChat.access$102(MultiUserChat.this, paramAnonymousPacket.getSubject());
        MultiUserChat.this.fireSubjectUpdatedListeners(paramAnonymousPacket.getSubject(), paramAnonymousPacket.getFrom());
      }
    };
    PacketListener local5 = new PacketListener()
    {
      public void processPacket(Packet paramAnonymousPacket)
      {
        Object localObject1 = (Presence)paramAnonymousPacket;
        paramAnonymousPacket = ((Presence)localObject1).getFrom();
        Object localObject2 = MultiUserChat.this.room + "/" + MultiUserChat.this.nickname;
        boolean bool = ((Presence)localObject1).getFrom().equals(localObject2);
        Object localObject3;
        if (((Presence)localObject1).getType() == Presence.Type.available)
        {
          localObject2 = (Presence)MultiUserChat.this.occupantsMap.put(paramAnonymousPacket, localObject1);
          if (localObject2 != null)
          {
            localObject3 = MultiUserChat.this.getMUCUserExtension((Packet)localObject2);
            localObject2 = ((MUCUser)localObject3).getItem().getAffiliation();
            localObject3 = ((MUCUser)localObject3).getItem().getRole();
            Object localObject4 = MultiUserChat.this.getMUCUserExtension((Packet)localObject1);
            localObject1 = ((MUCUser)localObject4).getItem().getAffiliation();
            localObject4 = ((MUCUser)localObject4).getItem().getRole();
            MultiUserChat.this.checkRoleModifications((String)localObject3, (String)localObject4, bool, paramAnonymousPacket);
            MultiUserChat.this.checkAffiliationModifications((String)localObject2, (String)localObject1, bool, paramAnonymousPacket);
          }
        }
        do
        {
          do
          {
            do
            {
              return;
            } while (bool);
            localObject1 = new ArrayList();
            ((List)localObject1).add(paramAnonymousPacket);
            MultiUserChat.this.fireParticipantStatusListeners("joined", (List)localObject1);
            return;
          } while (((Presence)localObject1).getType() != Presence.Type.unavailable);
          MultiUserChat.this.occupantsMap.remove(paramAnonymousPacket);
          localObject3 = MultiUserChat.this.getMUCUserExtension((Packet)localObject1);
          if ((localObject3 != null) && (((MUCUser)localObject3).getStatus() != null))
          {
            MultiUserChat.this.checkPresenceCode(((MUCUser)localObject3).getStatus().getCode(), ((Presence)localObject1).getFrom().equals(localObject2), (MUCUser)localObject3, paramAnonymousPacket);
            return;
          }
        } while (bool);
        localObject1 = new ArrayList();
        ((List)localObject1).add(paramAnonymousPacket);
        MultiUserChat.this.fireParticipantStatusListeners("left", (List)localObject1);
      }
    };
    PacketListener local6 = new PacketListener()
    {
      public void processPacket(Packet paramAnonymousPacket)
      {
        MUCUser localMUCUser = MultiUserChat.this.getMUCUserExtension(paramAnonymousPacket);
        if ((localMUCUser.getDecline() != null) && (((Message)paramAnonymousPacket).getType() != Message.Type.error)) {
          MultiUserChat.this.fireInvitationRejectionListeners(localMUCUser.getDecline().getFrom(), localMUCUser.getDecline().getReason());
        }
      }
    };
    localObject = new PacketMultiplexListener(this.messageCollector, local5, (PacketListener)localObject, local6);
    this.roomListenerMultiplexor = RoomListenerMultiplexor.getRoomMultiplexor(this.connection);
    this.roomListenerMultiplexor.addRoom(this.room, (PacketMultiplexListener)localObject);
  }
  
  public static boolean isServiceEnabled(XMPPConnection paramXMPPConnection, String paramString)
  {
    try
    {
      boolean bool = ServiceDiscoveryManager.getInstanceFor(paramXMPPConnection).discoverInfo(paramString).containsFeature("http://jabber.org/protocol/muc");
      return bool;
    }
    catch (XMPPException paramXMPPConnection)
    {
      paramXMPPConnection.printStackTrace();
    }
    return false;
  }
  
  public static void removeInvitationListener(XMPPConnection paramXMPPConnection, InvitationListener paramInvitationListener)
  {
    InvitationsMonitor.getInvitationsMonitor(paramXMPPConnection).removeInvitationListener(paramInvitationListener);
  }
  
  private void userHasJoined()
  {
    try
    {
      List localList = (List)joinedRooms.get(this.connection);
      Object localObject1 = localList;
      if (localList == null)
      {
        localObject1 = new ArrayList();
        joinedRooms.put(this.connection, localObject1);
      }
      ((List)localObject1).add(this.room);
      return;
    }
    finally {}
  }
  
  /* Error */
  private void userHasLeft()
  {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: getstatic 71	org/jivesoftware/smackx/muc/MultiUserChat:joinedRooms	Ljava/util/Map;
    //   5: aload_0
    //   6: getfield 107	org/jivesoftware/smackx/muc/MultiUserChat:connection	Lorg/jivesoftware/smack/XMPPConnection;
    //   9: invokeinterface 497 2 0
    //   14: checkcast 310	java/util/List
    //   17: astore_1
    //   18: aload_1
    //   19: ifnonnull +6 -> 25
    //   22: aload_0
    //   23: monitorexit
    //   24: return
    //   25: aload_1
    //   26: aload_0
    //   27: getfield 115	org/jivesoftware/smackx/muc/MultiUserChat:room	Ljava/lang/String;
    //   30: invokeinterface 621 2 0
    //   35: pop
    //   36: goto -14 -> 22
    //   39: astore_1
    //   40: aload_0
    //   41: monitorexit
    //   42: aload_1
    //   43: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	44	0	this	MultiUserChat
    //   17	9	1	localList	List
    //   39	4	1	localObject	Object
    // Exception table:
    //   from	to	target	type
    //   2	18	39	finally
    //   25	36	39	finally
  }
  
  public void addInvitationRejectionListener(InvitationRejectionListener paramInvitationRejectionListener)
  {
    synchronized (this.invitationRejectionListeners)
    {
      if (!this.invitationRejectionListeners.contains(paramInvitationRejectionListener)) {
        this.invitationRejectionListeners.add(paramInvitationRejectionListener);
      }
      return;
    }
  }
  
  public void addMessageListener(PacketListener paramPacketListener)
  {
    this.connection.addPacketListener(paramPacketListener, this.messageFilter);
    this.connectionListeners.add(paramPacketListener);
  }
  
  public void addParticipantListener(PacketListener paramPacketListener)
  {
    this.connection.addPacketListener(paramPacketListener, this.presenceFilter);
    this.connectionListeners.add(paramPacketListener);
  }
  
  public void addParticipantStatusListener(ParticipantStatusListener paramParticipantStatusListener)
  {
    synchronized (this.participantStatusListeners)
    {
      if (!this.participantStatusListeners.contains(paramParticipantStatusListener)) {
        this.participantStatusListeners.add(paramParticipantStatusListener);
      }
      return;
    }
  }
  
  public void addPresenceInterceptor(PacketInterceptor paramPacketInterceptor)
  {
    this.presenceInterceptors.add(paramPacketInterceptor);
  }
  
  public void addSubjectUpdatedListener(SubjectUpdatedListener paramSubjectUpdatedListener)
  {
    synchronized (this.subjectUpdatedListeners)
    {
      if (!this.subjectUpdatedListeners.contains(paramSubjectUpdatedListener)) {
        this.subjectUpdatedListeners.add(paramSubjectUpdatedListener);
      }
      return;
    }
  }
  
  public void addUserStatusListener(UserStatusListener paramUserStatusListener)
  {
    synchronized (this.userStatusListeners)
    {
      if (!this.userStatusListeners.contains(paramUserStatusListener)) {
        this.userStatusListeners.add(paramUserStatusListener);
      }
      return;
    }
  }
  
  public void banUser(String paramString1, String paramString2)
    throws XMPPException
  {
    changeAffiliationByAdmin(paramString1, "outcast", paramString2);
  }
  
  public void banUsers(Collection<String> paramCollection)
    throws XMPPException
  {
    changeAffiliationByAdmin(paramCollection, "outcast");
  }
  
  public void changeAvailabilityStatus(String paramString, Presence.Mode paramMode)
  {
    if ((this.nickname == null) || (this.nickname.equals(""))) {
      throw new IllegalArgumentException("Nickname must not be null or blank.");
    }
    if (!this.joined) {
      throw new IllegalStateException("Must be logged into the room to change the availability status.");
    }
    Presence localPresence = new Presence(Presence.Type.available);
    localPresence.setStatus(paramString);
    localPresence.setMode(paramMode);
    localPresence.setTo(this.room + "/" + this.nickname);
    paramString = this.presenceInterceptors.iterator();
    while (paramString.hasNext()) {
      ((PacketInterceptor)paramString.next()).interceptPacket(localPresence);
    }
    this.connection.sendPacket(localPresence);
  }
  
  public void changeNickname(String paramString)
    throws XMPPException
  {
    if ((paramString == null) || (paramString.equals(""))) {
      throw new IllegalArgumentException("Nickname must not be null or blank.");
    }
    if (!this.joined) {
      throw new IllegalStateException("Must be logged into the room to change nickname.");
    }
    Presence localPresence = new Presence(Presence.Type.available);
    localPresence.setTo(this.room + "/" + paramString);
    Object localObject = this.presenceInterceptors.iterator();
    while (((Iterator)localObject).hasNext()) {
      ((PacketInterceptor)((Iterator)localObject).next()).interceptPacket(localPresence);
    }
    localObject = new AndFilter(new PacketFilter[] { new FromMatchesFilter(this.room + "/" + paramString), new PacketTypeFilter(Presence.class) });
    localObject = this.connection.createPacketCollector((PacketFilter)localObject);
    this.connection.sendPacket(localPresence);
    localPresence = (Presence)((PacketCollector)localObject).nextResult(SmackConfiguration.getPacketReplyTimeout());
    ((PacketCollector)localObject).cancel();
    if (localPresence == null) {
      throw new XMPPException("No response from server.");
    }
    if (localPresence.getError() != null) {
      throw new XMPPException(localPresence.getError());
    }
    this.nickname = paramString;
  }
  
  public void changeSubject(final String paramString)
    throws XMPPException
  {
    Message localMessage = new Message(this.room, Message.Type.groupchat);
    localMessage.setSubject(paramString);
    paramString = new AndFilter(new PacketFilter[] { new AndFilter(new PacketFilter[] { new FromMatchesFilter(this.room), new PacketTypeFilter(Message.class) }), new PacketFilter()
    {
      public boolean accept(Packet paramAnonymousPacket)
      {
        paramAnonymousPacket = (Message)paramAnonymousPacket;
        return paramString.equals(paramAnonymousPacket.getSubject());
      }
    } });
    paramString = this.connection.createPacketCollector(paramString);
    this.connection.sendPacket(localMessage);
    localMessage = (Message)paramString.nextResult(SmackConfiguration.getPacketReplyTimeout());
    paramString.cancel();
    if (localMessage == null) {
      throw new XMPPException("No response from server.");
    }
    if (localMessage.getError() != null) {
      throw new XMPPException(localMessage.getError());
    }
  }
  
  /* Error */
  public void create(String paramString)
    throws XMPPException
  {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_1
    //   3: ifnull +13 -> 16
    //   6: aload_1
    //   7: ldc_w 655
    //   10: invokevirtual 300	java/lang/String:equals	(Ljava/lang/Object;)Z
    //   13: ifeq +19 -> 32
    //   16: new 657	java/lang/IllegalArgumentException
    //   19: dup
    //   20: ldc_w 659
    //   23: invokespecial 660	java/lang/IllegalArgumentException:<init>	(Ljava/lang/String;)V
    //   26: athrow
    //   27: astore_1
    //   28: aload_0
    //   29: monitorexit
    //   30: aload_1
    //   31: athrow
    //   32: aload_0
    //   33: getfield 85	org/jivesoftware/smackx/muc/MultiUserChat:joined	Z
    //   36: ifeq +14 -> 50
    //   39: new 662	java/lang/IllegalStateException
    //   42: dup
    //   43: ldc_w 717
    //   46: invokespecial 665	java/lang/IllegalStateException:<init>	(Ljava/lang/String;)V
    //   49: athrow
    //   50: new 578	org/jivesoftware/smack/packet/Presence
    //   53: dup
    //   54: getstatic 671	org/jivesoftware/smack/packet/Presence$Type:available	Lorg/jivesoftware/smack/packet/Presence$Type;
    //   57: invokespecial 674	org/jivesoftware/smack/packet/Presence:<init>	(Lorg/jivesoftware/smack/packet/Presence$Type;)V
    //   60: astore_3
    //   61: aload_3
    //   62: new 683	java/lang/StringBuilder
    //   65: dup
    //   66: invokespecial 684	java/lang/StringBuilder:<init>	()V
    //   69: aload_0
    //   70: getfield 115	org/jivesoftware/smackx/muc/MultiUserChat:room	Ljava/lang/String;
    //   73: invokevirtual 688	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   76: ldc_w 690
    //   79: invokevirtual 688	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   82: aload_1
    //   83: invokevirtual 688	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   86: invokevirtual 693	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   89: invokevirtual 694	org/jivesoftware/smack/packet/Presence:setTo	(Ljava/lang/String;)V
    //   92: aload_3
    //   93: new 719	org/jivesoftware/smackx/packet/MUCInitialPresence
    //   96: dup
    //   97: invokespecial 720	org/jivesoftware/smackx/packet/MUCInitialPresence:<init>	()V
    //   100: invokevirtual 721	org/jivesoftware/smack/packet/Presence:addExtension	(Lorg/jivesoftware/smack/packet/PacketExtension;)V
    //   103: aload_0
    //   104: getfield 103	org/jivesoftware/smackx/muc/MultiUserChat:presenceInterceptors	Ljava/util/List;
    //   107: invokeinterface 498 1 0
    //   112: astore 4
    //   114: aload 4
    //   116: invokeinterface 270 1 0
    //   121: ifeq +22 -> 143
    //   124: aload 4
    //   126: invokeinterface 274 1 0
    //   131: checkcast 696	org/jivesoftware/smack/PacketInterceptor
    //   134: aload_3
    //   135: invokeinterface 699 2 0
    //   140: goto -26 -> 114
    //   143: new 550	org/jivesoftware/smack/filter/AndFilter
    //   146: dup
    //   147: iconst_2
    //   148: anewarray 552	org/jivesoftware/smack/filter/PacketFilter
    //   151: dup
    //   152: iconst_0
    //   153: new 554	org/jivesoftware/smack/filter/FromMatchesFilter
    //   156: dup
    //   157: new 683	java/lang/StringBuilder
    //   160: dup
    //   161: invokespecial 684	java/lang/StringBuilder:<init>	()V
    //   164: aload_0
    //   165: getfield 115	org/jivesoftware/smackx/muc/MultiUserChat:room	Ljava/lang/String;
    //   168: invokevirtual 688	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   171: ldc_w 690
    //   174: invokevirtual 688	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   177: aload_1
    //   178: invokevirtual 688	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   181: invokevirtual 693	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   184: invokespecial 555	org/jivesoftware/smack/filter/FromMatchesFilter:<init>	(Ljava/lang/String;)V
    //   187: aastore
    //   188: dup
    //   189: iconst_1
    //   190: new 576	org/jivesoftware/smack/filter/PacketTypeFilter
    //   193: dup
    //   194: ldc_w 578
    //   197: invokespecial 581	org/jivesoftware/smack/filter/PacketTypeFilter:<init>	(Ljava/lang/Class;)V
    //   200: aastore
    //   201: invokespecial 569	org/jivesoftware/smack/filter/AndFilter:<init>	([Lorg/jivesoftware/smack/filter/PacketFilter;)V
    //   204: astore 4
    //   206: aload_0
    //   207: getfield 107	org/jivesoftware/smackx/muc/MultiUserChat:connection	Lorg/jivesoftware/smack/XMPPConnection;
    //   210: aload 4
    //   212: invokevirtual 225	org/jivesoftware/smack/XMPPConnection:createPacketCollector	(Lorg/jivesoftware/smack/filter/PacketFilter;)Lorg/jivesoftware/smack/PacketCollector;
    //   215: astore 4
    //   217: aload_0
    //   218: getfield 107	org/jivesoftware/smackx/muc/MultiUserChat:connection	Lorg/jivesoftware/smack/XMPPConnection;
    //   221: aload_3
    //   222: invokevirtual 229	org/jivesoftware/smack/XMPPConnection:sendPacket	(Lorg/jivesoftware/smack/packet/Packet;)V
    //   225: aload 4
    //   227: invokestatic 235	org/jivesoftware/smack/SmackConfiguration:getPacketReplyTimeout	()I
    //   230: i2l
    //   231: invokevirtual 241	org/jivesoftware/smack/PacketCollector:nextResult	(J)Lorg/jivesoftware/smack/packet/Packet;
    //   234: checkcast 578	org/jivesoftware/smack/packet/Presence
    //   237: astore_3
    //   238: aload 4
    //   240: invokevirtual 246	org/jivesoftware/smack/PacketCollector:cancel	()V
    //   243: aload_3
    //   244: ifnonnull +13 -> 257
    //   247: new 183	org/jivesoftware/smack/XMPPException
    //   250: dup
    //   251: ldc 248
    //   253: invokespecial 249	org/jivesoftware/smack/XMPPException:<init>	(Ljava/lang/String;)V
    //   256: athrow
    //   257: aload_3
    //   258: invokevirtual 703	org/jivesoftware/smack/packet/Presence:getError	()Lorg/jivesoftware/smack/packet/XMPPError;
    //   261: ifnull +15 -> 276
    //   264: new 183	org/jivesoftware/smack/XMPPException
    //   267: dup
    //   268: aload_3
    //   269: invokevirtual 703	org/jivesoftware/smack/packet/Presence:getError	()Lorg/jivesoftware/smack/packet/XMPPError;
    //   272: invokespecial 256	org/jivesoftware/smack/XMPPException:<init>	(Lorg/jivesoftware/smack/packet/XMPPError;)V
    //   275: athrow
    //   276: aload_0
    //   277: aload_1
    //   278: putfield 83	org/jivesoftware/smackx/muc/MultiUserChat:nickname	Ljava/lang/String;
    //   281: aload_0
    //   282: iconst_1
    //   283: putfield 85	org/jivesoftware/smackx/muc/MultiUserChat:joined	Z
    //   286: aload_0
    //   287: invokespecial 723	org/jivesoftware/smackx/muc/MultiUserChat:userHasJoined	()V
    //   290: aload_0
    //   291: aload_3
    //   292: invokespecial 154	org/jivesoftware/smackx/muc/MultiUserChat:getMUCUserExtension	(Lorg/jivesoftware/smack/packet/Packet;)Lorg/jivesoftware/smackx/packet/MUCUser;
    //   295: astore_1
    //   296: aload_1
    //   297: ifnull +31 -> 328
    //   300: aload_1
    //   301: invokevirtual 727	org/jivesoftware/smackx/packet/MUCUser:getStatus	()Lorg/jivesoftware/smackx/packet/MUCUser$Status;
    //   304: ifnull +24 -> 328
    //   307: ldc_w 729
    //   310: aload_1
    //   311: invokevirtual 727	org/jivesoftware/smackx/packet/MUCUser:getStatus	()Lorg/jivesoftware/smackx/packet/MUCUser$Status;
    //   314: invokevirtual 734	org/jivesoftware/smackx/packet/MUCUser$Status:getCode	()Ljava/lang/String;
    //   317: invokevirtual 300	java/lang/String:equals	(Ljava/lang/Object;)Z
    //   320: istore_2
    //   321: iload_2
    //   322: ifeq +6 -> 328
    //   325: aload_0
    //   326: monitorexit
    //   327: return
    //   328: aload_0
    //   329: invokevirtual 737	org/jivesoftware/smackx/muc/MultiUserChat:leave	()V
    //   332: new 183	org/jivesoftware/smack/XMPPException
    //   335: dup
    //   336: ldc_w 739
    //   339: invokespecial 249	org/jivesoftware/smack/XMPPException:<init>	(Ljava/lang/String;)V
    //   342: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	343	0	this	MultiUserChat
    //   0	343	1	paramString	String
    //   320	2	2	bool	boolean
    //   60	232	3	localPresence	Presence
    //   112	127	4	localObject	Object
    // Exception table:
    //   from	to	target	type
    //   6	16	27	finally
    //   16	27	27	finally
    //   32	50	27	finally
    //   50	114	27	finally
    //   114	140	27	finally
    //   143	243	27	finally
    //   247	257	27	finally
    //   257	276	27	finally
    //   276	296	27	finally
    //   300	321	27	finally
    //   328	343	27	finally
  }
  
  public Message createMessage()
  {
    return new Message(this.room, Message.Type.groupchat);
  }
  
  public Chat createPrivateChat(String paramString, MessageListener paramMessageListener)
  {
    return this.connection.getChatManager().createChat(paramString, paramMessageListener);
  }
  
  public void destroy(String paramString1, String paramString2)
    throws XMPPException
  {
    MUCOwner localMUCOwner = new MUCOwner();
    localMUCOwner.setTo(this.room);
    localMUCOwner.setType(IQ.Type.SET);
    MUCOwner.Destroy localDestroy = new MUCOwner.Destroy();
    localDestroy.setReason(paramString1);
    localDestroy.setJid(paramString2);
    localMUCOwner.setDestroy(localDestroy);
    paramString1 = new PacketIDFilter(localMUCOwner.getPacketID());
    paramString1 = this.connection.createPacketCollector(paramString1);
    this.connection.sendPacket(localMUCOwner);
    paramString2 = (IQ)paramString1.nextResult(SmackConfiguration.getPacketReplyTimeout());
    paramString1.cancel();
    if (paramString2 == null) {
      throw new XMPPException("No response from server.");
    }
    if (paramString2.getError() != null) {
      throw new XMPPException(paramString2.getError());
    }
    this.occupantsMap.clear();
    this.nickname = null;
    this.joined = false;
    userHasLeft();
  }
  
  public void finalize()
    throws Throwable
  {
    super.finalize();
    try
    {
      if (this.connection != null)
      {
        this.roomListenerMultiplexor.removeRoom(this.room);
        Iterator localIterator = this.connectionListeners.iterator();
        while (localIterator.hasNext())
        {
          PacketListener localPacketListener = (PacketListener)localIterator.next();
          this.connection.removePacketListener(localPacketListener);
        }
      }
      return;
    }
    catch (Exception localException) {}
  }
  
  public Collection<Affiliate> getAdmins()
    throws XMPPException
  {
    return getAffiliatesByOwner("admin");
  }
  
  public Form getConfigurationForm()
    throws XMPPException
  {
    Object localObject1 = new MUCOwner();
    ((MUCOwner)localObject1).setTo(this.room);
    ((MUCOwner)localObject1).setType(IQ.Type.GET);
    Object localObject2 = new PacketIDFilter(((MUCOwner)localObject1).getPacketID());
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
    return Form.getFormFrom((Packet)localObject1);
  }
  
  public Collection<Affiliate> getMembers()
    throws XMPPException
  {
    return getAffiliatesByAdmin("member");
  }
  
  public Collection<Occupant> getModerators()
    throws XMPPException
  {
    return getOccupants("moderator");
  }
  
  public String getNickname()
  {
    return this.nickname;
  }
  
  public Occupant getOccupant(String paramString)
  {
    paramString = (Presence)this.occupantsMap.get(paramString);
    if (paramString != null) {
      return new Occupant(paramString);
    }
    return null;
  }
  
  public Presence getOccupantPresence(String paramString)
  {
    return (Presence)this.occupantsMap.get(paramString);
  }
  
  public Iterator<String> getOccupants()
  {
    return Collections.unmodifiableList(new ArrayList(this.occupantsMap.keySet())).iterator();
  }
  
  public int getOccupantsCount()
  {
    return this.occupantsMap.size();
  }
  
  public Collection<Affiliate> getOutcasts()
    throws XMPPException
  {
    return getAffiliatesByAdmin("outcast");
  }
  
  public Collection<Affiliate> getOwners()
    throws XMPPException
  {
    return getAffiliatesByOwner("owner");
  }
  
  public Collection<Occupant> getParticipants()
    throws XMPPException
  {
    return getOccupants("participant");
  }
  
  public Form getRegistrationForm()
    throws XMPPException
  {
    Object localObject1 = new Registration();
    ((Registration)localObject1).setType(IQ.Type.GET);
    ((Registration)localObject1).setTo(this.room);
    Object localObject2 = new AndFilter(new PacketFilter[] { new PacketIDFilter(((Registration)localObject1).getPacketID()), new PacketTypeFilter(IQ.class) });
    localObject2 = this.connection.createPacketCollector((PacketFilter)localObject2);
    this.connection.sendPacket((Packet)localObject1);
    localObject1 = (IQ)((PacketCollector)localObject2).nextResult(SmackConfiguration.getPacketReplyTimeout());
    ((PacketCollector)localObject2).cancel();
    if (localObject1 == null) {
      throw new XMPPException("No response from server.");
    }
    if (((IQ)localObject1).getType() == IQ.Type.ERROR) {
      throw new XMPPException(((IQ)localObject1).getError());
    }
    return Form.getFormFrom((Packet)localObject1);
  }
  
  public String getReservedNickname()
  {
    try
    {
      Object localObject = ServiceDiscoveryManager.getInstanceFor(this.connection).discoverInfo(this.room, "x-roomuser-item").getIdentities();
      if (((Iterator)localObject).hasNext())
      {
        localObject = ((DiscoverInfo.Identity)((Iterator)localObject).next()).getName();
        return localObject;
      }
      return null;
    }
    catch (XMPPException localXMPPException)
    {
      localXMPPException.printStackTrace();
    }
    return null;
  }
  
  public String getRoom()
  {
    return this.room;
  }
  
  public String getSubject()
  {
    return this.subject;
  }
  
  public void grantAdmin(String paramString)
    throws XMPPException
  {
    changeAffiliationByOwner(paramString, "admin");
  }
  
  public void grantAdmin(Collection<String> paramCollection)
    throws XMPPException
  {
    changeAffiliationByOwner(paramCollection, "admin");
  }
  
  public void grantMembership(String paramString)
    throws XMPPException
  {
    changeAffiliationByAdmin(paramString, "member", null);
  }
  
  public void grantMembership(Collection<String> paramCollection)
    throws XMPPException
  {
    changeAffiliationByAdmin(paramCollection, "member");
  }
  
  public void grantModerator(String paramString)
    throws XMPPException
  {
    changeRole(paramString, "moderator", null);
  }
  
  public void grantModerator(Collection<String> paramCollection)
    throws XMPPException
  {
    changeRole(paramCollection, "moderator");
  }
  
  public void grantOwnership(String paramString)
    throws XMPPException
  {
    changeAffiliationByOwner(paramString, "owner");
  }
  
  public void grantOwnership(Collection<String> paramCollection)
    throws XMPPException
  {
    changeAffiliationByOwner(paramCollection, "owner");
  }
  
  public void grantVoice(String paramString)
    throws XMPPException
  {
    changeRole(paramString, "participant", null);
  }
  
  public void grantVoice(Collection<String> paramCollection)
    throws XMPPException
  {
    changeRole(paramCollection, "participant");
  }
  
  public void invite(String paramString1, String paramString2)
  {
    invite(new Message(), paramString1, paramString2);
  }
  
  public void invite(Message paramMessage, String paramString1, String paramString2)
  {
    paramMessage.setTo(this.room);
    MUCUser localMUCUser = new MUCUser();
    MUCUser.Invite localInvite = new MUCUser.Invite();
    localInvite.setTo(paramString1);
    localInvite.setReason(paramString2);
    localMUCUser.setInvite(localInvite);
    paramMessage.addExtension(localMUCUser);
    this.connection.sendPacket(paramMessage);
  }
  
  public boolean isJoined()
  {
    return this.joined;
  }
  
  public void join(String paramString)
    throws XMPPException
  {
    join(paramString, null, null, SmackConfiguration.getPacketReplyTimeout());
  }
  
  public void join(String paramString1, String paramString2)
    throws XMPPException
  {
    join(paramString1, paramString2, null, SmackConfiguration.getPacketReplyTimeout());
  }
  
  /* Error */
  public void join(String paramString1, String paramString2, DiscussionHistory paramDiscussionHistory, long paramLong)
    throws XMPPException
  {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_1
    //   3: ifnull +13 -> 16
    //   6: aload_1
    //   7: ldc_w 655
    //   10: invokevirtual 300	java/lang/String:equals	(Ljava/lang/Object;)Z
    //   13: ifeq +19 -> 32
    //   16: new 657	java/lang/IllegalArgumentException
    //   19: dup
    //   20: ldc_w 659
    //   23: invokespecial 660	java/lang/IllegalArgumentException:<init>	(Ljava/lang/String;)V
    //   26: athrow
    //   27: astore_1
    //   28: aload_0
    //   29: monitorexit
    //   30: aload_1
    //   31: athrow
    //   32: aload_0
    //   33: getfield 85	org/jivesoftware/smackx/muc/MultiUserChat:joined	Z
    //   36: ifeq +7 -> 43
    //   39: aload_0
    //   40: invokevirtual 737	org/jivesoftware/smackx/muc/MultiUserChat:leave	()V
    //   43: new 578	org/jivesoftware/smack/packet/Presence
    //   46: dup
    //   47: getstatic 671	org/jivesoftware/smack/packet/Presence$Type:available	Lorg/jivesoftware/smack/packet/Presence$Type;
    //   50: invokespecial 674	org/jivesoftware/smack/packet/Presence:<init>	(Lorg/jivesoftware/smack/packet/Presence$Type;)V
    //   53: astore 6
    //   55: aload 6
    //   57: new 683	java/lang/StringBuilder
    //   60: dup
    //   61: invokespecial 684	java/lang/StringBuilder:<init>	()V
    //   64: aload_0
    //   65: getfield 115	org/jivesoftware/smackx/muc/MultiUserChat:room	Ljava/lang/String;
    //   68: invokevirtual 688	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   71: ldc_w 690
    //   74: invokevirtual 688	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   77: aload_1
    //   78: invokevirtual 688	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   81: invokevirtual 693	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   84: invokevirtual 694	org/jivesoftware/smack/packet/Presence:setTo	(Ljava/lang/String;)V
    //   87: new 719	org/jivesoftware/smackx/packet/MUCInitialPresence
    //   90: dup
    //   91: invokespecial 720	org/jivesoftware/smackx/packet/MUCInitialPresence:<init>	()V
    //   94: astore 7
    //   96: aload_2
    //   97: ifnull +9 -> 106
    //   100: aload 7
    //   102: aload_2
    //   103: invokevirtual 889	org/jivesoftware/smackx/packet/MUCInitialPresence:setPassword	(Ljava/lang/String;)V
    //   106: aload_3
    //   107: ifnull +12 -> 119
    //   110: aload 7
    //   112: aload_3
    //   113: invokevirtual 895	org/jivesoftware/smackx/muc/DiscussionHistory:getMUCHistory	()Lorg/jivesoftware/smackx/packet/MUCInitialPresence$History;
    //   116: invokevirtual 899	org/jivesoftware/smackx/packet/MUCInitialPresence:setHistory	(Lorg/jivesoftware/smackx/packet/MUCInitialPresence$History;)V
    //   119: aload 6
    //   121: aload 7
    //   123: invokevirtual 721	org/jivesoftware/smack/packet/Presence:addExtension	(Lorg/jivesoftware/smack/packet/PacketExtension;)V
    //   126: aload_0
    //   127: getfield 103	org/jivesoftware/smackx/muc/MultiUserChat:presenceInterceptors	Ljava/util/List;
    //   130: invokeinterface 498 1 0
    //   135: astore_2
    //   136: aload_2
    //   137: invokeinterface 270 1 0
    //   142: ifeq +22 -> 164
    //   145: aload_2
    //   146: invokeinterface 274 1 0
    //   151: checkcast 696	org/jivesoftware/smack/PacketInterceptor
    //   154: aload 6
    //   156: invokeinterface 699 2 0
    //   161: goto -25 -> 136
    //   164: new 550	org/jivesoftware/smack/filter/AndFilter
    //   167: dup
    //   168: iconst_2
    //   169: anewarray 552	org/jivesoftware/smack/filter/PacketFilter
    //   172: dup
    //   173: iconst_0
    //   174: new 554	org/jivesoftware/smack/filter/FromMatchesFilter
    //   177: dup
    //   178: new 683	java/lang/StringBuilder
    //   181: dup
    //   182: invokespecial 684	java/lang/StringBuilder:<init>	()V
    //   185: aload_0
    //   186: getfield 115	org/jivesoftware/smackx/muc/MultiUserChat:room	Ljava/lang/String;
    //   189: invokevirtual 688	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   192: ldc_w 690
    //   195: invokevirtual 688	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   198: aload_1
    //   199: invokevirtual 688	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   202: invokevirtual 693	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   205: invokespecial 555	org/jivesoftware/smack/filter/FromMatchesFilter:<init>	(Ljava/lang/String;)V
    //   208: aastore
    //   209: dup
    //   210: iconst_1
    //   211: new 576	org/jivesoftware/smack/filter/PacketTypeFilter
    //   214: dup
    //   215: ldc_w 578
    //   218: invokespecial 581	org/jivesoftware/smack/filter/PacketTypeFilter:<init>	(Ljava/lang/Class;)V
    //   221: aastore
    //   222: invokespecial 569	org/jivesoftware/smack/filter/AndFilter:<init>	([Lorg/jivesoftware/smack/filter/PacketFilter;)V
    //   225: astore_3
    //   226: aconst_null
    //   227: astore_2
    //   228: aload_0
    //   229: getfield 107	org/jivesoftware/smackx/muc/MultiUserChat:connection	Lorg/jivesoftware/smack/XMPPConnection;
    //   232: aload_3
    //   233: invokevirtual 225	org/jivesoftware/smack/XMPPConnection:createPacketCollector	(Lorg/jivesoftware/smack/filter/PacketFilter;)Lorg/jivesoftware/smack/PacketCollector;
    //   236: astore_3
    //   237: aload_3
    //   238: astore_2
    //   239: aload_0
    //   240: getfield 107	org/jivesoftware/smackx/muc/MultiUserChat:connection	Lorg/jivesoftware/smack/XMPPConnection;
    //   243: aload 6
    //   245: invokevirtual 229	org/jivesoftware/smack/XMPPConnection:sendPacket	(Lorg/jivesoftware/smack/packet/Packet;)V
    //   248: aload_3
    //   249: astore_2
    //   250: aload_3
    //   251: lload 4
    //   253: invokevirtual 241	org/jivesoftware/smack/PacketCollector:nextResult	(J)Lorg/jivesoftware/smack/packet/Packet;
    //   256: checkcast 578	org/jivesoftware/smack/packet/Presence
    //   259: astore 6
    //   261: aload_3
    //   262: ifnull +7 -> 269
    //   265: aload_3
    //   266: invokevirtual 246	org/jivesoftware/smack/PacketCollector:cancel	()V
    //   269: aload 6
    //   271: ifnonnull +24 -> 295
    //   274: new 183	org/jivesoftware/smack/XMPPException
    //   277: dup
    //   278: ldc 248
    //   280: invokespecial 249	org/jivesoftware/smack/XMPPException:<init>	(Ljava/lang/String;)V
    //   283: athrow
    //   284: astore_1
    //   285: aload_2
    //   286: ifnull +7 -> 293
    //   289: aload_2
    //   290: invokevirtual 246	org/jivesoftware/smack/PacketCollector:cancel	()V
    //   293: aload_1
    //   294: athrow
    //   295: aload 6
    //   297: invokevirtual 703	org/jivesoftware/smack/packet/Presence:getError	()Lorg/jivesoftware/smack/packet/XMPPError;
    //   300: ifnull +16 -> 316
    //   303: new 183	org/jivesoftware/smack/XMPPException
    //   306: dup
    //   307: aload 6
    //   309: invokevirtual 703	org/jivesoftware/smack/packet/Presence:getError	()Lorg/jivesoftware/smack/packet/XMPPError;
    //   312: invokespecial 256	org/jivesoftware/smack/XMPPException:<init>	(Lorg/jivesoftware/smack/packet/XMPPError;)V
    //   315: athrow
    //   316: aload_0
    //   317: aload_1
    //   318: putfield 83	org/jivesoftware/smackx/muc/MultiUserChat:nickname	Ljava/lang/String;
    //   321: aload_0
    //   322: iconst_1
    //   323: putfield 85	org/jivesoftware/smackx/muc/MultiUserChat:joined	Z
    //   326: aload_0
    //   327: invokespecial 723	org/jivesoftware/smackx/muc/MultiUserChat:userHasJoined	()V
    //   330: aload_0
    //   331: monitorexit
    //   332: return
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	333	0	this	MultiUserChat
    //   0	333	1	paramString1	String
    //   0	333	2	paramString2	String
    //   0	333	3	paramDiscussionHistory	DiscussionHistory
    //   0	333	4	paramLong	long
    //   53	255	6	localPresence	Presence
    //   94	28	7	localMUCInitialPresence	org.jivesoftware.smackx.packet.MUCInitialPresence
    // Exception table:
    //   from	to	target	type
    //   6	16	27	finally
    //   16	27	27	finally
    //   32	43	27	finally
    //   43	96	27	finally
    //   100	106	27	finally
    //   110	119	27	finally
    //   119	136	27	finally
    //   136	161	27	finally
    //   164	226	27	finally
    //   265	269	27	finally
    //   274	284	27	finally
    //   289	293	27	finally
    //   293	295	27	finally
    //   295	316	27	finally
    //   316	330	27	finally
    //   228	237	284	finally
    //   239	248	284	finally
    //   250	261	284	finally
  }
  
  public void kickParticipant(String paramString1, String paramString2)
    throws XMPPException
  {
    changeRole(paramString1, "none", paramString2);
  }
  
  public void leave()
  {
    for (;;)
    {
      try
      {
        boolean bool = this.joined;
        if (!bool) {
          return;
        }
        Presence localPresence = new Presence(Presence.Type.unavailable);
        localPresence.setTo(this.room + "/" + this.nickname);
        Iterator localIterator = this.presenceInterceptors.iterator();
        if (localIterator.hasNext())
        {
          ((PacketInterceptor)localIterator.next()).interceptPacket(localPresence);
          continue;
        }
        this.connection.sendPacket(localPacket);
      }
      finally {}
      this.occupantsMap.clear();
      this.nickname = null;
      this.joined = false;
      userHasLeft();
    }
  }
  
  public Message nextMessage()
  {
    return (Message)this.messageCollector.nextResult();
  }
  
  public Message nextMessage(long paramLong)
  {
    return (Message)this.messageCollector.nextResult(paramLong);
  }
  
  public Message pollMessage()
  {
    return (Message)this.messageCollector.pollResult();
  }
  
  public void removeInvitationRejectionListener(InvitationRejectionListener paramInvitationRejectionListener)
  {
    synchronized (this.invitationRejectionListeners)
    {
      this.invitationRejectionListeners.remove(paramInvitationRejectionListener);
      return;
    }
  }
  
  public void removeMessageListener(PacketListener paramPacketListener)
  {
    this.connection.removePacketListener(paramPacketListener);
    this.connectionListeners.remove(paramPacketListener);
  }
  
  public void removeParticipantListener(PacketListener paramPacketListener)
  {
    this.connection.removePacketListener(paramPacketListener);
    this.connectionListeners.remove(paramPacketListener);
  }
  
  public void removeParticipantStatusListener(ParticipantStatusListener paramParticipantStatusListener)
  {
    synchronized (this.participantStatusListeners)
    {
      this.participantStatusListeners.remove(paramParticipantStatusListener);
      return;
    }
  }
  
  public void removePresenceInterceptor(PacketInterceptor paramPacketInterceptor)
  {
    this.presenceInterceptors.remove(paramPacketInterceptor);
  }
  
  public void removeSubjectUpdatedListener(SubjectUpdatedListener paramSubjectUpdatedListener)
  {
    synchronized (this.subjectUpdatedListeners)
    {
      this.subjectUpdatedListeners.remove(paramSubjectUpdatedListener);
      return;
    }
  }
  
  public void removeUserStatusListener(UserStatusListener paramUserStatusListener)
  {
    synchronized (this.userStatusListeners)
    {
      this.userStatusListeners.remove(paramUserStatusListener);
      return;
    }
  }
  
  public void revokeAdmin(String paramString)
    throws XMPPException
  {
    changeAffiliationByOwner(paramString, "member");
  }
  
  public void revokeAdmin(Collection<String> paramCollection)
    throws XMPPException
  {
    changeAffiliationByOwner(paramCollection, "member");
  }
  
  public void revokeMembership(String paramString)
    throws XMPPException
  {
    changeAffiliationByAdmin(paramString, "none", null);
  }
  
  public void revokeMembership(Collection<String> paramCollection)
    throws XMPPException
  {
    changeAffiliationByAdmin(paramCollection, "none");
  }
  
  public void revokeModerator(String paramString)
    throws XMPPException
  {
    changeRole(paramString, "participant", null);
  }
  
  public void revokeModerator(Collection<String> paramCollection)
    throws XMPPException
  {
    changeRole(paramCollection, "participant");
  }
  
  public void revokeOwnership(String paramString)
    throws XMPPException
  {
    changeAffiliationByOwner(paramString, "admin");
  }
  
  public void revokeOwnership(Collection<String> paramCollection)
    throws XMPPException
  {
    changeAffiliationByOwner(paramCollection, "admin");
  }
  
  public void revokeVoice(String paramString)
    throws XMPPException
  {
    changeRole(paramString, "visitor", null);
  }
  
  public void revokeVoice(Collection<String> paramCollection)
    throws XMPPException
  {
    changeRole(paramCollection, "visitor");
  }
  
  public void sendConfigurationForm(Form paramForm)
    throws XMPPException
  {
    Object localObject = new MUCOwner();
    ((MUCOwner)localObject).setTo(this.room);
    ((MUCOwner)localObject).setType(IQ.Type.SET);
    ((MUCOwner)localObject).addExtension(paramForm.getDataFormToSend());
    paramForm = new PacketIDFilter(((MUCOwner)localObject).getPacketID());
    paramForm = this.connection.createPacketCollector(paramForm);
    this.connection.sendPacket((Packet)localObject);
    localObject = (IQ)paramForm.nextResult(SmackConfiguration.getPacketReplyTimeout());
    paramForm.cancel();
    if (localObject == null) {
      throw new XMPPException("No response from server.");
    }
    if (((IQ)localObject).getError() != null) {
      throw new XMPPException(((IQ)localObject).getError());
    }
  }
  
  public void sendMessage(String paramString)
    throws XMPPException
  {
    Message localMessage = new Message(this.room, Message.Type.groupchat);
    localMessage.setBody(paramString);
    this.connection.sendPacket(localMessage);
  }
  
  public void sendMessage(Message paramMessage)
    throws XMPPException
  {
    this.connection.sendPacket(paramMessage);
  }
  
  public void sendRegistrationForm(Form paramForm)
    throws XMPPException
  {
    Object localObject = new Registration();
    ((Registration)localObject).setType(IQ.Type.SET);
    ((Registration)localObject).setTo(this.room);
    ((Registration)localObject).addExtension(paramForm.getDataFormToSend());
    paramForm = new AndFilter(new PacketFilter[] { new PacketIDFilter(((Registration)localObject).getPacketID()), new PacketTypeFilter(IQ.class) });
    paramForm = this.connection.createPacketCollector(paramForm);
    this.connection.sendPacket((Packet)localObject);
    localObject = (IQ)paramForm.nextResult(SmackConfiguration.getPacketReplyTimeout());
    paramForm.cancel();
    if (localObject == null) {
      throw new XMPPException("No response from server.");
    }
    if (((IQ)localObject).getType() == IQ.Type.ERROR) {
      throw new XMPPException(((IQ)localObject).getError());
    }
  }
  
  private static class InvitationsMonitor
    implements ConnectionListener
  {
    private static final Map<XMPPConnection, WeakReference<InvitationsMonitor>> monitors = new WeakHashMap();
    private XMPPConnection connection;
    private PacketFilter invitationFilter;
    private PacketListener invitationPacketListener;
    private final List<InvitationListener> invitationsListeners = new ArrayList();
    
    private InvitationsMonitor(XMPPConnection paramXMPPConnection)
    {
      this.connection = paramXMPPConnection;
    }
    
    private void cancel()
    {
      this.connection.removePacketListener(this.invitationPacketListener);
      this.connection.removeConnectionListener(this);
    }
    
    private void fireInvitationListeners(String paramString1, String paramString2, String paramString3, String paramString4, Message paramMessage)
    {
      synchronized (this.invitationsListeners)
      {
        InvitationListener[] arrayOfInvitationListener = new InvitationListener[this.invitationsListeners.size()];
        this.invitationsListeners.toArray(arrayOfInvitationListener);
        int j = arrayOfInvitationListener.length;
        int i = 0;
        if (i < j)
        {
          arrayOfInvitationListener[i].invitationReceived(this.connection, paramString1, paramString2, paramString3, paramString4, paramMessage);
          i += 1;
        }
      }
    }
    
    public static InvitationsMonitor getInvitationsMonitor(XMPPConnection paramXMPPConnection)
    {
      synchronized (monitors)
      {
        if (!monitors.containsKey(paramXMPPConnection)) {
          monitors.put(paramXMPPConnection, new WeakReference(new InvitationsMonitor(paramXMPPConnection)));
        }
        paramXMPPConnection = (InvitationsMonitor)((WeakReference)monitors.get(paramXMPPConnection)).get();
        return paramXMPPConnection;
      }
    }
    
    private void init()
    {
      this.invitationFilter = new PacketExtensionFilter("x", "http://jabber.org/protocol/muc#user");
      this.invitationPacketListener = new PacketListener()
      {
        public void processPacket(Packet paramAnonymousPacket)
        {
          MUCUser localMUCUser = (MUCUser)paramAnonymousPacket.getExtension("x", "http://jabber.org/protocol/muc#user");
          if ((localMUCUser.getInvite() != null) && (((Message)paramAnonymousPacket).getType() != Message.Type.error)) {
            MultiUserChat.InvitationsMonitor.this.fireInvitationListeners(paramAnonymousPacket.getFrom(), localMUCUser.getInvite().getFrom(), localMUCUser.getInvite().getReason(), localMUCUser.getPassword(), (Message)paramAnonymousPacket);
          }
        }
      };
      this.connection.addPacketListener(this.invitationPacketListener, this.invitationFilter);
      this.connection.addConnectionListener(this);
    }
    
    public void addInvitationListener(InvitationListener paramInvitationListener)
    {
      synchronized (this.invitationsListeners)
      {
        if (this.invitationsListeners.size() == 0) {
          init();
        }
        if (!this.invitationsListeners.contains(paramInvitationListener)) {
          this.invitationsListeners.add(paramInvitationListener);
        }
        return;
      }
    }
    
    public void connectionClosed()
    {
      cancel();
    }
    
    public void connectionClosedOnError(Exception paramException) {}
    
    public void reconnectingIn(int paramInt) {}
    
    public void reconnectionFailed(Exception paramException) {}
    
    public void reconnectionSuccessful() {}
    
    public void removeInvitationListener(InvitationListener paramInvitationListener)
    {
      synchronized (this.invitationsListeners)
      {
        if (this.invitationsListeners.contains(paramInvitationListener)) {
          this.invitationsListeners.remove(paramInvitationListener);
        }
        if (this.invitationsListeners.size() == 0) {
          cancel();
        }
        return;
      }
    }
  }
}


/* Location:           C:\Users\Administrator\Documents\ds\反编译\AndroidKiller_v1.3.1\projects\Jungledsq_ppc\ProjectSrc\smali\
 * Qualified Name:     org.jivesoftware.smackx.muc.MultiUserChat
 * JD-Core Version:    0.7.0.1
 */