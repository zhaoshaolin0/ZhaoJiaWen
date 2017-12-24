package org.jivesoftware.smack;

import java.util.LinkedList;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.packet.Packet;

public class PacketCollector
{
  private static final int MAX_PACKETS = 65536;
  private boolean cancelled = false;
  private PacketFilter packetFilter;
  private PacketReader packetReader;
  private LinkedList<Packet> resultQueue;
  
  protected PacketCollector(PacketReader paramPacketReader, PacketFilter paramPacketFilter)
  {
    this.packetReader = paramPacketReader;
    this.packetFilter = paramPacketFilter;
    this.resultQueue = new LinkedList();
  }
  
  public void cancel()
  {
    if (!this.cancelled)
    {
      this.cancelled = true;
      this.packetReader.cancelPacketCollector(this);
    }
  }
  
  public PacketFilter getPacketFilter()
  {
    return this.packetFilter;
  }
  
  public Packet nextResult()
  {
    try
    {
      for (;;)
      {
        boolean bool = this.resultQueue.isEmpty();
        if (!bool) {
          break;
        }
        try
        {
          wait();
        }
        catch (InterruptedException localInterruptedException) {}
      }
      Packet localPacket = (Packet)this.resultQueue.removeLast();
      return localPacket;
    }
    finally {}
  }
  
  public Packet nextResult(long paramLong)
  {
    long l2;
    long l1;
    label42:
    Packet localPacket;
    label64:
    try
    {
      if (this.resultQueue.isEmpty())
      {
        l2 = System.currentTimeMillis();
        l1 = paramLong;
        paramLong = l2;
      }
    }
    finally {}
    try
    {
      bool = this.resultQueue.isEmpty();
      if (bool) {
        if (l1 > 0L) {
          break label64;
        }
      }
    }
    catch (InterruptedException localInterruptedException)
    {
      break label42;
    }
    boolean bool = this.resultQueue.isEmpty();
    if (bool) {
      localPacket = null;
    }
    for (;;)
    {
      return localPacket;
      wait(l1);
      l2 = System.currentTimeMillis();
      l1 -= l2 - paramLong;
      paramLong = l2;
      break;
      localPacket = (Packet)this.resultQueue.removeLast();
      continue;
      localPacket = (Packet)this.resultQueue.removeLast();
    }
  }
  
  /* Error */
  public Packet pollResult()
  {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield 32	org/jivesoftware/smack/PacketCollector:resultQueue	Ljava/util/LinkedList;
    //   6: invokevirtual 50	java/util/LinkedList:isEmpty	()Z
    //   9: istore_1
    //   10: iload_1
    //   11: ifeq +9 -> 20
    //   14: aconst_null
    //   15: astore_2
    //   16: aload_0
    //   17: monitorexit
    //   18: aload_2
    //   19: areturn
    //   20: aload_0
    //   21: getfield 32	org/jivesoftware/smack/PacketCollector:resultQueue	Ljava/util/LinkedList;
    //   24: invokevirtual 57	java/util/LinkedList:removeLast	()Ljava/lang/Object;
    //   27: checkcast 59	org/jivesoftware/smack/packet/Packet
    //   30: astore_2
    //   31: goto -15 -> 16
    //   34: astore_2
    //   35: aload_0
    //   36: monitorexit
    //   37: aload_2
    //   38: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	39	0	this	PacketCollector
    //   9	2	1	bool	boolean
    //   15	16	2	localPacket	Packet
    //   34	4	2	localObject	Object
    // Exception table:
    //   from	to	target	type
    //   2	10	34	finally
    //   20	31	34	finally
  }
  
  protected void processPacket(Packet paramPacket)
  {
    if (paramPacket == null) {}
    for (;;)
    {
      return;
      try
      {
        if ((this.packetFilter != null) && (!this.packetFilter.accept(paramPacket))) {
          continue;
        }
        if (this.resultQueue.size() == 65536) {
          this.resultQueue.removeLast();
        }
        this.resultQueue.addFirst(paramPacket);
        notifyAll();
      }
      finally {}
    }
  }
}


/* Location:           C:\Users\Administrator\Documents\ds\反编译\AndroidKiller_v1.3.1\projects\Jungledsq_ppc\ProjectSrc\smali\
 * Qualified Name:     org.jivesoftware.smack.PacketCollector
 * JD-Core Version:    0.7.0.1
 */