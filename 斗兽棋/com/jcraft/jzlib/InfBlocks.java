package com.jcraft.jzlib;

final class InfBlocks
{
  private static final int BAD = 9;
  private static final int BTREE = 4;
  private static final int CODES = 6;
  private static final int DONE = 8;
  private static final int DRY = 7;
  private static final int DTREE = 5;
  private static final int LENS = 1;
  private static final int MANY = 1440;
  private static final int STORED = 2;
  private static final int TABLE = 3;
  private static final int TYPE = 0;
  private static final int Z_BUF_ERROR = -5;
  private static final int Z_DATA_ERROR = -3;
  private static final int Z_ERRNO = -1;
  private static final int Z_MEM_ERROR = -4;
  private static final int Z_NEED_DICT = 2;
  private static final int Z_OK = 0;
  private static final int Z_STREAM_END = 1;
  private static final int Z_STREAM_ERROR = -2;
  private static final int Z_VERSION_ERROR = -6;
  static final int[] border = { 16, 17, 18, 0, 8, 7, 9, 6, 10, 5, 11, 4, 12, 3, 13, 2, 14, 1, 15 };
  private static final int[] inflate_mask = { 0, 1, 3, 7, 15, 31, 63, 127, 255, 511, 1023, 2047, 4095, 8191, 16383, 32767, 65535 };
  int[] bb = new int[1];
  int bitb;
  int bitk;
  int[] blens;
  long check;
  Object checkfn;
  InfCodes codes = new InfCodes();
  int end;
  int[] hufts = new int[4320];
  int index;
  InfTree inftree = new InfTree();
  int last;
  int left;
  int mode;
  int read;
  int table;
  int[] tb = new int[1];
  byte[] window;
  int write;
  
  InfBlocks(ZStream paramZStream, Object paramObject, int paramInt)
  {
    this.window = new byte[paramInt];
    this.end = paramInt;
    this.checkfn = paramObject;
    this.mode = 0;
    reset(paramZStream, null);
  }
  
  void free(ZStream paramZStream)
  {
    reset(paramZStream, null);
    this.window = null;
    this.hufts = null;
  }
  
  int inflate_flush(ZStream paramZStream, int paramInt)
  {
    int m = paramZStream.next_out_index;
    int k = this.read;
    if (k <= this.write) {}
    for (int i = this.write;; i = this.end)
    {
      i -= k;
      int j = i;
      if (i > paramZStream.avail_out) {
        j = paramZStream.avail_out;
      }
      i = paramInt;
      if (j != 0)
      {
        i = paramInt;
        if (paramInt == -5) {
          i = 0;
        }
      }
      paramZStream.avail_out -= j;
      paramZStream.total_out += j;
      long l;
      if (this.checkfn != null)
      {
        l = paramZStream._adler.adler32(this.check, this.window, k, j);
        this.check = l;
        paramZStream.adler = l;
      }
      System.arraycopy(this.window, k, paramZStream.next_out, m, j);
      m += j;
      int n = k + j;
      k = n;
      j = m;
      paramInt = i;
      if (n == this.end)
      {
        if (this.write == this.end) {
          this.write = 0;
        }
        paramInt = this.write - 0;
        j = paramInt;
        if (paramInt > paramZStream.avail_out) {
          j = paramZStream.avail_out;
        }
        paramInt = i;
        if (j != 0)
        {
          paramInt = i;
          if (i == -5) {
            paramInt = 0;
          }
        }
        paramZStream.avail_out -= j;
        paramZStream.total_out += j;
        if (this.checkfn != null)
        {
          l = paramZStream._adler.adler32(this.check, this.window, 0, j);
          this.check = l;
          paramZStream.adler = l;
        }
        System.arraycopy(this.window, 0, paramZStream.next_out, m, j);
        i = m + j;
        k = 0 + j;
        j = i;
      }
      paramZStream.next_out_index = j;
      this.read = k;
      return paramInt;
    }
  }
  
