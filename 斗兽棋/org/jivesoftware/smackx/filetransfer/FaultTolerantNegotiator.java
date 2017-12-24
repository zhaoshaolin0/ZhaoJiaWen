package org.jivesoftware.smackx.filetransfer;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import org.jivesoftware.smack.PacketCollector;
import org.jivesoftware.smack.SmackConfiguration;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.filter.OrFilter;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smackx.packet.StreamInitiation;

public class FaultTolerantNegotiator
  extends StreamNegotiator
{
  private XMPPConnection connection;
  private PacketFilter primaryFilter;
  private StreamNegotiator primaryNegotiator;
  private PacketFilter secondaryFilter;
  private StreamNegotiator secondaryNegotiator;
  
  public FaultTolerantNegotiator(XMPPConnection paramXMPPConnection, StreamNegotiator paramStreamNegotiator1, StreamNegotiator paramStreamNegotiator2)
  {
    this.primaryNegotiator = paramStreamNegotiator1;
    this.secondaryNegotiator = paramStreamNegotiator2;
    this.connection = paramXMPPConnection;
  }
  
  private StreamNegotiator determineNegotiator(Packet paramPacket)
  {
    if (this.primaryFilter.accept(paramPacket)) {
      return this.primaryNegotiator;
    }
    return this.secondaryNegotiator;
  }
  
  public void cleanup() {}
  
  public InputStream createIncomingStream(StreamInitiation paramStreamInitiation)
    throws XMPPException
  {
    PacketCollector localPacketCollector = this.connection.createPacketCollector(getInitiationPacketFilter(paramStreamInitiation.getFrom(), paramStreamInitiation.getSessionID()));
    this.connection.sendPacket(super.createInitiationAccept(paramStreamInitiation, getNamespaces()));
    ExecutorCompletionService localExecutorCompletionService = new ExecutorCompletionService(Executors.newFixedThreadPool(2));
    ArrayList localArrayList = new ArrayList();
    Object localObject1 = null;
    int i;
    int j;
    Object localObject2;
    try
    {
      localArrayList.add(localExecutorCompletionService.submit(new NegotiatorService(localPacketCollector)));
      localArrayList.add(localExecutorCompletionService.submit(new NegotiatorService(localPacketCollector)));
      i = 0;
      paramStreamInitiation = null;
    }
    finally {}
    if (localObject1 == null) {}
    Iterator localIterator;
    localObject1 = localArrayList.iterator();
    while (((Iterator)localObject1).hasNext()) {
      ((Future)((Iterator)localObject1).next()).cancel(true);
    }
    localPacketCollector.cancel();
    throw paramStreamInitiation;
    throw new XMPPException("File transfer negotiation failed.");
    return localObject1;
  }
  
  public OutputStream createOutgoingStream(String paramString1, String paramString2, String paramString3)
    throws XMPPException
  {
    try
    {
      OutputStream localOutputStream = this.primaryNegotiator.createOutgoingStream(paramString1, paramString2, paramString3);
      return localOutputStream;
    }
    catch (XMPPException localXMPPException) {}
    return this.secondaryNegotiator.createOutgoingStream(paramString1, paramString2, paramString3);
  }
  
  public PacketFilter getInitiationPacketFilter(String paramString1, String paramString2)
  {
    if ((this.primaryFilter == null) || (this.secondaryFilter == null))
    {
      this.primaryFilter = this.primaryNegotiator.getInitiationPacketFilter(paramString1, paramString2);
      this.secondaryFilter = this.secondaryNegotiator.getInitiationPacketFilter(paramString1, paramString2);
    }
    return new OrFilter(this.primaryFilter, this.secondaryFilter);
  }
  
  public String[] getNamespaces()
  {
    String[] arrayOfString1 = this.primaryNegotiator.getNamespaces();
    String[] arrayOfString2 = this.secondaryNegotiator.getNamespaces();
    String[] arrayOfString3 = new String[arrayOfString1.length + arrayOfString2.length];
    System.arraycopy(arrayOfString1, 0, arrayOfString3, 0, arrayOfString1.length);
    System.arraycopy(arrayOfString2, 0, arrayOfString3, arrayOfString1.length, arrayOfString2.length);
    return arrayOfString3;
  }
  
  final Packet initiateIncomingStream(XMPPConnection paramXMPPConnection, StreamInitiation paramStreamInitiation)
  {
    throw new UnsupportedOperationException("Initiation handled by createIncomingStream method");
  }
  
  InputStream negotiateIncomingStream(Packet paramPacket)
    throws XMPPException
  {
    throw new UnsupportedOperationException("Negotiation only handled by create incoming stream method.");
  }
  
  private class NegotiatorService
    implements Callable<InputStream>
  {
    private PacketCollector collector;
    
    NegotiatorService(PacketCollector paramPacketCollector)
    {
      this.collector = paramPacketCollector;
    }
    
    public InputStream call()
      throws Exception
    {
      Packet localPacket = this.collector.nextResult(SmackConfiguration.getPacketReplyTimeout() * 2);
      if (localPacket == null) {
        throw new XMPPException("No response from remote client");
      }
      return FaultTolerantNegotiator.this.determineNegotiator(localPacket).negotiateIncomingStream(localPacket);
    }
  }
}


/* Location:           C:\Users\Administrator\Documents\ds\反编译\AndroidKiller_v1.3.1\projects\Jungledsq_ppc\ProjectSrc\smali\
 * Qualified Name:     org.jivesoftware.smackx.filetransfer.FaultTolerantNegotiator
 * JD-Core Version:    0.7.0.1
 */