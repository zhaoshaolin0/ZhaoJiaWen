package org.jivesoftware.smack.packet;

import org.jivesoftware.smack.util.StringUtils;

public abstract class IQ
  extends Packet
{
  private Type type = Type.GET;
  
  public abstract String getChildElementXML();
  
  public Type getType()
  {
    return this.type;
  }
  
  public void setType(Type paramType)
  {
    if (paramType == null)
    {
      this.type = Type.GET;
      return;
    }
    this.type = paramType;
  }
  
  public String toXML()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("<iq ");
    if (getPacketID() != null) {
      localStringBuilder.append("id=\"" + getPacketID() + "\" ");
    }
    if (getTo() != null) {
      localStringBuilder.append("to=\"").append(StringUtils.escapeForXML(getTo())).append("\" ");
    }
    if (getFrom() != null) {
      localStringBuilder.append("from=\"").append(StringUtils.escapeForXML(getFrom())).append("\" ");
    }
    if (this.type == null) {
      localStringBuilder.append("type=\"get\">");
    }
    for (;;)
    {
      Object localObject = getChildElementXML();
      if (localObject != null) {
        localStringBuilder.append((String)localObject);
      }
      localObject = getError();
      if (localObject != null) {
        localStringBuilder.append(((XMPPError)localObject).toXML());
      }
      localStringBuilder.append("</iq>");
      return localStringBuilder.toString();
      localStringBuilder.append("type=\"").append(getType()).append("\">");
    }
  }
  
  public static class Type
  {
    public static final Type ERROR = new Type("error");
    public static final Type GET = new Type("get");
    public static final Type RESULT;
    public static final Type SET = new Type("set");
    private String value;
    
    static
    {
      RESULT = new Type("result");
    }
    
    private Type(String paramString)
    {
      this.value = paramString;
    }
    
    public static Type fromString(String paramString)
    {
      if (paramString == null) {
        return null;
      }
      paramString = paramString.toLowerCase();
      if (GET.toString().equals(paramString)) {
        return GET;
      }
      if (SET.toString().equals(paramString)) {
        return SET;
      }
      if (ERROR.toString().equals(paramString)) {
        return ERROR;
      }
      if (RESULT.toString().equals(paramString)) {
        return RESULT;
      }
      return null;
    }
    
    public String toString()
    {
      return this.value;
    }
  }
}


/* Location:           C:\Users\Administrator\Documents\ds\反编译\AndroidKiller_v1.3.1\projects\Jungledsq_ppc\ProjectSrc\smali\
 * Qualified Name:     org.jivesoftware.smack.packet.IQ
 * JD-Core Version:    0.7.0.1
 */