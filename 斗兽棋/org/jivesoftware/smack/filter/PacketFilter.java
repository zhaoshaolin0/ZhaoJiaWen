package org.jivesoftware.smack.filter;

import org.jivesoftware.smack.packet.Packet;

public abstract interface PacketFilter
{
  public abstract boolean accept(Packet paramPacket);
}


/* Location:           C:\Users\Administrator\Documents\ds\反编译\AndroidKiller_v1.3.1\projects\Jungledsq_ppc\ProjectSrc\smali\
 * Qualified Name:     org.jivesoftware.smack.filter.PacketFilter
 * JD-Core Version:    0.7.0.1
 */