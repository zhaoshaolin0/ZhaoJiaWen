package org.jivesoftware.smackx.provider;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.provider.IQProvider;
import org.jivesoftware.smack.util.StringUtils;
import org.jivesoftware.smackx.packet.VCard;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

public class VCardProvider
  implements IQProvider
{
  private static final String PREFERRED_ENCODING = "UTF-8";
  
  public static VCard createVCardFromXML(String paramString)
    throws Exception
  {
    VCard localVCard = new VCard();
    new VCardReader(localVCard, DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new ByteArrayInputStream(paramString.getBytes("UTF-8")))).initializeFields();
    return localVCard;
  }
  
  public IQ parseIQ(XmlPullParser paramXmlPullParser)
    throws Exception
  {
    localStringBuilder = new StringBuilder();
    for (;;)
    {
      try
      {
        i = paramXmlPullParser.getEventType();
      }
      catch (XmlPullParserException paramXmlPullParser)
      {
        boolean bool;
        paramXmlPullParser.printStackTrace();
        continue;
        localStringBuilder.append('<').append(paramXmlPullParser.getName()).append('>');
        continue;
      }
      catch (IOException paramXmlPullParser)
      {
        paramXmlPullParser.printStackTrace();
        continue;
        localStringBuilder.append("</").append(paramXmlPullParser.getName()).append('>');
        continue;
        int i = paramXmlPullParser.next();
        switch (i)
        {
        }
        continue;
      }
      if (i != 3) {
        continue;
      }
      bool = "vCard".equals(paramXmlPullParser.getName());
      if (!bool) {
        continue;
      }
      return createVCardFromXML(localStringBuilder.toString());
      localStringBuilder.append(StringUtils.escapeForXML(paramXmlPullParser.getText()));
    }
  }
  
  private static class VCardReader
  {
    private final Document document;
    private final VCard vCard;
    
    VCardReader(VCard paramVCard, Document paramDocument)
    {
      this.vCard = paramVCard;
      this.document = paramDocument;
    }
    
    private void appendText(StringBuilder paramStringBuilder, Node paramNode)
    {
      paramNode = paramNode.getChildNodes();
      int i = 0;
      while (i < paramNode.getLength())
      {
        Node localNode = paramNode.item(i);
        String str = localNode.getNodeValue();
        if (str != null) {
          paramStringBuilder.append(str);
        }
        appendText(paramStringBuilder, localNode);
        i += 1;
      }
    }
    
    private String getTagContents(String paramString)
    {
      paramString = this.document.getElementsByTagName(paramString);
      if ((paramString != null) && (paramString.getLength() == 1)) {
        return getTextContent(paramString.item(0));
      }
      return null;
    }
    
    private String getTextContent(Node paramNode)
    {
      StringBuilder localStringBuilder = new StringBuilder();
      appendText(localStringBuilder, paramNode);
      return localStringBuilder.toString();
    }
    
    private boolean isWorkHome(String paramString)
    {
      return ("HOME".equals(paramString)) || ("WORK".equals(paramString));
    }
    
    private void setupAddresses()
    {
      NodeList localNodeList1 = this.document.getElementsByTagName("ADR");
      if (localNodeList1 == null) {}
      for (;;)
      {
        return;
        int i = 0;
        while (i < localNodeList1.getLength())
        {
          Object localObject2 = (Element)localNodeList1.item(i);
          Object localObject1 = null;
          ArrayList localArrayList1 = new ArrayList();
          ArrayList localArrayList2 = new ArrayList();
          NodeList localNodeList2 = ((Element)localObject2).getChildNodes();
          int j = 0;
          if (j < localNodeList2.getLength())
          {
            Node localNode = localNodeList2.item(j);
            if (localNode.getNodeType() != 1) {}
            for (;;)
            {
              j += 1;
              break;
              localObject2 = localNode.getNodeName();
              if (isWorkHome((String)localObject2))
              {
                localObject1 = localObject2;
              }
              else
              {
                localArrayList1.add(localObject2);
                localArrayList2.add(getTextContent(localNode));
              }
            }
          }
          j = 0;
          if (j < localArrayList2.size())
          {
            if ("HOME".equals(localObject1)) {
              this.vCard.setAddressFieldHome((String)localArrayList1.get(j), (String)localArrayList2.get(j));
            }
            for (;;)
            {
              j += 1;
              break;
              this.vCard.setAddressFieldWork((String)localArrayList1.get(j), (String)localArrayList2.get(j));
            }
          }
          i += 1;
        }
      }
    }
    
    private void setupEmails()
    {
      NodeList localNodeList = this.document.getElementsByTagName("USERID");
      if (localNodeList == null) {
        return;
      }
      int i = 0;
      label19:
      Element localElement;
      if (i < localNodeList.getLength())
      {
        localElement = (Element)localNodeList.item(i);
        if (!"WORK".equals(localElement.getParentNode().getFirstChild().getNodeName())) {
          break label83;
        }
        this.vCard.setEmailWork(getTextContent(localElement));
      }
      for (;;)
      {
        i += 1;
        break label19;
        break;
        label83:
        this.vCard.setEmailHome(getTextContent(localElement));
      }
    }
    
    private void setupPhones()
    {
      NodeList localNodeList1 = this.document.getElementsByTagName("TEL");
      if (localNodeList1 == null) {
        return;
      }
      int i = 0;
      label21:
      Object localObject1;
      Object localObject2;
      String str1;
      if (i < localNodeList1.getLength())
      {
        NodeList localNodeList2 = localNodeList1.item(i).getChildNodes();
        localObject1 = null;
        localObject2 = null;
        str1 = null;
        int j = 0;
        if (j < localNodeList2.getLength())
        {
          Node localNode = localNodeList2.item(j);
          if (localNode.getNodeType() != 1) {}
          for (;;)
          {
            j += 1;
            break;
            String str2 = localNode.getNodeName();
            if ("NUMBER".equals(str2)) {
              str1 = getTextContent(localNode);
            } else if (isWorkHome(str2)) {
              localObject1 = str2;
            } else {
              localObject2 = str2;
            }
          }
        }
        if ((localObject2 != null) && (str1 != null)) {
          break label164;
        }
      }
      for (;;)
      {
        i += 1;
        break label21;
        break;
        label164:
        if ("HOME".equals(localObject1)) {
          this.vCard.setPhoneHome(localObject2, str1);
        } else {
          this.vCard.setPhoneWork(localObject2, str1);
        }
      }
    }
    
    private void setupSimpleFields()
    {
      NodeList localNodeList = this.document.getDocumentElement().getChildNodes();
      int i = 0;
      if (i < localNodeList.getLength())
      {
        Object localObject = localNodeList.item(i);
        String str;
        if ((localObject instanceof Element))
        {
          localObject = (Element)localObject;
          str = ((Element)localObject).getNodeName();
          if (((Element)localObject).getChildNodes().getLength() != 0) {
            break label87;
          }
          this.vCard.setField(str, "");
        }
        for (;;)
        {
          i += 1;
          break;
          label87:
          if ((((Element)localObject).getChildNodes().getLength() == 1) && ((((Element)localObject).getChildNodes().item(0) instanceof Text))) {
            this.vCard.setField(str, getTextContent((Node)localObject));
          }
        }
      }
    }
    
    public void initializeFields()
    {
      this.vCard.setFirstName(getTagContents("GIVEN"));
      this.vCard.setLastName(getTagContents("FAMILY"));
      this.vCard.setMiddleName(getTagContents("MIDDLE"));
      this.vCard.setEncodedImage(getTagContents("BINVAL"));
      setupEmails();
      this.vCard.setOrganization(getTagContents("ORGNAME"));
      this.vCard.setOrganizationUnit(getTagContents("ORGUNIT"));
      setupSimpleFields();
      setupPhones();
      setupAddresses();
    }
  }
}


/* Location:           C:\Users\Administrator\Documents\ds\反编译\AndroidKiller_v1.3.1\projects\Jungledsq_ppc\ProjectSrc\smali\
 * Qualified Name:     org.jivesoftware.smackx.provider.VCardProvider
 * JD-Core Version:    0.7.0.1
 */