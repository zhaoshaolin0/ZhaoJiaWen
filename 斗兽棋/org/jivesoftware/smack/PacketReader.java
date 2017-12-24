package org.jivesoftware.smack;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.packet.Authentication;
import org.jivesoftware.smack.packet.Bind;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.packet.IQ.Type;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.packet.Presence.Type;
import org.jivesoftware.smack.packet.Registration;
import org.jivesoftware.smack.packet.RosterPacket;
import org.jivesoftware.smack.packet.RosterPacket.Item;
import org.jivesoftware.smack.packet.RosterPacket.ItemStatus;
import org.jivesoftware.smack.packet.RosterPacket.ItemType;
import org.jivesoftware.smack.packet.StreamError;
import org.jivesoftware.smack.packet.XMPPError;
import org.jivesoftware.smack.packet.XMPPError.Condition;
import org.jivesoftware.smack.provider.IQProvider;
import org.jivesoftware.smack.provider.ProviderManager;
import org.jivesoftware.smack.util.PacketParserUtils;
import org.xmlpull.mxp1.MXParser;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

class PacketReader
{
  private Collection<PacketCollector> collectors = new ConcurrentLinkedQueue();
  private XMPPConnection connection;
  private String connectionID = null;
  protected final Collection<ConnectionListener> connectionListeners = new CopyOnWriteArrayList();
  private Semaphore connectionSemaphore;
  private boolean done;
  private ExecutorService listenerExecutor;
  protected final Map<PacketListener, ListenerWrapper> listeners = new ConcurrentHashMap();
  private XmlPullParser parser;
  private Thread readerThread;
  
  protected PacketReader(XMPPConnection paramXMPPConnection)
  {
    this.connection = paramXMPPConnection;
    init();
  }
  
  private Authentication parseAuthentication(XmlPullParser paramXmlPullParser)
    throws Exception
  {
    Authentication localAuthentication = new Authentication();
    int i = 0;
    while (i == 0)
    {
      int j = paramXmlPullParser.next();
      if (j == 2)
      {
        if (paramXmlPullParser.getName().equals("username")) {
          localAuthentication.setUsername(paramXmlPullParser.nextText());
        } else if (paramXmlPullParser.getName().equals("password")) {
          localAuthentication.setPassword(paramXmlPullParser.nextText());
        } else if (paramXmlPullParser.getName().equals("digest")) {
          localAuthentication.setDigest(paramXmlPullParser.nextText());
        } else if (paramXmlPullParser.getName().equals("resource")) {
          localAuthentication.setResource(paramXmlPullParser.nextText());
        }
      }
      else if ((j == 3) && (paramXmlPullParser.getName().equals("query"))) {
        i = 1;
      }
    }
    return localAuthentication;
  }
  
  private Collection<String> parseCompressionMethods(XmlPullParser paramXmlPullParser)
    throws IOException, XmlPullParserException
  {
    ArrayList localArrayList = new ArrayList();
    int i = 0;
    while (i == 0)
    {
      int j = paramXmlPullParser.next();
      if (j == 2)
      {
        if (paramXmlPullParser.getName().equals("method")) {
          localArrayList.add(paramXmlPullParser.nextText());
        }
      }
      else if ((j == 3) && (paramXmlPullParser.getName().equals("compression"))) {
        i = 1;
      }
    }
    return localArrayList;
  }
  
  private void parseFeatures(XmlPullParser paramXmlPullParser)
    throws Exception
  {
    int i = 0;
    boolean bool = false;
    int j = 0;
    while (j == 0)
    {
      int k = paramXmlPullParser.next();
      if (k == 2)
      {
        if (paramXmlPullParser.getName().equals("starttls")) {
          i = 1;
        } else if (paramXmlPullParser.getName().equals("mechanisms")) {
          this.connection.getSASLAuthentication().setAvailableSASLMethods(parseMechanisms(paramXmlPullParser));
        } else if (paramXmlPullParser.getName().equals("bind")) {
          this.connection.getSASLAuthentication().bindingRequired();
        } else if (paramXmlPullParser.getName().equals("session")) {
          this.connection.getSASLAuthentication().sessionsSupported();
        } else if (paramXmlPullParser.getName().equals("compression")) {
          this.connection.setAvailableCompressionMethods(parseCompressionMethods(paramXmlPullParser));
        } else if (paramXmlPullParser.getName().equals("register")) {
          this.connection.getAccountManager().setSupportsAccountCreation(true);
        }
      }
      else if (k == 3) {
        if (paramXmlPullParser.getName().equals("starttls")) {
          this.connection.startTLSReceived(bool);
        } else if ((paramXmlPullParser.getName().equals("required")) && (i != 0)) {
          bool = true;
        } else if (paramXmlPullParser.getName().equals("features")) {
          j = 1;
        }
      }
    }
    if ((!this.connection.isSecureConnection()) && (i == 0) && (this.connection.getConfiguration().getSecurityMode() == ConnectionConfiguration.SecurityMode.required)) {
      throw new XMPPException("Server does not support security (TLS), but security required by connection configuration.", new XMPPError(XMPPError.Condition.forbidden));
    }
    if ((i == 0) || (this.connection.getConfiguration().getSecurityMode() == ConnectionConfiguration.SecurityMode.disabled)) {
      releaseConnectionIDLock();
    }
  }
  
