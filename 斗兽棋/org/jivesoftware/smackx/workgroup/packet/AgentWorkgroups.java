package org.jivesoftware.smackx.workgroup.packet;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.provider.IQProvider;
import org.xmlpull.v1.XmlPullParser;

public class AgentWorkgroups
  extends IQ
{
  private String agentJID;
  private List workgroups;
  
  public AgentWorkgroups(String paramString)
  {
    this.agentJID = paramString;
    this.workgroups = new ArrayList();
  }
  
  public AgentWorkgroups(String paramString, List paramList)
  {
    this.agentJID = paramString;
    this.workgroups = paramList;
  }
  
  public String getAgentJID()
  {
    return this.agentJID;
  }
  
  public String getChildElementXML()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("<workgroups xmlns=\"http://jabber.org/protocol/workgroup\" jid=\"").append(this.agentJID).append("\">");
    Iterator localIterator = this.workgroups.iterator();
    while (localIterator.hasNext())
    {
      String str = (String)localIterator.next();
      localStringBuilder.append("<workgroup jid=\"" + str + "\"/>");
    }
    localStringBuilder.append("</workgroups>");
    return localStringBuilder.toString();
  }
  
  public List getWorkgroups()
  {
    return Collections.unmodifiableList(this.workgroups);
  }
  
  public static class Provider
    implements IQProvider
  {
    public IQ parseIQ(XmlPullParser paramXmlPullParser)
      throws Exception
    {
      String str = paramXmlPullParser.getAttributeValue("", "jid");
      ArrayList localArrayList = new ArrayList();
      int i = 0;
      while (i == 0)
      {
        int j = paramXmlPullParser.next();
        if (j == 2)
        {
          if (paramXmlPullParser.getName().equals("workgroup")) {
            localArrayList.add(paramXmlPullParser.getAttributeValue("", "jid"));
          }
        }
        else if ((j == 3) && (paramXmlPullParser.getName().equals("workgroups"))) {
          i = 1;
        }
      }
      return new AgentWorkgroups(str, localArrayList);
    }
  }
}


/* Location:           C:\Users\Administrator\Documents\ds\反编译\AndroidKiller_v1.3.1\projects\Jungledsq_ppc\ProjectSrc\smali\
 * Qualified Name:     org.jivesoftware.smackx.workgroup.packet.AgentWorkgroups
 * JD-Core Version:    0.7.0.1
 */