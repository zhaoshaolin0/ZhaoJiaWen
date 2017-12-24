package org.jivesoftware.smackx.workgroup.agent;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.jivesoftware.smack.PacketCollector;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.SmackConfiguration;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.filter.AndFilter;
import org.jivesoftware.smack.filter.FromContainsFilter;
import org.jivesoftware.smack.filter.OrFilter;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.filter.PacketIDFilter;
import org.jivesoftware.smack.filter.PacketTypeFilter;
import org.jivesoftware.smack.packet.DefaultPacketExtension;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.packet.IQ.Type;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.packet.Presence.Mode;
import org.jivesoftware.smack.packet.Presence.Type;
import org.jivesoftware.smack.util.StringUtils;
import org.jivesoftware.smackx.Form;
import org.jivesoftware.smackx.ReportedData;
import org.jivesoftware.smackx.packet.MUCUser;
import org.jivesoftware.smackx.packet.MUCUser.Invite;
import org.jivesoftware.smackx.workgroup.MetaData;
import org.jivesoftware.smackx.workgroup.WorkgroupInvitation;
import org.jivesoftware.smackx.workgroup.WorkgroupInvitationListener;
import org.jivesoftware.smackx.workgroup.ext.history.AgentChatHistory;
import org.jivesoftware.smackx.workgroup.ext.history.ChatMetadata;
import org.jivesoftware.smackx.workgroup.ext.macros.MacroGroup;
import org.jivesoftware.smackx.workgroup.ext.macros.Macros;
import org.jivesoftware.smackx.workgroup.ext.notes.ChatNotes;
import org.jivesoftware.smackx.workgroup.packet.DepartQueuePacket;
import org.jivesoftware.smackx.workgroup.packet.MonitorPacket;
import org.jivesoftware.smackx.workgroup.packet.OccupantsInfo;
import org.jivesoftware.smackx.workgroup.packet.OfferRequestProvider.OfferRequestPacket;
import org.jivesoftware.smackx.workgroup.packet.OfferRevokeProvider.OfferRevokePacket;
import org.jivesoftware.smackx.workgroup.packet.QueueDetails;
import org.jivesoftware.smackx.workgroup.packet.QueueOverview;
import org.jivesoftware.smackx.workgroup.packet.RoomInvitation;
import org.jivesoftware.smackx.workgroup.packet.RoomInvitation.Type;
import org.jivesoftware.smackx.workgroup.packet.RoomTransfer;
import org.jivesoftware.smackx.workgroup.packet.RoomTransfer.Type;
import org.jivesoftware.smackx.workgroup.packet.SessionID;
import org.jivesoftware.smackx.workgroup.packet.Transcript;
import org.jivesoftware.smackx.workgroup.packet.Transcripts;
import org.jivesoftware.smackx.workgroup.settings.GenericSettings;
import org.jivesoftware.smackx.workgroup.settings.SearchSettings;

public class AgentSession
{
  private Agent agent;
  private AgentRoster agentRoster = null;
  private XMPPConnection connection;
  private final List<WorkgroupInvitationListener> invitationListeners;
  private int maxChats;
  private final Map metaData;
  private final List<OfferListener> offerListeners;
  private boolean online = false;
  private PacketListener packetListener;
  private Presence.Mode presenceMode;
  private final List<QueueUsersListener> queueUsersListeners;
  private Map<String, WorkgroupQueue> queues;
  private TranscriptManager transcriptManager;
  private TranscriptSearchManager transcriptSearchManager;
  private String workgroupJID;
  
