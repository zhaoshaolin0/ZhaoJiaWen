package org.jivesoftware.smack.util;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

public class ObservableReader
  extends Reader
{
  List listeners = new ArrayList();
  Reader wrappedReader = null;
  
  public ObservableReader(Reader paramReader)
  {
    this.wrappedReader = paramReader;
  }
  
  public void addReaderListener(ReaderListener paramReaderListener)
  {
    if (paramReaderListener == null) {
      return;
    }
    synchronized (this.listeners)
    {
      if (!this.listeners.contains(paramReaderListener)) {
        this.listeners.add(paramReaderListener);
      }
      return;
    }
  }
  
  public void close()
    throws IOException
  {
    this.wrappedReader.close();
  }
  
  public void mark(int paramInt)
    throws IOException
  {
    this.wrappedReader.mark(paramInt);
  }
  
  public boolean markSupported()
  {
    return this.wrappedReader.markSupported();
  }
  
  public int read()
    throws IOException
  {
    return this.wrappedReader.read();
  }
  
  public int read(char[] paramArrayOfChar)
    throws IOException
  {
    return this.wrappedReader.read(paramArrayOfChar);
  }
  
  public int read(char[] arg1, int paramInt1, int paramInt2)
    throws IOException
  {
    paramInt2 = this.wrappedReader.read(???, paramInt1, paramInt2);
    if (paramInt2 > 0)
    {
      String str = new String(???, paramInt1, paramInt2);
      synchronized (this.listeners)
      {
        ReaderListener[] arrayOfReaderListener = new ReaderListener[this.listeners.size()];
        this.listeners.toArray(arrayOfReaderListener);
        paramInt1 = 0;
        if (paramInt1 < arrayOfReaderListener.length)
        {
          arrayOfReaderListener[paramInt1].read(str);
          paramInt1 += 1;
        }
      }
    }
    return paramInt2;
  }
  
  public boolean ready()
    throws IOException
  {
    return this.wrappedReader.ready();
  }
  
  public void removeReaderListener(ReaderListener paramReaderListener)
  {
    synchronized (this.listeners)
    {
      this.listeners.remove(paramReaderListener);
      return;
    }
  }
  
  public void reset()
    throws IOException
  {
    this.wrappedReader.reset();
  }
  
  public long skip(long paramLong)
    throws IOException
  {
    return this.wrappedReader.skip(paramLong);
  }
}


/* Location:           C:\Users\Administrator\Documents\ds\反编译\AndroidKiller_v1.3.1\projects\Jungledsq_ppc\ProjectSrc\smali\
 * Qualified Name:     org.jivesoftware.smack.util.ObservableReader
 * JD-Core Version:    0.7.0.1
 */