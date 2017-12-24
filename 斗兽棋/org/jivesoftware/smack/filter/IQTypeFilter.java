package org.jivesoftware.smack.filter;

import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.packet.IQ.Type;
import org.jivesoftware.smack.packet.Packet;

public class IQTypeFilter
  implements PacketFilter
{
  private IQ.Type type;
  
  public IQTypeFilter(IQ.Type paramType)
  {
    this.type = paramType;
  }
  
  public boolean accept(Packet paramPacket)
  {
    return ((paramPacket instanceof IQ)) && (((IQ)paramPacket).getType().equals(this.type));
  }
}


/* Location:           C:\Users\Administrator\Documents\ds\反编译\AndroidKiller_v1.3.1\projects\Jungledsq_ppc\ProjectSrc\smali\
 * Qualified Name:     org.jivesoftware.smack.filter.IQTypeFilter
 * JD-Core Version:    0.7.0.1
 */