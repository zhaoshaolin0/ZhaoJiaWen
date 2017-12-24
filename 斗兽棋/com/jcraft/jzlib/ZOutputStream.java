package com.jcraft.jzlib;

import java.io.IOException;
import java.io.OutputStream;

public class ZOutputStream
  extends OutputStream
{
  protected byte[] buf = new byte[this.bufsize];
  protected byte[] buf1 = new byte[1];
  protected int bufsize = 512;
  protected boolean compress;
  protected int flush = 0;
  protected OutputStream out;
  protected ZStream z = new ZStream();
  
  public ZOutputStream(OutputStream paramOutputStream)
  {
    this.out = paramOutputStream;
    this.z.inflateInit();
    this.compress = false;
  }
  
  public ZOutputStream(OutputStream paramOutputStream, int paramInt)
  {
    this(paramOutputStream, paramInt, false);
  }
  
  public ZOutputStream(OutputStream paramOutputStream, int paramInt, boolean paramBoolean)
  {
    this.out = paramOutputStream;
    this.z.deflateInit(paramInt, paramBoolean);
    this.compress = true;
  }
  
  /* Error */
  public void close()
    throws IOException
  {
    // Byte code:
    //   0: aload_0
    //   1: invokevirtual 57	com/jcraft/jzlib/ZOutputStream:finish	()V
    //   4: aload_0
    //   5: invokevirtual 60	com/jcraft/jzlib/ZOutputStream:end	()V
    //   8: aload_0
    //   9: getfield 36	com/jcraft/jzlib/ZOutputStream:out	Ljava/io/OutputStream;
    //   12: invokevirtual 62	java/io/OutputStream:close	()V
    //   15: aload_0
    //   16: aconst_null
    //   17: putfield 36	com/jcraft/jzlib/ZOutputStream:out	Ljava/io/OutputStream;
    //   20: return
    //   21: astore_1
    //   22: aload_0
    //   23: invokevirtual 60	com/jcraft/jzlib/ZOutputStream:end	()V
    //   26: aload_0
    //   27: getfield 36	com/jcraft/jzlib/ZOutputStream:out	Ljava/io/OutputStream;
    //   30: invokevirtual 62	java/io/OutputStream:close	()V
    //   33: aload_0
    //   34: aconst_null
    //   35: putfield 36	com/jcraft/jzlib/ZOutputStream:out	Ljava/io/OutputStream;
    //   38: aload_1
    //   39: athrow
    //   40: astore_1
    //   41: goto -37 -> 4
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	44	0	this	ZOutputStream
    //   21	18	1	localObject	java.lang.Object
    //   40	1	1	localIOException	IOException
    // Exception table:
    //   from	to	target	type
    //   0	4	21	finally
    //   0	4	40	java/io/IOException
  }
  
  public void end()
  {
    if (this.z == null) {
      return;
    }
    if (this.compress) {
      this.z.deflateEnd();
    }
    for (;;)
    {
      this.z.free();
      this.z = null;
      return;
      this.z.inflateEnd();
    }
  }
  
  public void finish()
    throws IOException
  {
    label116:
    label122:
    do
    {
      this.z.next_out = this.buf;
      this.z.next_out_index = 0;
      this.z.avail_out = this.bufsize;
      int i;
      StringBuffer localStringBuffer;
      if (this.compress)
      {
        i = this.z.deflate(4);
        if ((i == 1) || (i == 0)) {
          break label122;
        }
        localStringBuffer = new StringBuffer();
        if (!this.compress) {
          break label116;
        }
      }
      for (String str = "de";; str = "in")
      {
        throw new ZStreamException(str + "flating: " + this.z.msg);
        i = this.z.inflate(4);
        break;
      }
      if (this.bufsize - this.z.avail_out > 0) {
        this.out.write(this.buf, 0, this.bufsize - this.z.avail_out);
      }
    } while ((this.z.avail_in > 0) || (this.z.avail_out == 0));
    flush();
  }
  
  public void flush()
    throws IOException
  {
    this.out.flush();
  }
  
  public int getFlushMode()
  {
    return this.flush;
  }
  
  public long getTotalIn()
  {
    return this.z.total_in;
  }
  
  public long getTotalOut()
  {
    return this.z.total_out;
  }
  
  public void setFlushMode(int paramInt)
  {
    this.flush = paramInt;
  }
  
  public void write(int paramInt)
    throws IOException
  {
    this.buf1[0] = ((byte)paramInt);
    write(this.buf1, 0, 1);
  }
  
  public void write(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
    throws IOException
  {
    if (paramInt2 == 0) {
      return;
    }
    this.z.next_in = paramArrayOfByte;
    this.z.next_in_index = paramInt1;
    this.z.avail_in = paramInt2;
    label148:
    label154:
    do
    {
      this.z.next_out = this.buf;
      this.z.next_out_index = 0;
      this.z.avail_out = this.bufsize;
      StringBuffer localStringBuffer;
      if (this.compress)
      {
        paramInt1 = this.z.deflate(this.flush);
        if (paramInt1 == 0) {
          break label154;
        }
        localStringBuffer = new StringBuffer();
        if (!this.compress) {
          break label148;
        }
      }
      for (paramArrayOfByte = "de";; paramArrayOfByte = "in")
      {
        throw new ZStreamException(paramArrayOfByte + "flating: " + this.z.msg);
        paramInt1 = this.z.inflate(this.flush);
        break;
      }
      this.out.write(this.buf, 0, this.bufsize - this.z.avail_out);
    } while ((this.z.avail_in > 0) || (this.z.avail_out == 0));
  }
}


/* Location:           C:\Users\Administrator\Documents\ds\反编译\AndroidKiller_v1.3.1\projects\Jungledsq_ppc\ProjectSrc\smali\
 * Qualified Name:     com.jcraft.jzlib.ZOutputStream
 * JD-Core Version:    0.7.0.1
 */