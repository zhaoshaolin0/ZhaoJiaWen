package org.jivesoftware.smackx.packet;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.packet.IQ.Type;
import org.jivesoftware.smack.packet.PacketExtension;

public class Bytestream
  extends IQ
{
  private Mode mode = Mode.tcp;
  private String sessionID;
  private final List<StreamHost> streamHosts = new ArrayList();
  private Activate toActivate;
  private StreamHostUsed usedHost;
  
  public Bytestream() {}
  
  public Bytestream(String paramString)
  {
    setSessionID(paramString);
  }
  
  public StreamHost addStreamHost(String paramString1, String paramString2)
  {
    return addStreamHost(paramString1, paramString2, 0);
  }
  
  public StreamHost addStreamHost(String paramString1, String paramString2, int paramInt)
  {
    paramString1 = new StreamHost(paramString1, paramString2);
    paramString1.setPort(paramInt);
    addStreamHost(paramString1);
    return paramString1;
  }
  
  public void addStreamHost(StreamHost paramStreamHost)
  {
    this.streamHosts.add(paramStreamHost);
  }
  
  public int countStreamHosts()
  {
    return this.streamHosts.size();
  }
  
  public String getChildElementXML()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("<query xmlns=\"http://jabber.org/protocol/bytestreams\"");
    Iterator localIterator;
    if (getType().equals(IQ.Type.SET))
    {
      if (getSessionID() != null) {
        localStringBuilder.append(" sid=\"").append(getSessionID()).append("\"");
      }
      if (getMode() != null) {
        localStringBuilder.append(" mode = \"").append(getMode()).append("\"");
      }
      localStringBuilder.append(">");
      if (getToActivate() == null)
      {
        localIterator = getStreamHosts().iterator();
        while (localIterator.hasNext()) {
          localStringBuilder.append(((StreamHost)localIterator.next()).toXML());
        }
      }
      localStringBuilder.append(getToActivate().toXML());
    }
    for (;;)
    {
      localStringBuilder.append("</query>");
      return localStringBuilder.toString();
      if (!getType().equals(IQ.Type.RESULT)) {
        break;
      }
      localStringBuilder.append(">");
      if (getUsedHost() != null)
      {
        localStringBuilder.append(getUsedHost().toXML());
      }
      else if (countStreamHosts() > 0)
      {
        localIterator = this.streamHosts.iterator();
        while (localIterator.hasNext()) {
          localStringBuilder.append(((StreamHost)localIterator.next()).toXML());
        }
      }
    }
    return null;
  }
  
  public Mode getMode()
  {
    return this.mode;
  }
  
  public String getSessionID()
  {
    return this.sessionID;
  }
  
  public StreamHost getStreamHost(String paramString)
  {
    if (paramString == null) {
      return null;
    }
    Iterator localIterator = this.streamHosts.iterator();
    while (localIterator.hasNext())
    {
      StreamHost localStreamHost = (StreamHost)localIterator.next();
      if (localStreamHost.getJID().equals(paramString)) {
        return localStreamHost;
      }
    }
    return null;
  }
  
  public Collection<StreamHost> getStreamHosts()
  {
    return Collections.unmodifiableCollection(this.streamHosts);
  }
  
  public Activate getToActivate()
  {
    return this.toActivate;
  }
  
  public StreamHostUsed getUsedHost()
  {
    return this.usedHost;
  }
  
  public void setMode(Mode paramMode)
  {
    this.mode = paramMode;
  }
  
  public void setSessionID(String paramString)
  {
    this.sessionID = paramString;
  }
  
  public void setToActivate(String paramString)
  {
    this.toActivate = new Activate(paramString);
  }
  
  public void setUsedHost(String paramString)
  {
    this.usedHost = new StreamHostUsed(paramString);
  }
  
  public static class Activate
    implements PacketExtension
  {
    public static String ELEMENTNAME = "activate";
    public String NAMESPACE = "";
    private final String target;
    
    public Activate(String paramString)
    {
      this.target = paramString;
    }
    
    public String getElementName()
    {
      return ELEMENTNAME;
    }
    
    public String getNamespace()
    {
      return this.NAMESPACE;
    }
    
    public String getTarget()
    {
      return this.target;
    }
    
    public String toXML()
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("<").append(getElementName()).append(">");
      localStringBuilder.append(getTarget());
      localStringBuilder.append("</").append(getElementName()).append(">");
      return localStringBuilder.toString();
    }
  }
  
  public static enum Mode
  {
    tcp,  udp;
    
    private Mode() {}
    
    public static Mode fromName(String paramString)
    {
      try
      {
        paramString = valueOf(paramString);
        return paramString;
      }
      catch (Exception paramString) {}
      return tcp;
    }
  }
  
  public static class StreamHost
    implements PacketExtension
  {
    public static String ELEMENTNAME = "streamhost";
    public static String NAMESPACE = "";
    private final String JID;
    private final String addy;
    private int port = 0;
    
    public StreamHost(String paramString1, String paramString2)
    {
      this.JID = paramString1;
      this.addy = paramString2;
    }
    
    public String getAddress()
    {
      return this.addy;
    }
    
    public String getElementName()
    {
      return ELEMENTNAME;
    }
    
    public String getJID()
    {
      return this.JID;
    }
    
    public String getNamespace()
    {
      return NAMESPACE;
    }
    
    public int getPort()
    {
      return this.port;
    }
    
    public void setPort(int paramInt)
    {
      this.port = paramInt;
    }
    
    public String toXML()
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("<").append(getElementName()).append(" ");
      localStringBuilder.append("jid=\"").append(getJID()).append("\" ");
      localStringBuilder.append("host=\"").append(getAddress()).append("\" ");
      if (getPort() != 0) {
        localStringBuilder.append("port=\"").append(getPort()).append("\"");
      }
      for (;;)
      {
        localStringBuilder.append("/>");
        return localStringBuilder.toString();
        localStringBuilder.append("zeroconf=\"_jabber.bytestreams\"");
      }
    }
  }
  
  public static class StreamHostUsed
    implements PacketExtension
  {
    public static String ELEMENTNAME = "streamhost-used";
    private final String JID;
    public String NAMESPACE = "";
    
    public StreamHostUsed(String paramString)
    {
      this.JID = paramString;
    }
    
    public String getElementName()
    {
      return ELEMENTNAME;
    }
    
    public String getJID()
    {
      return this.JID;
    }
    
    public String getNamespace()
    {
      return this.NAMESPACE;
    }
    
    public String toXML()
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("<").append(getElementName()).append(" ");
      localStringBuilder.append("jid=\"").append(getJID()).append("\" ");
      localStringBuilder.append("/>");
      return localStringBuilder.toString();
    }
  }
}


/* Location:           C:\Users\Administrator\Documents\ds\反编译\AndroidKiller_v1.3.1\projects\Jungledsq_ppc\ProjectSrc\smali\
 * Qualified Name:     org.jivesoftware.smackx.packet.Bytestream
 * JD-Core Version:    0.7.0.1
 */