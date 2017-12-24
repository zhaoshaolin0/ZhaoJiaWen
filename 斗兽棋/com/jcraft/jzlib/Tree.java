package com.jcraft.jzlib;

final class Tree
{
  private static final int BL_CODES = 19;
  static final int Buf_size = 16;
  static final int DIST_CODE_LEN = 512;
  private static final int D_CODES = 30;
  static final int END_BLOCK = 256;
  private static final int HEAP_SIZE = 573;
  private static final int LENGTH_CODES = 29;
  private static final int LITERALS = 256;
  private static final int L_CODES = 286;
  private static final int MAX_BITS = 15;
  static final int MAX_BL_BITS = 7;
  static final int REPZ_11_138 = 18;
  static final int REPZ_3_10 = 17;
  static final int REP_3_6 = 16;
  static final byte[] _dist_code;
  static final byte[] _length_code;
  static final int[] base_dist = { 0, 1, 2, 3, 4, 6, 8, 12, 16, 24, 32, 48, 64, 96, 128, 192, 256, 384, 512, 768, 1024, 1536, 2048, 3072, 4096, 6144, 8192, 12288, 16384, 24576 };
  static final int[] base_length;
  static final byte[] bl_order;
  static final int[] extra_blbits;
  static final int[] extra_dbits;
  static final int[] extra_lbits = { 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 2, 2, 2, 2, 3, 3, 3, 3, 4, 4, 4, 4, 5, 5, 5, 5, 0 };
  short[] dyn_tree;
  int max_code;
  StaticTree stat_desc;
  