  public AgentSession(String paramString, XMPPConnection paramXMPPConnection)
  {
    if (!paramXMPPConnection.isAuthenticated()) {
      throw new IllegalStateException("Must login to server before creating workgroup.");
    }
    this.workgroupJID = paramString;
    this.connection = paramXMPPConnection;
    this.transcriptManager = new TranscriptManager(paramXMPPConnection);
    this.transcriptSearchManager = new TranscriptSearchManager(paramXMPPConnection);
    this.maxChats = -1;
    this.metaData = new HashMap();
    this.queues = new HashMap();
    this.offerListeners = new ArrayList();
    this.invitationListeners = new ArrayList();
    this.queueUsersListeners = new ArrayList();
    OrFilter localOrFilter = new OrFilter();
    localOrFilter.addFilter(new PacketTypeFilter(OfferRequestProvider.OfferRequestPacket.class));
    localOrFilter.addFilter(new PacketTypeFilter(OfferRevokeProvider.OfferRevokePacket.class));
    localOrFilter.addFilter(new PacketTypeFilter(Presence.class));
    localOrFilter.addFilter(new PacketTypeFilter(Message.class));
    this.packetListener = new PacketListener()
    {
      public void processPacket(Packet paramAnonymousPacket)
      {
        try
        {
          AgentSession.this.handlePacket(paramAnonymousPacket);
          return;
        }
        catch (Exception paramAnonymousPacket)
        {
          paramAnonymousPacket.printStackTrace();
        }
      }
    };
    paramXMPPConnection.addPacketListener(this.packetListener, localOrFilter);
    this.agent = new Agent(paramXMPPConnection, paramString);
  }
  
  private void fireInvitationEvent(String arg1, String paramString2, String paramString3, String paramString4, Map paramMap)
  {
    paramString2 = new WorkgroupInvitation(this.connection.getUser(), ???, this.workgroupJID, paramString2, paramString3, paramString4, paramMap);
    synchronized (this.invitationListeners)
    {
      paramString3 = this.invitationListeners.iterator();
      if (paramString3.hasNext()) {
        ((WorkgroupInvitationListener)paramString3.next()).invitationReceived(paramString2);
      }
    }
  }
  
  private void fireOfferRequestEvent(OfferRequestProvider.OfferRequestPacket arg1)
  {
    Offer localOffer = new Offer(this.connection, this, ???.getUserID(), ???.getUserJID(), getWorkgroupJID(), new Date(new Date().getTime() + ???.getTimeout() * 1000), ???.getSessionID(), ???.getMetaData(), ???.getContent());
    synchronized (this.offerListeners)
    {
      Iterator localIterator = this.offerListeners.iterator();
      if (localIterator.hasNext()) {
        ((OfferListener)localIterator.next()).offerReceived(localOffer);
      }
    }
  }
  
  private void fireOfferRevokeEvent(OfferRevokeProvider.OfferRevokePacket arg1)
  {
    RevokedOffer localRevokedOffer = new RevokedOffer(???.getUserJID(), ???.getUserID(), getWorkgroupJID(), ???.getSessionID(), ???.getReason(), new Date());
    synchronized (this.offerListeners)
    {
      Iterator localIterator = this.offerListeners.iterator();
      if (localIterator.hasNext()) {
        ((OfferListener)localIterator.next()).offerRevoked(localRevokedOffer);
      }
    }
  }
  
  private void fireQueueUsersEvent(WorkgroupQueue paramWorkgroupQueue, WorkgroupQueue.Status paramStatus, int paramInt, Date paramDate, Set paramSet)
  {
    synchronized (this.queueUsersListeners)
    {
      Iterator localIterator = this.queueUsersListeners.iterator();
      while (localIterator.hasNext())
      {
        QueueUsersListener localQueueUsersListener = (QueueUsersListener)localIterator.next();
        if (paramStatus != null) {
          localQueueUsersListener.statusUpdated(paramWorkgroupQueue, paramStatus);
        }
        if (paramInt != -1) {
          localQueueUsersListener.averageWaitTimeUpdated(paramWorkgroupQueue, paramInt);
        }
        if (paramDate != null) {
          localQueueUsersListener.oldestEntryUpdated(paramWorkgroupQueue, paramDate);
        }
        if (paramSet != null) {
          localQueueUsersListener.usersUpdated(paramWorkgroupQueue, paramSet);
        }
      }
    }
  }
  
