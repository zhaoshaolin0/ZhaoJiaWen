package org.jivesoftware.smackx.packet;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import org.jivesoftware.smack.PacketCollector;
import org.jivesoftware.smack.SmackConfiguration;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.filter.PacketIDFilter;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.packet.IQ.Type;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.packet.XMPPError;
import org.jivesoftware.smack.packet.XMPPError.Condition;
import org.jivesoftware.smack.util.StringUtils;

public class VCard
  extends IQ
{
  private String avatar;
  private String emailHome;
  private String emailWork;
  private String firstName;
  private Map<String, String> homeAddr = new HashMap();
  private Map<String, String> homePhones = new HashMap();
  private String lastName;
  private String middleName;
  private String organization;
  private String organizationUnit;
  private Map<String, String> otherSimpleFields = new HashMap();
  private Map<String, String> otherUnescapableFields = new HashMap();
  private Map<String, String> workAddr = new HashMap();
  private Map<String, String> workPhones = new HashMap();
  
  private void checkAuthenticated(XMPPConnection paramXMPPConnection, boolean paramBoolean)
  {
    if (paramXMPPConnection == null) {
      throw new IllegalArgumentException("No connection was provided");
    }
    if (!paramXMPPConnection.isAuthenticated()) {
      throw new IllegalArgumentException("Connection is not authenticated");
    }
    if ((paramBoolean) && (paramXMPPConnection.isAnonymous())) {
      throw new IllegalArgumentException("Connection cannot be anonymous");
    }
  }
  
  private void copyFieldsFrom(VCard paramVCard)
  {
    VCard localVCard = paramVCard;
    if (paramVCard == null) {
      localVCard = new VCard();
    }
    Field[] arrayOfField = VCard.class.getDeclaredFields();
    int j = arrayOfField.length;
    int i = 0;
    while (i < j)
    {
      paramVCard = arrayOfField[i];
      if ((paramVCard.getDeclaringClass() == VCard.class) && (!Modifier.isFinal(paramVCard.getModifiers()))) {}
      try
      {
        paramVCard.setAccessible(true);
        paramVCard.set(this, paramVCard.get(localVCard));
        i += 1;
      }
      catch (IllegalAccessException localIllegalAccessException)
      {
        throw new RuntimeException("This cannot happen:" + paramVCard, localIllegalAccessException);
      }
    }
  }
  
  private void doLoad(XMPPConnection paramXMPPConnection, String paramString)
    throws XMPPException
  {
    setType(IQ.Type.GET);
    Object localObject1 = paramXMPPConnection.createPacketCollector(new PacketIDFilter(getPacketID()));
    paramXMPPConnection.sendPacket(this);
    paramXMPPConnection = null;
    do
    {
      try
      {
        localObject1 = (VCard)((PacketCollector)localObject1).nextResult(SmackConfiguration.getPacketReplyTimeout());
        if (localObject1 == null)
        {
          paramXMPPConnection = (XMPPConnection)localObject1;
          throw new XMPPException("Timeout getting VCard information", new XMPPError(XMPPError.Condition.request_timeout, "Timeout getting VCard information"));
        }
      }
      catch (ClassCastException localClassCastException)
      {
        System.out.println("No VCard for " + paramString);
        localObject2 = paramXMPPConnection;
        copyFieldsFrom((VCard)localObject2);
        return;
      }
      paramXMPPConnection = localClassCastException;
      Object localObject2 = localClassCastException;
    } while (localClassCastException.getError() == null);
    paramXMPPConnection = localClassCastException;
    throw new XMPPException(localClassCastException.getError());
  }
  
  public static byte[] getBytes(URL paramURL)
    throws IOException
  {
    paramURL = new File(paramURL.getPath());
    if (paramURL.exists()) {
      return getFileBytes(paramURL);
    }
    return null;
  }
  
  private static byte[] getFileBytes(File paramFile)
    throws IOException
  {
    Object localObject3 = null;
    try
    {
      localObject1 = new BufferedInputStream(new FileInputStream(paramFile));
      try
      {
        paramFile = new byte[(int)paramFile.length()];
        if (((BufferedInputStream)localObject1).read(paramFile) == paramFile.length) {
          break label62;
        }
        throw new IOException("Entire file not read");
      }
      finally
      {
        paramFile = (File)localObject1;
      }
    }
    finally
    {
      for (;;)
      {
        Object localObject1;
        paramFile = localObject4;
      }
    }
    if (paramFile != null) {
      paramFile.close();
    }
    throw ((Throwable)localObject1);
    label62:
    if (localObject1 != null) {
      ((BufferedInputStream)localObject1).close();
    }
    return paramFile;
  }
  
  private boolean hasContent()
  {
    return (hasNameField()) || (hasOrganizationFields()) || (this.emailHome != null) || (this.emailWork != null) || (this.otherSimpleFields.size() > 0) || (this.otherUnescapableFields.size() > 0) || (this.homeAddr.size() > 0) || (this.homePhones.size() > 0) || (this.workAddr.size() > 0) || (this.workPhones.size() > 0);
  }
  
  private boolean hasNameField()
  {
    return (this.firstName != null) || (this.lastName != null) || (this.middleName != null);
  }
  
  private boolean hasOrganizationFields()
  {
    return (this.organization != null) || (this.organizationUnit != null);
  }
  
  private void updateFN()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    if (this.firstName != null) {
      localStringBuilder.append(StringUtils.escapeForXML(this.firstName)).append(' ');
    }
    if (this.middleName != null) {
      localStringBuilder.append(StringUtils.escapeForXML(this.middleName)).append(' ');
    }
    if (this.lastName != null) {
      localStringBuilder.append(StringUtils.escapeForXML(this.lastName));
    }
    setField("FN", localStringBuilder.toString());
  }
  
  public boolean equals(Object paramObject)
  {
    if (this == paramObject) {
      return true;
    }
    if ((paramObject == null) || (getClass() != paramObject.getClass())) {
      return false;
    }
    paramObject = (VCard)paramObject;
    if (this.emailHome != null)
    {
      if (this.emailHome.equals(paramObject.emailHome)) {}
    }
    else {
      while (paramObject.emailHome != null) {
        return false;
      }
    }
    if (this.emailWork != null)
    {
      if (this.emailWork.equals(paramObject.emailWork)) {}
    }
    else {
      while (paramObject.emailWork != null) {
        return false;
      }
    }
    if (this.firstName != null)
    {
      if (this.firstName.equals(paramObject.firstName)) {}
    }
    else {
      while (paramObject.firstName != null) {
        return false;
      }
    }
    if (!this.homeAddr.equals(paramObject.homeAddr)) {
      return false;
    }
    if (!this.homePhones.equals(paramObject.homePhones)) {
      return false;
    }
    if (this.lastName != null)
    {
      if (this.lastName.equals(paramObject.lastName)) {}
    }
    else {
      while (paramObject.lastName != null) {
        return false;
      }
    }
    if (this.middleName != null)
    {
      if (this.middleName.equals(paramObject.middleName)) {}
    }
    else {
      while (paramObject.middleName != null) {
        return false;
      }
    }
    if (this.organization != null)
    {
      if (this.organization.equals(paramObject.organization)) {}
    }
    else {
      while (paramObject.organization != null) {
        return false;
      }
    }
    if (this.organizationUnit != null)
    {
      if (this.organizationUnit.equals(paramObject.organizationUnit)) {}
    }
    else {
      while (paramObject.organizationUnit != null) {
        return false;
      }
    }
    if (!this.otherSimpleFields.equals(paramObject.otherSimpleFields)) {
      return false;
    }
    if (!this.workAddr.equals(paramObject.workAddr)) {
      return false;
    }
    return this.workPhones.equals(paramObject.workPhones);
  }
  
  public String getAddressFieldHome(String paramString)
  {
    return (String)this.homeAddr.get(paramString);
  }
  
  public String getAddressFieldWork(String paramString)
  {
    return (String)this.workAddr.get(paramString);
  }
  
  public byte[] getAvatar()
  {
    if (this.avatar == null) {
      return null;
    }
    return StringUtils.decodeBase64(this.avatar);
  }
  
  public String getAvatarHash()
  {
    byte[] arrayOfByte = getAvatar();
    if (arrayOfByte == null) {
      return null;
    }
    try
    {
      MessageDigest localMessageDigest = MessageDigest.getInstance("SHA-1");
      localMessageDigest.update(arrayOfByte);
      return StringUtils.encodeHex(localMessageDigest.digest());
    }
    catch (NoSuchAlgorithmException localNoSuchAlgorithmException)
    {
      localNoSuchAlgorithmException.printStackTrace();
    }
    return null;
  }
  
  public String getChildElementXML()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    new VCardWriter(localStringBuilder).write();
    return localStringBuilder.toString();
  }
  
  public String getEmailHome()
  {
    return this.emailHome;
  }
  
  public String getEmailWork()
  {
    return this.emailWork;
  }
  
  public String getField(String paramString)
  {
    return (String)this.otherSimpleFields.get(paramString);
  }
  
  public String getFirstName()
  {
    return this.firstName;
  }
  
  public String getJabberId()
  {
    return (String)this.otherSimpleFields.get("JABBERID");
  }
  
  public String getLastName()
  {
    return this.lastName;
  }
  
  public String getMiddleName()
  {
    return this.middleName;
  }
  
  public String getNickName()
  {
    return (String)this.otherSimpleFields.get("NICKNAME");
  }
  
  public String getOrganization()
  {
    return this.organization;
  }
  
  public String getOrganizationUnit()
  {
    return this.organizationUnit;
  }
  
  public String getPhoneHome(String paramString)
  {
    return (String)this.homePhones.get(paramString);
  }
  
  public String getPhoneWork(String paramString)
  {
    return (String)this.workPhones.get(paramString);
  }
  
  public int hashCode()
  {
    int i3 = this.homePhones.hashCode();
    int i4 = this.workPhones.hashCode();
    int i5 = this.homeAddr.hashCode();
    int i6 = this.workAddr.hashCode();
    int i;
    int j;
    label66:
    int k;
    label81:
    int m;
    label97:
    int n;
    label113:
    int i1;
    if (this.firstName != null)
    {
      i = this.firstName.hashCode();
      if (this.lastName == null) {
        break label221;
      }
      j = this.lastName.hashCode();
      if (this.middleName == null) {
        break label226;
      }
      k = this.middleName.hashCode();
      if (this.emailHome == null) {
        break label231;
      }
      m = this.emailHome.hashCode();
      if (this.emailWork == null) {
        break label237;
      }
      n = this.emailWork.hashCode();
      if (this.organization == null) {
        break label243;
      }
      i1 = this.organization.hashCode();
      label129:
      if (this.organizationUnit == null) {
        break label249;
      }
    }
    label221:
    label226:
    label231:
    label237:
    label243:
    label249:
    for (int i2 = this.organizationUnit.hashCode();; i2 = 0)
    {
      return ((((((((((i3 * 29 + i4) * 29 + i5) * 29 + i6) * 29 + i) * 29 + j) * 29 + k) * 29 + m) * 29 + n) * 29 + i1) * 29 + i2) * 29 + this.otherSimpleFields.hashCode();
      i = 0;
      break;
      j = 0;
      break label66;
      k = 0;
      break label81;
      m = 0;
      break label97;
      n = 0;
      break label113;
      i1 = 0;
      break label129;
    }
  }
  
  public void load(XMPPConnection paramXMPPConnection)
    throws XMPPException
  {
    checkAuthenticated(paramXMPPConnection, true);
    setFrom(paramXMPPConnection.getUser());
    doLoad(paramXMPPConnection, paramXMPPConnection.getUser());
  }
  
  public void load(XMPPConnection paramXMPPConnection, String paramString)
    throws XMPPException
  {
    checkAuthenticated(paramXMPPConnection, false);
    setTo(paramString);
    doLoad(paramXMPPConnection, paramString);
  }
  
  public void save(XMPPConnection paramXMPPConnection)
    throws XMPPException
  {
    checkAuthenticated(paramXMPPConnection, true);
    setType(IQ.Type.SET);
    setFrom(paramXMPPConnection.getUser());
    PacketCollector localPacketCollector = paramXMPPConnection.createPacketCollector(new PacketIDFilter(getPacketID()));
    paramXMPPConnection.sendPacket(this);
    paramXMPPConnection = localPacketCollector.nextResult(SmackConfiguration.getPacketReplyTimeout());
    localPacketCollector.cancel();
    if (paramXMPPConnection == null) {
      throw new XMPPException("No response from server on status set.");
    }
    if (paramXMPPConnection.getError() != null) {
      throw new XMPPException(paramXMPPConnection.getError());
    }
  }
  
  public void setAddressFieldHome(String paramString1, String paramString2)
  {
    this.homeAddr.put(paramString1, paramString2);
  }
  
  public void setAddressFieldWork(String paramString1, String paramString2)
  {
    this.workAddr.put(paramString1, paramString2);
  }
  
  public void setAvatar(URL paramURL)
  {
    byte[] arrayOfByte = new byte[0];
    try
    {
      paramURL = getBytes(paramURL);
      setAvatar(paramURL);
      return;
    }
    catch (IOException paramURL)
    {
      for (;;)
      {
        paramURL.printStackTrace();
        paramURL = arrayOfByte;
      }
    }
  }
  
  public void setAvatar(byte[] paramArrayOfByte)
  {
    if (paramArrayOfByte == null)
    {
      this.otherUnescapableFields.remove("PHOTO");
      return;
    }
    paramArrayOfByte = StringUtils.encodeBase64(paramArrayOfByte);
    this.avatar = paramArrayOfByte;
    setField("PHOTO", "<TYPE>image/jpeg</TYPE><BINVAL>" + paramArrayOfByte + "</BINVAL>", true);
  }
  
  public void setAvatar(byte[] paramArrayOfByte, String paramString)
  {
    if (paramArrayOfByte == null)
    {
      this.otherUnescapableFields.remove("PHOTO");
      return;
    }
    paramArrayOfByte = StringUtils.encodeBase64(paramArrayOfByte);
    this.avatar = paramArrayOfByte;
    setField("PHOTO", "<TYPE>" + paramString + "</TYPE><BINVAL>" + paramArrayOfByte + "</BINVAL>", true);
  }
  
  public void setEmailHome(String paramString)
  {
    this.emailHome = paramString;
  }
  
  public void setEmailWork(String paramString)
  {
    this.emailWork = paramString;
  }
  
  public void setEncodedImage(String paramString)
  {
    this.avatar = paramString;
  }
  
  public void setField(String paramString1, String paramString2)
  {
    setField(paramString1, paramString2, false);
  }
  
  public void setField(String paramString1, String paramString2, boolean paramBoolean)
  {
    if (!paramBoolean)
    {
      this.otherSimpleFields.put(paramString1, paramString2);
      return;
    }
    this.otherUnescapableFields.put(paramString1, paramString2);
  }
  
  public void setFirstName(String paramString)
  {
    this.firstName = paramString;
    updateFN();
  }
  
  public void setJabberId(String paramString)
  {
    this.otherSimpleFields.put("JABBERID", paramString);
  }
  
  public void setLastName(String paramString)
  {
    this.lastName = paramString;
    updateFN();
  }
  
  public void setMiddleName(String paramString)
  {
    this.middleName = paramString;
    updateFN();
  }
  
  public void setNickName(String paramString)
  {
    this.otherSimpleFields.put("NICKNAME", paramString);
  }
  
  public void setOrganization(String paramString)
  {
    this.organization = paramString;
  }
  
  public void setOrganizationUnit(String paramString)
  {
    this.organizationUnit = paramString;
  }
  
  public void setPhoneHome(String paramString1, String paramString2)
  {
    this.homePhones.put(paramString1, paramString2);
  }
  
  public void setPhoneWork(String paramString1, String paramString2)
  {
    this.workPhones.put(paramString1, paramString2);
  }
  
  public String toString()
  {
    return getChildElementXML();
  }
  
  private static abstract interface ContentBuilder
  {
    public abstract void addTagContent();
  }
  
  private class VCardWriter
  {
    private final StringBuilder sb;
    
    VCardWriter(StringBuilder paramStringBuilder)
    {
      this.sb = paramStringBuilder;
    }
    
    private void appendAddress(final Map<String, String> paramMap, final String paramString)
    {
      if (paramMap.size() > 0) {
        appendTag("ADR", true, new VCard.ContentBuilder()
        {
          public void addTagContent()
          {
            VCard.VCardWriter.this.appendEmptyTag(paramString);
            Iterator localIterator = paramMap.entrySet().iterator();
            while (localIterator.hasNext())
            {
              Map.Entry localEntry = (Map.Entry)localIterator.next();
              VCard.VCardWriter.this.appendTag((String)localEntry.getKey(), StringUtils.escapeForXML((String)localEntry.getValue()));
            }
          }
        });
      }
    }
    
    private void appendEmail(final String paramString1, final String paramString2)
    {
      if (paramString1 != null) {
        appendTag("EMAIL", true, new VCard.ContentBuilder()
        {
          public void addTagContent()
          {
            VCard.VCardWriter.this.appendEmptyTag(paramString2);
            VCard.VCardWriter.this.appendEmptyTag("INTERNET");
            VCard.VCardWriter.this.appendEmptyTag("PREF");
            VCard.VCardWriter.this.appendTag("USERID", StringUtils.escapeForXML(paramString1));
          }
        });
      }
    }
    
    private void appendEmptyTag(Object paramObject)
    {
      this.sb.append('<').append(paramObject).append("/>");
    }
    
    private void appendGenericFields()
    {
      Iterator localIterator = VCard.this.otherSimpleFields.entrySet().iterator();
      Map.Entry localEntry;
      while (localIterator.hasNext())
      {
        localEntry = (Map.Entry)localIterator.next();
        appendTag(localEntry.getKey().toString(), StringUtils.escapeForXML((String)localEntry.getValue()));
      }
      localIterator = VCard.this.otherUnescapableFields.entrySet().iterator();
      while (localIterator.hasNext())
      {
        localEntry = (Map.Entry)localIterator.next();
        appendTag(localEntry.getKey().toString(), (String)localEntry.getValue());
      }
    }
    
    private void appendN()
    {
      appendTag("N", true, new VCard.ContentBuilder()
      {
        public void addTagContent()
        {
          VCard.VCardWriter.this.appendTag("FAMILY", StringUtils.escapeForXML(VCard.this.lastName));
          VCard.VCardWriter.this.appendTag("GIVEN", StringUtils.escapeForXML(VCard.this.firstName));
          VCard.VCardWriter.this.appendTag("MIDDLE", StringUtils.escapeForXML(VCard.this.middleName));
        }
      });
    }
    
    private void appendOrganization()
    {
      if (VCard.this.hasOrganizationFields()) {
        appendTag("ORG", true, new VCard.ContentBuilder()
        {
          public void addTagContent()
          {
            VCard.VCardWriter.this.appendTag("ORGNAME", StringUtils.escapeForXML(VCard.this.organization));
            VCard.VCardWriter.this.appendTag("ORGUNIT", StringUtils.escapeForXML(VCard.this.organizationUnit));
          }
        });
      }
    }
    
    private void appendPhones(Map<String, String> paramMap, final String paramString)
    {
      paramMap = paramMap.entrySet().iterator();
      while (paramMap.hasNext()) {
        appendTag("TEL", true, new VCard.ContentBuilder()
        {
          public void addTagContent()
          {
            VCard.VCardWriter.this.appendEmptyTag(this.val$entry.getKey());
            VCard.VCardWriter.this.appendEmptyTag(paramString);
            VCard.VCardWriter.this.appendTag("NUMBER", StringUtils.escapeForXML((String)this.val$entry.getValue()));
          }
        });
      }
    }
    
    private void appendTag(String paramString1, final String paramString2)
    {
      if (paramString2 == null) {
        return;
      }
      appendTag(paramString1, true, new VCard.ContentBuilder()
      {
        public void addTagContent()
        {
          VCard.VCardWriter.this.sb.append(paramString2.trim());
        }
      });
    }
    
    private void appendTag(String paramString1, String paramString2, String paramString3, boolean paramBoolean, VCard.ContentBuilder paramContentBuilder)
    {
      this.sb.append('<').append(paramString1);
      if (paramString2 != null) {
        this.sb.append(' ').append(paramString2).append('=').append('\'').append(paramString3).append('\'');
      }
      if (paramBoolean)
      {
        this.sb.append('>');
        paramContentBuilder.addTagContent();
        this.sb.append("</").append(paramString1).append(">\n");
        return;
      }
      this.sb.append("/>\n");
    }
    
    private void appendTag(String paramString, boolean paramBoolean, VCard.ContentBuilder paramContentBuilder)
    {
      appendTag(paramString, null, null, paramBoolean, paramContentBuilder);
    }
    
    private void buildActualContent()
    {
      if (VCard.this.hasNameField()) {
        appendN();
      }
      appendOrganization();
      appendGenericFields();
      appendEmail(VCard.this.emailWork, "WORK");
      appendEmail(VCard.this.emailHome, "HOME");
      appendPhones(VCard.this.workPhones, "WORK");
      appendPhones(VCard.this.homePhones, "HOME");
      appendAddress(VCard.this.workAddr, "WORK");
      appendAddress(VCard.this.homeAddr, "HOME");
    }
    
    public void write()
    {
      appendTag("vCard", "xmlns", "vcard-temp", VCard.this.hasContent(), new VCard.ContentBuilder()
      {
        public void addTagContent()
        {
          VCard.VCardWriter.this.buildActualContent();
        }
      });
    }
  }
}


/* Location:           C:\Users\Administrator\Documents\ds\反编译\AndroidKiller_v1.3.1\projects\Jungledsq_ppc\ProjectSrc\smali\
 * Qualified Name:     org.jivesoftware.smackx.packet.VCard
 * JD-Core Version:    0.7.0.1
 */