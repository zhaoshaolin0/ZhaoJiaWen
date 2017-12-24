package org.jivesoftware.smack.packet;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import org.jivesoftware.smack.util.StringUtils;

public abstract class Packet
{
  protected static final String DEFAULT_LANGUAGE = Locale.getDefault().getLanguage().toLowerCase();
  private static String DEFAULT_XML_NS = null;
  public static final String ID_NOT_AVAILABLE = "ID_NOT_AVAILABLE";
  private static long id = 0L;
  private static String prefix = StringUtils.randomString(5) + "-";
  private XMPPError error = null;
  private String from = null;
  private final List<PacketExtension> packetExtensions = new CopyOnWriteArrayList();
  private String packetID = null;
  private final Map<String, Object> properties = new HashMap();
  private String to = null;
  private String xmlns = DEFAULT_XML_NS;
  
  protected static String getDefaultLanguage()
  {
    return DEFAULT_LANGUAGE;
  }
  
  public static String nextID()
  {
    try
    {
      Object localObject1 = new StringBuilder().append(prefix);
      long l = id;
      id = 1L + l;
      localObject1 = Long.toString(l);
      return localObject1;
    }
    finally
    {
      localObject2 = finally;
      throw localObject2;
    }
  }
  
  protected static String parseXMLLang(String paramString)
  {
    String str;
    if (paramString != null)
    {
      str = paramString;
      if (!"".equals(paramString)) {}
    }
    else
    {
      str = DEFAULT_LANGUAGE;
    }
    return str;
  }
  
  public static void setDefaultXmlns(String paramString)
  {
    DEFAULT_XML_NS = paramString;
  }
  
  public void addExtension(PacketExtension paramPacketExtension)
  {
    this.packetExtensions.add(paramPacketExtension);
  }
  
  /* Error */
  public void deleteProperty(String paramString)
  {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield 89	org/jivesoftware/smack/packet/Packet:properties	Ljava/util/Map;
    //   6: astore_2
    //   7: aload_2
    //   8: ifnonnull +6 -> 14
    //   11: aload_0
    //   12: monitorexit
    //   13: return
    //   14: aload_0
    //   15: getfield 89	org/jivesoftware/smack/packet/Packet:properties	Ljava/util/Map;
    //   18: aload_1
    //   19: invokeinterface 122 2 0
    //   24: pop
    //   25: goto -14 -> 11
    //   28: astore_1
    //   29: aload_0
    //   30: monitorexit
    //   31: aload_1
    //   32: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	33	0	this	Packet
    //   0	33	1	paramString	String
    //   6	2	2	localMap	Map
    // Exception table:
    //   from	to	target	type
    //   2	7	28	finally
    //   14	25	28	finally
  }
  
  public boolean equals(Object paramObject)
  {
    if (this == paramObject) {
      return true;
    }
    if ((paramObject == null) || (getClass() != paramObject.getClass())) {
      return false;
    }
    paramObject = (Packet)paramObject;
    if (this.error != null)
    {
      if (this.error.equals(paramObject.error)) {}
    }
    else {
      while (paramObject.error != null) {
        return false;
      }
    }
    if (this.from != null)
    {
      if (this.from.equals(paramObject.from)) {}
    }
    else {
      while (paramObject.from != null) {
        return false;
      }
    }
    if (!this.packetExtensions.equals(paramObject.packetExtensions)) {
      return false;
    }
    if (this.packetID != null)
    {
      if (this.packetID.equals(paramObject.packetID)) {}
    }
    else {
      while (paramObject.packetID != null) {
        return false;
      }
    }
    if (this.properties != null)
    {
      if (this.properties.equals(paramObject.properties)) {}
    }
    else {
      while (paramObject.properties != null) {
        return false;
      }
    }
    if (this.to != null)
    {
      if (this.to.equals(paramObject.to)) {}
    }
    else {
      while (paramObject.to != null) {
        return false;
      }
    }
    if (this.xmlns != null)
    {
      if (this.xmlns.equals(paramObject.xmlns)) {}
    }
    else {
      while (paramObject.xmlns != null) {
        return false;
      }
    }
    return true;
  }
  
  public XMPPError getError()
  {
    return this.error;
  }
  
  public PacketExtension getExtension(String paramString)
  {
    return getExtension(null, paramString);
  }
  
  public PacketExtension getExtension(String paramString1, String paramString2)
  {
    if (paramString2 == null) {
      return null;
    }
    Iterator localIterator = this.packetExtensions.iterator();
    while (localIterator.hasNext())
    {
      PacketExtension localPacketExtension = (PacketExtension)localIterator.next();
      if (((paramString1 == null) || (paramString1.equals(localPacketExtension.getElementName()))) && (paramString2.equals(localPacketExtension.getNamespace()))) {
        return localPacketExtension;
      }
    }
    return null;
  }
  
