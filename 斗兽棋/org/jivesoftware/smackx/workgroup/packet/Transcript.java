package org.jivesoftware.smackx.workgroup.packet;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.packet.Packet;

public class Transcript
  extends IQ
{
  private List packets;
  private String sessionID;
  
  public Transcript(String paramString)
  {
    this.sessionID = paramString;
    this.packets = new ArrayList();
  }
  
  public Transcript(String paramString, List paramList)
  {
    this.sessionID = paramString;
    this.packets = paramList;
  }
  
  public String getChildElementXML()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("<transcript xmlns=\"http://jivesoftware.com/protocol/workgroup\" sessionID=\"").append(this.sessionID).append("\">");
    Iterator localIterator = this.packets.iterator();
    while (localIterator.hasNext()) {
      localStringBuilder.append(((Packet)localIterator.next()).toXML());
    }
    localStringBuilder.append("</transcript>");
    return localStringBuilder.toString();
  }
  
  public List getPackets()
  {
    return Collections.unmodifiableList(this.packets);
  }
  
  public String getSessionID()
  {
    return this.sessionID;
  }
}


/* Location:           C:\Users\Administrator\Documents\ds\反编译\AndroidKiller_v1.3.1\projects\Jungledsq_ppc\ProjectSrc\smali\
 * Qualified Name:     org.jivesoftware.smackx.workgroup.packet.Transcript
 * JD-Core Version:    0.7.0.1
 */