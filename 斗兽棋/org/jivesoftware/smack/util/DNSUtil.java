package org.jivesoftware.smack.util;

import java.util.Hashtable;
import java.util.Map;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;

public class DNSUtil
{
  private static Map cache = new Cache(100, 600000L);
  private static DirContext context;
  
  static
  {
    try
    {
      Hashtable localHashtable = new Hashtable();
      localHashtable.put("java.naming.factory.initial", "com.sun.jndi.dns.DnsContextFactory");
      context = new InitialDirContext(localHashtable);
      return;
    }
    catch (Exception localException) {}
  }
  
  public static HostAddress resolveXMPPDomain(String paramString)
  {
    if (context == null) {
      return new HostAddress(paramString, 5222, null);
    }
    String str = "c" + paramString;
    if (cache.containsKey(str))
    {
      localObject = (HostAddress)cache.get(str);
      if (localObject != null) {
        return localObject;
      }
    }
    localObject = paramString;
    int j = 5222;
    int i = j;
    try
    {
      paramString = ((String)context.getAttributes("_xmpp-client._tcp." + paramString, new String[] { "SRV" }).get("SRV").get()).split(" ");
      i = j;
      j = Integer.parseInt(paramString[(paramString.length - 2)]);
      i = j;
      paramString = paramString[(paramString.length - 1)];
      i = j;
    }
    catch (Exception paramString)
    {
      for (;;)
      {
        paramString = (String)localObject;
      }
    }
    localObject = paramString;
    if (paramString.endsWith(".")) {
      localObject = paramString.substring(0, paramString.length() - 1);
    }
    paramString = new HostAddress((String)localObject, i, null);
    cache.put(str, paramString);
    return paramString;
  }
  
  public static HostAddress resolveXMPPServerDomain(String paramString)
  {
    if (context == null) {
      return new HostAddress(paramString, 5269, null);
    }
    String str = "s" + paramString;
    Object localObject1;
    if (cache.containsKey(str))
    {
      localObject1 = (HostAddress)cache.get(str);
      if (localObject1 != null) {
        return localObject1;
      }
    }
    j = 5269;
    try
    {
      localObject1 = ((String)context.getAttributes("_xmpp-server._tcp." + paramString, new String[] { "SRV" }).get("SRV").get()).split(" ");
      i = Integer.parseInt(localObject1[(localObject1.length - 2)]);
    }
    catch (Exception localException1)
    {
      for (;;)
      {
        label174:
        i = j;
        try
        {
          localObject2 = ((String)context.getAttributes("_jabber._tcp." + paramString, new String[] { "SRV" }).get("SRV").get()).split(" ");
          j = Integer.parseInt(localObject2[(localObject2.length - 2)]);
        }
        catch (Exception localException2)
        {
          try
          {
            Object localObject2 = localObject2[(localObject2.length - 1)];
            i = j;
            paramString = (String)localObject2;
          }
          catch (Exception localException3)
          {
            for (;;)
            {
              i = j;
            }
          }
          localException2 = localException2;
        }
      }
    }
    try
    {
      localObject1 = localObject1[(localObject1.length - 1)];
      paramString = (String)localObject1;
      if (!paramString.endsWith(".")) {
        break label297;
      }
      paramString = paramString.substring(0, paramString.length() - 1);
    }
    catch (Exception localException4)
    {
      break label202;
      break label174;
    }
    paramString = new HostAddress(paramString, i, null);
    cache.put(str, paramString);
    return paramString;
  }
  
  public static class HostAddress
  {
    private String host;
    private int port;
    
    private HostAddress(String paramString, int paramInt)
    {
      this.host = paramString;
      this.port = paramInt;
    }
    
    public boolean equals(Object paramObject)
    {
      if (this == paramObject) {
        return true;
      }
      if (!(paramObject instanceof HostAddress)) {
        return false;
      }
      paramObject = (HostAddress)paramObject;
      if (!this.host.equals(paramObject.host)) {
        return false;
      }
      return this.port == paramObject.port;
    }
    
    public String getHost()
    {
      return this.host;
    }
    
    public int getPort()
    {
      return this.port;
    }
    
    public String toString()
    {
      return this.host + ":" + this.port;
    }
  }
}


/* Location:           C:\Users\Administrator\Documents\ds\反编译\AndroidKiller_v1.3.1\projects\Jungledsq_ppc\ProjectSrc\smali\
 * Qualified Name:     org.jivesoftware.smack.util.DNSUtil
 * JD-Core Version:    0.7.0.1
 */