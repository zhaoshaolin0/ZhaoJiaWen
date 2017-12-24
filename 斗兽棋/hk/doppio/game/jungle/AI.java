package hk.doppio.game.jungle;

import android.util.Log;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

class AI
{
  static int lookUpRange = 4;
  double aggressiveness = 1.0D;
  Player ai;
  HashSet<Chess> beastSet = new HashSet();
  Board board;
  double defensiveness = 1.0D;
  HashSet<Chess> feastSet = new HashSet();
  int maxMove = 15;
  HashSet<Chess.Animal> movedChessSet = new HashSet();
  Chess myLair;
  Player op;
  Chess opLair;
  Point opLastMove;
  Chess opLastMoveChess;
  HashSet<aiMove> pendingMoves = new HashSet();
  ArrayList<Point> points_adj = new ArrayList();
  ArrayList<Point> points_trap = new ArrayList();
  double raid = 1.0D;
  String tag = "doppio_Jungle_AI";
  HashSet<Chess> threatSet = new HashSet();
  
  AI(GameThread paramGameThread, int paramInt)
  {
    this.board = paramGameThread.board;
    this.ai = paramGameThread.player[paramInt];
    this.ai.isAI = true;
    this.ai.isPlayer = false;
    this.op = paramGameThread.player[(paramInt % 1)];
    this.myLair = ((Chess)this.ai.hand.get(Chess.Animal.LAIR.ordinal()));
    this.opLair = ((Chess)this.op.hand.get(Chess.Animal.LAIR.ordinal()));
  }
  
  public static BigDecimal log10(BigDecimal paramBigDecimal, int paramInt)
  {
    int k = paramInt + 2;
    MathContext localMathContext = new MathContext(k, RoundingMode.HALF_EVEN);
    if (paramBigDecimal.signum() <= 0) {
      throw new ArithmeticException("log of a negative number! (or zero): " + paramBigDecimal.toString());
    }
    if (paramBigDecimal.compareTo(BigDecimal.ONE) == 0) {
      return BigDecimal.ZERO;
    }
    if (paramBigDecimal.compareTo(BigDecimal.ONE) < 0) {
      return log10(BigDecimal.ONE.divide(paramBigDecimal, localMathContext), paramInt).negate();
    }
    StringBuffer localStringBuffer = new StringBuffer();
    int j = paramBigDecimal.precision() - paramBigDecimal.scale();
    localStringBuffer.append(j - 1).append(".");
    int i = 0;
    for (;;)
    {
      if (i >= k)
      {
        paramBigDecimal = new BigDecimal(localStringBuffer.toString());
        return paramBigDecimal.round(new MathContext(paramBigDecimal.precision() - paramBigDecimal.scale() + paramInt, RoundingMode.HALF_EVEN));
      }
      paramBigDecimal = paramBigDecimal.movePointLeft(j - 1).pow(10, localMathContext);
      j = paramBigDecimal.precision() - paramBigDecimal.scale();
      localStringBuffer.append(j - 1);
      i += 1;
    }
  }
  
  static double preciseAdd(double paramDouble1, double paramDouble2)
  {
    return new BigDecimal(Double.toString(paramDouble1)).add(new BigDecimal(Double.toString(paramDouble2))).doubleValue();
  }
  
  static double preciseDecimal(double paramDouble)
  {
    return new BigDecimal(Double.toString(paramDouble)).doubleValue();
  }
  
  static double preciseDivide(double paramDouble1, double paramDouble2)
  {
    return new BigDecimal(Double.toString(paramDouble1)).divide(new BigDecimal(Double.toString(paramDouble2))).doubleValue();
  }
  
  static double preciseLog10(double paramDouble)
  {
    return log10(new BigDecimal(Double.toString(paramDouble)), 0).doubleValue();
  }
  
  static double preciseMultiply(double paramDouble1, double paramDouble2)
  {
    return new BigDecimal(Double.toString(paramDouble1)).multiply(new BigDecimal(Double.toString(paramDouble2))).doubleValue();
  }
  
  static double preciseSub(double paramDouble1, double paramDouble2)
  {
    return new BigDecimal(Double.toString(paramDouble1)).subtract(new BigDecimal(Double.toString(paramDouble2))).doubleValue();
  }
  
  boolean checkAdjAlly(int paramInt1, int paramInt2)
  {
    Object localObject = new ArrayList();
    int i = paramInt2 - 1;
    int j = paramInt2 + 1;
    int k = paramInt1 - 1;
    int m = paramInt1 + 1;
    ((ArrayList)localObject).add(this.board.getTile(paramInt1, paramInt2).containing);
    if (i >= 0) {
      ((ArrayList)localObject).add(this.board.getTile(paramInt1, i).containing);
    }
    if (j < Board.maxY) {
      ((ArrayList)localObject).add(this.board.getTile(paramInt1, j).containing);
    }
    if (k >= 0) {
      ((ArrayList)localObject).add(this.board.getTile(k, paramInt2).containing);
    }
    if (m < Board.maxX) {
      ((ArrayList)localObject).add(this.board.getTile(m, paramInt2).containing);
    }
    localObject = ((ArrayList)localObject).iterator();
    Chess localChess;
    do
    {
      if (!((Iterator)localObject).hasNext()) {
        return false;
      }
      localChess = (Chess)((Iterator)localObject).next();
    } while ((localChess == null) || (localChess.rank == Chess.Animal.LAIR) || (localChess.color != this.ai.color));
    return true;
  }
  
  boolean checkAdjAlly(Point paramPoint)
  {
    return checkAdjAlly(paramPoint.x, paramPoint.y);
  }
  
