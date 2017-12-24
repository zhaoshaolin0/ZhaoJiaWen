package org.jivesoftware.smack;

import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.PasswordCallback;
import org.jivesoftware.smack.filter.PacketIDFilter;
import org.jivesoftware.smack.packet.Authentication;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.packet.IQ.Type;
import org.jivesoftware.smack.packet.Packet;

class NonSASLAuthentication
  implements UserAuthentication
{
  private XMPPConnection connection;
  
  public NonSASLAuthentication(XMPPConnection paramXMPPConnection)
  {
    this.connection = paramXMPPConnection;
  }
  
  public String authenticate(String paramString1, String paramString2, String paramString3)
    throws XMPPException
  {
    Object localObject2 = new Authentication();
    ((Authentication)localObject2).setType(IQ.Type.GET);
    ((Authentication)localObject2).setUsername(paramString1);
    Object localObject1 = this.connection.createPacketCollector(new PacketIDFilter(((Authentication)localObject2).getPacketID()));
    this.connection.sendPacket((Packet)localObject2);
    localObject2 = (IQ)((PacketCollector)localObject1).nextResult(SmackConfiguration.getPacketReplyTimeout());
    if (localObject2 == null) {
      throw new XMPPException("No response from the server.");
    }
    if (((IQ)localObject2).getType() == IQ.Type.ERROR) {
      throw new XMPPException(((IQ)localObject2).getError());
    }
    localObject2 = (Authentication)localObject2;
    ((PacketCollector)localObject1).cancel();
    localObject1 = new Authentication();
    ((Authentication)localObject1).setUsername(paramString1);
    if (((Authentication)localObject2).getDigest() != null) {
      ((Authentication)localObject1).setDigest(this.connection.getConnectionID(), paramString2);
    }
    for (;;)
    {
      ((Authentication)localObject1).setResource(paramString3);
      paramString1 = this.connection.createPacketCollector(new PacketIDFilter(((Authentication)localObject1).getPacketID()));
      this.connection.sendPacket((Packet)localObject1);
      paramString2 = (IQ)paramString1.nextResult(SmackConfiguration.getPacketReplyTimeout());
      if (paramString2 != null) {
        break label242;
      }
      throw new XMPPException("Authentication failed.");
      if (((Authentication)localObject2).getPassword() == null) {
        break;
      }
      ((Authentication)localObject1).setPassword(paramString2);
    }
    throw new XMPPException("Server does not support compatible authentication mechanism.");
    label242:
    if (paramString2.getType() == IQ.Type.ERROR) {
      throw new XMPPException(paramString2.getError());
    }
    paramString1.cancel();
    return paramString2.getTo();
  }
  
  public String authenticate(String paramString1, String paramString2, CallbackHandler paramCallbackHandler)
    throws XMPPException
  {
    PasswordCallback localPasswordCallback = new PasswordCallback("Password: ", false);
    try
    {
      paramCallbackHandler.handle(new Callback[] { localPasswordCallback });
      paramString1 = authenticate(paramString1, String.valueOf(localPasswordCallback.getPassword()), paramString2);
      return paramString1;
    }
    catch (Exception paramString1)
    {
      throw new XMPPException("Unable to determine password.", paramString1);
    }
  }
  
  public String authenticateAnonymously()
    throws XMPPException
  {
    Object localObject = new Authentication();
    PacketCollector localPacketCollector = this.connection.createPacketCollector(new PacketIDFilter(((Authentication)localObject).getPacketID()));
    this.connection.sendPacket((Packet)localObject);
    localObject = (IQ)localPacketCollector.nextResult(SmackConfiguration.getPacketReplyTimeout());
    if (localObject == null) {
      throw new XMPPException("Anonymous login failed.");
    }
    if (((IQ)localObject).getType() == IQ.Type.ERROR) {
      throw new XMPPException(((IQ)localObject).getError());
    }
    localPacketCollector.cancel();
    if (((IQ)localObject).getTo() != null) {
      return ((IQ)localObject).getTo();
    }
    return this.connection.serviceName + "/" + ((Authentication)localObject).getResource();
  }
}


/* Location:           C:\Users\Administrator\Documents\ds\反编译\AndroidKiller_v1.3.1\projects\Jungledsq_ppc\ProjectSrc\smali\
 * Qualified Name:     org.jivesoftware.smack.NonSASLAuthentication
 * JD-Core Version:    0.7.0.1
 */