import sun.awt.ExtendedKeyCodes;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;


public class Snake extends Frame implements Callback {
    int x = 40,y=50;
    int windowHeight = 600, windowWidth = 800;
    int stageLeft = 25, stageTop = 50, stageHeight = 500, stageWidth = 500;
    int infoLeft = 550, infoTop = 50, infoHeight = 500, infoWidth = 200;

    Color backColor = Color.DARK_GRAY;
    Color foreColor = Color.LIGHT_GRAY;

    ISnake player = new Block();

    @Override public Color getBackColor() {
        return backColor;
    }

    @Override public Color getForeColor() {
        return foreColor;
    }

    @Override public void gameOver() {
        // TODO
    }

    Snake() {
        player.start(this);
        this.setSize(windowWidth, windowHeight);
        this.setLocationRelativeTo(null);
        this.setBackground(backColor);
        this.setVisible(true);
        addWindowListener(new WindowAdapter() {
            @Override public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
        addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
                //System.out.println("type:\t" + e.getKeyCode());
            }

            @Override
            public void keyPressed(KeyEvent e) {
                //System.out.println("press:\t" + e.getKeyCode());
                switch (e.getKeyCode()) {
                    case 37:
                        player.turnLeft();
                        break;
                    case 38:
                        player.turnTop();
                        break;
                    case 39:
                        player.turnRight();
                        break;
                    case 40:
                        player.turnBottom();
                        break;
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                //System.out.println("release:\t" + e.getKeyCode());
            }
        });
    }

    public static void main(String[] args) {
        new Snake();
    }

    Image offScreenImage = null;
    Image stageBuffer = null;
    Image infoBuffer = null;

    public void update(Graphics g) { //双缓冲
        if(offScreenImage == null) {
            offScreenImage = this.createImage(800, 600);
        }
        Graphics gOffScreen = offScreenImage.getGraphics();
        Color c = gOffScreen.getColor();
        gOffScreen.setColor(Color.LIGHT_GRAY);
        gOffScreen.fillRect(0, 0, windowWidth, windowHeight);
        gOffScreen.setColor(c);
        paint(gOffScreen);
        g.drawImage(offScreenImage, 0, 0, null);
    }

    public void paint(Graphics g){
        //System.out.println("paint");
        super.paint(g);
        g.setColor(foreColor);
        g.fillRect(0, 0, windowWidth, windowHeight);
        if (stageBuffer == null)
            stageBuffer = this.createImage(stageWidth, stageHeight);
        refreshStage(stageBuffer.getGraphics());
        g.drawImage(stageBuffer, stageLeft, stageTop, null);
        if (infoBuffer == null)
            infoBuffer = this.createImage(infoWidth, infoHeight);
        refreshInfo(infoBuffer.getGraphics());
        g.drawImage(infoBuffer, infoLeft, infoTop, null);
        repaint();
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void refreshStage(Graphics g) {
        g.setColor(backColor);
        g.fillRect(0, 0, stageWidth, stageHeight);
        g.setColor(Color.RED);
        player.paint(g, 200, 200);
        g.drawString(String.valueOf(Math.random()), 0, 0);
    }

    private void refreshInfo(Graphics g) {
        g.setColor(backColor);
        g.fillRect(0, 0, stageWidth, stageHeight);
        g.setColor(foreColor);
        //g.setFont(new Font("Arial", Font.PLAIN, 40));
        g.drawString("SCORE: " + player.getScore(), 10, 20);
        g.drawString("X: " + player.getX(), 10, 40);
        g.drawString("Y: " + player.getY(), 10, 60);
        g.drawRect(0,0,100,100);
    }
}

class Block implements ISnake {
    private Callback callback;
    private int X, Y, score;
    enum Direction { left, right, up, down }
    private Direction direction = Direction.right;

    @Override public void start(Callback callback) {
        this.callback = callback;
    }

    @Override public int getX() { return X; }

    @Override public int getY() { return Y; }

    @Override public int getScore() { return score; }

    @Override public boolean turnLeft() {
        direction = Direction.left;
        return true;
    }

    @Override public boolean turnRight() {
        direction = Direction.right;
        return true;
    }

    @Override public boolean turnTop() {
        direction = Direction.up;
        return true;
    }

    @Override public boolean turnBottom() {
        direction = Direction.down;
        return true;
    }

    @Override public void paint(Graphics g, int coinX, int coinY) {
        g.setColor(callback.getForeColor());
        g.fillRect(X * 25, Y * 25, 25, 25);
        switch (direction) {
            case up:
                if (Y > 0) Y--;
                break;
            case down:
                if (Y < 19) Y++;
                break;
            case left:
                if (X > 0) X--;
                break;
            case right:
                if (X < 19) X++;
                break;
        }
    }
}

interface ISnake {
    void start(Callback i);
    int getScore();
    int getX();
    int getY();
    void paint(Graphics g, int coinX, int coinY);
    boolean turnLeft();
    boolean turnRight();
    boolean turnTop();
    boolean turnBottom();
}

interface Callback {
    void gameOver();
    Color getBackColor();
    Color getForeColor();
}
