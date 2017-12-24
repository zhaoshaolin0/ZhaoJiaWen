package org.jivesoftware.smackx.muc;

import java.util.Date;
import org.jivesoftware.smackx.packet.MUCInitialPresence.History;

public class DiscussionHistory
{
  private int maxChars = -1;
  private int maxStanzas = -1;
  private int seconds = -1;
  private Date since;
  
  private boolean isConfigured()
  {
    return (this.maxChars > -1) || (this.maxStanzas > -1) || (this.seconds > -1) || (this.since != null);
  }
  
  MUCInitialPresence.History getMUCHistory()
  {
    if (!isConfigured()) {
      return null;
    }
    MUCInitialPresence.History localHistory = new MUCInitialPresence.History();
    if (this.maxChars > -1) {
      localHistory.setMaxChars(this.maxChars);
    }
    if (this.maxStanzas > -1) {
      localHistory.setMaxStanzas(this.maxStanzas);
    }
    if (this.seconds > -1) {
      localHistory.setSeconds(this.seconds);
    }
    if (this.since != null) {
      localHistory.setSince(this.since);
    }
    return localHistory;
  }
  
  public int getMaxChars()
  {
    return this.maxChars;
  }
  
  public int getMaxStanzas()
  {
    return this.maxStanzas;
  }
  
  public int getSeconds()
  {
    return this.seconds;
  }
  
  public Date getSince()
  {
    return this.since;
  }
  
  public void setMaxChars(int paramInt)
  {
    this.maxChars = paramInt;
  }
  
  public void setMaxStanzas(int paramInt)
  {
    this.maxStanzas = paramInt;
  }
  
  public void setSeconds(int paramInt)
  {
    this.seconds = paramInt;
  }
  
  public void setSince(Date paramDate)
  {
    this.since = paramDate;
  }
}


/* Location:           C:\Users\Administrator\Documents\ds\反编译\AndroidKiller_v1.3.1\projects\Jungledsq_ppc\ProjectSrc\smali\
 * Qualified Name:     org.jivesoftware.smackx.muc.DiscussionHistory
 * JD-Core Version:    0.7.0.1
 */