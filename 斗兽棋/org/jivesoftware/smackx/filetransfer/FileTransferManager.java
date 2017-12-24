package org.jivesoftware.smackx.filetransfer;

import java.util.ArrayList;
import java.util.List;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.filter.AndFilter;
import org.jivesoftware.smack.filter.IQTypeFilter;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.filter.PacketTypeFilter;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.packet.IQ.Type;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.packet.XMPPError;
import org.jivesoftware.smack.packet.XMPPError.Condition;
import org.jivesoftware.smackx.packet.StreamInitiation;

public class FileTransferManager
{
  private XMPPConnection connection;
  private final FileTransferNegotiator fileTransferNegotiator;
  private List listeners;
  
  public FileTransferManager(XMPPConnection paramXMPPConnection)
  {
    this.connection = paramXMPPConnection;
    this.fileTransferNegotiator = FileTransferNegotiator.getInstanceFor(paramXMPPConnection);
  }
  
  private void initListeners()
  {
    this.listeners = new ArrayList();
    this.connection.addPacketListener(new PacketListener()new AndFilternew PacketTypeFilter
    {
      public void processPacket(Packet paramAnonymousPacket)
      {
        FileTransferManager.this.fireNewRequest((StreamInitiation)paramAnonymousPacket);
      }
    }, new AndFilter(new PacketFilter[] { new PacketTypeFilter(StreamInitiation.class), new IQTypeFilter(IQ.Type.SET) }));
  }
  
  public void addFileTransferListener(FileTransferListener paramFileTransferListener)
  {
    if (this.listeners == null) {
      initListeners();
    }
    synchronized (this.listeners)
    {
      this.listeners.add(paramFileTransferListener);
      return;
    }
  }
  
  protected IncomingFileTransfer createIncomingFileTransfer(FileTransferRequest paramFileTransferRequest)
  {
    if (paramFileTransferRequest == null) {
      throw new NullPointerException("RecieveRequest cannot be null");
    }
    IncomingFileTransfer localIncomingFileTransfer = new IncomingFileTransfer(paramFileTransferRequest, this.fileTransferNegotiator);
    localIncomingFileTransfer.setFileInfo(paramFileTransferRequest.getFileName(), paramFileTransferRequest.getFileSize());
    return localIncomingFileTransfer;
  }
  
  public OutgoingFileTransfer createOutgoingFileTransfer(String paramString)
  {
    return new OutgoingFileTransfer(this.connection.getUser(), paramString, this.fileTransferNegotiator.getNextStreamID(), this.fileTransferNegotiator);
  }
  
  protected void fireNewRequest(StreamInitiation paramStreamInitiation)
  {
    synchronized (this.listeners)
    {
      FileTransferListener[] arrayOfFileTransferListener = new FileTransferListener[this.listeners.size()];
      this.listeners.toArray(arrayOfFileTransferListener);
      paramStreamInitiation = new FileTransferRequest(this, paramStreamInitiation);
      int i = 0;
      if (i < arrayOfFileTransferListener.length)
      {
        arrayOfFileTransferListener[i].fileTransferRequest(paramStreamInitiation);
        i += 1;
      }
    }
  }
  
  protected void rejectIncomingFileTransfer(FileTransferRequest paramFileTransferRequest)
  {
    paramFileTransferRequest = paramFileTransferRequest.getStreamInitiation();
    paramFileTransferRequest = FileTransferNegotiator.createIQ(paramFileTransferRequest.getPacketID(), paramFileTransferRequest.getFrom(), paramFileTransferRequest.getTo(), IQ.Type.ERROR);
    paramFileTransferRequest.setError(new XMPPError(XMPPError.Condition.forbidden));
    this.connection.sendPacket(paramFileTransferRequest);
  }
  
  public void removeFileTransferListener(FileTransferListener paramFileTransferListener)
  {
    if (this.listeners == null) {
      return;
    }
    synchronized (this.listeners)
    {
      this.listeners.remove(paramFileTransferListener);
      return;
    }
  }
}


/* Location:           C:\Users\Administrator\Documents\ds\反编译\AndroidKiller_v1.3.1\projects\Jungledsq_ppc\ProjectSrc\smali\
 * Qualified Name:     org.jivesoftware.smackx.filetransfer.FileTransferManager
 * JD-Core Version:    0.7.0.1
 */