package org.jivesoftware.smackx.workgroup.packet;

import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.provider.IQProvider;
import org.xmlpull.v1.XmlPullParser;

public class OfferRevokeProvider
  implements IQProvider
{
  public IQ parseIQ(XmlPullParser paramXmlPullParser)
    throws Exception
  {
    String str4 = paramXmlPullParser.getAttributeValue("", "jid");
    String str3 = str4;
    String str2 = null;
    String str1 = null;
    int i = 0;
    while (i == 0)
    {
      int j = paramXmlPullParser.next();
      if ((j == 2) && (paramXmlPullParser.getName().equals("reason"))) {
        str2 = paramXmlPullParser.nextText();
      } else if ((j == 2) && (paramXmlPullParser.getName().equals("session"))) {
        str1 = paramXmlPullParser.getAttributeValue("", "id");
      } else if ((j == 2) && (paramXmlPullParser.getName().equals("user"))) {
        str3 = paramXmlPullParser.getAttributeValue("", "id");
      } else if ((j == 3) && (paramXmlPullParser.getName().equals("offer-revoke"))) {
        i = 1;
      }
    }
    return new OfferRevokePacket(str4, str3, str2, str1);
  }
  
  public class OfferRevokePacket
    extends IQ
  {
    private String reason;
    private String sessionID;
    private String userID;
    private String userJID;
    
    public OfferRevokePacket(String paramString1, String paramString2, String paramString3, String paramString4)
    {
      this.userJID = paramString1;
      this.userID = paramString2;
      this.reason = paramString3;
      this.sessionID = paramString4;
    }
    
    public String getChildElementXML()
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("<offer-revoke xmlns=\"http://jabber.org/protocol/workgroup\" jid=\"").append(this.userID).append("\">");
      if (this.reason != null) {
        localStringBuilder.append("<reason>").append(this.reason).append("</reason>");
      }
      if (this.sessionID != null) {
        localStringBuilder.append(new SessionID(this.sessionID).toXML());
      }
      if (this.userID != null) {
        localStringBuilder.append(new UserID(this.userID).toXML());
      }
      localStringBuilder.append("</offer-revoke>");
      return localStringBuilder.toString();
    }
    
    public String getReason()
    {
      return this.reason;
    }
    
    public String getSessionID()
    {
      return this.sessionID;
    }
    
    public String getUserID()
    {
      return this.userID;
    }
    
    public String getUserJID()
    {
      return this.userJID;
    }
  }
}


/* Location:           C:\Users\Administrator\Documents\ds\反编译\AndroidKiller_v1.3.1\projects\Jungledsq_ppc\ProjectSrc\smali\
 * Qualified Name:     org.jivesoftware.smackx.workgroup.packet.OfferRevokeProvider
 * JD-Core Version:    0.7.0.1
 */