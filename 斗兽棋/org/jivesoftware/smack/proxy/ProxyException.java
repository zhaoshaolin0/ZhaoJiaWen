package org.jivesoftware.smack.proxy;

import java.io.IOException;

public class ProxyException
  extends IOException
{
  public ProxyException(ProxyInfo.ProxyType paramProxyType)
  {
    super("Proxy Exception " + paramProxyType.toString() + " : " + "Unknown Error");
  }
  
  public ProxyException(ProxyInfo.ProxyType paramProxyType, String paramString)
  {
    super("Proxy Exception " + paramProxyType.toString() + " : " + paramString);
  }
  
  public ProxyException(ProxyInfo.ProxyType paramProxyType, String paramString, Throwable paramThrowable)
  {
    super("Proxy Exception " + paramProxyType.toString() + " : " + paramString + ", " + paramThrowable);
  }
}


/* Location:           C:\Users\Administrator\Documents\ds\反编译\AndroidKiller_v1.3.1\projects\Jungledsq_ppc\ProjectSrc\smali\
 * Qualified Name:     org.jivesoftware.smack.proxy.ProxyException
 * JD-Core Version:    0.7.0.1
 */