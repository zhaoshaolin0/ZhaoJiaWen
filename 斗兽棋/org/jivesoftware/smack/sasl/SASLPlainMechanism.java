package org.jivesoftware.smack.sasl;

import org.jivesoftware.smack.SASLAuthentication;

public class SASLPlainMechanism
  extends SASLMechanism
{
  public SASLPlainMechanism(SASLAuthentication paramSASLAuthentication)
  {
    super(paramSASLAuthentication);
  }
  
  protected String getName()
  {
    return "PLAIN";
  }
}


/* Location:           C:\Users\Administrator\Documents\ds\反编译\AndroidKiller_v1.3.1\projects\Jungledsq_ppc\ProjectSrc\smali\
 * Qualified Name:     org.jivesoftware.smack.sasl.SASLPlainMechanism
 * JD-Core Version:    0.7.0.1
 */