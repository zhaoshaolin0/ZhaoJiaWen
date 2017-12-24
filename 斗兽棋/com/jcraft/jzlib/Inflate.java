package com.jcraft.jzlib;

final class Inflate
{
  private static final int BAD = 13;
  private static final int BLOCKS = 7;
  private static final int CHECK1 = 11;
  private static final int CHECK2 = 10;
  private static final int CHECK3 = 9;
  private static final int CHECK4 = 8;
  private static final int DICT0 = 6;
  private static final int DICT1 = 5;
  private static final int DICT2 = 4;
  private static final int DICT3 = 3;
  private static final int DICT4 = 2;
  private static final int DONE = 12;
  private static final int FLAG = 1;
  private static final int MAX_WBITS = 15;
  private static final int METHOD = 0;
  private static final int PRESET_DICT = 32;
  private static final int Z_BUF_ERROR = -5;
  private static final int Z_DATA_ERROR = -3;
  private static final int Z_DEFLATED = 8;
  private static final int Z_ERRNO = -1;
  static final int Z_FINISH = 4;
  static final int Z_FULL_FLUSH = 3;
  private static final int Z_MEM_ERROR = -4;
  private static final int Z_NEED_DICT = 2;
  static final int Z_NO_FLUSH = 0;
  private static final int Z_OK = 0;
  static final int Z_PARTIAL_FLUSH = 1;
  private static final int Z_STREAM_END = 1;
  private static final int Z_STREAM_ERROR = -2;
  static final int Z_SYNC_FLUSH = 2;
  private static final int Z_VERSION_ERROR = -6;
  private static byte[] mark = { 0, 0, -1, -1 };
  InfBlocks blocks;
  int marker;
  int method;
  int mode;
  long need;
  int nowrap;
  long[] was = new long[1];
  int wbits;
  
