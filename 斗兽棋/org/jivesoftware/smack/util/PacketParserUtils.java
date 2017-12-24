package org.jivesoftware.smack.util;

import java.beans.PropertyDescriptor;
import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.io.PrintStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.jivesoftware.smack.packet.DefaultPacketExtension;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Message.Type;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.packet.PacketExtension;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.packet.Presence.Mode;
import org.jivesoftware.smack.packet.Presence.Type;
import org.jivesoftware.smack.packet.XMPPError;
import org.jivesoftware.smack.packet.XMPPError.Type;
import org.jivesoftware.smack.provider.PacketExtensionProvider;
import org.jivesoftware.smack.provider.ProviderManager;
import org.xmlpull.v1.XmlPullParser;

public class PacketParserUtils
{
  private static final String PROPERTIES_NAMESPACE = "http://www.jivesoftware.com/xmlns/xmpp/properties";
  
  private static Object decode(Class paramClass, String paramString)
    throws Exception
  {
    if (paramClass.getName().equals("java.lang.String")) {
      return paramString;
    }
    if (paramClass.getName().equals("boolean")) {
      return Boolean.valueOf(paramString);
    }
    if (paramClass.getName().equals("int")) {
      return Integer.valueOf(paramString);
    }
    if (paramClass.getName().equals("long")) {
      return Long.valueOf(paramString);
    }
    if (paramClass.getName().equals("float")) {
      return Float.valueOf(paramString);
    }
    if (paramClass.getName().equals("double")) {
      return Double.valueOf(paramString);
    }
    if (paramClass.getName().equals("java.lang.Class")) {
      return Class.forName(paramString);
    }
    return null;
  }
  
  private static String getLanguageAttribute(XmlPullParser paramXmlPullParser)
  {
    int i = 0;
    while (i < paramXmlPullParser.getAttributeCount())
    {
      String str = paramXmlPullParser.getAttributeName(i);
      if (("xml:lang".equals(str)) || (("lang".equals(str)) && ("xml".equals(paramXmlPullParser.getAttributePrefix(i))))) {
        return paramXmlPullParser.getAttributeValue(i);
      }
      i += 1;
    }
    return null;
  }
  
  public static XMPPError parseError(XmlPullParser paramXmlPullParser)
    throws Exception
  {
    String str1 = "-1";
    String str2 = null;
    String str3 = null;
    Object localObject1 = null;
    ArrayList localArrayList = new ArrayList();
    int i = 0;
    while (i < paramXmlPullParser.getAttributeCount())
    {
      if (paramXmlPullParser.getAttributeName(i).equals("code")) {
        str1 = paramXmlPullParser.getAttributeValue("", "code");
      }
      if (paramXmlPullParser.getAttributeName(i).equals("type")) {
        str2 = paramXmlPullParser.getAttributeValue("", "type");
      }
      i += 1;
    }
    i = 0;
    while (i == 0)
    {
      int j = paramXmlPullParser.next();
      if (j == 2)
      {
        if (paramXmlPullParser.getName().equals("text"))
        {
          str3 = paramXmlPullParser.nextText();
        }
        else
        {
          localObject2 = paramXmlPullParser.getName();
          String str4 = paramXmlPullParser.getNamespace();
          if ("urn:ietf:params:xml:ns:xmpp-stanzas".equals(str4)) {
            localObject1 = localObject2;
          } else {
            localArrayList.add(parsePacketExtension((String)localObject2, str4, paramXmlPullParser));
          }
        }
      }
      else if ((j == 3) && (paramXmlPullParser.getName().equals("error"))) {
        i = 1;
      }
    }
    Object localObject2 = XMPPError.Type.CANCEL;
    paramXmlPullParser = (XmlPullParser)localObject2;
    if (str2 != null) {}
    try
    {
      paramXmlPullParser = XMPPError.Type.valueOf(str2.toUpperCase());
      return new XMPPError(Integer.parseInt(str1), paramXmlPullParser, localObject1, str3, localArrayList);
    }
    catch (IllegalArgumentException paramXmlPullParser)
    {
      for (;;)
      {
        paramXmlPullParser.printStackTrace();
        paramXmlPullParser = (XmlPullParser)localObject2;
      }
    }
  }
  
