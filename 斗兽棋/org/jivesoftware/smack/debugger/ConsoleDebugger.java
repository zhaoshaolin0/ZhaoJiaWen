package org.jivesoftware.smack.debugger;

import java.io.PrintStream;
import java.io.Reader;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.jivesoftware.smack.ConnectionListener;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.util.ObservableReader;
import org.jivesoftware.smack.util.ObservableWriter;
import org.jivesoftware.smack.util.ReaderListener;
import org.jivesoftware.smack.util.StringUtils;
import org.jivesoftware.smack.util.WriterListener;

public class ConsoleDebugger
  implements SmackDebugger
{
  public static boolean printInterpreted = false;
  private ConnectionListener connListener = null;
  private XMPPConnection connection = null;
  private SimpleDateFormat dateFormatter = new SimpleDateFormat("hh:mm:ss aaa");
  private PacketListener listener = null;
  private Reader reader;
  private ReaderListener readerListener;
  private Writer writer;
  private WriterListener writerListener;
  
  public ConsoleDebugger(XMPPConnection paramXMPPConnection, Writer paramWriter, Reader paramReader)
  {
    this.connection = paramXMPPConnection;
    this.writer = paramWriter;
    this.reader = paramReader;
    createDebug();
  }
  
  private void createDebug()
  {
    ObservableReader localObservableReader = new ObservableReader(this.reader);
    this.readerListener = new ReaderListener()
    {
      public void read(String paramAnonymousString)
      {
        System.out.println(ConsoleDebugger.this.dateFormatter.format(new Date()) + " RCV  (" + ConsoleDebugger.this.connection.hashCode() + "): " + paramAnonymousString);
      }
    };
    localObservableReader.addReaderListener(this.readerListener);
    ObservableWriter localObservableWriter = new ObservableWriter(this.writer);
    this.writerListener = new WriterListener()
    {
      public void write(String paramAnonymousString)
      {
        System.out.println(ConsoleDebugger.this.dateFormatter.format(new Date()) + " SENT (" + ConsoleDebugger.this.connection.hashCode() + "): " + paramAnonymousString);
      }
    };
    localObservableWriter.addWriterListener(this.writerListener);
    this.reader = localObservableReader;
    this.writer = localObservableWriter;
    this.listener = new PacketListener()
    {
      public void processPacket(Packet paramAnonymousPacket)
      {
        if (ConsoleDebugger.printInterpreted) {
          System.out.println(ConsoleDebugger.this.dateFormatter.format(new Date()) + " RCV PKT (" + ConsoleDebugger.this.connection.hashCode() + "): " + paramAnonymousPacket.toXML());
        }
      }
    };
    this.connListener = new ConnectionListener()
    {
      public void connectionClosed()
      {
        System.out.println(ConsoleDebugger.this.dateFormatter.format(new Date()) + " Connection closed (" + ConsoleDebugger.this.connection.hashCode() + ")");
      }
      
      public void connectionClosedOnError(Exception paramAnonymousException)
      {
        System.out.println(ConsoleDebugger.this.dateFormatter.format(new Date()) + " Connection closed due to an exception (" + ConsoleDebugger.this.connection.hashCode() + ")");
        paramAnonymousException.printStackTrace();
      }
      
      public void reconnectingIn(int paramAnonymousInt)
      {
        System.out.println(ConsoleDebugger.this.dateFormatter.format(new Date()) + " Connection (" + ConsoleDebugger.this.connection.hashCode() + ") will reconnect in " + paramAnonymousInt);
      }
      
      public void reconnectionFailed(Exception paramAnonymousException)
      {
        System.out.println(ConsoleDebugger.this.dateFormatter.format(new Date()) + " Reconnection failed due to an exception (" + ConsoleDebugger.this.connection.hashCode() + ")");
        paramAnonymousException.printStackTrace();
      }
      
      public void reconnectionSuccessful()
      {
        System.out.println(ConsoleDebugger.this.dateFormatter.format(new Date()) + " Connection reconnected (" + ConsoleDebugger.this.connection.hashCode() + ")");
      }
    };
  }
  
  public Reader getReader()
  {
    return this.reader;
  }
  
  public PacketListener getReaderListener()
  {
    return this.listener;
  }
  
  public Writer getWriter()
  {
    return this.writer;
  }
  
  public PacketListener getWriterListener()
  {
    return null;
  }
  
  public Reader newConnectionReader(Reader paramReader)
  {
    ((ObservableReader)this.reader).removeReaderListener(this.readerListener);
    paramReader = new ObservableReader(paramReader);
    paramReader.addReaderListener(this.readerListener);
    this.reader = paramReader;
    return this.reader;
  }
  
  public Writer newConnectionWriter(Writer paramWriter)
  {
    ((ObservableWriter)this.writer).removeWriterListener(this.writerListener);
    paramWriter = new ObservableWriter(paramWriter);
    paramWriter.addWriterListener(this.writerListener);
    this.writer = paramWriter;
    return this.writer;
  }
  
  public void userHasLogged(String paramString)
  {
    boolean bool = "".equals(StringUtils.parseName(paramString));
    StringBuilder localStringBuilder = new StringBuilder().append("User logged (").append(this.connection.hashCode()).append("): ");
    if (bool) {}
    for (String str = "";; str = StringUtils.parseBareAddress(paramString))
    {
      str = str + "@" + this.connection.getServiceName() + ":" + this.connection.getPort();
      paramString = str + "/" + StringUtils.parseResource(paramString);
      System.out.println(paramString);
      this.connection.addConnectionListener(this.connListener);
      return;
    }
  }
}


/* Location:           C:\Users\Administrator\Documents\ds\反编译\AndroidKiller_v1.3.1\projects\Jungledsq_ppc\ProjectSrc\smali\
 * Qualified Name:     org.jivesoftware.smack.debugger.ConsoleDebugger
 * JD-Core Version:    0.7.0.1
 */