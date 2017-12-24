package org.jivesoftware.smackx.provider;

import java.text.SimpleDateFormat;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.provider.IQProvider;
import org.jivesoftware.smackx.packet.DataForm;
import org.jivesoftware.smackx.packet.DelayInformation;
import org.jivesoftware.smackx.packet.StreamInitiation;
import org.jivesoftware.smackx.packet.StreamInitiation.File;
import org.xmlpull.v1.XmlPullParser;

public class StreamInitiationProvider
  implements IQProvider
{
  public IQ parseIQ(XmlPullParser paramXmlPullParser)
    throws Exception
  {
    int i = 0;
    String str6 = paramXmlPullParser.getAttributeValue("", "id");
    String str7 = paramXmlPullParser.getAttributeValue("", "mime-type");
    StreamInitiation localStreamInitiation = new StreamInitiation();
    String str2 = null;
    String str3 = null;
    String str1 = null;
    String str5 = null;
    String str4 = null;
    boolean bool = false;
    DataForm localDataForm = null;
    DataFormProvider localDataFormProvider = new DataFormProvider();
    for (;;)
    {
      if (i == 0)
      {
        int j = paramXmlPullParser.next();
        Object localObject = paramXmlPullParser.getName();
        String str8 = paramXmlPullParser.getNamespace();
        if (j == 2)
        {
          if (((String)localObject).equals("file"))
          {
            str2 = paramXmlPullParser.getAttributeValue("", "name");
            str3 = paramXmlPullParser.getAttributeValue("", "size");
            str1 = paramXmlPullParser.getAttributeValue("", "hash");
            str5 = paramXmlPullParser.getAttributeValue("", "date");
            continue;
          }
          if (((String)localObject).equals("desc"))
          {
            str4 = paramXmlPullParser.nextText();
            continue;
          }
          if (((String)localObject).equals("range"))
          {
            bool = true;
            continue;
          }
          if ((!((String)localObject).equals("x")) || (!str8.equals("jabber:x:data"))) {
            continue;
          }
          localDataForm = (DataForm)localDataFormProvider.parseExtension(paramXmlPullParser);
          continue;
        }
        if (j != 3) {
          continue;
        }
        if (((String)localObject).equals("si"))
        {
          i = 1;
          continue;
        }
        if (!((String)localObject).equals("file")) {
          continue;
        }
        long l2 = 0L;
        long l1 = l2;
        if (str3 != null)
        {
          l1 = l2;
          if (str3.trim().length() == 0) {}
        }
        try
        {
          l1 = Long.parseLong(str3);
          localObject = new StreamInitiation.File(str2, l1);
          ((StreamInitiation.File)localObject).setHash(str1);
          if (str5 != null) {
            ((StreamInitiation.File)localObject).setDate(DelayInformation.UTC_FORMAT.parse(str5));
          }
          ((StreamInitiation.File)localObject).setDesc(str4);
          ((StreamInitiation.File)localObject).setRanged(bool);
          localStreamInitiation.setFile((StreamInitiation.File)localObject);
        }
        catch (NumberFormatException localNumberFormatException)
        {
          for (;;)
          {
            localNumberFormatException.printStackTrace();
            l1 = l2;
          }
        }
      }
    }
    localStreamInitiation.setSesssionID(str6);
    localStreamInitiation.setMimeType(str7);
    localStreamInitiation.setFeatureNegotiationForm(localDataForm);
    return localStreamInitiation;
  }
}


/* Location:           C:\Users\Administrator\Documents\ds\反编译\AndroidKiller_v1.3.1\projects\Jungledsq_ppc\ProjectSrc\smali\
 * Qualified Name:     org.jivesoftware.smackx.provider.StreamInitiationProvider
 * JD-Core Version:    0.7.0.1
 */