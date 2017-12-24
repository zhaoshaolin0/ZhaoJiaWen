package com.jcraft.jzlib;

final class InfCodes
{
  private static final int BADCODE = 9;
  private static final int COPY = 5;
  private static final int DIST = 3;
  private static final int DISTEXT = 4;
  private static final int END = 8;
  private static final int LEN = 1;
  private static final int LENEXT = 2;
  private static final int LIT = 6;
  private static final int START = 0;
  private static final int WASH = 7;
  private static final int Z_BUF_ERROR = -5;
  private static final int Z_DATA_ERROR = -3;
  private static final int Z_ERRNO = -1;
  private static final int Z_MEM_ERROR = -4;
  private static final int Z_NEED_DICT = 2;
  private static final int Z_OK = 0;
  private static final int Z_STREAM_END = 1;
  private static final int Z_STREAM_ERROR = -2;
  private static final int Z_VERSION_ERROR = -6;
  private static final int[] inflate_mask = { 0, 1, 3, 7, 15, 31, 63, 127, 255, 511, 1023, 2047, 4095, 8191, 16383, 32767, 65535 };
  byte dbits;
  int dist;
  int[] dtree;
  int dtree_index;
  int get;
  byte lbits;
  int len;
  int lit;
  int[] ltree;
  int ltree_index;
  int mode;
  int need;
  int[] tree;
  int tree_index = 0;
  
  void free(ZStream paramZStream) {}
  