  public static Packet parseMessage(XmlPullParser paramXmlPullParser)
    throws Exception
  {
    Message localMessage = new Message();
    String str1 = paramXmlPullParser.getAttributeValue("", "id");
    Object localObject1 = str1;
    if (str1 == null) {
      localObject1 = "ID_NOT_AVAILABLE";
    }
    localMessage.setPacketID((String)localObject1);
    localMessage.setTo(paramXmlPullParser.getAttributeValue("", "to"));
    localMessage.setFrom(paramXmlPullParser.getAttributeValue("", "from"));
    localMessage.setType(Message.Type.fromString(paramXmlPullParser.getAttributeValue("", "type")));
    localObject1 = getLanguageAttribute(paramXmlPullParser);
    if ((localObject1 != null) && (!"".equals(((String)localObject1).trim()))) {
      localMessage.setLanguage((String)localObject1);
    }
    localObject1 = null;
    str1 = null;
    Object localObject2 = null;
    int i = 0;
    int j;
    Object localObject3;
    String str2;
    if (i == 0)
    {
      j = paramXmlPullParser.next();
      if (j == 2)
      {
        localObject3 = paramXmlPullParser.getName();
        str2 = paramXmlPullParser.getNamespace();
        if (((String)localObject3).equals("subject"))
        {
          if (str1 != null) {
            break label352;
          }
          str1 = paramXmlPullParser.nextText();
          localObject3 = localObject2;
          localObject2 = localObject1;
          localObject1 = localObject3;
        }
      }
    }
    for (;;)
    {
      localObject3 = localObject2;
      localObject2 = localObject1;
      localObject1 = localObject3;
      break;
      if (((String)localObject3).equals("body"))
      {
        localMessage.addBody(getLanguageAttribute(paramXmlPullParser), paramXmlPullParser.nextText());
        localObject3 = localObject1;
        localObject1 = localObject2;
        localObject2 = localObject3;
      }
      else
      {
        if (((String)localObject3).equals("thread"))
        {
          if (localObject1 == null)
          {
            localObject3 = paramXmlPullParser.nextText();
            localObject1 = localObject2;
            localObject2 = localObject3;
          }
        }
        else
        {
          if (((String)localObject3).equals("error"))
          {
            localMessage.setError(parseError(paramXmlPullParser));
            localObject3 = localObject1;
            localObject1 = localObject2;
            localObject2 = localObject3;
            continue;
          }
          if ((((String)localObject3).equals("properties")) && (str2.equals("http://www.jivesoftware.com/xmlns/xmpp/properties")))
          {
            localObject3 = parseProperties(paramXmlPullParser);
            localObject2 = localObject1;
            localObject1 = localObject3;
            continue;
          }
          localMessage.addExtension(parsePacketExtension((String)localObject3, str2, paramXmlPullParser));
        }
        label352:
        localObject3 = localObject1;
        localObject1 = localObject2;
        localObject2 = localObject3;
        continue;
        if ((j == 3) && (paramXmlPullParser.getName().equals("message")))
        {
          i = 1;
          localObject3 = localObject1;
          localObject1 = localObject2;
          localObject2 = localObject3;
          continue;
          localMessage.setSubject(str1);
          localMessage.setThread((String)localObject1);
          if (localObject2 != null)
          {
            paramXmlPullParser = localObject2.keySet().iterator();
            while (paramXmlPullParser.hasNext())
            {
              localObject1 = (String)paramXmlPullParser.next();
              localMessage.setProperty((String)localObject1, localObject2.get(localObject1));
            }
          }
          return localMessage;
        }
        else
        {
          localObject3 = localObject2;
          localObject2 = localObject1;
          localObject1 = localObject3;
        }
      }
    }
  }
  
