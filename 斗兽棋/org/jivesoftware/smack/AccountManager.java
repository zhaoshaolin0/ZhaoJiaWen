package org.jivesoftware.smack;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.jivesoftware.smack.filter.AndFilter;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.filter.PacketIDFilter;
import org.jivesoftware.smack.filter.PacketTypeFilter;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.packet.IQ.Type;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.packet.Registration;
import org.jivesoftware.smack.util.StringUtils;

public class AccountManager
{
  private boolean accountCreationSupported = false;
  private XMPPConnection connection;
  private Registration info = null;
  
  public AccountManager(XMPPConnection paramXMPPConnection)
  {
    this.connection = paramXMPPConnection;
  }
  
  private void getRegistrationInfo()
    throws XMPPException
  {
    try
    {
      Object localObject1 = new Registration();
      ((Registration)localObject1).setTo(this.connection.getServiceName());
      Object localObject3 = new AndFilter(new PacketFilter[] { new PacketIDFilter(((Registration)localObject1).getPacketID()), new PacketTypeFilter(IQ.class) });
      localObject3 = this.connection.createPacketCollector((PacketFilter)localObject3);
      this.connection.sendPacket((Packet)localObject1);
      localObject1 = (IQ)((PacketCollector)localObject3).nextResult(SmackConfiguration.getPacketReplyTimeout());
      ((PacketCollector)localObject3).cancel();
      if (localObject1 == null) {
        throw new XMPPException("No response from server.");
      }
    }
    finally {}
    if (localObject2.getType() == IQ.Type.ERROR) {
      throw new XMPPException(localObject2.getError());
    }
    this.info = ((Registration)localObject2);
  }
  
  public void changePassword(String paramString)
    throws XMPPException
  {
    Object localObject = new Registration();
    ((Registration)localObject).setType(IQ.Type.SET);
    ((Registration)localObject).setTo(this.connection.getServiceName());
    HashMap localHashMap = new HashMap();
    localHashMap.put("username", StringUtils.parseName(this.connection.getUser()));
    localHashMap.put("password", paramString);
    ((Registration)localObject).setAttributes(localHashMap);
    paramString = new AndFilter(new PacketFilter[] { new PacketIDFilter(((Registration)localObject).getPacketID()), new PacketTypeFilter(IQ.class) });
    paramString = this.connection.createPacketCollector(paramString);
    this.connection.sendPacket((Packet)localObject);
    localObject = (IQ)paramString.nextResult(SmackConfiguration.getPacketReplyTimeout());
    paramString.cancel();
    if (localObject == null) {
      throw new XMPPException("No response from server.");
    }
    if (((IQ)localObject).getType() == IQ.Type.ERROR) {
      throw new XMPPException(((IQ)localObject).getError());
    }
  }
  
  public void createAccount(String paramString1, String paramString2)
    throws XMPPException
  {
    if (!supportsAccountCreation()) {
      throw new XMPPException("Server does not support account creation.");
    }
    HashMap localHashMap = new HashMap();
    Iterator localIterator = getAccountAttributes().iterator();
    while (localIterator.hasNext()) {
      localHashMap.put((String)localIterator.next(), "");
    }
    createAccount(paramString1, paramString2, localHashMap);
  }
  
  public void createAccount(String paramString1, String paramString2, Map<String, String> paramMap)
    throws XMPPException
  {
    if (!supportsAccountCreation()) {
      throw new XMPPException("Server does not support account creation.");
    }
    Registration localRegistration = new Registration();
    localRegistration.setType(IQ.Type.SET);
    localRegistration.setTo(this.connection.getServiceName());
    paramMap.put("username", paramString1);
    paramMap.put("password", paramString2);
    localRegistration.setAttributes(paramMap);
    paramString1 = new AndFilter(new PacketFilter[] { new PacketIDFilter(localRegistration.getPacketID()), new PacketTypeFilter(IQ.class) });
    paramString1 = this.connection.createPacketCollector(paramString1);
    this.connection.sendPacket(localRegistration);
    paramString2 = (IQ)paramString1.nextResult(SmackConfiguration.getPacketReplyTimeout());
    paramString1.cancel();
    if (paramString2 == null) {
      throw new XMPPException("No response from server.");
    }
    if (paramString2.getType() == IQ.Type.ERROR) {
      throw new XMPPException(paramString2.getError());
    }
  }
  
  public void deleteAccount()
    throws XMPPException
  {
    if (!this.connection.isAuthenticated()) {
      throw new IllegalStateException("Must be logged in to delete a account.");
    }
    Object localObject1 = new Registration();
    ((Registration)localObject1).setType(IQ.Type.SET);
    ((Registration)localObject1).setTo(this.connection.getServiceName());
    Object localObject2 = new HashMap();
    ((Map)localObject2).put("remove", "");
    ((Registration)localObject1).setAttributes((Map)localObject2);
    localObject2 = new AndFilter(new PacketFilter[] { new PacketIDFilter(((Registration)localObject1).getPacketID()), new PacketTypeFilter(IQ.class) });
    localObject2 = this.connection.createPacketCollector((PacketFilter)localObject2);
    this.connection.sendPacket((Packet)localObject1);
    localObject1 = (IQ)((PacketCollector)localObject2).nextResult(SmackConfiguration.getPacketReplyTimeout());
    ((PacketCollector)localObject2).cancel();
    if (localObject1 == null) {
      throw new XMPPException("No response from server.");
    }
    if (((IQ)localObject1).getType() == IQ.Type.ERROR) {
      throw new XMPPException(((IQ)localObject1).getError());
    }
  }
  
  public String getAccountAttribute(String paramString)
  {
    try
    {
      if (this.info == null) {
        getRegistrationInfo();
      }
      paramString = (String)this.info.getAttributes().get(paramString);
      return paramString;
    }
    catch (XMPPException paramString)
    {
      paramString.printStackTrace();
    }
    return null;
  }
  
  public Collection<String> getAccountAttributes()
  {
    try
    {
      if (this.info == null) {
        getRegistrationInfo();
      }
      Object localObject = this.info.getAttributes();
      if (localObject != null)
      {
        localObject = Collections.unmodifiableSet(((Map)localObject).keySet());
        return localObject;
      }
    }
    catch (XMPPException localXMPPException)
    {
      localXMPPException.printStackTrace();
    }
    return Collections.emptySet();
  }
  
  public String getAccountInstructions()
  {
    try
    {
      if (this.info == null) {
        getRegistrationInfo();
      }
      String str = this.info.getInstructions();
      return str;
    }
    catch (XMPPException localXMPPException) {}
    return null;
  }
  
  void setSupportsAccountCreation(boolean paramBoolean)
  {
    this.accountCreationSupported = paramBoolean;
  }
  
  public boolean supportsAccountCreation()
  {
    if (this.accountCreationSupported) {
      return true;
    }
    try
    {
      if (this.info == null)
      {
        getRegistrationInfo();
        if (this.info.getType() == IQ.Type.ERROR) {
          break label47;
        }
      }
      label47:
      for (boolean bool = true;; bool = false)
      {
        this.accountCreationSupported = bool;
        bool = this.accountCreationSupported;
        return bool;
      }
      return false;
    }
    catch (XMPPException localXMPPException) {}
  }
}


/* Location:           C:\Users\Administrator\Documents\ds\反编译\AndroidKiller_v1.3.1\projects\Jungledsq_ppc\ProjectSrc\smali\
 * Qualified Name:     org.jivesoftware.smack.AccountManager
 * JD-Core Version:    0.7.0.1
 */