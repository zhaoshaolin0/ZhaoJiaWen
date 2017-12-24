package org.jivesoftware.smack.sasl;

import java.io.IOException;
import javax.security.auth.callback.CallbackHandler;
import org.jivesoftware.smack.SASLAuthentication;

public class SASLAnonymous
  extends SASLMechanism
{
  public SASLAnonymous(SASLAuthentication paramSASLAuthentication)
  {
    super(paramSASLAuthentication);
  }
  
  protected void authenticate()
    throws IOException
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("<auth mechanism=\"").append(getName());
    localStringBuilder.append("\" xmlns=\"urn:ietf:params:xml:ns:xmpp-sasl\">");
    localStringBuilder.append("</auth>");
    getSASLAuthentication().send(localStringBuilder.toString());
  }
  
  public void authenticate(String paramString1, String paramString2, String paramString3)
    throws IOException
  {
    authenticate();
  }
  
  public void authenticate(String paramString1, String paramString2, CallbackHandler paramCallbackHandler)
    throws IOException
  {
    authenticate();
  }
  
  public void challengeReceived(String paramString)
    throws IOException
  {
    paramString = new StringBuilder();
    paramString.append("<response xmlns=\"urn:ietf:params:xml:ns:xmpp-sasl\">");
    paramString.append("=");
    paramString.append("</response>");
    getSASLAuthentication().send(paramString.toString());
  }
  
  protected String getName()
  {
    return "ANONYMOUS";
  }
}


/* Location:           C:\Users\Administrator\Documents\ds\反编译\AndroidKiller_v1.3.1\projects\Jungledsq_ppc\ProjectSrc\smali\
 * Qualified Name:     org.jivesoftware.smack.sasl.SASLAnonymous
 * JD-Core Version:    0.7.0.1
 */