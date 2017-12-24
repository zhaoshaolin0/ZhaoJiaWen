package org.jivesoftware.smack.packet;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class Privacy
  extends IQ
{
  private String activeName;
  private boolean declineActiveList = false;
  private boolean declineDefaultList = false;
  private String defaultName;
  private Map<String, List<PrivacyItem>> itemLists = new HashMap();
  
  public boolean changeDefaultList(String paramString)
  {
    if (getItemLists().containsKey(paramString))
    {
      setDefaultName(paramString);
      return true;
    }
    return false;
  }
  
  public void deleteList(String paramString)
  {
    getItemLists().remove(paramString);
  }
  
  public void deletePrivacyList(String paramString)
  {
    getItemLists().remove(paramString);
    if ((getDefaultName() != null) && (paramString.equals(getDefaultName()))) {
      setDefaultName(null);
    }
  }
  
  public String getActiveName()
  {
    return this.activeName;
  }
  
  public List<PrivacyItem> getActivePrivacyList()
  {
    if (getActiveName() == null) {
      return null;
    }
    return (List)getItemLists().get(getActiveName());
  }
  
  public String getChildElementXML()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("<query xmlns=\"jabber:iq:privacy\">");
    label43:
    Iterator localIterator;
    if (isDeclineActiveList())
    {
      localStringBuilder.append("<active/>");
      if (!isDeclineDefaultList()) {
        break label193;
      }
      localStringBuilder.append("<default/>");
      localIterator = getItemLists().entrySet().iterator();
    }
    for (;;)
    {
      if (!localIterator.hasNext()) {
        break label261;
      }
      Object localObject2 = (Map.Entry)localIterator.next();
      Object localObject1 = (String)((Map.Entry)localObject2).getKey();
      localObject2 = (List)((Map.Entry)localObject2).getValue();
      if (((List)localObject2).isEmpty()) {
        localStringBuilder.append("<list name=\"").append((String)localObject1).append("\"/>");
      }
      for (;;)
      {
        localObject1 = ((List)localObject2).iterator();
        while (((Iterator)localObject1).hasNext()) {
          localStringBuilder.append(((PrivacyItem)((Iterator)localObject1).next()).toXML());
        }
        if (getActiveName() == null) {
          break;
        }
        localStringBuilder.append("<active name=\"").append(getActiveName()).append("\"/>");
        break;
        label193:
        if (getDefaultName() == null) {
          break label43;
        }
        localStringBuilder.append("<default name=\"").append(getDefaultName()).append("\"/>");
        break label43;
        localStringBuilder.append("<list name=\"").append((String)localObject1).append("\">");
      }
      if (!((List)localObject2).isEmpty()) {
        localStringBuilder.append("</list>");
      }
    }
    label261:
    localStringBuilder.append(getExtensionsXML());
    localStringBuilder.append("</query>");
    return localStringBuilder.toString();
  }
  
  public String getDefaultName()
  {
    return this.defaultName;
  }
  
  public List<PrivacyItem> getDefaultPrivacyList()
  {
    if (getDefaultName() == null) {
      return null;
    }
    return (List)getItemLists().get(getDefaultName());
  }
  
  public PrivacyItem getItem(String paramString, int paramInt)
  {
    Iterator localIterator = getPrivacyList(paramString).iterator();
    paramString = null;
    while ((paramString == null) && (localIterator.hasNext()))
    {
      PrivacyItem localPrivacyItem = (PrivacyItem)localIterator.next();
      if (localPrivacyItem.getOrder() == paramInt) {
        paramString = localPrivacyItem;
      }
    }
    return paramString;
  }
  
  public Map<String, List<PrivacyItem>> getItemLists()
  {
    return this.itemLists;
  }
  
  public List<PrivacyItem> getPrivacyList(String paramString)
  {
    return (List)getItemLists().get(paramString);
  }
  
  public Set<String> getPrivacyListNames()
  {
    return this.itemLists.keySet();
  }
  
  public boolean isDeclineActiveList()
  {
    return this.declineActiveList;
  }
  
  public boolean isDeclineDefaultList()
  {
    return this.declineDefaultList;
  }
  
  public void setActiveName(String paramString)
  {
    this.activeName = paramString;
  }
  
  public List<PrivacyItem> setActivePrivacyList()
  {
    setActiveName(getDefaultName());
    return (List)getItemLists().get(getActiveName());
  }
  
  public void setDeclineActiveList(boolean paramBoolean)
  {
    this.declineActiveList = paramBoolean;
  }
  
  public void setDeclineDefaultList(boolean paramBoolean)
  {
    this.declineDefaultList = paramBoolean;
  }
  
  public void setDefaultName(String paramString)
  {
    this.defaultName = paramString;
  }
  
  public List setPrivacyList(String paramString, List<PrivacyItem> paramList)
  {
    getItemLists().put(paramString, paramList);
    return paramList;
  }
}


/* Location:           C:\Users\Administrator\Documents\ds\反编译\AndroidKiller_v1.3.1\projects\Jungledsq_ppc\ProjectSrc\smali\
 * Qualified Name:     org.jivesoftware.smack.packet.Privacy
 * JD-Core Version:    0.7.0.1
 */