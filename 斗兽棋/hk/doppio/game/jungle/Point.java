package hk.doppio.game.jungle;

import java.io.Serializable;

class Point
  implements Serializable
{
  private static final long serialVersionUID = 1L;
  public Point parent = null;
  public int validMoveID;
  public int x;
  public int y;
  
  public Point()
  {
    this(0, 0, -1, null);
  }
  
  public Point(int paramInt1, int paramInt2)
  {
    this(paramInt1, paramInt2, -1, null);
  }
  
  public Point(int paramInt1, int paramInt2, int paramInt3)
  {
    this(paramInt1, paramInt2, paramInt3, null);
  }
  
  public Point(int paramInt1, int paramInt2, int paramInt3, Point paramPoint)
  {
    this.x = paramInt1;
    this.y = paramInt2;
    this.validMoveID = paramInt3;
    this.parent = paramPoint;
  }
  
  public Point(Point paramPoint)
  {
    this(paramPoint.x, paramPoint.y, paramPoint.validMoveID, paramPoint.parent);
  }
  
  public boolean equals(Object paramObject)
  {
    if ((paramObject instanceof Point))
    {
      paramObject = (Point)paramObject;
      return (this.x == paramObject.x) && (this.y == paramObject.y);
    }
    return super.equals(paramObject);
  }
  
  public int hashCode()
  {
    long l = Double.doubleToLongBits(this.x) ^ Double.doubleToLongBits(this.y) * 31L;
    return (int)l ^ (int)(l >> 32);
  }
  
  public int move()
  {
    int i = 0;
    Point localPoint = this;
    for (;;)
    {
      if (localPoint.parent == null) {
        return i;
      }
      localPoint = localPoint.parent;
      i += 1;
    }
  }
  
  public void set(int paramInt1, int paramInt2)
  {
    this.x = paramInt1;
    this.y = paramInt2;
  }
  
  public void set(Point paramPoint)
  {
    this.x = paramPoint.x;
    this.y = paramPoint.y;
  }
  
  public String toString()
  {
    return this.x + ", " + this.y;
  }
}


/* Location:           C:\Users\Administrator\Documents\ds\反编译\AndroidKiller_v1.3.1\projects\Jungledsq_ppc\ProjectSrc\smali\
 * Qualified Name:     hk.doppio.game.jungle.Point
 * JD-Core Version:    0.7.0.1
 */