  boolean checkAdjPredator(Chess paramChess)
  {
    return checkAdjPredator(paramChess, paramChess.coor);
  }
  
  boolean checkAdjPredator(Chess paramChess, Point paramPoint)
  {
    Chess localChess2 = Chess.clone(paramChess);
    localChess2.coor.set(paramPoint.x, paramPoint.y);
    paramPoint = new ArrayList();
    int i = localChess2.coor.x;
    int j = localChess2.coor.y;
    int k = j - 1;
    int m = j + 1;
    int n = i - 1;
    int i1 = i + 1;
    if (k >= 0) {
      paramPoint.add(this.board.getTile(i, k).containing);
    }
    if (m < Board.maxY) {
      paramPoint.add(this.board.getTile(i, m).containing);
    }
    if (n >= 0) {
      paramPoint.add(this.board.getTile(n, j).containing);
    }
    if (i1 < Board.maxX) {
      paramPoint.add(this.board.getTile(i1, j).containing);
    }
    paramChess = null;
    Iterator localIterator = paramPoint.iterator();
    do
    {
      Chess localChess1;
      do
      {
        if (!localIterator.hasNext()) {
          return false;
        }
        localChess1 = (Chess)localIterator.next();
      } while ((localChess1 == null) || (localChess1.color == localChess2.color));
      paramPoint = paramChess;
      if (paramChess == null) {
        paramPoint = localChess1;
      }
      paramChess = paramPoint;
    } while (!this.board.isMoveValid(paramPoint, localChess2));
    return true;
  }
  
  boolean checkAdjPrey(Chess paramChess)
  {
    return checkAdjPrey(paramChess, paramChess.coor);
  }
  
  boolean checkAdjPrey(Chess paramChess, Point paramPoint)
  {
    Chess localChess2 = Chess.clone(paramChess);
    localChess2.coor.set(paramPoint.x, paramPoint.y);
    paramPoint = new ArrayList();
    int i = localChess2.coor.x;
    int j = localChess2.coor.y;
    int k = j - 1;
    int m = j + 1;
    int n = i - 1;
    int i1 = i + 1;
    if (k >= 0) {
      paramPoint.add(this.board.getTile(i, k).containing);
    }
    if (m < Board.maxY) {
      paramPoint.add(this.board.getTile(i, m).containing);
    }
    if (n >= 0) {
      paramPoint.add(this.board.getTile(n, j).containing);
    }
    if (i1 < Board.maxX) {
      paramPoint.add(this.board.getTile(i1, j).containing);
    }
    paramChess = null;
    Iterator localIterator = paramPoint.iterator();
    do
    {
      Chess localChess1;
      do
      {
        if (!localIterator.hasNext()) {
          return false;
        }
        localChess1 = (Chess)localIterator.next();
      } while ((localChess1 == null) || (localChess1.color == localChess2.color));
      paramPoint = paramChess;
      if (paramChess == null) {
        paramPoint = localChess1;
      }
      paramChess = paramPoint;
    } while (!this.board.isMoveValid(localChess2, paramPoint));
    return true;
  }
  
  void checkLair()
  {
    ArrayList localArrayList = checkRangeEnemy(this.myLair, 3);
    label14:
    Iterator localIterator;
    if (localArrayList == null) {
      return;
    } else {
      localIterator = localArrayList.iterator();
    }
    for (;;)
    {
      if (!localIterator.hasNext()) {
        break label14;
      }
      Object localObject = (Chess)localIterator.next();
      if (GameActivity.debugMode) {
        Log.d(this.tag, ((Chess)localObject).rank + " is a threat to my lair");
      }
      int i = findShortestMove((Chess)localObject, this.myLair.coor);
      if (i >= 10) {
        break;
      }
      localObject = this.ai.hand.iterator();
      while (((Iterator)localObject).hasNext())
      {
        Chess localChess = (Chess)((Iterator)localObject).next();
        if ((localChess.alive) && (localChess.rank != Chess.Animal.LAIR))
        {
          setPath2ProtectLair(localChess, i, this.points_adj, localArrayList);
          setPath2ProtectLair(localChess, i, this.points_trap, localArrayList);
        }
      }
    }
  }
  
  ArrayList<Chess> checkRangeEnemy(Chess paramChess, int paramInt)
  {
    ArrayList localArrayList = new ArrayList();
    int k = paramChess.coor.y - paramInt;
    int j = paramChess.coor.y + paramInt;
    int m = paramChess.coor.x - paramInt;
    int n = paramChess.coor.x + paramInt;
    int i = k;
    if (k < 0) {
      i = 0;
    }
    k = j;
    if (j >= Board.maxY) {
      k = Board.maxY - 1;
    }
    j = m;
    if (m < 0) {
      j = 0;
    }
    m = n;
    if (n >= Board.maxX) {
      m = Board.maxX - 1;
    }
    for (;;)
    {
      if (j > m) {
        return localArrayList;
      }
      n = i;
      if (n <= k) {
        break;
      }
      j += 1;
    }
    Chess localChess;
    if (Math.abs(paramChess.coor.x - j) + Math.abs(paramChess.coor.y - n) <= paramInt)
    {
      localChess = this.board.getTile(j, n).containing;
      if (localChess != null) {
        break label203;
      }
    }
    for (;;)
    {
      n += 1;
      break;
      label203:
      if (localChess.color != paramChess.color)
      {
        localArrayList.add(this.board.getTile(j, n).containing);
        if (GameActivity.debugMode) {
          Log.d(this.tag, j + "," + n);
        }
      }
    }
  }
  
