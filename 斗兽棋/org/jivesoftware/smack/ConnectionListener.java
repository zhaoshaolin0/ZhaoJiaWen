package org.jivesoftware.smack;

public abstract interface ConnectionListener
{
  public abstract void connectionClosed();
  
  public abstract void connectionClosedOnError(Exception paramException);
  
  public abstract void reconnectingIn(int paramInt);
  
  public abstract void reconnectionFailed(Exception paramException);
  
  public abstract void reconnectionSuccessful();
}


/* Location:           C:\Users\Administrator\Documents\ds\反编译\AndroidKiller_v1.3.1\projects\Jungledsq_ppc\ProjectSrc\smali\
 * Qualified Name:     org.jivesoftware.smack.ConnectionListener
 * JD-Core Version:    0.7.0.1
 */