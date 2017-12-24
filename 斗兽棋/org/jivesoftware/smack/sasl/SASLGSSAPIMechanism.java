package org.jivesoftware.smack.sasl;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.security.auth.callback.CallbackHandler;
import javax.security.sasl.Sasl;
import org.jivesoftware.smack.SASLAuthentication;
import org.jivesoftware.smack.XMPPException;

public class SASLGSSAPIMechanism
  extends SASLMechanism
{
  public SASLGSSAPIMechanism(SASLAuthentication paramSASLAuthentication)
  {
    super(paramSASLAuthentication);
    System.setProperty("javax.security.auth.useSubjectCredsOnly", "false");
    System.setProperty("java.security.auth.login.config", "gss.conf");
  }
  
  public void authenticate(String paramString1, String paramString2, String paramString3)
    throws IOException, XMPPException
  {
    paramString3 = getName();
    HashMap localHashMap = new HashMap();
    localHashMap.put("javax.security.sasl.server.authentication", "TRUE");
    this.sc = Sasl.createSaslClient(new String[] { paramString3 }, paramString1, "xmpp", paramString2, localHashMap, this);
    authenticate();
  }
  
  public void authenticate(String paramString1, String paramString2, CallbackHandler paramCallbackHandler)
    throws IOException, XMPPException
  {
    String str = getName();
    HashMap localHashMap = new HashMap();
    localHashMap.put("javax.security.sasl.server.authentication", "TRUE");
    this.sc = Sasl.createSaslClient(new String[] { str }, paramString1, "xmpp", paramString2, localHashMap, paramCallbackHandler);
    authenticate();
  }
  
  protected String getName()
  {
    return "GSSAPI";
  }
}


/* Location:           C:\Users\Administrator\Documents\ds\反编译\AndroidKiller_v1.3.1\projects\Jungledsq_ppc\ProjectSrc\smali\
 * Qualified Name:     org.jivesoftware.smack.sasl.SASLGSSAPIMechanism
 * JD-Core Version:    0.7.0.1
 */