  public static PacketExtension parsePacketExtension(String paramString1, String paramString2, XmlPullParser paramXmlPullParser)
    throws Exception
  {
    Object localObject = ProviderManager.getInstance().getExtensionProvider(paramString1, paramString2);
    if (localObject != null)
    {
      if ((localObject instanceof PacketExtensionProvider)) {
        return ((PacketExtensionProvider)localObject).parseExtension(paramXmlPullParser);
      }
      if ((localObject instanceof Class)) {
        return (PacketExtension)parseWithIntrospection(paramString1, (Class)localObject, paramXmlPullParser);
      }
    }
    paramString2 = new DefaultPacketExtension(paramString1, paramString2);
    int i = 0;
    while (i == 0)
    {
      int j = paramXmlPullParser.next();
      if (j == 2)
      {
        localObject = paramXmlPullParser.getName();
        if (paramXmlPullParser.isEmptyElementTag()) {
          paramString2.setValue((String)localObject, "");
        } else if (paramXmlPullParser.next() == 4) {
          paramString2.setValue((String)localObject, paramXmlPullParser.getText());
        }
      }
      else if ((j == 3) && (paramXmlPullParser.getName().equals(paramString1)))
      {
        i = 1;
      }
    }
    return paramString2;
  }
  
  public static Presence parsePresence(XmlPullParser paramXmlPullParser)
    throws Exception
  {
    Object localObject1 = Presence.Type.available;
    Object localObject5 = paramXmlPullParser.getAttributeValue("", "type");
    if ((localObject5 != null) && (!((String)localObject5).equals(""))) {}
    label465:
    for (;;)
    {
      int i;
      int j;
      try
      {
        Object localObject3 = Presence.Type.valueOf((String)localObject5);
        localObject1 = localObject3;
        localObject5 = new Presence((Presence.Type)localObject1);
        ((Presence)localObject5).setTo(paramXmlPullParser.getAttributeValue("", "to"));
        ((Presence)localObject5).setFrom(paramXmlPullParser.getAttributeValue("", "from"));
        localObject1 = paramXmlPullParser.getAttributeValue("", "id");
        if (localObject1 == null)
        {
          localObject3 = "ID_NOT_AVAILABLE";
          ((Presence)localObject5).setPacketID((String)localObject3);
          localObject3 = getLanguageAttribute(paramXmlPullParser);
          if ((localObject3 != null) && (!"".equals(((String)localObject3).trim()))) {
            ((Presence)localObject5).setLanguage((String)localObject3);
          }
          localObject3 = localObject1;
          if (localObject1 == null) {
            localObject3 = "ID_NOT_AVAILABLE";
          }
          ((Presence)localObject5).setPacketID((String)localObject3);
          i = 0;
          if (i != 0) {
            break;
          }
          j = paramXmlPullParser.next();
          if (j != 2) {
            break label465;
          }
          localObject1 = paramXmlPullParser.getName();
          localObject3 = paramXmlPullParser.getNamespace();
          if (!((String)localObject1).equals("status")) {
            break label251;
          }
          ((Presence)localObject5).setStatus(paramXmlPullParser.nextText());
          continue;
          continue;
        }
      }
      catch (IllegalArgumentException localIllegalArgumentException2)
      {
        System.err.println("Found invalid presence type " + (String)localObject5);
      }
      Object localObject4 = localObject1;
      continue;
      label251:
      if (((String)localObject1).equals("priority"))
      {
        try
        {
          ((Presence)localObject5).setPriority(Integer.parseInt(paramXmlPullParser.nextText()));
        }
        catch (NumberFormatException localNumberFormatException) {}catch (IllegalArgumentException localIllegalArgumentException1)
        {
          ((Presence)localObject5).setPriority(0);
        }
      }
      else
      {
        Object localObject2;
        if (localIllegalArgumentException1.equals("show"))
        {
          localObject2 = paramXmlPullParser.nextText();
          try
          {
            ((Presence)localObject5).setMode(Presence.Mode.valueOf((String)localObject2));
          }
          catch (IllegalArgumentException localIllegalArgumentException3)
          {
            System.err.println("Found invalid presence mode " + (String)localObject2);
          }
        }
        else if (((String)localObject2).equals("error"))
        {
          ((Presence)localObject5).setError(parseError(paramXmlPullParser));
        }
        else
        {
          Iterator localIterator;
          if ((((String)localObject2).equals("properties")) && (localIllegalArgumentException3.equals("http://www.jivesoftware.com/xmlns/xmpp/properties")))
          {
            localObject2 = parseProperties(paramXmlPullParser);
            localIterator = ((Map)localObject2).keySet().iterator();
            while (localIterator.hasNext())
            {
              String str = (String)localIterator.next();
              ((Presence)localObject5).setProperty(str, ((Map)localObject2).get(str));
            }
          }
          else
          {
            ((Presence)localObject5).addExtension(parsePacketExtension((String)localObject2, localIterator, paramXmlPullParser));
            continue;
            if ((j == 3) && (paramXmlPullParser.getName().equals("presence"))) {
              i = 1;
            }
          }
        }
      }
    }
    return localObject5;
  }
  
