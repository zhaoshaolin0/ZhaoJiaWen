package org.jivesoftware.smack.util;

import java.util.AbstractCollection;
import java.util.AbstractSet;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import org.jivesoftware.smack.util.collections.AbstractMapEntry;

public class Cache<K, V>
  implements Map<K, V>
{
  protected LinkedList ageList;
  protected long cacheHits;
  protected long cacheMisses = 0L;
  protected LinkedList lastAccessedList;
  protected Map<K, CacheObject<V>> map;
  protected int maxCacheSize;
  protected long maxLifetime;
  
  public Cache(int paramInt, long paramLong)
  {
    if (paramInt == 0) {
      throw new IllegalArgumentException("Max cache size cannot be 0.");
    }
    this.maxCacheSize = paramInt;
    this.maxLifetime = paramLong;
    this.map = new HashMap(103);
    this.lastAccessedList = new LinkedList();
    this.ageList = new LinkedList();
  }
  
  public void clear()
  {
    try
    {
      Object[] arrayOfObject = this.map.keySet().toArray();
      int j = arrayOfObject.length;
      int i = 0;
      while (i < j)
      {
        remove(arrayOfObject[i]);
        i += 1;
      }
      this.map.clear();
      this.lastAccessedList.clear();
      this.ageList.clear();
      this.cacheHits = 0L;
      this.cacheMisses = 0L;
      return;
    }
    finally {}
  }
  
  public boolean containsKey(Object paramObject)
  {
    try
    {
      deleteExpiredEntries();
      boolean bool = this.map.containsKey(paramObject);
      return bool;
    }
    finally
    {
      paramObject = finally;
      throw paramObject;
    }
  }
  
  public boolean containsValue(Object paramObject)
  {
    try
    {
      deleteExpiredEntries();
      paramObject = new CacheObject(paramObject);
      boolean bool = this.map.containsValue(paramObject);
      return bool;
    }
    finally
    {
      paramObject = finally;
      throw paramObject;
    }
  }
  
  /* Error */
  protected void cullCache()
  {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield 54	org/jivesoftware/smack/util/Cache:maxCacheSize	I
    //   6: istore_1
    //   7: iload_1
    //   8: ifge +6 -> 14
    //   11: aload_0
    //   12: monitorexit
    //   13: return
    //   14: aload_0
    //   15: getfield 63	org/jivesoftware/smack/util/Cache:map	Ljava/util/Map;
    //   18: invokeinterface 107 1 0
    //   23: aload_0
    //   24: getfield 54	org/jivesoftware/smack/util/Cache:maxCacheSize	I
    //   27: if_icmple -16 -> 11
    //   30: aload_0
    //   31: invokevirtual 94	org/jivesoftware/smack/util/Cache:deleteExpiredEntries	()V
    //   34: aload_0
    //   35: getfield 54	org/jivesoftware/smack/util/Cache:maxCacheSize	I
    //   38: i2d
    //   39: ldc2_w 108
    //   42: dmul
    //   43: d2i
    //   44: istore_2
    //   45: aload_0
    //   46: getfield 63	org/jivesoftware/smack/util/Cache:map	Ljava/util/Map;
    //   49: invokeinterface 107 1 0
    //   54: istore_1
    //   55: iload_1
    //   56: iload_2
    //   57: if_icmple -46 -> 11
    //   60: aload_0
    //   61: aload_0
    //   62: getfield 66	org/jivesoftware/smack/util/Cache:lastAccessedList	Lorg/jivesoftware/smack/util/Cache$LinkedList;
    //   65: invokevirtual 113	org/jivesoftware/smack/util/Cache$LinkedList:getLast	()Lorg/jivesoftware/smack/util/Cache$LinkedListNode;
    //   68: getfield 117	org/jivesoftware/smack/util/Cache$LinkedListNode:object	Ljava/lang/Object;
    //   71: iconst_1
    //   72: invokevirtual 120	org/jivesoftware/smack/util/Cache:remove	(Ljava/lang/Object;Z)Ljava/lang/Object;
    //   75: ifnonnull +60 -> 135
    //   78: getstatic 126	java/lang/System:err	Ljava/io/PrintStream;
    //   81: new 128	java/lang/StringBuilder
    //   84: dup
    //   85: invokespecial 129	java/lang/StringBuilder:<init>	()V
    //   88: ldc 131
    //   90: invokevirtual 135	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   93: aload_0
    //   94: getfield 66	org/jivesoftware/smack/util/Cache:lastAccessedList	Lorg/jivesoftware/smack/util/Cache$LinkedList;
    //   97: invokevirtual 113	org/jivesoftware/smack/util/Cache$LinkedList:getLast	()Lorg/jivesoftware/smack/util/Cache$LinkedListNode;
    //   100: getfield 117	org/jivesoftware/smack/util/Cache$LinkedListNode:object	Ljava/lang/Object;
    //   103: invokevirtual 139	java/lang/Object:toString	()Ljava/lang/String;
    //   106: invokevirtual 135	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   109: ldc 141
    //   111: invokevirtual 135	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   114: ldc 143
    //   116: invokevirtual 135	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   119: invokevirtual 144	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   122: invokevirtual 149	java/io/PrintStream:println	(Ljava/lang/String;)V
    //   125: aload_0
    //   126: getfield 66	org/jivesoftware/smack/util/Cache:lastAccessedList	Lorg/jivesoftware/smack/util/Cache$LinkedList;
    //   129: invokevirtual 113	org/jivesoftware/smack/util/Cache$LinkedList:getLast	()Lorg/jivesoftware/smack/util/Cache$LinkedListNode;
    //   132: invokevirtual 151	org/jivesoftware/smack/util/Cache$LinkedListNode:remove	()V
    //   135: iload_1
    //   136: iconst_1
    //   137: isub
    //   138: istore_1
    //   139: goto -84 -> 55
    //   142: astore_3
    //   143: aload_0
    //   144: monitorexit
    //   145: aload_3
    //   146: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	147	0	this	Cache
    //   6	133	1	i	int
    //   44	14	2	j	int
    //   142	4	3	localObject	Object
    // Exception table:
    //   from	to	target	type
    //   2	7	142	finally
    //   14	55	142	finally
    //   60	135	142	finally
  }
  
  /* Error */
  protected void deleteExpiredEntries()
  {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield 56	org/jivesoftware/smack/util/Cache:maxLifetime	J
    //   6: lstore_1
    //   7: lload_1
    //   8: lconst_0
    //   9: lcmp
    //   10: ifgt +6 -> 16
    //   13: aload_0
    //   14: monitorexit
    //   15: return
    //   16: aload_0
    //   17: getfield 68	org/jivesoftware/smack/util/Cache:ageList	Lorg/jivesoftware/smack/util/Cache$LinkedList;
    //   20: invokevirtual 113	org/jivesoftware/smack/util/Cache$LinkedList:getLast	()Lorg/jivesoftware/smack/util/Cache$LinkedListNode;
    //   23: astore 5
    //   25: aload 5
    //   27: ifnull -14 -> 13
    //   30: invokestatic 155	java/lang/System:currentTimeMillis	()J
    //   33: lstore_1
    //   34: aload_0
    //   35: getfield 56	org/jivesoftware/smack/util/Cache:maxLifetime	J
    //   38: lstore_3
    //   39: lload_1
    //   40: lload_3
    //   41: lsub
    //   42: aload 5
    //   44: getfield 158	org/jivesoftware/smack/util/Cache$LinkedListNode:timestamp	J
    //   47: lcmp
    //   48: ifle -35 -> 13
    //   51: aload_0
    //   52: aload 5
    //   54: getfield 117	org/jivesoftware/smack/util/Cache$LinkedListNode:object	Ljava/lang/Object;
    //   57: iconst_1
    //   58: invokevirtual 120	org/jivesoftware/smack/util/Cache:remove	(Ljava/lang/Object;Z)Ljava/lang/Object;
    //   61: ifnonnull +45 -> 106
    //   64: getstatic 126	java/lang/System:err	Ljava/io/PrintStream;
    //   67: new 128	java/lang/StringBuilder
    //   70: dup
    //   71: invokespecial 129	java/lang/StringBuilder:<init>	()V
    //   74: ldc 160
    //   76: invokevirtual 135	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   79: aload 5
    //   81: getfield 117	org/jivesoftware/smack/util/Cache$LinkedListNode:object	Ljava/lang/Object;
    //   84: invokevirtual 139	java/lang/Object:toString	()Ljava/lang/String;
    //   87: invokevirtual 135	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   90: ldc 162
    //   92: invokevirtual 135	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   95: invokevirtual 144	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   98: invokevirtual 149	java/io/PrintStream:println	(Ljava/lang/String;)V
    //   101: aload 5
    //   103: invokevirtual 151	org/jivesoftware/smack/util/Cache$LinkedListNode:remove	()V
    //   106: aload_0
    //   107: getfield 68	org/jivesoftware/smack/util/Cache:ageList	Lorg/jivesoftware/smack/util/Cache$LinkedList;
    //   110: invokevirtual 113	org/jivesoftware/smack/util/Cache$LinkedList:getLast	()Lorg/jivesoftware/smack/util/Cache$LinkedListNode;
    //   113: astore 6
    //   115: aload 6
    //   117: astore 5
    //   119: aload 6
    //   121: ifnonnull -82 -> 39
    //   124: goto -111 -> 13
    //   127: astore 5
    //   129: aload_0
    //   130: monitorexit
    //   131: aload 5
    //   133: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	134	0	this	Cache
    //   6	34	1	l1	long
    //   38	3	3	l2	long
    //   23	95	5	localObject1	Object
    //   127	5	5	localObject2	Object
    //   113	7	6	localLinkedListNode	LinkedListNode
    // Exception table:
    //   from	to	target	type
    //   2	7	127	finally
    //   16	25	127	finally
    //   30	39	127	finally
    //   39	106	127	finally
    //   106	115	127	finally
  }
  
  public Set<Map.Entry<K, V>> entrySet()
  {
    try
    {
      deleteExpiredEntries();
      AbstractSet local2 = new AbstractSet()
      {
        private final Set<Map.Entry<K, Cache.CacheObject<V>>> set = Cache.this.map.entrySet();
        
        public Iterator<Map.Entry<K, V>> iterator()
        {
          new Iterator()
          {
            private final Iterator<Map.Entry<K, Cache.CacheObject<V>>> it = Cache.2.this.set.iterator();
            
            public boolean hasNext()
            {
              return this.it.hasNext();
            }
            
            public Map.Entry<K, V> next()
            {
              Map.Entry localEntry = (Map.Entry)this.it.next();
              new AbstractMapEntry(localEntry.getKey(), ((Cache.CacheObject)localEntry.getValue()).object)
              {
                public V setValue(V paramAnonymous3V)
                {
                  throw new UnsupportedOperationException("Cannot set");
                }
              };
            }
            
            public void remove()
            {
              this.it.remove();
            }
          };
        }
        
        public int size()
        {
          return this.set.size();
        }
      };
      return local2;
    }
    finally
    {
      localObject = finally;
      throw localObject;
    }
  }
  
  /* Error */
  public V get(Object paramObject)
  {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: invokevirtual 94	org/jivesoftware/smack/util/Cache:deleteExpiredEntries	()V
    //   6: aload_0
    //   7: getfield 63	org/jivesoftware/smack/util/Cache:map	Ljava/util/Map;
    //   10: aload_1
    //   11: invokeinterface 171 2 0
    //   16: checkcast 19	org/jivesoftware/smack/util/Cache$CacheObject
    //   19: astore_1
    //   20: aload_1
    //   21: ifnonnull +19 -> 40
    //   24: aload_0
    //   25: aload_0
    //   26: getfield 45	org/jivesoftware/smack/util/Cache:cacheMisses	J
    //   29: lconst_1
    //   30: ladd
    //   31: putfield 45	org/jivesoftware/smack/util/Cache:cacheMisses	J
    //   34: aconst_null
    //   35: astore_1
    //   36: aload_0
    //   37: monitorexit
    //   38: aload_1
    //   39: areturn
    //   40: aload_1
    //   41: getfield 175	org/jivesoftware/smack/util/Cache$CacheObject:lastAccessedListNode	Lorg/jivesoftware/smack/util/Cache$LinkedListNode;
    //   44: invokevirtual 151	org/jivesoftware/smack/util/Cache$LinkedListNode:remove	()V
    //   47: aload_0
    //   48: getfield 66	org/jivesoftware/smack/util/Cache:lastAccessedList	Lorg/jivesoftware/smack/util/Cache$LinkedList;
    //   51: aload_1
    //   52: getfield 175	org/jivesoftware/smack/util/Cache$CacheObject:lastAccessedListNode	Lorg/jivesoftware/smack/util/Cache$LinkedListNode;
    //   55: invokevirtual 179	org/jivesoftware/smack/util/Cache$LinkedList:addFirst	(Lorg/jivesoftware/smack/util/Cache$LinkedListNode;)Lorg/jivesoftware/smack/util/Cache$LinkedListNode;
    //   58: pop
    //   59: aload_0
    //   60: aload_0
    //   61: getfield 89	org/jivesoftware/smack/util/Cache:cacheHits	J
    //   64: lconst_1
    //   65: ladd
    //   66: putfield 89	org/jivesoftware/smack/util/Cache:cacheHits	J
    //   69: aload_1
    //   70: aload_1
    //   71: getfield 182	org/jivesoftware/smack/util/Cache$CacheObject:readCount	I
    //   74: iconst_1
    //   75: iadd
    //   76: putfield 182	org/jivesoftware/smack/util/Cache$CacheObject:readCount	I
    //   79: aload_1
    //   80: getfield 183	org/jivesoftware/smack/util/Cache$CacheObject:object	Ljava/lang/Object;
    //   83: astore_1
    //   84: goto -48 -> 36
    //   87: astore_1
    //   88: aload_0
    //   89: monitorexit
    //   90: aload_1
    //   91: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	92	0	this	Cache
    //   0	92	1	paramObject	Object
    // Exception table:
    //   from	to	target	type
    //   2	20	87	finally
    //   24	34	87	finally
    //   40	84	87	finally
  }
  
  public long getCacheHits()
  {
    return this.cacheHits;
  }
  
  public long getCacheMisses()
  {
    return this.cacheMisses;
  }
  
  public int getMaxCacheSize()
  {
    return this.maxCacheSize;
  }
  
  public long getMaxLifetime()
  {
    return this.maxLifetime;
  }
  
  public boolean isEmpty()
  {
    try
    {
      deleteExpiredEntries();
      boolean bool = this.map.isEmpty();
      return bool;
    }
    finally
    {
      localObject = finally;
      throw localObject;
    }
  }
  
  public Set<K> keySet()
  {
    try
    {
      deleteExpiredEntries();
      Set localSet = Collections.unmodifiableSet(this.map.keySet());
      return localSet;
    }
    finally
    {
      localObject = finally;
      throw localObject;
    }
  }
  
  public V put(K paramK, V paramV)
  {
    Object localObject = null;
    try
    {
      if (this.map.containsKey(paramK)) {
        localObject = remove(paramK, true);
      }
      paramV = new CacheObject(paramV);
      this.map.put(paramK, paramV);
      paramV.lastAccessedListNode = this.lastAccessedList.addFirst(paramK);
      paramK = this.ageList.addFirst(paramK);
      paramK.timestamp = System.currentTimeMillis();
      paramV.ageListNode = paramK;
      cullCache();
      return localObject;
    }
    finally {}
  }
  
  public void putAll(Map<? extends K, ? extends V> paramMap)
  {
    Iterator localIterator = paramMap.entrySet().iterator();
    while (localIterator.hasNext())
    {
      Map.Entry localEntry = (Map.Entry)localIterator.next();
      Object localObject = localEntry.getValue();
      paramMap = localObject;
      if ((localObject instanceof CacheObject)) {
        paramMap = ((CacheObject)localObject).object;
      }
      put(localEntry.getKey(), paramMap);
    }
  }
  
  public V remove(Object paramObject)
  {
    try
    {
      paramObject = remove(paramObject, false);
      return paramObject;
    }
    finally
    {
      paramObject = finally;
      throw paramObject;
    }
  }
  
  /* Error */
  public V remove(Object paramObject, boolean paramBoolean)
  {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield 63	org/jivesoftware/smack/util/Cache:map	Ljava/util/Map;
    //   6: aload_1
    //   7: invokeinterface 240 2 0
    //   12: checkcast 19	org/jivesoftware/smack/util/Cache$CacheObject
    //   15: astore_1
    //   16: aload_1
    //   17: ifnonnull +9 -> 26
    //   20: aconst_null
    //   21: astore_1
    //   22: aload_0
    //   23: monitorexit
    //   24: aload_1
    //   25: areturn
    //   26: aload_1
    //   27: getfield 175	org/jivesoftware/smack/util/Cache$CacheObject:lastAccessedListNode	Lorg/jivesoftware/smack/util/Cache$LinkedListNode;
    //   30: invokevirtual 151	org/jivesoftware/smack/util/Cache$LinkedListNode:remove	()V
    //   33: aload_1
    //   34: getfield 209	org/jivesoftware/smack/util/Cache$CacheObject:ageListNode	Lorg/jivesoftware/smack/util/Cache$LinkedListNode;
    //   37: invokevirtual 151	org/jivesoftware/smack/util/Cache$LinkedListNode:remove	()V
    //   40: aload_1
    //   41: aconst_null
    //   42: putfield 209	org/jivesoftware/smack/util/Cache$CacheObject:ageListNode	Lorg/jivesoftware/smack/util/Cache$LinkedListNode;
    //   45: aload_1
    //   46: aconst_null
    //   47: putfield 175	org/jivesoftware/smack/util/Cache$CacheObject:lastAccessedListNode	Lorg/jivesoftware/smack/util/Cache$LinkedListNode;
    //   50: aload_1
    //   51: getfield 183	org/jivesoftware/smack/util/Cache$CacheObject:object	Ljava/lang/Object;
    //   54: astore_1
    //   55: goto -33 -> 22
    //   58: astore_1
    //   59: aload_0
    //   60: monitorexit
    //   61: aload_1
    //   62: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	63	0	this	Cache
    //   0	63	1	paramObject	Object
    //   0	63	2	paramBoolean	boolean
    // Exception table:
    //   from	to	target	type
    //   2	16	58	finally
    //   26	55	58	finally
  }
  
  public void setMaxCacheSize(int paramInt)
  {
    try
    {
      this.maxCacheSize = paramInt;
      cullCache();
      return;
    }
    finally
    {
      localObject = finally;
      throw localObject;
    }
  }
  
  public void setMaxLifetime(long paramLong)
  {
    this.maxLifetime = paramLong;
  }
  
  public int size()
  {
    try
    {
      deleteExpiredEntries();
      int i = this.map.size();
      return i;
    }
    finally
    {
      localObject = finally;
      throw localObject;
    }
  }
  
  public Collection<V> values()
  {
    try
    {
      deleteExpiredEntries();
      Collection localCollection = Collections.unmodifiableCollection(new AbstractCollection()
      {
        Collection<Cache.CacheObject<V>> values = Cache.this.map.values();
        
        public Iterator<V> iterator()
        {
          new Iterator()
          {
            Iterator<Cache.CacheObject<V>> it = Cache.1.this.values.iterator();
            
            public boolean hasNext()
            {
              return this.it.hasNext();
            }
            
            public V next()
            {
              return ((Cache.CacheObject)this.it.next()).object;
            }
            
            public void remove()
            {
              this.it.remove();
            }
          };
        }
        
        public int size()
        {
          return this.values.size();
        }
      });
      return localCollection;
    }
    finally
    {
      localObject = finally;
      throw localObject;
    }
  }
  
  private static class CacheObject<V>
  {
    public Cache.LinkedListNode ageListNode;
    public Cache.LinkedListNode lastAccessedListNode;
    public V object;
    public int readCount = 0;
    
    public CacheObject(V paramV)
    {
      this.object = paramV;
    }
    
    public boolean equals(Object paramObject)
    {
      if (this == paramObject) {
        return true;
      }
      if (!(paramObject instanceof CacheObject)) {
        return false;
      }
      paramObject = (CacheObject)paramObject;
      return this.object.equals(paramObject.object);
    }
    
    public int hashCode()
    {
      return this.object.hashCode();
    }
  }
  
  private static class LinkedList
  {
    private Cache.LinkedListNode head = new Cache.LinkedListNode("head", null, null);
    
    public LinkedList()
    {
      Cache.LinkedListNode localLinkedListNode1 = this.head;
      Cache.LinkedListNode localLinkedListNode2 = this.head;
      Cache.LinkedListNode localLinkedListNode3 = this.head;
      localLinkedListNode2.previous = localLinkedListNode3;
      localLinkedListNode1.next = localLinkedListNode3;
    }
    
    public Cache.LinkedListNode addFirst(Object paramObject)
    {
      paramObject = new Cache.LinkedListNode(paramObject, this.head.next, this.head);
      paramObject.previous.next = paramObject;
      paramObject.next.previous = paramObject;
      return paramObject;
    }
    
    public Cache.LinkedListNode addFirst(Cache.LinkedListNode paramLinkedListNode)
    {
      paramLinkedListNode.next = this.head.next;
      paramLinkedListNode.previous = this.head;
      paramLinkedListNode.previous.next = paramLinkedListNode;
      paramLinkedListNode.next.previous = paramLinkedListNode;
      return paramLinkedListNode;
    }
    
    public Cache.LinkedListNode addLast(Object paramObject)
    {
      paramObject = new Cache.LinkedListNode(paramObject, this.head, this.head.previous);
      paramObject.previous.next = paramObject;
      paramObject.next.previous = paramObject;
      return paramObject;
    }
    
    public void clear()
    {
      for (Cache.LinkedListNode localLinkedListNode1 = getLast(); localLinkedListNode1 != null; localLinkedListNode1 = getLast()) {
        localLinkedListNode1.remove();
      }
      localLinkedListNode1 = this.head;
      Cache.LinkedListNode localLinkedListNode2 = this.head;
      Cache.LinkedListNode localLinkedListNode3 = this.head;
      localLinkedListNode2.previous = localLinkedListNode3;
      localLinkedListNode1.next = localLinkedListNode3;
    }
    
    public Cache.LinkedListNode getFirst()
    {
      Cache.LinkedListNode localLinkedListNode = this.head.next;
      if (localLinkedListNode == this.head) {
        return null;
      }
      return localLinkedListNode;
    }
    
    public Cache.LinkedListNode getLast()
    {
      Cache.LinkedListNode localLinkedListNode = this.head.previous;
      if (localLinkedListNode == this.head) {
        return null;
      }
      return localLinkedListNode;
    }
    
    public String toString()
    {
      Cache.LinkedListNode localLinkedListNode = this.head.next;
      StringBuilder localStringBuilder = new StringBuilder();
      while (localLinkedListNode != this.head)
      {
        localStringBuilder.append(localLinkedListNode.toString()).append(", ");
        localLinkedListNode = localLinkedListNode.next;
      }
      return localStringBuilder.toString();
    }
  }
  
  private static class LinkedListNode
  {
    public LinkedListNode next;
    public Object object;
    public LinkedListNode previous;
    public long timestamp;
    
    public LinkedListNode(Object paramObject, LinkedListNode paramLinkedListNode1, LinkedListNode paramLinkedListNode2)
    {
      this.object = paramObject;
      this.next = paramLinkedListNode1;
      this.previous = paramLinkedListNode2;
    }
    
    public void remove()
    {
      this.previous.next = this.next;
      this.next.previous = this.previous;
    }
    
    public String toString()
    {
      return this.object.toString();
    }
  }
}


/* Location:           C:\Users\Administrator\Documents\ds\反编译\AndroidKiller_v1.3.1\projects\Jungledsq_ppc\ProjectSrc\smali\
 * Qualified Name:     org.jivesoftware.smack.util.Cache
 * JD-Core Version:    0.7.0.1
 */