  int inflate(ZStream paramZStream, int paramInt)
  {
    if ((paramZStream == null) || (paramZStream.istate == null) || (paramZStream.next_in == null)) {
      return -2;
    }
    int i;
    if (paramInt == 4)
    {
      i = -5;
      paramInt = -5;
    }
    for (;;)
    {
      int j = paramInt;
      int k = paramInt;
      int m = paramInt;
      int n = paramInt;
      int i1 = paramInt;
      int i2 = paramInt;
      int i3 = paramInt;
      int i4 = paramInt;
      int i5 = paramInt;
      Object localObject;
      byte[] arrayOfByte;
      long l;
      switch (paramZStream.istate.mode)
      {
      default: 
        return -2;
        i = 0;
        break;
      case 0: 
        if (paramZStream.avail_in == 0) {
          return paramInt;
        }
        paramInt = i;
        paramZStream.avail_in -= 1;
        paramZStream.total_in += 1L;
        localObject = paramZStream.istate;
        arrayOfByte = paramZStream.next_in;
        j = paramZStream.next_in_index;
        paramZStream.next_in_index = (j + 1);
        j = arrayOfByte[j];
        ((Inflate)localObject).method = j;
        if ((j & 0xF) != 8)
        {
          paramZStream.istate.mode = 13;
          paramZStream.msg = "unknown compression method";
          paramZStream.istate.marker = 5;
        }
        else if ((paramZStream.istate.method >> 4) + 8 > paramZStream.istate.wbits)
        {
          paramZStream.istate.mode = 13;
          paramZStream.msg = "invalid window size";
          paramZStream.istate.marker = 5;
        }
        else
        {
          paramZStream.istate.mode = 1;
          j = paramInt;
        }
        break;
      case 1: 
        if (paramZStream.avail_in == 0) {
          return j;
        }
        paramInt = i;
        paramZStream.avail_in -= 1;
        paramZStream.total_in += 1L;
        localObject = paramZStream.next_in;
        j = paramZStream.next_in_index;
        paramZStream.next_in_index = (j + 1);
        j = localObject[j] & 0xFF;
        if (((paramZStream.istate.method << 8) + j) % 31 != 0)
        {
          paramZStream.istate.mode = 13;
          paramZStream.msg = "incorrect header check";
          paramZStream.istate.marker = 5;
        }
        else if ((j & 0x20) == 0)
        {
          paramZStream.istate.mode = 7;
        }
        else
        {
          paramZStream.istate.mode = 2;
          k = paramInt;
        }
        break;
      case 2: 
        if (paramZStream.avail_in == 0) {
          return k;
        }
        m = i;
        paramZStream.avail_in -= 1;
        paramZStream.total_in += 1L;
        localObject = paramZStream.istate;
        arrayOfByte = paramZStream.next_in;
        paramInt = paramZStream.next_in_index;
        paramZStream.next_in_index = (paramInt + 1);
        ((Inflate)localObject).need = ((arrayOfByte[paramInt] & 0xFF) << 24 & 0xFF000000);
        paramZStream.istate.mode = 3;
      case 3: 
        if (paramZStream.avail_in == 0) {
          return m;
        }
        n = i;
        paramZStream.avail_in -= 1;
        paramZStream.total_in += 1L;
        localObject = paramZStream.istate;
        l = ((Inflate)localObject).need;
        arrayOfByte = paramZStream.next_in;
        paramInt = paramZStream.next_in_index;
        paramZStream.next_in_index = (paramInt + 1);
        ((Inflate)localObject).need = (l + ((arrayOfByte[paramInt] & 0xFF) << 16 & 0xFF0000));
        paramZStream.istate.mode = 4;
      case 4: 
        if (paramZStream.avail_in == 0) {
          return n;
        }
        paramZStream.avail_in -= 1;
        paramZStream.total_in += 1L;
        localObject = paramZStream.istate;
        l = ((Inflate)localObject).need;
        arrayOfByte = paramZStream.next_in;
        paramInt = paramZStream.next_in_index;
        paramZStream.next_in_index = (paramInt + 1);
        ((Inflate)localObject).need = (l + ((arrayOfByte[paramInt] & 0xFF) << 8 & 0xFF00));
        paramZStream.istate.mode = 5;
        i1 = i;
      case 5: 
        if (paramZStream.avail_in == 0) {
          return i1;
        }
        paramZStream.avail_in -= 1;
        paramZStream.total_in += 1L;
        localObject = paramZStream.istate;
        l = ((Inflate)localObject).need;
        arrayOfByte = paramZStream.next_in;
        paramInt = paramZStream.next_in_index;
        paramZStream.next_in_index = (paramInt + 1);
        ((Inflate)localObject).need = (l + (arrayOfByte[paramInt] & 0xFF));
        paramZStream.adler = paramZStream.istate.need;
        paramZStream.istate.mode = 6;
        return 2;
      case 6: 
        paramZStream.istate.mode = 13;
        paramZStream.msg = "need dictionary";
        paramZStream.istate.marker = 0;
        return -2;
      case 7: 
        paramInt = paramZStream.istate.blocks.proc(paramZStream, paramInt);
        if (paramInt == -3)
        {
          paramZStream.istate.mode = 13;
          paramZStream.istate.marker = 0;
        }
        else
        {
          j = paramInt;
          if (paramInt == 0) {
            j = i;
          }
          if (j != 1) {
            return j;
          }
          paramInt = i;
          paramZStream.istate.blocks.reset(paramZStream, paramZStream.istate.was);
          if (paramZStream.istate.nowrap != 0)
          {
            paramZStream.istate.mode = 12;
          }
          else
          {
            paramZStream.istate.mode = 8;
            i2 = paramInt;
          }
        }
        break;
      case 8: 
        if (paramZStream.avail_in == 0) {
          return i2;
        }
        i3 = i;
        paramZStream.avail_in -= 1;
        paramZStream.total_in += 1L;
        localObject = paramZStream.istate;
        arrayOfByte = paramZStream.next_in;
        paramInt = paramZStream.next_in_index;
        paramZStream.next_in_index = (paramInt + 1);
        ((Inflate)localObject).need = ((arrayOfByte[paramInt] & 0xFF) << 24 & 0xFF000000);
        paramZStream.istate.mode = 9;
      case 9: 
        if (paramZStream.avail_in == 0) {
          return i3;
        }
        i4 = i;
        paramZStream.avail_in -= 1;
        paramZStream.total_in += 1L;
        localObject = paramZStream.istate;
        l = ((Inflate)localObject).need;
        arrayOfByte = paramZStream.next_in;
        paramInt = paramZStream.next_in_index;
        paramZStream.next_in_index = (paramInt + 1);
        ((Inflate)localObject).need = (l + ((arrayOfByte[paramInt] & 0xFF) << 16 & 0xFF0000));
        paramZStream.istate.mode = 10;
      case 10: 
        if (paramZStream.avail_in == 0) {
          return i4;
        }
        i5 = i;
        paramZStream.avail_in -= 1;
        paramZStream.total_in += 1L;
        localObject = paramZStream.istate;
        l = ((Inflate)localObject).need;
        arrayOfByte = paramZStream.next_in;
        paramInt = paramZStream.next_in_index;
        paramZStream.next_in_index = (paramInt + 1);
        ((Inflate)localObject).need = (l + ((arrayOfByte[paramInt] & 0xFF) << 8 & 0xFF00));
        paramZStream.istate.mode = 11;
      case 11: 
        if (paramZStream.avail_in == 0) {
          return i5;
        }
        paramInt = i;
        paramZStream.avail_in -= 1;
        paramZStream.total_in += 1L;
        localObject = paramZStream.istate;
        l = ((Inflate)localObject).need;
        arrayOfByte = paramZStream.next_in;
        j = paramZStream.next_in_index;
        paramZStream.next_in_index = (j + 1);
        ((Inflate)localObject).need = (l + (arrayOfByte[j] & 0xFF));
        if ((int)paramZStream.istate.was[0] == (int)paramZStream.istate.need) {
          break label1376;
        }
        paramZStream.istate.mode = 13;
        paramZStream.msg = "incorrect data check";
        paramZStream.istate.marker = 5;
      }
    }
    label1376:
    paramZStream.istate.mode = 12;
    return 1;
    return -3;
  }
  
