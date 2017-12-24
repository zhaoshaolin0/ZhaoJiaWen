package org.jivesoftware.smack.util.collections;

import java.util.Iterator;

public class EmptyIterator<E>
  extends AbstractEmptyIterator<E>
  implements ResettableIterator<E>
{
  public static final Iterator INSTANCE = RESETTABLE_INSTANCE;
  public static final ResettableIterator RESETTABLE_INSTANCE = new EmptyIterator();
  
  public static <T> Iterator<T> getInstance()
  {
    return INSTANCE;
  }
}


/* Location:           C:\Users\Administrator\Documents\ds\反编译\AndroidKiller_v1.3.1\projects\Jungledsq_ppc\ProjectSrc\smali\
 * Qualified Name:     org.jivesoftware.smack.util.collections.EmptyIterator
 * JD-Core Version:    0.7.0.1
 */