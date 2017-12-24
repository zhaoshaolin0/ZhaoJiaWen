package org.jivesoftware.smackx.workgroup.user;

public abstract interface QueueListener
{
  public abstract void departedQueue();
  
  public abstract void joinedQueue();
  
  public abstract void queuePositionUpdated(int paramInt);
  
  public abstract void queueWaitTimeUpdated(int paramInt);
}


/* Location:           C:\Users\Administrator\Documents\ds\反编译\AndroidKiller_v1.3.1\projects\Jungledsq_ppc\ProjectSrc\smali\
 * Qualified Name:     org.jivesoftware.smackx.workgroup.user.QueueListener
 * JD-Core Version:    0.7.0.1
 */