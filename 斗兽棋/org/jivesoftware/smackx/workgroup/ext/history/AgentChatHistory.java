package org.jivesoftware.smackx.workgroup.ext.history;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.provider.IQProvider;
import org.xmlpull.v1.XmlPullParser;

public class AgentChatHistory
  extends IQ
{
  public static final String ELEMENT_NAME = "chat-sessions";
  public static final String NAMESPACE = "http://jivesoftware.com/protocol/workgroup";
  private List agentChatSessions = new ArrayList();
  private String agentJID;
  private int maxSessions;
  private long startDate;
  
  public AgentChatHistory() {}
  
  public AgentChatHistory(String paramString, int paramInt)
  {
    this.agentJID = paramString;
    this.maxSessions = paramInt;
    this.startDate = 0L;
  }
  
  public AgentChatHistory(String paramString, int paramInt, Date paramDate)
  {
    this.agentJID = paramString;
    this.maxSessions = paramInt;
    this.startDate = paramDate.getTime();
  }
  
  public void addChatSession(AgentChatSession paramAgentChatSession)
  {
    this.agentChatSessions.add(paramAgentChatSession);
  }
  
  public Collection getAgentChatSessions()
  {
    return this.agentChatSessions;
  }
  
  public String getChildElementXML()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("<").append("chat-sessions").append(" xmlns=");
    localStringBuilder.append('"');
    localStringBuilder.append("http://jivesoftware.com/protocol/workgroup");
    localStringBuilder.append('"');
    localStringBuilder.append(" agentJID=\"" + this.agentJID + "\"");
    localStringBuilder.append(" maxSessions=\"" + this.maxSessions + "\"");
    localStringBuilder.append(" startDate=\"" + this.startDate + "\"");
    localStringBuilder.append("></").append("chat-sessions").append("> ");
    return localStringBuilder.toString();
  }
  
  public static class InternalProvider
    implements IQProvider
  {
    private AgentChatSession parseChatSetting(XmlPullParser paramXmlPullParser)
      throws Exception
    {
      int i = 0;
      Date localDate = null;
      long l = 0L;
      String str4 = null;
      String str3 = null;
      String str2 = null;
      String str1 = null;
      while (i == 0)
      {
        int j = paramXmlPullParser.next();
        if ((j == 2) && ("date".equals(paramXmlPullParser.getName()))) {
          localDate = new Date(Long.valueOf(paramXmlPullParser.nextText()).longValue());
        } else if ((j == 2) && ("duration".equals(paramXmlPullParser.getName()))) {
          l = Long.valueOf(paramXmlPullParser.nextText()).longValue();
        } else if ((j == 2) && ("visitorsName".equals(paramXmlPullParser.getName()))) {
          str4 = paramXmlPullParser.nextText();
        } else if ((j == 2) && ("visitorsEmail".equals(paramXmlPullParser.getName()))) {
          str3 = paramXmlPullParser.nextText();
        } else if ((j == 2) && ("sessionID".equals(paramXmlPullParser.getName()))) {
          str2 = paramXmlPullParser.nextText();
        } else if ((j == 2) && ("question".equals(paramXmlPullParser.getName()))) {
          str1 = paramXmlPullParser.nextText();
        } else if ((j == 3) && ("chat-session".equals(paramXmlPullParser.getName()))) {
          i = 1;
        }
      }
      return new AgentChatSession(localDate, l, str4, str3, str2, str1);
    }
    
    public IQ parseIQ(XmlPullParser paramXmlPullParser)
      throws Exception
    {
      if (paramXmlPullParser.getEventType() != 2) {
        throw new IllegalStateException("Parser not in proper position, or bad XML.");
      }
      AgentChatHistory localAgentChatHistory = new AgentChatHistory();
      int i = 0;
      while (i == 0)
      {
        int j = paramXmlPullParser.next();
        if ((j == 2) && ("chat-session".equals(paramXmlPullParser.getName()))) {
          localAgentChatHistory.addChatSession(parseChatSetting(paramXmlPullParser));
        } else if ((j == 3) && ("chat-sessions".equals(paramXmlPullParser.getName()))) {
          i = 1;
        }
      }
      return localAgentChatHistory;
    }
  }
}


/* Location:           C:\Users\Administrator\Documents\ds\反编译\AndroidKiller_v1.3.1\projects\Jungledsq_ppc\ProjectSrc\smali\
 * Qualified Name:     org.jivesoftware.smackx.workgroup.ext.history.AgentChatHistory
 * JD-Core Version:    0.7.0.1
 */