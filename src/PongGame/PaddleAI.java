package PongGame;

import java.awt.*;

/**
 * Created by Danyon on 22/06/2016.
 */
public class PaddleAI {

    /*VARIABLE DECLARATION*/
    public Ball ball;
    public Paddle player2;

    public float distToBall = 0;

    //VARIABLES HERE ARE DECLARED AFTER REFERENCE TO BALL AND P2 ARE ESTABLISHED
    //Distance from 1st quarter line on paddle to mid ball (when Below)
    public float dToBBelowQOne;
    //Distance from 3rd quarter line on paddle to mid ball (when Below)
    public float dToBBelowQThree;
    //Distance from 3rd quarter line on paddle to mid ball (when Above)
    public float dToBAboveQThree;
    //Distance from 1st quarter line on paddle to mid ball (when Above)
    public float dToBAboveQOne;

    //Middle of ball points
    public float ballMidX;
    public float ballMidY;

    //Get middle point at each third
    //top of paddle
    public float pTopX;
    public float pTopY;
    //1st third of paddle
    public float pOneY;
    //2nd third of paddle
    public float pTwoY;
    //bottom of paddle
    public float pBottomY;

    //Rebound position
    public float reboundOneX;
    public float reboundOneY;
    public float reboundTwoX;
    public float reboundTwoY;
    public float reboundThreeY;
    public float reboundThreeX;

    //Set Direction to move
    public boolean moveUp = false;
    public boolean moveDown = false;
    /*END VARIABLE DECLARATION*/


    public PaddleAI(Ball ball, Paddle p2) {
        this.ball = ball;
        this.player2 = p2;
        adjustStats();

    }

    public void run() {
        setPoints();
        traceBall();
        distanceToBall();
        moveAI();
        //checkInRange();
    }

    public void moveAI() {
        if (moveUp == true) {
            player2.isMoveDown = false;
            player2.isMoveUp = true;
        }
        else if (moveDown == true) {
            player2.isMoveUp = false;
            player2.isMoveDown = true;
        }
        else {
            player2.isMoveUp = false;
            player2.isMoveDown = false;
        }
    }

    //Gets points pathway points from ball to paddle
    public void setPoints() {
        this.ballMidX = ball.x + (ball.width / 2);
        this.ballMidY = ball.y + (ball.height / 2);
        this.pTopX = player2.x + (player2.width / 2);
        this.pTopY = player2.y;
        this.pOneY = player2.y + (player2.height / 3);
        this.pTwoY = player2.y + ((player2.height / 3) * 2);
        this.pBottomY = player2.y + player2.height;
    }

    //Paddle Moves based on predicted rebound position
    public void traceBall(){
        ballPath();
        if(player2.y + (player2.height/2) > reboundOneY){
            moveDown = false;
            moveUp = true;
        }
        else{
            moveUp = false;
            moveDown = true;
        }
    }

    //Gets distance to center of ball
    public void distanceToBall() {
        getPosition();
        //IF ball below 1st quarter on paddle
        if ((player2.y + (player2.height / 3)) > (ball.y + (ball.height / 2))) {
            distToBall = dToBBelowQOne;
            //moveDown = false;
            //moveUp = true;
        }
        //ELSE IF ball above 3rd quarter on paddle
        else if ((player2.y + ((player2.height / 3) * 2)) < (ball.y + (ball.height / 2))) {
            distToBall = dToBAboveQThree;
            //moveUp = false;
            //moveDown = true;
        }
        //ELSE IF ball between 1st quarter and 3rd quarter on paddle
        else if ((player2.y + (player2.height / 3)) < (ball.y + (ball.height / 2)) &&
                (player2.y + ((player2.height / 3) * 2)) > (ball.y + (ball.height / 2))) {
            distToBall = (dToBAboveQOne + dToBBelowQThree) / 2;
            //moveUp = false;
            //moveDown = false;
        }
    }

    public void getPosition() {
        this.dToBBelowQOne = (player2.y + (player2.height / 3)) - (ball.y + (ball.height / 2));
        this.dToBBelowQThree = (player2.y + ((player2.height / 3) * 3)) - (ball.y + (ball.height / 2));
        this.dToBAboveQThree = (ball.y + (ball.height / 2)) - (player2.y + ((player2.height / 3) * 2));
        this.dToBAboveQOne = (ball.y + (ball.height / 2)) - (player2.y + (player2.height / 3));
    }

    public void adjustStats() {
        //player2.velY *= 0.5;
        //player2.moveAcc = 0.5;
    } //

    public void ballPath() {
        checkNotSideBorders();
        if(reboundOneX > player2.pong.width){
            if(ball.velY < 0) {
                reboundOneY = (ball.pong.width - (ball.x + ball.width));
                reboundOneX = ball.pong.width;
            }
            else{
                reboundOneY = ball.y + ((ball.pong.width - (ball.x + ball.width)));
                reboundOneX = ball.pong.width;
            }
        }
        else if(reboundOneX < 0){
            reboundOneY = (ball.x + (ball.width / 2));
            reboundOneX = 0;
        }
    }

    //MESSY FUNCTION NEEDS CLEANING
    //DRAWS THE PATH OF THE BALL
    public void checkNotSideBorders() {
        //IF ball is travelling right
        if(ball.velX > 0) {
            //IF ball is travelling up
            if (ball.velY < 0) {
                reboundOneX = ball.x + ball.y;
                reboundOneY = 0;
            }
            //ELSE IF ball is travelling down
            else {
                reboundOneX = ball.x + (ball.pong.height - ball.y);
                reboundOneY = ball.pong.height;
            }
        }
        //ELSE ball is travelling left
        else{
            //IF ball is travelling up
            if (ball.velY < 0) {
                reboundOneX = ball.x - ball.y;
                reboundOneY = 0;
            }
            //ELSE IF ball is travelling down
            else {
                reboundOneX = ball.x - (ball.pong.height - ball.y);
                reboundOneY = ball.pong.height;
            }
        }
    }

    public void renderThoughLines(Graphics g) {
        g.setColor(Color.RED);
        //Ball to paddle
        g.drawLine((int) ballMidX, (int) ballMidY, (int) pTopX, (int) pTopY);
        g.drawLine((int) ballMidX, (int) ballMidY, (int) pTopX, (int) pOneY);
        g.drawLine((int) ballMidX, (int) ballMidY, (int) pTopX, (int) pTwoY);
        g.drawLine((int) ballMidX, (int) ballMidY, (int) pTopX, (int) pBottomY);

        //Ball to bottom
        g.drawLine((int) ballMidX, (int) ballMidY, (int) ballMidX, player2.pong.height);

        g.setColor(Color.BLACK);
        //Ball path
        g.drawLine((int) ballMidX, (int) ballMidY, (int) reboundOneX, (int)reboundOneY);
    }
}
