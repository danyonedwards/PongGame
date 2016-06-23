package PongGame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * Created by Danyon on 20/06/2016.
 */
public class Pong implements ActionListener, KeyListener {

    /*VARIABLE DECLARATION*/

    //Creates instance of Pong
    public static Pong pong;

    //Frame Width and Height
    public int width = 1200;
    public int height = 700;

    public int ballRally = 0;

    //Master reference of game objects
    public Renderer renderer;
    public Paddle player1;
    public Paddle player2;
    public Ball ball;
    public PaddleAI playerAI;
    public JFrame frame;

    /*END VARIABLE DECLARATION*/

    public Pong() {
        Timer timer = new Timer(15, this);
        frame = new JFrame("Pong");

        renderer = new Renderer();
        renderer.setSize(width, height);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        frame.add(renderer);
        frame.setSize(width, height + 20);
        frame.addKeyListener(this);
        start();

        timer.start();
    }

    public void start() {
        player1 = new Paddle(this, 1);
        player2 = new Paddle(this, 2);
        ball = new Ball(this, player1, player2);
        playerAI = new PaddleAI(ball, player2);
    }

    public void update() {
        playerAI.run();
        if (player1.isMoveUp == true || player1.isMoveDown == true) {
            player1.checkMove();
        }
        if (player1.isMoveUp == false && player1.isMoveDown == false) {
            player1.stopMove();
        }
        if (player2.isMoveUp == true || player2.isMoveDown == true) {
            player2.checkMove();
        }
        if (player2.isMoveUp == false && player2.isMoveDown == false) {
            player2.stopMove();
        }
        ball.ballPos();
    }

    //Used for increasing ball speed based on rally
    public void ballRally(int rallyInc) {
        if (rallyInc == 1) {
            ballRally++;
            if (ball.velX < 0) {
                ball.velX -= (ballRally * 0.2);
            }
            else {
                ball.velX += (ballRally * 0.2);
            }
            if (ball.velY < 0) {
                ball.velY -= (ballRally * 0.2);
            }
            else {
                ball.velY += (ballRally * 0.2);
            }
        }
        else if (rallyInc == 0) {
            ballRally = 0;
            ball.velX = 8;
            ball.velY = 8;
        }
    }

    public static void main(String[] args) {
        pong = new Pong();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        update();
        renderer.repaint();
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        //If up is pressed
        if (e.getKeyCode() == 38) {
            player1.isMoveUp = true;
        }
        //If down is pressed
        if (e.getKeyCode() == 40) {
            player1.isMoveDown = true;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        //If up is pressed
        if (e.getKeyCode() == 38) {
            player1.isMoveUp = false;
        }
        //If down is pressed
        if (e.getKeyCode() == 40) {
            player1.isMoveDown = false;
        }
    }

    public void checkChangeScreenSize() {
        width = frame.getWidth();
        height = frame.getHeight();
        if (frame.getWidth() != renderer.getWidth()) {
            renderer.setSize(width, height - 20);
        }
        if (frame.getHeight() != renderer.getHeight()) {
            renderer.setSize(width, height - 20);
        }
    }

    //Used to render all objects in the game
    public void render(Graphics g) {

        //First check if size has been altered
        checkChangeScreenSize();

        g.setColor(new Color(94, 199, 232));
        g.fillRect(0, 0, width, height);

        //Draw Game Line
        g.setColor(new Color(193, 232, 122));
        g.fillRect((width / 2) - 15, 0, 30, height);

        //Draw player paddles
        player1.render(g);
        player2.render(g);

        //Draw the Ball
        ball.render(g);

        //Draw AI Path Lines
        playerAI.renderThoughLines(g);

        //Draw Scores
        g.setColor(new Color(193, 232, 122));
        g.setFont(new Font("Menlo", Font.BOLD, 80));
        g.drawString(player1.score + "", (width / 2 - 130), 80);
        if (player2.score < 10) {
            g.drawString(player2.score + "", (width / 2 + 80), 80);

        }
        else {
            g.drawString(player2.score + "", (width / 2 + 35), 80);

        }
        g.setFont(new Font("Menlo", Font.PLAIN, 14));
        g.drawString("distToBall: " + playerAI.distToBall, pong.width - 180, 50);
    }
}
