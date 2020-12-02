import shed.mbed.LEDColor;
import shed.mbed.*;

/**
 * @author LW518
 * @version 2019-03-03
 *
 * 
 */
public class Program {
    // The object for interacting with the FRDM/MBED.
    private MBed mbed;
    private LCD lcd;
    private LED led;
    private LED led2;
    Piezo piezo;
    Paddle P1;
    Paddle P2;
    private int P1Score;
    private int P2Score;
    private Boolean started = false;
    private boolean optionStarted = false;
    private String se_mode =  "*2-Players     CPU   Options";
    private int mode = 1;
    private int optionMode = 1;
    private int Stage = 1;
    private boolean pauseClicked = false;

    /**
     * Open a connection to the MBED.
     */
    public Program() {
        this.mbed = MBedUtils.getMBed();
        this.lcd = mbed.getLCD();
        this.led = mbed.getLEDBoard();
        this.led2 = mbed.getLEDShield();
        this.piezo = mbed.getPiezo();      
    }

    /**
     * This shows up at the start of the game, and its like a menu option
     */
    public void shell() 
    {
        clearLCD(lcd);
        //resets 
        started = false;
        optionStarted = false;
        Stage = 1;
        mode = 1;
        optionMode = 1;
        pauseClicked = false;
        mbed.getJoystickFire().addListener(isPressed -> { if(isPressed){started = true; Stage++;}} );
        mbed.getJoystickLeft().addListener(isPressed -> { if(isPressed){if (mode == 1) { mode = 3;} else { mode--;}}} );
        mbed.getJoystickRight().addListener( isPressed -> { if(isPressed)  {if (mode == 3) {mode = 1;} else {mode++;}} });        
        while (!started) {
            if (Stage == 1) {                
                try {
                    lcd.print(3, 5, "Welcome to Pong");
                    sleep(1000);
                    clearLCD(lcd);
                    Stage++;
                }
                catch (Exception e) {
                    // do nothing
                }
            }
            if (Stage == 2) {          
                if (mode == 1) {
                    se_mode = "*2-Players   CPU   Options";
                }
                else if (mode == 2) {
                    se_mode = " 2-Players  *CPU   Options";
                }
                else if (mode == 3){
                    se_mode = " 2-Players   CPU  *Options";
                }
                try {
                    clearLCD(lcd);
                    lcd.print(3, 5, se_mode); 
                    lcd.print(3, 18, "fire to start");                  
                    sleep(200);
                }
                catch (Exception e) {
                    // do nothing
                }
            }
            if (Stage == 3) {
                if (mode != 3) {
                    startScreen();
                }
                Stage++;
            }
            if (Stage == 4) {
                if (mode == 1) {
                    render1();
                }
                else if (mode == 2) {
                    render2();
                }
                else if (mode == 3) {
                    renderOptions();
                }
            }
        }
        // recursive fucntion to start the shell method again, acts like a back/main menu function
        if (started) {
            //restoreMbedUtils(); 
            shell();
        }
    }

    public void restoreMbedUtils() 
    {
        this.mbed = MBedUtils.getMBed();
        this.lcd = mbed.getLCD();
        this.led = mbed.getLEDBoard();
        this.led2 = mbed.getLEDShield();
        this.piezo = mbed.getPiezo(); 
    }

    public void startScreen() {
        clearLCD(lcd);
        int y = (lcd.getHeight() / 2);
        for (int i = 4; i > 0; i--) {
            Boolean isTitleShowing = (i>3);
            String message = (isTitleShowing) ? "Ping Pong" : String.format("%d", i);
            int x = (isTitleShowing) ? lcd.getWidth() / 4 : lcd.getWidth() / 2;
            Note note = (!isTitleShowing) ? Note.A5 : Note.E5; 
            playNote(piezo, note, 200);
            display(message, x, y, 1000);
        }
    }

    /**
     * The starting point for the interactions.
     */
    public void display(String text, int x, int y, int duration) {
        // led.setColor(color);
        try {
            lcd.print(x, y, text);
        }
        catch(Exception e) {
            //do nothing
        }
        // duration = (duration != null) ? sleep(duration) : null; 
        sleep(duration);
        //lcd.clear(); replaced with method below - method below handles exceptins
        clearLCD(lcd);
    }

