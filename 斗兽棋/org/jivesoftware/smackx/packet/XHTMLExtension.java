package org.jivesoftware.smackx.packet;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import org.jivesoftware.smack.packet.PacketExtension;

public class XHTMLExtension
  implements PacketExtension
{
  private List bodies = new ArrayList();
  
  public void addBody(String paramString)
  {
    synchronized (this.bodies)
    {
      this.bodies.add(paramString);
      return;
    }
  }
  
  public Iterator getBodies()
  {
    synchronized (this.bodies)
    {
      Iterator localIterator = Collections.unmodifiableList(new ArrayList(this.bodies)).iterator();
      return localIterator;
    }
  }
  
  public int getBodiesCount()
  {
    return this.bodies.size();
  }
  
  public String getElementName()
  {
    return "html";
  }
  
  public String getNamespace()
  {
    return "http://jabber.org/protocol/xhtml-im";
  }
  
  public String toXML()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("<").append(getElementName()).append(" xmlns=\"").append(getNamespace()).append("\">");
    Iterator localIterator = getBodies();
    while (localIterator.hasNext()) {
      localStringBuilder.append((String)localIterator.next());
    }
    localStringBuilder.append("</").append(getElementName()).append(">");
    return localStringBuilder.toString();
  }
}


/* Location:           C:\Users\Administrator\Documents\ds\反编译\AndroidKiller_v1.3.1\projects\Jungledsq_ppc\ProjectSrc\smali\
 * Qualified Name:     org.jivesoftware.smackx.packet.XHTMLExtension
 * JD-Core Version:    0.7.0.1
 */