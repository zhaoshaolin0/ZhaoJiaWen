package org.jivesoftware.smackx;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class RemoteRosterEntry
{
  private final List<String> groupNames = new ArrayList();
  private String name;
  private String user;
  
  public RemoteRosterEntry(String paramString1, String paramString2, String[] paramArrayOfString)
  {
    this.user = paramString1;
    this.name = paramString2;
    if (paramArrayOfString != null) {
      this.groupNames.addAll(Arrays.asList(paramArrayOfString));
    }
  }
  
  public String[] getGroupArrayNames()
  {
    synchronized (this.groupNames)
    {
      String[] arrayOfString = (String[])Collections.unmodifiableList(this.groupNames).toArray(new String[this.groupNames.size()]);
      return arrayOfString;
    }
  }
  
  public Iterator getGroupNames()
  {
    synchronized (this.groupNames)
    {
      Iterator localIterator = Collections.unmodifiableList(this.groupNames).iterator();
      return localIterator;
    }
  }
  
  public String getName()
  {
    return this.name;
  }
  
  public String getUser()
  {
    return this.user;
  }
  
  public String toXML()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("<item jid=\"").append(this.user).append("\"");
    if (this.name != null) {
      localStringBuilder.append(" name=\"").append(this.name).append("\"");
    }
    localStringBuilder.append(">");
    synchronized (this.groupNames)
    {
      Iterator localIterator = this.groupNames.iterator();
      if (localIterator.hasNext())
      {
        String str = (String)localIterator.next();
        localStringBuilder.append("<group>").append(str).append("</group>");
      }
    }
    localObject.append("</item>");
    return localObject.toString();
  }
}


/* Location:           C:\Users\Administrator\Documents\ds\反编译\AndroidKiller_v1.3.1\projects\Jungledsq_ppc\ProjectSrc\smali\
 * Qualified Name:     org.jivesoftware.smackx.RemoteRosterEntry
 * JD-Core Version:    0.7.0.1
 */