package org.jivesoftware.smack.util;

import java.io.FilterInputStream;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.Serializable;

public class Base64
{
  public static final int DECODE = 0;
  public static final int DONT_BREAK_LINES = 8;
  public static final int ENCODE = 1;
  private static final byte EQUALS_SIGN = 61;
  private static final byte EQUALS_SIGN_ENC = -1;
  public static final int GZIP = 2;
  private static final int MAX_LINE_LENGTH = 76;
  private static final byte NEW_LINE = 10;
  public static final int NO_OPTIONS = 0;
  public static final int ORDERED = 32;
  private static final String PREFERRED_ENCODING = "UTF-8";
  public static final int URL_SAFE = 16;
  private static final byte WHITE_SPACE_ENC = -5;
  private static final byte[] _ORDERED_ALPHABET = { 45, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90, 95, 97, 98, 99, 100, 101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 111, 112, 113, 114, 115, 116, 117, 118, 119, 120, 121, 122 };
  private static final byte[] _ORDERED_DECODABET = { -9, -9, -9, -9, -9, -9, -9, -9, -9, -5, -5, -9, -9, -5, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -5, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, 0, -9, -9, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, -9, -9, -9, -1, -9, -9, -9, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, -9, -9, -9, -9, 37, -9, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, 62, 63, -9, -9, -9, -9 };
  private static final byte[] _STANDARD_ALPHABET = { 65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90, 97, 98, 99, 100, 101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 111, 112, 113, 114, 115, 116, 117, 118, 119, 120, 121, 122, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 43, 47 };
  private static final byte[] _STANDARD_DECODABET = { -9, -9, -9, -9, -9, -9, -9, -9, -9, -5, -5, -9, -9, -5, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -5, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, 62, -9, -9, -9, 63, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, -9, -9, -9, -1, -9, -9, -9, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, -9, -9, -9, -9, -9, -9, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, -9, -9, -9, -9 };
  private static final byte[] _URL_SAFE_ALPHABET = { 65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90, 97, 98, 99, 100, 101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 111, 112, 113, 114, 115, 116, 117, 118, 119, 120, 121, 122, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 45, 95 };
  private static final byte[] _URL_SAFE_DECODABET = { -9, -9, -9, -9, -9, -9, -9, -9, -9, -5, -5, -9, -9, -5, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -5, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, 62, -9, -9, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, -9, -9, -9, -1, -9, -9, -9, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, -9, -9, -9, -9, 63, -9, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, -9, -9, -9, -9 };
  
  public static byte[] decode(String paramString)
  {
    return decode(paramString, 0);
  }
  
  /* Error */
  public static byte[] decode(String paramString, int paramInt)
  {
    // Byte code:
    //   0: aload_0
    //   1: ldc 35
    //   3: invokevirtual 212	java/lang/String:getBytes	(Ljava/lang/String;)[B
    //   6: astore_2
    //   7: aload_2
    //   8: astore_0
    //   9: aload_0
    //   10: iconst_0
    //   11: aload_0
    //   12: arraylength
    //   13: iload_1
    //   14: invokestatic 215	org/jivesoftware/smack/util/Base64:decode	([BIII)[B
    //   17: astore 9
    //   19: aload 9
    //   21: ifnull +121 -> 142
    //   24: aload 9
    //   26: arraylength
    //   27: iconst_4
    //   28: if_icmplt +114 -> 142
    //   31: ldc 216
    //   33: aload 9
    //   35: iconst_0
    //   36: baload
    //   37: sipush 255
    //   40: iand
    //   41: aload 9
    //   43: iconst_1
    //   44: baload
    //   45: bipush 8
    //   47: ishl
    //   48: ldc 217
    //   50: iand
    //   51: ior
    //   52: if_icmpne +90 -> 142
    //   55: aconst_null
    //   56: astore_3
    //   57: aconst_null
    //   58: astore 6
    //   60: aconst_null
    //   61: astore 4
    //   63: aconst_null
    //   64: astore 5
    //   66: aconst_null
    //   67: astore 7
    //   69: aconst_null
    //   70: astore 8
    //   72: sipush 2048
    //   75: newarray byte
    //   77: astore 10
    //   79: new 219	java/io/ByteArrayOutputStream
    //   82: dup
    //   83: invokespecial 220	java/io/ByteArrayOutputStream:<init>	()V
    //   86: astore_0
    //   87: new 222	java/io/ByteArrayInputStream
    //   90: dup
    //   91: aload 9
    //   93: invokespecial 225	java/io/ByteArrayInputStream:<init>	([B)V
    //   96: astore_2
    //   97: new 227	java/util/zip/GZIPInputStream
    //   100: dup
    //   101: aload_2
    //   102: invokespecial 230	java/util/zip/GZIPInputStream:<init>	(Ljava/io/InputStream;)V
    //   105: astore_3
    //   106: aload_3
    //   107: aload 10
    //   109: invokevirtual 234	java/util/zip/GZIPInputStream:read	([B)I
    //   112: istore_1
    //   113: iload_1
    //   114: iflt +40 -> 154
    //   117: aload_0
    //   118: aload 10
    //   120: iconst_0
    //   121: iload_1
    //   122: invokevirtual 238	java/io/ByteArrayOutputStream:write	([BII)V
    //   125: goto -19 -> 106
    //   128: astore 4
    //   130: aload_0
    //   131: invokevirtual 241	java/io/ByteArrayOutputStream:close	()V
    //   134: aload_3
    //   135: invokevirtual 242	java/util/zip/GZIPInputStream:close	()V
    //   138: aload_2
    //   139: invokevirtual 243	java/io/ByteArrayInputStream:close	()V
    //   142: aload 9
    //   144: areturn
    //   145: astore_2
    //   146: aload_0
    //   147: invokevirtual 246	java/lang/String:getBytes	()[B
    //   150: astore_0
    //   151: goto -142 -> 9
    //   154: aload_0
    //   155: invokevirtual 249	java/io/ByteArrayOutputStream:toByteArray	()[B
    //   158: astore 4
    //   160: aload_0
    //   161: invokevirtual 241	java/io/ByteArrayOutputStream:close	()V
    //   164: aload_3
    //   165: invokevirtual 242	java/util/zip/GZIPInputStream:close	()V
    //   168: aload_2
    //   169: invokevirtual 243	java/io/ByteArrayInputStream:close	()V
    //   172: aload 4
    //   174: areturn
    //   175: astore_0
    //   176: aload 4
    //   178: areturn
    //   179: astore_2
    //   180: aload 7
    //   182: astore_0
    //   183: aload_0
    //   184: invokevirtual 241	java/io/ByteArrayOutputStream:close	()V
    //   187: aload 4
    //   189: invokevirtual 242	java/util/zip/GZIPInputStream:close	()V
    //   192: aload_3
    //   193: invokevirtual 243	java/io/ByteArrayInputStream:close	()V
    //   196: aload_2
    //   197: athrow
    //   198: astore_0
    //   199: goto -35 -> 164
    //   202: astore_0
    //   203: goto -35 -> 168
    //   206: astore_0
    //   207: goto -73 -> 134
    //   210: astore_0
    //   211: goto -73 -> 138
    //   214: astore_0
    //   215: aload 9
    //   217: areturn
    //   218: astore_0
    //   219: goto -32 -> 187
    //   222: astore_0
    //   223: goto -31 -> 192
    //   226: astore_0
    //   227: goto -31 -> 196
    //   230: astore_2
    //   231: goto -48 -> 183
    //   234: astore 5
    //   236: aload_2
    //   237: astore_3
    //   238: aload 5
    //   240: astore_2
    //   241: goto -58 -> 183
    //   244: astore 5
    //   246: aload_3
    //   247: astore 4
    //   249: aload_2
    //   250: astore_3
    //   251: aload 5
    //   253: astore_2
    //   254: goto -71 -> 183
    //   257: astore_0
    //   258: aload 6
    //   260: astore_2
    //   261: aload 8
    //   263: astore_0
    //   264: aload 5
    //   266: astore_3
    //   267: goto -137 -> 130
    //   270: astore_2
    //   271: aload 6
    //   273: astore_2
    //   274: aload 5
    //   276: astore_3
    //   277: goto -147 -> 130
    //   280: astore_3
    //   281: aload 5
    //   283: astore_3
    //   284: goto -154 -> 130
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	287	0	paramString	String
    //   0	287	1	paramInt	int
    //   6	133	2	localObject1	Object
    //   145	24	2	localUnsupportedEncodingException	java.io.UnsupportedEncodingException
    //   179	18	2	localObject2	Object
    //   230	7	2	localObject3	Object
    //   240	21	2	localObject4	Object
    //   270	1	2	localIOException1	IOException
    //   273	1	2	localObject5	Object
    //   56	221	3	localObject6	Object
    //   280	1	3	localIOException2	IOException
    //   283	1	3	localObject7	Object
    //   61	1	4	localObject8	Object
    //   128	1	4	localIOException3	IOException
    //   158	90	4	localObject9	Object
    //   64	1	5	localObject10	Object
    //   234	5	5	localObject11	Object
    //   244	38	5	localObject12	Object
    //   58	214	6	localObject13	Object
    //   67	114	7	localObject14	Object
    //   70	192	8	localObject15	Object
    //   17	199	9	arrayOfByte1	byte[]
    //   77	42	10	arrayOfByte2	byte[]
    // Exception table:
    //   from	to	target	type
    //   106	113	128	java/io/IOException
    //   117	125	128	java/io/IOException
    //   154	160	128	java/io/IOException
    //   0	7	145	java/io/UnsupportedEncodingException
    //   168	172	175	java/lang/Exception
    //   79	87	179	finally
    //   160	164	198	java/lang/Exception
    //   164	168	202	java/lang/Exception
    //   130	134	206	java/lang/Exception
    //   134	138	210	java/lang/Exception
    //   138	142	214	java/lang/Exception
    //   183	187	218	java/lang/Exception
    //   187	192	222	java/lang/Exception
    //   192	196	226	java/lang/Exception
    //   87	97	230	finally
    //   97	106	234	finally
    //   106	113	244	finally
    //   117	125	244	finally
    //   154	160	244	finally
    //   79	87	257	java/io/IOException
    //   87	97	270	java/io/IOException
    //   97	106	280	java/io/IOException
  }
  
