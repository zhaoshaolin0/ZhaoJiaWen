package org.jivesoftware.smack.packet;

public abstract interface PacketExtension
{
  public abstract String getElementName();
  
  public abstract String getNamespace();
  
  public abstract String toXML();
}


/* Location:           C:\Users\Administrator\Documents\ds\反编译\AndroidKiller_v1.3.1\projects\Jungledsq_ppc\ProjectSrc\smali\
 * Qualified Name:     org.jivesoftware.smack.packet.PacketExtension
 * JD-Core Version:    0.7.0.1
 */