  void checkThreat(Chess paramChess, int paramInt)
  {
    paramChess.threat = new ArrayList();
    paramChess.target = new ArrayList();
    Iterator localIterator = checkRangeEnemy(paramChess, paramInt).iterator();
    for (;;)
    {
      if (!localIterator.hasNext()) {
        return;
      }
      Chess localChess = (Chess)localIterator.next();
      if (!this.board.isSameLand(paramChess, localChess))
      {
        if (GameActivity.debugMode) {
          Log.d(this.tag, "Different Land, no direct threat.");
        }
      }
      else
      {
        if ((findShortestMove(localChess, paramChess) > -1) && (Chess.canCapture(localChess, paramChess, false)) && (this.board.getTile(localChess.coor).tileType != Board.TileType.TRAP))
        {
          if (GameActivity.debugMode) {
            Log.d(this.tag, paramChess.rank + ": " + localChess.rank + "(" + localChess.coor.x + "," + localChess.coor.y + ") is threatening me!");
          }
          this.threatSet.add(localChess);
          paramChess.threat.add(localChess);
        }
        if ((findShortestMove(paramChess, localChess) > -1) && ((Chess.canCapture(paramChess, localChess, false)) || (this.board.getTile(localChess.coor).tileType == Board.TileType.TRAP)))
        {
          if (GameActivity.debugMode) {
            Log.d(this.tag, paramChess.rank + ": " + localChess.rank + "(" + localChess.coor.x + "," + localChess.coor.y + ") looks yummy!");
          }
          this.feastSet.add(localChess);
          this.beastSet.add(paramChess);
          paramChess.target.add(localChess);
        }
      }
    }
  }
  
  void createPossibleMoves(Chess paramChess)
  {
    Iterator localIterator = this.board.getValidMove(paramChess).iterator();
    for (;;)
    {
      if (!localIterator.hasNext()) {
        return;
      }
      Point localPoint = (Point)localIterator.next();
      double d = 1.0D;
      if (checkAdjPredator(paramChess, paramChess.coor)) {
        d = 2.0D;
      }
      Chess localChess = Chess.clone(paramChess);
      localChess.coor = new Point(localPoint);
      if ((this.board.getTile(localChess.coor).containing != null) && (this.board.isMoveValid(paramChess, localPoint))) {
        preciseAdd(d, 10.0D);
      }
      this.pendingMoves.add(new aiMove(paramChess, localPoint, 0.1D));
    }
  }
  
  float distance(Point paramPoint1, Point paramPoint2)
  {
    return Math.abs(paramPoint1.x - paramPoint2.x) + Math.abs(paramPoint1.y - paramPoint2.y);
  }
  
  float distance2myLair(Point paramPoint)
  {
    return Math.abs(this.myLair.coor.x - paramPoint.x) + Math.abs(this.myLair.coor.y - paramPoint.y);
  }
  
  float distance2opLair(Point paramPoint)
  {
    return Math.abs(this.opLair.coor.x - paramPoint.x) + Math.abs(this.opLair.coor.y - paramPoint.y);
  }
  
