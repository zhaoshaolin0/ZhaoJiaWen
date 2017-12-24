package org.jivesoftware.smackx;

import org.jivesoftware.smack.util.StringUtils;

public class XHTMLText
{
  private StringBuilder text = new StringBuilder(30);
  
  public XHTMLText(String paramString1, String paramString2)
  {
    appendOpenBodyTag(paramString1, paramString2);
  }
  
  private void appendOpenBodyTag(String paramString1, String paramString2)
  {
    StringBuilder localStringBuilder = new StringBuilder("<body");
    if (paramString1 != null)
    {
      localStringBuilder.append(" style=\"");
      localStringBuilder.append(paramString1);
      localStringBuilder.append("\"");
    }
    if (paramString2 != null)
    {
      localStringBuilder.append(" xml:lang=\"");
      localStringBuilder.append(paramString2);
      localStringBuilder.append("\"");
    }
    localStringBuilder.append(">");
    this.text.append(localStringBuilder.toString());
  }
  
  private String closeBodyTag()
  {
    return "</body>";
  }
  
  public void append(String paramString)
  {
    this.text.append(StringUtils.escapeForXML(paramString));
  }
  
  public void appendBrTag()
  {
    this.text.append("<br>");
  }
  
  public void appendCloseAnchorTag()
  {
    this.text.append("</a>");
  }
  
  public void appendCloseBlockQuoteTag()
  {
    this.text.append("</blockquote>");
  }
  
  public void appendCloseCodeTag()
  {
    this.text.append("</code>");
  }
  
  public void appendCloseEmTag()
  {
    this.text.append("</em>");
  }
  
  public void appendCloseHeaderTag(int paramInt)
  {
    if ((paramInt > 3) || (paramInt < 1)) {
      return;
    }
    StringBuilder localStringBuilder = new StringBuilder("</h");
    localStringBuilder.append(paramInt);
    localStringBuilder.append(">");
    this.text.append(localStringBuilder.toString());
  }
  
  public void appendCloseInlinedQuoteTag()
  {
    this.text.append("</q>");
  }
  
  public void appendCloseOrderedListTag()
  {
    this.text.append("</ol>");
  }
  
  public void appendCloseParagraphTag()
  {
    this.text.append("</p>");
  }
  
  public void appendCloseSpanTag()
  {
    this.text.append("</span>");
  }
  
  public void appendCloseStrongTag()
  {
    this.text.append("</strong>");
  }
  
  public void appendCloseUnorderedListTag()
  {
    this.text.append("</ul>");
  }
  
  public void appendImageTag(String paramString1, String paramString2, String paramString3, String paramString4, String paramString5)
  {
    StringBuilder localStringBuilder = new StringBuilder("<img");
    if (paramString1 != null)
    {
      localStringBuilder.append(" align=\"");
      localStringBuilder.append(paramString1);
      localStringBuilder.append("\"");
    }
    if (paramString2 != null)
    {
      localStringBuilder.append(" alt=\"");
      localStringBuilder.append(paramString2);
      localStringBuilder.append("\"");
    }
    if (paramString3 != null)
    {
      localStringBuilder.append(" height=\"");
      localStringBuilder.append(paramString3);
      localStringBuilder.append("\"");
    }
    if (paramString4 != null)
    {
      localStringBuilder.append(" src=\"");
      localStringBuilder.append(paramString4);
      localStringBuilder.append("\"");
    }
    if (paramString5 != null)
    {
      localStringBuilder.append(" width=\"");
      localStringBuilder.append(paramString5);
      localStringBuilder.append("\"");
    }
    localStringBuilder.append(">");
    this.text.append(localStringBuilder.toString());
  }
  
  public void appendLineItemTag(String paramString)
  {
    StringBuilder localStringBuilder = new StringBuilder("<li");
    if (paramString != null)
    {
      localStringBuilder.append(" style=\"");
      localStringBuilder.append(paramString);
      localStringBuilder.append("\"");
    }
    localStringBuilder.append(">");
    this.text.append(localStringBuilder.toString());
  }
  
