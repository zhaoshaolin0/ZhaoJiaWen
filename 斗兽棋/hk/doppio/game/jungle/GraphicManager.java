package hk.doppio.game.jungle;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.SurfaceHolder;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;

public class GraphicManager
{
  public static int CHESS_BOARD_HEIGHT;
  public static int CHESS_BOARD_WIDTH = 350;
  public static int CHESS_TILE_HEIGHT;
  public static int CHESS_TILE_WIDTH;
  public static int offsetX = 0;
  public static int offsetY = 0;
  public static float scaleFactor;
  Bitmap background;
  int canvasHeight = 0;
  int canvasWidth = 0;
  ArrayList<ChessSprite> chessBlue = new ArrayList();
  ArrayList<ChessSprite> chessRed = new ArrayList();
  GameThread game;
  SurfaceHolder holder;
  Paint mChessTextPaint;
  Paint mChessTextPaintStroke;
  Paint mHLSquareColor;
  RectF mHLSquareRect;
  Paint mP1IndicatorColor;
  RectF mP1IndicatorRect;
  Paint mP2IndicatorColor;
  RectF mP2IndicatorRect;
  ArrayList<Paint> paintArr = new ArrayList();
  boolean showChessText = true;
  
  static
  {
    CHESS_BOARD_HEIGHT = 450;
    CHESS_TILE_WIDTH = 50;
    CHESS_TILE_HEIGHT = 50;
    scaleFactor = 1.0F;
  }
  
  GraphicManager(Resources paramResources, SurfaceHolder paramSurfaceHolder, GameThread paramGameThread)
  {
    this.game = paramGameThread;
    this.holder = paramSurfaceHolder;
    scaleFactor = paramResources.getDisplayMetrics().density;
    this.background = BitmapFactory.decodeResource(paramResources, 2130837527);
    if (GameActivity.debugMode) {
      Log.d("doppioDebug", this.background.getWidth() + "x" + this.background.getHeight());
    }
    for (;;)
    {
      try
      {
        paramSurfaceHolder = Class.forName("hk.doppio.game.jungle.R$drawable");
        paramGameThread = Chess.Animal.values();
        int j = paramGameThread.length;
        i = 0;
        if (i < j) {
          continue;
        }
        paramGameThread = Chess.Animal.values();
        j = paramGameThread.length;
        i = 0;
        if (i < j) {
          continue;
        }
      }
      catch (NoSuchFieldException paramResources)
      {
        int i;
        Object localObject;
        if (!GameActivity.debugMode) {
          continue;
        }
        Log.d("doppioDebug", paramResources.toString());
        paramResources.printStackTrace();
        continue;
      }
      catch (IllegalAccessException paramResources)
      {
        if (!GameActivity.debugMode) {
          continue;
        }
        Log.d("doppioDebug", paramResources.toString());
        paramResources.printStackTrace();
        continue;
      }
      catch (ClassNotFoundException paramResources)
      {
        if (!GameActivity.debugMode) {
          continue;
        }
        Log.d("doppioDebug", paramResources.toString());
        paramResources.printStackTrace();
        continue;
      }
      this.mHLSquareRect = new RectF(0.0F, 0.0F, 0.0F, 0.0F);
      this.mHLSquareColor = new Paint();
      this.mHLSquareColor.setAntiAlias(true);
      this.mHLSquareColor.setARGB(120, 255, 255, 0);
      this.mP1IndicatorRect = new RectF(0.0F, 0.0F, 0.0F, 0.0F);
      this.mP1IndicatorColor = new Paint();
      this.mP1IndicatorColor.setAntiAlias(true);
      this.mP1IndicatorColor.setARGB(120, 255, 0, 0);
      this.mP2IndicatorRect = new RectF(0.0F, 0.0F, 0.0F, 0.0F);
      this.mP2IndicatorColor = new Paint();
      this.mP2IndicatorColor.setAntiAlias(true);
      this.mP2IndicatorColor.setARGB(120, 0, 0, 255);
      this.mChessTextPaint = new Paint();
      this.mChessTextPaint.setTextSize(12.0F);
      this.mChessTextPaint.setColor(-1);
      this.mChessTextPaint.setTextAlign(Paint.Align.RIGHT);
      this.mChessTextPaintStroke = new Paint();
      this.mChessTextPaintStroke.setTextSize(12.0F);
      this.mChessTextPaintStroke.setColor(-16777216);
      this.mChessTextPaintStroke.setTextAlign(Paint.Align.RIGHT);
      this.mChessTextPaintStroke.setStyle(Paint.Style.STROKE);
      this.mChessTextPaintStroke.setStrokeWidth(2.0F);
      return;
      localObject = paramGameThread[i];
      localObject = new ChessSprite((Chess.Animal)localObject, BitmapFactory.decodeResource(paramResources, paramSurfaceHolder.getDeclaredField("chess_red_" + ((Chess.Animal)localObject).toString().toLowerCase()).getInt(paramSurfaceHolder)));
      ((ChessSprite)localObject).resize(CHESS_TILE_WIDTH, CHESS_TILE_HEIGHT);
      this.chessRed.add(localObject);
      i += 1;
      continue;
      localObject = paramGameThread[i];
      localObject = new ChessSprite((Chess.Animal)localObject, BitmapFactory.decodeResource(paramResources, paramSurfaceHolder.getDeclaredField("chess_blue_" + ((Chess.Animal)localObject).toString().toLowerCase()).getInt(paramSurfaceHolder)));
      ((ChessSprite)localObject).resize(CHESS_TILE_WIDTH, CHESS_TILE_HEIGHT);
      this.chessBlue.add(localObject);
      i += 1;
    }
  }
  
