package PongGame;

import java.awt.*;

/**
 * Created by Danyon on 21/06/2016.
 */
public class Ball {

    //VARIABLE DECLARATION

    //Ball width and height
    public int height = 35;
    public int width = 35;

    //Ball x and y position
    public float x;
    public float y;

    //Ball x and y velocity
    public double velY = 0.5;
    public double velX = -0.5;

    final int secondToWait = 3;
    final int timerToSeconds = 200 / secondToWait;
    int secondsOnClock = 3;
    int secondsPrevious = secondsOnClock;
    int zoomCounter;

    public boolean ballRespawn = true;
    public int respawnWaitCount = 1;

    public Pong pong;
    public Paddle player1;
    public Paddle player2;


    //END VARIABLE DECLARATION


    //Ball constructor
    public Ball(Pong pong, Paddle player1, Paddle player2) {
        this.pong = pong;
        this.player1 = player1;
        this.player2 = player2;
        this.x = ((pong.width / 2) - (width / 2));
        this.y = (pong.height / 2) - (height / 2);
    }

    public void newPos() {
        if (checkPos() != false) {
            x += velX;
            y += velY;
        }
    }

    //Used to check the next position of ball to ensure it doesnt leave the boundaries
    //Return true if not going to contact with frame else return false and update position
    public boolean checkPos() {
        //IF past bottom of frame set to frame height
        if (this.y + velY > pong.height) {
            y = pong.height;
            return false;
        }
        //IF past top of frame set to 0
        else if (this.y + velY < 0) {
            y = 0;
            return false;
        }
        else {
            return true;
        }
    }

    /*Checks for next position to be a contact with paddle*/
    public void checkPaddleColl() {
        if (x < pong.width / 2) {
            if (x + velX < player1.getWidthPosX()) {
                x = player1.getWidthPosX();
            }
        }
        else if (x > pong.width / 2) {
            if (x + velX + width > player2.x) {
                x = player2.x - width;
            }
        }
    }

    /*Rebound determination for the ball
    IF Ball touches the bottom or top invert y velocity
    IF ball hits paddle invert x velocity
    IF ball goes past paddle reset ball
    NOTE ball rally is for altering ball speed on paddle rally and resetting
     */
    public void rebound() {
        //If in contact with player 1 rebound on paddle contact
        if (x < pong.width / 2) {
            if (y <= player1.getBottomRect() && y >= player1.y) {
                checkPaddleColl();
                if (x == player1.getWidthPosX()) {
                    velX = -velX;
                    pong.ballRally(1);
                }
            }
            else {
                if (x + width < 0) {
                    player2.score++;
                    ballRespawn();
                    ballRespawn = true;
                    pong.ballRally(0);
                }
            }
        }
        else if (x > pong.width / 2) {
            if (y <= player2.getBottomRect() && y >= player2.y) {
                checkPaddleColl();
                if (x + width == player2.x) {
                    velX = -velX;
                    pong.ballRally(1);
                }
            }
            else {
                if (x > pong.width) {
                    player1.score++;
                    ballRespawn();
                    ballRespawn = true;
                    pong.ballRally(0);
                }
            }
        }
        //Rebound on the top and bottom walls
        if (y <= 0 || y >= (pong.renderer.getHeight() - height - 2)) {
            velY = -velY;
        }
    }

    public void ballRespawn() {
        this.x = ((pong.width / 2) - (width / 2));
        this.y = (pong.height / 2) - (height / 2);
    }

    //Respawn for 3 seconds before replaying
    public void ballRespawnCount() {
        if (respawnWaitCount != 200) {
            respawnWaitCount++;
            secondsOnClock = ((200 - respawnWaitCount) / timerToSeconds) + 1;
        }
        else {
            ballRespawn = false;
            respawnWaitCount = 1;
            secondsOnClock = 3;
        }
    }

    public void timerDisplay(Graphics g) {
        g.setColor(new Color(207, 94, 232));
        g.setFont(new Font("Menlo", Font.PLAIN, 80));
        if (secondsPrevious == secondsOnClock) {
            zoomCounter++;
            g.setFont(new Font("Menlo", Font.PLAIN, 80 - zoomCounter));
            g.drawString(secondsOnClock + "", pong.width / 2 - (24 - (zoomCounter / 3)), (pong.height / 4) + zoomCounter);
            secondsPrevious = secondsOnClock;
        }
        else {
            zoomCounter = 0;
            g.drawString(secondsOnClock + "", pong.width / 2 - 24, pong.height / 4);
            secondsPrevious = secondsOnClock;
        }
    }

    public void ballPos() {
        if (ballRespawn == false) {
            newPos();
            rebound();
        }
        else {
            ballRespawnCount();
        }
    }

    //Used to render paddle objects
    public void render(Graphics g) {
        if (ballRespawn == true) {
            pong.frame.setResizable(false);
            timerDisplay(g);
        }
        else {
            pong.frame.setResizable(true);
        }
        g.setColor(new Color(207, 94, 232));
        g.fillOval((int) x, (int) y, width, height);

        //g.setFont(new Font("Menlo", Font.PLAIN, 14));
        //g.drawString("velX: " + velX, 50, 80);
        //g.drawString("velY: " + velY, 50, 100);
    }
}