    /**
     * Main Render function
     * 
     * NB. draw, flush, repeat
     * 
     * render for 2 humans
     */
    public void render1() {
        pauseClicked = false;
        mbed.getSwitch3().addListener( isPressed -> { if(isPressed)  {pauseClicked = true;} } );
        lcd.setBufferMode(BufferMode.BUFFERED);//might delete
        Potentiometer pot1 = mbed.getPotentiometer1();
        Potentiometer pot2 = mbed.getPotentiometer2();
        // Score Display
        int centerX = lcd.getWidth()/2;
        int centerY = lcd.getHeight()/2;
        //System.out.println(centerX*2 + " " + centerY*2);
        // Paddles
        int paddleWidth = 1; //changed from 3 to 1
        int paddleHeight = 10;
        int P1X = 4; //changed from 2 to 4, chnaged collsiion area in nextX method
        int P2X = 120; // changed from 122 to 120
        double P1Y;
        double P2Y;
        //Paddle x  = new Paddle(0,0,0,0);
        Paddle P1 = new Paddle(P1X,0,paddleWidth,paddleHeight);
        Paddle P2 = new Paddle(P2X,0,paddleWidth,paddleHeight);
        // Ball
        int ballSize = 1; // changes from 2 to 1
        int ballSpeed = 1;
        //int sleepSpeed = 30; // controls framerate i think, therefore it controls balls speed
        Ball ball = new Ball(centerX, centerY, ballSize, led, led2, piezo);
        while (mbed.isOpen()) {
            if (pauseClicked) {
                break;
            }
            clearLCD(lcd);
            P1Y = pot1.getValue() * 22; 
            P2Y = pot2.getValue() * 22;        
            P1.setY((int) P1Y);
            P1.render(lcd);
            P2.setY((int) P2Y);
            P2.render(lcd);
            P1Score = ball.getP1Score();
            P2Score = ball.getP2Score();
            ball.nextX(P1, P2);
            ball.nextY(P1, P2);
            P2.runRightAI(ball);
            ball.render(lcd);
            lcd.print(centerX - 6, 2, String.format("%d:%d", P1Score, P2Score));
            lcd.flush();
            sleep(20);
        }
    }

    /**
     * render for human vs AI 
     */
    public void render2() 
    {
        pauseClicked = false;
        mbed.getSwitch3().addListener(isPressed -> { if(isPressed) {pauseClicked = true;}} );
        lcd.setBufferMode(BufferMode.BUFFERED);
        Potentiometer pot1 = mbed.getPotentiometer1();
        int centerX = lcd.getWidth()/2;
        int centerY = lcd.getHeight()/2;
        int paddleWidth = 1;
        int paddleHeight = 10;
        int P1X = 4; 
        int P2X = 120; 
        double P1Y;
        double P2Y;
        Paddle P1 = new Paddle(P1X,0,paddleWidth,paddleHeight);
        Paddle P2 = new Paddle(P2X,0,paddleWidth,paddleHeight);
        int ballSize = 1; 
        int ballSpeed = 1;
        Ball ball = new Ball(centerX, centerY, ballSize, led, led2, piezo);
        while (mbed.isOpen()) {
            if (pauseClicked) {
                break;
            }
            clearLCD(lcd);
            P1Y = pot1.getValue() * 22;       
            P1.setY((int) P1Y);
            P1.render(lcd);
            P2.runRightAI(ball);
            P2.render(lcd);  
            P1Score = ball.getP1Score();
            P2Score = ball.getP2Score();
            ball.nextX(P1, P2);
            ball.nextY(P1, P2);
            ball.render(lcd);
            lcd.print(centerX - 6, 2, String.format("%d:%d", P1Score, P2Score));
            lcd.flush();
            sleep(10);
        }
    }

    /**
     * renders options menu
     */
    public void renderOptions() {
        pauseClicked = false;
        mbed.getJoystickFire().addListener(isPressed -> { if(isPressed){optionStarted = true;}} );
        mbed.getSwitch3().addListener(isPressed -> { if(isPressed) {pauseClicked = true;}});
        mbed.getJoystickUp().addListener(isPressed -> { if(isPressed){ if (optionMode == 1) { optionMode =5;} else {optionMode--;}}} );
        mbed.getJoystickDown().addListener(isPressed -> { if(isPressed){ if (optionMode == 5) { optionMode =1;} else {optionMode++;}    }} );
        lcd.setBufferMode(BufferMode.BUFFERED);
        String uparrow = "^";
        String downarrow = "v";
        String firstLine = "2P powerupMode";
        String secondLine = "1P powerupMode";
        while (mbed.isOpen()){ 
            if (pauseClicked) {
                break;
            }           
            if (optionMode == 1) {
                firstLine = "*2P powerupMode";
                secondLine = "1P powerupMode";
            }
            else if (optionMode == 2) {
                firstLine = "2P powerupMode";
                secondLine = "*1P powerupMode";
            }
            else if (optionMode == 3) {
                firstLine = "1P powerupMode";
                secondLine = "*nothing yet1";
            }
            else if (optionMode == 4) {
                firstLine = "nothing yet1";
                secondLine = "*nothing yet2";
            }
            else if (optionMode == 5) {
                firstLine = "nothing yet2";
                secondLine = "*nothing yet3";
            }
            //when an option is choosen, it runs the render for it
            while (optionStarted) {
                if (optionMode == 1) {
                    renderOptionMode1();
                }
                else if (optionMode == 2) {
                    renderOptionMode2();
                }
                else if (optionMode == 3) {
                    renderOptionMode3();
                }
                else if (optionMode == 4) {
                    renderOptionMode4();
                }
                else if (optionMode == 5) {
                    renderOptionMode5();
                }
            }
            clearLCD(lcd);
            lcd.print(5, 2, firstLine);
            lcd.print(5, 18, secondLine);
            lcd.print(115,2,uparrow);
            lcd.print(115,18,downarrow);
            lcd.flush();
            sleep(150);
        }

    }

