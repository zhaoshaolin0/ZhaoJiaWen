package org.jivesoftware.smack.util.collections;

import java.util.Iterator;

public abstract interface ResettableIterator<E>
  extends Iterator<E>
{
  public abstract void reset();
}


/* Location:           C:\Users\Administrator\Documents\ds\反编译\AndroidKiller_v1.3.1\projects\Jungledsq_ppc\ProjectSrc\smali\
 * Qualified Name:     org.jivesoftware.smack.util.collections.ResettableIterator
 * JD-Core Version:    0.7.0.1
 */