  public static byte[] decode(byte[] paramArrayOfByte, int paramInt1, int paramInt2, int paramInt3)
  {
    byte[] arrayOfByte2 = getDecodabet(paramInt3);
    byte[] arrayOfByte1 = new byte[paramInt2 * 3 / 4];
    byte[] arrayOfByte3 = new byte[4];
    int n = 0;
    int k = 0;
    int i1 = paramInt1;
    int j = 0;
    int m = 0;
    int i;
    int i2;
    if (i1 < paramInt1 + paramInt2)
    {
      i = (byte)(paramArrayOfByte[i1] & 0x7F);
      m = arrayOfByte2[i];
      if (m >= -5)
      {
        if (m < -1) {
          break label236;
        }
        i2 = j + 1;
        arrayOfByte3[j] = i;
        j = i2;
        n = k;
        if (i2 <= 3) {
          break label207;
        }
        j = k + decode4to3(arrayOfByte3, 0, arrayOfByte1, k, paramInt3);
        k = 0;
        if (i != 61) {
          break label199;
        }
        paramInt2 = i;
        paramInt1 = j;
        n = m;
      }
    }
    for (;;)
    {
      paramArrayOfByte = new byte[paramInt1];
      System.arraycopy(arrayOfByte1, 0, paramArrayOfByte, 0, paramInt1);
      return paramArrayOfByte;
      System.err.println("Bad Base64 input character at " + i1 + ": " + paramArrayOfByte[i1] + "(decimal)");
      return null;
      label199:
      n = j;
      j = k;
      for (;;)
      {
        label207:
        i1 += 1;
        k = i;
        i2 = m;
        m = k;
        k = n;
        n = i2;
        break;
        label236:
        n = k;
      }
      paramInt1 = k;
      paramInt2 = m;
    }
  }
  
  private static int decode4to3(byte[] paramArrayOfByte1, int paramInt1, byte[] paramArrayOfByte2, int paramInt2, int paramInt3)
  {
    byte[] arrayOfByte = getDecodabet(paramInt3);
    if (paramArrayOfByte1[(paramInt1 + 2)] == 61)
    {
      paramArrayOfByte2[paramInt2] = ((byte)(((arrayOfByte[paramArrayOfByte1[paramInt1]] & 0xFF) << 18 | (arrayOfByte[paramArrayOfByte1[(paramInt1 + 1)]] & 0xFF) << 12) >>> 16));
      return 1;
    }
    if (paramArrayOfByte1[(paramInt1 + 3)] == 61)
    {
      paramInt1 = (arrayOfByte[paramArrayOfByte1[paramInt1]] & 0xFF) << 18 | (arrayOfByte[paramArrayOfByte1[(paramInt1 + 1)]] & 0xFF) << 12 | (arrayOfByte[paramArrayOfByte1[(paramInt1 + 2)]] & 0xFF) << 6;
      paramArrayOfByte2[paramInt2] = ((byte)(paramInt1 >>> 16));
      paramArrayOfByte2[(paramInt2 + 1)] = ((byte)(paramInt1 >>> 8));
      return 2;
    }
    paramInt1 = (arrayOfByte[paramArrayOfByte1[paramInt1]] & 0xFF) << 18 | (arrayOfByte[paramArrayOfByte1[(paramInt1 + 1)]] & 0xFF) << 12 | (arrayOfByte[paramArrayOfByte1[(paramInt1 + 2)]] & 0xFF) << 6 | arrayOfByte[paramArrayOfByte1[(paramInt1 + 3)]] & 0xFF;
    paramArrayOfByte2[paramInt2] = ((byte)(paramInt1 >> 16));
    paramArrayOfByte2[(paramInt2 + 1)] = ((byte)(paramInt1 >> 8));
    paramArrayOfByte2[(paramInt2 + 2)] = ((byte)paramInt1);
    return 3;
  }
  
  /* Error */
  public static void decodeFileToFile(String paramString1, String paramString2)
  {
    // Byte code:
    //   0: aload_0
    //   1: invokestatic 290	org/jivesoftware/smack/util/Base64:decodeFromFile	(Ljava/lang/String;)[B
    //   4: astore_2
    //   5: aconst_null
    //   6: astore_0
    //   7: aconst_null
    //   8: astore_3
    //   9: new 292	java/io/BufferedOutputStream
    //   12: dup
    //   13: new 294	java/io/FileOutputStream
    //   16: dup
    //   17: aload_1
    //   18: invokespecial 296	java/io/FileOutputStream:<init>	(Ljava/lang/String;)V
    //   21: invokespecial 299	java/io/BufferedOutputStream:<init>	(Ljava/io/OutputStream;)V
    //   24: astore_1
    //   25: aload_1
    //   26: aload_2
    //   27: invokevirtual 303	java/io/OutputStream:write	([B)V
    //   30: aload_1
    //   31: invokevirtual 304	java/io/OutputStream:close	()V
    //   34: return
    //   35: astore_0
    //   36: return
    //   37: astore_2
    //   38: aload_3
    //   39: astore_1
    //   40: aload_1
    //   41: astore_0
    //   42: aload_2
    //   43: invokevirtual 307	java/io/IOException:printStackTrace	()V
    //   46: aload_1
    //   47: invokevirtual 304	java/io/OutputStream:close	()V
    //   50: return
    //   51: astore_0
    //   52: return
    //   53: astore_1
    //   54: aload_0
    //   55: invokevirtual 304	java/io/OutputStream:close	()V
    //   58: aload_1
    //   59: athrow
    //   60: astore_0
    //   61: goto -3 -> 58
    //   64: astore_2
    //   65: aload_1
    //   66: astore_0
    //   67: aload_2
    //   68: astore_1
    //   69: goto -15 -> 54
    //   72: astore_2
    //   73: goto -33 -> 40
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	76	0	paramString1	String
    //   0	76	1	paramString2	String
    //   4	23	2	arrayOfByte	byte[]
    //   37	6	2	localIOException1	IOException
    //   64	4	2	localObject1	Object
    //   72	1	2	localIOException2	IOException
    //   8	31	3	localObject2	Object
    // Exception table:
    //   from	to	target	type
    //   30	34	35	java/lang/Exception
    //   9	25	37	java/io/IOException
    //   46	50	51	java/lang/Exception
    //   9	25	53	finally
    //   42	46	53	finally
    //   54	58	60	java/lang/Exception
    //   25	30	64	finally
    //   25	30	72	java/io/IOException
  }
  
