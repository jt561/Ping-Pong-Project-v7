import shed.mbed.*;
/**
 * Write a description of class Brick here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class Brick
{
    // instance variables - replace the example below with your own
    private int x;
    private int y;
    private int width;
    private int height;

    public Brick(int x, int y, int width, int height)
    {
        // initialise instance variables
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public void render(LCD lcd)
    {
        lcd.fillRectangle(this.x,this.y,this.width,this.height,PixelColor.ON);
    }
    
    public void setX(int x) 
    {
        this.x = x;
    }
    
     public void setY(int y) 
    {
        this.y = y;
    }
}
