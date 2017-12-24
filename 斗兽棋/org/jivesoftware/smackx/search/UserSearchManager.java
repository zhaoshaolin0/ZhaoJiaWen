package org.jivesoftware.smackx.search;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smackx.Form;
import org.jivesoftware.smackx.ReportedData;
import org.jivesoftware.smackx.ServiceDiscoveryManager;
import org.jivesoftware.smackx.packet.DiscoverInfo;
import org.jivesoftware.smackx.packet.DiscoverItems;
import org.jivesoftware.smackx.packet.DiscoverItems.Item;

public class UserSearchManager
{
  private XMPPConnection con;
  private UserSearch userSearch;
  
  public UserSearchManager(XMPPConnection paramXMPPConnection)
  {
    this.con = paramXMPPConnection;
    this.userSearch = new UserSearch();
  }
  
  public Form getSearchForm(String paramString)
    throws XMPPException
  {
    return this.userSearch.getSearchForm(this.con, paramString);
  }
  
  public ReportedData getSearchResults(Form paramForm, String paramString)
    throws XMPPException
  {
    return this.userSearch.sendSearchForm(this.con, paramForm, paramString);
  }
  
  public Collection getSearchServices()
    throws XMPPException
  {
    ArrayList localArrayList = new ArrayList();
    ServiceDiscoveryManager localServiceDiscoveryManager = ServiceDiscoveryManager.getInstanceFor(this.con);
    Iterator localIterator = localServiceDiscoveryManager.discoverItems(this.con.getServiceName()).getItems();
    for (;;)
    {
      DiscoverItems.Item localItem;
      if (localIterator.hasNext()) {
        localItem = (DiscoverItems.Item)localIterator.next();
      }
      try
      {
        DiscoverInfo localDiscoverInfo = localServiceDiscoveryManager.discoverInfo(localItem.getEntityID());
        if (localDiscoverInfo.containsFeature("jabber:iq:search")) {
          localArrayList.add(localItem.getEntityID());
        }
      }
      catch (Exception localException)
      {
        return localArrayList;
      }
      catch (XMPPException localXMPPException) {}
    }
  }
}


/* Location:           C:\Users\Administrator\Documents\ds\反编译\AndroidKiller_v1.3.1\projects\Jungledsq_ppc\ProjectSrc\smali\
 * Qualified Name:     org.jivesoftware.smackx.search.UserSearchManager
 * JD-Core Version:    0.7.0.1
 */