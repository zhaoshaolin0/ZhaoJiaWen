package org.jivesoftware.smackx.muc;

import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.util.StringUtils;
import org.jivesoftware.smackx.packet.MUCAdmin.Item;
import org.jivesoftware.smackx.packet.MUCUser;
import org.jivesoftware.smackx.packet.MUCUser.Item;

public class Occupant
{
  private String affiliation;
  private String jid;
  private String nick;
  private String role;
  
  Occupant(Presence paramPresence)
  {
    MUCUser.Item localItem = ((MUCUser)paramPresence.getExtension("x", "http://jabber.org/protocol/muc#user")).getItem();
    this.jid = localItem.getJid();
    this.affiliation = localItem.getAffiliation();
    this.role = localItem.getRole();
    this.nick = StringUtils.parseResource(paramPresence.getFrom());
  }
  
  Occupant(MUCAdmin.Item paramItem)
  {
    this.jid = paramItem.getJid();
    this.affiliation = paramItem.getAffiliation();
    this.role = paramItem.getRole();
    this.nick = paramItem.getNick();
  }
  
  public boolean equals(Object paramObject)
  {
    if (!(paramObject instanceof Occupant)) {
      return false;
    }
    paramObject = (Occupant)paramObject;
    return this.jid.equals(paramObject.jid);
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
  
  public int hashCode()
  {
    int j = this.affiliation.hashCode();
    int k = this.role.hashCode();
    int m = this.jid.hashCode();
    if (this.nick != null) {}
    for (int i = this.nick.hashCode();; i = 0) {
      return ((j * 17 + k) * 17 + m) * 17 + i;
    }
  }
}


/* Location:           C:\Users\Administrator\Documents\ds\反编译\AndroidKiller_v1.3.1\projects\Jungledsq_ppc\ProjectSrc\smali\
 * Qualified Name:     org.jivesoftware.smackx.muc.Occupant
 * JD-Core Version:    0.7.0.1
 */