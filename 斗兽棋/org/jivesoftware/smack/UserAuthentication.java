package org.jivesoftware.smack;

import javax.security.auth.callback.CallbackHandler;

abstract interface UserAuthentication
{
  public abstract String authenticate(String paramString1, String paramString2, String paramString3)
    throws XMPPException;
  
  public abstract String authenticate(String paramString1, String paramString2, CallbackHandler paramCallbackHandler)
    throws XMPPException;
  
  public abstract String authenticateAnonymously()
    throws XMPPException;
}


/* Location:           C:\Users\Administrator\Documents\ds\反编译\AndroidKiller_v1.3.1\projects\Jungledsq_ppc\ProjectSrc\smali\
 * Qualified Name:     org.jivesoftware.smack.UserAuthentication
 * JD-Core Version:    0.7.0.1
 */