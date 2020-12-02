import shed.mbed.*;
import java.util.Random;
/**
 * Write a description of class Paddle here.
 *
 * @author LW518
 * @version 03-03-2020
 */
public class Ball
{
    // instance variables - replace the example below with your own
    private int x;
    private int y;
    private int radius;
    private String LRdirection = "LEFT"; //which side(left/right) of the screen has been hit by the ball 
    private String TBdirection = "BOTTOM"; // which side(top/bottom) of the screen has been hit by the ball 
    private int P1Score = 0;
    private int P2Score = 0;
    private int ballSpeed = 1; //cant change ball speed becuase collision wont work properly
    MBed mbed;
    LED led;
    LED led2;
    Piezo piezo;
    private Random randomizer = new Random();
    /**
     * Constructor for objects of class Paddle
     */
    public Ball(int x, int y, int radius, LED led, LED led2, Piezo piezo)
    {
        // initialise instance variables
        this.x = x;
        this.y = y;
        this.radius = radius;
        LRdirection = (randomizer.nextInt(2)==0) ? "LEFT" : "RIGHT";
        TBdirection = (randomizer.nextInt(2)==0) ? "BOTTOM" : "TOP";
        this.led = led;
        this.led2 = led2;
        this.piezo = piezo;
    }

    public void render(LCD lcd)
    {
        lcd.fillCircle(this.x,this.y,this.radius,PixelColor.ON);
    }

    /**
     * getters and setters
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

    public String getLRdirection()
    {
        return LRdirection;
    }

    public String getTBdirection()
    {
        return TBdirection;
    }

    public int getP1Score()
    {
        return P1Score;
    }

    public int getP2Score()
    {
        return P2Score;
    }

    public int[] getLocation()
    {
        return new int[]{this.x,this.y};
    }

    public int setY(int y)
    {
        return this.y = y;
    }

    public int setX(int x)
    {
        return this.x = x;
    }

    public Boolean hasHitEdge()
    {   
        //checks to see if the ball has hit the edge of the board which is bwteen x values of 
        // 2-124, and returns true if it is at these x positions
        if(this.x >= 124 || this.x <= 2 || this.y <= 0 || this.y >= 32){ 
            //System.out.println("true");
            //System.out.println("X: " + this.x + " Y: " + this.y);
            return true;
        }   
        return false;
    }

    /**
     * checks to see if balls x and y, matches both paddles x and y
     */
    public Boolean hasHitPaddle(Paddle p1, Paddle p2) {
        int ballX = this.x;
        int ballY = this.y;
        int P1X = p1.getX() + 3; // returns 2 then add 3 = 5
        int P1Y = p1.getY();
        int P2X = p2.getX() - 1; // returns 122 then minus 1 = 121
        int P2Y = p2.getY();
        //checks the the Y range for paddles, therefore if the paddle is at y=11, its range will be 11+paddleheight
        if ((ballY >= P1Y-1 && ballY <= P1Y+10 && ballX == P1X) || (ballY >= P2Y-1 && ballY <= P2Y+10 && ballX == P2X)) {
            //System.out.println("true");
            return true;
        }
        return false;
    }

    /**
     * returns the next X positon for the ball
     */
    public void nextX(Paddle p1, Paddle p2) 
    {
        //if the ball has hit the edges of the board
        if (hasHitPaddle(p1, p2)) {
            //if it hits any paddle, it changes to the oppositite direction
            if (this.x <= 7) {
                this.x+=this.ballSpeed; //changes the direction of the ball if it hits the left
                LRdirection = "LEFT"; //sets which side of the screen the ball hit.
                piezo.playSound(1.0, 11000.0);
                piezo.silence();
                flashColor(led,LEDColor.GREEN);
            }
            else if (this.x >= 119) {
                this.x-=this.ballSpeed; 
                LRdirection = "RIGHT";
                piezo.playSound(1.0, 11000.0);
                piezo.silence();
                flashColor(led2, LEDColor.GREEN);
            }
        }
        else if (hasHitEdge()) { 
            //check which side of the board it hit, if its x value is 2, then it hit the left side
            //if x is 124, then it hit the right side.
            if (this.x <= 2) {
                this.x+=this.ballSpeed; //changes the direction of the ball if it hits the left
                LRdirection = "LEFT"; //sets which side of the screen the ball hit.
                P2Score++;
                playNote(piezo, Note.A3, 30);
                flashColor(led, LEDColor.RED);
                flashColor(led2, LEDColor.GREEN);
            }
            else if (this.x >= 124) {
                this.x-=this.ballSpeed; 
                LRdirection = "RIGHT";
                P1Score++;
                playNote(piezo, Note.A5, 30);
                flashColor(led, LEDColor.GREEN);
                flashColor(led2, LEDColor.RED);
            }
        }
        else {
            //if the ball hasnt hit the edge of the board, it should keep going in its current
            //direction
            if (LRdirection.equals("LEFT")) {
                this.x+=this.ballSpeed; // if it hit the left, it should move to the right
            }
            else { //if LRdirection equals RIGHT
                this.x-=this.ballSpeed;
            }
        }
    }

    /**
     * returns the next Y positon for the ball
     */
    public void nextY(Paddle p1, Paddle p2) 
    {
        //if the ball has hit the edges of the board
        if (hasHitEdge()) {
            //check which side of the board it hit, if its y value is 0, then it hit the top
            //if y is 30, then it hit the bottom.
            if (this.y <= 0) {
                this.y+=this.ballSpeed; //change directions
                TBdirection = "TOP"; //which side the ball hit
            }
            else if (this.y >= 30) {
                this.y-=this.ballSpeed;
                TBdirection = "BOTTOM";
            }
        }
        else {
            if (TBdirection.equals("BOTTOM")) {
                this.y-=this.ballSpeed;
            }
            else { //if TBdirection equals TOP
                this.y+=this.ballSpeed;
            }
        }
    }
    
    
    public void playNote(Piezo piezo, Note note, int delay) {
        long NOTE_DURATION = 300;
        piezo.playNote(note);
        sleep(delay);
        piezo.playNote(null);
        sleep(delay);
    }
    
    public void flashColor(LED led, LEDColor color)
    {
        led.setColor(color);
        sleep(10);
        led.setColor(LEDColor.BLACK);
    }
    
     /**
     * A simple support method for sleeping the program.
     * 
     * @param millis The number of milliseconds to sleep for.
     */
    public void sleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException ex) {
            // Nothing we can do.
            System.out.println(String.format("Error: %s", ex));
        }
    }
}
