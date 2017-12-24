package org.jivesoftware.smack;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.net.Socket;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.atomic.AtomicInteger;
import javax.security.auth.callback.CallbackHandler;
import org.jivesoftware.smack.debugger.SmackDebugger;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.packet.Presence.Type;
import org.jivesoftware.smack.util.StringUtils;

public class XMPPConnection
{
  public static boolean DEBUG_ENABLED = false;
  private static AtomicInteger connectionCounter = new AtomicInteger(0);
  private static final Set<ConnectionCreationListener> connectionEstablishedListeners = new CopyOnWriteArraySet();
  private AccountManager accountManager = null;
  private boolean anonymous = false;
  private boolean authenticated = false;
  private CallbackHandler callbackHandler = null;
  private ChatManager chatManager;
  private Collection compressionMethods;
  private ConnectionConfiguration configuration;
  private boolean connected = false;
  int connectionCounterValue = connectionCounter.getAndIncrement();
  String connectionID = null;
  private SmackDebugger debugger = null;
  String host;
  PacketReader packetReader;
  PacketWriter packetWriter;
  int port;
  Reader reader;
  Roster roster = null;
  private SASLAuthentication saslAuthentication = new SASLAuthentication(this);
  String serviceName;
  Socket socket;
  private String user = null;
  private boolean usingCompression;
  private boolean usingTLS = false;
  private boolean wasAuthenticated = false;
  Writer writer;
  
  static
  {
    try
    {
      DEBUG_ENABLED = Boolean.getBoolean("smack.debugEnabled");
      label33:
      SmackConfiguration.getVersion();
      return;
    }
    catch (Exception localException)
    {
      break label33;
    }
  }
  
  public XMPPConnection(String paramString)
  {
    paramString = new ConnectionConfiguration(paramString);
    paramString.setCompressionEnabled(false);
    paramString.setSASLAuthenticationEnabled(true);
    paramString.setDebuggerEnabled(DEBUG_ENABLED);
    this.configuration = paramString;
    this.callbackHandler = null;
  }
  
  public XMPPConnection(String paramString, CallbackHandler paramCallbackHandler)
  {
    paramString = new ConnectionConfiguration(paramString);
    paramString.setCompressionEnabled(false);
    paramString.setSASLAuthenticationEnabled(true);
    paramString.setDebuggerEnabled(DEBUG_ENABLED);
    this.configuration = paramString;
    this.callbackHandler = paramCallbackHandler;
  }
  
  public XMPPConnection(ConnectionConfiguration paramConnectionConfiguration)
  {
    this.configuration = paramConnectionConfiguration;
    this.callbackHandler = null;
  }
  
  public XMPPConnection(ConnectionConfiguration paramConnectionConfiguration, CallbackHandler paramCallbackHandler)
  {
    this.configuration = paramConnectionConfiguration;
    this.callbackHandler = paramCallbackHandler;
  }
  
  public static void addConnectionCreationListener(ConnectionCreationListener paramConnectionCreationListener)
  {
    connectionEstablishedListeners.add(paramConnectionCreationListener);
  }
  
  /* Error */
  private void connectUsingConfiguration(ConnectionConfiguration paramConnectionConfiguration)
    throws XMPPException
  {
    // Byte code:
    //   0: aload_0
    //   1: aload_1
    //   2: invokevirtual 160	org/jivesoftware/smack/ConnectionConfiguration:getHost	()Ljava/lang/String;
    //   5: putfield 162	org/jivesoftware/smack/XMPPConnection:host	Ljava/lang/String;
    //   8: aload_0
    //   9: aload_1
    //   10: invokevirtual 165	org/jivesoftware/smack/ConnectionConfiguration:getPort	()I
    //   13: putfield 167	org/jivesoftware/smack/XMPPConnection:port	I
    //   16: aload_1
    //   17: invokevirtual 171	org/jivesoftware/smack/ConnectionConfiguration:getSocketFactory	()Ljavax/net/SocketFactory;
    //   20: ifnonnull +35 -> 55
    //   23: aload_0
    //   24: new 173	java/net/Socket
    //   27: dup
    //   28: aload_0
    //   29: getfield 162	org/jivesoftware/smack/XMPPConnection:host	Ljava/lang/String;
    //   32: aload_0
    //   33: getfield 167	org/jivesoftware/smack/XMPPConnection:port	I
    //   36: invokespecial 176	java/net/Socket:<init>	(Ljava/lang/String;I)V
    //   39: putfield 178	org/jivesoftware/smack/XMPPConnection:socket	Ljava/net/Socket;
    //   42: aload_0
    //   43: aload_1
    //   44: invokevirtual 181	org/jivesoftware/smack/ConnectionConfiguration:getServiceName	()Ljava/lang/String;
    //   47: putfield 183	org/jivesoftware/smack/XMPPConnection:serviceName	Ljava/lang/String;
    //   50: aload_0
    //   51: invokespecial 186	org/jivesoftware/smack/XMPPConnection:initConnection	()V
    //   54: return
    //   55: aload_0
    //   56: aload_1
    //   57: invokevirtual 171	org/jivesoftware/smack/ConnectionConfiguration:getSocketFactory	()Ljavax/net/SocketFactory;
    //   60: aload_0
    //   61: getfield 162	org/jivesoftware/smack/XMPPConnection:host	Ljava/lang/String;
    //   64: aload_0
    //   65: getfield 167	org/jivesoftware/smack/XMPPConnection:port	I
    //   68: invokevirtual 192	javax/net/SocketFactory:createSocket	(Ljava/lang/String;I)Ljava/net/Socket;
    //   71: putfield 178	org/jivesoftware/smack/XMPPConnection:socket	Ljava/net/Socket;
    //   74: goto -32 -> 42
    //   77: astore_1
    //   78: new 194	java/lang/StringBuilder
    //   81: dup
    //   82: invokespecial 195	java/lang/StringBuilder:<init>	()V
    //   85: ldc 197
    //   87: invokevirtual 201	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   90: aload_0
    //   91: getfield 162	org/jivesoftware/smack/XMPPConnection:host	Ljava/lang/String;
    //   94: invokevirtual 201	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   97: ldc 203
    //   99: invokevirtual 201	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   102: aload_0
    //   103: getfield 167	org/jivesoftware/smack/XMPPConnection:port	I
    //   106: invokevirtual 206	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
    //   109: ldc 208
    //   111: invokevirtual 201	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   114: invokevirtual 211	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   117: astore_2
    //   118: new 153	org/jivesoftware/smack/XMPPException
    //   121: dup
    //   122: aload_2
    //   123: new 213	org/jivesoftware/smack/packet/XMPPError
    //   126: dup
    //   127: getstatic 219	org/jivesoftware/smack/packet/XMPPError$Condition:remote_server_timeout	Lorg/jivesoftware/smack/packet/XMPPError$Condition;
    //   130: aload_2
    //   131: invokespecial 222	org/jivesoftware/smack/packet/XMPPError:<init>	(Lorg/jivesoftware/smack/packet/XMPPError$Condition;Ljava/lang/String;)V
    //   134: aload_1
    //   135: invokespecial 225	org/jivesoftware/smack/XMPPException:<init>	(Ljava/lang/String;Lorg/jivesoftware/smack/packet/XMPPError;Ljava/lang/Throwable;)V
    //   138: athrow
    //   139: astore_1
    //   140: new 194	java/lang/StringBuilder
    //   143: dup
    //   144: invokespecial 195	java/lang/StringBuilder:<init>	()V
    //   147: ldc 227
    //   149: invokevirtual 201	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   152: aload_0
    //   153: getfield 162	org/jivesoftware/smack/XMPPConnection:host	Ljava/lang/String;
    //   156: invokevirtual 201	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   159: ldc 203
    //   161: invokevirtual 201	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   164: aload_0
    //   165: getfield 167	org/jivesoftware/smack/XMPPConnection:port	I
    //   168: invokevirtual 206	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
    //   171: ldc 208
    //   173: invokevirtual 201	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   176: invokevirtual 211	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   179: astore_2
    //   180: new 153	org/jivesoftware/smack/XMPPException
    //   183: dup
    //   184: aload_2
    //   185: new 213	org/jivesoftware/smack/packet/XMPPError
    //   188: dup
    //   189: getstatic 230	org/jivesoftware/smack/packet/XMPPError$Condition:remote_server_error	Lorg/jivesoftware/smack/packet/XMPPError$Condition;
    //   192: aload_2
    //   193: invokespecial 222	org/jivesoftware/smack/packet/XMPPError:<init>	(Lorg/jivesoftware/smack/packet/XMPPError$Condition;Ljava/lang/String;)V
    //   196: aload_1
    //   197: invokespecial 225	org/jivesoftware/smack/XMPPException:<init>	(Ljava/lang/String;Lorg/jivesoftware/smack/packet/XMPPError;Ljava/lang/Throwable;)V
    //   200: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	201	0	this	XMPPConnection
    //   0	201	1	paramConnectionConfiguration	ConnectionConfiguration
    //   117	76	2	str	String
    // Exception table:
    //   from	to	target	type
    //   16	42	77	java/net/UnknownHostException
    //   55	74	77	java/net/UnknownHostException
    //   16	42	139	java/io/IOException
    //   55	74	139	java/io/IOException
  }
  
