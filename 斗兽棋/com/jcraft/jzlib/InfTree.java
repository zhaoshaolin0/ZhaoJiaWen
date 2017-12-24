package com.jcraft.jzlib;

final class InfTree
{
  static final int BMAX = 15;
  private static final int MANY = 1440;
  private static final int Z_BUF_ERROR = -5;
  private static final int Z_DATA_ERROR = -3;
  private static final int Z_ERRNO = -1;
  private static final int Z_MEM_ERROR = -4;
  private static final int Z_NEED_DICT = 2;
  private static final int Z_OK = 0;
  private static final int Z_STREAM_END = 1;
  private static final int Z_STREAM_ERROR = -2;
  private static final int Z_VERSION_ERROR = -6;
  static final int[] cpdext = { 0, 0, 0, 0, 1, 1, 2, 2, 3, 3, 4, 4, 5, 5, 6, 6, 7, 7, 8, 8, 9, 9, 10, 10, 11, 11, 12, 12, 13, 13 };
  static final int[] cpdist;
  static final int[] cplens;
  static final int[] cplext;
  static final int fixed_bd = 5;
  static final int fixed_bl = 9;
  static final int[] fixed_td;
  static final int[] fixed_tl = { 96, 7, 256, 0, 8, 80, 0, 8, 16, 84, 8, 115, 82, 7, 31, 0, 8, 112, 0, 8, 48, 0, 9, 192, 80, 7, 10, 0, 8, 96, 0, 8, 32, 0, 9, 160, 0, 8, 0, 0, 8, 128, 0, 8, 64, 0, 9, 224, 80, 7, 6, 0, 8, 88, 0, 8, 24, 0, 9, 144, 83, 7, 59, 0, 8, 120, 0, 8, 56, 0, 9, 208, 81, 7, 17, 0, 8, 104, 0, 8, 40, 0, 9, 176, 0, 8, 8, 0, 8, 136, 0, 8, 72, 0, 9, 240, 80, 7, 4, 0, 8, 84, 0, 8, 20, 85, 8, 227, 83, 7, 43, 0, 8, 116, 0, 8, 52, 0, 9, 200, 81, 7, 13, 0, 8, 100, 0, 8, 36, 0, 9, 168, 0, 8, 4, 0, 8, 132, 0, 8, 68, 0, 9, 232, 80, 7, 8, 0, 8, 92, 0, 8, 28, 0, 9, 152, 84, 7, 83, 0, 8, 124, 0, 8, 60, 0, 9, 216, 82, 7, 23, 0, 8, 108, 0, 8, 44, 0, 9, 184, 0, 8, 12, 0, 8, 140, 0, 8, 76, 0, 9, 248, 80, 7, 3, 0, 8, 82, 0, 8, 18, 85, 8, 163, 83, 7, 35, 0, 8, 114, 0, 8, 50, 0, 9, 196, 81, 7, 11, 0, 8, 98, 0, 8, 34, 0, 9, 164, 0, 8, 2, 0, 8, 130, 0, 8, 66, 0, 9, 228, 80, 7, 7, 0, 8, 90, 0, 8, 26, 0, 9, 148, 84, 7, 67, 0, 8, 122, 0, 8, 58, 0, 9, 212, 82, 7, 19, 0, 8, 106, 0, 8, 42, 0, 9, 180, 0, 8, 10, 0, 8, 138, 0, 8, 74, 0, 9, 244, 80, 7, 5, 0, 8, 86, 0, 8, 22, 192, 8, 0, 83, 7, 51, 0, 8, 118, 0, 8, 54, 0, 9, 204, 81, 7, 15, 0, 8, 102, 0, 8, 38, 0, 9, 172, 0, 8, 6, 0, 8, 134, 0, 8, 70, 0, 9, 236, 80, 7, 9, 0, 8, 94, 0, 8, 30, 0, 9, 156, 84, 7, 99, 0, 8, 126, 0, 8, 62, 0, 9, 220, 82, 7, 27, 0, 8, 110, 0, 8, 46, 0, 9, 188, 0, 8, 14, 0, 8, 142, 0, 8, 78, 0, 9, 252, 96, 7, 256, 0, 8, 81, 0, 8, 17, 85, 8, 131, 82, 7, 31, 0, 8, 113, 0, 8, 49, 0, 9, 194, 80, 7, 10, 0, 8, 97, 0, 8, 33, 0, 9, 162, 0, 8, 1, 0, 8, 129, 0, 8, 65, 0, 9, 226, 80, 7, 6, 0, 8, 89, 0, 8, 25, 0, 9, 146, 83, 7, 59, 0, 8, 121, 0, 8, 57, 0, 9, 210, 81, 7, 17, 0, 8, 105, 0, 8, 41, 0, 9, 178, 0, 8, 9, 0, 8, 137, 0, 8, 73, 0, 9, 242, 80, 7, 4, 0, 8, 85, 0, 8, 21, 80, 8, 258, 83, 7, 43, 0, 8, 117, 0, 8, 53, 0, 9, 202, 81, 7, 13, 0, 8, 101, 0, 8, 37, 0, 9, 170, 0, 8, 5, 0, 8, 133, 0, 8, 69, 0, 9, 234, 80, 7, 8, 0, 8, 93, 0, 8, 29, 0, 9, 154, 84, 7, 83, 0, 8, 125, 0, 8, 61, 0, 9, 218, 82, 7, 23, 0, 8, 109, 0, 8, 45, 0, 9, 186, 0, 8, 13, 0, 8, 141, 0, 8, 77, 0, 9, 250, 80, 7, 3, 0, 8, 83, 0, 8, 19, 85, 8, 195, 83, 7, 35, 0, 8, 115, 0, 8, 51, 0, 9, 198, 81, 7, 11, 0, 8, 99, 0, 8, 35, 0, 9, 166, 0, 8, 3, 0, 8, 131, 0, 8, 67, 0, 9, 230, 80, 7, 7, 0, 8, 91, 0, 8, 27, 0, 9, 150, 84, 7, 67, 0, 8, 123, 0, 8, 59, 0, 9, 214, 82, 7, 19, 0, 8, 107, 0, 8, 43, 0, 9, 182, 0, 8, 11, 0, 8, 139, 0, 8, 75, 0, 9, 246, 80, 7, 5, 0, 8, 87, 0, 8, 23, 192, 8, 0, 83, 7, 51, 0, 8, 119, 0, 8, 55, 0, 9, 206, 81, 7, 15, 0, 8, 103, 0, 8, 39, 0, 9, 174, 0, 8, 7, 0, 8, 135, 0, 8, 71, 0, 9, 238, 80, 7, 9, 0, 8, 95, 0, 8, 31, 0, 9, 158, 84, 7, 99, 0, 8, 127, 0, 8, 63, 0, 9, 222, 82, 7, 27, 0, 8, 111, 0, 8, 47, 0, 9, 190, 0, 8, 15, 0, 8, 143, 0, 8, 79, 0, 9, 254, 96, 7, 256, 0, 8, 80, 0, 8, 16, 84, 8, 115, 82, 7, 31, 0, 8, 112, 0, 8, 48, 0, 9, 193, 80, 7, 10, 0, 8, 96, 0, 8, 32, 0, 9, 161, 0, 8, 0, 0, 8, 128, 0, 8, 64, 0, 9, 225, 80, 7, 6, 0, 8, 88, 0, 8, 24, 0, 9, 145, 83, 7, 59, 0, 8, 120, 0, 8, 56, 0, 9, 209, 81, 7, 17, 0, 8, 104, 0, 8, 40, 0, 9, 177, 0, 8, 8, 0, 8, 136, 0, 8, 72, 0, 9, 241, 80, 7, 4, 0, 8, 84, 0, 8, 20, 85, 8, 227, 83, 7, 43, 0, 8, 116, 0, 8, 52, 0, 9, 201, 81, 7, 13, 0, 8, 100, 0, 8, 36, 0, 9, 169, 0, 8, 4, 0, 8, 132, 0, 8, 68, 0, 9, 233, 80, 7, 8, 0, 8, 92, 0, 8, 28, 0, 9, 153, 84, 7, 83, 0, 8, 124, 0, 8, 60, 0, 9, 217, 82, 7, 23, 0, 8, 108, 0, 8, 44, 0, 9, 185, 0, 8, 12, 0, 8, 140, 0, 8, 76, 0, 9, 249, 80, 7, 3, 0, 8, 82, 0, 8, 18, 85, 8, 163, 83, 7, 35, 0, 8, 114, 0, 8, 50, 0, 9, 197, 81, 7, 11, 0, 8, 98, 0, 8, 34, 0, 9, 165, 0, 8, 2, 0, 8, 130, 0, 8, 66, 0, 9, 229, 80, 7, 7, 0, 8, 90, 0, 8, 26, 0, 9, 149, 84, 7, 67, 0, 8, 122, 0, 8, 58, 0, 9, 213, 82, 7, 19, 0, 8, 106, 0, 8, 42, 0, 9, 181, 0, 8, 10, 0, 8, 138, 0, 8, 74, 0, 9, 245, 80, 7, 5, 0, 8, 86, 0, 8, 22, 192, 8, 0, 83, 7, 51, 0, 8, 118, 0, 8, 54, 0, 9, 205, 81, 7, 15, 0, 8, 102, 0, 8, 38, 0, 9, 173, 0, 8, 6, 0, 8, 134, 0, 8, 70, 0, 9, 237, 80, 7, 9, 0, 8, 94, 0, 8, 30, 0, 9, 157, 84, 7, 99, 0, 8, 126, 0, 8, 62, 0, 9, 221, 82, 7, 27, 0, 8, 110, 0, 8, 46, 0, 9, 189, 0, 8, 14, 0, 8, 142, 0, 8, 78, 0, 9, 253, 96, 7, 256, 0, 8, 81, 0, 8, 17, 85, 8, 131, 82, 7, 31, 0, 8, 113, 0, 8, 49, 0, 9, 195, 80, 7, 10, 0, 8, 97, 0, 8, 33, 0, 9, 163, 0, 8, 1, 0, 8, 129, 0, 8, 65, 0, 9, 227, 80, 7, 6, 0, 8, 89, 0, 8, 25, 0, 9, 147, 83, 7, 59, 0, 8, 121, 0, 8, 57, 0, 9, 211, 81, 7, 17, 0, 8, 105, 0, 8, 41, 0, 9, 179, 0, 8, 9, 0, 8, 137, 0, 8, 73, 0, 9, 243, 80, 7, 4, 0, 8, 85, 0, 8, 21, 80, 8, 258, 83, 7, 43, 0, 8, 117, 0, 8, 53, 0, 9, 203, 81, 7, 13, 0, 8, 101, 0, 8, 37, 0, 9, 171, 0, 8, 5, 0, 8, 133, 0, 8, 69, 0, 9, 235, 80, 7, 8, 0, 8, 93, 0, 8, 29, 0, 9, 155, 84, 7, 83, 0, 8, 125, 0, 8, 61, 0, 9, 219, 82, 7, 23, 0, 8, 109, 0, 8, 45, 0, 9, 187, 0, 8, 13, 0, 8, 141, 0, 8, 77, 0, 9, 251, 80, 7, 3, 0, 8, 83, 0, 8, 19, 85, 8, 195, 83, 7, 35, 0, 8, 115, 0, 8, 51, 0, 9, 199, 81, 7, 11, 0, 8, 99, 0, 8, 35, 0, 9, 167, 0, 8, 3, 0, 8, 131, 0, 8, 67, 0, 9, 231, 80, 7, 7, 0, 8, 91, 0, 8, 27, 0, 9, 151, 84, 7, 67, 0, 8, 123, 0, 8, 59, 0, 9, 215, 82, 7, 19, 0, 8, 107, 0, 8, 43, 0, 9, 183, 0, 8, 11, 0, 8, 139, 0, 8, 75, 0, 9, 247, 80, 7, 5, 0, 8, 87, 0, 8, 23, 192, 8, 0, 83, 7, 51, 0, 8, 119, 0, 8, 55, 0, 9, 207, 81, 7, 15, 0, 8, 103, 0, 8, 39, 0, 9, 175, 0, 8, 7, 0, 8, 135, 0, 8, 71, 0, 9, 239, 80, 7, 9, 0, 8, 95, 0, 8, 31, 0, 9, 159, 84, 7, 99, 0, 8, 127, 0, 8, 63, 0, 9, 223, 82, 7, 27, 0, 8, 111, 0, 8, 47, 0, 9, 191, 0, 8, 15, 0, 8, 143, 0, 8, 79, 0, 9, 255 };
  int[] c = null;
  int[] hn = null;
  int[] r = null;
  int[] u = null;
  int[] v = null;
  int[] x = null;
  