  int inflate_fast(int paramInt1, int paramInt2, int[] paramArrayOfInt1, int paramInt3, int[] paramArrayOfInt2, int paramInt4, InfBlocks paramInfBlocks, ZStream paramZStream)
  {
    int m = paramZStream.next_in_index;
    int i = paramZStream.avail_in;
    int n = paramInfBlocks.bitb;
    int k = paramInfBlocks.bitk;
    int i1 = paramInfBlocks.write;
    if (i1 < paramInfBlocks.read) {}
    int i7;
    int i8;
    for (int j = paramInfBlocks.read - i1 - 1;; j = paramInfBlocks.end - i1)
    {
      i7 = inflate_mask[paramInt1];
      i8 = inflate_mask[paramInt2];
      paramInt2 = i1;
      paramInt1 = m;
      m = j;
      j = n;
      while (k < 20)
      {
        i -= 1;
        j |= (paramZStream.next_in[paramInt1] & 0xFF) << k;
        k += 8;
        paramInt1 += 1;
      }
    }
    int i4 = j & i7;
    int i5 = (paramInt3 + i4) * 3;
    int i6 = paramArrayOfInt1[i5];
    n = j;
    i1 = i6;
    int i2 = k;
    int i3 = i5;
    byte[] arrayOfByte1;
    if (i6 == 0)
    {
      j >>= paramArrayOfInt1[(i5 + 1)];
      k -= paramArrayOfInt1[(i5 + 1)];
      arrayOfByte1 = paramInfBlocks.window;
      n = paramInt2 + 1;
      arrayOfByte1[paramInt2] = ((byte)paramArrayOfInt1[(i5 + 2)]);
      m -= 1;
      paramInt2 = n;
      label241:
      if ((m < 258) || (i < 10))
      {
        paramInt4 = paramZStream.avail_in - i;
        paramInt3 = paramInt4;
        if (k >> 3 < paramInt4) {
          paramInt3 = k >> 3;
        }
        paramInt1 -= paramInt3;
        paramInfBlocks.bitb = j;
        paramInfBlocks.bitk = (k - (paramInt3 << 3));
        paramZStream.avail_in = (i + paramInt3);
        paramZStream.total_in += paramInt1 - paramZStream.next_in_index;
        paramZStream.next_in_index = paramInt1;
        paramInfBlocks.write = paramInt2;
        return 0;
      }
    }
    else
    {
      label352:
      k = n >> paramArrayOfInt1[(i3 + 1)];
      j = i2 - paramArrayOfInt1[(i3 + 1)];
      if ((i1 & 0x10) != 0)
      {
        i1 &= 0xF;
        i4 = paramArrayOfInt1[(i3 + 2)] + (inflate_mask[i1] & k);
        n = k >> i1;
        k = j - i1;
        j = n;
        while (k < 15)
        {
          i -= 1;
          j |= (paramZStream.next_in[paramInt1] & 0xFF) << k;
          k += 8;
          paramInt1 += 1;
        }
        n = j & i8;
        i1 = (paramInt4 + n) * 3;
        i3 = paramArrayOfInt2[i1];
        i2 = k;
        label498:
        k = j >> paramArrayOfInt2[(i1 + 1)];
        i2 -= paramArrayOfInt2[(i1 + 1)];
        if ((i3 & 0x10) != 0)
        {
          i3 &= 0xF;
          j = paramInt1;
          n = i;
          while (i2 < i3)
          {
            n -= 1;
            k |= (paramZStream.next_in[j] & 0xFF) << i2;
            i2 += 8;
            j += 1;
          }
          paramInt1 = paramArrayOfInt2[(i1 + 2)] + (inflate_mask[i3] & k);
          i1 = k >> i3;
          i2 -= i3;
          m -= i4;
          if (paramInt2 >= paramInt1)
          {
            paramInt1 = paramInt2 - paramInt1;
            if ((paramInt2 - paramInt1 > 0) && (2 > paramInt2 - paramInt1))
            {
              arrayOfByte1 = paramInfBlocks.window;
              k = paramInt2 + 1;
              byte[] arrayOfByte2 = paramInfBlocks.window;
              i3 = paramInt1 + 1;
              arrayOfByte1[paramInt2] = arrayOfByte2[paramInt1];
              arrayOfByte1 = paramInfBlocks.window;
              arrayOfByte2 = paramInfBlocks.window;
              i = i3 + 1;
              arrayOfByte1[k] = arrayOfByte2[i3];
              paramInt1 = i4 - 2;
              paramInt2 = k + 1;
            }
          }
        }
      }
    }
    for (;;)
    {
      if ((paramInt2 - i > 0) && (paramInt1 > paramInt2 - i))
      {
        label741:
        arrayOfByte1 = paramInfBlocks.window;
        k = paramInt2 + 1;
        arrayOfByte1[paramInt2] = paramInfBlocks.window[i];
        paramInt1 -= 1;
        if (paramInt1 != 0) {
          break label1489;
        }
        paramInt2 = k;
        paramInt1 = j;
        j = i1;
        k = i2;
        i = n;
        break label241;
        System.arraycopy(paramInfBlocks.window, paramInt1, paramInfBlocks.window, paramInt2, 2);
        paramInt2 += 2;
        i = paramInt1 + 2;
        paramInt1 = i4 - 2;
        continue;
        paramInt1 = paramInt2 - paramInt1;
        do
        {
          i = paramInt1 + paramInfBlocks.end;
          paramInt1 = i;
        } while (i < 0);
        paramInt1 = paramInfBlocks.end - i;
        if (i4 <= paramInt1) {
          break label1513;
        }
        i3 = i4 - paramInt1;
        if ((paramInt2 - i <= 0) || (paramInt1 <= paramInt2 - i)) {}
      }
      for (;;)
      {
        arrayOfByte1 = paramInfBlocks.window;
        k = paramInt2 + 1;
        arrayOfByte1[paramInt2] = paramInfBlocks.window[i];
        paramInt1 -= 1;
        if (paramInt1 == 0)
        {
          paramInt2 = k;
          for (;;)
          {
            i = 0;
            paramInt1 = i3;
            break;
            System.arraycopy(paramInfBlocks.window, i, paramInfBlocks.window, paramInt2, paramInt1);
            paramInt2 += paramInt1;
          }
          System.arraycopy(paramInfBlocks.window, i, paramInfBlocks.window, paramInt2, paramInt1);
          paramInt2 += paramInt1;
          paramInt1 = j;
          j = i1;
          k = i2;
          i = n;
          break label241;
          if ((i3 & 0x40) == 0)
          {
            n = n + paramArrayOfInt2[(i1 + 2)] + (inflate_mask[i3] & k);
            i1 = (paramInt4 + n) * 3;
            i3 = paramArrayOfInt2[i1];
            j = k;
            break label498;
          }
          paramZStream.msg = "invalid distance code";
          paramInt4 = paramZStream.avail_in - i;
          paramInt3 = paramInt4;
          if (i2 >> 3 < paramInt4) {
            paramInt3 = i2 >> 3;
          }
          paramInt1 -= paramInt3;
          paramInfBlocks.bitb = k;
          paramInfBlocks.bitk = (i2 - (paramInt3 << 3));
          paramZStream.avail_in = (i + paramInt3);
          paramZStream.total_in += paramInt1 - paramZStream.next_in_index;
          paramZStream.next_in_index = paramInt1;
          paramInfBlocks.write = paramInt2;
          return -3;
          if ((i1 & 0x40) == 0)
          {
            i4 = i4 + paramArrayOfInt1[(i3 + 2)] + (inflate_mask[i1] & k);
            i5 = (paramInt3 + i4) * 3;
            i6 = paramArrayOfInt1[i5];
            n = k;
            i1 = i6;
            i2 = j;
            i3 = i5;
            if (i6 != 0) {
              break label352;
            }
            k >>= paramArrayOfInt1[(i5 + 1)];
            i1 = j - paramArrayOfInt1[(i5 + 1)];
            arrayOfByte1 = paramInfBlocks.window;
            n = paramInt2 + 1;
            arrayOfByte1[paramInt2] = ((byte)paramArrayOfInt1[(i5 + 2)]);
            m -= 1;
            j = k;
            k = i1;
            paramInt2 = n;
            break label241;
          }
          if ((i1 & 0x20) != 0)
          {
            paramInt4 = paramZStream.avail_in - i;
            paramInt3 = paramInt4;
            if (j >> 3 < paramInt4) {
              paramInt3 = j >> 3;
            }
            paramInt1 -= paramInt3;
            paramInfBlocks.bitb = k;
            paramInfBlocks.bitk = (j - (paramInt3 << 3));
            paramZStream.avail_in = (i + paramInt3);
            paramZStream.total_in += paramInt1 - paramZStream.next_in_index;
            paramZStream.next_in_index = paramInt1;
            paramInfBlocks.write = paramInt2;
            return 1;
          }
          paramZStream.msg = "invalid literal/length code";
          paramInt4 = paramZStream.avail_in - i;
          paramInt3 = paramInt4;
          if (j >> 3 < paramInt4) {
            paramInt3 = j >> 3;
          }
          paramInt1 -= paramInt3;
          paramInfBlocks.bitb = k;
          paramInfBlocks.bitk = (j - (paramInt3 << 3));
          paramZStream.avail_in = (i + paramInt3);
          paramZStream.total_in += paramInt1 - paramZStream.next_in_index;
          paramZStream.next_in_index = paramInt1;
          paramInfBlocks.write = paramInt2;
          return -3;
          break;
          label1489:
          i += 1;
          paramInt2 = k;
          break label741;
        }
        i += 1;
        paramInt2 = k;
      }
      label1513:
      paramInt1 = i4;
    }
  }
  
