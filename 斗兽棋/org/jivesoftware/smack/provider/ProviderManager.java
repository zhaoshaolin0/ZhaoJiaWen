package org.jivesoftware.smack.provider;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.jivesoftware.smack.packet.IQ;

public class ProviderManager
{
  private static ProviderManager instance;
  private Map<String, Object> extensionProviders = new ConcurrentHashMap();
  private Map<String, Object> iqProviders = new ConcurrentHashMap();
  
  private ProviderManager()
  {
    initialize();
  }
  
  private ClassLoader[] getClassLoaders()
  {
    ClassLoader[] arrayOfClassLoader = new ClassLoader[2];
    arrayOfClassLoader[0] = ProviderManager.class.getClassLoader();
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
  
  public static ProviderManager getInstance()
  {
    try
    {
      if (instance == null) {
        instance = new ProviderManager();
      }
      ProviderManager localProviderManager = instance;
      return localProviderManager;
    }
    finally {}
  }
  
  private String getProviderKey(String paramString1, String paramString2)
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("<").append(paramString1).append("/><").append(paramString2).append("/>");
    return localStringBuilder.toString();
  }
  
  public static void setInstance(ProviderManager paramProviderManager)
  {
    try
    {
      if (instance != null) {
        throw new IllegalStateException("ProviderManager singleton already set");
      }
    }
    finally {}
    instance = paramProviderManager;
  }
  
  public void addExtensionProvider(String paramString1, String paramString2, Object paramObject)
  {
    if ((!(paramObject instanceof PacketExtensionProvider)) && (!(paramObject instanceof Class))) {
      throw new IllegalArgumentException("Provider must be a PacketExtensionProvider or a Class instance.");
    }
    paramString1 = getProviderKey(paramString1, paramString2);
    this.extensionProviders.put(paramString1, paramObject);
  }
  
  public void addIQProvider(String paramString1, String paramString2, Object paramObject)
  {
    if ((!(paramObject instanceof IQProvider)) && ((!(paramObject instanceof Class)) || (!IQ.class.isAssignableFrom((Class)paramObject)))) {
      throw new IllegalArgumentException("Provider must be an IQProvider or a Class instance.");
    }
    paramString1 = getProviderKey(paramString1, paramString2);
    this.iqProviders.put(paramString1, paramObject);
  }
  
  public Object getExtensionProvider(String paramString1, String paramString2)
  {
    paramString1 = getProviderKey(paramString1, paramString2);
    return this.extensionProviders.get(paramString1);
  }
  
  public Collection<Object> getExtensionProviders()
  {
    return Collections.unmodifiableCollection(this.extensionProviders.values());
  }
  
  public Object getIQProvider(String paramString1, String paramString2)
  {
    paramString1 = getProviderKey(paramString1, paramString2);
    return this.iqProviders.get(paramString1);
  }
  
  public Collection<Object> getIQProviders()
  {
    return Collections.unmodifiableCollection(this.iqProviders.values());
  }
  
