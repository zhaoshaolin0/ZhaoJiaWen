package org.jivesoftware.smack.util.collections;

import java.util.NoSuchElementException;

abstract class AbstractEmptyIterator<E>
{
  public void add(E paramE)
  {
    throw new UnsupportedOperationException("add() not supported for empty Iterator");
  }
  
  public E getKey()
  {
    throw new IllegalStateException("Iterator contains no elements");
  }
  
  public E getValue()
  {
    throw new IllegalStateException("Iterator contains no elements");
  }
  
  public boolean hasNext()
  {
    return false;
  }
  
  public boolean hasPrevious()
  {
    return false;
  }
  
  public E next()
  {
    throw new NoSuchElementException("Iterator contains no elements");
  }
  
  public int nextIndex()
  {
    return 0;
  }
  
  public E previous()
  {
    throw new NoSuchElementException("Iterator contains no elements");
  }
  
  public int previousIndex()
  {
    return -1;
  }
  
  public void remove()
  {
    throw new IllegalStateException("Iterator contains no elements");
  }
  
  public void reset() {}
  
  public void set(E paramE)
  {
    throw new IllegalStateException("Iterator contains no elements");
  }
  
  public E setValue(E paramE)
  {
    throw new IllegalStateException("Iterator contains no elements");
  }
}


/* Location:           C:\Users\Administrator\Documents\ds\反编译\AndroidKiller_v1.3.1\projects\Jungledsq_ppc\ProjectSrc\smali\
 * Qualified Name:     org.jivesoftware.smack.util.collections.AbstractEmptyIterator
 * JD-Core Version:    0.7.0.1
 */