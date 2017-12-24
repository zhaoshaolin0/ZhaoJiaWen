package org.jivesoftware.smackx.workgroup.packet;

import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.packet.IQ.Type;

public class DepartQueuePacket
  extends IQ
{
  private String user;
  
  public DepartQueuePacket(String paramString)
  {
    this(paramString, null);
  }
  
  public DepartQueuePacket(String paramString1, String paramString2)
  {
    this.user = paramString2;
    setTo(paramString1);
    setType(IQ.Type.SET);
    setFrom(paramString2);
  }
  
  public String getChildElementXML()
  {
    StringBuilder localStringBuilder = new StringBuilder("<depart-queue xmlns=\"http://jabber.org/protocol/workgroup\"");
    if (this.user != null) {
      localStringBuilder.append("><jid>").append(this.user).append("</jid></depart-queue>");
    }
    for (;;)
    {
      return localStringBuilder.toString();
      localStringBuilder.append("/>");
    }
  }
}


/* Location:           C:\Users\Administrator\Documents\ds\反编译\AndroidKiller_v1.3.1\projects\Jungledsq_ppc\ProjectSrc\smali\
 * Qualified Name:     org.jivesoftware.smackx.workgroup.packet.DepartQueuePacket
 * JD-Core Version:    0.7.0.1
 */