  int inflateEnd(ZStream paramZStream)
  {
    if (this.blocks != null) {
      this.blocks.free(paramZStream);
    }
    this.blocks = null;
    return 0;
  }
  
  int inflateInit(ZStream paramZStream, int paramInt)
  {
    paramZStream.msg = null;
    this.blocks = null;
    this.nowrap = 0;
    int i = paramInt;
    if (paramInt < 0)
    {
      i = -paramInt;
      this.nowrap = 1;
    }
    if ((i < 8) || (i > 15))
    {
      inflateEnd(paramZStream);
      return -2;
    }
    this.wbits = i;
    Inflate localInflate = paramZStream.istate;
    if (paramZStream.istate.nowrap != 0) {}
    for (Object localObject = null;; localObject = this)
    {
      localInflate.blocks = new InfBlocks(paramZStream, localObject, 1 << i);
      inflateReset(paramZStream);
      return 0;
    }
  }
  
  int inflateReset(ZStream paramZStream)
  {
    if ((paramZStream == null) || (paramZStream.istate == null)) {
      return -2;
    }
    paramZStream.total_out = 0L;
    paramZStream.total_in = 0L;
    paramZStream.msg = null;
    Inflate localInflate = paramZStream.istate;
    if (paramZStream.istate.nowrap != 0) {}
    for (int i = 7;; i = 0)
    {
      localInflate.mode = i;
      paramZStream.istate.blocks.reset(paramZStream, null);
      return 0;
    }
  }
  
  int inflateSetDictionary(ZStream paramZStream, byte[] paramArrayOfByte, int paramInt)
  {
    int k = 0;
    int i = paramInt;
    if ((paramZStream == null) || (paramZStream.istate == null) || (paramZStream.istate.mode != 6)) {
      return -2;
    }
    if (paramZStream._adler.adler32(1L, paramArrayOfByte, 0, paramInt) != paramZStream.adler) {
      return -3;
    }
    paramZStream.adler = paramZStream._adler.adler32(0L, null, 0, 0);
    int j = i;
    if (i >= 1 << paramZStream.istate.wbits)
    {
      j = (1 << paramZStream.istate.wbits) - 1;
      k = paramInt - j;
    }
    paramZStream.istate.blocks.set_dictionary(paramArrayOfByte, k, j);
    paramZStream.istate.mode = 7;
    return 0;
  }
  
  int inflateSync(ZStream paramZStream)
  {
    if ((paramZStream == null) || (paramZStream.istate == null)) {
      return -2;
    }
    if (paramZStream.istate.mode != 13)
    {
      paramZStream.istate.mode = 13;
      paramZStream.istate.marker = 0;
    }
    int j = paramZStream.avail_in;
    if (j == 0) {
      return -5;
    }
    int k = paramZStream.next_in_index;
    int i = paramZStream.istate.marker;
    if ((j != 0) && (i < 4))
    {
      if (paramZStream.next_in[k] == mark[i]) {
        i += 1;
      }
      for (;;)
      {
        k += 1;
        j -= 1;
        break;
        if (paramZStream.next_in[k] != 0) {
          i = 0;
        } else {
          i = 4 - i;
        }
      }
    }
    paramZStream.total_in += k - paramZStream.next_in_index;
    paramZStream.next_in_index = k;
    paramZStream.avail_in = j;
    paramZStream.istate.marker = i;
    if (i != 4) {
      return -3;
    }
    long l1 = paramZStream.total_in;
    long l2 = paramZStream.total_out;
    inflateReset(paramZStream);
    paramZStream.total_in = l1;
    paramZStream.total_out = l2;
    paramZStream.istate.mode = 7;
    return 0;
  }
  
  int inflateSyncPoint(ZStream paramZStream)
  {
    if ((paramZStream == null) || (paramZStream.istate == null) || (paramZStream.istate.blocks == null)) {
      return -2;
    }
    return paramZStream.istate.blocks.sync_point();
  }
}


/* Location:           C:\Users\Administrator\Documents\ds\反编译\AndroidKiller_v1.3.1\projects\Jungledsq_ppc\ProjectSrc\smali\
 * Qualified Name:     com.jcraft.jzlib.Inflate
 * JD-Core Version:    0.7.0.1
 */