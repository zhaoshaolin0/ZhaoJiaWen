package org.jivesoftware.smackx;

import java.util.Iterator;
import org.jivesoftware.smack.ConnectionCreationListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smackx.packet.DiscoverInfo;
import org.jivesoftware.smackx.packet.XHTMLExtension;

public class XHTMLManager
{
  private static final String namespace = "http://jabber.org/protocol/xhtml-im";
  
  static
  {
    XMPPConnection.addConnectionCreationListener(new ConnectionCreationListener()
    {
      public void connectionCreated(XMPPConnection paramAnonymousXMPPConnection)
      {
        XHTMLManager.setServiceEnabled(paramAnonymousXMPPConnection, true);
      }
    });
  }
  
  public static void addBody(Message paramMessage, String paramString)
  {
    XHTMLExtension localXHTMLExtension2 = (XHTMLExtension)paramMessage.getExtension("html", "http://jabber.org/protocol/xhtml-im");
    XHTMLExtension localXHTMLExtension1 = localXHTMLExtension2;
    if (localXHTMLExtension2 == null)
    {
      localXHTMLExtension1 = new XHTMLExtension();
      paramMessage.addExtension(localXHTMLExtension1);
    }
    localXHTMLExtension1.addBody(paramString);
  }
  
  public static Iterator getBodies(Message paramMessage)
  {
    paramMessage = (XHTMLExtension)paramMessage.getExtension("html", "http://jabber.org/protocol/xhtml-im");
    if (paramMessage != null) {
      return paramMessage.getBodies();
    }
    return null;
  }
  
  public static boolean isServiceEnabled(XMPPConnection paramXMPPConnection)
  {
    return ServiceDiscoveryManager.getInstanceFor(paramXMPPConnection).includesFeature("http://jabber.org/protocol/xhtml-im");
  }
  
  public static boolean isServiceEnabled(XMPPConnection paramXMPPConnection, String paramString)
  {
    try
    {
      boolean bool = ServiceDiscoveryManager.getInstanceFor(paramXMPPConnection).discoverInfo(paramString).containsFeature("http://jabber.org/protocol/xhtml-im");
      return bool;
    }
    catch (XMPPException paramXMPPConnection)
    {
      paramXMPPConnection.printStackTrace();
    }
    return false;
  }
  
  public static boolean isXHTMLMessage(Message paramMessage)
  {
    return paramMessage.getExtension("html", "http://jabber.org/protocol/xhtml-im") != null;
  }
  
  public static void setServiceEnabled(XMPPConnection paramXMPPConnection, boolean paramBoolean)
  {
    for (;;)
    {
      try
      {
        boolean bool = isServiceEnabled(paramXMPPConnection);
        if (bool == paramBoolean) {
          return;
        }
        if (paramBoolean) {
          ServiceDiscoveryManager.getInstanceFor(paramXMPPConnection).addFeature("http://jabber.org/protocol/xhtml-im");
        } else {
          ServiceDiscoveryManager.getInstanceFor(paramXMPPConnection).removeFeature("http://jabber.org/protocol/xhtml-im");
        }
      }
      finally {}
    }
  }
}


/* Location:           C:\Users\Administrator\Documents\ds\反编译\AndroidKiller_v1.3.1\projects\Jungledsq_ppc\ProjectSrc\smali\
 * Qualified Name:     org.jivesoftware.smackx.XHTMLManager
 * JD-Core Version:    0.7.0.1
 */