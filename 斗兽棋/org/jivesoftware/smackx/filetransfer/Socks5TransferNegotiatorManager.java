package org.jivesoftware.smackx.filetransfer;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.jivesoftware.smack.PacketCollector;
import org.jivesoftware.smack.SmackConfiguration;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.filter.PacketIDFilter;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.packet.IQ.Type;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.util.Cache;
import org.jivesoftware.smackx.ServiceDiscoveryManager;
import org.jivesoftware.smackx.packet.Bytestream;
import org.jivesoftware.smackx.packet.Bytestream.StreamHost;
import org.jivesoftware.smackx.packet.DiscoverInfo;
import org.jivesoftware.smackx.packet.DiscoverInfo.Identity;
import org.jivesoftware.smackx.packet.DiscoverItems;
import org.jivesoftware.smackx.packet.DiscoverItems.Item;

public class Socks5TransferNegotiatorManager
  implements FileTransferNegotiatorManager
{
  private static final long BLACKLIST_LIFETIME = 7200000L;
  private static ProxyProcess proxyProcess;
  private final Cache<String, Integer> addressBlacklist = new Cache(100, 7200000L);
  private XMPPConnection connection;
  private final Object processLock = new Object();
  private List<String> proxies;
  private final Object proxyLock = new Object();
  private List<Bytestream.StreamHost> streamHosts;
  
  public Socks5TransferNegotiatorManager(XMPPConnection paramXMPPConnection)
  {
    this.connection = paramXMPPConnection;
  }
  
  private String checkIsProxy(ServiceDiscoveryManager paramServiceDiscoveryManager, DiscoverItems.Item paramItem)
  {
    try
    {
      paramServiceDiscoveryManager = paramServiceDiscoveryManager.discoverInfo(paramItem.getEntityID());
      paramItem = paramServiceDiscoveryManager.getIdentities();
      while (paramItem.hasNext())
      {
        DiscoverInfo.Identity localIdentity = (DiscoverInfo.Identity)paramItem.next();
        if (("proxy".equalsIgnoreCase(localIdentity.getCategory())) && ("bytestreams".equalsIgnoreCase(localIdentity.getType()))) {
          return paramServiceDiscoveryManager.getFrom();
        }
      }
    }
    catch (XMPPException paramServiceDiscoveryManager)
    {
      return null;
    }
    return null;
  }
  
  private void initProxies()
  {
    this.proxies = new ArrayList();
    ServiceDiscoveryManager localServiceDiscoveryManager = ServiceDiscoveryManager.getInstanceFor(this.connection);
    try
    {
      Iterator localIterator = localServiceDiscoveryManager.discoverItems(this.connection.getServiceName()).getItems();
      while (localIterator.hasNext())
      {
        String str = checkIsProxy(localServiceDiscoveryManager, (DiscoverItems.Item)localIterator.next());
        if (str != null) {
          this.proxies.add(str);
        }
      }
      while (this.proxies.size() <= 0) {}
    }
    catch (XMPPException localXMPPException)
    {
      return;
    }
    initStreamHosts();
  }
  
  private void initStreamHosts()
  {
    ArrayList localArrayList = new ArrayList();
    Iterator localIterator = this.proxies.iterator();
    while (localIterator.hasNext())
    {
      Object localObject2 = localIterator.next().toString();
      Object localObject1 = new IQ()
      {
        public String getChildElementXML()
        {
          return "<query xmlns=\"http://jabber.org/protocol/bytestreams\"/>";
        }
      };
      ((IQ)localObject1).setType(IQ.Type.GET);
      ((IQ)localObject1).setTo((String)localObject2);
      localObject2 = this.connection.createPacketCollector(new PacketIDFilter(((IQ)localObject1).getPacketID()));
      this.connection.sendPacket((Packet)localObject1);
      localObject1 = (Bytestream)((PacketCollector)localObject2).nextResult(SmackConfiguration.getPacketReplyTimeout());
      if (localObject1 != null) {
        localArrayList.addAll(((Bytestream)localObject1).getStreamHosts());
      }
      ((PacketCollector)localObject2).cancel();
    }
    this.streamHosts = localArrayList;
  }
  
  public ProxyProcess addTransfer()
    throws IOException
  {
    synchronized (this.processLock)
    {
      if (proxyProcess == null)
      {
        proxyProcess = new ProxyProcess(new ServerSocket(7777));
        proxyProcess.start();
      }
      proxyProcess.addTransfer();
      return proxyProcess;
    }
  }
  
  public void cleanup()
  {
    synchronized (this.processLock)
    {
      if (proxyProcess != null)
      {
        proxyProcess.stop();
        proxyProcess = null;
      }
      return;
    }
  }
  
  public StreamNegotiator createNegotiator()
  {
    return new Socks5TransferNegotiator(this, this.connection);
  }
  
  public int getConnectionFailures(String paramString)
  {
    paramString = (Integer)this.addressBlacklist.get(paramString);
    if (paramString != null) {
      return paramString.intValue();
    }
    return 0;
  }
  
  public Collection<Bytestream.StreamHost> getStreamHosts()
  {
    synchronized (this.proxyLock)
    {
      if (this.proxies == null) {
        initProxies();
      }
      return Collections.unmodifiableCollection(this.streamHosts);
    }
  }
  
  public void incrementConnectionFailures(String paramString)
  {
    Integer localInteger = (Integer)this.addressBlacklist.get(paramString);
    if (localInteger == null) {}
    for (localInteger = Integer.valueOf(1);; localInteger = Integer.valueOf(localInteger.intValue() + 1))
    {
      this.addressBlacklist.put(paramString, localInteger);
      return;
    }
  }
  
  public void removeTransfer()
  {
    if (proxyProcess == null) {
      return;
    }
    proxyProcess.removeTransfer();
  }
  
  class ProxyProcess
    implements Runnable
  {
    private final Map<String, Socket> connectionMap = new HashMap();
    private boolean done = false;
    private final ServerSocket listeningSocket;
    private Thread thread = new Thread(this, "File Transfer Connection Listener");
    private int transfers;
    
    ProxyProcess(ServerSocket paramServerSocket)
    {
      this.listeningSocket = paramServerSocket;
    }
    
    private String establishSocks5UploadConnection(Socket paramSocket)
      throws XMPPException, IOException
    {
      DataOutputStream localDataOutputStream = new DataOutputStream(paramSocket.getOutputStream());
      Object localObject1 = new DataInputStream(paramSocket.getInputStream());
      if (((InputStream)localObject1).read() != 5) {
        throw new XMPPException("Only SOCKS5 supported");
      }
      int j = ((InputStream)localObject1).read();
      Object localObject2 = new int[j];
      int i = 0;
      while (i < j)
      {
        localObject2[i] = ((InputStream)localObject1).read();
        i += 1;
      }
      i = -1;
      int k = localObject2.length;
      j = 0;
      for (;;)
      {
        if (j < k) {
          if (localObject2[j] != 0) {
            break label121;
          }
        }
        label121:
        for (i = 0; i == 0; i = -1)
        {
          if (i == 0) {
            break label133;
          }
          throw new XMPPException("Authentication method not supported");
        }
        j += 1;
      }
      label133:
      localDataOutputStream.write(new byte[] { 5, 0 });
      localObject1 = Socks5TransferNegotiator.createIncomingSocks5Message((InputStream)localObject1);
      localObject2 = Socks5TransferNegotiator.createOutgoingSocks5Message(0, (String)localObject1);
      if (!paramSocket.isConnected()) {
        throw new XMPPException("Socket closed by remote user");
      }
      localDataOutputStream.write((byte[])localObject2);
      return localObject1;
    }
    
    /* Error */
    public void addTransfer()
    {
      // Byte code:
      //   0: aload_0
      //   1: monitorenter
      //   2: aload_0
      //   3: getfield 113	org/jivesoftware/smackx/filetransfer/Socks5TransferNegotiatorManager$ProxyProcess:transfers	I
      //   6: iconst_m1
      //   7: if_icmpne +15 -> 22
      //   10: aload_0
      //   11: iconst_1
      //   12: putfield 113	org/jivesoftware/smackx/filetransfer/Socks5TransferNegotiatorManager$ProxyProcess:transfers	I
      //   15: aload_0
      //   16: invokevirtual 116	java/lang/Object:notify	()V
      //   19: aload_0
      //   20: monitorexit
      //   21: return
      //   22: aload_0
      //   23: aload_0
      //   24: getfield 113	org/jivesoftware/smackx/filetransfer/Socks5TransferNegotiatorManager$ProxyProcess:transfers	I
      //   27: iconst_1
      //   28: iadd
      //   29: putfield 113	org/jivesoftware/smackx/filetransfer/Socks5TransferNegotiatorManager$ProxyProcess:transfers	I
      //   32: goto -13 -> 19
      //   35: astore_1
      //   36: aload_0
      //   37: monitorexit
      //   38: aload_1
      //   39: athrow
      // Local variable table:
      //   start	length	slot	name	signature
      //   0	40	0	this	ProxyProcess
      //   35	4	1	localObject	Object
      // Exception table:
      //   from	to	target	type
      //   2	19	35	finally
      //   19	21	35	finally
      //   22	32	35	finally
      //   36	38	35	finally
    }
    
    public int getPort()
    {
      return this.listeningSocket.getLocalPort();
    }
    
    public Socket getSocket(String paramString)
    {
      synchronized (this.connectionMap)
      {
        paramString = (Socket)this.connectionMap.get(paramString);
        return paramString;
      }
    }
    
    public void removeTransfer()
    {
      try
      {
        this.transfers -= 1;
        return;
      }
      finally {}
    }
    
    public void run()
    {
      try
      {
        this.listeningSocket.setSoTimeout(10000);
        if (!this.done)
        {
          localMap = null;
          localSocket = null;
        }
      }
      catch (SocketException localSocketException)
      {
        label101:
        try
        {
          this.listeningSocket.close();
          return;
        }
        catch (IOException localIOException4) {}
        boolean bool = this.done;
        if (bool) {
          try
          {
            this.listeningSocket.close();
            return;
          }
          catch (IOException localIOException1) {}
        }
      }
      finally
      {
        try
        {
          for (;;)
          {
            Map localMap;
            Socket localSocket;
            this.listeningSocket.close();
            throw localObject2;
            Object localObject3 = localMap;
            try
            {
              localObject5 = this.listeningSocket;
              localObject3 = localMap;
              localObject3 = localSocket;
            }
            catch (SocketTimeoutException localSocketTimeoutException)
            {
              try
              {
                localSocket = this.listeningSocket.accept();
                localObject3 = localSocket;
                if (localSocket == null) {
                  continue;
                }
                localObject3 = localSocket;
                Object localObject5 = establishSocks5UploadConnection(localSocket);
                localObject3 = localSocket;
                localMap = this.connectionMap;
                localObject3 = localSocket;
                try
                {
                  this.connectionMap.put(localObject5, localSocket);
                  continue;
                }
                finally
                {
                  localObject3 = localSocket;
                }
                localSocketTimeoutException = localSocketTimeoutException;
                continue;
              }
              finally {}
            }
            catch (IOException localIOException2) {}catch (XMPPException localXMPPException)
            {
              localXMPPException.printStackTrace();
            }
            if (localIOException2 != null) {
              try
              {
                localIOException2.close();
              }
              catch (IOException localIOException3) {}
            }
          }
        }
        catch (IOException localIOException5)
        {
          break label101;
        }
      }
    }
    
    public void start()
    {
      this.thread.start();
    }
    
    /* Error */
    public void stop()
    {
      // Byte code:
      //   0: aload_0
      //   1: iconst_1
      //   2: putfield 36	org/jivesoftware/smackx/filetransfer/Socks5TransferNegotiatorManager$ProxyProcess:done	Z
      //   5: aload_0
      //   6: monitorenter
      //   7: aload_0
      //   8: invokevirtual 116	java/lang/Object:notify	()V
      //   11: aload_0
      //   12: monitorexit
      //   13: aload_0
      //   14: getfield 47	org/jivesoftware/smackx/filetransfer/Socks5TransferNegotiatorManager$ProxyProcess:listeningSocket	Ljava/net/ServerSocket;
      //   17: astore_1
      //   18: aload_1
      //   19: monitorenter
      //   20: aload_0
      //   21: getfield 47	org/jivesoftware/smackx/filetransfer/Socks5TransferNegotiatorManager$ProxyProcess:listeningSocket	Ljava/net/ServerSocket;
      //   24: invokevirtual 116	java/lang/Object:notify	()V
      //   27: aload_1
      //   28: monitorexit
      //   29: return
      //   30: astore_1
      //   31: aload_0
      //   32: monitorexit
      //   33: aload_1
      //   34: athrow
      //   35: astore_2
      //   36: aload_1
      //   37: monitorexit
      //   38: aload_2
      //   39: athrow
      // Local variable table:
      //   start	length	slot	name	signature
      //   0	40	0	this	ProxyProcess
      //   30	7	1	localObject1	Object
      //   35	4	2	localObject2	Object
      // Exception table:
      //   from	to	target	type
      //   7	13	30	finally
      //   31	33	30	finally
      //   20	29	35	finally
      //   36	38	35	finally
    }
  }
}


/* Location:           C:\Users\Administrator\Documents\ds\反编译\AndroidKiller_v1.3.1\projects\Jungledsq_ppc\ProjectSrc\smali\
 * Qualified Name:     org.jivesoftware.smackx.filetransfer.Socks5TransferNegotiatorManager
 * JD-Core Version:    0.7.0.1
 */