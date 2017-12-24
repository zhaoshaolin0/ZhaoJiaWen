package org.jivesoftware.smackx;

import java.util.List;
import org.jivesoftware.smackx.packet.DiscoverInfo.Identity;
import org.jivesoftware.smackx.packet.DiscoverItems.Item;

public abstract interface NodeInformationProvider
{
  public abstract List<String> getNodeFeatures();
  
  public abstract List<DiscoverInfo.Identity> getNodeIdentities();
  
  public abstract List<DiscoverItems.Item> getNodeItems();
}


/* Location:           C:\Users\Administrator\Documents\ds\反编译\AndroidKiller_v1.3.1\projects\Jungledsq_ppc\ProjectSrc\smali\
 * Qualified Name:     org.jivesoftware.smackx.NodeInformationProvider
 * JD-Core Version:    0.7.0.1
 */