  private void handlePacket(Packet paramPacket)
  {
    if ((paramPacket instanceof OfferRequestProvider.OfferRequestPacket))
    {
      localObject1 = new IQ()
      {
        public String getChildElementXML()
        {
          return null;
        }
      };
      ((IQ)localObject1).setPacketID(paramPacket.getPacketID());
      ((IQ)localObject1).setTo(paramPacket.getFrom());
      ((IQ)localObject1).setType(IQ.Type.RESULT);
      this.connection.sendPacket((Packet)localObject1);
      fireOfferRequestEvent((OfferRequestProvider.OfferRequestPacket)paramPacket);
    }
    do
    {
      for (;;)
      {
        return;
        Object localObject3;
        Object localObject2;
        if ((paramPacket instanceof Presence))
        {
          localObject3 = (Presence)paramPacket;
          String str = StringUtils.parseResource(((Presence)localObject3).getFrom());
          localObject2 = (WorkgroupQueue)this.queues.get(str);
          localObject1 = localObject2;
          if (localObject2 == null)
          {
            localObject1 = new WorkgroupQueue(str);
            this.queues.put(str, localObject1);
          }
          localObject2 = (QueueOverview)((Presence)localObject3).getExtension(QueueOverview.ELEMENT_NAME, QueueOverview.NAMESPACE);
          if (localObject2 != null)
          {
            if (((QueueOverview)localObject2).getStatus() == null) {
              ((WorkgroupQueue)localObject1).setStatus(WorkgroupQueue.Status.CLOSED);
            }
            for (;;)
            {
              ((WorkgroupQueue)localObject1).setAverageWaitTime(((QueueOverview)localObject2).getAverageWaitTime());
              ((WorkgroupQueue)localObject1).setOldestEntry(((QueueOverview)localObject2).getOldestEntry());
              fireQueueUsersEvent((WorkgroupQueue)localObject1, ((QueueOverview)localObject2).getStatus(), ((QueueOverview)localObject2).getAverageWaitTime(), ((QueueOverview)localObject2).getOldestEntry(), null);
              return;
              ((WorkgroupQueue)localObject1).setStatus(((QueueOverview)localObject2).getStatus());
            }
          }
          paramPacket = (QueueDetails)paramPacket.getExtension("notify-queue-details", "http://jabber.org/protocol/workgroup");
          if (paramPacket != null)
          {
            ((WorkgroupQueue)localObject1).setUsers(paramPacket.getUsers());
            fireQueueUsersEvent((WorkgroupQueue)localObject1, null, -1, null, paramPacket.getUsers());
            return;
          }
          paramPacket = (DefaultPacketExtension)((Presence)localObject3).getExtension("notify-agents", "http://jabber.org/protocol/workgroup");
          if (paramPacket != null)
          {
            int i = Integer.parseInt(paramPacket.getValue("current-chats"));
            int j = Integer.parseInt(paramPacket.getValue("max-chats"));
            ((WorkgroupQueue)localObject1).setCurrentChats(i);
            ((WorkgroupQueue)localObject1).setMaxChats(j);
          }
        }
        else
        {
          if (!(paramPacket instanceof Message)) {
            break;
          }
          localObject2 = (Message)paramPacket;
          paramPacket = (MUCUser)((Message)localObject2).getExtension("x", "http://jabber.org/protocol/muc#user");
          if (paramPacket != null) {}
          for (paramPacket = paramPacket.getInvite(); (paramPacket != null) && (this.workgroupJID.equals(paramPacket.getFrom())); paramPacket = null)
          {
            paramPacket = null;
            localObject1 = null;
            localObject3 = (SessionID)((Message)localObject2).getExtension("session", "http://jivesoftware.com/protocol/workgroup");
            if (localObject3 != null) {
              paramPacket = ((SessionID)localObject3).getSessionID();
            }
            localObject3 = (MetaData)((Message)localObject2).getExtension("metadata", "http://jivesoftware.com/protocol/workgroup");
            if (localObject3 != null) {
              localObject1 = ((MetaData)localObject3).getMetaData();
            }
            fireInvitationEvent(((Message)localObject2).getFrom(), paramPacket, ((Message)localObject2).getBody(), ((Message)localObject2).getFrom(), (Map)localObject1);
            return;
          }
        }
      }
    } while (!(paramPacket instanceof OfferRevokeProvider.OfferRevokePacket));
    Object localObject1 = new IQ()
    {
      public String getChildElementXML()
      {
        return null;
      }
    };
    ((IQ)localObject1).setPacketID(paramPacket.getPacketID());
    ((IQ)localObject1).setType(IQ.Type.RESULT);
    this.connection.sendPacket((Packet)localObject1);
    fireOfferRevokeEvent((OfferRevokeProvider.OfferRevokePacket)paramPacket);
  }
  