  void findBestMove()
  {
    if (this.points_trap.size() == 0) {
      setPointsTrap();
    }
    this.aggressiveness = (1.0D + (this.ai.chessLeft() - this.op.chessLeft() + 1) / 8.0D);
    double d = 0.0D;
    Iterator localIterator1 = this.op.hand.iterator();
    if (!localIterator1.hasNext())
    {
      if (GameActivity.debugMode) {
        Log.d(this.tag, d);
      }
      this.defensiveness = (1.0D + d / 10.0D);
      d = 0.0D;
      localIterator1 = this.ai.hand.iterator();
      label119:
      if (localIterator1.hasNext()) {
        break label697;
      }
      this.raid = (1.0D + d / 92.0D);
      if (GameActivity.debugMode) {
        Log.d(this.tag, "aggressiveness: " + this.aggressiveness);
      }
      if (GameActivity.debugMode) {
        Log.d(this.tag, "defensiveness: " + this.defensiveness);
      }
      if (GameActivity.debugMode) {
        Log.d(this.tag, "raid: " + this.raid);
      }
      localIterator1 = this.ai.hand.iterator();
      label254:
      if (localIterator1.hasNext()) {
        break label736;
      }
      localIterator1 = this.ai.hand.iterator();
      label276:
      if (localIterator1.hasNext()) {
        break label768;
      }
      if (GameActivity.debugMode) {
        Log.d(this.tag, this.movedChessSet.toString());
      }
      sortPendingMoves();
      checkLair();
      d = 0.0D;
      localIterator1 = this.pendingMoves.iterator();
      label326:
      if (localIterator1.hasNext()) {
        break label1415;
      }
      this.threatSet.clear();
      this.feastSet.clear();
      this.beastSet.clear();
      this.pendingMoves.clear();
      this.movedChessSet.clear();
      if ((this.ai.chessToMove == -1) || (this.ai.nextMove == -1))
      {
        localIterator1 = this.ai.hand.iterator();
        label405:
        if (localIterator1.hasNext()) {
          break label1557;
        }
        localIterator1 = this.pendingMoves.iterator();
      }
    }
    for (;;)
    {
      if (!localIterator1.hasNext())
      {
        if (GameActivity.debugMode) {
          Log.d(this.tag, this.ai.chessToMove + ": " + this.ai.nextMove);
        }
        if (GameActivity.debugMode) {
          Log.d(this.tag, ((Chess)this.ai.hand.get(this.ai.chessToMove)).rank + ": " + this.board.getValidMove((Chess)this.ai.hand.get(this.ai.chessToMove)).get(this.ai.nextMove));
        }
        return;
        localObject = (Chess)localIterator1.next();
        if (!((Chess)localObject).alive) {
          break;
        }
        if (GameActivity.debugMode) {
          Log.d(this.tag, findShortestMove((Chess)localObject, this.myLair) + 1);
        }
        if (GameActivity.debugMode) {
          Log.d(this.tag, d);
        }
        d = preciseAdd(d, 1.0D / preciseLog10(findShortestMove((Chess)localObject, this.myLair) + 1.0D));
        break;
        label697:
        localObject = (Chess)localIterator1.next();
        if (!((Chess)localObject).alive) {
          break label119;
        }
        d = preciseAdd(d, findShortestMove((Chess)localObject, this.opLair));
        break label119;
        label736:
        localObject = (Chess)localIterator1.next();
        if (!((Chess)localObject).alive) {
          break label254;
        }
        checkThreat((Chess)localObject, lookUpRange);
        break label254;
        label768:
        localObject = (Chess)localIterator1.next();
        if (!((Chess)localObject).alive) {
          break label276;
        }
        Iterator localIterator2;
        if (((Chess)localObject).threat != null) {
          localIterator2 = ((Chess)localObject).threat.iterator();
        }
        for (;;)
        {
          if (!localIterator2.hasNext())
          {
            if ((((Chess)localObject).target == null) || (this.movedChessSet.contains(((Chess)localObject).rank))) {
              break;
            }
            localIterator2 = ((Chess)localObject).target.iterator();
            while (localIterator2.hasNext())
            {
              localChess1 = (Chess)localIterator2.next();
              if (GameActivity.debugMode) {
                Log.d(this.tag, ((Chess)localObject).rank + ": I am going to hunt " + localChess1.rank);
              }
              setPath2Prey((Chess)localObject, localChess1, null);
            }
            break;
          }
          Chess localChess1 = (Chess)localIterator2.next();
          if (((Chess)localObject).rank.equals(localChess1.rank))
          {
            if (distance(((Chess)localObject).coor, localChess1.coor) > 2.0F)
            {
              if (!GameActivity.debugMode) {
                continue;
              }
              Log.d(this.tag, ((Chess)localObject).rank + ": I am far away enough from " + localChess1.rank);
            }
          }
          else
          {
            if (GameActivity.debugMode) {
              Log.d(this.tag, ((Chess)localObject).rank + ": I am going to flee from " + localChess1.rank);
            }
            setPath2Flee((Chess)localObject, localChess1);
          }
          if (this.feastSet.contains(localChess1))
          {
            if (GameActivity.debugMode) {
              Log.d(this.tag, "Send someone for " + localChess1.rank + ".");
            }
            float f1 = this.maxMove;
            Iterator localIterator3 = this.ai.hand.iterator();
            while (localIterator3.hasNext())
            {
              Chess localChess2 = (Chess)localIterator3.next();
              if (localChess2.target.contains(localChess1))
              {
                if (GameActivity.debugMode) {
                  Log.d(this.tag, "Send " + localChess2.rank);
                }
                float f2 = findShortestMove(localChess2, localChess1);
                if (f1 >= f2) {
                  if ((localChess2.rank.equals(localChess1.rank)) && (f2 <= 2.0F))
                  {
                    f1 = f2;
                    if (GameActivity.debugMode)
                    {
                      Log.d(this.tag, localChess2.rank + ": Seems Dangerous to get closer...");
                      f1 = f2;
                    }
                  }
                  else
                  {
                    setPath2Prey(localChess2, localChess1, (Chess)localObject);
                    f1 = f2;
                    if (GameActivity.debugMode)
                    {
                      Log.d(this.tag, localChess2.rank + ": I am going for " + localChess1.rank + "(" + localChess1.coor.x + "," + localChess1.coor.y + ")!");
                      f1 = f2;
                    }
                  }
                }
              }
            }
          }
        }
        label1415:
        localObject = (aiMove)localIterator1.next();
        if (GameActivity.debugMode) {
          Log.d(this.tag, Math.round(((aiMove)localObject).priority * 100.0D) / 100.0D + ": \t" + ((aiMove)localObject).point.validMoveID + "_" + ((aiMove)localObject).chess.rank);
        }
        if (d >= ((aiMove)localObject).priority) {
          break label326;
        }
        d = ((aiMove)localObject).priority;
        this.ai.chessToMove = ((aiMove)localObject).chess.rank.ordinal();
        this.ai.nextMove = ((aiMove)localObject).point.validMoveID;
        break label326;
        label1557:
        localObject = (Chess)localIterator1.next();
        if (!((Chess)localObject).alive) {
          break label405;
        }
        createPossibleMoves((Chess)localObject);
        break label405;
      }
      Object localObject = (aiMove)localIterator1.next();
      if (GameActivity.debugMode) {
        Log.d(this.tag, Math.round(((aiMove)localObject).priority * 100.0D) / 100.0D + ": \t" + ((aiMove)localObject).chess.rank);
      }
      if (d < ((aiMove)localObject).priority)
      {
        d = ((aiMove)localObject).priority;
        this.ai.chessToMove = ((aiMove)localObject).chess.rank.ordinal();
        this.ai.nextMove = ((aiMove)localObject).point.validMoveID;
      }
    }
  }
  
