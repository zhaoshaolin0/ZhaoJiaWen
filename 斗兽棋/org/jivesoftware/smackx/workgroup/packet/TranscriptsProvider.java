package org.jivesoftware.smackx.workgroup.packet;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.provider.IQProvider;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

public class TranscriptsProvider
  implements IQProvider
{
  private static final SimpleDateFormat UTC_FORMAT = new SimpleDateFormat("yyyyMMdd'T'HH:mm:ss");
  
  static
  {
    UTC_FORMAT.setTimeZone(TimeZone.getTimeZone("GMT+0"));
  }
  
  private List<Transcripts.AgentDetail> parseAgents(XmlPullParser paramXmlPullParser)
    throws IOException, XmlPullParserException
  {
    ArrayList localArrayList = new ArrayList();
    String str = null;
    Object localObject1 = null;
    Object localObject2 = null;
    int i = 0;
    for (;;)
    {
      int j;
      if (i == 0)
      {
        j = paramXmlPullParser.next();
        if (j == 2)
        {
          if (paramXmlPullParser.getName().equals("agentJID"))
          {
            str = paramXmlPullParser.nextText();
            continue;
          }
          if (!paramXmlPullParser.getName().equals("joinTime")) {}
        }
      }
      Date localDate;
      try
      {
        localDate = UTC_FORMAT.parse(paramXmlPullParser.nextText());
        localObject1 = localDate;
      }
      catch (ParseException localParseException2) {}
      if (paramXmlPullParser.getName().equals("leftTime")) {}
      try
      {
        localDate = UTC_FORMAT.parse(paramXmlPullParser.nextText());
        localObject2 = localDate;
      }
      catch (ParseException localParseException1) {}
      if (paramXmlPullParser.getName().equals("agent"))
      {
        str = null;
        localObject1 = null;
        localObject2 = null;
        continue;
        if (j == 3) {
          if (paramXmlPullParser.getName().equals("agents"))
          {
            i = 1;
          }
          else if (paramXmlPullParser.getName().equals("agent"))
          {
            localArrayList.add(new Transcripts.AgentDetail(str, localObject1, localObject2));
            continue;
            return localArrayList;
          }
        }
      }
    }
  }
  
  private Transcripts.TranscriptSummary parseSummary(XmlPullParser paramXmlPullParser)
    throws IOException, XmlPullParserException
  {
    String str = paramXmlPullParser.getAttributeValue("", "sessionID");
    Object localObject2 = null;
    Object localObject1 = null;
    Object localObject3 = new ArrayList();
    int i = 0;
    for (;;)
    {
      int j;
      if (i == 0)
      {
        j = paramXmlPullParser.next();
        if ((j == 2) && (!paramXmlPullParser.getName().equals("joinTime"))) {}
      }
      Date localDate;
      try
      {
        localDate = UTC_FORMAT.parse(paramXmlPullParser.nextText());
        localObject2 = localDate;
      }
      catch (ParseException localParseException2) {}
      if (paramXmlPullParser.getName().equals("leftTime")) {}
      try
      {
        localDate = UTC_FORMAT.parse(paramXmlPullParser.nextText());
        localObject1 = localDate;
      }
      catch (ParseException localParseException1) {}
      if (paramXmlPullParser.getName().equals("agents"))
      {
        localObject3 = parseAgents(paramXmlPullParser);
        continue;
        if ((j == 3) && (paramXmlPullParser.getName().equals("transcript")))
        {
          i = 1;
          continue;
          return new Transcripts.TranscriptSummary(str, localObject2, localObject1, (List)localObject3);
        }
      }
    }
  }
  
  public IQ parseIQ(XmlPullParser paramXmlPullParser)
    throws Exception
  {
    String str = paramXmlPullParser.getAttributeValue("", "userID");
    ArrayList localArrayList = new ArrayList();
    int i = 0;
    while (i == 0)
    {
      int j = paramXmlPullParser.next();
      if (j == 2)
      {
        if (paramXmlPullParser.getName().equals("transcript")) {
          localArrayList.add(parseSummary(paramXmlPullParser));
        }
      }
      else if ((j == 3) && (paramXmlPullParser.getName().equals("transcripts"))) {
        i = 1;
      }
    }
    return new Transcripts(str, localArrayList);
  }
}


/* Location:           C:\Users\Administrator\Documents\ds\反编译\AndroidKiller_v1.3.1\projects\Jungledsq_ppc\ProjectSrc\smali\
 * Qualified Name:     org.jivesoftware.smackx.workgroup.packet.TranscriptsProvider
 * JD-Core Version:    0.7.0.1
 */