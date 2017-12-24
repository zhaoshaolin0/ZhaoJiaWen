package org.jivesoftware.smackx.workgroup.settings;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.provider.IQProvider;
import org.xmlpull.v1.XmlPullParser;

public class ChatSettings
  extends IQ
{
  public static final int BOT_SETTINGS = 2;
  public static final String ELEMENT_NAME = "chat-settings";
  public static final int IMAGE_SETTINGS = 0;
  public static final String NAMESPACE = "http://jivesoftware.com/protocol/workgroup";
  public static final int TEXT_SETTINGS = 1;
  private String key;
  private List settings;
  private int type = -1;
  
  public ChatSettings()
  {
    this.settings = new ArrayList();
  }
  
  public ChatSettings(String paramString)
  {
    setKey(paramString);
  }
  
  public void addSetting(ChatSetting paramChatSetting)
  {
    this.settings.add(paramChatSetting);
  }
  
  public ChatSetting getChatSetting(String paramString)
  {
    Object localObject = getSettings();
    if (localObject != null)
    {
      localObject = ((Collection)localObject).iterator();
      while (((Iterator)localObject).hasNext())
      {
        ChatSetting localChatSetting = (ChatSetting)((Iterator)localObject).next();
        if (localChatSetting.getKey().equals(paramString)) {
          return localChatSetting;
        }
      }
    }
    return null;
  }
  
  public String getChildElementXML()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("<").append("chat-settings").append(" xmlns=");
    localStringBuilder.append('"');
    localStringBuilder.append("http://jivesoftware.com/protocol/workgroup");
    localStringBuilder.append('"');
    if (this.key != null) {
      localStringBuilder.append(" key=\"" + this.key + "\"");
    }
    if (this.type != -1) {
      localStringBuilder.append(" type=\"" + this.type + "\"");
    }
    localStringBuilder.append("></").append("chat-settings").append("> ");
    return localStringBuilder.toString();
  }
  
  public ChatSetting getFirstEntry()
  {
    if (this.settings.size() > 0) {
      return (ChatSetting)this.settings.get(0);
    }
    return null;
  }
  
  public Collection getSettings()
  {
    return this.settings;
  }
  
  public void setKey(String paramString)
  {
    this.key = paramString;
  }
  
  public void setType(int paramInt)
  {
    this.type = paramInt;
  }
  
  public static class InternalProvider
    implements IQProvider
  {
    private ChatSetting parseChatSetting(XmlPullParser paramXmlPullParser)
      throws Exception
    {
      int j = 0;
      String str2 = null;
      String str1 = null;
      int i = 0;
      while (j == 0)
      {
        int k = paramXmlPullParser.next();
        if ((k == 2) && ("key".equals(paramXmlPullParser.getName()))) {
          str2 = paramXmlPullParser.nextText();
        } else if ((k == 2) && ("value".equals(paramXmlPullParser.getName()))) {
          str1 = paramXmlPullParser.nextText();
        } else if ((k == 2) && ("type".equals(paramXmlPullParser.getName()))) {
          i = Integer.parseInt(paramXmlPullParser.nextText());
        } else if ((k == 3) && ("chat-setting".equals(paramXmlPullParser.getName()))) {
          j = 1;
        }
      }
      return new ChatSetting(str2, str1, i);
    }
    
    public IQ parseIQ(XmlPullParser paramXmlPullParser)
      throws Exception
    {
      if (paramXmlPullParser.getEventType() != 2) {
        throw new IllegalStateException("Parser not in proper position, or bad XML.");
      }
      ChatSettings localChatSettings = new ChatSettings();
      int i = 0;
      while (i == 0)
      {
        int j = paramXmlPullParser.next();
        if ((j == 2) && ("chat-setting".equals(paramXmlPullParser.getName()))) {
          localChatSettings.addSetting(parseChatSetting(paramXmlPullParser));
        } else if ((j == 3) && ("chat-settings".equals(paramXmlPullParser.getName()))) {
          i = 1;
        }
      }
      return localChatSettings;
    }
  }
}


/* Location:           C:\Users\Administrator\Documents\ds\反编译\AndroidKiller_v1.3.1\projects\Jungledsq_ppc\ProjectSrc\smali\
 * Qualified Name:     org.jivesoftware.smackx.workgroup.settings.ChatSettings
 * JD-Core Version:    0.7.0.1
 */