package org.jivesoftware.smack.packet;

public class Session
  extends IQ
{
  public Session()
  {
    setType(IQ.Type.SET);
  }
  
  public String getChildElementXML()
  {
    return "<session xmlns=\"urn:ietf:params:xml:ns:xmpp-session\"/>";
  }
}


/* Location:           C:\Users\Administrator\Documents\ds\反编译\AndroidKiller_v1.3.1\projects\Jungledsq_ppc\ProjectSrc\smali\
 * Qualified Name:     org.jivesoftware.smack.packet.Session
 * JD-Core Version:    0.7.0.1
 */