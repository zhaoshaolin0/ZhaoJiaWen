package hk.doppio.game.jungle;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.Toast;

public class MenuActivity
  extends Activity
{
  MenuButton menuButton1;
  MenuButton menuButton2;
  MenuButton menuButton3;
  MenuButton menuButton4;
  ImageView titleImage;
  
  public void onCreate(final Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    requestWindowFeature(1);
    getWindow().setFlags(1024, 1024);
    setRequestedOrientation(1);
    setTheme(2131165184);
    setContentView(2130903042);
    paramBundle = getIntent();
    final Bundle localBundle = paramBundle.getExtras();
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
        localBundle.putInt("CLICKED", 1);
        paramBundle.putExtras(localBundle);
        MenuActivity.this.setResult(-1, paramBundle);
        MenuActivity.this.finish();
      }
    });
    this.menuButton2.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramAnonymousView)
      {
        localBundle.putInt("CLICKED", 2);
        paramBundle.putExtras(localBundle);
        MenuActivity.this.setResult(-1, paramBundle);
        MenuActivity.this.finish();
      }
    });
    this.menuButton3.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramAnonymousView)
      {
        localBundle.putInt("CLICKED", 3);
        paramBundle.putExtras(localBundle);
        MenuActivity.this.setResult(-1, paramBundle);
        MenuActivity.this.finish();
      }
    });
    this.menuButton4.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramAnonymousView)
      {
        localBundle.putInt("CLICKED", 4);
        paramBundle.putExtras(localBundle);
        MenuActivity.this.setResult(-1, paramBundle);
        MenuActivity.this.finish();
      }
    });
  }
  
  public boolean onKeyUp(int paramInt, KeyEvent paramKeyEvent)
  {
    switch (paramInt)
    {
    }
    for (;;)
    {
      return super.onKeyUp(paramInt, paramKeyEvent);
      finish();
    }
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
 * Qualified Name:     hk.doppio.game.jungle.MenuActivity
 * JD-Core Version:    0.7.0.1
 */