  int findShortestMove(Chess paramChess, int paramInt1, int paramInt2)
  {
    Point localPoint1 = new Point(paramInt1, paramInt2);
    if (paramChess.coor.equals(localPoint1)) {
      return 0;
    }
    paramInt1 = this.maxMove;
    Chess localChess = Chess.clone(paramChess);
    Iterator localIterator1 = this.board.getValidMove(paramChess).iterator();
    if (!localIterator1.hasNext()) {
      return paramInt1;
    }
    Object localObject1 = (Point)localIterator1.next();
    HashSet localHashSet1 = new HashSet();
    HashSet localHashSet2 = new HashSet();
    localHashSet1.add(localObject1);
    localHashSet1.addAll(this.board.getValidMove(paramChess));
    paramInt2 = paramInt1;
    do
    {
      do
      {
        paramInt1 = paramInt2;
        if (localHashSet1.size() <= 0) {
          break;
        }
        localObject1 = (Point)localHashSet1.iterator().next();
        localHashSet1.remove(localObject1);
      } while (localHashSet2.contains(localObject1));
      localHashSet2.add(localObject1);
      localObject2 = this.board.getTile((Point)localObject1).containing;
    } while (((localObject2 != null) && (Chess.canCapture((Chess)localObject2, paramChess, false))) || (checkAdjPredator(paramChess, (Point)localObject1)));
    int i = paramInt2;
    Object localObject2 = localObject1;
    if (localPoint1.equals(localObject1))
    {
      if (GameActivity.debugMode) {
        Log.d(this.tag, "move(): " + ((Point)localObject1).move());
      }
      paramInt1 = 1;
      if (((Point)localObject1).parent != null) {
        break label418;
      }
      i = paramInt2;
      localObject2 = localObject1;
      if (paramInt1 < paramInt2)
      {
        localObject2 = localObject1;
        i = paramInt1;
      }
    }
    localChess.coor.set((Point)localObject2);
    Iterator localIterator2 = this.board.getValidMove(localChess).iterator();
    label310:
    label365:
    label498:
    for (;;)
    {
      paramInt2 = i;
      if (!localIterator2.hasNext()) {
        break;
      }
      Point localPoint2 = (Point)localIterator2.next();
      localObject1 = new Point();
      Object localObject3 = localObject1;
      Iterator localIterator3;
      if (localHashSet1.contains(localPoint2))
      {
        localIterator3 = localHashSet1.iterator();
        if (localIterator3.hasNext()) {
          break label467;
        }
      }
      for (;;)
      {
        if (((Point)localObject2).move() >= ((Point)localObject1).move()) {
          break label498;
        }
        localObject3 = localObject1;
        localHashSet1.remove(localObject3);
        localPoint2.parent = ((Point)localObject2);
        localHashSet1.add(localPoint2);
        break label310;
        if (GameActivity.debugMode) {
          Log.d(this.tag, localObject1 + ",");
        }
        localObject1 = ((Point)localObject1).parent;
        paramInt1 += 1;
        break;
        localObject3 = (Point)localIterator3.next();
        localObject1 = localObject3;
        if (!((Point)localObject3).equals(localPoint2)) {
          break label365;
        }
        localObject1 = localObject3;
      }
    }
  }
  
  int findShortestMove(Chess paramChess1, Chess paramChess2)
  {
    return findShortestMove(paramChess1, paramChess2.coor);
  }
  
  int findShortestMove(Chess paramChess, Point paramPoint)
  {
    return findShortestMove(paramChess, paramPoint.x, paramPoint.y);
  }
  
  double findShortestPath(Chess paramChess)
  {
    Chess localChess1 = Chess.clone(paramChess);
    double d2 = 10.0D;
    HashSet localHashSet1 = new HashSet();
    HashSet localHashSet2 = new HashSet();
    localHashSet1.addAll(this.board.getValidMove(paramChess));
    for (;;)
    {
      if (localHashSet1.size() <= 0)
      {
        if (GameActivity.debugMode) {
          Log.d(this.tag, paramChess.rank + " shortest path score: " + d2);
        }
        return d2;
      }
      Point localPoint = (Point)localHashSet1.iterator().next();
      localHashSet1.remove(localPoint);
      if (GameActivity.debugMode) {
        Log.d(this.tag, paramChess.rank + ":" + localPoint);
      }
      if (!localHashSet2.contains(localPoint))
      {
        if (GameActivity.debugMode) {
          Log.d(this.tag, paramChess.rank + ":" + localPoint + " not yet checked.");
        }
        localHashSet2.add(localPoint);
        Chess localChess2 = this.board.getTile(localPoint).containing;
        double d1 = d2;
        if (localChess2 != null)
        {
          if (GameActivity.debugMode) {
            Log.d(this.tag, paramChess.rank + " may encounter an enemy.");
          }
          d1 = d2;
          if (Chess.canCapture(localChess2, paramChess, false))
          {
            if (GameActivity.debugMode) {
              Log.d(this.tag, paramChess.rank + " may be captured by an enemy if it move to " + localPoint.x + "," + localPoint.y + ".");
            }
            d2 = preciseMultiply(d2, 0.5D);
            d1 = d2;
            if (distance(localChess2.coor, localPoint) < distance(localChess2.coor, paramChess.coor)) {
              d1 = preciseMultiply(d2, 0.1D);
            }
          }
          d2 = d1;
          if (checkAdjPredator(paramChess, localPoint)) {
            d2 = preciseMultiply(d1, 0.8D);
          }
          d1 = d2;
          if (localChess2.rank.equals(Chess.Animal.LAIR)) {
            d1 = preciseMultiply(d2, 1.2D);
          }
        }
        localChess1.coor.set(localPoint);
        if (GameActivity.debugMode) {
          Log.d(this.tag, paramChess.rank + ": if it moves to " + localPoint);
        }
        localHashSet1.addAll(this.board.getValidMove(localChess1));
        d2 = d1;
        if (GameActivity.debugMode)
        {
          Log.d(this.tag, "validMoves Size: " + localHashSet1.size());
          d2 = d1;
        }
      }
    }
  }
  