  public void addInvitationListener(WorkgroupInvitationListener paramWorkgroupInvitationListener)
  {
    synchronized (this.invitationListeners)
    {
      if (!this.invitationListeners.contains(paramWorkgroupInvitationListener)) {
        this.invitationListeners.add(paramWorkgroupInvitationListener);
      }
      return;
    }
  }
  
  public void addOfferListener(OfferListener paramOfferListener)
  {
    synchronized (this.offerListeners)
    {
      if (!this.offerListeners.contains(paramOfferListener)) {
        this.offerListeners.add(paramOfferListener);
      }
      return;
    }
  }
  
  public void addQueueUsersListener(QueueUsersListener paramQueueUsersListener)
  {
    synchronized (this.queueUsersListeners)
    {
      if (!this.queueUsersListeners.contains(paramQueueUsersListener)) {
        this.queueUsersListeners.add(paramQueueUsersListener);
      }
      return;
    }
  }
  
  public void close()
  {
    this.connection.removePacketListener(this.packetListener);
  }
  
  public void dequeueUser(String paramString)
    throws XMPPException
  {
    paramString = new DepartQueuePacket(this.workgroupJID);
    this.connection.sendPacket(paramString);
  }
  
  public Agent getAgent()
  {
    return this.agent;
  }
  
  public AgentChatHistory getAgentHistory(String paramString, int paramInt, Date paramDate)
    throws XMPPException
  {
    if (paramDate != null) {}
    for (paramString = new AgentChatHistory(paramString, paramInt, paramDate);; paramString = new AgentChatHistory(paramString, paramInt))
    {
      paramString.setType(IQ.Type.GET);
      paramString.setTo(this.workgroupJID);
      paramDate = this.connection.createPacketCollector(new PacketIDFilter(paramString.getPacketID()));
      this.connection.sendPacket(paramString);
      paramString = (AgentChatHistory)paramDate.nextResult(SmackConfiguration.getPacketReplyTimeout());
      paramDate.cancel();
      if (paramString != null) {
        break;
      }
      throw new XMPPException("No response from server.");
    }
    if (paramString.getError() != null) {
      throw new XMPPException(paramString.getError());
    }
    return paramString;
  }
  
  public AgentRoster getAgentRoster()
  {
    if (this.agentRoster == null) {
      this.agentRoster = new AgentRoster(this.connection, this.workgroupJID);
    }
    int i = 0;
    for (;;)
    {
      if ((!this.agentRoster.rosterInitialized) && (i <= 2000)) {}
      try
      {
        Thread.sleep(500L);
        label51:
        i += 500;
        continue;
        return this.agentRoster;
      }
      catch (Exception localException)
      {
        break label51;
      }
    }
  }
  
  public Map getChatMetadata(String paramString)
    throws XMPPException
  {
    ChatMetadata localChatMetadata = new ChatMetadata();
    localChatMetadata.setType(IQ.Type.GET);
    localChatMetadata.setTo(this.workgroupJID);
    localChatMetadata.setSessionID(paramString);
    paramString = this.connection.createPacketCollector(new PacketIDFilter(localChatMetadata.getPacketID()));
    this.connection.sendPacket(localChatMetadata);
    localChatMetadata = (ChatMetadata)paramString.nextResult(SmackConfiguration.getPacketReplyTimeout());
    paramString.cancel();
    if (localChatMetadata == null) {
      throw new XMPPException("No response from server.");
    }
    if (localChatMetadata.getError() != null) {
      throw new XMPPException(localChatMetadata.getError());
    }
    return localChatMetadata.getMetadata();
  }
  
