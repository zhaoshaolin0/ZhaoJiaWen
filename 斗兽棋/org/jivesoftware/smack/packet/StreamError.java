package org.jivesoftware.smack.packet;

public class StreamError
{
  private String code;
  
  public StreamError(String paramString)
  {
    this.code = paramString;
  }
  
  public String getCode()
  {
    return this.code;
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("stream:error (").append(this.code).append(")");
    return localStringBuilder.toString();
  }
}


/* Location:           C:\Users\Administrator\Documents\ds\反编译\AndroidKiller_v1.3.1\projects\Jungledsq_ppc\ProjectSrc\smali\
 * Qualified Name:     org.jivesoftware.smack.packet.StreamError
 * JD-Core Version:    0.7.0.1
 */