  double getPriority(double paramDouble, Chess paramChess1, Chess paramChess2, Chess paramChess3, int paramInt)
  {
    return Math.cbrt(paramChess2.rank.ordinal()) * paramDouble + (1.0D - paramInt / 8.0D);
  }
  
  void predict()
  {
    if (GameActivity.debugMode) {
      Log.d(this.tag, "op Last Move " + this.opLastMoveChess.rank + " from \n" + this.opLastMove + " to \n" + this.opLastMoveChess.coor);
    }
    float f1 = distance2myLair(this.opLastMove);
    float f2 = distance2myLair(this.opLastMoveChess.coor);
    if (GameActivity.debugMode) {
      Log.d(this.tag, "old: " + f1 + ", new: " + f2);
    }
    Object localObject = null;
    int i = this.maxMove;
    Iterator localIterator = this.ai.hand.iterator();
    for (;;)
    {
      if (!localIterator.hasNext())
      {
        if (GameActivity.debugMode) {
          Log.d(this.tag, "Closest Chess to Last op Move is " + ((Chess)localObject).rank + " at distance " + i);
        }
        Chess.canCapture(this.opLastMoveChess, (Chess)localObject, false);
        return;
      }
      Chess localChess = (Chess)localIterator.next();
      int j = Math.abs(localChess.coor.x - this.opLastMoveChess.coor.x) + Math.abs(localChess.coor.y - this.opLastMoveChess.coor.y);
      if (i > j)
      {
        i = j;
        localObject = localChess;
      }
    }
  }
  
  void removePendingMoves(Chess paramChess)
  {
    if (GameActivity.debugMode) {
      Log.d(this.tag, "Remove chess moves for " + paramChess.rank);
    }
    Iterator localIterator = this.pendingMoves.iterator();
    for (;;)
    {
      if (!localIterator.hasNext()) {
        return;
      }
      aiMove localaiMove = (aiMove)localIterator.next();
      if (localaiMove.chess == paramChess) {
        localaiMove.priority = 0.0D;
      }
    }
  }
  
  void retrieveOpMove(Chess paramChess)
  {
    this.opLastMoveChess = paramChess;
    this.opLastMove = new Point(paramChess.coor);
  }
  
  boolean setPath2Flee(Chess paramChess1, Chess paramChess2)
  {
    boolean bool = false;
    if (!this.board.isSameLand(paramChess1, paramChess2))
    {
      if (GameActivity.debugMode) {
        Log.d(this.tag, paramChess1.rank + ": One of us is in River.  No Worry. ");
      }
      return false;
    }
    distance(paramChess1.coor, paramChess2.coor);
    float f1 = findShortestMove(paramChess2, paramChess1);
    if (GameActivity.debugMode) {
      Log.d(this.tag, paramChess1.rank + ": Original distance from " + paramChess2.rank + " is " + f1);
    }
    if (f1 > 4.0F)
    {
      if (GameActivity.debugMode) {
        Log.d(this.tag, paramChess1.rank + ": Seems far enough.  No Worry");
      }
      return false;
    }
    Iterator localIterator = this.board.getValidMove(paramChess1).iterator();
    for (;;)
    {
      if (!localIterator.hasNext()) {
        return bool;
      }
      Point localPoint = (Point)localIterator.next();
      distance(localPoint, paramChess2.coor);
      float f2 = findShortestMove(paramChess2, localPoint);
      if (GameActivity.debugMode) {
        Log.d(this.tag, paramChess1.rank + ": Moving to " + localPoint + " will have distance " + f2);
      }
      if (f2 >= f1)
      {
        if (GameActivity.debugMode) {
          Log.d(this.tag, paramChess1.rank + ": So I am going " + localPoint);
        }
        f1 = f2;
        double d1 = this.defensiveness;
        double d2 = preciseLog10(paramChess1.rank.ordinal() + 1.0D);
        d2 = 1.0D - preciseLog10(f1) + d1 * (1.0D + d2);
        d1 = d2;
        if (checkAdjPredator(paramChess1, localPoint)) {
          d1 = preciseDivide(d2, 5.0D);
        }
        d2 = d1;
        if (this.board.getTile(localPoint).tileType == Board.TileType.RIVER) {
          d2 = preciseAdd(d1, 0.01D);
        }
        this.pendingMoves.add(new aiMove(paramChess1, localPoint, d2));
        bool = true;
      }
    }
  }
  