  static
  {
    extra_dbits = new int[] { 0, 0, 0, 0, 1, 1, 2, 2, 3, 3, 4, 4, 5, 5, 6, 6, 7, 7, 8, 8, 9, 9, 10, 10, 11, 11, 12, 12, 13, 13 };
    extra_blbits = new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 3, 7 };
    bl_order = new byte[] { 16, 17, 18, 0, 8, 7, 9, 6, 10, 5, 11, 4, 12, 3, 13, 2, 14, 1, 15 };
    _dist_code = new byte[] { 0, 1, 2, 3, 4, 4, 5, 5, 6, 6, 6, 6, 7, 7, 7, 7, 8, 8, 8, 8, 8, 8, 8, 8, 9, 9, 9, 9, 9, 9, 9, 9, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 13, 13, 13, 13, 13, 13, 13, 13, 13, 13, 13, 13, 13, 13, 13, 13, 13, 13, 13, 13, 13, 13, 13, 13, 13, 13, 13, 13, 13, 13, 13, 13, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 0, 0, 16, 17, 18, 18, 19, 19, 20, 20, 20, 20, 21, 21, 21, 21, 22, 22, 22, 22, 22, 22, 22, 22, 23, 23, 23, 23, 23, 23, 23, 23, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 25, 25, 25, 25, 25, 25, 25, 25, 25, 25, 25, 25, 25, 25, 25, 25, 26, 26, 26, 26, 26, 26, 26, 26, 26, 26, 26, 26, 26, 26, 26, 26, 26, 26, 26, 26, 26, 26, 26, 26, 26, 26, 26, 26, 26, 26, 26, 26, 27, 27, 27, 27, 27, 27, 27, 27, 27, 27, 27, 27, 27, 27, 27, 27, 27, 27, 27, 27, 27, 27, 27, 27, 27, 27, 27, 27, 27, 27, 27, 27, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28, 29, 29, 29, 29, 29, 29, 29, 29, 29, 29, 29, 29, 29, 29, 29, 29, 29, 29, 29, 29, 29, 29, 29, 29, 29, 29, 29, 29, 29, 29, 29, 29, 29, 29, 29, 29, 29, 29, 29, 29, 29, 29, 29, 29, 29, 29, 29, 29, 29, 29, 29, 29, 29, 29, 29, 29, 29, 29, 29, 29, 29, 29, 29, 29 };
    _length_code = new byte[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 8, 9, 9, 10, 10, 11, 11, 12, 12, 12, 12, 13, 13, 13, 13, 14, 14, 14, 14, 15, 15, 15, 15, 16, 16, 16, 16, 16, 16, 16, 16, 17, 17, 17, 17, 17, 17, 17, 17, 18, 18, 18, 18, 18, 18, 18, 18, 19, 19, 19, 19, 19, 19, 19, 19, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 21, 21, 21, 21, 21, 21, 21, 21, 21, 21, 21, 21, 21, 21, 21, 21, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 23, 23, 23, 23, 23, 23, 23, 23, 23, 23, 23, 23, 23, 23, 23, 23, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 25, 25, 25, 25, 25, 25, 25, 25, 25, 25, 25, 25, 25, 25, 25, 25, 25, 25, 25, 25, 25, 25, 25, 25, 25, 25, 25, 25, 25, 25, 25, 25, 26, 26, 26, 26, 26, 26, 26, 26, 26, 26, 26, 26, 26, 26, 26, 26, 26, 26, 26, 26, 26, 26, 26, 26, 26, 26, 26, 26, 26, 26, 26, 26, 27, 27, 27, 27, 27, 27, 27, 27, 27, 27, 27, 27, 27, 27, 27, 27, 27, 27, 27, 27, 27, 27, 27, 27, 27, 27, 27, 27, 27, 27, 27, 28 };
    base_length = new int[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 10, 12, 14, 16, 20, 24, 28, 32, 40, 48, 56, 64, 80, 96, 112, 128, 160, 192, 224, 0 };
  }
  
  static int bi_reverse(int paramInt1, int paramInt2)
  {
    int i = 0;
    int j;
    int k;
    do
    {
      j = paramInt1;
      paramInt1 = j >>> 1;
      j = (i | j & 0x1) << 1;
      k = paramInt2 - 1;
      i = j;
      paramInt2 = k;
    } while (k > 0);
    return j >>> 1;
  }
  
  static int d_code(int paramInt)
  {
    if (paramInt < 256) {
      return _dist_code[paramInt];
    }
    return _dist_code[((paramInt >>> 7) + 256)];
  }
  
  static void gen_codes(short[] paramArrayOfShort1, int paramInt, short[] paramArrayOfShort2)
  {
    short[] arrayOfShort = new short[16];
    int k = 0;
    int j = 1;
    while (j <= 15)
    {
      int i = (short)(paramArrayOfShort2[(j - 1)] + k << 1);
      arrayOfShort[j] = i;
      j += 1;
      k = i;
    }
    j = 0;
    if (j <= paramInt)
    {
      k = paramArrayOfShort1[(j * 2 + 1)];
      if (k == 0) {}
      for (;;)
      {
        j += 1;
        break;
        int m = arrayOfShort[k];
        arrayOfShort[k] = ((short)(m + 1));
        paramArrayOfShort1[(j * 2)] = ((short)bi_reverse(m, k));
      }
    }
  }
  
  void build_tree(Deflate paramDeflate)
  {
    short[] arrayOfShort = this.dyn_tree;
    Object localObject = this.stat_desc.static_tree;
    int n = this.stat_desc.elems;
    int k = -1;
    paramDeflate.heap_len = 0;
    paramDeflate.heap_max = 573;
    int j = 0;
    int m = k;
    int[] arrayOfInt;
    if (j < n)
    {
      if (arrayOfShort[(j * 2)] != 0)
      {
        arrayOfInt = paramDeflate.heap;
        m = paramDeflate.heap_len + 1;
        paramDeflate.heap_len = m;
        k = j;
        arrayOfInt[m] = j;
        paramDeflate.depth[j] = 0;
      }
      for (;;)
      {
        j += 1;
        break;
        arrayOfShort[(j * 2 + 1)] = 0;
      }
    }
    int i1;
    if (paramDeflate.heap_len < 2)
    {
      arrayOfInt = paramDeflate.heap;
      i1 = paramDeflate.heap_len + 1;
      paramDeflate.heap_len = i1;
      if (m < 2)
      {
        j = m + 1;
        k = j;
      }
      for (;;)
      {
        arrayOfInt[i1] = k;
        arrayOfShort[(k * 2)] = 1;
        paramDeflate.depth[k] = 0;
        paramDeflate.opt_len -= 1;
        m = j;
        if (localObject == null) {
          break;
        }
        paramDeflate.static_len -= localObject[(k * 2 + 1)];
        m = j;
        break;
        k = 0;
        j = m;
      }
    }
    this.max_code = m;
    j = paramDeflate.heap_len / 2;
    while (j >= 1)
    {
      paramDeflate.pqdownheap(arrayOfShort, j);
      j -= 1;
    }
    j = n;
    for (;;)
    {
      k = paramDeflate.heap[1];
      localObject = paramDeflate.heap;
      arrayOfInt = paramDeflate.heap;
      n = paramDeflate.heap_len;
      paramDeflate.heap_len = (n - 1);
      localObject[1] = arrayOfInt[n];
      paramDeflate.pqdownheap(arrayOfShort, 1);
      n = paramDeflate.heap[1];
      localObject = paramDeflate.heap;
      i1 = paramDeflate.heap_max - 1;
      paramDeflate.heap_max = i1;
      localObject[i1] = k;
      localObject = paramDeflate.heap;
      i1 = paramDeflate.heap_max - 1;
      paramDeflate.heap_max = i1;
      localObject[i1] = n;
      arrayOfShort[(j * 2)] = ((short)(arrayOfShort[(k * 2)] + arrayOfShort[(n * 2)]));
      paramDeflate.depth[j] = ((byte)(Math.max(paramDeflate.depth[k], paramDeflate.depth[n]) + 1));
      int i = (short)j;
      arrayOfShort[(n * 2 + 1)] = i;
      arrayOfShort[(k * 2 + 1)] = i;
      paramDeflate.heap[1] = j;
      paramDeflate.pqdownheap(arrayOfShort, 1);
      if (paramDeflate.heap_len < 2)
      {
        localObject = paramDeflate.heap;
        j = paramDeflate.heap_max - 1;
        paramDeflate.heap_max = j;
        localObject[j] = paramDeflate.heap[1];
        gen_bitlen(paramDeflate);
        gen_codes(arrayOfShort, m, paramDeflate.bl_count);
        return;
      }
      j += 1;
    }
  }
  
  void gen_bitlen(Deflate paramDeflate)
  {
    short[] arrayOfShort1 = this.dyn_tree;
    Object localObject = this.stat_desc.static_tree;
    int[] arrayOfInt = this.stat_desc.extra_bits;
    int i2 = this.stat_desc.extra_base;
    int k = this.stat_desc.max_length;
    int j = 0;
    int i = 0;
    while (i <= 15)
    {
      paramDeflate.bl_count[i] = 0;
      i += 1;
    }
    arrayOfShort1[(paramDeflate.heap[paramDeflate.heap_max] * 2 + 1)] = 0;
    i = paramDeflate.heap_max + 1;
    int n;
    if (i < 573)
    {
      int i3 = paramDeflate.heap[i];
      int i1 = arrayOfShort1[(arrayOfShort1[(i3 * 2 + 1)] * 2 + 1)] + 1;
      n = i1;
      m = j;
      if (i1 > k)
      {
        n = k;
        m = j + 1;
      }
      arrayOfShort1[(i3 * 2 + 1)] = ((short)n);
      if (i3 > this.max_code) {}
      for (;;)
      {
        i += 1;
        j = m;
        break;
        short[] arrayOfShort2 = paramDeflate.bl_count;
        arrayOfShort2[n] = ((short)(arrayOfShort2[n] + 1));
        j = 0;
        if (i3 >= i2) {
          j = arrayOfInt[(i3 - i2)];
        }
        i1 = arrayOfShort1[(i3 * 2)];
        paramDeflate.opt_len += (n + j) * i1;
        if (localObject != null) {
          paramDeflate.static_len += (localObject[(i3 * 2 + 1)] + j) * i1;
        }
      }
    }
    int m = j;
    if (j == 0) {}
    for (;;)
    {
      return;
      do
      {
        j = k - 1;
        while (paramDeflate.bl_count[j] == 0) {
          j -= 1;
        }
        localObject = paramDeflate.bl_count;
        localObject[j] = ((short)(localObject[j] - 1));
        localObject = paramDeflate.bl_count;
        j += 1;
        localObject[j] = ((short)(localObject[j] + 2));
        localObject = paramDeflate.bl_count;
        localObject[k] = ((short)(localObject[k] - 1));
        j = m - 2;
        m = j;
      } while (j > 0);
      j = i;
      i = k;
      while (i != 0)
      {
        k = paramDeflate.bl_count[i];
        while (k != 0)
        {
          localObject = paramDeflate.heap;
          m = j - 1;
          n = localObject[m];
          j = m;
          if (n <= this.max_code)
          {
            if (arrayOfShort1[(n * 2 + 1)] != i)
            {
              paramDeflate.opt_len = ((int)(paramDeflate.opt_len + (i - arrayOfShort1[(n * 2 + 1)]) * arrayOfShort1[(n * 2)]));
              arrayOfShort1[(n * 2 + 1)] = ((short)i);
            }
            k -= 1;
            j = m;
          }
        }
        i -= 1;
      }
    }
  }
}


/* Location:           C:\Users\Administrator\Documents\ds\反编译\AndroidKiller_v1.3.1\projects\Jungledsq_ppc\ProjectSrc\smali\
 * Qualified Name:     com.jcraft.jzlib.Tree
 * JD-Core Version:    0.7.0.1
 */