  private boolean hasAvailableCompressionMethod(String paramString)
  {
    return (this.compressionMethods != null) && (this.compressionMethods.contains(paramString));
  }
  
  private void initConnection()
    throws XMPPException
  {
    if ((this.packetReader == null) || (this.packetWriter == null))
    {
      i = 1;
      for (;;)
      {
        if (i == 0) {
          this.usingCompression = false;
        }
        initReaderAndWriter();
        if (i != 0) {
          try
          {
            this.packetWriter = new PacketWriter(this);
            this.packetReader = new PacketReader(this);
            if (this.configuration.isDebuggerEnabled())
            {
              this.packetReader.addPacketListener(this.debugger.getReaderListener(), null);
              if (this.debugger.getWriterListener() != null) {
                this.packetWriter.addPacketListener(this.debugger.getWriterListener(), null);
              }
            }
            label113:
            this.packetWriter.startup();
            this.packetReader.startup();
            this.connected = true;
            this.packetWriter.startKeepAliveProcess();
            if (i != 0)
            {
              Iterator localIterator = connectionEstablishedListeners.iterator();
              while (localIterator.hasNext()) {
                ((ConnectionCreationListener)localIterator.next()).connectionCreated(this);
              }
            }
            try
            {
              this.packetWriter.shutdown();
              this.packetWriter = null;
              if (this.packetReader != null) {}
              try
              {
                this.packetReader.shutdown();
                this.packetReader = null;
                if (this.reader != null) {}
                try
                {
                  this.reader.close();
                  this.reader = null;
                  if (this.writer != null) {}
                  try
                  {
                    this.writer.close();
                    this.writer = null;
                    if (this.socket != null) {}
                    try
                    {
                      this.socket.close();
                      this.socket = null;
                      setWasAuthenticated(this.authenticated);
                      this.authenticated = false;
                      this.connected = false;
                      throw localXMPPException;
                      i = 0;
                      continue;
                      this.packetWriter.init();
                      this.packetReader.init();
                      break label113;
                      this.packetReader.notifyReconnection();
                      return;
                    }
                    catch (Exception localException)
                    {
                      break label270;
                    }
                  }
                  catch (Throwable localThrowable1)
                  {
                    break label251;
                  }
                }
                catch (Throwable localThrowable2)
                {
                  break label232;
                }
              }
              catch (Throwable localThrowable3)
              {
                break label213;
              }
            }
            catch (Throwable localThrowable4)
            {
              break label194;
            }
          }
          catch (XMPPException localXMPPException)
          {
            if (this.packetWriter == null) {}
          }
        }
      }
    }
  }
  
