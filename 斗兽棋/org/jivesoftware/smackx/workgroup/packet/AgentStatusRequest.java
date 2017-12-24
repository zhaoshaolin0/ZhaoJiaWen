package org.jivesoftware.smackx.workgroup.packet;

import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.provider.IQProvider;
import org.xmlpull.v1.XmlPullParser;

public class AgentStatusRequest
  extends IQ
{
  public static final String ELEMENT_NAME = "agent-status-request";
  public static final String NAMESPACE = "http://jabber.org/protocol/workgroup";
  private Set agents = new HashSet();
  
  public int getAgentCount()
  {
    return this.agents.size();
  }
  
  public Set getAgents()
  {
    return Collections.unmodifiableSet(this.agents);
  }
  
  public String getChildElementXML()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("<").append("agent-status-request").append(" xmlns=\"").append("http://jabber.org/protocol/workgroup").append("\">");
    synchronized (this.agents)
    {
      Iterator localIterator = this.agents.iterator();
      if (localIterator.hasNext())
      {
        Item localItem = (Item)localIterator.next();
        localStringBuilder.append("<agent jid=\"").append(localItem.getJID()).append("\">");
        if (localItem.getName() != null)
        {
          localStringBuilder.append("<name xmlns=\"http://jivesoftware.com/protocol/workgroup\">");
          localStringBuilder.append(localItem.getName());
          localStringBuilder.append("</name>");
        }
        localStringBuilder.append("</agent>");
      }
    }
    localObject.append("</").append(getElementName()).append("> ");
    return localObject.toString();
  }
  
  public String getElementName()
  {
    return "agent-status-request";
  }
  
  public String getNamespace()
  {
    return "http://jabber.org/protocol/workgroup";
  }
  
  public static class Item
  {
    private String jid;
    private String name;
    private String type;
    
    public Item(String paramString1, String paramString2, String paramString3)
    {
      this.jid = paramString1;
      this.type = paramString2;
      this.name = paramString3;
    }
    
    public String getJID()
    {
      return this.jid;
    }
    
    public String getName()
    {
      return this.name;
    }
    
    public String getType()
    {
      return this.type;
    }
  }
  
  public static class Provider
    implements IQProvider
  {
    private AgentStatusRequest.Item parseAgent(XmlPullParser paramXmlPullParser)
      throws Exception
    {
      int i = 0;
      String str2 = paramXmlPullParser.getAttributeValue("", "jid");
      String str3 = paramXmlPullParser.getAttributeValue("", "type");
      String str1 = null;
      while (i == 0)
      {
        int j = paramXmlPullParser.next();
        if ((j == 2) && ("name".equals(paramXmlPullParser.getName()))) {
          str1 = paramXmlPullParser.nextText();
        } else if ((j == 3) && ("agent".equals(paramXmlPullParser.getName()))) {
          i = 1;
        }
      }
      return new AgentStatusRequest.Item(str2, str3, str1);
    }
    
    public IQ parseIQ(XmlPullParser paramXmlPullParser)
      throws Exception
    {
      AgentStatusRequest localAgentStatusRequest = new AgentStatusRequest();
      if (paramXmlPullParser.getEventType() != 2) {
        throw new IllegalStateException("Parser not in proper position, or bad XML.");
      }
      int i = 0;
      while (i == 0)
      {
        int j = paramXmlPullParser.next();
        if ((j == 2) && ("agent".equals(paramXmlPullParser.getName()))) {
          localAgentStatusRequest.agents.add(parseAgent(paramXmlPullParser));
        } else if ((j == 3) && ("agent-status-request".equals(paramXmlPullParser.getName()))) {
          i = 1;
        }
      }
      return localAgentStatusRequest;
    }
  }
}


/* Location:           C:\Users\Administrator\Documents\ds\反编译\AndroidKiller_v1.3.1\projects\Jungledsq_ppc\ProjectSrc\smali\
 * Qualified Name:     org.jivesoftware.smackx.workgroup.packet.AgentStatusRequest
 * JD-Core Version:    0.7.0.1
 */