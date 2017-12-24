package org.jivesoftware.smackx.workgroup.packet;

import java.util.HashMap;
import java.util.Map;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.packet.IQ.Type;
import org.jivesoftware.smack.provider.IQProvider;
import org.jivesoftware.smack.util.PacketParserUtils;
import org.jivesoftware.smackx.workgroup.agent.InvitationRequest;
import org.jivesoftware.smackx.workgroup.agent.OfferContent;
import org.jivesoftware.smackx.workgroup.agent.TransferRequest;
import org.jivesoftware.smackx.workgroup.agent.UserRequest;
import org.jivesoftware.smackx.workgroup.util.MetaDataUtils;
import org.xmlpull.v1.XmlPullParser;

public class OfferRequestProvider
  implements IQProvider
{
  public IQ parseIQ(XmlPullParser paramXmlPullParser)
    throws Exception
  {
    int k = paramXmlPullParser.getEventType();
    String str1 = null;
    int j = -1;
    Object localObject1 = null;
    int i = 0;
    Object localObject2 = new HashMap();
    if (k != 2) {}
    String str3 = paramXmlPullParser.getAttributeValue("", "jid");
    String str2 = str3;
    while (i == 0)
    {
      k = paramXmlPullParser.next();
      if (k == 2)
      {
        String str4 = paramXmlPullParser.getName();
        if ("timeout".equals(str4))
        {
          j = Integer.parseInt(paramXmlPullParser.nextText());
        }
        else if ("metadata".equals(str4))
        {
          localObject2 = MetaDataUtils.parseMetaData(paramXmlPullParser);
        }
        else if ("session".equals(str4))
        {
          str1 = paramXmlPullParser.getAttributeValue("", "id");
        }
        else if ("user".equals(str4))
        {
          str2 = paramXmlPullParser.getAttributeValue("", "id");
        }
        else if ("user-request".equals(str4))
        {
          localObject1 = UserRequest.getInstance();
        }
        else if ("invite".equals(str4))
        {
          localObject1 = (RoomInvitation)PacketParserUtils.parsePacketExtension("invite", "http://jabber.org/protocol/workgroup", paramXmlPullParser);
          localObject1 = new InvitationRequest(((RoomInvitation)localObject1).getInviter(), ((RoomInvitation)localObject1).getRoom(), ((RoomInvitation)localObject1).getReason());
        }
        else if ("transfer".equals(str4))
        {
          localObject1 = (RoomTransfer)PacketParserUtils.parsePacketExtension("transfer", "http://jabber.org/protocol/workgroup", paramXmlPullParser);
          localObject1 = new TransferRequest(((RoomTransfer)localObject1).getInviter(), ((RoomTransfer)localObject1).getRoom(), ((RoomTransfer)localObject1).getReason());
        }
      }
      else if ((k == 3) && ("offer".equals(paramXmlPullParser.getName())))
      {
        i = 1;
      }
    }
    paramXmlPullParser = new OfferRequestPacket(str3, str2, j, (Map)localObject2, str1, (OfferContent)localObject1);
    paramXmlPullParser.setType(IQ.Type.SET);
    return paramXmlPullParser;
  }
  
  public static class OfferRequestPacket
    extends IQ
  {
    private OfferContent content;
    private Map metaData;
    private String sessionID;
    private int timeout;
    private String userID;
    private String userJID;
    
    public OfferRequestPacket(String paramString1, String paramString2, int paramInt, Map paramMap, String paramString3, OfferContent paramOfferContent)
    {
      this.userJID = paramString1;
      this.userID = paramString2;
      this.timeout = paramInt;
      this.metaData = paramMap;
      this.sessionID = paramString3;
      this.content = paramOfferContent;
    }
    
    public String getChildElementXML()
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("<offer xmlns=\"http://jabber.org/protocol/workgroup\" jid=\"").append(this.userJID).append("\">");
      localStringBuilder.append("<timeout>").append(this.timeout).append("</timeout>");
      if (this.sessionID != null)
      {
        localStringBuilder.append('<').append("session");
        localStringBuilder.append(" session=\"");
        localStringBuilder.append(getSessionID()).append("\" xmlns=\"");
        localStringBuilder.append("http://jivesoftware.com/protocol/workgroup").append("\"/>");
      }
      if (this.metaData != null) {
        localStringBuilder.append(MetaDataUtils.serializeMetaData(this.metaData));
      }
      if (this.userID != null)
      {
        localStringBuilder.append('<').append("user");
        localStringBuilder.append(" id=\"");
        localStringBuilder.append(this.userID).append("\" xmlns=\"");
        localStringBuilder.append("http://jivesoftware.com/protocol/workgroup").append("\"/>");
      }
      localStringBuilder.append("</offer>");
      return localStringBuilder.toString();
    }
    
    public OfferContent getContent()
    {
      return this.content;
    }
    
    public Map getMetaData()
    {
      return this.metaData;
    }
    
    public String getSessionID()
    {
      return this.sessionID;
    }
    
    public int getTimeout()
    {
      return this.timeout;
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
 * Qualified Name:     org.jivesoftware.smackx.workgroup.packet.OfferRequestProvider
 * JD-Core Version:    0.7.0.1
 */