package org.jivesoftware.smackx.packet;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import org.jivesoftware.smack.packet.PacketExtension;
import org.jivesoftware.smackx.FormField;

public class DataForm
  implements PacketExtension
{
  private final List<FormField> fields = new ArrayList();
  private List<String> instructions = new ArrayList();
  private final List<Item> items = new ArrayList();
  private ReportedData reportedData;
  private String title;
  private String type;
  
  public DataForm(String paramString)
  {
    this.type = paramString;
  }
  
  public void addField(FormField paramFormField)
  {
    synchronized (this.fields)
    {
      this.fields.add(paramFormField);
      return;
    }
  }
  
  public void addInstruction(String paramString)
  {
    synchronized (this.instructions)
    {
      this.instructions.add(paramString);
      return;
    }
  }
  
  public void addItem(Item paramItem)
  {
    synchronized (this.items)
    {
      this.items.add(paramItem);
      return;
    }
  }
  
  public String getElementName()
  {
    return "x";
  }
  
  public Iterator<FormField> getFields()
  {
    synchronized (this.fields)
    {
      Iterator localIterator = Collections.unmodifiableList(new ArrayList(this.fields)).iterator();
      return localIterator;
    }
  }
  
  public Iterator<String> getInstructions()
  {
    synchronized (this.instructions)
    {
      Iterator localIterator = Collections.unmodifiableList(new ArrayList(this.instructions)).iterator();
      return localIterator;
    }
  }
  
  public Iterator<Item> getItems()
  {
    synchronized (this.items)
    {
      Iterator localIterator = Collections.unmodifiableList(new ArrayList(this.items)).iterator();
      return localIterator;
    }
  }
  
  public String getNamespace()
  {
    return "jabber:x:data";
  }
  
  public ReportedData getReportedData()
  {
    return this.reportedData;
  }
  
  public String getTitle()
  {
    return this.title;
  }
  
  public String getType()
  {
    return this.type;
  }
  
  public void setInstructions(List<String> paramList)
  {
    this.instructions = paramList;
  }
  
  public void setReportedData(ReportedData paramReportedData)
  {
    this.reportedData = paramReportedData;
  }
  
  public void setTitle(String paramString)
  {
    this.title = paramString;
  }
  
  public String toXML()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("<").append(getElementName()).append(" xmlns=\"").append(getNamespace()).append("\" type=\"" + getType() + "\">");
    if (getTitle() != null) {
      localStringBuilder.append("<title>").append(getTitle()).append("</title>");
    }
    Iterator localIterator = getInstructions();
    while (localIterator.hasNext()) {
      localStringBuilder.append("<instructions>").append(localIterator.next()).append("</instructions>");
    }
    if (getReportedData() != null) {
      localStringBuilder.append(getReportedData().toXML());
    }
    localIterator = getItems();
    while (localIterator.hasNext()) {
      localStringBuilder.append(((Item)localIterator.next()).toXML());
    }
    localIterator = getFields();
    while (localIterator.hasNext()) {
      localStringBuilder.append(((FormField)localIterator.next()).toXML());
    }
    localStringBuilder.append("</").append(getElementName()).append(">");
    return localStringBuilder.toString();
  }
  
  public static class Item
  {
    private List<FormField> fields = new ArrayList();
    
    public Item(List<FormField> paramList)
    {
      this.fields = paramList;
    }
    
    public Iterator<FormField> getFields()
    {
      return Collections.unmodifiableList(new ArrayList(this.fields)).iterator();
    }
    
    public String toXML()
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("<item>");
      Iterator localIterator = getFields();
      while (localIterator.hasNext()) {
        localStringBuilder.append(((FormField)localIterator.next()).toXML());
      }
      localStringBuilder.append("</item>");
      return localStringBuilder.toString();
    }
  }
  
  public static class ReportedData
  {
    private List<FormField> fields = new ArrayList();
    
    public ReportedData(List<FormField> paramList)
    {
      this.fields = paramList;
    }
    
    public Iterator<FormField> getFields()
    {
      return Collections.unmodifiableList(new ArrayList(this.fields)).iterator();
    }
    
    public String toXML()
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("<reported>");
      Iterator localIterator = getFields();
      while (localIterator.hasNext()) {
        localStringBuilder.append(((FormField)localIterator.next()).toXML());
      }
      localStringBuilder.append("</reported>");
      return localStringBuilder.toString();
    }
  }
}


/* Location:           C:\Users\Administrator\Documents\ds\反编译\AndroidKiller_v1.3.1\projects\Jungledsq_ppc\ProjectSrc\smali\
 * Qualified Name:     org.jivesoftware.smackx.packet.DataForm
 * JD-Core Version:    0.7.0.1
 */