    /**
     * renders game modes
     */
    public void renderOptionMode1() 
    {
        pauseClicked = false;
        mbed.getSwitch3().addListener( isPressed -> { if(isPressed)  {pauseClicked = true; optionStarted = false;}});
        lcd.setBufferMode(BufferMode.BUFFERED);
        startScreen();
        Potentiometer pot1 = mbed.getPotentiometer1();
        Potentiometer pot2 = mbed.getPotentiometer2();       
        int centerX = lcd.getWidth()/2;
        int centerY = lcd.getHeight()/2;
        int paddleWidth = 1; 
        int paddleHeight = 10;
        int P1X = 4; 
        int P2X = 120; 
        double P1Y;
        double P2Y;    
        Paddle P1 = new Paddle(P1X,0,paddleWidth,paddleHeight);
        Paddle P2 = new Paddle(P2X,0,paddleWidth,paddleHeight);
        int ballSize = 1; 
        int ballSpeed = 1;
        Ball ball = new Ball(centerX, centerY, ballSize, led, led2, piezo);
        PUHandler phandler = new PUHandler(P1,P2, lcd);
        while (mbed.isOpen()) {
            if (pauseClicked) {
                break;
            }
            clearLCD(lcd);
            phandler.render(lcd, ball);
            P1Y = pot1.getValue() * 22; 
            P2Y = pot2.getValue() * 22;        
            P1.setY((int) P1Y);
            P1.render(lcd);
            P2.setY((int) P2Y);
            P2.render(lcd);
            P1Score = ball.getP1Score();
            P2Score = ball.getP2Score();
            ball.nextX(P1, P2);
            ball.nextY(P1, P2);
            P2.runRightAI(ball);
            ball.render(lcd);
            lcd.print(centerX - 6, 2, String.format("%d:%d", P1Score, P2Score));
            lcd.flush();
            sleep(20);
        }
        restoreMbedUtils();
        renderOptions();
    }

    public void renderOptionMode2() 
    {
        pauseClicked = false;
        mbed.getSwitch3().addListener(isPressed -> { if(isPressed) {pauseClicked = true;  optionStarted = false;}} );
        lcd.setBufferMode(BufferMode.BUFFERED);
        startScreen();
        Potentiometer pot1 = mbed.getPotentiometer1();
        int centerX = lcd.getWidth()/2;
        int centerY = lcd.getHeight()/2;
        int paddleWidth = 1;
        int paddleHeight = 10;
        int P1X = 4; 
        int P2X = 120; 
        double P1Y;
        double P2Y;
        Paddle P1 = new Paddle(P1X,0,paddleWidth,paddleHeight);
        Paddle P2 = new Paddle(P2X,0,paddleWidth,paddleHeight);
        int ballSize = 1; 
        int ballSpeed = 1;
        Ball ball = new Ball(centerX, centerY, ballSize, led, led2, piezo);
        PUHandler phandler = new PUHandler(P1,P2, lcd);
        while (mbed.isOpen()) {
            if (pauseClicked) {
                break;
            }
            clearLCD(lcd);
            phandler.render(lcd, ball);
            P1Y = pot1.getValue() * 22;       
            P1.setY((int) P1Y);
            P1.render(lcd);
            P2.runRightAI(ball);
            P2.render(lcd);  
            P1Score = ball.getP1Score();
            P2Score = ball.getP2Score();
            ball.nextX(P1, P2);
            ball.nextY(P1, P2);
            ball.render(lcd);
            lcd.print(centerX - 6, 2, String.format("%d:%d", P1Score, P2Score));
            lcd.flush();
            sleep(10);
        }
        restoreMbedUtils();
        renderOptions();
    }

    public void renderOptionMode3() 
    {

    }

    public void renderOptionMode4() 
    {

    }

    public void renderOptionMode5() 
    {

    }

    /**
     * Clear the board and handle exceptions
     */
    public void clearLCD(LCD lcd) {
        try {
            lcd.clear();
        }
        catch (Exception e) {
            //do nothing
        }
    }

    public void playNote(Piezo piezo, Note note, int delay) {
        long NOTE_DURATION = 300;
        piezo.playNote(note);
        sleep(delay);
        piezo.playNote(null);
        sleep(delay);
    }

    /**
     * Close the connection to the MBED.
     */
    public void finish() {
        mbed.close();
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
