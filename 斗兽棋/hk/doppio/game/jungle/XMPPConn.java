package hk.doppio.game.jungle;

import android.os.Build;
import android.util.Log;
import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;

public class XMPPConn
{
  static final int INIT_WAIT = 5000;
  static final int SEND_WAIT = 1000;
  static final String tag = "doppioDebug_XMPP";
  String CHESS = "";
  String FROM = "";
  String GAME_ID = "";
  String MOVE = "";
  String RQ_TYPE = "";
  String TO = "";
  Chat chat = null;
  int chess = -1;
  XMPPConnection connection = null;
  XMPPConnection connection_list = null;
  String device_id = "0";
  GameThread gameThread;
  volatile boolean game_established = false;
  volatile boolean initialized = false;
  int move = -1;
  volatile boolean move_received = false;
  volatile boolean move_receiving = false;
  volatile boolean move_sending = false;
  volatile boolean move_sent = false;
  volatile boolean my_turn = false;
  String opponent_id = "0";
  PacketListener packetListener = null;
  volatile boolean runflag = true;
  volatile boolean wait_for_ACK = false;
  
  public XMPPConn(GameThread paramGameThread)
  {
    this(getDeviceID(), paramGameThread);
  }
  
  public XMPPConn(String paramString, GameThread paramGameThread)
  {
    this.device_id = paramString;
    this.gameThread = paramGameThread;
    if (GameActivity.debugMode) {
      Log.d("doppioDebug_XMPP", paramString);
    }
  }
  
  public static String getDeviceID()
  {
    return Float.toHexString(Float.parseFloat("35" + Build.BOARD.length() % 10 + Build.BRAND.length() % 10 + Build.CPU_ABI.length() % 10 + Build.DEVICE.length() % 10 + Build.DISPLAY.length() % 10 + Build.HOST.length() % 10 + Build.ID.length() % 10 + Build.MANUFACTURER.length() % 10 + Build.MODEL.length() % 10 + Build.PRODUCT.length() % 10 + Build.TAGS.length() % 10 + Build.TYPE.length() % 10 + Build.USER.length() % 10));
  }
  
  void closeConn()
    throws XMPPException
  {
    this.runflag = false;
    if (GameActivity.debugMode) {
      Log.d("doppioDebug_XMPP", "Sending end game request...");
    }
    String str = "0," + this.device_id + "," + this.opponent_id + "," + "QUIT_GAME" + "," + "-1" + "," + "-1";
    if (this.game_established) {
      sendMsg(str);
    }
    this.connection.disconnect();
    this.connection_list.disconnect();
  }
  
  public void destroy()
  {
    try
    {
      closeConn();
      return;
    }
    catch (Exception localException)
    {
      localException.printStackTrace();
    }
  }
  
  boolean doMove(int paramInt1, int paramInt2)
    throws XMPPException
  {
    if (GameActivity.debugMode) {
      Log.d("doppioDebug_XMPP", "Sending move...");
    }
    for (;;)
    {
      if ((!this.runflag) || (!this.my_turn))
      {
        if (GameActivity.debugMode) {
          Log.d("doppioDebug_XMPP", "Move sent and ACKed.");
        }
        return true;
      }
      String str = "0," + this.device_id + "," + this.opponent_id + "," + "MOVE_RQ" + "," + paramInt1 + "," + paramInt2;
      this.chat.sendMessage(str);
      if (!this.wait_for_ACK) {
        this.wait_for_ACK = true;
      }
      try
      {
        Thread.sleep(1000L);
      }
      catch (InterruptedException localInterruptedException) {}
    }
    return false;
  }
  
  public int getChess()
  {
    int i = -1;
    if (this.move_received) {
      i = this.chess;
    }
    return i;
  }
  
  public int getMove()
  {
    int i = -1;
    if (this.move_received) {
      i = this.move;
    }
    return i;
  }
  
