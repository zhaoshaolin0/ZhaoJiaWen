package org.jivesoftware.smackx.workgroup;

import java.util.Date;

public class QueueUser
{
  private int estimatedTime;
  private Date joinDate;
  private int queuePosition;
  private String userID;
  
  public QueueUser(String paramString, int paramInt1, int paramInt2, Date paramDate)
  {
    this.userID = paramString;
    this.queuePosition = paramInt1;
    this.estimatedTime = paramInt2;
    this.joinDate = paramDate;
  }
  
  public int getEstimatedRemainingTime()
  {
    return this.estimatedTime;
  }
  
  public Date getQueueJoinTimestamp()
  {
    return this.joinDate;
  }
  
  public int getQueuePosition()
  {
    return this.queuePosition;
  }
  
  public String getUserID()
  {
    return this.userID;
  }
}


/* Location:           C:\Users\Administrator\Documents\ds\反编译\AndroidKiller_v1.3.1\projects\Jungledsq_ppc\ProjectSrc\smali\
 * Qualified Name:     org.jivesoftware.smackx.workgroup.QueueUser
 * JD-Core Version:    0.7.0.1
 */