package org.jivesoftware.smackx.packet;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class DefaultPrivateData
  implements PrivateData
{
  private String elementName;
  private Map map;
  private String namespace;
  
  public DefaultPrivateData(String paramString1, String paramString2)
  {
    this.elementName = paramString1;
    this.namespace = paramString2;
  }
  
  public String getElementName()
  {
    return this.elementName;
  }
  
  /* Error */
  public Iterator getNames()
  {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield 27	org/jivesoftware/smackx/packet/DefaultPrivateData:map	Ljava/util/Map;
    //   6: ifnonnull +16 -> 22
    //   9: getstatic 33	java/util/Collections:EMPTY_LIST	Ljava/util/List;
    //   12: invokeinterface 38 1 0
    //   17: astore_1
    //   18: aload_0
    //   19: monitorexit
    //   20: aload_1
    //   21: areturn
    //   22: new 40	java/util/HashMap
    //   25: dup
    //   26: aload_0
    //   27: getfield 27	org/jivesoftware/smackx/packet/DefaultPrivateData:map	Ljava/util/Map;
    //   30: invokespecial 43	java/util/HashMap:<init>	(Ljava/util/Map;)V
    //   33: invokestatic 47	java/util/Collections:unmodifiableMap	(Ljava/util/Map;)Ljava/util/Map;
    //   36: invokeinterface 53 1 0
    //   41: invokeinterface 56 1 0
    //   46: astore_1
    //   47: goto -29 -> 18
    //   50: astore_1
    //   51: aload_0
    //   52: monitorexit
    //   53: aload_1
    //   54: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	55	0	this	DefaultPrivateData
    //   17	30	1	localIterator	Iterator
    //   50	4	1	localObject	Object
    // Exception table:
    //   from	to	target	type
    //   2	18	50	finally
    //   22	47	50	finally
  }
  
  public String getNamespace()
  {
    return this.namespace;
  }
  
  /* Error */
  public String getValue(String paramString)
  {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield 27	org/jivesoftware/smackx/packet/DefaultPrivateData:map	Ljava/util/Map;
    //   6: astore_2
    //   7: aload_2
    //   8: ifnonnull +9 -> 17
    //   11: aconst_null
    //   12: astore_1
    //   13: aload_0
    //   14: monitorexit
    //   15: aload_1
    //   16: areturn
    //   17: aload_0
    //   18: getfield 27	org/jivesoftware/smackx/packet/DefaultPrivateData:map	Ljava/util/Map;
    //   21: aload_1
    //   22: invokeinterface 63 2 0
    //   27: checkcast 65	java/lang/String
    //   30: astore_1
    //   31: goto -18 -> 13
    //   34: astore_1
    //   35: aload_0
    //   36: monitorexit
    //   37: aload_1
    //   38: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	39	0	this	DefaultPrivateData
    //   0	39	1	paramString	String
    //   6	2	2	localMap	Map
    // Exception table:
    //   from	to	target	type
    //   2	7	34	finally
    //   17	31	34	finally
  }
  
  public void setValue(String paramString1, String paramString2)
  {
    try
    {
      if (this.map == null) {
        this.map = new HashMap();
      }
      this.map.put(paramString1, paramString2);
      return;
    }
    finally {}
  }
  
  public String toXML()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("<").append(this.elementName).append(" xmlns=\"").append(this.namespace).append("\">");
    Iterator localIterator = getNames();
    while (localIterator.hasNext())
    {
      String str1 = (String)localIterator.next();
      String str2 = getValue(str1);
      localStringBuilder.append("<").append(str1).append(">");
      localStringBuilder.append(str2);
      localStringBuilder.append("</").append(str1).append(">");
    }
    localStringBuilder.append("</").append(this.elementName).append(">");
    return localStringBuilder.toString();
  }
}


/* Location:           C:\Users\Administrator\Documents\ds\反编译\AndroidKiller_v1.3.1\projects\Jungledsq_ppc\ProjectSrc\smali\
 * Qualified Name:     org.jivesoftware.smackx.packet.DefaultPrivateData
 * JD-Core Version:    0.7.0.1
 */