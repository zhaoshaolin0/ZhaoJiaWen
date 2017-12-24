package org.jivesoftware.smack.proxy;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import javax.net.SocketFactory;

public class Socks4ProxySocketFactory
  extends SocketFactory
{
  private ProxyInfo proxy;
  
  public Socks4ProxySocketFactory(ProxyInfo paramProxyInfo)
  {
    this.proxy = paramProxyInfo;
  }
  
  /* Error */
  private Socket socks4ProxifiedSocket(String paramString, int paramInt)
    throws IOException
  {
    // Byte code:
    //   0: aconst_null
    //   1: astore 6
    //   3: aload_0
    //   4: getfield 13	org/jivesoftware/smack/proxy/Socks4ProxySocketFactory:proxy	Lorg/jivesoftware/smack/proxy/ProxyInfo;
    //   7: invokevirtual 30	org/jivesoftware/smack/proxy/ProxyInfo:getProxyAddress	()Ljava/lang/String;
    //   10: astore 5
    //   12: aload_0
    //   13: getfield 13	org/jivesoftware/smack/proxy/Socks4ProxySocketFactory:proxy	Lorg/jivesoftware/smack/proxy/ProxyInfo;
    //   16: invokevirtual 34	org/jivesoftware/smack/proxy/ProxyInfo:getProxyPort	()I
    //   19: istore_3
    //   20: aload_0
    //   21: getfield 13	org/jivesoftware/smack/proxy/Socks4ProxySocketFactory:proxy	Lorg/jivesoftware/smack/proxy/ProxyInfo;
    //   24: invokevirtual 37	org/jivesoftware/smack/proxy/ProxyInfo:getProxyUsername	()Ljava/lang/String;
    //   27: astore 7
    //   29: aload_0
    //   30: getfield 13	org/jivesoftware/smack/proxy/Socks4ProxySocketFactory:proxy	Lorg/jivesoftware/smack/proxy/ProxyInfo;
    //   33: invokevirtual 40	org/jivesoftware/smack/proxy/ProxyInfo:getProxyPassword	()Ljava/lang/String;
    //   36: pop
    //   37: new 42	java/net/Socket
    //   40: dup
    //   41: aload 5
    //   43: iload_3
    //   44: invokespecial 45	java/net/Socket:<init>	(Ljava/lang/String;I)V
    //   47: astore 5
    //   49: aload 5
    //   51: invokevirtual 49	java/net/Socket:getInputStream	()Ljava/io/InputStream;
    //   54: astore 8
    //   56: aload 5
    //   58: invokevirtual 53	java/net/Socket:getOutputStream	()Ljava/io/OutputStream;
    //   61: astore 9
    //   63: aload 5
    //   65: iconst_1
    //   66: invokevirtual 57	java/net/Socket:setTcpNoDelay	(Z)V
    //   69: sipush 1024
    //   72: newarray byte
    //   74: astore 6
    //   76: iconst_0
    //   77: iconst_1
    //   78: iadd
    //   79: istore 4
    //   81: aload 6
    //   83: iconst_0
    //   84: iconst_4
    //   85: bastore
    //   86: iload 4
    //   88: iconst_1
    //   89: iadd
    //   90: istore_3
    //   91: aload 6
    //   93: iload 4
    //   95: iconst_1
    //   96: bastore
    //   97: iload_3
    //   98: iconst_1
    //   99: iadd
    //   100: istore 4
    //   102: aload 6
    //   104: iload_3
    //   105: iload_2
    //   106: bipush 8
    //   108: iushr
    //   109: i2b
    //   110: bastore
    //   111: aload 6
    //   113: iload 4
    //   115: iload_2
    //   116: sipush 255
    //   119: iand
    //   120: i2b
    //   121: bastore
    //   122: aload_1
    //   123: invokestatic 63	java/net/InetAddress:getByName	(Ljava/lang/String;)Ljava/net/InetAddress;
    //   126: invokevirtual 67	java/net/InetAddress:getAddress	()[B
    //   129: astore_1
    //   130: iconst_0
    //   131: istore_3
    //   132: iload 4
    //   134: iconst_1
    //   135: iadd
    //   136: istore_2
    //   137: aload_1
    //   138: arraylength
    //   139: istore 4
    //   141: iload_3
    //   142: iload 4
    //   144: if_icmpge +41 -> 185
    //   147: aload 6
    //   149: iload_2
    //   150: aload_1
    //   151: iload_3
    //   152: baload
    //   153: bastore
    //   154: iload_3
    //   155: iconst_1
    //   156: iadd
    //   157: istore_3
    //   158: iload_2
    //   159: iconst_1
    //   160: iadd
    //   161: istore_2
    //   162: goto -25 -> 137
    //   165: astore_1
    //   166: new 69	org/jivesoftware/smack/proxy/ProxyException
    //   169: dup
    //   170: getstatic 75	org/jivesoftware/smack/proxy/ProxyInfo$ProxyType:SOCKS4	Lorg/jivesoftware/smack/proxy/ProxyInfo$ProxyType;
    //   173: aload_1
    //   174: invokevirtual 78	java/net/UnknownHostException:toString	()Ljava/lang/String;
    //   177: aload_1
    //   178: invokespecial 81	org/jivesoftware/smack/proxy/ProxyException:<init>	(Lorg/jivesoftware/smack/proxy/ProxyInfo$ProxyType;Ljava/lang/String;Ljava/lang/Throwable;)V
    //   181: athrow
    //   182: astore_1
    //   183: aload_1
    //   184: athrow
    //   185: iload_2
    //   186: istore_3
    //   187: aload 7
    //   189: ifnull +28 -> 217
    //   192: aload 7
    //   194: invokevirtual 86	java/lang/String:getBytes	()[B
    //   197: iconst_0
    //   198: aload 6
    //   200: iload_2
    //   201: aload 7
    //   203: invokevirtual 89	java/lang/String:length	()I
    //   206: invokestatic 95	java/lang/System:arraycopy	(Ljava/lang/Object;ILjava/lang/Object;II)V
    //   209: iload_2
    //   210: aload 7
    //   212: invokevirtual 89	java/lang/String:length	()I
    //   215: iadd
    //   216: istore_3
    //   217: aload 6
    //   219: iload_3
    //   220: iconst_0
    //   221: bastore
    //   222: aload 9
    //   224: aload 6
    //   226: iconst_0
    //   227: iload_3
    //   228: iconst_1
    //   229: iadd
    //   230: invokevirtual 101	java/io/OutputStream:write	([BII)V
    //   233: iconst_0
    //   234: istore_2
    //   235: iload_2
    //   236: bipush 6
    //   238: if_icmpge +66 -> 304
    //   241: aload 8
    //   243: aload 6
    //   245: iload_2
    //   246: bipush 6
    //   248: iload_2
    //   249: isub
    //   250: invokevirtual 107	java/io/InputStream:read	([BII)I
    //   253: istore_3
    //   254: iload_3
    //   255: ifgt +42 -> 297
    //   258: new 69	org/jivesoftware/smack/proxy/ProxyException
    //   261: dup
    //   262: getstatic 75	org/jivesoftware/smack/proxy/ProxyInfo$ProxyType:SOCKS4	Lorg/jivesoftware/smack/proxy/ProxyInfo$ProxyType;
    //   265: ldc 109
    //   267: invokespecial 112	org/jivesoftware/smack/proxy/ProxyException:<init>	(Lorg/jivesoftware/smack/proxy/ProxyInfo$ProxyType;Ljava/lang/String;)V
    //   270: athrow
    //   271: astore_1
    //   272: aload 5
    //   274: ifnull +8 -> 282
    //   277: aload 5
    //   279: invokevirtual 115	java/net/Socket:close	()V
    //   282: new 69	org/jivesoftware/smack/proxy/ProxyException
    //   285: dup
    //   286: getstatic 75	org/jivesoftware/smack/proxy/ProxyInfo$ProxyType:SOCKS4	Lorg/jivesoftware/smack/proxy/ProxyInfo$ProxyType;
    //   289: aload_1
    //   290: invokevirtual 116	java/lang/Exception:toString	()Ljava/lang/String;
    //   293: invokespecial 112	org/jivesoftware/smack/proxy/ProxyException:<init>	(Lorg/jivesoftware/smack/proxy/ProxyInfo$ProxyType;Ljava/lang/String;)V
    //   296: athrow
    //   297: iload_2
    //   298: iload_3
    //   299: iadd
    //   300: istore_2
    //   301: goto -66 -> 235
    //   304: aload 6
    //   306: iconst_0
    //   307: baload
    //   308: ifeq +36 -> 344
    //   311: new 69	org/jivesoftware/smack/proxy/ProxyException
    //   314: dup
    //   315: getstatic 75	org/jivesoftware/smack/proxy/ProxyInfo$ProxyType:SOCKS4	Lorg/jivesoftware/smack/proxy/ProxyInfo$ProxyType;
    //   318: new 118	java/lang/StringBuilder
    //   321: dup
    //   322: invokespecial 119	java/lang/StringBuilder:<init>	()V
    //   325: ldc 121
    //   327: invokevirtual 125	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   330: aload 6
    //   332: iconst_0
    //   333: baload
    //   334: invokevirtual 128	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
    //   337: invokevirtual 129	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   340: invokespecial 112	org/jivesoftware/smack/proxy/ProxyException:<init>	(Lorg/jivesoftware/smack/proxy/ProxyInfo$ProxyType;Ljava/lang/String;)V
    //   343: athrow
    //   344: aload 6
    //   346: iconst_1
    //   347: baload
    //   348: istore_2
    //   349: iload_2
    //   350: bipush 90
    //   352: if_icmpeq +43 -> 395
    //   355: aload 5
    //   357: invokevirtual 115	java/net/Socket:close	()V
    //   360: new 118	java/lang/StringBuilder
    //   363: dup
    //   364: invokespecial 119	java/lang/StringBuilder:<init>	()V
    //   367: ldc 131
    //   369: invokevirtual 125	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   372: aload 6
    //   374: iconst_1
    //   375: baload
    //   376: invokevirtual 128	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
    //   379: invokevirtual 129	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   382: astore_1
    //   383: new 69	org/jivesoftware/smack/proxy/ProxyException
    //   386: dup
    //   387: getstatic 75	org/jivesoftware/smack/proxy/ProxyInfo$ProxyType:SOCKS4	Lorg/jivesoftware/smack/proxy/ProxyInfo$ProxyType;
    //   390: aload_1
    //   391: invokespecial 112	org/jivesoftware/smack/proxy/ProxyException:<init>	(Lorg/jivesoftware/smack/proxy/ProxyInfo$ProxyType;Ljava/lang/String;)V
    //   394: athrow
    //   395: aload 8
    //   397: iconst_2
    //   398: newarray byte
    //   400: iconst_0
    //   401: iconst_2
    //   402: invokevirtual 107	java/io/InputStream:read	([BII)I
    //   405: pop
    //   406: aload 5
    //   408: areturn
    //   409: astore_1
    //   410: goto -50 -> 360
    //   413: astore 5
    //   415: goto -133 -> 282
    //   418: astore_1
    //   419: aload 6
    //   421: astore 5
    //   423: goto -151 -> 272
    //   426: astore_1
    //   427: goto -244 -> 183
    //   430: astore_1
    //   431: goto -265 -> 166
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	434	0	this	Socks4ProxySocketFactory
    //   0	434	1	paramString	String
    //   0	434	2	paramInt	int
    //   19	281	3	i	int
    //   79	66	4	j	int
    //   10	397	5	localObject	java.lang.Object
    //   413	1	5	localException	java.lang.Exception
    //   421	1	5	arrayOfByte1	byte[]
    //   1	419	6	arrayOfByte2	byte[]
    //   27	184	7	str	String
    //   54	342	8	localInputStream	java.io.InputStream
    //   61	162	9	localOutputStream	java.io.OutputStream
    // Exception table:
    //   from	to	target	type
    //   122	130	165	java/net/UnknownHostException
    //   49	76	182	java/lang/RuntimeException
    //   122	130	182	java/lang/RuntimeException
    //   137	141	182	java/lang/RuntimeException
    //   166	182	182	java/lang/RuntimeException
    //   192	217	182	java/lang/RuntimeException
    //   222	233	182	java/lang/RuntimeException
    //   241	254	182	java/lang/RuntimeException
    //   258	271	182	java/lang/RuntimeException
    //   311	344	182	java/lang/RuntimeException
    //   355	360	182	java/lang/RuntimeException
    //   360	395	182	java/lang/RuntimeException
    //   395	406	182	java/lang/RuntimeException
    //   49	76	271	java/lang/Exception
    //   122	130	271	java/lang/Exception
    //   137	141	271	java/lang/Exception
    //   166	182	271	java/lang/Exception
    //   192	217	271	java/lang/Exception
    //   222	233	271	java/lang/Exception
    //   241	254	271	java/lang/Exception
    //   258	271	271	java/lang/Exception
    //   311	344	271	java/lang/Exception
    //   360	395	271	java/lang/Exception
    //   395	406	271	java/lang/Exception
    //   355	360	409	java/lang/Exception
    //   277	282	413	java/lang/Exception
    //   37	49	418	java/lang/Exception
    //   37	49	426	java/lang/RuntimeException
    //   137	141	430	java/net/UnknownHostException
  }
  
  public Socket createSocket(String paramString, int paramInt)
    throws IOException, UnknownHostException
  {
    return socks4ProxifiedSocket(paramString, paramInt);
  }
  
  public Socket createSocket(String paramString, int paramInt1, InetAddress paramInetAddress, int paramInt2)
    throws IOException, UnknownHostException
  {
    return socks4ProxifiedSocket(paramString, paramInt1);
  }
  
  public Socket createSocket(InetAddress paramInetAddress, int paramInt)
    throws IOException
  {
    return socks4ProxifiedSocket(paramInetAddress.getHostAddress(), paramInt);
  }
  
  public Socket createSocket(InetAddress paramInetAddress1, int paramInt1, InetAddress paramInetAddress2, int paramInt2)
    throws IOException
  {
    return socks4ProxifiedSocket(paramInetAddress1.getHostAddress(), paramInt1);
  }
}


/* Location:           C:\Users\Administrator\Documents\ds\反编译\AndroidKiller_v1.3.1\projects\Jungledsq_ppc\ProjectSrc\smali\
 * Qualified Name:     org.jivesoftware.smack.proxy.Socks4ProxySocketFactory
 * JD-Core Version:    0.7.0.1
 */