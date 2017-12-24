package org.jivesoftware.smackx.workgroup.util;

import java.io.PrintStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.ListIterator;

public class ListenerEventDispatcher
  implements Runnable
{
  protected transient boolean hasFinishedDispatching = false;
  protected transient boolean isRunning = false;
  protected transient ArrayList triplets = new ArrayList();
  
  public void addListenerTriplet(Object paramObject, Method paramMethod, Object[] paramArrayOfObject)
  {
    if (!this.isRunning) {
      this.triplets.add(new TripletContainer(paramObject, paramMethod, paramArrayOfObject));
    }
  }
  
  public boolean hasFinished()
  {
    return this.hasFinishedDispatching;
  }
  
  public void run()
  {
    this.isRunning = true;
    ListIterator localListIterator = this.triplets.listIterator();
    while (localListIterator.hasNext())
    {
      TripletContainer localTripletContainer = (TripletContainer)localListIterator.next();
      try
      {
        localTripletContainer.getListenerMethod().invoke(localTripletContainer.getListenerInstance(), localTripletContainer.getMethodArguments());
      }
      catch (Exception localException)
      {
        System.err.println("Exception dispatching an event: " + localException);
        localException.printStackTrace();
      }
    }
    this.hasFinishedDispatching = true;
  }
  
  protected class TripletContainer
  {
    protected Object listenerInstance;
    protected Method listenerMethod;
    protected Object[] methodArguments;
    
    protected TripletContainer(Object paramObject, Method paramMethod, Object[] paramArrayOfObject)
    {
      this.listenerInstance = paramObject;
      this.listenerMethod = paramMethod;
      this.methodArguments = paramArrayOfObject;
    }
    
    protected Object getListenerInstance()
    {
      return this.listenerInstance;
    }
    
    protected Method getListenerMethod()
    {
      return this.listenerMethod;
    }
    
    protected Object[] getMethodArguments()
    {
      return this.methodArguments;
    }
  }
}


/* Location:           C:\Users\Administrator\Documents\ds\反编译\AndroidKiller_v1.3.1\projects\Jungledsq_ppc\ProjectSrc\smali\
 * Qualified Name:     org.jivesoftware.smackx.workgroup.util.ListenerEventDispatcher
 * JD-Core Version:    0.7.0.1
 */