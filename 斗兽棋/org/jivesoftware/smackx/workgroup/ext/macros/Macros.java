package org.jivesoftware.smackx.workgroup.ext.macros;

import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.provider.IQProvider;
import org.xmlpull.v1.XmlPullParser;

public class Macros
  extends IQ
{
  public static final String ELEMENT_NAME = "macros";
  public static final String NAMESPACE = "http://jivesoftware.com/protocol/workgroup";
  private static ClassLoader cl;
  private boolean personal;
  private MacroGroup personalMacroGroup;
  private MacroGroup rootGroup;
  
  public static void setClassLoader(ClassLoader paramClassLoader)
  {
    cl = paramClassLoader;
  }
  
  public String getChildElementXML()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("<").append("macros").append(" xmlns=\"").append("http://jivesoftware.com/protocol/workgroup").append("\">");
    if (isPersonal()) {
      localStringBuilder.append("<personal>true</personal>");
    }
    localStringBuilder.append("</").append("macros").append("> ");
    return localStringBuilder.toString();
  }
  
  public MacroGroup getPersonalMacroGroup()
  {
    return this.personalMacroGroup;
  }
  
  public MacroGroup getRootGroup()
  {
    return this.rootGroup;
  }
  
  public boolean isPersonal()
  {
    return this.personal;
  }
  
  public void setPersonal(boolean paramBoolean)
  {
    this.personal = paramBoolean;
  }
  
  public void setPersonalMacroGroup(MacroGroup paramMacroGroup)
  {
    this.personalMacroGroup = paramMacroGroup;
  }
  
  public void setRootGroup(MacroGroup paramMacroGroup)
  {
    this.rootGroup = paramMacroGroup;
  }
  
  public static class InternalProvider
    implements IQProvider
  {
    public IQ parseIQ(XmlPullParser paramXmlPullParser)
      throws Exception
    {
      Macros localMacros = new Macros();
      int i = 0;
      while (i == 0)
      {
        int j = paramXmlPullParser.next();
        if (j == 2)
        {
          if (paramXmlPullParser.getName().equals("model")) {
            paramXmlPullParser.nextText();
          }
        }
        else if ((j == 3) && (paramXmlPullParser.getName().equals("macros"))) {
          i = 1;
        }
      }
      return localMacros;
    }
  }
}


/* Location:           C:\Users\Administrator\Documents\ds\反编译\AndroidKiller_v1.3.1\projects\Jungledsq_ppc\ProjectSrc\smali\
 * Qualified Name:     org.jivesoftware.smackx.workgroup.ext.macros.Macros
 * JD-Core Version:    0.7.0.1
 */