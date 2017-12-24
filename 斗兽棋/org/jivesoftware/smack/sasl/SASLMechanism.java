package org.jivesoftware.smack.sasl;

import java.io.IOException;
import java.util.HashMap;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.PasswordCallback;
import javax.security.auth.callback.UnsupportedCallbackException;
import javax.security.sasl.RealmCallback;
import javax.security.sasl.RealmChoiceCallback;
import javax.security.sasl.Sasl;
import javax.security.sasl.SaslClient;
import javax.security.sasl.SaslException;
import org.jivesoftware.smack.SASLAuthentication;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.util.Base64;

public abstract class SASLMechanism
  implements CallbackHandler
{
  protected String authenticationId;
  protected String hostname;
  protected String password;
  private SASLAuthentication saslAuthentication;
  protected SaslClient sc;
  
  public SASLMechanism(SASLAuthentication paramSASLAuthentication)
  {
    this.saslAuthentication = paramSASLAuthentication;
  }
  
  protected void authenticate()
    throws IOException, XMPPException
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("<auth mechanism=\"").append(getName());
    localStringBuilder.append("\" xmlns=\"urn:ietf:params:xml:ns:xmpp-sasl\">");
    try
    {
      if (this.sc.hasInitialResponse())
      {
        String str = Base64.encodeBytes(this.sc.evaluateChallenge(new byte[0]), 8);
        if ((str != null) && (!str.equals(""))) {
          localStringBuilder.append(str);
        }
      }
      localStringBuilder.append("</auth>");
      getSASLAuthentication().send(localStringBuilder.toString());
      return;
    }
    catch (SaslException localSaslException)
    {
      throw new XMPPException("SASL authentication failed", localSaslException);
    }
  }
  
  public void authenticate(String paramString1, String paramString2, String paramString3)
    throws IOException, XMPPException
  {
    this.authenticationId = paramString1;
    this.password = paramString3;
    this.hostname = paramString2;
    paramString3 = getName();
    HashMap localHashMap = new HashMap();
    this.sc = Sasl.createSaslClient(new String[] { paramString3 }, paramString1, "xmpp", paramString2, localHashMap, this);
    authenticate();
  }
  
  public void authenticate(String paramString1, String paramString2, CallbackHandler paramCallbackHandler)
    throws IOException, XMPPException
  {
    String str = getName();
    HashMap localHashMap = new HashMap();
    this.sc = Sasl.createSaslClient(new String[] { str }, paramString1, "xmpp", paramString2, localHashMap, paramCallbackHandler);
    authenticate();
  }
  
  public void challengeReceived(String paramString)
    throws IOException
  {
    StringBuilder localStringBuilder = new StringBuilder();
    if (paramString != null) {}
    for (paramString = this.sc.evaluateChallenge(Base64.decode(paramString));; paramString = this.sc.evaluateChallenge(null))
    {
      String str = Base64.encodeBytes(paramString, 8);
      paramString = str;
      if (str.equals("")) {
        paramString = "=";
      }
      localStringBuilder.append("<response xmlns=\"urn:ietf:params:xml:ns:xmpp-sasl\">");
      localStringBuilder.append(paramString);
      localStringBuilder.append("</response>");
      getSASLAuthentication().send(localStringBuilder.toString());
      return;
    }
  }
  
  protected abstract String getName();
  
  protected SASLAuthentication getSASLAuthentication()
  {
    return this.saslAuthentication;
  }
  
  public void handle(Callback[] paramArrayOfCallback)
    throws IOException, UnsupportedCallbackException
  {
    int i = 0;
    if (i < paramArrayOfCallback.length)
    {
      if ((paramArrayOfCallback[i] instanceof NameCallback)) {
        ((NameCallback)paramArrayOfCallback[i]).setName(this.authenticationId);
      }
      label90:
      do
      {
        for (;;)
        {
          i += 1;
          break;
          if ((paramArrayOfCallback[i] instanceof PasswordCallback))
          {
            ((PasswordCallback)paramArrayOfCallback[i]).setPassword(this.password.toCharArray());
          }
          else
          {
            if (!(paramArrayOfCallback[i] instanceof RealmCallback)) {
              break label90;
            }
            ((RealmCallback)paramArrayOfCallback[i]).setText(this.hostname);
          }
        }
      } while ((paramArrayOfCallback[i] instanceof RealmChoiceCallback));
      throw new UnsupportedCallbackException(paramArrayOfCallback[i]);
    }
  }
}


/* Location:           C:\Users\Administrator\Documents\ds\反编译\AndroidKiller_v1.3.1\projects\Jungledsq_ppc\ProjectSrc\smali\
 * Qualified Name:     org.jivesoftware.smack.sasl.SASLMechanism
 * JD-Core Version:    0.7.0.1
 */