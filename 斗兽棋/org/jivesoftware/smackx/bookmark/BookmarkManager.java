package org.jivesoftware.smackx.bookmark;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smackx.PrivateDataManager;

public class BookmarkManager
{
  private static final Map bookmarkManagerMap = new HashMap();
  private final Object bookmarkLock = new Object();
  private Bookmarks bookmarks;
  private PrivateDataManager privateDataManager;
  
  static
  {
    PrivateDataManager.addPrivateDataProvider("storage", "storage:bookmarks", new Bookmarks.Provider());
  }
  
  private BookmarkManager(XMPPConnection paramXMPPConnection)
    throws XMPPException
  {
    if ((paramXMPPConnection == null) || (!paramXMPPConnection.isAuthenticated())) {
      throw new XMPPException("Invalid connection.");
    }
    this.privateDataManager = new PrivateDataManager(paramXMPPConnection);
  }
  
  public static BookmarkManager getBookmarkManager(XMPPConnection paramXMPPConnection)
    throws XMPPException
  {
    try
    {
      BookmarkManager localBookmarkManager2 = (BookmarkManager)bookmarkManagerMap.get(paramXMPPConnection);
      BookmarkManager localBookmarkManager1 = localBookmarkManager2;
      if (localBookmarkManager2 == null)
      {
        localBookmarkManager1 = new BookmarkManager(paramXMPPConnection);
        bookmarkManagerMap.put(paramXMPPConnection, localBookmarkManager1);
      }
      return localBookmarkManager1;
    }
    finally {}
  }
  
  private Bookmarks retrieveBookmarks()
    throws XMPPException
  {
    synchronized (this.bookmarkLock)
    {
      if (this.bookmarks == null) {
        this.bookmarks = ((Bookmarks)this.privateDataManager.getPrivateData("storage", "storage:bookmarks"));
      }
      Bookmarks localBookmarks = this.bookmarks;
      return localBookmarks;
    }
  }
  
  public void addBookmarkedConference(String paramString1, String paramString2, boolean paramBoolean, String paramString3, String paramString4)
    throws XMPPException
  {
    retrieveBookmarks();
    paramString2 = new BookmarkedConference(paramString1, paramString2, paramBoolean, paramString3, paramString4);
    List localList = this.bookmarks.getBookmarkedConferences();
    if (localList.contains(paramString2))
    {
      paramString2 = (BookmarkedConference)localList.get(localList.indexOf(paramString2));
      if (paramString2.isShared()) {
        throw new IllegalArgumentException("Cannot modify shared bookmark");
      }
      paramString2.setAutoJoin(paramBoolean);
      paramString2.setName(paramString1);
      paramString2.setNickname(paramString3);
      paramString2.setPassword(paramString4);
    }
    for (;;)
    {
      this.privateDataManager.setPrivateData(this.bookmarks);
      return;
      this.bookmarks.addBookmarkedConference(paramString2);
    }
  }
  
  public void addBookmarkedURL(String paramString1, String paramString2, boolean paramBoolean)
    throws XMPPException
  {
    retrieveBookmarks();
    paramString1 = new BookmarkedURL(paramString1, paramString2, paramBoolean);
    List localList = this.bookmarks.getBookmarkedURLS();
    if (localList.contains(paramString1))
    {
      paramString1 = (BookmarkedURL)localList.get(localList.indexOf(paramString1));
      if (paramString1.isShared()) {
        throw new IllegalArgumentException("Cannot modify shared bookmarks");
      }
      paramString1.setName(paramString2);
      paramString1.setRss(paramBoolean);
    }
    for (;;)
    {
      this.privateDataManager.setPrivateData(this.bookmarks);
      return;
      this.bookmarks.addBookmarkedURL(paramString1);
    }
  }
  
  public Collection getBookmarkedConferences()
    throws XMPPException
  {
    retrieveBookmarks();
    return Collections.unmodifiableCollection(this.bookmarks.getBookmarkedConferences());
  }
  
  public Collection getBookmarkedURLs()
    throws XMPPException
  {
    retrieveBookmarks();
    return Collections.unmodifiableCollection(this.bookmarks.getBookmarkedURLS());
  }
  
  public void removeBookmarkedConference(String paramString)
    throws XMPPException
  {
    retrieveBookmarks();
    Iterator localIterator = this.bookmarks.getBookmarkedConferences().iterator();
    while (localIterator.hasNext())
    {
      BookmarkedConference localBookmarkedConference = (BookmarkedConference)localIterator.next();
      if (localBookmarkedConference.getJid().equalsIgnoreCase(paramString))
      {
        if (localBookmarkedConference.isShared()) {
          throw new IllegalArgumentException("Conference is shared and can't be removed");
        }
        localIterator.remove();
        this.privateDataManager.setPrivateData(this.bookmarks);
      }
    }
  }
  
  public void removeBookmarkedURL(String paramString)
    throws XMPPException
  {
    retrieveBookmarks();
    Iterator localIterator = this.bookmarks.getBookmarkedURLS().iterator();
    while (localIterator.hasNext())
    {
      BookmarkedURL localBookmarkedURL = (BookmarkedURL)localIterator.next();
      if (localBookmarkedURL.getURL().equalsIgnoreCase(paramString))
      {
        if (localBookmarkedURL.isShared()) {
          throw new IllegalArgumentException("Cannot delete a shared bookmark.");
        }
        localIterator.remove();
        this.privateDataManager.setPrivateData(this.bookmarks);
      }
    }
  }
}


/* Location:           C:\Users\Administrator\Documents\ds\反编译\AndroidKiller_v1.3.1\projects\Jungledsq_ppc\ProjectSrc\smali\
 * Qualified Name:     org.jivesoftware.smackx.bookmark.BookmarkManager
 * JD-Core Version:    0.7.0.1
 */