package hk.doppio.game.jungle;

import java.util.ArrayList;
import java.util.Iterator;

class Player
{
  int chessToMove = -1;
  int color;
  ArrayList<Chess> hand = new ArrayList();
  boolean isAI;
  boolean isNetwork;
  boolean isPlayer;
  boolean isWin;
  int nextMove = -1;
  boolean toMove = false;
  
  Player(int paramInt)
  {
    this.color = paramInt;
  }
  
  Player(boolean paramBoolean1, boolean paramBoolean2, int paramInt)
  {
    this.isPlayer = paramBoolean1;
    this.isAI = paramBoolean2;
    this.color = paramInt;
  }
  
  public int chessLeft()
  {
    int i = 0;
    Iterator localIterator = this.hand.iterator();
    for (;;)
    {
      if (!localIterator.hasNext()) {
        return i;
      }
      if (((Chess)localIterator.next()).alive) {
        i += 1;
      }
    }
  }
  
  void createHand(int paramInt)
  {
    Chess.Animal[] arrayOfAnimal = Chess.Animal.values();
    int j = arrayOfAnimal.length;
    int i = 0;
    for (;;)
    {
      if (i >= j) {
        return;
      }
      Chess.Animal localAnimal = arrayOfAnimal[i];
      this.hand.add(new Chess(paramInt, localAnimal));
      i += 1;
    }
  }
  
  void reset()
  {
    this.toMove = false;
    this.isWin = false;
    this.nextMove = -1;
    this.chessToMove = -1;
  }
  
  void switchTurn()
  {
    this.nextMove = -1;
    this.chessToMove = -1;
    this.toMove ^= true;
    this.isWin = false;
  }
}


/* Location:           C:\Users\Administrator\Documents\ds\反编译\AndroidKiller_v1.3.1\projects\Jungledsq_ppc\ProjectSrc\smali\
 * Qualified Name:     hk.doppio.game.jungle.Player
 * JD-Core Version:    0.7.0.1
 */