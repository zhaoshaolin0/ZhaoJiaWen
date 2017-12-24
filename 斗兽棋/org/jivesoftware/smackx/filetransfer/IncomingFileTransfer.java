package org.jivesoftware.smackx.filetransfer;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.Callable;
import org.jivesoftware.smack.XMPPException;

public class IncomingFileTransfer
  extends FileTransfer
{
  private InputStream inputStream;
  private FileTransferRequest recieveRequest;
  
  protected IncomingFileTransfer(FileTransferRequest paramFileTransferRequest, FileTransferNegotiator paramFileTransferNegotiator)
  {
    super(paramFileTransferRequest.getRequestor(), paramFileTransferRequest.getStreamID(), paramFileTransferNegotiator);
    this.recieveRequest = paramFileTransferRequest;
  }
  
  private void handleXMPPException(XMPPException paramXMPPException)
  {
    setStatus(FileTransfer.Status.error);
    setException(paramXMPPException);
  }
  
  /* Error */
  private InputStream negotiateStream()
    throws XMPPException
  {
    // Byte code:
    //   0: aload_0
    //   1: getstatic 74	org/jivesoftware/smackx/filetransfer/FileTransfer$Status:negotiating_transfer	Lorg/jivesoftware/smackx/filetransfer/FileTransfer$Status;
    //   4: invokevirtual 61	org/jivesoftware/smackx/filetransfer/IncomingFileTransfer:setStatus	(Lorg/jivesoftware/smackx/filetransfer/FileTransfer$Status;)V
    //   7: aload_0
    //   8: getfield 78	org/jivesoftware/smackx/filetransfer/IncomingFileTransfer:negotiator	Lorg/jivesoftware/smackx/filetransfer/FileTransferNegotiator;
    //   11: aload_0
    //   12: getfield 28	org/jivesoftware/smackx/filetransfer/IncomingFileTransfer:recieveRequest	Lorg/jivesoftware/smackx/filetransfer/FileTransferRequest;
    //   15: invokevirtual 84	org/jivesoftware/smackx/filetransfer/FileTransferNegotiator:selectStreamNegotiator	(Lorg/jivesoftware/smackx/filetransfer/FileTransferRequest;)Lorg/jivesoftware/smackx/filetransfer/StreamNegotiator;
    //   18: astore_1
    //   19: aload_0
    //   20: getstatic 87	org/jivesoftware/smackx/filetransfer/FileTransfer$Status:negotiating_stream	Lorg/jivesoftware/smackx/filetransfer/FileTransfer$Status;
    //   23: invokevirtual 61	org/jivesoftware/smackx/filetransfer/IncomingFileTransfer:setStatus	(Lorg/jivesoftware/smackx/filetransfer/FileTransfer$Status;)V
    //   26: new 89	java/util/concurrent/FutureTask
    //   29: dup
    //   30: new 8	org/jivesoftware/smackx/filetransfer/IncomingFileTransfer$2
    //   33: dup
    //   34: aload_0
    //   35: aload_1
    //   36: invokespecial 92	org/jivesoftware/smackx/filetransfer/IncomingFileTransfer$2:<init>	(Lorg/jivesoftware/smackx/filetransfer/IncomingFileTransfer;Lorg/jivesoftware/smackx/filetransfer/StreamNegotiator;)V
    //   39: invokespecial 95	java/util/concurrent/FutureTask:<init>	(Ljava/util/concurrent/Callable;)V
    //   42: astore_1
    //   43: aload_1
    //   44: invokevirtual 99	java/util/concurrent/FutureTask:run	()V
    //   47: aload_1
    //   48: ldc2_w 100
    //   51: getstatic 107	java/util/concurrent/TimeUnit:SECONDS	Ljava/util/concurrent/TimeUnit;
    //   54: invokevirtual 111	java/util/concurrent/FutureTask:get	(JLjava/util/concurrent/TimeUnit;)Ljava/lang/Object;
    //   57: checkcast 113	java/io/InputStream
    //   60: astore_2
    //   61: aload_1
    //   62: iconst_1
    //   63: invokevirtual 117	java/util/concurrent/FutureTask:cancel	(Z)Z
    //   66: pop
    //   67: aload_0
    //   68: getstatic 120	org/jivesoftware/smackx/filetransfer/FileTransfer$Status:negotiated	Lorg/jivesoftware/smackx/filetransfer/FileTransfer$Status;
    //   71: invokevirtual 61	org/jivesoftware/smackx/filetransfer/IncomingFileTransfer:setStatus	(Lorg/jivesoftware/smackx/filetransfer/FileTransfer$Status;)V
    //   74: aload_2
    //   75: areturn
    //   76: astore_2
    //   77: new 38	org/jivesoftware/smack/XMPPException
    //   80: dup
    //   81: ldc 122
    //   83: aload_2
    //   84: invokespecial 125	org/jivesoftware/smack/XMPPException:<init>	(Ljava/lang/String;Ljava/lang/Throwable;)V
    //   87: athrow
    //   88: astore_2
    //   89: aload_1
    //   90: iconst_1
    //   91: invokevirtual 117	java/util/concurrent/FutureTask:cancel	(Z)Z
    //   94: pop
    //   95: aload_2
    //   96: athrow
    //   97: astore_2
    //   98: new 38	org/jivesoftware/smack/XMPPException
    //   101: dup
    //   102: ldc 127
    //   104: aload_2
    //   105: invokespecial 125	org/jivesoftware/smack/XMPPException:<init>	(Ljava/lang/String;Ljava/lang/Throwable;)V
    //   108: athrow
    //   109: astore_2
    //   110: new 38	org/jivesoftware/smack/XMPPException
    //   113: dup
    //   114: ldc 129
    //   116: aload_2
    //   117: invokespecial 125	org/jivesoftware/smack/XMPPException:<init>	(Ljava/lang/String;Ljava/lang/Throwable;)V
    //   120: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	121	0	this	IncomingFileTransfer
    //   18	72	1	localObject1	Object
    //   60	15	2	localInputStream	InputStream
    //   76	8	2	localInterruptedException	java.lang.InterruptedException
    //   88	8	2	localObject2	Object
    //   97	8	2	localExecutionException	java.util.concurrent.ExecutionException
    //   109	8	2	localTimeoutException	java.util.concurrent.TimeoutException
    // Exception table:
    //   from	to	target	type
    //   47	61	76	java/lang/InterruptedException
    //   47	61	88	finally
    //   77	88	88	finally
    //   98	109	88	finally
    //   110	121	88	finally
    //   47	61	97	java/util/concurrent/ExecutionException
    //   47	61	109	java/util/concurrent/TimeoutException
  }
  
  public void cancel()
  {
    setStatus(FileTransfer.Status.cancelled);
  }
  
  public InputStream recieveFile()
    throws XMPPException
  {
    if (this.inputStream != null) {
      throw new IllegalStateException("Transfer already negotiated!");
    }
    try
    {
      this.inputStream = negotiateStream();
      return this.inputStream;
    }
    catch (XMPPException localXMPPException)
    {
      setException(localXMPPException);
      throw localXMPPException;
    }
  }
  
  public void recieveFile(final File paramFile)
    throws XMPPException
  {
    if (paramFile != null)
    {
      if (!paramFile.exists()) {}
      try
      {
        paramFile.createNewFile();
        if (paramFile.canWrite()) {
          break label55;
        }
        throw new IllegalArgumentException("Cannot write to provided file");
      }
      catch (IOException paramFile)
      {
        throw new XMPPException("Could not create file to write too", paramFile);
      }
    }
    else
    {
      throw new IllegalArgumentException("File cannot be null");
    }
    label55:
    new Thread(new Runnable()
    {
      /* Error */
      public void run()
      {
        // Byte code:
        //   0: aload_0
        //   1: getfield 19	org/jivesoftware/smackx/filetransfer/IncomingFileTransfer$1:this$0	Lorg/jivesoftware/smackx/filetransfer/IncomingFileTransfer;
        //   4: aload_0
        //   5: getfield 19	org/jivesoftware/smackx/filetransfer/IncomingFileTransfer$1:this$0	Lorg/jivesoftware/smackx/filetransfer/IncomingFileTransfer;
        //   8: invokestatic 36	org/jivesoftware/smackx/filetransfer/IncomingFileTransfer:access$100	(Lorg/jivesoftware/smackx/filetransfer/IncomingFileTransfer;)Ljava/io/InputStream;
        //   11: invokestatic 40	org/jivesoftware/smackx/filetransfer/IncomingFileTransfer:access$002	(Lorg/jivesoftware/smackx/filetransfer/IncomingFileTransfer;Ljava/io/InputStream;)Ljava/io/InputStream;
        //   14: pop
        //   15: aconst_null
        //   16: astore 4
        //   18: aconst_null
        //   19: astore_3
        //   20: new 42	java/io/FileOutputStream
        //   23: dup
        //   24: aload_0
        //   25: getfield 21	org/jivesoftware/smackx/filetransfer/IncomingFileTransfer$1:val$file	Ljava/io/File;
        //   28: invokespecial 44	java/io/FileOutputStream:<init>	(Ljava/io/File;)V
        //   31: astore_1
        //   32: aload_0
        //   33: getfield 19	org/jivesoftware/smackx/filetransfer/IncomingFileTransfer$1:this$0	Lorg/jivesoftware/smackx/filetransfer/IncomingFileTransfer;
        //   36: getstatic 50	org/jivesoftware/smackx/filetransfer/FileTransfer$Status:in_progress	Lorg/jivesoftware/smackx/filetransfer/FileTransfer$Status;
        //   39: invokevirtual 54	org/jivesoftware/smackx/filetransfer/IncomingFileTransfer:setStatus	(Lorg/jivesoftware/smackx/filetransfer/FileTransfer$Status;)V
        //   42: aload_0
        //   43: getfield 19	org/jivesoftware/smackx/filetransfer/IncomingFileTransfer$1:this$0	Lorg/jivesoftware/smackx/filetransfer/IncomingFileTransfer;
        //   46: aload_0
        //   47: getfield 19	org/jivesoftware/smackx/filetransfer/IncomingFileTransfer$1:this$0	Lorg/jivesoftware/smackx/filetransfer/IncomingFileTransfer;
        //   50: invokestatic 57	org/jivesoftware/smackx/filetransfer/IncomingFileTransfer:access$000	(Lorg/jivesoftware/smackx/filetransfer/IncomingFileTransfer;)Ljava/io/InputStream;
        //   53: aload_1
        //   54: invokevirtual 61	org/jivesoftware/smackx/filetransfer/IncomingFileTransfer:writeToStream	(Ljava/io/InputStream;Ljava/io/OutputStream;)V
        //   57: aload_0
        //   58: getfield 19	org/jivesoftware/smackx/filetransfer/IncomingFileTransfer$1:this$0	Lorg/jivesoftware/smackx/filetransfer/IncomingFileTransfer;
        //   61: invokevirtual 65	org/jivesoftware/smackx/filetransfer/IncomingFileTransfer:getStatus	()Lorg/jivesoftware/smackx/filetransfer/FileTransfer$Status;
        //   64: getstatic 50	org/jivesoftware/smackx/filetransfer/FileTransfer$Status:in_progress	Lorg/jivesoftware/smackx/filetransfer/FileTransfer$Status;
        //   67: invokevirtual 69	org/jivesoftware/smackx/filetransfer/FileTransfer$Status:equals	(Ljava/lang/Object;)Z
        //   70: ifeq +13 -> 83
        //   73: aload_0
        //   74: getfield 19	org/jivesoftware/smackx/filetransfer/IncomingFileTransfer$1:this$0	Lorg/jivesoftware/smackx/filetransfer/IncomingFileTransfer;
        //   77: getstatic 72	org/jivesoftware/smackx/filetransfer/FileTransfer$Status:complete	Lorg/jivesoftware/smackx/filetransfer/FileTransfer$Status;
        //   80: invokevirtual 54	org/jivesoftware/smackx/filetransfer/IncomingFileTransfer:setStatus	(Lorg/jivesoftware/smackx/filetransfer/FileTransfer$Status;)V
        //   83: aload_0
        //   84: getfield 19	org/jivesoftware/smackx/filetransfer/IncomingFileTransfer$1:this$0	Lorg/jivesoftware/smackx/filetransfer/IncomingFileTransfer;
        //   87: invokestatic 57	org/jivesoftware/smackx/filetransfer/IncomingFileTransfer:access$000	(Lorg/jivesoftware/smackx/filetransfer/IncomingFileTransfer;)Ljava/io/InputStream;
        //   90: ifnull +13 -> 103
        //   93: aload_0
        //   94: getfield 19	org/jivesoftware/smackx/filetransfer/IncomingFileTransfer$1:this$0	Lorg/jivesoftware/smackx/filetransfer/IncomingFileTransfer;
        //   97: invokestatic 57	org/jivesoftware/smackx/filetransfer/IncomingFileTransfer:access$000	(Lorg/jivesoftware/smackx/filetransfer/IncomingFileTransfer;)Ljava/io/InputStream;
        //   100: invokevirtual 77	java/io/InputStream:close	()V
        //   103: aload_1
        //   104: ifnull +7 -> 111
        //   107: aload_1
        //   108: invokevirtual 80	java/io/OutputStream:close	()V
        //   111: return
        //   112: astore_1
        //   113: aload_0
        //   114: getfield 19	org/jivesoftware/smackx/filetransfer/IncomingFileTransfer$1:this$0	Lorg/jivesoftware/smackx/filetransfer/IncomingFileTransfer;
        //   117: aload_1
        //   118: invokestatic 84	org/jivesoftware/smackx/filetransfer/IncomingFileTransfer:access$200	(Lorg/jivesoftware/smackx/filetransfer/IncomingFileTransfer;Lorg/jivesoftware/smack/XMPPException;)V
        //   121: return
        //   122: astore_2
        //   123: aload_3
        //   124: astore_1
        //   125: aload_0
        //   126: getfield 19	org/jivesoftware/smackx/filetransfer/IncomingFileTransfer$1:this$0	Lorg/jivesoftware/smackx/filetransfer/IncomingFileTransfer;
        //   129: getstatic 87	org/jivesoftware/smackx/filetransfer/FileTransfer$Status:error	Lorg/jivesoftware/smackx/filetransfer/FileTransfer$Status;
        //   132: invokevirtual 54	org/jivesoftware/smackx/filetransfer/IncomingFileTransfer:setStatus	(Lorg/jivesoftware/smackx/filetransfer/FileTransfer$Status;)V
        //   135: aload_0
        //   136: getfield 19	org/jivesoftware/smackx/filetransfer/IncomingFileTransfer$1:this$0	Lorg/jivesoftware/smackx/filetransfer/IncomingFileTransfer;
        //   139: getstatic 93	org/jivesoftware/smackx/filetransfer/FileTransfer$Error:stream	Lorg/jivesoftware/smackx/filetransfer/FileTransfer$Error;
        //   142: invokevirtual 97	org/jivesoftware/smackx/filetransfer/IncomingFileTransfer:setError	(Lorg/jivesoftware/smackx/filetransfer/FileTransfer$Error;)V
        //   145: aload_0
        //   146: getfield 19	org/jivesoftware/smackx/filetransfer/IncomingFileTransfer$1:this$0	Lorg/jivesoftware/smackx/filetransfer/IncomingFileTransfer;
        //   149: aload_2
        //   150: invokevirtual 101	org/jivesoftware/smackx/filetransfer/IncomingFileTransfer:setException	(Ljava/lang/Exception;)V
        //   153: goto -96 -> 57
        //   156: astore_2
        //   157: aload 4
        //   159: astore_1
        //   160: aload_0
        //   161: getfield 19	org/jivesoftware/smackx/filetransfer/IncomingFileTransfer$1:this$0	Lorg/jivesoftware/smackx/filetransfer/IncomingFileTransfer;
        //   164: getstatic 87	org/jivesoftware/smackx/filetransfer/FileTransfer$Status:error	Lorg/jivesoftware/smackx/filetransfer/FileTransfer$Status;
        //   167: invokevirtual 54	org/jivesoftware/smackx/filetransfer/IncomingFileTransfer:setStatus	(Lorg/jivesoftware/smackx/filetransfer/FileTransfer$Status;)V
        //   170: aload_0
        //   171: getfield 19	org/jivesoftware/smackx/filetransfer/IncomingFileTransfer$1:this$0	Lorg/jivesoftware/smackx/filetransfer/IncomingFileTransfer;
        //   174: getstatic 104	org/jivesoftware/smackx/filetransfer/FileTransfer$Error:bad_file	Lorg/jivesoftware/smackx/filetransfer/FileTransfer$Error;
        //   177: invokevirtual 97	org/jivesoftware/smackx/filetransfer/IncomingFileTransfer:setError	(Lorg/jivesoftware/smackx/filetransfer/FileTransfer$Error;)V
        //   180: aload_0
        //   181: getfield 19	org/jivesoftware/smackx/filetransfer/IncomingFileTransfer$1:this$0	Lorg/jivesoftware/smackx/filetransfer/IncomingFileTransfer;
        //   184: aload_2
        //   185: invokevirtual 101	org/jivesoftware/smackx/filetransfer/IncomingFileTransfer:setException	(Ljava/lang/Exception;)V
        //   188: goto -131 -> 57
        //   191: astore_1
        //   192: return
        //   193: astore_2
        //   194: goto -91 -> 103
        //   197: astore_2
        //   198: goto -38 -> 160
        //   201: astore_2
        //   202: goto -77 -> 125
        // Local variable table:
        //   start	length	slot	name	signature
        //   0	205	0	this	1
        //   31	77	1	localFileOutputStream	java.io.FileOutputStream
        //   112	6	1	localXMPPException1	XMPPException
        //   124	36	1	localObject1	Object
        //   191	1	1	localThrowable1	java.lang.Throwable
        //   122	28	2	localXMPPException2	XMPPException
        //   156	29	2	localFileNotFoundException1	java.io.FileNotFoundException
        //   193	1	2	localThrowable2	java.lang.Throwable
        //   197	1	2	localFileNotFoundException2	java.io.FileNotFoundException
        //   201	1	2	localXMPPException3	XMPPException
        //   19	105	3	localObject2	Object
        //   16	142	4	localObject3	Object
        // Exception table:
        //   from	to	target	type
        //   0	15	112	org/jivesoftware/smack/XMPPException
        //   20	32	122	org/jivesoftware/smack/XMPPException
        //   20	32	156	java/io/FileNotFoundException
        //   107	111	191	java/lang/Throwable
        //   93	103	193	java/lang/Throwable
        //   32	57	197	java/io/FileNotFoundException
        //   32	57	201	org/jivesoftware/smack/XMPPException
      }
    }, "File Transfer " + this.streamID).start();
  }
}


/* Location:           C:\Users\Administrator\Documents\ds\反编译\AndroidKiller_v1.3.1\projects\Jungledsq_ppc\ProjectSrc\smali\
 * Qualified Name:     org.jivesoftware.smackx.filetransfer.IncomingFileTransfer
 * JD-Core Version:    0.7.0.1
 */