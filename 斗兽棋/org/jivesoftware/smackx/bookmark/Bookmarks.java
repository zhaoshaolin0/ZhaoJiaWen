package org.jivesoftware.smackx.bookmark;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.jivesoftware.smackx.packet.PrivateData;
import org.jivesoftware.smackx.provider.PrivateDataProvider;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

public class Bookmarks
  implements PrivateData
{
  private List bookmarkedConferences = new ArrayList();
  private List bookmarkedURLS = new ArrayList();
  
  private static BookmarkedConference getConferenceStorage(XmlPullParser paramXmlPullParser)
    throws Exception
  {
    String str1 = paramXmlPullParser.getAttributeValue("", "name");
    String str2 = paramXmlPullParser.getAttributeValue("", "autojoin");
    BookmarkedConference localBookmarkedConference = new BookmarkedConference(paramXmlPullParser.getAttributeValue("", "jid"));
    localBookmarkedConference.setName(str1);
    localBookmarkedConference.setAutoJoin(Boolean.valueOf(str2).booleanValue());
    int i = 0;
    while (i == 0)
    {
      int j = paramXmlPullParser.next();
      if ((j == 2) && ("nick".equals(paramXmlPullParser.getName()))) {
        localBookmarkedConference.setNickname(paramXmlPullParser.nextText());
      } else if ((j == 2) && ("password".equals(paramXmlPullParser.getName()))) {
        localBookmarkedConference.setPassword(paramXmlPullParser.nextText());
      } else if ((j == 2) && ("shared_bookmark".equals(paramXmlPullParser.getName()))) {
        localBookmarkedConference.setShared(true);
      } else if ((j == 3) && ("conference".equals(paramXmlPullParser.getName()))) {
        i = 1;
      }
    }
    return localBookmarkedConference;
  }
  
  private static BookmarkedURL getURLStorage(XmlPullParser paramXmlPullParser)
    throws IOException, XmlPullParserException
  {
    Object localObject = paramXmlPullParser.getAttributeValue("", "name");
    String str1 = paramXmlPullParser.getAttributeValue("", "url");
    String str2 = paramXmlPullParser.getAttributeValue("", "rss");
    boolean bool;
    int i;
    if ((str2 != null) && ("true".equals(str2)))
    {
      bool = true;
      localObject = new BookmarkedURL(str1, (String)localObject, bool);
      i = 0;
    }
    for (;;)
    {
      if (i != 0) {
        break label137;
      }
      int j = paramXmlPullParser.next();
      if ((j == 2) && ("shared_bookmark".equals(paramXmlPullParser.getName())))
      {
        ((BookmarkedURL)localObject).setShared(true);
        continue;
        bool = false;
        break;
      }
      if ((j == 3) && ("url".equals(paramXmlPullParser.getName()))) {
        i = 1;
      }
    }
    label137:
    return localObject;
  }
  
  public void addBookmarkedConference(BookmarkedConference paramBookmarkedConference)
  {
    this.bookmarkedConferences.add(paramBookmarkedConference);
  }
  
  public void addBookmarkedURL(BookmarkedURL paramBookmarkedURL)
  {
    this.bookmarkedURLS.add(paramBookmarkedURL);
  }
  
  public void clearBookmarkedConferences()
  {
    this.bookmarkedConferences.clear();
  }
  
  public void clearBookmarkedURLS()
  {
    this.bookmarkedURLS.clear();
  }
  
  public List getBookmarkedConferences()
  {
    return this.bookmarkedConferences;
  }
  
  public List getBookmarkedURLS()
  {
    return this.bookmarkedURLS;
  }
  
  public String getElementName()
  {
    return "storage";
  }
  
  public String getNamespace()
  {
    return "storage:bookmarks";
  }
  
  public void removeBookmarkedConference(BookmarkedConference paramBookmarkedConference)
  {
    this.bookmarkedConferences.remove(paramBookmarkedConference);
  }
  
  public void removeBookmarkedURL(BookmarkedURL paramBookmarkedURL)
  {
    this.bookmarkedURLS.remove(paramBookmarkedURL);
  }
  
  public String toXML()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("<storage xmlns=\"storage:bookmarks\">");
    Iterator localIterator = getBookmarkedURLS().iterator();
    Object localObject;
    while (localIterator.hasNext())
    {
      localObject = (BookmarkedURL)localIterator.next();
      if (!((BookmarkedURL)localObject).isShared())
      {
        localStringBuilder.append("<url name=\"").append(((BookmarkedURL)localObject).getName()).append("\" url=\"").append(((BookmarkedURL)localObject).getURL()).append("\"");
        if (((BookmarkedURL)localObject).isRss()) {
          localStringBuilder.append(" rss=\"").append(true).append("\"");
        }
        localStringBuilder.append(" />");
      }
    }
    localIterator = getBookmarkedConferences().iterator();
    while (localIterator.hasNext())
    {
      localObject = (BookmarkedConference)localIterator.next();
      if (!((BookmarkedConference)localObject).isShared())
      {
        localStringBuilder.append("<conference ");
        localStringBuilder.append("name=\"").append(((BookmarkedConference)localObject).getName()).append("\" ");
        localStringBuilder.append("autojoin=\"").append(((BookmarkedConference)localObject).isAutoJoin()).append("\" ");
        localStringBuilder.append("jid=\"").append(((BookmarkedConference)localObject).getJid()).append("\" ");
        localStringBuilder.append(">");
        if (((BookmarkedConference)localObject).getNickname() != null) {
          localStringBuilder.append("<nick>").append(((BookmarkedConference)localObject).getNickname()).append("</nick>");
        }
        if (((BookmarkedConference)localObject).getPassword() != null) {
          localStringBuilder.append("<password>").append(((BookmarkedConference)localObject).getPassword()).append("</password>");
        }
        localStringBuilder.append("</conference>");
      }
    }
    localStringBuilder.append("</storage>");
    return localStringBuilder.toString();
  }
  
  public static class Provider
    implements PrivateDataProvider
  {
    public PrivateData parsePrivateData(XmlPullParser paramXmlPullParser)
      throws Exception
    {
      Bookmarks localBookmarks = new Bookmarks();
      int i = 0;
      while (i == 0)
      {
        int j = paramXmlPullParser.next();
        if ((j == 2) && ("url".equals(paramXmlPullParser.getName())))
        {
          BookmarkedURL localBookmarkedURL = Bookmarks.getURLStorage(paramXmlPullParser);
          if (localBookmarkedURL != null) {
            localBookmarks.addBookmarkedURL(localBookmarkedURL);
          }
        }
        else if ((j == 2) && ("conference".equals(paramXmlPullParser.getName())))
        {
          localBookmarks.addBookmarkedConference(Bookmarks.getConferenceStorage(paramXmlPullParser));
        }
        else if ((j == 3) && ("storage".equals(paramXmlPullParser.getName())))
        {
          i = 1;
        }
      }
      return localBookmarks;
    }
  }
}


/* Location:           C:\Users\Administrator\Documents\ds\反编译\AndroidKiller_v1.3.1\projects\Jungledsq_ppc\ProjectSrc\smali\
 * Qualified Name:     org.jivesoftware.smackx.bookmark.Bookmarks
 * JD-Core Version:    0.7.0.1
 */