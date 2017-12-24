package org.jivesoftware.smackx.bookmark;

public class BookmarkedConference
  implements SharedBookmark
{
  private boolean autoJoin;
  private boolean isShared;
  private final String jid;
  private String name;
  private String nickname;
  private String password;
  
  protected BookmarkedConference(String paramString)
  {
    this.jid = paramString;
  }
  
  protected BookmarkedConference(String paramString1, String paramString2, boolean paramBoolean, String paramString3, String paramString4)
  {
    this.name = paramString1;
    this.jid = paramString2;
    this.autoJoin = paramBoolean;
    this.nickname = paramString3;
    this.password = paramString4;
  }
  
  public boolean equals(Object paramObject)
  {
    if ((paramObject == null) || (!(paramObject instanceof BookmarkedConference))) {
      return false;
    }
    return ((BookmarkedConference)paramObject).getJid().equalsIgnoreCase(this.jid);
  }
  
  public String getJid()
  {
    return this.jid;
  }
  
  public String getName()
  {
    return this.name;
  }
  
  public String getNickname()
  {
    return this.nickname;
  }
  
  public String getPassword()
  {
    return this.password;
  }
  
  public boolean isAutoJoin()
  {
    return this.autoJoin;
  }
  
  public boolean isShared()
  {
    return this.isShared;
  }
  
  protected void setAutoJoin(boolean paramBoolean)
  {
    this.autoJoin = paramBoolean;
  }
  
  protected void setName(String paramString)
  {
    this.name = paramString;
  }
  
  protected void setNickname(String paramString)
  {
    this.nickname = paramString;
  }
  
  protected void setPassword(String paramString)
  {
    this.password = paramString;
  }
  
  protected void setShared(boolean paramBoolean)
  {
    this.isShared = paramBoolean;
  }
}


/* Location:           C:\Users\Administrator\Documents\ds\反编译\AndroidKiller_v1.3.1\projects\Jungledsq_ppc\ProjectSrc\smali\
 * Qualified Name:     org.jivesoftware.smackx.bookmark.BookmarkedConference
 * JD-Core Version:    0.7.0.1
 */