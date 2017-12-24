package org.jivesoftware.smackx.search;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smackx.Form;
import org.jivesoftware.smackx.FormField;
import org.jivesoftware.smackx.ReportedData;
import org.jivesoftware.smackx.ReportedData.Column;
import org.jivesoftware.smackx.ReportedData.Field;
import org.jivesoftware.smackx.ReportedData.Row;
import org.xmlpull.v1.XmlPullParser;

class SimpleUserSearch
  extends IQ
{
  private ReportedData data;
  private Form form;
  
  private String getItemsToSearch()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    if (this.form == null) {
      this.form = Form.getFormFrom(this);
    }
    if (this.form == null) {
      return "";
    }
    Iterator localIterator = this.form.getFields();
    while (localIterator.hasNext())
    {
      Object localObject = (FormField)localIterator.next();
      String str = ((FormField)localObject).getVariable();
      localObject = getSingleValue((FormField)localObject);
      if (((String)localObject).trim().length() > 0) {
        localStringBuilder.append("<").append(str).append(">").append((String)localObject).append("</").append(str).append(">");
      }
    }
    return localStringBuilder.toString();
  }
  
  private static String getSingleValue(FormField paramFormField)
  {
    paramFormField = paramFormField.getValues();
    if (paramFormField.hasNext()) {
      return (String)paramFormField.next();
    }
    return "";
  }
  
  public String getChildElementXML()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("<query xmlns=\"jabber:iq:search\">");
    localStringBuilder.append(getItemsToSearch());
    localStringBuilder.append("</query>");
    return localStringBuilder.toString();
  }
  
  public ReportedData getReportedData()
  {
    return this.data;
  }
  
  protected void parseItems(XmlPullParser paramXmlPullParser)
    throws Exception
  {
    ReportedData localReportedData = new ReportedData();
    localReportedData.addColumn(new ReportedData.Column("JID", "jid", "text-single"));
    int i = 0;
    ArrayList localArrayList1 = new ArrayList();
    while (i == 0)
    {
      String str;
      Object localObject;
      if (paramXmlPullParser.getAttributeCount() > 0)
      {
        str = paramXmlPullParser.getAttributeValue("", "jid");
        localObject = new ArrayList();
        ((List)localObject).add(str);
        localArrayList1.add(new ReportedData.Field("jid", (List)localObject));
      }
      int j = paramXmlPullParser.next();
      if ((j == 2) && (paramXmlPullParser.getName().equals("item")))
      {
        localArrayList1 = new ArrayList();
      }
      else if ((j == 3) && (paramXmlPullParser.getName().equals("item")))
      {
        localReportedData.addRow(new ReportedData.Row(localArrayList1));
      }
      else if (j == 2)
      {
        str = paramXmlPullParser.getName();
        localObject = paramXmlPullParser.nextText();
        ArrayList localArrayList2 = new ArrayList();
        localArrayList2.add(localObject);
        localArrayList1.add(new ReportedData.Field(str, localArrayList2));
        j = 0;
        localObject = localReportedData.getColumns();
        while (((Iterator)localObject).hasNext()) {
          if (((ReportedData.Column)((Iterator)localObject).next()).getVariable().equals(str)) {
            j = 1;
          }
        }
        if (j == 0) {
          localReportedData.addColumn(new ReportedData.Column(str, str, "text-single"));
        }
      }
      else if ((j == 3) && (paramXmlPullParser.getName().equals("query")))
      {
        i = 1;
      }
    }
    this.data = localReportedData;
  }
  
  public void setForm(Form paramForm)
  {
    this.form = paramForm;
  }
}


/* Location:           C:\Users\Administrator\Documents\ds\反编译\AndroidKiller_v1.3.1\projects\Jungledsq_ppc\ProjectSrc\smali\
 * Qualified Name:     org.jivesoftware.smackx.search.SimpleUserSearch
 * JD-Core Version:    0.7.0.1
 */