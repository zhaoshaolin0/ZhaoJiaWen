package org.jivesoftware.smack;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import org.xmlpull.v1.XmlPullParser;

public final class SmackConfiguration
{
  private static final String SMACK_VERSION = "3.1.0";
  private static Vector<String> defaultMechs;
  private static int keepAliveInterval;
  private static int packetReplyTimeout = 5000;
  
  /* Error */
  static
  {
    // Byte code:
    //   0: sipush 5000
    //   3: putstatic 20	org/jivesoftware/smack/SmackConfiguration:packetReplyTimeout	I
    //   6: sipush 30000
    //   9: putstatic 22	org/jivesoftware/smack/SmackConfiguration:keepAliveInterval	I
    //   12: new 24	java/util/Vector
    //   15: dup
    //   16: invokespecial 27	java/util/Vector:<init>	()V
    //   19: putstatic 29	org/jivesoftware/smack/SmackConfiguration:defaultMechs	Ljava/util/Vector;
    //   22: invokestatic 33	org/jivesoftware/smack/SmackConfiguration:getClassLoaders	()[Ljava/lang/ClassLoader;
    //   25: astore 7
    //   27: aload 7
    //   29: arraylength
    //   30: istore_3
    //   31: iconst_0
    //   32: istore_0
    //   33: iload_0
    //   34: iload_3
    //   35: if_icmpge +327 -> 362
    //   38: aload 7
    //   40: iload_0
    //   41: aaload
    //   42: ldc 35
    //   44: invokevirtual 41	java/lang/ClassLoader:getResources	(Ljava/lang/String;)Ljava/util/Enumeration;
    //   47: astore 8
    //   49: aload 8
    //   51: invokeinterface 47 1 0
    //   56: ifeq +355 -> 411
    //   59: aload 8
    //   61: invokeinterface 51 1 0
    //   66: checkcast 53	java/net/URL
    //   69: astore 6
    //   71: aconst_null
    //   72: astore 5
    //   74: aconst_null
    //   75: astore 4
    //   77: aload 6
    //   79: invokevirtual 57	java/net/URL:openStream	()Ljava/io/InputStream;
    //   82: astore 6
    //   84: aload 6
    //   86: astore 4
    //   88: aload 6
    //   90: astore 5
    //   92: new 59	org/xmlpull/mxp1/MXParser
    //   95: dup
    //   96: invokespecial 60	org/xmlpull/mxp1/MXParser:<init>	()V
    //   99: astore 9
    //   101: aload 6
    //   103: astore 4
    //   105: aload 6
    //   107: astore 5
    //   109: aload 9
    //   111: ldc 62
    //   113: iconst_1
    //   114: invokeinterface 68 3 0
    //   119: aload 6
    //   121: astore 4
    //   123: aload 6
    //   125: astore 5
    //   127: aload 9
    //   129: aload 6
    //   131: ldc 70
    //   133: invokeinterface 74 3 0
    //   138: aload 6
    //   140: astore 4
    //   142: aload 6
    //   144: astore 5
    //   146: aload 9
    //   148: invokeinterface 78 1 0
    //   153: istore_1
    //   154: iload_1
    //   155: iconst_2
    //   156: if_icmpne +39 -> 195
    //   159: aload 6
    //   161: astore 4
    //   163: aload 6
    //   165: astore 5
    //   167: aload 9
    //   169: invokeinterface 82 1 0
    //   174: ldc 84
    //   176: invokevirtual 90	java/lang/String:equals	(Ljava/lang/Object;)Z
    //   179: ifeq +52 -> 231
    //   182: aload 6
    //   184: astore 4
    //   186: aload 6
    //   188: astore 5
    //   190: aload 9
    //   192: invokestatic 94	org/jivesoftware/smack/SmackConfiguration:parseClassToLoad	(Lorg/xmlpull/v1/XmlPullParser;)V
    //   195: aload 6
    //   197: astore 4
    //   199: aload 6
    //   201: astore 5
    //   203: aload 9
    //   205: invokeinterface 97 1 0
    //   210: istore_2
    //   211: iload_2
    //   212: istore_1
    //   213: iload_2
    //   214: iconst_1
    //   215: if_icmpne -61 -> 154
    //   218: aload 6
    //   220: invokevirtual 102	java/io/InputStream:close	()V
    //   223: goto -174 -> 49
    //   226: astore 4
    //   228: goto -179 -> 49
    //   231: aload 6
    //   233: astore 4
    //   235: aload 6
    //   237: astore 5
    //   239: aload 9
    //   241: invokeinterface 82 1 0
    //   246: ldc 103
    //   248: invokevirtual 90	java/lang/String:equals	(Ljava/lang/Object;)Z
    //   251: ifeq +49 -> 300
    //   254: aload 6
    //   256: astore 4
    //   258: aload 6
    //   260: astore 5
    //   262: aload 9
    //   264: getstatic 20	org/jivesoftware/smack/SmackConfiguration:packetReplyTimeout	I
    //   267: invokestatic 107	org/jivesoftware/smack/SmackConfiguration:parseIntProperty	(Lorg/xmlpull/v1/XmlPullParser;I)I
    //   270: putstatic 20	org/jivesoftware/smack/SmackConfiguration:packetReplyTimeout	I
    //   273: goto -78 -> 195
    //   276: astore 6
    //   278: aload 4
    //   280: astore 5
    //   282: aload 6
    //   284: invokevirtual 110	java/lang/Exception:printStackTrace	()V
    //   287: aload 4
    //   289: invokevirtual 102	java/io/InputStream:close	()V
    //   292: goto -243 -> 49
    //   295: astore 4
    //   297: goto -248 -> 49
    //   300: aload 6
    //   302: astore 4
    //   304: aload 6
    //   306: astore 5
    //   308: aload 9
    //   310: invokeinterface 82 1 0
    //   315: ldc 111
    //   317: invokevirtual 90	java/lang/String:equals	(Ljava/lang/Object;)Z
    //   320: ifeq +43 -> 363
    //   323: aload 6
    //   325: astore 4
    //   327: aload 6
    //   329: astore 5
    //   331: aload 9
    //   333: getstatic 22	org/jivesoftware/smack/SmackConfiguration:keepAliveInterval	I
    //   336: invokestatic 107	org/jivesoftware/smack/SmackConfiguration:parseIntProperty	(Lorg/xmlpull/v1/XmlPullParser;I)I
    //   339: putstatic 22	org/jivesoftware/smack/SmackConfiguration:keepAliveInterval	I
    //   342: goto -147 -> 195
    //   345: astore 4
    //   347: aload 5
    //   349: invokevirtual 102	java/io/InputStream:close	()V
    //   352: aload 4
    //   354: athrow
    //   355: astore 4
    //   357: aload 4
    //   359: invokevirtual 110	java/lang/Exception:printStackTrace	()V
    //   362: return
    //   363: aload 6
    //   365: astore 4
    //   367: aload 6
    //   369: astore 5
    //   371: aload 9
    //   373: invokeinterface 82 1 0
    //   378: ldc 113
    //   380: invokevirtual 90	java/lang/String:equals	(Ljava/lang/Object;)Z
    //   383: ifeq -188 -> 195
    //   386: aload 6
    //   388: astore 4
    //   390: aload 6
    //   392: astore 5
    //   394: getstatic 29	org/jivesoftware/smack/SmackConfiguration:defaultMechs	Ljava/util/Vector;
    //   397: aload 9
    //   399: invokeinterface 116 1 0
    //   404: invokevirtual 119	java/util/Vector:add	(Ljava/lang/Object;)Z
    //   407: pop
    //   408: goto -213 -> 195
    //   411: iload_0
    //   412: iconst_1
    //   413: iadd
    //   414: istore_0
    //   415: goto -382 -> 33
    //   418: astore 5
    //   420: goto -68 -> 352
    // Local variable table:
    //   start	length	slot	name	signature
    //   32	383	0	i	int
    //   153	60	1	j	int
    //   210	6	2	k	int
    //   30	6	3	m	int
    //   75	123	4	localObject1	Object
    //   226	1	4	localException1	Exception
    //   233	55	4	localObject2	Object
    //   295	1	4	localException2	Exception
    //   302	24	4	localException3	Exception
    //   345	8	4	localObject3	Object
    //   355	3	4	localException4	Exception
    //   365	24	4	localException5	Exception
    //   72	321	5	localObject4	Object
    //   418	1	5	localException6	Exception
    //   69	190	6	localObject5	Object
    //   276	115	6	localException7	Exception
    //   25	14	7	arrayOfClassLoader	ClassLoader[]
    //   47	13	8	localEnumeration	java.util.Enumeration
    //   99	299	9	localMXParser	org.xmlpull.mxp1.MXParser
    // Exception table:
    //   from	to	target	type
    //   218	223	226	java/lang/Exception
    //   77	84	276	java/lang/Exception
    //   92	101	276	java/lang/Exception
    //   109	119	276	java/lang/Exception
    //   127	138	276	java/lang/Exception
    //   146	154	276	java/lang/Exception
    //   167	182	276	java/lang/Exception
    //   190	195	276	java/lang/Exception
    //   203	211	276	java/lang/Exception
    //   239	254	276	java/lang/Exception
    //   262	273	276	java/lang/Exception
    //   308	323	276	java/lang/Exception
    //   331	342	276	java/lang/Exception
    //   371	386	276	java/lang/Exception
    //   394	408	276	java/lang/Exception
    //   287	292	295	java/lang/Exception
    //   77	84	345	finally
    //   92	101	345	finally
    //   109	119	345	finally
    //   127	138	345	finally
    //   146	154	345	finally
    //   167	182	345	finally
    //   190	195	345	finally
    //   203	211	345	finally
    //   239	254	345	finally
    //   262	273	345	finally
    //   282	287	345	finally
    //   308	323	345	finally
    //   331	342	345	finally
    //   371	386	345	finally
    //   394	408	345	finally
    //   22	31	355	java/lang/Exception
    //   38	49	355	java/lang/Exception
    //   49	71	355	java/lang/Exception
    //   352	355	355	java/lang/Exception
    //   347	352	418	java/lang/Exception
  }
  
