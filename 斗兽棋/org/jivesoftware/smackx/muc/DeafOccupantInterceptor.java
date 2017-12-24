package org.jivesoftware.smackx.muc;

import org.jivesoftware.smack.PacketInterceptor;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.packet.PacketExtension;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.packet.Presence.Type;

public class DeafOccupantInterceptor
  implements PacketInterceptor
{
  public void interceptPacket(Packet paramPacket)
  {
    Presence localPresence = (Presence)paramPacket;
    if ((Presence.Type.available == localPresence.getType()) && (localPresence.getExtension("x", "http://jabber.org/protocol/muc") != null)) {
      paramPacket.addExtension(new DeafExtension(null));
    }
  }
  
  private static class DeafExtension
    implements PacketExtension
  {
    public String getElementName()
    {
      return "x";
    }
    
    public String getNamespace()
    {
      return "http://jivesoftware.org/protocol/muc";
    }
    
    public String toXML()
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("<").append(getElementName()).append(" xmlns=\"").append(getNamespace()).append("\">");
      localStringBuilder.append("<deaf-occupant/>");
      localStringBuilder.append("</").append(getElementName()).append(">");
      return localStringBuilder.toString();
    }
  }
}


/* Location:           C:\Users\Administrator\Documents\ds\反编译\AndroidKiller_v1.3.1\projects\Jungledsq_ppc\ProjectSrc\smali\
 * Qualified Name:     org.jivesoftware.smackx.muc.DeafOccupantInterceptor
 * JD-Core Version:    0.7.0.1
 */