  /* Error */
  public static byte[] decodeFromFile(String paramString)
  {
    // Byte code:
    //   0: aconst_null
    //   1: astore 7
    //   3: aconst_null
    //   4: astore 6
    //   6: aconst_null
    //   7: astore 4
    //   9: aconst_null
    //   10: astore 5
    //   12: aload 4
    //   14: astore_3
    //   15: new 309	java/io/File
    //   18: dup
    //   19: aload_0
    //   20: invokespecial 310	java/io/File:<init>	(Ljava/lang/String;)V
    //   23: astore 9
    //   25: iconst_0
    //   26: istore_1
    //   27: aload 4
    //   29: astore_3
    //   30: aload 9
    //   32: invokevirtual 314	java/io/File:length	()J
    //   35: ldc2_w 315
    //   38: lcmp
    //   39: ifle +50 -> 89
    //   42: aload 4
    //   44: astore_3
    //   45: getstatic 259	java/lang/System:err	Ljava/io/PrintStream;
    //   48: new 261	java/lang/StringBuilder
    //   51: dup
    //   52: invokespecial 262	java/lang/StringBuilder:<init>	()V
    //   55: ldc_w 318
    //   58: invokevirtual 268	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   61: aload 9
    //   63: invokevirtual 314	java/io/File:length	()J
    //   66: invokevirtual 321	java/lang/StringBuilder:append	(J)Ljava/lang/StringBuilder;
    //   69: ldc_w 323
    //   72: invokevirtual 268	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   75: invokevirtual 279	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   78: invokevirtual 285	java/io/PrintStream:println	(Ljava/lang/String;)V
    //   81: new 325	java/lang/NullPointerException
    //   84: dup
    //   85: invokespecial 326	java/lang/NullPointerException:<init>	()V
    //   88: athrow
    //   89: aload 4
    //   91: astore_3
    //   92: aload 9
    //   94: invokevirtual 314	java/io/File:length	()J
    //   97: l2i
    //   98: newarray byte
    //   100: astore 8
    //   102: aload 4
    //   104: astore_3
    //   105: new 6	org/jivesoftware/smack/util/Base64$InputStream
    //   108: dup
    //   109: new 328	java/io/BufferedInputStream
    //   112: dup
    //   113: new 330	java/io/FileInputStream
    //   116: dup
    //   117: aload 9
    //   119: invokespecial 333	java/io/FileInputStream:<init>	(Ljava/io/File;)V
    //   122: invokespecial 334	java/io/BufferedInputStream:<init>	(Ljava/io/InputStream;)V
    //   125: iconst_0
    //   126: invokespecial 337	org/jivesoftware/smack/util/Base64$InputStream:<init>	(Ljava/io/InputStream;I)V
    //   129: astore 4
    //   131: aload 7
    //   133: astore_3
    //   134: aload 4
    //   136: aload 8
    //   138: iload_1
    //   139: sipush 4096
    //   142: invokevirtual 340	org/jivesoftware/smack/util/Base64$InputStream:read	([BII)I
    //   145: istore_2
    //   146: iload_2
    //   147: iflt +10 -> 157
    //   150: iload_1
    //   151: iload_2
    //   152: iadd
    //   153: istore_1
    //   154: goto -23 -> 131
    //   157: aload 7
    //   159: astore_3
    //   160: iload_1
    //   161: newarray byte
    //   163: astore 5
    //   165: aload 5
    //   167: astore_3
    //   168: aload 8
    //   170: iconst_0
    //   171: aload 5
    //   173: iconst_0
    //   174: iload_1
    //   175: invokestatic 255	java/lang/System:arraycopy	(Ljava/lang/Object;ILjava/lang/Object;II)V
    //   178: aload 4
    //   180: invokevirtual 341	org/jivesoftware/smack/util/Base64$InputStream:close	()V
    //   183: aload 5
    //   185: astore 4
    //   187: aload 4
    //   189: areturn
    //   190: astore_0
    //   191: aload 5
    //   193: astore 4
    //   195: goto -8 -> 187
    //   198: astore_3
    //   199: aload 6
    //   201: astore 4
    //   203: aload 5
    //   205: astore_3
    //   206: getstatic 259	java/lang/System:err	Ljava/io/PrintStream;
    //   209: new 261	java/lang/StringBuilder
    //   212: dup
    //   213: invokespecial 262	java/lang/StringBuilder:<init>	()V
    //   216: ldc_w 343
    //   219: invokevirtual 268	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   222: aload_0
    //   223: invokevirtual 268	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   226: invokevirtual 279	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   229: invokevirtual 285	java/io/PrintStream:println	(Ljava/lang/String;)V
    //   232: aload 5
    //   234: invokevirtual 341	org/jivesoftware/smack/util/Base64$InputStream:close	()V
    //   237: goto -50 -> 187
    //   240: astore_0
    //   241: goto -54 -> 187
    //   244: astore_0
    //   245: aload_3
    //   246: invokevirtual 341	org/jivesoftware/smack/util/Base64$InputStream:close	()V
    //   249: aload_0
    //   250: athrow
    //   251: astore_0
    //   252: aconst_null
    //   253: areturn
    //   254: astore_3
    //   255: goto -6 -> 249
    //   258: astore_0
    //   259: aload 4
    //   261: astore_3
    //   262: goto -17 -> 245
    //   265: astore 5
    //   267: aload 4
    //   269: astore 5
    //   271: aload_3
    //   272: astore 4
    //   274: goto -71 -> 203
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	277	0	paramString	String
    //   26	149	1	i	int
    //   145	8	2	j	int
    //   14	154	3	localObject1	Object
    //   198	1	3	localIOException1	IOException
    //   205	41	3	arrayOfByte1	byte[]
    //   254	1	3	localException	java.lang.Exception
    //   261	11	3	localObject2	Object
    //   7	266	4	localObject3	Object
    //   10	223	5	arrayOfByte2	byte[]
    //   265	1	5	localIOException2	IOException
    //   269	1	5	localObject4	Object
    //   4	196	6	localObject5	Object
    //   1	157	7	localObject6	Object
    //   100	69	8	arrayOfByte3	byte[]
    //   23	95	9	localFile	java.io.File
    // Exception table:
    //   from	to	target	type
    //   178	183	190	java/lang/Exception
    //   15	25	198	java/io/IOException
    //   30	42	198	java/io/IOException
    //   45	81	198	java/io/IOException
    //   92	102	198	java/io/IOException
    //   105	131	198	java/io/IOException
    //   232	237	240	java/lang/Exception
    //   15	25	244	finally
    //   30	42	244	finally
    //   45	81	244	finally
    //   92	102	244	finally
    //   105	131	244	finally
    //   206	232	244	finally
    //   81	89	251	java/lang/Exception
    //   245	249	254	java/lang/Exception
    //   134	146	258	finally
    //   160	165	258	finally
    //   168	178	258	finally
    //   134	146	265	java/io/IOException
    //   160	165	265	java/io/IOException
    //   168	178	265	java/io/IOException
  }
  
  /* Error */
  public static boolean decodeToFile(String paramString1, String paramString2)
  {
    // Byte code:
    //   0: aconst_null
    //   1: astore_2
    //   2: aconst_null
    //   3: astore_3
    //   4: new 9	org/jivesoftware/smack/util/Base64$OutputStream
    //   7: dup
    //   8: new 294	java/io/FileOutputStream
    //   11: dup
    //   12: aload_1
    //   13: invokespecial 296	java/io/FileOutputStream:<init>	(Ljava/lang/String;)V
    //   16: iconst_0
    //   17: invokespecial 348	org/jivesoftware/smack/util/Base64$OutputStream:<init>	(Ljava/io/OutputStream;I)V
    //   20: astore_1
    //   21: aload_1
    //   22: aload_0
    //   23: ldc 35
    //   25: invokevirtual 212	java/lang/String:getBytes	(Ljava/lang/String;)[B
    //   28: invokevirtual 349	org/jivesoftware/smack/util/Base64$OutputStream:write	([B)V
    //   31: aload_1
    //   32: invokevirtual 350	org/jivesoftware/smack/util/Base64$OutputStream:close	()V
    //   35: iconst_1
    //   36: ireturn
    //   37: astore_0
    //   38: iconst_1
    //   39: ireturn
    //   40: astore_0
    //   41: aload_3
    //   42: astore_0
    //   43: aload_0
    //   44: invokevirtual 350	org/jivesoftware/smack/util/Base64$OutputStream:close	()V
    //   47: iconst_0
    //   48: ireturn
    //   49: astore_0
    //   50: iconst_0
    //   51: ireturn
    //   52: astore_0
    //   53: aload_2
    //   54: astore_1
    //   55: aload_1
    //   56: invokevirtual 350	org/jivesoftware/smack/util/Base64$OutputStream:close	()V
    //   59: aload_0
    //   60: athrow
    //   61: astore_1
    //   62: goto -3 -> 59
    //   65: astore_0
    //   66: goto -11 -> 55
    //   69: astore_0
    //   70: aload_1
    //   71: astore_0
    //   72: goto -29 -> 43
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	75	0	paramString1	String
    //   0	75	1	paramString2	String
    //   1	53	2	localObject1	Object
    //   3	39	3	localObject2	Object
    // Exception table:
    //   from	to	target	type
    //   31	35	37	java/lang/Exception
    //   4	21	40	java/io/IOException
    //   43	47	49	java/lang/Exception
    //   4	21	52	finally
    //   55	59	61	java/lang/Exception
    //   21	31	65	finally
    //   21	31	69	java/io/IOException
  }
  
