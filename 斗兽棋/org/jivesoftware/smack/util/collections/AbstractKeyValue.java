package org.jivesoftware.smack.util.collections;

public abstract class AbstractKeyValue<K, V>
  implements KeyValue<K, V>
{
  protected K key;
  protected V value;
  
  protected AbstractKeyValue(K paramK, V paramV)
  {
    this.key = paramK;
    this.value = paramV;
  }
  
  public K getKey()
  {
    return this.key;
  }
  
  public V getValue()
  {
    return this.value;
  }
  
  public String toString()
  {
    return getKey() + '=' + getValue();
  }
}


/* Location:           C:\Users\Administrator\Documents\ds\反编译\AndroidKiller_v1.3.1\projects\Jungledsq_ppc\ProjectSrc\smali\
 * Qualified Name:     org.jivesoftware.smack.util.collections.AbstractKeyValue
 * JD-Core Version:    0.7.0.1
 */