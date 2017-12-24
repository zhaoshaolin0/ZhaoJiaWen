package org.xmlpull.mxp1;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

public class MXParser
  implements XmlPullParser
{
  protected static final String FEATURE_NAMES_INTERNED = "http://xmlpull.org/v1/doc/features.html#names-interned";
  protected static final String FEATURE_XML_ROUNDTRIP = "http://xmlpull.org/v1/doc/features.html#xml-roundtrip";
  protected static final int LOOKUP_MAX = 1024;
  protected static final char LOOKUP_MAX_CHAR = 'Ѐ';
  protected static final char[] NCODING;
  protected static final char[] NO;
  protected static final String PROPERTY_LOCATION = "http://xmlpull.org/v1/doc/properties.html#location";
  protected static final String PROPERTY_XMLDECL_CONTENT = "http://xmlpull.org/v1/doc/properties.html#xmldecl-content";
  protected static final String PROPERTY_XMLDECL_STANDALONE = "http://xmlpull.org/v1/doc/properties.html#xmldecl-standalone";
  protected static final String PROPERTY_XMLDECL_VERSION = "http://xmlpull.org/v1/doc/properties.html#xmldecl-version";
  protected static final int READ_CHUNK_SIZE = 8192;
  protected static final char[] TANDALONE;
  private static final boolean TRACE_SIZING = false;
  protected static final char[] VERSION = "version".toCharArray();
  protected static final String XMLNS_URI = "http://www.w3.org/2000/xmlns/";
  protected static final String XML_URI = "http://www.w3.org/XML/1998/namespace";
  protected static final char[] YES;
  protected static boolean[] lookupNameChar;
  protected static boolean[] lookupNameStartChar;
  protected boolean allStringsInterned;
  protected int attributeCount;
  protected String[] attributeName;
  protected int[] attributeNameHash;
  protected String[] attributePrefix;
  protected String[] attributeUri;
  protected String[] attributeValue;
  protected char[] buf;
  protected int bufAbsoluteStart;
  protected int bufEnd;
  protected int bufLoadFactor = 95;
  protected int bufSoftLimit;
  protected int bufStart;
  protected char[] charRefOneCharBuf;
  protected int columnNumber;
  protected int depth;
  protected String[] elName;
  protected int[] elNamespaceCount;
  protected String[] elPrefix;
  protected char[][] elRawName;
  protected int[] elRawNameEnd;
  protected int[] elRawNameLine;
  protected String[] elUri;
  protected boolean emptyElementTag;
  protected int entityEnd;
  protected String[] entityName;
  protected char[][] entityNameBuf;
  protected int[] entityNameHash;
  protected String entityRefName;
  protected String[] entityReplacement;
  protected char[][] entityReplacementBuf;
  protected int eventType;
  protected String inputEncoding;
  protected InputStream inputStream;
  protected int lineNumber;
  protected String location;
  protected int namespaceEnd;
  protected String[] namespacePrefix;
  protected int[] namespacePrefixHash;
  protected String[] namespaceUri;
  protected boolean pastEndTag;
  protected char[] pc;
  protected int pcEnd;
  protected int pcStart;
  protected int pos;
  protected int posEnd;
  protected int posStart;
  protected boolean preventBufferCompaction;
  protected boolean processNamespaces;
  protected boolean reachedEnd;
  protected Reader reader;
  protected boolean roundtripSupported;
  protected boolean seenAmpersand;
  protected boolean seenDocdecl;
  protected boolean seenEndTag;
  protected boolean seenMarkup;
  protected boolean seenRoot;
  protected boolean seenStartTag;
  protected String text;
  protected boolean tokenize;
  protected boolean usePC;
  protected String xmlDeclContent;
  protected Boolean xmlDeclStandalone;
  protected String xmlDeclVersion;
  
  static
  {
    NCODING = "ncoding".toCharArray();
    TANDALONE = "tandalone".toCharArray();
    YES = "yes".toCharArray();
    NO = "no".toCharArray();
    lookupNameStartChar = new boolean[1024];
    lookupNameChar = new boolean[1024];
    setNameStart(':');
    for (int i = 65; i <= 90; i = (char)(i + 1)) {
      setNameStart(i);
    }
    setNameStart('_');
    for (i = 97; i <= 122; i = (char)(i + 1)) {
      setNameStart(i);
    }
    for (i = 192; i <= 767; j = (char)(i + 1)) {
      setNameStart(i);
    }
    for (int j = 880; j <= 893; k = (char)(j + 1)) {
      setNameStart(j);
    }
    for (int k = 895; k < 1024; c = (char)(k + 1)) {
      setNameStart(k);
    }
    setName('-');
    setName('.');
    for (char c = '0'; c <= '9'; c = (char)(c + '\001')) {
      setName(c);
    }
    setName('·');
    int n;
    for (int m = 768; m <= 879; n = (char)(m + 1)) {
      setName(m);
    }
  }
  
  public MXParser()
  {
    if (Runtime.getRuntime().freeMemory() > 1000000L)
    {
      i = 8192;
      this.buf = new char[i];
      this.bufSoftLimit = (this.bufLoadFactor * this.buf.length / 100);
      if (Runtime.getRuntime().freeMemory() <= 1000000L) {
        break label90;
      }
    }
    label90:
    for (int i = 8192;; i = 64)
    {
      this.pc = new char[i];
      this.charRefOneCharBuf = new char[1];
      return;
      i = 256;
      break;
    }
  }
  
  protected static final int fastHash(char[] paramArrayOfChar, int paramInt1, int paramInt2)
  {
    if (paramInt2 == 0) {
      return 0;
    }
    int j = (paramArrayOfChar[paramInt1] << '\007') + paramArrayOfChar[(paramInt1 + paramInt2 - 1)];
    int i = j;
    if (paramInt2 > 16) {
      i = (j << 7) + paramArrayOfChar[(paramInt2 / 4 + paramInt1)];
    }
    j = i;
    if (paramInt2 > 8) {
      j = (i << 7) + paramArrayOfChar[(paramInt2 / 2 + paramInt1)];
    }
    return j;
  }
  
  private static int findFragment(int paramInt1, char[] paramArrayOfChar, int paramInt2, int paramInt3)
  {
    if (paramInt2 < paramInt1)
    {
      paramInt2 = paramInt1;
      if (paramInt1 > paramInt3) {
        paramInt2 = paramInt3;
      }
      return paramInt2;
    }
    int i = paramInt2;
    if (paramInt3 - paramInt2 > 65) {
      i = paramInt3 - 10;
    }
    paramInt2 = i + 1;
    int j = paramInt2 - 1;
    if ((j <= paramInt1) || (paramInt3 - j > 65)) {}
    for (;;)
    {
      return j;
      paramInt2 = j;
      if (paramArrayOfChar[j] != '<') {
        break;
      }
      paramInt2 = j;
      if (i - j <= 10) {
        break;
      }
    }
  }
  
  private static final void setName(char paramChar)
  {
    lookupNameChar[paramChar] = true;
  }
  
  private static final void setNameStart(char paramChar)
  {
    lookupNameStartChar[paramChar] = true;
    setName(paramChar);
  }
  
  public void defineEntityReplacementText(String paramString1, String paramString2)
    throws XmlPullParserException
  {
    ensureEntityCapacity();
    this.entityName[this.entityEnd] = newString(paramString1.toCharArray(), 0, paramString1.length());
    this.entityNameBuf[this.entityEnd] = paramString1.toCharArray();
    this.entityReplacement[this.entityEnd] = paramString2;
    this.entityReplacementBuf[this.entityEnd] = paramString2.toCharArray();
    if (!this.allStringsInterned) {
      this.entityNameHash[this.entityEnd] = fastHash(this.entityNameBuf[this.entityEnd], 0, this.entityNameBuf[this.entityEnd].length);
    }
    this.entityEnd += 1;
  }
  
  protected void ensureAttributesCapacity(int paramInt)
  {
    int i;
    if (this.attributeName != null)
    {
      i = this.attributeName.length;
      if (paramInt >= i)
      {
        if (paramInt <= 7) {
          break label186;
        }
        paramInt *= 2;
        label28:
        if (i <= 0) {
          break label192;
        }
      }
    }
    label186:
    label192:
    for (int j = 1;; j = 0)
    {
      Object localObject = new String[paramInt];
      if (j != 0) {
        System.arraycopy(this.attributeName, 0, localObject, 0, i);
      }
      this.attributeName = ((String[])localObject);
      localObject = new String[paramInt];
      if (j != 0) {
        System.arraycopy(this.attributePrefix, 0, localObject, 0, i);
      }
      this.attributePrefix = ((String[])localObject);
      localObject = new String[paramInt];
      if (j != 0) {
        System.arraycopy(this.attributeUri, 0, localObject, 0, i);
      }
      this.attributeUri = ((String[])localObject);
      localObject = new String[paramInt];
      if (j != 0) {
        System.arraycopy(this.attributeValue, 0, localObject, 0, i);
      }
      this.attributeValue = ((String[])localObject);
      if (!this.allStringsInterned)
      {
        localObject = new int[paramInt];
        if (j != 0) {
          System.arraycopy(this.attributeNameHash, 0, localObject, 0, i);
        }
        this.attributeNameHash = ((int[])localObject);
      }
      return;
      i = 0;
      break;
      paramInt = 8;
      break label28;
    }
  }
  
  protected void ensureElementsCapacity()
  {
    int i;
    int j;
    label39:
    int k;
    label49:
    Object localObject;
    if (this.elName != null)
    {
      i = this.elName.length;
      if (this.depth + 1 >= i)
      {
        if (this.depth < 7) {
          break label248;
        }
        j = this.depth * 2;
        k = j + 2;
        if (i <= 0) {
          break label254;
        }
        j = 1;
        localObject = new String[k];
        if (j != 0) {
          System.arraycopy(this.elName, 0, localObject, 0, i);
        }
        this.elName = ((String[])localObject);
        localObject = new String[k];
        if (j != 0) {
          System.arraycopy(this.elPrefix, 0, localObject, 0, i);
        }
        this.elPrefix = ((String[])localObject);
        localObject = new String[k];
        if (j != 0) {
          System.arraycopy(this.elUri, 0, localObject, 0, i);
        }
        this.elUri = ((String[])localObject);
        localObject = new int[k];
        if (j == 0) {
          break label259;
        }
        System.arraycopy(this.elNamespaceCount, 0, localObject, 0, i);
      }
    }
    for (;;)
    {
      this.elNamespaceCount = ((int[])localObject);
      localObject = new int[k];
      if (j != 0) {
        System.arraycopy(this.elRawNameEnd, 0, localObject, 0, i);
      }
      this.elRawNameEnd = ((int[])localObject);
      localObject = new int[k];
      if (j != 0) {
        System.arraycopy(this.elRawNameLine, 0, localObject, 0, i);
      }
      this.elRawNameLine = ((int[])localObject);
      localObject = new char[k][];
      if (j != 0) {
        System.arraycopy(this.elRawName, 0, localObject, 0, i);
      }
      this.elRawName = ((char[][])localObject);
      return;
      i = 0;
      break;
      label248:
      j = 8;
      break label39;
      label254:
      j = 0;
      break label49;
      label259:
      localObject[0] = 0;
    }
  }
  
  protected void ensureEntityCapacity()
  {
    if (this.entityReplacementBuf != null)
    {
      i = this.entityReplacementBuf.length;
      if (this.entityEnd >= i) {
        if (this.entityEnd <= 7) {
          break label189;
        }
      }
    }
    label189:
    for (int i = this.entityEnd * 2;; i = 8)
    {
      Object localObject = new String[i];
      char[][] arrayOfChar1 = new char[i][];
      String[] arrayOfString = new String[i];
      char[][] arrayOfChar2 = new char[i][];
      if (this.entityName != null)
      {
        System.arraycopy(this.entityName, 0, localObject, 0, this.entityEnd);
        System.arraycopy(this.entityNameBuf, 0, arrayOfChar1, 0, this.entityEnd);
        System.arraycopy(this.entityReplacement, 0, arrayOfString, 0, this.entityEnd);
        System.arraycopy(this.entityReplacementBuf, 0, arrayOfChar2, 0, this.entityEnd);
      }
      this.entityName = ((String[])localObject);
      this.entityNameBuf = arrayOfChar1;
      this.entityReplacement = arrayOfString;
      this.entityReplacementBuf = arrayOfChar2;
      if (!this.allStringsInterned)
      {
        localObject = new int[i];
        if (this.entityNameHash != null) {
          System.arraycopy(this.entityNameHash, 0, localObject, 0, this.entityEnd);
        }
        this.entityNameHash = ((int[])localObject);
      }
      return;
      i = 0;
      break;
    }
  }
  
  protected void ensureNamespacesCapacity(int paramInt)
  {
    int i;
    if (this.namespacePrefix != null)
    {
      i = this.namespacePrefix.length;
      if (paramInt >= i)
      {
        if (paramInt <= 7) {
          break label129;
        }
        paramInt *= 2;
      }
    }
    for (;;)
    {
      Object localObject = new String[paramInt];
      String[] arrayOfString = new String[paramInt];
      if (this.namespacePrefix != null)
      {
        System.arraycopy(this.namespacePrefix, 0, localObject, 0, this.namespaceEnd);
        System.arraycopy(this.namespaceUri, 0, arrayOfString, 0, this.namespaceEnd);
      }
      this.namespacePrefix = ((String[])localObject);
      this.namespaceUri = arrayOfString;
      if (!this.allStringsInterned)
      {
        localObject = new int[paramInt];
        if (this.namespacePrefixHash != null) {
          System.arraycopy(this.namespacePrefixHash, 0, localObject, 0, this.namespaceEnd);
        }
        this.namespacePrefixHash = ((int[])localObject);
      }
      return;
      i = 0;
      break;
      label129:
      paramInt = 8;
    }
  }
  
  protected void ensurePC(int paramInt)
  {
    if (paramInt > 8192) {
      paramInt *= 2;
    }
    for (;;)
    {
      char[] arrayOfChar = new char[paramInt];
      System.arraycopy(this.pc, 0, arrayOfChar, 0, this.pcEnd);
      this.pc = arrayOfChar;
      return;
      paramInt = 16384;
    }
  }
  
  protected void fillBuf()
    throws IOException, XmlPullParserException
  {
    if (this.reader == null) {
      throw new XmlPullParserException("reader must be set before parsing is started");
    }
    int m;
    int j;
    int k;
    if (this.bufEnd > this.bufSoftLimit)
    {
      if (this.bufStart > this.bufSoftLimit)
      {
        i = 1;
        m = 0;
        if (!this.preventBufferCompaction) {
          break label212;
        }
        j = 0;
        k = 1;
        label56:
        if (j == 0) {
          break label250;
        }
        System.arraycopy(this.buf, this.bufStart, this.buf, 0, this.bufEnd - this.bufStart);
        label85:
        this.bufEnd -= this.bufStart;
        this.pos -= this.bufStart;
        this.posStart -= this.bufStart;
        this.posEnd -= this.bufStart;
        this.bufAbsoluteStart += this.bufStart;
        this.bufStart = 0;
      }
    }
    else {
      if (this.buf.length - this.bufEnd <= 8192) {
        break label336;
      }
    }
    label212:
    label250:
    Object localObject;
    label336:
    for (int i = 8192;; i = this.buf.length - this.bufEnd)
    {
      i = this.reader.read(this.buf, this.bufEnd, i);
      if (i <= 0) {
        break label350;
      }
      this.bufEnd += i;
      return;
      i = 0;
      break;
      j = i;
      k = m;
      if (i != 0) {
        break label56;
      }
      if (this.bufStart < this.buf.length / 2)
      {
        k = 1;
        j = i;
        break label56;
      }
      j = 1;
      k = m;
      break label56;
      if (k != 0)
      {
        localObject = new char[this.buf.length * 2];
        System.arraycopy(this.buf, this.bufStart, localObject, 0, this.bufEnd - this.bufStart);
        this.buf = ((char[])localObject);
        if (this.bufLoadFactor <= 0) {
          break label85;
        }
        this.bufSoftLimit = ((int)(this.bufLoadFactor * this.buf.length / 100L));
        break label85;
      }
      throw new XmlPullParserException("internal error in fillBuffer()");
    }
    label350:
    if (i == -1)
    {
      if ((this.bufAbsoluteStart == 0) && (this.pos == 0)) {
        throw new EOFException("input contained no data");
      }
      if ((this.seenRoot) && (this.depth == 0))
      {
        this.reachedEnd = true;
        return;
      }
      localObject = new StringBuffer();
      if (this.depth > 0)
      {
        ((StringBuffer)localObject).append(" - expected end tag");
        if (this.depth > 1) {
          ((StringBuffer)localObject).append("s");
        }
        ((StringBuffer)localObject).append(" ");
        i = this.depth;
        String str;
        while (i > 0)
        {
          str = new String(this.elRawName[i], 0, this.elRawNameEnd[i]);
          ((StringBuffer)localObject).append("</").append(str).append('>');
          i -= 1;
        }
        ((StringBuffer)localObject).append(" to close");
        i = this.depth;
        while (i > 0)
        {
          if (i != this.depth) {
            ((StringBuffer)localObject).append(" and");
          }
          str = new String(this.elRawName[i], 0, this.elRawNameEnd[i]);
          ((StringBuffer)localObject).append(" start tag <" + str + ">");
          ((StringBuffer)localObject).append(" from line " + this.elRawNameLine[i]);
          i -= 1;
        }
        ((StringBuffer)localObject).append(", parser stopped on");
      }
      throw new EOFException("no more data available" + ((StringBuffer)localObject).toString() + getPositionDescription());
    }
    throw new IOException("error reading input, returned " + i);
  }
  
  public int getAttributeCount()
  {
    if (this.eventType != 2) {
      return -1;
    }
    return this.attributeCount;
  }
  
  public String getAttributeName(int paramInt)
  {
    if (this.eventType != 2) {
      throw new IndexOutOfBoundsException("only START_TAG can have attributes");
    }
    if ((paramInt < 0) || (paramInt >= this.attributeCount)) {
      throw new IndexOutOfBoundsException("attribute position must be 0.." + (this.attributeCount - 1) + " and not " + paramInt);
    }
    return this.attributeName[paramInt];
  }
  
  public String getAttributeNamespace(int paramInt)
  {
    if (this.eventType != 2) {
      throw new IndexOutOfBoundsException("only START_TAG can have attributes");
    }
    if (!this.processNamespaces) {
      return "";
    }
    if ((paramInt < 0) || (paramInt >= this.attributeCount)) {
      throw new IndexOutOfBoundsException("attribute position must be 0.." + (this.attributeCount - 1) + " and not " + paramInt);
    }
    return this.attributeUri[paramInt];
  }
  
  public String getAttributePrefix(int paramInt)
  {
    if (this.eventType != 2) {
      throw new IndexOutOfBoundsException("only START_TAG can have attributes");
    }
    if (!this.processNamespaces) {
      return null;
    }
    if ((paramInt < 0) || (paramInt >= this.attributeCount)) {
      throw new IndexOutOfBoundsException("attribute position must be 0.." + (this.attributeCount - 1) + " and not " + paramInt);
    }
    return this.attributePrefix[paramInt];
  }
  
  public String getAttributeType(int paramInt)
  {
    if (this.eventType != 2) {
      throw new IndexOutOfBoundsException("only START_TAG can have attributes");
    }
    if ((paramInt < 0) || (paramInt >= this.attributeCount)) {
      throw new IndexOutOfBoundsException("attribute position must be 0.." + (this.attributeCount - 1) + " and not " + paramInt);
    }
    return "CDATA";
  }
  
  public String getAttributeValue(int paramInt)
  {
    if (this.eventType != 2) {
      throw new IndexOutOfBoundsException("only START_TAG can have attributes");
    }
    if ((paramInt < 0) || (paramInt >= this.attributeCount)) {
      throw new IndexOutOfBoundsException("attribute position must be 0.." + (this.attributeCount - 1) + " and not " + paramInt);
    }
    return this.attributeValue[paramInt];
  }
  
  public String getAttributeValue(String paramString1, String paramString2)
  {
    if (this.eventType != 2) {
      throw new IndexOutOfBoundsException("only START_TAG can have attributes" + getPositionDescription());
    }
    if (paramString2 == null) {
      throw new IllegalArgumentException("attribute name can not be null");
    }
    if (this.processNamespaces)
    {
      str = paramString1;
      if (paramString1 == null) {
        str = "";
      }
      i = 0;
      while (i < this.attributeCount)
      {
        if (((str == this.attributeUri[i]) || (str.equals(this.attributeUri[i]))) && (paramString2.equals(this.attributeName[i]))) {
          return this.attributeValue[i];
        }
        i += 1;
      }
    }
    String str = paramString1;
    if (paramString1 != null)
    {
      str = paramString1;
      if (paramString1.length() == 0) {
        str = null;
      }
    }
    if (str != null) {
      throw new IllegalArgumentException("when namespaces processing is disabled attribute namespace must be null");
    }
    int i = 0;
    while (i < this.attributeCount)
    {
      if (paramString2.equals(this.attributeName[i])) {
        return this.attributeValue[i];
      }
      i += 1;
    }
    return null;
  }
  
  public int getColumnNumber()
  {
    return this.columnNumber;
  }
  
  public int getDepth()
  {
    return this.depth;
  }
  
  public int getEventType()
    throws XmlPullParserException
  {
    return this.eventType;
  }
  
  public boolean getFeature(String paramString)
  {
    if (paramString == null) {
      throw new IllegalArgumentException("feature name should not be null");
    }
    if ("http://xmlpull.org/v1/doc/features.html#process-namespaces".equals(paramString)) {
      return this.processNamespaces;
    }
    if ("http://xmlpull.org/v1/doc/features.html#names-interned".equals(paramString)) {
      return false;
    }
    if ("http://xmlpull.org/v1/doc/features.html#process-docdecl".equals(paramString)) {
      return false;
    }
    if ("http://xmlpull.org/v1/doc/features.html#xml-roundtrip".equals(paramString)) {
      return this.roundtripSupported;
    }
    return false;
  }
  
  public String getInputEncoding()
  {
    return this.inputEncoding;
  }
  
  public int getLineNumber()
  {
    return this.lineNumber;
  }
  
  public String getName()
  {
    if (this.eventType == 2) {
      return this.elName[this.depth];
    }
    if (this.eventType == 3) {
      return this.elName[this.depth];
    }
    if (this.eventType == 6)
    {
      if (this.entityRefName == null) {
        this.entityRefName = newString(this.buf, this.posStart, this.posEnd - this.posStart);
      }
      return this.entityRefName;
    }
    return null;
  }
  
  public String getNamespace()
  {
    if (this.eventType == 2)
    {
      if (this.processNamespaces) {
        return this.elUri[this.depth];
      }
      return "";
    }
    if (this.eventType == 3)
    {
      if (this.processNamespaces) {
        return this.elUri[this.depth];
      }
      return "";
    }
    return null;
  }
  
  public String getNamespace(String paramString)
  {
    int i;
    if (paramString != null)
    {
      i = this.namespaceEnd - 1;
      while (i >= 0)
      {
        if (paramString.equals(this.namespacePrefix[i])) {
          return this.namespaceUri[i];
        }
        i -= 1;
      }
      if ("xml".equals(paramString)) {
        return "http://www.w3.org/XML/1998/namespace";
      }
      if ("xmlns".equals(paramString)) {
        return "http://www.w3.org/2000/xmlns/";
      }
    }
    else
    {
      i = this.namespaceEnd - 1;
      while (i >= 0)
      {
        if (this.namespacePrefix[i] == null) {
          return this.namespaceUri[i];
        }
        i -= 1;
      }
    }
    return null;
  }
  
  public int getNamespaceCount(int paramInt)
    throws XmlPullParserException
  {
    if ((!this.processNamespaces) || (paramInt == 0)) {
      return 0;
    }
    if ((paramInt < 0) || (paramInt > this.depth)) {
      throw new IllegalArgumentException("allowed namespace depth 0.." + this.depth + " not " + paramInt);
    }
    return this.elNamespaceCount[paramInt];
  }
  
  public String getNamespacePrefix(int paramInt)
    throws XmlPullParserException
  {
    if (paramInt < this.namespaceEnd) {
      return this.namespacePrefix[paramInt];
    }
    throw new XmlPullParserException("position " + paramInt + " exceeded number of available namespaces " + this.namespaceEnd);
  }
  
  public String getNamespaceUri(int paramInt)
    throws XmlPullParserException
  {
    if (paramInt < this.namespaceEnd) {
      return this.namespaceUri[paramInt];
    }
    throw new XmlPullParserException("position " + paramInt + " exceeded number of available namespaces " + this.namespaceEnd);
  }
  
  public String getPositionDescription()
  {
    Object localObject1 = null;
    Object localObject2 = null;
    if (this.posStart <= this.pos)
    {
      int i = findFragment(0, this.buf, this.posStart, this.pos);
      if (i < this.pos) {
        localObject2 = new String(this.buf, i, this.pos - i);
      }
      if (this.bufAbsoluteStart <= 0)
      {
        localObject1 = localObject2;
        if (i <= 0) {}
      }
      else
      {
        localObject1 = "..." + (String)localObject2;
      }
    }
    localObject2 = new StringBuffer().append(" ").append(XmlPullParser.TYPES[this.eventType]);
    if (localObject1 != null)
    {
      localObject1 = " seen " + printable((String)localObject1) + "...";
      localObject2 = ((StringBuffer)localObject2).append((String)localObject1).append(" ");
      if (this.location == null) {
        break label219;
      }
    }
    label219:
    for (localObject1 = this.location;; localObject1 = "")
    {
      return (String)localObject1 + "@" + getLineNumber() + ":" + getColumnNumber();
      localObject1 = "";
      break;
    }
  }
  
  public String getPrefix()
  {
    if (this.eventType == 2) {
      return this.elPrefix[this.depth];
    }
    if (this.eventType == 3) {
      return this.elPrefix[this.depth];
    }
    return null;
  }
  
  public Object getProperty(String paramString)
  {
    if (paramString == null) {
      throw new IllegalArgumentException("property name should not be null");
    }
    if ("http://xmlpull.org/v1/doc/properties.html#xmldecl-version".equals(paramString)) {
      return this.xmlDeclVersion;
    }
    if ("http://xmlpull.org/v1/doc/properties.html#xmldecl-standalone".equals(paramString)) {
      return this.xmlDeclStandalone;
    }
    if ("http://xmlpull.org/v1/doc/properties.html#xmldecl-content".equals(paramString)) {
      return this.xmlDeclContent;
    }
    if ("http://xmlpull.org/v1/doc/properties.html#location".equals(paramString)) {
      return this.location;
    }
    return null;
  }
  
  public String getText()
  {
    if ((this.eventType == 0) || (this.eventType == 1)) {
      return null;
    }
    if (this.eventType == 6) {
      return this.text;
    }
    if (this.text == null) {
      if ((this.usePC) && (this.eventType != 2) && (this.eventType != 3)) {
        break label94;
      }
    }
    label94:
    for (this.text = new String(this.buf, this.posStart, this.posEnd - this.posStart);; this.text = new String(this.pc, this.pcStart, this.pcEnd - this.pcStart)) {
      return this.text;
    }
  }
  
  public char[] getTextCharacters(int[] paramArrayOfInt)
  {
    if (this.eventType == 4)
    {
      if (this.usePC)
      {
        paramArrayOfInt[0] = this.pcStart;
        paramArrayOfInt[1] = (this.pcEnd - this.pcStart);
        return this.pc;
      }
      paramArrayOfInt[0] = this.posStart;
      paramArrayOfInt[1] = (this.posEnd - this.posStart);
      return this.buf;
    }
    if ((this.eventType == 2) || (this.eventType == 3) || (this.eventType == 5) || (this.eventType == 9) || (this.eventType == 6) || (this.eventType == 8) || (this.eventType == 7) || (this.eventType == 10))
    {
      paramArrayOfInt[0] = this.posStart;
      paramArrayOfInt[1] = (this.posEnd - this.posStart);
      return this.buf;
    }
    if ((this.eventType == 0) || (this.eventType == 1))
    {
      paramArrayOfInt[1] = -1;
      paramArrayOfInt[0] = -1;
      return null;
    }
    throw new IllegalArgumentException("unknown text eventType: " + this.eventType);
  }
  
  public boolean isAttributeDefault(int paramInt)
  {
    if (this.eventType != 2) {
      throw new IndexOutOfBoundsException("only START_TAG can have attributes");
    }
    if ((paramInt < 0) || (paramInt >= this.attributeCount)) {
      throw new IndexOutOfBoundsException("attribute position must be 0.." + (this.attributeCount - 1) + " and not " + paramInt);
    }
    return false;
  }
  
  public boolean isEmptyElementTag()
    throws XmlPullParserException
  {
    if (this.eventType != 2) {
      throw new XmlPullParserException("parser must be on START_TAG to check for empty element", this, null);
    }
    return this.emptyElementTag;
  }
  
  protected boolean isNameChar(char paramChar)
  {
    return ((paramChar < 'Ѐ') && (lookupNameChar[paramChar] != 0)) || ((paramChar >= 'Ѐ') && (paramChar <= '‧')) || ((paramChar >= '‪') && (paramChar <= '↏')) || ((paramChar >= '⠀') && (paramChar <= 65519));
  }
  
  protected boolean isNameStartChar(char paramChar)
  {
    return ((paramChar < 'Ѐ') && (lookupNameStartChar[paramChar] != 0)) || ((paramChar >= 'Ѐ') && (paramChar <= '‧')) || ((paramChar >= '‪') && (paramChar <= '↏')) || ((paramChar >= '⠀') && (paramChar <= 65519));
  }
  
  protected boolean isS(char paramChar)
  {
    return (paramChar == ' ') || (paramChar == '\n') || (paramChar == '\r') || (paramChar == '\t');
  }
  
  public boolean isWhitespace()
    throws XmlPullParserException
  {
    if ((this.eventType == 4) || (this.eventType == 5))
    {
      if (this.usePC)
      {
        i = this.pcStart;
        while (i < this.pcEnd)
        {
          if (!isS(this.pc[i])) {
            return false;
          }
          i += 1;
        }
        return true;
      }
      int i = this.posStart;
      while (i < this.posEnd)
      {
        if (!isS(this.buf[i])) {
          return false;
        }
        i += 1;
      }
      return true;
    }
    if (this.eventType == 7) {
      return true;
    }
    throw new XmlPullParserException("no content available to check for white spaces");
  }
  
  protected void joinPC()
  {
    int i = this.posEnd - this.posStart;
    int j = this.pcEnd + i + 1;
    if (j >= this.pc.length) {
      ensurePC(j);
    }
    System.arraycopy(this.buf, this.posStart, this.pc, this.pcEnd, i);
    this.pcEnd += i;
    this.usePC = true;
  }
  
  protected char[] lookuEntityReplacement(int paramInt)
    throws XmlPullParserException, IOException
  {
    if (!this.allStringsInterned)
    {
      int k = fastHash(this.buf, this.posStart, this.posEnd - this.posStart);
      int i = this.entityEnd - 1;
      if (i >= 0)
      {
        char[] arrayOfChar;
        int j;
        if ((k == this.entityNameHash[i]) && (paramInt == this.entityNameBuf[i].length))
        {
          arrayOfChar = this.entityNameBuf[i];
          j = 0;
        }
        for (;;)
        {
          if (j >= paramInt) {
            break label109;
          }
          if (this.buf[(this.posStart + j)] != arrayOfChar[j])
          {
            i -= 1;
            break;
          }
          j += 1;
        }
        label109:
        if (this.tokenize) {
          this.text = this.entityReplacement[i];
        }
        return this.entityReplacementBuf[i];
      }
    }
    else
    {
      this.entityRefName = newString(this.buf, this.posStart, this.posEnd - this.posStart);
      paramInt = this.entityEnd - 1;
      while (paramInt >= 0)
      {
        if (this.entityRefName == this.entityName[paramInt])
        {
          if (this.tokenize) {
            this.text = this.entityReplacement[paramInt];
          }
          return this.entityReplacementBuf[paramInt];
        }
        paramInt -= 1;
      }
    }
    return null;
  }
  
  protected char more()
    throws IOException, XmlPullParserException
  {
    if (this.pos >= this.bufEnd)
    {
      fillBuf();
      if (this.reachedEnd) {
        return 65535;
      }
    }
    char[] arrayOfChar = this.buf;
    int i = this.pos;
    this.pos = (i + 1);
    char c = arrayOfChar[i];
    if (c == '\n') {
      this.lineNumber += 1;
    }
    for (this.columnNumber = 1;; this.columnNumber += 1) {
      return c;
    }
  }
  
  protected String newString(char[] paramArrayOfChar, int paramInt1, int paramInt2)
  {
    return new String(paramArrayOfChar, paramInt1, paramInt2);
  }
  
  protected String newStringIntern(char[] paramArrayOfChar, int paramInt1, int paramInt2)
  {
    return new String(paramArrayOfChar, paramInt1, paramInt2).intern();
  }
  
  public int next()
    throws XmlPullParserException, IOException
  {
    this.tokenize = false;
    return nextImpl();
  }
  
  protected int nextImpl()
    throws XmlPullParserException, IOException
  {
    this.text = null;
    this.pcStart = 0;
    this.pcEnd = 0;
    this.usePC = false;
    this.bufStart = this.posEnd;
    if (this.pastEndTag)
    {
      this.pastEndTag = false;
      this.depth -= 1;
      this.namespaceEnd = this.elNamespaceCount[this.depth];
    }
    if (this.emptyElementTag)
    {
      this.emptyElementTag = false;
      this.pastEndTag = true;
      this.eventType = 3;
      return 3;
    }
    if (this.depth > 0)
    {
      int i;
      if (this.seenStartTag)
      {
        this.seenStartTag = false;
        i = parseStartTag();
        this.eventType = i;
        return i;
      }
      if (this.seenEndTag)
      {
        this.seenEndTag = false;
        i = parseEndTag();
        this.eventType = i;
        return i;
      }
      if (this.seenMarkup)
      {
        this.seenMarkup = false;
        c1 = '<';
      }
      int j;
      for (;;)
      {
        this.posStart = (this.pos - 1);
        bool1 = false;
        j = 0;
        if (c1 != '<') {
          break label593;
        }
        if ((!bool1) || (!this.tokenize)) {
          break;
        }
        this.seenMarkup = true;
        this.eventType = 4;
        return 4;
        if (this.seenAmpersand)
        {
          this.seenAmpersand = false;
          c1 = '&';
        }
        else
        {
          c1 = more();
        }
      }
      char c1 = more();
      if (c1 == '/')
      {
        if ((!this.tokenize) && (bool1))
        {
          this.seenEndTag = true;
          this.eventType = 4;
          return 4;
        }
        i = parseEndTag();
        this.eventType = i;
        return i;
      }
      if (c1 == '!')
      {
        c1 = more();
        if (c1 == '-')
        {
          parseComment();
          if (this.tokenize)
          {
            this.eventType = 9;
            return 9;
          }
          if ((!this.usePC) && (bool1)) {
            i = 1;
          }
        }
      }
      int k;
      label593:
      int n;
      char[] arrayOfChar1;
      for (;;)
      {
        c1 = more();
        j = i;
        break;
        this.posStart = this.pos;
        i = j;
        continue;
        if (c1 == '[')
        {
          parseCDSect(bool1);
          if (this.tokenize)
          {
            this.eventType = 5;
            return 5;
          }
          k = this.posStart;
          i = j;
          if (this.posEnd - k > 0)
          {
            boolean bool2 = true;
            bool1 = bool2;
            i = j;
            if (!this.usePC)
            {
              i = 1;
              bool1 = bool2;
            }
          }
        }
        else
        {
          throw new XmlPullParserException("unexpected character in markup " + printable(c1), this, null);
          if (c1 == '?')
          {
            parsePI();
            if (this.tokenize)
            {
              this.eventType = 8;
              return 8;
            }
            if ((!this.usePC) && (bool1))
            {
              i = 1;
            }
            else
            {
              this.posStart = this.pos;
              i = j;
            }
          }
          else
          {
            if (isNameStartChar(c1))
            {
              if ((!this.tokenize) && (bool1))
              {
                this.seenStartTag = true;
                this.eventType = 4;
                return 4;
              }
              i = parseStartTag();
              this.eventType = i;
              return i;
            }
            throw new XmlPullParserException("unexpected character in markup " + printable(c1), this, null);
            if (c1 != '&') {
              break label893;
            }
            if ((this.tokenize) && (bool1))
            {
              this.seenAmpersand = true;
              this.eventType = 4;
              return 4;
            }
            i = this.posStart;
            k = this.bufAbsoluteStart;
            m = this.posEnd;
            n = this.bufAbsoluteStart;
            arrayOfChar1 = parseEntityRef();
            if (this.tokenize)
            {
              this.eventType = 6;
              return 6;
            }
            if (arrayOfChar1 == null)
            {
              if (this.entityRefName == null) {
                this.entityRefName = newString(this.buf, this.posStart, this.posEnd - this.posStart);
              }
              throw new XmlPullParserException("could not resolve entity named '" + printable(this.entityRefName) + "'", this, null);
            }
            this.posStart = (i + k - this.bufAbsoluteStart);
            this.posEnd = (m + n - this.bufAbsoluteStart);
            i = j;
            if (!this.usePC)
            {
              if (!bool1) {
                break label866;
              }
              joinPC();
            }
            for (i = 0;; i = j)
            {
              j = 0;
              while (j < arrayOfChar1.length)
              {
                if (this.pcEnd >= this.pc.length) {
                  ensurePC(this.pcEnd);
                }
                char[] arrayOfChar2 = this.pc;
                k = this.pcEnd;
                this.pcEnd = (k + 1);
                arrayOfChar2[k] = arrayOfChar1[j];
                j += 1;
              }
              label866:
              this.usePC = true;
              this.pcEnd = 0;
              this.pcStart = 0;
            }
            bool1 = true;
          }
        }
      }
      label893:
      int m = j;
      if (j != 0)
      {
        joinPC();
        m = 0;
      }
      boolean bool1 = true;
      int i1 = 0;
      if ((!this.tokenize) || (!this.roundtripSupported))
      {
        n = 1;
        label932:
        i = 0;
        k = 0;
        label937:
        if (c1 != ']') {
          break label1104;
        }
        if (i == 0) {
          break label1098;
        }
        k = 1;
        j = i;
        label953:
        i = i1;
        if (n != 0)
        {
          if (c1 != '\r') {
            break label1162;
          }
          i = 1;
          this.posEnd = (this.pos - 1);
          if (!this.usePC)
          {
            if (this.posEnd <= this.posStart) {
              break label1144;
            }
            joinPC();
          }
          label1001:
          if (this.pcEnd >= this.pc.length) {
            ensurePC(this.pcEnd);
          }
          arrayOfChar1 = this.pc;
          i1 = this.pcEnd;
          this.pcEnd = (i1 + 1);
          arrayOfChar1[i1] = '\n';
        }
      }
      for (;;)
      {
        char c2 = more();
        if (c2 != '<')
        {
          c1 = c2;
          i1 = i;
          i = j;
          if (c2 != '&') {
            break label937;
          }
        }
        this.posEnd = (this.pos - 1);
        c1 = c2;
        j = m;
        break;
        n = 0;
        break label932;
        label1098:
        j = 1;
        break label953;
        label1104:
        if ((k != 0) && (c1 == '>')) {
          throw new XmlPullParserException("characters ]]> are not allowed in content", this, null);
        }
        j = i;
        if (i == 0) {
          break label953;
        }
        j = 0;
        k = 0;
        break label953;
        label1144:
        this.usePC = true;
        this.pcEnd = 0;
        this.pcStart = 0;
        break label1001;
        label1162:
        if (c1 == '\n')
        {
          if ((i1 == 0) && (this.usePC))
          {
            if (this.pcEnd >= this.pc.length) {
              ensurePC(this.pcEnd);
            }
            arrayOfChar1 = this.pc;
            i = this.pcEnd;
            this.pcEnd = (i + 1);
            arrayOfChar1[i] = '\n';
          }
          i = 0;
        }
        else
        {
          if (this.usePC)
          {
            if (this.pcEnd >= this.pc.length) {
              ensurePC(this.pcEnd);
            }
            arrayOfChar1 = this.pc;
            i = this.pcEnd;
            this.pcEnd = (i + 1);
            arrayOfChar1[i] = c1;
          }
          i = 0;
        }
      }
    }
    if (this.seenRoot) {
      return parseEpilog();
    }
    return parseProlog();
  }
  
  public int nextTag()
    throws XmlPullParserException, IOException
  {
    next();
    if ((this.eventType == 4) && (isWhitespace())) {
      next();
    }
    if ((this.eventType != 2) && (this.eventType != 3)) {
      throw new XmlPullParserException("expected START_TAG or END_TAG not " + XmlPullParser.TYPES[getEventType()], this, null);
    }
    return this.eventType;
  }
  
  public String nextText()
    throws XmlPullParserException, IOException
  {
    if (getEventType() != 2) {
      throw new XmlPullParserException("parser must be on START_TAG to read next text", this, null);
    }
    int i = next();
    if (i == 4)
    {
      String str = getText();
      if (next() != 3) {
        throw new XmlPullParserException("TEXT must be immediately followed by END_TAG and not " + XmlPullParser.TYPES[getEventType()], this, null);
      }
      return str;
    }
    if (i == 3) {
      return "";
    }
    throw new XmlPullParserException("parser must be on START_TAG or TEXT to read text", this, null);
  }
  
  public int nextToken()
    throws XmlPullParserException, IOException
  {
    this.tokenize = true;
    return nextImpl();
  }
  
  protected char parseAttribute()
    throws XmlPullParserException, IOException
  {
    int i4 = this.posStart;
    int i5 = this.bufAbsoluteStart;
    int i6 = this.pos - 1 + this.bufAbsoluteStart;
    int n = -1;
    int i = this.buf[(this.pos - 1)];
    if ((i == 58) && (this.processNamespaces)) {
      throw new XmlPullParserException("when namespaces processing enabled colon can not be at attribute name start", this, null);
    }
    if ((this.processNamespaces) && (i == 120))
    {
      j = 1;
      k = 0;
      c1 = more();
    }
    int m;
    for (;;)
    {
      if (!isNameChar(c1)) {
        break label378;
      }
      int i3 = n;
      int i1 = j;
      int i2 = k;
      if (this.processNamespaces)
      {
        i = j;
        m = k;
        if (j != 0)
        {
          i = j;
          m = k;
          if (k < 5)
          {
            k += 1;
            if (k != 1) {
              break label215;
            }
            i = j;
            m = k;
            if (c1 != 'm')
            {
              i = 0;
              m = k;
            }
          }
        }
        label215:
        do
        {
          do
          {
            for (;;)
            {
              i3 = n;
              i1 = i;
              i2 = m;
              if (c1 != ':') {
                break label358;
              }
              if (n == -1) {
                break label338;
              }
              throw new XmlPullParserException("only one colon is allowed in attribute name when namespaces are enabled", this, null);
              j = 0;
              break;
              if (k == 2)
              {
                i = j;
                m = k;
                if (c1 != 'l')
                {
                  i = 0;
                  m = k;
                }
              }
              else if (k == 3)
              {
                i = j;
                m = k;
                if (c1 != 'n')
                {
                  i = 0;
                  m = k;
                }
              }
              else
              {
                if (k != 4) {
                  break label299;
                }
                i = j;
                m = k;
                if (c1 != 's')
                {
                  i = 0;
                  m = k;
                }
              }
            }
            i = j;
            m = k;
          } while (k != 5);
          i = j;
          m = k;
        } while (c1 == ':');
        label299:
        throw new XmlPullParserException("after xmlns in attribute name must be colonwhen namespaces are enabled", this, null);
        label338:
        i3 = this.pos - 1 + this.bufAbsoluteStart;
        i2 = m;
        i1 = i;
      }
      label358:
      c1 = more();
      n = i3;
      j = i1;
      k = i2;
    }
    label378:
    ensureAttributesCapacity(this.attributeCount);
    Object localObject1 = null;
    if (this.processNamespaces)
    {
      if (k < 4) {
        j = 0;
      }
      if (j != 0)
      {
        c2 = c1;
        k = j;
        if (n != -1)
        {
          i = this.pos - 2 - (n - this.bufAbsoluteStart);
          if (i == 0) {
            throw new XmlPullParserException("namespace prefix is required after xmlns:  when namespaces are enabled", this, null);
          }
          localObject1 = newString(this.buf, n - this.bufAbsoluteStart + 1, i);
          k = j;
          c2 = c1;
        }
      }
    }
    while (isS(c2))
    {
      c2 = more();
      continue;
      if (n != -1)
      {
        this.attributePrefix[this.attributeCount] = newString(this.buf, i6 - this.bufAbsoluteStart, n - i6);
        i = this.pos;
        k = this.bufAbsoluteStart;
        localObject1 = this.attributeName;
        m = this.attributeCount;
        localObject2 = newString(this.buf, n - this.bufAbsoluteStart + 1, i - 2 - (n - k));
        localObject1[m] = localObject2;
      }
      for (;;)
      {
        c2 = c1;
        localObject1 = localObject2;
        k = j;
        if (this.allStringsInterned) {
          break;
        }
        this.attributeNameHash[this.attributeCount] = ((String)localObject2).hashCode();
        c2 = c1;
        localObject1 = localObject2;
        k = j;
        break;
        this.attributePrefix[this.attributeCount] = null;
        localObject1 = this.attributeName;
        i = this.attributeCount;
        localObject2 = newString(this.buf, i6 - this.bufAbsoluteStart, this.pos - 1 - (i6 - this.bufAbsoluteStart));
        localObject1[i] = localObject2;
      }
      localObject1 = this.attributeName;
      i = this.attributeCount;
      localObject2 = newString(this.buf, i6 - this.bufAbsoluteStart, this.pos - 1 - (i6 - this.bufAbsoluteStart));
      localObject1[i] = localObject2;
      c2 = c1;
      localObject1 = localObject2;
      k = j;
      if (!this.allStringsInterned)
      {
        this.attributeNameHash[this.attributeCount] = ((String)localObject2).hashCode();
        c2 = c1;
        localObject1 = localObject2;
        k = j;
      }
    }
    if (c2 != '=') {
      throw new XmlPullParserException("expected = after attribute name", this, null);
    }
    for (char c1 = more(); isS(c1); c1 = more()) {}
    if ((c1 != '"') && (c1 != '\'')) {
      throw new XmlPullParserException("attribute value must start with quotation or apostrophe not " + printable(c1), this, null);
    }
    i = 0;
    this.usePC = false;
    this.pcStart = this.pcEnd;
    this.posStart = this.pos;
    char c2 = more();
    if (c2 == c1)
    {
      if ((!this.processNamespaces) || (k == 0)) {
        break label1737;
      }
      if (this.usePC) {
        break label1414;
      }
    }
    label1135:
    label1140:
    Object localObject3;
    label1409:
    label1414:
    for (Object localObject2 = newStringIntern(this.buf, this.posStart, this.pos - 1 - this.posStart);; localObject2 = newStringIntern(this.pc, this.pcStart, this.pcEnd - this.pcStart))
    {
      ensureNamespacesCapacity(this.namespaceEnd);
      i = -1;
      if (n == -1) {
        break label1630;
      }
      if (((String)localObject2).length() != 0) {
        break label1440;
      }
      throw new XmlPullParserException("non-default namespace can not be declared to be empty string", this, null);
      if (c2 == '<') {
        throw new XmlPullParserException("markup not allowed inside attribute value - illegal < ", this, null);
      }
      if (c2 == '&')
      {
        this.posEnd = (this.pos - 1);
        if (!this.usePC)
        {
          if (this.posEnd <= this.posStart) {
            break label1135;
          }
          i = 1;
          if (i == 0) {
            break label1140;
          }
          joinPC();
        }
        for (;;)
        {
          localObject2 = parseEntityRef();
          if (localObject2 != null) {
            break label1158;
          }
          if (this.entityRefName == null) {
            this.entityRefName = newString(this.buf, this.posStart, this.posEnd - this.posStart);
          }
          throw new XmlPullParserException("could not resolve entity named '" + printable(this.entityRefName) + "'", this, null);
          i = 0;
          break;
          this.usePC = true;
          this.pcEnd = 0;
          this.pcStart = 0;
        }
        label1158:
        i = 0;
        while (i < localObject2.length)
        {
          if (this.pcEnd >= this.pc.length) {
            ensurePC(this.pcEnd);
          }
          localObject3 = this.pc;
          j = this.pcEnd;
          this.pcEnd = (j + 1);
          localObject3[j] = localObject2[i];
          i += 1;
        }
      }
      if ((c2 == '\t') || (c2 == '\n') || (c2 == '\r')) {
        if (!this.usePC)
        {
          this.posEnd = (this.pos - 1);
          if (this.posEnd > this.posStart) {
            joinPC();
          }
        }
        else
        {
          label1273:
          if (this.pcEnd >= this.pc.length) {
            ensurePC(this.pcEnd);
          }
          if ((c2 != '\n') || (i == 0))
          {
            localObject2 = this.pc;
            i = this.pcEnd;
            this.pcEnd = (i + 1);
            localObject2[i] = 32;
          }
          label1327:
          if (c2 != '\r') {
            break label1409;
          }
        }
      }
      for (i = 1;; i = 0)
      {
        break;
        this.usePC = true;
        this.pcStart = 0;
        this.pcEnd = 0;
        break label1273;
        if (!this.usePC) {
          break label1327;
        }
        if (this.pcEnd >= this.pc.length) {
          ensurePC(this.pcEnd);
        }
        localObject2 = this.pc;
        i = this.pcEnd;
        this.pcEnd = (i + 1);
        localObject2[i] = c2;
        break label1327;
      }
    }
    label1440:
    this.namespacePrefix[this.namespaceEnd] = localObject1;
    if (!this.allStringsInterned)
    {
      localObject3 = this.namespacePrefixHash;
      j = this.namespaceEnd;
      i = ((String)localObject1).hashCode();
      localObject3[j] = i;
    }
    this.namespaceUri[this.namespaceEnd] = localObject2;
    int k = this.elNamespaceCount[(this.depth - 1)];
    int j = this.namespaceEnd - 1;
    for (;;)
    {
      if (j < k) {
        break label1711;
      }
      if (((!this.allStringsInterned) && (localObject1 != null)) || ((this.namespacePrefix[j] == localObject1) || ((!this.allStringsInterned) && (localObject1 != null) && (this.namespacePrefixHash[j] == i) && (((String)localObject1).equals(this.namespacePrefix[j])))))
      {
        if (localObject1 == null) {}
        for (localObject1 = "default";; localObject1 = "'" + (String)localObject1 + "'")
        {
          throw new XmlPullParserException("duplicated namespace declaration for " + (String)localObject1 + " prefix", this, null);
          label1630:
          this.namespacePrefix[this.namespaceEnd] = null;
          if (this.allStringsInterned) {
            break;
          }
          localObject3 = this.namespacePrefixHash;
          j = this.namespaceEnd;
          i = -1;
          localObject3[j] = -1;
          break;
        }
      }
      j -= 1;
    }
    label1711:
    this.namespaceEnd += 1;
    this.posStart = (i4 + i5 - this.bufAbsoluteStart);
    return c2;
    label1737:
    if (!this.usePC) {
      this.attributeValue[this.attributeCount] = new String(this.buf, this.posStart, this.pos - 1 - this.posStart);
    }
    for (;;)
    {
      this.attributeCount += 1;
      break;
      this.attributeValue[this.attributeCount] = new String(this.pc, this.pcStart, this.pcEnd - this.pcStart);
    }
  }
  
  protected void parseCDSect(boolean paramBoolean)
    throws XmlPullParserException, IOException
  {
    if (more() != 'C') {
      throw new XmlPullParserException("expected <[CDATA[ for comment start", this, null);
    }
    if (more() != 'D') {
      throw new XmlPullParserException("expected <[CDATA[ for comment start", this, null);
    }
    if (more() != 'A') {
      throw new XmlPullParserException("expected <[CDATA[ for comment start", this, null);
    }
    if (more() != 'T') {
      throw new XmlPullParserException("expected <[CDATA[ for comment start", this, null);
    }
    if (more() != 'A') {
      throw new XmlPullParserException("expected <[CDATA[ for comment start", this, null);
    }
    if (more() != '[') {
      throw new XmlPullParserException("expected <![CDATA[ for comment start", this, null);
    }
    int i3 = this.pos + this.bufAbsoluteStart;
    int i4 = this.lineNumber;
    int i5 = this.columnNumber;
    int n;
    if ((!this.tokenize) || (!this.roundtripSupported))
    {
      n = 1;
      if ((n == 0) || (!paramBoolean)) {
        break label674;
      }
    }
    for (;;)
    {
      int i1;
      int j;
      try
      {
        if (this.usePC) {
          break label674;
        }
        if (this.posEnd <= this.posStart) {
          break label406;
        }
        joinPC();
      }
      catch (EOFException localEOFException)
      {
        char[] arrayOfChar1;
        throw new XmlPullParserException("CDATA section started on line " + i4 + " and column " + i5 + " was not closed", this, localEOFException);
      }
      int i = more();
      int i2;
      int m;
      int k;
      if (i == 93)
      {
        if (i1 == 0)
        {
          j = 1;
          m = i2;
          i1 = j;
          i2 = m;
          if (n == 0) {
            continue;
          }
          if (i != 13) {
            break label540;
          }
          k = 1;
          this.posStart = (i3 - this.bufAbsoluteStart);
          this.posEnd = (this.pos - 1);
          if (!this.usePC)
          {
            if (this.posEnd <= this.posStart) {
              break label522;
            }
            joinPC();
          }
          if (this.pcEnd >= this.pc.length) {
            ensurePC(this.pcEnd);
          }
          arrayOfChar1 = this.pc;
          i1 = this.pcEnd;
          this.pcEnd = (i1 + 1);
          arrayOfChar1[i1] = '\n';
          i1 = j;
          i2 = m;
          continue;
          n = 0;
          break;
          label406:
          this.usePC = true;
          this.pcEnd = 0;
          this.pcStart = 0;
          break label674;
        }
        m = 1;
        j = i1;
        continue;
      }
      if (i == 62)
      {
        if ((i1 != 0) && (i2 != 0))
        {
          if ((n != 0) && (this.usePC)) {
            this.pcEnd -= 2;
          }
          this.posStart = (i3 - this.bufAbsoluteStart);
          this.posEnd = (this.pos - 3);
          return;
        }
        m = 0;
        j = 0;
      }
      else
      {
        j = i1;
        m = i2;
        if (i1 != 0)
        {
          j = 0;
          m = i2;
          continue;
          label522:
          this.usePC = true;
          this.pcEnd = 0;
          this.pcStart = 0;
          continue;
          label540:
          char[] arrayOfChar2;
          if (i == 10)
          {
            if ((k == 0) && (this.usePC))
            {
              if (this.pcEnd >= this.pc.length) {
                ensurePC(this.pcEnd);
              }
              arrayOfChar2 = this.pc;
              k = this.pcEnd;
              this.pcEnd = (k + 1);
              arrayOfChar2[k] = '\n';
            }
          }
          else
          {
            if (this.usePC)
            {
              if (this.pcEnd >= this.pc.length) {
                ensurePC(this.pcEnd);
              }
              arrayOfChar2 = this.pc;
              k = this.pcEnd;
              this.pcEnd = (k + 1);
              arrayOfChar2[k] = i;
            }
            k = 0;
            i1 = j;
            i2 = m;
            continue;
            label674:
            i1 = 0;
            i2 = 0;
            k = 0;
            continue;
          }
          k = 0;
          i1 = j;
          i2 = m;
        }
      }
    }
  }
  
  protected void parseComment()
    throws XmlPullParserException, IOException
  {
    if (more() != '-') {
      throw new XmlPullParserException("expected <!-- for comment start", this, null);
    }
    if (this.tokenize) {
      this.posStart = this.pos;
    }
    int i2 = this.lineNumber;
    int i3 = this.columnNumber;
    for (;;)
    {
      int m;
      try
      {
        if ((this.tokenize != true) || (this.roundtripSupported)) {
          break label171;
        }
        m = 1;
      }
      catch (EOFException localEOFException)
      {
        throw new XmlPullParserException("comment started on line " + i2 + " and column " + i3 + " was not closed", this, localEOFException);
      }
      char c = more();
      label171:
      int i;
      int k;
      if ((n != 0) && (c != '>'))
      {
        throw new XmlPullParserException("in comment after two dashes (--) next character must be > not " + printable(c), this, null);
        m = 0;
      }
      else
      {
        if (c == '-') {
          if (i1 == 0)
          {
            i = 1;
            k = n;
            label194:
            i1 = i;
            n = k;
            if (m == 0) {
              continue;
            }
            if (c != '\r') {
              break label392;
            }
            j = 1;
            if (!this.usePC)
            {
              this.posEnd = (this.pos - 1);
              if (this.posEnd <= this.posStart) {
                break label374;
              }
              joinPC();
            }
          }
        }
        char[] arrayOfChar;
        for (;;)
        {
          if (this.pcEnd >= this.pc.length) {
            ensurePC(this.pcEnd);
          }
          arrayOfChar = this.pc;
          n = this.pcEnd;
          this.pcEnd = (n + 1);
          arrayOfChar[n] = '\n';
          i1 = i;
          n = k;
          break;
          k = 1;
          i = 0;
          break label194;
          if (c == '>')
          {
            if (n != 0)
            {
              if (this.tokenize)
              {
                this.posEnd = (this.pos - 3);
                if (this.usePC) {
                  this.pcEnd -= 2;
                }
              }
              return;
            }
            k = 0;
            i = 0;
            break label194;
          }
          i = 0;
          k = n;
          break label194;
          label374:
          this.usePC = true;
          this.pcEnd = 0;
          this.pcStart = 0;
        }
        label392:
        if (c == '\n')
        {
          if ((j != 0) || (!this.usePC)) {
            break label529;
          }
          if (this.pcEnd >= this.pc.length) {
            ensurePC(this.pcEnd);
          }
          arrayOfChar = this.pc;
          j = this.pcEnd;
          this.pcEnd = (j + 1);
          arrayOfChar[j] = '\n';
          break label529;
        }
        if (this.usePC)
        {
          if (this.pcEnd >= this.pc.length) {
            ensurePC(this.pcEnd);
          }
          arrayOfChar = this.pc;
          j = this.pcEnd;
          this.pcEnd = (j + 1);
          arrayOfChar[j] = c;
        }
        j = 0;
        i1 = i;
        n = k;
        continue;
      }
      int j = 0;
      int i1 = 0;
      int n = 0;
      continue;
      label529:
      j = 0;
      i1 = i;
      n = k;
    }
  }
  
  protected void parseDocdecl()
    throws XmlPullParserException, IOException
  {
    if (more() != 'O') {
      throw new XmlPullParserException("expected <!DOCTYPE", this, null);
    }
    if (more() != 'C') {
      throw new XmlPullParserException("expected <!DOCTYPE", this, null);
    }
    if (more() != 'T') {
      throw new XmlPullParserException("expected <!DOCTYPE", this, null);
    }
    if (more() != 'Y') {
      throw new XmlPullParserException("expected <!DOCTYPE", this, null);
    }
    if (more() != 'P') {
      throw new XmlPullParserException("expected <!DOCTYPE", this, null);
    }
    if (more() != 'E') {
      throw new XmlPullParserException("expected <!DOCTYPE", this, null);
    }
    this.posStart = this.pos;
    int m = 0;
    int i1;
    int j;
    if ((this.tokenize == true) && (!this.roundtripSupported))
    {
      i1 = 1;
      j = 0;
    }
    for (;;)
    {
      int i = more();
      int n = m;
      if (i == 91) {
        n = m + 1;
      }
      int k = n;
      if (i == 93) {
        k = n - 1;
      }
      if ((i == 62) && (k == 0))
      {
        this.posEnd = (this.pos - 1);
        return;
        i1 = 0;
        break;
      }
      m = k;
      if (i1 != 0)
      {
        char[] arrayOfChar;
        if (i == 13)
        {
          j = 1;
          if (!this.usePC)
          {
            this.posEnd = (this.pos - 1);
            if (this.posEnd <= this.posStart) {
              break label326;
            }
            joinPC();
          }
          for (;;)
          {
            if (this.pcEnd >= this.pc.length) {
              ensurePC(this.pcEnd);
            }
            arrayOfChar = this.pc;
            m = this.pcEnd;
            this.pcEnd = (m + 1);
            arrayOfChar[m] = '\n';
            m = k;
            break;
            label326:
            this.usePC = true;
            this.pcEnd = 0;
            this.pcStart = 0;
          }
        }
        if (i == 10)
        {
          if ((j == 0) && (this.usePC))
          {
            if (this.pcEnd >= this.pc.length) {
              ensurePC(this.pcEnd);
            }
            arrayOfChar = this.pc;
            j = this.pcEnd;
            this.pcEnd = (j + 1);
            arrayOfChar[j] = '\n';
          }
          j = 0;
          m = k;
        }
        else
        {
          if (this.usePC)
          {
            if (this.pcEnd >= this.pc.length) {
              ensurePC(this.pcEnd);
            }
            arrayOfChar = this.pc;
            j = this.pcEnd;
            this.pcEnd = (j + 1);
            arrayOfChar[j] = i;
          }
          j = 0;
          m = k;
        }
      }
    }
  }
  
  public int parseEndTag()
    throws XmlPullParserException, IOException
  {
    char c1 = more();
    if (!isNameStartChar(c1)) {
      throw new XmlPullParserException("expected name start and not " + printable(c1), this, null);
    }
    this.posStart = (this.pos - 3);
    int i = this.pos;
    int j = this.bufAbsoluteStart;
    char c2;
    do
    {
      c2 = more();
    } while (isNameChar(c2));
    j = i - 1 + j - this.bufAbsoluteStart;
    int m = this.pos - 1 - j;
    Object localObject1 = this.elRawName[this.depth];
    Object localObject2;
    if (this.elRawNameEnd[this.depth] != m)
    {
      localObject1 = new String((char[])localObject1, 0, this.elRawNameEnd[this.depth]);
      localObject2 = new String(this.buf, j, m);
      throw new XmlPullParserException("end tag name </" + (String)localObject2 + "> must match start tag name <" + (String)localObject1 + ">" + " from line " + this.elRawNameLine[this.depth], this, null);
    }
    i = 0;
    for (;;)
    {
      c1 = c2;
      if (i >= m) {
        break;
      }
      localObject2 = this.buf;
      int k = j + 1;
      if (localObject2[j] != localObject1[i])
      {
        localObject1 = new String((char[])localObject1, 0, m);
        localObject2 = new String(this.buf, k - i - 1, m);
        throw new XmlPullParserException("end tag name </" + (String)localObject2 + "> must be the same as start tag <" + (String)localObject1 + ">" + " from line " + this.elRawNameLine[this.depth], this, null);
      }
      i += 1;
      j = k;
    }
    while (isS(c1)) {
      c1 = more();
    }
    if (c1 != '>') {
      throw new XmlPullParserException("expected > to finish end tag not " + printable(c1) + " from line " + this.elRawNameLine[this.depth], this, null);
    }
    this.posEnd = this.pos;
    this.pastEndTag = true;
    this.eventType = 3;
    return 3;
  }
  
  protected char[] parseEntityRef()
    throws XmlPullParserException, IOException
  {
    this.entityRefName = null;
    this.posStart = this.pos;
    char c1 = more();
    if (c1 == '#')
    {
      c1 = '\000';
      char c3 = '\000';
      char c4 = more();
      char c2 = c4;
      if (c4 == 'x')
      {
        c1 = c3;
        for (;;)
        {
          c2 = more();
          if ((c2 >= '0') && (c2 <= '9'))
          {
            c1 = (char)(c1 * '\020' + (c2 - '0'));
          }
          else if ((c2 >= 'a') && (c2 <= 'f'))
          {
            c1 = (char)(c1 * '\020' + (c2 - 'W'));
          }
          else
          {
            if ((c2 < 'A') || (c2 > 'F')) {
              break;
            }
            c1 = (char)(c1 * '\020' + (c2 - '7'));
          }
        }
        if (c2 != ';') {}
      }
      do
      {
        this.posEnd = (this.pos - 1);
        this.charRefOneCharBuf[0] = c1;
        if (this.tokenize) {
          this.text = newString(this.charRefOneCharBuf, 0, 1);
        }
        return this.charRefOneCharBuf;
        throw new XmlPullParserException("character reference (with hex value) may not contain " + printable(c2), this, null);
        while ((c2 >= '0') && (c2 <= '9'))
        {
          c1 = (char)(c1 * '\n' + (c2 - '0'));
          c2 = more();
        }
      } while (c2 == ';');
      throw new XmlPullParserException("character reference (with decimal value) may not contain " + printable(c2), this, null);
    }
    if (!isNameStartChar(c1)) {
      throw new XmlPullParserException("entity reference names can not start with character '" + printable(c1) + "'", this, null);
    }
    int i;
    do
    {
      c1 = more();
      if (c1 == ';')
      {
        this.posEnd = (this.pos - 1);
        i = this.posEnd - this.posStart;
        if ((i != 2) || (this.buf[this.posStart] != 'l') || (this.buf[(this.posStart + 1)] != 't')) {
          break;
        }
        if (this.tokenize) {
          this.text = "<";
        }
        this.charRefOneCharBuf[0] = '<';
        return this.charRefOneCharBuf;
      }
    } while (isNameChar(c1));
    throw new XmlPullParserException("entity reference name can not contain character " + printable(c1) + "'", this, null);
    if ((i == 3) && (this.buf[this.posStart] == 'a') && (this.buf[(this.posStart + 1)] == 'm') && (this.buf[(this.posStart + 2)] == 'p'))
    {
      if (this.tokenize) {
        this.text = "&";
      }
      this.charRefOneCharBuf[0] = '&';
      return this.charRefOneCharBuf;
    }
    if ((i == 2) && (this.buf[this.posStart] == 'g') && (this.buf[(this.posStart + 1)] == 't'))
    {
      if (this.tokenize) {
        this.text = ">";
      }
      this.charRefOneCharBuf[0] = '>';
      return this.charRefOneCharBuf;
    }
    if ((i == 4) && (this.buf[this.posStart] == 'a') && (this.buf[(this.posStart + 1)] == 'p') && (this.buf[(this.posStart + 2)] == 'o') && (this.buf[(this.posStart + 3)] == 's'))
    {
      if (this.tokenize) {
        this.text = "'";
      }
      this.charRefOneCharBuf[0] = '\'';
      return this.charRefOneCharBuf;
    }
    if ((i == 4) && (this.buf[this.posStart] == 'q') && (this.buf[(this.posStart + 1)] == 'u') && (this.buf[(this.posStart + 2)] == 'o') && (this.buf[(this.posStart + 3)] == 't'))
    {
      if (this.tokenize) {
        this.text = "\"";
      }
      this.charRefOneCharBuf[0] = '"';
      return this.charRefOneCharBuf;
    }
    char[] arrayOfChar = lookuEntityReplacement(i);
    if (arrayOfChar != null) {
      return arrayOfChar;
    }
    if (this.tokenize) {
      this.text = null;
    }
    return null;
  }
  
  protected int parseEpilog()
    throws XmlPullParserException, IOException
  {
    if (this.eventType == 1) {
      throw new XmlPullParserException("already reached end of XML input", this, null);
    }
    if (this.reachedEnd)
    {
      this.eventType = 1;
      return 1;
    }
    int j = 0;
    int k = 0;
    int m = 0;
    int n;
    int i1;
    int i;
    char c;
    label120:
    int i2;
    label210:
    label245:
    char[] arrayOfChar;
    if ((this.tokenize == true) && (!this.roundtripSupported))
    {
      n = 1;
      i1 = 0;
      i = k;
      try
      {
        if (this.seenMarkup) {
          i = k;
        }
        for (c = this.buf[(this.pos - 1)];; c = more())
        {
          i = k;
          this.seenMarkup = false;
          i = k;
          this.posStart = (this.pos - 1);
          i = k;
          if (this.reachedEnd) {
            break label210;
          }
          if (c != '<') {
            break label576;
          }
          if (m == 0) {
            break;
          }
          i = m;
          if (!this.tokenize) {
            break;
          }
          i = m;
          this.posEnd = (this.pos - 1);
          i = m;
          this.seenMarkup = true;
          i = m;
          this.eventType = 7;
          return 7;
          i = k;
        }
        i = m;
        c = more();
        i = m;
        boolean bool = this.reachedEnd;
        if (!bool) {
          break label245;
        }
        j = m;
      }
      catch (EOFException localEOFException)
      {
        do
        {
          for (;;)
          {
            this.reachedEnd = true;
            j = i;
          }
          if (c != '!') {
            break;
          }
          i = m;
          c = more();
          j = m;
          i = m;
        } while (this.reachedEnd);
        if (c != 'D') {
          break label369;
        }
        i = m;
        parseDocdecl();
        i = m;
        j = m;
        k = i1;
        if (!this.tokenize) {
          break label720;
        }
        i = m;
        this.eventType = 10;
        return 10;
        label369:
        if (c != '-') {
          break label411;
        }
        i = m;
        parseComment();
        i = m;
        j = m;
        k = i1;
        if (!this.tokenize) {
          break label720;
        }
        i = m;
        this.eventType = 9;
        return 9;
        label411:
        i = m;
        throw new XmlPullParserException("unexpected markup <!" + printable(c), this, null);
        if (c != '/') {
          break label491;
        }
        i = m;
        throw new XmlPullParserException("end tag not allowed in epilog but got " + printable(c), this, null);
        label491:
        i = m;
        if (!isNameStartChar(c)) {
          break label539;
        }
        i = m;
        throw new XmlPullParserException("start tag not allowed in epilog but got " + printable(c), this, null);
        label539:
        i = m;
        throw new XmlPullParserException("in epilog expected ignorable content and not " + printable(c), this, null);
      }
      if (this.reachedEnd) {
        if ((this.tokenize) && (j != 0))
        {
          this.posEnd = this.pos;
          this.eventType = 7;
          return 7;
          if (c == '?')
          {
            i = m;
            parsePI();
            i = m;
            j = m;
            k = i1;
            if (this.tokenize)
            {
              i = m;
              this.eventType = 8;
              return 8;
            }
          }
          else
          {
            label576:
            i = m;
            if (!isS(c)) {
              break label927;
            }
            m = 1;
            i2 = 1;
            j = m;
            k = i1;
            if (n != 0)
            {
              if (c != '\r') {
                break label773;
              }
              k = 1;
              i = i2;
              if (!this.usePC)
              {
                i = i2;
                this.posEnd = (this.pos - 1);
                i = i2;
                if (this.posEnd <= this.posStart) {
                  break label746;
                }
                i = i2;
                joinPC();
              }
              label658:
              i = i2;
              if (this.pcEnd >= this.pc.length)
              {
                i = i2;
                ensurePC(this.pcEnd);
              }
              i = i2;
              arrayOfChar = this.pc;
              i = i2;
              j = this.pcEnd;
              i = i2;
              this.pcEnd = (j + 1);
              arrayOfChar[j] = '\n';
              j = m;
            }
          }
        }
      }
    }
    for (;;)
    {
      label720:
      i = j;
      c = more();
      m = j;
      i1 = k;
      i = j;
      if (!this.reachedEnd) {
        break label120;
      }
      break label210;
      label746:
      i = i2;
      this.usePC = true;
      i = i2;
      this.pcEnd = 0;
      i = i2;
      this.pcStart = 0;
      break label658;
      label773:
      if (c == '\n')
      {
        if (i1 == 0)
        {
          i = i2;
          if (this.usePC)
          {
            i = i2;
            if (this.pcEnd >= this.pc.length)
            {
              i = i2;
              ensurePC(this.pcEnd);
            }
            i = i2;
            arrayOfChar = this.pc;
            i = i2;
            j = this.pcEnd;
            i = i2;
            this.pcEnd = (j + 1);
            arrayOfChar[j] = '\n';
          }
        }
      }
      else
      {
        i = i2;
        if (!this.usePC) {
          break label997;
        }
        i = i2;
        if (this.pcEnd >= this.pc.length)
        {
          i = i2;
          ensurePC(this.pcEnd);
        }
        i = i2;
        arrayOfChar = this.pc;
        i = i2;
        j = this.pcEnd;
        i = i2;
        this.pcEnd = (j + 1);
        arrayOfChar[j] = c;
        break label997;
        label927:
        i = m;
        throw new XmlPullParserException("in epilog non whitespace content is not allowed but got " + printable(c), this, null);
        this.eventType = 1;
        return 1;
        throw new XmlPullParserException("internal error in parseEpilog");
        n = 0;
        break;
      }
      k = 0;
      j = m;
      continue;
      label997:
      k = 0;
      j = m;
    }
  }
  
  protected boolean parsePI()
    throws XmlPullParserException, IOException
  {
    if (this.tokenize) {
      this.posStart = this.pos;
    }
    int i1 = this.lineNumber;
    int i2 = this.columnNumber;
    int i3 = this.pos + this.bufAbsoluteStart;
    int j = -1;
    if ((this.tokenize == true) && (!this.roundtripSupported)) {}
    int m;
    int i;
    char c1;
    for (int k = 1;; k = 0)
    {
      m = 0;
      i = 0;
      try
      {
        char c2 = more();
        c1 = c2;
        if (!isS(c2)) {
          break;
        }
        throw new XmlPullParserException("processing instruction PITarget must be exactly after <? and not white space character", this, null);
      }
      catch (EOFException localEOFException)
      {
        throw new XmlPullParserException("processing instruction started on line " + i1 + " and column " + i2 + " was not closed", this, localEOFException);
      }
    }
    int n;
    label161:
    label211:
    char[] arrayOfChar;
    if (c1 == '?')
    {
      i = 1;
      n = j;
      j = m;
      if (k != 0)
      {
        if (c1 != '\r') {
          break label641;
        }
        j = 1;
        if (!this.usePC)
        {
          this.posEnd = (this.pos - 1);
          if (this.posEnd <= this.posStart) {
            break label623;
          }
          joinPC();
        }
        if (this.pcEnd >= this.pc.length) {
          ensurePC(this.pcEnd);
        }
        arrayOfChar = this.pc;
        m = this.pcEnd;
        this.pcEnd = (m + 1);
        arrayOfChar[m] = '\n';
      }
    }
    for (;;)
    {
      c1 = more();
      m = j;
      j = n;
      break;
      if (c1 == '>')
      {
        if (i != 0)
        {
          i = j;
          if (j == -1) {
            i = this.pos - 2 + this.bufAbsoluteStart;
          }
          i = this.bufAbsoluteStart;
          i = this.bufAbsoluteStart;
          if (this.tokenize)
          {
            this.posEnd = (this.pos - 2);
            if (k != 0) {
              this.pcEnd -= 1;
            }
          }
          return true;
        }
        i = 0;
        n = j;
        break label161;
      }
      i = j;
      if (j == -1)
      {
        i = j;
        if (isS(c1))
        {
          j = this.pos - 1 + this.bufAbsoluteStart;
          i = j;
          if (j - i3 == 3) {
            if (this.buf[i3] != 'x')
            {
              i = j;
              if (this.buf[i3] != 'X') {}
            }
            else if (this.buf[(i3 + 1)] != 'm')
            {
              i = j;
              if (this.buf[(i3 + 1)] != 'M') {}
            }
            else if (this.buf[(i3 + 2)] != 'l')
            {
              i = j;
              if (this.buf[(i3 + 2)] != 'L') {}
            }
            else
            {
              if (i3 > 3) {
                throw new XmlPullParserException("processing instruction can not have PITarget with reserveld xml name", this, null);
              }
              if ((this.buf[i3] != 'x') && (this.buf[(i3 + 1)] != 'm') && (this.buf[(i3 + 2)] != 'l')) {
                throw new XmlPullParserException("XMLDecl must have xml name in lowercase", this, null);
              }
              parseXmlDecl(c1);
              if (this.tokenize) {
                this.posEnd = (this.pos - 2);
              }
              i = i3 - this.bufAbsoluteStart + 3;
              j = this.pos;
              this.xmlDeclContent = newString(this.buf, i, j - 2 - i);
              return false;
              label623:
              this.usePC = true;
              this.pcEnd = 0;
              this.pcStart = 0;
              break label211;
              label641:
              if (c1 == '\n')
              {
                if ((m != 0) || (!this.usePC)) {
                  break label780;
                }
                if (this.pcEnd >= this.pc.length) {
                  ensurePC(this.pcEnd);
                }
                arrayOfChar = this.pc;
                j = this.pcEnd;
                this.pcEnd = (j + 1);
                arrayOfChar[j] = '\n';
                break label780;
              }
              if (this.usePC)
              {
                if (this.pcEnd >= this.pc.length) {
                  ensurePC(this.pcEnd);
                }
                arrayOfChar = this.pc;
                j = this.pcEnd;
                this.pcEnd = (j + 1);
                arrayOfChar[j] = c1;
              }
              j = 0;
              continue;
            }
          }
        }
      }
      j = 0;
      n = i;
      i = j;
      break label161;
      label780:
      j = 0;
    }
  }
  
  protected int parseProlog()
    throws XmlPullParserException, IOException
  {
    if (this.seenMarkup) {}
    for (int i = this.buf[(this.pos - 1)];; i = more())
    {
      j = i;
      if (this.eventType != 0) {
        break label70;
      }
      if (i != 65534) {
        break;
      }
      throw new XmlPullParserException("first character in input was UNICODE noncharacter (0xFFFE)- input requires int swapping", this, null);
    }
    int j = i;
    char c;
    if (i == 65279) {
      c = more();
    }
    label70:
    this.seenMarkup = false;
    int m = 0;
    this.posStart = (this.pos - 1);
    if ((this.tokenize == true) && (!this.roundtripSupported)) {}
    int i1;
    for (int n = 1;; n = 0)
    {
      i1 = 0;
      if (c != '<') {
        break label439;
      }
      if ((m == 0) || (!this.tokenize)) {
        break;
      }
      this.posEnd = (this.pos - 1);
      this.seenMarkup = true;
      this.eventType = 7;
      return 7;
    }
    i = more();
    int k;
    if (i == 63) {
      if (parsePI())
      {
        k = i1;
        if (this.tokenize)
        {
          this.eventType = 8;
          return 8;
        }
      }
      else
      {
        this.posStart = this.pos;
        m = 0;
        k = i1;
      }
    }
    for (;;)
    {
      c = more();
      i1 = k;
      break;
      if (i == 33)
      {
        i = more();
        if (i == 68)
        {
          if (this.seenDocdecl) {
            throw new XmlPullParserException("only one docdecl allowed in XML document", this, null);
          }
          this.seenDocdecl = true;
          parseDocdecl();
          k = i1;
          if (this.tokenize)
          {
            this.eventType = 10;
            return 10;
          }
        }
        else if (i == 45)
        {
          parseComment();
          k = i1;
          if (this.tokenize)
          {
            this.eventType = 9;
            return 9;
          }
        }
        else
        {
          throw new XmlPullParserException("unexpected markup <!" + printable(i), this, null);
        }
      }
      else
      {
        if (i == 47) {
          throw new XmlPullParserException("expected start tag name and not " + printable(i), this, null);
        }
        if (isNameStartChar(i))
        {
          this.seenRoot = true;
          return parseStartTag();
        }
        throw new XmlPullParserException("expected start tag name and not " + printable(i), this, null);
        label439:
        if (!isS(c)) {
          break label704;
        }
        int i2 = 1;
        m = i2;
        k = i1;
        if (n != 0)
        {
          char[] arrayOfChar;
          if (c == '\r')
          {
            k = 1;
            if (!this.usePC)
            {
              this.posEnd = (this.pos - 1);
              if (this.posEnd <= this.posStart) {
                break label556;
              }
              joinPC();
            }
            for (;;)
            {
              if (this.pcEnd >= this.pc.length) {
                ensurePC(this.pcEnd);
              }
              arrayOfChar = this.pc;
              m = this.pcEnd;
              this.pcEnd = (m + 1);
              arrayOfChar[m] = '\n';
              m = i2;
              break;
              label556:
              this.usePC = true;
              this.pcEnd = 0;
              this.pcStart = 0;
            }
          }
          if (c == '\n')
          {
            if ((i1 == 0) && (this.usePC))
            {
              if (this.pcEnd >= this.pc.length) {
                ensurePC(this.pcEnd);
              }
              arrayOfChar = this.pc;
              k = this.pcEnd;
              this.pcEnd = (k + 1);
              arrayOfChar[k] = '\n';
            }
            k = 0;
            m = i2;
          }
          else
          {
            if (this.usePC)
            {
              if (this.pcEnd >= this.pc.length) {
                ensurePC(this.pcEnd);
              }
              arrayOfChar = this.pc;
              k = this.pcEnd;
              this.pcEnd = (k + 1);
              arrayOfChar[k] = c;
            }
            k = 0;
            m = i2;
          }
        }
      }
    }
    label704:
    throw new XmlPullParserException("only whitespace content allowed before start tag and not " + printable(c), this, null);
  }
  
  public int parseStartTag()
    throws XmlPullParserException, IOException
  {
    this.depth += 1;
    this.posStart = (this.pos - 2);
    this.emptyElementTag = false;
    this.attributeCount = 0;
    int k = this.pos - 1 + this.bufAbsoluteStart;
    int j = -1;
    int i = j;
    if (this.buf[(this.pos - 1)] == ':')
    {
      i = j;
      if (this.processNamespaces)
      {
        throw new XmlPullParserException("when namespaces processing enabled colon can not be at element name start", this, null);
        i = this.pos - 1 + this.bufAbsoluteStart;
      }
    }
    label97:
    char c = more();
    Object localObject1;
    Object localObject2;
    if (!isNameChar(c))
    {
      ensureElementsCapacity();
      j = this.pos - 1 - (k - this.bufAbsoluteStart);
      if ((this.elRawName[this.depth] == null) || (this.elRawName[this.depth].length < j)) {
        this.elRawName[this.depth] = new char[j * 2];
      }
      System.arraycopy(this.buf, k - this.bufAbsoluteStart, this.elRawName[this.depth], 0, j);
      this.elRawNameEnd[this.depth] = j;
      this.elRawNameLine[this.depth] = this.lineNumber;
      localObject1 = null;
      if (!this.processNamespaces) {
        break label402;
      }
      if (i == -1) {
        break label355;
      }
      localObject2 = this.elPrefix;
      j = this.depth;
      localObject1 = newString(this.buf, k - this.bufAbsoluteStart, i - k);
      localObject2[j] = localObject1;
      this.elName[this.depth] = newString(this.buf, i + 1 - this.bufAbsoluteStart, this.pos - 2 - (i - this.bufAbsoluteStart));
    }
    label355:
    String str;
    for (;;)
    {
      if (isS(c))
      {
        c = more();
        continue;
        if ((c != ':') || (!this.processNamespaces)) {
          break label97;
        }
        if (i == -1) {
          break;
        }
        throw new XmlPullParserException("only one colon is allowed in name of element when namespaces are enabled", this, null);
        localObject2 = this.elPrefix;
        i = this.depth;
        localObject1 = null;
        localObject2[i] = null;
        this.elName[this.depth] = newString(this.buf, k - this.bufAbsoluteStart, j);
        continue;
        label402:
        this.elName[this.depth] = newString(this.buf, k - this.bufAbsoluteStart, j);
        continue;
      }
      if (c == '>') {}
      do
      {
        if (!this.processNamespaces) {
          break label996;
        }
        str = getNamespace((String)localObject1);
        localObject2 = str;
        if (str == null)
        {
          if (localObject1 != null) {
            break label679;
          }
          localObject2 = "";
        }
        this.elUri[this.depth] = localObject2;
        i = 0;
        if (i >= this.attributeCount) {
          break label737;
        }
        localObject1 = this.attributePrefix[i];
        if (localObject1 == null) {
          break label725;
        }
        localObject2 = getNamespace((String)localObject1);
        if (localObject2 != null) {
          break label710;
        }
        throw new XmlPullParserException("could not determine namespace bound to attribute prefix " + (String)localObject1, this, null);
        if (c != '/') {
          break;
        }
        if (this.emptyElementTag) {
          throw new XmlPullParserException("repeated / in tag declaration", this, null);
        }
        this.emptyElementTag = true;
        c = more();
      } while (c == '>');
      throw new XmlPullParserException("expected > to end empty tag not " + printable(c), this, null);
      if (!isNameStartChar(c)) {
        break label645;
      }
      parseAttribute();
      c = more();
    }
    label645:
    throw new XmlPullParserException("start tag unexpected character " + printable(c), this, null);
    label679:
    throw new XmlPullParserException("could not determine namespace bound to element prefix " + (String)localObject1, this, null);
    label710:
    this.attributeUri[i] = localObject2;
    for (;;)
    {
      i += 1;
      break;
      label725:
      this.attributeUri[i] = "";
    }
    label737:
    i = 1;
    while (i < this.attributeCount)
    {
      j = 0;
      while (j < i)
      {
        if ((this.attributeUri[j] == this.attributeUri[i]) && (((this.allStringsInterned) && (this.attributeName[j].equals(this.attributeName[i]))) || ((!this.allStringsInterned) && (this.attributeNameHash[j] == this.attributeNameHash[i]) && (this.attributeName[j].equals(this.attributeName[i])))))
        {
          localObject2 = this.attributeName[j];
          localObject1 = localObject2;
          if (this.attributeUri[j] != null) {
            localObject1 = this.attributeUri[j] + ":" + (String)localObject2;
          }
          str = this.attributeName[i];
          localObject2 = str;
          if (this.attributeUri[i] != null) {
            localObject2 = this.attributeUri[i] + ":" + str;
          }
          throw new XmlPullParserException("duplicated attributes " + (String)localObject1 + " and " + (String)localObject2, this, null);
        }
        j += 1;
      }
      i += 1;
    }
    label996:
    i = 1;
    while (i < this.attributeCount)
    {
      j = 0;
      while (j < i)
      {
        if (((this.allStringsInterned) && (this.attributeName[j].equals(this.attributeName[i]))) || ((!this.allStringsInterned) && (this.attributeNameHash[j] == this.attributeNameHash[i]) && (this.attributeName[j].equals(this.attributeName[i]))))
        {
          localObject1 = this.attributeName[j];
          localObject2 = this.attributeName[i];
          throw new XmlPullParserException("duplicated attributes " + (String)localObject1 + " and " + (String)localObject2, this, null);
        }
        j += 1;
      }
      i += 1;
    }
    this.elNamespaceCount[this.depth] = this.namespaceEnd;
    this.posEnd = this.pos;
    this.eventType = 2;
    return 2;
  }
  
  protected void parseXmlDecl(char paramChar)
    throws XmlPullParserException, IOException
  {
    this.preventBufferCompaction = true;
    this.bufStart = 0;
    paramChar = skipS(requireInput(skipS(paramChar), VERSION));
    if (paramChar != '=') {
      throw new XmlPullParserException("expected equals sign (=) after version and not " + printable(paramChar), this, null);
    }
    char c = skipS(more());
    if ((c != '\'') && (c != '"')) {
      throw new XmlPullParserException("expected apostrophe (') or quotation mark (\") after version and not " + printable(c), this, null);
    }
    int i = this.pos;
    for (paramChar = more(); paramChar != c; paramChar = more()) {
      if (((paramChar < 'a') || (paramChar > 'z')) && ((paramChar < 'A') || (paramChar > 'Z')) && ((paramChar < '0') || (paramChar > '9')) && (paramChar != '_') && (paramChar != '.') && (paramChar != ':') && (paramChar != '-')) {
        throw new XmlPullParserException("<?xml version value expected to be in ([a-zA-Z0-9_.:] | '-') not " + printable(paramChar), this, null);
      }
    }
    parseXmlDeclWithVersion(i, this.pos - 1);
    this.preventBufferCompaction = false;
  }
  
  protected void parseXmlDeclWithVersion(int paramInt1, int paramInt2)
    throws XmlPullParserException, IOException
  {
    String str = this.inputEncoding;
    if ((paramInt2 - paramInt1 != 3) || (this.buf[paramInt1] != '1') || (this.buf[(paramInt1 + 1)] != '.') || (this.buf[(paramInt1 + 2)] != '0')) {
      throw new XmlPullParserException("only 1.0 is supported as <?xml version not '" + printable(new String(this.buf, paramInt1, paramInt2 - paramInt1)) + "'", this, null);
    }
    this.xmlDeclVersion = newString(this.buf, paramInt1, paramInt2 - paramInt1);
    char c2 = skipS(more());
    char c1 = c2;
    if (c2 == 'e')
    {
      c1 = skipS(requireInput(more(), NCODING));
      if (c1 != '=') {
        throw new XmlPullParserException("expected equals sign (=) after encoding and not " + printable(c1), this, null);
      }
      c2 = skipS(more());
      if ((c2 != '\'') && (c2 != '"')) {
        throw new XmlPullParserException("expected apostrophe (') or quotation mark (\") after encoding and not " + printable(c2), this, null);
      }
      paramInt1 = this.pos;
      c1 = more();
      if (((c1 < 'a') || (c1 > 'z')) && ((c1 < 'A') || (c1 > 'Z'))) {
        throw new XmlPullParserException("<?xml encoding name expected to start with [A-Za-z] not " + printable(c1), this, null);
      }
      for (c1 = more(); c1 != c2; c1 = more()) {
        if (((c1 < 'a') || (c1 > 'z')) && ((c1 < 'A') || (c1 > 'Z')) && ((c1 < '0') || (c1 > '9')) && (c1 != '.') && (c1 != '_') && (c1 != '-')) {
          throw new XmlPullParserException("<?xml encoding value expected to be in ([A-Za-z0-9._] | '-') not " + printable(c1), this, null);
        }
      }
      paramInt2 = this.pos;
      this.inputEncoding = newString(this.buf, paramInt1, paramInt2 - 1 - paramInt1);
      c1 = more();
    }
    c2 = skipS(c1);
    c1 = c2;
    if (c2 == 's')
    {
      c1 = skipS(requireInput(more(), TANDALONE));
      if (c1 != '=') {
        throw new XmlPullParserException("expected equals sign (=) after standalone and not " + printable(c1), this, null);
      }
      c2 = skipS(more());
      if ((c2 != '\'') && (c2 != '"')) {
        throw new XmlPullParserException("expected apostrophe (') or quotation mark (\") after encoding and not " + printable(c2), this, null);
      }
      paramInt1 = this.pos;
      c1 = more();
      if (c1 == 'y')
      {
        c1 = requireInput(c1, YES);
        this.xmlDeclStandalone = new Boolean(true);
      }
      while (c1 != c2)
      {
        throw new XmlPullParserException("expected " + c2 + " after standalone value not " + printable(c1), this, null);
        if (c1 == 'n')
        {
          c1 = requireInput(c1, NO);
          this.xmlDeclStandalone = new Boolean(false);
        }
        else
        {
          throw new XmlPullParserException("expected 'yes' or 'no' after standalone and not " + printable(c1), this, null);
        }
      }
      c1 = more();
    }
    c1 = skipS(c1);
    if (c1 != '?') {
      throw new XmlPullParserException("expected ?> as last part of <?xml not " + printable(c1), this, null);
    }
    c1 = more();
    if (c1 != '>') {
      throw new XmlPullParserException("expected ?> as last part of <?xml not " + printable(c1), this, null);
    }
  }
  
  protected String printable(char paramChar)
  {
    if (paramChar == '\n') {
      return "\\n";
    }
    if (paramChar == '\r') {
      return "\\r";
    }
    if (paramChar == '\t') {
      return "\\t";
    }
    if (paramChar == '\'') {
      return "\\'";
    }
    if ((paramChar > '') || (paramChar < ' ')) {
      return "\\u" + Integer.toHexString(paramChar);
    }
    return "" + paramChar;
  }
  
  protected String printable(String paramString)
  {
    if (paramString == null) {
      return null;
    }
    int j = paramString.length();
    StringBuffer localStringBuffer = new StringBuffer(j + 10);
    int i = 0;
    while (i < j)
    {
      localStringBuffer.append(printable(paramString.charAt(i)));
      i += 1;
    }
    return localStringBuffer.toString();
  }
  
  public void require(int paramInt, String paramString1, String paramString2)
    throws XmlPullParserException, IOException
  {
    if ((!this.processNamespaces) && (paramString1 != null)) {
      throw new XmlPullParserException("processing namespaces must be enabled on parser (or factory) to have possible namespaces declared on elements" + " (position:" + getPositionDescription() + ")");
    }
    if ((paramInt != getEventType()) || ((paramString1 != null) && (!paramString1.equals(getNamespace()))) || ((paramString2 != null) && (!paramString2.equals(getName()))))
    {
      StringBuffer localStringBuffer = new StringBuffer().append("expected event ").append(XmlPullParser.TYPES[paramInt]);
      Object localObject;
      if (paramString2 != null)
      {
        localObject = " with name '" + paramString2 + "'";
        localStringBuffer = localStringBuffer.append((String)localObject);
        if ((paramString1 == null) || (paramString2 == null)) {
          break label483;
        }
        localObject = " and";
        label169:
        localStringBuffer = localStringBuffer.append((String)localObject);
        if (paramString1 == null) {
          break label491;
        }
        localObject = " with namespace '" + paramString1 + "'";
        label210:
        localStringBuffer = localStringBuffer.append((String)localObject).append(" but got");
        if (paramInt == getEventType()) {
          break label499;
        }
        localObject = " " + XmlPullParser.TYPES[getEventType()];
        label262:
        localStringBuffer = localStringBuffer.append((String)localObject);
        if ((paramString2 == null) || (getName() == null) || (paramString2.equals(getName()))) {
          break label507;
        }
        localObject = " name '" + getName() + "'";
        label324:
        localObject = localStringBuffer.append((String)localObject);
        if ((paramString1 == null) || (paramString2 == null) || (getName() == null) || (paramString2.equals(getName())) || (getNamespace() == null) || (paramString1.equals(getNamespace()))) {
          break label515;
        }
        paramString2 = " and";
        label381:
        paramString2 = ((StringBuffer)localObject).append(paramString2);
        if ((paramString1 == null) || (getNamespace() == null) || (paramString1.equals(getNamespace()))) {
          break label522;
        }
      }
      label515:
      label522:
      for (paramString1 = " namespace '" + getNamespace() + "'";; paramString1 = "")
      {
        throw new XmlPullParserException(paramString1 + " (position:" + getPositionDescription() + ")");
        localObject = "";
        break;
        label483:
        localObject = "";
        break label169;
        label491:
        localObject = "";
        break label210;
        label499:
        localObject = "";
        break label262;
        label507:
        localObject = "";
        break label324;
        paramString2 = "";
        break label381;
      }
    }
  }
  
  protected char requireInput(char paramChar, char[] paramArrayOfChar)
    throws XmlPullParserException, IOException
  {
    int i = 0;
    while (i < paramArrayOfChar.length)
    {
      if (paramChar != paramArrayOfChar[i]) {
        throw new XmlPullParserException("expected " + printable(paramArrayOfChar[i]) + " in " + new String(paramArrayOfChar) + " and not " + printable(paramChar), this, null);
      }
      paramChar = more();
      i += 1;
    }
    return paramChar;
  }
  
  protected char requireNextS()
    throws XmlPullParserException, IOException
  {
    char c = more();
    if (!isS(c)) {
      throw new XmlPullParserException("white space is required and not " + printable(c), this, null);
    }
    return skipS(c);
  }
  
  protected void reset()
  {
    this.location = null;
    this.lineNumber = 1;
    this.columnNumber = 0;
    this.seenRoot = false;
    this.reachedEnd = false;
    this.eventType = 0;
    this.emptyElementTag = false;
    this.depth = 0;
    this.attributeCount = 0;
    this.namespaceEnd = 0;
    this.entityEnd = 0;
    this.reader = null;
    this.inputEncoding = null;
    this.preventBufferCompaction = false;
    this.bufAbsoluteStart = 0;
    this.bufStart = 0;
    this.bufEnd = 0;
    this.posEnd = 0;
    this.posStart = 0;
    this.pos = 0;
    this.pcStart = 0;
    this.pcEnd = 0;
    this.usePC = false;
    this.seenStartTag = false;
    this.seenEndTag = false;
    this.pastEndTag = false;
    this.seenAmpersand = false;
    this.seenMarkup = false;
    this.seenDocdecl = false;
    this.xmlDeclVersion = null;
    this.xmlDeclStandalone = null;
    this.xmlDeclContent = null;
    resetStringCache();
  }
  
  protected void resetStringCache() {}
  
  public void setFeature(String paramString, boolean paramBoolean)
    throws XmlPullParserException
  {
    if (paramString == null) {
      throw new IllegalArgumentException("feature name should not be null");
    }
    if ("http://xmlpull.org/v1/doc/features.html#process-namespaces".equals(paramString))
    {
      if (this.eventType != 0) {
        throw new XmlPullParserException("namespace processing feature can only be changed before parsing", this, null);
      }
      this.processNamespaces = paramBoolean;
    }
    do
    {
      do
      {
        return;
        if (!"http://xmlpull.org/v1/doc/features.html#names-interned".equals(paramString)) {
          break;
        }
      } while (!paramBoolean);
      throw new XmlPullParserException("interning names in this implementation is not supported");
      if (!"http://xmlpull.org/v1/doc/features.html#process-docdecl".equals(paramString)) {
        break;
      }
    } while (!paramBoolean);
    throw new XmlPullParserException("processing DOCDECL is not supported");
    if ("http://xmlpull.org/v1/doc/features.html#xml-roundtrip".equals(paramString))
    {
      this.roundtripSupported = paramBoolean;
      return;
    }
    throw new XmlPullParserException("unsupported feature " + paramString);
  }
  
  public void setInput(InputStream paramInputStream, String paramString)
    throws XmlPullParserException
  {
    if (paramInputStream == null) {
      throw new IllegalArgumentException("input stream can not be null");
    }
    this.inputStream = paramInputStream;
    if (paramString != null) {}
    for (;;)
    {
      try
      {
        paramInputStream = new InputStreamReader(paramInputStream, paramString);
        setInput(paramInputStream);
        this.inputEncoding = paramString;
        return;
      }
      catch (UnsupportedEncodingException paramInputStream)
      {
        throw new XmlPullParserException("could not create reader for encoding " + paramString + " : " + paramInputStream, this, paramInputStream);
      }
      paramInputStream = new InputStreamReader(paramInputStream, "UTF-8");
    }
  }
  
  public void setInput(Reader paramReader)
    throws XmlPullParserException
  {
    reset();
    this.reader = paramReader;
  }
  
  public void setProperty(String paramString, Object paramObject)
    throws XmlPullParserException
  {
    if ("http://xmlpull.org/v1/doc/properties.html#location".equals(paramString))
    {
      this.location = ((String)paramObject);
      return;
    }
    throw new XmlPullParserException("unsupported property: '" + paramString + "'");
  }
  
  protected char skipS(char paramChar)
    throws XmlPullParserException, IOException
  {
    while (isS(paramChar)) {
      paramChar = more();
    }
    return paramChar;
  }
  
  public void skipSubTree()
    throws XmlPullParserException, IOException
  {
    require(2, null, null);
    int i = 1;
    while (i > 0)
    {
      int j = next();
      if (j == 3) {
        i -= 1;
      } else if (j == 2) {
        i += 1;
      }
    }
  }
}


/* Location:           C:\Users\Administrator\Documents\ds\反编译\AndroidKiller_v1.3.1\projects\Jungledsq_ppc\ProjectSrc\smali\
 * Qualified Name:     org.xmlpull.mxp1.MXParser
 * JD-Core Version:    0.7.0.1
 */