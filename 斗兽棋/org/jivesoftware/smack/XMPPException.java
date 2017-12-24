package org.jivesoftware.smack;

import java.io.PrintStream;
import java.io.PrintWriter;
import org.jivesoftware.smack.packet.StreamError;
import org.jivesoftware.smack.packet.XMPPError;

public class XMPPException
  extends Exception
{
  private XMPPError error = null;
  private StreamError streamError = null;
  private Throwable wrappedThrowable = null;
  
  public XMPPException() {}
  
  public XMPPException(String paramString)
  {
    super(paramString);
  }
  
  public XMPPException(String paramString, Throwable paramThrowable)
  {
    super(paramString);
    this.wrappedThrowable = paramThrowable;
  }
  
  public XMPPException(String paramString, XMPPError paramXMPPError)
  {
    super(paramString);
    this.error = paramXMPPError;
  }
  
  public XMPPException(String paramString, XMPPError paramXMPPError, Throwable paramThrowable)
  {
    super(paramString);
    this.error = paramXMPPError;
    this.wrappedThrowable = paramThrowable;
  }
  
  public XMPPException(Throwable paramThrowable)
  {
    this.wrappedThrowable = paramThrowable;
  }
  
  public XMPPException(StreamError paramStreamError)
  {
    this.streamError = paramStreamError;
  }
  
  public XMPPException(XMPPError paramXMPPError)
  {
    this.error = paramXMPPError;
  }
  
  public String getMessage()
  {
    String str = super.getMessage();
    if ((str == null) && (this.error != null)) {
      return this.error.toString();
    }
    if ((str == null) && (this.streamError != null)) {
      return this.streamError.toString();
    }
    return str;
  }
  
  public StreamError getStreamError()
  {
    return this.streamError;
  }
  
  public Throwable getWrappedThrowable()
  {
    return this.wrappedThrowable;
  }
  
  public XMPPError getXMPPError()
  {
    return this.error;
  }
  
  public void printStackTrace()
  {
    printStackTrace(System.err);
  }
  
  public void printStackTrace(PrintStream paramPrintStream)
  {
    super.printStackTrace(paramPrintStream);
    if (this.wrappedThrowable != null)
    {
      paramPrintStream.println("Nested Exception: ");
      this.wrappedThrowable.printStackTrace(paramPrintStream);
    }
  }
  
  public void printStackTrace(PrintWriter paramPrintWriter)
  {
    super.printStackTrace(paramPrintWriter);
    if (this.wrappedThrowable != null)
    {
      paramPrintWriter.println("Nested Exception: ");
      this.wrappedThrowable.printStackTrace(paramPrintWriter);
    }
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    String str = super.getMessage();
    if (str != null) {
      localStringBuilder.append(str).append(": ");
    }
    if (this.error != null) {
      localStringBuilder.append(this.error);
    }
    if (this.streamError != null) {
      localStringBuilder.append(this.streamError);
    }
    if (this.wrappedThrowable != null) {
      localStringBuilder.append("\n  -- caused by: ").append(this.wrappedThrowable);
    }
    return localStringBuilder.toString();
  }
}


/* Location:           C:\Users\Administrator\Documents\ds\反编译\AndroidKiller_v1.3.1\projects\Jungledsq_ppc\ProjectSrc\smali\
 * Qualified Name:     org.jivesoftware.smack.XMPPException
 * JD-Core Version:    0.7.0.1
 */