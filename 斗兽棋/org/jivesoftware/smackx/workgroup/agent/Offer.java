package org.jivesoftware.smackx.workgroup.agent;

import java.util.Date;
import java.util.Map;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.packet.IQ.Type;

public class Offer
{
  private boolean accepted = false;
  private XMPPConnection connection;
  private OfferContent content;
  private Date expiresDate;
  private Map metaData;
  private boolean rejected = false;
  private AgentSession session;
  private String sessionID;
  private String userID;
  private String userJID;
  private String workgroupName;
  
  Offer(XMPPConnection paramXMPPConnection, AgentSession paramAgentSession, String paramString1, String paramString2, String paramString3, Date paramDate, String paramString4, Map paramMap, OfferContent paramOfferContent)
  {
    this.connection = paramXMPPConnection;
    this.session = paramAgentSession;
    this.userID = paramString1;
    this.userJID = paramString2;
    this.workgroupName = paramString3;
    this.expiresDate = paramDate;
    this.sessionID = paramString4;
    this.metaData = paramMap;
    this.content = paramOfferContent;
  }
  
  public void accept()
  {
    AcceptPacket localAcceptPacket = new AcceptPacket(this.session.getWorkgroupJID());
    this.connection.sendPacket(localAcceptPacket);
    this.accepted = true;
  }
  
  public OfferContent getContent()
  {
    return this.content;
  }
  
  public Date getExpiresDate()
  {
    return this.expiresDate;
  }
  
  public Map getMetaData()
  {
    return this.metaData;
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
  
  public String getWorkgroupName()
  {
    return this.workgroupName;
  }
  
  public boolean isAccepted()
  {
    return this.accepted;
  }
  
  public boolean isRejected()
  {
    return this.rejected;
  }
  
  public void reject()
  {
    RejectPacket localRejectPacket = new RejectPacket(this.session.getWorkgroupJID());
    this.connection.sendPacket(localRejectPacket);
    this.rejected = true;
  }
  
  private class AcceptPacket
    extends IQ
  {
    AcceptPacket(String paramString)
    {
      setTo(paramString);
      setType(IQ.Type.SET);
    }
    
    public String getChildElementXML()
    {
      return "<offer-accept id=\"" + Offer.this.getSessionID() + "\" xmlns=\"http://jabber.org/protocol/workgroup" + "\"/>";
    }
  }
  
  private class RejectPacket
    extends IQ
  {
    RejectPacket(String paramString)
    {
      setTo(paramString);
      setType(IQ.Type.SET);
    }
    
    public String getChildElementXML()
    {
      return "<offer-reject id=\"" + Offer.this.getSessionID() + "\" xmlns=\"http://jabber.org/protocol/workgroup" + "\"/>";
    }
  }
}


/* Location:           C:\Users\Administrator\Documents\ds\反编译\AndroidKiller_v1.3.1\projects\Jungledsq_ppc\ProjectSrc\smali\
 * Qualified Name:     org.jivesoftware.smackx.workgroup.agent.Offer
 * JD-Core Version:    0.7.0.1
 */