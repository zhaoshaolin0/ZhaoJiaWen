package org.jivesoftware.smack.proxy;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import javax.net.SocketFactory;

public class Socks5ProxySocketFactory
  extends SocketFactory
{
  private ProxyInfo proxy;
  
  public Socks5ProxySocketFactory(ProxyInfo paramProxyInfo)
  {
    this.proxy = paramProxyInfo;
  }
  
  private void fill(InputStream paramInputStream, byte[] paramArrayOfByte, int paramInt)
    throws IOException
  {
    int i = 0;
    while (i < paramInt)
    {
      int j = paramInputStream.read(paramArrayOfByte, i, paramInt - i);
      if (j <= 0) {
        throw new ProxyException(ProxyInfo.ProxyType.SOCKS5, "stream is closed");
      }
      i += j;
    }
  }
  
  /* Error */
  private Socket socks5ProxifiedSocket(String paramString, int paramInt)
    throws IOException
  {
    // Byte code:
    //   0: aconst_null
    //   1: astore 7
    //   3: aload_0
    //   4: getfield 13	org/jivesoftware/smack/proxy/Socks5ProxySocketFactory:proxy	Lorg/jivesoftware/smack/proxy/ProxyInfo;
    //   7: invokevirtual 50	org/jivesoftware/smack/proxy/ProxyInfo:getProxyAddress	()Ljava/lang/String;
    //   10: astore 6
    //   12: aload_0
    //   13: getfield 13	org/jivesoftware/smack/proxy/Socks5ProxySocketFactory:proxy	Lorg/jivesoftware/smack/proxy/ProxyInfo;
    //   16: invokevirtual 54	org/jivesoftware/smack/proxy/ProxyInfo:getProxyPort	()I
    //   19: istore_3
    //   20: aload_0
    //   21: getfield 13	org/jivesoftware/smack/proxy/Socks5ProxySocketFactory:proxy	Lorg/jivesoftware/smack/proxy/ProxyInfo;
    //   24: invokevirtual 57	org/jivesoftware/smack/proxy/ProxyInfo:getProxyUsername	()Ljava/lang/String;
    //   27: astore 8
    //   29: aload_0
    //   30: getfield 13	org/jivesoftware/smack/proxy/Socks5ProxySocketFactory:proxy	Lorg/jivesoftware/smack/proxy/ProxyInfo;
    //   33: invokevirtual 60	org/jivesoftware/smack/proxy/ProxyInfo:getProxyPassword	()Ljava/lang/String;
    //   36: astore 9
    //   38: new 62	java/net/Socket
    //   41: dup
    //   42: aload 6
    //   44: iload_3
    //   45: invokespecial 65	java/net/Socket:<init>	(Ljava/lang/String;I)V
    //   48: astore 6
    //   50: aload 6
    //   52: invokevirtual 69	java/net/Socket:getInputStream	()Ljava/io/InputStream;
    //   55: astore 10
    //   57: aload 6
    //   59: invokevirtual 73	java/net/Socket:getOutputStream	()Ljava/io/OutputStream;
    //   62: astore 11
    //   64: aload 6
    //   66: iconst_1
    //   67: invokevirtual 77	java/net/Socket:setTcpNoDelay	(Z)V
    //   70: sipush 1024
    //   73: newarray byte
    //   75: astore 7
    //   77: iconst_0
    //   78: iconst_1
    //   79: iadd
    //   80: istore 4
    //   82: aload 7
    //   84: iconst_0
    //   85: iconst_5
    //   86: bastore
    //   87: iload 4
    //   89: iconst_1
    //   90: iadd
    //   91: istore_3
    //   92: aload 7
    //   94: iload 4
    //   96: iconst_2
    //   97: bastore
    //   98: iload_3
    //   99: iconst_1
    //   100: iadd
    //   101: istore 4
    //   103: aload 7
    //   105: iload_3
    //   106: iconst_0
    //   107: bastore
    //   108: aload 7
    //   110: iload 4
    //   112: iconst_2
    //   113: bastore
    //   114: aload 11
    //   116: aload 7
    //   118: iconst_0
    //   119: iload 4
    //   121: iconst_1
    //   122: iadd
    //   123: invokevirtual 83	java/io/OutputStream:write	([BII)V
    //   126: aload_0
    //   127: aload 10
    //   129: aload 7
    //   131: iconst_2
    //   132: invokespecial 85	org/jivesoftware/smack/proxy/Socks5ProxySocketFactory:fill	(Ljava/io/InputStream;[BI)V
    //   135: iconst_0
    //   136: istore 4
    //   138: aload 7
    //   140: iconst_1
    //   141: baload
    //   142: istore 5
    //   144: iload 4
    //   146: istore_3
    //   147: iload 5
    //   149: sipush 255
    //   152: iand
    //   153: tableswitch	default:+27 -> 180, 0:+55->208, 1:+30->183, 2:+60->213
    //   181: iconst_1
    //   182: istore_3
    //   183: iload_3
    //   184: ifne +164 -> 348
    //   187: aload 6
    //   189: invokevirtual 88	java/net/Socket:close	()V
    //   192: new 26	org/jivesoftware/smack/proxy/ProxyException
    //   195: dup
    //   196: getstatic 32	org/jivesoftware/smack/proxy/ProxyInfo$ProxyType:SOCKS5	Lorg/jivesoftware/smack/proxy/ProxyInfo$ProxyType;
    //   199: ldc 90
    //   201: invokespecial 37	org/jivesoftware/smack/proxy/ProxyException:<init>	(Lorg/jivesoftware/smack/proxy/ProxyInfo$ProxyType;Ljava/lang/String;)V
    //   204: athrow
    //   205: astore_1
    //   206: aload_1
    //   207: athrow
    //   208: iconst_1
    //   209: istore_3
    //   210: goto -27 -> 183
    //   213: iload 4
    //   215: istore_3
    //   216: aload 8
    //   218: ifnull -35 -> 183
    //   221: iload 4
    //   223: istore_3
    //   224: aload 9
    //   226: ifnull -43 -> 183
    //   229: iconst_0
    //   230: iconst_1
    //   231: iadd
    //   232: istore_3
    //   233: aload 7
    //   235: iconst_0
    //   236: iconst_1
    //   237: bastore
    //   238: aload 7
    //   240: iload_3
    //   241: aload 8
    //   243: invokevirtual 95	java/lang/String:length	()I
    //   246: i2b
    //   247: bastore
    //   248: aload 8
    //   250: invokevirtual 99	java/lang/String:getBytes	()[B
    //   253: iconst_0
    //   254: aload 7
    //   256: iload_3
    //   257: iconst_1
    //   258: iadd
    //   259: aload 8
    //   261: invokevirtual 95	java/lang/String:length	()I
    //   264: invokestatic 105	java/lang/System:arraycopy	(Ljava/lang/Object;ILjava/lang/Object;II)V
    //   267: aload 8
    //   269: invokevirtual 95	java/lang/String:length	()I
    //   272: iconst_2
    //   273: iadd
    //   274: istore_3
    //   275: iload_3
    //   276: iconst_1
    //   277: iadd
    //   278: istore 5
    //   280: aload 7
    //   282: iload_3
    //   283: aload 9
    //   285: invokevirtual 95	java/lang/String:length	()I
    //   288: i2b
    //   289: bastore
    //   290: aload 9
    //   292: invokevirtual 99	java/lang/String:getBytes	()[B
    //   295: iconst_0
    //   296: aload 7
    //   298: iload 5
    //   300: aload 9
    //   302: invokevirtual 95	java/lang/String:length	()I
    //   305: invokestatic 105	java/lang/System:arraycopy	(Ljava/lang/Object;ILjava/lang/Object;II)V
    //   308: aload 11
    //   310: aload 7
    //   312: iconst_0
    //   313: iload 5
    //   315: aload 9
    //   317: invokevirtual 95	java/lang/String:length	()I
    //   320: iadd
    //   321: invokevirtual 83	java/io/OutputStream:write	([BII)V
    //   324: aload_0
    //   325: aload 10
    //   327: aload 7
    //   329: iconst_2
    //   330: invokespecial 85	org/jivesoftware/smack/proxy/Socks5ProxySocketFactory:fill	(Ljava/io/InputStream;[BI)V
    //   333: iload 4
    //   335: istore_3
    //   336: aload 7
    //   338: iconst_1
    //   339: baload
    //   340: ifne -157 -> 183
    //   343: iconst_1
    //   344: istore_3
    //   345: goto -162 -> 183
    //   348: iconst_0
    //   349: iconst_1
    //   350: iadd
    //   351: istore_3
    //   352: aload 7
    //   354: iconst_0
    //   355: iconst_5
    //   356: bastore
    //   357: iload_3
    //   358: iconst_1
    //   359: iadd
    //   360: istore 4
    //   362: aload 7
    //   364: iload_3
    //   365: iconst_1
    //   366: bastore
    //   367: iload 4
    //   369: iconst_1
    //   370: iadd
    //   371: istore_3
    //   372: aload 7
    //   374: iload 4
    //   376: iconst_0
    //   377: bastore
    //   378: aload_1
    //   379: invokevirtual 99	java/lang/String:getBytes	()[B
    //   382: astore_1
    //   383: aload_1
    //   384: arraylength
    //   385: istore 4
    //   387: iload_3
    //   388: iconst_1
    //   389: iadd
    //   390: istore 5
    //   392: aload 7
    //   394: iload_3
    //   395: iconst_3
    //   396: bastore
    //   397: aload 7
    //   399: iload 5
    //   401: iload 4
    //   403: i2b
    //   404: bastore
    //   405: aload_1
    //   406: iconst_0
    //   407: aload 7
    //   409: iload 5
    //   411: iconst_1
    //   412: iadd
    //   413: iload 4
    //   415: invokestatic 105	java/lang/System:arraycopy	(Ljava/lang/Object;ILjava/lang/Object;II)V
    //   418: iload 4
    //   420: iconst_5
    //   421: iadd
    //   422: istore_3
    //   423: iload_3
    //   424: iconst_1
    //   425: iadd
    //   426: istore 4
    //   428: aload 7
    //   430: iload_3
    //   431: iload_2
    //   432: bipush 8
    //   434: iushr
    //   435: i2b
    //   436: bastore
    //   437: aload 7
    //   439: iload 4
    //   441: iload_2
    //   442: sipush 255
    //   445: iand
    //   446: i2b
    //   447: bastore
    //   448: aload 11
    //   450: aload 7
    //   452: iconst_0
    //   453: iload 4
    //   455: iconst_1
    //   456: iadd
    //   457: invokevirtual 83	java/io/OutputStream:write	([BII)V
    //   460: aload_0
    //   461: aload 10
    //   463: aload 7
    //   465: iconst_4
    //   466: invokespecial 85	org/jivesoftware/smack/proxy/Socks5ProxySocketFactory:fill	(Ljava/io/InputStream;[BI)V
    //   469: aload 7
    //   471: iconst_1
    //   472: baload
    //   473: istore_2
    //   474: iload_2
    //   475: ifeq +97 -> 572
    //   478: aload 6
    //   480: invokevirtual 88	java/net/Socket:close	()V
    //   483: new 26	org/jivesoftware/smack/proxy/ProxyException
    //   486: dup
    //   487: getstatic 32	org/jivesoftware/smack/proxy/ProxyInfo$ProxyType:SOCKS5	Lorg/jivesoftware/smack/proxy/ProxyInfo$ProxyType;
    //   490: new 107	java/lang/StringBuilder
    //   493: dup
    //   494: invokespecial 108	java/lang/StringBuilder:<init>	()V
    //   497: ldc 110
    //   499: invokevirtual 114	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   502: aload 7
    //   504: iconst_1
    //   505: baload
    //   506: invokevirtual 117	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
    //   509: invokevirtual 120	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   512: invokespecial 37	org/jivesoftware/smack/proxy/ProxyException:<init>	(Lorg/jivesoftware/smack/proxy/ProxyInfo$ProxyType;Ljava/lang/String;)V
    //   515: athrow
    //   516: astore_1
    //   517: aload 6
    //   519: ifnull +8 -> 527
    //   522: aload 6
    //   524: invokevirtual 88	java/net/Socket:close	()V
    //   527: new 107	java/lang/StringBuilder
    //   530: dup
    //   531: invokespecial 108	java/lang/StringBuilder:<init>	()V
    //   534: ldc 122
    //   536: invokevirtual 114	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   539: aload_1
    //   540: invokevirtual 123	java/lang/Exception:toString	()Ljava/lang/String;
    //   543: invokevirtual 114	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   546: invokevirtual 120	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   549: astore 6
    //   551: aload_1
    //   552: instanceof 125
    //   555: ifeq +113 -> 668
    //   558: new 26	org/jivesoftware/smack/proxy/ProxyException
    //   561: dup
    //   562: getstatic 32	org/jivesoftware/smack/proxy/ProxyInfo$ProxyType:SOCKS5	Lorg/jivesoftware/smack/proxy/ProxyInfo$ProxyType;
    //   565: aload 6
    //   567: aload_1
    //   568: invokespecial 128	org/jivesoftware/smack/proxy/ProxyException:<init>	(Lorg/jivesoftware/smack/proxy/ProxyInfo$ProxyType;Ljava/lang/String;Ljava/lang/Throwable;)V
    //   571: athrow
    //   572: aload 7
    //   574: iconst_3
    //   575: baload
    //   576: sipush 255
    //   579: iand
    //   580: tableswitch	default:+123 -> 703, 1:+32->612, 2:+123->703, 3:+45->625, 4:+75->655
    //   613: aload 10
    //   615: aload 7
    //   617: bipush 6
    //   619: invokespecial 85	org/jivesoftware/smack/proxy/Socks5ProxySocketFactory:fill	(Ljava/io/InputStream;[BI)V
    //   622: aload 6
    //   624: areturn
    //   625: aload_0
    //   626: aload 10
    //   628: aload 7
    //   630: iconst_1
    //   631: invokespecial 85	org/jivesoftware/smack/proxy/Socks5ProxySocketFactory:fill	(Ljava/io/InputStream;[BI)V
    //   634: aload_0
    //   635: aload 10
    //   637: aload 7
    //   639: aload 7
    //   641: iconst_0
    //   642: baload
    //   643: sipush 255
    //   646: iand
    //   647: iconst_2
    //   648: iadd
    //   649: invokespecial 85	org/jivesoftware/smack/proxy/Socks5ProxySocketFactory:fill	(Ljava/io/InputStream;[BI)V
    //   652: aload 6
    //   654: areturn
    //   655: aload_0
    //   656: aload 10
    //   658: aload 7
    //   660: bipush 18
    //   662: invokespecial 85	org/jivesoftware/smack/proxy/Socks5ProxySocketFactory:fill	(Ljava/io/InputStream;[BI)V
    //   665: aload 6
    //   667: areturn
    //   668: new 18	java/io/IOException
    //   671: dup
    //   672: aload 6
    //   674: invokespecial 131	java/io/IOException:<init>	(Ljava/lang/String;)V
    //   677: athrow
    //   678: astore_1
    //   679: goto -487 -> 192
    //   682: astore_1
    //   683: goto -200 -> 483
    //   686: astore 6
    //   688: goto -161 -> 527
    //   691: astore_1
    //   692: aload 7
    //   694: astore 6
    //   696: goto -179 -> 517
    //   699: astore_1
    //   700: goto -494 -> 206
    //   703: aload 6
    //   705: areturn
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	706	0	this	Socks5ProxySocketFactory
    //   0	706	1	paramString	String
    //   0	706	2	paramInt	int
    //   19	412	3	i	int
    //   80	377	4	j	int
    //   142	271	5	k	int
    //   10	663	6	localObject	java.lang.Object
    //   686	1	6	localException	java.lang.Exception
    //   694	10	6	arrayOfByte1	byte[]
    //   1	692	7	arrayOfByte2	byte[]
    //   27	241	8	str1	String
    //   36	280	9	str2	String
    //   55	602	10	localInputStream	InputStream
    //   62	387	11	localOutputStream	java.io.OutputStream
    // Exception table:
    //   from	to	target	type
    //   50	77	205	java/lang/RuntimeException
    //   114	135	205	java/lang/RuntimeException
    //   187	192	205	java/lang/RuntimeException
    //   192	205	205	java/lang/RuntimeException
    //   238	275	205	java/lang/RuntimeException
    //   280	333	205	java/lang/RuntimeException
    //   378	387	205	java/lang/RuntimeException
    //   405	418	205	java/lang/RuntimeException
    //   448	469	205	java/lang/RuntimeException
    //   478	483	205	java/lang/RuntimeException
    //   483	516	205	java/lang/RuntimeException
    //   612	622	205	java/lang/RuntimeException
    //   625	652	205	java/lang/RuntimeException
    //   655	665	205	java/lang/RuntimeException
    //   50	77	516	java/lang/Exception
    //   114	135	516	java/lang/Exception
    //   192	205	516	java/lang/Exception
    //   238	275	516	java/lang/Exception
    //   280	333	516	java/lang/Exception
    //   378	387	516	java/lang/Exception
    //   405	418	516	java/lang/Exception
    //   448	469	516	java/lang/Exception
    //   483	516	516	java/lang/Exception
    //   612	622	516	java/lang/Exception
    //   625	652	516	java/lang/Exception
    //   655	665	516	java/lang/Exception
    //   187	192	678	java/lang/Exception
    //   478	483	682	java/lang/Exception
    //   522	527	686	java/lang/Exception
    //   38	50	691	java/lang/Exception
    //   38	50	699	java/lang/RuntimeException
  }
  
  public Socket createSocket(String paramString, int paramInt)
    throws IOException, UnknownHostException
  {
    return socks5ProxifiedSocket(paramString, paramInt);
  }
  
  public Socket createSocket(String paramString, int paramInt1, InetAddress paramInetAddress, int paramInt2)
    throws IOException, UnknownHostException
  {
    return socks5ProxifiedSocket(paramString, paramInt1);
  }
  
  public Socket createSocket(InetAddress paramInetAddress, int paramInt)
    throws IOException
  {
    return socks5ProxifiedSocket(paramInetAddress.getHostAddress(), paramInt);
  }
  
  public Socket createSocket(InetAddress paramInetAddress1, int paramInt1, InetAddress paramInetAddress2, int paramInt2)
    throws IOException
  {
    return socks5ProxifiedSocket(paramInetAddress1.getHostAddress(), paramInt1);
  }
}


/* Location:           C:\Users\Administrator\Documents\ds\反编译\AndroidKiller_v1.3.1\projects\Jungledsq_ppc\ProjectSrc\smali\
 * Qualified Name:     org.jivesoftware.smack.proxy.Socks5ProxySocketFactory
 * JD-Core Version:    0.7.0.1
 */