package org.jivesoftware.smack.util;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

public class ObservableWriter
  extends Writer
{
  List listeners = new ArrayList();
  Writer wrappedWriter = null;
  
  public ObservableWriter(Writer paramWriter)
  {
    this.wrappedWriter = paramWriter;
  }
  
  private void notifyListeners(String paramString)
  {
    synchronized (this.listeners)
    {
      WriterListener[] arrayOfWriterListener = new WriterListener[this.listeners.size()];
      this.listeners.toArray(arrayOfWriterListener);
      int i = 0;
      if (i < arrayOfWriterListener.length)
      {
        arrayOfWriterListener[i].write(paramString);
        i += 1;
      }
    }
  }
  
  public void addWriterListener(WriterListener paramWriterListener)
  {
    if (paramWriterListener == null) {
      return;
    }
    synchronized (this.listeners)
    {
      if (!this.listeners.contains(paramWriterListener)) {
        this.listeners.add(paramWriterListener);
      }
      return;
    }
  }
  
  public void close()
    throws IOException
  {
    this.wrappedWriter.close();
  }
  
  public void flush()
    throws IOException
  {
    this.wrappedWriter.flush();
  }
  
  public void removeWriterListener(WriterListener paramWriterListener)
  {
    synchronized (this.listeners)
    {
      this.listeners.remove(paramWriterListener);
      return;
    }
  }
  
  public void write(int paramInt)
    throws IOException
  {
    this.wrappedWriter.write(paramInt);
  }
  
  public void write(String paramString)
    throws IOException
  {
    this.wrappedWriter.write(paramString);
    notifyListeners(paramString);
  }
  
  public void write(String paramString, int paramInt1, int paramInt2)
    throws IOException
  {
    this.wrappedWriter.write(paramString, paramInt1, paramInt2);
    notifyListeners(paramString.substring(paramInt1, paramInt1 + paramInt2));
  }
  
  public void write(char[] paramArrayOfChar)
    throws IOException
  {
    this.wrappedWriter.write(paramArrayOfChar);
    notifyListeners(new String(paramArrayOfChar));
  }
  
  public void write(char[] paramArrayOfChar, int paramInt1, int paramInt2)
    throws IOException
  {
    this.wrappedWriter.write(paramArrayOfChar, paramInt1, paramInt2);
    notifyListeners(new String(paramArrayOfChar, paramInt1, paramInt2));
  }
}


/* Location:           C:\Users\Administrator\Documents\ds\反编译\AndroidKiller_v1.3.1\projects\Jungledsq_ppc\ProjectSrc\smali\
 * Qualified Name:     org.jivesoftware.smack.util.ObservableWriter
 * JD-Core Version:    0.7.0.1
 */