  public void init()
  {
    this.move_received = false;
    this.move_sent = false;
    this.move_receiving = false;
    this.move_sending = false;
    this.wait_for_ACK = false;
    this.my_turn = false;
    new Thread()
    {
      public void run()
      {
        boolean bool;
        do
        {
          if (!XMPPConn.this.runflag) {
            break;
          }
          XMPPConn localXMPPConn = XMPPConn.this;
          bool = XMPPConn.this.initConn();
          localXMPPConn.initialized = bool;
        } while (!bool);
        for (;;)
        {
          if ((!XMPPConn.this.runflag) || (XMPPConn.this.game_established)) {
            return;
          }
          try
          {
            XMPPConn.this.newGameRQ();
            try
            {
              Thread.sleep(5000L);
            }
            catch (InterruptedException localInterruptedException) {}
          }
          catch (Exception localException)
          {
            localException.printStackTrace();
          }
        }
      }
    }.start();
  }
  
  /* Error */
  boolean initConn()
  {
    // Byte code:
    //   0: aload_0
    //   1: invokevirtual 267	hk/doppio/game/jungle/XMPPConn:newPacketListener	()V
    //   4: aload_0
    //   5: getfield 79	hk/doppio/game/jungle/XMPPConn:initialized	Z
    //   8: ifeq +5 -> 13
    //   11: iconst_1
    //   12: ireturn
    //   13: getstatic 120	hk/doppio/game/jungle/GameActivity:debugMode	Z
    //   16: ifeq +12 -> 28
    //   19: ldc 21
    //   21: ldc_w 269
    //   24: invokestatic 126	android/util/Log:d	(Ljava/lang/String;Ljava/lang/String;)I
    //   27: pop
    //   28: new 271	org/jivesoftware/smack/ConnectionConfiguration
    //   31: dup
    //   32: ldc_w 273
    //   35: sipush 5222
    //   38: ldc_w 275
    //   41: invokespecial 278	org/jivesoftware/smack/ConnectionConfiguration:<init>	(Ljava/lang/String;ILjava/lang/String;)V
    //   44: astore_1
    //   45: aload_1
    //   46: iconst_1
    //   47: invokevirtual 282	org/jivesoftware/smack/ConnectionConfiguration:setCompressionEnabled	(Z)V
    //   50: aload_1
    //   51: iconst_0
    //   52: invokevirtual 285	org/jivesoftware/smack/ConnectionConfiguration:setSASLAuthenticationEnabled	(Z)V
    //   55: getstatic 120	hk/doppio/game/jungle/GameActivity:debugMode	Z
    //   58: ifeq +12 -> 70
    //   61: ldc 21
    //   63: ldc_w 287
    //   66: invokestatic 126	android/util/Log:d	(Ljava/lang/String;Ljava/lang/String;)I
    //   69: pop
    //   70: aload_0
    //   71: new 218	org/jivesoftware/smack/XMPPConnection
    //   74: dup
    //   75: aload_1
    //   76: invokespecial 290	org/jivesoftware/smack/XMPPConnection:<init>	(Lorg/jivesoftware/smack/ConnectionConfiguration;)V
    //   79: putfield 73	hk/doppio/game/jungle/XMPPConn:connection	Lorg/jivesoftware/smack/XMPPConnection;
    //   82: getstatic 120	hk/doppio/game/jungle/GameActivity:debugMode	Z
    //   85: ifeq +12 -> 97
    //   88: ldc 21
    //   90: ldc_w 292
    //   93: invokestatic 126	android/util/Log:d	(Ljava/lang/String;Ljava/lang/String;)I
    //   96: pop
    //   97: aload_0
    //   98: getfield 73	hk/doppio/game/jungle/XMPPConn:connection	Lorg/jivesoftware/smack/XMPPConnection;
    //   101: invokevirtual 295	org/jivesoftware/smack/XMPPConnection:connect	()V
    //   104: getstatic 120	hk/doppio/game/jungle/GameActivity:debugMode	Z
    //   107: ifeq +12 -> 119
    //   110: ldc 21
    //   112: ldc_w 297
    //   115: invokestatic 126	android/util/Log:d	(Ljava/lang/String;Ljava/lang/String;)I
    //   118: pop
    //   119: aload_0
    //   120: getfield 73	hk/doppio/game/jungle/XMPPConn:connection	Lorg/jivesoftware/smack/XMPPConnection;
    //   123: ldc_w 299
    //   126: ldc_w 301
    //   129: new 128	java/lang/StringBuilder
    //   132: dup
    //   133: ldc_w 303
    //   136: invokespecial 133	java/lang/StringBuilder:<init>	(Ljava/lang/String;)V
    //   139: aload_0
    //   140: getfield 67	hk/doppio/game/jungle/XMPPConn:device_id	Ljava/lang/String;
    //   143: invokevirtual 207	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   146: invokevirtual 187	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   149: invokevirtual 307	org/jivesoftware/smack/XMPPConnection:login	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)V
    //   152: getstatic 120	hk/doppio/game/jungle/GameActivity:debugMode	Z
    //   155: ifeq +12 -> 167
    //   158: ldc 21
    //   160: ldc_w 309
    //   163: invokestatic 126	android/util/Log:d	(Ljava/lang/String;Ljava/lang/String;)I
    //   166: pop
    //   167: aload_0
    //   168: new 218	org/jivesoftware/smack/XMPPConnection
    //   171: dup
    //   172: aload_1
    //   173: invokespecial 290	org/jivesoftware/smack/XMPPConnection:<init>	(Lorg/jivesoftware/smack/ConnectionConfiguration;)V
    //   176: putfield 75	hk/doppio/game/jungle/XMPPConn:connection_list	Lorg/jivesoftware/smack/XMPPConnection;
    //   179: aload_0
    //   180: getfield 75	hk/doppio/game/jungle/XMPPConn:connection_list	Lorg/jivesoftware/smack/XMPPConnection;
    //   183: invokevirtual 295	org/jivesoftware/smack/XMPPConnection:connect	()V
    //   186: aload_0
    //   187: getfield 75	hk/doppio/game/jungle/XMPPConn:connection_list	Lorg/jivesoftware/smack/XMPPConnection;
    //   190: ldc_w 311
    //   193: ldc_w 301
    //   196: new 128	java/lang/StringBuilder
    //   199: dup
    //   200: ldc_w 313
    //   203: invokespecial 133	java/lang/StringBuilder:<init>	(Ljava/lang/String;)V
    //   206: aload_0
    //   207: getfield 67	hk/doppio/game/jungle/XMPPConn:device_id	Ljava/lang/String;
    //   210: invokevirtual 207	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   213: invokevirtual 187	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   216: invokevirtual 307	org/jivesoftware/smack/XMPPConnection:login	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)V
    //   219: new 6	hk/doppio/game/jungle/XMPPConn$1
    //   222: dup
    //   223: aload_0
    //   224: invokespecial 314	hk/doppio/game/jungle/XMPPConn$1:<init>	(Lhk/doppio/game/jungle/XMPPConn;)V
    //   227: astore_1
    //   228: aload_0
    //   229: aload_0
    //   230: getfield 73	hk/doppio/game/jungle/XMPPConn:connection	Lorg/jivesoftware/smack/XMPPConnection;
    //   233: invokevirtual 318	org/jivesoftware/smack/XMPPConnection:getChatManager	()Lorg/jivesoftware/smack/ChatManager;
    //   236: ldc_w 311
    //   239: aload_1
    //   240: invokevirtual 324	org/jivesoftware/smack/ChatManager:createChat	(Ljava/lang/String;Lorg/jivesoftware/smack/MessageListener;)Lorg/jivesoftware/smack/Chat;
    //   243: putfield 71	hk/doppio/game/jungle/XMPPConn:chat	Lorg/jivesoftware/smack/Chat;
    //   246: new 326	org/jivesoftware/smack/filter/AndFilter
    //   249: dup
    //   250: iconst_2
    //   251: anewarray 328	org/jivesoftware/smack/filter/PacketFilter
    //   254: dup
    //   255: iconst_0
    //   256: new 330	org/jivesoftware/smack/filter/PacketTypeFilter
    //   259: dup
    //   260: ldc_w 332
    //   263: invokespecial 335	org/jivesoftware/smack/filter/PacketTypeFilter:<init>	(Ljava/lang/Class;)V
    //   266: aastore
    //   267: dup
    //   268: iconst_1
    //   269: new 337	org/jivesoftware/smack/filter/FromContainsFilter
    //   272: dup
    //   273: ldc_w 299
    //   276: invokespecial 338	org/jivesoftware/smack/filter/FromContainsFilter:<init>	(Ljava/lang/String;)V
    //   279: aastore
    //   280: invokespecial 341	org/jivesoftware/smack/filter/AndFilter:<init>	([Lorg/jivesoftware/smack/filter/PacketFilter;)V
    //   283: astore_1
    //   284: aload_0
    //   285: getfield 75	hk/doppio/game/jungle/XMPPConn:connection_list	Lorg/jivesoftware/smack/XMPPConnection;
    //   288: aload_0
    //   289: getfield 113	hk/doppio/game/jungle/XMPPConn:packetListener	Lorg/jivesoftware/smack/PacketListener;
    //   292: aload_1
    //   293: invokevirtual 345	org/jivesoftware/smack/XMPPConnection:addPacketListener	(Lorg/jivesoftware/smack/PacketListener;Lorg/jivesoftware/smack/filter/PacketFilter;)V
    //   296: getstatic 120	hk/doppio/game/jungle/GameActivity:debugMode	Z
    //   299: ifeq +12 -> 311
    //   302: ldc 21
    //   304: ldc_w 347
    //   307: invokestatic 126	android/util/Log:d	(Ljava/lang/String;Ljava/lang/String;)I
    //   310: pop
    //   311: iconst_1
    //   312: ireturn
    //   313: astore_1
    //   314: aload_1
    //   315: invokevirtual 348	org/jivesoftware/smack/XMPPException:printStackTrace	()V
    //   318: getstatic 120	hk/doppio/game/jungle/GameActivity:debugMode	Z
    //   321: ifeq +13 -> 334
    //   324: ldc 21
    //   326: aload_1
    //   327: invokevirtual 349	org/jivesoftware/smack/XMPPException:toString	()Ljava/lang/String;
    //   330: invokestatic 126	android/util/Log:d	(Ljava/lang/String;Ljava/lang/String;)I
    //   333: pop
    //   334: iconst_0
    //   335: ireturn
    //   336: astore_1
    //   337: aload_1
    //   338: invokevirtual 348	org/jivesoftware/smack/XMPPException:printStackTrace	()V
    //   341: getstatic 120	hk/doppio/game/jungle/GameActivity:debugMode	Z
    //   344: ifeq +13 -> 357
    //   347: ldc 21
    //   349: aload_1
    //   350: invokevirtual 349	org/jivesoftware/smack/XMPPException:toString	()Ljava/lang/String;
    //   353: invokestatic 126	android/util/Log:d	(Ljava/lang/String;Ljava/lang/String;)I
    //   356: pop
    //   357: iconst_0
    //   358: ireturn
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	359	0	this	XMPPConn
    //   44	249	1	localObject	Object
    //   313	14	1	localXMPPException1	XMPPException
    //   336	14	1	localXMPPException2	XMPPException
    // Exception table:
    //   from	to	target	type
    //   82	97	313	org/jivesoftware/smack/XMPPException
    //   97	119	313	org/jivesoftware/smack/XMPPException
    //   119	167	313	org/jivesoftware/smack/XMPPException
    //   179	219	336	org/jivesoftware/smack/XMPPException
  }
  