  private IQ parseIQ(XmlPullParser paramXmlPullParser)
    throws Exception
  {
    Object localObject1 = null;
    String str1 = paramXmlPullParser.getAttributeValue("", "id");
    String str2 = paramXmlPullParser.getAttributeValue("", "to");
    String str3 = paramXmlPullParser.getAttributeValue("", "from");
    IQ.Type localType = IQ.Type.fromString(paramXmlPullParser.getAttributeValue("", "type"));
    XMPPError localXMPPError = null;
    int i = 0;
    while (i == 0)
    {
      int j = paramXmlPullParser.next();
      if (j == 2)
      {
        String str4 = paramXmlPullParser.getName();
        Object localObject2 = paramXmlPullParser.getNamespace();
        if (str4.equals("error"))
        {
          localXMPPError = PacketParserUtils.parseError(paramXmlPullParser);
        }
        else if ((str4.equals("query")) && (((String)localObject2).equals("jabber:iq:auth")))
        {
          localObject1 = parseAuthentication(paramXmlPullParser);
        }
        else if ((str4.equals("query")) && (((String)localObject2).equals("jabber:iq:roster")))
        {
          localObject1 = parseRoster(paramXmlPullParser);
        }
        else if ((str4.equals("query")) && (((String)localObject2).equals("jabber:iq:register")))
        {
          localObject1 = parseRegistration(paramXmlPullParser);
        }
        else if ((str4.equals("bind")) && (((String)localObject2).equals("urn:ietf:params:xml:ns:xmpp-bind")))
        {
          localObject1 = parseResourceBinding(paramXmlPullParser);
        }
        else
        {
          localObject2 = ProviderManager.getInstance().getIQProvider(str4, (String)localObject2);
          if (localObject2 != null) {
            if ((localObject2 instanceof IQProvider)) {
              localObject1 = ((IQProvider)localObject2).parseIQ(paramXmlPullParser);
            } else if ((localObject2 instanceof Class)) {
              localObject1 = (IQ)PacketParserUtils.parseWithIntrospection(str4, (Class)localObject2, paramXmlPullParser);
            }
          }
        }
      }
      else if ((j == 3) && (paramXmlPullParser.getName().equals("iq")))
      {
        i = 1;
      }
    }
    paramXmlPullParser = (XmlPullParser)localObject1;
    if (localObject1 == null)
    {
      if ((IQ.Type.GET == localType) || (IQ.Type.SET == localType))
      {
        paramXmlPullParser = new IQ()
        {
          public String getChildElementXML()
          {
            return null;
          }
        };
        paramXmlPullParser.setPacketID(str1);
        paramXmlPullParser.setTo(str3);
        paramXmlPullParser.setFrom(str2);
        paramXmlPullParser.setType(IQ.Type.ERROR);
        paramXmlPullParser.setError(new XMPPError(XMPPError.Condition.feature_not_implemented));
        this.connection.sendPacket(paramXmlPullParser);
        return null;
      }
      paramXmlPullParser = new IQ()
      {
        public String getChildElementXML()
        {
          return null;
        }
      };
    }
    paramXmlPullParser.setPacketID(str1);
    paramXmlPullParser.setTo(str2);
    paramXmlPullParser.setFrom(str3);
    paramXmlPullParser.setType(localType);
    paramXmlPullParser.setError(localXMPPError);
    return paramXmlPullParser;
  }
  
  private Collection<String> parseMechanisms(XmlPullParser paramXmlPullParser)
    throws Exception
  {
    ArrayList localArrayList = new ArrayList();
    int i = 0;
    while (i == 0)
    {
      int j = paramXmlPullParser.next();
      if (j == 2)
      {
        if (paramXmlPullParser.getName().equals("mechanism")) {
          localArrayList.add(paramXmlPullParser.nextText());
        }
      }
      else if ((j == 3) && (paramXmlPullParser.getName().equals("mechanisms"))) {
        i = 1;
      }
    }
    return localArrayList;
  }
  
