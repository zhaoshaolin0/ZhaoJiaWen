package org.jivesoftware.smack.sasl;

import org.jivesoftware.smack.SASLAuthentication;

public class SASLCramMD5Mechanism
  extends SASLMechanism
{
  public SASLCramMD5Mechanism(SASLAuthentication paramSASLAuthentication)
  {
    super(paramSASLAuthentication);
  }
  
  protected String getName()
  {
    return "CRAM-MD5";
  }
}


/* Location:           C:\Users\Administrator\Documents\ds\反编译\AndroidKiller_v1.3.1\projects\Jungledsq_ppc\ProjectSrc\smali\
 * Qualified Name:     org.jivesoftware.smack.sasl.SASLCramMD5Mechanism
 * JD-Core Version:    0.7.0.1
 */