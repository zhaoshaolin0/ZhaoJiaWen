package org.jivesoftware.smackx.workgroup.ext.history;

import java.util.Date;

public class AgentChatSession
{
  public long duration;
  public String question;
  public String sessionID;
  public Date startDate;
  public String visitorsEmail;
  public String visitorsName;
  
  public AgentChatSession(Date paramDate, long paramLong, String paramString1, String paramString2, String paramString3, String paramString4)
  {
    this.startDate = paramDate;
    this.duration = paramLong;
    this.visitorsName = paramString1;
    this.visitorsEmail = paramString2;
    this.sessionID = paramString3;
    this.question = paramString4;
  }
  
  public long getDuration()
  {
    return this.duration;
  }
  
  public String getQuestion()
  {
    return this.question;
  }
  
  public String getSessionID()
  {
    return this.sessionID;
  }
  
  public Date getStartDate()
  {
    return this.startDate;
  }
  
  public String getVisitorsEmail()
  {
    return this.visitorsEmail;
  }
  
  public String getVisitorsName()
  {
    return this.visitorsName;
  }
  
  public void setDuration(long paramLong)
  {
    this.duration = paramLong;
  }
  
  public void setQuestion(String paramString)
  {
    this.question = paramString;
  }
  
  public void setSessionID(String paramString)
  {
    this.sessionID = paramString;
  }
  
  public void setStartDate(Date paramDate)
  {
    this.startDate = paramDate;
  }
  
  public void setVisitorsEmail(String paramString)
  {
    this.visitorsEmail = paramString;
  }
  
  public void setVisitorsName(String paramString)
  {
    this.visitorsName = paramString;
  }
}


/* Location:           C:\Users\Administrator\Documents\ds\反编译\AndroidKiller_v1.3.1\projects\Jungledsq_ppc\ProjectSrc\smali\
 * Qualified Name:     org.jivesoftware.smackx.workgroup.ext.history.AgentChatSession
 * JD-Core Version:    0.7.0.1
 */