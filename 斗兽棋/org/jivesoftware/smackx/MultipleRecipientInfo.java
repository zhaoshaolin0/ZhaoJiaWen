package org.jivesoftware.smackx;

import java.util.List;
import org.jivesoftware.smackx.packet.MultipleAddresses;
import org.jivesoftware.smackx.packet.MultipleAddresses.Address;

public class MultipleRecipientInfo
{
  MultipleAddresses extension;
  
  MultipleRecipientInfo(MultipleAddresses paramMultipleAddresses)
  {
    this.extension = paramMultipleAddresses;
  }
  
  public List getCCAddresses()
  {
    return this.extension.getAddressesOfType("cc");
  }
  
  public MultipleAddresses.Address getReplyAddress()
  {
    List localList = this.extension.getAddressesOfType("replyto");
    if (localList.isEmpty()) {
      return null;
    }
    return (MultipleAddresses.Address)localList.get(0);
  }
  
  public String getReplyRoom()
  {
    List localList = this.extension.getAddressesOfType("replyroom");
    if (localList.isEmpty()) {
      return null;
    }
    return ((MultipleAddresses.Address)localList.get(0)).getJid();
  }
  
  public List getTOAddresses()
  {
    return this.extension.getAddressesOfType("to");
  }
  
  public boolean shouldNotReply()
  {
    return !this.extension.getAddressesOfType("noreply").isEmpty();
  }
}


/* Location:           C:\Users\Administrator\Documents\ds\反编译\AndroidKiller_v1.3.1\projects\Jungledsq_ppc\ProjectSrc\smali\
 * Qualified Name:     org.jivesoftware.smackx.MultipleRecipientInfo
 * JD-Core Version:    0.7.0.1
 */