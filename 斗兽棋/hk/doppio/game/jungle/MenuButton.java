package hk.doppio.game.jungle;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.MeasureSpec;

public class MenuButton
  extends View
{
  private Context context;
  private Bitmap image;
  
  public MenuButton(Context paramContext)
  {
    super(paramContext);
    init(paramContext);
  }
  
  public MenuButton(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
    init(paramContext);
  }
  
  public MenuButton(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    super(paramContext, paramAttributeSet, paramInt);
    init(paramContext);
  }
  
  private int getMeasurement(int paramInt1, int paramInt2)
  {
    int i = View.MeasureSpec.getSize(paramInt1);
    switch (View.MeasureSpec.getMode(paramInt1))
    {
    default: 
      return paramInt2;
    case 1073741824: 
      return i;
    }
    return Math.min(paramInt2, i);
  }
  
  private void init(Context paramContext)
  {
    this.context = paramContext;
    this.image = BitmapFactory.decodeResource(paramContext.getResources(), 2130837504);
    setBackgroundColor(0);
    setFocusable(true);
    setClickable(true);
  }
  
  private int measureHeight(int paramInt)
  {
    return getMeasurement(paramInt, this.image.getHeight());
  }
  
  private int measureWidth(int paramInt)
  {
    return getMeasurement(paramInt, this.image.getWidth());
  }
  
  int minHeight()
  {
    return this.image.getHeight();
  }
  
  int minWidth()
  {
    return this.image.getWidth();
  }
  
  protected void onDraw(Canvas paramCanvas)
  {
    paramCanvas.drawBitmap(this.image, 0.0F, 0.0F, null);
  }
  
  protected void onFocusChanged(boolean paramBoolean, int paramInt, Rect paramRect)
  {
    super.onFocusChanged(paramBoolean, paramInt, paramRect);
    if (paramBoolean)
    {
      setBackgroundColor(-1);
      return;
    }
    setBackgroundColor(-16777216);
  }
  
  protected void onMeasure(int paramInt1, int paramInt2)
  {
    double d = measureWidth(paramInt1) / this.image.getWidth();
    this.image = Bitmap.createScaledBitmap(this.image, (int)(this.image.getWidth() * d), (int)(this.image.getHeight() * d), false);
    setMeasuredDimension(measureWidth(paramInt1), measureHeight(paramInt2));
  }
  
  public void setImage(int paramInt)
  {
    switch (paramInt)
    {
    default: 
      return;
    case 0: 
      this.image = BitmapFactory.decodeResource(this.context.getResources(), 2130837507);
      return;
    case 1: 
      this.image = BitmapFactory.decodeResource(this.context.getResources(), 2130837506);
      return;
    case 2: 
      this.image = BitmapFactory.decodeResource(this.context.getResources(), 2130837505);
      return;
    }
    this.image = BitmapFactory.decodeResource(this.context.getResources(), 2130837504);
  }
}


/* Location:           C:\Users\Administrator\Documents\ds\反编译\AndroidKiller_v1.3.1\projects\Jungledsq_ppc\ProjectSrc\smali\
 * Qualified Name:     hk.doppio.game.jungle.MenuButton
 * JD-Core Version:    0.7.0.1
 */