  public static Map<String, Object> parseProperties(XmlPullParser paramXmlPullParser)
    throws Exception
  {
    HashMap localHashMap = new HashMap();
    int i = paramXmlPullParser.next();
    int j;
    Object localObject3;
    Object localObject6;
    Object localObject5;
    Object localObject4;
    label50:
    Object localObject1;
    if ((i == 2) && (paramXmlPullParser.getName().equals("property")))
    {
      j = 0;
      localObject3 = null;
      localObject6 = null;
      localObject5 = null;
      localObject4 = null;
      if (j == 0)
      {
        i = paramXmlPullParser.next();
        if (i == 2)
        {
          localObject1 = paramXmlPullParser.getName();
          if (((String)localObject1).equals("name"))
          {
            localObject1 = paramXmlPullParser.nextText();
            localObject4 = localObject3;
            localObject3 = localObject5;
          }
        }
      }
    }
    for (;;)
    {
      Object localObject7 = localObject4;
      localObject4 = localObject1;
      localObject5 = localObject3;
      localObject3 = localObject7;
      break label50;
      if (((String)localObject1).equals("value"))
      {
        localObject3 = paramXmlPullParser.getAttributeValue("", "type");
        localObject5 = paramXmlPullParser.nextText();
        localObject1 = localObject4;
        localObject4 = localObject5;
        continue;
        if ((i == 3) && (paramXmlPullParser.getName().equals("property")))
        {
          if ("integer".equals(localObject5)) {
            localObject1 = new Integer((String)localObject3);
          }
          for (;;)
          {
            if ((localObject4 != null) && (localObject1 != null)) {
              localHashMap.put(localObject4, localObject1);
            }
            j = 1;
            localObject6 = localObject1;
            break;
            if ("long".equals(localObject5)) {
              localObject1 = new Long((String)localObject3);
            } else if ("float".equals(localObject5)) {
              localObject1 = new Float((String)localObject3);
            } else if ("double".equals(localObject5)) {
              localObject1 = new Double((String)localObject3);
            } else if ("boolean".equals(localObject5)) {
              localObject1 = Boolean.valueOf((String)localObject3);
            } else if ("string".equals(localObject5)) {
              localObject1 = localObject3;
            } else if ("java-object".equals(localObject5)) {
              try
              {
                localObject1 = new ObjectInputStream(new ByteArrayInputStream(StringUtils.decodeBase64((String)localObject3))).readObject();
              }
              catch (Exception localException)
              {
                localException.printStackTrace();
              }
            } else {
              localObject2 = localObject6;
            }
          }
          break;
          if ((i == 3) && (paramXmlPullParser.getName().equals("properties"))) {
            return localHashMap;
          }
          break;
        }
        break label50;
      }
      Object localObject2 = localObject4;
      localObject4 = localObject3;
      localObject3 = localObject5;
    }
  }
  
  public static Object parseWithIntrospection(String paramString, Class paramClass, XmlPullParser paramXmlPullParser)
    throws Exception
  {
    int i = 0;
    Object localObject1 = paramClass.newInstance();
    while (i == 0)
    {
      int j = paramXmlPullParser.next();
      if (j == 2)
      {
        Object localObject3 = paramXmlPullParser.getName();
        Object localObject2 = paramXmlPullParser.nextText();
        localObject3 = new PropertyDescriptor((String)localObject3, paramClass);
        localObject2 = decode(((PropertyDescriptor)localObject3).getPropertyType(), (String)localObject2);
        ((PropertyDescriptor)localObject3).getWriteMethod().invoke(localObject1, new Object[] { localObject2 });
      }
      else if ((j == 3) && (paramXmlPullParser.getName().equals(paramString)))
      {
        i = 1;
      }
    }
    return localObject1;
  }
}


/* Location:           C:\Users\Administrator\Documents\ds\反编译\AndroidKiller_v1.3.1\projects\Jungledsq_ppc\ProjectSrc\smali\
 * Qualified Name:     org.jivesoftware.smack.util.PacketParserUtils
 * JD-Core Version:    0.7.0.1
 */