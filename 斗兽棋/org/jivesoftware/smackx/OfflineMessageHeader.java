package org.jivesoftware.smackx;

import org.jivesoftware.smackx.packet.DiscoverItems.Item;

public class OfflineMessageHeader
{
  private String jid;
  private String stamp;
  private String user;
  
  public OfflineMessageHeader(DiscoverItems.Item paramItem)
  {
    this.user = paramItem.getEntityID();
    this.jid = paramItem.getName();
    this.stamp = paramItem.getNode();
  }
  
  public String getJid()
  {
    return this.jid;
  }
  
  public String getStamp()
  {
    return this.stamp;
  }
  
  public String getUser()
  {
    return this.user;
  }
}


/* Location:           C:\Users\Administrator\Documents\ds\反编译\AndroidKiller_v1.3.1\projects\Jungledsq_ppc\ProjectSrc\smali\
 * Qualified Name:     org.jivesoftware.smackx.OfflineMessageHeader
 * JD-Core Version:    0.7.0.1
 */