  /* Error */
  public static Object decodeToObject(String paramString)
  {
    // Byte code:
    //   0: aload_0
    //   1: invokestatic 356	org/jivesoftware/smack/util/Base64:decode	(Ljava/lang/String;)[B
    //   4: astore_0
    //   5: aconst_null
    //   6: astore 7
    //   8: aconst_null
    //   9: astore_1
    //   10: aconst_null
    //   11: astore 6
    //   13: aconst_null
    //   14: astore 5
    //   16: aconst_null
    //   17: astore_2
    //   18: aconst_null
    //   19: astore_3
    //   20: aconst_null
    //   21: astore 4
    //   23: new 222	java/io/ByteArrayInputStream
    //   26: dup
    //   27: aload_0
    //   28: invokespecial 225	java/io/ByteArrayInputStream:<init>	([B)V
    //   31: astore_0
    //   32: new 358	java/io/ObjectInputStream
    //   35: dup
    //   36: aload_0
    //   37: invokespecial 359	java/io/ObjectInputStream:<init>	(Ljava/io/InputStream;)V
    //   40: astore_1
    //   41: aload_1
    //   42: invokevirtual 363	java/io/ObjectInputStream:readObject	()Ljava/lang/Object;
    //   45: astore_2
    //   46: aload_0
    //   47: invokevirtual 243	java/io/ByteArrayInputStream:close	()V
    //   50: aload_1
    //   51: invokevirtual 364	java/io/ObjectInputStream:close	()V
    //   54: aload_2
    //   55: areturn
    //   56: astore_0
    //   57: aload_2
    //   58: areturn
    //   59: astore_3
    //   60: aload 6
    //   62: astore_0
    //   63: aload_0
    //   64: astore_1
    //   65: aload 4
    //   67: astore_2
    //   68: aload_3
    //   69: invokevirtual 307	java/io/IOException:printStackTrace	()V
    //   72: aload_0
    //   73: invokevirtual 243	java/io/ByteArrayInputStream:close	()V
    //   76: aload 4
    //   78: invokevirtual 364	java/io/ObjectInputStream:close	()V
    //   81: aconst_null
    //   82: areturn
    //   83: astore_0
    //   84: aconst_null
    //   85: areturn
    //   86: astore_3
    //   87: aload 5
    //   89: astore 4
    //   91: aload 7
    //   93: astore_0
    //   94: aload_0
    //   95: astore_1
    //   96: aload 4
    //   98: astore_2
    //   99: aload_3
    //   100: invokevirtual 365	java/lang/ClassNotFoundException:printStackTrace	()V
    //   103: aload_0
    //   104: invokevirtual 243	java/io/ByteArrayInputStream:close	()V
    //   107: aload 4
    //   109: invokevirtual 364	java/io/ObjectInputStream:close	()V
    //   112: aconst_null
    //   113: areturn
    //   114: astore_0
    //   115: aconst_null
    //   116: areturn
    //   117: astore_0
    //   118: aload_1
    //   119: invokevirtual 243	java/io/ByteArrayInputStream:close	()V
    //   122: aload_2
    //   123: invokevirtual 364	java/io/ObjectInputStream:close	()V
    //   126: aload_0
    //   127: athrow
    //   128: astore_0
    //   129: goto -79 -> 50
    //   132: astore_0
    //   133: goto -57 -> 76
    //   136: astore_0
    //   137: goto -30 -> 107
    //   140: astore_1
    //   141: goto -19 -> 122
    //   144: astore_1
    //   145: goto -19 -> 126
    //   148: astore 4
    //   150: aload_0
    //   151: astore_1
    //   152: aload_3
    //   153: astore_2
    //   154: aload 4
    //   156: astore_0
    //   157: goto -39 -> 118
    //   160: astore_3
    //   161: aload_1
    //   162: astore_2
    //   163: aload_0
    //   164: astore_1
    //   165: aload_3
    //   166: astore_0
    //   167: goto -49 -> 118
    //   170: astore_3
    //   171: aload 5
    //   173: astore 4
    //   175: goto -81 -> 94
    //   178: astore_3
    //   179: aload_1
    //   180: astore 4
    //   182: goto -88 -> 94
    //   185: astore_3
    //   186: goto -123 -> 63
    //   189: astore_3
    //   190: aload_1
    //   191: astore 4
    //   193: goto -130 -> 63
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	196	0	paramString	String
    //   9	110	1	localObject1	Object
    //   140	1	1	localException1	java.lang.Exception
    //   144	1	1	localException2	java.lang.Exception
    //   151	40	1	str	String
    //   17	146	2	localObject2	Object
    //   19	1	3	localObject3	Object
    //   59	10	3	localIOException1	IOException
    //   86	67	3	localClassNotFoundException1	java.lang.ClassNotFoundException
    //   160	6	3	localObject4	Object
    //   170	1	3	localClassNotFoundException2	java.lang.ClassNotFoundException
    //   178	1	3	localClassNotFoundException3	java.lang.ClassNotFoundException
    //   185	1	3	localIOException2	IOException
    //   189	1	3	localIOException3	IOException
    //   21	87	4	localObject5	Object
    //   148	7	4	localObject6	Object
    //   173	19	4	localObject7	Object
    //   14	158	5	localObject8	Object
    //   11	50	6	localObject9	Object
    //   6	86	7	localObject10	Object
    // Exception table:
    //   from	to	target	type
    //   50	54	56	java/lang/Exception
    //   23	32	59	java/io/IOException
    //   76	81	83	java/lang/Exception
    //   23	32	86	java/lang/ClassNotFoundException
    //   107	112	114	java/lang/Exception
    //   23	32	117	finally
    //   68	72	117	finally
    //   99	103	117	finally
    //   46	50	128	java/lang/Exception
    //   72	76	132	java/lang/Exception
    //   103	107	136	java/lang/Exception
    //   118	122	140	java/lang/Exception
    //   122	126	144	java/lang/Exception
    //   32	41	148	finally
    //   41	46	160	finally
    //   32	41	170	java/lang/ClassNotFoundException
    //   41	46	178	java/lang/ClassNotFoundException
    //   32	41	185	java/io/IOException
    //   41	46	189	java/io/IOException
  }
  
  private static byte[] encode3to4(byte[] paramArrayOfByte1, int paramInt1, int paramInt2, byte[] paramArrayOfByte2, int paramInt3, int paramInt4)
  {
    byte[] arrayOfByte = getAlphabet(paramInt4);
    label22:
    int i;
    if (paramInt2 > 0)
    {
      paramInt4 = paramArrayOfByte1[paramInt1] << 24 >>> 8;
      if (paramInt2 <= 1) {
        break label100;
      }
      i = paramArrayOfByte1[(paramInt1 + 1)] << 24 >>> 16;
      label40:
      if (paramInt2 <= 2) {
        break label106;
      }
    }
    label100:
    label106:
    for (paramInt1 = paramArrayOfByte1[(paramInt1 + 2)] << 24 >>> 24;; paramInt1 = 0)
    {
      paramInt1 = paramInt4 | i | paramInt1;
      switch (paramInt2)
      {
      default: 
        return paramArrayOfByte2;
        paramInt4 = 0;
        break label22;
        i = 0;
        break label40;
      }
    }
    paramArrayOfByte2[paramInt3] = arrayOfByte[(paramInt1 >>> 18)];
    paramArrayOfByte2[(paramInt3 + 1)] = arrayOfByte[(paramInt1 >>> 12 & 0x3F)];
    paramArrayOfByte2[(paramInt3 + 2)] = arrayOfByte[(paramInt1 >>> 6 & 0x3F)];
    paramArrayOfByte2[(paramInt3 + 3)] = arrayOfByte[(paramInt1 & 0x3F)];
    return paramArrayOfByte2;
    paramArrayOfByte2[paramInt3] = arrayOfByte[(paramInt1 >>> 18)];
    paramArrayOfByte2[(paramInt3 + 1)] = arrayOfByte[(paramInt1 >>> 12 & 0x3F)];
    paramArrayOfByte2[(paramInt3 + 2)] = arrayOfByte[(paramInt1 >>> 6 & 0x3F)];
    paramArrayOfByte2[(paramInt3 + 3)] = 61;
    return paramArrayOfByte2;
    paramArrayOfByte2[paramInt3] = arrayOfByte[(paramInt1 >>> 18)];
    paramArrayOfByte2[(paramInt3 + 1)] = arrayOfByte[(paramInt1 >>> 12 & 0x3F)];
    paramArrayOfByte2[(paramInt3 + 2)] = 61;
    paramArrayOfByte2[(paramInt3 + 3)] = 61;
    return paramArrayOfByte2;
  }
  
  private static byte[] encode3to4(byte[] paramArrayOfByte1, byte[] paramArrayOfByte2, int paramInt1, int paramInt2)
  {
    encode3to4(paramArrayOfByte2, 0, paramInt1, paramArrayOfByte1, 0, paramInt2);
    return paramArrayOfByte1;
  }
  
  public static String encodeBytes(byte[] paramArrayOfByte)
  {
    return encodeBytes(paramArrayOfByte, 0, paramArrayOfByte.length, 0);
  }
  
  public static String encodeBytes(byte[] paramArrayOfByte, int paramInt)
  {
    return encodeBytes(paramArrayOfByte, 0, paramArrayOfByte.length, paramInt);
  }
  