  public GenericSettings getGenericSettings(XMPPConnection paramXMPPConnection, String paramString)
    throws XMPPException
  {
    paramString = new GenericSettings();
    paramString.setType(IQ.Type.GET);
    paramString.setTo(this.workgroupJID);
    paramXMPPConnection = this.connection.createPacketCollector(new PacketIDFilter(paramString.getPacketID()));
    this.connection.sendPacket(paramString);
    paramString = (GenericSettings)paramXMPPConnection.nextResult(SmackConfiguration.getPacketReplyTimeout());
    paramXMPPConnection.cancel();
    if (paramString == null) {
      throw new XMPPException("No response from server on status set.");
    }
    if (paramString.getError() != null) {
      throw new XMPPException(paramString.getError());
    }
    return paramString;
  }
  
  public MacroGroup getMacros(boolean paramBoolean)
    throws XMPPException
  {
    Macros localMacros = new Macros();
    localMacros.setType(IQ.Type.GET);
    localMacros.setTo(this.workgroupJID);
    if (!paramBoolean) {}
    for (paramBoolean = true;; paramBoolean = false)
    {
      localMacros.setPersonal(paramBoolean);
      PacketCollector localPacketCollector = this.connection.createPacketCollector(new PacketIDFilter(localMacros.getPacketID()));
      this.connection.sendPacket(localMacros);
      localMacros = (Macros)localPacketCollector.nextResult(SmackConfiguration.getPacketReplyTimeout());
      localPacketCollector.cancel();
      if (localMacros != null) {
        break;
      }
      throw new XMPPException("No response from server.");
    }
    if (localMacros.getError() != null) {
      throw new XMPPException(localMacros.getError());
    }
    return localMacros.getRootGroup();
  }
  
  public int getMaxChats()
  {
    return this.maxChats;
  }
  
  public String getMetaData(String paramString)
  {
    return (String)this.metaData.get(paramString);
  }
  
  public ChatNotes getNote(String paramString)
    throws XMPPException
  {
    ChatNotes localChatNotes = new ChatNotes();
    localChatNotes.setType(IQ.Type.GET);
    localChatNotes.setTo(this.workgroupJID);
    localChatNotes.setSessionID(paramString);
    paramString = this.connection.createPacketCollector(new PacketIDFilter(localChatNotes.getPacketID()));
    this.connection.sendPacket(localChatNotes);
    localChatNotes = (ChatNotes)paramString.nextResult(SmackConfiguration.getPacketReplyTimeout());
    paramString.cancel();
    if (localChatNotes == null) {
      throw new XMPPException("No response from server.");
    }
    if (localChatNotes.getError() != null) {
      throw new XMPPException(localChatNotes.getError());
    }
    return localChatNotes;
  }
  
  public OccupantsInfo getOccupantsInfo(String paramString)
    throws XMPPException
  {
    OccupantsInfo localOccupantsInfo = new OccupantsInfo(paramString);
    localOccupantsInfo.setType(IQ.Type.GET);
    localOccupantsInfo.setTo(this.workgroupJID);
    paramString = this.connection.createPacketCollector(new PacketIDFilter(localOccupantsInfo.getPacketID()));
    this.connection.sendPacket(localOccupantsInfo);
    localOccupantsInfo = (OccupantsInfo)paramString.nextResult(SmackConfiguration.getPacketReplyTimeout());
    paramString.cancel();
    if (localOccupantsInfo == null) {
      throw new XMPPException("No response from server.");
    }
    if (localOccupantsInfo.getError() != null) {
      throw new XMPPException(localOccupantsInfo.getError());
    }
    return localOccupantsInfo;
  }
  
  public Presence.Mode getPresenceMode()
  {
    return this.presenceMode;
  }
  
  public WorkgroupQueue getQueue(String paramString)
  {
    return (WorkgroupQueue)this.queues.get(paramString);
  }
  
  public Iterator<WorkgroupQueue> getQueues()
  {
    return Collections.unmodifiableMap(new HashMap(this.queues)).values().iterator();
  }
  
  public SearchSettings getSearchSettings()
    throws XMPPException
  {
    SearchSettings localSearchSettings = new SearchSettings();
    localSearchSettings.setType(IQ.Type.GET);
    localSearchSettings.setTo(this.workgroupJID);
    PacketCollector localPacketCollector = this.connection.createPacketCollector(new PacketIDFilter(localSearchSettings.getPacketID()));
    this.connection.sendPacket(localSearchSettings);
    localSearchSettings = (SearchSettings)localPacketCollector.nextResult(SmackConfiguration.getPacketReplyTimeout());
    localPacketCollector.cancel();
    if (localSearchSettings == null) {
      throw new XMPPException("No response from server.");
    }
    if (localSearchSettings.getError() != null) {
      throw new XMPPException(localSearchSettings.getError());
    }
    return localSearchSettings;
  }
  
