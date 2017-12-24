package org.jivesoftware.smackx.muc;

import java.util.Iterator;
import org.jivesoftware.smackx.Form;
import org.jivesoftware.smackx.FormField;
import org.jivesoftware.smackx.packet.DiscoverInfo;

public class RoomInfo
{
  private String description = "";
  private boolean membersOnly;
  private boolean moderated;
  private boolean nonanonymous;
  private int occupantsCount = -1;
  private boolean passwordProtected;
  private boolean persistent;
  private String room;
  private String subject = "";
  
  RoomInfo(DiscoverInfo paramDiscoverInfo)
  {
    this.room = paramDiscoverInfo.getFrom();
    this.membersOnly = paramDiscoverInfo.containsFeature("muc_membersonly");
    this.moderated = paramDiscoverInfo.containsFeature("muc_moderated");
    this.nonanonymous = paramDiscoverInfo.containsFeature("muc_nonanonymous");
    this.passwordProtected = paramDiscoverInfo.containsFeature("muc_passwordprotected");
    this.persistent = paramDiscoverInfo.containsFeature("muc_persistent");
    paramDiscoverInfo = Form.getFormFrom(paramDiscoverInfo);
    Iterator localIterator;
    if (paramDiscoverInfo != null)
    {
      this.description = ((String)paramDiscoverInfo.getField("muc#roominfo_description").getValues().next());
      localIterator = paramDiscoverInfo.getField("muc#roominfo_subject").getValues();
      if (!localIterator.hasNext()) {
        break label166;
      }
    }
    label166:
    for (this.subject = ((String)localIterator.next());; this.subject = "")
    {
      this.occupantsCount = Integer.parseInt((String)paramDiscoverInfo.getField("muc#roominfo_occupants").getValues().next());
      return;
    }
  }
  
  public String getDescription()
  {
    return this.description;
  }
  
  public int getOccupantsCount()
  {
    return this.occupantsCount;
  }
  
  public String getRoom()
  {
    return this.room;
  }
  
  public String getSubject()
  {
    return this.subject;
  }
  
  public boolean isMembersOnly()
  {
    return this.membersOnly;
  }
  
  public boolean isModerated()
  {
    return this.moderated;
  }
  
  public boolean isNonanonymous()
  {
    return this.nonanonymous;
  }
  
  public boolean isPasswordProtected()
  {
    return this.passwordProtected;
  }
  
  public boolean isPersistent()
  {
    return this.persistent;
  }
}


/* Location:           C:\Users\Administrator\Documents\ds\反编译\AndroidKiller_v1.3.1\projects\Jungledsq_ppc\ProjectSrc\smali\
 * Qualified Name:     org.jivesoftware.smackx.muc.RoomInfo
 * JD-Core Version:    0.7.0.1
 */