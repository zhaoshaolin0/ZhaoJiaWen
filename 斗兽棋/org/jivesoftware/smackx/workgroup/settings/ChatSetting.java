package org.jivesoftware.smackx.workgroup.settings;

public class ChatSetting
{
  private String key;
  private int type;
  private String value;
  
  public ChatSetting(String paramString1, String paramString2, int paramInt)
  {
    setKey(paramString1);
    setValue(paramString2);
    setType(paramInt);
  }
  
  public String getKey()
  {
    return this.key;
  }
  
  public int getType()
  {
    return this.type;
  }
  
  public String getValue()
  {
    return this.value;
  }
  
  public void setKey(String paramString)
  {
    this.key = paramString;
  }
  
  public void setType(int paramInt)
  {
    this.type = paramInt;
  }
  
  public void setValue(String paramString)
  {
    this.value = paramString;
  }
}


/* Location:           C:\Users\Administrator\Documents\ds\反编译\AndroidKiller_v1.3.1\projects\Jungledsq_ppc\ProjectSrc\smali\
 * Qualified Name:     org.jivesoftware.smackx.workgroup.settings.ChatSetting
 * JD-Core Version:    0.7.0.1
 */