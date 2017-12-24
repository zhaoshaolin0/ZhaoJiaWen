package org.jivesoftware.smackx.muc;

import java.util.LinkedList;
import org.jivesoftware.smack.packet.Packet;

class ConnectionDetachedPacketCollector
{
  private static final int MAX_PACKETS = 65536;
  private LinkedList<Packet> resultQueue = new LinkedList();
  
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
  
  /* Error */
  public Packet nextResult(long paramLong)
  {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield 19	org/jivesoftware/smackx/muc/ConnectionDetachedPacketCollector:resultQueue	Ljava/util/LinkedList;
    //   6: invokevirtual 28	java/util/LinkedList:isEmpty	()Z
    //   9: istore_3
    //   10: iload_3
    //   11: ifeq +8 -> 19
    //   14: aload_0
    //   15: lload_1
    //   16: invokevirtual 41	java/lang/Object:wait	(J)V
    //   19: aload_0
    //   20: getfield 19	org/jivesoftware/smackx/muc/ConnectionDetachedPacketCollector:resultQueue	Ljava/util/LinkedList;
    //   23: invokevirtual 28	java/util/LinkedList:isEmpty	()Z
    //   26: istore_3
    //   27: iload_3
    //   28: ifeq +11 -> 39
    //   31: aconst_null
    //   32: astore 4
    //   34: aload_0
    //   35: monitorexit
    //   36: aload 4
    //   38: areturn
    //   39: aload_0
    //   40: getfield 19	org/jivesoftware/smackx/muc/ConnectionDetachedPacketCollector:resultQueue	Ljava/util/LinkedList;
    //   43: invokevirtual 35	java/util/LinkedList:removeLast	()Ljava/lang/Object;
    //   46: checkcast 37	org/jivesoftware/smack/packet/Packet
    //   49: astore 4
    //   51: goto -17 -> 34
    //   54: astore 4
    //   56: goto -37 -> 19
    //   59: astore 4
    //   61: aload_0
    //   62: monitorexit
    //   63: aload 4
    //   65: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	66	0	this	ConnectionDetachedPacketCollector
    //   0	66	1	paramLong	long
    //   9	19	3	bool	boolean
    //   32	18	4	localPacket	Packet
    //   54	1	4	localInterruptedException	InterruptedException
    //   59	5	4	localObject	Object
    // Exception table:
    //   from	to	target	type
    //   14	19	54	java/lang/InterruptedException
    //   2	10	59	finally
    //   14	19	59	finally
    //   19	27	59	finally
    //   39	51	59	finally
  }
  
  /* Error */
  public Packet pollResult()
  {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield 19	org/jivesoftware/smackx/muc/ConnectionDetachedPacketCollector:resultQueue	Ljava/util/LinkedList;
    //   6: invokevirtual 28	java/util/LinkedList:isEmpty	()Z
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
    //   21: getfield 19	org/jivesoftware/smackx/muc/ConnectionDetachedPacketCollector:resultQueue	Ljava/util/LinkedList;
    //   24: invokevirtual 35	java/util/LinkedList:removeLast	()Ljava/lang/Object;
    //   27: checkcast 37	org/jivesoftware/smack/packet/Packet
    //   30: astore_2
    //   31: goto -15 -> 16
    //   34: astore_2
    //   35: aload_0
    //   36: monitorexit
    //   37: aload_2
    //   38: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	39	0	this	ConnectionDetachedPacketCollector
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
 * Qualified Name:     org.jivesoftware.smackx.muc.ConnectionDetachedPacketCollector
 * JD-Core Version:    0.7.0.1
 */