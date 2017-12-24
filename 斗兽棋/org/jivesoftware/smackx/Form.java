package org.jivesoftware.smackx;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smackx.packet.DataForm;

public class Form
{
  public static final String TYPE_CANCEL = "cancel";
  public static final String TYPE_FORM = "form";
  public static final String TYPE_RESULT = "result";
  public static final String TYPE_SUBMIT = "submit";
  private DataForm dataForm;
  
  public Form(String paramString)
  {
    this.dataForm = new DataForm(paramString);
  }
  
  public Form(DataForm paramDataForm)
  {
    this.dataForm = paramDataForm;
  }
  
  public static Form getFormFrom(Packet paramPacket)
  {
    paramPacket = paramPacket.getExtension("x", "jabber:x:data");
    if (paramPacket != null)
    {
      paramPacket = (DataForm)paramPacket;
      if (paramPacket.getReportedData() == null) {
        return new Form(paramPacket);
      }
    }
    return null;
  }
  
  private boolean isFormType()
  {
    return "form".equals(this.dataForm.getType());
  }
  
  private boolean isSubmitType()
  {
    return "submit".equals(this.dataForm.getType());
  }
  
  private void setAnswer(FormField paramFormField, Object paramObject)
  {
    if (!isSubmitType()) {
      throw new IllegalStateException("Cannot set an answer if the form is not of type \"submit\"");
    }
    paramFormField.resetValues();
    paramFormField.addValue(paramObject.toString());
  }
  
  public void addField(FormField paramFormField)
  {
    this.dataForm.addField(paramFormField);
  }
  
  public Form createAnswerForm()
  {
    if (!isFormType()) {
      throw new IllegalStateException("Only forms of type \"form\" could be answered");
    }
    Form localForm = new Form("submit");
    Iterator localIterator1 = getFields();
    while (localIterator1.hasNext())
    {
      FormField localFormField = (FormField)localIterator1.next();
      if (localFormField.getVariable() != null)
      {
        Object localObject = new FormField(localFormField.getVariable());
        ((FormField)localObject).setType(localFormField.getType());
        localForm.addField((FormField)localObject);
        if ("hidden".equals(localFormField.getType()))
        {
          localObject = new ArrayList();
          Iterator localIterator2 = localFormField.getValues();
          while (localIterator2.hasNext()) {
            ((List)localObject).add(localIterator2.next());
          }
          localForm.setAnswer(localFormField.getVariable(), (List)localObject);
        }
      }
    }
    return localForm;
  }
  
  public DataForm getDataFormToSend()
  {
    if (isSubmitType())
    {
      DataForm localDataForm = new DataForm(getType());
      Iterator localIterator = getFields();
      while (localIterator.hasNext())
      {
        FormField localFormField = (FormField)localIterator.next();
        if (localFormField.getValues().hasNext()) {
          localDataForm.addField(localFormField);
        }
      }
      return localDataForm;
    }
    return this.dataForm;
  }
  
  public FormField getField(String paramString)
  {
    if ((paramString == null) || (paramString.equals(""))) {
      throw new IllegalArgumentException("Variable must not be null or blank.");
    }
    Iterator localIterator = getFields();
    while (localIterator.hasNext())
    {
      FormField localFormField = (FormField)localIterator.next();
      if (paramString.equals(localFormField.getVariable())) {
        return localFormField;
      }
    }
    return null;
  }
  
  public Iterator<FormField> getFields()
  {
    return this.dataForm.getFields();
  }
  