  /* Error */
  public java.util.Collection<PacketExtension> getExtensions()
  {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield 84	org/jivesoftware/smack/packet/Packet:packetExtensions	Ljava/util/List;
    //   6: ifnonnull +11 -> 17
    //   9: invokestatic 164	java/util/Collections:emptyList	()Ljava/util/List;
    //   12: astore_1
    //   13: aload_0
    //   14: monitorexit
    //   15: aload_1
    //   16: areturn
    //   17: new 166	java/util/ArrayList
    //   20: dup
    //   21: aload_0
    //   22: getfield 84	org/jivesoftware/smack/packet/Packet:packetExtensions	Ljava/util/List;
    //   25: invokespecial 169	java/util/ArrayList:<init>	(Ljava/util/Collection;)V
    //   28: invokestatic 173	java/util/Collections:unmodifiableList	(Ljava/util/List;)Ljava/util/List;
    //   31: astore_1
    //   32: goto -19 -> 13
    //   35: astore_1
    //   36: aload_0
    //   37: monitorexit
    //   38: aload_1
    //   39: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	40	0	this	Packet
    //   12	20	1	localList	List
    //   35	4	1	localObject	Object
    // Exception table:
    //   from	to	target	type
    //   2	13	35	finally
    //   17	32	35	finally
  }
  
