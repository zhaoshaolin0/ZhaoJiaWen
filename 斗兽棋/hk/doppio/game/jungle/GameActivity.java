package hk.doppio.game.jungle;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Window;

public class GameActivity
  extends Activity
{
  static boolean debugMode = false;
  GameThread gameThread;
  GameView gameView;
  
  public void callMenu()
  {
    Intent localIntent = new Intent();
    localIntent.putExtras(new Bundle());
    localIntent.setClass(this, MenuActivity.class);
    startActivityForResult(localIntent, 0);
  }
  
  protected void onActivityResult(int paramInt1, int paramInt2, Intent paramIntent)
  {
    if (paramIntent == null) {
      return;
    }
    switch (paramIntent.getExtras().getInt("CLICKED"))
    {
    default: 
      return;
    case 1: 
      this.gameThread.setNetworkMode(false);
      this.gameThread.setTwoPlayerMode(false);
      this.gameThread.newGame();
      return;
    case 2: 
      this.gameThread.setTwoPlayerMode(true);
      this.gameThread.newGame();
      return;
    case 3: 
      this.gameThread.setNetworkMode(true);
      this.gameThread.newGame();
      return;
    }
    finish();
  }
  
  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    requestWindowFeature(1);
    getWindow().setFlags(1024, 1024);
    setRequestedOrientation(1);
    setContentView(2130903040);
    this.gameView = ((GameView)findViewById(2131230720));
    this.gameThread = this.gameView.getThread();
    paramBundle = (GameThread.GameStateObj)getLastNonConfigurationInstance();
    if (paramBundle == null) {
      if (debugMode) {
        Log.d("doppioDebug", "No Restored GameState.");
      }
    }
    for (;;)
    {
      switch (getIntent().getExtras().getInt("CLICKED"))
      {
      default: 
        return;
        if (debugMode) {
          Log.d("doppioDebug", "Loaded GameState.");
        }
        this.gameThread.loadGameState(paramBundle);
      }
    }
    this.gameThread.setNetworkMode(false);
    this.gameThread.setTwoPlayerMode(false);
    this.gameThread.newGame();
    return;
    this.gameThread.setTwoPlayerMode(true);
    this.gameThread.newGame();
    return;
    this.gameThread.setNetworkMode(true);
    this.gameThread.newGame();
  }
  
  protected void onDestroy()
  {
    super.onDestroy();
    this.gameThread.clean();
  }
  
  public boolean onKeyUp(int paramInt, KeyEvent paramKeyEvent)
  {
    if (debugMode) {
      Log.d("doppioDebug", "Key Detected: " + paramInt);
    }
    switch (paramInt)
    {
    default: 
      return super.onKeyUp(paramInt, paramKeyEvent);
    }
    callMenu();
    return true;
  }
  
  protected void onPause()
  {
    if (debugMode) {
      Log.d("doppioDebug", "onPause");
    }
    this.gameView.getThread().pause();
    super.onPause();
  }
  
  protected void onResume()
  {
    if (debugMode) {
      Log.d("doppioDebug", "onResume");
    }
    super.onResume();
    this.gameView.getThread().unpause();
    getWindow().setFlags(1024, 1024);
  }
  
  public Object onRetainNonConfigurationInstance()
  {
    Log.w(getClass().getName(), "onRetainNonConfigurationInstance called");
    return this.gameThread.collectGameState();
  }
  
  protected void onSaveInstanceState(Bundle paramBundle)
  {
    super.onSaveInstanceState(paramBundle);
    Log.w(getClass().getName(), "SIS called");
  }
}


/* Location:           C:\Users\Administrator\Documents\ds\反编译\AndroidKiller_v1.3.1\projects\Jungledsq_ppc\ProjectSrc\smali\
 * Qualified Name:     hk.doppio.game.jungle.GameActivity
 * JD-Core Version:    0.7.0.1
 */