  static
  {
    fixed_td = new int[] { 80, 5, 1, 87, 5, 257, 83, 5, 17, 91, 5, 4097, 81, 5, 5, 89, 5, 1025, 85, 5, 65, 93, 5, 16385, 80, 5, 3, 88, 5, 513, 84, 5, 33, 92, 5, 8193, 82, 5, 9, 90, 5, 2049, 86, 5, 129, 192, 5, 24577, 80, 5, 2, 87, 5, 385, 83, 5, 25, 91, 5, 6145, 81, 5, 7, 89, 5, 1537, 85, 5, 97, 93, 5, 24577, 80, 5, 4, 88, 5, 769, 84, 5, 49, 92, 5, 12289, 82, 5, 13, 90, 5, 3073, 86, 5, 193, 192, 5, 24577 };
    cplens = new int[] { 3, 4, 5, 6, 7, 8, 9, 10, 11, 13, 15, 17, 19, 23, 27, 31, 35, 43, 51, 59, 67, 83, 99, 115, 131, 163, 195, 227, 258, 0, 0 };
    cplext = new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 2, 2, 2, 2, 3, 3, 3, 3, 4, 4, 4, 4, 5, 5, 5, 5, 0, 112, 112 };
    cpdist = new int[] { 1, 2, 3, 4, 5, 7, 9, 13, 17, 25, 33, 49, 65, 97, 129, 193, 257, 385, 513, 769, 1025, 1537, 2049, 3073, 4097, 6145, 8193, 12289, 16385, 24577 };
  }
  
  private int huft_build(int[] paramArrayOfInt1, int paramInt1, int paramInt2, int paramInt3, int[] paramArrayOfInt2, int[] paramArrayOfInt3, int[] paramArrayOfInt4, int[] paramArrayOfInt5, int[] paramArrayOfInt6, int[] paramArrayOfInt7, int[] paramArrayOfInt8)
  {
    int j = 0;
    int i = paramInt2;
    int k;
    do
    {
      int[] arrayOfInt = this.c;
      k = paramArrayOfInt1[(paramInt1 + j)];
      arrayOfInt[k] += 1;
      j += 1;
      k = i - 1;
      i = k;
    } while (k != 0);
    if (this.c[0] == paramInt2)
    {
      paramArrayOfInt4[0] = -1;
      paramArrayOfInt5[0] = 0;
      return 0;
    }
    j = paramArrayOfInt5[0];
    i = 1;
    int n;
    if ((i > 15) || (this.c[i] != 0))
    {
      n = i;
      m = j;
      if (j < i) {
        m = i;
      }
      j = 15;
      label124:
      if ((j != 0) && (this.c[j] == 0)) {
        break label202;
      }
      k = m;
      if (m > j) {
        k = j;
      }
      paramArrayOfInt5[0] = k;
      m = 1 << i;
    }
    for (;;)
    {
      if (i >= j) {
        break label226;
      }
      m -= this.c[i];
      if (m < 0)
      {
        return -3;
        i += 1;
        break;
        label202:
        j -= 1;
        break label124;
      }
      i += 1;
      m <<= 1;
    }
    label226:
    int i7 = m - this.c[j];
    if (i7 < 0) {
      return -3;
    }
    paramArrayOfInt5 = this.c;
    paramArrayOfInt5[j] += i7;
    paramArrayOfInt5 = this.x;
    int i2 = 0;
    paramArrayOfInt5[1] = 0;
    int m = 1;
    int i1 = 2;
    i = j;
    for (;;)
    {
      i -= 1;
      if (i == 0) {
        break;
      }
      paramArrayOfInt5 = this.x;
      i2 += this.c[m];
      paramArrayOfInt5[i1] = i2;
      i1 += 1;
      m += 1;
    }
    i = 0;
    m = 0;
    do
    {
      i1 = paramArrayOfInt1[(paramInt1 + m)];
      if (i1 != 0)
      {
        paramArrayOfInt5 = this.x;
        i2 = paramArrayOfInt5[i1];
        paramArrayOfInt5[i1] = (i2 + 1);
        paramArrayOfInt8[i2] = i;
      }
      m += 1;
      i1 = i + 1;
      i = i1;
    } while (i1 < paramInt2);
    int i8 = this.x[j];
    paramArrayOfInt1 = this.x;
    i = 0;
    paramArrayOfInt1[0] = 0;
    paramInt1 = 0;
    m = -1;
    int i3 = -k;
    this.u[0] = 0;
    i1 = 0;
    i2 = 0;
    paramInt2 = n;
    int i4;
    if (paramInt2 <= j)
    {
      i4 = this.c[paramInt2];
      n = i3;
      i3 = i4;
    }
    for (;;)
    {
      i4 = i3 - 1;
      int i5;
      if (i3 != 0)
      {
        while (paramInt2 > n + k)
        {
          int i6 = m + 1;
          i5 = n + k;
          n = j - i5;
          m = n;
          if (n > k) {
            m = k;
          }
          i2 = paramInt2 - i5;
          i1 = 1 << i2;
          n = i2;
          if (i1 > i4 + 1)
          {
            i3 = i1 - (i4 + 1);
            i1 = paramInt2;
            n = i2;
            if (i2 >= m) {}
          }
          for (;;)
          {
            i2 += 1;
            n = i2;
            if (i2 < m)
            {
              n = i3 << 1;
              paramArrayOfInt1 = this.c;
              i1 += 1;
              if (n <= paramArrayOfInt1[i1]) {
                n = i2;
              }
            }
            else
            {
              i2 = 1 << n;
              if (paramArrayOfInt7[0] + i2 <= 1440) {
                break;
              }
              return -3;
            }
            i3 = n - this.c[i1];
          }
          paramArrayOfInt1 = this.u;
          i1 = paramArrayOfInt7[0];
          paramArrayOfInt1[i6] = i1;
          paramArrayOfInt7[0] += i2;
          if (i6 != 0)
          {
            this.x[i6] = i;
            this.r[0] = ((byte)n);
            this.r[1] = ((byte)k);
            m = i >>> i5 - k;
            this.r[2] = (i1 - this.u[(i6 - 1)] - m);
            System.arraycopy(this.r, 0, paramArrayOfInt6, (this.u[(i6 - 1)] + m) * 3, 3);
            m = i6;
            n = i5;
          }
          else
          {
            paramArrayOfInt4[0] = i1;
            m = i6;
            n = i5;
          }
        }
        this.r[1] = ((byte)(paramInt2 - n));
        if (paramInt1 >= i8) {
          this.r[0] = 192;
        }
        for (;;)
        {
          i3 = i >>> n;
          while (i3 < i2)
          {
            System.arraycopy(this.r, 0, paramArrayOfInt6, (i1 + i3) * 3, 3);
            i3 += (1 << paramInt2 - n);
          }
          if (paramArrayOfInt8[paramInt1] < paramInt3)
          {
            paramArrayOfInt1 = this.r;
            if (paramArrayOfInt8[paramInt1] < 256) {}
            for (i3 = 0;; i3 = 96)
            {
              paramArrayOfInt1[0] = ((byte)i3);
              paramArrayOfInt1 = this.r;
              i3 = paramInt1 + 1;
              paramArrayOfInt1[2] = paramArrayOfInt8[paramInt1];
              paramInt1 = i3;
              break;
            }
          }
          this.r[0] = ((byte)(paramArrayOfInt3[(paramArrayOfInt8[paramInt1] - paramInt3)] + 16 + 64));
          paramArrayOfInt1 = this.r;
          i3 = paramInt1 + 1;
          paramArrayOfInt1[2] = paramArrayOfInt2[(paramArrayOfInt8[paramInt1] - paramInt3)];
          paramInt1 = i3;
        }
        i3 = 1 << paramInt2 - 1;
        while ((i & i3) != 0)
        {
          i ^= i3;
          i3 >>>= 1;
        }
        i5 = i ^ i3;
        for (i = (1 << n) - 1; (i5 & i) != this.x[m]; i = (1 << n) - 1)
        {
          m -= 1;
          n -= k;
        }
      }
      paramInt2 += 1;
      i3 = n;
      break;
      if ((i7 != 0) && (j != 1)) {
        return -5;
      }
      return 0;
      i3 = i4;
      i = i5;
    }
  }
  
  static int inflate_trees_fixed(int[] paramArrayOfInt1, int[] paramArrayOfInt2, int[][] paramArrayOfInt3, int[][] paramArrayOfInt4, ZStream paramZStream)
  {
    paramArrayOfInt1[0] = 9;
    paramArrayOfInt2[0] = 5;
    paramArrayOfInt3[0] = fixed_tl;
    paramArrayOfInt4[0] = fixed_td;
    return 0;
  }
  
  private void initWorkArea(int paramInt)
  {
    if (this.hn == null)
    {
      this.hn = new int[1];
      this.v = new int[paramInt];
      this.c = new int[16];
      this.r = new int[3];
      this.u = new int[15];
      this.x = new int[16];
    }
    if (this.v.length < paramInt) {
      this.v = new int[paramInt];
    }
    int i = 0;
    while (i < paramInt)
    {
      this.v[i] = 0;
      i += 1;
    }
    paramInt = 0;
    while (paramInt < 16)
    {
      this.c[paramInt] = 0;
      paramInt += 1;
    }
    paramInt = 0;
    while (paramInt < 3)
    {
      this.r[paramInt] = 0;
      paramInt += 1;
    }
    System.arraycopy(this.c, 0, this.u, 0, 15);
    System.arraycopy(this.c, 0, this.x, 0, 16);
  }
  
  int inflate_trees_bits(int[] paramArrayOfInt1, int[] paramArrayOfInt2, int[] paramArrayOfInt3, int[] paramArrayOfInt4, ZStream paramZStream)
  {
    initWorkArea(19);
    this.hn[0] = 0;
    int i = huft_build(paramArrayOfInt1, 0, 19, 19, null, null, paramArrayOfInt3, paramArrayOfInt2, paramArrayOfInt4, this.hn, this.v);
    if (i == -3) {
      paramZStream.msg = "oversubscribed dynamic bit lengths tree";
    }
    while ((i != -5) && (paramArrayOfInt2[0] != 0)) {
      return i;
    }
    paramZStream.msg = "incomplete dynamic bit lengths tree";
    return -3;
  }
  
  int inflate_trees_dynamic(int paramInt1, int paramInt2, int[] paramArrayOfInt1, int[] paramArrayOfInt2, int[] paramArrayOfInt3, int[] paramArrayOfInt4, int[] paramArrayOfInt5, int[] paramArrayOfInt6, ZStream paramZStream)
  {
    initWorkArea(288);
    this.hn[0] = 0;
    int i = huft_build(paramArrayOfInt1, 0, paramInt1, 257, cplens, cplext, paramArrayOfInt4, paramArrayOfInt2, paramArrayOfInt6, this.hn, this.v);
    if ((i != 0) || (paramArrayOfInt2[0] == 0))
    {
      if (i == -3)
      {
        paramZStream.msg = "oversubscribed literal/length tree";
        paramInt1 = i;
      }
      for (;;)
      {
        return paramInt1;
        paramInt1 = i;
        if (i != -4)
        {
          paramZStream.msg = "incomplete literal/length tree";
          paramInt1 = -3;
        }
      }
    }
    initWorkArea(288);
    paramInt2 = huft_build(paramArrayOfInt1, paramInt1, paramInt2, 0, cpdist, cpdext, paramArrayOfInt5, paramArrayOfInt3, paramArrayOfInt6, this.hn, this.v);
    if ((paramInt2 != 0) || ((paramArrayOfInt3[0] == 0) && (paramInt1 > 257)))
    {
      if (paramInt2 == -3)
      {
        paramZStream.msg = "oversubscribed distance tree";
        paramInt1 = paramInt2;
      }
      for (;;)
      {
        return paramInt1;
        if (paramInt2 == -5)
        {
          paramZStream.msg = "incomplete distance tree";
          paramInt1 = -3;
        }
        else
        {
          paramInt1 = paramInt2;
          if (paramInt2 != -4)
          {
            paramZStream.msg = "empty distance tree with lengths";
            paramInt1 = -3;
          }
        }
      }
    }
    return 0;
  }
}


/* Location:           C:\Users\Administrator\Documents\ds\反编译\AndroidKiller_v1.3.1\projects\Jungledsq_ppc\ProjectSrc\smali\
 * Qualified Name:     com.jcraft.jzlib.InfTree
 * JD-Core Version:    0.7.0.1
 */