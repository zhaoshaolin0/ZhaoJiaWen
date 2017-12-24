package org.jivesoftware.smack;

import java.io.IOException;
import java.io.Writer;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.packet.Packet;

class PacketWriter
{
  private XMPPConnection connection;
  private boolean done;
  private final Map<PacketInterceptor, InterceptorWrapper> interceptors = new ConcurrentHashMap();
  private Thread keepAliveThread;
  private long lastActive = System.currentTimeMillis();
  private final Map<PacketListener, ListenerWrapper> listeners = new ConcurrentHashMap();
  private final BlockingQueue<Packet> queue = new ArrayBlockingQueue(500, true);
  private Writer writer;
  private Thread writerThread;
  
  protected PacketWriter(XMPPConnection paramXMPPConnection)
  {
    this.connection = paramXMPPConnection;
    init();
  }
  
  private Packet nextPacket()
  {
    Packet localPacket = null;
    for (;;)
    {
      ??? = localPacket;
      if (!this.done)
      {
        localPacket = (Packet)this.queue.poll();
        ??? = localPacket;
        if (localPacket == null) {
          try
          {
            synchronized (this.queue)
            {
              this.queue.wait();
            }
          }
          catch (InterruptedException localInterruptedException) {}
        }
      }
    }
    return localInterruptedException;
  }
  
  private void processInterceptors(Packet paramPacket)
  {
    if (paramPacket != null)
    {
      Iterator localIterator = this.interceptors.values().iterator();
      while (localIterator.hasNext()) {
        ((InterceptorWrapper)localIterator.next()).notifyListener(paramPacket);
      }
    }
  }
  
  private void processListeners(Packet paramPacket)
  {
    Iterator localIterator = this.listeners.values().iterator();
    while (localIterator.hasNext()) {
      ((ListenerWrapper)localIterator.next()).notifyListener(paramPacket);
    }
  }
  
