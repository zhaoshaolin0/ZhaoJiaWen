package org.jivesoftware.smackx.packet;

import org.jivesoftware.smack.PacketCollector;
import org.jivesoftware.smack.SmackConfiguration;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.filter.PacketIDFilter;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.packet.IQ.Type;
import org.jivesoftware.smack.provider.IQProvider;
import org.jivesoftware.smack.util.StringUtils;
import org.xmlpull.v1.XmlPullParser;

public class LastActivity
  extends IQ
{
  public long lastActivity = -1L;
  public String message;
  
  public LastActivity()
  {
    setType(IQ.Type.GET);
  }
  
  public static LastActivity getLastActivity(XMPPConnection paramXMPPConnection, String paramString)
    throws XMPPException
  {
    LastActivity localLastActivity = new LastActivity();
    localLastActivity.setTo(StringUtils.parseBareAddress(paramString));
    paramString = paramXMPPConnection.createPacketCollector(new PacketIDFilter(localLastActivity.getPacketID()));
    paramXMPPConnection.sendPacket(localLastActivity);
    paramXMPPConnection = (LastActivity)paramString.nextResult(SmackConfiguration.getPacketReplyTimeout());
    paramString.cancel();
    if (paramXMPPConnection == null) {
      throw new XMPPException("No response from server on status set.");
    }
    if (paramXMPPConnection.getError() != null) {
      throw new XMPPException(paramXMPPConnection.getError());
    }
    return paramXMPPConnection;
  }
  
  private void setMessage(String paramString)
  {
    this.message = paramString;
  }
  
  public String getChildElementXML()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("<query xmlns=\"jabber:iq:last\"");
    if (this.lastActivity != -1L) {
      localStringBuilder.append(" seconds=\"").append(this.lastActivity).append("\"");
    }
    localStringBuilder.append("></query>");
    return localStringBuilder.toString();
  }
  
  public long getIdleTime()
  {
    return this.lastActivity;
  }
  
  public String getStatusMessage()
  {
    return this.message;
  }
  
  public void setLastActivity(long paramLong)
  {
    this.lastActivity = paramLong;
  }
  
  public static class Provider
    implements IQProvider
  {
    public IQ parseIQ(XmlPullParser paramXmlPullParser)
      throws Exception
    {
      if (paramXmlPullParser.getEventType() != 2) {
        throw new IllegalStateException("Parser not in proper position, or bad XML.");
      }
      LastActivity localLastActivity = new LastActivity();
      try
      {
        String str = paramXmlPullParser.getAttributeValue("", "seconds");
        paramXmlPullParser = paramXmlPullParser.nextText();
        if (str != null) {
          localLastActivity.setLastActivity((int)new Double(str).longValue());
        }
        if (paramXmlPullParser != null) {
          localLastActivity.setMessage(paramXmlPullParser);
        }
        return localLastActivity;
      }
      catch (Exception paramXmlPullParser)
      {
        paramXmlPullParser.printStackTrace();
      }
      return localLastActivity;
    }
  }
}


/* Location:           C:\Users\Administrator\Documents\ds\反编译\AndroidKiller_v1.3.1\projects\Jungledsq_ppc\ProjectSrc\smali\
 * Qualified Name:     org.jivesoftware.smackx.packet.LastActivity
 * JD-Core Version:    0.7.0.1
 */