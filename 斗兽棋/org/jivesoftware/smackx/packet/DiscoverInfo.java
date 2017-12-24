package org.jivesoftware.smackx.packet;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import org.jivesoftware.smack.packet.IQ;

public class DiscoverInfo
  extends IQ
{
  private final List<Feature> features = new CopyOnWriteArrayList();
  private final List<Identity> identities = new CopyOnWriteArrayList();
  private String node;
  
  private void addFeature(Feature paramFeature)
  {
    synchronized (this.features)
    {
      this.features.add(paramFeature);
      return;
    }
  }
  
  public void addFeature(String paramString)
  {
    addFeature(new Feature(paramString));
  }
  
  public void addIdentity(Identity paramIdentity)
  {
    synchronized (this.identities)
    {
      this.identities.add(paramIdentity);
      return;
    }
  }
  
  public boolean containsFeature(String paramString)
  {
    Iterator localIterator = getFeatures();
    while (localIterator.hasNext()) {
      if (paramString.equals(((Feature)localIterator.next()).getVar())) {
        return true;
      }
    }
    return false;
  }
  
  public String getChildElementXML()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("<query xmlns=\"http://jabber.org/protocol/disco#info\"");
    if (getNode() != null)
    {
      localStringBuilder.append(" node=\"");
      localStringBuilder.append(getNode());
      localStringBuilder.append("\"");
    }
    localStringBuilder.append(">");
    Iterator localIterator;
    synchronized (this.identities)
    {
      localIterator = this.identities.iterator();
      if (localIterator.hasNext()) {
        localStringBuilder.append(((Identity)localIterator.next()).toXML());
      }
    }
    synchronized (this.features)
    {
      localIterator = this.features.iterator();
      if (localIterator.hasNext()) {
        localObject1.append(((Feature)localIterator.next()).toXML());
      }
    }
    localObject2.append(getExtensionsXML());
    localObject2.append("</query>");
    return localObject2.toString();
  }
  
  Iterator<Feature> getFeatures()
  {
    synchronized (this.features)
    {
      Iterator localIterator = Collections.unmodifiableList(this.features).iterator();
      return localIterator;
    }
  }
  
  public Iterator<Identity> getIdentities()
  {
    synchronized (this.identities)
    {
      Iterator localIterator = Collections.unmodifiableList(this.identities).iterator();
      return localIterator;
    }
  }
  
  public String getNode()
  {
    return this.node;
  }
  
  public void setNode(String paramString)
  {
    this.node = paramString;
  }
  
  public static class Feature
  {
    private String variable;
    
    public Feature(String paramString)
    {
      this.variable = paramString;
    }
    
    public String getVar()
    {
      return this.variable;
    }
    
    public String toXML()
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("<feature var=\"").append(this.variable).append("\"/>");
      return localStringBuilder.toString();
    }
  }
  
  public static class Identity
  {
    private String category;
    private String name;
    private String type;
    
    public Identity(String paramString1, String paramString2)
    {
      this.category = paramString1;
      this.name = paramString2;
    }
    
    public String getCategory()
    {
      return this.category;
    }
    
    public String getName()
    {
      return this.name;
    }
    
    public String getType()
    {
      return this.type;
    }
    
    public void setType(String paramString)
    {
      this.type = paramString;
    }
    
    public String toXML()
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("<identity category=\"").append(this.category).append("\"");
      localStringBuilder.append(" name=\"").append(this.name).append("\"");
      if (this.type != null) {
        localStringBuilder.append(" type=\"").append(this.type).append("\"");
      }
      localStringBuilder.append("/>");
      return localStringBuilder.toString();
    }
  }
}


/* Location:           C:\Users\Administrator\Documents\ds\反编译\AndroidKiller_v1.3.1\projects\Jungledsq_ppc\ProjectSrc\smali\
 * Qualified Name:     org.jivesoftware.smackx.packet.DiscoverInfo
 * JD-Core Version:    0.7.0.1
 */