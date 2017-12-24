package org.jivesoftware.smackx.filetransfer;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.jivesoftware.smack.PacketCollector;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.SmackConfiguration;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.filter.AndFilter;
import org.jivesoftware.smack.filter.FromContainsFilter;
import org.jivesoftware.smack.filter.FromMatchesFilter;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.filter.PacketIDFilter;
import org.jivesoftware.smack.filter.PacketTypeFilter;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.packet.IQ.Type;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.packet.XMPPError;
import org.jivesoftware.smack.packet.XMPPError.Condition;
import org.jivesoftware.smack.util.StringUtils;
import org.jivesoftware.smackx.packet.IBBExtensions.Close;
import org.jivesoftware.smackx.packet.IBBExtensions.Data;
import org.jivesoftware.smackx.packet.IBBExtensions.Open;
import org.jivesoftware.smackx.packet.StreamInitiation;

public class IBBTransferNegotiator
  extends StreamNegotiator
{
  public static final int DEFAULT_BLOCK_SIZE = 4096;
  protected static final String NAMESPACE = "http://jabber.org/protocol/ibb";
  private XMPPConnection connection;
  
  protected IBBTransferNegotiator(XMPPConnection paramXMPPConnection)
  {
    this.connection = paramXMPPConnection;
  }
  
  private void initInBandTransfer(IBBExtensions.Open paramOpen)
  {
    this.connection.sendPacket(FileTransferNegotiator.createIQ(paramOpen.getPacketID(), paramOpen.getFrom(), paramOpen.getTo(), IQ.Type.RESULT));
  }
  
  public void cleanup() {}
  
  public InputStream createIncomingStream(StreamInitiation paramStreamInitiation)
    throws XMPPException
  {
    return negotiateIncomingStream(initiateIncomingStream(this.connection, paramStreamInitiation));
  }
  
  public OutputStream createOutgoingStream(String paramString1, String paramString2, String paramString3)
    throws XMPPException
  {
    Object localObject = new IBBExtensions.Open(paramString1, 4096);
    ((IBBExtensions.Open)localObject).setTo(paramString3);
    ((IBBExtensions.Open)localObject).setType(IQ.Type.SET);
    paramString2 = this.connection.createPacketCollector(new PacketIDFilter(((IBBExtensions.Open)localObject).getPacketID()));
    this.connection.sendPacket((Packet)localObject);
    localObject = (IQ)paramString2.nextResult(SmackConfiguration.getPacketReplyTimeout());
    paramString2.cancel();
    if (localObject == null) {
      throw new XMPPException("No response from peer on IBB open");
    }
    paramString2 = ((IQ)localObject).getType();
    if (!paramString2.equals(IQ.Type.RESULT))
    {
      if (paramString2.equals(IQ.Type.ERROR)) {
        throw new XMPPException("Target returned an error", ((IQ)localObject).getError());
      }
      throw new XMPPException("Target returned unknown response");
    }
    return new IBBOutputStream(paramString3, paramString1, 4096);
  }
  
  public PacketFilter getInitiationPacketFilter(String paramString1, String paramString2)
  {
    return new AndFilter(new PacketFilter[] { new FromContainsFilter(paramString1), new IBBOpenSidFilter(paramString2) });
  }
  
  public String[] getNamespaces()
  {
    return new String[] { "http://jabber.org/protocol/ibb" };
  }
  
  InputStream negotiateIncomingStream(Packet paramPacket)
    throws XMPPException
  {
    paramPacket = (IBBExtensions.Open)paramPacket;
    if (paramPacket.getType().equals(IQ.Type.ERROR)) {
      throw new XMPPException(paramPacket.getError());
    }
    Object localObject = new IBBMessageSidFilter(paramPacket.getFrom(), paramPacket.getSessionID());
    AndFilter localAndFilter = new AndFilter(new PacketFilter[] { new PacketTypeFilter(IBBExtensions.Close.class), new FromMatchesFilter(paramPacket.getFrom()) });
    localObject = new IBBInputStream(paramPacket.getSessionID(), (PacketFilter)localObject, localAndFilter, null);
    initInBandTransfer(paramPacket);
    return localObject;
  }
  
  private class IBBInputStream
    extends InputStream
    implements PacketListener
  {
    private byte[] buffer;
    private int bufferPointer;
    private IQ closeConfirmation;
    private PacketCollector dataCollector;
    private boolean isClosed;
    private boolean isDone;
    private boolean isEOF;
    private Message lastMess;
    private int seq = -1;
    private String streamID;
    
    private IBBInputStream(String paramString, PacketFilter paramPacketFilter1, PacketFilter paramPacketFilter2)
    {
      this.streamID = paramString;
      this.dataCollector = IBBTransferNegotiator.this.connection.createPacketCollector(paramPacketFilter1);
      IBBTransferNegotiator.this.connection.addPacketListener(this, paramPacketFilter2);
      this.bufferPointer = -1;
    }
    
    private void cancelTransfer(Message paramMessage)
    {
      cleanup();
      sendCancelMessage(paramMessage);
    }
    
    private void checkSequence(Message paramMessage, int paramInt)
      throws IOException
    {
      if (this.seq == 65535) {
        this.seq = -1;
      }
      if (paramInt - 1 != this.seq)
      {
        cancelTransfer(paramMessage);
        throw new IOException("Packets out of sequence");
      }
      this.seq = paramInt;
    }
    
    private void cleanup()
    {
      this.dataCollector.cancel();
      IBBTransferNegotiator.this.connection.removePacketListener(this);
    }
    
    private boolean loadBufferWait()
      throws IOException
    {
      Object localObject1 = null;
      while (localObject1 == null) {
        if (this.isDone)
        {
          localObject2 = (Message)this.dataCollector.pollResult();
          localObject1 = localObject2;
          if (localObject2 == null) {
            return false;
          }
        }
        else
        {
          localObject1 = (Message)this.dataCollector.nextResult(1000L);
        }
      }
      this.lastMess = ((Message)localObject1);
      Object localObject2 = (IBBExtensions.Data)((Message)localObject1).getExtension("data", "http://jabber.org/protocol/ibb");
      checkSequence((Message)localObject1, (int)((IBBExtensions.Data)localObject2).getSeq());
      this.buffer = StringUtils.decodeBase64(((IBBExtensions.Data)localObject2).getData());
      this.bufferPointer = 0;
      return true;
    }
    
    private void sendCancelMessage(Message paramMessage)
    {
      paramMessage = FileTransferNegotiator.createIQ(paramMessage.getPacketID(), paramMessage.getFrom(), paramMessage.getTo(), IQ.Type.ERROR);
      paramMessage.setError(new XMPPError(XMPPError.Condition.remote_server_timeout, "Cancel Message Transfer"));
      IBBTransferNegotiator.this.connection.sendPacket(paramMessage);
    }
    
    private void sendCloseConfirmation()
    {
      IBBTransferNegotiator.this.connection.sendPacket(this.closeConfirmation);
    }
    
    public void close()
      throws IOException
    {
      for (;;)
      {
        try
        {
          boolean bool = this.isClosed;
          if (bool) {
            return;
          }
          cleanup();
          if (this.isEOF)
          {
            sendCloseConfirmation();
            this.isClosed = true;
            continue;
          }
          if (this.lastMess == null) {
            continue;
          }
        }
        finally {}
        sendCancelMessage(this.lastMess);
      }
    }
    
    public boolean markSupported()
    {
      return false;
    }
    
    public void processPacket(Packet paramPacket)
    {
      if (((IBBExtensions.Close)paramPacket).getSessionID().equals(this.streamID))
      {
        this.isDone = true;
        this.closeConfirmation = FileTransferNegotiator.createIQ(paramPacket.getPacketID(), paramPacket.getFrom(), paramPacket.getTo(), IQ.Type.RESULT);
      }
    }
    
    /* Error */
    public int read()
      throws IOException
    {
      // Byte code:
      //   0: aload_0
      //   1: monitorenter
      //   2: aload_0
      //   3: getfield 189	org/jivesoftware/smackx/filetransfer/IBBTransferNegotiator$IBBInputStream:isEOF	Z
      //   6: ifne +12 -> 18
      //   9: aload_0
      //   10: getfield 187	org/jivesoftware/smackx/filetransfer/IBBTransferNegotiator$IBBInputStream:isClosed	Z
      //   13: istore_2
      //   14: iload_2
      //   15: ifeq +9 -> 24
      //   18: iconst_m1
      //   19: istore_1
      //   20: aload_0
      //   21: monitorexit
      //   22: iload_1
      //   23: ireturn
      //   24: aload_0
      //   25: getfield 57	org/jivesoftware/smackx/filetransfer/IBBTransferNegotiator$IBBInputStream:bufferPointer	I
      //   28: iconst_m1
      //   29: if_icmpeq +15 -> 44
      //   32: aload_0
      //   33: getfield 57	org/jivesoftware/smackx/filetransfer/IBBTransferNegotiator$IBBInputStream:bufferPointer	I
      //   36: aload_0
      //   37: getfield 137	org/jivesoftware/smackx/filetransfer/IBBTransferNegotiator$IBBInputStream:buffer	[B
      //   40: arraylength
      //   41: if_icmplt +8 -> 49
      //   44: aload_0
      //   45: invokespecial 216	org/jivesoftware/smackx/filetransfer/IBBTransferNegotiator$IBBInputStream:loadBufferWait	()Z
      //   48: pop
      //   49: aload_0
      //   50: getfield 137	org/jivesoftware/smackx/filetransfer/IBBTransferNegotiator$IBBInputStream:buffer	[B
      //   53: astore_3
      //   54: aload_0
      //   55: getfield 57	org/jivesoftware/smackx/filetransfer/IBBTransferNegotiator$IBBInputStream:bufferPointer	I
      //   58: istore_1
      //   59: aload_0
      //   60: iload_1
      //   61: iconst_1
      //   62: iadd
      //   63: putfield 57	org/jivesoftware/smackx/filetransfer/IBBTransferNegotiator$IBBInputStream:bufferPointer	I
      //   66: aload_3
      //   67: iload_1
      //   68: baload
      //   69: istore_1
      //   70: goto -50 -> 20
      //   73: astore_3
      //   74: aload_0
      //   75: monitorexit
      //   76: aload_3
      //   77: athrow
      // Local variable table:
      //   start	length	slot	name	signature
      //   0	78	0	this	IBBInputStream
      //   19	51	1	i	int
      //   13	2	2	bool	boolean
      //   53	14	3	arrayOfByte	byte[]
      //   73	4	3	localObject	Object
      // Exception table:
      //   from	to	target	type
      //   2	14	73	finally
      //   24	44	73	finally
      //   44	49	73	finally
      //   49	66	73	finally
    }
    
    public int read(byte[] paramArrayOfByte)
      throws IOException
    {
      try
      {
        int i = read(paramArrayOfByte, 0, paramArrayOfByte.length);
        return i;
      }
      finally
      {
        paramArrayOfByte = finally;
        throw paramArrayOfByte;
      }
    }
    
    /* Error */
    public int read(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
      throws IOException
    {
      // Byte code:
      //   0: aload_0
      //   1: monitorenter
      //   2: aload_0
      //   3: getfield 189	org/jivesoftware/smackx/filetransfer/IBBTransferNegotiator$IBBInputStream:isEOF	Z
      //   6: ifne +14 -> 20
      //   9: aload_0
      //   10: getfield 187	org/jivesoftware/smackx/filetransfer/IBBTransferNegotiator$IBBInputStream:isClosed	Z
      //   13: istore 5
      //   15: iload 5
      //   17: ifeq +9 -> 26
      //   20: iconst_m1
      //   21: istore_2
      //   22: aload_0
      //   23: monitorexit
      //   24: iload_2
      //   25: ireturn
      //   26: aload_0
      //   27: getfield 57	org/jivesoftware/smackx/filetransfer/IBBTransferNegotiator$IBBInputStream:bufferPointer	I
      //   30: iconst_m1
      //   31: if_icmpeq +15 -> 46
      //   34: aload_0
      //   35: getfield 57	org/jivesoftware/smackx/filetransfer/IBBTransferNegotiator$IBBInputStream:bufferPointer	I
      //   38: aload_0
      //   39: getfield 137	org/jivesoftware/smackx/filetransfer/IBBTransferNegotiator$IBBInputStream:buffer	[B
      //   42: arraylength
      //   43: if_icmplt +20 -> 63
      //   46: aload_0
      //   47: invokespecial 216	org/jivesoftware/smackx/filetransfer/IBBTransferNegotiator$IBBInputStream:loadBufferWait	()Z
      //   50: ifne +13 -> 63
      //   53: aload_0
      //   54: iconst_1
      //   55: putfield 189	org/jivesoftware/smackx/filetransfer/IBBTransferNegotiator$IBBInputStream:isEOF	Z
      //   58: iconst_m1
      //   59: istore_2
      //   60: goto -38 -> 22
      //   63: iload_3
      //   64: istore 4
      //   66: iload_3
      //   67: iload_2
      //   68: isub
      //   69: aload_0
      //   70: getfield 137	org/jivesoftware/smackx/filetransfer/IBBTransferNegotiator$IBBInputStream:buffer	[B
      //   73: arraylength
      //   74: aload_0
      //   75: getfield 57	org/jivesoftware/smackx/filetransfer/IBBTransferNegotiator$IBBInputStream:bufferPointer	I
      //   78: isub
      //   79: if_icmple +15 -> 94
      //   82: aload_0
      //   83: getfield 137	org/jivesoftware/smackx/filetransfer/IBBTransferNegotiator$IBBInputStream:buffer	[B
      //   86: arraylength
      //   87: aload_0
      //   88: getfield 57	org/jivesoftware/smackx/filetransfer/IBBTransferNegotiator$IBBInputStream:bufferPointer	I
      //   91: isub
      //   92: istore 4
      //   94: aload_0
      //   95: getfield 137	org/jivesoftware/smackx/filetransfer/IBBTransferNegotiator$IBBInputStream:buffer	[B
      //   98: aload_0
      //   99: getfield 57	org/jivesoftware/smackx/filetransfer/IBBTransferNegotiator$IBBInputStream:bufferPointer	I
      //   102: aload_1
      //   103: iload_2
      //   104: iload 4
      //   106: invokestatic 226	java/lang/System:arraycopy	(Ljava/lang/Object;ILjava/lang/Object;II)V
      //   109: aload_0
      //   110: aload_0
      //   111: getfield 57	org/jivesoftware/smackx/filetransfer/IBBTransferNegotiator$IBBInputStream:bufferPointer	I
      //   114: iload 4
      //   116: iadd
      //   117: putfield 57	org/jivesoftware/smackx/filetransfer/IBBTransferNegotiator$IBBInputStream:bufferPointer	I
      //   120: iload 4
      //   122: istore_2
      //   123: goto -101 -> 22
      //   126: astore_1
      //   127: aload_0
      //   128: monitorexit
      //   129: aload_1
      //   130: athrow
      // Local variable table:
      //   start	length	slot	name	signature
      //   0	131	0	this	IBBInputStream
      //   0	131	1	paramArrayOfByte	byte[]
      //   0	131	2	paramInt1	int
      //   0	131	3	paramInt2	int
      //   64	57	4	i	int
      //   13	3	5	bool	boolean
      // Exception table:
      //   from	to	target	type
      //   2	15	126	finally
      //   26	46	126	finally
      //   46	58	126	finally
      //   66	94	126	finally
      //   94	120	126	finally
    }
  }
  
  private static class IBBMessageSidFilter
    implements PacketFilter
  {
    private String from;
    private final String sessionID;
    
    public IBBMessageSidFilter(String paramString1, String paramString2)
    {
      this.from = paramString1;
      this.sessionID = paramString2;
    }
    
    public boolean accept(Packet paramPacket)
    {
      if (!(paramPacket instanceof Message)) {
        return false;
      }
      if (!paramPacket.getFrom().equalsIgnoreCase(this.from)) {
        return false;
      }
      paramPacket = (IBBExtensions.Data)paramPacket.getExtension("data", "http://jabber.org/protocol/ibb");
      return (paramPacket != null) && (paramPacket.getSessionID() != null) && (paramPacket.getSessionID().equalsIgnoreCase(this.sessionID));
    }
  }
  
  private static class IBBOpenSidFilter
    implements PacketFilter
  {
    private String sessionID;
    
    public IBBOpenSidFilter(String paramString)
    {
      if (paramString == null) {
        throw new IllegalArgumentException("StreamID cannot be null");
      }
      this.sessionID = paramString;
    }
    
    public boolean accept(Packet paramPacket)
    {
      if (!IBBExtensions.Open.class.isInstance(paramPacket)) {
        return false;
      }
      paramPacket = ((IBBExtensions.Open)paramPacket).getSessionID();
      return (paramPacket != null) && (paramPacket.equals(this.sessionID));
    }
  }
  
  private class IBBOutputStream
    extends OutputStream
  {
    protected byte[] buffer;
    private final IQ closePacket;
    protected int count = 0;
    private String messageID;
    protected int seq = 0;
    private String sid;
    final String userID;
    
    IBBOutputStream(String paramString1, String paramString2, int paramInt)
    {
      if (paramInt <= 0) {
        throw new IllegalArgumentException("Buffer size <= 0");
      }
      this.buffer = new byte[paramInt];
      this.userID = paramString1;
      this.messageID = new Message(paramString1).getPacketID();
      this.sid = paramString2;
      this.closePacket = createClosePacket(paramString1, paramString2);
    }
    
    private IQ createClosePacket(String paramString1, String paramString2)
    {
      paramString2 = new IBBExtensions.Close(paramString2);
      paramString2.setTo(paramString1);
      paramString2.setType(IQ.Type.SET);
      return paramString2;
    }
    
    private void flushBuffer()
    {
      try
      {
        writeToXML(this.buffer, 0, this.count);
        this.count = 0;
        return;
      }
      finally
      {
        localObject = finally;
        throw localObject;
      }
    }
    
    private void writeOut(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
    {
      if (paramInt2 > this.buffer.length - this.count) {
        flushBuffer();
      }
      System.arraycopy(paramArrayOfByte, paramInt1, this.buffer, this.count, paramInt2);
      this.count += paramInt2;
    }
    
    /* Error */
    private void writeToXML(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
    {
      // Byte code:
      //   0: aload_0
      //   1: monitorenter
      //   2: aload_0
      //   3: new 96	java/lang/StringBuilder
      //   6: dup
      //   7: invokespecial 97	java/lang/StringBuilder:<init>	()V
      //   10: aload_0
      //   11: getfield 51	org/jivesoftware/smackx/filetransfer/IBBTransferNegotiator$IBBOutputStream:messageID	Ljava/lang/String;
      //   14: invokevirtual 101	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   17: ldc 103
      //   19: invokevirtual 101	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   22: aload_0
      //   23: getfield 31	org/jivesoftware/smackx/filetransfer/IBBTransferNegotiator$IBBOutputStream:seq	I
      //   26: invokevirtual 106	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
      //   29: invokevirtual 109	java/lang/StringBuilder:toString	()Ljava/lang/String;
      //   32: invokevirtual 113	org/jivesoftware/smackx/filetransfer/IBBTransferNegotiator$IBBOutputStream:createTemplate	(Ljava/lang/String;)Lorg/jivesoftware/smack/packet/Message;
      //   35: astore 4
      //   37: new 115	org/jivesoftware/smackx/packet/IBBExtensions$Data
      //   40: dup
      //   41: aload_0
      //   42: getfield 53	org/jivesoftware/smackx/filetransfer/IBBTransferNegotiator$IBBOutputStream:sid	Ljava/lang/String;
      //   45: invokespecial 116	org/jivesoftware/smackx/packet/IBBExtensions$Data:<init>	(Ljava/lang/String;)V
      //   48: astore 5
      //   50: aload 4
      //   52: aload 5
      //   54: invokevirtual 120	org/jivesoftware/smack/packet/Message:addExtension	(Lorg/jivesoftware/smack/packet/PacketExtension;)V
      //   57: aload 5
      //   59: aload_1
      //   60: iload_2
      //   61: iload_3
      //   62: iconst_0
      //   63: invokestatic 126	org/jivesoftware/smack/util/StringUtils:encodeBase64	([BIIZ)Ljava/lang/String;
      //   66: invokevirtual 129	org/jivesoftware/smackx/packet/IBBExtensions$Data:setData	(Ljava/lang/String;)V
      //   69: aload 5
      //   71: aload_0
      //   72: getfield 31	org/jivesoftware/smackx/filetransfer/IBBTransferNegotiator$IBBOutputStream:seq	I
      //   75: i2l
      //   76: invokevirtual 133	org/jivesoftware/smackx/packet/IBBExtensions$Data:setSeq	(J)V
      //   79: aload_0
      //   80: monitorenter
      //   81: aload_0
      //   82: ldc2_w 134
      //   85: invokevirtual 140	java/lang/Object:wait	(J)V
      //   88: aload_0
      //   89: monitorexit
      //   90: aload_0
      //   91: getfield 24	org/jivesoftware/smackx/filetransfer/IBBTransferNegotiator$IBBOutputStream:this$0	Lorg/jivesoftware/smackx/filetransfer/IBBTransferNegotiator;
      //   94: invokestatic 144	org/jivesoftware/smackx/filetransfer/IBBTransferNegotiator:access$100	(Lorg/jivesoftware/smackx/filetransfer/IBBTransferNegotiator;)Lorg/jivesoftware/smack/XMPPConnection;
      //   97: aload 4
      //   99: invokevirtual 150	org/jivesoftware/smack/XMPPConnection:sendPacket	(Lorg/jivesoftware/smack/packet/Packet;)V
      //   102: aload_0
      //   103: getfield 31	org/jivesoftware/smackx/filetransfer/IBBTransferNegotiator$IBBOutputStream:seq	I
      //   106: iconst_1
      //   107: iadd
      //   108: ldc 151
      //   110: if_icmpne +23 -> 133
      //   113: iconst_0
      //   114: istore_2
      //   115: aload_0
      //   116: iload_2
      //   117: putfield 31	org/jivesoftware/smackx/filetransfer/IBBTransferNegotiator$IBBOutputStream:seq	I
      //   120: aload_0
      //   121: monitorexit
      //   122: return
      //   123: astore_1
      //   124: aload_0
      //   125: monitorexit
      //   126: aload_1
      //   127: athrow
      //   128: astore_1
      //   129: aload_0
      //   130: monitorexit
      //   131: aload_1
      //   132: athrow
      //   133: aload_0
      //   134: getfield 31	org/jivesoftware/smackx/filetransfer/IBBTransferNegotiator$IBBOutputStream:seq	I
      //   137: istore_2
      //   138: iload_2
      //   139: iconst_1
      //   140: iadd
      //   141: istore_2
      //   142: goto -27 -> 115
      //   145: astore_1
      //   146: goto -58 -> 88
      // Local variable table:
      //   start	length	slot	name	signature
      //   0	149	0	this	IBBOutputStream
      //   0	149	1	paramArrayOfByte	byte[]
      //   0	149	2	paramInt1	int
      //   0	149	3	paramInt2	int
      //   35	63	4	localMessage	Message
      //   48	22	5	localData	IBBExtensions.Data
      // Exception table:
      //   from	to	target	type
      //   81	88	123	finally
      //   88	90	123	finally
      //   124	126	123	finally
      //   2	81	128	finally
      //   90	113	128	finally
      //   115	120	128	finally
      //   126	128	128	finally
      //   133	138	128	finally
      //   81	88	145	java/lang/InterruptedException
    }
    
    public void close()
      throws IOException
    {
      flush();
      IBBTransferNegotiator.this.connection.sendPacket(this.closePacket);
    }
    
    public Message createTemplate(String paramString)
    {
      Message localMessage = new Message(this.userID);
      localMessage.setPacketID(paramString);
      return localMessage;
    }
    
    public void flush()
      throws IOException
    {
      flushBuffer();
    }
    
    public void write(int paramInt)
      throws IOException
    {
      if (this.count >= this.buffer.length) {
        flushBuffer();
      }
      byte[] arrayOfByte = this.buffer;
      int i = this.count;
      this.count = (i + 1);
      arrayOfByte[i] = ((byte)paramInt);
    }
    
    public void write(byte[] paramArrayOfByte)
      throws IOException
    {
      write(paramArrayOfByte, 0, paramArrayOfByte.length);
    }
    
    /* Error */
    public void write(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
      throws IOException
    {
      // Byte code:
      //   0: aload_0
      //   1: monitorenter
      //   2: iload_3
      //   3: aload_0
      //   4: getfield 40	org/jivesoftware/smackx/filetransfer/IBBTransferNegotiator$IBBOutputStream:buffer	[B
      //   7: arraylength
      //   8: if_icmplt +36 -> 44
      //   11: aload_0
      //   12: aload_1
      //   13: iload_2
      //   14: aload_0
      //   15: getfield 40	org/jivesoftware/smackx/filetransfer/IBBTransferNegotiator$IBBOutputStream:buffer	[B
      //   18: arraylength
      //   19: invokespecial 168	org/jivesoftware/smackx/filetransfer/IBBTransferNegotiator$IBBOutputStream:writeOut	([BII)V
      //   22: aload_0
      //   23: aload_1
      //   24: aload_0
      //   25: getfield 40	org/jivesoftware/smackx/filetransfer/IBBTransferNegotiator$IBBOutputStream:buffer	[B
      //   28: arraylength
      //   29: iload_2
      //   30: iadd
      //   31: iload_3
      //   32: aload_0
      //   33: getfield 40	org/jivesoftware/smackx/filetransfer/IBBTransferNegotiator$IBBOutputStream:buffer	[B
      //   36: arraylength
      //   37: isub
      //   38: invokevirtual 166	org/jivesoftware/smackx/filetransfer/IBBTransferNegotiator$IBBOutputStream:write	([BII)V
      //   41: aload_0
      //   42: monitorexit
      //   43: return
      //   44: aload_0
      //   45: aload_1
      //   46: iload_2
      //   47: iload_3
      //   48: invokespecial 168	org/jivesoftware/smackx/filetransfer/IBBTransferNegotiator$IBBOutputStream:writeOut	([BII)V
      //   51: goto -10 -> 41
      //   54: astore_1
      //   55: aload_0
      //   56: monitorexit
      //   57: aload_1
      //   58: athrow
      // Local variable table:
      //   start	length	slot	name	signature
      //   0	59	0	this	IBBOutputStream
      //   0	59	1	paramArrayOfByte	byte[]
      //   0	59	2	paramInt1	int
      //   0	59	3	paramInt2	int
      // Exception table:
      //   from	to	target	type
      //   2	41	54	finally
      //   44	51	54	finally
    }
  }
}


/* Location:           C:\Users\Administrator\Documents\ds\反编译\AndroidKiller_v1.3.1\projects\Jungledsq_ppc\ProjectSrc\smali\
 * Qualified Name:     org.jivesoftware.smackx.filetransfer.IBBTransferNegotiator
 * JD-Core Version:    0.7.0.1
 */