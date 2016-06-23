package PongGame;

import javax.swing.JPanel;
import java.awt.*;

/**
 * Created by Danyon on 20/06/2016.
 */
public class Renderer extends JPanel {

    //Used For display info (FPS)
    long nextSecond = System.currentTimeMillis() + 1000;
    int framesInLastSecond = 0;
    int framesInCurrentSecond = 0;

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        Pong.pong.render(g);
        g2.setFont(new Font("Menlo", Font.PLAIN, 14));
        g2.drawString("FPS " + showFPS(), 50, 60);
    }

    //Used to show the FPS of the game
    public int showFPS() {
        long currentTime = System.currentTimeMillis();
        if (currentTime > nextSecond) {
            nextSecond += 1000;
            framesInLastSecond = framesInCurrentSecond;
            framesInCurrentSecond = 0;
        }
        framesInCurrentSecond++;
        return framesInLastSecond;
    }
}