  /* Error */
  protected void initialize()
  {
    // Byte code:
    //   0: aload_0
    //   1: invokespecial 151	org/jivesoftware/smack/provider/ProviderManager:getClassLoaders	()[Ljava/lang/ClassLoader;
    //   4: astore 8
    //   6: aload 8
    //   8: arraylength
    //   9: istore 4
    //   11: iconst_0
    //   12: istore_1
    //   13: iload_1
    //   14: iload 4
    //   16: if_icmpge +415 -> 431
    //   19: aload 8
    //   21: iload_1
    //   22: aaload
    //   23: ldc 153
    //   25: invokevirtual 157	java/lang/ClassLoader:getResources	(Ljava/lang/String;)Ljava/util/Enumeration;
    //   28: astore 9
    //   30: aload 9
    //   32: invokeinterface 163 1 0
    //   37: ifeq +659 -> 696
    //   40: aload 9
    //   42: invokeinterface 167 1 0
    //   47: checkcast 169	java/net/URL
    //   50: astore 7
    //   52: aconst_null
    //   53: astore 6
    //   55: aload 7
    //   57: invokevirtual 173	java/net/URL:openStream	()Ljava/io/InputStream;
    //   60: astore 7
    //   62: aload 7
    //   64: astore 6
    //   66: new 175	org/xmlpull/mxp1/MXParser
    //   69: dup
    //   70: invokespecial 176	org/xmlpull/mxp1/MXParser:<init>	()V
    //   73: astore 10
    //   75: aload 7
    //   77: astore 6
    //   79: aload 10
    //   81: ldc 178
    //   83: iconst_1
    //   84: invokeinterface 184 3 0
    //   89: aload 7
    //   91: astore 6
    //   93: aload 10
    //   95: aload 7
    //   97: ldc 186
    //   99: invokeinterface 190 3 0
    //   104: aload 7
    //   106: astore 6
    //   108: aload 10
    //   110: invokeinterface 193 1 0
    //   115: istore_2
    //   116: iload_2
    //   117: iconst_2
    //   118: if_icmpne +215 -> 333
    //   121: aload 7
    //   123: astore 6
    //   125: aload 10
    //   127: invokeinterface 196 1 0
    //   132: ldc 198
    //   134: invokevirtual 203	java/lang/String:equals	(Ljava/lang/Object;)Z
    //   137: ifeq +295 -> 432
    //   140: aload 7
    //   142: astore 6
    //   144: aload 10
    //   146: invokeinterface 206 1 0
    //   151: pop
    //   152: aload 7
    //   154: astore 6
    //   156: aload 10
    //   158: invokeinterface 206 1 0
    //   163: pop
    //   164: aload 7
    //   166: astore 6
    //   168: aload 10
    //   170: invokeinterface 209 1 0
    //   175: astore 12
    //   177: aload 7
    //   179: astore 6
    //   181: aload 10
    //   183: invokeinterface 206 1 0
    //   188: pop
    //   189: aload 7
    //   191: astore 6
    //   193: aload 10
    //   195: invokeinterface 206 1 0
    //   200: pop
    //   201: aload 7
    //   203: astore 6
    //   205: aload 10
    //   207: invokeinterface 209 1 0
    //   212: astore 13
    //   214: aload 7
    //   216: astore 6
    //   218: aload 10
    //   220: invokeinterface 206 1 0
    //   225: pop
    //   226: aload 7
    //   228: astore 6
    //   230: aload 10
    //   232: invokeinterface 206 1 0
    //   237: pop
    //   238: aload 7
    //   240: astore 6
    //   242: aload 10
    //   244: invokeinterface 209 1 0
    //   249: astore 11
    //   251: aload 7
    //   253: astore 6
    //   255: aload_0
    //   256: aload 12
    //   258: aload 13
    //   260: invokespecial 107	org/jivesoftware/smack/provider/ProviderManager:getProviderKey	(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
    //   263: astore 12
    //   265: aload 7
    //   267: astore 6
    //   269: aload_0
    //   270: getfield 21	org/jivesoftware/smack/provider/ProviderManager:iqProviders	Ljava/util/Map;
    //   273: aload 12
    //   275: invokeinterface 212 2 0
    //   280: istore 5
    //   282: iload 5
    //   284: ifne +49 -> 333
    //   287: aload 7
    //   289: astore 6
    //   291: aload 11
    //   293: invokestatic 216	java/lang/Class:forName	(Ljava/lang/String;)Ljava/lang/Class;
    //   296: astore 11
    //   298: aload 7
    //   300: astore 6
    //   302: ldc 116
    //   304: aload 11
    //   306: invokevirtual 122	java/lang/Class:isAssignableFrom	(Ljava/lang/Class;)Z
    //   309: ifeq +56 -> 365
    //   312: aload 7
    //   314: astore 6
    //   316: aload_0
    //   317: getfield 21	org/jivesoftware/smack/provider/ProviderManager:iqProviders	Ljava/util/Map;
    //   320: aload 12
    //   322: aload 11
    //   324: invokevirtual 219	java/lang/Class:newInstance	()Ljava/lang/Object;
    //   327: invokeinterface 113 3 0
    //   332: pop
    //   333: aload 7
    //   335: astore 6
    //   337: aload 10
    //   339: invokeinterface 206 1 0
    //   344: istore_3
    //   345: iload_3
    //   346: istore_2
    //   347: iload_3
    //   348: iconst_1
    //   349: if_icmpne -233 -> 116
    //   352: aload 7
    //   354: invokevirtual 224	java/io/InputStream:close	()V
    //   357: goto -327 -> 30
    //   360: astore 6
    //   362: goto -332 -> 30
    //   365: aload 7
    //   367: astore 6
    //   369: ldc 118
    //   371: aload 11
    //   373: invokevirtual 122	java/lang/Class:isAssignableFrom	(Ljava/lang/Class;)Z
    //   376: ifeq -43 -> 333
    //   379: aload 7
    //   381: astore 6
    //   383: aload_0
    //   384: getfield 21	org/jivesoftware/smack/provider/ProviderManager:iqProviders	Ljava/util/Map;
    //   387: aload 12
    //   389: aload 11
    //   391: invokeinterface 113 3 0
    //   396: pop
    //   397: goto -64 -> 333
    //   400: astore 11
    //   402: aload 7
    //   404: astore 6
    //   406: aload 11
    //   408: invokevirtual 227	java/lang/ClassNotFoundException:printStackTrace	()V
    //   411: goto -78 -> 333
    //   414: astore 7
    //   416: aload 6
    //   418: invokevirtual 224	java/io/InputStream:close	()V
    //   421: aload 7
    //   423: athrow
    //   424: astore 6
    //   426: aload 6
    //   428: invokevirtual 228	java/lang/Exception:printStackTrace	()V
    //   431: return
    //   432: aload 7
    //   434: astore 6
    //   436: aload 10
    //   438: invokeinterface 196 1 0
    //   443: ldc 230
    //   445: invokevirtual 203	java/lang/String:equals	(Ljava/lang/Object;)Z
    //   448: ifeq -115 -> 333
    //   451: aload 7
    //   453: astore 6
    //   455: aload 10
    //   457: invokeinterface 206 1 0
    //   462: pop
    //   463: aload 7
    //   465: astore 6
    //   467: aload 10
    //   469: invokeinterface 206 1 0
    //   474: pop
    //   475: aload 7
    //   477: astore 6
    //   479: aload 10
    //   481: invokeinterface 209 1 0
    //   486: astore 12
    //   488: aload 7
    //   490: astore 6
    //   492: aload 10
    //   494: invokeinterface 206 1 0
    //   499: pop
    //   500: aload 7
    //   502: astore 6
    //   504: aload 10
    //   506: invokeinterface 206 1 0
    //   511: pop
    //   512: aload 7
    //   514: astore 6
    //   516: aload 10
    //   518: invokeinterface 209 1 0
    //   523: astore 13
    //   525: aload 7
    //   527: astore 6
    //   529: aload 10
    //   531: invokeinterface 206 1 0
    //   536: pop
    //   537: aload 7
    //   539: astore 6
    //   541: aload 10
    //   543: invokeinterface 206 1 0
    //   548: pop
    //   549: aload 7
    //   551: astore 6
    //   553: aload 10
    //   555: invokeinterface 209 1 0
    //   560: astore 11
    //   562: aload 7
    //   564: astore 6
    //   566: aload_0
    //   567: aload 12
    //   569: aload 13
    //   571: invokespecial 107	org/jivesoftware/smack/provider/ProviderManager:getProviderKey	(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
    //   574: astore 12
    //   576: aload 7
    //   578: astore 6
    //   580: aload_0
    //   581: getfield 19	org/jivesoftware/smack/provider/ProviderManager:extensionProviders	Ljava/util/Map;
    //   584: aload 12
    //   586: invokeinterface 212 2 0
    //   591: istore 5
    //   593: iload 5
    //   595: ifne -262 -> 333
    //   598: aload 7
    //   600: astore 6
    //   602: aload 11
    //   604: invokestatic 216	java/lang/Class:forName	(Ljava/lang/String;)Ljava/lang/Class;
    //   607: astore 11
    //   609: aload 7
    //   611: astore 6
    //   613: ldc 100
    //   615: aload 11
    //   617: invokevirtual 122	java/lang/Class:isAssignableFrom	(Ljava/lang/Class;)Z
    //   620: ifeq +41 -> 661
    //   623: aload 7
    //   625: astore 6
    //   627: aload_0
    //   628: getfield 19	org/jivesoftware/smack/provider/ProviderManager:extensionProviders	Ljava/util/Map;
    //   631: aload 12
    //   633: aload 11
    //   635: invokevirtual 219	java/lang/Class:newInstance	()Ljava/lang/Object;
    //   638: invokeinterface 113 3 0
    //   643: pop
    //   644: goto -311 -> 333
    //   647: astore 11
    //   649: aload 7
    //   651: astore 6
    //   653: aload 11
    //   655: invokevirtual 227	java/lang/ClassNotFoundException:printStackTrace	()V
    //   658: goto -325 -> 333
    //   661: aload 7
    //   663: astore 6
    //   665: ldc 232
    //   667: aload 11
    //   669: invokevirtual 122	java/lang/Class:isAssignableFrom	(Ljava/lang/Class;)Z
    //   672: ifeq -339 -> 333
    //   675: aload 7
    //   677: astore 6
    //   679: aload_0
    //   680: getfield 19	org/jivesoftware/smack/provider/ProviderManager:extensionProviders	Ljava/util/Map;
    //   683: aload 12
    //   685: aload 11
    //   687: invokeinterface 113 3 0
    //   692: pop
    //   693: goto -360 -> 333
    //   696: iload_1
    //   697: iconst_1
    //   698: iadd
    //   699: istore_1
    //   700: goto -687 -> 13
    //   703: astore 6
    //   705: goto -284 -> 421
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	708	0	this	ProviderManager
    //   12	688	1	i	int
    //   115	232	2	j	int
    //   344	6	3	k	int
    //   9	8	4	m	int
    //   280	314	5	bool	boolean
    //   53	283	6	localObject1	Object
    //   360	1	6	localException1	java.lang.Exception
    //   367	50	6	localObject2	Object
    //   424	3	6	localException2	java.lang.Exception
    //   434	244	6	localObject3	Object
    //   703	1	6	localException3	java.lang.Exception
    //   50	353	7	localObject4	Object
    //   414	262	7	localObject5	Object
    //   4	16	8	arrayOfClassLoader	ClassLoader[]
    //   28	13	9	localEnumeration	java.util.Enumeration
    //   73	481	10	localMXParser	org.xmlpull.mxp1.MXParser
    //   249	141	11	localObject6	Object
    //   400	7	11	localClassNotFoundException1	java.lang.ClassNotFoundException
    //   560	74	11	localObject7	Object
    //   647	39	11	localClassNotFoundException2	java.lang.ClassNotFoundException
    //   175	509	12	str1	String
    //   212	358	13	str2	String
    // Exception table:
    //   from	to	target	type
    //   352	357	360	java/lang/Exception
    //   291	298	400	java/lang/ClassNotFoundException
    //   302	312	400	java/lang/ClassNotFoundException
    //   316	333	400	java/lang/ClassNotFoundException
    //   369	379	400	java/lang/ClassNotFoundException
    //   383	397	400	java/lang/ClassNotFoundException
    //   55	62	414	finally
    //   66	75	414	finally
    //   79	89	414	finally
    //   93	104	414	finally
    //   108	116	414	finally
    //   125	140	414	finally
    //   144	152	414	finally
    //   156	164	414	finally
    //   168	177	414	finally
    //   181	189	414	finally
    //   193	201	414	finally
    //   205	214	414	finally
    //   218	226	414	finally
    //   230	238	414	finally
    //   242	251	414	finally
    //   255	265	414	finally
    //   269	282	414	finally
    //   291	298	414	finally
    //   302	312	414	finally
    //   316	333	414	finally
    //   337	345	414	finally
    //   369	379	414	finally
    //   383	397	414	finally
    //   406	411	414	finally
    //   436	451	414	finally
    //   455	463	414	finally
    //   467	475	414	finally
    //   479	488	414	finally
    //   492	500	414	finally
    //   504	512	414	finally
    //   516	525	414	finally
    //   529	537	414	finally
    //   541	549	414	finally
    //   553	562	414	finally
    //   566	576	414	finally
    //   580	593	414	finally
    //   602	609	414	finally
    //   613	623	414	finally
    //   627	644	414	finally
    //   653	658	414	finally
    //   665	675	414	finally
    //   679	693	414	finally
    //   0	11	424	java/lang/Exception
    //   19	30	424	java/lang/Exception
    //   30	52	424	java/lang/Exception
    //   421	424	424	java/lang/Exception
    //   602	609	647	java/lang/ClassNotFoundException
    //   613	623	647	java/lang/ClassNotFoundException
    //   627	644	647	java/lang/ClassNotFoundException
    //   665	675	647	java/lang/ClassNotFoundException
    //   679	693	647	java/lang/ClassNotFoundException
    //   416	421	703	java/lang/Exception
  }
  
  public void removeExtensionProvider(String paramString1, String paramString2)
  {
    paramString1 = getProviderKey(paramString1, paramString2);
    this.extensionProviders.remove(paramString1);
  }
  
  public void removeIQProvider(String paramString1, String paramString2)
  {
    paramString1 = getProviderKey(paramString1, paramString2);
    this.iqProviders.remove(paramString1);
  }
}


/* Location:           C:\Users\Administrator\Documents\ds\反编译\AndroidKiller_v1.3.1\projects\Jungledsq_ppc\ProjectSrc\smali\
 * Qualified Name:     org.jivesoftware.smack.provider.ProviderManager
 * JD-Core Version:    0.7.0.1
 */