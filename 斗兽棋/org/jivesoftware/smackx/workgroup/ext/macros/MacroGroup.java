package org.jivesoftware.smackx.workgroup.ext.macros;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class MacroGroup
{
  private List macroGroups = new ArrayList();
  private List macros = new ArrayList();
  private String title;
  
  public void addMacro(Macro paramMacro)
  {
    this.macros.add(paramMacro);
  }
  
  public void addMacroGroup(MacroGroup paramMacroGroup)
  {
    this.macroGroups.add(paramMacroGroup);
  }
  
  public Macro getMacro(int paramInt)
  {
    return (Macro)this.macros.get(paramInt);
  }
  
  public Macro getMacroByTitle(String paramString)
  {
    Iterator localIterator = Collections.unmodifiableList(this.macros).iterator();
    while (localIterator.hasNext())
    {
      Macro localMacro = (Macro)localIterator.next();
      if (localMacro.getTitle().equalsIgnoreCase(paramString)) {
        return localMacro;
      }
    }
    return null;
  }
  
  public MacroGroup getMacroGroup(int paramInt)
  {
    return (MacroGroup)this.macroGroups.get(paramInt);
  }
  
  public MacroGroup getMacroGroupByTitle(String paramString)
  {
    Iterator localIterator = Collections.unmodifiableList(this.macroGroups).iterator();
    while (localIterator.hasNext())
    {
      MacroGroup localMacroGroup = (MacroGroup)localIterator.next();
      if (localMacroGroup.getTitle().equalsIgnoreCase(paramString)) {
        return localMacroGroup;
      }
    }
    return null;
  }
  
  public List getMacroGroups()
  {
    return this.macroGroups;
  }
  
  public List getMacros()
  {
    return this.macros;
  }
  
  public String getTitle()
  {
    return this.title;
  }
  
  public void removeMacro(Macro paramMacro)
  {
    this.macros.remove(paramMacro);
  }
  
  public void removeMacroGroup(MacroGroup paramMacroGroup)
  {
    this.macroGroups.remove(paramMacroGroup);
  }
  
  public void setMacroGroups(List paramList)
  {
    this.macroGroups = paramList;
  }
  
  public void setMacros(List paramList)
  {
    this.macros = paramList;
  }
  
  public void setTitle(String paramString)
  {
    this.title = paramString;
  }
}


/* Location:           C:\Users\Administrator\Documents\ds\反编译\AndroidKiller_v1.3.1\projects\Jungledsq_ppc\ProjectSrc\smali\
 * Qualified Name:     org.jivesoftware.smackx.workgroup.ext.macros.MacroGroup
 * JD-Core Version:    0.7.0.1
 */