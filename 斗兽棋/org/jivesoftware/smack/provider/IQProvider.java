package org.jivesoftware.smack.provider;

import org.jivesoftware.smack.packet.IQ;
import org.xmlpull.v1.XmlPullParser;

public abstract interface IQProvider
{
  public abstract IQ parseIQ(XmlPullParser paramXmlPullParser)
    throws Exception;
}


/* Location:           C:\Users\Administrator\Documents\ds\反编译\AndroidKiller_v1.3.1\projects\Jungledsq_ppc\ProjectSrc\smali\
 * Qualified Name:     org.jivesoftware.smack.provider.IQProvider
 * JD-Core Version:    0.7.0.1
 */