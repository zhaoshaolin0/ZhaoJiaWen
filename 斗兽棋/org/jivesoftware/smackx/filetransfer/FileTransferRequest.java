package org.jivesoftware.smackx.filetransfer;

import org.jivesoftware.smackx.packet.StreamInitiation;
import org.jivesoftware.smackx.packet.StreamInitiation.File;

public class FileTransferRequest
{
  private final FileTransferManager manager;
  private final StreamInitiation streamInitiation;
  
  public FileTransferRequest(FileTransferManager paramFileTransferManager, StreamInitiation paramStreamInitiation)
  {
    this.streamInitiation = paramStreamInitiation;
    this.manager = paramFileTransferManager;
  }
  
  public IncomingFileTransfer accept()
  {
    return this.manager.createIncomingFileTransfer(this);
  }
  
  public String getDescription()
  {
    return this.streamInitiation.getFile().getDesc();
  }
  
  public String getFileName()
  {
    return this.streamInitiation.getFile().getName();
  }
  
  public long getFileSize()
  {
    return this.streamInitiation.getFile().getSize();
  }
  
  public String getMimeType()
  {
    return this.streamInitiation.getMimeType();
  }
  
  public String getRequestor()
  {
    return this.streamInitiation.getFrom();
  }
  
  public String getStreamID()
  {
    return this.streamInitiation.getSessionID();
  }
  
  protected StreamInitiation getStreamInitiation()
  {
    return this.streamInitiation;
  }
  
  public void reject()
  {
    this.manager.rejectIncomingFileTransfer(this);
  }
}


/* Location:           C:\Users\Administrator\Documents\ds\反编译\AndroidKiller_v1.3.1\projects\Jungledsq_ppc\ProjectSrc\smali\
 * Qualified Name:     org.jivesoftware.smackx.filetransfer.FileTransferRequest
 * JD-Core Version:    0.7.0.1
 */