package org.jivesoftware.smackx.workgroup.agent;

public abstract interface OfferListener
{
  public abstract void offerReceived(Offer paramOffer);
  
  public abstract void offerRevoked(RevokedOffer paramRevokedOffer);
}


/* Location:           C:\Users\Administrator\Documents\ds\反编译\AndroidKiller_v1.3.1\projects\Jungledsq_ppc\ProjectSrc\smali\
 * Qualified Name:     org.jivesoftware.smackx.workgroup.agent.OfferListener
 * JD-Core Version:    0.7.0.1
 */