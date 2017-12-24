package com.jcraft.jzlib;

final class Adler32
{
  private static final int BASE = 65521;
  private static final int NMAX = 5552;
  
  long adler32(long paramLong, byte[] paramArrayOfByte, int paramInt1, int paramInt2)
  {
    if (paramArrayOfByte == null) {
      return 1L;
    }
    long l1 = paramLong & 0xFFFF;
    paramLong = paramLong >> 16 & 0xFFFF;
    while (paramInt2 > 0)
    {
      if (paramInt2 < 5552) {}
      int j;
      for (int i = paramInt2;; i = 5552)
      {
        j = paramInt2 - i;
        paramInt2 = i;
        while (paramInt2 >= 16)
        {
          i = paramInt1 + 1;
          l2 = l1 + (paramArrayOfByte[paramInt1] & 0xFF);
          paramInt1 = i + 1;
          l3 = l2 + (paramArrayOfByte[i] & 0xFF);
          i = paramInt1 + 1;
          long l4 = l3 + (paramArrayOfByte[paramInt1] & 0xFF);
          paramInt1 = i + 1;
          long l5 = l4 + (paramArrayOfByte[i] & 0xFF);
          i = paramInt1 + 1;
          long l6 = l5 + (paramArrayOfByte[paramInt1] & 0xFF);
          paramInt1 = i + 1;
          long l7 = l6 + (paramArrayOfByte[i] & 0xFF);
          i = paramInt1 + 1;
          long l8 = l7 + (paramArrayOfByte[paramInt1] & 0xFF);
          paramInt1 = i + 1;
          long l9 = l8 + (paramArrayOfByte[i] & 0xFF);
          i = paramInt1 + 1;
          long l10 = l9 + (paramArrayOfByte[paramInt1] & 0xFF);
          paramInt1 = i + 1;
          long l11 = l10 + (paramArrayOfByte[i] & 0xFF);
          i = paramInt1 + 1;
          long l12 = l11 + (paramArrayOfByte[paramInt1] & 0xFF);
          paramInt1 = i + 1;
          long l13 = l12 + (paramArrayOfByte[i] & 0xFF);
          i = paramInt1 + 1;
          long l14 = l13 + (paramArrayOfByte[paramInt1] & 0xFF);
          paramInt1 = i + 1;
          long l15 = l14 + (paramArrayOfByte[i] & 0xFF);
          i = paramInt1 + 1;
          long l16 = l15 + (paramArrayOfByte[paramInt1] & 0xFF);
          paramInt1 = i + 1;
          l1 = l16 + (paramArrayOfByte[i] & 0xFF);
          paramLong = paramLong + l2 + l3 + l4 + l5 + l6 + l7 + l8 + l9 + l10 + l11 + l12 + l13 + l14 + l15 + l16 + l1;
          paramInt2 -= 16;
        }
      }
      i = paramInt1;
      long l3 = l1;
      long l2 = paramLong;
      if (paramInt2 != 0)
      {
        l2 = l1;
        int k;
        do
        {
          i = paramInt1 + 1;
          l1 = l2 + (paramArrayOfByte[paramInt1] & 0xFF);
          l3 = paramLong + l1;
          k = paramInt2 - 1;
          paramInt1 = i;
          paramInt2 = k;
          l2 = l1;
          paramLong = l3;
        } while (k != 0);
        l2 = l3;
        l3 = l1;
      }
      paramInt1 = i;
      l1 = l3 % 65521L;
      paramLong = l2 % 65521L;
      paramInt2 = j;
    }
    return paramLong << 16 | l1;
  }
}


/* Location:           C:\Users\Administrator\Documents\ds\反编译\AndroidKiller_v1.3.1\projects\Jungledsq_ppc\ProjectSrc\smali\
 * Qualified Name:     com.jcraft.jzlib.Adler32
 * JD-Core Version:    0.7.0.1
 */