  /* Error */
  private void writePackets(Thread paramThread)
  {
    // Byte code:
    //   0: aload_0
    //   1: invokevirtual 137	org/jivesoftware/smack/PacketWriter:openStream	()V
    //   4: aload_0
    //   5: getfield 77	org/jivesoftware/smack/PacketWriter:done	Z
    //   8: ifne +87 -> 95
    //   11: aload_0
    //   12: getfield 139	org/jivesoftware/smack/PacketWriter:writerThread	Ljava/lang/Thread;
    //   15: aload_1
    //   16: if_acmpne +79 -> 95
    //   19: aload_0
    //   20: invokespecial 141	org/jivesoftware/smack/PacketWriter:nextPacket	()Lorg/jivesoftware/smack/packet/Packet;
    //   23: astore_3
    //   24: aload_3
    //   25: ifnull -21 -> 4
    //   28: aload_0
    //   29: getfield 85	org/jivesoftware/smack/PacketWriter:writer	Ljava/io/Writer;
    //   32: astore_2
    //   33: aload_2
    //   34: monitorenter
    //   35: aload_0
    //   36: getfield 85	org/jivesoftware/smack/PacketWriter:writer	Ljava/io/Writer;
    //   39: aload_3
    //   40: invokevirtual 145	org/jivesoftware/smack/packet/Packet:toXML	()Ljava/lang/String;
    //   43: invokevirtual 151	java/io/Writer:write	(Ljava/lang/String;)V
    //   46: aload_0
    //   47: getfield 85	org/jivesoftware/smack/PacketWriter:writer	Ljava/io/Writer;
    //   50: invokevirtual 154	java/io/Writer:flush	()V
    //   53: aload_0
    //   54: invokestatic 50	java/lang/System:currentTimeMillis	()J
    //   57: putfield 52	org/jivesoftware/smack/PacketWriter:lastActive	J
    //   60: aload_2
    //   61: monitorexit
    //   62: goto -58 -> 4
    //   65: astore_1
    //   66: aload_2
    //   67: monitorexit
    //   68: aload_1
    //   69: athrow
    //   70: astore_1
    //   71: aload_0
    //   72: getfield 77	org/jivesoftware/smack/PacketWriter:done	Z
    //   75: ifne +19 -> 94
    //   78: aload_0
    //   79: iconst_1
    //   80: putfield 77	org/jivesoftware/smack/PacketWriter:done	Z
    //   83: aload_0
    //   84: getfield 63	org/jivesoftware/smack/PacketWriter:connection	Lorg/jivesoftware/smack/XMPPConnection;
    //   87: getfield 160	org/jivesoftware/smack/XMPPConnection:packetReader	Lorg/jivesoftware/smack/PacketReader;
    //   90: aload_1
    //   91: invokevirtual 166	org/jivesoftware/smack/PacketReader:notifyConnectionError	(Ljava/lang/Exception;)V
    //   94: return
    //   95: aload_0
    //   96: getfield 85	org/jivesoftware/smack/PacketWriter:writer	Ljava/io/Writer;
    //   99: astore_1
    //   100: aload_1
    //   101: monitorenter
    //   102: aload_0
    //   103: getfield 61	org/jivesoftware/smack/PacketWriter:queue	Ljava/util/concurrent/BlockingQueue;
    //   106: invokeinterface 169 1 0
    //   111: ifne +75 -> 186
    //   114: aload_0
    //   115: getfield 61	org/jivesoftware/smack/PacketWriter:queue	Ljava/util/concurrent/BlockingQueue;
    //   118: invokeinterface 172 1 0
    //   123: checkcast 99	org/jivesoftware/smack/packet/Packet
    //   126: astore_2
    //   127: aload_0
    //   128: getfield 85	org/jivesoftware/smack/PacketWriter:writer	Ljava/io/Writer;
    //   131: aload_2
    //   132: invokevirtual 145	org/jivesoftware/smack/packet/Packet:toXML	()Ljava/lang/String;
    //   135: invokevirtual 151	java/io/Writer:write	(Ljava/lang/String;)V
    //   138: goto -36 -> 102
    //   141: astore_2
    //   142: aload_1
    //   143: monitorexit
    //   144: aload_2
    //   145: athrow
    //   146: astore_1
    //   147: aload_1
    //   148: invokevirtual 175	java/lang/Exception:printStackTrace	()V
    //   151: aload_0
    //   152: getfield 61	org/jivesoftware/smack/PacketWriter:queue	Ljava/util/concurrent/BlockingQueue;
    //   155: invokeinterface 178 1 0
    //   160: aload_0
    //   161: getfield 85	org/jivesoftware/smack/PacketWriter:writer	Ljava/io/Writer;
    //   164: ldc 180
    //   166: invokevirtual 151	java/io/Writer:write	(Ljava/lang/String;)V
    //   169: aload_0
    //   170: getfield 85	org/jivesoftware/smack/PacketWriter:writer	Ljava/io/Writer;
    //   173: invokevirtual 154	java/io/Writer:flush	()V
    //   176: aload_0
    //   177: getfield 85	org/jivesoftware/smack/PacketWriter:writer	Ljava/io/Writer;
    //   180: invokevirtual 183	java/io/Writer:close	()V
    //   183: return
    //   184: astore_1
    //   185: return
    //   186: aload_0
    //   187: getfield 85	org/jivesoftware/smack/PacketWriter:writer	Ljava/io/Writer;
    //   190: invokevirtual 154	java/io/Writer:flush	()V
    //   193: aload_1
    //   194: monitorexit
    //   195: goto -44 -> 151
    //   198: astore_1
    //   199: aload_0
    //   200: getfield 85	org/jivesoftware/smack/PacketWriter:writer	Ljava/io/Writer;
    //   203: invokevirtual 183	java/io/Writer:close	()V
    //   206: return
    //   207: astore_1
    //   208: return
    //   209: astore_1
    //   210: aload_0
    //   211: getfield 85	org/jivesoftware/smack/PacketWriter:writer	Ljava/io/Writer;
    //   214: invokevirtual 183	java/io/Writer:close	()V
    //   217: aload_1
    //   218: athrow
    //   219: astore_2
    //   220: goto -3 -> 217
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	223	0	this	PacketWriter
    //   0	223	1	paramThread	Thread
    //   32	100	2	localObject1	Object
    //   141	4	2	localObject2	Object
    //   219	1	2	localException	java.lang.Exception
    //   23	17	3	localPacket	Packet
    // Exception table:
    //   from	to	target	type
    //   35	62	65	finally
    //   66	68	65	finally
    //   0	4	70	java/io/IOException
    //   4	24	70	java/io/IOException
    //   28	35	70	java/io/IOException
    //   68	70	70	java/io/IOException
    //   95	102	70	java/io/IOException
    //   144	146	70	java/io/IOException
    //   147	151	70	java/io/IOException
    //   151	160	70	java/io/IOException
    //   176	183	70	java/io/IOException
    //   199	206	70	java/io/IOException
    //   210	217	70	java/io/IOException
    //   217	219	70	java/io/IOException
    //   102	138	141	finally
    //   142	144	141	finally
    //   186	195	141	finally
    //   95	102	146	java/lang/Exception
    //   144	146	146	java/lang/Exception
    //   176	183	184	java/lang/Exception
    //   160	176	198	java/lang/Exception
    //   199	206	207	java/lang/Exception
    //   160	176	209	finally
    //   210	217	219	java/lang/Exception
  }
  
