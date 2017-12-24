package org.jivesoftware.smackx.filetransfer;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.XMPPError;

public class OutgoingFileTransfer
  extends FileTransfer
{
  private static int RESPONSE_TIMEOUT = 60000;
  private NegotiationProgress callback;
  private String initiator;
  private OutputStream outputStream;
  private Thread transferThread;
  
  protected OutgoingFileTransfer(String paramString1, String paramString2, String paramString3, FileTransferNegotiator paramFileTransferNegotiator)
  {
    super(paramString2, paramString3, paramFileTransferNegotiator);
    this.initiator = paramString1;
  }
  
  private void checkTransferThread()
  {
    if (((this.transferThread != null) && (this.transferThread.isAlive())) || (isDone())) {
      throw new IllegalStateException("File transfer in progress or has already completed.");
    }
  }
  
  public static int getResponseTimeout()
  {
    return RESPONSE_TIMEOUT;
  }
  
  private void handleXMPPException(XMPPException paramXMPPException)
  {
    XMPPError localXMPPError = paramXMPPException.getXMPPError();
    if (localXMPPError != null)
    {
      int i = localXMPPError.getCode();
      if (i == 403)
      {
        setStatus(FileTransfer.Status.refused);
        return;
      }
      if (i != 400) {
        break label56;
      }
      setStatus(FileTransfer.Status.error);
      setError(FileTransfer.Error.not_acceptable);
    }
    for (;;)
    {
      setException(paramXMPPException);
      return;
      label56:
      setStatus(FileTransfer.Status.error);
    }
  }
  
  private OutputStream negotiateStream(String paramString1, long paramLong, String paramString2)
    throws XMPPException
  {
    if (!updateStatus(FileTransfer.Status.initial, FileTransfer.Status.negotiating_transfer)) {
      throw new XMPPException("Illegal state change");
    }
    paramString1 = this.negotiator.negotiateOutgoingTransfer(getPeer(), this.streamID, paramString1, paramLong, paramString2, RESPONSE_TIMEOUT);
    if (paramString1 == null)
    {
      setStatus(FileTransfer.Status.error);
      setError(FileTransfer.Error.no_response);
      return null;
    }
    if (!updateStatus(FileTransfer.Status.negotiating_transfer, FileTransfer.Status.negotiating_stream)) {
      throw new XMPPException("Illegal state change");
    }
    this.outputStream = paramString1.createOutgoingStream(this.streamID, this.initiator, getPeer());
    if (!updateStatus(FileTransfer.Status.negotiating_stream, FileTransfer.Status.negotiated)) {
      throw new XMPPException("Illegal state change");
    }
    return this.outputStream;
  }
  
  public static void setResponseTimeout(int paramInt)
  {
    RESPONSE_TIMEOUT = paramInt;
  }
  
  public void cancel()
  {
    setStatus(FileTransfer.Status.cancelled);
  }
  
  public long getBytesSent()
  {
    return this.amountWritten;
  }
  
  protected OutputStream getOutputStream()
  {
    if (getStatus().equals(FileTransfer.Status.negotiated)) {
      return this.outputStream;
    }
    return null;
  }
  
  /* Error */
  public OutputStream sendFile(String paramString1, long paramLong, String paramString2)
    throws XMPPException
  {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: invokevirtual 69	org/jivesoftware/smackx/filetransfer/OutgoingFileTransfer:isDone	()Z
    //   6: ifne +10 -> 16
    //   9: aload_0
    //   10: getfield 40	org/jivesoftware/smackx/filetransfer/OutgoingFileTransfer:outputStream	Ljava/io/OutputStream;
    //   13: ifnull +18 -> 31
    //   16: new 71	java/lang/IllegalStateException
    //   19: dup
    //   20: ldc 184
    //   22: invokespecial 76	java/lang/IllegalStateException:<init>	(Ljava/lang/String;)V
    //   25: athrow
    //   26: astore_1
    //   27: aload_0
    //   28: monitorexit
    //   29: aload_1
    //   30: athrow
    //   31: aload_0
    //   32: aload_0
    //   33: aload_1
    //   34: lload_2
    //   35: aload 4
    //   37: invokespecial 50	org/jivesoftware/smackx/filetransfer/OutgoingFileTransfer:negotiateStream	(Ljava/lang/String;JLjava/lang/String;)Ljava/io/OutputStream;
    //   40: putfield 40	org/jivesoftware/smackx/filetransfer/OutgoingFileTransfer:outputStream	Ljava/io/OutputStream;
    //   43: aload_0
    //   44: getfield 40	org/jivesoftware/smackx/filetransfer/OutgoingFileTransfer:outputStream	Ljava/io/OutputStream;
    //   47: astore_1
    //   48: aload_0
    //   49: monitorexit
    //   50: aload_1
    //   51: areturn
    //   52: astore_1
    //   53: aload_0
    //   54: aload_1
    //   55: invokespecial 57	org/jivesoftware/smackx/filetransfer/OutgoingFileTransfer:handleXMPPException	(Lorg/jivesoftware/smack/XMPPException;)V
    //   58: aload_1
    //   59: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	60	0	this	OutgoingFileTransfer
    //   0	60	1	paramString1	String
    //   0	60	2	paramLong	long
    //   0	60	4	paramString2	String
    // Exception table:
    //   from	to	target	type
    //   2	16	26	finally
    //   16	26	26	finally
    //   31	43	26	finally
    //   43	48	26	finally
    //   53	60	26	finally
    //   31	43	52	org/jivesoftware/smack/XMPPException
  }
  
  public void sendFile(final File paramFile, final String paramString)
    throws XMPPException
  {
    try
    {
      checkTransferThread();
      if ((paramFile == null) || (!paramFile.exists()) || (!paramFile.canRead())) {
        throw new IllegalArgumentException("Could not read file");
      }
    }
    finally {}
    setFileInfo(paramFile.getAbsolutePath(), paramFile.getName(), paramFile.length());
    this.transferThread = new Thread(new Runnable()
    {
      /* Error */
      public void run()
      {
        // Byte code:
        //   0: aload_0
        //   1: getfield 21	org/jivesoftware/smackx/filetransfer/OutgoingFileTransfer$2:this$0	Lorg/jivesoftware/smackx/filetransfer/OutgoingFileTransfer;
        //   4: aload_0
        //   5: getfield 21	org/jivesoftware/smackx/filetransfer/OutgoingFileTransfer$2:this$0	Lorg/jivesoftware/smackx/filetransfer/OutgoingFileTransfer;
        //   8: aload_0
        //   9: getfield 23	org/jivesoftware/smackx/filetransfer/OutgoingFileTransfer$2:val$file	Ljava/io/File;
        //   12: invokevirtual 42	java/io/File:getName	()Ljava/lang/String;
        //   15: aload_0
        //   16: getfield 23	org/jivesoftware/smackx/filetransfer/OutgoingFileTransfer$2:val$file	Ljava/io/File;
        //   19: invokevirtual 46	java/io/File:length	()J
        //   22: aload_0
        //   23: getfield 25	org/jivesoftware/smackx/filetransfer/OutgoingFileTransfer$2:val$description	Ljava/lang/String;
        //   26: invokestatic 50	org/jivesoftware/smackx/filetransfer/OutgoingFileTransfer:access$100	(Lorg/jivesoftware/smackx/filetransfer/OutgoingFileTransfer;Ljava/lang/String;JLjava/lang/String;)Ljava/io/OutputStream;
        //   29: invokestatic 54	org/jivesoftware/smackx/filetransfer/OutgoingFileTransfer:access$002	(Lorg/jivesoftware/smackx/filetransfer/OutgoingFileTransfer;Ljava/io/OutputStream;)Ljava/io/OutputStream;
        //   32: pop
        //   33: aload_0
        //   34: getfield 21	org/jivesoftware/smackx/filetransfer/OutgoingFileTransfer$2:this$0	Lorg/jivesoftware/smackx/filetransfer/OutgoingFileTransfer;
        //   37: invokestatic 58	org/jivesoftware/smackx/filetransfer/OutgoingFileTransfer:access$000	(Lorg/jivesoftware/smackx/filetransfer/OutgoingFileTransfer;)Ljava/io/OutputStream;
        //   40: ifnonnull +14 -> 54
        //   43: return
        //   44: astore_1
        //   45: aload_0
        //   46: getfield 21	org/jivesoftware/smackx/filetransfer/OutgoingFileTransfer$2:this$0	Lorg/jivesoftware/smackx/filetransfer/OutgoingFileTransfer;
        //   49: aload_1
        //   50: invokestatic 62	org/jivesoftware/smackx/filetransfer/OutgoingFileTransfer:access$200	(Lorg/jivesoftware/smackx/filetransfer/OutgoingFileTransfer;Lorg/jivesoftware/smack/XMPPException;)V
        //   53: return
        //   54: aload_0
        //   55: getfield 21	org/jivesoftware/smackx/filetransfer/OutgoingFileTransfer$2:this$0	Lorg/jivesoftware/smackx/filetransfer/OutgoingFileTransfer;
        //   58: getstatic 68	org/jivesoftware/smackx/filetransfer/FileTransfer$Status:negotiated	Lorg/jivesoftware/smackx/filetransfer/FileTransfer$Status;
        //   61: getstatic 71	org/jivesoftware/smackx/filetransfer/FileTransfer$Status:in_progress	Lorg/jivesoftware/smackx/filetransfer/FileTransfer$Status;
        //   64: invokevirtual 75	org/jivesoftware/smackx/filetransfer/OutgoingFileTransfer:updateStatus	(Lorg/jivesoftware/smackx/filetransfer/FileTransfer$Status;Lorg/jivesoftware/smackx/filetransfer/FileTransfer$Status;)Z
        //   67: ifeq -24 -> 43
        //   70: aconst_null
        //   71: astore 5
        //   73: aconst_null
        //   74: astore_1
        //   75: aconst_null
        //   76: astore 4
        //   78: new 77	java/io/FileInputStream
        //   81: dup
        //   82: aload_0
        //   83: getfield 23	org/jivesoftware/smackx/filetransfer/OutgoingFileTransfer$2:val$file	Ljava/io/File;
        //   86: invokespecial 80	java/io/FileInputStream:<init>	(Ljava/io/File;)V
        //   89: astore_2
        //   90: aload_0
        //   91: getfield 21	org/jivesoftware/smackx/filetransfer/OutgoingFileTransfer$2:this$0	Lorg/jivesoftware/smackx/filetransfer/OutgoingFileTransfer;
        //   94: aload_2
        //   95: aload_0
        //   96: getfield 21	org/jivesoftware/smackx/filetransfer/OutgoingFileTransfer$2:this$0	Lorg/jivesoftware/smackx/filetransfer/OutgoingFileTransfer;
        //   99: invokestatic 58	org/jivesoftware/smackx/filetransfer/OutgoingFileTransfer:access$000	(Lorg/jivesoftware/smackx/filetransfer/OutgoingFileTransfer;)Ljava/io/OutputStream;
        //   102: invokevirtual 84	org/jivesoftware/smackx/filetransfer/OutgoingFileTransfer:writeToStream	(Ljava/io/InputStream;Ljava/io/OutputStream;)V
        //   105: aload_2
        //   106: ifnull +7 -> 113
        //   109: aload_2
        //   110: invokevirtual 89	java/io/InputStream:close	()V
        //   113: aload_0
        //   114: getfield 21	org/jivesoftware/smackx/filetransfer/OutgoingFileTransfer$2:this$0	Lorg/jivesoftware/smackx/filetransfer/OutgoingFileTransfer;
        //   117: invokestatic 58	org/jivesoftware/smackx/filetransfer/OutgoingFileTransfer:access$000	(Lorg/jivesoftware/smackx/filetransfer/OutgoingFileTransfer;)Ljava/io/OutputStream;
        //   120: invokevirtual 94	java/io/OutputStream:flush	()V
        //   123: aload_0
        //   124: getfield 21	org/jivesoftware/smackx/filetransfer/OutgoingFileTransfer$2:this$0	Lorg/jivesoftware/smackx/filetransfer/OutgoingFileTransfer;
        //   127: invokestatic 58	org/jivesoftware/smackx/filetransfer/OutgoingFileTransfer:access$000	(Lorg/jivesoftware/smackx/filetransfer/OutgoingFileTransfer;)Ljava/io/OutputStream;
        //   130: invokevirtual 95	java/io/OutputStream:close	()V
        //   133: aload_0
        //   134: getfield 21	org/jivesoftware/smackx/filetransfer/OutgoingFileTransfer$2:this$0	Lorg/jivesoftware/smackx/filetransfer/OutgoingFileTransfer;
        //   137: getstatic 71	org/jivesoftware/smackx/filetransfer/FileTransfer$Status:in_progress	Lorg/jivesoftware/smackx/filetransfer/FileTransfer$Status;
        //   140: getstatic 98	org/jivesoftware/smackx/filetransfer/FileTransfer$Status:complete	Lorg/jivesoftware/smackx/filetransfer/FileTransfer$Status;
        //   143: invokevirtual 75	org/jivesoftware/smackx/filetransfer/OutgoingFileTransfer:updateStatus	(Lorg/jivesoftware/smackx/filetransfer/FileTransfer$Status;Lorg/jivesoftware/smackx/filetransfer/FileTransfer$Status;)Z
        //   146: pop
        //   147: return
        //   148: astore_1
        //   149: goto -16 -> 133
        //   152: astore_3
        //   153: aload 4
        //   155: astore_2
        //   156: aload_2
        //   157: astore_1
        //   158: aload_0
        //   159: getfield 21	org/jivesoftware/smackx/filetransfer/OutgoingFileTransfer$2:this$0	Lorg/jivesoftware/smackx/filetransfer/OutgoingFileTransfer;
        //   162: getstatic 101	org/jivesoftware/smackx/filetransfer/FileTransfer$Status:error	Lorg/jivesoftware/smackx/filetransfer/FileTransfer$Status;
        //   165: invokevirtual 105	org/jivesoftware/smackx/filetransfer/OutgoingFileTransfer:setStatus	(Lorg/jivesoftware/smackx/filetransfer/FileTransfer$Status;)V
        //   168: aload_2
        //   169: astore_1
        //   170: aload_0
        //   171: getfield 21	org/jivesoftware/smackx/filetransfer/OutgoingFileTransfer$2:this$0	Lorg/jivesoftware/smackx/filetransfer/OutgoingFileTransfer;
        //   174: getstatic 111	org/jivesoftware/smackx/filetransfer/FileTransfer$Error:bad_file	Lorg/jivesoftware/smackx/filetransfer/FileTransfer$Error;
        //   177: invokevirtual 115	org/jivesoftware/smackx/filetransfer/OutgoingFileTransfer:setError	(Lorg/jivesoftware/smackx/filetransfer/FileTransfer$Error;)V
        //   180: aload_2
        //   181: astore_1
        //   182: aload_0
        //   183: getfield 21	org/jivesoftware/smackx/filetransfer/OutgoingFileTransfer$2:this$0	Lorg/jivesoftware/smackx/filetransfer/OutgoingFileTransfer;
        //   186: aload_3
        //   187: invokevirtual 119	org/jivesoftware/smackx/filetransfer/OutgoingFileTransfer:setException	(Ljava/lang/Exception;)V
        //   190: aload_2
        //   191: ifnull +7 -> 198
        //   194: aload_2
        //   195: invokevirtual 89	java/io/InputStream:close	()V
        //   198: aload_0
        //   199: getfield 21	org/jivesoftware/smackx/filetransfer/OutgoingFileTransfer$2:this$0	Lorg/jivesoftware/smackx/filetransfer/OutgoingFileTransfer;
        //   202: invokestatic 58	org/jivesoftware/smackx/filetransfer/OutgoingFileTransfer:access$000	(Lorg/jivesoftware/smackx/filetransfer/OutgoingFileTransfer;)Ljava/io/OutputStream;
        //   205: invokevirtual 94	java/io/OutputStream:flush	()V
        //   208: aload_0
        //   209: getfield 21	org/jivesoftware/smackx/filetransfer/OutgoingFileTransfer$2:this$0	Lorg/jivesoftware/smackx/filetransfer/OutgoingFileTransfer;
        //   212: invokestatic 58	org/jivesoftware/smackx/filetransfer/OutgoingFileTransfer:access$000	(Lorg/jivesoftware/smackx/filetransfer/OutgoingFileTransfer;)Ljava/io/OutputStream;
        //   215: invokevirtual 95	java/io/OutputStream:close	()V
        //   218: goto -85 -> 133
        //   221: astore_1
        //   222: goto -89 -> 133
        //   225: astore_3
        //   226: aload 5
        //   228: astore_2
        //   229: aload_2
        //   230: astore_1
        //   231: aload_0
        //   232: getfield 21	org/jivesoftware/smackx/filetransfer/OutgoingFileTransfer$2:this$0	Lorg/jivesoftware/smackx/filetransfer/OutgoingFileTransfer;
        //   235: getstatic 101	org/jivesoftware/smackx/filetransfer/FileTransfer$Status:error	Lorg/jivesoftware/smackx/filetransfer/FileTransfer$Status;
        //   238: invokevirtual 105	org/jivesoftware/smackx/filetransfer/OutgoingFileTransfer:setStatus	(Lorg/jivesoftware/smackx/filetransfer/FileTransfer$Status;)V
        //   241: aload_2
        //   242: astore_1
        //   243: aload_0
        //   244: getfield 21	org/jivesoftware/smackx/filetransfer/OutgoingFileTransfer$2:this$0	Lorg/jivesoftware/smackx/filetransfer/OutgoingFileTransfer;
        //   247: aload_3
        //   248: invokevirtual 119	org/jivesoftware/smackx/filetransfer/OutgoingFileTransfer:setException	(Ljava/lang/Exception;)V
        //   251: aload_2
        //   252: ifnull +7 -> 259
        //   255: aload_2
        //   256: invokevirtual 89	java/io/InputStream:close	()V
        //   259: aload_0
        //   260: getfield 21	org/jivesoftware/smackx/filetransfer/OutgoingFileTransfer$2:this$0	Lorg/jivesoftware/smackx/filetransfer/OutgoingFileTransfer;
        //   263: invokestatic 58	org/jivesoftware/smackx/filetransfer/OutgoingFileTransfer:access$000	(Lorg/jivesoftware/smackx/filetransfer/OutgoingFileTransfer;)Ljava/io/OutputStream;
        //   266: invokevirtual 94	java/io/OutputStream:flush	()V
        //   269: aload_0
        //   270: getfield 21	org/jivesoftware/smackx/filetransfer/OutgoingFileTransfer$2:this$0	Lorg/jivesoftware/smackx/filetransfer/OutgoingFileTransfer;
        //   273: invokestatic 58	org/jivesoftware/smackx/filetransfer/OutgoingFileTransfer:access$000	(Lorg/jivesoftware/smackx/filetransfer/OutgoingFileTransfer;)Ljava/io/OutputStream;
        //   276: invokevirtual 95	java/io/OutputStream:close	()V
        //   279: goto -146 -> 133
        //   282: astore_1
        //   283: goto -150 -> 133
        //   286: astore_2
        //   287: aload_1
        //   288: ifnull +7 -> 295
        //   291: aload_1
        //   292: invokevirtual 89	java/io/InputStream:close	()V
        //   295: aload_0
        //   296: getfield 21	org/jivesoftware/smackx/filetransfer/OutgoingFileTransfer$2:this$0	Lorg/jivesoftware/smackx/filetransfer/OutgoingFileTransfer;
        //   299: invokestatic 58	org/jivesoftware/smackx/filetransfer/OutgoingFileTransfer:access$000	(Lorg/jivesoftware/smackx/filetransfer/OutgoingFileTransfer;)Ljava/io/OutputStream;
        //   302: invokevirtual 94	java/io/OutputStream:flush	()V
        //   305: aload_0
        //   306: getfield 21	org/jivesoftware/smackx/filetransfer/OutgoingFileTransfer$2:this$0	Lorg/jivesoftware/smackx/filetransfer/OutgoingFileTransfer;
        //   309: invokestatic 58	org/jivesoftware/smackx/filetransfer/OutgoingFileTransfer:access$000	(Lorg/jivesoftware/smackx/filetransfer/OutgoingFileTransfer;)Ljava/io/OutputStream;
        //   312: invokevirtual 95	java/io/OutputStream:close	()V
        //   315: aload_2
        //   316: athrow
        //   317: astore_1
        //   318: goto -3 -> 315
        //   321: astore_3
        //   322: aload_2
        //   323: astore_1
        //   324: aload_3
        //   325: astore_2
        //   326: goto -39 -> 287
        //   329: astore_3
        //   330: goto -101 -> 229
        //   333: astore_3
        //   334: goto -178 -> 156
        // Local variable table:
        //   start	length	slot	name	signature
        //   0	337	0	this	2
        //   44	6	1	localXMPPException1	XMPPException
        //   74	1	1	localObject1	Object
        //   148	1	1	localIOException1	IOException
        //   157	25	1	localObject2	Object
        //   221	1	1	localIOException2	IOException
        //   230	13	1	localObject3	Object
        //   282	10	1	localIOException3	IOException
        //   317	1	1	localIOException4	IOException
        //   323	1	1	localObject4	Object
        //   89	167	2	localObject5	Object
        //   286	37	2	localObject6	Object
        //   325	1	2	localObject7	Object
        //   152	35	3	localFileNotFoundException1	java.io.FileNotFoundException
        //   225	23	3	localXMPPException2	XMPPException
        //   321	4	3	localObject8	Object
        //   329	1	3	localXMPPException3	XMPPException
        //   333	1	3	localFileNotFoundException2	java.io.FileNotFoundException
        //   76	78	4	localObject9	Object
        //   71	156	5	localObject10	Object
        // Exception table:
        //   from	to	target	type
        //   0	33	44	org/jivesoftware/smack/XMPPException
        //   109	113	148	java/io/IOException
        //   113	133	148	java/io/IOException
        //   78	90	152	java/io/FileNotFoundException
        //   194	198	221	java/io/IOException
        //   198	218	221	java/io/IOException
        //   78	90	225	org/jivesoftware/smack/XMPPException
        //   255	259	282	java/io/IOException
        //   259	279	282	java/io/IOException
        //   78	90	286	finally
        //   158	168	286	finally
        //   170	180	286	finally
        //   182	190	286	finally
        //   231	241	286	finally
        //   243	251	286	finally
        //   291	295	317	java/io/IOException
        //   295	315	317	java/io/IOException
        //   90	105	321	finally
        //   90	105	329	org/jivesoftware/smack/XMPPException
        //   90	105	333	java/io/FileNotFoundException
      }
    }, "File Transfer " + this.streamID);
    this.transferThread.start();
  }
  
  public void sendFile(final String paramString1, final long paramLong, String paramString2, final NegotiationProgress paramNegotiationProgress)
  {
    if (paramNegotiationProgress == null) {
      try
      {
        throw new IllegalArgumentException("Callback progress cannot be null.");
      }
      finally {}
    }
    checkTransferThread();
    if ((isDone()) || (this.outputStream != null)) {
      throw new IllegalStateException("The negotation process has already been attempted for this file transfer");
    }
    this.callback = paramNegotiationProgress;
    this.transferThread = new Thread(new Runnable()
    {
      public void run()
      {
        try
        {
          OutgoingFileTransfer.access$002(OutgoingFileTransfer.this, OutgoingFileTransfer.this.negotiateStream(paramString1, paramLong, paramNegotiationProgress));
          this.val$progress.outputStreamEstablished(OutgoingFileTransfer.this.outputStream);
          return;
        }
        catch (XMPPException localXMPPException)
        {
          OutgoingFileTransfer.this.handleXMPPException(localXMPPException);
        }
      }
    }, "File Transfer Negotiation " + this.streamID);
    this.transferThread.start();
  }
  
  public void sendStream(final InputStream paramInputStream, final String paramString1, final long paramLong, String paramString2)
  {
    try
    {
      checkTransferThread();
      this.transferThread = new Thread(new Runnable()
      {
        public void run()
        {
          do
          {
            try
            {
              OutgoingFileTransfer.access$002(OutgoingFileTransfer.this, OutgoingFileTransfer.this.negotiateStream(paramString1, paramLong, paramInputStream));
              if (OutgoingFileTransfer.this.outputStream == null) {
                return;
              }
            }
            catch (XMPPException localXMPPException1)
            {
              OutgoingFileTransfer.this.handleXMPPException(localXMPPException1);
              return;
            }
          } while (!OutgoingFileTransfer.this.updateStatus(FileTransfer.Status.negotiated, FileTransfer.Status.in_progress));
          try
          {
            OutgoingFileTransfer.this.writeToStream(this.val$in, OutgoingFileTransfer.this.outputStream);
          }
          catch (XMPPException localXMPPException2)
          {
            for (;;)
            {
              label116:
              OutgoingFileTransfer.this.setStatus(FileTransfer.Status.error);
              OutgoingFileTransfer.this.setException(localXMPPException2);
              try
              {
                if (this.val$in != null) {
                  this.val$in.close();
                }
                OutgoingFileTransfer.this.outputStream.flush();
                OutgoingFileTransfer.this.outputStream.close();
              }
              catch (IOException localIOException1) {}
            }
          }
          finally
          {
            try
            {
              if (this.val$in != null) {
                this.val$in.close();
              }
              OutgoingFileTransfer.this.outputStream.flush();
              OutgoingFileTransfer.this.outputStream.close();
            }
            catch (IOException localIOException3)
            {
              break label226;
            }
          }
          try
          {
            if (this.val$in != null) {
              this.val$in.close();
            }
            OutgoingFileTransfer.this.outputStream.flush();
            OutgoingFileTransfer.this.outputStream.close();
          }
          catch (IOException localIOException2)
          {
            break label116;
          }
          OutgoingFileTransfer.this.updateStatus(FileTransfer.Status.in_progress, FileTransfer.Status.complete);
        }
      }, "File Transfer " + this.streamID);
      this.transferThread.start();
      return;
    }
    finally
    {
      paramInputStream = finally;
      throw paramInputStream;
    }
  }
  
  protected void setException(Exception paramException)
  {
    super.setException(paramException);
    if (this.callback != null) {
      this.callback.errorEstablishingStream(paramException);
    }
  }
  
  protected void setOutputStream(OutputStream paramOutputStream)
  {
    if (this.outputStream == null) {
      this.outputStream = paramOutputStream;
    }
  }
  
  protected void setStatus(FileTransfer.Status paramStatus)
  {
    FileTransfer.Status localStatus = getStatus();
    super.setStatus(paramStatus);
    if (this.callback != null) {
      this.callback.statusUpdated(localStatus, paramStatus);
    }
  }
  
  protected boolean updateStatus(FileTransfer.Status paramStatus1, FileTransfer.Status paramStatus2)
  {
    boolean bool = super.updateStatus(paramStatus1, paramStatus2);
    if ((this.callback != null) && (bool)) {
      this.callback.statusUpdated(paramStatus1, paramStatus2);
    }
    return bool;
  }
  
  public static abstract interface NegotiationProgress
  {
    public abstract void errorEstablishingStream(Exception paramException);
    
    public abstract void outputStreamEstablished(OutputStream paramOutputStream);
    
    public abstract void statusUpdated(FileTransfer.Status paramStatus1, FileTransfer.Status paramStatus2);
  }
}


/* Location:           C:\Users\Administrator\Documents\ds\反编译\AndroidKiller_v1.3.1\projects\Jungledsq_ppc\ProjectSrc\smali\
 * Qualified Name:     org.jivesoftware.smackx.filetransfer.OutgoingFileTransfer
 * JD-Core Version:    0.7.0.1
 */