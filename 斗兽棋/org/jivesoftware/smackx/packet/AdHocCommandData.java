package org.jivesoftware.smackx.packet;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.packet.IQ.Type;
import org.jivesoftware.smack.packet.PacketExtension;
import org.jivesoftware.smackx.commands.AdHocCommand.Action;
import org.jivesoftware.smackx.commands.AdHocCommand.SpecificErrorCondition;
import org.jivesoftware.smackx.commands.AdHocCommand.Status;
import org.jivesoftware.smackx.commands.AdHocCommandNote;
import org.jivesoftware.smackx.commands.AdHocCommandNote.Type;

public class AdHocCommandData
  extends IQ
{
  private AdHocCommand.Action action;
  private ArrayList<AdHocCommand.Action> actions = new ArrayList();
  private AdHocCommand.Action executeAction;
  private DataForm form;
  private String id;
  private String lang;
  private String name;
  private String node;
  private List<AdHocCommandNote> notes = new ArrayList();
  private String sessionID;
  private AdHocCommand.Status status;
  
  public void addAction(AdHocCommand.Action paramAction)
  {
    this.actions.add(paramAction);
  }
  
  public void addNote(AdHocCommandNote paramAdHocCommandNote)
  {
    this.notes.add(paramAdHocCommandNote);
  }
  
  public AdHocCommand.Action getAction()
  {
    return this.action;
  }
  
  public List<AdHocCommand.Action> getActions()
  {
    return this.actions;
  }
  
  public String getChildElementXML()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("<command xmlns=\"http://jabber.org/protocol/commands\"");
    localStringBuilder.append(" node=\"").append(this.node).append("\"");
    if ((this.sessionID != null) && (!this.sessionID.equals(""))) {
      localStringBuilder.append(" sessionid=\"").append(this.sessionID).append("\"");
    }
    if (this.status != null) {
      localStringBuilder.append(" status=\"").append(this.status).append("\"");
    }
    if (this.action != null) {
      localStringBuilder.append(" action=\"").append(this.action).append("\"");
    }
    if ((this.lang != null) && (!this.lang.equals(""))) {
      localStringBuilder.append(" lang=\"").append(this.lang).append("\"");
    }
    localStringBuilder.append(">");
    if (getType() == IQ.Type.RESULT)
    {
      localStringBuilder.append("<actions");
      if (this.executeAction != null) {
        localStringBuilder.append(" execute=\"").append(this.executeAction).append("\"");
      }
      if (this.actions.size() != 0) {
        break label318;
      }
      localStringBuilder.append("/>");
    }
    for (;;)
    {
      if (this.form != null) {
        localStringBuilder.append(this.form.toXML());
      }
      Iterator localIterator = this.notes.iterator();
      Object localObject;
      while (localIterator.hasNext())
      {
        localObject = (AdHocCommandNote)localIterator.next();
        localStringBuilder.append("<note type=\"").append(((AdHocCommandNote)localObject).getType().toString()).append("\">");
        localStringBuilder.append(((AdHocCommandNote)localObject).getValue());
        localStringBuilder.append("</note>");
      }
      label318:
      localStringBuilder.append(">");
      localIterator = this.actions.iterator();
      while (localIterator.hasNext())
      {
        localObject = (AdHocCommand.Action)localIterator.next();
        localStringBuilder.append("<").append(localObject).append("/>");
      }
      localStringBuilder.append("</actions>");
    }
    localStringBuilder.append("</command>");
    return localStringBuilder.toString();
  }
  
  public AdHocCommand.Action getExecuteAction()
  {
    return this.executeAction;
  }
  
  public DataForm getForm()
  {
    return this.form;
  }
  
  public String getId()
  {
    return this.id;
  }
  
  public String getName()
  {
    return this.name;
  }
  
  public String getNode()
  {
    return this.node;
  }
  
  public List<AdHocCommandNote> getNotes()
  {
    return this.notes;
  }
  
  public String getSessionID()
  {
    return this.sessionID;
  }
  
  public AdHocCommand.Status getStatus()
  {
    return this.status;
  }
  
  public void remveNote(AdHocCommandNote paramAdHocCommandNote)
  {
    this.notes.remove(paramAdHocCommandNote);
  }
  
  public void setAction(AdHocCommand.Action paramAction)
  {
    this.action = paramAction;
  }
  
  public void setExecuteAction(AdHocCommand.Action paramAction)
  {
    this.executeAction = paramAction;
  }
  
  public void setForm(DataForm paramDataForm)
  {
    this.form = paramDataForm;
  }
  
  public void setId(String paramString)
  {
    this.id = paramString;
  }
  
  public void setName(String paramString)
  {
    this.name = paramString;
  }
  
  public void setNode(String paramString)
  {
    this.node = paramString;
  }
  
  public void setSessionID(String paramString)
  {
    this.sessionID = paramString;
  }
  
  public void setStatus(AdHocCommand.Status paramStatus)
  {
    this.status = paramStatus;
  }
  
  public static class SpecificError
    implements PacketExtension
  {
    public static final String namespace = "http://jabber.org/protocol/commands";
    public AdHocCommand.SpecificErrorCondition condition;
    
    public SpecificError(AdHocCommand.SpecificErrorCondition paramSpecificErrorCondition)
    {
      this.condition = paramSpecificErrorCondition;
    }
    
    public AdHocCommand.SpecificErrorCondition getCondition()
    {
      return this.condition;
    }
    
    public String getElementName()
    {
      return this.condition.toString();
    }
    
    public String getNamespace()
    {
      return "http://jabber.org/protocol/commands";
    }
    
    public String toXML()
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("<").append(getElementName());
      localStringBuilder.append(" xmlns=\"").append(getNamespace()).append("\"/>");
      return localStringBuilder.toString();
    }
  }
}


/* Location:           C:\Users\Administrator\Documents\ds\反编译\AndroidKiller_v1.3.1\projects\Jungledsq_ppc\ProjectSrc\smali\
 * Qualified Name:     org.jivesoftware.smackx.packet.AdHocCommandData
 * JD-Core Version:    0.7.0.1
 */