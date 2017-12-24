package org.jivesoftware.smackx.workgroup.packet;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.TimeZone;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.provider.IQProvider;
import org.xmlpull.v1.XmlPullParser;

public class OccupantsInfo
  extends IQ
{
  public static final String ELEMENT_NAME = "occupants-info";
  public static final String NAMESPACE = "http://jivesoftware.com/protocol/workgroup";
  private static final SimpleDateFormat UTC_FORMAT = new SimpleDateFormat("yyyyMMdd'T'HH:mm:ss");
  private final Set<OccupantInfo> occupants;
  private String roomID;
  
  static
  {
    UTC_FORMAT.setTimeZone(TimeZone.getTimeZone("GMT+0"));
  }
  
  public OccupantsInfo(String paramString)
  {
    this.roomID = paramString;
    this.occupants = new HashSet();
  }
  
  public String getChildElementXML()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("<").append("occupants-info").append(" xmlns=\"").append("http://jivesoftware.com/protocol/workgroup");
    localStringBuilder.append("\" roomID=\"").append(this.roomID).append("\">");
    synchronized (this.occupants)
    {
      Iterator localIterator = this.occupants.iterator();
      if (localIterator.hasNext())
      {
        OccupantInfo localOccupantInfo = (OccupantInfo)localIterator.next();
        localStringBuilder.append("<occupant>");
        localStringBuilder.append("<jid>");
        localStringBuilder.append(localOccupantInfo.getJID());
        localStringBuilder.append("</jid>");
        localStringBuilder.append("<name>");
        localStringBuilder.append(localOccupantInfo.getNickname());
        localStringBuilder.append("</name>");
        localStringBuilder.append("<joined>");
        localStringBuilder.append(UTC_FORMAT.format(localOccupantInfo.getJoined()));
        localStringBuilder.append("</joined>");
        localStringBuilder.append("</occupant>");
      }
    }
    localObject.append("</").append("occupants-info").append("> ");
    return localObject.toString();
  }
  
  public Set<OccupantInfo> getOccupants()
  {
    return Collections.unmodifiableSet(this.occupants);
  }
  
  public int getOccupantsCount()
  {
    return this.occupants.size();
  }
  
  public String getRoomID()
  {
    return this.roomID;
  }
  
  public static class OccupantInfo
  {
    private String jid;
    private Date joined;
    private String nickname;
    
    public OccupantInfo(String paramString1, String paramString2, Date paramDate)
    {
      this.jid = paramString1;
      this.nickname = paramString2;
      this.joined = paramDate;
    }
    
    public String getJID()
    {
      return this.jid;
    }
    
    public Date getJoined()
    {
      return this.joined;
    }
    
    public String getNickname()
    {
      return this.nickname;
    }
  }
  
  public static class Provider
    implements IQProvider
  {
    private OccupantsInfo.OccupantInfo parseOccupantInfo(XmlPullParser paramXmlPullParser)
      throws Exception
    {
      int i = 0;
      String str2 = null;
      String str1 = null;
      Date localDate = null;
      while (i == 0)
      {
        int j = paramXmlPullParser.next();
        if ((j == 2) && ("jid".equals(paramXmlPullParser.getName()))) {
          str2 = paramXmlPullParser.nextText();
        } else if ((j == 2) && ("nickname".equals(paramXmlPullParser.getName()))) {
          str1 = paramXmlPullParser.nextText();
        } else if ((j == 2) && ("joined".equals(paramXmlPullParser.getName()))) {
          localDate = OccupantsInfo.UTC_FORMAT.parse(paramXmlPullParser.nextText());
        } else if ((j == 3) && ("occupant".equals(paramXmlPullParser.getName()))) {
          i = 1;
        }
      }
      return new OccupantsInfo.OccupantInfo(str2, str1, localDate);
    }
    
    public IQ parseIQ(XmlPullParser paramXmlPullParser)
      throws Exception
    {
      if (paramXmlPullParser.getEventType() != 2) {
        throw new IllegalStateException("Parser not in proper position, or bad XML.");
      }
      OccupantsInfo localOccupantsInfo = new OccupantsInfo(paramXmlPullParser.getAttributeValue("", "roomID"));
      int i = 0;
      while (i == 0)
      {
        int j = paramXmlPullParser.next();
        if ((j == 2) && ("occupant".equals(paramXmlPullParser.getName()))) {
          localOccupantsInfo.occupants.add(parseOccupantInfo(paramXmlPullParser));
        } else if ((j == 3) && ("occupants-info".equals(paramXmlPullParser.getName()))) {
          i = 1;
        }
      }
      return localOccupantsInfo;
    }
  }
}


/* Location:           C:\Users\Administrator\Documents\ds\反编译\AndroidKiller_v1.3.1\projects\Jungledsq_ppc\ProjectSrc\smali\
 * Qualified Name:     org.jivesoftware.smackx.workgroup.packet.OccupantsInfo
 * JD-Core Version:    0.7.0.1
 */