  /* Error */
  protected String getExtensionsXML()
  {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: new 47	java/lang/StringBuilder
    //   5: dup
    //   6: invokespecial 50	java/lang/StringBuilder:<init>	()V
    //   9: astore 7
    //   11: aload_0
    //   12: invokevirtual 180	org/jivesoftware/smack/packet/Packet:getExtensions	()Ljava/util/Collection;
    //   15: invokeinterface 183 1 0
    //   20: astore_1
    //   21: aload_1
    //   22: invokeinterface 144 1 0
    //   27: ifeq +31 -> 58
    //   30: aload 7
    //   32: aload_1
    //   33: invokeinterface 148 1 0
    //   38: checkcast 150	org/jivesoftware/smack/packet/PacketExtension
    //   41: invokeinterface 186 1 0
    //   46: invokevirtual 60	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   49: pop
    //   50: goto -29 -> 21
    //   53: astore_1
    //   54: aload_0
    //   55: monitorexit
    //   56: aload_1
    //   57: athrow
    //   58: aload_0
    //   59: getfield 89	org/jivesoftware/smack/packet/Packet:properties	Ljava/util/Map;
    //   62: ifnull +445 -> 507
    //   65: aload_0
    //   66: getfield 89	org/jivesoftware/smack/packet/Packet:properties	Ljava/util/Map;
    //   69: invokeinterface 189 1 0
    //   74: ifne +433 -> 507
    //   77: aload 7
    //   79: ldc 191
    //   81: invokevirtual 60	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   84: pop
    //   85: aload_0
    //   86: invokevirtual 194	org/jivesoftware/smack/packet/Packet:getPropertyNames	()Ljava/util/Collection;
    //   89: invokeinterface 183 1 0
    //   94: astore 8
    //   96: aload 8
    //   98: invokeinterface 144 1 0
    //   103: ifeq +395 -> 498
    //   106: aload 8
    //   108: invokeinterface 148 1 0
    //   113: checkcast 38	java/lang/String
    //   116: astore_1
    //   117: aload_0
    //   118: aload_1
    //   119: invokevirtual 198	org/jivesoftware/smack/packet/Packet:getProperty	(Ljava/lang/String;)Ljava/lang/Object;
    //   122: astore 9
    //   124: aload 7
    //   126: ldc 200
    //   128: invokevirtual 60	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   131: pop
    //   132: aload 7
    //   134: ldc 202
    //   136: invokevirtual 60	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   139: aload_1
    //   140: invokestatic 205	org/jivesoftware/smack/util/StringUtils:escapeForXML	(Ljava/lang/String;)Ljava/lang/String;
    //   143: invokevirtual 60	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   146: ldc 207
    //   148: invokevirtual 60	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   151: pop
    //   152: aload 7
    //   154: ldc 209
    //   156: invokevirtual 60	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   159: pop
    //   160: aload 9
    //   162: instanceof 211
    //   165: ifeq +32 -> 197
    //   168: aload 7
    //   170: ldc 213
    //   172: invokevirtual 60	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   175: aload 9
    //   177: invokevirtual 216	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   180: ldc 218
    //   182: invokevirtual 60	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   185: pop
    //   186: aload 7
    //   188: ldc 220
    //   190: invokevirtual 60	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   193: pop
    //   194: goto -98 -> 96
    //   197: aload 9
    //   199: instanceof 95
    //   202: ifeq +24 -> 226
    //   205: aload 7
    //   207: ldc 222
    //   209: invokevirtual 60	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   212: aload 9
    //   214: invokevirtual 216	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   217: ldc 218
    //   219: invokevirtual 60	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   222: pop
    //   223: goto -37 -> 186
    //   226: aload 9
    //   228: instanceof 224
    //   231: ifeq +24 -> 255
    //   234: aload 7
    //   236: ldc 226
    //   238: invokevirtual 60	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   241: aload 9
    //   243: invokevirtual 216	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   246: ldc 218
    //   248: invokevirtual 60	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   251: pop
    //   252: goto -66 -> 186
    //   255: aload 9
    //   257: instanceof 228
    //   260: ifeq +24 -> 284
    //   263: aload 7
    //   265: ldc 230
    //   267: invokevirtual 60	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   270: aload 9
    //   272: invokevirtual 216	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   275: ldc 218
    //   277: invokevirtual 60	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   280: pop
    //   281: goto -95 -> 186
    //   284: aload 9
    //   286: instanceof 232
    //   289: ifeq +24 -> 313
    //   292: aload 7
    //   294: ldc 234
    //   296: invokevirtual 60	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   299: aload 9
    //   301: invokevirtual 216	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   304: ldc 218
    //   306: invokevirtual 60	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   309: pop
    //   310: goto -124 -> 186
    //   313: aload 9
    //   315: instanceof 38
    //   318: ifeq +36 -> 354
    //   321: aload 7
    //   323: ldc 236
    //   325: invokevirtual 60	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   328: pop
    //   329: aload 7
    //   331: aload 9
    //   333: checkcast 38	java/lang/String
    //   336: invokestatic 205	org/jivesoftware/smack/util/StringUtils:escapeForXML	(Ljava/lang/String;)Ljava/lang/String;
    //   339: invokevirtual 60	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   342: pop
    //   343: aload 7
    //   345: ldc 218
    //   347: invokevirtual 60	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   350: pop
    //   351: goto -165 -> 186
    //   354: aconst_null
    //   355: astore_2
    //   356: aconst_null
    //   357: astore 6
    //   359: aconst_null
    //   360: astore_3
    //   361: aconst_null
    //   362: astore 4
    //   364: aconst_null
    //   365: astore 5
    //   367: new 238	java/io/ByteArrayOutputStream
    //   370: dup
    //   371: invokespecial 239	java/io/ByteArrayOutputStream:<init>	()V
    //   374: astore_1
    //   375: new 241	java/io/ObjectOutputStream
    //   378: dup
    //   379: aload_1
    //   380: invokespecial 244	java/io/ObjectOutputStream:<init>	(Ljava/io/OutputStream;)V
    //   383: astore_2
    //   384: aload_2
    //   385: aload 9
    //   387: invokevirtual 248	java/io/ObjectOutputStream:writeObject	(Ljava/lang/Object;)V
    //   390: aload 7
    //   392: ldc 250
    //   394: invokevirtual 60	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   397: pop
    //   398: aload 7
    //   400: aload_1
    //   401: invokevirtual 254	java/io/ByteArrayOutputStream:toByteArray	()[B
    //   404: invokestatic 258	org/jivesoftware/smack/util/StringUtils:encodeBase64	([B)Ljava/lang/String;
    //   407: invokevirtual 60	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   410: ldc 218
    //   412: invokevirtual 60	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   415: pop
    //   416: aload_2
    //   417: ifnull +7 -> 424
    //   420: aload_2
    //   421: invokevirtual 261	java/io/ObjectOutputStream:close	()V
    //   424: aload_1
    //   425: ifnull -239 -> 186
    //   428: aload_1
    //   429: invokevirtual 262	java/io/ByteArrayOutputStream:close	()V
    //   432: goto -246 -> 186
    //   435: astore_1
    //   436: goto -250 -> 186
    //   439: astore 4
    //   441: aload 6
    //   443: astore_1
    //   444: aload_1
    //   445: astore_2
    //   446: aload 5
    //   448: astore_3
    //   449: aload 4
    //   451: invokevirtual 265	java/lang/Exception:printStackTrace	()V
    //   454: aload 5
    //   456: ifnull +8 -> 464
    //   459: aload 5
    //   461: invokevirtual 261	java/io/ObjectOutputStream:close	()V
    //   464: aload_1
    //   465: ifnull -279 -> 186
    //   468: aload_1
    //   469: invokevirtual 262	java/io/ByteArrayOutputStream:close	()V
    //   472: goto -286 -> 186
    //   475: astore_1
    //   476: goto -290 -> 186
    //   479: astore_1
    //   480: aload_3
    //   481: ifnull +7 -> 488
    //   484: aload_3
    //   485: invokevirtual 261	java/io/ObjectOutputStream:close	()V
    //   488: aload_2
    //   489: ifnull +7 -> 496
    //   492: aload_2
    //   493: invokevirtual 262	java/io/ByteArrayOutputStream:close	()V
    //   496: aload_1
    //   497: athrow
    //   498: aload 7
    //   500: ldc_w 267
    //   503: invokevirtual 60	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   506: pop
    //   507: aload 7
    //   509: invokevirtual 65	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   512: astore_1
    //   513: aload_0
    //   514: monitorexit
    //   515: aload_1
    //   516: areturn
    //   517: astore_2
    //   518: goto -94 -> 424
    //   521: astore_2
    //   522: goto -58 -> 464
    //   525: astore_3
    //   526: goto -38 -> 488
    //   529: astore_2
    //   530: goto -34 -> 496
    //   533: astore 5
    //   535: aload_1
    //   536: astore_2
    //   537: aload 4
    //   539: astore_3
    //   540: aload 5
    //   542: astore_1
    //   543: goto -63 -> 480
    //   546: astore 4
    //   548: aload_2
    //   549: astore_3
    //   550: aload_1
    //   551: astore_2
    //   552: aload 4
    //   554: astore_1
    //   555: goto -75 -> 480
    //   558: astore 4
    //   560: goto -116 -> 444
    //   563: astore 4
    //   565: aload_2
    //   566: astore 5
    //   568: goto -124 -> 444
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	571	0	this	Packet
    //   20	13	1	localIterator1	Iterator
    //   53	4	1	localObject1	Object
    //   116	313	1	localObject2	Object
    //   435	1	1	localException1	java.lang.Exception
    //   443	26	1	localObject3	Object
    //   475	1	1	localException2	java.lang.Exception
    //   479	18	1	localObject4	Object
    //   512	43	1	localObject5	Object
    //   355	138	2	localObject6	Object
    //   517	1	2	localException3	java.lang.Exception
    //   521	1	2	localException4	java.lang.Exception
    //   529	1	2	localException5	java.lang.Exception
    //   536	30	2	localObject7	Object
    //   360	125	3	localObject8	Object
    //   525	1	3	localException6	java.lang.Exception
    //   539	11	3	localObject9	Object
    //   362	1	4	localObject10	Object
    //   439	99	4	localException7	java.lang.Exception
    //   546	7	4	localObject11	Object
    //   558	1	4	localException8	java.lang.Exception
    //   563	1	4	localException9	java.lang.Exception
    //   365	95	5	localObject12	Object
    //   533	8	5	localObject13	Object
    //   566	1	5	localObject14	Object
    //   357	85	6	localObject15	Object
    //   9	499	7	localStringBuilder	StringBuilder
    //   94	13	8	localIterator2	Iterator
    //   122	264	9	localObject16	Object
    // Exception table:
    //   from	to	target	type
    //   2	21	53	finally
    //   21	50	53	finally
    //   58	96	53	finally
    //   96	186	53	finally
    //   186	194	53	finally
    //   197	223	53	finally
    //   226	252	53	finally
    //   255	281	53	finally
    //   284	310	53	finally
    //   313	351	53	finally
    //   420	424	53	finally
    //   428	432	53	finally
    //   459	464	53	finally
    //   468	472	53	finally
    //   484	488	53	finally
    //   492	496	53	finally
    //   496	498	53	finally
    //   498	507	53	finally
    //   507	513	53	finally
    //   428	432	435	java/lang/Exception
    //   367	375	439	java/lang/Exception
    //   468	472	475	java/lang/Exception
    //   367	375	479	finally
    //   449	454	479	finally
    //   420	424	517	java/lang/Exception
    //   459	464	521	java/lang/Exception
    //   484	488	525	java/lang/Exception
    //   492	496	529	java/lang/Exception
    //   375	384	533	finally
    //   384	416	546	finally
    //   375	384	558	java/lang/Exception
    //   384	416	563	java/lang/Exception
  }
  
