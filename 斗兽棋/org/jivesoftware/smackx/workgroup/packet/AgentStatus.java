package org.jivesoftware.smackx.workgroup.packet;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.TimeZone;
import org.jivesoftware.smack.packet.PacketExtension;
import org.jivesoftware.smack.provider.PacketExtensionProvider;
import org.xmlpull.v1.XmlPullParser;

public class AgentStatus
  implements PacketExtension
{
  public static final String ELEMENT_NAME = "agent-status";
  public static final String NAMESPACE = "http://jabber.org/protocol/workgroup";
  private static final SimpleDateFormat UTC_FORMAT = new SimpleDateFormat("yyyyMMdd'T'HH:mm:ss");
  private List currentChats = new ArrayList();
  private int maxChats = -1;
  private String workgroupJID;
  
  static
  {
    UTC_FORMAT.setTimeZone(TimeZone.getTimeZone("GMT+0"));
  }
  
  public List getCurrentChats()
  {
    return Collections.unmodifiableList(this.currentChats);
  }
  
  public String getElementName()
  {
    return "agent-status";
  }
  
  public int getMaxChats()
  {
    return this.maxChats;
  }
  
  public String getNamespace()
  {
    return "http://jabber.org/protocol/workgroup";
  }
  
  public String getWorkgroupJID()
  {
    return this.workgroupJID;
  }
  
  public String toXML()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("<").append("agent-status").append(" xmlns=\"").append("http://jabber.org/protocol/workgroup").append("\"");
    if (this.workgroupJID != null) {
      localStringBuilder.append(" jid=\"").append(this.workgroupJID).append("\"");
    }
    localStringBuilder.append(">");
    if (this.maxChats != -1) {
      localStringBuilder.append("<max-chats>").append(this.maxChats).append("</max-chats>");
    }
    if (!this.currentChats.isEmpty())
    {
      localStringBuilder.append("<current-chats xmlns= \"http://jivesoftware.com/protocol/workgroup\">");
      Iterator localIterator = this.currentChats.iterator();
      while (localIterator.hasNext()) {
        localStringBuilder.append(((ChatInfo)localIterator.next()).toXML());
      }
      localStringBuilder.append("</current-chats>");
    }
    localStringBuilder.append("</").append(getElementName()).append("> ");
    return localStringBuilder.toString();
  }
  
  public static class ChatInfo
  {
    private Date date;
    private String email;
    private String question;
    private String sessionID;
    private String userID;
    private String username;
    
    public ChatInfo(String paramString1, String paramString2, Date paramDate, String paramString3, String paramString4, String paramString5)
    {
      this.sessionID = paramString1;
      this.userID = paramString2;
      this.date = paramDate;
      this.email = paramString3;
      this.username = paramString4;
      this.question = paramString5;
    }
    
    public Date getDate()
    {
      return this.date;
    }
    
    public String getEmail()
    {
      return this.email;
    }
    
    public String getQuestion()
    {
      return this.question;
    }
    
    public String getSessionID()
    {
      return this.sessionID;
    }
    
    public String getUserID()
    {
      return this.userID;
    }
    
    public String getUsername()
    {
      return this.username;
    }
    
    public String toXML()
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("<chat ");
      if (this.sessionID != null) {
        localStringBuilder.append(" sessionID=\"").append(this.sessionID).append("\"");
      }
      if (this.userID != null) {
        localStringBuilder.append(" userID=\"").append(this.userID).append("\"");
      }
      if (this.date != null) {
        localStringBuilder.append(" startTime=\"").append(AgentStatus.UTC_FORMAT.format(this.date)).append("\"");
      }
      if (this.email != null) {
        localStringBuilder.append(" email=\"").append(this.email).append("\"");
      }
      if (this.username != null) {
        localStringBuilder.append(" username=\"").append(this.username).append("\"");
      }
      if (this.question != null) {
        localStringBuilder.append(" question=\"").append(this.question).append("\"");
      }
      localStringBuilder.append("/>");
      return localStringBuilder.toString();
    }
  }
  
  public static class Provider
    implements PacketExtensionProvider
  {
    private AgentStatus.ChatInfo parseChatInfo(XmlPullParser paramXmlPullParser)
    {
      String str1 = paramXmlPullParser.getAttributeValue("", "sessionID");
      String str2 = paramXmlPullParser.getAttributeValue("", "userID");
      Object localObject = null;
      try
      {
        Date localDate = AgentStatus.UTC_FORMAT.parse(paramXmlPullParser.getAttributeValue("", "startTime"));
        localObject = localDate;
      }
      catch (ParseException localParseException)
      {
        label45:
        break label45;
      }
      return new AgentStatus.ChatInfo(str1, str2, localObject, paramXmlPullParser.getAttributeValue("", "email"), paramXmlPullParser.getAttributeValue("", "username"), paramXmlPullParser.getAttributeValue("", "question"));
    }
    
    public PacketExtension parseExtension(XmlPullParser paramXmlPullParser)
      throws Exception
    {
      AgentStatus localAgentStatus = new AgentStatus();
      AgentStatus.access$102(localAgentStatus, paramXmlPullParser.getAttributeValue("", "jid"));
      int i = 0;
      while (i == 0)
      {
        int j = paramXmlPullParser.next();
        if (j == 2)
        {
          if ("chat".equals(paramXmlPullParser.getName())) {
            localAgentStatus.currentChats.add(parseChatInfo(paramXmlPullParser));
          } else if ("max-chats".equals(paramXmlPullParser.getName())) {
            AgentStatus.access$302(localAgentStatus, Integer.parseInt(paramXmlPullParser.nextText()));
          }
        }
        else if ((j == 3) && ("agent-status".equals(paramXmlPullParser.getName()))) {
          i = 1;
        }
      }
      return localAgentStatus;
    }
  }
}


/* Location:           C:\Users\Administrator\Documents\ds\反编译\AndroidKiller_v1.3.1\projects\Jungledsq_ppc\ProjectSrc\smali\
 * Qualified Name:     org.jivesoftware.smackx.workgroup.packet.AgentStatus
 * JD-Core Version:    0.7.0.1
 */