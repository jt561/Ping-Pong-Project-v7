import shed.mbed.*;
import java.util.ArrayList;
import java.util.Random;
/**
 * Write a description of class PUHandler here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class PUHandler
{
    // instance variables - replace the example below with your own
    Paddle p1;
    Paddle p2;
    private LCD lcd;
    private Random randomizer = new Random();
    private ArrayList<PowerUps> powerupsList = new ArrayList<>();
    private ArrayList<Brick> brickList = new ArrayList<>();
    private int dropTimer = 0;
    private boolean powerupInProgress = false;
    private String activePowerUpType = "";
    private int activeStage = 1;
    private int powerupToRun = 0;
    private int tempPowerupTimer = 0;
    private int randomPaddleSize;

    /**
     * Constructor for objects of class PUHandler
     * stores powerup objects
     */
    public PUHandler(Paddle p1, Paddle p2, LCD lcd)
    {
        this.p1 = p1;
        this.p2 = p2;
        this.lcd = lcd;
        for (int k = 0; k < 8; k++) {
            powerupsList.add(new PowerUps( 35+randomizer.nextInt(36), 0, 2, 2));
        }
        for (int k = 0; k < 5; k++) {
            brickList.add(new Brick(65, 15, 1, 6));
        }
    }

    /**
     * drops 8 random power up objects 
     */
    public void render(LCD lcd, Ball  ball) 
    {
        if (!powerupInProgress) {
            dropTimer++;
            if (dropTimer >= 200 && dropTimer <= 250) {
                powerupsList.get(0).render(lcd);
                powerupsList.get(0).drop();
                if (powerupsList.get(0).hasBeenHit(ball)) {
                    powerupInProgress = true;
                    activePowerUpType = powerupsList.get(0).getType();
                }            
            }
            else if (dropTimer >= 251 && dropTimer <= 300) {
                powerupsList.get(1).render(lcd);
                powerupsList.get(1).drop();
                if (powerupsList.get(1).hasBeenHit(ball)) {
                    powerupInProgress = true;
                    activePowerUpType = powerupsList.get(1).getType();
                } 
            }
            else if (dropTimer >= 301 && dropTimer <= 350) {
                powerupsList.get(2).render(lcd);
                powerupsList.get(2).drop();
                if (powerupsList.get(2).hasBeenHit(ball)) {
                    powerupInProgress = true;
                    activePowerUpType = powerupsList.get(2).getType();
                } 
            }
            else if (dropTimer >= 351 && dropTimer <= 400) {
                powerupsList.get(3).render(lcd);
                powerupsList.get(3).drop();
                if (powerupsList.get(3).hasBeenHit(ball)) {
                    powerupInProgress = true;
                    activePowerUpType = powerupsList.get(3).getType();
                } 
            }
            else if (dropTimer >= 401 && dropTimer <= 450) {
                powerupsList.get(4).render(lcd);
                powerupsList.get(4).drop();
                if (powerupsList.get(4).hasBeenHit(ball)) {
                    powerupInProgress = true;
                    activePowerUpType = powerupsList.get(4).getType();
                } 
            }
            else if (dropTimer >= 451 && dropTimer <= 500) {
                powerupsList.get(5).render(lcd);
                powerupsList.get(5).drop();
                if (powerupsList.get(5).hasBeenHit(ball)) {
                    powerupInProgress = true;
                    activePowerUpType = powerupsList.get(5).getType();
                } 
            }
            else if (dropTimer >= 501 && dropTimer <= 550) {
                powerupsList.get(6).render(lcd);
                powerupsList.get(6).drop();
                if (powerupsList.get(6).hasBeenHit(ball)) {
                    powerupInProgress = true;
                    activePowerUpType = powerupsList.get(6).getType();
                } 
            }
            else if (dropTimer >= 551 && dropTimer <= 600) {
                powerupsList.get(7).render(lcd);
                powerupsList.get(7).drop();
                if (powerupsList.get(7).hasBeenHit(ball)) {
                    powerupInProgress = true;
                    activePowerUpType = powerupsList.get(7).getType();
                } 
            }
            else if (dropTimer >= 601) {
                //resets the drop and powerups
                dropTimer = 0;
                for (int i = 0; i < powerupsList.size(); i++) {
                    powerupsList.get(i).setX(35+randomizer.nextInt(36));
                    powerupsList.get(i).setY(0);
                    powerupsList.get(i).setType(randomizer.nextInt(2));
                }
            }
        }
        else if (powerupInProgress) {
            //if a power up is active, check what power up it is and who activated it     
            if (activeStage == 1) {
                if (ball.getLRdirection().equals("LEFT")) {
                    if (activePowerUpType.equals("PaddleModiffier")) {
                        powerupToRun = 1;
                    }
                    else if (activePowerUpType.equals("BrickBreaker")) {
                        powerupToRun = 2;
                    }
                }
                else if (ball.getLRdirection().equals("RIGHT")) {
                    if (activePowerUpType.equals("PaddleModiffier")) {
                        powerupToRun = 3;
                    }
                    else if (activePowerUpType.equals("BrickBreaker")) {
                        powerupToRun = 4;
                    }
                }
                randomPaddleSize = randomizer.nextInt(8)+6;
                activeStage = 2;
            }
            else if (activeStage == 2) {
                runPowerUp(powerupToRun);
            }
        }
    }

    public void runPowerUp(int powerupToRun) {
        if (tempPowerupTimer < 500) {
            if (powerupToRun == 1) {
                p1.setHeight(randomPaddleSize);
            }
            else if (powerupToRun == 2) {
                brickList.get(0).setX(30);
                brickList.get(0).setY(12);
                brickList.get(0).render(lcd);
                
                brickList.get(1).setX(35);
                brickList.get(1).setY(18);                
                brickList.get(1).render(lcd);
                
                brickList.get(2).setX(25);
                brickList.get(2).setY(8);
                brickList.get(2).render(lcd);
                
                brickList.get(3).setX(40);
                brickList.get(3).setY(20);
                brickList.get(3).render(lcd);
                
                brickList.get(4).setX(40);
                brickList.get(4).setY(4);
                brickList.get(4).render(lcd);                
            }
            else if (powerupToRun == 3) {
                p2.setHeight(randomPaddleSize);
            }
            else if (powerupToRun == 4) {
                brickList.get(0).setX(90);
                brickList.get(0).setY(12);
                brickList.get(0).render(lcd);
                
                brickList.get(1).setX(85);
                brickList.get(1).setY(18);                
                brickList.get(1).render(lcd);
                
                brickList.get(2).setX(85);
                brickList.get(2).setY(8);
                brickList.get(2).render(lcd);
                
                brickList.get(3).setX(80);
                brickList.get(3).setY(20);
                brickList.get(3).render(lcd);
                
                brickList.get(4).setX(80);
                brickList.get(4).setY(4);
                brickList.get(4).render(lcd); 
            }
            tempPowerupTimer++;
        }
        else if (tempPowerupTimer >= 500) {
            powerupInProgress = false;
            tempPowerupTimer = 0;
            activeStage = 1;
            p1.setHeight(10);
            p2.setHeight(10);
        }
    }
}
