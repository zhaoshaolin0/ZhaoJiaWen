package org.jivesoftware.smack.proxy;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.Socket;
import java.net.UnknownHostException;
import javax.net.SocketFactory;

class DirectSocketFactory
  extends SocketFactory
{
  public Socket createSocket(String paramString, int paramInt)
    throws IOException, UnknownHostException
  {
    Socket localSocket = new Socket(Proxy.NO_PROXY);
    localSocket.connect(new InetSocketAddress(paramString, paramInt));
    return localSocket;
  }
  
  public Socket createSocket(String paramString, int paramInt1, InetAddress paramInetAddress, int paramInt2)
    throws IOException, UnknownHostException
  {
    return new Socket(paramString, paramInt1, paramInetAddress, paramInt2);
  }
  
  public Socket createSocket(InetAddress paramInetAddress, int paramInt)
    throws IOException
  {
    Socket localSocket = new Socket(Proxy.NO_PROXY);
    localSocket.connect(new InetSocketAddress(paramInetAddress, paramInt));
    return localSocket;
  }
  
  public Socket createSocket(InetAddress paramInetAddress1, int paramInt1, InetAddress paramInetAddress2, int paramInt2)
    throws IOException
  {
    return new Socket(paramInetAddress1, paramInt1, paramInetAddress2, paramInt2);
  }
}


/* Location:           C:\Users\Administrator\Documents\ds\反编译\AndroidKiller_v1.3.1\projects\Jungledsq_ppc\ProjectSrc\smali\
 * Qualified Name:     org.jivesoftware.smack.proxy.DirectSocketFactory
 * JD-Core Version:    0.7.0.1
 */