package com.jcraft.jzlib;

import java.io.PrintStream;

public final class ZStream
{
  private static final int DEF_WBITS = 15;
  private static final int MAX_MEM_LEVEL = 9;
  private static final int MAX_WBITS = 15;
  private static final int Z_BUF_ERROR = -5;
  private static final int Z_DATA_ERROR = -3;
  private static final int Z_ERRNO = -1;
  private static final int Z_FINISH = 4;
  private static final int Z_FULL_FLUSH = 3;
  private static final int Z_MEM_ERROR = -4;
  private static final int Z_NEED_DICT = 2;
  private static final int Z_NO_FLUSH = 0;
  private static final int Z_OK = 0;
  private static final int Z_PARTIAL_FLUSH = 1;
  private static final int Z_STREAM_END = 1;
  private static final int Z_STREAM_ERROR = -2;
  private static final int Z_SYNC_FLUSH = 2;
  private static final int Z_VERSION_ERROR = -6;
  Adler32 _adler = new Adler32();
  public long adler;
  public int avail_in;
  public int avail_out;
  int data_type;
  Deflate dstate;
  Inflate istate;
  public String msg;
  public byte[] next_in;
  public int next_in_index;
  public byte[] next_out;
  public int next_out_index;
  public long total_in;
  public long total_out;
  
  public int deflate(int paramInt)
  {
    if (this.dstate == null) {
      return -2;
    }
    return this.dstate.deflate(this, paramInt);
  }
  
  public int deflateEnd()
  {
    if (this.dstate == null) {
      return -2;
    }
    int i = this.dstate.deflateEnd();
    this.dstate = null;
    return i;
  }
  
  public int deflateInit(int paramInt)
  {
    return deflateInit(paramInt, 15);
  }
  
  public int deflateInit(int paramInt1, int paramInt2)
  {
    return deflateInit(paramInt1, paramInt2, false);
  }
  
  public int deflateInit(int paramInt1, int paramInt2, boolean paramBoolean)
  {
    this.dstate = new Deflate();
    Deflate localDeflate = this.dstate;
    if (paramBoolean) {
      paramInt2 = -paramInt2;
    }
    for (;;)
    {
      return localDeflate.deflateInit(this, paramInt1, paramInt2);
    }
  }
  
  public int deflateInit(int paramInt, boolean paramBoolean)
  {
    return deflateInit(paramInt, 15, paramBoolean);
  }
  
  public int deflateParams(int paramInt1, int paramInt2)
  {
    if (this.dstate == null) {
      return -2;
    }
    return this.dstate.deflateParams(this, paramInt1, paramInt2);
  }
  
  public int deflateSetDictionary(byte[] paramArrayOfByte, int paramInt)
  {
    if (this.dstate == null) {
      return -2;
    }
    return this.dstate.deflateSetDictionary(this, paramArrayOfByte, paramInt);
  }
  
  void flush_pending()
  {
    int j = this.dstate.pending;
    int i = j;
    if (j > this.avail_out) {
      i = this.avail_out;
    }
    if (i == 0) {}
    do
    {
      return;
      if ((this.dstate.pending_buf.length <= this.dstate.pending_out) || (this.next_out.length <= this.next_out_index) || (this.dstate.pending_buf.length < this.dstate.pending_out + i) || (this.next_out.length < this.next_out_index + i))
      {
        System.out.println(this.dstate.pending_buf.length + ", " + this.dstate.pending_out + ", " + this.next_out.length + ", " + this.next_out_index + ", " + i);
        System.out.println("avail_out=" + this.avail_out);
      }
      System.arraycopy(this.dstate.pending_buf, this.dstate.pending_out, this.next_out, this.next_out_index, i);
      this.next_out_index += i;
      Deflate localDeflate = this.dstate;
      localDeflate.pending_out += i;
      this.total_out += i;
      this.avail_out -= i;
      localDeflate = this.dstate;
      localDeflate.pending -= i;
    } while (this.dstate.pending != 0);
    this.dstate.pending_out = 0;
  }
  
  public void free()
  {
    this.next_in = null;
    this.next_out = null;
    this.msg = null;
    this._adler = null;
  }
  
  public int inflate(int paramInt)
  {
    if (this.istate == null) {
      return -2;
    }
    return this.istate.inflate(this, paramInt);
  }
  
  public int inflateEnd()
  {
    if (this.istate == null) {
      return -2;
    }
    int i = this.istate.inflateEnd(this);
    this.istate = null;
    return i;
  }
  
  public int inflateInit()
  {
    return inflateInit(15);
  }
  
  public int inflateInit(int paramInt)
  {
    return inflateInit(paramInt, false);
  }
  
  public int inflateInit(int paramInt, boolean paramBoolean)
  {
    this.istate = new Inflate();
    Inflate localInflate = this.istate;
    if (paramBoolean) {
      paramInt = -paramInt;
    }
    for (;;)
    {
      return localInflate.inflateInit(this, paramInt);
    }
  }
  
  public int inflateInit(boolean paramBoolean)
  {
    return inflateInit(15, paramBoolean);
  }
  
  public int inflateSetDictionary(byte[] paramArrayOfByte, int paramInt)
  {
    if (this.istate == null) {
      return -2;
    }
    return this.istate.inflateSetDictionary(this, paramArrayOfByte, paramInt);
  }
  
  public int inflateSync()
  {
    if (this.istate == null) {
      return -2;
    }
    return this.istate.inflateSync(this);
  }
  
  int read_buf(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
  {
    int j = this.avail_in;
    int i = j;
    if (j > paramInt2) {
      i = paramInt2;
    }
    if (i == 0) {
      return 0;
    }
    this.avail_in -= i;
    if (this.dstate.noheader == 0) {
      this.adler = this._adler.adler32(this.adler, this.next_in, this.next_in_index, i);
    }
    System.arraycopy(this.next_in, this.next_in_index, paramArrayOfByte, paramInt1, i);
    this.next_in_index += i;
    this.total_in += i;
    return i;
  }
}


/* Location:           C:\Users\Administrator\Documents\ds\反编译\AndroidKiller_v1.3.1\projects\Jungledsq_ppc\ProjectSrc\smali\
 * Qualified Name:     com.jcraft.jzlib.ZStream
 * JD-Core Version:    0.7.0.1
 */