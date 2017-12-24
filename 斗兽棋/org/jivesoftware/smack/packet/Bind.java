package org.jivesoftware.smack.packet;

public class Bind
  extends IQ
{
  private String jid = null;
  private String resource = null;
  
  public Bind()
  {
    setType(IQ.Type.SET);
  }
  
  public String getChildElementXML()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("<bind xmlns=\"urn:ietf:params:xml:ns:xmpp-bind\">");
    if (this.resource != null) {
      localStringBuilder.append("<resource>").append(this.resource).append("</resource>");
    }
    if (this.jid != null) {
      localStringBuilder.append("<jid>").append(this.jid).append("</jid>");
    }
    localStringBuilder.append("</bind>");
    return localStringBuilder.toString();
  }
  
  public String getJid()
  {
    return this.jid;
  }
  
  public String getResource()
  {
    return this.resource;
  }
  
  public void setJid(String paramString)
  {
    this.jid = paramString;
  }
  
  public void setResource(String paramString)
  {
    this.resource = paramString;
  }
}


/* Location:           C:\Users\Administrator\Documents\ds\反编译\AndroidKiller_v1.3.1\projects\Jungledsq_ppc\ProjectSrc\smali\
 * Qualified Name:     org.jivesoftware.smack.packet.Bind
 * JD-Core Version:    0.7.0.1
 */