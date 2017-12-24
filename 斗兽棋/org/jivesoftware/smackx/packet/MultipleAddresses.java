package org.jivesoftware.smackx.packet;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.jivesoftware.smack.packet.PacketExtension;

public class MultipleAddresses
  implements PacketExtension
{
  public static final String BCC = "bcc";
  public static final String CC = "cc";
  public static final String NO_REPLY = "noreply";
  public static final String REPLY_ROOM = "replyroom";
  public static final String REPLY_TO = "replyto";
  public static final String TO = "to";
  private List addresses = new ArrayList();
  
  public void addAddress(String paramString1, String paramString2, String paramString3, String paramString4, boolean paramBoolean, String paramString5)
  {
    paramString1 = new Address(paramString1, null);
    paramString1.setJid(paramString2);
    paramString1.setNode(paramString3);
    paramString1.setDescription(paramString4);
    paramString1.setDelivered(paramBoolean);
    paramString1.setUri(paramString5);
    this.addresses.add(paramString1);
  }
  
  public List getAddressesOfType(String paramString)
  {
    ArrayList localArrayList = new ArrayList(this.addresses.size());
    Iterator localIterator = this.addresses.iterator();
    while (localIterator.hasNext())
    {
      Address localAddress = (Address)localIterator.next();
      if (localAddress.getType().equals(paramString)) {
        localArrayList.add(localAddress);
      }
    }
    return localArrayList;
  }
  
  public String getElementName()
  {
    return "addresses";
  }
  
  public String getNamespace()
  {
    return "http://jabber.org/protocol/address";
  }
  
  public void setNoReply()
  {
    Address localAddress = new Address("noreply", null);
    this.addresses.add(localAddress);
  }
  
  public String toXML()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("<").append(getElementName());
    localStringBuilder.append(" xmlns=\"").append(getNamespace()).append("\">");
    Iterator localIterator = this.addresses.iterator();
    while (localIterator.hasNext()) {
      localStringBuilder.append(((Address)localIterator.next()).toXML());
    }
    localStringBuilder.append("</").append(getElementName()).append(">");
    return localStringBuilder.toString();
  }
  
  public static class Address
  {
    private boolean delivered;
    private String description;
    private String jid;
    private String node;
    private String type;
    private String uri;
    
    private Address(String paramString)
    {
      this.type = paramString;
    }
    
    private void setDelivered(boolean paramBoolean)
    {
      this.delivered = paramBoolean;
    }
    
    private void setDescription(String paramString)
    {
      this.description = paramString;
    }
    
    private void setJid(String paramString)
    {
      this.jid = paramString;
    }
    
    private void setNode(String paramString)
    {
      this.node = paramString;
    }
    
    private void setUri(String paramString)
    {
      this.uri = paramString;
    }
    
    private String toXML()
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("<address type=\"");
      localStringBuilder.append(this.type).append("\"");
      if (this.jid != null)
      {
        localStringBuilder.append(" jid=\"");
        localStringBuilder.append(this.jid).append("\"");
      }
      if (this.node != null)
      {
        localStringBuilder.append(" node=\"");
        localStringBuilder.append(this.node).append("\"");
      }
      if ((this.description != null) && (this.description.trim().length() > 0))
      {
        localStringBuilder.append(" desc=\"");
        localStringBuilder.append(this.description).append("\"");
      }
      if (this.delivered) {
        localStringBuilder.append(" delivered=\"true\"");
      }
      if (this.uri != null)
      {
        localStringBuilder.append(" uri=\"");
        localStringBuilder.append(this.uri).append("\"");
      }
      localStringBuilder.append("/>");
      return localStringBuilder.toString();
    }
    
    public String getDescription()
    {
      return this.description;
    }
    
    public String getJid()
    {
      return this.jid;
    }
    
    public String getNode()
    {
      return this.node;
    }
    
    public String getType()
    {
      return this.type;
    }
    
    public String getUri()
    {
      return this.uri;
    }
    
    public boolean isDelivered()
    {
      return this.delivered;
    }
  }
}


/* Location:           C:\Users\Administrator\Documents\ds\反编译\AndroidKiller_v1.3.1\projects\Jungledsq_ppc\ProjectSrc\smali\
 * Qualified Name:     org.jivesoftware.smackx.packet.MultipleAddresses
 * JD-Core Version:    0.7.0.1
 */