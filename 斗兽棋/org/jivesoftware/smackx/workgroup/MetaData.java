package org.jivesoftware.smackx.workgroup;

import java.util.Map;
import org.jivesoftware.smack.packet.PacketExtension;
import org.jivesoftware.smackx.workgroup.util.MetaDataUtils;

public class MetaData
  implements PacketExtension
{
  public static final String ELEMENT_NAME = "metadata";
  public static final String NAMESPACE = "http://jivesoftware.com/protocol/workgroup";
  private Map metaData;
  
  public MetaData(Map paramMap)
  {
    this.metaData = paramMap;
  }
  
  public String getElementName()
  {
    return "metadata";
  }
  
  public Map getMetaData()
  {
    return this.metaData;
  }
  
  public String getNamespace()
  {
    return "http://jivesoftware.com/protocol/workgroup";
  }
  
  public String toXML()
  {
    return MetaDataUtils.serializeMetaData(getMetaData());
  }
}


/* Location:           C:\Users\Administrator\Documents\ds\反编译\AndroidKiller_v1.3.1\projects\Jungledsq_ppc\ProjectSrc\smali\
 * Qualified Name:     org.jivesoftware.smackx.workgroup.MetaData
 * JD-Core Version:    0.7.0.1
 */