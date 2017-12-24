package org.jivesoftware.smackx.workgroup.util;

import java.util.Iterator;
import java.util.ListIterator;

class ReverseListIterator
  implements Iterator
{
  private ListIterator _i;
  
  ReverseListIterator(ListIterator paramListIterator)
  {
    this._i = paramListIterator;
    while (this._i.hasNext()) {
      this._i.next();
    }
  }
  
  public boolean hasNext()
  {
    return this._i.hasPrevious();
  }
  
  public Object next()
  {
    return this._i.previous();
  }
  
  public void remove()
  {
    this._i.remove();
  }
}


/* Location:           C:\Users\Administrator\Documents\ds\反编译\AndroidKiller_v1.3.1\projects\Jungledsq_ppc\ProjectSrc\smali\
 * Qualified Name:     org.jivesoftware.smackx.workgroup.util.ReverseListIterator
 * JD-Core Version:    0.7.0.1
 */