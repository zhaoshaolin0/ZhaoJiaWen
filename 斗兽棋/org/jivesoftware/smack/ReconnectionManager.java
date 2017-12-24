package org.jivesoftware.smack;

import java.util.Collection;
import java.util.Iterator;
import org.jivesoftware.smack.packet.StreamError;

public class ReconnectionManager
  implements ConnectionListener
{
  private XMPPConnection connection;
  boolean done = false;
  
  static
  {
    XMPPConnection.addConnectionCreationListener(new ConnectionCreationListener()
    {
      public void connectionCreated(XMPPConnection paramAnonymousXMPPConnection)
      {
        paramAnonymousXMPPConnection.addConnectionListener(new ReconnectionManager(paramAnonymousXMPPConnection, null));
      }
    });
  }
  
  private ReconnectionManager(XMPPConnection paramXMPPConnection)
  {
    this.connection = paramXMPPConnection;
  }
  
  private boolean isReconnectionAllowed()
  {
    return (!this.done) && (!this.connection.isConnected()) && (this.connection.getConfiguration().isReconnectionAllowed()) && (this.connection.packetReader != null);
  }
  
  public void connectionClosed()
  {
    this.done = true;
  }
  
  public void connectionClosedOnError(Exception paramException)
  {
    this.done = false;
    if ((paramException instanceof XMPPException))
    {
      paramException = ((XMPPException)paramException).getStreamError();
      if ((paramException == null) || (!"conflict".equals(paramException.getCode()))) {}
    }
    while (!isReconnectionAllowed()) {
      return;
    }
    reconnect();
  }
  
  protected void notifyAttemptToReconnectIn(int paramInt)
  {
    if (isReconnectionAllowed())
    {
      Iterator localIterator = this.connection.packetReader.connectionListeners.iterator();
      while (localIterator.hasNext()) {
        ((ConnectionListener)localIterator.next()).reconnectingIn(paramInt);
      }
    }
  }
  
  protected void notifyReconnectionFailed(Exception paramException)
  {
    if (isReconnectionAllowed())
    {
      Iterator localIterator = this.connection.packetReader.connectionListeners.iterator();
      while (localIterator.hasNext()) {
        ((ConnectionListener)localIterator.next()).reconnectionFailed(paramException);
      }
    }
  }
  
  protected void reconnect()
  {
    if (isReconnectionAllowed())
    {
      Thread local2 = new Thread()
      {
        private int attempts = 0;
        
        private int timeDelay()
        {
          if (this.attempts > 13) {
            return 300;
          }
          if (this.attempts > 7) {
            return 60;
          }
          return 10;
        }
        
        public void run()
        {
          while (ReconnectionManager.this.isReconnectionAllowed())
          {
            int i = timeDelay();
            while ((ReconnectionManager.this.isReconnectionAllowed()) && (i > 0))
            {
              int j = i;
              try
              {
                Thread.sleep(1000L);
                i -= 1;
                j = i;
                ReconnectionManager.this.notifyAttemptToReconnectIn(i);
              }
              catch (InterruptedException localInterruptedException)
              {
                localInterruptedException.printStackTrace();
                ReconnectionManager.this.notifyReconnectionFailed(localInterruptedException);
                i = j;
              }
            }
            try
            {
              if (ReconnectionManager.this.isReconnectionAllowed()) {
                ReconnectionManager.this.connection.connect();
              }
            }
            catch (XMPPException localXMPPException)
            {
              ReconnectionManager.this.notifyReconnectionFailed(localXMPPException);
            }
          }
        }
      };
      local2.setName("Smack Reconnection Manager");
      local2.setDaemon(true);
      local2.start();
    }
  }
  
  public void reconnectingIn(int paramInt) {}
  
  public void reconnectionFailed(Exception paramException) {}
  
  public void reconnectionSuccessful() {}
}


/* Location:           C:\Users\Administrator\Documents\ds\反编译\AndroidKiller_v1.3.1\projects\Jungledsq_ppc\ProjectSrc\smali\
 * Qualified Name:     org.jivesoftware.smack.ReconnectionManager
 * JD-Core Version:    0.7.0.1
 */