  private void parsePackets(Thread paramThread)
  {
    label122:
    do
    {
      for (;;)
      {
        try
        {
          i = this.parser.getEventType();
          if (i != 2) {
            break label574;
          }
          if (this.parser.getName().equals("message"))
          {
            processPacket(PacketParserUtils.parseMessage(this.parser));
            i = this.parser.next();
            if ((this.done) || (i == 1)) {
              break label652;
            }
            if (paramThread == this.readerThread) {
              continue;
            }
            return;
          }
          if (!this.parser.getName().equals("iq")) {
            break label122;
          }
          processPacket(parseIQ(this.parser));
          continue;
          notifyConnectionError(paramThread);
        }
        catch (Exception paramThread)
        {
          if (this.done) {
            break label652;
          }
        }
        return;
        if (!this.parser.getName().equals("presence")) {
          break;
        }
        processPacket(PacketParserUtils.parsePresence(this.parser));
      }
      if (!this.parser.getName().equals("stream")) {
        break;
      }
    } while (!"jabber:client".equals(this.parser.getNamespace(null)));
    int i = 0;
    while (i < this.parser.getAttributeCount())
    {
      if (this.parser.getAttributeName(i).equals("id"))
      {
        this.connectionID = this.parser.getAttributeValue(i);
        if (!"1.0".equals(this.parser.getAttributeValue("", "version"))) {
          releaseConnectionIDLock();
        }
      }
      else if (this.parser.getAttributeName(i).equals("from"))
      {
        this.connection.serviceName = this.parser.getAttributeValue(i);
        break label653;
        if (this.parser.getName().equals("error")) {
          throw new XMPPException(parseStreamError(this.parser));
        }
        if (this.parser.getName().equals("features"))
        {
          parseFeatures(this.parser);
          break;
        }
        if (this.parser.getName().equals("proceed"))
        {
          this.connection.proceedTLSReceived();
          resetParser();
          break;
        }
        if (this.parser.getName().equals("failure"))
        {
          String str = this.parser.getNamespace(null);
          if ("urn:ietf:params:xml:ns:xmpp-tls".equals(str)) {
            throw new Exception("TLS negotiation has failed");
          }
          if ("http://jabber.org/protocol/compress".equals(str))
          {
            this.connection.streamCompressionDenied();
            break;
          }
          this.connection.getSASLAuthentication().authenticationFailed();
          break;
        }
        if (this.parser.getName().equals("challenge"))
        {
          this.connection.getSASLAuthentication().challengeReceived(this.parser.nextText());
          break;
        }
        if ((this.parser.getName().equals("success")) || (!this.parser.getName().equals("compressed"))) {
          break;
        }
        this.connection.startStreamCompression();
        resetParser();
        break;
        label574:
        if (i != 3) {
          break;
        }
        if (this.parser.getName().equals("stream"))
        {
          this.connection.disconnect();
          break;
        }
        if (!this.parser.getName().equals("success")) {
          break;
        }
        this.connection.packetWriter.openStream();
        resetParser();
        this.connection.getSASLAuthentication().authenticated();
        break;
        label652:
        return;
      }
      label653:
      i += 1;
    }
  }
  
  private Registration parseRegistration(XmlPullParser paramXmlPullParser)
    throws Exception
  {
    Registration localRegistration = new Registration();
    Object localObject2 = null;
    int i = 0;
    while (i == 0)
    {
      int j = paramXmlPullParser.next();
      if (j == 2)
      {
        if (paramXmlPullParser.getNamespace().equals("jabber:iq:register"))
        {
          String str2 = paramXmlPullParser.getName();
          String str1 = "";
          Object localObject1 = localObject2;
          if (localObject2 == null) {
            localObject1 = new HashMap();
          }
          localObject2 = str1;
          if (paramXmlPullParser.next() == 4) {
            localObject2 = paramXmlPullParser.getText();
          }
          if (!str2.equals("instructions"))
          {
            ((Map)localObject1).put(str2, localObject2);
            localObject2 = localObject1;
          }
          else
          {
            localRegistration.setInstructions((String)localObject2);
            localObject2 = localObject1;
          }
        }
        else
        {
          localRegistration.addExtension(PacketParserUtils.parsePacketExtension(paramXmlPullParser.getName(), paramXmlPullParser.getNamespace(), paramXmlPullParser));
        }
      }
      else if ((j == 3) && (paramXmlPullParser.getName().equals("query"))) {
        i = 1;
      }
    }
    localRegistration.setAttributes((Map)localObject2);
    return localRegistration;
  }
  
