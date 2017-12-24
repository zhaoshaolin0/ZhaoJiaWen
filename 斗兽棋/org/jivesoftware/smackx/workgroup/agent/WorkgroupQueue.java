package org.jivesoftware.smackx.workgroup.agent;

import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.Set;

public class WorkgroupQueue
{
  private int averageWaitTime = -1;
  private int currentChats = 0;
  private int maxChats = 0;
  private String name;
  private Date oldestEntry = null;
  private Status status = Status.CLOSED;
  private Set users = Collections.EMPTY_SET;
  
  WorkgroupQueue(String paramString)
  {
    this.name = paramString;
  }
  
  public int getAverageWaitTime()
  {
    return this.averageWaitTime;
  }
  
  public int getCurrentChats()
  {
    return this.currentChats;
  }
  
  public int getMaxChats()
  {
    return this.maxChats;
  }
  
  public String getName()
  {
    return this.name;
  }
  
  public Date getOldestEntry()
  {
    return this.oldestEntry;
  }
  
  public Status getStatus()
  {
    return this.status;
  }
  
  public int getUserCount()
  {
    if (this.users == null) {
      return 0;
    }
    return this.users.size();
  }
  
  public Iterator getUsers()
  {
    if (this.users == null) {
      return Collections.EMPTY_SET.iterator();
    }
    return Collections.unmodifiableSet(this.users).iterator();
  }
  
  void setAverageWaitTime(int paramInt)
  {
    this.averageWaitTime = paramInt;
  }
  
  void setCurrentChats(int paramInt)
  {
    this.currentChats = paramInt;
  }
  
  void setMaxChats(int paramInt)
  {
    this.maxChats = paramInt;
  }
  
  void setOldestEntry(Date paramDate)
  {
    this.oldestEntry = paramDate;
  }
  
  void setStatus(Status paramStatus)
  {
    this.status = paramStatus;
  }
  
  void setUsers(Set paramSet)
  {
    this.users = paramSet;
  }
  
  public static class Status
  {
    public static final Status ACTIVE = new Status("active");
    public static final Status CLOSED = new Status("closed");
    public static final Status OPEN = new Status("open");
    private String value;
    
    private Status(String paramString)
    {
      this.value = paramString;
    }
    
    public static Status fromString(String paramString)
    {
      if (paramString == null) {
        return null;
      }
      paramString = paramString.toLowerCase();
      if (OPEN.toString().equals(paramString)) {
        return OPEN;
      }
      if (ACTIVE.toString().equals(paramString)) {
        return ACTIVE;
      }
      if (CLOSED.toString().equals(paramString)) {
        return CLOSED;
      }
      return null;
    }
    
    public String toString()
    {
      return this.value;
    }
  }
}


/* Location:           C:\Users\Administrator\Documents\ds\反编译\AndroidKiller_v1.3.1\projects\Jungledsq_ppc\ProjectSrc\smali\
 * Qualified Name:     org.jivesoftware.smackx.workgroup.agent.WorkgroupQueue
 * JD-Core Version:    0.7.0.1
 */