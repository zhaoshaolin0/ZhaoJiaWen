package org.jivesoftware.smack.proxy;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringReader;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.net.SocketFactory;
import org.jivesoftware.smack.util.Base64;

class HTTPProxySocketFactory
  extends SocketFactory
{
  private static final Pattern RESPONSE_PATTERN = Pattern.compile("HTTP/\\S+\\s(\\d+)\\s(.*)\\s*");
  private ProxyInfo proxy;
  
  public HTTPProxySocketFactory(ProxyInfo paramProxyInfo)
  {
    this.proxy = paramProxyInfo;
  }
  
  private Socket httpProxifiedSocket(String paramString, int paramInt)
    throws IOException
  {
    String str1 = this.proxy.getProxyAddress();
    Socket localSocket = new Socket(str1, this.proxy.getProxyPort());
    Object localObject = "CONNECT " + paramString + ":" + paramInt;
    paramString = this.proxy.getProxyUsername();
    if (paramString == null) {}
    int j;
    int i;
    String str2;
    for (paramString = "";; paramString = "\r\nProxy-Authorization: Basic " + new String(Base64.encodeBytes(new StringBuilder().append(paramString).append(":").append(str2).toString().getBytes("UTF-8"))))
    {
      localSocket.getOutputStream().write(((String)localObject + " HTTP/1.1\r\nHost: " + (String)localObject + paramString + "\r\n\r\n").getBytes("UTF-8"));
      paramString = localSocket.getInputStream();
      localObject = new StringBuilder(100);
      j = 0;
      i = (char)paramString.read();
      ((StringBuilder)localObject).append(i);
      if (((StringBuilder)localObject).length() <= 1024) {
        break;
      }
      throw new ProxyException(ProxyInfo.ProxyType.HTTP, "Recieved header of >1024 characters from " + str1 + ", cancelling connection");
      str2 = this.proxy.getProxyPassword();
    }
    if (i == -1) {
      throw new ProxyException(ProxyInfo.ProxyType.HTTP);
    }
    if (((j == 0) || (j == 2)) && (i == 13)) {
      paramInt = j + 1;
    }
    for (;;)
    {
      j = paramInt;
      if (paramInt != 4) {
        break;
      }
      if (paramInt == 4) {
        break label387;
      }
      throw new ProxyException(ProxyInfo.ProxyType.HTTP, "Never received blank line from " + str1 + ", cancelling connection");
      if (((j == 1) || (j == 3)) && (i == 10)) {
        paramInt = j + 1;
      } else {
        paramInt = 0;
      }
    }
    label387:
    paramString = new BufferedReader(new StringReader(((StringBuilder)localObject).toString())).readLine();
    if (paramString == null) {
      throw new ProxyException(ProxyInfo.ProxyType.HTTP, "Empty proxy response from " + str1 + ", cancelling");
    }
    localObject = RESPONSE_PATTERN.matcher(paramString);
    if (!((Matcher)localObject).matches()) {
      throw new ProxyException(ProxyInfo.ProxyType.HTTP, "Unexpected proxy response from " + str1 + ": " + paramString);
    }
    if (Integer.parseInt(((Matcher)localObject).group(1)) != 200) {
      throw new ProxyException(ProxyInfo.ProxyType.HTTP);
    }
    return localSocket;
  }
  
  public Socket createSocket(String paramString, int paramInt)
    throws IOException, UnknownHostException
  {
    return httpProxifiedSocket(paramString, paramInt);
  }
  
  public Socket createSocket(String paramString, int paramInt1, InetAddress paramInetAddress, int paramInt2)
    throws IOException, UnknownHostException
  {
    return httpProxifiedSocket(paramString, paramInt1);
  }
  
  public Socket createSocket(InetAddress paramInetAddress, int paramInt)
    throws IOException
  {
    return httpProxifiedSocket(paramInetAddress.getHostAddress(), paramInt);
  }
  
  public Socket createSocket(InetAddress paramInetAddress1, int paramInt1, InetAddress paramInetAddress2, int paramInt2)
    throws IOException
  {
    return httpProxifiedSocket(paramInetAddress1.getHostAddress(), paramInt1);
  }
}


/* Location:           C:\Users\Administrator\Documents\ds\反编译\AndroidKiller_v1.3.1\projects\Jungledsq_ppc\ProjectSrc\smali\
 * Qualified Name:     org.jivesoftware.smack.proxy.HTTPProxySocketFactory
 * JD-Core Version:    0.7.0.1
 */