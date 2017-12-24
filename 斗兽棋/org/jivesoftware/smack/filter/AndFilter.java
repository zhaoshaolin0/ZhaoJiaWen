package org.jivesoftware.smack.filter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.jivesoftware.smack.packet.Packet;

public class AndFilter
  implements PacketFilter
{
  private List<PacketFilter> filters = new ArrayList();
  
  public AndFilter() {}
  
  public AndFilter(PacketFilter... paramVarArgs)
  {
    if (paramVarArgs == null) {
      throw new IllegalArgumentException("Parameter cannot be null.");
    }
    int j = paramVarArgs.length;
    int i = 0;
    while (i < j)
    {
      PacketFilter localPacketFilter = paramVarArgs[i];
      if (localPacketFilter == null) {
        throw new IllegalArgumentException("Parameter cannot be null.");
      }
      this.filters.add(localPacketFilter);
      i += 1;
    }
  }
  
  public boolean accept(Packet paramPacket)
  {
    Iterator localIterator = this.filters.iterator();
    while (localIterator.hasNext()) {
      if (!((PacketFilter)localIterator.next()).accept(paramPacket)) {
        return false;
      }
    }
    return true;
  }
  
  public void addFilter(PacketFilter paramPacketFilter)
  {
    if (paramPacketFilter == null) {
      throw new IllegalArgumentException("Parameter cannot be null.");
    }
    this.filters.add(paramPacketFilter);
  }
  
  public String toString()
  {
    return this.filters.toString();
  }
}


/* Location:           C:\Users\Administrator\Documents\ds\反编译\AndroidKiller_v1.3.1\projects\Jungledsq_ppc\ProjectSrc\smali\
 * Qualified Name:     org.jivesoftware.smack.filter.AndFilter
 * JD-Core Version:    0.7.0.1
 */