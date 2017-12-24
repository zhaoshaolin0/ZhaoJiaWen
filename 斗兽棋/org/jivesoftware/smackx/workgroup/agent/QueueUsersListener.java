package org.jivesoftware.smackx.workgroup.agent;

import java.util.Date;
import java.util.Set;

public abstract interface QueueUsersListener
{
  public abstract void averageWaitTimeUpdated(WorkgroupQueue paramWorkgroupQueue, int paramInt);
  
  public abstract void oldestEntryUpdated(WorkgroupQueue paramWorkgroupQueue, Date paramDate);
  
  public abstract void statusUpdated(WorkgroupQueue paramWorkgroupQueue, WorkgroupQueue.Status paramStatus);
  
  public abstract void usersUpdated(WorkgroupQueue paramWorkgroupQueue, Set paramSet);
}


/* Location:           C:\Users\Administrator\Documents\ds\反编译\AndroidKiller_v1.3.1\projects\Jungledsq_ppc\ProjectSrc\smali\
 * Qualified Name:     org.jivesoftware.smackx.workgroup.agent.QueueUsersListener
 * JD-Core Version:    0.7.0.1
 */