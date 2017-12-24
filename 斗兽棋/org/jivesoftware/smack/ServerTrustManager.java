package org.jivesoftware.smack;

import java.io.PrintStream;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.Principal;
import java.security.cert.CertificateException;
import java.security.cert.CertificateParsingException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.net.ssl.X509TrustManager;

class ServerTrustManager
  implements X509TrustManager
{
  private static Pattern cnPattern = Pattern.compile("(?i)(cn=)([^,]*)");
  private ConnectionConfiguration configuration;
  private String server;
  private KeyStore trustStore;
  
  /* Error */
  public ServerTrustManager(String paramString, ConnectionConfiguration paramConnectionConfiguration)
  {
    // Byte code:
    //   0: aload_0
    //   1: invokespecial 35	java/lang/Object:<init>	()V
    //   4: aload_0
    //   5: aload_2
    //   6: putfield 37	org/jivesoftware/smack/ServerTrustManager:configuration	Lorg/jivesoftware/smack/ConnectionConfiguration;
    //   9: aload_0
    //   10: aload_1
    //   11: putfield 39	org/jivesoftware/smack/ServerTrustManager:server	Ljava/lang/String;
    //   14: aconst_null
    //   15: astore_3
    //   16: aconst_null
    //   17: astore 5
    //   19: aload_3
    //   20: astore_1
    //   21: aload_0
    //   22: aload_2
    //   23: invokevirtual 45	org/jivesoftware/smack/ConnectionConfiguration:getTruststoreType	()Ljava/lang/String;
    //   26: invokestatic 51	java/security/KeyStore:getInstance	(Ljava/lang/String;)Ljava/security/KeyStore;
    //   29: putfield 53	org/jivesoftware/smack/ServerTrustManager:trustStore	Ljava/security/KeyStore;
    //   32: aload_3
    //   33: astore_1
    //   34: new 55	java/io/FileInputStream
    //   37: dup
    //   38: aload_2
    //   39: invokevirtual 58	org/jivesoftware/smack/ConnectionConfiguration:getTruststorePath	()Ljava/lang/String;
    //   42: invokespecial 61	java/io/FileInputStream:<init>	(Ljava/lang/String;)V
    //   45: astore_3
    //   46: aload_0
    //   47: getfield 53	org/jivesoftware/smack/ServerTrustManager:trustStore	Ljava/security/KeyStore;
    //   50: aload_3
    //   51: aload_2
    //   52: invokevirtual 64	org/jivesoftware/smack/ConnectionConfiguration:getTruststorePassword	()Ljava/lang/String;
    //   55: invokevirtual 70	java/lang/String:toCharArray	()[C
    //   58: invokevirtual 74	java/security/KeyStore:load	(Ljava/io/InputStream;[C)V
    //   61: aload_3
    //   62: ifnull +66 -> 128
    //   65: aload_3
    //   66: invokevirtual 79	java/io/InputStream:close	()V
    //   69: return
    //   70: astore_1
    //   71: return
    //   72: astore 4
    //   74: aload 5
    //   76: astore_3
    //   77: aload_3
    //   78: astore_1
    //   79: aload 4
    //   81: invokevirtual 82	java/lang/Exception:printStackTrace	()V
    //   84: aload_3
    //   85: astore_1
    //   86: aload_2
    //   87: iconst_0
    //   88: invokevirtual 86	org/jivesoftware/smack/ConnectionConfiguration:setVerifyRootCAEnabled	(Z)V
    //   91: aload_3
    //   92: ifnull -23 -> 69
    //   95: aload_3
    //   96: invokevirtual 79	java/io/InputStream:close	()V
    //   99: return
    //   100: astore_1
    //   101: return
    //   102: astore_2
    //   103: aload_1
    //   104: ifnull +7 -> 111
    //   107: aload_1
    //   108: invokevirtual 79	java/io/InputStream:close	()V
    //   111: aload_2
    //   112: athrow
    //   113: astore_1
    //   114: goto -3 -> 111
    //   117: astore_2
    //   118: aload_3
    //   119: astore_1
    //   120: goto -17 -> 103
    //   123: astore 4
    //   125: goto -48 -> 77
    //   128: return
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	129	0	this	ServerTrustManager
    //   0	129	1	paramString	String
    //   0	129	2	paramConnectionConfiguration	ConnectionConfiguration
    //   15	104	3	localObject1	Object
    //   72	8	4	localException1	java.lang.Exception
    //   123	1	4	localException2	java.lang.Exception
    //   17	58	5	localObject2	Object
    // Exception table:
    //   from	to	target	type
    //   65	69	70	java/io/IOException
    //   21	32	72	java/lang/Exception
    //   34	46	72	java/lang/Exception
    //   95	99	100	java/io/IOException
    //   21	32	102	finally
    //   34	46	102	finally
    //   79	84	102	finally
    //   86	91	102	finally
    //   107	111	113	java/io/IOException
    //   46	61	117	finally
    //   46	61	123	java/lang/Exception
  }
  
  public static List<String> getPeerIdentity(X509Certificate paramX509Certificate)
  {
    List localList = getSubjectAlternativeNames(paramX509Certificate);
    Object localObject = localList;
    if (localList.isEmpty())
    {
      paramX509Certificate = paramX509Certificate.getSubjectDN().getName();
      localObject = cnPattern.matcher(paramX509Certificate);
      if (((Matcher)localObject).find()) {
        paramX509Certificate = ((Matcher)localObject).group(2);
      }
      localObject = new ArrayList();
      ((List)localObject).add(paramX509Certificate);
    }
    return localObject;
  }
  
  private static List<String> getSubjectAlternativeNames(X509Certificate paramX509Certificate)
  {
    ArrayList localArrayList = new ArrayList();
    try
    {
      if (paramX509Certificate.getSubjectAlternativeNames() == null)
      {
        paramX509Certificate = Collections.emptyList();
        return paramX509Certificate;
      }
    }
    catch (CertificateParsingException paramX509Certificate)
    {
      paramX509Certificate.printStackTrace();
    }
    return localArrayList;
  }
  
  public void checkClientTrusted(X509Certificate[] paramArrayOfX509Certificate, String paramString)
    throws CertificateException
  {}
  
  public void checkServerTrusted(X509Certificate[] paramArrayOfX509Certificate, String paramString)
    throws CertificateException
  {
    int m = paramArrayOfX509Certificate.length;
    List localList = getPeerIdentity(paramArrayOfX509Certificate[0]);
    int i;
    if (this.configuration.isVerifyChainEnabled())
    {
      paramString = null;
      i = m - 1;
      for (;;)
      {
        if (i < 0) {
          break label148;
        }
        Object localObject = paramArrayOfX509Certificate[i];
        Principal localPrincipal = ((X509Certificate)localObject).getIssuerDN();
        localObject = ((X509Certificate)localObject).getSubjectDN();
        if ((paramString == null) || (localPrincipal.equals(paramString))) {
          try
          {
            paramString = paramArrayOfX509Certificate[(i + 1)].getPublicKey();
            paramArrayOfX509Certificate[i].verify(paramString);
            paramString = (String)localObject;
            i -= 1;
          }
          catch (GeneralSecurityException paramArrayOfX509Certificate)
          {
            throw new CertificateException("signature verification failed of " + localList);
          }
        }
      }
      throw new CertificateException("subject/issuer verification failed of " + localList);
    }
    label148:
    int k;
    if (this.configuration.isVerifyRootCAEnabled())
    {
      k = 0;
      for (;;)
      {
        try
        {
          if (this.trustStore.getCertificateAlias(paramArrayOfX509Certificate[(m - 1)]) == null) {
            continue;
          }
          i = 1;
          j = i;
          if (i == 0)
          {
            j = i;
            if (m == 1)
            {
              j = i;
              k = i;
              if (this.configuration.isSelfSignedCertificateEnabled())
              {
                k = i;
                System.out.println("Accepting self-signed certificate of remote server: " + localList);
                j = 1;
              }
            }
          }
        }
        catch (KeyStoreException paramString)
        {
          paramString.printStackTrace();
          int j = k;
          continue;
        }
        if (j != 0) {
          break label293;
        }
        throw new CertificateException("root certificate not trusted of " + localList);
        i = 0;
      }
    }
    label293:
    if (this.configuration.isNotMatchingDomainCheckEnabled()) {
      if ((localList.size() == 1) && (((String)localList.get(0)).startsWith("*.")))
      {
        paramString = ((String)localList.get(0)).replace("*.", "");
        if (!this.server.endsWith(paramString)) {
          throw new CertificateException("target verification failed of " + localList);
        }
      }
      else if (!localList.contains(this.server))
      {
        throw new CertificateException("target verification failed of " + localList);
      }
    }
    if (this.configuration.isExpiredCertificatesCheckEnabled())
    {
      paramString = new Date();
      i = 0;
      while (i < m) {
        try
        {
          paramArrayOfX509Certificate[i].checkValidity(paramString);
          i += 1;
        }
        catch (GeneralSecurityException paramArrayOfX509Certificate)
        {
          throw new CertificateException("invalid date of " + this.server);
        }
      }
    }
  }
  
  public X509Certificate[] getAcceptedIssuers()
  {
    return new X509Certificate[0];
  }
}


/* Location:           C:\Users\Administrator\Documents\ds\反编译\AndroidKiller_v1.3.1\projects\Jungledsq_ppc\ProjectSrc\smali\
 * Qualified Name:     org.jivesoftware.smack.ServerTrustManager
 * JD-Core Version:    0.7.0.1
 */