  private Bind parseResourceBinding(XmlPullParser paramXmlPullParser)
    throws IOException, XmlPullParserException
  {
    Bind localBind = new Bind();
    int i = 0;
    while (i == 0)
    {
      int j = paramXmlPullParser.next();
      if (j == 2)
      {
        if (paramXmlPullParser.getName().equals("resource")) {
          localBind.setResource(paramXmlPullParser.nextText());
        } else if (paramXmlPullParser.getName().equals("jid")) {
          localBind.setJid(paramXmlPullParser.nextText());
        }
      }
      else if ((j == 3) && (paramXmlPullParser.getName().equals("bind"))) {
        i = 1;
      }
    }
    return localBind;
  }
  
  private RosterPacket parseRoster(XmlPullParser paramXmlPullParser)
    throws Exception
  {
    RosterPacket localRosterPacket = new RosterPacket();
    int i = 0;
    Object localObject1 = null;
    while (i == 0)
    {
      int j = paramXmlPullParser.next();
      if (j == 2)
      {
        Object localObject2 = localObject1;
        if (paramXmlPullParser.getName().equals("item"))
        {
          localObject2 = new RosterPacket.Item(paramXmlPullParser.getAttributeValue("", "jid"), paramXmlPullParser.getAttributeValue("", "name"));
          ((RosterPacket.Item)localObject2).setItemStatus(RosterPacket.ItemStatus.fromString(paramXmlPullParser.getAttributeValue("", "ask")));
          ((RosterPacket.Item)localObject2).setItemType(RosterPacket.ItemType.valueOf(paramXmlPullParser.getAttributeValue("", "subscription")));
        }
        localObject1 = localObject2;
        if (paramXmlPullParser.getName().equals("group"))
        {
          localObject1 = localObject2;
          if (localObject2 != null)
          {
            ((RosterPacket.Item)localObject2).addGroupName(paramXmlPullParser.nextText());
            localObject1 = localObject2;
          }
        }
      }
      else if (j == 3)
      {
        if (paramXmlPullParser.getName().equals("item")) {
          localRosterPacket.addRosterItem(localObject1);
        }
        if (paramXmlPullParser.getName().equals("query")) {
          i = 1;
        }
      }
    }
    return localRosterPacket;
  }
  
  private StreamError parseStreamError(XmlPullParser paramXmlPullParser)
    throws IOException, XmlPullParserException
  {
    StreamError localStreamError = null;
    int i = 0;
    while (i == 0)
    {
      int j = paramXmlPullParser.next();
      if (j == 2) {
        localStreamError = new StreamError(paramXmlPullParser.getName());
      } else if ((j == 3) && (paramXmlPullParser.getName().equals("error"))) {
        i = 1;
      }
    }
    return localStreamError;
  }
  
  private void processPacket(Packet paramPacket)
  {
    if (paramPacket == null) {
      return;
    }
    Iterator localIterator = this.collectors.iterator();
    while (localIterator.hasNext()) {
      ((PacketCollector)localIterator.next()).processPacket(paramPacket);
    }
    this.listenerExecutor.submit(new ListenerNotification(paramPacket));
  }
  
  private void releaseConnectionIDLock()
  {
    this.connectionSemaphore.release();
  }
  
  private void resetParser()
  {
    try
    {
      this.parser = new MXParser();
      this.parser.setFeature("http://xmlpull.org/v1/doc/features.html#process-namespaces", true);
      this.parser.setInput(this.connection.reader);
      return;
    }
    catch (XmlPullParserException localXmlPullParserException)
    {
      localXmlPullParserException.printStackTrace();
    }
  }
  
  public void addPacketListener(PacketListener paramPacketListener, PacketFilter paramPacketFilter)
  {
    paramPacketFilter = new ListenerWrapper(paramPacketListener, paramPacketFilter);
    this.listeners.put(paramPacketListener, paramPacketFilter);
  }
  
  protected void cancelPacketCollector(PacketCollector paramPacketCollector)
  {
    this.collectors.remove(paramPacketCollector);
  }
  
  void cleanup()
  {
    this.connectionListeners.clear();
    this.listeners.clear();
    this.collectors.clear();
  }
  
