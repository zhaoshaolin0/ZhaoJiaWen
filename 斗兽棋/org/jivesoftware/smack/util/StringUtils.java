package org.jivesoftware.smack.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.util.Random;

public class StringUtils
{
  private static final char[] AMP_ENCODE;
  private static final char[] GT_ENCODE;
  private static final char[] LT_ENCODE;
  private static final char[] QUOTE_ENCODE = "&quot;".toCharArray();
  private static MessageDigest digest;
  private static char[] numbersAndLetters = "0123456789abcdefghijklmnopqrstuvwxyz0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
  private static Random randGen;
  
  static
  {
    AMP_ENCODE = "&amp;".toCharArray();
    LT_ENCODE = "&lt;".toCharArray();
    GT_ENCODE = "&gt;".toCharArray();
    digest = null;
    randGen = new Random();
  }
  
  public static byte[] decodeBase64(String paramString)
  {
    return Base64.decode(paramString);
  }
  
  public static String encodeBase64(String paramString)
  {
    Object localObject = null;
    try
    {
      paramString = paramString.getBytes("ISO-8859-1");
      return encodeBase64(paramString);
    }
    catch (UnsupportedEncodingException paramString)
    {
      for (;;)
      {
        paramString.printStackTrace();
        paramString = localObject;
      }
    }
  }
  
  public static String encodeBase64(byte[] paramArrayOfByte)
  {
    return encodeBase64(paramArrayOfByte, false);
  }
  
  public static String encodeBase64(byte[] paramArrayOfByte, int paramInt1, int paramInt2, boolean paramBoolean)
  {
    if (paramBoolean) {}
    for (int i = 0;; i = 8) {
      return Base64.encodeBytes(paramArrayOfByte, paramInt1, paramInt2, i);
    }
  }
  
  public static String encodeBase64(byte[] paramArrayOfByte, boolean paramBoolean)
  {
    return encodeBase64(paramArrayOfByte, 0, paramArrayOfByte.length, paramBoolean);
  }
  
  public static String encodeHex(byte[] paramArrayOfByte)
  {
    StringBuilder localStringBuilder = new StringBuilder(paramArrayOfByte.length * 2);
    int j = paramArrayOfByte.length;
    int i = 0;
    while (i < j)
    {
      int k = paramArrayOfByte[i];
      if ((k & 0xFF) < 16) {
        localStringBuilder.append("0");
      }
      localStringBuilder.append(Integer.toString(k & 0xFF, 16));
      i += 1;
    }
    return localStringBuilder.toString();
  }
  
  public static String escapeForXML(String paramString)
  {
    if (paramString == null) {
      return null;
    }
    int j = 0;
    int k = 0;
    char[] arrayOfChar = paramString.toCharArray();
    int m = arrayOfChar.length;
    StringBuilder localStringBuilder = new StringBuilder((int)(m * 1.3D));
    if (j < m)
    {
      int n = arrayOfChar[j];
      int i;
      if (n > 62) {
        i = k;
      }
      for (;;)
      {
        j += 1;
        k = i;
        break;
        if (n == 60)
        {
          if (j > k) {
            localStringBuilder.append(arrayOfChar, k, j - k);
          }
          i = j + 1;
          localStringBuilder.append(LT_ENCODE);
        }
        else if (n == 62)
        {
          if (j > k) {
            localStringBuilder.append(arrayOfChar, k, j - k);
          }
          i = j + 1;
          localStringBuilder.append(GT_ENCODE);
        }
        else if (n == 38)
        {
          if (j > k) {
            localStringBuilder.append(arrayOfChar, k, j - k);
          }
          if ((m > j + 5) && (arrayOfChar[(j + 1)] == '#') && (Character.isDigit(arrayOfChar[(j + 2)])) && (Character.isDigit(arrayOfChar[(j + 3)])) && (Character.isDigit(arrayOfChar[(j + 4)])))
          {
            i = k;
            if (arrayOfChar[(j + 5)] == ';') {}
          }
          else
          {
            i = j + 1;
            localStringBuilder.append(AMP_ENCODE);
          }
        }
        else
        {
          i = k;
          if (n == 34)
          {
            if (j > k) {
              localStringBuilder.append(arrayOfChar, k, j - k);
            }
            i = j + 1;
            localStringBuilder.append(QUOTE_ENCODE);
          }
        }
      }
    }
    if (k == 0) {
      return paramString;
    }
    if (j > k) {
      localStringBuilder.append(arrayOfChar, k, j - k);
    }
    return localStringBuilder.toString();
  }
  
