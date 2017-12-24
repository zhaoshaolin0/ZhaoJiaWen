package org.jivesoftware.smackx.workgroup.user;

import java.util.ArrayList;
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
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.filter.PacketIDFilter;
import org.jivesoftware.smack.filter.PacketTypeFilter;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.packet.IQ.Type;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.packet.PacketExtension;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.packet.Presence.Type;
import org.jivesoftware.smack.util.StringUtils;
import org.jivesoftware.smackx.Form;
import org.jivesoftware.smackx.FormField;
import org.jivesoftware.smackx.ServiceDiscoveryManager;
import org.jivesoftware.smackx.muc.InvitationListener;
import org.jivesoftware.smackx.muc.MultiUserChat;
import org.jivesoftware.smackx.packet.DataForm;
import org.jivesoftware.smackx.packet.DiscoverInfo;
import org.jivesoftware.smackx.packet.MUCUser;
import org.jivesoftware.smackx.packet.MUCUser.Invite;
import org.jivesoftware.smackx.workgroup.MetaData;
import org.jivesoftware.smackx.workgroup.WorkgroupInvitation;
import org.jivesoftware.smackx.workgroup.WorkgroupInvitationListener;
import org.jivesoftware.smackx.workgroup.ext.forms.WorkgroupForm;
import org.jivesoftware.smackx.workgroup.packet.DepartQueuePacket;
import org.jivesoftware.smackx.workgroup.packet.QueueUpdate;
import org.jivesoftware.smackx.workgroup.packet.SessionID;
import org.jivesoftware.smackx.workgroup.packet.UserID;
import org.jivesoftware.smackx.workgroup.settings.ChatSetting;
import org.jivesoftware.smackx.workgroup.settings.ChatSettings;
import org.jivesoftware.smackx.workgroup.settings.OfflineSettings;
import org.jivesoftware.smackx.workgroup.settings.SoundSettings;
import org.jivesoftware.smackx.workgroup.settings.WorkgroupProperties;

public class Workgroup
{
  private XMPPConnection connection;
  private boolean inQueue;
  private List invitationListeners;
  private List queueListeners;
  private int queuePosition = -1;
  private int queueRemainingTime = -1;
  private List siteInviteListeners;
  private String workgroupJID;
  
  public Workgroup(String paramString, XMPPConnection paramXMPPConnection)
  {
    if (!paramXMPPConnection.isAuthenticated()) {
      throw new IllegalStateException("Must login to server before creating workgroup.");
    }
    this.workgroupJID = paramString;
    this.connection = paramXMPPConnection;
    this.inQueue = false;
    this.invitationListeners = new ArrayList();
    this.queueListeners = new ArrayList();
    this.siteInviteListeners = new ArrayList();
    addQueueListener(new QueueListener()
    {
      public void departedQueue()
      {
        Workgroup.access$002(Workgroup.this, false);
        Workgroup.access$102(Workgroup.this, -1);
        Workgroup.access$202(Workgroup.this, -1);
      }
      
      public void joinedQueue()
      {
        Workgroup.access$002(Workgroup.this, true);
      }
      
      public void queuePositionUpdated(int paramAnonymousInt)
      {
        Workgroup.access$102(Workgroup.this, paramAnonymousInt);
      }
      
      public void queueWaitTimeUpdated(int paramAnonymousInt)
      {
        Workgroup.access$202(Workgroup.this, paramAnonymousInt);
      }
    });
    MultiUserChat.addInvitationListener(paramXMPPConnection, new InvitationListener()
    {
      public void invitationReceived(XMPPConnection paramAnonymousXMPPConnection, String paramAnonymousString1, String paramAnonymousString2, String paramAnonymousString3, String paramAnonymousString4, Message paramAnonymousMessage)
      {
        Workgroup.access$002(Workgroup.this, false);
        Workgroup.access$102(Workgroup.this, -1);
        Workgroup.access$202(Workgroup.this, -1);
      }
    });
    paramString = new PacketTypeFilter(Message.class);
    paramXMPPConnection.addPacketListener(new PacketListener()
    {
      public void processPacket(Packet paramAnonymousPacket)
      {
        Workgroup.this.handlePacket(paramAnonymousPacket);
      }
    }, paramString);
  }
  