  public Transcript getTranscript(String paramString)
    throws XMPPException
  {
    return this.transcriptManager.getTranscript(this.workgroupJID, paramString);
  }
  
  public Form getTranscriptSearchForm()
    throws XMPPException
  {
    return this.transcriptSearchManager.getSearchForm(StringUtils.parseServer(this.workgroupJID));
  }
  
  public Transcripts getTranscripts(String paramString)
    throws XMPPException
  {
    return this.transcriptManager.getTranscripts(this.workgroupJID, paramString);
  }
  
  public String getWorkgroupJID()
  {
    return this.workgroupJID;
  }
  
  public boolean hasMonitorPrivileges(XMPPConnection paramXMPPConnection)
    throws XMPPException
  {
    MonitorPacket localMonitorPacket = new MonitorPacket();
    localMonitorPacket.setType(IQ.Type.GET);
    localMonitorPacket.setTo(this.workgroupJID);
    paramXMPPConnection = this.connection.createPacketCollector(new PacketIDFilter(localMonitorPacket.getPacketID()));
    this.connection.sendPacket(localMonitorPacket);
    localMonitorPacket = (MonitorPacket)paramXMPPConnection.nextResult(SmackConfiguration.getPacketReplyTimeout());
    paramXMPPConnection.cancel();
    if (localMonitorPacket == null) {
      throw new XMPPException("No response from server on status set.");
    }
    if (localMonitorPacket.getError() != null) {
      throw new XMPPException(localMonitorPacket.getError());
    }
    return localMonitorPacket.isMonitor();
  }
  
  public boolean isOnline()
  {
    return this.online;
  }
  
  public void makeRoomOwner(XMPPConnection paramXMPPConnection, String paramString)
    throws XMPPException
  {
    paramXMPPConnection = new MonitorPacket();
    paramXMPPConnection.setType(IQ.Type.SET);
    paramXMPPConnection.setTo(this.workgroupJID);
    paramXMPPConnection.setSessionID(paramString);
    paramString = this.connection.createPacketCollector(new PacketIDFilter(paramXMPPConnection.getPacketID()));
    this.connection.sendPacket(paramXMPPConnection);
    paramXMPPConnection = paramString.nextResult(SmackConfiguration.getPacketReplyTimeout());
    paramString.cancel();
    if (paramXMPPConnection == null) {
      throw new XMPPException("No response from server on status set.");
    }
    if (paramXMPPConnection.getError() != null) {
      throw new XMPPException(paramXMPPConnection.getError());
    }
  }
  
  public void removeInvitationListener(WorkgroupInvitationListener paramWorkgroupInvitationListener)
  {
    synchronized (this.invitationListeners)
    {
      this.invitationListeners.remove(paramWorkgroupInvitationListener);
      return;
    }
  }
  
  public void removeMetaData(String paramString)
    throws XMPPException
  {
    synchronized (this.metaData)
    {
      if ((String)this.metaData.remove(paramString) != null) {
        setStatus(this.presenceMode, this.maxChats);
      }
      return;
    }
  }
  
  public void removeOfferListener(OfferListener paramOfferListener)
  {
    synchronized (this.offerListeners)
    {
      this.offerListeners.remove(paramOfferListener);
      return;
    }
  }
  
  public void removeQueueUsersListener(QueueUsersListener paramQueueUsersListener)
  {
    synchronized (this.queueUsersListeners)
    {
      this.queueUsersListeners.remove(paramQueueUsersListener);
      return;
    }
  }
  