  public void appendOpenAnchorTag(String paramString1, String paramString2)
  {
    StringBuilder localStringBuilder = new StringBuilder("<a");
    if (paramString1 != null)
    {
      localStringBuilder.append(" href=\"");
      localStringBuilder.append(paramString1);
      localStringBuilder.append("\"");
    }
    if (paramString2 != null)
    {
      localStringBuilder.append(" style=\"");
      localStringBuilder.append(paramString2);
      localStringBuilder.append("\"");
    }
    localStringBuilder.append(">");
    this.text.append(localStringBuilder.toString());
  }
  
  public void appendOpenBlockQuoteTag(String paramString)
  {
    StringBuilder localStringBuilder = new StringBuilder("<blockquote");
    if (paramString != null)
    {
      localStringBuilder.append(" style=\"");
      localStringBuilder.append(paramString);
      localStringBuilder.append("\"");
    }
    localStringBuilder.append(">");
    this.text.append(localStringBuilder.toString());
  }
  
  public void appendOpenCiteTag()
  {
    this.text.append("<cite>");
  }
  
  public void appendOpenCodeTag()
  {
    this.text.append("<code>");
  }
  
  public void appendOpenEmTag()
  {
    this.text.append("<em>");
  }
  
  public void appendOpenHeaderTag(int paramInt, String paramString)
  {
    if ((paramInt > 3) || (paramInt < 1)) {
      return;
    }
    StringBuilder localStringBuilder = new StringBuilder("<h");
    localStringBuilder.append(paramInt);
    if (paramString != null)
    {
      localStringBuilder.append(" style=\"");
      localStringBuilder.append(paramString);
      localStringBuilder.append("\"");
    }
    localStringBuilder.append(">");
    this.text.append(localStringBuilder.toString());
  }
  
  public void appendOpenInlinedQuoteTag(String paramString)
  {
    StringBuilder localStringBuilder = new StringBuilder("<q");
    if (paramString != null)
    {
      localStringBuilder.append(" style=\"");
      localStringBuilder.append(paramString);
      localStringBuilder.append("\"");
    }
    localStringBuilder.append(">");
    this.text.append(localStringBuilder.toString());
  }
  
  public void appendOpenOrderedListTag(String paramString)
  {
    StringBuilder localStringBuilder = new StringBuilder("<ol");
    if (paramString != null)
    {
      localStringBuilder.append(" style=\"");
      localStringBuilder.append(paramString);
      localStringBuilder.append("\"");
    }
    localStringBuilder.append(">");
    this.text.append(localStringBuilder.toString());
  }
  
  public void appendOpenParagraphTag(String paramString)
  {
    StringBuilder localStringBuilder = new StringBuilder("<p");
    if (paramString != null)
    {
      localStringBuilder.append(" style=\"");
      localStringBuilder.append(paramString);
      localStringBuilder.append("\"");
    }
    localStringBuilder.append(">");
    this.text.append(localStringBuilder.toString());
  }
  
  public void appendOpenSpanTag(String paramString)
  {
    StringBuilder localStringBuilder = new StringBuilder("<span");
    if (paramString != null)
    {
      localStringBuilder.append(" style=\"");
      localStringBuilder.append(paramString);
      localStringBuilder.append("\"");
    }
    localStringBuilder.append(">");
    this.text.append(localStringBuilder.toString());
  }
  
  public void appendOpenStrongTag()
  {
    this.text.append("<strong>");
  }
  
  public void appendOpenUnorderedListTag(String paramString)
  {
    StringBuilder localStringBuilder = new StringBuilder("<ul");
    if (paramString != null)
    {
      localStringBuilder.append(" style=\"");
      localStringBuilder.append(paramString);
      localStringBuilder.append("\"");
    }
    localStringBuilder.append(">");
    this.text.append(localStringBuilder.toString());
  }
  
  public String toString()
  {
    return this.text.toString().concat(closeBodyTag());
  }
}


/* Location:           C:\Users\Administrator\Documents\ds\反编译\AndroidKiller_v1.3.1\projects\Jungledsq_ppc\ProjectSrc\smali\
 * Qualified Name:     org.jivesoftware.smackx.XHTMLText
 * JD-Core Version:    0.7.0.1
 */