  public void addPacketInterceptor(PacketInterceptor paramPacketInterceptor, PacketFilter paramPacketFilter)
  {
    this.interceptors.put(paramPacketInterceptor, new InterceptorWrapper(paramPacketInterceptor, paramPacketFilter));
  }
  
  public void addPacketListener(PacketListener paramPacketListener, PacketFilter paramPacketFilter)
  {
    this.listeners.put(paramPacketListener, new ListenerWrapper(paramPacketListener, paramPacketFilter));
  }
  
  void cleanup()
  {
    this.interceptors.clear();
    this.listeners.clear();
  }
  
  public int getPacketListenerCount()
  {
    return this.listeners.size();
  }
  
  protected void init()
  {
    this.writer = this.connection.writer;
    this.done = false;
    this.writerThread = new Thread()
    {
      public void run()
      {
        PacketWriter.this.writePackets(this);
      }
    };
    this.writerThread.setName("Smack Packet Writer (" + this.connection.connectionCounterValue + ")");
    this.writerThread.setDaemon(true);
  }
  
  void openStream()
    throws IOException
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("<stream:stream");
    localStringBuilder.append(" to=\"").append(this.connection.serviceName).append("\"");
    localStringBuilder.append(" xmlns=\"jabber:client\"");
    localStringBuilder.append(" xmlns:stream=\"http://etherx.jabber.org/streams\"");
    localStringBuilder.append(" version=\"1.0\">");
    this.writer.write(localStringBuilder.toString());
    this.writer.flush();
  }
  
  public void removePacketInterceptor(PacketInterceptor paramPacketInterceptor)
  {
    this.interceptors.remove(paramPacketInterceptor);
  }
  
  public void removePacketListener(PacketListener paramPacketListener)
  {
    this.listeners.remove(paramPacketListener);
  }
  
  /* Error */
  public void sendPacket(Packet paramPacket)
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 77	org/jivesoftware/smack/PacketWriter:done	Z
    //   4: ifne +39 -> 43
    //   7: aload_0
    //   8: aload_1
    //   9: invokespecial 263	org/jivesoftware/smack/PacketWriter:processInterceptors	(Lorg/jivesoftware/smack/packet/Packet;)V
    //   12: aload_0
    //   13: getfield 61	org/jivesoftware/smack/PacketWriter:queue	Ljava/util/concurrent/BlockingQueue;
    //   16: aload_1
    //   17: invokeinterface 266 2 0
    //   22: aload_0
    //   23: getfield 61	org/jivesoftware/smack/PacketWriter:queue	Ljava/util/concurrent/BlockingQueue;
    //   26: astore_2
    //   27: aload_2
    //   28: monitorenter
    //   29: aload_0
    //   30: getfield 61	org/jivesoftware/smack/PacketWriter:queue	Ljava/util/concurrent/BlockingQueue;
    //   33: invokevirtual 269	java/lang/Object:notifyAll	()V
    //   36: aload_2
    //   37: monitorexit
    //   38: aload_0
    //   39: aload_1
    //   40: invokespecial 271	org/jivesoftware/smack/PacketWriter:processListeners	(Lorg/jivesoftware/smack/packet/Packet;)V
    //   43: return
    //   44: astore_1
    //   45: aload_1
    //   46: invokevirtual 272	java/lang/InterruptedException:printStackTrace	()V
    //   49: return
    //   50: astore_1
    //   51: aload_2
    //   52: monitorexit
    //   53: aload_1
    //   54: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	55	0	this	PacketWriter
    //   0	55	1	paramPacket	Packet
    // Exception table:
    //   from	to	target	type
    //   12	22	44	java/lang/InterruptedException
    //   29	38	50	finally
    //   51	53	50	finally
  }
  
  void setWriter(Writer paramWriter)
  {
    this.writer = paramWriter;
  }
  
  public void shutdown()
  {
    this.done = true;
    synchronized (this.queue)
    {
      this.queue.notifyAll();
      return;
    }
  }
  
  void startKeepAliveProcess()
  {
    int i = SmackConfiguration.getKeepAliveInterval();
    if (i > 0)
    {
      KeepAliveTask localKeepAliveTask = new KeepAliveTask(i);
      this.keepAliveThread = new Thread(localKeepAliveTask);
      localKeepAliveTask.setThread(this.keepAliveThread);
      this.keepAliveThread.setDaemon(true);
      this.keepAliveThread.setName("Smack Keep Alive (" + this.connection.connectionCounterValue + ")");
      this.keepAliveThread.start();
    }
  }
  
  public void startup()
  {
    this.writerThread.start();
  }
  
  private static class InterceptorWrapper
  {
    private PacketFilter packetFilter;
    private PacketInterceptor packetInterceptor;
    
    public InterceptorWrapper(PacketInterceptor paramPacketInterceptor, PacketFilter paramPacketFilter)
    {
      this.packetInterceptor = paramPacketInterceptor;
      this.packetFilter = paramPacketFilter;
    }
    
    public boolean equals(Object paramObject)
    {
      if (paramObject == null) {
        return false;
      }
      if ((paramObject instanceof InterceptorWrapper)) {
        return ((InterceptorWrapper)paramObject).packetInterceptor.equals(this.packetInterceptor);
      }
      if ((paramObject instanceof PacketInterceptor)) {
        return paramObject.equals(this.packetInterceptor);
      }
      return false;
    }
    
    public void notifyListener(Packet paramPacket)
    {
      if ((this.packetFilter == null) || (this.packetFilter.accept(paramPacket))) {
        this.packetInterceptor.interceptPacket(paramPacket);
      }
    }
  }
  
  private class KeepAliveTask
    implements Runnable
  {
    private int delay;
    private Thread thread;
    
    public KeepAliveTask(int paramInt)
    {
      this.delay = paramInt;
    }
    
    /* Error */
    public void run()
    {
      // Byte code:
      //   0: ldc2_w 31
      //   3: invokestatic 38	java/lang/Thread:sleep	(J)V
      //   6: aload_0
      //   7: getfield 19	org/jivesoftware/smack/PacketWriter$KeepAliveTask:this$0	Lorg/jivesoftware/smack/PacketWriter;
      //   10: invokestatic 42	org/jivesoftware/smack/PacketWriter:access$100	(Lorg/jivesoftware/smack/PacketWriter;)Z
      //   13: ifne +111 -> 124
      //   16: aload_0
      //   17: getfield 19	org/jivesoftware/smack/PacketWriter$KeepAliveTask:this$0	Lorg/jivesoftware/smack/PacketWriter;
      //   20: invokestatic 46	org/jivesoftware/smack/PacketWriter:access$200	(Lorg/jivesoftware/smack/PacketWriter;)Ljava/lang/Thread;
      //   23: aload_0
      //   24: getfield 48	org/jivesoftware/smack/PacketWriter$KeepAliveTask:thread	Ljava/lang/Thread;
      //   27: if_acmpne +97 -> 124
      //   30: aload_0
      //   31: getfield 19	org/jivesoftware/smack/PacketWriter$KeepAliveTask:this$0	Lorg/jivesoftware/smack/PacketWriter;
      //   34: invokestatic 52	org/jivesoftware/smack/PacketWriter:access$300	(Lorg/jivesoftware/smack/PacketWriter;)Ljava/io/Writer;
      //   37: astore 6
      //   39: aload 6
      //   41: monitorenter
      //   42: invokestatic 58	java/lang/System:currentTimeMillis	()J
      //   45: lstore_2
      //   46: aload_0
      //   47: getfield 19	org/jivesoftware/smack/PacketWriter$KeepAliveTask:this$0	Lorg/jivesoftware/smack/PacketWriter;
      //   50: invokestatic 62	org/jivesoftware/smack/PacketWriter:access$400	(Lorg/jivesoftware/smack/PacketWriter;)J
      //   53: lstore 4
      //   55: aload_0
      //   56: getfield 24	org/jivesoftware/smack/PacketWriter$KeepAliveTask:delay	I
      //   59: istore_1
      //   60: lload_2
      //   61: lload 4
      //   63: lsub
      //   64: iload_1
      //   65: i2l
      //   66: lcmp
      //   67: iflt +25 -> 92
      //   70: aload_0
      //   71: getfield 19	org/jivesoftware/smack/PacketWriter$KeepAliveTask:this$0	Lorg/jivesoftware/smack/PacketWriter;
      //   74: invokestatic 52	org/jivesoftware/smack/PacketWriter:access$300	(Lorg/jivesoftware/smack/PacketWriter;)Ljava/io/Writer;
      //   77: ldc 64
      //   79: invokevirtual 70	java/io/Writer:write	(Ljava/lang/String;)V
      //   82: aload_0
      //   83: getfield 19	org/jivesoftware/smack/PacketWriter$KeepAliveTask:this$0	Lorg/jivesoftware/smack/PacketWriter;
      //   86: invokestatic 52	org/jivesoftware/smack/PacketWriter:access$300	(Lorg/jivesoftware/smack/PacketWriter;)Ljava/io/Writer;
      //   89: invokevirtual 73	java/io/Writer:flush	()V
      //   92: aload 6
      //   94: monitorexit
      //   95: aload_0
      //   96: getfield 24	org/jivesoftware/smack/PacketWriter$KeepAliveTask:delay	I
      //   99: i2l
      //   100: invokestatic 38	java/lang/Thread:sleep	(J)V
      //   103: goto -97 -> 6
      //   106: astore 6
      //   108: goto -102 -> 6
      //   111: astore 7
      //   113: aload 6
      //   115: monitorexit
      //   116: aload 7
      //   118: athrow
      //   119: astore 6
      //   121: goto -115 -> 6
      //   124: return
      //   125: astore 7
      //   127: goto -35 -> 92
      // Local variable table:
      //   start	length	slot	name	signature
      //   0	130	0	this	KeepAliveTask
      //   59	6	1	i	int
      //   45	16	2	l1	long
      //   53	9	4	l2	long
      //   106	8	6	localInterruptedException1	InterruptedException
      //   119	1	6	localInterruptedException2	InterruptedException
      //   111	6	7	localObject	Object
      //   125	1	7	localException	java.lang.Exception
      // Exception table:
      //   from	to	target	type
      //   95	103	106	java/lang/InterruptedException
      //   42	60	111	finally
      //   70	92	111	finally
      //   92	95	111	finally
      //   113	116	111	finally
      //   0	6	119	java/lang/InterruptedException
      //   70	92	125	java/lang/Exception
    }
    
    protected void setThread(Thread paramThread)
    {
      this.thread = paramThread;
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
 * Qualified Name:     org.jivesoftware.smack.PacketWriter
 * JD-Core Version:    0.7.0.1
 */