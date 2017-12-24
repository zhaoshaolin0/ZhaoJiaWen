package org.jivesoftware.smackx.packet;

import java.text.SimpleDateFormat;
import java.util.Date;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.packet.IQ.Type;
import org.jivesoftware.smack.packet.PacketExtension;
import org.jivesoftware.smack.util.StringUtils;

public class StreamInitiation
  extends IQ
{
  private Feature featureNegotiation;
  private File file;
  private String id;
  private String mimeType;
  
  public String getChildElementXML()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    if (getType().equals(IQ.Type.SET))
    {
      localStringBuilder.append("<si xmlns=\"http://jabber.org/protocol/si\" ");
      if (getSessionID() != null) {
        localStringBuilder.append("id=\"").append(getSessionID()).append("\" ");
      }
      if (getMimeType() != null) {
        localStringBuilder.append("mime-type=\"").append(getMimeType()).append("\" ");
      }
      localStringBuilder.append("profile=\"http://jabber.org/protocol/si/profile/file-transfer\">");
      String str = this.file.toXML();
      if (str != null) {
        localStringBuilder.append(str);
      }
    }
    for (;;)
    {
      if (this.featureNegotiation != null) {
        localStringBuilder.append(this.featureNegotiation.toXML());
      }
      localStringBuilder.append("</si>");
      return localStringBuilder.toString();
      if (!getType().equals(IQ.Type.RESULT)) {
        break;
      }
      localStringBuilder.append("<si xmlns=\"http://jabber.org/protocol/si\">");
    }
    throw new IllegalArgumentException("IQ Type not understood");
  }
  
  public DataForm getFeatureNegotiationForm()
  {
    return this.featureNegotiation.getData();
  }
  
  public File getFile()
  {
    return this.file;
  }
  
  public String getMimeType()
  {
    return this.mimeType;
  }
  
  public String getSessionID()
  {
    return this.id;
  }
  
  public void setFeatureNegotiationForm(DataForm paramDataForm)
  {
    this.featureNegotiation = new Feature(paramDataForm);
  }
  
  public void setFile(File paramFile)
  {
    this.file = paramFile;
  }
  
  public void setMimeType(String paramString)
  {
    this.mimeType = paramString;
  }
  
  public void setSesssionID(String paramString)
  {
    this.id = paramString;
  }
  
  public class Feature
    implements PacketExtension
  {
    private final DataForm data;
    
    public Feature(DataForm paramDataForm)
    {
      this.data = paramDataForm;
    }
    
    public DataForm getData()
    {
      return this.data;
    }
    
    public String getElementName()
    {
      return "feature";
    }
    
    public String getNamespace()
    {
      return "http://jabber.org/protocol/feature-neg";
    }
    
    public String toXML()
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("<feature xmlns=\"http://jabber.org/protocol/feature-neg\">");
      localStringBuilder.append(this.data.toXML());
      localStringBuilder.append("</feature>");
      return localStringBuilder.toString();
    }
  }
  
  public static class File
    implements PacketExtension
  {
    private Date date;
    private String desc;
    private String hash;
    private boolean isRanged;
    private final String name;
    private final long size;
    
    public File(String paramString, long paramLong)
    {
      if (paramString == null) {
        throw new NullPointerException("name cannot be null");
      }
      this.name = paramString;
      this.size = paramLong;
    }
    
    public Date getDate()
    {
      return this.date;
    }
    
    public String getDesc()
    {
      return this.desc;
    }
    
    public String getElementName()
    {
      return "file";
    }
    
    public String getHash()
    {
      return this.hash;
    }
    
    public String getName()
    {
      return this.name;
    }
    
    public String getNamespace()
    {
      return "http://jabber.org/protocol/si/profile/file-transfer";
    }
    
    public long getSize()
    {
      return this.size;
    }
    
    public boolean isRanged()
    {
      return this.isRanged;
    }
    
    public void setDate(Date paramDate)
    {
      this.date = paramDate;
    }
    
    public void setDesc(String paramString)
    {
      this.desc = paramString;
    }
    
    public void setHash(String paramString)
    {
      this.hash = paramString;
    }
    
    public void setRanged(boolean paramBoolean)
    {
      this.isRanged = paramBoolean;
    }
    
    public String toXML()
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("<").append(getElementName()).append(" xmlns=\"").append(getNamespace()).append("\" ");
      if (getName() != null) {
        localStringBuilder.append("name=\"").append(StringUtils.escapeForXML(getName())).append("\" ");
      }
      if (getSize() > 0L) {
        localStringBuilder.append("size=\"").append(getSize()).append("\" ");
      }
      if (getDate() != null) {
        localStringBuilder.append("date=\"").append(DelayInformation.UTC_FORMAT.format(this.date)).append("\" ");
      }
      if (getHash() != null) {
        localStringBuilder.append("hash=\"").append(getHash()).append("\" ");
      }
      if (((this.desc != null) && (this.desc.length() > 0)) || (this.isRanged))
      {
        localStringBuilder.append(">");
        if ((getDesc() != null) && (this.desc.length() > 0)) {
          localStringBuilder.append("<desc>").append(StringUtils.escapeForXML(getDesc())).append("</desc>");
        }
        if (isRanged()) {
          localStringBuilder.append("<range/>");
        }
        localStringBuilder.append("</").append(getElementName()).append(">");
      }
      for (;;)
      {
        return localStringBuilder.toString();
        localStringBuilder.append("/>");
      }
    }
  }
}


/* Location:           C:\Users\Administrator\Documents\ds\反编译\AndroidKiller_v1.3.1\projects\Jungledsq_ppc\ProjectSrc\smali\
 * Qualified Name:     org.jivesoftware.smackx.packet.StreamInitiation
 * JD-Core Version:    0.7.0.1
 */