  public String getFrom()
  {
    return this.from;
  }
  
  public String getPacketID()
  {
    if ("ID_NOT_AVAILABLE".equals(this.packetID)) {
      return null;
    }
    if (this.packetID == null) {
      this.packetID = nextID();
    }
    return this.packetID;
  }
  
  /* Error */
  public Object getProperty(String paramString)
  {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield 89	org/jivesoftware/smack/packet/Packet:properties	Ljava/util/Map;
    //   6: astore_2
    //   7: aload_2
    //   8: ifnonnull +9 -> 17
    //   11: aconst_null
    //   12: astore_1
    //   13: aload_0
    //   14: monitorexit
    //   15: aload_1
    //   16: areturn
    //   17: aload_0
    //   18: getfield 89	org/jivesoftware/smack/packet/Packet:properties	Ljava/util/Map;
    //   21: aload_1
    //   22: invokeinterface 274 2 0
    //   27: astore_1
    //   28: goto -15 -> 13
    //   31: astore_1
    //   32: aload_0
    //   33: monitorexit
    //   34: aload_1
    //   35: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	36	0	this	Packet
    //   0	36	1	paramString	String
    //   6	2	2	localMap	Map
    // Exception table:
    //   from	to	target	type
    //   2	7	31	finally
    //   17	28	31	finally
  }
  
