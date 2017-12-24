package org.jivesoftware.smack.packet;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class DefaultPacketExtension
  implements PacketExtension
{
  private String elementName;
  private Map<String, String> map;
  private String namespace;
  
  public DefaultPacketExtension(String paramString1, String paramString2)
  {
    this.elementName = paramString1;
    this.namespace = paramString2;
  }
  
  public String getElementName()
  {
    return this.elementName;
  }
  
  /* Error */
  public Collection<String> getNames()
  {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield 28	org/jivesoftware/smack/packet/DefaultPacketExtension:map	Ljava/util/Map;
    //   6: ifnonnull +11 -> 17
    //   9: invokestatic 34	java/util/Collections:emptySet	()Ljava/util/Set;
    //   12: astore_1
    //   13: aload_0
    //   14: monitorexit
    //   15: aload_1
    //   16: areturn
    //   17: new 36	java/util/HashMap
    //   20: dup
    //   21: aload_0
    //   22: getfield 28	org/jivesoftware/smack/packet/DefaultPacketExtension:map	Ljava/util/Map;
    //   25: invokespecial 39	java/util/HashMap:<init>	(Ljava/util/Map;)V
    //   28: invokevirtual 42	java/util/HashMap:keySet	()Ljava/util/Set;
    //   31: invokestatic 46	java/util/Collections:unmodifiableSet	(Ljava/util/Set;)Ljava/util/Set;
    //   34: astore_1
    //   35: goto -22 -> 13
    //   38: astore_1
    //   39: aload_0
    //   40: monitorexit
    //   41: aload_1
    //   42: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	43	0	this	DefaultPacketExtension
    //   12	23	1	localSet	java.util.Set
    //   38	4	1	localObject	Object
    // Exception table:
    //   from	to	target	type
    //   2	13	38	finally
    //   17	35	38	finally
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
    //   3: getfield 28	org/jivesoftware/smack/packet/DefaultPacketExtension:map	Ljava/util/Map;
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
    //   18: getfield 28	org/jivesoftware/smack/packet/DefaultPacketExtension:map	Ljava/util/Map;
    //   21: aload_1
    //   22: invokeinterface 57 2 0
    //   27: checkcast 59	java/lang/String
    //   30: astore_1
    //   31: goto -18 -> 13
    //   34: astore_1
    //   35: aload_0
    //   36: monitorexit
    //   37: aload_1
    //   38: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	39	0	this	DefaultPacketExtension
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
    Iterator localIterator = getNames().iterator();
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
 * Qualified Name:     org.jivesoftware.smack.packet.DefaultPacketExtension
 * JD-Core Version:    0.7.0.1
 */