  private void fireInvitationEvent(WorkgroupInvitation paramWorkgroupInvitation)
  {
    synchronized (this.invitationListeners)
    {
      Iterator localIterator = this.invitationListeners.iterator();
      if (localIterator.hasNext()) {
        ((WorkgroupInvitationListener)localIterator.next()).invitationReceived(paramWorkgroupInvitation);
      }
    }
  }
  
  private void fireQueueDepartedEvent()
  {
    synchronized (this.queueListeners)
    {
      Iterator localIterator = this.queueListeners.iterator();
      if (localIterator.hasNext()) {
        ((QueueListener)localIterator.next()).departedQueue();
      }
    }
  }
  
  private void fireQueueJoinedEvent()
  {
    synchronized (this.queueListeners)
    {
      Iterator localIterator = this.queueListeners.iterator();
      if (localIterator.hasNext()) {
        ((QueueListener)localIterator.next()).joinedQueue();
      }
    }
  }
  
  private void fireQueuePositionEvent(int paramInt)
  {
    synchronized (this.queueListeners)
    {
      Iterator localIterator = this.queueListeners.iterator();
      if (localIterator.hasNext()) {
        ((QueueListener)localIterator.next()).queuePositionUpdated(paramInt);
      }
    }
  }
  
  private void fireQueueTimeEvent(int paramInt)
  {
    synchronized (this.queueListeners)
    {
      Iterator localIterator = this.queueListeners.iterator();
      if (localIterator.hasNext()) {
        ((QueueListener)localIterator.next()).queueWaitTimeUpdated(paramInt);
      }
    }
  }
  
  private ChatSettings getChatSettings(String paramString, int paramInt)
    throws XMPPException
  {
    ChatSettings localChatSettings = new ChatSettings();
    if (paramString != null) {
      localChatSettings.setKey(paramString);
    }
    if (paramInt != -1) {
      localChatSettings.setType(paramInt);
    }
    localChatSettings.setType(IQ.Type.GET);
    localChatSettings.setTo(this.workgroupJID);
    paramString = this.connection.createPacketCollector(new PacketIDFilter(localChatSettings.getPacketID()));
    this.connection.sendPacket(localChatSettings);
    localChatSettings = (ChatSettings)paramString.nextResult(SmackConfiguration.getPacketReplyTimeout());
    paramString.cancel();
    if (localChatSettings == null) {
      throw new XMPPException("No response from server.");
    }
    if (localChatSettings.getError() != null) {
      throw new XMPPException(localChatSettings.getError());
    }
    return localChatSettings;
  }
  
