package PongGame;

import java.awt.*;

/**
 * Created by Danyon on 20/06/2016.
 */
public class Paddle {

    /*VARIABLE DECLARATION*/

    //paddleID to distinguish paddles
    public int paddleNum;

    //Score for players
    public int score = 0;

    //instantiates X and Y pos of paddle
    public int x;
    public float y;

    //Sets height and width of paddle
    public int width = 30;
    public int height = 200;

    public double moveAcc = 0.55;
    public double stopAcc = 0.35;

    //Velocity of paddle per 5 nanoseconds
    public float velY = 0;

    //Booleans to check which direction key is pressed
    public boolean isMoveUp = false;
    public boolean isMoveDown = false;

    //Gets reference to pongObject
    public Pong pong;

    //Constructor for Paddle Class
    public Paddle(Pong p, int paddleNum) {
        this.paddleNum = paddleNum;

        this.pong = p;

        if (paddleNum == 1) {
            this.x = 0;
        }
        else if (paddleNum == 2) {
            this.x = pong.width - width;
        }
        this.y = pong.height / 2 - this.height / 2;
    }

    //updates the y position and velocity based on checkMove()
    public void move(double increment) {
        if (checkPos(increment) == true) {
            this.y += increment;
        }
        else if (increment > 0) {
            this.y = pong.renderer.getHeight() - height;
            this.velY = 0;
        }
        else if (increment < 0) {
            this.y = 0;
            this.velY = 0;
        }
    }

    //Changes velocity based on which button is pressed
    public void checkMove() {
        if (this.isMoveUp == true) {
            this.velY -= moveAcc;
            this.move(velY);
        }
        if (this.isMoveDown == true) {
            this.velY += moveAcc;
            this.move(velY);
        }
    }

    //Used for the decelerating of the paddle
    public void stopMove() {
        if (velY < 0.25 && velY > -0.25) {
            velY = 0;
        }
        else if (velY > 0) {
            velY -= stopAcc;
            this.move(velY);
        }
        else if (velY < 0) {
            velY += stopAcc;
            this.move(velY);
        }
    }

    /*Used to check next forward position of paddle
    * Used as a collision check for wall
    * returns true if clear else false*/
    public Boolean checkPos(double increment) {
        //IF next position is past the bottom of frame
        if ((y + height + increment) >= pong.renderer.getHeight()) {
            return false;
        }
        //IF next position is past top of frame
        else if ((y + increment) <= 0) {
            return false;
        }
        //ELSE true
        else {
            return true;
        }
    }

    /*Return the x position the bottom of the paddle*/
    public float getBottomRect() {
        float bottomX;
        bottomX = y + (float) height;
        return bottomX;
    }

    /*Return the x position of paddle width*/
    public float getWidthPosX() {
        float widthX;
        widthX = x + (float) width;
        return widthX;
    }

    public void changeScreenSizePaddle() {
        if (x != 0) {
            this.x = pong.frame.getWidth() - width;
        }
    }

    //Used to render paddle objects
    public void render(Graphics g) {
        changeScreenSizePaddle();
        //Draws the current paddle
        g.setColor(new Color(255, 109, 104));
        g.fillRect(x, (int) y, this.width, this.height);
    }
}
