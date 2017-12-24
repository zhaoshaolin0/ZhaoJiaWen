package org.jivesoftware.smackx.commands;

import org.jivesoftware.smackx.packet.AdHocCommandData;

public abstract class LocalCommand
  extends AdHocCommand
{
  private long creationDate = System.currentTimeMillis();
  private int currenStage = -1;
  private String ownerJID;
  private String sessionID;
  
  void decrementStage()
  {
    this.currenStage -= 1;
  }
  
  public long getCreationDate()
  {
    return this.creationDate;
  }
  
  public int getCurrentStage()
  {
    return this.currenStage;
  }
  
  public String getOwnerJID()
  {
    return this.ownerJID;
  }
  
  public String getSessionID()
  {
    return this.sessionID;
  }
  
  public abstract boolean hasPermission(String paramString);
  
  void incrementStage()
  {
    this.currenStage += 1;
  }
  
  public abstract boolean isLastStage();
  
  void setData(AdHocCommandData paramAdHocCommandData)
  {
    paramAdHocCommandData.setSessionID(this.sessionID);
    super.setData(paramAdHocCommandData);
  }
  
  public void setOwnerJID(String paramString)
  {
    this.ownerJID = paramString;
  }
  
  public void setSessionID(String paramString)
  {
    this.sessionID = paramString;
    getData().setSessionID(paramString);
  }
}


/* Location:           C:\Users\Administrator\Documents\ds\反编译\AndroidKiller_v1.3.1\projects\Jungledsq_ppc\ProjectSrc\smali\
 * Qualified Name:     org.jivesoftware.smackx.commands.LocalCommand
 * JD-Core Version:    0.7.0.1
 */