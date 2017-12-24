package org.jivesoftware.smack;

import java.util.Collection;
import org.jivesoftware.smack.packet.Presence;

public abstract interface RosterListener
{
  public abstract void entriesAdded(Collection<String> paramCollection);
  
  public abstract void entriesDeleted(Collection<String> paramCollection);
  
  public abstract void entriesUpdated(Collection<String> paramCollection);
  
  public abstract void presenceChanged(Presence paramPresence);
}


/* Location:           C:\Users\Administrator\Documents\ds\反编译\AndroidKiller_v1.3.1\projects\Jungledsq_ppc\ProjectSrc\smali\
 * Qualified Name:     org.jivesoftware.smack.RosterListener
 * JD-Core Version:    0.7.0.1
 */