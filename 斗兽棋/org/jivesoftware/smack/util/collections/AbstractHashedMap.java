package org.jivesoftware.smack.util.collections;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.AbstractCollection;
import java.util.AbstractMap;
import java.util.AbstractSet;
import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NoSuchElementException;
import java.util.Set;

public class AbstractHashedMap<K, V>
  extends AbstractMap<K, V>
  implements IterableMap<K, V>
{
  protected static final int DEFAULT_CAPACITY = 16;
  protected static final float DEFAULT_LOAD_FACTOR = 0.75F;
  protected static final int DEFAULT_THRESHOLD = 12;
  protected static final String GETKEY_INVALID = "getKey() can only be called after next() and before remove()";
  protected static final String GETVALUE_INVALID = "getValue() can only be called after next() and before remove()";
  protected static final int MAXIMUM_CAPACITY = 1073741824;
  protected static final String NO_NEXT_ENTRY = "No next() entry in the iteration";
  protected static final String NO_PREVIOUS_ENTRY = "No previous() entry in the iteration";
  protected static final Object NULL = new Object();
  protected static final String REMOVE_INVALID = "remove() can only be called once after next()";
  protected static final String SETVALUE_INVALID = "setValue() can only be called after next() and before remove()";
  protected transient HashEntry<K, V>[] data;
  protected transient EntrySet<K, V> entrySet;
  protected transient KeySet<K, V> keySet;
  protected transient float loadFactor;
  protected transient int modCount;
  protected transient int size;
  protected transient int threshold;
  protected transient Values<K, V> values;
  
  protected AbstractHashedMap() {}
  
  protected AbstractHashedMap(int paramInt)
  {
    this(paramInt, 0.75F);
  }
  
  protected AbstractHashedMap(int paramInt, float paramFloat)
  {
    if (paramInt < 1) {
      throw new IllegalArgumentException("Initial capacity must be greater than 0");
    }
    if ((paramFloat <= 0.0F) || (Float.isNaN(paramFloat))) {
      throw new IllegalArgumentException("Load factor must be greater than 0");
    }
    this.loadFactor = paramFloat;
    this.threshold = calculateThreshold(paramInt, paramFloat);
    this.data = new HashEntry[calculateNewCapacity(paramInt)];
    init();
  }
  
  protected AbstractHashedMap(int paramInt1, float paramFloat, int paramInt2)
  {
    this.loadFactor = paramFloat;
    this.data = new HashEntry[paramInt1];
    this.threshold = paramInt2;
    init();
  }
  
  protected AbstractHashedMap(Map<? extends K, ? extends V> paramMap)
  {
    this(Math.max(paramMap.size() * 2, 16), 0.75F);
    putAll(paramMap);
  }
  
  protected void addEntry(HashEntry<K, V> paramHashEntry, int paramInt)
  {
    this.data[paramInt] = paramHashEntry;
  }
  
  protected void addMapping(int paramInt1, int paramInt2, K paramK, V paramV)
  {
    this.modCount += 1;
    addEntry(createEntry(this.data[paramInt1], paramInt2, paramK, paramV), paramInt1);
    this.size += 1;
    checkCapacity();
  }
  
  protected int calculateNewCapacity(int paramInt)
  {
    int i = 1;
    if (paramInt > 1073741824) {
      paramInt = 1073741824;
    }
    do
    {
      return paramInt;
      while (i < paramInt) {
        i <<= 1;
      }
      paramInt = i;
    } while (i <= 1073741824);
    return 1073741824;
  }
  
  protected int calculateThreshold(int paramInt, float paramFloat)
  {
    return (int)(paramInt * paramFloat);
  }
  
  protected void checkCapacity()
  {
    if (this.size >= this.threshold)
    {
      int i = this.data.length * 2;
      if (i <= 1073741824) {
        ensureCapacity(i);
      }
    }
  }
  
  public void clear()
  {
    this.modCount += 1;
    HashEntry[] arrayOfHashEntry = this.data;
    int i = arrayOfHashEntry.length - 1;
    while (i >= 0)
    {
      arrayOfHashEntry[i] = null;
      i -= 1;
    }
    this.size = 0;
  }
  
  protected Object clone()
  {
    try
    {
      AbstractHashedMap localAbstractHashedMap = (AbstractHashedMap)super.clone();
      localAbstractHashedMap.data = new HashEntry[this.data.length];
      localAbstractHashedMap.entrySet = null;
      localAbstractHashedMap.keySet = null;
      localAbstractHashedMap.values = null;
      localAbstractHashedMap.modCount = 0;
      localAbstractHashedMap.size = 0;
      localAbstractHashedMap.init();
      localAbstractHashedMap.putAll(this);
      return localAbstractHashedMap;
    }
    catch (CloneNotSupportedException localCloneNotSupportedException) {}
    return null;
  }
  
  public boolean containsKey(Object paramObject)
  {
    int i;
    if (paramObject == null)
    {
      localObject = NULL;
      i = hash(localObject);
    }
    for (Object localObject = this.data[hashIndex(i, this.data.length)];; localObject = ((HashEntry)localObject).next)
    {
      if (localObject == null) {
        break label69;
      }
      if ((((HashEntry)localObject).hashCode == i) && (isEqualKey(paramObject, ((HashEntry)localObject).getKey())))
      {
        return true;
        localObject = paramObject;
        break;
      }
    }
    label69:
    return false;
  }
  
  public boolean containsValue(Object paramObject)
  {
    if (paramObject == null)
    {
      i = 0;
      j = this.data.length;
      while (i < j)
      {
        for (paramObject = this.data[i]; paramObject != null; paramObject = paramObject.next) {
          if (paramObject.getValue() == null) {
            return true;
          }
        }
        i += 1;
      }
    }
    int i = 0;
    int j = this.data.length;
    while (i < j)
    {
      for (HashEntry localHashEntry = this.data[i]; localHashEntry != null; localHashEntry = localHashEntry.next) {
        if (isEqualValue(paramObject, localHashEntry.getValue())) {
          return true;
        }
      }
      i += 1;
    }
    return false;
  }
  
  protected HashEntry<K, V> createEntry(HashEntry<K, V> paramHashEntry, int paramInt, K paramK, V paramV)
  {
    return new HashEntry(paramHashEntry, paramInt, paramK, paramV);
  }
  
  protected Iterator<Map.Entry<K, V>> createEntrySetIterator()
  {
    if (size() == 0) {
      return EmptyIterator.INSTANCE;
    }
    return new EntrySetIterator(this);
  }
  
  protected Iterator<K> createKeySetIterator()
  {
    if (size() == 0) {
      return EmptyIterator.INSTANCE;
    }
    return new KeySetIterator(this);
  }
  
  protected Iterator<V> createValuesIterator()
  {
    if (size() == 0) {
      return EmptyIterator.INSTANCE;
    }
    return new ValuesIterator(this);
  }
  
  protected void destroyEntry(HashEntry<K, V> paramHashEntry)
  {
    paramHashEntry.next = null;
    HashEntry.access$002(paramHashEntry, null);
    HashEntry.access$102(paramHashEntry, null);
  }
  
  protected void doReadObject(ObjectInputStream paramObjectInputStream)
    throws IOException, ClassNotFoundException
  {
    this.loadFactor = paramObjectInputStream.readFloat();
    int i = paramObjectInputStream.readInt();
    int j = paramObjectInputStream.readInt();
    init();
    this.data = new HashEntry[i];
    i = 0;
    while (i < j)
    {
      put(paramObjectInputStream.readObject(), paramObjectInputStream.readObject());
      i += 1;
    }
    this.threshold = calculateThreshold(this.data.length, this.loadFactor);
  }
  
  protected void doWriteObject(ObjectOutputStream paramObjectOutputStream)
    throws IOException
  {
    paramObjectOutputStream.writeFloat(this.loadFactor);
    paramObjectOutputStream.writeInt(this.data.length);
    paramObjectOutputStream.writeInt(this.size);
    MapIterator localMapIterator = mapIterator();
    while (localMapIterator.hasNext())
    {
      paramObjectOutputStream.writeObject(localMapIterator.next());
      paramObjectOutputStream.writeObject(localMapIterator.getValue());
    }
  }
  
  protected void ensureCapacity(int paramInt)
  {
    int i = this.data.length;
    if (paramInt <= i) {
      return;
    }
    if (this.size == 0)
    {
      this.threshold = calculateThreshold(paramInt, this.loadFactor);
      this.data = new HashEntry[paramInt];
      return;
    }
    HashEntry[] arrayOfHashEntry1 = this.data;
    HashEntry[] arrayOfHashEntry2 = new HashEntry[paramInt];
    this.modCount += 1;
    i -= 1;
    while (i >= 0)
    {
      Object localObject = arrayOfHashEntry1[i];
      if (localObject != null)
      {
        arrayOfHashEntry1[i] = null;
        HashEntry localHashEntry;
        do
        {
          localHashEntry = ((HashEntry)localObject).next;
          int j = hashIndex(((HashEntry)localObject).hashCode, paramInt);
          ((HashEntry)localObject).next = arrayOfHashEntry2[j];
          arrayOfHashEntry2[j] = localObject;
          localObject = localHashEntry;
        } while (localHashEntry != null);
      }
      i -= 1;
    }
    this.threshold = calculateThreshold(paramInt, this.loadFactor);
    this.data = arrayOfHashEntry2;
  }
  
  protected int entryHashCode(HashEntry<K, V> paramHashEntry)
  {
    return paramHashEntry.hashCode;
  }
  
  protected K entryKey(HashEntry<K, V> paramHashEntry)
  {
    return paramHashEntry.key;
  }
  
  protected HashEntry<K, V> entryNext(HashEntry<K, V> paramHashEntry)
  {
    return paramHashEntry.next;
  }
  
  public Set<Map.Entry<K, V>> entrySet()
  {
    if (this.entrySet == null) {
      this.entrySet = new EntrySet(this);
    }
    return this.entrySet;
  }
  
  protected V entryValue(HashEntry<K, V> paramHashEntry)
  {
    return paramHashEntry.value;
  }
  
  public boolean equals(Object paramObject)
  {
    if (paramObject == this) {
      return true;
    }
    if (!(paramObject instanceof Map)) {
      return false;
    }
    paramObject = (Map)paramObject;
    if (paramObject.size() != size()) {
      return false;
    }
    MapIterator localMapIterator = mapIterator();
    try
    {
      while (localMapIterator.hasNext())
      {
        Object localObject1 = localMapIterator.next();
        Object localObject2 = localMapIterator.getValue();
        if (localObject2 == null)
        {
          if (paramObject.get(localObject1) != null) {
            break label124;
          }
          if (!paramObject.containsKey(localObject1)) {
            break label124;
          }
        }
        else
        {
          boolean bool = localObject2.equals(paramObject.get(localObject1));
          if (!bool) {
            return false;
          }
        }
      }
    }
    catch (ClassCastException paramObject)
    {
      return false;
    }
    catch (NullPointerException paramObject)
    {
      return false;
    }
    return true;
    label124:
    return false;
  }
  
  public V get(Object paramObject)
  {
    int i;
    if (paramObject == null)
    {
      localObject = NULL;
      i = hash(localObject);
    }
    for (Object localObject = this.data[hashIndex(i, this.data.length)];; localObject = ((HashEntry)localObject).next)
    {
      if (localObject == null) {
        break label72;
      }
      if ((((HashEntry)localObject).hashCode == i) && (isEqualKey(paramObject, ((HashEntry)localObject).key)))
      {
        return ((HashEntry)localObject).getValue();
        localObject = paramObject;
        break;
      }
    }
    label72:
    return null;
  }
  
  protected HashEntry<K, V> getEntry(Object paramObject)
  {
    int i;
    if (paramObject == null)
    {
      localObject = NULL;
      i = hash(localObject);
    }
    for (Object localObject = this.data[hashIndex(i, this.data.length)];; localObject = ((HashEntry)localObject).next)
    {
      if (localObject == null) {
        break label69;
      }
      if ((((HashEntry)localObject).hashCode == i) && (isEqualKey(paramObject, ((HashEntry)localObject).getKey())))
      {
        return localObject;
        localObject = paramObject;
        break;
      }
    }
    label69:
    return null;
  }
  
  protected int hash(Object paramObject)
  {
    int i = paramObject.hashCode();
    i += (i << 9 ^ 0xFFFFFFFF);
    i ^= i >>> 14;
    i += (i << 4);
    return i ^ i >>> 10;
  }
  
  public int hashCode()
  {
    int i = 0;
    Iterator localIterator = createEntrySetIterator();
    while (localIterator.hasNext()) {
      i += localIterator.next().hashCode();
    }
    return i;
  }
  
  protected int hashIndex(int paramInt1, int paramInt2)
  {
    return paramInt2 - 1 & paramInt1;
  }
  
  protected void init() {}
  
  public boolean isEmpty()
  {
    return this.size == 0;
  }
  
  protected boolean isEqualKey(Object paramObject1, Object paramObject2)
  {
    return (paramObject1 == paramObject2) || ((paramObject1 != null) && (paramObject1.equals(paramObject2)));
  }
  
  protected boolean isEqualValue(Object paramObject1, Object paramObject2)
  {
    return (paramObject1 == paramObject2) || (paramObject1.equals(paramObject2));
  }
  
  public Set<K> keySet()
  {
    if (this.keySet == null) {
      this.keySet = new KeySet(this);
    }
    return this.keySet;
  }
  
  public MapIterator<K, V> mapIterator()
  {
    if (this.size == 0) {
      return EmptyMapIterator.INSTANCE;
    }
    return new HashMapIterator(this);
  }
  
  public V put(K paramK, V paramV)
  {
    int i;
    int j;
    if (paramK == null)
    {
      localObject = NULL;
      i = hash(localObject);
      j = hashIndex(i, this.data.length);
    }
    for (Object localObject = this.data[j];; localObject = ((HashEntry)localObject).next)
    {
      if (localObject == null) {
        break label95;
      }
      if ((((HashEntry)localObject).hashCode == i) && (isEqualKey(paramK, ((HashEntry)localObject).getKey())))
      {
        paramK = ((HashEntry)localObject).getValue();
        updateEntry((HashEntry)localObject, paramV);
        return paramK;
        localObject = paramK;
        break;
      }
    }
    label95:
    addMapping(j, i, paramK, paramV);
    return null;
  }
  
  public void putAll(Map<? extends K, ? extends V> paramMap)
  {
    int i = paramMap.size();
    if (i == 0) {}
    for (;;)
    {
      return;
      ensureCapacity(calculateNewCapacity((int)((this.size + i) / this.loadFactor + 1.0F)));
      paramMap = paramMap.entrySet().iterator();
      while (paramMap.hasNext())
      {
        Map.Entry localEntry = (Map.Entry)paramMap.next();
        put(localEntry.getKey(), localEntry.getValue());
      }
    }
  }
  
  public V remove(Object paramObject)
  {
    Object localObject1;
    int i;
    int j;
    Object localObject2;
    if (paramObject == null)
    {
      localObject1 = NULL;
      i = hash(localObject1);
      j = hashIndex(i, this.data.length);
      localObject1 = this.data[j];
      localObject2 = null;
    }
    for (;;)
    {
      if (localObject1 == null) {
        break label102;
      }
      if ((((HashEntry)localObject1).hashCode == i) && (isEqualKey(paramObject, ((HashEntry)localObject1).getKey())))
      {
        paramObject = ((HashEntry)localObject1).getValue();
        removeMapping((HashEntry)localObject1, j, (HashEntry)localObject2);
        return paramObject;
        localObject1 = paramObject;
        break;
      }
      localObject2 = localObject1;
      localObject1 = ((HashEntry)localObject1).next;
    }
    label102:
    return null;
  }
  
  protected void removeEntry(HashEntry<K, V> paramHashEntry1, int paramInt, HashEntry<K, V> paramHashEntry2)
  {
    if (paramHashEntry2 == null)
    {
      this.data[paramInt] = paramHashEntry1.next;
      return;
    }
    paramHashEntry2.next = paramHashEntry1.next;
  }
  
  protected void removeMapping(HashEntry<K, V> paramHashEntry1, int paramInt, HashEntry<K, V> paramHashEntry2)
  {
    this.modCount += 1;
    removeEntry(paramHashEntry1, paramInt, paramHashEntry2);
    this.size -= 1;
    destroyEntry(paramHashEntry1);
  }
  
  protected void reuseEntry(HashEntry<K, V> paramHashEntry, int paramInt1, int paramInt2, K paramK, V paramV)
  {
    paramHashEntry.next = this.data[paramInt1];
    paramHashEntry.hashCode = paramInt2;
    HashEntry.access$002(paramHashEntry, paramK);
    HashEntry.access$102(paramHashEntry, paramV);
  }
  
  public int size()
  {
    return this.size;
  }
  
  public String toString()
  {
    if (size() == 0) {
      return "{}";
    }
    StringBuilder localStringBuilder1 = new StringBuilder(size() * 32);
    localStringBuilder1.append('{');
    MapIterator localMapIterator = mapIterator();
    boolean bool1 = localMapIterator.hasNext();
    if (bool1)
    {
      Object localObject1 = localMapIterator.next();
      Object localObject2 = localMapIterator.getValue();
      label79:
      StringBuilder localStringBuilder2;
      if (localObject1 == this)
      {
        localObject1 = "(this Map)";
        localStringBuilder2 = localStringBuilder1.append(localObject1).append('=');
        if (localObject2 != this) {
          break label144;
        }
      }
      label144:
      for (localObject1 = "(this Map)";; localObject1 = localObject2)
      {
        localStringBuilder2.append(localObject1);
        boolean bool2 = localMapIterator.hasNext();
        bool1 = bool2;
        if (!bool2) {
          break;
        }
        localStringBuilder1.append(',').append(' ');
        bool1 = bool2;
        break;
        break label79;
      }
    }
    localStringBuilder1.append('}');
    return localStringBuilder1.toString();
  }
  
  protected void updateEntry(HashEntry<K, V> paramHashEntry, V paramV)
  {
    paramHashEntry.setValue(paramV);
  }
  
  public Collection<V> values()
  {
    if (this.values == null) {
      this.values = new Values(this);
    }
    return this.values;
  }
  
  protected static class EntrySet<K, V>
    extends AbstractSet<Map.Entry<K, V>>
  {
    protected final AbstractHashedMap<K, V> parent;
    
    protected EntrySet(AbstractHashedMap<K, V> paramAbstractHashedMap)
    {
      this.parent = paramAbstractHashedMap;
    }
    
    public void clear()
    {
      this.parent.clear();
    }
    
    public boolean contains(Map.Entry<K, V> paramEntry)
    {
      AbstractHashedMap.HashEntry localHashEntry = this.parent.getEntry(paramEntry.getKey());
      return (localHashEntry != null) && (localHashEntry.equals(paramEntry));
    }
    
    public Iterator<Map.Entry<K, V>> iterator()
    {
      return this.parent.createEntrySetIterator();
    }
    
    public boolean remove(Object paramObject)
    {
      if (!(paramObject instanceof Map.Entry)) {
        return false;
      }
      if (!contains(paramObject)) {
        return false;
      }
      paramObject = ((Map.Entry)paramObject).getKey();
      this.parent.remove(paramObject);
      return true;
    }
    
    public int size()
    {
      return this.parent.size();
    }
  }
  
  protected static class EntrySetIterator<K, V>
    extends AbstractHashedMap.HashIterator<K, V>
    implements Iterator<Map.Entry<K, V>>
  {
    protected EntrySetIterator(AbstractHashedMap<K, V> paramAbstractHashedMap)
    {
      super();
    }
    
    public AbstractHashedMap.HashEntry<K, V> next()
    {
      return super.nextEntry();
    }
  }
  
  protected static class HashEntry<K, V>
    implements Map.Entry<K, V>, KeyValue<K, V>
  {
    protected int hashCode;
    private K key;
    protected HashEntry<K, V> next;
    private V value;
    
    protected HashEntry(HashEntry<K, V> paramHashEntry, int paramInt, K paramK, V paramV)
    {
      this.next = paramHashEntry;
      this.hashCode = paramInt;
      this.key = paramK;
      this.value = paramV;
    }
    
    public boolean equals(Object paramObject)
    {
      if (paramObject == this) {
        return true;
      }
      if (!(paramObject instanceof Map.Entry)) {
        return false;
      }
      paramObject = (Map.Entry)paramObject;
      if (getKey() == null)
      {
        if (paramObject.getKey() != null) {
          break label71;
        }
        if (getValue() != null) {
          break label73;
        }
        if (paramObject.getValue() != null) {
          break label71;
        }
      }
      for (;;)
      {
        return true;
        if (getKey().equals(paramObject.getKey())) {
          break;
        }
        label71:
        label73:
        do
        {
          return false;
        } while (!getValue().equals(paramObject.getValue()));
      }
    }
    
    public K getKey()
    {
      return this.key;
    }
    
    public V getValue()
    {
      return this.value;
    }
    
    public int hashCode()
    {
      int i;
      if (getKey() == null)
      {
        i = 0;
        if (getValue() != null) {
          break label33;
        }
      }
      label33:
      for (int j = 0;; j = getValue().hashCode())
      {
        return i ^ j;
        i = getKey().hashCode();
        break;
      }
    }
    
    public void setKey(K paramK)
    {
      this.key = paramK;
    }
    
    public V setValue(V paramV)
    {
      Object localObject = this.value;
      this.value = paramV;
      return localObject;
    }
    
    public String toString()
    {
      return getKey() + '=' + getValue();
    }
  }
  
  protected static abstract class HashIterator<K, V>
  {
    protected int expectedModCount;
    protected int hashIndex;
    protected AbstractHashedMap.HashEntry<K, V> last;
    protected AbstractHashedMap.HashEntry<K, V> next;
    protected final AbstractHashedMap parent;
    
    protected HashIterator(AbstractHashedMap<K, V> paramAbstractHashedMap)
    {
      this.parent = paramAbstractHashedMap;
      AbstractHashedMap.HashEntry[] arrayOfHashEntry = paramAbstractHashedMap.data;
      int i = arrayOfHashEntry.length;
      for (AbstractHashedMap.HashEntry localHashEntry = null; (i > 0) && (localHashEntry == null); localHashEntry = arrayOfHashEntry[i]) {
        i -= 1;
      }
      this.next = localHashEntry;
      this.hashIndex = i;
      this.expectedModCount = paramAbstractHashedMap.modCount;
    }
    
    protected AbstractHashedMap.HashEntry<K, V> currentEntry()
    {
      return this.last;
    }
    
    public boolean hasNext()
    {
      return this.next != null;
    }
    
    protected AbstractHashedMap.HashEntry<K, V> nextEntry()
    {
      if (this.parent.modCount != this.expectedModCount) {
        throw new ConcurrentModificationException();
      }
      AbstractHashedMap.HashEntry localHashEntry2 = this.next;
      if (localHashEntry2 == null) {
        throw new NoSuchElementException("No next() entry in the iteration");
      }
      AbstractHashedMap.HashEntry[] arrayOfHashEntry = this.parent.data;
      int i = this.hashIndex;
      for (AbstractHashedMap.HashEntry localHashEntry1 = localHashEntry2.next; (localHashEntry1 == null) && (i > 0); localHashEntry1 = arrayOfHashEntry[i]) {
        i -= 1;
      }
      this.next = localHashEntry1;
      this.hashIndex = i;
      this.last = localHashEntry2;
      return localHashEntry2;
    }
    
    public void remove()
    {
      if (this.last == null) {
        throw new IllegalStateException("remove() can only be called once after next()");
      }
      if (this.parent.modCount != this.expectedModCount) {
        throw new ConcurrentModificationException();
      }
      this.parent.remove(this.last.getKey());
      this.last = null;
      this.expectedModCount = this.parent.modCount;
    }
    
    public String toString()
    {
      if (this.last != null) {
        return "Iterator[" + this.last.getKey() + "=" + this.last.getValue() + "]";
      }
      return "Iterator[]";
    }
  }
  
  protected static class HashMapIterator<K, V>
    extends AbstractHashedMap.HashIterator<K, V>
    implements MapIterator<K, V>
  {
    protected HashMapIterator(AbstractHashedMap<K, V> paramAbstractHashedMap)
    {
      super();
    }
    
    public K getKey()
    {
      AbstractHashedMap.HashEntry localHashEntry = currentEntry();
      if (localHashEntry == null) {
        throw new IllegalStateException("getKey() can only be called after next() and before remove()");
      }
      return localHashEntry.getKey();
    }
    
    public V getValue()
    {
      AbstractHashedMap.HashEntry localHashEntry = currentEntry();
      if (localHashEntry == null) {
        throw new IllegalStateException("getValue() can only be called after next() and before remove()");
      }
      return localHashEntry.getValue();
    }
    
    public K next()
    {
      return super.nextEntry().getKey();
    }
    
    public V setValue(V paramV)
    {
      AbstractHashedMap.HashEntry localHashEntry = currentEntry();
      if (localHashEntry == null) {
        throw new IllegalStateException("setValue() can only be called after next() and before remove()");
      }
      return localHashEntry.setValue(paramV);
    }
  }
  
  protected static class KeySet<K, V>
    extends AbstractSet<K>
  {
    protected final AbstractHashedMap<K, V> parent;
    
    protected KeySet(AbstractHashedMap<K, V> paramAbstractHashedMap)
    {
      this.parent = paramAbstractHashedMap;
    }
    
    public void clear()
    {
      this.parent.clear();
    }
    
    public boolean contains(Object paramObject)
    {
      return this.parent.containsKey(paramObject);
    }
    
    public Iterator<K> iterator()
    {
      return this.parent.createKeySetIterator();
    }
    
    public boolean remove(Object paramObject)
    {
      boolean bool = this.parent.containsKey(paramObject);
      this.parent.remove(paramObject);
      return bool;
    }
    
    public int size()
    {
      return this.parent.size();
    }
  }
  
  protected static class KeySetIterator<K, V>
    extends AbstractHashedMap.HashIterator<K, V>
    implements Iterator<K>
  {
    protected KeySetIterator(AbstractHashedMap<K, V> paramAbstractHashedMap)
    {
      super();
    }
    
    public K next()
    {
      return super.nextEntry().getKey();
    }
  }
  
  protected static class Values<K, V>
    extends AbstractCollection<V>
  {
    protected final AbstractHashedMap<K, V> parent;
    
    protected Values(AbstractHashedMap<K, V> paramAbstractHashedMap)
    {
      this.parent = paramAbstractHashedMap;
    }
    
    public void clear()
    {
      this.parent.clear();
    }
    
    public boolean contains(Object paramObject)
    {
      return this.parent.containsValue(paramObject);
    }
    
    public Iterator<V> iterator()
    {
      return this.parent.createValuesIterator();
    }
    
    public int size()
    {
      return this.parent.size();
    }
  }
  
  protected static class ValuesIterator<K, V>
    extends AbstractHashedMap.HashIterator<K, V>
    implements Iterator<V>
  {
    protected ValuesIterator(AbstractHashedMap<K, V> paramAbstractHashedMap)
    {
      super();
    }
    
    public V next()
    {
      return super.nextEntry().getValue();
    }
  }
}


/* Location:           C:\Users\Administrator\Documents\ds\反编译\AndroidKiller_v1.3.1\projects\Jungledsq_ppc\ProjectSrc\smali\
 * Qualified Name:     org.jivesoftware.smack.util.collections.AbstractHashedMap
 * JD-Core Version:    0.7.0.1
 */