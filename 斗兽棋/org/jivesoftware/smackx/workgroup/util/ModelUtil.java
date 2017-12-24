package org.jivesoftware.smackx.workgroup.util;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public final class ModelUtil
{
  public static final boolean areBooleansDifferent(Boolean paramBoolean1, Boolean paramBoolean2)
  {
    return !areBooleansEqual(paramBoolean1, paramBoolean2);
  }
  
  public static final boolean areBooleansEqual(Boolean paramBoolean1, Boolean paramBoolean2)
  {
    return ((paramBoolean1 == Boolean.TRUE) && (paramBoolean2 == Boolean.TRUE)) || ((paramBoolean1 != Boolean.TRUE) && (paramBoolean2 != Boolean.TRUE));
  }
  
  public static final boolean areDifferent(Object paramObject1, Object paramObject2)
  {
    return !areEqual(paramObject1, paramObject2);
  }
  
  public static final boolean areEqual(Object paramObject1, Object paramObject2)
  {
    if (paramObject1 == paramObject2) {
      return true;
    }
    if ((paramObject1 == null) || (paramObject2 == null)) {
      return false;
    }
    return paramObject1.equals(paramObject2);
  }
  
  public static final String concat(String[] paramArrayOfString)
  {
    return concat(paramArrayOfString, " ");
  }
  
  public static final String concat(String[] paramArrayOfString, String paramString)
  {
    if (paramArrayOfString != null)
    {
      StringBuilder localStringBuilder = new StringBuilder();
      int j = paramArrayOfString.length;
      int i = 0;
      while (i < j)
      {
        String str = paramArrayOfString[i];
        if (str != null) {
          localStringBuilder.append(str).append(paramString);
        }
        i += 1;
      }
      i = localStringBuilder.length();
      if (i > 0) {
        localStringBuilder.setLength(i - 1);
      }
      return localStringBuilder.toString();
    }
    return "";
  }
  
  public static String getTimeFromLong(long paramLong)
  {
    new Date();
    long l1 = paramLong / 86400000L;
    l1 = paramLong % 86400000L;
    paramLong = l1 / 3600000L;
    long l2 = l1 % 3600000L;
    l1 = l2 / 60000L;
    l2 %= 60000L;
    l2 /= 1000L;
    StringBuilder localStringBuilder = new StringBuilder();
    if (paramLong > 0L) {
      localStringBuilder.append(paramLong + " " + "h" + ", ");
    }
    if (l1 > 0L) {
      localStringBuilder.append(l1 + " " + "min" + ", ");
    }
    localStringBuilder.append(l2 + " " + "sec");
    return localStringBuilder.toString();
  }
  
  public static final boolean hasLength(String paramString)
  {
    return (paramString != null) && (paramString.length() > 0);
  }
  
  public static final boolean hasNonNullElement(Object[] paramArrayOfObject)
  {
    if (paramArrayOfObject != null)
    {
      int j = paramArrayOfObject.length;
      int i = 0;
      while (i < j)
      {
        if (paramArrayOfObject[i] != null) {
          return true;
        }
        i += 1;
      }
    }
    return false;
  }
  
  public static boolean hasStringChanged(String paramString1, String paramString2)
  {
    if ((paramString1 == null) && (paramString2 == null)) {
      return false;
    }
    if (((paramString1 == null) && (paramString2 != null)) || ((paramString1 != null) && (paramString2 == null))) {
      return true;
    }
    return !paramString1.equals(paramString2);
  }
  
  public static List iteratorAsList(Iterator paramIterator)
  {
    ArrayList localArrayList = new ArrayList(10);
    while (paramIterator.hasNext()) {
      localArrayList.add(paramIterator.next());
    }
    return localArrayList;
  }
  
  public static final String nullifyIfEmpty(String paramString)
  {
    if (hasLength(paramString)) {
      return paramString;
    }
    return null;
  }
  
  public static final String nullifyingToString(Object paramObject)
  {
    if (paramObject != null) {
      return nullifyIfEmpty(paramObject.toString());
    }
    return null;
  }
  
  public static Iterator reverseListIterator(ListIterator paramListIterator)
  {
    return new ReverseListIterator(paramListIterator);
  }
}


/* Location:           C:\Users\Administrator\Documents\ds\反编译\AndroidKiller_v1.3.1\projects\Jungledsq_ppc\ProjectSrc\smali\
 * Qualified Name:     org.jivesoftware.smackx.workgroup.util.ModelUtil
 * JD-Core Version:    0.7.0.1
 */