package org.jivesoftware.smack;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.jivesoftware.smack.sasl.SASLAnonymous;
import org.jivesoftware.smack.sasl.SASLCramMD5Mechanism;
import org.jivesoftware.smack.sasl.SASLDigestMD5Mechanism;
import org.jivesoftware.smack.sasl.SASLExternalMechanism;
import org.jivesoftware.smack.sasl.SASLGSSAPIMechanism;
import org.jivesoftware.smack.sasl.SASLMechanism;
import org.jivesoftware.smack.sasl.SASLPlainMechanism;

public class SASLAuthentication
  implements UserAuthentication
{
  private static Map<String, Class> implementedMechanisms = new HashMap();
  private static List<String> mechanismsPreferences = new ArrayList();
  private XMPPConnection connection;
  private SASLMechanism currentMechanism = null;
  private boolean resourceBinded;
  private boolean saslFailed;
  private boolean saslNegotiated;
  private Collection<String> serverMechanisms = new ArrayList();
  private boolean sessionSupported;
  
  static
  {
    registerSASLMechanism("EXTERNAL", SASLExternalMechanism.class);
    registerSASLMechanism("GSSAPI", SASLGSSAPIMechanism.class);
    registerSASLMechanism("DIGEST-MD5", SASLDigestMD5Mechanism.class);
    registerSASLMechanism("CRAM-MD5", SASLCramMD5Mechanism.class);
    registerSASLMechanism("PLAIN", SASLPlainMechanism.class);
    registerSASLMechanism("ANONYMOUS", SASLAnonymous.class);
    supportSASLMechanism("GSSAPI", 0);
    supportSASLMechanism("DIGEST-MD5", 1);
    supportSASLMechanism("CRAM-MD5", 2);
    supportSASLMechanism("PLAIN", 3);
    supportSASLMechanism("ANONYMOUS", 4);
  }
  
  SASLAuthentication(XMPPConnection paramXMPPConnection)
  {
    this.connection = paramXMPPConnection;
    init();
  }
  
  /* Error */
  private String bindResourceAndEstablishSession(String paramString)
    throws XMPPException
  {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield 90	org/jivesoftware/smack/SASLAuthentication:resourceBinded	Z
    //   6: istore_2
    //   7: iload_2
    //   8: ifne +10 -> 18
    //   11: aload_0
    //   12: ldc2_w 91
    //   15: invokevirtual 96	java/lang/Object:wait	(J)V
    //   18: aload_0
    //   19: monitorexit
    //   20: aload_0
    //   21: getfield 90	org/jivesoftware/smack/SASLAuthentication:resourceBinded	Z
    //   24: ifne +18 -> 42
    //   27: new 86	org/jivesoftware/smack/XMPPException
    //   30: dup
    //   31: ldc 98
    //   33: invokespecial 101	org/jivesoftware/smack/XMPPException:<init>	(Ljava/lang/String;)V
    //   36: athrow
    //   37: astore_1
    //   38: aload_0
    //   39: monitorexit
    //   40: aload_1
    //   41: athrow
    //   42: new 103	org/jivesoftware/smack/packet/Bind
    //   45: dup
    //   46: invokespecial 104	org/jivesoftware/smack/packet/Bind:<init>	()V
    //   49: astore_3
    //   50: aload_3
    //   51: aload_1
    //   52: invokevirtual 107	org/jivesoftware/smack/packet/Bind:setResource	(Ljava/lang/String;)V
    //   55: aload_0
    //   56: getfield 79	org/jivesoftware/smack/SASLAuthentication:connection	Lorg/jivesoftware/smack/XMPPConnection;
    //   59: new 109	org/jivesoftware/smack/filter/PacketIDFilter
    //   62: dup
    //   63: aload_3
    //   64: invokevirtual 113	org/jivesoftware/smack/packet/Bind:getPacketID	()Ljava/lang/String;
    //   67: invokespecial 114	org/jivesoftware/smack/filter/PacketIDFilter:<init>	(Ljava/lang/String;)V
    //   70: invokevirtual 120	org/jivesoftware/smack/XMPPConnection:createPacketCollector	(Lorg/jivesoftware/smack/filter/PacketFilter;)Lorg/jivesoftware/smack/PacketCollector;
    //   73: astore_1
    //   74: aload_0
    //   75: getfield 79	org/jivesoftware/smack/SASLAuthentication:connection	Lorg/jivesoftware/smack/XMPPConnection;
    //   78: aload_3
    //   79: invokevirtual 124	org/jivesoftware/smack/XMPPConnection:sendPacket	(Lorg/jivesoftware/smack/packet/Packet;)V
    //   82: aload_1
    //   83: invokestatic 130	org/jivesoftware/smack/SmackConfiguration:getPacketReplyTimeout	()I
    //   86: i2l
    //   87: invokevirtual 136	org/jivesoftware/smack/PacketCollector:nextResult	(J)Lorg/jivesoftware/smack/packet/Packet;
    //   90: checkcast 103	org/jivesoftware/smack/packet/Bind
    //   93: astore_3
    //   94: aload_1
    //   95: invokevirtual 139	org/jivesoftware/smack/PacketCollector:cancel	()V
    //   98: aload_3
    //   99: ifnonnull +13 -> 112
    //   102: new 86	org/jivesoftware/smack/XMPPException
    //   105: dup
    //   106: ldc 141
    //   108: invokespecial 101	org/jivesoftware/smack/XMPPException:<init>	(Ljava/lang/String;)V
    //   111: athrow
    //   112: aload_3
    //   113: invokevirtual 145	org/jivesoftware/smack/packet/Bind:getType	()Lorg/jivesoftware/smack/packet/IQ$Type;
    //   116: getstatic 151	org/jivesoftware/smack/packet/IQ$Type:ERROR	Lorg/jivesoftware/smack/packet/IQ$Type;
    //   119: if_acmpne +15 -> 134
    //   122: new 86	org/jivesoftware/smack/XMPPException
    //   125: dup
    //   126: aload_3
    //   127: invokevirtual 155	org/jivesoftware/smack/packet/Bind:getError	()Lorg/jivesoftware/smack/packet/XMPPError;
    //   130: invokespecial 158	org/jivesoftware/smack/XMPPException:<init>	(Lorg/jivesoftware/smack/packet/XMPPError;)V
    //   133: athrow
    //   134: aload_3
    //   135: invokevirtual 161	org/jivesoftware/smack/packet/Bind:getJid	()Ljava/lang/String;
    //   138: astore_1
    //   139: aload_0
    //   140: getfield 163	org/jivesoftware/smack/SASLAuthentication:sessionSupported	Z
    //   143: ifeq +97 -> 240
    //   146: new 165	org/jivesoftware/smack/packet/Session
    //   149: dup
    //   150: invokespecial 166	org/jivesoftware/smack/packet/Session:<init>	()V
    //   153: astore 4
    //   155: aload_0
    //   156: getfield 79	org/jivesoftware/smack/SASLAuthentication:connection	Lorg/jivesoftware/smack/XMPPConnection;
    //   159: new 109	org/jivesoftware/smack/filter/PacketIDFilter
    //   162: dup
    //   163: aload 4
    //   165: invokevirtual 167	org/jivesoftware/smack/packet/Session:getPacketID	()Ljava/lang/String;
    //   168: invokespecial 114	org/jivesoftware/smack/filter/PacketIDFilter:<init>	(Ljava/lang/String;)V
    //   171: invokevirtual 120	org/jivesoftware/smack/XMPPConnection:createPacketCollector	(Lorg/jivesoftware/smack/filter/PacketFilter;)Lorg/jivesoftware/smack/PacketCollector;
    //   174: astore_3
    //   175: aload_0
    //   176: getfield 79	org/jivesoftware/smack/SASLAuthentication:connection	Lorg/jivesoftware/smack/XMPPConnection;
    //   179: aload 4
    //   181: invokevirtual 124	org/jivesoftware/smack/XMPPConnection:sendPacket	(Lorg/jivesoftware/smack/packet/Packet;)V
    //   184: aload_3
    //   185: invokestatic 130	org/jivesoftware/smack/SmackConfiguration:getPacketReplyTimeout	()I
    //   188: i2l
    //   189: invokevirtual 136	org/jivesoftware/smack/PacketCollector:nextResult	(J)Lorg/jivesoftware/smack/packet/Packet;
    //   192: checkcast 169	org/jivesoftware/smack/packet/IQ
    //   195: astore 4
    //   197: aload_3
    //   198: invokevirtual 139	org/jivesoftware/smack/PacketCollector:cancel	()V
    //   201: aload 4
    //   203: ifnonnull +13 -> 216
    //   206: new 86	org/jivesoftware/smack/XMPPException
    //   209: dup
    //   210: ldc 141
    //   212: invokespecial 101	org/jivesoftware/smack/XMPPException:<init>	(Ljava/lang/String;)V
    //   215: athrow
    //   216: aload 4
    //   218: invokevirtual 170	org/jivesoftware/smack/packet/IQ:getType	()Lorg/jivesoftware/smack/packet/IQ$Type;
    //   221: getstatic 151	org/jivesoftware/smack/packet/IQ$Type:ERROR	Lorg/jivesoftware/smack/packet/IQ$Type;
    //   224: if_acmpne +30 -> 254
    //   227: new 86	org/jivesoftware/smack/XMPPException
    //   230: dup
    //   231: aload 4
    //   233: invokevirtual 171	org/jivesoftware/smack/packet/IQ:getError	()Lorg/jivesoftware/smack/packet/XMPPError;
    //   236: invokespecial 158	org/jivesoftware/smack/XMPPException:<init>	(Lorg/jivesoftware/smack/packet/XMPPError;)V
    //   239: athrow
    //   240: new 86	org/jivesoftware/smack/XMPPException
    //   243: dup
    //   244: ldc 173
    //   246: invokespecial 101	org/jivesoftware/smack/XMPPException:<init>	(Ljava/lang/String;)V
    //   249: athrow
    //   250: astore_3
    //   251: goto -233 -> 18
    //   254: aload_1
    //   255: areturn
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	256	0	this	SASLAuthentication
    //   0	256	1	paramString	String
    //   6	2	2	bool	boolean
    //   49	149	3	localObject1	Object
    //   250	1	3	localInterruptedException	java.lang.InterruptedException
    //   153	79	4	localObject2	Object
    // Exception table:
    //   from	to	target	type
    //   2	7	37	finally
    //   11	18	37	finally
    //   18	20	37	finally
    //   38	40	37	finally
    //   11	18	250	java/lang/InterruptedException
  }
  
  public static List<Class> getRegisterSASLMechanisms()
  {
    ArrayList localArrayList = new ArrayList();
    Iterator localIterator = mechanismsPreferences.iterator();
    while (localIterator.hasNext())
    {
      String str = (String)localIterator.next();
      localArrayList.add(implementedMechanisms.get(str));
    }
    return localArrayList;
  }
  
  public static void registerSASLMechanism(String paramString, Class paramClass)
  {
    implementedMechanisms.put(paramString, paramClass);
  }
  
  public static void supportSASLMechanism(String paramString)
  {
    mechanismsPreferences.add(0, paramString);
  }
  
  public static void supportSASLMechanism(String paramString, int paramInt)
  {
    mechanismsPreferences.add(paramInt, paramString);
  }
  
  public static void unregisterSASLMechanism(String paramString)
  {
    implementedMechanisms.remove(paramString);
    mechanismsPreferences.remove(paramString);
  }
  
  public static void unsupportSASLMechanism(String paramString)
  {
    mechanismsPreferences.remove(paramString);
  }
  
  /* Error */
  public String authenticate(String paramString1, String paramString2, String paramString3)
    throws XMPPException
  {
    // Byte code:
    //   0: aconst_null
    //   1: astore 6
    //   3: getstatic 38	org/jivesoftware/smack/SASLAuthentication:mechanismsPreferences	Ljava/util/List;
    //   6: invokeinterface 182 1 0
    //   11: astore 7
    //   13: aload 6
    //   15: astore 5
    //   17: aload 7
    //   19: invokeinterface 188 1 0
    //   24: ifeq +42 -> 66
    //   27: aload 7
    //   29: invokeinterface 192 1 0
    //   34: checkcast 194	java/lang/String
    //   37: astore 5
    //   39: getstatic 33	org/jivesoftware/smack/SASLAuthentication:implementedMechanisms	Ljava/util/Map;
    //   42: aload 5
    //   44: invokeinterface 227 2 0
    //   49: ifeq -36 -> 13
    //   52: aload_0
    //   53: getfield 75	org/jivesoftware/smack/SASLAuthentication:serverMechanisms	Ljava/util/Collection;
    //   56: aload 5
    //   58: invokeinterface 232 2 0
    //   63: ifeq -50 -> 13
    //   66: aload 5
    //   68: ifnull +197 -> 265
    //   71: aload_0
    //   72: getstatic 33	org/jivesoftware/smack/SASLAuthentication:implementedMechanisms	Ljava/util/Map;
    //   75: aload 5
    //   77: invokeinterface 200 2 0
    //   82: checkcast 234	java/lang/Class
    //   85: iconst_1
    //   86: anewarray 234	java/lang/Class
    //   89: dup
    //   90: iconst_0
    //   91: ldc 2
    //   93: aastore
    //   94: invokevirtual 238	java/lang/Class:getConstructor	([Ljava/lang/Class;)Ljava/lang/reflect/Constructor;
    //   97: iconst_1
    //   98: anewarray 4	java/lang/Object
    //   101: dup
    //   102: iconst_0
    //   103: aload_0
    //   104: aastore
    //   105: invokevirtual 244	java/lang/reflect/Constructor:newInstance	([Ljava/lang/Object;)Ljava/lang/Object;
    //   108: checkcast 246	org/jivesoftware/smack/sasl/SASLMechanism
    //   111: putfield 77	org/jivesoftware/smack/SASLAuthentication:currentMechanism	Lorg/jivesoftware/smack/sasl/SASLMechanism;
    //   114: aload_0
    //   115: getfield 77	org/jivesoftware/smack/SASLAuthentication:currentMechanism	Lorg/jivesoftware/smack/sasl/SASLMechanism;
    //   118: aload_1
    //   119: aload_0
    //   120: getfield 79	org/jivesoftware/smack/SASLAuthentication:connection	Lorg/jivesoftware/smack/XMPPConnection;
    //   123: invokevirtual 249	org/jivesoftware/smack/XMPPConnection:getServiceName	()Ljava/lang/String;
    //   126: aload_2
    //   127: invokevirtual 252	org/jivesoftware/smack/sasl/SASLMechanism:authenticate	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)V
    //   130: aload_0
    //   131: monitorenter
    //   132: aload_0
    //   133: getfield 254	org/jivesoftware/smack/SASLAuthentication:saslNegotiated	Z
    //   136: ifne +21 -> 157
    //   139: aload_0
    //   140: getfield 256	org/jivesoftware/smack/SASLAuthentication:saslFailed	Z
    //   143: istore 4
    //   145: iload 4
    //   147: ifne +10 -> 157
    //   150: aload_0
    //   151: ldc2_w 91
    //   154: invokevirtual 96	java/lang/Object:wait	(J)V
    //   157: aload_0
    //   158: monitorexit
    //   159: aload_0
    //   160: getfield 256	org/jivesoftware/smack/SASLAuthentication:saslFailed	Z
    //   163: ifeq +67 -> 230
    //   166: new 86	org/jivesoftware/smack/XMPPException
    //   169: dup
    //   170: new 258	java/lang/StringBuilder
    //   173: dup
    //   174: invokespecial 259	java/lang/StringBuilder:<init>	()V
    //   177: ldc_w 261
    //   180: invokevirtual 265	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   183: aload 5
    //   185: invokevirtual 265	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   188: invokevirtual 268	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   191: invokespecial 101	org/jivesoftware/smack/XMPPException:<init>	(Ljava/lang/String;)V
    //   194: athrow
    //   195: astore_1
    //   196: aload_1
    //   197: athrow
    //   198: astore 5
    //   200: aload_0
    //   201: monitorexit
    //   202: aload 5
    //   204: athrow
    //   205: astore 5
    //   207: aload 5
    //   209: invokevirtual 271	java/lang/Exception:printStackTrace	()V
    //   212: new 273	org/jivesoftware/smack/NonSASLAuthentication
    //   215: dup
    //   216: aload_0
    //   217: getfield 79	org/jivesoftware/smack/SASLAuthentication:connection	Lorg/jivesoftware/smack/XMPPConnection;
    //   220: invokespecial 275	org/jivesoftware/smack/NonSASLAuthentication:<init>	(Lorg/jivesoftware/smack/XMPPConnection;)V
    //   223: aload_1
    //   224: aload_2
    //   225: aload_3
    //   226: invokevirtual 277	org/jivesoftware/smack/NonSASLAuthentication:authenticate	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
    //   229: areturn
    //   230: aload_0
    //   231: getfield 254	org/jivesoftware/smack/SASLAuthentication:saslNegotiated	Z
    //   234: ifeq +9 -> 243
    //   237: aload_0
    //   238: aload_3
    //   239: invokespecial 279	org/jivesoftware/smack/SASLAuthentication:bindResourceAndEstablishSession	(Ljava/lang/String;)Ljava/lang/String;
    //   242: areturn
    //   243: new 273	org/jivesoftware/smack/NonSASLAuthentication
    //   246: dup
    //   247: aload_0
    //   248: getfield 79	org/jivesoftware/smack/SASLAuthentication:connection	Lorg/jivesoftware/smack/XMPPConnection;
    //   251: invokespecial 275	org/jivesoftware/smack/NonSASLAuthentication:<init>	(Lorg/jivesoftware/smack/XMPPConnection;)V
    //   254: aload_1
    //   255: aload_2
    //   256: aload_3
    //   257: invokevirtual 277	org/jivesoftware/smack/NonSASLAuthentication:authenticate	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
    //   260: astore 5
    //   262: aload 5
    //   264: areturn
    //   265: new 273	org/jivesoftware/smack/NonSASLAuthentication
    //   268: dup
    //   269: aload_0
    //   270: getfield 79	org/jivesoftware/smack/SASLAuthentication:connection	Lorg/jivesoftware/smack/XMPPConnection;
    //   273: invokespecial 275	org/jivesoftware/smack/NonSASLAuthentication:<init>	(Lorg/jivesoftware/smack/XMPPConnection;)V
    //   276: aload_1
    //   277: aload_2
    //   278: aload_3
    //   279: invokevirtual 277	org/jivesoftware/smack/NonSASLAuthentication:authenticate	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
    //   282: areturn
    //   283: astore 6
    //   285: goto -128 -> 157
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	288	0	this	SASLAuthentication
    //   0	288	1	paramString1	String
    //   0	288	2	paramString2	String
    //   0	288	3	paramString3	String
    //   143	3	4	bool	boolean
    //   15	169	5	localObject1	Object
    //   198	5	5	localObject2	Object
    //   205	3	5	localException	java.lang.Exception
    //   260	3	5	str	String
    //   1	13	6	localObject3	Object
    //   283	1	6	localInterruptedException	java.lang.InterruptedException
    //   11	17	7	localIterator	Iterator
    // Exception table:
    //   from	to	target	type
    //   71	132	195	org/jivesoftware/smack/XMPPException
    //   159	195	195	org/jivesoftware/smack/XMPPException
    //   202	205	195	org/jivesoftware/smack/XMPPException
    //   230	243	195	org/jivesoftware/smack/XMPPException
    //   243	262	195	org/jivesoftware/smack/XMPPException
    //   132	145	198	finally
    //   150	157	198	finally
    //   157	159	198	finally
    //   200	202	198	finally
    //   71	132	205	java/lang/Exception
    //   159	195	205	java/lang/Exception
    //   202	205	205	java/lang/Exception
    //   230	243	205	java/lang/Exception
    //   243	262	205	java/lang/Exception
    //   150	157	283	java/lang/InterruptedException
  }
  
  /* Error */
  public String authenticate(String paramString1, String paramString2, javax.security.auth.callback.CallbackHandler paramCallbackHandler)
    throws XMPPException
  {
    // Byte code:
    //   0: aconst_null
    //   1: astore 6
    //   3: getstatic 38	org/jivesoftware/smack/SASLAuthentication:mechanismsPreferences	Ljava/util/List;
    //   6: invokeinterface 182 1 0
    //   11: astore 7
    //   13: aload 6
    //   15: astore 5
    //   17: aload 7
    //   19: invokeinterface 188 1 0
    //   24: ifeq +42 -> 66
    //   27: aload 7
    //   29: invokeinterface 192 1 0
    //   34: checkcast 194	java/lang/String
    //   37: astore 5
    //   39: getstatic 33	org/jivesoftware/smack/SASLAuthentication:implementedMechanisms	Ljava/util/Map;
    //   42: aload 5
    //   44: invokeinterface 227 2 0
    //   49: ifeq -36 -> 13
    //   52: aload_0
    //   53: getfield 75	org/jivesoftware/smack/SASLAuthentication:serverMechanisms	Ljava/util/Collection;
    //   56: aload 5
    //   58: invokeinterface 232 2 0
    //   63: ifeq -50 -> 13
    //   66: aload 5
    //   68: ifnull +166 -> 234
    //   71: aload_0
    //   72: getstatic 33	org/jivesoftware/smack/SASLAuthentication:implementedMechanisms	Ljava/util/Map;
    //   75: aload 5
    //   77: invokeinterface 200 2 0
    //   82: checkcast 234	java/lang/Class
    //   85: iconst_1
    //   86: anewarray 234	java/lang/Class
    //   89: dup
    //   90: iconst_0
    //   91: ldc 2
    //   93: aastore
    //   94: invokevirtual 238	java/lang/Class:getConstructor	([Ljava/lang/Class;)Ljava/lang/reflect/Constructor;
    //   97: iconst_1
    //   98: anewarray 4	java/lang/Object
    //   101: dup
    //   102: iconst_0
    //   103: aload_0
    //   104: aastore
    //   105: invokevirtual 244	java/lang/reflect/Constructor:newInstance	([Ljava/lang/Object;)Ljava/lang/Object;
    //   108: checkcast 246	org/jivesoftware/smack/sasl/SASLMechanism
    //   111: putfield 77	org/jivesoftware/smack/SASLAuthentication:currentMechanism	Lorg/jivesoftware/smack/sasl/SASLMechanism;
    //   114: aload_0
    //   115: getfield 77	org/jivesoftware/smack/SASLAuthentication:currentMechanism	Lorg/jivesoftware/smack/sasl/SASLMechanism;
    //   118: aload_1
    //   119: aload_0
    //   120: getfield 79	org/jivesoftware/smack/SASLAuthentication:connection	Lorg/jivesoftware/smack/XMPPConnection;
    //   123: invokevirtual 283	org/jivesoftware/smack/XMPPConnection:getHost	()Ljava/lang/String;
    //   126: aload_3
    //   127: invokevirtual 286	org/jivesoftware/smack/sasl/SASLMechanism:authenticate	(Ljava/lang/String;Ljava/lang/String;Ljavax/security/auth/callback/CallbackHandler;)V
    //   130: aload_0
    //   131: monitorenter
    //   132: aload_0
    //   133: getfield 254	org/jivesoftware/smack/SASLAuthentication:saslNegotiated	Z
    //   136: ifne +21 -> 157
    //   139: aload_0
    //   140: getfield 256	org/jivesoftware/smack/SASLAuthentication:saslFailed	Z
    //   143: istore 4
    //   145: iload 4
    //   147: ifne +10 -> 157
    //   150: aload_0
    //   151: ldc2_w 91
    //   154: invokevirtual 96	java/lang/Object:wait	(J)V
    //   157: aload_0
    //   158: monitorexit
    //   159: aload_0
    //   160: getfield 256	org/jivesoftware/smack/SASLAuthentication:saslFailed	Z
    //   163: ifeq +56 -> 219
    //   166: new 86	org/jivesoftware/smack/XMPPException
    //   169: dup
    //   170: new 258	java/lang/StringBuilder
    //   173: dup
    //   174: invokespecial 259	java/lang/StringBuilder:<init>	()V
    //   177: ldc_w 261
    //   180: invokevirtual 265	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   183: aload 5
    //   185: invokevirtual 265	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   188: invokevirtual 268	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   191: invokespecial 101	org/jivesoftware/smack/XMPPException:<init>	(Ljava/lang/String;)V
    //   194: athrow
    //   195: astore_1
    //   196: aload_1
    //   197: athrow
    //   198: astore_1
    //   199: aload_0
    //   200: monitorexit
    //   201: aload_1
    //   202: athrow
    //   203: astore_1
    //   204: aload_1
    //   205: invokevirtual 271	java/lang/Exception:printStackTrace	()V
    //   208: new 86	org/jivesoftware/smack/XMPPException
    //   211: dup
    //   212: ldc_w 288
    //   215: invokespecial 101	org/jivesoftware/smack/XMPPException:<init>	(Ljava/lang/String;)V
    //   218: athrow
    //   219: aload_0
    //   220: getfield 254	org/jivesoftware/smack/SASLAuthentication:saslNegotiated	Z
    //   223: ifeq -15 -> 208
    //   226: aload_0
    //   227: aload_2
    //   228: invokespecial 279	org/jivesoftware/smack/SASLAuthentication:bindResourceAndEstablishSession	(Ljava/lang/String;)Ljava/lang/String;
    //   231: astore_1
    //   232: aload_1
    //   233: areturn
    //   234: new 86	org/jivesoftware/smack/XMPPException
    //   237: dup
    //   238: ldc_w 290
    //   241: invokespecial 101	org/jivesoftware/smack/XMPPException:<init>	(Ljava/lang/String;)V
    //   244: athrow
    //   245: astore_1
    //   246: goto -89 -> 157
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	249	0	this	SASLAuthentication
    //   0	249	1	paramString1	String
    //   0	249	2	paramString2	String
    //   0	249	3	paramCallbackHandler	javax.security.auth.callback.CallbackHandler
    //   143	3	4	bool	boolean
    //   15	169	5	localObject1	Object
    //   1	13	6	localObject2	Object
    //   11	17	7	localIterator	Iterator
    // Exception table:
    //   from	to	target	type
    //   71	132	195	org/jivesoftware/smack/XMPPException
    //   159	195	195	org/jivesoftware/smack/XMPPException
    //   201	203	195	org/jivesoftware/smack/XMPPException
    //   219	232	195	org/jivesoftware/smack/XMPPException
    //   132	145	198	finally
    //   150	157	198	finally
    //   157	159	198	finally
    //   199	201	198	finally
    //   71	132	203	java/lang/Exception
    //   159	195	203	java/lang/Exception
    //   201	203	203	java/lang/Exception
    //   219	232	203	java/lang/Exception
    //   150	157	245	java/lang/InterruptedException
  }
  
  /* Error */
  public String authenticateAnonymously()
    throws XMPPException
  {
    // Byte code:
    //   0: aload_0
    //   1: new 66	org/jivesoftware/smack/sasl/SASLAnonymous
    //   4: dup
    //   5: aload_0
    //   6: invokespecial 296	org/jivesoftware/smack/sasl/SASLAnonymous:<init>	(Lorg/jivesoftware/smack/SASLAuthentication;)V
    //   9: putfield 77	org/jivesoftware/smack/SASLAuthentication:currentMechanism	Lorg/jivesoftware/smack/sasl/SASLMechanism;
    //   12: aload_0
    //   13: getfield 77	org/jivesoftware/smack/SASLAuthentication:currentMechanism	Lorg/jivesoftware/smack/sasl/SASLMechanism;
    //   16: aconst_null
    //   17: aconst_null
    //   18: ldc_w 298
    //   21: invokevirtual 252	org/jivesoftware/smack/sasl/SASLMechanism:authenticate	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)V
    //   24: aload_0
    //   25: monitorenter
    //   26: aload_0
    //   27: getfield 254	org/jivesoftware/smack/SASLAuthentication:saslNegotiated	Z
    //   30: ifne +19 -> 49
    //   33: aload_0
    //   34: getfield 256	org/jivesoftware/smack/SASLAuthentication:saslFailed	Z
    //   37: istore_1
    //   38: iload_1
    //   39: ifne +10 -> 49
    //   42: aload_0
    //   43: ldc2_w 299
    //   46: invokevirtual 96	java/lang/Object:wait	(J)V
    //   49: aload_0
    //   50: monitorexit
    //   51: aload_0
    //   52: getfield 256	org/jivesoftware/smack/SASLAuthentication:saslFailed	Z
    //   55: ifeq +35 -> 90
    //   58: new 86	org/jivesoftware/smack/XMPPException
    //   61: dup
    //   62: ldc_w 288
    //   65: invokespecial 101	org/jivesoftware/smack/XMPPException:<init>	(Ljava/lang/String;)V
    //   68: athrow
    //   69: astore_2
    //   70: new 273	org/jivesoftware/smack/NonSASLAuthentication
    //   73: dup
    //   74: aload_0
    //   75: getfield 79	org/jivesoftware/smack/SASLAuthentication:connection	Lorg/jivesoftware/smack/XMPPConnection;
    //   78: invokespecial 275	org/jivesoftware/smack/NonSASLAuthentication:<init>	(Lorg/jivesoftware/smack/XMPPConnection;)V
    //   81: invokevirtual 302	org/jivesoftware/smack/NonSASLAuthentication:authenticateAnonymously	()Ljava/lang/String;
    //   84: areturn
    //   85: astore_2
    //   86: aload_0
    //   87: monitorexit
    //   88: aload_2
    //   89: athrow
    //   90: aload_0
    //   91: getfield 254	org/jivesoftware/smack/SASLAuthentication:saslNegotiated	Z
    //   94: ifeq +9 -> 103
    //   97: aload_0
    //   98: aconst_null
    //   99: invokespecial 279	org/jivesoftware/smack/SASLAuthentication:bindResourceAndEstablishSession	(Ljava/lang/String;)Ljava/lang/String;
    //   102: areturn
    //   103: new 273	org/jivesoftware/smack/NonSASLAuthentication
    //   106: dup
    //   107: aload_0
    //   108: getfield 79	org/jivesoftware/smack/SASLAuthentication:connection	Lorg/jivesoftware/smack/XMPPConnection;
    //   111: invokespecial 275	org/jivesoftware/smack/NonSASLAuthentication:<init>	(Lorg/jivesoftware/smack/XMPPConnection;)V
    //   114: invokevirtual 302	org/jivesoftware/smack/NonSASLAuthentication:authenticateAnonymously	()Ljava/lang/String;
    //   117: astore_2
    //   118: aload_2
    //   119: areturn
    //   120: astore_2
    //   121: goto -72 -> 49
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	124	0	this	SASLAuthentication
    //   37	2	1	bool	boolean
    //   69	1	2	localIOException	IOException
    //   85	4	2	localObject	Object
    //   117	2	2	str	String
    //   120	1	2	localInterruptedException	java.lang.InterruptedException
    // Exception table:
    //   from	to	target	type
    //   0	26	69	java/io/IOException
    //   51	69	69	java/io/IOException
    //   88	90	69	java/io/IOException
    //   90	103	69	java/io/IOException
    //   103	118	69	java/io/IOException
    //   26	38	85	finally
    //   42	49	85	finally
    //   49	51	85	finally
    //   86	88	85	finally
    //   42	49	120	java/lang/InterruptedException
  }
  
  void authenticated()
  {
    try
    {
      this.saslNegotiated = true;
      notify();
      return;
    }
    finally {}
  }
  
  void authenticationFailed()
  {
    try
    {
      this.saslFailed = true;
      notify();
      return;
    }
    finally {}
  }
  
  void bindingRequired()
  {
    try
    {
      this.resourceBinded = true;
      notify();
      return;
    }
    finally {}
  }
  
  void challengeReceived(String paramString)
    throws IOException
  {
    this.currentMechanism.challengeReceived(paramString);
  }
  
  public boolean hasAnonymousAuthentication()
  {
    return this.serverMechanisms.contains("ANONYMOUS");
  }
  
  public boolean hasNonAnonymousAuthentication()
  {
    return (!this.serverMechanisms.isEmpty()) && ((this.serverMechanisms.size() != 1) || (!hasAnonymousAuthentication()));
  }
  
  protected void init()
  {
    this.saslNegotiated = false;
    this.saslFailed = false;
    this.resourceBinded = false;
    this.sessionSupported = false;
  }
  
  public boolean isAuthenticated()
  {
    return this.saslNegotiated;
  }
  
  public void send(String paramString)
    throws IOException
  {
    this.connection.writer.write(paramString);
    this.connection.writer.flush();
  }
  
  void sessionsSupported()
  {
    this.sessionSupported = true;
  }
  
  void setAvailableSASLMethods(Collection<String> paramCollection)
  {
    this.serverMechanisms = paramCollection;
  }
}


/* Location:           C:\Users\Administrator\Documents\ds\反编译\AndroidKiller_v1.3.1\projects\Jungledsq_ppc\ProjectSrc\smali\
 * Qualified Name:     org.jivesoftware.smack.SASLAuthentication
 * JD-Core Version:    0.7.0.1
 */