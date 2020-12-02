import shed.mbed.*;
/**
 * Write a description of class Powerups here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class PowerUps
{
    // instance variables - replace the example below with your own
    private int x;
    private int y;
    private int width;
    private int height;
    private String type;
    private String[] poweruptypes = {"BrickBreaker", "PaddleModiffier"}; //powerup can have one of these types
    
     /**
     * Constructor for objects of class Paddle
     */
    public PowerUps(int x, int y, int width, int height)
    {
        // initialise instance variables
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        type = poweruptypes[(int)(Math.random()*1.5)];
    }
    
    public int getX()
    {
        return x;
    }

    public int getY()
    {
        return y;
    }

    public int setY(int y)
    {
        return this.y = y;
    }

    public int setX(int x)
    {
        return this.x = x;
    }
    
    public String getType()
    {
        return type;
    }
    
    public void setType(int type)
    {
        this.type = poweruptypes[type];
    }

    public void render(LCD lcd)
    {
        lcd.drawRectangle(this.x,this.y,this.width,this.height,PixelColor.ON);
    }
    
    public void drop() {
        if (this.y < 33) {
            this.y++;
        }
    }
    
    public boolean hasBeenHit(Ball ball)
     {
         if ((ball.getY() <= this.y )&& (ball.getX() == this.x)) {
             return true;
        }
         return false;
     }
}
