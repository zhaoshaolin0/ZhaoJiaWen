package org.jivesoftware.smackx.packet;

import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.packet.PacketExtension;

public class IBBExtensions
{
  public static final String NAMESPACE = "http://jabber.org/protocol/ibb";
  
  public static class Close
    extends IBBExtensions.IBB
  {
    public static final String ELEMENT_NAME = "close";
    
    public Close(String paramString)
    {
      super(null);
    }
    
    public String getChildElementXML()
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("<").append(getElementName()).append(" xmlns=\"").append(getNamespace()).append("\" ");
      localStringBuilder.append("sid=\"").append(getSessionID()).append("\"");
      localStringBuilder.append("/>");
      return localStringBuilder.toString();
    }
    
    public String getElementName()
    {
      return "close";
    }
  }
  
  public static class Data
    implements PacketExtension
  {
    public static final String ELEMENT_NAME = "data";
    private String data;
    private long seq;
    final String sid;
    
    public Data(String paramString)
    {
      this.sid = paramString;
    }
    
    public Data(String paramString1, long paramLong, String paramString2)
    {
      this(paramString1);
      this.seq = paramLong;
      this.data = paramString2;
    }
    
    public String getData()
    {
      return this.data;
    }
    
    public String getElementName()
    {
      return "data";
    }
    
    public String getNamespace()
    {
      return "http://jabber.org/protocol/ibb";
    }
    
    public long getSeq()
    {
      return this.seq;
    }
    
    public String getSessionID()
    {
      return this.sid;
    }
    
    public void setData(String paramString)
    {
      this.data = paramString;
    }
    
    public void setSeq(long paramLong)
    {
      this.seq = paramLong;
    }
    
    public String toXML()
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("<").append(getElementName()).append(" xmlns=\"").append(getNamespace()).append("\" ");
      localStringBuilder.append("sid=\"").append(getSessionID()).append("\" ");
      localStringBuilder.append("seq=\"").append(getSeq()).append("\"");
      localStringBuilder.append(">");
      localStringBuilder.append(getData());
      localStringBuilder.append("</").append(getElementName()).append(">");
      return localStringBuilder.toString();
    }
  }
  
  private static abstract class IBB
    extends IQ
  {
    final String sid;
    
    private IBB(String paramString)
    {
      this.sid = paramString;
    }
    
    public String getNamespace()
    {
      return "http://jabber.org/protocol/ibb";
    }
    
    public String getSessionID()
    {
      return this.sid;
    }
  }
  
  public static class Open
    extends IBBExtensions.IBB
  {
    public static final String ELEMENT_NAME = "open";
    private final int blockSize;
    
    public Open(String paramString, int paramInt)
    {
      super(null);
      this.blockSize = paramInt;
    }
    
    public int getBlockSize()
    {
      return this.blockSize;
    }
    
    public String getChildElementXML()
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("<").append(getElementName()).append(" xmlns=\"").append(getNamespace()).append("\" ");
      localStringBuilder.append("sid=\"").append(getSessionID()).append("\" ");
      localStringBuilder.append("block-size=\"").append(getBlockSize()).append("\"");
      localStringBuilder.append("/>");
      return localStringBuilder.toString();
    }
    
    public String getElementName()
    {
      return "open";
    }
  }
}


/* Location:           C:\Users\Administrator\Documents\ds\反编译\AndroidKiller_v1.3.1\projects\Jungledsq_ppc\ProjectSrc\smali\
 * Qualified Name:     org.jivesoftware.smackx.packet.IBBExtensions
 * JD-Core Version:    0.7.0.1
 */