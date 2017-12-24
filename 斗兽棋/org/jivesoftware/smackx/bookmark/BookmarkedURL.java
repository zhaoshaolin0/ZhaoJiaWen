package org.jivesoftware.smackx.bookmark;

public class BookmarkedURL
  implements SharedBookmark
{
  private final String URL;
  private boolean isRss;
  private boolean isShared;
  private String name;
  
  protected BookmarkedURL(String paramString)
  {
    this.URL = paramString;
  }
  
  protected BookmarkedURL(String paramString1, String paramString2, boolean paramBoolean)
  {
    this.URL = paramString1;
    this.name = paramString2;
    this.isRss = paramBoolean;
  }
  
  public boolean equals(Object paramObject)
  {
    if (!(paramObject instanceof BookmarkedURL)) {
      return false;
    }
    return ((BookmarkedURL)paramObject).getURL().equalsIgnoreCase(this.URL);
  }
  
  public String getName()
  {
    return this.name;
  }
  
  public String getURL()
  {
    return this.URL;
  }
  
  public boolean isRss()
  {
    return this.isRss;
  }
  
  public boolean isShared()
  {
    return this.isShared;
  }
  
  protected void setName(String paramString)
  {
    this.name = paramString;
  }
  
  protected void setRss(boolean paramBoolean)
  {
    this.isRss = paramBoolean;
  }
  
  protected void setShared(boolean paramBoolean)
  {
    this.isShared = paramBoolean;
  }
}


/* Location:           C:\Users\Administrator\Documents\ds\反编译\AndroidKiller_v1.3.1\projects\Jungledsq_ppc\ProjectSrc\smali\
 * Qualified Name:     org.jivesoftware.smackx.bookmark.BookmarkedURL
 * JD-Core Version:    0.7.0.1
 */