  public PacketCollector createPacketCollector(PacketFilter paramPacketFilter)
  {
    paramPacketFilter = new PacketCollector(this, paramPacketFilter);
    this.collectors.add(paramPacketFilter);
    return paramPacketFilter;
  }
  
  protected void init()
  {
    this.done = false;
    this.connectionID = null;
    this.readerThread = new Thread()
    {
      public void run()
      {
        PacketReader.this.parsePackets(this);
      }
    };
    this.readerThread.setName("Smack Packet Reader (" + this.connection.connectionCounterValue + ")");
    this.readerThread.setDaemon(true);
    this.listenerExecutor = Executors.newSingleThreadExecutor(new ThreadFactory()
    {
      public Thread newThread(Runnable paramAnonymousRunnable)
      {
        paramAnonymousRunnable = new Thread(paramAnonymousRunnable, "Smack Listener Processor (" + PacketReader.this.connection.connectionCounterValue + ")");
        paramAnonymousRunnable.setDaemon(true);
        return paramAnonymousRunnable;
      }
    });
    resetParser();
  }
  
  void notifyConnectionError(Exception paramException)
  {
    this.done = true;
    this.connection.shutdown(new Presence(Presence.Type.unavailable));
    paramException.printStackTrace();
    Iterator localIterator = this.connectionListeners.iterator();
    while (localIterator.hasNext())
    {
      ConnectionListener localConnectionListener = (ConnectionListener)localIterator.next();
      try
      {
        localConnectionListener.connectionClosedOnError(paramException);
      }
      catch (Exception localException)
      {
        localException.printStackTrace();
      }
    }
  }
  
  protected void notifyReconnection()
  {
    Iterator localIterator = this.connectionListeners.iterator();
    while (localIterator.hasNext())
    {
      ConnectionListener localConnectionListener = (ConnectionListener)localIterator.next();
      try
      {
        localConnectionListener.reconnectionSuccessful();
      }
      catch (Exception localException)
      {
        localException.printStackTrace();
      }
    }
  }
  
  public void removePacketListener(PacketListener paramPacketListener)
  {
    this.listeners.remove(paramPacketListener);
  }
  
  public void shutdown()
  {
    if (!this.done)
    {
      Iterator localIterator = this.connectionListeners.iterator();
      while (localIterator.hasNext())
      {
        ConnectionListener localConnectionListener = (ConnectionListener)localIterator.next();
        try
        {
          localConnectionListener.connectionClosed();
        }
        catch (Exception localException)
        {
          localException.printStackTrace();
        }
      }
    }
    this.done = true;
    this.listenerExecutor.shutdown();
  }
  
  public void startup()
    throws XMPPException
  {
    this.connectionSemaphore = new Semaphore(1);
    this.readerThread.start();
    try
    {
      this.connectionSemaphore.acquire();
      int i = SmackConfiguration.getPacketReplyTimeout();
      this.connectionSemaphore.tryAcquire(i * 3, TimeUnit.MILLISECONDS);
      label45:
      if (this.connectionID == null) {
        throw new XMPPException("Connection failed. No response from server.");
      }
      this.connection.connectionID = this.connectionID;
      return;
    }
    catch (InterruptedException localInterruptedException)
    {
      break label45;
    }
  }
  
  private class ListenerNotification
    implements Runnable
  {
    private Packet packet;
    
    public ListenerNotification(Packet paramPacket)
    {
      this.packet = paramPacket;
    }
    
    public void run()
    {
      Iterator localIterator = PacketReader.this.listeners.values().iterator();
      while (localIterator.hasNext()) {
        ((PacketReader.ListenerWrapper)localIterator.next()).notifyListener(this.packet);
      }
    }
  }
  
  private static class ListenerWrapper
  {
    private PacketFilter packetFilter;
    private PacketListener packetListener;
    
    public ListenerWrapper(PacketListener paramPacketListener, PacketFilter paramPacketFilter)
    {
      this.packetListener = paramPacketListener;
      this.packetFilter = paramPacketFilter;
    }
    
    public void notifyListener(Packet paramPacket)
    {
      if ((this.packetFilter == null) || (this.packetFilter.accept(paramPacket))) {
        this.packetListener.processPacket(paramPacket);
      }
    }
  }
}


/* Location:           C:\Users\Administrator\Documents\ds\反编译\AndroidKiller_v1.3.1\projects\Jungledsq_ppc\ProjectSrc\smali\
 * Qualified Name:     org.jivesoftware.smack.PacketReader
 * JD-Core Version:    0.7.0.1
 */