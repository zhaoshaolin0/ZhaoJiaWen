package hk.doppio.game.jungle;

import android.app.Activity;
import android.content.Intent;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.Toast;

public class TitleActivity
  extends Activity
{
  MenuButton menuButton1;
  MenuButton menuButton2;
  MenuButton menuButton3;
  MenuButton menuButton4;
  ImageView titleImage;
  
  public void callGame(Bundle paramBundle)
  {
    setVisible(false);
    Intent localIntent = new Intent();
    localIntent.putExtras(paramBundle);
    localIntent.setClass(this, GameActivity.class);
    startActivityForResult(localIntent, 0);
    if (Integer.parseInt(Build.VERSION.SDK) > 4) {
      CompatibilityHandler.overridePendingTransition(this, 17432576, 17432577);
    }
  }
  
  protected void onActivityResult(int paramInt1, int paramInt2, Intent paramIntent)
  {
    super.onActivityResult(paramInt1, paramInt2, paramIntent);
    setVisible(true);
  }
  
  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    requestWindowFeature(1);
    getWindow().setFlags(1024, 1024);
    setRequestedOrientation(1);
    setTheme(2131165184);
    setContentView(2130903043);
    this.menuButton1 = ((MenuButton)findViewById(2131230725));
    this.menuButton2 = ((MenuButton)findViewById(2131230726));
    this.menuButton3 = ((MenuButton)findViewById(2131230727));
    this.menuButton4 = ((MenuButton)findViewById(2131230728));
    this.menuButton1.setImage(0);
    this.menuButton2.setImage(1);
    this.menuButton3.setImage(2);
    this.menuButton4.setImage(3);
    this.menuButton1.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramAnonymousView)
      {
        paramAnonymousView = new Bundle();
        paramAnonymousView.putInt("CLICKED", 1);
        TitleActivity.this.callGame(paramAnonymousView);
      }
    });
    this.menuButton2.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramAnonymousView)
      {
        paramAnonymousView = new Bundle();
        paramAnonymousView.putInt("CLICKED", 2);
        TitleActivity.this.callGame(paramAnonymousView);
      }
    });
    this.menuButton3.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramAnonymousView)
      {
        paramAnonymousView = new Bundle();
        paramAnonymousView.putInt("CLICKED", 3);
        TitleActivity.this.callGame(paramAnonymousView);
      }
    });
    this.menuButton4.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramAnonymousView)
      {
        TitleActivity.this.finish();
      }
    });
  }
  
  public boolean onKeyUp(int paramInt, KeyEvent paramKeyEvent)
  {
    return super.onKeyUp(paramInt, paramKeyEvent);
  }
  
  public boolean onTouchEvent(MotionEvent paramMotionEvent)
  {
    if (GameActivity.debugMode) {
      Log.d("doppioDebug", "Touch detected on menu view");
    }
    return super.onTouchEvent(paramMotionEvent);
  }
  
  public void openToast(String paramString)
  {
    Toast.makeText(this, paramString, 0).show();
  }
}


/* Location:           C:\Users\Administrator\Documents\ds\反编译\AndroidKiller_v1.3.1\projects\Jungledsq_ppc\ProjectSrc\smali\
 * Qualified Name:     hk.doppio.game.jungle.TitleActivity
 * JD-Core Version:    0.7.0.1
 */