  public static void addSaslMech(String paramString)
  {
    if (!defaultMechs.contains(paramString)) {
      defaultMechs.add(paramString);
    }
  }
  
  public static void addSaslMechs(Collection<String> paramCollection)
  {
    paramCollection = paramCollection.iterator();
    while (paramCollection.hasNext()) {
      addSaslMech((String)paramCollection.next());
    }
  }
  
  private static ClassLoader[] getClassLoaders()
  {
    ClassLoader[] arrayOfClassLoader = new ClassLoader[2];
    arrayOfClassLoader[0] = SmackConfiguration.class.getClassLoader();
    arrayOfClassLoader[1] = Thread.currentThread().getContextClassLoader();
    ArrayList localArrayList = new ArrayList();
    int j = arrayOfClassLoader.length;
    int i = 0;
    while (i < j)
    {
      ClassLoader localClassLoader = arrayOfClassLoader[i];
      if (localClassLoader != null) {
        localArrayList.add(localClassLoader);
      }
      i += 1;
    }
    return (ClassLoader[])localArrayList.toArray(new ClassLoader[localArrayList.size()]);
  }
  
  public static int getKeepAliveInterval()
  {
    return keepAliveInterval;
  }
  
  public static int getPacketReplyTimeout()
  {
    if (packetReplyTimeout <= 0) {
      packetReplyTimeout = 5000;
    }
    return packetReplyTimeout;
  }
  
