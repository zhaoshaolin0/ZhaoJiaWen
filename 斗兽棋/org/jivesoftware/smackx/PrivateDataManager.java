package org.jivesoftware.smackx;

import java.util.Hashtable;
import java.util.Map;
import org.jivesoftware.smack.PacketCollector;
import org.jivesoftware.smack.SmackConfiguration;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.filter.PacketIDFilter;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.packet.IQ.Type;
import org.jivesoftware.smack.provider.IQProvider;
import org.jivesoftware.smackx.packet.DefaultPrivateData;
import org.jivesoftware.smackx.packet.PrivateData;
import org.jivesoftware.smackx.provider.PrivateDataProvider;
import org.xmlpull.v1.XmlPullParser;

public class PrivateDataManager
{
  private static Map privateDataProviders = new Hashtable();
  private XMPPConnection connection;
  private String user;
  
  public PrivateDataManager(XMPPConnection paramXMPPConnection)
  {
    if (!paramXMPPConnection.isAuthenticated()) {
      throw new IllegalStateException("Must be logged in to XMPP server.");
    }
    this.connection = paramXMPPConnection;
  }
  
  public PrivateDataManager(XMPPConnection paramXMPPConnection, String paramString)
  {
    if (!paramXMPPConnection.isAuthenticated()) {
      throw new IllegalStateException("Must be logged in to XMPP server.");
    }
    this.connection = paramXMPPConnection;
    this.user = paramString;
  }
  
  public static void addPrivateDataProvider(String paramString1, String paramString2, PrivateDataProvider paramPrivateDataProvider)
  {
    paramString1 = getProviderKey(paramString1, paramString2);
    privateDataProviders.put(paramString1, paramPrivateDataProvider);
  }
  
  public static PrivateDataProvider getPrivateDataProvider(String paramString1, String paramString2)
  {
    paramString1 = getProviderKey(paramString1, paramString2);
    return (PrivateDataProvider)privateDataProviders.get(paramString1);
  }
  
  private static String getProviderKey(String paramString1, String paramString2)
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("<").append(paramString1).append("/><").append(paramString2).append("/>");
    return localStringBuilder.toString();
  }
  
  public static void removePrivateDataProvider(String paramString1, String paramString2)
  {
    paramString1 = getProviderKey(paramString1, paramString2);
    privateDataProviders.remove(paramString1);
  }
  
  public PrivateData getPrivateData(final String paramString1, final String paramString2)
    throws XMPPException
  {
    paramString1 = new IQ()
    {
      public String getChildElementXML()
      {
        StringBuilder localStringBuilder = new StringBuilder();
        localStringBuilder.append("<query xmlns=\"jabber:iq:private\">");
        localStringBuilder.append("<").append(paramString1).append(" xmlns=\"").append(paramString2).append("\"/>");
        localStringBuilder.append("</query>");
        return localStringBuilder.toString();
      }
    };
    paramString1.setType(IQ.Type.GET);
    if (this.user != null) {
      paramString1.setTo(this.user);
    }
    paramString2 = paramString1.getPacketID();
    paramString2 = this.connection.createPacketCollector(new PacketIDFilter(paramString2));
    this.connection.sendPacket(paramString1);
    paramString1 = (IQ)paramString2.nextResult(SmackConfiguration.getPacketReplyTimeout());
    paramString2.cancel();
    if (paramString1 == null) {
      throw new XMPPException("No response from the server.");
    }
    if (paramString1.getType() == IQ.Type.ERROR) {
      throw new XMPPException(paramString1.getError());
    }
    return ((PrivateDataResult)paramString1).getPrivateData();
  }
  
  public void setPrivateData(final PrivateData paramPrivateData)
    throws XMPPException
  {
    paramPrivateData = new IQ()
    {
      public String getChildElementXML()
      {
        StringBuilder localStringBuilder = new StringBuilder();
        localStringBuilder.append("<query xmlns=\"jabber:iq:private\">");
        localStringBuilder.append(paramPrivateData.toXML());
        localStringBuilder.append("</query>");
        return localStringBuilder.toString();
      }
    };
    paramPrivateData.setType(IQ.Type.SET);
    if (this.user != null) {
      paramPrivateData.setTo(this.user);
    }
    Object localObject = paramPrivateData.getPacketID();
    localObject = this.connection.createPacketCollector(new PacketIDFilter((String)localObject));
    this.connection.sendPacket(paramPrivateData);
    paramPrivateData = (IQ)((PacketCollector)localObject).nextResult(5000L);
    ((PacketCollector)localObject).cancel();
    if (paramPrivateData == null) {
      throw new XMPPException("No response from the server.");
    }
    if (paramPrivateData.getType() == IQ.Type.ERROR) {
      throw new XMPPException(paramPrivateData.getError());
    }
  }
  
  public static class PrivateDataIQProvider
    implements IQProvider
  {
    public IQ parseIQ(XmlPullParser paramXmlPullParser)
      throws Exception
    {
      Object localObject1 = null;
      int i = 0;
      while (i == 0)
      {
        int j = paramXmlPullParser.next();
        if (j == 2)
        {
          String str = paramXmlPullParser.getName();
          localObject1 = paramXmlPullParser.getNamespace();
          Object localObject2 = PrivateDataManager.getPrivateDataProvider(str, (String)localObject1);
          if (localObject2 != null)
          {
            localObject1 = ((PrivateDataProvider)localObject2).parsePrivateData(paramXmlPullParser);
          }
          else
          {
            localObject1 = new DefaultPrivateData(str, (String)localObject1);
            j = 0;
            while (j == 0)
            {
              int k = paramXmlPullParser.next();
              if (k == 2)
              {
                localObject2 = paramXmlPullParser.getName();
                if (paramXmlPullParser.isEmptyElementTag()) {
                  ((DefaultPrivateData)localObject1).setValue((String)localObject2, "");
                } else if (paramXmlPullParser.next() == 4) {
                  ((DefaultPrivateData)localObject1).setValue((String)localObject2, paramXmlPullParser.getText());
                }
              }
              else if ((k == 3) && (paramXmlPullParser.getName().equals(str)))
              {
                j = 1;
              }
            }
          }
        }
        else if ((j == 3) && (paramXmlPullParser.getName().equals("query")))
        {
          i = 1;
        }
      }
      return new PrivateDataManager.PrivateDataResult((PrivateData)localObject1);
    }
  }
  
  private static class PrivateDataResult
    extends IQ
  {
    private PrivateData privateData;
    
    PrivateDataResult(PrivateData paramPrivateData)
    {
      this.privateData = paramPrivateData;
    }
    
    public String getChildElementXML()
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("<query xmlns=\"jabber:iq:private\">");
      if (this.privateData != null) {
        this.privateData.toXML();
      }
      localStringBuilder.append("</query>");
      return localStringBuilder.toString();
    }
    
    public PrivateData getPrivateData()
    {
      return this.privateData;
    }
  }
}


/* Location:           C:\Users\Administrator\Documents\ds\反编译\AndroidKiller_v1.3.1\projects\Jungledsq_ppc\ProjectSrc\smali\
 * Qualified Name:     org.jivesoftware.smackx.PrivateDataManager
 * JD-Core Version:    0.7.0.1
 */