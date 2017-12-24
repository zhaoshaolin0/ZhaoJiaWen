package org.jivesoftware.smack;

import java.util.List;
import org.jivesoftware.smack.packet.PrivacyItem;

public class PrivacyList
{
  private boolean isActiveList;
  private boolean isDefaultList;
  private List<PrivacyItem> items;
  private String listName;
  
  protected PrivacyList(boolean paramBoolean1, boolean paramBoolean2, String paramString, List<PrivacyItem> paramList)
  {
    this.isActiveList = paramBoolean1;
    this.isDefaultList = paramBoolean2;
    this.listName = paramString;
    this.items = paramList;
  }
  
  public List<PrivacyItem> getItems()
  {
    return this.items;
  }
  
  public boolean isActiveList()
  {
    return this.isActiveList;
  }
  
  public boolean isDefaultList()
  {
    return this.isDefaultList;
  }
  
  public String toString()
  {
    return this.listName;
  }
}


/* Location:           C:\Users\Administrator\Documents\ds\反编译\AndroidKiller_v1.3.1\projects\Jungledsq_ppc\ProjectSrc\smali\
 * Qualified Name:     org.jivesoftware.smack.PrivacyList
 * JD-Core Version:    0.7.0.1
 */