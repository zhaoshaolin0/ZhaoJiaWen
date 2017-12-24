package org.jivesoftware.smack.util.collections;

import java.util.Map.Entry;

public abstract class AbstractMapEntry<K, V>
  extends AbstractKeyValue<K, V>
  implements Map.Entry<K, V>
{
  protected AbstractMapEntry(K paramK, V paramV)
  {
    super(paramK, paramV);
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
  
  public V setValue(V paramV)
  {
    Object localObject = this.value;
    this.value = paramV;
    return localObject;
  }
}


/* Location:           C:\Users\Administrator\Documents\ds\反编译\AndroidKiller_v1.3.1\projects\Jungledsq_ppc\ProjectSrc\smali\
 * Qualified Name:     org.jivesoftware.smack.util.collections.AbstractMapEntry
 * JD-Core Version:    0.7.0.1
 */