import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class RayTracer extends PApplet {

Boundry[] walls;
LightSource source;
FPScounter fps;

public void setup()
{  
  
  walls = new Boundry[100];
  for(int i = 0; i < walls.length; i++)
    walls[i] = new Boundry();
  
  source = new LightSource(0, 0, walls);
  fps = new FPScounter();
}

int lastMillis = 0;
  
public void draw()
{
  background(0);
 
  fps.show();
  
  source.move(mouseX, mouseY);
  source.castLight();
}
class Boundry{  

  PVector p1;
  PVector p2;
  
  Boundry()
  {
    p1 = new PVector(random(0, width), random(0, height));
    p2 = new PVector(random(0, width), random(0, height));
  }
  
  public void show()
  {
    stroke(255);
    strokeWeight(3);
    line(p1.x, p1.y, p2.x, p2.y);
    
    strokeWeight(5);
    stroke(0, 255, 0);
    point(p1.x, p1.y);
    point(p2.x, p2.y);
  }
  
  public boolean belongs(PVector p)
  {
    if(p.x>=min(p1.x, p2.x) && p.x<=max(p1.x, p2.x)
       && p.y>=min(p1.y, p2.y) && p.y<=max(p1.y, p2.y))
     {
        return true;
     }
     
     return false;
  }
}
class FPScounter
{
  int timePassed;
  int timePassedLast = 0;
  
  float fps;
  
  public void update()
  {
    timePassed = millis();
    fps = 1000.0f/((float)(timePassed-timePassedLast));
    timePassedLast = timePassed;
  }
  
  public void show()
  {
    update();
    
    println(fps);
    
    fill(255);
    textSize(20);
    text(fps, 0, 20);
  }
}
static class Geometry
{ 
  public static float calcSurface(PVector A, PVector B, PVector C)
  {
      return ((float)(A.x*B.y + A.y*C.x + B.x*C.y) - (float)(A.y*B.x + A.x*C.y + B.y*C.x))*0.5f;
  }
  
  public static PVector intersect(Boundry b1, Boundry b2)
  {
      RayTracer rt = new RayTracer();
      Ray r = rt.new Ray(b1);
      
      PVector p = (r).cast(b2, false);
      if(p!=null && b1.belongs(p)==false) p = null;
      
      return p;
  }
  
  public static boolean isSmaller(PVector A, PVector B, PVector pivot)
  {
      if(calcSurface(pivot, A, B)>0.00f) return true;
      if(calcSurface(pivot, A, B)<0.00f) return false;
      
      if(A.x!=B.x) return A.x<B.x;
      return A.y<B.y;
  }
  
  public static void swap(PVector[] p, int ind1, int ind2)
  {
      PVector C = p[ind1];
      p[ind1] = p[ind2];
      p[ind2] = C;
  }
  
  public static void sortPoints(PVector[] p, int l, int r, PVector pivot, PVector qSortPivot)
  {
     if(l>=r) return;
    
      if(qSortPivot!=null)
      {
          for(int i = l;i<=r;i++)
          {
              if(p[i]==qSortPivot) 
              {
                  swap(p, i, l);
                  break;
              }
          }
      }
    
     int lInd = l, rInd = r; 
     while(lInd<rInd)
     {
         if(isSmaller(pivot, p[lInd+1], p[lInd])==true)
         {
             swap(p, lInd, lInd+1);
             lInd++;
         }
         else
         {
             while(rInd>lInd && isSmaller(pivot, p[rInd], p[lInd])==false) rInd--;
             if(rInd==lInd) break;
            
             swap(p, rInd, lInd+1);
         }
     }
         
     sortPoints(p, l, lInd-1, pivot, null);
     sortPoints(p, lInd+1, r, pivot, null);
  }
  
  public static void correctCyclicShift(PVector[] p, PVector pivot)
  {
      int pivotInd = -1;
      PVector[] newP = new PVector[p.length];
  
      for(int i = 0;i<p.length;i++)
      {
          if(p[i]==pivot)
          {
              pivotInd = i;
              break;
          }
      }
      
      int ind = 0;
      for(int i = pivotInd;i<p.length;i++) 
      {
        newP[ind] = p[i];
        ind++;
      }
      for(int i = 0;i<pivotInd;i++)
      {
          newP[ind] = p[i];
          ind++;
      }
      
      for(int i = 0;i<p.length;i++) p[i] = newP[i];
  }
  
  public static void reversePoints(PVector[] p)
  {
      int l = 0, r = p.length - 1;
      while(l<r)
      {
          swap(p, l, r);
          l++;r--;
      }
  }
  
  public static PVector[] getAllPoints(Boundry[] walls)
  {
      ArrayList <PVector> l = new ArrayList <PVector>();
      for(Boundry wall: walls)
      {
          l.add(wall.p1);
          l.add(wall.p2);
      }
    
      for(int i = 0;i<walls.length;i++)
      {
          for(int j = i+1;j<walls.length;j++)
          {
              PVector p = intersect(walls[i], walls[j]);
              
              if(p!=null) l.add(p);
          }
      }
      
      PVector[] arr = new PVector[l.size()];
      for(int i = 0;i<l.size();i++) arr[i] = l.get(i);
      
      return arr;
  }
  
  public static int sign(float x)
  {
     if(x<0) return -1;
     if(x==0) return 0;
     if(x>0) return +1;
  
     return 69;
  }
  
  public static boolean checkArc(PVector p[], PVector pos)
  {
     int n = p.length; 
     ArrayList <PVector> l = new ArrayList();
     
     int startInd = n - 1;
     while(startInd!=0 && calcSurface(pos, p[startInd], p[0])==0) startInd--;
     
     l.add(p[startInd]);
     for(int i = 1;i<n;i++)
     {
         PVector curr = p[(i+startInd)%n];
         if(calcSurface(pos, l.get(l.size()-1), curr)!=0) l.add(curr);
     }
    
     n = l.size();
     for(int i = 0;i<n;i++)
     {
        PVector curr = l.get(i);
        PVector last = l.get((i-1+n)%n);
        PVector nxt = l.get((i+1)%n);
       
        if(sign(calcSurface(pos, curr, last))==sign(calcSurface(pos, curr, nxt))) return true;
     }
    
     return false;
  }
}
class LightSource
{
  PVector pos;
  PVector[] points;
  
  public LightSource(float x, float y, Boundry[] walls)
  {
    pos = new PVector(x, y);
    
    points = Geometry.getAllPoints(walls);
    Geometry.sortPoints(points, 0, points.length-1, pos, null);
  }
  
  public void move(float x, float y)  
  {
    pos.x = x;
    pos.y = y;
  }
  
  public void castLight()
  {
    Geometry.sortPoints(points, 0, points.length-1, pos, null);
    boolean isArc = Geometry.checkArc(points, pos);
    
    for(Boundry wall: walls) 
      wall.show();
    
    stroke(0, 255, 0);
    strokeWeight(5);
    
    beginShape();
    vertex(pos.x, pos.y);
    
    int iterLen = points.length;
    if(isArc==true)
      iterLen = points.length - 1;
    
    for(int i = 0;i<iterLen;i++)
    {
      PVector A = points[i];
      PVector B = points[(i+1)%points.length];
  
      PVector midPoint = new PVector((A.x+B.x)*0.5f, (A.y+B.y)*0.5f);
      Ray ray = new Ray(pos, midPoint);
      
      PVector closest = null;
      Boundry bestWall = null;
      
      for(Boundry wall: walls)
      {
        PVector collision = ray.cast(wall, false);
        if(collision==null) continue;
        
        if(closest==null
           || pos.dist(closest)>pos.dist(collision)) 
        {
          bestWall = wall;
          closest = collision;
        }
      }
      
      if(closest!=null)
      {          
        Ray rA = new Ray(pos, A);
        Ray rB = new Ray(pos, B);
        
        PVector p1 = rA.cast(bestWall, true);
        PVector p2 = rB.cast(bestWall, true);  
        
        strokeWeight(0);
        stroke(255, 0, 255);
        if(p1!=null && p2!=null) 
        {
          vertex(p1.x, p1.y);
          vertex(p2.x, p2.y);
        }
      }
      else
        vertex(pos.x, pos.y);
    }
    
    fill(80);
    endShape();
  }
}
class Ray{
  PVector origin;
  PVector dir;
  
  public Ray(float orgX, float orgY, float dirX, float dirY)
  {
    origin = new PVector(orgX, orgY);
    dir = new PVector(dirX-orgX, dirY-orgY);
    dir.normalize();
  }
  
  public Ray(PVector origin, PVector dir) 
  { 
    this(origin.x, origin.y, dir.x, dir.y);
  }
  
  public Ray(Boundry b)
  {
     this(b.p1, b.p2);
  }
  
  public PVector cast(Boundry wall, boolean noCheck)
  {
    float x1 = wall.p1.x;
    float y1 = wall.p1.y;
    float x2 = wall.p2.x;
    float y2 = wall.p2.y;
    float x3 = origin.x;
    float y3 = origin.y;
    float x4 = origin.x + dir.x;
    float y4 = origin.y + dir.y;
    
    float den, numo;
    den = (x1 - x2) * (y3 - y4) - (y1 - y2) * (x3 - x4);
    numo = (x1 - x3) * (y3 - y4) - (y1 - y3) * (x3 - x4);
    
    if(den == 0){
      return null;
    }
      
    float t = numo/den;
    float u = -numo/den;
        
    if((t >= 0 && t <= 1) || (noCheck==true))
    {
      numo = (x1 - x2) * (y1 - y3) - (y1 - y2) * (x1 - x3);
      u = -numo/den;
      if(u >= 0){
        PVector ret = new PVector();
        ret.x = x1 + t * (x2 - x1);
        ret.y = y1 + t * (y2 - y1);
        return ret;
      }
    }
    
    return null;
  }
  
  public void show()
  {
    pushMatrix();
    stroke(255, 0, 0);
    translate(origin.x, origin.y);
    
    strokeWeight(2);
    line(0, 0, dir.x * 50, dir.y * 50);
    popMatrix();
  }
}
class WallKeeper
{
    private ArrayList<Boundry> walls = new ArrayList<Boundry>();

    public WallKeeper(){}

    public void addWall(Boundry w)
    {
        walls.add(w);
    }
}
  public void settings() {  size(1280, 720); }
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "RayTracer" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
