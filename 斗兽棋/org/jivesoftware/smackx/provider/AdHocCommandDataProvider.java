package org.jivesoftware.smackx.provider;

import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.packet.PacketExtension;
import org.jivesoftware.smack.provider.IQProvider;
import org.jivesoftware.smack.provider.PacketExtensionProvider;
import org.jivesoftware.smack.util.PacketParserUtils;
import org.jivesoftware.smackx.commands.AdHocCommand.Action;
import org.jivesoftware.smackx.commands.AdHocCommand.SpecificErrorCondition;
import org.jivesoftware.smackx.commands.AdHocCommand.Status;
import org.jivesoftware.smackx.commands.AdHocCommandNote;
import org.jivesoftware.smackx.commands.AdHocCommandNote.Type;
import org.jivesoftware.smackx.packet.AdHocCommandData;
import org.jivesoftware.smackx.packet.AdHocCommandData.SpecificError;
import org.jivesoftware.smackx.packet.DataForm;
import org.xmlpull.v1.XmlPullParser;

public class AdHocCommandDataProvider
  implements IQProvider
{
  public IQ parseIQ(XmlPullParser paramXmlPullParser)
    throws Exception
  {
    int j = 0;
    AdHocCommandData localAdHocCommandData = new AdHocCommandData();
    DataFormProvider localDataFormProvider = new DataFormProvider();
    localAdHocCommandData.setSessionID(paramXmlPullParser.getAttributeValue("", "sessionid"));
    localAdHocCommandData.setNode(paramXmlPullParser.getAttributeValue("", "node"));
    Object localObject = paramXmlPullParser.getAttributeValue("", "status");
    int i;
    if (AdHocCommand.Status.executing.toString().equalsIgnoreCase((String)localObject))
    {
      localAdHocCommandData.setStatus(AdHocCommand.Status.executing);
      localObject = paramXmlPullParser.getAttributeValue("", "action");
      i = j;
      if (localObject != null)
      {
        localObject = AdHocCommand.Action.valueOf((String)localObject);
        if ((localObject != null) && (!((AdHocCommand.Action)localObject).equals(AdHocCommand.Action.unknown))) {
          break label262;
        }
        localAdHocCommandData.setAction(AdHocCommand.Action.unknown);
        i = j;
      }
    }
    for (;;)
    {
      if (i != 0) {
        break label484;
      }
      j = paramXmlPullParser.next();
      localObject = paramXmlPullParser.getName();
      String str = paramXmlPullParser.getNamespace();
      if (j == 2)
      {
        if (paramXmlPullParser.getName().equals("actions"))
        {
          localObject = paramXmlPullParser.getAttributeValue("", "execute");
          if (localObject == null) {
            continue;
          }
          localAdHocCommandData.setExecuteAction(AdHocCommand.Action.valueOf((String)localObject));
          continue;
          if (AdHocCommand.Status.completed.toString().equalsIgnoreCase((String)localObject))
          {
            localAdHocCommandData.setStatus(AdHocCommand.Status.completed);
            break;
          }
          if (!AdHocCommand.Status.canceled.toString().equalsIgnoreCase((String)localObject)) {
            break;
          }
          localAdHocCommandData.setStatus(AdHocCommand.Status.canceled);
          break;
          label262:
          localAdHocCommandData.setAction((AdHocCommand.Action)localObject);
          i = j;
          continue;
        }
        if (paramXmlPullParser.getName().equals("next"))
        {
          localAdHocCommandData.addAction(AdHocCommand.Action.next);
          continue;
        }
        if (paramXmlPullParser.getName().equals("complete"))
        {
          localAdHocCommandData.addAction(AdHocCommand.Action.complete);
          continue;
        }
        if (paramXmlPullParser.getName().equals("prev"))
        {
          localAdHocCommandData.addAction(AdHocCommand.Action.prev);
          continue;
        }
        if ((((String)localObject).equals("x")) && (str.equals("jabber:x:data")))
        {
          localAdHocCommandData.setForm((DataForm)localDataFormProvider.parseExtension(paramXmlPullParser));
          continue;
        }
        if (paramXmlPullParser.getName().equals("note"))
        {
          localAdHocCommandData.addNote(new AdHocCommandNote(AdHocCommandNote.Type.valueOf(paramXmlPullParser.getAttributeValue("", "type")), paramXmlPullParser.nextText()));
          continue;
        }
        if (!paramXmlPullParser.getName().equals("error")) {
          continue;
        }
        localAdHocCommandData.setError(PacketParserUtils.parseError(paramXmlPullParser));
        continue;
      }
      if ((j == 3) && (paramXmlPullParser.getName().equals("command"))) {
        i = 1;
      }
    }
    label484:
    return localAdHocCommandData;
  }
  
  public static class BadActionError
    implements PacketExtensionProvider
  {
    public PacketExtension parseExtension(XmlPullParser paramXmlPullParser)
      throws Exception
    {
      return new AdHocCommandData.SpecificError(AdHocCommand.SpecificErrorCondition.badAction);
    }
  }
  
  public static class BadLocaleError
    implements PacketExtensionProvider
  {
    public PacketExtension parseExtension(XmlPullParser paramXmlPullParser)
      throws Exception
    {
      return new AdHocCommandData.SpecificError(AdHocCommand.SpecificErrorCondition.badLocale);
    }
  }
  
  public static class BadPayloadError
    implements PacketExtensionProvider
  {
    public PacketExtension parseExtension(XmlPullParser paramXmlPullParser)
      throws Exception
    {
      return new AdHocCommandData.SpecificError(AdHocCommand.SpecificErrorCondition.badPayload);
    }
  }
  
  public static class BadSessionIDError
    implements PacketExtensionProvider
  {
    public PacketExtension parseExtension(XmlPullParser paramXmlPullParser)
      throws Exception
    {
      return new AdHocCommandData.SpecificError(AdHocCommand.SpecificErrorCondition.badSessionid);
    }
  }
  
  public static class MalformedActionError
    implements PacketExtensionProvider
  {
    public PacketExtension parseExtension(XmlPullParser paramXmlPullParser)
      throws Exception
    {
      return new AdHocCommandData.SpecificError(AdHocCommand.SpecificErrorCondition.malformedAction);
    }
  }
  
  public static class SessionExpiredError
    implements PacketExtensionProvider
  {
    public PacketExtension parseExtension(XmlPullParser paramXmlPullParser)
      throws Exception
    {
      return new AdHocCommandData.SpecificError(AdHocCommand.SpecificErrorCondition.sessionExpired);
    }
  }
}


/* Location:           C:\Users\Administrator\Documents\ds\反编译\AndroidKiller_v1.3.1\projects\Jungledsq_ppc\ProjectSrc\smali\
 * Qualified Name:     org.jivesoftware.smackx.provider.AdHocCommandDataProvider
 * JD-Core Version:    0.7.0.1
 */