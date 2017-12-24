package org.jivesoftware.smackx.workgroup.agent;

import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.packet.IQ.Type;
import org.jivesoftware.smack.provider.IQProvider;
import org.xmlpull.v1.XmlPullParser;

public class OfferConfirmation
  extends IQ
{
  private long sessionID;
  private String userJID;
  
  public String getChildElementXML()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("<offer-confirmation xmlns=\"http://jabber.org/protocol/workgroup\">");
    localStringBuilder.append("</offer-confirmation>");
    return localStringBuilder.toString();
  }
  
  public long getSessionID()
  {
    return this.sessionID;
  }
  
  public String getUserJID()
  {
    return this.userJID;
  }
  
  public void notifyService(XMPPConnection paramXMPPConnection, String paramString1, String paramString2)
  {
    paramXMPPConnection.sendPacket(new NotifyServicePacket(paramString1, paramString2));
  }
  
  public void setSessionID(long paramLong)
  {
    this.sessionID = paramLong;
  }
  
  public void setUserJID(String paramString)
  {
    this.userJID = paramString;
  }
  
  private class NotifyServicePacket
    extends IQ
  {
    String roomName;
    
    NotifyServicePacket(String paramString1, String paramString2)
    {
      setTo(paramString1);
      setType(IQ.Type.RESULT);
      this.roomName = paramString2;
    }
    
    public String getChildElementXML()
    {
      return "<offer-confirmation  roomname=\"" + this.roomName + "\" xmlns=\"http://jabber.org/protocol/workgroup" + "\"/>";
    }
  }
  
  public static class Provider
    implements IQProvider
  {
    public IQ parseIQ(XmlPullParser paramXmlPullParser)
      throws Exception
    {
      OfferConfirmation localOfferConfirmation = new OfferConfirmation();
      int i = 0;
      while (i == 0)
      {
        paramXmlPullParser.next();
        String str = paramXmlPullParser.getName();
        if ((paramXmlPullParser.getEventType() == 2) && ("user-jid".equals(str))) {
          try
          {
            localOfferConfirmation.setUserJID(paramXmlPullParser.nextText());
          }
          catch (NumberFormatException localNumberFormatException1) {}
        } else if ((paramXmlPullParser.getEventType() == 2) && ("session-id".equals(localNumberFormatException1))) {
          try
          {
            localOfferConfirmation.setSessionID(Long.valueOf(paramXmlPullParser.nextText()).longValue());
          }
          catch (NumberFormatException localNumberFormatException2) {}
        } else if ((paramXmlPullParser.getEventType() == 3) && ("offer-confirmation".equals(localNumberFormatException2))) {
          i = 1;
        }
      }
      return localOfferConfirmation;
    }
  }
}


/* Location:           C:\Users\Administrator\Documents\ds\反编译\AndroidKiller_v1.3.1\projects\Jungledsq_ppc\ProjectSrc\smali\
 * Qualified Name:     org.jivesoftware.smackx.workgroup.agent.OfferConfirmation
 * JD-Core Version:    0.7.0.1
 */