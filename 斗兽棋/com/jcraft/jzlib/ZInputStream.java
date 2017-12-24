package com.jcraft.jzlib;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

public class ZInputStream
  extends FilterInputStream
{
  protected byte[] buf = new byte[this.bufsize];
  protected byte[] buf1 = new byte[1];
  protected int bufsize = 512;
  protected boolean compress;
  protected int flush = 0;
  protected InputStream in = null;
  private boolean nomoreinput = false;
  protected ZStream z = new ZStream();
  
  public ZInputStream(InputStream paramInputStream)
  {
    this(paramInputStream, false);
  }
  
  public ZInputStream(InputStream paramInputStream, int paramInt)
  {
    super(paramInputStream);
    this.in = paramInputStream;
    this.z.deflateInit(paramInt);
    this.compress = true;
    this.z.next_in = this.buf;
    this.z.next_in_index = 0;
    this.z.avail_in = 0;
  }
  
  public ZInputStream(InputStream paramInputStream, boolean paramBoolean)
  {
    super(paramInputStream);
    this.in = paramInputStream;
    this.z.inflateInit(paramBoolean);
    this.compress = false;
    this.z.next_in = this.buf;
    this.z.next_in_index = 0;
    this.z.avail_in = 0;
  }
  
  public void close()
    throws IOException
  {
    this.in.close();
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
  
  public int read()
    throws IOException
  {
    if (read(this.buf1, 0, 1) == -1) {
      return -1;
    }
    return this.buf1[0] & 0xFF;
  }
  
  public int read(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
    throws IOException
  {
    if (paramInt2 == 0) {
      return 0;
    }
    this.z.next_out = paramArrayOfByte;
    this.z.next_out_index = paramInt1;
    this.z.avail_out = paramInt2;
    do
    {
      if ((this.z.avail_in == 0) && (!this.nomoreinput))
      {
        this.z.next_in_index = 0;
        this.z.avail_in = this.in.read(this.buf, 0, this.bufsize);
        if (this.z.avail_in == -1)
        {
          this.z.avail_in = 0;
          this.nomoreinput = true;
        }
      }
      if (this.compress) {}
      for (paramInt1 = this.z.deflate(this.flush); (this.nomoreinput) && (paramInt1 == -5); paramInt1 = this.z.inflate(this.flush)) {
        return -1;
      }
      if ((paramInt1 != 0) && (paramInt1 != 1))
      {
        StringBuffer localStringBuffer = new StringBuffer();
        if (this.compress) {}
        for (paramArrayOfByte = "de";; paramArrayOfByte = "in") {
          throw new ZStreamException(paramArrayOfByte + "flating: " + this.z.msg);
        }
      }
      if (((this.nomoreinput) || (paramInt1 == 1)) && (this.z.avail_out == paramInt2)) {
        return -1;
      }
    } while ((this.z.avail_out == paramInt2) && (paramInt1 == 0));
    return paramInt2 - this.z.avail_out;
  }
  
  public void setFlushMode(int paramInt)
  {
    this.flush = paramInt;
  }
  
  public long skip(long paramLong)
    throws IOException
  {
    int i = 512;
    if (paramLong < 512) {
      i = (int)paramLong;
    }
    return read(new byte[i]);
  }
}


/* Location:           C:\Users\Administrator\Documents\ds\反编译\AndroidKiller_v1.3.1\projects\Jungledsq_ppc\ProjectSrc\smali\
 * Qualified Name:     com.jcraft.jzlib.ZInputStream
 * JD-Core Version:    0.7.0.1
 */