  int proc(ZStream paramZStream, int paramInt)
  {
    int i = paramZStream.next_in_index;
    int m = paramZStream.avail_in;
    int j = this.bitb;
    int k = this.bitk;
    int n = this.write;
    if (n < this.read) {}
    int i6;
    int i7;
    int i10;
    int i8;
    int i11;
    int i2;
    int i3;
    int i5;
    int i4;
    int i9;
    int i18;
    int i19;
    int i20;
    int i21;
    int i22;
    int i15;
    int i14;
    int i13;
    int i12;
    int i17;
    int i16;
    int i23;
    int i24;
    int i25;
    int i26;
    int i27;
    for (int i1 = this.read - n - 1;; i1 = this.end - n)
    {
      i6 = j;
      i7 = k;
      i10 = m;
      i8 = i;
      i11 = paramInt;
      i2 = j;
      i3 = k;
      i5 = m;
      i4 = i;
      i9 = paramInt;
      i18 = j;
      i19 = k;
      i20 = m;
      i21 = i;
      i22 = paramInt;
      i15 = j;
      i14 = k;
      i13 = m;
      i12 = i;
      i17 = n;
      i16 = paramInt;
      i23 = j;
      i24 = k;
      i25 = m;
      i26 = i;
      i27 = n;
      switch (this.mode)
      {
      default: 
        this.bitb = j;
        this.bitk = k;
        paramZStream.avail_in = m;
        paramZStream.total_in += i - paramZStream.next_in_index;
        paramZStream.next_in_index = i;
        this.write = n;
        return inflate_flush(paramZStream, -2);
      }
    }
    for (;;)
    {
      if (k < 3)
      {
        if (m != 0)
        {
          paramInt = 0;
          m -= 1;
          j |= (paramZStream.next_in[i] & 0xFF) << k;
          k += 8;
          i += 1;
        }
        else
        {
          this.bitb = j;
          this.bitk = k;
          paramZStream.avail_in = m;
          paramZStream.total_in += i - paramZStream.next_in_index;
          paramZStream.next_in_index = i;
          this.write = n;
          return inflate_flush(paramZStream, paramInt);
        }
      }
      else
      {
        i2 = j & 0x7;
        this.last = (i2 & 0x1);
        int[] arrayOfInt1;
        int[] arrayOfInt2;
        Object localObject1;
        Object localObject2;
        switch (i2 >>> 1)
        {
        default: 
        case 0: 
        case 1: 
        case 2: 
          for (;;)
          {
            break;
            k -= 3;
            i2 = k & 0x7;
            j = j >>> 3 >>> i2;
            k -= i2;
            this.mode = 1;
            continue;
            arrayOfInt1 = new int[1];
            arrayOfInt2 = new int[1];
            localObject1 = new int[1][];
            localObject2 = new int[1][];
            InfTree.inflate_trees_fixed(arrayOfInt1, arrayOfInt2, (int[][])localObject1, (int[][])localObject2, paramZStream);
            this.codes.init(arrayOfInt1[0], arrayOfInt2[0], localObject1[0], 0, localObject2[0], 0, paramZStream);
            j >>>= 3;
            k -= 3;
            this.mode = 6;
            continue;
            j >>>= 3;
            k -= 3;
            this.mode = 3;
          }
        }
        this.mode = 9;
        paramZStream.msg = "invalid block type";
        this.bitb = (j >>> 3);
        this.bitk = (k - 3);
        paramZStream.avail_in = m;
        paramZStream.total_in += i - paramZStream.next_in_index;
        paramZStream.next_in_index = i;
        this.write = n;
        return inflate_flush(paramZStream, -3);
        for (;;)
        {
          if (k < 32)
          {
            if (m != 0)
            {
              i2 = 0;
              m -= 1;
              j |= (paramZStream.next_in[i] & 0xFF) << k;
              k += 8;
              i += 1;
            }
            else
            {
              this.bitb = j;
              this.bitk = k;
              paramZStream.avail_in = m;
              paramZStream.total_in += i - paramZStream.next_in_index;
              paramZStream.next_in_index = i;
              this.write = n;
              return inflate_flush(paramZStream, i2);
            }
          }
          else
          {
            if (((j ^ 0xFFFFFFFF) >>> 16 & 0xFFFF) != (0xFFFF & j))
            {
              this.mode = 9;
              paramZStream.msg = "invalid stored block lengths";
              this.bitb = j;
              this.bitk = k;
              paramZStream.avail_in = m;
              paramZStream.total_in += i - paramZStream.next_in_index;
              paramZStream.next_in_index = i;
              this.write = n;
              return inflate_flush(paramZStream, -3);
            }
            this.left = (0xFFFF & j);
            k = 0;
            j = 0;
            if (this.left != 0) {
              paramInt = 2;
            }
            for (;;)
            {
              this.mode = paramInt;
              paramInt = i2;
              break;
              if (this.last != 0) {
                paramInt = 7;
              } else {
                paramInt = 0;
              }
            }
            if (m == 0)
            {
              this.bitb = j;
              this.bitk = k;
              paramZStream.avail_in = m;
              paramZStream.total_in += i - paramZStream.next_in_index;
              paramZStream.next_in_index = i;
              this.write = n;
              return inflate_flush(paramZStream, paramInt);
            }
            i2 = i1;
            i4 = n;
            if (i1 == 0)
            {
              i3 = i1;
              i5 = n;
              if (n == this.end)
              {
                i3 = i1;
                i5 = n;
                if (this.read != 0)
                {
                  i5 = 0;
                  if (this.read >= 0) {
                    break label1161;
                  }
                  i3 = this.read - 0 - 1;
                }
              }
              i2 = i3;
              i4 = i5;
              if (i3 == 0)
              {
                this.write = i5;
                i3 = inflate_flush(paramZStream, paramInt);
                i2 = this.write;
                if (i2 < this.read)
                {
                  i1 = this.read - i2 - 1;
                  label1048:
                  paramInt = i1;
                  n = i2;
                  if (i2 == this.end)
                  {
                    paramInt = i1;
                    n = i2;
                    if (this.read != 0)
                    {
                      n = 0;
                      if (this.read >= 0) {
                        break label1184;
                      }
                    }
                  }
                }
                label1161:
                label1184:
                for (paramInt = this.read - 0 - 1;; paramInt = this.end - 0)
                {
                  i2 = paramInt;
                  i4 = n;
                  if (paramInt != 0) {
                    break label1194;
                  }
                  this.bitb = j;
                  this.bitk = k;
                  paramZStream.avail_in = m;
                  paramZStream.total_in += i - paramZStream.next_in_index;
                  paramZStream.next_in_index = i;
                  this.write = n;
                  return inflate_flush(paramZStream, i3);
                  i3 = this.end - 0;
                  break;
                  i1 = this.end - i2;
                  break label1048;
                }
              }
            }
            label1194:
            i3 = 0;
            n = this.left;
            paramInt = n;
            if (n > m) {
              paramInt = m;
            }
            n = paramInt;
            if (paramInt > i2) {
              n = i2;
            }
            System.arraycopy(paramZStream.next_in, i, this.window, i4, n);
            i5 = i + n;
            i6 = m - n;
            i4 += n;
            i2 -= n;
            i7 = this.left - n;
            this.left = i7;
            i1 = i2;
            m = i6;
            i = i5;
            n = i4;
            paramInt = i3;
            if (i7 != 0) {
              break;
            }
            if (this.last != 0) {}
            for (paramInt = 7;; paramInt = 0)
            {
              this.mode = paramInt;
              i1 = i2;
              m = i6;
              i = i5;
              n = i4;
              paramInt = i3;
              break;
            }
            for (;;)
            {
              if (k < 14)
              {
                if (m != 0)
                {
                  paramInt = 0;
                  m -= 1;
                  j |= (paramZStream.next_in[i] & 0xFF) << k;
                  k += 8;
                  i += 1;
                }
                else
                {
                  this.bitb = j;
                  this.bitk = k;
                  paramZStream.avail_in = m;
                  paramZStream.total_in += i - paramZStream.next_in_index;
                  paramZStream.next_in_index = i;
                  this.write = n;
                  return inflate_flush(paramZStream, paramInt);
                }
              }
              else
              {
                i1 = j & 0x3FFF;
                this.table = i1;
                if (((i1 & 0x1F) > 29) || ((i1 >> 5 & 0x1F) > 29))
                {
                  this.mode = 9;
                  paramZStream.msg = "too many length or distance symbols";
                  this.bitb = j;
                  this.bitk = k;
                  paramZStream.avail_in = m;
                  paramZStream.total_in += i - paramZStream.next_in_index;
                  paramZStream.next_in_index = i;
                  this.write = n;
                  return inflate_flush(paramZStream, -3);
                }
                i2 = (i1 & 0x1F) + 258 + (i1 >> 5 & 0x1F);
                if ((this.blens == null) || (this.blens.length < i2))
                {
                  this.blens = new int[i2];
                  i6 = j >>> 14;
                  i7 = k - 14;
                  this.index = 0;
                  this.mode = 4;
                  i11 = paramInt;
                  i8 = i;
                  i10 = m;
                }
                for (;;)
                {
                  if (this.index >= (this.table >>> 10) + 4) {
                    break label1838;
                  }
                  paramInt = i8;
                  for (;;)
                  {
                    if (i7 < 3)
                    {
                      if (i10 != 0)
                      {
                        i11 = 0;
                        i10 -= 1;
                        i6 |= (paramZStream.next_in[paramInt] & 0xFF) << i7;
                        i7 += 8;
                        paramInt += 1;
                        continue;
                        i1 = 0;
                        while (i1 < i2)
                        {
                          this.blens[i1] = 0;
                          i1 += 1;
                        }
                        break;
                      }
                      this.bitb = i6;
                      this.bitk = i7;
                      paramZStream.avail_in = i10;
                      paramZStream.total_in += paramInt - paramZStream.next_in_index;
                      paramZStream.next_in_index = paramInt;
                      this.write = n;
                      return inflate_flush(paramZStream, i11);
                    }
                  }
                  arrayOfInt1 = this.blens;
                  arrayOfInt2 = border;
                  i = this.index;
                  this.index = (i + 1);
                  arrayOfInt1[arrayOfInt2[i]] = (i6 & 0x7);
                  i6 >>>= 3;
                  i7 -= 3;
                  i8 = paramInt;
                }
                label1838:
                while (this.index < 19)
                {
                  arrayOfInt1 = this.blens;
                  arrayOfInt2 = border;
                  paramInt = this.index;
                  this.index = (paramInt + 1);
                  arrayOfInt1[arrayOfInt2[paramInt]] = 0;
                }
                this.bb[0] = 7;
                paramInt = this.inftree.inflate_trees_bits(this.blens, this.bb, this.tb, this.hufts, paramZStream);
                if (paramInt != 0)
                {
                  if (paramInt == -3)
                  {
                    this.blens = null;
                    this.mode = 9;
                  }
                  this.bitb = i6;
                  this.bitk = i7;
                  paramZStream.avail_in = i10;
                  paramZStream.total_in += i8 - paramZStream.next_in_index;
                  paramZStream.next_in_index = i8;
                  this.write = n;
                  return inflate_flush(paramZStream, paramInt);
                }
                this.index = 0;
                this.mode = 5;
                i9 = i11;
                i4 = i8;
                i5 = i10;
                i3 = i7;
                i2 = i6;
                for (;;)
                {
                  label2019:
                  paramInt = this.table;
                  if (this.index >= (paramInt & 0x1F) + 258 + (paramInt >> 5 & 0x1F))
                  {
                    this.tb[0] = -1;
                    arrayOfInt1 = new int[1];
                    arrayOfInt2 = new int[1];
                    localObject1 = new int[1];
                    localObject2 = new int[1];
                    arrayOfInt1[0] = 9;
                    arrayOfInt2[0] = 6;
                    paramInt = this.table;
                    paramInt = this.inftree.inflate_trees_dynamic((paramInt & 0x1F) + 257, (paramInt >> 5 & 0x1F) + 1, this.blens, arrayOfInt1, arrayOfInt2, (int[])localObject1, (int[])localObject2, this.hufts, paramZStream);
                    if (paramInt == 0) {
                      break label2794;
                    }
                    if (paramInt == -3)
                    {
                      this.blens = null;
                      this.mode = 9;
                    }
                    this.bitb = i2;
                    this.bitk = i3;
                    paramZStream.avail_in = i5;
                    paramZStream.total_in += i4 - paramZStream.next_in_index;
                    paramZStream.next_in_index = i4;
                    this.write = n;
                    return inflate_flush(paramZStream, paramInt);
                  }
                  i = this.bb[0];
                  paramInt = i4;
                  while (i3 < i) {
                    if (i5 != 0)
                    {
                      i9 = 0;
                      i5 -= 1;
                      i2 |= (paramZStream.next_in[paramInt] & 0xFF) << i3;
                      i3 += 8;
                      paramInt += 1;
                    }
                    else
                    {
                      this.bitb = i2;
                      this.bitk = i3;
                      paramZStream.avail_in = i5;
                      paramZStream.total_in += paramInt - paramZStream.next_in_index;
                      paramZStream.next_in_index = paramInt;
                      this.write = n;
                      return inflate_flush(paramZStream, i9);
                    }
                  }
                  if (this.tb[0] == -1) {}
                  i1 = this.hufts[((this.tb[0] + (inflate_mask[i] & i2)) * 3 + 1)];
                  m = this.hufts[((this.tb[0] + (inflate_mask[i1] & i2)) * 3 + 2)];
                  if (m >= 16) {
                    break;
                  }
                  i2 >>>= i1;
                  i3 -= i1;
                  arrayOfInt1 = this.blens;
                  i = this.index;
                  this.index = (i + 1);
                  arrayOfInt1[i] = m;
                  i4 = paramInt;
                }
                if (m == 18)
                {
                  i = 7;
                  if (m != 18) {
                    break label2520;
                  }
                  j = 11;
                }
                for (;;)
                {
                  if (i3 < i1 + i)
                  {
                    if (i5 != 0)
                    {
                      i9 = 0;
                      i5 -= 1;
                      i2 |= (paramZStream.next_in[paramInt] & 0xFF) << i3;
                      i3 += 8;
                      paramInt += 1;
                      continue;
                      i = m - 14;
                      break;
                      label2520:
                      j = 3;
                      continue;
                    }
                    this.bitb = i2;
                    this.bitk = i3;
                    paramZStream.avail_in = i5;
                    paramZStream.total_in += paramInt - paramZStream.next_in_index;
                    paramZStream.next_in_index = paramInt;
                    this.write = n;
                    return inflate_flush(paramZStream, i9);
                  }
                }
                i2 >>>= i1;
                k = j + (inflate_mask[i] & i2);
                i2 >>>= i;
                i3 = i3 - i1 - i;
                j = this.index;
                i = this.table;
                if ((j + k > (i & 0x1F) + 258 + (i >> 5 & 0x1F)) || ((m == 16) && (j < 1)))
                {
                  this.blens = null;
                  this.mode = 9;
                  paramZStream.msg = "invalid bit length repeat";
                  this.bitb = i2;
                  this.bitk = i3;
                  paramZStream.avail_in = i5;
                  paramZStream.total_in += paramInt - paramZStream.next_in_index;
                  paramZStream.next_in_index = paramInt;
                  this.write = n;
                  return inflate_flush(paramZStream, -3);
                }
                if (m == 16) {
                  i = this.blens[(j - 1)];
                }
                for (;;)
                {
                  arrayOfInt1 = this.blens;
                  m = j + 1;
                  arrayOfInt1[j] = i;
                  k -= 1;
                  if (k == 0)
                  {
                    this.index = m;
                    i4 = paramInt;
                    break label2019;
                    i = 0;
                    continue;
                    label2794:
                    this.codes.init(arrayOfInt1[0], arrayOfInt2[0], this.hufts, localObject1[0], this.hufts, localObject2[0], paramZStream);
                    this.mode = 6;
                    i22 = i9;
                    i21 = i4;
                    i20 = i5;
                    i19 = i3;
                    i18 = i2;
                    this.bitb = i18;
                    this.bitk = i19;
                    paramZStream.avail_in = i20;
                    paramZStream.total_in += i21 - paramZStream.next_in_index;
                    paramZStream.next_in_index = i21;
                    this.write = n;
                    paramInt = this.codes.proc(this, paramZStream, i22);
                    if (paramInt != 1) {
                      return inflate_flush(paramZStream, paramInt);
                    }
                    i16 = 0;
                    paramInt = 0;
                    this.codes.free(paramZStream);
                    i = paramZStream.next_in_index;
                    m = paramZStream.avail_in;
                    j = this.bitb;
                    k = this.bitk;
                    n = this.write;
                    if (n < this.read) {}
                    for (i1 = this.read - n - 1;; i1 = this.end - n)
                    {
                      if (this.last != 0) {
                        break label3012;
                      }
                      this.mode = 0;
                      break;
                    }
                    label3012:
                    this.mode = 7;
                    i17 = n;
                    i12 = i;
                    i13 = m;
                    i14 = k;
                    i15 = j;
                    this.write = i17;
                    paramInt = inflate_flush(paramZStream, i16);
                    i27 = this.write;
                    if (i27 < this.read) {}
                    for (i = this.read; this.read != this.write; i = this.end)
                    {
                      this.bitb = i15;
                      this.bitk = i14;
                      paramZStream.avail_in = i13;
                      paramZStream.total_in += i12 - paramZStream.next_in_index;
                      paramZStream.next_in_index = i12;
                      this.write = i27;
                      return inflate_flush(paramZStream, paramInt);
                    }
                    this.mode = 8;
                    i26 = i12;
                    i25 = i13;
                    i24 = i14;
                    i23 = i15;
                    this.bitb = i23;
                    this.bitk = i24;
                    paramZStream.avail_in = i25;
                    paramZStream.total_in += i26 - paramZStream.next_in_index;
                    paramZStream.next_in_index = i26;
                    this.write = i27;
                    return inflate_flush(paramZStream, 1);
                    this.bitb = j;
                    this.bitk = k;
                    paramZStream.avail_in = m;
                    paramZStream.total_in += i - paramZStream.next_in_index;
                    paramZStream.next_in_index = i;
                    this.write = n;
                    return inflate_flush(paramZStream, -3);
                  }
                  j = m;
                }
              }
            }
            i2 = paramInt;
          }
        }
      }
    }
  }
  
  void reset(ZStream paramZStream, long[] paramArrayOfLong)
  {
    if (paramArrayOfLong != null) {
      paramArrayOfLong[0] = this.check;
    }
    if (((this.mode == 4) || (this.mode != 5)) || (this.mode == 6)) {
      this.codes.free(paramZStream);
    }
    this.mode = 0;
    this.bitk = 0;
    this.bitb = 0;
    this.write = 0;
    this.read = 0;
    if (this.checkfn != null)
    {
      long l = paramZStream._adler.adler32(0L, null, 0, 0);
      this.check = l;
      paramZStream.adler = l;
    }
  }
  
  void set_dictionary(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
  {
    System.arraycopy(paramArrayOfByte, paramInt1, this.window, 0, paramInt2);
    this.write = paramInt2;
    this.read = paramInt2;
  }
  
  int sync_point()
  {
    if (this.mode == 1) {
      return 1;
    }
    return 0;
  }
}


/* Location:           C:\Users\Administrator\Documents\ds\反编译\AndroidKiller_v1.3.1\projects\Jungledsq_ppc\ProjectSrc\smali\
 * Qualified Name:     com.jcraft.jzlib.InfBlocks
 * JD-Core Version:    0.7.0.1
 */