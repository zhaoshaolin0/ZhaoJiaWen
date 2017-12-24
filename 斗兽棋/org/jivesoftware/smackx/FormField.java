package org.jivesoftware.smackx;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import org.jivesoftware.smack.util.StringUtils;

public class FormField
{
  public static final String TYPE_BOOLEAN = "boolean";
  public static final String TYPE_FIXED = "fixed";
  public static final String TYPE_HIDDEN = "hidden";
  public static final String TYPE_JID_MULTI = "jid-multi";
  public static final String TYPE_JID_SINGLE = "jid-single";
  public static final String TYPE_LIST_MULTI = "list-multi";
  public static final String TYPE_LIST_SINGLE = "list-single";
  public static final String TYPE_TEXT_MULTI = "text-multi";
  public static final String TYPE_TEXT_PRIVATE = "text-private";
  public static final String TYPE_TEXT_SINGLE = "text-single";
  private String description;
  private String label;
  private final List<Option> options = new ArrayList();
  private boolean required = false;
  private String type;
  private final List<String> values = new ArrayList();
  private String variable;
  
  public FormField()
  {
    this.type = "fixed";
  }
  
  public FormField(String paramString)
  {
    this.variable = paramString;
  }
  
  public void addOption(Option paramOption)
  {
    synchronized (this.options)
    {
      this.options.add(paramOption);
      return;
    }
  }
  
  public void addValue(String paramString)
  {
    synchronized (this.values)
    {
      this.values.add(paramString);
      return;
    }
  }
  
  public void addValues(List<String> paramList)
  {
    synchronized (this.values)
    {
      this.values.addAll(paramList);
      return;
    }
  }
  
  public String getDescription()
  {
    return this.description;
  }
  
  public String getLabel()
  {
    return this.label;
  }
  
  public Iterator<Option> getOptions()
  {
    synchronized (this.options)
    {
      Iterator localIterator = Collections.unmodifiableList(new ArrayList(this.options)).iterator();
      return localIterator;
    }
  }
  
  public String getType()
  {
    return this.type;
  }
  
  public Iterator<String> getValues()
  {
    synchronized (this.values)
    {
      Iterator localIterator = Collections.unmodifiableList(new ArrayList(this.values)).iterator();
      return localIterator;
    }
  }
  
  public String getVariable()
  {
    return this.variable;
  }
  
  public boolean isRequired()
  {
    return this.required;
  }
  
  protected void resetValues()
  {
    synchronized (this.values)
    {
      this.values.removeAll(new ArrayList(this.values));
      return;
    }
  }
  
  public void setDescription(String paramString)
  {
    this.description = paramString;
  }
  
  public void setLabel(String paramString)
  {
    this.label = paramString;
  }
  
  public void setRequired(boolean paramBoolean)
  {
    this.required = paramBoolean;
  }
  
  public void setType(String paramString)
  {
    this.type = paramString;
  }
  
  public String toXML()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("<field");
    if (getLabel() != null) {
      localStringBuilder.append(" label=\"").append(getLabel()).append("\"");
    }
    if (getVariable() != null) {
      localStringBuilder.append(" var=\"").append(getVariable()).append("\"");
    }
    if (getType() != null) {
      localStringBuilder.append(" type=\"").append(getType()).append("\"");
    }
    localStringBuilder.append(">");
    if (getDescription() != null) {
      localStringBuilder.append("<desc>").append(getDescription()).append("</desc>");
    }
    if (isRequired()) {
      localStringBuilder.append("<required/>");
    }
    Iterator localIterator = getValues();
    while (localIterator.hasNext()) {
      localStringBuilder.append("<value>").append((String)localIterator.next()).append("</value>");
    }
    localIterator = getOptions();
    while (localIterator.hasNext()) {
      localStringBuilder.append(((Option)localIterator.next()).toXML());
    }
    localStringBuilder.append("</field>");
    return localStringBuilder.toString();
  }
  
  public static class Option
  {
    private String label;
    private String value;
    
    public Option(String paramString)
    {
      this.value = paramString;
    }
    
    public Option(String paramString1, String paramString2)
    {
      this.label = paramString1;
      this.value = paramString2;
    }
    
    public String getLabel()
    {
      return this.label;
    }
    
    public String getValue()
    {
      return this.value;
    }
    
    public String toString()
    {
      return getLabel();
    }
    
    public String toXML()
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("<option");
      if (getLabel() != null) {
        localStringBuilder.append(" label=\"").append(getLabel()).append("\"");
      }
      localStringBuilder.append(">");
      localStringBuilder.append("<value>").append(StringUtils.escapeForXML(getValue())).append("</value>");
      localStringBuilder.append("</option>");
      return localStringBuilder.toString();
    }
  }
}


/* Location:           C:\Users\Administrator\Documents\ds\反编译\AndroidKiller_v1.3.1\projects\Jungledsq_ppc\ProjectSrc\smali\
 * Qualified Name:     org.jivesoftware.smackx.FormField
 * JD-Core Version:    0.7.0.1
 */