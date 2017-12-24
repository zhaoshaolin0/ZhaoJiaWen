package org.jivesoftware.smackx.workgroup.packet;

import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.provider.IQProvider;
import org.xmlpull.v1.XmlPullParser;

public class AgentInfo
  extends IQ
{
  public static final String ELEMENT_NAME = "agent-info";
  public static final String NAMESPACE = "http://jivesoftware.com/protocol/workgroup";
  private String jid;
  private String name;
  
  public String getChildElementXML()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("<").append("agent-info").append(" xmlns=\"").append("http://jivesoftware.com/protocol/workgroup").append("\">");
    if (this.jid != null) {
      localStringBuilder.append("<jid>").append(getJid()).append("</jid>");
    }
    if (this.name != null) {
      localStringBuilder.append("<name>").append(getName()).append("</name>");
    }
    localStringBuilder.append("</").append("agent-info").append("> ");
    return localStringBuilder.toString();
  }
  
  public String getJid()
  {
    return this.jid;
  }
  
  public String getName()
  {
    return this.name;
  }
  
  public void setJid(String paramString)
  {
    this.jid = paramString;
  }
  
  public void setName(String paramString)
  {
    this.name = paramString;
  }
  
  public static class Provider
    implements IQProvider
  {
    public IQ parseIQ(XmlPullParser paramXmlPullParser)
      throws Exception
    {
      AgentInfo localAgentInfo = new AgentInfo();
      int i = 0;
      while (i == 0)
      {
        int j = paramXmlPullParser.next();
        if (j == 2)
        {
          if (paramXmlPullParser.getName().equals("jid")) {
            localAgentInfo.setJid(paramXmlPullParser.nextText());
          } else if (paramXmlPullParser.getName().equals("name")) {
            localAgentInfo.setName(paramXmlPullParser.nextText());
          }
        }
        else if ((j == 3) && (paramXmlPullParser.getName().equals("agent-info"))) {
          i = 1;
        }
      }
      return localAgentInfo;
    }
  }
}


/* Location:           C:\Users\Administrator\Documents\ds\反编译\AndroidKiller_v1.3.1\projects\Jungledsq_ppc\ProjectSrc\smali\
 * Qualified Name:     org.jivesoftware.smackx.workgroup.packet.AgentInfo
 * JD-Core Version:    0.7.0.1
 */