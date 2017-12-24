package org.jivesoftware.smack.packet;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class Registration
  extends IQ
{
  private Map<String, String> attributes = null;
  private String instructions = null;
  
  public Map<String, String> getAttributes()
  {
    return this.attributes;
  }
  
  public String getChildElementXML()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("<query xmlns=\"jabber:iq:register\">");
    if (this.instructions != null) {
      localStringBuilder.append("<instructions>").append(this.instructions).append("</instructions>");
    }
    if ((this.attributes != null) && (this.attributes.size() > 0))
    {
      Iterator localIterator = this.attributes.keySet().iterator();
      while (localIterator.hasNext())
      {
        String str1 = (String)localIterator.next();
        String str2 = (String)this.attributes.get(str1);
        localStringBuilder.append("<").append(str1).append(">");
        localStringBuilder.append(str2);
        localStringBuilder.append("</").append(str1).append(">");
      }
    }
    localStringBuilder.append(getExtensionsXML());
    localStringBuilder.append("</query>");
    return localStringBuilder.toString();
  }
  
  public String getInstructions()
  {
    return this.instructions;
  }
  
  public void setAttributes(Map<String, String> paramMap)
  {
    this.attributes = paramMap;
  }
  
  public void setInstructions(String paramString)
  {
    this.instructions = paramString;
  }
}


/* Location:           C:\Users\Administrator\Documents\ds\反编译\AndroidKiller_v1.3.1\projects\Jungledsq_ppc\ProjectSrc\smali\
 * Qualified Name:     org.jivesoftware.smack.packet.Registration
 * JD-Core Version:    0.7.0.1
 */