  public static List<String> getSaslMechs()
  {
    return defaultMechs;
  }
  
  public static String getVersion()
  {
    return "3.1.0";
  }
  
  private static void parseClassToLoad(XmlPullParser paramXmlPullParser)
    throws Exception
  {
    paramXmlPullParser = paramXmlPullParser.nextText();
    try
    {
      Class.forName(paramXmlPullParser);
      return;
    }
    catch (ClassNotFoundException localClassNotFoundException)
    {
      System.err.println("Error! A startup class specified in smack-config.xml could not be loaded: " + paramXmlPullParser);
    }
  }
  
  private static int parseIntProperty(XmlPullParser paramXmlPullParser, int paramInt)
    throws Exception
  {
    try
    {
      int i = Integer.parseInt(paramXmlPullParser.nextText());
      return i;
    }
    catch (NumberFormatException paramXmlPullParser)
    {
      paramXmlPullParser.printStackTrace();
    }
    return paramInt;
  }
  
  public static void removeSaslMech(String paramString)
  {
    if (defaultMechs.contains(paramString)) {
      defaultMechs.remove(paramString);
    }
  }
  
  public static void removeSaslMechs(Collection<String> paramCollection)
  {
    paramCollection = paramCollection.iterator();
    while (paramCollection.hasNext()) {
      removeSaslMech((String)paramCollection.next());
    }
  }
  
  public static void setKeepAliveInterval(int paramInt)
  {
    keepAliveInterval = paramInt;
  }
  
  public static void setPacketReplyTimeout(int paramInt)
  {
    if (paramInt <= 0) {
      throw new IllegalArgumentException();
    }
    packetReplyTimeout = paramInt;
  }
}


/* Location:           C:\Users\Administrator\Documents\ds\反编译\AndroidKiller_v1.3.1\projects\Jungledsq_ppc\ProjectSrc\smali\
 * Qualified Name:     org.jivesoftware.smack.SmackConfiguration
 * JD-Core Version:    0.7.0.1
 */