  public String getInstructions()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    Iterator localIterator = this.dataForm.getInstructions();
    while (localIterator.hasNext())
    {
      localStringBuilder.append((String)localIterator.next());
      if (localIterator.hasNext()) {
        localStringBuilder.append("\n");
      }
    }
    return localStringBuilder.toString();
  }
  
  public String getTitle()
  {
    return this.dataForm.getTitle();
  }
  
  public String getType()
  {
    return this.dataForm.getType();
  }
  
  public void setAnswer(String paramString, double paramDouble)
  {
    paramString = getField(paramString);
    if (paramString == null) {
      throw new IllegalArgumentException("Field not found for the specified variable name.");
    }
    if ((!"text-multi".equals(paramString.getType())) && (!"text-private".equals(paramString.getType())) && (!"text-single".equals(paramString.getType()))) {
      throw new IllegalArgumentException("This field is not of type double.");
    }
    setAnswer(paramString, Double.valueOf(paramDouble));
  }
  
  public void setAnswer(String paramString, float paramFloat)
  {
    paramString = getField(paramString);
    if (paramString == null) {
      throw new IllegalArgumentException("Field not found for the specified variable name.");
    }
    if ((!"text-multi".equals(paramString.getType())) && (!"text-private".equals(paramString.getType())) && (!"text-single".equals(paramString.getType()))) {
      throw new IllegalArgumentException("This field is not of type float.");
    }
    setAnswer(paramString, Float.valueOf(paramFloat));
  }
  
  public void setAnswer(String paramString, int paramInt)
  {
    paramString = getField(paramString);
    if (paramString == null) {
      throw new IllegalArgumentException("Field not found for the specified variable name.");
    }
    if ((!"text-multi".equals(paramString.getType())) && (!"text-private".equals(paramString.getType())) && (!"text-single".equals(paramString.getType()))) {
      throw new IllegalArgumentException("This field is not of type int.");
    }
    setAnswer(paramString, Integer.valueOf(paramInt));
  }
  
  public void setAnswer(String paramString, long paramLong)
  {
    paramString = getField(paramString);
    if (paramString == null) {
      throw new IllegalArgumentException("Field not found for the specified variable name.");
    }
    if ((!"text-multi".equals(paramString.getType())) && (!"text-private".equals(paramString.getType())) && (!"text-single".equals(paramString.getType()))) {
      throw new IllegalArgumentException("This field is not of type long.");
    }
    setAnswer(paramString, Long.valueOf(paramLong));
  }
  
  public void setAnswer(String paramString1, String paramString2)
  {
    paramString1 = getField(paramString1);
    if (paramString1 == null) {
      throw new IllegalArgumentException("Field not found for the specified variable name.");
    }
    if ((!"text-multi".equals(paramString1.getType())) && (!"text-private".equals(paramString1.getType())) && (!"text-single".equals(paramString1.getType())) && (!"jid-single".equals(paramString1.getType())) && (!"hidden".equals(paramString1.getType()))) {
      throw new IllegalArgumentException("This field is not of type String.");
    }
    setAnswer(paramString1, paramString2);
  }
  
  public void setAnswer(String paramString, List<String> paramList)
  {
    if (!isSubmitType()) {
      throw new IllegalStateException("Cannot set an answer if the form is not of type \"submit\"");
    }
    paramString = getField(paramString);
    if (paramString != null)
    {
      if ((!"jid-multi".equals(paramString.getType())) && (!"list-multi".equals(paramString.getType())) && (!"list-single".equals(paramString.getType())) && (!"hidden".equals(paramString.getType()))) {
        throw new IllegalArgumentException("This field only accept list of values.");
      }
      paramString.resetValues();
      paramString.addValues(paramList);
      return;
    }
    throw new IllegalArgumentException("Couldn't find a field for the specified variable.");
  }
  
  public void setAnswer(String paramString, boolean paramBoolean)
  {
    FormField localFormField = getField(paramString);
    if (localFormField == null) {
      throw new IllegalArgumentException("Field not found for the specified variable name.");
    }
    if (!"boolean".equals(localFormField.getType())) {
      throw new IllegalArgumentException("This field is not of type boolean.");
    }
    if (paramBoolean) {}
    for (paramString = "1";; paramString = "0")
    {
      setAnswer(localFormField, paramString);
      return;
    }
  }
  
  public void setDefaultAnswer(String paramString)
  {
    if (!isSubmitType()) {
      throw new IllegalStateException("Cannot set an answer if the form is not of type \"submit\"");
    }
    paramString = getField(paramString);
    if (paramString != null)
    {
      paramString.resetValues();
      Iterator localIterator = paramString.getValues();
      while (localIterator.hasNext()) {
        paramString.addValue((String)localIterator.next());
      }
    }
    throw new IllegalArgumentException("Couldn't find a field for the specified variable.");
  }
  
  public void setInstructions(String paramString)
  {
    ArrayList localArrayList = new ArrayList();
    paramString = new StringTokenizer(paramString, "\n");
    while (paramString.hasMoreTokens()) {
      localArrayList.add(paramString.nextToken());
    }
    this.dataForm.setInstructions(localArrayList);
  }
  
  public void setTitle(String paramString)
  {
    this.dataForm.setTitle(paramString);
  }
}


/* Location:           C:\Users\Administrator\Documents\ds\反编译\AndroidKiller_v1.3.1\projects\Jungledsq_ppc\ProjectSrc\smali\
 * Qualified Name:     org.jivesoftware.smackx.Form
 * JD-Core Version:    0.7.0.1
 */