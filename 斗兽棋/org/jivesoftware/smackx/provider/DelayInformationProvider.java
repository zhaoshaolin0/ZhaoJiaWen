package org.jivesoftware.smackx.provider;

import org.jivesoftware.smack.provider.PacketExtensionProvider;

public class DelayInformationProvider
  implements PacketExtensionProvider
{
  /* Error */
  public org.jivesoftware.smack.packet.PacketExtension parseExtension(org.xmlpull.v1.XmlPullParser paramXmlPullParser)
    throws java.lang.Exception
  {
    // Byte code:
    //   0: getstatic 23	org/jivesoftware/smackx/packet/DelayInformation:UTC_FORMAT	Ljava/text/SimpleDateFormat;
    //   3: astore_3
    //   4: aload_3
    //   5: monitorenter
    //   6: getstatic 23	org/jivesoftware/smackx/packet/DelayInformation:UTC_FORMAT	Ljava/text/SimpleDateFormat;
    //   9: aload_1
    //   10: ldc 25
    //   12: ldc 27
    //   14: invokeinterface 33 3 0
    //   19: invokevirtual 39	java/text/SimpleDateFormat:parse	(Ljava/lang/String;)Ljava/util/Date;
    //   22: astore_2
    //   23: aload_3
    //   24: monitorexit
    //   25: new 19	org/jivesoftware/smackx/packet/DelayInformation
    //   28: dup
    //   29: aload_2
    //   30: invokespecial 42	org/jivesoftware/smackx/packet/DelayInformation:<init>	(Ljava/util/Date;)V
    //   33: astore_2
    //   34: aload_2
    //   35: aload_1
    //   36: ldc 25
    //   38: ldc 44
    //   40: invokeinterface 33 3 0
    //   45: invokevirtual 48	org/jivesoftware/smackx/packet/DelayInformation:setFrom	(Ljava/lang/String;)V
    //   48: aload_2
    //   49: aload_1
    //   50: invokeinterface 52 1 0
    //   55: invokevirtual 55	org/jivesoftware/smackx/packet/DelayInformation:setReason	(Ljava/lang/String;)V
    //   58: aload_2
    //   59: areturn
    //   60: astore_2
    //   61: aload_3
    //   62: monitorexit
    //   63: aload_2
    //   64: athrow
    //   65: astore_2
    //   66: getstatic 58	org/jivesoftware/smackx/packet/DelayInformation:NEW_UTC_FORMAT	Ljava/text/SimpleDateFormat;
    //   69: astore_3
    //   70: aload_3
    //   71: monitorenter
    //   72: getstatic 58	org/jivesoftware/smackx/packet/DelayInformation:NEW_UTC_FORMAT	Ljava/text/SimpleDateFormat;
    //   75: aload_1
    //   76: ldc 25
    //   78: ldc 27
    //   80: invokeinterface 33 3 0
    //   85: invokevirtual 39	java/text/SimpleDateFormat:parse	(Ljava/lang/String;)Ljava/util/Date;
    //   88: astore_2
    //   89: aload_3
    //   90: monitorexit
    //   91: goto -66 -> 25
    //   94: astore_2
    //   95: aload_3
    //   96: monitorexit
    //   97: aload_2
    //   98: athrow
    //   99: astore_2
    //   100: new 35	java/text/SimpleDateFormat
    //   103: dup
    //   104: ldc 60
    //   106: invokespecial 62	java/text/SimpleDateFormat:<init>	(Ljava/lang/String;)V
    //   109: astore_2
    //   110: aload_2
    //   111: ldc 64
    //   113: invokestatic 70	java/util/TimeZone:getTimeZone	(Ljava/lang/String;)Ljava/util/TimeZone;
    //   116: invokevirtual 74	java/text/SimpleDateFormat:setTimeZone	(Ljava/util/TimeZone;)V
    //   119: aload_2
    //   120: aload_1
    //   121: ldc 25
    //   123: ldc 27
    //   125: invokeinterface 33 3 0
    //   130: invokevirtual 39	java/text/SimpleDateFormat:parse	(Ljava/lang/String;)Ljava/util/Date;
    //   133: astore_2
    //   134: goto -109 -> 25
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	137	0	this	DelayInformationProvider
    //   0	137	1	paramXmlPullParser	org.xmlpull.v1.XmlPullParser
    //   22	37	2	localObject1	Object
    //   60	4	2	localObject2	Object
    //   65	1	2	localParseException1	java.text.ParseException
    //   88	1	2	localDate	java.util.Date
    //   94	4	2	localObject3	Object
    //   99	1	2	localParseException2	java.text.ParseException
    //   109	25	2	localObject4	Object
    // Exception table:
    //   from	to	target	type
    //   6	25	60	finally
    //   61	63	60	finally
    //   0	6	65	java/text/ParseException
    //   63	65	65	java/text/ParseException
    //   72	91	94	finally
    //   95	97	94	finally
    //   66	72	99	java/text/ParseException
    //   97	99	99	java/text/ParseException
  }
}


/* Location:           C:\Users\Administrator\Documents\ds\反编译\AndroidKiller_v1.3.1\projects\Jungledsq_ppc\ProjectSrc\smali\
 * Qualified Name:     org.jivesoftware.smackx.provider.DelayInformationProvider
 * JD-Core Version:    0.7.0.1
 */