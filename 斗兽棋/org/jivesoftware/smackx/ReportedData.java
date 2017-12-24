package org.jivesoftware.smackx;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smackx.packet.DataForm;
import org.jivesoftware.smackx.packet.DataForm.Item;
import org.jivesoftware.smackx.packet.DataForm.ReportedData;

public class ReportedData
{
  private List<Column> columns = new ArrayList();
  private List<Row> rows = new ArrayList();
  private String title = "";
  
  public ReportedData() {}
  
  private ReportedData(DataForm paramDataForm)
  {
    Iterator localIterator1 = paramDataForm.getReportedData().getFields();
    Object localObject1;
    while (localIterator1.hasNext())
    {
      localObject1 = (FormField)localIterator1.next();
      this.columns.add(new Column(((FormField)localObject1).getLabel(), ((FormField)localObject1).getVariable(), ((FormField)localObject1).getType()));
    }
    localIterator1 = paramDataForm.getItems();
    while (localIterator1.hasNext())
    {
      Object localObject2 = (DataForm.Item)localIterator1.next();
      localObject1 = new ArrayList(this.columns.size());
      localObject2 = ((DataForm.Item)localObject2).getFields();
      while (((Iterator)localObject2).hasNext())
      {
        FormField localFormField = (FormField)((Iterator)localObject2).next();
        ArrayList localArrayList = new ArrayList();
        Iterator localIterator2 = localFormField.getValues();
        while (localIterator2.hasNext()) {
          localArrayList.add(localIterator2.next());
        }
        ((List)localObject1).add(new Field(localFormField.getVariable(), localArrayList));
      }
      this.rows.add(new Row((List)localObject1));
    }
    this.title = paramDataForm.getTitle();
  }
  
  public static ReportedData getReportedDataFrom(Packet paramPacket)
  {
    paramPacket = paramPacket.getExtension("x", "jabber:x:data");
    if (paramPacket != null)
    {
      paramPacket = (DataForm)paramPacket;
      if (paramPacket.getReportedData() != null) {
        return new ReportedData(paramPacket);
      }
    }
    return null;
  }
  
  public void addColumn(Column paramColumn)
  {
    this.columns.add(paramColumn);
  }
  
  public void addRow(Row paramRow)
  {
    this.rows.add(paramRow);
  }
  
  public Iterator<Column> getColumns()
  {
    return Collections.unmodifiableList(new ArrayList(this.columns)).iterator();
  }
  
  public Iterator<Row> getRows()
  {
    return Collections.unmodifiableList(new ArrayList(this.rows)).iterator();
  }
  
  public String getTitle()
  {
    return this.title;
  }
  
  public static class Column
  {
    private String label;
    private String type;
    private String variable;
    
    public Column(String paramString1, String paramString2, String paramString3)
    {
      this.label = paramString1;
      this.variable = paramString2;
      this.type = paramString3;
    }
    
    public String getLabel()
    {
      return this.label;
    }
    
    public String getType()
    {
      return this.type;
    }
    
    public String getVariable()
    {
      return this.variable;
    }
  }
  
  public static class Field
  {
    private List<String> values;
    private String variable;
    
    public Field(String paramString, List<String> paramList)
    {
      this.variable = paramString;
      this.values = paramList;
    }
    
    public Iterator<String> getValues()
    {
      return Collections.unmodifiableList(this.values).iterator();
    }
    
    public String getVariable()
    {
      return this.variable;
    }
  }
  
  public static class Row
  {
    private List<ReportedData.Field> fields = new ArrayList();
    
    public Row(List<ReportedData.Field> paramList)
    {
      this.fields = paramList;
    }
    
    private Iterator<ReportedData.Field> getFields()
    {
      return Collections.unmodifiableList(new ArrayList(this.fields)).iterator();
    }
    
    public Iterator getValues(String paramString)
    {
      Iterator localIterator = getFields();
      while (localIterator.hasNext())
      {
        ReportedData.Field localField = (ReportedData.Field)localIterator.next();
        if (paramString.equalsIgnoreCase(localField.getVariable())) {
          return localField.getValues();
        }
      }
      return null;
    }
  }
}


/* Location:           C:\Users\Administrator\Documents\ds\反编译\AndroidKiller_v1.3.1\projects\Jungledsq_ppc\ProjectSrc\smali\
 * Qualified Name:     org.jivesoftware.smackx.ReportedData
 * JD-Core Version:    0.7.0.1
 */