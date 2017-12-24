package org.jivesoftware.smackx.muc;

import org.jivesoftware.smackx.packet.DiscoverItems.Item;

public class HostedRoom
{
  private String jid;
  private String name;
  
  public HostedRoom(DiscoverItems.Item paramItem)
  {
    this.jid = paramItem.getEntityID();
    this.name = paramItem.getName();
  }
  
  public String getJid()
  {
    return this.jid;
  }
  
  public String getName()
  {
    return this.name;
  }
}


/* Location:           C:\Users\Administrator\Documents\ds\反编译\AndroidKiller_v1.3.1\projects\Jungledsq_ppc\ProjectSrc\smali\
 * Qualified Name:     org.jivesoftware.smackx.muc.HostedRoom
 * JD-Core Version:    0.7.0.1
 */