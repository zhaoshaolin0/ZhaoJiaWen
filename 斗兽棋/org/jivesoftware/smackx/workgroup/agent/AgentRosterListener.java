package org.jivesoftware.smackx.workgroup.agent;

import org.jivesoftware.smack.packet.Presence;

public abstract interface AgentRosterListener
{
  public abstract void agentAdded(String paramString);
  
  public abstract void agentRemoved(String paramString);
  
  public abstract void presenceChanged(Presence paramPresence);
}


/* Location:           C:\Users\Administrator\Documents\ds\反编译\AndroidKiller_v1.3.1\projects\Jungledsq_ppc\ProjectSrc\smali\
 * Qualified Name:     org.jivesoftware.smackx.workgroup.agent.AgentRosterListener
 * JD-Core Version:    0.7.0.1
 */