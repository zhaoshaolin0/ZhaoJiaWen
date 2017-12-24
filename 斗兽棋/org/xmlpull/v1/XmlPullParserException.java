package org.xmlpull.v1;

import java.io.PrintStream;

public class XmlPullParserException
  extends Exception
{
  protected int column = -1;
  protected Throwable detail;
  protected int row = -1;
  
  public XmlPullParserException(String paramString)
  {
    super(paramString);
  }
  
  public XmlPullParserException(String paramString, XmlPullParser paramXmlPullParser, Throwable paramThrowable) {}
  
  public int getColumnNumber()
  {
    return this.column;
  }
  
  public Throwable getDetail()
  {
    return this.detail;
  }
  
  public int getLineNumber()
  {
    return this.row;
  }
  
  public void printStackTrace()
  {
    if (this.detail == null)
    {
      super.printStackTrace();
      return;
    }
    synchronized (System.err)
    {
      System.err.println(super.getMessage() + "; nested exception is:");
      this.detail.printStackTrace();
      return;
    }
  }
}


/* Location:           C:\Users\Administrator\Documents\ds\反编译\AndroidKiller_v1.3.1\projects\Jungledsq_ppc\ProjectSrc\smali\
 * Qualified Name:     org.xmlpull.v1.XmlPullParserException
 * JD-Core Version:    0.7.0.1
 */