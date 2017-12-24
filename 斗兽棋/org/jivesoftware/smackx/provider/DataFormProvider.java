package org.jivesoftware.smackx.provider;

import java.util.ArrayList;
import java.util.List;
import org.jivesoftware.smack.packet.PacketExtension;
import org.jivesoftware.smack.provider.PacketExtensionProvider;
import org.jivesoftware.smackx.FormField;
import org.jivesoftware.smackx.FormField.Option;
import org.jivesoftware.smackx.packet.DataForm;
import org.jivesoftware.smackx.packet.DataForm.Item;
import org.jivesoftware.smackx.packet.DataForm.ReportedData;
import org.xmlpull.v1.XmlPullParser;

public class DataFormProvider
  implements PacketExtensionProvider
{
  private FormField parseField(XmlPullParser paramXmlPullParser)
    throws Exception
  {
    int i = 0;
    FormField localFormField = new FormField(paramXmlPullParser.getAttributeValue("", "var"));
    localFormField.setLabel(paramXmlPullParser.getAttributeValue("", "label"));
    localFormField.setType(paramXmlPullParser.getAttributeValue("", "type"));
    while (i == 0)
    {
      int j = paramXmlPullParser.next();
      if (j == 2)
      {
        if (paramXmlPullParser.getName().equals("desc")) {
          localFormField.setDescription(paramXmlPullParser.nextText());
        } else if (paramXmlPullParser.getName().equals("value")) {
          localFormField.addValue(paramXmlPullParser.nextText());
        } else if (paramXmlPullParser.getName().equals("required")) {
          localFormField.setRequired(true);
        } else if (paramXmlPullParser.getName().equals("option")) {
          localFormField.addOption(parseOption(paramXmlPullParser));
        }
      }
      else if ((j == 3) && (paramXmlPullParser.getName().equals("field"))) {
        i = 1;
      }
    }
    return localFormField;
  }
  
  private DataForm.Item parseItem(XmlPullParser paramXmlPullParser)
    throws Exception
  {
    int i = 0;
    ArrayList localArrayList = new ArrayList();
    while (i == 0)
    {
      int j = paramXmlPullParser.next();
      if (j == 2)
      {
        if (paramXmlPullParser.getName().equals("field")) {
          localArrayList.add(parseField(paramXmlPullParser));
        }
      }
      else if ((j == 3) && (paramXmlPullParser.getName().equals("item"))) {
        i = 1;
      }
    }
    return new DataForm.Item(localArrayList);
  }
  
  private FormField.Option parseOption(XmlPullParser paramXmlPullParser)
    throws Exception
  {
    int i = 0;
    FormField.Option localOption = null;
    String str = paramXmlPullParser.getAttributeValue("", "label");
    while (i == 0)
    {
      int j = paramXmlPullParser.next();
      if (j == 2)
      {
        if (paramXmlPullParser.getName().equals("value")) {
          localOption = new FormField.Option(str, paramXmlPullParser.nextText());
        }
      }
      else if ((j == 3) && (paramXmlPullParser.getName().equals("option"))) {
        i = 1;
      }
    }
    return localOption;
  }
  
  private DataForm.ReportedData parseReported(XmlPullParser paramXmlPullParser)
    throws Exception
  {
    int i = 0;
    ArrayList localArrayList = new ArrayList();
    while (i == 0)
    {
      int j = paramXmlPullParser.next();
      if (j == 2)
      {
        if (paramXmlPullParser.getName().equals("field")) {
          localArrayList.add(parseField(paramXmlPullParser));
        }
      }
      else if ((j == 3) && (paramXmlPullParser.getName().equals("reported"))) {
        i = 1;
      }
    }
    return new DataForm.ReportedData(localArrayList);
  }
  
  public PacketExtension parseExtension(XmlPullParser paramXmlPullParser)
    throws Exception
  {
    int i = 0;
    DataForm localDataForm = new DataForm(paramXmlPullParser.getAttributeValue("", "type"));
    while (i == 0)
    {
      int j = paramXmlPullParser.next();
      if (j == 2)
      {
        if (paramXmlPullParser.getName().equals("instructions")) {
          localDataForm.addInstruction(paramXmlPullParser.nextText());
        } else if (paramXmlPullParser.getName().equals("title")) {
          localDataForm.setTitle(paramXmlPullParser.nextText());
        } else if (paramXmlPullParser.getName().equals("field")) {
          localDataForm.addField(parseField(paramXmlPullParser));
        } else if (paramXmlPullParser.getName().equals("item")) {
          localDataForm.addItem(parseItem(paramXmlPullParser));
        } else if (paramXmlPullParser.getName().equals("reported")) {
          localDataForm.setReportedData(parseReported(paramXmlPullParser));
        }
      }
      else if ((j == 3) && (paramXmlPullParser.getName().equals(localDataForm.getElementName()))) {
        i = 1;
      }
    }
    return localDataForm;
  }
}


/* Location:           C:\Users\Administrator\Documents\ds\反编译\AndroidKiller_v1.3.1\projects\Jungledsq_ppc\ProjectSrc\smali\
 * Qualified Name:     org.jivesoftware.smackx.provider.DataFormProvider
 * JD-Core Version:    0.7.0.1
 */