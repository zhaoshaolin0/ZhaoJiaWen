package org.jivesoftware.smackx.workgroup.ext.notes;

import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.provider.IQProvider;
import org.xmlpull.v1.XmlPullParser;

public class ChatNotes
  extends IQ
{
  public static final String ELEMENT_NAME = "chat-notes";
  public static final String NAMESPACE = "http://jivesoftware.com/protocol/workgroup";
  private String notes;
  private String sessionID;
  
  public static final String replace(String paramString1, String paramString2, String paramString3)
  {
    if (paramString1 == null) {
      return null;
    }
    if (paramString3 == null) {
      return paramString1;
    }
    int i = paramString1.indexOf(paramString2, 0);
    if (i >= 0)
    {
      char[] arrayOfChar = paramString1.toCharArray();
      paramString3 = paramString3.toCharArray();
      int k = paramString2.length();
      StringBuilder localStringBuilder = new StringBuilder(arrayOfChar.length);
      localStringBuilder.append(arrayOfChar, 0, i).append(paramString3);
      int j = i + k;
      for (i = j;; i = j)
      {
        j = paramString1.indexOf(paramString2, j);
        if (j <= 0) {
          break;
        }
        localStringBuilder.append(arrayOfChar, i, j - i).append(paramString3);
        j += k;
      }
      localStringBuilder.append(arrayOfChar, i, arrayOfChar.length - i);
      return localStringBuilder.toString();
    }
    return paramString1;
  }
  
  public String getChildElementXML()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("<").append("chat-notes").append(" xmlns=\"").append("http://jivesoftware.com/protocol/workgroup").append("\">");
    localStringBuilder.append("<sessionID>").append(getSessionID()).append("</sessionID>");
    if (getNotes() != null) {
      localStringBuilder.append("<notes>").append(getNotes()).append("</notes>");
    }
    localStringBuilder.append("</").append("chat-notes").append("> ");
    return localStringBuilder.toString();
  }
  
  public String getNotes()
  {
    return this.notes;
  }
  
  public String getSessionID()
  {
    return this.sessionID;
  }
  
  public void setNotes(String paramString)
  {
    this.notes = paramString;
  }
  
  public void setSessionID(String paramString)
  {
    this.sessionID = paramString;
  }
  
  public static class Provider
    implements IQProvider
  {
    public IQ parseIQ(XmlPullParser paramXmlPullParser)
      throws Exception
    {
      ChatNotes localChatNotes = new ChatNotes();
      int i = 0;
      while (i == 0)
      {
        int j = paramXmlPullParser.next();
        if (j == 2)
        {
          if (paramXmlPullParser.getName().equals("sessionID")) {
            localChatNotes.setSessionID(paramXmlPullParser.nextText());
          } else if (paramXmlPullParser.getName().equals("text")) {
            localChatNotes.setNotes(paramXmlPullParser.nextText().replaceAll("\\\\n", "\n"));
          }
        }
        else if ((j == 3) && (paramXmlPullParser.getName().equals("chat-notes"))) {
          i = 1;
        }
      }
      return localChatNotes;
    }
  }
}


/* Location:           C:\Users\Administrator\Documents\ds\反编译\AndroidKiller_v1.3.1\projects\Jungledsq_ppc\ProjectSrc\smali\
 * Qualified Name:     org.jivesoftware.smackx.workgroup.ext.notes.ChatNotes
 * JD-Core Version:    0.7.0.1
 */