  public static String escapeNode(String paramString)
  {
    if (paramString == null) {
      return null;
    }
    StringBuilder localStringBuilder = new StringBuilder(paramString.length() + 8);
    int i = 0;
    int j = paramString.length();
    if (i < j)
    {
      char c = paramString.charAt(i);
      switch (c)
      {
      default: 
        if (Character.isWhitespace(c)) {
          localStringBuilder.append("\\20");
        }
        break;
      }
      for (;;)
      {
        i += 1;
        break;
        localStringBuilder.append("\\22");
        continue;
        localStringBuilder.append("\\26");
        continue;
        localStringBuilder.append("\\27");
        continue;
        localStringBuilder.append("\\2f");
        continue;
        localStringBuilder.append("\\3a");
        continue;
        localStringBuilder.append("\\3c");
        continue;
        localStringBuilder.append("\\3e");
        continue;
        localStringBuilder.append("\\40");
        continue;
        localStringBuilder.append("\\5c");
        continue;
        localStringBuilder.append(c);
      }
    }
    return localStringBuilder.toString();
  }
  
  /* Error */
  public static String hash(String paramString)
  {
    // Byte code:
    //   0: ldc 2
    //   2: monitorenter
    //   3: getstatic 40	org/jivesoftware/smack/util/StringUtils:digest	Ljava/security/MessageDigest;
    //   6: astore_1
    //   7: aload_1
    //   8: ifnonnull +11 -> 19
    //   11: ldc 161
    //   13: invokestatic 167	java/security/MessageDigest:getInstance	(Ljava/lang/String;)Ljava/security/MessageDigest;
    //   16: putstatic 40	org/jivesoftware/smack/util/StringUtils:digest	Ljava/security/MessageDigest;
    //   19: getstatic 40	org/jivesoftware/smack/util/StringUtils:digest	Ljava/security/MessageDigest;
    //   22: aload_0
    //   23: ldc 169
    //   25: invokevirtual 69	java/lang/String:getBytes	(Ljava/lang/String;)[B
    //   28: invokevirtual 173	java/security/MessageDigest:update	([B)V
    //   31: getstatic 40	org/jivesoftware/smack/util/StringUtils:digest	Ljava/security/MessageDigest;
    //   34: invokevirtual 176	java/security/MessageDigest:digest	()[B
    //   37: invokestatic 178	org/jivesoftware/smack/util/StringUtils:encodeHex	([B)Ljava/lang/String;
    //   40: astore_0
    //   41: ldc 2
    //   43: monitorexit
    //   44: aload_0
    //   45: areturn
    //   46: astore_1
    //   47: getstatic 184	java/lang/System:err	Ljava/io/PrintStream;
    //   50: ldc 186
    //   52: invokevirtual 192	java/io/PrintStream:println	(Ljava/lang/String;)V
    //   55: goto -36 -> 19
    //   58: astore_0
    //   59: ldc 2
    //   61: monitorexit
    //   62: aload_0
    //   63: athrow
    //   64: astore_0
    //   65: getstatic 184	java/lang/System:err	Ljava/io/PrintStream;
    //   68: aload_0
    //   69: invokevirtual 195	java/io/PrintStream:println	(Ljava/lang/Object;)V
    //   72: goto -41 -> 31
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	75	0	paramString	String
    //   6	2	1	localMessageDigest	MessageDigest
    //   46	1	1	localNoSuchAlgorithmException	java.security.NoSuchAlgorithmException
    // Exception table:
    //   from	to	target	type
    //   11	19	46	java/security/NoSuchAlgorithmException
    //   3	7	58	finally
    //   11	19	58	finally
    //   19	31	58	finally
    //   31	41	58	finally
    //   47	55	58	finally
    //   65	72	58	finally
    //   19	31	64	java/io/UnsupportedEncodingException
  }
  
