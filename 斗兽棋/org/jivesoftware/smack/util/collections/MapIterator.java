package org.jivesoftware.smack.util.collections;

import java.util.Iterator;

public abstract interface MapIterator<K, V>
  extends Iterator<K>
{
  public abstract K getKey();
  
  public abstract V getValue();
  
  public abstract boolean hasNext();
  
  public abstract K next();
  
  public abstract void remove();
  
  public abstract V setValue(V paramV);
}


/* Location:           C:\Users\Administrator\Documents\ds\反编译\AndroidKiller_v1.3.1\projects\Jungledsq_ppc\ProjectSrc\smali\
 * Qualified Name:     org.jivesoftware.smack.util.collections.MapIterator
 * JD-Core Version:    0.7.0.1
 */