  private void handlePacket(Packet paramPacket)
  {
    Message localMessage;
    Object localObject;
    if ((paramPacket instanceof Message))
    {
      localMessage = (Message)paramPacket;
      paramPacket = localMessage.getExtension("depart-queue", "http://jabber.org/protocol/workgroup");
      localObject = localMessage.getExtension("queue-status", "http://jabber.org/protocol/workgroup");
      if (paramPacket == null) {
        break label39;
      }
      fireQueueDepartedEvent();
    }
    for (;;)
    {
      return;
      label39:
      if (localObject != null)
      {
        paramPacket = (QueueUpdate)localObject;
        if (paramPacket.getPosition() != -1) {
          fireQueuePositionEvent(paramPacket.getPosition());
        }
        if (paramPacket.getRemaingTime() != -1) {
          fireQueueTimeEvent(paramPacket.getRemaingTime());
        }
      }
      else
      {
        paramPacket = (MUCUser)localMessage.getExtension("x", "http://jabber.org/protocol/muc#user");
        if (paramPacket != null) {}
        for (paramPacket = paramPacket.getInvite(); (paramPacket != null) && (this.workgroupJID.equals(paramPacket.getFrom())); paramPacket = null)
        {
          paramPacket = null;
          localObject = null;
          PacketExtension localPacketExtension = localMessage.getExtension("session", "http://jivesoftware.com/protocol/workgroup");
          if (localPacketExtension != null) {
            paramPacket = ((SessionID)localPacketExtension).getSessionID();
          }
          localPacketExtension = localMessage.getExtension("metadata", "http://jivesoftware.com/protocol/workgroup");
          if (localPacketExtension != null) {
            localObject = ((MetaData)localPacketExtension).getMetaData();
          }
          fireInvitationEvent(new WorkgroupInvitation(this.connection.getUser(), localMessage.getFrom(), this.workgroupJID, paramPacket, localMessage.getBody(), localMessage.getFrom(), (Map)localObject));
          return;
        }
      }
    }
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
  
  public void addQueueListener(QueueListener paramQueueListener)
  {
    synchronized (this.queueListeners)
    {
      if (!this.queueListeners.contains(paramQueueListener)) {
        this.queueListeners.add(paramQueueListener);
      }
      return;
    }
  }
  
  public void departQueue()
    throws XMPPException
  {
    if (!this.inQueue) {
      return;
    }
    Object localObject = new DepartQueuePacket(this.workgroupJID);
    PacketCollector localPacketCollector = this.connection.createPacketCollector(new PacketIDFilter(((DepartQueuePacket)localObject).getPacketID()));
    this.connection.sendPacket((Packet)localObject);
    localObject = (IQ)localPacketCollector.nextResult(5000L);
    localPacketCollector.cancel();
    if (localObject == null) {
      throw new XMPPException("No response from the server.");
    }
    if (((IQ)localObject).getError() != null) {
      throw new XMPPException(((IQ)localObject).getError());
    }
    fireQueueDepartedEvent();
  }
  
  public ChatSetting getChatSetting(String paramString)
    throws XMPPException
  {
    return getChatSettings(paramString, -1).getFirstEntry();
  }
  
  public ChatSettings getChatSettings()
    throws XMPPException
  {
    return getChatSettings(null, -1);
  }
  
  public ChatSettings getChatSettings(int paramInt)
    throws XMPPException
  {
    return getChatSettings(null, paramInt);
  }
  
  public OfflineSettings getOfflineSettings()
    throws XMPPException
  {
    OfflineSettings localOfflineSettings = new OfflineSettings();
    localOfflineSettings.setType(IQ.Type.GET);
    localOfflineSettings.setTo(this.workgroupJID);
    PacketCollector localPacketCollector = this.connection.createPacketCollector(new PacketIDFilter(localOfflineSettings.getPacketID()));
    this.connection.sendPacket(localOfflineSettings);
    localOfflineSettings = (OfflineSettings)localPacketCollector.nextResult(SmackConfiguration.getPacketReplyTimeout());
    localPacketCollector.cancel();
    if (localOfflineSettings == null) {
      throw new XMPPException("No response from server.");
    }
    if (localOfflineSettings.getError() != null) {
      throw new XMPPException(localOfflineSettings.getError());
    }
    return localOfflineSettings;
  }
  
  public int getQueuePosition()
  {
    return this.queuePosition;
  }
  
  public int getQueueRemainingTime()
  {
    return this.queueRemainingTime;
  }
  
  public SoundSettings getSoundSettings()
    throws XMPPException
  {
    SoundSettings localSoundSettings = new SoundSettings();
    localSoundSettings.setType(IQ.Type.GET);
    localSoundSettings.setTo(this.workgroupJID);
    PacketCollector localPacketCollector = this.connection.createPacketCollector(new PacketIDFilter(localSoundSettings.getPacketID()));
    this.connection.sendPacket(localSoundSettings);
    localSoundSettings = (SoundSettings)localPacketCollector.nextResult(SmackConfiguration.getPacketReplyTimeout());
    localPacketCollector.cancel();
    if (localSoundSettings == null) {
      throw new XMPPException("No response from server.");
    }
    if (localSoundSettings.getError() != null) {
      throw new XMPPException(localSoundSettings.getError());
    }
    return localSoundSettings;
  }
  
  public Form getWorkgroupForm()
    throws XMPPException
  {
    WorkgroupForm localWorkgroupForm = new WorkgroupForm();
    localWorkgroupForm.setType(IQ.Type.GET);
    localWorkgroupForm.setTo(this.workgroupJID);
    PacketCollector localPacketCollector = this.connection.createPacketCollector(new PacketIDFilter(localWorkgroupForm.getPacketID()));
    this.connection.sendPacket(localWorkgroupForm);
    localWorkgroupForm = (WorkgroupForm)localPacketCollector.nextResult(SmackConfiguration.getPacketReplyTimeout());
    localPacketCollector.cancel();
    if (localWorkgroupForm == null) {
      throw new XMPPException("No response from server on status set.");
    }
    if (localWorkgroupForm.getError() != null) {
      throw new XMPPException(localWorkgroupForm.getError());
    }
    return Form.getFormFrom(localWorkgroupForm);
  }
  
  public String getWorkgroupJID()
  {
    return this.workgroupJID;
  }
  
  public WorkgroupProperties getWorkgroupProperties()
    throws XMPPException
  {
    WorkgroupProperties localWorkgroupProperties = new WorkgroupProperties();
    localWorkgroupProperties.setType(IQ.Type.GET);
    localWorkgroupProperties.setTo(this.workgroupJID);
    PacketCollector localPacketCollector = this.connection.createPacketCollector(new PacketIDFilter(localWorkgroupProperties.getPacketID()));
    this.connection.sendPacket(localWorkgroupProperties);
    localWorkgroupProperties = (WorkgroupProperties)localPacketCollector.nextResult(SmackConfiguration.getPacketReplyTimeout());
    localPacketCollector.cancel();
    if (localWorkgroupProperties == null) {
      throw new XMPPException("No response from server.");
    }
    if (localWorkgroupProperties.getError() != null) {
      throw new XMPPException(localWorkgroupProperties.getError());
    }
    return localWorkgroupProperties;
  }
  
  public WorkgroupProperties getWorkgroupProperties(String paramString)
    throws XMPPException
  {
    WorkgroupProperties localWorkgroupProperties = new WorkgroupProperties();
    localWorkgroupProperties.setJid(paramString);
    localWorkgroupProperties.setType(IQ.Type.GET);
    localWorkgroupProperties.setTo(this.workgroupJID);
    paramString = this.connection.createPacketCollector(new PacketIDFilter(localWorkgroupProperties.getPacketID()));
    this.connection.sendPacket(localWorkgroupProperties);
    localWorkgroupProperties = (WorkgroupProperties)paramString.nextResult(SmackConfiguration.getPacketReplyTimeout());
    paramString.cancel();
    if (localWorkgroupProperties == null) {
      throw new XMPPException("No response from server.");
    }
    if (localWorkgroupProperties.getError() != null) {
      throw new XMPPException(localWorkgroupProperties.getError());
    }
    return localWorkgroupProperties;
  }
  
  public boolean isAvailable()
  {
    Presence localPresence = new Presence(Presence.Type.available);
    localPresence.setTo(this.workgroupJID);
    Object localObject = new PacketTypeFilter(Presence.class);
    FromContainsFilter localFromContainsFilter = new FromContainsFilter(this.workgroupJID);
    localObject = this.connection.createPacketCollector(new AndFilter(new PacketFilter[] { localFromContainsFilter, localObject }));
    this.connection.sendPacket(localPresence);
    localPresence = (Presence)((PacketCollector)localObject).nextResult(SmackConfiguration.getPacketReplyTimeout());
    ((PacketCollector)localObject).cancel();
    if (localPresence == null) {
      return false;
    }
    if (localPresence.getError() != null) {
      return false;
    }
    return Presence.Type.available == localPresence.getType();
  }
  
  public boolean isEmailAvailable()
  {
    ServiceDiscoveryManager localServiceDiscoveryManager = ServiceDiscoveryManager.getInstanceFor(this.connection);
    try
    {
      boolean bool = localServiceDiscoveryManager.discoverInfo(StringUtils.parseServer(this.workgroupJID)).containsFeature("jive:email:provider");
      return bool;
    }
    catch (XMPPException localXMPPException) {}
    return false;
  }
  
  public boolean isInQueue()
  {
    return this.inQueue;
  }
  
  public void joinQueue()
    throws XMPPException
  {
    joinQueue(null);
  }
  
  public void joinQueue(Map paramMap, String paramString)
    throws XMPPException
  {
    if (this.inQueue) {
      throw new IllegalStateException("Already in queue " + this.workgroupJID);
    }
    Form localForm = new Form("submit");
    Iterator localIterator = paramMap.keySet().iterator();
    while (localIterator.hasNext())
    {
      String str1 = (String)localIterator.next();
      String str2 = paramMap.get(str1).toString();
      str1 = StringUtils.escapeForXML(str1);
      str2 = StringUtils.escapeForXML(str2);
      FormField localFormField = new FormField(str1);
      localFormField.setType("text-single");
      localForm.addField(localFormField);
      localForm.setAnswer(str1, str2);
    }
    joinQueue(localForm, paramString);
  }
  
  public void joinQueue(Form paramForm)
    throws XMPPException
  {
    joinQueue(paramForm, null);
  }
  
  public void joinQueue(Form paramForm, String paramString)
    throws XMPPException
  {
    if (this.inQueue) {
      throw new IllegalStateException("Already in queue " + this.workgroupJID);
    }
    paramString = new JoinQueuePacket(this.workgroupJID, paramForm, paramString);
    paramForm = this.connection.createPacketCollector(new PacketIDFilter(paramString.getPacketID()));
    this.connection.sendPacket(paramString);
    paramString = (IQ)paramForm.nextResult(10000L);
    paramForm.cancel();
    if (paramString == null) {
      throw new XMPPException("No response from the server.");
    }
    if (paramString.getError() != null) {
      throw new XMPPException(paramString.getError());
    }
    fireQueueJoinedEvent();
  }
  
  public void removeQueueListener(WorkgroupInvitationListener paramWorkgroupInvitationListener)
  {
    synchronized (this.invitationListeners)
    {
      this.invitationListeners.remove(paramWorkgroupInvitationListener);
      return;
    }
  }
  
  public void removeQueueListener(QueueListener paramQueueListener)
  {
    synchronized (this.queueListeners)
    {
      this.queueListeners.remove(paramQueueListener);
      return;
    }
  }
  
  private class JoinQueuePacket
    extends IQ
  {
    private DataForm form;
    private String userID = null;
    
    public JoinQueuePacket(String paramString1, Form paramForm, String paramString2)
    {
      this.userID = paramString2;
      setTo(paramString1);
      setType(IQ.Type.SET);
      this.form = paramForm.getDataFormToSend();
      addExtension(this.form);
    }
    
    public String getChildElementXML()
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("<join-queue xmlns=\"http://jabber.org/protocol/workgroup\">");
      localStringBuilder.append("<queue-notifications/>");
      if (Workgroup.this.connection.isAnonymous()) {
        localStringBuilder.append(new UserID(this.userID).toXML());
      }
      localStringBuilder.append(this.form.toXML());
      localStringBuilder.append("</join-queue>");
      return localStringBuilder.toString();
    }
  }
}


/* Location:           C:\Users\Administrator\Documents\ds\反编译\AndroidKiller_v1.3.1\projects\Jungledsq_ppc\ProjectSrc\smali\
 * Qualified Name:     org.jivesoftware.smackx.workgroup.user.Workgroup
 * JD-Core Version:    0.7.0.1
 */