  public void saveMacros(MacroGroup paramMacroGroup)
    throws XMPPException
  {
    Object localObject = new Macros();
    ((Macros)localObject).setType(IQ.Type.SET);
    ((Macros)localObject).setTo(this.workgroupJID);
    ((Macros)localObject).setPersonal(true);
    ((Macros)localObject).setPersonalMacroGroup(paramMacroGroup);
    paramMacroGroup = this.connection.createPacketCollector(new PacketIDFilter(((Macros)localObject).getPacketID()));
    this.connection.sendPacket((Packet)localObject);
    localObject = (IQ)paramMacroGroup.nextResult(SmackConfiguration.getPacketReplyTimeout());
    paramMacroGroup.cancel();
    if (localObject == null) {
      throw new XMPPException("No response from server on status set.");
    }
    if (((IQ)localObject).getError() != null) {
      throw new XMPPException(((IQ)localObject).getError());
    }
  }
  
  public ReportedData searchTranscripts(Form paramForm)
    throws XMPPException
  {
    return this.transcriptSearchManager.submitSearch(StringUtils.parseServer(this.workgroupJID), paramForm);
  }
  
  public void sendRoomInvitation(RoomInvitation.Type paramType, String paramString1, String paramString2, String paramString3)
    throws XMPPException
  {
    paramString1 = new IQ()
    {
      public String getChildElementXML()
      {
        return this.val$invitation.toXML();
      }
    };
    paramString1.setType(IQ.Type.SET);
    paramString1.setTo(this.workgroupJID);
    paramString1.setFrom(this.connection.getUser());
    paramType = this.connection.createPacketCollector(new PacketIDFilter(paramString1.getPacketID()));
    this.connection.sendPacket(paramString1);
    paramString1 = (IQ)paramType.nextResult(SmackConfiguration.getPacketReplyTimeout());
    paramType.cancel();
    if (paramString1 == null) {
      throw new XMPPException("No response from server.");
    }
    if (paramString1.getError() != null) {
      throw new XMPPException(paramString1.getError());
    }
  }
  
  public void sendRoomTransfer(RoomTransfer.Type paramType, String paramString1, String paramString2, String paramString3)
    throws XMPPException
  {
    paramString1 = new IQ()
    {
      public String getChildElementXML()
      {
        return this.val$transfer.toXML();
      }
    };
    paramString1.setType(IQ.Type.SET);
    paramString1.setTo(this.workgroupJID);
    paramString1.setFrom(this.connection.getUser());
    paramType = this.connection.createPacketCollector(new PacketIDFilter(paramString1.getPacketID()));
    this.connection.sendPacket(paramString1);
    paramString1 = (IQ)paramType.nextResult(SmackConfiguration.getPacketReplyTimeout());
    paramType.cancel();
    if (paramString1 == null) {
      throw new XMPPException("No response from server.");
    }
    if (paramString1.getError() != null) {
      throw new XMPPException(paramString1.getError());
    }
  }
  
  public void setMetaData(String paramString1, String paramString2)
    throws XMPPException
  {
    synchronized (this.metaData)
    {
      String str = (String)this.metaData.get(paramString1);
      if ((str == null) || (!str.equals(paramString2)))
      {
        this.metaData.put(paramString1, paramString2);
        setStatus(this.presenceMode, this.maxChats);
      }
      return;
    }
  }
  
  public void setNote(String paramString1, String paramString2)
    throws XMPPException
  {
    String str = StringUtils.escapeForXML(ChatNotes.replace(paramString2, "\n", "\\n"));
    paramString2 = new ChatNotes();
    paramString2.setType(IQ.Type.SET);
    paramString2.setTo(this.workgroupJID);
    paramString2.setSessionID(paramString1);
    paramString2.setNotes(str);
    paramString1 = this.connection.createPacketCollector(new PacketIDFilter(paramString2.getPacketID()));
    this.connection.sendPacket(paramString2);
    paramString2 = (IQ)paramString1.nextResult(SmackConfiguration.getPacketReplyTimeout());
    paramString1.cancel();
    if (paramString2 == null) {
      throw new XMPPException("No response from server on status set.");
    }
    if (paramString2.getError() != null) {
      throw new XMPPException(paramString2.getError());
    }
  }
  