  /* Error */
  public java.util.Collection<String> getPropertyNames()
  {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield 89	org/jivesoftware/smack/packet/Packet:properties	Ljava/util/Map;
    //   6: ifnonnull +11 -> 17
    //   9: invokestatic 278	java/util/Collections:emptySet	()Ljava/util/Set;
    //   12: astore_1
    //   13: aload_0
    //   14: monitorexit
    //   15: aload_1
    //   16: areturn
    //   17: new 280	java/util/HashSet
    //   20: dup
    //   21: aload_0
    //   22: getfield 89	org/jivesoftware/smack/packet/Packet:properties	Ljava/util/Map;
    //   25: invokeinterface 283 1 0
    //   30: invokespecial 284	java/util/HashSet:<init>	(Ljava/util/Collection;)V
    //   33: invokestatic 288	java/util/Collections:unmodifiableSet	(Ljava/util/Set;)Ljava/util/Set;
    //   36: astore_1
    //   37: goto -24 -> 13
    //   40: astore_1
    //   41: aload_0
    //   42: monitorexit
    //   43: aload_1
    //   44: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	45	0	this	Packet
    //   12	25	1	localSet	java.util.Set
    //   40	4	1	localObject	Object
    // Exception table:
    //   from	to	target	type
    //   2	13	40	finally
    //   17	37	40	finally
  }
  
  public String getTo()
  {
    return this.to;
  }
  
  public String getXmlns()
  {
    return this.xmlns;
  }
  
  public int hashCode()
  {
    int i;
    int j;
    label30:
    int k;
    label45:
    int m;
    label61:
    int i1;
    int i2;
    if (this.xmlns != null)
    {
      i = this.xmlns.hashCode();
      if (this.packetID == null) {
        break label136;
      }
      j = this.packetID.hashCode();
      if (this.to == null) {
        break label141;
      }
      k = this.to.hashCode();
      if (this.from == null) {
        break label146;
      }
      m = this.from.hashCode();
      i1 = this.packetExtensions.hashCode();
      i2 = this.properties.hashCode();
      if (this.error == null) {
        break label152;
      }
    }
    label136:
    label141:
    label146:
    label152:
    for (int n = this.error.hashCode();; n = 0)
    {
      return (((((i * 31 + j) * 31 + k) * 31 + m) * 31 + i1) * 31 + i2) * 31 + n;
      i = 0;
      break;
      j = 0;
      break label30;
      k = 0;
      break label45;
      m = 0;
      break label61;
    }
  }
  
  public void removeExtension(PacketExtension paramPacketExtension)
  {
    this.packetExtensions.remove(paramPacketExtension);
  }
  
  public void setError(XMPPError paramXMPPError)
  {
    this.error = paramXMPPError;
  }
  
  public void setFrom(String paramString)
  {
    this.from = paramString;
  }
  
  public void setPacketID(String paramString)
  {
    this.packetID = paramString;
  }
  
  public void setProperty(String paramString, Object paramObject)
  {
    try
    {
      if (!(paramObject instanceof Serializable)) {
        throw new IllegalArgumentException("Value must be serialiazble");
      }
    }
    finally {}
    this.properties.put(paramString, paramObject);
  }
  
  public void setTo(String paramString)
  {
    this.to = paramString;
  }
  
  public abstract String toXML();
}


/* Location:           C:\Users\Administrator\Documents\ds\反编译\AndroidKiller_v1.3.1\projects\Jungledsq_ppc\ProjectSrc\smali\
 * Qualified Name:     org.jivesoftware.smack.packet.Packet
 * JD-Core Version:    0.7.0.1
 */