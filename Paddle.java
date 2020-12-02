import shed.mbed.*;
/**
 * Write a description of class Paddle here.
 *
 * @author LW518
 * @version 03-03-2020
 */
public class Paddle
{
    // instance variables - replace the example below with your own
    private int x;
    private int y;
    private int width;
    private int height;
    private int AIY;
    private int shake = 1;
    private int shakeTimer = 1;

    /**
     * Constructor for objects of class Paddle
     */
    public Paddle(int x, int y, int width, int height)
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

    /**
     * An example of a method - replace this comment with your own
     *
     * @param  y  a sample parameter for a method
     * @return    the sum of x and y
     */
    public int getX()
    {
        // put your code here
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
    
    public void setHeight(int height) 
    {
        this.height = height;
    }
    public void setWidth(int width) 
    {
        this.width = width;
    }
    
    public int getHeight() 
    {
        return this.height;
    }
    public int getWidth() 
    {
        return this.width;
    }

    /**
     * calculates the "trajectory" of the ball and moves the paddle on the right to the right position
     */
    public void runRightAI(Ball ball) 
    {
        //this calculates what the Y value the ball will be at when it ia at X value 119
        //starts calculating as soon as the balls X is 79
        if (ball.getX() == 79 && ball.getLRdirection().equals("LEFT")) {
            int temp = ball.getY();
            if (ball.getTBdirection().equals("TOP")) {
                int rev = 1;
                for (int k = 0; k<40; k++) {        
                    if (rev == 1) {
                        temp++;
                        if (temp == 32) {
                            rev = 2;
                        }
                    }
                    else if ( rev == 2){
                        temp--;
                        if (temp == 0) {
                            rev = 1;
                        }
                    }
                }
            }
            else if (ball.getTBdirection().equals("BOTTOM")) {
                int rev = 1;
                for (int k = 0; k<40; k++) {                
                    if (rev == 1) {
                        temp--;
                        if (temp == 0) {
                            rev = 2;
                        }
                    }
                    else if ( rev == 2){
                        temp++;
                        if (temp == 32) {
                            rev = 1;
                        }
                    }
                }
            }
            AIY = temp;
        }   
        //this just shakes the paddle up and down for no reason
        if (ball.getX() < 70) {
            if (shake == 1) {
                if (this.y < 22) {
                    this.y+=1;
                }
                else {
                    if (this.y > 16) {
                        this.y--;
                    }
                }
                shakeTimer++;
                if (shakeTimer == 10) {
                    if (shake == 1) {
                        shake = 2;
                    }
                    shakeTimer = 0;
                }
            }
            else if (shake == 2) {
                
                if (this.y>0) {///just edited
                    this.y-=1;
                }
                shakeTimer++;
                if (shakeTimer == 10) {
                    if (shake == 2) {
                        shake = 1;
                    }
                    shakeTimer = 0; 
                }                
            }
        }        
        //this moves the paddle slowy to where the calculated Y position of the ball
        else if (this.y != AIY && this.y <=24) {            
            if (this.y > AIY) {
                this.y--;
            }
            else if (this.y < AIY) {
                if (this.y <22) {
                    this.y++;
                }
            } 
        }
    }
    
    /**
     * calculates the "trajectory" of the ball and moves the paddle on the left to the right position
     */
    public void runLeftAI(Ball ball)
    {
        //finish later
    }
}