  public void moveReceived()
  {
    this.move_received = false;
    this.move_sent = false;
  }
  
  void newGameRQ()
    throws XMPPException
  {
    if (GameActivity.debugMode) {
      Log.d("doppioDebug_XMPP", "Sending new game request...");
    }
    String str = "0," + this.device_id + "," + "0" + "," + "NEW_GAME_RQ" + "," + "-1" + "," + "-1";
    this.chat.sendMessage(str);
  }
  
  void newPacketListener()
  {
    this.packetListener = new PacketListener()
    {
      public void processPacket(Packet paramAnonymousPacket)
      {
        if (!XMPPConn.this.runflag) {}
        String str4;
        label925:
        do
        {
          do
          {
            String str1;
            String str3;
            do
            {
              String str5;
              do
              {
                do
                {
                  do
                  {
                    return;
                    str2 = "";
                    str5 = "";
                    str4 = "";
                    str1 = "";
                    str3 = "";
                    if (paramAnonymousPacket == null) {
                      break;
                    }
                    paramAnonymousPacket = paramAnonymousPacket.toXML();
                    paramAnonymousPacket = paramAnonymousPacket.substring(paramAnonymousPacket.indexOf("<body>") + "<body>".length(), paramAnonymousPacket.indexOf("</body>")).split(",");
                  } while (paramAnonymousPacket.length != 6);
                  str1 = paramAnonymousPacket[0];
                  String str2 = paramAnonymousPacket[1];
                  str5 = paramAnonymousPacket[2];
                  str4 = paramAnonymousPacket[3];
                  str1 = paramAnonymousPacket[4];
                  str3 = paramAnonymousPacket[5];
                  if ((XMPPConn.this.initialized) && (!XMPPConn.this.game_established))
                  {
                    if (!XMPPConn.this.wait_for_ACK)
                    {
                      if ((!str2.equals(XMPPConn.this.device_id)) && (str4.equals("NEW_GAME_RQ")))
                      {
                        if (GameActivity.debugMode) {
                          Log.d("doppioDebug_XMPP", "New game request caught!  Let me reply...");
                        }
                        paramAnonymousPacket = "0," + XMPPConn.this.device_id + "," + str2 + "," + "NEW_GAME_ACK" + "," + "-1" + "," + "-1";
                        XMPPConn.this.sendMsg(paramAnonymousPacket);
                        XMPPConn.this.wait_for_ACK = true;
                      }
                      if ((str5.equals(XMPPConn.this.device_id)) && (str4.equals("NEW_GAME_ACK")))
                      {
                        if (GameActivity.debugMode) {
                          Log.d("doppioDebug_XMPP", "Reply caught!  Let me ack...");
                        }
                        paramAnonymousPacket = "0," + XMPPConn.this.device_id + "," + str2 + "," + "NEW_GAME_ACK" + "," + "-1" + "," + "-1";
                        XMPPConn.this.sendMsg(paramAnonymousPacket);
                        if (GameActivity.debugMode) {
                          Log.d("doppioDebug_XMPP", "Game established with " + str2);
                        }
                        XMPPConn.this.opponent_id = str2;
                        XMPPConn.this.my_turn = false;
                        XMPPConn.this.game_established = true;
                      }
                    }
                    if ((XMPPConn.this.wait_for_ACK) && (str5.equals(XMPPConn.this.device_id)) && (str4.equals("NEW_GAME_ACK")))
                    {
                      if (GameActivity.debugMode) {
                        Log.d("doppioDebug_XMPP", "Game established with " + str2);
                      }
                      XMPPConn.this.opponent_id = str2;
                      XMPPConn.this.my_turn = true;
                      XMPPConn.this.game_established = true;
                    }
                  }
                } while (!str5.equals(XMPPConn.this.device_id));
                if (GameActivity.debugMode) {
                  Log.d("doppioDebug_XMPP", str4 + " received.");
                }
                if (GameActivity.debugMode) {
                  Log.d("doppioDebug_XMPP", "My Turn: " + XMPPConn.this.my_turn);
                }
                if (GameActivity.debugMode) {
                  Log.d("doppioDebug_XMPP", "Wait for ACK: " + XMPPConn.this.wait_for_ACK);
                }
              } while (!XMPPConn.this.game_established);
              if ((str5.equals(XMPPConn.this.device_id)) && (str4.equals("QUIT_GAME")))
              {
                if (GameActivity.debugMode) {
                  Log.d("doppioDebug_XMPP", "Opponent exit game!");
                }
                XMPPConn.this.game_established = false;
                XMPPConn.this.gameThread.sendInterrupt();
              }
              if (XMPPConn.this.my_turn) {
                break label925;
              }
              if (XMPPConn.this.wait_for_ACK) {
                break;
              }
            } while (!str4.equals("MOVE_RQ"));
            if (GameActivity.debugMode) {
              Log.d("doppioDebug_XMPP", "Opponent's next move is " + str1 + " to " + str3);
            }
            XMPPConn.this.chess = Integer.parseInt(str1);
            XMPPConn.this.move = Integer.parseInt(str3);
            paramAnonymousPacket = "0," + XMPPConn.this.device_id + "," + XMPPConn.this.opponent_id + "," + "MOVE_ACK" + "," + "-1" + "," + "-1";
            XMPPConn.this.sendMsg(paramAnonymousPacket);
            if (GameActivity.debugMode) {
              Log.d("doppioDebug_XMPP", "Wait for MOVE_ACK...");
            }
            XMPPConn.this.wait_for_ACK = true;
            return;
          } while (!str4.equals("MOVE_ACK"));
          if (GameActivity.debugMode) {
            Log.d("doppioDebug_XMPP", "MOVE_ACK received.");
          }
          XMPPConn.this.move_received = true;
          XMPPConn.this.wait_for_ACK = false;
          XMPPConn.this.my_turn = true;
          XMPPConn.this.gameThread.sendInterrupt();
          return;
        } while ((!XMPPConn.this.wait_for_ACK) || (!str4.equals("MOVE_ACK")));
        if (GameActivity.debugMode) {
          Log.d("doppioDebug_XMPP", "Opponent received my move.");
        }
        paramAnonymousPacket = "0," + XMPPConn.this.device_id + "," + XMPPConn.this.opponent_id + "," + "MOVE_ACK" + "," + "-1" + "," + "-1";
        XMPPConn.this.sendMsg(paramAnonymousPacket);
        XMPPConn.this.move_sent = true;
        XMPPConn.this.wait_for_ACK = false;
        XMPPConn.this.my_turn = false;
      }
    };
  }
  
  public void sendMove(final int paramInt1, final int paramInt2)
  {
    if ((this.move_sending) || (this.move_sent)) {
      return;
    }
    new Thread()
    {
      public void run()
      {
        XMPPConn.this.move_sending = true;
        try
        {
          XMPPConn.this.doMove(paramInt1, paramInt2);
          return;
        }
        catch (Exception localException)
        {
          localException.printStackTrace();
          return;
        }
        finally
        {
          XMPPConn.this.move_sending = false;
        }
      }
    }.start();
  }
  
  void sendMsg(String paramString)
  {
    try
    {
      this.chat.sendMessage(paramString);
      return;
    }
    catch (XMPPException paramString)
    {
      paramString.printStackTrace();
    }
  }
}


/* Location:           C:\Users\Administrator\Documents\ds\反编译\AndroidKiller_v1.3.1\projects\Jungledsq_ppc\ProjectSrc\smali\
 * Qualified Name:     hk.doppio.game.jungle.XMPPConn
 * JD-Core Version:    0.7.0.1
 */