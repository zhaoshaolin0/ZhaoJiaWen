package org.jivesoftware.smackx.workgroup.settings;

import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.provider.IQProvider;
import org.jivesoftware.smack.util.StringUtils;
import org.xmlpull.v1.XmlPullParser;

public class SoundSettings
  extends IQ
{
  public static final String ELEMENT_NAME = "sound-settings";
  public static final String NAMESPACE = "http://jivesoftware.com/protocol/workgroup";
  private String incomingSound;
  private String outgoingSound;
  
  public String getChildElementXML()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("<").append("sound-settings").append(" xmlns=");
    localStringBuilder.append('"');
    localStringBuilder.append("http://jivesoftware.com/protocol/workgroup");
    localStringBuilder.append('"');
    localStringBuilder.append("></").append("sound-settings").append("> ");
    return localStringBuilder.toString();
  }
  
  public byte[] getIncomingSoundBytes()
  {
    return StringUtils.decodeBase64(this.incomingSound);
  }
  
  public byte[] getOutgoingSoundBytes()
  {
    return StringUtils.decodeBase64(this.outgoingSound);
  }
  
  public void setIncomingSound(String paramString)
  {
    this.incomingSound = paramString;
  }
  
  public void setOutgoingSound(String paramString)
  {
    this.outgoingSound = paramString;
  }
  
  public static class InternalProvider
    implements IQProvider
  {
    public IQ parseIQ(XmlPullParser paramXmlPullParser)
      throws Exception
    {
      if (paramXmlPullParser.getEventType() != 2) {
        throw new IllegalStateException("Parser not in proper position, or bad XML.");
      }
      SoundSettings localSoundSettings = new SoundSettings();
      int i = 0;
      while (i == 0)
      {
        int j = paramXmlPullParser.next();
        if ((j == 2) && ("outgoingSound".equals(paramXmlPullParser.getName()))) {
          localSoundSettings.setOutgoingSound(paramXmlPullParser.nextText());
        } else if ((j == 2) && ("incomingSound".equals(paramXmlPullParser.getName()))) {
          localSoundSettings.setIncomingSound(paramXmlPullParser.nextText());
        } else if ((j == 3) && ("sound-settings".equals(paramXmlPullParser.getName()))) {
          i = 1;
        }
      }
      return localSoundSettings;
    }
  }
}


/* Location:           C:\Users\Administrator\Documents\ds\反编译\AndroidKiller_v1.3.1\projects\Jungledsq_ppc\ProjectSrc\smali\
 * Qualified Name:     org.jivesoftware.smackx.workgroup.settings.SoundSettings
 * JD-Core Version:    0.7.0.1
 */