  boolean setPath2Point(Chess paramChess, Point paramPoint, double paramDouble)
  {
    Chess localChess = Chess.clone(paramChess);
    Iterator localIterator1 = this.board.getValidMove(paramChess).iterator();
    int i;
    HashSet localHashSet1;
    Point localPoint1;
    Object localObject2;
    int k;
    do
    {
      HashSet localHashSet2;
      do
      {
        while (localHashSet1.size() <= 0)
        {
          if (!localIterator1.hasNext()) {
            return false;
          }
          localObject1 = (Point)localIterator1.next();
          i = 0;
          localHashSet1 = new HashSet();
          localHashSet2 = new HashSet();
          localHashSet1.add(localObject1);
        }
        localPoint1 = (Point)localHashSet1.iterator().next();
        localHashSet1.remove(localPoint1);
      } while (localHashSet2.contains(localPoint1));
      localHashSet2.add(localPoint1);
      localObject1 = this.board.getTile(localPoint1).containing;
      localObject2 = localPoint1;
      k = i;
      if (!paramPoint.equals(localPoint1)) {
        break;
      }
    } while ((localObject1 != null) && (Chess.canCapture((Chess)localObject1, paramChess, false)));
    Object localObject1 = localPoint1;
    int j = i;
    if (GameActivity.debugMode)
    {
      Log.d(this.tag, "move(): " + localPoint1.move());
      j = i;
      localObject1 = localPoint1;
    }
    Iterator localIterator2;
    if (((Point)localObject1).parent == null)
    {
      this.pendingMoves.add(new aiMove(paramChess, (Point)localObject1, paramDouble));
      localObject2 = localObject1;
      k = j;
      if (GameActivity.debugMode)
      {
        Log.d(this.tag, "moves: " + j);
        k = j;
        localObject2 = localObject1;
      }
      localChess.coor.set((Point)localObject2);
      localIterator2 = this.board.getValidMove(localChess).iterator();
    }
    label318:
    label370:
    label501:
    for (;;)
    {
      i = k;
      if (!localIterator2.hasNext()) {
        break;
      }
      Point localPoint2 = (Point)localIterator2.next();
      localObject1 = new Point();
      Iterator localIterator3;
      if (localHashSet1.contains(localPoint2))
      {
        localIterator3 = localHashSet1.iterator();
        if (localIterator3.hasNext()) {
          break label470;
        }
      }
      for (;;)
      {
        if (((Point)localObject2).move() >= ((Point)localObject1).move()) {
          break label501;
        }
        localHashSet1.remove(localObject1);
        localPoint2.parent = ((Point)localObject2);
        localHashSet1.add(localPoint2);
        break label318;
        if (GameActivity.debugMode) {
          Log.d(this.tag, localObject1 + ",");
        }
        localObject1 = ((Point)localObject1).parent;
        j += 1;
        break;
        localPoint1 = (Point)localIterator3.next();
        localObject1 = localPoint1;
        if (!localPoint1.equals(localPoint2)) {
          break label370;
        }
        localObject1 = localPoint1;
      }
    }
  }
  
  int setPath2Prey(Chess paramChess1, Chess paramChess2, Chess paramChess3)
  {
    Chess localChess = Chess.clone(paramChess1);
    Iterator localIterator1 = this.board.getValidMove(paramChess1).iterator();
    Object localObject1;
    int i;
    HashSet localHashSet1;
    Object localObject3;
    int k;
    Object localObject2;
    int j;
    double d1;
    double d2;
    label348:
    do
    {
      HashSet localHashSet2;
      do
      {
        while (localHashSet1.size() <= 0)
        {
          if (!localIterator1.hasNext()) {
            return -1;
          }
          localObject1 = (Point)localIterator1.next();
          i = 1;
          localHashSet1 = new HashSet();
          localHashSet2 = new HashSet();
          localHashSet1.add(localObject1);
        }
        localObject1 = (Point)localHashSet1.iterator().next();
        localHashSet1.remove(localObject1);
      } while (localHashSet2.contains(localObject1));
      localHashSet2.add(localObject1);
      localObject4 = this.board.getTile((Point)localObject1).containing;
      localObject3 = localObject1;
      k = i;
      if (localObject4 == null) {
        break;
      }
      localObject3 = localObject1;
      k = i;
      if (localObject4 == paramChess2)
      {
        localObject2 = localObject1;
        j = i;
        if (GameActivity.debugMode)
        {
          Log.d(this.tag, "move(): " + ((Point)localObject1).move());
          j = i;
          localObject2 = localObject1;
        }
        if (((Point)localObject2).parent != null) {
          break label592;
        }
        d1 = this.aggressiveness;
        d2 = preciseLog10(paramChess2.rank.ordinal() + 1.0D);
        d2 = 1.0D - preciseLog10(j) + d1 * (1.0D + d2);
        if (paramChess2 == this.opLair)
        {
          d1 = this.raid;
          d2 = preciseLog10(10.0D);
          d2 = 1.0D - preciseLog10(j) + d1 * d2;
        }
        d1 = d2;
        if (paramChess3 != null)
        {
          if (!paramChess3.rank.equals(Chess.Animal.LAIR)) {
            break label643;
          }
          i = findShortestMove(paramChess2, paramChess3);
          d1 = preciseAdd(preciseMultiply(d2, 10.0D), 1.0D - preciseLog10(i));
          this.movedChessSet.add(paramChess1.rank);
        }
        d2 = d1;
        if (checkAdjPredator(paramChess1, (Point)localObject2)) {
          d2 = preciseDivide(d1, 5.0D);
        }
        this.pendingMoves.add(new aiMove(paramChess1, (Point)localObject2, d2));
        localObject3 = localObject2;
        k = j;
        if (GameActivity.debugMode)
        {
          Log.d(this.tag, "moves: " + j);
          k = j;
          localObject3 = localObject2;
        }
      }
      i = k;
    } while (Chess.canCapture((Chess)localObject4, paramChess1, false));
    localChess.coor.set(localObject3);
    Object localObject4 = this.board.getValidMove(localChess).iterator();
    label543:
    label695:
    for (;;)
    {
      label491:
      i = k;
      if (!((Iterator)localObject4).hasNext()) {
        break;
      }
      Point localPoint = (Point)((Iterator)localObject4).next();
      localObject1 = new Point();
      Iterator localIterator2;
      if (localHashSet1.contains(localPoint))
      {
        localIterator2 = localHashSet1.iterator();
        if (localIterator2.hasNext()) {
          break label664;
        }
      }
      for (;;)
      {
        if (localObject3.move() >= ((Point)localObject1).move()) {
          break label695;
        }
        localHashSet1.remove(localObject1);
        localPoint.parent = localObject3;
        localHashSet1.add(localPoint);
        break label491;
        label592:
        if (GameActivity.debugMode) {
          Log.d(this.tag, localObject2 + ",");
        }
        localObject2 = ((Point)localObject2).parent;
        j += 1;
        break;
        d1 = preciseMultiply(d2, Math.cbrt(paramChess3.rank.ordinal()));
        break label348;
        localObject2 = (Point)localIterator2.next();
        localObject1 = localObject2;
        if (!((Point)localObject2).equals(localPoint)) {
          break label543;
        }
        localObject1 = localObject2;
      }
    }
  }
  