  public void setOnline(boolean paramBoolean)
    throws XMPPException
  {
    if (this.online == paramBoolean) {
      return;
    }
    if (paramBoolean)
    {
      Presence localPresence = new Presence(Presence.Type.available);
      localPresence.setTo(this.workgroupJID);
      localPresence.addExtension(new DefaultPacketExtension("agent-status", "http://jabber.org/protocol/workgroup"));
      localObject = this.connection.createPacketCollector(new AndFilter(new PacketFilter[] { new PacketTypeFilter(Presence.class), new FromContainsFilter(this.workgroupJID) }));
      this.connection.sendPacket(localPresence);
      localPresence = (Presence)((PacketCollector)localObject).nextResult(5000L);
      ((PacketCollector)localObject).cancel();
      if (!localPresence.isAvailable()) {
        throw new XMPPException("No response from server on status set.");
      }
      if (localPresence.getError() != null) {
        throw new XMPPException(localPresence.getError());
      }
      this.online = paramBoolean;
      return;
    }
    this.online = paramBoolean;
    Object localObject = new Presence(Presence.Type.unavailable);
    ((Presence)localObject).setTo(this.workgroupJID);
    ((Presence)localObject).addExtension(new DefaultPacketExtension("agent-status", "http://jabber.org/protocol/workgroup"));
    this.connection.sendPacket((Packet)localObject);
  }
  
  public void setStatus(Presence.Mode paramMode, int paramInt)
    throws XMPPException
  {
    setStatus(paramMode, paramInt, null);
  }
  
  public void setStatus(Presence.Mode paramMode, int paramInt, String paramString)
    throws XMPPException
  {
    if (!this.online) {
      throw new IllegalStateException("Cannot set status when the agent is not online.");
    }
    Presence.Mode localMode = paramMode;
    if (paramMode == null) {
      localMode = Presence.Mode.available;
    }
    this.presenceMode = localMode;
    this.maxChats = paramInt;
    paramMode = new Presence(Presence.Type.available);
    paramMode.setMode(localMode);
    paramMode.setTo(getWorkgroupJID());
    if (paramString != null) {
      paramMode.setStatus(paramString);
    }
    paramString = new DefaultPacketExtension("agent-status", "http://jabber.org/protocol/workgroup");
    paramString.setValue("max-chats", "" + paramInt);
    paramMode.addExtension(paramString);
    paramMode.addExtension(new MetaData(this.metaData));
    paramString = this.connection.createPacketCollector(new AndFilter(new PacketFilter[] { new PacketTypeFilter(Presence.class), new FromContainsFilter(this.workgroupJID) }));
    this.connection.sendPacket(paramMode);
    paramMode = (Presence)paramString.nextResult(5000L);
    paramString.cancel();
    if (!paramMode.isAvailable()) {
      throw new XMPPException("No response from server on status set.");
    }
    if (paramMode.getError() != null) {
      throw new XMPPException(paramMode.getError());
    }
  }
  
  public void setStatus(Presence.Mode paramMode, String paramString)
    throws XMPPException
  {
    if (!this.online) {
      throw new IllegalStateException("Cannot set status when the agent is not online.");
    }
    Presence.Mode localMode = paramMode;
    if (paramMode == null) {
      localMode = Presence.Mode.available;
    }
    this.presenceMode = localMode;
    paramMode = new Presence(Presence.Type.available);
    paramMode.setMode(localMode);
    paramMode.setTo(getWorkgroupJID());
    if (paramString != null) {
      paramMode.setStatus(paramString);
    }
    paramMode.addExtension(new MetaData(this.metaData));
    paramString = this.connection.createPacketCollector(new AndFilter(new PacketFilter[] { new PacketTypeFilter(Presence.class), new FromContainsFilter(this.workgroupJID) }));
    this.connection.sendPacket(paramMode);
    paramMode = (Presence)paramString.nextResult(5000L);
    paramString.cancel();
    if (!paramMode.isAvailable()) {
      throw new XMPPException("No response from server on status set.");
    }
    if (paramMode.getError() != null) {
      throw new XMPPException(paramMode.getError());
    }
  }
}


/* Location:           C:\Users\Administrator\Documents\ds\反编译\AndroidKiller_v1.3.1\projects\Jungledsq_ppc\ProjectSrc\smali\
 * Qualified Name:     org.jivesoftware.smackx.workgroup.agent.AgentSession
 * JD-Core Version:    0.7.0.1
 */