  public static String encodeBytes(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
  {
    return encodeBytes(paramArrayOfByte, paramInt1, paramInt2, 0);
  }
  
  /* Error */
  public static String encodeBytes(byte[] paramArrayOfByte, int paramInt1, int paramInt2, int paramInt3)
  {
    // Byte code:
    //   0: iload_3
    //   1: iconst_2
    //   2: iand
    //   3: iconst_2
    //   4: if_icmpne +154 -> 158
    //   7: new 219	java/io/ByteArrayOutputStream
    //   10: dup
    //   11: invokespecial 220	java/io/ByteArrayOutputStream:<init>	()V
    //   14: astore 8
    //   16: new 9	org/jivesoftware/smack/util/Base64$OutputStream
    //   19: dup
    //   20: aload 8
    //   22: iload_3
    //   23: iconst_1
    //   24: ior
    //   25: invokespecial 348	org/jivesoftware/smack/util/Base64$OutputStream:<init>	(Ljava/io/OutputStream;I)V
    //   28: astore 9
    //   30: new 374	java/util/zip/GZIPOutputStream
    //   33: dup
    //   34: aload 9
    //   36: invokespecial 375	java/util/zip/GZIPOutputStream:<init>	(Ljava/io/OutputStream;)V
    //   39: astore 10
    //   41: aload 10
    //   43: aload_0
    //   44: iload_1
    //   45: iload_2
    //   46: invokevirtual 376	java/util/zip/GZIPOutputStream:write	([BII)V
    //   49: aload 10
    //   51: invokevirtual 377	java/util/zip/GZIPOutputStream:close	()V
    //   54: aload 10
    //   56: invokevirtual 377	java/util/zip/GZIPOutputStream:close	()V
    //   59: aload 9
    //   61: invokevirtual 350	org/jivesoftware/smack/util/Base64$OutputStream:close	()V
    //   64: aload 8
    //   66: invokevirtual 241	java/io/ByteArrayOutputStream:close	()V
    //   69: new 209	java/lang/String
    //   72: dup
    //   73: aload 8
    //   75: invokevirtual 249	java/io/ByteArrayOutputStream:toByteArray	()[B
    //   78: ldc 35
    //   80: invokespecial 380	java/lang/String:<init>	([BLjava/lang/String;)V
    //   83: astore_0
    //   84: aload_0
    //   85: areturn
    //   86: astore 10
    //   88: aconst_null
    //   89: astore 9
    //   91: aconst_null
    //   92: astore 8
    //   94: aconst_null
    //   95: astore_0
    //   96: aload 10
    //   98: invokevirtual 307	java/io/IOException:printStackTrace	()V
    //   101: aload 9
    //   103: invokevirtual 377	java/util/zip/GZIPOutputStream:close	()V
    //   106: aload_0
    //   107: invokevirtual 350	org/jivesoftware/smack/util/Base64$OutputStream:close	()V
    //   110: aload 8
    //   112: invokevirtual 241	java/io/ByteArrayOutputStream:close	()V
    //   115: aconst_null
    //   116: areturn
    //   117: astore 10
    //   119: aconst_null
    //   120: astore 9
    //   122: aconst_null
    //   123: astore 8
    //   125: aconst_null
    //   126: astore_0
    //   127: aload 9
    //   129: invokevirtual 377	java/util/zip/GZIPOutputStream:close	()V
    //   132: aload_0
    //   133: invokevirtual 350	org/jivesoftware/smack/util/Base64$OutputStream:close	()V
    //   136: aload 8
    //   138: invokevirtual 241	java/io/ByteArrayOutputStream:close	()V
    //   141: aload 10
    //   143: athrow
    //   144: astore_0
    //   145: new 209	java/lang/String
    //   148: dup
    //   149: aload 8
    //   151: invokevirtual 249	java/io/ByteArrayOutputStream:toByteArray	()[B
    //   154: invokespecial 381	java/lang/String:<init>	([B)V
    //   157: areturn
    //   158: iload_3
    //   159: bipush 8
    //   161: iand
    //   162: ifne +129 -> 291
    //   165: iconst_1
    //   166: istore 6
    //   168: iload_2
    //   169: iconst_4
    //   170: imul
    //   171: iconst_3
    //   172: idiv
    //   173: istore 7
    //   175: iload_2
    //   176: iconst_3
    //   177: irem
    //   178: ifle +119 -> 297
    //   181: iconst_4
    //   182: istore 4
    //   184: iload 6
    //   186: ifeq +117 -> 303
    //   189: iload 7
    //   191: bipush 76
    //   193: idiv
    //   194: istore 5
    //   196: iload 5
    //   198: iload 4
    //   200: iload 7
    //   202: iadd
    //   203: iadd
    //   204: newarray byte
    //   206: astore 8
    //   208: iconst_0
    //   209: istore 4
    //   211: iconst_0
    //   212: istore 5
    //   214: iconst_0
    //   215: istore 7
    //   217: iload 7
    //   219: iload_2
    //   220: iconst_2
    //   221: isub
    //   222: if_icmpge +87 -> 309
    //   225: aload_0
    //   226: iload 7
    //   228: iload_1
    //   229: iadd
    //   230: iconst_3
    //   231: aload 8
    //   233: iload 4
    //   235: iload_3
    //   236: invokestatic 187	org/jivesoftware/smack/util/Base64:encode3to4	([BII[BII)[B
    //   239: pop
    //   240: iload 5
    //   242: iconst_4
    //   243: iadd
    //   244: istore 5
    //   246: iload 6
    //   248: ifeq +253 -> 501
    //   251: iload 5
    //   253: bipush 76
    //   255: if_icmpne +246 -> 501
    //   258: aload 8
    //   260: iload 4
    //   262: iconst_4
    //   263: iadd
    //   264: bipush 10
    //   266: bastore
    //   267: iload 4
    //   269: iconst_1
    //   270: iadd
    //   271: istore 4
    //   273: iconst_0
    //   274: istore 5
    //   276: iload 4
    //   278: iconst_4
    //   279: iadd
    //   280: istore 4
    //   282: iload 7
    //   284: iconst_3
    //   285: iadd
    //   286: istore 7
    //   288: goto -71 -> 217
    //   291: iconst_0
    //   292: istore 6
    //   294: goto -126 -> 168
    //   297: iconst_0
    //   298: istore 4
    //   300: goto -116 -> 184
    //   303: iconst_0
    //   304: istore 5
    //   306: goto -110 -> 196
    //   309: iload 4
    //   311: istore 5
    //   313: iload 7
    //   315: iload_2
    //   316: if_icmpge +27 -> 343
    //   319: aload_0
    //   320: iload 7
    //   322: iload_1
    //   323: iadd
    //   324: iload_2
    //   325: iload 7
    //   327: isub
    //   328: aload 8
    //   330: iload 4
    //   332: iload_3
    //   333: invokestatic 187	org/jivesoftware/smack/util/Base64:encode3to4	([BII[BII)[B
    //   336: pop
    //   337: iload 4
    //   339: iconst_4
    //   340: iadd
    //   341: istore 5
    //   343: new 209	java/lang/String
    //   346: dup
    //   347: aload 8
    //   349: iconst_0
    //   350: iload 5
    //   352: ldc 35
    //   354: invokespecial 384	java/lang/String:<init>	([BIILjava/lang/String;)V
    //   357: astore_0
    //   358: aload_0
    //   359: areturn
    //   360: astore_0
    //   361: new 209	java/lang/String
    //   364: dup
    //   365: aload 8
    //   367: iconst_0
    //   368: iload 5
    //   370: invokespecial 386	java/lang/String:<init>	([BII)V
    //   373: areturn
    //   374: astore_0
    //   375: goto -316 -> 59
    //   378: astore_0
    //   379: goto -315 -> 64
    //   382: astore_0
    //   383: goto -314 -> 69
    //   386: astore 9
    //   388: goto -282 -> 106
    //   391: astore_0
    //   392: goto -282 -> 110
    //   395: astore_0
    //   396: goto -281 -> 115
    //   399: astore 9
    //   401: goto -269 -> 132
    //   404: astore_0
    //   405: goto -269 -> 136
    //   408: astore_0
    //   409: goto -268 -> 141
    //   412: astore 10
    //   414: aconst_null
    //   415: astore 9
    //   417: aconst_null
    //   418: astore_0
    //   419: goto -292 -> 127
    //   422: astore 10
    //   424: aconst_null
    //   425: astore 11
    //   427: aload 9
    //   429: astore_0
    //   430: aload 11
    //   432: astore 9
    //   434: goto -307 -> 127
    //   437: astore 11
    //   439: aload 9
    //   441: astore_0
    //   442: aload 10
    //   444: astore 9
    //   446: aload 11
    //   448: astore 10
    //   450: goto -323 -> 127
    //   453: astore 10
    //   455: goto -328 -> 127
    //   458: astore 10
    //   460: aconst_null
    //   461: astore 9
    //   463: aconst_null
    //   464: astore_0
    //   465: goto -369 -> 96
    //   468: astore 10
    //   470: aconst_null
    //   471: astore 11
    //   473: aload 9
    //   475: astore_0
    //   476: aload 11
    //   478: astore 9
    //   480: goto -384 -> 96
    //   483: astore_0
    //   484: aload 10
    //   486: astore 11
    //   488: aload_0
    //   489: astore 10
    //   491: aload 9
    //   493: astore_0
    //   494: aload 11
    //   496: astore 9
    //   498: goto -402 -> 96
    //   501: goto -225 -> 276
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	504	0	paramArrayOfByte	byte[]
    //   0	504	1	paramInt1	int
    //   0	504	2	paramInt2	int
    //   0	504	3	paramInt3	int
    //   182	159	4	i	int
    //   194	175	5	j	int
    //   166	127	6	k	int
    //   173	155	7	m	int
    //   14	352	8	localObject1	Object
    //   28	100	9	localOutputStream	OutputStream
    //   386	1	9	localException1	java.lang.Exception
    //   399	1	9	localException2	java.lang.Exception
    //   415	82	9	localObject2	Object
    //   39	16	10	localGZIPOutputStream	java.util.zip.GZIPOutputStream
    //   86	11	10	localIOException1	IOException
    //   117	25	10	localObject3	Object
    //   412	1	10	localObject4	Object
    //   422	21	10	localObject5	Object
    //   448	1	10	localObject6	Object
    //   453	1	10	localObject7	Object
    //   458	1	10	localIOException2	IOException
    //   468	17	10	localIOException3	IOException
    //   489	1	10	arrayOfByte	byte[]
    //   425	6	11	localObject8	Object
    //   437	10	11	localObject9	Object
    //   471	24	11	localIOException4	IOException
    // Exception table:
    //   from	to	target	type
    //   7	16	86	java/io/IOException
    //   7	16	117	finally
    //   69	84	144	java/io/UnsupportedEncodingException
    //   343	358	360	java/io/UnsupportedEncodingException
    //   54	59	374	java/lang/Exception
    //   59	64	378	java/lang/Exception
    //   64	69	382	java/lang/Exception
    //   101	106	386	java/lang/Exception
    //   106	110	391	java/lang/Exception
    //   110	115	395	java/lang/Exception
    //   127	132	399	java/lang/Exception
    //   132	136	404	java/lang/Exception
    //   136	141	408	java/lang/Exception
    //   16	30	412	finally
    //   30	41	422	finally
    //   41	54	437	finally
    //   96	101	453	finally
    //   16	30	458	java/io/IOException
    //   30	41	468	java/io/IOException
    //   41	54	483	java/io/IOException
  }
  
  /* Error */
  public static void encodeFileToFile(String paramString1, String paramString2)
  {
    // Byte code:
    //   0: aload_0
    //   1: invokestatic 391	org/jivesoftware/smack/util/Base64:encodeFromFile	(Ljava/lang/String;)Ljava/lang/String;
    //   4: astore_2
    //   5: aconst_null
    //   6: astore_0
    //   7: aconst_null
    //   8: astore_3
    //   9: new 292	java/io/BufferedOutputStream
    //   12: dup
    //   13: new 294	java/io/FileOutputStream
    //   16: dup
    //   17: aload_1
    //   18: invokespecial 296	java/io/FileOutputStream:<init>	(Ljava/lang/String;)V
    //   21: invokespecial 299	java/io/BufferedOutputStream:<init>	(Ljava/io/OutputStream;)V
    //   24: astore_1
    //   25: aload_1
    //   26: aload_2
    //   27: ldc_w 393
    //   30: invokevirtual 212	java/lang/String:getBytes	(Ljava/lang/String;)[B
    //   33: invokevirtual 303	java/io/OutputStream:write	([B)V
    //   36: aload_1
    //   37: invokevirtual 304	java/io/OutputStream:close	()V
    //   40: return
    //   41: astore_0
    //   42: return
    //   43: astore_2
    //   44: aload_3
    //   45: astore_1
    //   46: aload_1
    //   47: astore_0
    //   48: aload_2
    //   49: invokevirtual 307	java/io/IOException:printStackTrace	()V
    //   52: aload_1
    //   53: invokevirtual 304	java/io/OutputStream:close	()V
    //   56: return
    //   57: astore_0
    //   58: return
    //   59: astore_1
    //   60: aload_0
    //   61: invokevirtual 304	java/io/OutputStream:close	()V
    //   64: aload_1
    //   65: athrow
    //   66: astore_0
    //   67: goto -3 -> 64
    //   70: astore_2
    //   71: aload_1
    //   72: astore_0
    //   73: aload_2
    //   74: astore_1
    //   75: goto -15 -> 60
    //   78: astore_2
    //   79: goto -33 -> 46
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	82	0	paramString1	String
    //   0	82	1	paramString2	String
    //   4	23	2	str	String
    //   43	6	2	localIOException1	IOException
    //   70	4	2	localObject1	Object
    //   78	1	2	localIOException2	IOException
    //   8	37	3	localObject2	Object
    // Exception table:
    //   from	to	target	type
    //   36	40	41	java/lang/Exception
    //   9	25	43	java/io/IOException
    //   52	56	57	java/lang/Exception
    //   9	25	59	finally
    //   48	52	59	finally
    //   60	64	66	java/lang/Exception
    //   25	36	70	finally
    //   25	36	78	java/io/IOException
  }
  
  /* Error */
  public static String encodeFromFile(String paramString)
  {
    // Byte code:
    //   0: aconst_null
    //   1: astore 4
    //   3: aconst_null
    //   4: astore 5
    //   6: aload 4
    //   8: astore_3
    //   9: new 309	java/io/File
    //   12: dup
    //   13: aload_0
    //   14: invokespecial 310	java/io/File:<init>	(Ljava/lang/String;)V
    //   17: astore 7
    //   19: aload 4
    //   21: astore_3
    //   22: aload 7
    //   24: invokevirtual 314	java/io/File:length	()J
    //   27: l2d
    //   28: ldc2_w 394
    //   31: dmul
    //   32: d2i
    //   33: bipush 40
    //   35: invokestatic 401	java/lang/Math:max	(II)I
    //   38: newarray byte
    //   40: astore 6
    //   42: iconst_0
    //   43: istore_1
    //   44: aload 4
    //   46: astore_3
    //   47: new 6	org/jivesoftware/smack/util/Base64$InputStream
    //   50: dup
    //   51: new 328	java/io/BufferedInputStream
    //   54: dup
    //   55: new 330	java/io/FileInputStream
    //   58: dup
    //   59: aload 7
    //   61: invokespecial 333	java/io/FileInputStream:<init>	(Ljava/io/File;)V
    //   64: invokespecial 334	java/io/BufferedInputStream:<init>	(Ljava/io/InputStream;)V
    //   67: iconst_1
    //   68: invokespecial 337	org/jivesoftware/smack/util/Base64$InputStream:<init>	(Ljava/io/InputStream;I)V
    //   71: astore 4
    //   73: aload 4
    //   75: aload 6
    //   77: iload_1
    //   78: sipush 4096
    //   81: invokevirtual 340	org/jivesoftware/smack/util/Base64$InputStream:read	([BII)I
    //   84: istore_2
    //   85: iload_2
    //   86: iflt +10 -> 96
    //   89: iload_1
    //   90: iload_2
    //   91: iadd
    //   92: istore_1
    //   93: goto -20 -> 73
    //   96: new 209	java/lang/String
    //   99: dup
    //   100: aload 6
    //   102: iconst_0
    //   103: iload_1
    //   104: ldc 35
    //   106: invokespecial 384	java/lang/String:<init>	([BIILjava/lang/String;)V
    //   109: astore_3
    //   110: aload 4
    //   112: invokevirtual 341	org/jivesoftware/smack/util/Base64$InputStream:close	()V
    //   115: aload_3
    //   116: areturn
    //   117: astore_0
    //   118: aload_3
    //   119: areturn
    //   120: astore_3
    //   121: aload 5
    //   123: astore 4
    //   125: aload 4
    //   127: astore_3
    //   128: getstatic 259	java/lang/System:err	Ljava/io/PrintStream;
    //   131: new 261	java/lang/StringBuilder
    //   134: dup
    //   135: invokespecial 262	java/lang/StringBuilder:<init>	()V
    //   138: ldc_w 403
    //   141: invokevirtual 268	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   144: aload_0
    //   145: invokevirtual 268	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   148: invokevirtual 279	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   151: invokevirtual 285	java/io/PrintStream:println	(Ljava/lang/String;)V
    //   154: aload 4
    //   156: invokevirtual 341	org/jivesoftware/smack/util/Base64$InputStream:close	()V
    //   159: aconst_null
    //   160: areturn
    //   161: astore_0
    //   162: aconst_null
    //   163: areturn
    //   164: astore_0
    //   165: aload_3
    //   166: invokevirtual 341	org/jivesoftware/smack/util/Base64$InputStream:close	()V
    //   169: aload_0
    //   170: athrow
    //   171: astore_3
    //   172: goto -3 -> 169
    //   175: astore_0
    //   176: aload 4
    //   178: astore_3
    //   179: goto -14 -> 165
    //   182: astore_3
    //   183: goto -58 -> 125
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	186	0	paramString	String
    //   43	61	1	i	int
    //   84	8	2	j	int
    //   8	111	3	localObject1	Object
    //   120	1	3	localIOException1	IOException
    //   127	39	3	localObject2	Object
    //   171	1	3	localException	java.lang.Exception
    //   178	1	3	localObject3	Object
    //   182	1	3	localIOException2	IOException
    //   1	176	4	localObject4	Object
    //   4	118	5	localObject5	Object
    //   40	61	6	arrayOfByte	byte[]
    //   17	43	7	localFile	java.io.File
    // Exception table:
    //   from	to	target	type
    //   110	115	117	java/lang/Exception
    //   9	19	120	java/io/IOException
    //   22	42	120	java/io/IOException
    //   47	73	120	java/io/IOException
    //   154	159	161	java/lang/Exception
    //   9	19	164	finally
    //   22	42	164	finally
    //   47	73	164	finally
    //   128	154	164	finally
    //   165	169	171	java/lang/Exception
    //   73	85	175	finally
    //   96	110	175	finally
    //   73	85	182	java/io/IOException
    //   96	110	182	java/io/IOException
  }
  
  public static String encodeObject(Serializable paramSerializable)
  {
    return encodeObject(paramSerializable, 0);
  }
  
  /* Error */
  public static String encodeObject(Serializable paramSerializable, int paramInt)
  {
    // Byte code:
    //   0: new 219	java/io/ByteArrayOutputStream
    //   3: dup
    //   4: invokespecial 220	java/io/ByteArrayOutputStream:<init>	()V
    //   7: astore_2
    //   8: new 9	org/jivesoftware/smack/util/Base64$OutputStream
    //   11: dup
    //   12: aload_2
    //   13: iload_1
    //   14: iconst_1
    //   15: ior
    //   16: invokespecial 348	org/jivesoftware/smack/util/Base64$OutputStream:<init>	(Ljava/io/OutputStream;I)V
    //   19: astore_3
    //   20: iload_1
    //   21: iconst_2
    //   22: iand
    //   23: iconst_2
    //   24: if_icmpne +64 -> 88
    //   27: new 374	java/util/zip/GZIPOutputStream
    //   30: dup
    //   31: aload_3
    //   32: invokespecial 375	java/util/zip/GZIPOutputStream:<init>	(Ljava/io/OutputStream;)V
    //   35: astore 5
    //   37: new 410	java/io/ObjectOutputStream
    //   40: dup
    //   41: aload 5
    //   43: invokespecial 411	java/io/ObjectOutputStream:<init>	(Ljava/io/OutputStream;)V
    //   46: astore 4
    //   48: aload 4
    //   50: aload_0
    //   51: invokevirtual 415	java/io/ObjectOutputStream:writeObject	(Ljava/lang/Object;)V
    //   54: aload 4
    //   56: invokevirtual 416	java/io/ObjectOutputStream:close	()V
    //   59: aload 5
    //   61: invokevirtual 377	java/util/zip/GZIPOutputStream:close	()V
    //   64: aload_3
    //   65: invokevirtual 304	java/io/OutputStream:close	()V
    //   68: aload_2
    //   69: invokevirtual 241	java/io/ByteArrayOutputStream:close	()V
    //   72: new 209	java/lang/String
    //   75: dup
    //   76: aload_2
    //   77: invokevirtual 249	java/io/ByteArrayOutputStream:toByteArray	()[B
    //   80: ldc 35
    //   82: invokespecial 380	java/lang/String:<init>	([BLjava/lang/String;)V
    //   85: astore_0
    //   86: aload_0
    //   87: areturn
    //   88: new 410	java/io/ObjectOutputStream
    //   91: dup
    //   92: aload_3
    //   93: invokespecial 411	java/io/ObjectOutputStream:<init>	(Ljava/io/OutputStream;)V
    //   96: astore 4
    //   98: aconst_null
    //   99: astore 5
    //   101: goto -53 -> 48
    //   104: astore 4
    //   106: aconst_null
    //   107: astore_3
    //   108: aconst_null
    //   109: astore 5
    //   111: aconst_null
    //   112: astore_0
    //   113: aconst_null
    //   114: astore_2
    //   115: aload 4
    //   117: invokevirtual 307	java/io/IOException:printStackTrace	()V
    //   120: aload_3
    //   121: invokevirtual 416	java/io/ObjectOutputStream:close	()V
    //   124: aload_0
    //   125: invokevirtual 377	java/util/zip/GZIPOutputStream:close	()V
    //   128: aload_2
    //   129: invokevirtual 304	java/io/OutputStream:close	()V
    //   132: aload 5
    //   134: invokevirtual 241	java/io/ByteArrayOutputStream:close	()V
    //   137: aconst_null
    //   138: areturn
    //   139: astore 4
    //   141: aconst_null
    //   142: astore 5
    //   144: aconst_null
    //   145: astore_3
    //   146: aconst_null
    //   147: astore_2
    //   148: aconst_null
    //   149: astore_0
    //   150: aload_3
    //   151: invokevirtual 416	java/io/ObjectOutputStream:close	()V
    //   154: aload_0
    //   155: invokevirtual 377	java/util/zip/GZIPOutputStream:close	()V
    //   158: aload_2
    //   159: invokevirtual 304	java/io/OutputStream:close	()V
    //   162: aload 5
    //   164: invokevirtual 241	java/io/ByteArrayOutputStream:close	()V
    //   167: aload 4
    //   169: athrow
    //   170: astore_0
    //   171: new 209	java/lang/String
    //   174: dup
    //   175: aload_2
    //   176: invokevirtual 249	java/io/ByteArrayOutputStream:toByteArray	()[B
    //   179: invokespecial 381	java/lang/String:<init>	([B)V
    //   182: areturn
    //   183: astore_0
    //   184: goto -125 -> 59
    //   187: astore_0
    //   188: goto -124 -> 64
    //   191: astore_0
    //   192: goto -124 -> 68
    //   195: astore_0
    //   196: goto -124 -> 72
    //   199: astore_3
    //   200: goto -76 -> 124
    //   203: astore_0
    //   204: goto -76 -> 128
    //   207: astore_0
    //   208: goto -76 -> 132
    //   211: astore_0
    //   212: goto -75 -> 137
    //   215: astore_3
    //   216: goto -62 -> 154
    //   219: astore_0
    //   220: goto -62 -> 158
    //   223: astore_0
    //   224: goto -62 -> 162
    //   227: astore_0
    //   228: goto -61 -> 167
    //   231: astore 4
    //   233: aconst_null
    //   234: astore_3
    //   235: aload_2
    //   236: astore 5
    //   238: aconst_null
    //   239: astore_2
    //   240: aconst_null
    //   241: astore_0
    //   242: goto -92 -> 150
    //   245: astore 4
    //   247: aconst_null
    //   248: astore_0
    //   249: aload_2
    //   250: astore 5
    //   252: aload_3
    //   253: astore_2
    //   254: aconst_null
    //   255: astore_3
    //   256: goto -106 -> 150
    //   259: astore 4
    //   261: aload 5
    //   263: astore_0
    //   264: aload_2
    //   265: astore 5
    //   267: aload_3
    //   268: astore_2
    //   269: aconst_null
    //   270: astore_3
    //   271: goto -121 -> 150
    //   274: astore 7
    //   276: aload_3
    //   277: astore 6
    //   279: aload 4
    //   281: astore_3
    //   282: aload 5
    //   284: astore_0
    //   285: aload_2
    //   286: astore 5
    //   288: aload 7
    //   290: astore 4
    //   292: aload 6
    //   294: astore_2
    //   295: goto -145 -> 150
    //   298: astore 4
    //   300: goto -150 -> 150
    //   303: astore 4
    //   305: aconst_null
    //   306: astore_0
    //   307: aload_2
    //   308: astore 5
    //   310: aconst_null
    //   311: astore_3
    //   312: aconst_null
    //   313: astore_2
    //   314: goto -199 -> 115
    //   317: astore 4
    //   319: aload_2
    //   320: astore 5
    //   322: aconst_null
    //   323: astore 6
    //   325: aload_3
    //   326: astore_2
    //   327: aconst_null
    //   328: astore_0
    //   329: aload 6
    //   331: astore_3
    //   332: goto -217 -> 115
    //   335: astore 4
    //   337: aload_3
    //   338: astore 6
    //   340: aload 5
    //   342: astore_0
    //   343: aload_2
    //   344: astore 5
    //   346: aconst_null
    //   347: astore_3
    //   348: aload 6
    //   350: astore_2
    //   351: goto -236 -> 115
    //   354: astore 7
    //   356: aload_3
    //   357: astore 6
    //   359: aload 5
    //   361: astore_0
    //   362: aload_2
    //   363: astore 5
    //   365: aload 4
    //   367: astore_3
    //   368: aload 7
    //   370: astore 4
    //   372: aload 6
    //   374: astore_2
    //   375: goto -260 -> 115
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	378	0	paramSerializable	Serializable
    //   0	378	1	paramInt	int
    //   7	368	2	localObject1	Object
    //   19	132	3	localOutputStream	OutputStream
    //   199	1	3	localException1	java.lang.Exception
    //   215	1	3	localException2	java.lang.Exception
    //   234	134	3	localObject2	Object
    //   46	51	4	localObjectOutputStream	java.io.ObjectOutputStream
    //   104	12	4	localIOException1	IOException
    //   139	29	4	localObject3	Object
    //   231	1	4	localObject4	Object
    //   245	1	4	localObject5	Object
    //   259	21	4	localObject6	Object
    //   290	1	4	localObject7	Object
    //   298	1	4	localObject8	Object
    //   303	1	4	localIOException2	IOException
    //   317	1	4	localIOException3	IOException
    //   335	31	4	localIOException4	IOException
    //   370	1	4	localObject9	Object
    //   35	329	5	localObject10	Object
    //   277	96	6	localObject11	Object
    //   274	15	7	localObject12	Object
    //   354	15	7	localIOException5	IOException
    // Exception table:
    //   from	to	target	type
    //   0	8	104	java/io/IOException
    //   0	8	139	finally
    //   72	86	170	java/io/UnsupportedEncodingException
    //   54	59	183	java/lang/Exception
    //   59	64	187	java/lang/Exception
    //   64	68	191	java/lang/Exception
    //   68	72	195	java/lang/Exception
    //   120	124	199	java/lang/Exception
    //   124	128	203	java/lang/Exception
    //   128	132	207	java/lang/Exception
    //   132	137	211	java/lang/Exception
    //   150	154	215	java/lang/Exception
    //   154	158	219	java/lang/Exception
    //   158	162	223	java/lang/Exception
    //   162	167	227	java/lang/Exception
    //   8	20	231	finally
    //   27	37	245	finally
    //   88	98	245	finally
    //   37	48	259	finally
    //   48	54	274	finally
    //   115	120	298	finally
    //   8	20	303	java/io/IOException
    //   27	37	317	java/io/IOException
    //   88	98	317	java/io/IOException
    //   37	48	335	java/io/IOException
    //   48	54	354	java/io/IOException
  }
  
  /* Error */
  public static boolean encodeToFile(byte[] paramArrayOfByte, String paramString)
  {
    // Byte code:
    //   0: aconst_null
    //   1: astore_2
    //   2: aconst_null
    //   3: astore_3
    //   4: new 9	org/jivesoftware/smack/util/Base64$OutputStream
    //   7: dup
    //   8: new 294	java/io/FileOutputStream
    //   11: dup
    //   12: aload_1
    //   13: invokespecial 296	java/io/FileOutputStream:<init>	(Ljava/lang/String;)V
    //   16: iconst_1
    //   17: invokespecial 348	org/jivesoftware/smack/util/Base64$OutputStream:<init>	(Ljava/io/OutputStream;I)V
    //   20: astore_1
    //   21: aload_1
    //   22: aload_0
    //   23: invokevirtual 349	org/jivesoftware/smack/util/Base64$OutputStream:write	([B)V
    //   26: aload_1
    //   27: invokevirtual 350	org/jivesoftware/smack/util/Base64$OutputStream:close	()V
    //   30: iconst_1
    //   31: ireturn
    //   32: astore_0
    //   33: iconst_1
    //   34: ireturn
    //   35: astore_0
    //   36: aload_3
    //   37: astore_0
    //   38: aload_0
    //   39: invokevirtual 350	org/jivesoftware/smack/util/Base64$OutputStream:close	()V
    //   42: iconst_0
    //   43: ireturn
    //   44: astore_0
    //   45: iconst_0
    //   46: ireturn
    //   47: astore_0
    //   48: aload_2
    //   49: astore_1
    //   50: aload_1
    //   51: invokevirtual 350	org/jivesoftware/smack/util/Base64$OutputStream:close	()V
    //   54: aload_0
    //   55: athrow
    //   56: astore_1
    //   57: goto -3 -> 54
    //   60: astore_0
    //   61: goto -11 -> 50
    //   64: astore_0
    //   65: aload_1
    //   66: astore_0
    //   67: goto -29 -> 38
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	70	0	paramArrayOfByte	byte[]
    //   0	70	1	paramString	String
    //   1	48	2	localObject1	Object
    //   3	34	3	localObject2	Object
    // Exception table:
    //   from	to	target	type
    //   26	30	32	java/lang/Exception
    //   4	21	35	java/io/IOException
    //   38	42	44	java/lang/Exception
    //   4	21	47	finally
    //   50	54	56	java/lang/Exception
    //   21	26	60	finally
    //   21	26	64	java/io/IOException
  }
  
  private static final byte[] getAlphabet(int paramInt)
  {
    if ((paramInt & 0x10) == 16) {
      return _URL_SAFE_ALPHABET;
    }
    if ((paramInt & 0x20) == 32) {
      return _ORDERED_ALPHABET;
    }
    return _STANDARD_ALPHABET;
  }
  
  private static final byte[] getDecodabet(int paramInt)
  {
    if ((paramInt & 0x10) == 16) {
      return _URL_SAFE_DECODABET;
    }
    if ((paramInt & 0x20) == 32) {
      return _ORDERED_DECODABET;
    }
    return _STANDARD_DECODABET;
  }
  
  public static final void main(String[] paramArrayOfString)
  {
    if (paramArrayOfString.length < 3)
    {
      usage("Not enough arguments.");
      return;
    }
    String str1 = paramArrayOfString[0];
    String str2 = paramArrayOfString[1];
    paramArrayOfString = paramArrayOfString[2];
    if (str1.equals("-e"))
    {
      encodeFileToFile(str2, paramArrayOfString);
      return;
    }
    if (str1.equals("-d"))
    {
      decodeFileToFile(str2, paramArrayOfString);
      return;
    }
    usage("Unknown flag: " + str1);
  }
  
  private static final void usage(String paramString)
  {
    System.err.println(paramString);
    System.err.println("Usage: java Base64 -e|-d inputfile outputfile");
  }
  
  public static class InputStream
    extends FilterInputStream
  {
    private byte[] alphabet;
    private boolean breakLines;
    private byte[] buffer;
    private int bufferLength;
    private byte[] decodabet;
    private boolean encode;
    private int lineLength;
    private int numSigBytes;
    private int options;
    private int position;
    
    public InputStream(InputStream paramInputStream)
    {
      this(paramInputStream, 0);
    }
    
    public InputStream(InputStream paramInputStream, int paramInt)
    {
      super();
      boolean bool;
      if ((paramInt & 0x8) != 8)
      {
        bool = true;
        this.breakLines = bool;
        if ((paramInt & 0x1) != 1) {
          break label101;
        }
        bool = true;
        label33:
        this.encode = bool;
        if (!this.encode) {
          break label107;
        }
      }
      label101:
      label107:
      for (int i = 4;; i = 3)
      {
        this.bufferLength = i;
        this.buffer = new byte[this.bufferLength];
        this.position = -1;
        this.lineLength = 0;
        this.options = paramInt;
        this.alphabet = Base64.getAlphabet(paramInt);
        this.decodabet = Base64.getDecodabet(paramInt);
        return;
        bool = false;
        break;
        bool = false;
        break label33;
      }
    }
    
    public int read()
      throws IOException
    {
      int j;
      if (this.position < 0)
      {
        if (!this.encode) {
          break label128;
        }
        arrayOfByte = new byte[3];
        j = 0;
        i = 0;
        while (i < 3)
        {
          try
          {
            int m = this.in.read();
            k = j;
            if (m >= 0)
            {
              arrayOfByte[i] = ((byte)m);
              k = j + 1;
            }
          }
          catch (IOException localIOException)
          {
            do
            {
              int k = j;
            } while (i != 0);
            throw localIOException;
          }
          i += 1;
          j = k;
        }
        if (j > 0)
        {
          Base64.encode3to4(arrayOfByte, 0, j, this.buffer, 0, this.options);
          this.position = 0;
          this.numSigBytes = 4;
        }
      }
      else
      {
        if (this.position < 0) {
          break label317;
        }
        if (this.position < this.numSigBytes) {
          break label231;
        }
        return -1;
      }
      return -1;
      label128:
      byte[] arrayOfByte = new byte[4];
      int i = 0;
      for (;;)
      {
        if (i < 4)
        {
          do
          {
            j = this.in.read();
          } while ((j >= 0) && (this.decodabet[(j & 0x7F)] <= -5));
          if (j >= 0) {}
        }
        else
        {
          if (i != 4) {
            break label215;
          }
          this.numSigBytes = Base64.decode4to3(arrayOfByte, 0, this.buffer, 0, this.options);
          this.position = 0;
          break;
        }
        arrayOfByte[i] = ((byte)j);
        i += 1;
      }
      label215:
      if (i == 0) {
        return -1;
      }
      throw new IOException("Improperly padded Base64 input.");
      label231:
      if ((this.encode) && (this.breakLines) && (this.lineLength >= 76))
      {
        this.lineLength = 0;
        return 10;
      }
      this.lineLength += 1;
      arrayOfByte = this.buffer;
      i = this.position;
      this.position = (i + 1);
      i = arrayOfByte[i];
      if (this.position >= this.bufferLength) {
        this.position = -1;
      }
      return i & 0xFF;
      label317:
      throw new IOException("Error in Base64 code reading stream.");
    }
    
    public int read(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
      throws IOException
    {
      int i = 0;
      while (i < paramInt2)
      {
        int j = read();
        if (j >= 0)
        {
          paramArrayOfByte[(paramInt1 + i)] = ((byte)j);
          i += 1;
        }
        else if (i == 0)
        {
          return -1;
        }
      }
      return i;
    }
  }
  
  public static class OutputStream
    extends FilterOutputStream
  {
    private byte[] alphabet;
    private byte[] b4;
    private boolean breakLines;
    private byte[] buffer;
    private int bufferLength;
    private byte[] decodabet;
    private boolean encode;
    private int lineLength;
    private int options;
    private int position;
    private boolean suspendEncoding;
    
    public OutputStream(OutputStream paramOutputStream)
    {
      this(paramOutputStream, 1);
    }
    
    public OutputStream(OutputStream paramOutputStream, int paramInt)
    {
      super();
      boolean bool;
      if ((paramInt & 0x8) != 8)
      {
        bool = true;
        this.breakLines = bool;
        if ((paramInt & 0x1) != 1) {
          break label113;
        }
        bool = true;
        label33:
        this.encode = bool;
        if (!this.encode) {
          break label119;
        }
      }
      label113:
      label119:
      for (int i = 3;; i = 4)
      {
        this.bufferLength = i;
        this.buffer = new byte[this.bufferLength];
        this.position = 0;
        this.lineLength = 0;
        this.suspendEncoding = false;
        this.b4 = new byte[4];
        this.options = paramInt;
        this.alphabet = Base64.getAlphabet(paramInt);
        this.decodabet = Base64.getDecodabet(paramInt);
        return;
        bool = false;
        break;
        bool = false;
        break label33;
      }
    }
    
    public void close()
      throws IOException
    {
      flushBase64();
      super.close();
      this.buffer = null;
      this.out = null;
    }
    
    public void flushBase64()
      throws IOException
    {
      if (this.position > 0)
      {
        if (this.encode)
        {
          this.out.write(Base64.encode3to4(this.b4, this.buffer, this.position, this.options));
          this.position = 0;
        }
      }
      else {
        return;
      }
      throw new IOException("Base64 input not properly padded.");
    }
    
    public void resumeEncoding()
    {
      this.suspendEncoding = false;
    }
    
    public void suspendEncoding()
      throws IOException
    {
      flushBase64();
      this.suspendEncoding = true;
    }
    
    public void write(int paramInt)
      throws IOException
    {
      if (this.suspendEncoding) {
        this.out.write(paramInt);
      }
      do
      {
        do
        {
          do
          {
            return;
            if (!this.encode) {
              break;
            }
            arrayOfByte = this.buffer;
            i = this.position;
            this.position = (i + 1);
            arrayOfByte[i] = ((byte)paramInt);
          } while (this.position < this.bufferLength);
          this.out.write(Base64.encode3to4(this.b4, this.buffer, this.bufferLength, this.options));
          this.lineLength += 4;
          if ((this.breakLines) && (this.lineLength >= 76))
          {
            this.out.write(10);
            this.lineLength = 0;
          }
          this.position = 0;
          return;
          if (this.decodabet[(paramInt & 0x7F)] <= -5) {
            break;
          }
          byte[] arrayOfByte = this.buffer;
          int i = this.position;
          this.position = (i + 1);
          arrayOfByte[i] = ((byte)paramInt);
        } while (this.position < this.bufferLength);
        paramInt = Base64.decode4to3(this.buffer, 0, this.b4, 0, this.options);
        this.out.write(this.b4, 0, paramInt);
        this.position = 0;
        return;
      } while (this.decodabet[(paramInt & 0x7F)] == -5);
      throw new IOException("Invalid character in Base64 data.");
    }
    
    public void write(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
      throws IOException
    {
      if (this.suspendEncoding) {
        this.out.write(paramArrayOfByte, paramInt1, paramInt2);
      }
      for (;;)
      {
        return;
        int i = 0;
        while (i < paramInt2)
        {
          write(paramArrayOfByte[(paramInt1 + i)]);
          i += 1;
        }
      }
    }
  }
}


/* Location:           C:\Users\Administrator\Documents\ds\反编译\AndroidKiller_v1.3.1\projects\Jungledsq_ppc\ProjectSrc\smali\
 * Qualified Name:     org.jivesoftware.smack.util.Base64
 * JD-Core Version:    0.7.0.1
 */