  void setPath2ProtectLair(Chess paramChess, int paramInt, ArrayList<Point> paramArrayList, ArrayList<Chess> paramArrayList1)
  {
    paramArrayList = paramArrayList.iterator();
    for (;;)
    {
      if (!paramArrayList.hasNext()) {}
      Point localPoint;
      int j;
      label176:
      do
      {
        return;
        localPoint = (Point)paramArrayList.next();
        Iterator localIterator = checkRangeEnemy(paramChess, 1).iterator();
        Chess localChess;
        do
        {
          if (!localIterator.hasNext())
          {
            j = findShortestMove(paramChess, localPoint);
            if ((j != 0) || (checkAdjPredator(paramChess))) {
              break label176;
            }
            removePendingMoves(paramChess);
            break;
          }
          localChess = (Chess)localIterator.next();
        } while ((!paramArrayList1.contains(localChess)) || (!this.board.isMoveValid(paramChess, localChess)));
        if (GameActivity.debugMode) {
          Log.d(this.tag, paramChess.rank + ": Attack " + localChess.rank + "!");
        }
        setPath2Prey(paramChess, localChess, this.myLair);
        return;
      } while (j > 5);
      int i = j;
      if (j == 0) {
        i = 1;
      }
      double d2 = this.defensiveness * 10.0D + (1.0D - preciseLog10(i)) + (1.0D - preciseLog10(paramInt));
      if (GameActivity.debugMode) {
        Log.d(this.tag, paramChess.rank);
      }
      double d1 = d2;
      if (checkAdjPredator(paramChess, localPoint))
      {
        d1 = d2;
        if (i <= 2) {
          d1 = preciseDivide(d2, 5.0D);
        }
      }
      if (checkAdjPredator(this.myLair, localPoint)) {}
      d2 = d1;
      if (checkAdjPredator(paramChess)) {
        d2 = preciseMultiply(d1, 5.0D);
      }
      d1 = d2;
      if (checkAdjAlly(localPoint)) {
        d1 = preciseDivide(d2, 5.0D);
      }
      if (GameActivity.debugMode) {
        Log.d(this.tag, paramChess.rank + " sees danger to lair and going with priority " + d1);
      }
      setPath2Point(paramChess, localPoint, d1);
    }
  }
  
  void setPointsTrap()
  {
    int k = this.myLair.coor.x;
    int m = this.myLair.coor.y;
    int j = m - 1;
    int i = j;
    if (j < 0) {
      i = m + 1;
    }
    int n = k - 1;
    int i1 = k + 1;
    Point[] arrayOfPoint = new Point[3];
    arrayOfPoint[0] = new Point(k, i);
    arrayOfPoint[1] = new Point(i1, m);
    arrayOfPoint[2] = new Point(n, m);
    int i2 = arrayOfPoint.length;
    j = 0;
    if (j >= i2)
    {
      arrayOfPoint = new Point[5];
      arrayOfPoint[0] = new Point(k, i + 1);
      arrayOfPoint[1] = new Point(n, i);
      arrayOfPoint[2] = new Point(i1, i);
      arrayOfPoint[3] = new Point(n + 1, m);
      arrayOfPoint[4] = new Point(i1 + 1, m);
      j = arrayOfPoint.length;
      i = 0;
    }
    for (;;)
    {
      if (i >= j)
      {
        return;
        localPoint = arrayOfPoint[j];
        this.points_trap.add(localPoint);
        j += 1;
        break;
      }
      Point localPoint = arrayOfPoint[i];
      this.points_adj.add(localPoint);
      i += 1;
    }
  }
  
  void sortPendingMoves()
  {
    if (GameActivity.debugMode) {
      Log.d(this.tag, "Sort");
    }
    Iterator localIterator1 = this.pendingMoves.iterator();
    for (;;)
    {
      if (!localIterator1.hasNext()) {
        return;
      }
      Object localObject = (aiMove)localIterator1.next();
      Iterator localIterator2 = this.pendingMoves.iterator();
      while (localIterator2.hasNext())
      {
        aiMove localaiMove = (aiMove)localIterator2.next();
        if ((localObject != localaiMove) && (localaiMove.chess == ((aiMove)localObject).chess) && (localaiMove.point.validMoveID == ((aiMove)localObject).point.validMoveID))
        {
          localaiMove.priority = ((((aiMove)localObject).priority + localaiMove.priority) / 2.0D);
          localObject = localaiMove;
        }
      }
    }
  }
  
  class aiMove
  {
    Chess chess;
    Point point;
    double priority;
    
    aiMove(Chess paramChess, Point paramPoint, double paramDouble)
    {
      this.chess = paramChess;
      this.point = new Point(paramPoint);
      this.priority = paramDouble;
    }
  }
}



/* Location:           C:\Users\Administrator\Documents\ds\反编译\AndroidKiller_v1.3.1\projects\Jungledsq_ppc\ProjectSrc\smali\

 * Qualified Name:     hk.doppio.game.jungle.AI

 * JD-Core Version:    0.7.0.1

 */