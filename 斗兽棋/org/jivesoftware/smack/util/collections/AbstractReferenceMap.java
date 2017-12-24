package org.jivesoftware.smack.util.collections;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.NoSuchElementException;
import java.util.Set;

public abstract class AbstractReferenceMap<K, V>
  extends AbstractHashedMap<K, V>
{
  public static final int HARD = 0;
  public static final int SOFT = 1;
  public static final int WEAK = 2;
  protected int keyType;
  protected boolean purgeValues;
  private transient ReferenceQueue queue;
  protected int valueType;
  
  protected AbstractReferenceMap() {}
  
  protected AbstractReferenceMap(int paramInt1, int paramInt2, int paramInt3, float paramFloat, boolean paramBoolean)
  {
    super(paramInt3, paramFloat);
    verify("keyType", paramInt1);
    verify("valueType", paramInt2);
    this.keyType = paramInt1;
    this.valueType = paramInt2;
    this.purgeValues = paramBoolean;
  }
  
  private static void verify(String paramString, int paramInt)
  {
    if ((paramInt < 0) || (paramInt > 2)) {
      throw new IllegalArgumentException(paramString + " must be HARD, SOFT, WEAK.");
    }
  }
  
  public void clear()
  {
    super.clear();
    while (this.queue.poll() != null) {}
  }
  
  public boolean containsKey(Object paramObject)
  {
    purgeBeforeRead();
    paramObject = getEntry(paramObject);
    if (paramObject == null) {
      return false;
    }
    return paramObject.getValue() != null;
  }
  
  public boolean containsValue(Object paramObject)
  {
    purgeBeforeRead();
    if (paramObject == null) {
      return false;
    }
    return super.containsValue(paramObject);
  }
  
  public AbstractHashedMap.HashEntry<K, V> createEntry(AbstractHashedMap.HashEntry<K, V> paramHashEntry, int paramInt, K paramK, V paramV)
  {
    return new ReferenceEntry(this, (ReferenceEntry)paramHashEntry, paramInt, paramK, paramV);
  }
  
  protected Iterator<Map.Entry<K, V>> createEntrySetIterator()
  {
    return new ReferenceEntrySetIterator(this);
  }
  
  protected Iterator<K> createKeySetIterator()
  {
    return new ReferenceKeySetIterator(this);
  }
  
  protected Iterator<V> createValuesIterator()
  {
    return new ReferenceValuesIterator(this);
  }
  
  protected void doReadObject(ObjectInputStream paramObjectInputStream)
    throws IOException, ClassNotFoundException
  {
    this.keyType = paramObjectInputStream.readInt();
    this.valueType = paramObjectInputStream.readInt();
    this.purgeValues = paramObjectInputStream.readBoolean();
    this.loadFactor = paramObjectInputStream.readFloat();
    int i = paramObjectInputStream.readInt();
    init();
    this.data = new AbstractHashedMap.HashEntry[i];
    for (;;)
    {
      Object localObject = paramObjectInputStream.readObject();
      if (localObject == null)
      {
        this.threshold = calculateThreshold(this.data.length, this.loadFactor);
        return;
      }
      put(localObject, paramObjectInputStream.readObject());
    }
  }
  
  protected void doWriteObject(ObjectOutputStream paramObjectOutputStream)
    throws IOException
  {
    paramObjectOutputStream.writeInt(this.keyType);
    paramObjectOutputStream.writeInt(this.valueType);
    paramObjectOutputStream.writeBoolean(this.purgeValues);
    paramObjectOutputStream.writeFloat(this.loadFactor);
    paramObjectOutputStream.writeInt(this.data.length);
    MapIterator localMapIterator = mapIterator();
    while (localMapIterator.hasNext())
    {
      paramObjectOutputStream.writeObject(localMapIterator.next());
      paramObjectOutputStream.writeObject(localMapIterator.getValue());
    }
    paramObjectOutputStream.writeObject(null);
  }
  
  public Set<Map.Entry<K, V>> entrySet()
  {
    if (this.entrySet == null) {
      this.entrySet = new ReferenceEntrySet(this);
    }
    return this.entrySet;
  }
  
  public V get(Object paramObject)
  {
    purgeBeforeRead();
    paramObject = getEntry(paramObject);
    if (paramObject == null) {
      return null;
    }
    return paramObject.getValue();
  }
  
  protected AbstractHashedMap.HashEntry<K, V> getEntry(Object paramObject)
  {
    if (paramObject == null) {
      return null;
    }
    return super.getEntry(paramObject);
  }
  
  protected int hashEntry(Object paramObject1, Object paramObject2)
  {
    int j = 0;
    int i;
    if (paramObject1 == null)
    {
      i = 0;
      if (paramObject2 != null) {
        break label26;
      }
    }
    for (;;)
    {
      return i ^ j;
      i = paramObject1.hashCode();
      break;
      label26:
      j = paramObject2.hashCode();
    }
  }
  
  protected void init()
  {
    this.queue = new ReferenceQueue();
  }
  
  public boolean isEmpty()
  {
    purgeBeforeRead();
    return super.isEmpty();
  }
  
  protected boolean isEqualKey(Object paramObject1, Object paramObject2)
  {
    return (paramObject1 == paramObject2) || (paramObject1.equals(paramObject2));
  }
  
  public Set<K> keySet()
  {
    if (this.keySet == null) {
      this.keySet = new ReferenceKeySet(this);
    }
    return this.keySet;
  }
  
  public MapIterator<K, V> mapIterator()
  {
    return new ReferenceMapIterator(this);
  }
  
  protected void purge()
  {
    for (Reference localReference = this.queue.poll(); localReference != null; localReference = this.queue.poll()) {
      purge(localReference);
    }
  }
  
  protected void purge(Reference paramReference)
  {
    int i = hashIndex(paramReference.hashCode(), this.data.length);
    Object localObject = null;
    for (AbstractHashedMap.HashEntry localHashEntry = this.data[i];; localHashEntry = localHashEntry.next)
    {
      if (localHashEntry != null)
      {
        if (!((ReferenceEntry)localHashEntry).purge(paramReference)) {
          break label77;
        }
        if (localObject != null) {
          break label65;
        }
        this.data[i] = localHashEntry.next;
      }
      for (;;)
      {
        this.size -= 1;
        return;
        label65:
        localObject.next = localHashEntry.next;
      }
      label77:
      localObject = localHashEntry;
    }
  }
  
  protected void purgeBeforeRead()
  {
    purge();
  }
  
  protected void purgeBeforeWrite()
  {
    purge();
  }
  
  public V put(K paramK, V paramV)
  {
    if (paramK == null) {
      throw new NullPointerException("null keys not allowed");
    }
    if (paramV == null) {
      throw new NullPointerException("null values not allowed");
    }
    purgeBeforeWrite();
    return super.put(paramK, paramV);
  }
  
  public V remove(Object paramObject)
  {
    if (paramObject == null) {
      return null;
    }
    purgeBeforeWrite();
    return super.remove(paramObject);
  }
  
  public int size()
  {
    purgeBeforeRead();
    return super.size();
  }
  
  public Collection<V> values()
  {
    if (this.values == null) {
      this.values = new ReferenceValues(this);
    }
    return this.values;
  }
  
  protected static class ReferenceEntry<K, V>
    extends AbstractHashedMap.HashEntry<K, V>
  {
    protected final AbstractReferenceMap<K, V> parent;
    protected Reference<K> refKey;
    protected Reference<V> refValue;
    
    public ReferenceEntry(AbstractReferenceMap<K, V> paramAbstractReferenceMap, ReferenceEntry<K, V> paramReferenceEntry, int paramInt, K paramK, V paramV)
    {
      super(paramInt, null, null);
      this.parent = paramAbstractReferenceMap;
      if (paramAbstractReferenceMap.keyType != 0) {
        this.refKey = toReference(paramAbstractReferenceMap.keyType, paramK, paramInt);
      }
      while (paramAbstractReferenceMap.valueType != 0)
      {
        this.refValue = toReference(paramAbstractReferenceMap.valueType, paramV, paramInt);
        return;
        setKey(paramK);
      }
      setValue(paramV);
    }
    
    public boolean equals(Object paramObject)
    {
      if (paramObject == this) {
        return true;
      }
      if (!(paramObject instanceof Map.Entry)) {
        return false;
      }
      Object localObject = (Map.Entry)paramObject;
      paramObject = ((Map.Entry)localObject).getKey();
      localObject = ((Map.Entry)localObject).getValue();
      if ((paramObject == null) || (localObject == null)) {
        return false;
      }
      return (this.parent.isEqualKey(paramObject, getKey())) && (this.parent.isEqualValue(localObject, getValue()));
    }
    
    public K getKey()
    {
      if (this.parent.keyType > 0) {
        return this.refKey.get();
      }
      return super.getKey();
    }
    
    public V getValue()
    {
      if (this.parent.valueType > 0) {
        return this.refValue.get();
      }
      return super.getValue();
    }
    
    public int hashCode()
    {
      return this.parent.hashEntry(getKey(), getValue());
    }
    
    protected ReferenceEntry<K, V> next()
    {
      return (ReferenceEntry)this.next;
    }
    
    boolean purge(Reference paramReference)
    {
      int i;
      boolean bool;
      if ((this.parent.keyType > 0) && (this.refKey == paramReference))
      {
        i = 1;
        if ((i == 0) && ((this.parent.valueType <= 0) || (this.refValue != paramReference))) {
          break label89;
        }
        bool = true;
        label44:
        if (bool)
        {
          if (this.parent.keyType > 0) {
            this.refKey.clear();
          }
          if (this.parent.valueType <= 0) {
            break label94;
          }
          this.refValue.clear();
        }
      }
      label89:
      label94:
      while (!this.parent.purgeValues)
      {
        return bool;
        i = 0;
        break;
        bool = false;
        break label44;
      }
      setValue(null);
      return bool;
    }
    
    public V setValue(V paramV)
    {
      Object localObject = getValue();
      if (this.parent.valueType > 0)
      {
        this.refValue.clear();
        this.refValue = toReference(this.parent.valueType, paramV, this.hashCode);
        return localObject;
      }
      super.setValue(paramV);
      return localObject;
    }
    
    protected <T> Reference<T> toReference(int paramInt1, T paramT, int paramInt2)
    {
      switch (paramInt1)
      {
      default: 
        throw new Error("Attempt to create hard reference in ReferenceMap!");
      case 1: 
        return new AbstractReferenceMap.SoftRef(paramInt2, paramT, this.parent.queue);
      }
      return new AbstractReferenceMap.WeakRef(paramInt2, paramT, this.parent.queue);
    }
  }
  
  static class ReferenceEntrySet<K, V>
    extends AbstractHashedMap.EntrySet<K, V>
  {
    protected ReferenceEntrySet(AbstractHashedMap<K, V> paramAbstractHashedMap)
    {
      super();
    }
    
    public Object[] toArray()
    {
      return toArray(new Object[0]);
    }
    
    public <T> T[] toArray(T[] paramArrayOfT)
    {
      ArrayList localArrayList = new ArrayList();
      Iterator localIterator = iterator();
      while (localIterator.hasNext())
      {
        Map.Entry localEntry = (Map.Entry)localIterator.next();
        localArrayList.add(new DefaultMapEntry(localEntry.getKey(), localEntry.getValue()));
      }
      return localArrayList.toArray(paramArrayOfT);
    }
  }
  
  static class ReferenceEntrySetIterator<K, V>
    extends AbstractReferenceMap.ReferenceIteratorBase<K, V>
    implements Iterator<Map.Entry<K, V>>
  {
    public ReferenceEntrySetIterator(AbstractReferenceMap<K, V> paramAbstractReferenceMap)
    {
      super();
    }
    
    public AbstractReferenceMap.ReferenceEntry<K, V> next()
    {
      return superNext();
    }
  }
  
  static class ReferenceIteratorBase<K, V>
  {
    K currentKey;
    V currentValue;
    AbstractReferenceMap.ReferenceEntry<K, V> entry;
    int expectedModCount;
    int index;
    K nextKey;
    V nextValue;
    final AbstractReferenceMap<K, V> parent;
    AbstractReferenceMap.ReferenceEntry<K, V> previous;
    
    public ReferenceIteratorBase(AbstractReferenceMap<K, V> paramAbstractReferenceMap)
    {
      this.parent = paramAbstractReferenceMap;
      if (paramAbstractReferenceMap.size() != 0) {}
      for (int i = paramAbstractReferenceMap.data.length;; i = 0)
      {
        this.index = i;
        this.expectedModCount = paramAbstractReferenceMap.modCount;
        return;
      }
    }
    
    private void checkMod()
    {
      if (this.parent.modCount != this.expectedModCount) {
        throw new ConcurrentModificationException();
      }
    }
    
    private boolean nextNull()
    {
      return (this.nextKey == null) || (this.nextValue == null);
    }
    
    protected AbstractReferenceMap.ReferenceEntry<K, V> currentEntry()
    {
      checkMod();
      return this.previous;
    }
    
    public boolean hasNext()
    {
      checkMod();
      while (nextNull())
      {
        AbstractReferenceMap.ReferenceEntry localReferenceEntry = this.entry;
        int i = this.index;
        while ((localReferenceEntry == null) && (i > 0))
        {
          i -= 1;
          localReferenceEntry = (AbstractReferenceMap.ReferenceEntry)this.parent.data[i];
        }
        this.entry = localReferenceEntry;
        this.index = i;
        if (localReferenceEntry == null)
        {
          this.currentKey = null;
          this.currentValue = null;
          return false;
        }
        this.nextKey = localReferenceEntry.getKey();
        this.nextValue = localReferenceEntry.getValue();
        if (nextNull()) {
          this.entry = this.entry.next();
        }
      }
      return true;
    }
    
    protected AbstractReferenceMap.ReferenceEntry<K, V> nextEntry()
    {
      checkMod();
      if ((nextNull()) && (!hasNext())) {
        throw new NoSuchElementException();
      }
      this.previous = this.entry;
      this.entry = this.entry.next();
      this.currentKey = this.nextKey;
      this.currentValue = this.nextValue;
      this.nextKey = null;
      this.nextValue = null;
      return this.previous;
    }
    
    public void remove()
    {
      checkMod();
      if (this.previous == null) {
        throw new IllegalStateException();
      }
      this.parent.remove(this.currentKey);
      this.previous = null;
      this.currentKey = null;
      this.currentValue = null;
      this.expectedModCount = this.parent.modCount;
    }
    
    public AbstractReferenceMap.ReferenceEntry<K, V> superNext()
    {
      return nextEntry();
    }
  }
  
  static class ReferenceKeySet<K, V>
    extends AbstractHashedMap.KeySet<K, V>
  {
    protected ReferenceKeySet(AbstractHashedMap<K, V> paramAbstractHashedMap)
    {
      super();
    }
    
    public Object[] toArray()
    {
      return toArray(new Object[0]);
    }
    
    public <T> T[] toArray(T[] paramArrayOfT)
    {
      ArrayList localArrayList = new ArrayList(this.parent.size());
      Iterator localIterator = iterator();
      while (localIterator.hasNext()) {
        localArrayList.add(localIterator.next());
      }
      return localArrayList.toArray(paramArrayOfT);
    }
  }
  
  static class ReferenceKeySetIterator<K, V>
    extends AbstractReferenceMap.ReferenceIteratorBase<K, V>
    implements Iterator<K>
  {
    ReferenceKeySetIterator(AbstractReferenceMap<K, V> paramAbstractReferenceMap)
    {
      super();
    }
    
    public K next()
    {
      return nextEntry().getKey();
    }
  }
  
  static class ReferenceMapIterator<K, V>
    extends AbstractReferenceMap.ReferenceIteratorBase<K, V>
    implements MapIterator<K, V>
  {
    protected ReferenceMapIterator(AbstractReferenceMap<K, V> paramAbstractReferenceMap)
    {
      super();
    }
    
    public K getKey()
    {
      AbstractReferenceMap.ReferenceEntry localReferenceEntry = currentEntry();
      if (localReferenceEntry == null) {
        throw new IllegalStateException("getKey() can only be called after next() and before remove()");
      }
      return localReferenceEntry.getKey();
    }
    
    public V getValue()
    {
      AbstractReferenceMap.ReferenceEntry localReferenceEntry = currentEntry();
      if (localReferenceEntry == null) {
        throw new IllegalStateException("getValue() can only be called after next() and before remove()");
      }
      return localReferenceEntry.getValue();
    }
    
    public K next()
    {
      return nextEntry().getKey();
    }
    
    public V setValue(V paramV)
    {
      AbstractReferenceMap.ReferenceEntry localReferenceEntry = currentEntry();
      if (localReferenceEntry == null) {
        throw new IllegalStateException("setValue() can only be called after next() and before remove()");
      }
      return localReferenceEntry.setValue(paramV);
    }
  }
  
  static class ReferenceValues<K, V>
    extends AbstractHashedMap.Values<K, V>
  {
    protected ReferenceValues(AbstractHashedMap<K, V> paramAbstractHashedMap)
    {
      super();
    }
    
    public Object[] toArray()
    {
      return toArray(new Object[0]);
    }
    
    public <T> T[] toArray(T[] paramArrayOfT)
    {
      ArrayList localArrayList = new ArrayList(this.parent.size());
      Iterator localIterator = iterator();
      while (localIterator.hasNext()) {
        localArrayList.add(localIterator.next());
      }
      return localArrayList.toArray(paramArrayOfT);
    }
  }
  
  static class ReferenceValuesIterator<K, V>
    extends AbstractReferenceMap.ReferenceIteratorBase<K, V>
    implements Iterator<V>
  {
    ReferenceValuesIterator(AbstractReferenceMap<K, V> paramAbstractReferenceMap)
    {
      super();
    }
    
    public V next()
    {
      return nextEntry().getValue();
    }
  }
  
  static class SoftRef<T>
    extends SoftReference<T>
  {
    private int hash;
    
    public SoftRef(int paramInt, T paramT, ReferenceQueue paramReferenceQueue)
    {
      super(paramReferenceQueue);
      this.hash = paramInt;
    }
    
    public int hashCode()
    {
      return this.hash;
    }
  }
  
  static class WeakRef<T>
    extends WeakReference<T>
  {
    private int hash;
    
    public WeakRef(int paramInt, T paramT, ReferenceQueue paramReferenceQueue)
    {
      super(paramReferenceQueue);
      this.hash = paramInt;
    }
    
    public int hashCode()
    {
      return this.hash;
    }
  }
}


/* Location:           C:\Users\Administrator\Documents\ds\反编译\AndroidKiller_v1.3.1\projects\Jungledsq_ppc\ProjectSrc\smali\
 * Qualified Name:     org.jivesoftware.smack.util.collections.AbstractReferenceMap
 * JD-Core Version:    0.7.0.1
 */