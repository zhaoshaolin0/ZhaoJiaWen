package org.jivesoftware.smack.debugger;

import java.io.Reader;
import java.io.Writer;
import org.jivesoftware.smack.PacketListener;

public abstract interface SmackDebugger
{
  public abstract Reader getReader();
  
  public abstract PacketListener getReaderListener();
  
  public abstract Writer getWriter();
  
  public abstract PacketListener getWriterListener();
  
  public abstract Reader newConnectionReader(Reader paramReader);
  
  public abstract Writer newConnectionWriter(Writer paramWriter);
  
  public abstract void userHasLogged(String paramString);
}


/* Location:           C:\Users\Administrator\Documents\ds\反编译\AndroidKiller_v1.3.1\projects\Jungledsq_ppc\ProjectSrc\smali\
 * Qualified Name:     org.jivesoftware.smack.debugger.SmackDebugger
 * JD-Core Version:    0.7.0.1
 */