package org.jivesoftware.smackx.packet;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.provider.IQProvider;
import org.xmlpull.v1.XmlPullParser;

public class SharedGroupsInfo
  extends IQ
{
  private List groups = new ArrayList();
  
  public String getChildElementXML()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("<sharedgroup xmlns=\"http://www.jivesoftware.org/protocol/sharedgroup\">");
    Iterator localIterator = this.groups.iterator();
    while (localIterator.hasNext()) {
      localStringBuilder.append("<group>").append(localIterator.next()).append("</group>");
    }
    localStringBuilder.append("</sharedgroup>");
    return localStringBuilder.toString();
  }
  
  public List getGroups()
  {
    return this.groups;
  }
  
  public static class Provider
    implements IQProvider
  {
    public IQ parseIQ(XmlPullParser paramXmlPullParser)
      throws Exception
    {
      SharedGroupsInfo localSharedGroupsInfo = new SharedGroupsInfo();
      int i = 0;
      while (i == 0)
      {
        int j = paramXmlPullParser.next();
        if ((j == 2) && (paramXmlPullParser.getName().equals("group"))) {
          localSharedGroupsInfo.getGroups().add(paramXmlPullParser.nextText());
        } else if ((j == 3) && (paramXmlPullParser.getName().equals("sharedgroup"))) {
          i = 1;
        }
      }
      return localSharedGroupsInfo;
    }
  }
}


/* Location:           C:\Users\Administrator\Documents\ds\反编译\AndroidKiller_v1.3.1\projects\Jungledsq_ppc\ProjectSrc\smali\
 * Qualified Name:     org.jivesoftware.smackx.packet.SharedGroupsInfo
 * JD-Core Version:    0.7.0.1
 */