  void init(int paramInt1, int paramInt2, int[] paramArrayOfInt1, int paramInt3, int[] paramArrayOfInt2, int paramInt4, ZStream paramZStream)
  {
    this.mode = 0;
    this.lbits = ((byte)paramInt1);
    this.dbits = ((byte)paramInt2);
    this.ltree = paramArrayOfInt1;
    this.ltree_index = paramInt3;
    this.dtree = paramArrayOfInt2;
    this.dtree_index = paramInt4;
    this.tree = null;
  }
  
  int proc(InfBlocks paramInfBlocks, ZStream paramZStream, int paramInt)
  {
    int i = paramZStream.next_in_index;
    int k = paramZStream.avail_in;
    int m = paramInfBlocks.bitb;
    int j = paramInfBlocks.bitk;
    int i1 = paramInfBlocks.write;
    int n;
    if (i1 < paramInfBlocks.read) {
      n = paramInfBlocks.read - i1 - 1;
    }
    int i5;
    int i4;
    int i22;
    int i21;
    int i20;
    int i19;
    for (;;)
    {
      i5 = m;
      int i6 = j;
      int i18 = n;
      i4 = k;
      int i11 = i;
      int i17 = i1;
      i3 = paramInt;
      int i7 = m;
      int i8 = j;
      int i9 = k;
      int i12 = i;
      int i10 = paramInt;
      i16 = m;
      i15 = j;
      i14 = k;
      i13 = i;
      i2 = paramInt;
      i22 = j;
      i21 = k;
      i20 = i;
      i19 = i1;
      switch (this.mode)
      {
      default: 
        paramInfBlocks.bitb = m;
        paramInfBlocks.bitk = j;
        paramZStream.avail_in = k;
        paramZStream.total_in += i - paramZStream.next_in_index;
        paramZStream.next_in_index = i;
        paramInfBlocks.write = i1;
        return paramInfBlocks.inflate_flush(paramZStream, -2);
        n = paramInfBlocks.end - i1;
        break;
      case 0: 
        i5 = m;
        i6 = j;
        i2 = n;
        i4 = k;
        i11 = i;
        i7 = i1;
        i3 = paramInt;
        if (n >= 258)
        {
          i5 = m;
          i6 = j;
          i2 = n;
          i4 = k;
          i11 = i;
          i7 = i1;
          i3 = paramInt;
          if (k >= 10)
          {
            paramInfBlocks.bitb = m;
            paramInfBlocks.bitk = j;
            paramZStream.avail_in = k;
            paramZStream.total_in += i - paramZStream.next_in_index;
            paramZStream.next_in_index = i;
            paramInfBlocks.write = i1;
            paramInt = inflate_fast(this.lbits, this.dbits, this.ltree, this.ltree_index, this.dtree, this.dtree_index, paramInfBlocks, paramZStream);
            i = paramZStream.next_in_index;
            k = paramZStream.avail_in;
            m = paramInfBlocks.bitb;
            j = paramInfBlocks.bitk;
            i1 = paramInfBlocks.write;
            if (i1 < paramInfBlocks.read)
            {
              n = paramInfBlocks.read - i1 - 1;
              i5 = m;
              i6 = j;
              i2 = n;
              i4 = k;
              i11 = i;
              i7 = i1;
              i3 = paramInt;
              if (paramInt == 0) {
                break label520;
              }
              if (paramInt != 1) {
                break label513;
              }
            }
            for (i2 = 7;; i2 = 9)
            {
              this.mode = i2;
              break;
              n = paramInfBlocks.end - i1;
              break label452;
            }
          }
        }
        this.need = this.lbits;
        this.tree = this.ltree;
        this.tree_index = this.ltree_index;
        this.mode = 1;
        i17 = i7;
        i18 = i2;
      case 1: 
        i = this.need;
        paramInt = i11;
        k = i4;
        while (i6 < i) {
          if (k != 0)
          {
            i3 = 0;
            k -= 1;
            i5 |= (paramZStream.next_in[paramInt] & 0xFF) << i6;
            i6 += 8;
            paramInt += 1;
          }
          else
          {
            paramInfBlocks.bitb = i5;
            paramInfBlocks.bitk = i6;
            paramZStream.avail_in = k;
            paramZStream.total_in += paramInt - paramZStream.next_in_index;
            paramZStream.next_in_index = paramInt;
            paramInfBlocks.write = i17;
            return paramInfBlocks.inflate_flush(paramZStream, i3);
          }
        }
        i = (this.tree_index + (inflate_mask[i] & i5)) * 3;
        m = i5 >>> this.tree[(i + 1)];
        j = i6 - this.tree[(i + 1)];
        n = this.tree[i];
        if (n == 0)
        {
          this.lit = this.tree[(i + 2)];
          this.mode = 6;
          n = i18;
          i = paramInt;
          i1 = i17;
          paramInt = i3;
        }
        else if ((n & 0x10) != 0)
        {
          this.get = (n & 0xF);
          this.len = this.tree[(i + 2)];
          this.mode = 2;
          n = i18;
          i = paramInt;
          i1 = i17;
          paramInt = i3;
        }
        else if ((n & 0x40) == 0)
        {
          this.need = n;
          this.tree_index = (i / 3 + this.tree[(i + 2)]);
          n = i18;
          i = paramInt;
          i1 = i17;
          paramInt = i3;
        }
        else if ((n & 0x20) != 0)
        {
          this.mode = 7;
          n = i18;
          i = paramInt;
          i1 = i17;
          paramInt = i3;
        }
        else
        {
          this.mode = 9;
          paramZStream.msg = "invalid literal/length code";
          paramInfBlocks.bitb = m;
          paramInfBlocks.bitk = j;
          paramZStream.avail_in = k;
          paramZStream.total_in += paramInt - paramZStream.next_in_index;
          paramZStream.next_in_index = paramInt;
          paramInfBlocks.write = i17;
          return paramInfBlocks.inflate_flush(paramZStream, -3);
        }
        break;
      case 2: 
        i2 = this.get;
        i10 = paramInt;
        paramInt = i;
        while (j < i2) {
          if (k != 0)
          {
            i10 = 0;
            k -= 1;
            m |= (paramZStream.next_in[paramInt] & 0xFF) << j;
            j += 8;
            paramInt += 1;
          }
          else
          {
            paramInfBlocks.bitb = m;
            paramInfBlocks.bitk = j;
            paramZStream.avail_in = k;
            paramZStream.total_in += paramInt - paramZStream.next_in_index;
            paramZStream.next_in_index = paramInt;
            paramInfBlocks.write = i1;
            return paramInfBlocks.inflate_flush(paramZStream, i10);
          }
        }
        this.len += (inflate_mask[i2] & m);
        i7 = m >> i2;
        i8 = j - i2;
        this.need = this.dbits;
        this.tree = this.dtree;
        this.tree_index = this.dtree_index;
        this.mode = 3;
        i12 = paramInt;
        i9 = k;
      case 3: 
        label452:
        i = this.need;
        label513:
        label520:
        paramInt = i12;
        k = i9;
        while (i8 < i) {
          if (k != 0)
          {
            i10 = 0;
            k -= 1;
            i7 |= (paramZStream.next_in[paramInt] & 0xFF) << i8;
            i8 += 8;
            paramInt += 1;
          }
          else
          {
            paramInfBlocks.bitb = i7;
            paramInfBlocks.bitk = i8;
            paramZStream.avail_in = k;
            paramZStream.total_in += paramInt - paramZStream.next_in_index;
            paramZStream.next_in_index = paramInt;
            paramInfBlocks.write = i1;
            return paramInfBlocks.inflate_flush(paramZStream, i10);
          }
        }
        i = (this.tree_index + (inflate_mask[i] & i7)) * 3;
        m = i7 >> this.tree[(i + 1)];
        j = i8 - this.tree[(i + 1)];
        i2 = this.tree[i];
        if ((i2 & 0x10) != 0)
        {
          this.get = (i2 & 0xF);
          this.dist = this.tree[(i + 2)];
          this.mode = 4;
          i = paramInt;
          paramInt = i10;
        }
        else
        {
          if ((i2 & 0x40) != 0) {
            break label1414;
          }
          this.need = i2;
          this.tree_index = (i / 3 + this.tree[(i + 2)]);
          i = paramInt;
          paramInt = i10;
        }
        break;
      }
    }
    label1414:
    this.mode = 9;
    paramZStream.msg = "invalid distance code";
    paramInfBlocks.bitb = m;
    paramInfBlocks.bitk = j;
    paramZStream.avail_in = k;
    paramZStream.total_in += paramInt - paramZStream.next_in_index;
    paramZStream.next_in_index = paramInt;
    paramInfBlocks.write = i1;
    return paramInfBlocks.inflate_flush(paramZStream, -3);
    int i3 = this.get;
    int i2 = paramInt;
    paramInt = i;
    while (j < i3) {
      if (k != 0)
      {
        i2 = 0;
        k -= 1;
        m |= (paramZStream.next_in[paramInt] & 0xFF) << j;
        j += 8;
        paramInt += 1;
      }
      else
      {
        paramInfBlocks.bitb = m;
        paramInfBlocks.bitk = j;
        paramZStream.avail_in = k;
        paramZStream.total_in += paramInt - paramZStream.next_in_index;
        paramZStream.next_in_index = paramInt;
        paramInfBlocks.write = i1;
        return paramInfBlocks.inflate_flush(paramZStream, i2);
      }
    }
    this.dist += (inflate_mask[i3] & m);
    int i16 = m >> i3;
    int i15 = j - i3;
    this.mode = 5;
    int i13 = paramInt;
    int i14 = k;
    j = i1 - this.dist;
    for (;;)
    {
      i4 = j;
      i = n;
      paramInt = i1;
      i3 = i2;
      if (j >= 0) {
        break;
      }
      j += paramInfBlocks.end;
    }
    label1682:
    byte[] arrayOfByte1 = paramInfBlocks.window;
    byte[] arrayOfByte2 = paramInfBlocks.window;
    j = i4 + 1;
    arrayOfByte1[k] = arrayOfByte2[i4];
    i = m - 1;
    if (j == paramInfBlocks.end) {
      j = 0;
    }
    for (;;)
    {
      this.len -= 1;
      paramInt = k + 1;
      i3 = i1;
      i4 = j;
      if (this.len != 0)
      {
        m = i;
        k = paramInt;
        i1 = i3;
        if (i != 0) {
          break label1682;
        }
        j = i;
        n = paramInt;
        if (paramInt == paramInfBlocks.end)
        {
          j = i;
          n = paramInt;
          if (paramInfBlocks.read != 0)
          {
            n = 0;
            if (paramInfBlocks.read >= 0) {
              break label2000;
            }
            j = paramInfBlocks.read - 0 - 1;
          }
        }
        label1823:
        m = j;
        k = n;
        i1 = i3;
        if (j != 0) {
          break label1682;
        }
        paramInfBlocks.write = n;
        n = paramInfBlocks.inflate_flush(paramZStream, i3);
        k = paramInfBlocks.write;
        if (k < paramInfBlocks.read)
        {
          j = paramInfBlocks.read - k - 1;
          label1881:
          paramInt = j;
          i = k;
          if (k == paramInfBlocks.end)
          {
            paramInt = j;
            i = k;
            if (paramInfBlocks.read != 0)
            {
              i = 0;
              if (paramInfBlocks.read >= 0) {
                break label2023;
              }
            }
          }
        }
        label2000:
        label2023:
        for (paramInt = paramInfBlocks.read - 0 - 1;; paramInt = paramInfBlocks.end - 0)
        {
          m = paramInt;
          k = i;
          i1 = n;
          if (paramInt != 0) {
            break;
          }
          paramInfBlocks.bitb = i16;
          paramInfBlocks.bitk = i15;
          paramZStream.avail_in = i14;
          paramZStream.total_in += i13 - paramZStream.next_in_index;
          paramZStream.next_in_index = i13;
          paramInfBlocks.write = i;
          return paramInfBlocks.inflate_flush(paramZStream, n);
          j = paramInfBlocks.end - 0;
          break label1823;
          j = paramInfBlocks.end - k;
          break label1881;
        }
      }
      this.mode = 0;
      m = i16;
      j = i15;
      n = i;
      k = i14;
      i = i13;
      i1 = paramInt;
      paramInt = i3;
      break;
      i4 = n;
      i3 = i1;
      if (n == 0)
      {
        i2 = n;
        i5 = i1;
        if (i1 == paramInfBlocks.end)
        {
          i2 = n;
          i5 = i1;
          if (paramInfBlocks.read != 0)
          {
            i5 = 0;
            if (paramInfBlocks.read >= 0) {
              break label2300;
            }
            i2 = paramInfBlocks.read - 0 - 1;
          }
        }
        i4 = i2;
        i3 = i5;
        if (i2 == 0)
        {
          paramInfBlocks.write = i5;
          i5 = paramInfBlocks.inflate_flush(paramZStream, paramInt);
          i2 = paramInfBlocks.write;
          if (i2 < paramInfBlocks.read)
          {
            i1 = paramInfBlocks.read - i2 - 1;
            label2185:
            paramInt = i1;
            n = i2;
            if (i2 == paramInfBlocks.end)
            {
              paramInt = i1;
              n = i2;
              if (paramInfBlocks.read != 0)
              {
                n = 0;
                if (paramInfBlocks.read >= 0) {
                  break label2323;
                }
              }
            }
          }
          label2323:
          for (paramInt = paramInfBlocks.read - 0 - 1;; paramInt = paramInfBlocks.end - 0)
          {
            i4 = paramInt;
            i3 = n;
            if (paramInt != 0) {
              break label2333;
            }
            paramInfBlocks.bitb = m;
            paramInfBlocks.bitk = j;
            paramZStream.avail_in = k;
            paramZStream.total_in += i - paramZStream.next_in_index;
            paramZStream.next_in_index = i;
            paramInfBlocks.write = n;
            return paramInfBlocks.inflate_flush(paramZStream, i5);
            label2300:
            i2 = paramInfBlocks.end - 0;
            break;
            i1 = paramInfBlocks.end - i2;
            break label2185;
          }
        }
      }
      label2333:
      paramInt = 0;
      paramInfBlocks.window[i3] = ((byte)this.lit);
      n = i4 - 1;
      this.mode = 0;
      i1 = i3 + 1;
      break;
      n = j;
      i2 = k;
      i3 = i;
      if (j > 7)
      {
        n = j - 8;
        i2 = k + 1;
        i3 = i - 1;
      }
      paramInfBlocks.write = i1;
      paramInt = paramInfBlocks.inflate_flush(paramZStream, paramInt);
      i19 = paramInfBlocks.write;
      if (i19 < paramInfBlocks.read) {}
      for (i = paramInfBlocks.read; paramInfBlocks.read != paramInfBlocks.write; i = paramInfBlocks.end)
      {
        paramInfBlocks.bitb = m;
        paramInfBlocks.bitk = n;
        paramZStream.avail_in = i2;
        paramZStream.total_in += i3 - paramZStream.next_in_index;
        paramZStream.next_in_index = i3;
        paramInfBlocks.write = i19;
        return paramInfBlocks.inflate_flush(paramZStream, paramInt);
      }
      this.mode = 8;
      i20 = i3;
      i21 = i2;
      i22 = n;
      paramInfBlocks.bitb = m;
      paramInfBlocks.bitk = i22;
      paramZStream.avail_in = i21;
      paramZStream.total_in += i20 - paramZStream.next_in_index;
      paramZStream.next_in_index = i20;
      paramInfBlocks.write = i19;
      return paramInfBlocks.inflate_flush(paramZStream, 1);
      paramInfBlocks.bitb = m;
      paramInfBlocks.bitk = j;
      paramZStream.avail_in = k;
      paramZStream.total_in += i - paramZStream.next_in_index;
      paramZStream.next_in_index = i;
      paramInfBlocks.write = i1;
      return paramInfBlocks.inflate_flush(paramZStream, -3);
    }
  }
}


/* Location:           C:\Users\Administrator\Documents\ds\反编译\AndroidKiller_v1.3.1\projects\Jungledsq_ppc\ProjectSrc\smali\
 * Qualified Name:     com.jcraft.jzlib.InfCodes
 * JD-Core Version:    0.7.0.1
 */