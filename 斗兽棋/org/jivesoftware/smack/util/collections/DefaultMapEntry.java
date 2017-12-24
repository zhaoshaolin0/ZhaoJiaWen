package org.jivesoftware.smack.util.collections;

import java.util.Map.Entry;

public final class DefaultMapEntry<K, V>
  extends AbstractMapEntry<K, V>
{
  public DefaultMapEntry(K paramK, V paramV)
  {
    super(paramK, paramV);
  }
  
  public DefaultMapEntry(Map.Entry<K, V> paramEntry)
  {
    super(paramEntry.getKey(), paramEntry.getValue());
  }
  
  public DefaultMapEntry(KeyValue<K, V> paramKeyValue)
  {
    super(paramKeyValue.getKey(), paramKeyValue.getValue());
  }
}


/* Location:           C:\Users\Administrator\Documents\ds\反编译\AndroidKiller_v1.3.1\projects\Jungledsq_ppc\ProjectSrc\smali\
 * Qualified Name:     org.jivesoftware.smack.util.collections.DefaultMapEntry
 * JD-Core Version:    0.7.0.1
 */