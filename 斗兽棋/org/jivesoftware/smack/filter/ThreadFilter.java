package org.jivesoftware.smack.filter;

import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;

public class ThreadFilter
  implements PacketFilter
{
  private String thread;
  
  public ThreadFilter(String paramString)
  {
    if (paramString == null) {
      throw new IllegalArgumentException("Thread cannot be null.");
    }
    this.thread = paramString;
  }
  
  public boolean accept(Packet paramPacket)
  {
    return ((paramPacket instanceof Message)) && (this.thread.equals(((Message)paramPacket).getThread()));
  }
}


/* Location:           C:\Users\Administrator\Documents\ds\反编译\AndroidKiller_v1.3.1\projects\Jungledsq_ppc\ProjectSrc\smali\
 * Qualified Name:     org.jivesoftware.smack.filter.ThreadFilter
 * JD-Core Version:    0.7.0.1
 */