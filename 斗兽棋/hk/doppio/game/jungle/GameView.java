package hk.doppio.game.jungle;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class GameView
  extends SurfaceView
  implements SurfaceHolder.Callback
{
  static final int NETWORK_AWAITS_MOVE = 2;
  static final int NETWORK_ESTABLISHED = 1;
  static final int NETWORK_INITIALIZING = 0;
  static final int NETWORK_LOSE = 4;
  static final int NETWORK_OPPONENT_EXIT = 9;
  static final int NETWORK_WIN = 3;
  static final int PLAYER_IS_BLUE = 10;
  static final int PLAYER_IS_RED = 11;
  static final int PLAYER_LOSE = 21;
  static final int PLAYER_WIN = 20;
  static final int REMOVE_DIALOGUE = -1;
  Context context;
  int currentNetworkState = -1;
  AlertDialog dialog;
  GameThread thread;
  
  public GameView(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
    this.context = paramContext;
    if (GameActivity.debugMode) {
      Log.d("doppioDebug", "new GameView");
    }
    setKeepScreenOn(true);
    paramAttributeSet = getHolder();
    paramAttributeSet.addCallback(this);
    this.thread = new GameThread(paramAttributeSet, paramContext, new MyHandler());
    setFocusable(true);
  }
  
  public void closeAlertDialog()
  {
    if (this.dialog != null) {
      this.dialog.dismiss();
    }
  }
  
  public GameThread getThread()
  {
    return this.thread;
  }
  
  public boolean onTouchEvent(MotionEvent paramMotionEvent)
  {
    return this.thread.onTouchEvent(paramMotionEvent);
  }
  
  public void openAlertDialog(int paramInt)
  {
    openAlertDialog(this.context.getString(paramInt));
  }
  
  public void openAlertDialog(String paramString)
  {
    closeAlertDialog();
    View localView = LayoutInflater.from(this.context).inflate(2130903041, null);
    AlertDialog.Builder localBuilder = new AlertDialog.Builder(this.context);
    localBuilder.setView(localView);
    ((TextView)localView.findViewById(2131230724)).setText(paramString);
    this.dialog = localBuilder.create();
    this.dialog.show();
  }
  
  public void openToast(int paramInt)
  {
    openToast(this.context.getString(paramInt));
  }
  
  public void openToast(String paramString)
  {
    Toast.makeText(getContext(), paramString, 0).show();
  }
  
  public void surfaceChanged(SurfaceHolder paramSurfaceHolder, int paramInt1, int paramInt2, int paramInt3)
  {
    this.thread.setSurfaceSize(paramInt2, paramInt3);
  }
  
  public void surfaceCreated(SurfaceHolder paramSurfaceHolder)
  {
    if (GameActivity.debugMode) {
      Log.d("doppioDebug", "surfaceCreated");
    }
    this.thread.setRunning(true);
    try
    {
      this.thread.start();
      return;
    }
    catch (IllegalThreadStateException paramSurfaceHolder)
    {
      if ((this.thread.isAlive()) && (GameActivity.debugMode)) {
        Log.d("doppioDebug", "Thread is alive");
      }
      if (GameActivity.debugMode) {
        Log.d("doppioDebug", "Thread already running");
      }
      this.thread.unpause();
    }
  }
  
  public void surfaceDestroyed(SurfaceHolder paramSurfaceHolder)
  {
    if (GameActivity.debugMode) {
      Log.d("doppioDebug", "surfaceDestroyed");
    }
    this.thread.pause();
  }
  
  class MyHandler
    extends Handler
  {
    MyHandler() {}
    
    public void handleMessage(Message paramMessage)
    {
      if (GameView.this.currentNetworkState != paramMessage.what)
      {
        GameView.this.currentNetworkState = paramMessage.what;
        switch (paramMessage.what)
        {
        }
      }
      for (;;)
      {
        super.handleMessage(paramMessage);
        return;
        GameView.this.closeAlertDialog();
        continue;
        GameView.this.openAlertDialog(2131099651);
        continue;
        GameView.this.openAlertDialog(2131099652);
        continue;
        GameView.this.openAlertDialog(2131099653);
        continue;
        GameView.this.openToast(2131099654);
        continue;
        GameView.this.openToast(2131099655);
        continue;
        GameView.this.openToast(2131099656);
        continue;
        GameView.this.openToast(2131099657);
        while ((!GameView.this.thread.xmpp.move_sent) && (GameView.this.thread.xmpp.move_sending)) {}
        GameView.this.thread.xmpp.game_established = false;
        GameView.this.thread.sendInterrupt();
        GameView.this.thread.newGame();
        ((Activity)GameView.this.context).finish();
        continue;
        GameView.this.openToast(2131099658);
        GameView.this.thread.xmpp.game_established = false;
        GameView.this.thread.sendInterrupt();
        GameView.this.thread.newGame();
        ((Activity)GameView.this.context).finish();
        continue;
        GameView.this.openToast(2131099657);
        ((Activity)GameView.this.context).finish();
        continue;
        GameView.this.openToast(2131099658);
        ((Activity)GameView.this.context).finish();
      }
    }
  }
}


/* Location:           C:\Users\Administrator\Documents\ds\反编译\AndroidKiller_v1.3.1\projects\Jungledsq_ppc\ProjectSrc\smali\
 * Qualified Name:     hk.doppio.game.jungle.GameView
 * JD-Core Version:    0.7.0.1
 */