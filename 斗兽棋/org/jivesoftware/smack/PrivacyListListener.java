package org.jivesoftware.smack;

import java.util.List;
import org.jivesoftware.smack.packet.PrivacyItem;

public abstract interface PrivacyListListener
{
  public abstract void setPrivacyList(String paramString, List<PrivacyItem> paramList);
  
  public abstract void updatedPrivacyList(String paramString);
}


/* Location:           C:\Users\Administrator\Documents\ds\反编译\AndroidKiller_v1.3.1\projects\Jungledsq_ppc\ProjectSrc\smali\
 * Qualified Name:     org.jivesoftware.smack.PrivacyListListener
 * JD-Core Version:    0.7.0.1
 */