  /* Error */
  private void initReaderAndWriter()
    throws XMPPException
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 247	org/jivesoftware/smack/XMPPConnection:usingCompression	Z
    //   4: ifne +197 -> 201
    //   7: aload_0
    //   8: new 328	java/io/BufferedReader
    //   11: dup
    //   12: new 330	java/io/InputStreamReader
    //   15: dup
    //   16: aload_0
    //   17: getfield 178	org/jivesoftware/smack/XMPPConnection:socket	Ljava/net/Socket;
    //   20: invokevirtual 334	java/net/Socket:getInputStream	()Ljava/io/InputStream;
    //   23: ldc_w 336
    //   26: invokespecial 339	java/io/InputStreamReader:<init>	(Ljava/io/InputStream;Ljava/lang/String;)V
    //   29: invokespecial 342	java/io/BufferedReader:<init>	(Ljava/io/Reader;)V
    //   32: putfield 305	org/jivesoftware/smack/XMPPConnection:reader	Ljava/io/Reader;
    //   35: aload_0
    //   36: new 344	java/io/BufferedWriter
    //   39: dup
    //   40: new 346	java/io/OutputStreamWriter
    //   43: dup
    //   44: aload_0
    //   45: getfield 178	org/jivesoftware/smack/XMPPConnection:socket	Ljava/net/Socket;
    //   48: invokevirtual 350	java/net/Socket:getOutputStream	()Ljava/io/OutputStream;
    //   51: ldc_w 336
    //   54: invokespecial 353	java/io/OutputStreamWriter:<init>	(Ljava/io/OutputStream;Ljava/lang/String;)V
    //   57: invokespecial 356	java/io/BufferedWriter:<init>	(Ljava/io/Writer;)V
    //   60: putfield 312	org/jivesoftware/smack/XMPPConnection:writer	Ljava/io/Writer;
    //   63: aload_0
    //   64: getfield 139	org/jivesoftware/smack/XMPPConnection:configuration	Lorg/jivesoftware/smack/ConnectionConfiguration;
    //   67: invokevirtual 260	org/jivesoftware/smack/ConnectionConfiguration:isDebuggerEnabled	()Z
    //   70: ifeq +130 -> 200
    //   73: aload_0
    //   74: getfield 92	org/jivesoftware/smack/XMPPConnection:debugger	Lorg/jivesoftware/smack/debugger/SmackDebugger;
    //   77: ifnonnull +466 -> 543
    //   80: aconst_null
    //   81: astore_2
    //   82: ldc_w 358
    //   85: invokestatic 364	java/lang/System:getProperty	(Ljava/lang/String;)Ljava/lang/String;
    //   88: astore_1
    //   89: aload_1
    //   90: astore_2
    //   91: aconst_null
    //   92: astore_3
    //   93: aload_3
    //   94: astore_1
    //   95: aload_2
    //   96: ifnull +8 -> 104
    //   99: aload_2
    //   100: invokestatic 370	java/lang/Class:forName	(Ljava/lang/String;)Ljava/lang/Class;
    //   103: astore_1
    //   104: aload_1
    //   105: astore_2
    //   106: aload_1
    //   107: ifnonnull +10 -> 117
    //   110: ldc_w 372
    //   113: invokestatic 370	java/lang/Class:forName	(Ljava/lang/String;)Ljava/lang/Class;
    //   116: astore_2
    //   117: aload_0
    //   118: aload_2
    //   119: iconst_3
    //   120: anewarray 366	java/lang/Class
    //   123: dup
    //   124: iconst_0
    //   125: ldc 2
    //   127: aastore
    //   128: dup
    //   129: iconst_1
    //   130: ldc_w 314
    //   133: aastore
    //   134: dup
    //   135: iconst_2
    //   136: ldc_w 307
    //   139: aastore
    //   140: invokevirtual 376	java/lang/Class:getConstructor	([Ljava/lang/Class;)Ljava/lang/reflect/Constructor;
    //   143: iconst_3
    //   144: anewarray 4	java/lang/Object
    //   147: dup
    //   148: iconst_0
    //   149: aload_0
    //   150: aastore
    //   151: dup
    //   152: iconst_1
    //   153: aload_0
    //   154: getfield 312	org/jivesoftware/smack/XMPPConnection:writer	Ljava/io/Writer;
    //   157: aastore
    //   158: dup
    //   159: iconst_2
    //   160: aload_0
    //   161: getfield 305	org/jivesoftware/smack/XMPPConnection:reader	Ljava/io/Reader;
    //   164: aastore
    //   165: invokevirtual 382	java/lang/reflect/Constructor:newInstance	([Ljava/lang/Object;)Ljava/lang/Object;
    //   168: checkcast 262	org/jivesoftware/smack/debugger/SmackDebugger
    //   171: putfield 92	org/jivesoftware/smack/XMPPConnection:debugger	Lorg/jivesoftware/smack/debugger/SmackDebugger;
    //   174: aload_0
    //   175: aload_0
    //   176: getfield 92	org/jivesoftware/smack/XMPPConnection:debugger	Lorg/jivesoftware/smack/debugger/SmackDebugger;
    //   179: invokeinterface 386 1 0
    //   184: putfield 305	org/jivesoftware/smack/XMPPConnection:reader	Ljava/io/Reader;
    //   187: aload_0
    //   188: aload_0
    //   189: getfield 92	org/jivesoftware/smack/XMPPConnection:debugger	Lorg/jivesoftware/smack/debugger/SmackDebugger;
    //   192: invokeinterface 390 1 0
    //   197: putfield 312	org/jivesoftware/smack/XMPPConnection:writer	Ljava/io/Writer;
    //   200: return
    //   201: ldc_w 392
    //   204: invokestatic 370	java/lang/Class:forName	(Ljava/lang/String;)Ljava/lang/Class;
    //   207: astore_1
    //   208: aload_1
    //   209: iconst_2
    //   210: anewarray 366	java/lang/Class
    //   213: dup
    //   214: iconst_0
    //   215: ldc_w 394
    //   218: aastore
    //   219: dup
    //   220: iconst_1
    //   221: getstatic 400	java/lang/Integer:TYPE	Ljava/lang/Class;
    //   224: aastore
    //   225: invokevirtual 376	java/lang/Class:getConstructor	([Ljava/lang/Class;)Ljava/lang/reflect/Constructor;
    //   228: iconst_2
    //   229: anewarray 4	java/lang/Object
    //   232: dup
    //   233: iconst_0
    //   234: aload_0
    //   235: getfield 178	org/jivesoftware/smack/XMPPConnection:socket	Ljava/net/Socket;
    //   238: invokevirtual 350	java/net/Socket:getOutputStream	()Ljava/io/OutputStream;
    //   241: aastore
    //   242: dup
    //   243: iconst_1
    //   244: bipush 9
    //   246: invokestatic 404	java/lang/Integer:valueOf	(I)Ljava/lang/Integer;
    //   249: aastore
    //   250: invokevirtual 382	java/lang/reflect/Constructor:newInstance	([Ljava/lang/Object;)Ljava/lang/Object;
    //   253: astore_2
    //   254: aload_1
    //   255: ldc_w 406
    //   258: iconst_1
    //   259: anewarray 366	java/lang/Class
    //   262: dup
    //   263: iconst_0
    //   264: getstatic 400	java/lang/Integer:TYPE	Ljava/lang/Class;
    //   267: aastore
    //   268: invokevirtual 410	java/lang/Class:getMethod	(Ljava/lang/String;[Ljava/lang/Class;)Ljava/lang/reflect/Method;
    //   271: aload_2
    //   272: iconst_1
    //   273: anewarray 4	java/lang/Object
    //   276: dup
    //   277: iconst_0
    //   278: iconst_2
    //   279: invokestatic 404	java/lang/Integer:valueOf	(I)Ljava/lang/Integer;
    //   282: aastore
    //   283: invokevirtual 416	java/lang/reflect/Method:invoke	(Ljava/lang/Object;[Ljava/lang/Object;)Ljava/lang/Object;
    //   286: pop
    //   287: aload_0
    //   288: new 344	java/io/BufferedWriter
    //   291: dup
    //   292: new 346	java/io/OutputStreamWriter
    //   295: dup
    //   296: aload_2
    //   297: checkcast 394	java/io/OutputStream
    //   300: ldc_w 336
    //   303: invokespecial 353	java/io/OutputStreamWriter:<init>	(Ljava/io/OutputStream;Ljava/lang/String;)V
    //   306: invokespecial 356	java/io/BufferedWriter:<init>	(Ljava/io/Writer;)V
    //   309: putfield 312	org/jivesoftware/smack/XMPPConnection:writer	Ljava/io/Writer;
    //   312: ldc_w 418
    //   315: invokestatic 370	java/lang/Class:forName	(Ljava/lang/String;)Ljava/lang/Class;
    //   318: astore_1
    //   319: aload_1
    //   320: iconst_1
    //   321: anewarray 366	java/lang/Class
    //   324: dup
    //   325: iconst_0
    //   326: ldc_w 420
    //   329: aastore
    //   330: invokevirtual 376	java/lang/Class:getConstructor	([Ljava/lang/Class;)Ljava/lang/reflect/Constructor;
    //   333: iconst_1
    //   334: anewarray 4	java/lang/Object
    //   337: dup
    //   338: iconst_0
    //   339: aload_0
    //   340: getfield 178	org/jivesoftware/smack/XMPPConnection:socket	Ljava/net/Socket;
    //   343: invokevirtual 334	java/net/Socket:getInputStream	()Ljava/io/InputStream;
    //   346: aastore
    //   347: invokevirtual 382	java/lang/reflect/Constructor:newInstance	([Ljava/lang/Object;)Ljava/lang/Object;
    //   350: astore_2
    //   351: aload_1
    //   352: ldc_w 406
    //   355: iconst_1
    //   356: anewarray 366	java/lang/Class
    //   359: dup
    //   360: iconst_0
    //   361: getstatic 400	java/lang/Integer:TYPE	Ljava/lang/Class;
    //   364: aastore
    //   365: invokevirtual 410	java/lang/Class:getMethod	(Ljava/lang/String;[Ljava/lang/Class;)Ljava/lang/reflect/Method;
    //   368: aload_2
    //   369: iconst_1
    //   370: anewarray 4	java/lang/Object
    //   373: dup
    //   374: iconst_0
    //   375: iconst_2
    //   376: invokestatic 404	java/lang/Integer:valueOf	(I)Ljava/lang/Integer;
    //   379: aastore
    //   380: invokevirtual 416	java/lang/reflect/Method:invoke	(Ljava/lang/Object;[Ljava/lang/Object;)Ljava/lang/Object;
    //   383: pop
    //   384: aload_0
    //   385: new 328	java/io/BufferedReader
    //   388: dup
    //   389: new 330	java/io/InputStreamReader
    //   392: dup
    //   393: aload_2
    //   394: checkcast 420	java/io/InputStream
    //   397: ldc_w 336
    //   400: invokespecial 339	java/io/InputStreamReader:<init>	(Ljava/io/InputStream;Ljava/lang/String;)V
    //   403: invokespecial 342	java/io/BufferedReader:<init>	(Ljava/io/Reader;)V
    //   406: putfield 305	org/jivesoftware/smack/XMPPConnection:reader	Ljava/io/Reader;
    //   409: goto -346 -> 63
    //   412: astore_1
    //   413: aload_1
    //   414: invokevirtual 423	java/lang/Exception:printStackTrace	()V
    //   417: aload_0
    //   418: new 328	java/io/BufferedReader
    //   421: dup
    //   422: new 330	java/io/InputStreamReader
    //   425: dup
    //   426: aload_0
    //   427: getfield 178	org/jivesoftware/smack/XMPPConnection:socket	Ljava/net/Socket;
    //   430: invokevirtual 334	java/net/Socket:getInputStream	()Ljava/io/InputStream;
    //   433: ldc_w 336
    //   436: invokespecial 339	java/io/InputStreamReader:<init>	(Ljava/io/InputStream;Ljava/lang/String;)V
    //   439: invokespecial 342	java/io/BufferedReader:<init>	(Ljava/io/Reader;)V
    //   442: putfield 305	org/jivesoftware/smack/XMPPConnection:reader	Ljava/io/Reader;
    //   445: aload_0
    //   446: new 344	java/io/BufferedWriter
    //   449: dup
    //   450: new 346	java/io/OutputStreamWriter
    //   453: dup
    //   454: aload_0
    //   455: getfield 178	org/jivesoftware/smack/XMPPConnection:socket	Ljava/net/Socket;
    //   458: invokevirtual 350	java/net/Socket:getOutputStream	()Ljava/io/OutputStream;
    //   461: ldc_w 336
    //   464: invokespecial 353	java/io/OutputStreamWriter:<init>	(Ljava/io/OutputStream;Ljava/lang/String;)V
    //   467: invokespecial 356	java/io/BufferedWriter:<init>	(Ljava/io/Writer;)V
    //   470: putfield 312	org/jivesoftware/smack/XMPPConnection:writer	Ljava/io/Writer;
    //   473: goto -410 -> 63
    //   476: astore_1
    //   477: new 153	org/jivesoftware/smack/XMPPException
    //   480: dup
    //   481: ldc_w 425
    //   484: new 213	org/jivesoftware/smack/packet/XMPPError
    //   487: dup
    //   488: getstatic 230	org/jivesoftware/smack/packet/XMPPError$Condition:remote_server_error	Lorg/jivesoftware/smack/packet/XMPPError$Condition;
    //   491: ldc_w 425
    //   494: invokespecial 222	org/jivesoftware/smack/packet/XMPPError:<init>	(Lorg/jivesoftware/smack/packet/XMPPError$Condition;Ljava/lang/String;)V
    //   497: aload_1
    //   498: invokespecial 225	org/jivesoftware/smack/XMPPException:<init>	(Ljava/lang/String;Lorg/jivesoftware/smack/packet/XMPPError;Ljava/lang/Throwable;)V
    //   501: athrow
    //   502: astore_1
    //   503: aload_1
    //   504: invokevirtual 423	java/lang/Exception:printStackTrace	()V
    //   507: aload_3
    //   508: astore_1
    //   509: goto -405 -> 104
    //   512: astore_2
    //   513: ldc_w 427
    //   516: invokestatic 370	java/lang/Class:forName	(Ljava/lang/String;)Ljava/lang/Class;
    //   519: astore_2
    //   520: goto -403 -> 117
    //   523: astore_2
    //   524: aload_2
    //   525: invokevirtual 423	java/lang/Exception:printStackTrace	()V
    //   528: aload_1
    //   529: astore_2
    //   530: goto -413 -> 117
    //   533: astore_1
    //   534: aload_1
    //   535: invokevirtual 423	java/lang/Exception:printStackTrace	()V
    //   538: iconst_0
    //   539: putstatic 57	org/jivesoftware/smack/XMPPConnection:DEBUG_ENABLED	Z
    //   542: return
    //   543: aload_0
    //   544: aload_0
    //   545: getfield 92	org/jivesoftware/smack/XMPPConnection:debugger	Lorg/jivesoftware/smack/debugger/SmackDebugger;
    //   548: aload_0
    //   549: getfield 305	org/jivesoftware/smack/XMPPConnection:reader	Ljava/io/Reader;
    //   552: invokeinterface 431 2 0
    //   557: putfield 305	org/jivesoftware/smack/XMPPConnection:reader	Ljava/io/Reader;
    //   560: aload_0
    //   561: aload_0
    //   562: getfield 92	org/jivesoftware/smack/XMPPConnection:debugger	Lorg/jivesoftware/smack/debugger/SmackDebugger;
    //   565: aload_0
    //   566: getfield 312	org/jivesoftware/smack/XMPPConnection:writer	Ljava/io/Writer;
    //   569: invokeinterface 435 2 0
    //   574: putfield 312	org/jivesoftware/smack/XMPPConnection:writer	Ljava/io/Writer;
    //   577: return
    //   578: astore_1
    //   579: goto -488 -> 91
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	582	0	this	XMPPConnection
    //   88	264	1	localObject1	Object
    //   412	2	1	localException1	Exception
    //   476	22	1	localIOException	IOException
    //   502	2	1	localException2	Exception
    //   508	21	1	localObject2	Object
    //   533	2	1	localException3	Exception
    //   578	1	1	localThrowable	Throwable
    //   81	313	2	localObject3	Object
    //   512	1	2	localException4	Exception
    //   519	1	2	localClass	java.lang.Class
    //   523	2	2	localException5	Exception
    //   529	1	2	localObject4	Object
    //   92	416	3	localObject5	Object
    // Exception table:
    //   from	to	target	type
    //   201	409	412	java/lang/Exception
    //   0	63	476	java/io/IOException
    //   201	409	476	java/io/IOException
    //   413	473	476	java/io/IOException
    //   99	104	502	java/lang/Exception
    //   110	117	512	java/lang/Exception
    //   513	520	523	java/lang/Exception
    //   117	200	533	java/lang/Exception
    //   82	89	578	java/lang/Throwable
  }
  
  public static void removeConnectionCreationListener(ConnectionCreationListener paramConnectionCreationListener)
  {
    connectionEstablishedListeners.remove(paramConnectionCreationListener);
  }
  
  private void requestStreamCompression()
  {
    try
    {
      this.writer.write("<compress xmlns='http://jabber.org/protocol/compress'>");
      this.writer.write("<method>zlib</method></compress>");
      this.writer.flush();
      return;
    }
    catch (IOException localIOException)
    {
      this.packetReader.notifyConnectionError(localIOException);
    }
  }
  
  private void setWasAuthenticated(boolean paramBoolean)
  {
    if (!this.wasAuthenticated) {
      this.wasAuthenticated = paramBoolean;
    }
  }
  
  /* Error */
  private boolean useCompression()
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 106	org/jivesoftware/smack/XMPPConnection:authenticated	Z
    //   4: ifeq +14 -> 18
    //   7: new 461	java/lang/IllegalStateException
    //   10: dup
    //   11: ldc_w 463
    //   14: invokespecial 464	java/lang/IllegalStateException:<init>	(Ljava/lang/String;)V
    //   17: athrow
    //   18: ldc_w 392
    //   21: invokestatic 370	java/lang/Class:forName	(Ljava/lang/String;)Ljava/lang/Class;
    //   24: pop
    //   25: aload_0
    //   26: ldc_w 466
    //   29: invokespecial 468	org/jivesoftware/smack/XMPPConnection:hasAvailableCompressionMethod	(Ljava/lang/String;)Z
    //   32: ifeq +43 -> 75
    //   35: aload_0
    //   36: invokespecial 470	org/jivesoftware/smack/XMPPConnection:requestStreamCompression	()V
    //   39: aload_0
    //   40: monitorenter
    //   41: aload_0
    //   42: invokestatic 473	org/jivesoftware/smack/SmackConfiguration:getPacketReplyTimeout	()I
    //   45: iconst_5
    //   46: imul
    //   47: i2l
    //   48: invokevirtual 477	java/lang/Object:wait	(J)V
    //   51: aload_0
    //   52: monitorexit
    //   53: aload_0
    //   54: getfield 247	org/jivesoftware/smack/XMPPConnection:usingCompression	Z
    //   57: ireturn
    //   58: astore_1
    //   59: new 461	java/lang/IllegalStateException
    //   62: dup
    //   63: ldc_w 479
    //   66: invokespecial 464	java/lang/IllegalStateException:<init>	(Ljava/lang/String;)V
    //   69: athrow
    //   70: astore_1
    //   71: aload_0
    //   72: monitorexit
    //   73: aload_1
    //   74: athrow
    //   75: iconst_0
    //   76: ireturn
    //   77: astore_1
    //   78: goto -27 -> 51
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	81	0	this	XMPPConnection
    //   58	1	1	localClassNotFoundException	java.lang.ClassNotFoundException
    //   70	4	1	localObject	Object
    //   77	1	1	localInterruptedException	java.lang.InterruptedException
    // Exception table:
    //   from	to	target	type
    //   18	25	58	java/lang/ClassNotFoundException
    //   41	51	70	finally
    //   51	53	70	finally
    //   71	73	70	finally
    //   41	51	77	java/lang/InterruptedException
  }
  
  public void addConnectionListener(ConnectionListener paramConnectionListener)
  {
    if (!isConnected()) {
      throw new IllegalStateException("Not connected to server.");
    }
    if (paramConnectionListener == null) {}
    while (this.packetReader.connectionListeners.contains(paramConnectionListener)) {
      return;
    }
    this.packetReader.connectionListeners.add(paramConnectionListener);
  }
  
  public void addPacketListener(PacketListener paramPacketListener, PacketFilter paramPacketFilter)
  {
    if (!isConnected()) {
      throw new IllegalStateException("Not connected to server.");
    }
    this.packetReader.addPacketListener(paramPacketListener, paramPacketFilter);
  }
  
  public void addPacketWriterInterceptor(PacketInterceptor paramPacketInterceptor, PacketFilter paramPacketFilter)
  {
    if (!isConnected()) {
      throw new IllegalStateException("Not connected to server.");
    }
    this.packetWriter.addPacketInterceptor(paramPacketInterceptor, paramPacketFilter);
  }
  
  public void addPacketWriterListener(PacketListener paramPacketListener, PacketFilter paramPacketFilter)
  {
    if (!isConnected()) {
      throw new IllegalStateException("Not connected to server.");
    }
    this.packetWriter.addPacketListener(paramPacketListener, paramPacketFilter);
  }
  
  public void connect()
    throws XMPPException
  {
    connectUsingConfiguration(this.configuration);
    if ((this.connected) && (this.wasAuthenticated)) {
      try
      {
        if (isAnonymous())
        {
          loginAnonymously();
          return;
        }
        login(getConfiguration().getUsername(), getConfiguration().getPassword(), getConfiguration().getResource());
        return;
      }
      catch (XMPPException localXMPPException)
      {
        localXMPPException.printStackTrace();
      }
    }
  }
  
  public PacketCollector createPacketCollector(PacketFilter paramPacketFilter)
  {
    return this.packetReader.createPacketCollector(paramPacketFilter);
  }
  
  public void disconnect()
  {
    disconnect(new Presence(Presence.Type.unavailable));
  }
  
  public void disconnect(Presence paramPresence)
  {
    if ((this.packetReader == null) || (this.packetWriter == null)) {
      return;
    }
    shutdown(paramPresence);
    if (this.roster != null)
    {
      this.roster.cleanup();
      this.roster = null;
    }
    this.wasAuthenticated = false;
    this.packetWriter.cleanup();
    this.packetWriter = null;
    this.packetReader.cleanup();
    this.packetReader = null;
  }
  
  public AccountManager getAccountManager()
  {
    try
    {
      if (this.accountManager == null) {
        this.accountManager = new AccountManager(this);
      }
      AccountManager localAccountManager = this.accountManager;
      return localAccountManager;
    }
    finally {}
  }
  
  public ChatManager getChatManager()
  {
    try
    {
      if (this.chatManager == null) {
        this.chatManager = new ChatManager(this);
      }
      ChatManager localChatManager = this.chatManager;
      return localChatManager;
    }
    finally {}
  }
  
  protected ConnectionConfiguration getConfiguration()
  {
    return this.configuration;
  }
  
  public String getConnectionID()
  {
    if (!isConnected()) {
      return null;
    }
    return this.connectionID;
  }
  
  public String getHost()
  {
    return this.host;
  }
  
  public int getPort()
  {
    return this.port;
  }
  
  /* Error */
  public Roster getRoster()
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 139	org/jivesoftware/smack/XMPPConnection:configuration	Lorg/jivesoftware/smack/ConnectionConfiguration;
    //   4: invokevirtual 569	org/jivesoftware/smack/ConnectionConfiguration:isRosterLoadedAtLogin	()Z
    //   7: ifne +10 -> 17
    //   10: aload_0
    //   11: getfield 114	org/jivesoftware/smack/XMPPConnection:roster	Lorg/jivesoftware/smack/Roster;
    //   14: invokevirtual 572	org/jivesoftware/smack/Roster:reload	()V
    //   17: aload_0
    //   18: getfield 114	org/jivesoftware/smack/XMPPConnection:roster	Lorg/jivesoftware/smack/Roster;
    //   21: ifnonnull +5 -> 26
    //   24: aconst_null
    //   25: areturn
    //   26: aload_0
    //   27: getfield 114	org/jivesoftware/smack/XMPPConnection:roster	Lorg/jivesoftware/smack/Roster;
    //   30: getfield 575	org/jivesoftware/smack/Roster:rosterInitialized	Z
    //   33: ifne +40 -> 73
    //   36: aload_0
    //   37: getfield 114	org/jivesoftware/smack/XMPPConnection:roster	Lorg/jivesoftware/smack/Roster;
    //   40: astore 7
    //   42: aload 7
    //   44: monitorenter
    //   45: invokestatic 473	org/jivesoftware/smack/SmackConfiguration:getPacketReplyTimeout	()I
    //   48: i2l
    //   49: lstore_3
    //   50: invokestatic 579	java/lang/System:currentTimeMillis	()J
    //   53: lstore_1
    //   54: aload_0
    //   55: getfield 114	org/jivesoftware/smack/XMPPConnection:roster	Lorg/jivesoftware/smack/Roster;
    //   58: getfield 575	org/jivesoftware/smack/Roster:rosterInitialized	Z
    //   61: ifne +9 -> 70
    //   64: lload_3
    //   65: lconst_0
    //   66: lcmp
    //   67: ifgt +11 -> 78
    //   70: aload 7
    //   72: monitorexit
    //   73: aload_0
    //   74: getfield 114	org/jivesoftware/smack/XMPPConnection:roster	Lorg/jivesoftware/smack/Roster;
    //   77: areturn
    //   78: aload_0
    //   79: getfield 114	org/jivesoftware/smack/XMPPConnection:roster	Lorg/jivesoftware/smack/Roster;
    //   82: lload_3
    //   83: invokevirtual 477	java/lang/Object:wait	(J)V
    //   86: invokestatic 579	java/lang/System:currentTimeMillis	()J
    //   89: lstore 5
    //   91: lload_3
    //   92: lload 5
    //   94: lload_1
    //   95: lsub
    //   96: lsub
    //   97: lstore_3
    //   98: lload 5
    //   100: lstore_1
    //   101: goto -47 -> 54
    //   104: astore 8
    //   106: aload 7
    //   108: monitorexit
    //   109: aload 8
    //   111: athrow
    //   112: astore 7
    //   114: goto -41 -> 73
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	117	0	this	XMPPConnection
    //   53	48	1	l1	long
    //   49	49	3	l2	long
    //   89	10	5	l3	long
    //   112	1	7	localInterruptedException	java.lang.InterruptedException
    //   104	6	8	localObject	Object
    // Exception table:
    //   from	to	target	type
    //   45	54	104	finally
    //   54	64	104	finally
    //   70	73	104	finally
    //   78	91	104	finally
    //   106	109	104	finally
    //   36	45	112	java/lang/InterruptedException
    //   109	112	112	java/lang/InterruptedException
  }
  
  public SASLAuthentication getSASLAuthentication()
  {
    return this.saslAuthentication;
  }
  
  public String getServiceName()
  {
    return this.serviceName;
  }
  
  public String getUser()
  {
    if (!isAuthenticated()) {
      return null;
    }
    return this.user;
  }
  
  public boolean isAnonymous()
  {
    return this.anonymous;
  }
  
  public boolean isAuthenticated()
  {
    return this.authenticated;
  }
  
  public boolean isConnected()
  {
    return this.connected;
  }
  
  public boolean isSecureConnection()
  {
    return isUsingTLS();
  }
  
  public boolean isUsingCompression()
  {
    return this.usingCompression;
  }
  
  public boolean isUsingTLS()
  {
    return this.usingTLS;
  }
  
  public void login(String paramString1, String paramString2)
    throws XMPPException
  {
    login(paramString1, paramString2, "Smack");
  }
  
  public void login(String paramString1, String paramString2, String paramString3)
    throws XMPPException
  {
    try
    {
      if (!isConnected()) {
        throw new IllegalStateException("Not connected to server.");
      }
    }
    finally {}
    if (this.authenticated) {
      throw new IllegalStateException("Already logged in to server.");
    }
    String str = paramString1.toLowerCase().trim();
    if ((this.configuration.isSASLAuthenticationEnabled()) && (this.saslAuthentication.hasNonAnonymousAuthentication())) {
      if (paramString2 != null)
      {
        paramString1 = this.saslAuthentication.authenticate(str, paramString2, paramString3);
        if (paramString1 == null) {
          break label277;
        }
        this.user = paramString1;
        this.serviceName = StringUtils.parseServer(paramString1);
      }
    }
    for (;;)
    {
      if (this.configuration.isCompressionEnabled()) {
        useCompression();
      }
      if (this.roster == null) {
        this.roster = new Roster(this);
      }
      if (this.configuration.isRosterLoadedAtLogin()) {
        this.roster.reload();
      }
      if (this.configuration.isSendPresence()) {
        this.packetWriter.sendPacket(new Presence(Presence.Type.available));
      }
      this.authenticated = true;
      this.anonymous = false;
      getConfiguration().setLoginInfo(str, paramString2, paramString3);
      if ((this.configuration.isDebuggerEnabled()) && (this.debugger != null)) {
        this.debugger.userHasLogged(this.user);
      }
      return;
      paramString1 = this.saslAuthentication.authenticate(str, paramString3, this.configuration.getCallbackHandler());
      break;
      paramString1 = new NonSASLAuthentication(this).authenticate(str, paramString2, paramString3);
      break;
      label277:
      this.user = (str + "@" + this.serviceName);
      if (paramString3 != null) {
        this.user = (this.user + "/" + paramString3);
      }
    }
  }
  
  public void loginAnonymously()
    throws XMPPException
  {
    try
    {
      if (!isConnected()) {
        throw new IllegalStateException("Not connected to server.");
      }
    }
    finally {}
    if (this.authenticated) {
      throw new IllegalStateException("Already logged in to server.");
    }
    if ((this.configuration.isSASLAuthenticationEnabled()) && (this.saslAuthentication.hasAnonymousAuthentication())) {}
    for (String str = this.saslAuthentication.authenticateAnonymously();; str = new NonSASLAuthentication(this).authenticateAnonymously())
    {
      this.user = str;
      this.serviceName = StringUtils.parseServer(str);
      if (this.configuration.isCompressionEnabled()) {
        useCompression();
      }
      this.roster = null;
      this.packetWriter.sendPacket(new Presence(Presence.Type.available));
      this.authenticated = true;
      this.anonymous = true;
      if ((this.configuration.isDebuggerEnabled()) && (this.debugger != null)) {
        this.debugger.userHasLogged(this.user);
      }
      return;
    }
  }
  
  /* Error */
  void proceedTLSReceived()
    throws Exception
  {
    // Byte code:
    //   0: ldc_w 667
    //   3: invokestatic 673	javax/net/ssl/SSLContext:getInstance	(Ljava/lang/String;)Ljavax/net/ssl/SSLContext;
    //   6: astore_3
    //   7: aconst_null
    //   8: astore_1
    //   9: aconst_null
    //   10: astore_2
    //   11: aload_0
    //   12: getfield 90	org/jivesoftware/smack/XMPPConnection:callbackHandler	Ljavax/security/auth/callback/CallbackHandler;
    //   15: ifnonnull +126 -> 141
    //   18: new 675	org/jivesoftware/smack/ServerTrustManager
    //   21: dup
    //   22: aload_0
    //   23: getfield 183	org/jivesoftware/smack/XMPPConnection:serviceName	Ljava/lang/String;
    //   26: aload_0
    //   27: getfield 139	org/jivesoftware/smack/XMPPConnection:configuration	Lorg/jivesoftware/smack/ConnectionConfiguration;
    //   30: invokespecial 678	org/jivesoftware/smack/ServerTrustManager:<init>	(Ljava/lang/String;Lorg/jivesoftware/smack/ConnectionConfiguration;)V
    //   33: astore_2
    //   34: new 680	java/security/SecureRandom
    //   37: dup
    //   38: invokespecial 681	java/security/SecureRandom:<init>	()V
    //   41: astore 4
    //   43: aload_3
    //   44: aload_1
    //   45: iconst_1
    //   46: anewarray 683	javax/net/ssl/TrustManager
    //   49: dup
    //   50: iconst_0
    //   51: aload_2
    //   52: aastore
    //   53: aload 4
    //   55: invokevirtual 686	javax/net/ssl/SSLContext:init	([Ljavax/net/ssl/KeyManager;[Ljavax/net/ssl/TrustManager;Ljava/security/SecureRandom;)V
    //   58: aload_0
    //   59: getfield 178	org/jivesoftware/smack/XMPPConnection:socket	Ljava/net/Socket;
    //   62: astore_1
    //   63: aload_0
    //   64: aload_3
    //   65: invokevirtual 689	javax/net/ssl/SSLContext:getSocketFactory	()Ljavax/net/ssl/SSLSocketFactory;
    //   68: aload_1
    //   69: aload_1
    //   70: invokevirtual 693	java/net/Socket:getInetAddress	()Ljava/net/InetAddress;
    //   73: invokevirtual 698	java/net/InetAddress:getHostName	()Ljava/lang/String;
    //   76: aload_1
    //   77: invokevirtual 699	java/net/Socket:getPort	()I
    //   80: iconst_1
    //   81: invokevirtual 704	javax/net/ssl/SSLSocketFactory:createSocket	(Ljava/net/Socket;Ljava/lang/String;IZ)Ljava/net/Socket;
    //   84: putfield 178	org/jivesoftware/smack/XMPPConnection:socket	Ljava/net/Socket;
    //   87: aload_0
    //   88: getfield 178	org/jivesoftware/smack/XMPPConnection:socket	Ljava/net/Socket;
    //   91: iconst_0
    //   92: invokevirtual 707	java/net/Socket:setSoTimeout	(I)V
    //   95: aload_0
    //   96: getfield 178	org/jivesoftware/smack/XMPPConnection:socket	Ljava/net/Socket;
    //   99: iconst_1
    //   100: invokevirtual 710	java/net/Socket:setKeepAlive	(Z)V
    //   103: aload_0
    //   104: invokespecial 250	org/jivesoftware/smack/XMPPConnection:initReaderAndWriter	()V
    //   107: aload_0
    //   108: getfield 178	org/jivesoftware/smack/XMPPConnection:socket	Ljava/net/Socket;
    //   111: checkcast 712	javax/net/ssl/SSLSocket
    //   114: invokevirtual 715	javax/net/ssl/SSLSocket:startHandshake	()V
    //   117: aload_0
    //   118: iconst_1
    //   119: putfield 112	org/jivesoftware/smack/XMPPConnection:usingTLS	Z
    //   122: aload_0
    //   123: getfield 245	org/jivesoftware/smack/XMPPConnection:packetWriter	Lorg/jivesoftware/smack/PacketWriter;
    //   126: aload_0
    //   127: getfield 312	org/jivesoftware/smack/XMPPConnection:writer	Ljava/io/Writer;
    //   130: invokevirtual 718	org/jivesoftware/smack/PacketWriter:setWriter	(Ljava/io/Writer;)V
    //   133: aload_0
    //   134: getfield 245	org/jivesoftware/smack/XMPPConnection:packetWriter	Lorg/jivesoftware/smack/PacketWriter;
    //   137: invokevirtual 721	org/jivesoftware/smack/PacketWriter:openStream	()V
    //   140: return
    //   141: aload_0
    //   142: getfield 139	org/jivesoftware/smack/XMPPConnection:configuration	Lorg/jivesoftware/smack/ConnectionConfiguration;
    //   145: invokevirtual 724	org/jivesoftware/smack/ConnectionConfiguration:getKeystoreType	()Ljava/lang/String;
    //   148: ldc_w 726
    //   151: invokevirtual 729	java/lang/String:equals	(Ljava/lang/Object;)Z
    //   154: ifeq +35 -> 189
    //   157: aconst_null
    //   158: astore_1
    //   159: aconst_null
    //   160: astore_2
    //   161: ldc_w 731
    //   164: invokestatic 736	javax/net/ssl/KeyManagerFactory:getInstance	(Ljava/lang/String;)Ljavax/net/ssl/KeyManagerFactory;
    //   167: astore 4
    //   169: aload_2
    //   170: ifnonnull +243 -> 413
    //   173: aload 4
    //   175: aload_1
    //   176: aconst_null
    //   177: invokevirtual 739	javax/net/ssl/KeyManagerFactory:init	(Ljava/security/KeyStore;[C)V
    //   180: aload 4
    //   182: invokevirtual 743	javax/net/ssl/KeyManagerFactory:getKeyManagers	()[Ljavax/net/ssl/KeyManager;
    //   185: astore_1
    //   186: goto -168 -> 18
    //   189: aload_0
    //   190: getfield 139	org/jivesoftware/smack/XMPPConnection:configuration	Lorg/jivesoftware/smack/ConnectionConfiguration;
    //   193: invokevirtual 724	org/jivesoftware/smack/ConnectionConfiguration:getKeystoreType	()Ljava/lang/String;
    //   196: ldc_w 745
    //   199: invokevirtual 729	java/lang/String:equals	(Ljava/lang/Object;)Z
    //   202: ifeq +134 -> 336
    //   205: ldc_w 747
    //   208: invokestatic 370	java/lang/Class:forName	(Ljava/lang/String;)Ljava/lang/Class;
    //   211: iconst_1
    //   212: anewarray 366	java/lang/Class
    //   215: dup
    //   216: iconst_0
    //   217: ldc_w 420
    //   220: aastore
    //   221: invokevirtual 376	java/lang/Class:getConstructor	([Ljava/lang/Class;)Ljava/lang/reflect/Constructor;
    //   224: iconst_1
    //   225: anewarray 4	java/lang/Object
    //   228: dup
    //   229: iconst_0
    //   230: new 749	java/io/ByteArrayInputStream
    //   233: dup
    //   234: new 194	java/lang/StringBuilder
    //   237: dup
    //   238: invokespecial 195	java/lang/StringBuilder:<init>	()V
    //   241: ldc_w 751
    //   244: invokevirtual 201	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   247: aload_0
    //   248: getfield 139	org/jivesoftware/smack/XMPPConnection:configuration	Lorg/jivesoftware/smack/ConnectionConfiguration;
    //   251: invokevirtual 754	org/jivesoftware/smack/ConnectionConfiguration:getPKCS11Library	()Ljava/lang/String;
    //   254: invokevirtual 201	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   257: invokevirtual 211	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   260: invokevirtual 758	java/lang/String:getBytes	()[B
    //   263: invokespecial 761	java/io/ByteArrayInputStream:<init>	([B)V
    //   266: aastore
    //   267: invokevirtual 382	java/lang/reflect/Constructor:newInstance	([Ljava/lang/Object;)Ljava/lang/Object;
    //   270: checkcast 763	java/security/Provider
    //   273: astore_1
    //   274: aload_1
    //   275: invokestatic 769	java/security/Security:addProvider	(Ljava/security/Provider;)I
    //   278: pop
    //   279: ldc_w 745
    //   282: aload_1
    //   283: invokestatic 774	java/security/KeyStore:getInstance	(Ljava/lang/String;Ljava/security/Provider;)Ljava/security/KeyStore;
    //   286: astore_1
    //   287: new 776	javax/security/auth/callback/PasswordCallback
    //   290: dup
    //   291: ldc_w 778
    //   294: iconst_0
    //   295: invokespecial 781	javax/security/auth/callback/PasswordCallback:<init>	(Ljava/lang/String;Z)V
    //   298: astore_2
    //   299: aload_0
    //   300: getfield 90	org/jivesoftware/smack/XMPPConnection:callbackHandler	Ljavax/security/auth/callback/CallbackHandler;
    //   303: iconst_1
    //   304: anewarray 783	javax/security/auth/callback/Callback
    //   307: dup
    //   308: iconst_0
    //   309: aload_2
    //   310: aastore
    //   311: invokeinterface 789 2 0
    //   316: aload_1
    //   317: aconst_null
    //   318: aload_2
    //   319: invokevirtual 792	javax/security/auth/callback/PasswordCallback:getPassword	()[C
    //   322: invokevirtual 796	java/security/KeyStore:load	(Ljava/io/InputStream;[C)V
    //   325: goto -164 -> 161
    //   328: astore_1
    //   329: aconst_null
    //   330: astore_1
    //   331: aconst_null
    //   332: astore_2
    //   333: goto -172 -> 161
    //   336: aload_0
    //   337: getfield 139	org/jivesoftware/smack/XMPPConnection:configuration	Lorg/jivesoftware/smack/ConnectionConfiguration;
    //   340: invokevirtual 724	org/jivesoftware/smack/ConnectionConfiguration:getKeystoreType	()Ljava/lang/String;
    //   343: ldc_w 798
    //   346: invokevirtual 729	java/lang/String:equals	(Ljava/lang/Object;)Z
    //   349: ifeq +22 -> 371
    //   352: ldc_w 800
    //   355: ldc_w 798
    //   358: invokestatic 803	java/security/KeyStore:getInstance	(Ljava/lang/String;Ljava/lang/String;)Ljava/security/KeyStore;
    //   361: astore_1
    //   362: aload_1
    //   363: aconst_null
    //   364: aconst_null
    //   365: invokevirtual 796	java/security/KeyStore:load	(Ljava/io/InputStream;[C)V
    //   368: goto -207 -> 161
    //   371: aload_0
    //   372: getfield 139	org/jivesoftware/smack/XMPPConnection:configuration	Lorg/jivesoftware/smack/ConnectionConfiguration;
    //   375: invokevirtual 724	org/jivesoftware/smack/ConnectionConfiguration:getKeystoreType	()Ljava/lang/String;
    //   378: invokestatic 806	java/security/KeyStore:getInstance	(Ljava/lang/String;)Ljava/security/KeyStore;
    //   381: astore_1
    //   382: new 808	java/io/FileInputStream
    //   385: dup
    //   386: aload_0
    //   387: getfield 139	org/jivesoftware/smack/XMPPConnection:configuration	Lorg/jivesoftware/smack/ConnectionConfiguration;
    //   390: invokevirtual 811	org/jivesoftware/smack/ConnectionConfiguration:getKeystorePath	()Ljava/lang/String;
    //   393: invokespecial 812	java/io/FileInputStream:<init>	(Ljava/lang/String;)V
    //   396: pop
    //   397: new 665	java/lang/NullPointerException
    //   400: dup
    //   401: invokespecial 813	java/lang/NullPointerException:<init>	()V
    //   404: athrow
    //   405: astore_1
    //   406: aconst_null
    //   407: astore_1
    //   408: aconst_null
    //   409: astore_2
    //   410: goto -249 -> 161
    //   413: aload 4
    //   415: aload_1
    //   416: aload_2
    //   417: invokevirtual 792	javax/security/auth/callback/PasswordCallback:getPassword	()[C
    //   420: invokevirtual 739	javax/net/ssl/KeyManagerFactory:init	(Ljava/security/KeyStore;[C)V
    //   423: aload_2
    //   424: invokevirtual 816	javax/security/auth/callback/PasswordCallback:clearPassword	()V
    //   427: goto -247 -> 180
    //   430: astore_1
    //   431: aconst_null
    //   432: astore_1
    //   433: goto -415 -> 18
    //   436: astore_1
    //   437: goto -108 -> 329
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	440	0	this	XMPPConnection
    //   8	309	1	localObject1	Object
    //   328	1	1	localException1	Exception
    //   330	52	1	localKeyStore1	java.security.KeyStore
    //   405	1	1	localException2	Exception
    //   407	9	1	localKeyStore2	java.security.KeyStore
    //   430	1	1	localNullPointerException	NullPointerException
    //   432	1	1	localObject2	Object
    //   436	1	1	localException3	Exception
    //   10	414	2	localObject3	Object
    //   6	59	3	localSSLContext	javax.net.ssl.SSLContext
    //   41	373	4	localObject4	Object
    // Exception table:
    //   from	to	target	type
    //   205	299	328	java/lang/Exception
    //   382	405	405	java/lang/Exception
    //   173	180	430	java/lang/NullPointerException
    //   180	186	430	java/lang/NullPointerException
    //   413	427	430	java/lang/NullPointerException
    //   299	325	436	java/lang/Exception
  }
  
  public void removeConnectionListener(ConnectionListener paramConnectionListener)
  {
    if (this.packetReader != null) {
      this.packetReader.connectionListeners.remove(paramConnectionListener);
    }
  }
  
  public void removePacketListener(PacketListener paramPacketListener)
  {
    if (this.packetReader != null) {
      this.packetReader.removePacketListener(paramPacketListener);
    }
  }
  
  public void removePacketWriterInterceptor(PacketInterceptor paramPacketInterceptor)
  {
    this.packetWriter.removePacketInterceptor(paramPacketInterceptor);
  }
  
  public void removePacketWriterListener(PacketListener paramPacketListener)
  {
    if (this.packetWriter != null) {
      this.packetWriter.removePacketListener(paramPacketListener);
    }
  }
  
  public void sendPacket(Packet paramPacket)
  {
    if (!isConnected()) {
      throw new IllegalStateException("Not connected to server.");
    }
    if (paramPacket == null) {
      throw new NullPointerException("Packet is null.");
    }
    this.packetWriter.sendPacket(paramPacket);
  }
  
  void setAvailableCompressionMethods(Collection paramCollection)
  {
    this.compressionMethods = paramCollection;
  }
  
  protected void shutdown(Presence paramPresence)
  {
    this.packetWriter.sendPacket(paramPresence);
    setWasAuthenticated(this.authenticated);
    this.authenticated = false;
    this.connected = false;
    this.packetReader.shutdown();
    this.packetWriter.shutdown();
    try
    {
      Thread.sleep(150L);
      if (this.reader == null) {}
    }
    catch (Exception paramPresence)
    {
      try
      {
        for (;;)
        {
          this.reader.close();
          label60:
          this.reader = null;
          if (this.writer != null) {}
          try
          {
            this.writer.close();
            label79:
            this.writer = null;
            try
            {
              this.socket.close();
              label91:
              this.saslAuthentication.init();
              return;
              paramPresence = paramPresence;
            }
            catch (Exception paramPresence)
            {
              break label91;
            }
          }
          catch (Throwable paramPresence)
          {
            break label79;
          }
        }
      }
      catch (Throwable paramPresence)
      {
        break label60;
      }
    }
  }
  
  void startStreamCompression()
    throws Exception
  {
    this.usingCompression = true;
    initReaderAndWriter();
    this.packetWriter.setWriter(this.writer);
    this.packetWriter.openStream();
    try
    {
      notify();
      return;
    }
    finally {}
  }
  
  void startTLSReceived(boolean paramBoolean)
  {
    if ((paramBoolean) && (this.configuration.getSecurityMode() == ConnectionConfiguration.SecurityMode.disabled)) {
      this.packetReader.notifyConnectionError(new IllegalStateException("TLS required by server but not allowed by connection configuration"));
    }
    while (this.configuration.getSecurityMode() == ConnectionConfiguration.SecurityMode.disabled) {
      return;
    }
    try
    {
      this.writer.write("<starttls xmlns=\"urn:ietf:params:xml:ns:xmpp-tls\"/>");
      this.writer.flush();
      return;
    }
    catch (IOException localIOException)
    {
      this.packetReader.notifyConnectionError(localIOException);
    }
  }
  
  void streamCompressionDenied()
  {
    try
    {
      notify();
      return;
    }
    finally {}
  }
}


/* Location:           C:\Users\Administrator\Documents\ds\反编译\AndroidKiller_v1.3.1\projects\Jungledsq_ppc\ProjectSrc\smali\
 * Qualified Name:     org.jivesoftware.smack.XMPPConnection
 * JD-Core Version:    0.7.0.1
 */