  void createPaint()
  {
    Paint localPaint = new Paint();
    localPaint.setAntiAlias(true);
    localPaint.setARGB(255, 0, 255, 0);
    this.paintArr.add(localPaint);
  }
  
  void rescale(int paramInt1, int paramInt2)
  {
    this.canvasWidth = paramInt1;
    this.canvasHeight = paramInt2;
    scaleFactor = paramInt1 / CHESS_BOARD_WIDTH;
    offsetY = (int)(Math.abs(paramInt2 - CHESS_BOARD_HEIGHT * scaleFactor) / 2.0F);
    if (offsetY < 0) {
      offsetY = 0;
    }
    Log.d("doppioDebug", paramInt1 + "/" + CHESS_BOARD_WIDTH);
    Log.d("doppioDebug", paramInt2 + "/" + CHESS_BOARD_HEIGHT);
    Log.d("doppioDebug", offsetX + "<offset>" + offsetY);
    Log.d("doppioDebug", scaleFactor + "x");
    this.background = Bitmap.createScaledBitmap(this.background, (int)(CHESS_BOARD_WIDTH * scaleFactor), (int)(CHESS_BOARD_HEIGHT * scaleFactor), true);
  }
  
  void update(Canvas paramCanvas)
  {
    paramCanvas.drawColor(-1);
    int j = 0;
    int i = j;
    if (this.game.networkMode)
    {
      i = j;
      if (this.game.player[1].isPlayer) {
        i = 1;
      }
    }
    this.mP1IndicatorRect.set(new RectF(0.0F, this.canvasHeight - offsetY - 5, this.canvasWidth, this.canvasHeight));
    this.mP2IndicatorRect.set(new RectF(0.0F, 0.0F, this.canvasWidth, offsetY + 5));
    if (i != 0)
    {
      paramCanvas.save();
      paramCanvas.rotate(180.0F, this.canvasWidth / 2, this.canvasHeight / 2);
    }
    if (this.game.gameState == GameThread.GAME_STATE.RUNNING)
    {
      if ((this.game.player[0].toMove) || (this.game.player[0].isWin)) {
        paramCanvas.drawRect(this.mP1IndicatorRect, this.mP1IndicatorColor);
      }
      if ((this.game.player[1].toMove) || (this.game.player[1].isWin)) {
        paramCanvas.drawRect(this.mP2IndicatorRect, this.mP2IndicatorColor);
      }
    }
    if (i != 0) {
      paramCanvas.restore();
    }
    paramCanvas.drawBitmap(this.background, offsetX, offsetY, null);
    Iterator localIterator = this.chessRed.iterator();
    if (!localIterator.hasNext())
    {
      localIterator = this.chessBlue.iterator();
      label281:
      if (localIterator.hasNext()) {
        break label588;
      }
      if (i != 0)
      {
        paramCanvas.save();
        paramCanvas.rotate(180.0F, this.canvasWidth / 2, this.canvasHeight / 2);
      }
      localIterator = this.game.getValidMoves().iterator();
    }
    for (;;)
    {
      if (!localIterator.hasNext())
      {
        if (this.game.networkMode) {
          paramCanvas.restore();
        }
        return;
        localObject = (ChessSprite)localIterator.next();
        paramCanvas.save();
        if (i != 0)
        {
          paramCanvas.rotate(180.0F, this.canvasWidth / 2, this.canvasHeight / 2);
          paramCanvas.rotate(180.0F, ((Chess)this.game.player[0].hand.get(((ChessSprite)localObject).rank.ordinal())).getCenterX(), ((Chess)this.game.player[0].hand.get(((ChessSprite)localObject).rank.ordinal())).getCenterY());
        }
        ((ChessSprite)localObject).image.draw(paramCanvas);
        if (this.showChessText)
        {
          paramCanvas.drawText(((ChessSprite)localObject).rank, ((ChessSprite)localObject).image.getBounds().right, ((ChessSprite)localObject).image.getBounds().bottom, this.mChessTextPaintStroke);
          paramCanvas.drawText(((ChessSprite)localObject).rank, ((ChessSprite)localObject).image.getBounds().right, ((ChessSprite)localObject).image.getBounds().bottom, this.mChessTextPaint);
        }
        paramCanvas.restore();
        break;
        label588:
        localObject = (ChessSprite)localIterator.next();
        paramCanvas.save();
        if (i != 0) {
          paramCanvas.rotate(180.0F, this.canvasWidth / 2, this.canvasHeight / 2);
        }
        if (this.game.player[1].isPlayer) {
          paramCanvas.rotate(180.0F, ((Chess)this.game.player[1].hand.get(((ChessSprite)localObject).rank.ordinal())).getCenterX(), ((Chess)this.game.player[1].hand.get(((ChessSprite)localObject).rank.ordinal())).getCenterY());
        }
        ((ChessSprite)localObject).image.draw(paramCanvas);
        if (this.showChessText)
        {
          paramCanvas.drawText(((ChessSprite)localObject).rank, ((ChessSprite)localObject).image.getBounds().right, ((ChessSprite)localObject).image.getBounds().bottom, this.mChessTextPaintStroke);
          paramCanvas.drawText(((ChessSprite)localObject).rank, ((ChessSprite)localObject).image.getBounds().right, ((ChessSprite)localObject).image.getBounds().bottom, this.mChessTextPaint);
        }
        paramCanvas.restore();
        break label281;
      }
      Object localObject = (Point)localIterator.next();
      this.mHLSquareRect.set(((Point)localObject).x * CHESS_TILE_WIDTH * scaleFactor + offsetX, ((Point)localObject).y * CHESS_TILE_HEIGHT * scaleFactor + offsetY, (((Point)localObject).x + 1) * CHESS_TILE_WIDTH * scaleFactor + offsetX, (((Point)localObject).y + 1) * CHESS_TILE_HEIGHT * scaleFactor + offsetY);
      this.mHLSquareColor.setColor(this.game.playerCurrent.color);
      this.mHLSquareColor.setAlpha(120);
      paramCanvas.drawRect(this.mHLSquareRect, this.mHLSquareColor);
    }
  }
  
  public class ChessSprite
  {
    BitmapDrawable image;
    Chess.Animal rank;
    
    ChessSprite(Chess.Animal paramAnimal, Bitmap paramBitmap)
    {
      this.rank = paramAnimal;
      this.image = new BitmapDrawable(paramBitmap);
    }
    
    public void resize(int paramInt1, int paramInt2)
    {
      this.image = new BitmapDrawable(Bitmap.createScaledBitmap(this.image.getBitmap(), paramInt1, paramInt2, true));
    }
  }
}


/* Location:           C:\Users\Administrator\Documents\ds\反编译\AndroidKiller_v1.3.1\projects\Jungledsq_ppc\ProjectSrc\smali\
 * Qualified Name:     hk.doppio.game.jungle.GraphicManager
 * JD-Core Version:    0.7.0.1
 */