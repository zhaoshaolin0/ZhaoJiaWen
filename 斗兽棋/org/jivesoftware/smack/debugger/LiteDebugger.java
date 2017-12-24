package org.jivesoftware.smack.debugger;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.Reader;
import java.io.Writer;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.util.ObservableReader;
import org.jivesoftware.smack.util.ObservableWriter;
import org.jivesoftware.smack.util.ReaderListener;
import org.jivesoftware.smack.util.StringUtils;
import org.jivesoftware.smack.util.WriterListener;

public class LiteDebugger
  implements SmackDebugger
{
  private static final String NEWLINE = "\n";
  private XMPPConnection connection = null;
  private JFrame frame = null;
  private PacketListener listener = null;
  private Reader reader;
  private ReaderListener readerListener;
  private Writer writer;
  private WriterListener writerListener;
  
  public LiteDebugger(XMPPConnection paramXMPPConnection, Writer paramWriter, Reader paramReader)
  {
    this.connection = paramXMPPConnection;
    this.writer = paramWriter;
    this.reader = paramReader;
    createDebug();
  }
  
  private void createDebug()
  {
    this.frame = new JFrame("Smack Debug Window -- " + this.connection.getServiceName() + ":" + this.connection.getPort());
    this.frame.addWindowListener(new WindowAdapter()
    {
      public void windowClosing(WindowEvent paramAnonymousWindowEvent)
      {
        LiteDebugger.this.rootWindowClosing(paramAnonymousWindowEvent);
      }
    });
    Object localObject1 = new JTabbedPane();
    Object localObject4 = new JPanel();
    ((JPanel)localObject4).setLayout(new GridLayout(3, 1));
    ((JTabbedPane)localObject1).add("All", (Component)localObject4);
    final JTextArea localJTextArea1 = new JTextArea();
    final JTextArea localJTextArea2 = new JTextArea();
    localJTextArea1.setEditable(false);
    localJTextArea2.setEditable(false);
    localJTextArea1.setForeground(new Color(112, 3, 3));
    localJTextArea2.setForeground(new Color(112, 3, 3));
    ((JPanel)localObject4).add(new JScrollPane(localJTextArea1));
    ((JTabbedPane)localObject1).add("Sent", new JScrollPane(localJTextArea2));
    Object localObject2 = new JPopupMenu();
    Object localObject3 = new JMenuItem("Copy");
    ((JMenuItem)localObject3).addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(localJTextArea1.getText()), null);
      }
    });
    Object localObject5 = new JMenuItem("Clear");
    ((JMenuItem)localObject5).addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        localJTextArea1.setText("");
        localJTextArea2.setText("");
      }
    });
    Object localObject6 = new PopupListener((JPopupMenu)localObject2);
    localJTextArea1.addMouseListener((MouseListener)localObject6);
    localJTextArea2.addMouseListener((MouseListener)localObject6);
    ((JPopupMenu)localObject2).add((JMenuItem)localObject3);
    ((JPopupMenu)localObject2).add((JMenuItem)localObject5);
    localObject2 = new JTextArea();
    localObject3 = new JTextArea();
    ((JTextArea)localObject2).setEditable(false);
    ((JTextArea)localObject3).setEditable(false);
    ((JTextArea)localObject2).setForeground(new Color(6, 76, 133));
    ((JTextArea)localObject3).setForeground(new Color(6, 76, 133));
    ((JPanel)localObject4).add(new JScrollPane((Component)localObject2));
    ((JTabbedPane)localObject1).add("Received", new JScrollPane((Component)localObject3));
    localObject5 = new JPopupMenu();
    localObject6 = new JMenuItem("Copy");
    ((JMenuItem)localObject6).addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(this.val$receivedText1.getText()), null);
      }
    });
    JMenuItem localJMenuItem = new JMenuItem("Clear");
    localJMenuItem.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        this.val$receivedText1.setText("");
        this.val$receivedText2.setText("");
      }
    });
    Object localObject7 = new PopupListener((JPopupMenu)localObject5);
    ((JTextArea)localObject2).addMouseListener((MouseListener)localObject7);
    ((JTextArea)localObject3).addMouseListener((MouseListener)localObject7);
    ((JPopupMenu)localObject5).add((JMenuItem)localObject6);
    ((JPopupMenu)localObject5).add(localJMenuItem);
    localObject5 = new JTextArea();
    localObject6 = new JTextArea();
    ((JTextArea)localObject5).setEditable(false);
    ((JTextArea)localObject6).setEditable(false);
    ((JTextArea)localObject5).setForeground(new Color(1, 94, 35));
    ((JTextArea)localObject6).setForeground(new Color(1, 94, 35));
    ((JPanel)localObject4).add(new JScrollPane((Component)localObject5));
    ((JTabbedPane)localObject1).add("Interpreted", new JScrollPane((Component)localObject6));
    localObject4 = new JPopupMenu();
    localJMenuItem = new JMenuItem("Copy");
    localJMenuItem.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(this.val$interpretedText1.getText()), null);
      }
    });
    localObject7 = new JMenuItem("Clear");
    ((JMenuItem)localObject7).addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        this.val$interpretedText1.setText("");
        this.val$interpretedText2.setText("");
      }
    });
    PopupListener localPopupListener = new PopupListener((JPopupMenu)localObject4);
    ((JTextArea)localObject5).addMouseListener(localPopupListener);
    ((JTextArea)localObject6).addMouseListener(localPopupListener);
    ((JPopupMenu)localObject4).add(localJMenuItem);
    ((JPopupMenu)localObject4).add((JMenuItem)localObject7);
    this.frame.getContentPane().add((Component)localObject1);
    this.frame.setSize(550, 400);
    this.frame.setVisible(true);
    localObject1 = new ObservableReader(this.reader);
    this.readerListener = new ReaderListener()
    {
      public void read(String paramAnonymousString)
      {
        int i = paramAnonymousString.lastIndexOf(">");
        if (i != -1)
        {
          this.val$receivedText1.append(paramAnonymousString.substring(0, i + 1));
          this.val$receivedText2.append(paramAnonymousString.substring(0, i + 1));
          this.val$receivedText1.append("\n");
          this.val$receivedText2.append("\n");
          if (paramAnonymousString.length() > i)
          {
            this.val$receivedText1.append(paramAnonymousString.substring(i + 1));
            this.val$receivedText2.append(paramAnonymousString.substring(i + 1));
          }
          return;
        }
        this.val$receivedText1.append(paramAnonymousString);
        this.val$receivedText2.append(paramAnonymousString);
      }
    };
    ((ObservableReader)localObject1).addReaderListener(this.readerListener);
    localObject2 = new ObservableWriter(this.writer);
    this.writerListener = new WriterListener()
    {
      public void write(String paramAnonymousString)
      {
        localJTextArea1.append(paramAnonymousString);
        localJTextArea2.append(paramAnonymousString);
        if (paramAnonymousString.endsWith(">"))
        {
          localJTextArea1.append("\n");
          localJTextArea2.append("\n");
        }
      }
    };
    ((ObservableWriter)localObject2).addWriterListener(this.writerListener);
    this.reader = ((Reader)localObject1);
    this.writer = ((Writer)localObject2);
    this.listener = new PacketListener()
    {
      public void processPacket(Packet paramAnonymousPacket)
      {
        this.val$interpretedText1.append(paramAnonymousPacket.toXML());
        this.val$interpretedText2.append(paramAnonymousPacket.toXML());
        this.val$interpretedText1.append("\n");
        this.val$interpretedText2.append("\n");
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
  
  public void rootWindowClosing(WindowEvent paramWindowEvent)
  {
    this.connection.removePacketListener(this.listener);
    ((ObservableReader)this.reader).removeReaderListener(this.readerListener);
    ((ObservableWriter)this.writer).removeWriterListener(this.writerListener);
  }
  
  public void userHasLogged(String paramString)
  {
    boolean bool = "".equals(StringUtils.parseName(paramString));
    StringBuilder localStringBuilder = new StringBuilder().append("Smack Debug Window -- ");
    if (bool) {}
    for (String str = "";; str = StringUtils.parseBareAddress(paramString))
    {
      str = str + "@" + this.connection.getServiceName() + ":" + this.connection.getPort();
      paramString = str + "/" + StringUtils.parseResource(paramString);
      this.frame.setTitle(paramString);
      return;
    }
  }
  
  private class PopupListener
    extends MouseAdapter
  {
    JPopupMenu popup;
    
    PopupListener(JPopupMenu paramJPopupMenu)
    {
      this.popup = paramJPopupMenu;
    }
    
    private void maybeShowPopup(MouseEvent paramMouseEvent)
    {
      if (paramMouseEvent.isPopupTrigger()) {
        this.popup.show(paramMouseEvent.getComponent(), paramMouseEvent.getX(), paramMouseEvent.getY());
      }
    }
    
    public void mousePressed(MouseEvent paramMouseEvent)
    {
      maybeShowPopup(paramMouseEvent);
    }
    
    public void mouseReleased(MouseEvent paramMouseEvent)
    {
      maybeShowPopup(paramMouseEvent);
    }
  }
}


/* Location:           C:\Users\Administrator\Documents\ds\反编译\AndroidKiller_v1.3.1\projects\Jungledsq_ppc\ProjectSrc\smali\
 * Qualified Name:     org.jivesoftware.smack.debugger.LiteDebugger
 * JD-Core Version:    0.7.0.1
 */