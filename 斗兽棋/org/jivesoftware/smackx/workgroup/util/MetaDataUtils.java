package org.jivesoftware.smackx.workgroup.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.jivesoftware.smack.util.StringUtils;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

public class MetaDataUtils
{
  public static Map parseMetaData(XmlPullParser paramXmlPullParser)
    throws XmlPullParserException, IOException
  {
    if ((paramXmlPullParser.getEventType() == 2) && (paramXmlPullParser.getName().equals("metadata")) && (paramXmlPullParser.getNamespace().equals("http://jivesoftware.com/protocol/workgroup")))
    {
      Hashtable localHashtable = new Hashtable();
      int i = paramXmlPullParser.nextTag();
      if ((i != 3) || (!paramXmlPullParser.getName().equals("metadata")))
      {
        String str1 = paramXmlPullParser.getAttributeValue(0);
        String str2 = paramXmlPullParser.nextText();
        if (localHashtable.containsKey(str1)) {
          ((List)localHashtable.get(str1)).add(str2);
        }
        for (;;)
        {
          i = paramXmlPullParser.nextTag();
          break;
          ArrayList localArrayList = new ArrayList();
          localArrayList.add(str2);
          localHashtable.put(str1, localArrayList);
        }
      }
      return localHashtable;
    }
    return Collections.EMPTY_MAP;
  }
  
  public static String serializeMetaData(Map paramMap)
  {
    StringBuilder localStringBuilder = new StringBuilder();
    if ((paramMap != null) && (paramMap.size() > 0))
    {
      localStringBuilder.append("<metadata xmlns=\"http://jivesoftware.com/protocol/workgroup\">");
      Iterator localIterator = paramMap.keySet().iterator();
      while (localIterator.hasNext())
      {
        Object localObject1 = localIterator.next();
        Object localObject2 = paramMap.get(localObject1);
        if ((localObject2 instanceof List))
        {
          localObject2 = ((List)paramMap.get(localObject1)).iterator();
          while (((Iterator)localObject2).hasNext())
          {
            String str = (String)((Iterator)localObject2).next();
            localStringBuilder.append("<value name=\"").append(localObject1).append("\">");
            localStringBuilder.append(StringUtils.escapeForXML(str));
            localStringBuilder.append("</value>");
          }
        }
        else if ((localObject2 instanceof String))
        {
          localStringBuilder.append("<value name=\"").append(localObject1).append("\">");
          localStringBuilder.append(StringUtils.escapeForXML((String)localObject2));
          localStringBuilder.append("</value>");
        }
      }
      localStringBuilder.append("</metadata>");
    }
    return localStringBuilder.toString();
  }
}


/* Location:           C:\Users\Administrator\Documents\ds\反编译\AndroidKiller_v1.3.1\projects\Jungledsq_ppc\ProjectSrc\smali\
 * Qualified Name:     org.jivesoftware.smackx.workgroup.util.MetaDataUtils
 * JD-Core Version:    0.7.0.1
 */