  public static String parseBareAddress(String paramString)
  {
    if (paramString == null) {
      return null;
    }
    int i = paramString.indexOf("/");
    if (i < 0) {
      return paramString;
    }
    if (i == 0) {
      return "";
    }
    return paramString.substring(0, i);
  }
  
  public static String parseName(String paramString)
  {
    if (paramString == null) {
      return null;
    }
    int i = paramString.lastIndexOf("@");
    if (i <= 0) {
      return "";
    }
    return paramString.substring(0, i);
  }
  
  public static String parseResource(String paramString)
  {
    if (paramString == null) {
      return null;
    }
    int i = paramString.indexOf("/");
    if ((i + 1 > paramString.length()) || (i < 0)) {
      return "";
    }
    return paramString.substring(i + 1);
  }
  
  public static String parseServer(String paramString)
  {
    if (paramString == null) {
      return null;
    }
    int i = paramString.lastIndexOf("@");
    if (i + 1 > paramString.length()) {
      return "";
    }
    int j = paramString.indexOf("/");
    if ((j > 0) && (j > i)) {
      return paramString.substring(i + 1, j);
    }
    return paramString.substring(i + 1);
  }
  
  public static String randomString(int paramInt)
  {
    if (paramInt < 1) {
      return null;
    }
    char[] arrayOfChar = new char[paramInt];
    paramInt = 0;
    while (paramInt < arrayOfChar.length)
    {
      arrayOfChar[paramInt] = numbersAndLetters[randGen.nextInt(71)];
      paramInt += 1;
    }
    return new String(arrayOfChar);
  }
  
  public static String unescapeNode(String paramString)
  {
    if (paramString == null) {
      return null;
    }
    char[] arrayOfChar = paramString.toCharArray();
    StringBuilder localStringBuilder = new StringBuilder(arrayOfChar.length);
    int i = 0;
    int j = arrayOfChar.length;
    if (i < j)
    {
      char c = paramString.charAt(i);
      int k;
      int m;
      if ((c == '\\') && (i + 2 < j))
      {
        k = arrayOfChar[(i + 1)];
        m = arrayOfChar[(i + 2)];
        if (k != 50) {}
      }
      else
      {
        switch (m)
        {
        default: 
          label128:
          localStringBuilder.append(c);
        }
      }
      for (;;)
      {
        i += 1;
        break;
        localStringBuilder.append(' ');
        i += 2;
        continue;
        localStringBuilder.append('"');
        i += 2;
        continue;
        localStringBuilder.append('&');
        i += 2;
        continue;
        localStringBuilder.append('\'');
        i += 2;
        continue;
        localStringBuilder.append('/');
        i += 2;
        continue;
        if (k == 51) {
          switch (m)
          {
          case 98: 
          case 100: 
          default: 
            break;
          case 97: 
            localStringBuilder.append(':');
            i += 2;
            break;
          case 99: 
            localStringBuilder.append('<');
            i += 2;
            break;
          case 101: 
            localStringBuilder.append('>');
            i += 2;
            break;
          }
        }
        if (k == 52)
        {
          if (m != 48) {
            break label128;
          }
          localStringBuilder.append("@");
          i += 2;
          continue;
        }
        if ((k != 53) || (m != 99)) {
          break label128;
        }
        localStringBuilder.append("\\");
        i += 2;
      }
    }
    return localStringBuilder.toString();
  }
}


/* Location:           C:\Users\Administrator\Documents\ds\反编译\AndroidKiller_v1.3.1\projects\Jungledsq_ppc\ProjectSrc\smali\
 * Qualified Name:     org.jivesoftware.smack.util.StringUtils
 * JD-Core Version:    0.7.0.1
 */