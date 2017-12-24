package org.jivesoftware.smackx.workgroup.ext.macros;

public class Macro
{
  public static final int IMAGE = 2;
  public static final int TEXT = 0;
  public static final int URL = 1;
  private String description;
  private String response;
  private String title;
  private int type;
  
  public String getDescription()
  {
    return this.description;
  }
  
  public String getResponse()
  {
    return this.response;
  }
  
  public String getTitle()
  {
    return this.title;
  }
  
  public int getType()
  {
    return this.type;
  }
  
  public void setDescription(String paramString)
  {
    this.description = paramString;
  }
  
  public void setResponse(String paramString)
  {
    this.response = paramString;
  }
  
  public void setTitle(String paramString)
  {
    this.title = paramString;
  }
  
  public void setType(int paramInt)
  {
    this.type = paramInt;
  }
}


/* Location:           C:\Users\Administrator\Documents\ds\反编译\AndroidKiller_v1.3.1\projects\Jungledsq_ppc\ProjectSrc\smali\
 * Qualified Name:     org.jivesoftware.smackx.workgroup.ext.macros.Macro
 * JD-Core Version:    0.7.0.1
 */