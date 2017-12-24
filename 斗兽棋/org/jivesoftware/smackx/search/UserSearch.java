package org.jivesoftware.smackx.search;

import org.jivesoftware.smack.PacketCollector;
import org.jivesoftware.smack.SmackConfiguration;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.filter.PacketIDFilter;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.packet.IQ.Type;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.provider.IQProvider;
import org.jivesoftware.smack.util.PacketParserUtils;
import org.jivesoftware.smackx.Form;
import org.jivesoftware.smackx.FormField;
import org.jivesoftware.smackx.ReportedData;
import org.jivesoftware.smackx.packet.DataForm;
import org.xmlpull.v1.XmlPullParser;

public class UserSearch
  extends IQ
{
  private static void buildDataForm(SimpleUserSearch paramSimpleUserSearch, String paramString, XmlPullParser paramXmlPullParser)
    throws Exception
  {
    DataForm localDataForm = new DataForm("form");
    int i = 0;
    localDataForm.setTitle("User Search");
    localDataForm.addInstruction(paramString);
    while (i == 0)
    {
      int j = paramXmlPullParser.next();
      if ((j == 2) && (!paramXmlPullParser.getNamespace().equals("jabber:x:data")))
      {
        paramString = paramXmlPullParser.getName();
        FormField localFormField = new FormField(paramString);
        if (paramString.equals("first")) {
          localFormField.setLabel("First Name");
        }
        for (;;)
        {
          localFormField.setType("text-single");
          localDataForm.addField(localFormField);
          break;
          if (paramString.equals("last")) {
            localFormField.setLabel("Last Name");
          } else if (paramString.equals("email")) {
            localFormField.setLabel("Email Address");
          } else if (paramString.equals("nick")) {
            localFormField.setLabel("Nickname");
          }
        }
      }
      if (j == 3)
      {
        if (paramXmlPullParser.getName().equals("query")) {
          i = 1;
        }
      }
      else if ((j == 2) && (paramXmlPullParser.getNamespace().equals("jabber:x:data")))
      {
        paramSimpleUserSearch.addExtension(PacketParserUtils.parsePacketExtension(paramXmlPullParser.getName(), paramXmlPullParser.getNamespace(), paramXmlPullParser));
        i = 1;
      }
    }
    if (paramSimpleUserSearch.getExtension("x", "jabber:x:data") == null) {
      paramSimpleUserSearch.addExtension(localDataForm);
    }
  }
  
  public String getChildElementXML()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("<query xmlns=\"jabber:iq:search\">");
    localStringBuilder.append(getExtensionsXML());
    localStringBuilder.append("</query>");
    return localStringBuilder.toString();
  }
  
  public Form getSearchForm(XMPPConnection paramXMPPConnection, String paramString)
    throws XMPPException
  {
    UserSearch localUserSearch = new UserSearch();
    localUserSearch.setType(IQ.Type.GET);
    localUserSearch.setTo(paramString);
    paramString = paramXMPPConnection.createPacketCollector(new PacketIDFilter(localUserSearch.getPacketID()));
    paramXMPPConnection.sendPacket(localUserSearch);
    paramXMPPConnection = (IQ)paramString.nextResult(SmackConfiguration.getPacketReplyTimeout());
    paramString.cancel();
    if (paramXMPPConnection == null) {
      throw new XMPPException("No response from server on status set.");
    }
    if (paramXMPPConnection.getError() != null) {
      throw new XMPPException(paramXMPPConnection.getError());
    }
    return Form.getFormFrom(paramXMPPConnection);
  }
  
  public ReportedData sendSearchForm(XMPPConnection paramXMPPConnection, Form paramForm, String paramString)
    throws XMPPException
  {
    Object localObject = new UserSearch();
    ((UserSearch)localObject).setType(IQ.Type.SET);
    ((UserSearch)localObject).setTo(paramString);
    ((UserSearch)localObject).addExtension(paramForm.getDataFormToSend());
    PacketCollector localPacketCollector = paramXMPPConnection.createPacketCollector(new PacketIDFilter(((UserSearch)localObject).getPacketID()));
    paramXMPPConnection.sendPacket((Packet)localObject);
    localObject = (IQ)localPacketCollector.nextResult(SmackConfiguration.getPacketReplyTimeout());
    localPacketCollector.cancel();
    if (localObject == null) {
      throw new XMPPException("No response from server on status set.");
    }
    if (((IQ)localObject).getError() != null) {
      return sendSimpleSearchForm(paramXMPPConnection, paramForm, paramString);
    }
    return ReportedData.getReportedDataFrom((Packet)localObject);
  }
  
  public ReportedData sendSimpleSearchForm(XMPPConnection paramXMPPConnection, Form paramForm, String paramString)
    throws XMPPException
  {
    SimpleUserSearch localSimpleUserSearch = new SimpleUserSearch();
    localSimpleUserSearch.setForm(paramForm);
    localSimpleUserSearch.setType(IQ.Type.SET);
    localSimpleUserSearch.setTo(paramString);
    paramForm = paramXMPPConnection.createPacketCollector(new PacketIDFilter(localSimpleUserSearch.getPacketID()));
    paramXMPPConnection.sendPacket(localSimpleUserSearch);
    paramXMPPConnection = (IQ)paramForm.nextResult(SmackConfiguration.getPacketReplyTimeout());
    paramForm.cancel();
    if (paramXMPPConnection == null) {
      throw new XMPPException("No response from server on status set.");
    }
    if (paramXMPPConnection.getError() != null) {
      throw new XMPPException(paramXMPPConnection.getError());
    }
    if ((paramXMPPConnection instanceof SimpleUserSearch)) {
      return ((SimpleUserSearch)paramXMPPConnection).getReportedData();
    }
    return null;
  }
  
  public static class Provider
    implements IQProvider
  {
    public IQ parseIQ(XmlPullParser paramXmlPullParser)
      throws Exception
    {
      UserSearch localUserSearch = null;
      SimpleUserSearch localSimpleUserSearch = new SimpleUserSearch();
      int i = 0;
      while (i == 0)
      {
        int j = paramXmlPullParser.next();
        if ((j == 2) && (paramXmlPullParser.getName().equals("instructions")))
        {
          UserSearch.buildDataForm(localSimpleUserSearch, paramXmlPullParser.nextText(), paramXmlPullParser);
          return localSimpleUserSearch;
        }
        if ((j == 2) && (paramXmlPullParser.getName().equals("item")))
        {
          localSimpleUserSearch.parseItems(paramXmlPullParser);
          return localSimpleUserSearch;
        }
        if ((j == 2) && (paramXmlPullParser.getNamespace().equals("jabber:x:data")))
        {
          localUserSearch = new UserSearch();
          localUserSearch.addExtension(PacketParserUtils.parsePacketExtension(paramXmlPullParser.getName(), paramXmlPullParser.getNamespace(), paramXmlPullParser));
        }
        else if ((j == 3) && (paramXmlPullParser.getName().equals("query")))
        {
          i = 1;
        }
      }
      if (localUserSearch != null) {
        return localUserSearch;
      }
      return localSimpleUserSearch;
    }
  }
}


/* Location:           C:\Users\Administrator\Documents\ds\反编译\AndroidKiller_v1.3.1\projects\Jungledsq_ppc\ProjectSrc\smali\
 * Qualified Name:     org.jivesoftware.smackx.search.UserSearch
 * JD-Core Version:    0.7.0.1
 */