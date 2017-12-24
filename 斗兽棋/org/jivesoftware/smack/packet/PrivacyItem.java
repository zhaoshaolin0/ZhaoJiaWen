package org.jivesoftware.smack.packet;

public class PrivacyItem
{
  private boolean allow;
  private boolean filterIQ = false;
  private boolean filterMessage = false;
  private boolean filterPresence_in = false;
  private boolean filterPresence_out = false;
  private int order;
  private PrivacyRule rule;
  
  public PrivacyItem(String paramString, boolean paramBoolean, int paramInt)
  {
    setRule(PrivacyRule.fromString(paramString));
    setAllow(paramBoolean);
    setOrder(paramInt);
  }
  
  private PrivacyRule getRule()
  {
    return this.rule;
  }
  
  private void setAllow(boolean paramBoolean)
  {
    this.allow = paramBoolean;
  }
  
  private void setOrder(int paramInt)
  {
    this.order = paramInt;
  }
  
  private void setRule(PrivacyRule paramPrivacyRule)
  {
    this.rule = paramPrivacyRule;
  }
  
  public int getOrder()
  {
    return this.order;
  }
  
  public Type getType()
  {
    if (getRule() == null) {
      return null;
    }
    return getRule().getType();
  }
  
  public String getValue()
  {
    if (getRule() == null) {
      return null;
    }
    return getRule().getValue();
  }
  
  public boolean isAllow()
  {
    return this.allow;
  }
  
  public boolean isFilterEverything()
  {
    return (!isFilterIQ()) && (!isFilterMessage()) && (!isFilterPresence_in()) && (!isFilterPresence_out());
  }
  
  public boolean isFilterIQ()
  {
    return this.filterIQ;
  }
  
  public boolean isFilterMessage()
  {
    return this.filterMessage;
  }
  
  public boolean isFilterPresence_in()
  {
    return this.filterPresence_in;
  }
  
  public boolean isFilterPresence_out()
  {
    return this.filterPresence_out;
  }
  
  public void setFilterIQ(boolean paramBoolean)
  {
    this.filterIQ = paramBoolean;
  }
  
  public void setFilterMessage(boolean paramBoolean)
  {
    this.filterMessage = paramBoolean;
  }
  
  public void setFilterPresence_in(boolean paramBoolean)
  {
    this.filterPresence_in = paramBoolean;
  }
  
  public void setFilterPresence_out(boolean paramBoolean)
  {
    this.filterPresence_out = paramBoolean;
  }
  
  public void setValue(String paramString)
  {
    if ((getRule() != null) || (paramString != null)) {
      getRule().setValue(paramString);
    }
  }
  
  public String toXML()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("<item");
    if (isAllow())
    {
      localStringBuilder.append(" action=\"allow\"");
      localStringBuilder.append(" order=\"").append(getOrder()).append("\"");
      if (getType() != null) {
        localStringBuilder.append(" type=\"").append(getType()).append("\"");
      }
      if (getValue() != null) {
        localStringBuilder.append(" value=\"").append(getValue()).append("\"");
      }
      if (!isFilterEverything()) {
        break label129;
      }
      localStringBuilder.append("/>");
    }
    for (;;)
    {
      return localStringBuilder.toString();
      localStringBuilder.append(" action=\"deny\"");
      break;
      label129:
      localStringBuilder.append(">");
      if (isFilterIQ()) {
        localStringBuilder.append("<iq/>");
      }
      if (isFilterMessage()) {
        localStringBuilder.append("<message/>");
      }
      if (isFilterPresence_in()) {
        localStringBuilder.append("<presence-in/>");
      }
      if (isFilterPresence_out()) {
        localStringBuilder.append("<presence-out/>");
      }
      localStringBuilder.append("</item>");
    }
  }
  
  public static class PrivacyRule
  {
    public static final String SUBSCRIPTION_BOTH = "both";
    public static final String SUBSCRIPTION_FROM = "from";
    public static final String SUBSCRIPTION_NONE = "none";
    public static final String SUBSCRIPTION_TO = "to";
    private PrivacyItem.Type type;
    private String value;
    
    protected static PrivacyRule fromString(String paramString)
    {
      if (paramString == null) {
        return null;
      }
      PrivacyRule localPrivacyRule = new PrivacyRule();
      localPrivacyRule.setType(PrivacyItem.Type.valueOf(paramString.toLowerCase()));
      return localPrivacyRule;
    }
    
    private void setSuscriptionValue(String paramString)
    {
      if ((paramString != null) || ("both".equalsIgnoreCase(paramString))) {
        paramString = "both";
      }
      for (;;)
      {
        this.value = paramString;
        return;
        if ("to".equalsIgnoreCase(paramString)) {
          paramString = "to";
        } else if ("from".equalsIgnoreCase(paramString)) {
          paramString = "from";
        } else if ("none".equalsIgnoreCase(paramString)) {
          paramString = "none";
        } else {
          paramString = null;
        }
      }
    }
    
    private void setType(PrivacyItem.Type paramType)
    {
      this.type = paramType;
    }
    
    public PrivacyItem.Type getType()
    {
      return this.type;
    }
    
    public String getValue()
    {
      return this.value;
    }
    
    public boolean isSuscription()
    {
      return getType() == PrivacyItem.Type.subscription;
    }
    
    protected void setValue(String paramString)
    {
      if (isSuscription())
      {
        setSuscriptionValue(paramString);
        return;
      }
      this.value = paramString;
    }
  }
  
  public static enum Type
  {
    group,  jid,  subscription;
    
    private Type() {}
  }
}


/* Location:           C:\Users\Administrator\Documents\ds\反编译\AndroidKiller_v1.3.1\projects\Jungledsq_ppc\ProjectSrc\smali\
 * Qualified Name:     org.jivesoftware.smack.packet.PrivacyItem
 * JD-Core Version:    0.7.0.1
 */