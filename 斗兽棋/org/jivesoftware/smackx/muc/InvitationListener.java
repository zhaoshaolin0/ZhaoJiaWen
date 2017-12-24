package org.jivesoftware.smackx.muc;

import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.packet.Message;

public abstract interface InvitationListener
{
  public abstract void invitationReceived(XMPPConnection paramXMPPConnection, String paramString1, String paramString2, String paramString3, String paramString4, Message paramMessage);
}


/* Location:           C:\Users\Administrator\Documents\ds\反编译\AndroidKiller_v1.3.1\projects\Jungledsq_ppc\ProjectSrc\smali\
 * Qualified Name:     org.jivesoftware.smackx.muc.InvitationListener
 * JD-Core Version:    0.7.0.1
 */