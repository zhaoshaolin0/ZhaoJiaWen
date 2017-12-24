package org.jivesoftware.smackx.workgroup.packet;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.TimeZone;
import org.jivesoftware.smack.packet.IQ;

public class Transcripts
  extends IQ
{
  private static final SimpleDateFormat UTC_FORMAT = new SimpleDateFormat("yyyyMMdd'T'HH:mm:ss");
  private List<TranscriptSummary> summaries;
  private String userID;
  
  static
  {
    UTC_FORMAT.setTimeZone(TimeZone.getTimeZone("GMT+0"));
  }
  
  public Transcripts(String paramString)
  {
    this.userID = paramString;
    this.summaries = new ArrayList();
  }
  
  public Transcripts(String paramString, List<TranscriptSummary> paramList)
  {
    this.userID = paramString;
    this.summaries = paramList;
  }
  
  public String getChildElementXML()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("<transcripts xmlns=\"http://jivesoftware.com/protocol/workgroup\" userID=\"").append(this.userID).append("\">");
    Iterator localIterator = this.summaries.iterator();
    while (localIterator.hasNext()) {
      localStringBuilder.append(((TranscriptSummary)localIterator.next()).toXML());
    }
    localStringBuilder.append("</transcripts>");
    return localStringBuilder.toString();
  }
  
  public List<TranscriptSummary> getSummaries()
  {
    return Collections.unmodifiableList(this.summaries);
  }
  
  public String getUserID()
  {
    return this.userID;
  }
  
  public static class AgentDetail
  {
    private String agentJID;
    private Date joinTime;
    private Date leftTime;
    
    public AgentDetail(String paramString, Date paramDate1, Date paramDate2)
    {
      this.agentJID = paramString;
      this.joinTime = paramDate1;
      this.leftTime = paramDate2;
    }
    
    public String getAgentJID()
    {
      return this.agentJID;
    }
    
    public Date getJoinTime()
    {
      return this.joinTime;
    }
    
    public Date getLeftTime()
    {
      return this.leftTime;
    }
    
    public String toXML()
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("<agent>");
      if (this.agentJID != null) {
        localStringBuilder.append("<agentJID>").append(this.agentJID).append("</agentJID>");
      }
      if (this.joinTime != null) {
        localStringBuilder.append("<joinTime>").append(Transcripts.UTC_FORMAT.format(this.joinTime)).append("</joinTime>");
      }
      if (this.leftTime != null) {
        localStringBuilder.append("<leftTime>").append(Transcripts.UTC_FORMAT.format(this.leftTime)).append("</leftTime>");
      }
      localStringBuilder.append("</agent>");
      return localStringBuilder.toString();
    }
  }
  
  public static class TranscriptSummary
  {
    private List<Transcripts.AgentDetail> agentDetails;
    private Date joinTime;
    private Date leftTime;
    private String sessionID;
    
    public TranscriptSummary(String paramString, Date paramDate1, Date paramDate2, List<Transcripts.AgentDetail> paramList)
    {
      this.sessionID = paramString;
      this.joinTime = paramDate1;
      this.leftTime = paramDate2;
      this.agentDetails = paramList;
    }
    
    public List<Transcripts.AgentDetail> getAgentDetails()
    {
      return this.agentDetails;
    }
    
    public Date getJoinTime()
    {
      return this.joinTime;
    }
    
    public Date getLeftTime()
    {
      return this.leftTime;
    }
    
    public String getSessionID()
    {
      return this.sessionID;
    }
    
    public String toXML()
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("<transcript sessionID=\"").append(this.sessionID).append("\">");
      if (this.joinTime != null) {
        localStringBuilder.append("<joinTime>").append(Transcripts.UTC_FORMAT.format(this.joinTime)).append("</joinTime>");
      }
      if (this.leftTime != null) {
        localStringBuilder.append("<leftTime>").append(Transcripts.UTC_FORMAT.format(this.leftTime)).append("</leftTime>");
      }
      localStringBuilder.append("<agents>");
      Iterator localIterator = this.agentDetails.iterator();
      while (localIterator.hasNext()) {
        localStringBuilder.append(((Transcripts.AgentDetail)localIterator.next()).toXML());
      }
      localStringBuilder.append("</agents></transcript>");
      return localStringBuilder.toString();
    }
  }
}


/* Location:           C:\Users\Administrator\Documents\ds\反编译\AndroidKiller_v1.3.1\projects\Jungledsq_ppc\ProjectSrc\smali\
 * Qualified Name:     org.jivesoftware.smackx.workgroup.packet.Transcripts
 * JD-Core Version:    0.7.0.1
 */