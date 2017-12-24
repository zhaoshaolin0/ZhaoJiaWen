package org.jivesoftware.smackx.muc;

import org.jivesoftware.smackx.packet.MUCAdmin.Item;
import org.jivesoftware.smackx.packet.MUCOwner.Item;

public class Affiliate
{
  private String affiliation;
  private String jid;
  private String nick;
  private String role;
  
  Affiliate(MUCAdmin.Item paramItem)
  {
    this.jid = paramItem.getJid();
    this.affiliation = paramItem.getAffiliation();
    this.role = paramItem.getRole();
    this.nick = paramItem.getNick();
  }
  
  Affiliate(MUCOwner.Item paramItem)
  {
    this.jid = paramItem.getJid();
    this.affiliation = paramItem.getAffiliation();
    this.role = paramItem.getRole();
    this.nick = paramItem.getNick();
  }
  
  public String getAffiliation()
  {
    return this.affiliation;
  }
  
  public String getJid()
  {
    return this.jid;
  }
  
  public String getNick()
  {
    return this.nick;
  }
  
  public String getRole()
  {
    return this.role;
  }
}


/* Location:           C:\Users\Administrator\Documents\ds\反编译\AndroidKiller_v1.3.1\projects\Jungledsq_ppc\ProjectSrc\smali\
 * Qualified Name:     org.jivesoftware.smackx.muc.Affiliate
 * JD-Core Version:    0.7.0.1
 */