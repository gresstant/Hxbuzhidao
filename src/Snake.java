import java.awt.*;
import java.awt.event.*;
import java.util.*;


public class Snake extends Frame implements Callback {
    int x = 40,y=50;
    int windowHeight = 600, windowWidth = 800;
    int stageLeft = 25, stageTop = 50, stageHeight = 500, stageWidth = 500;
    int infoLeft = 550, infoTop = 50, infoHeight = 500, infoWidth = 200;
    int coinX = 12, coinY = 12;

    boolean inGame = true;

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
        inGame = false;
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
            @Override public void keyTyped(KeyEvent e) {
                //System.out.println("type:\t" + e.getKeyCode());
            }

            @Override public void keyPressed(KeyEvent e) {
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

            @Override public void keyReleased(KeyEvent e) {
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
        if (inGame) {
            player.paint(g, coinX, coinY);
            if (player.checkCollision(coinX, coinY))
                newCoin();
            g.drawRect(coinX * 25, coinY * 25, 25, 25);
        } else {
            Font tmp = g.getFont();
            g.setFont(new Font("Arial", Font.PLAIN, 40));
            g.drawString("GAME OVER!", 120, 250);
            g.setFont(tmp);
        }
    }

    private void refreshInfo(Graphics g) {
        g.setColor(backColor);
        g.fillRect(0, 0, stageWidth, stageHeight);
        g.setColor(foreColor);
        //g.setFont(new Font("Arial", Font.PLAIN, 40));
        g.drawString("SCORE: " + player.getScore(), 10, 20);
        g.drawString("X: " + player.getX(), 10, 40);
        g.drawString("Y: " + player.getY(), 10, 60);
        if (!inGame) {
            g.setColor(Color.RED);
            g.drawString("GAME OVER!", 10, 90);
        }
    }

    private void newCoin() {
        while (player.checkCollision(coinX, coinY)) {
            coinX = (int)(Math.random() * 20);
            coinY = (int)(Math.random() * 20);
        }
    }
}

class Block implements ISnake {
    private Callback callback;
    private int /*X, Y,*/ score;
    enum Direction { left, right, up, down }
    private Direction direction = Direction.right;
    //private BodyNode head = new BodyNode(5, 0, new BodyNode(4, 0, new BodyNode(3, 0, null)));
    private LinkedList<BodyNode> body = new LinkedList<BodyNode>() {{
        addLast(new BodyNode(4, 0));
        addLast(new BodyNode(3, 0));
        addLast(new BodyNode(2, 0));
        addLast(new BodyNode(1, 0));
        addLast(new BodyNode(0, 0));
    }};

    private class BodyNode {
        int x;
        int y;

        BodyNode(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    @Override public void start(Callback callback) {
        this.callback = callback;
    }

    @Override public int getX() { return body.peekFirst().x; }

    @Override public int getY() { return body.peekFirst().y; }

    @Override public int getScore() { return score; }

    @Override public boolean turnLeft() {
        if (direction != Direction.right)
            direction = Direction.left;
        return true;
    }

    @Override public boolean turnRight() {
        if (direction != Direction.left)
            direction = Direction.right;
        return true;
    }

    @Override public boolean turnTop() {
        if (direction != Direction.down)
            direction = Direction.up;
        return true;
    }

    @Override public boolean turnBottom() {
        if (direction != Direction.up)
            direction = Direction.down;
        return true;
    }

    @Override public void paint(Graphics g, int coinX, int coinY) {
        switch (direction) {
            case up:
                if (body.peekFirst().y <= 0) { callback.gameOver(); return; }
                body.addFirst(new BodyNode(body.peekFirst().x, body.peekFirst().y - 1));
                break;
            case down:
                if (body.peekFirst().y >= 19) { callback.gameOver(); return; }
                body.addFirst(new BodyNode(body.peekFirst().x, body.peekFirst().y + 1));
                //dropLast();
                break;
            case left:
                if (body.peekFirst().x <= 0) { callback.gameOver(); return; }
                body.addFirst(new BodyNode(body.peekFirst().x - 1, body.peekFirst().y));
                //dropLast();
                break;
            case right:
                if (body.peekFirst().x >= 19) { callback.gameOver(); return; }
                body.addFirst(new BodyNode(body.peekFirst().x + 1, body.peekFirst().y));
                //dropLast();
                break;
        }
        if (coinX != body.peekFirst().x || coinY != body.peekFirst().y)
            dropLast();
        checkCollision();
        g.setColor(callback.getForeColor());
        for (BodyNode node : body) {
            g.fillRect(node.x * 25, node.y * 25, 25, 25);
        }
    }

    private void dropLast() {
        body.removeLast();
        score = body.size();
    }

    private void checkCollision() {
        BodyNode head = null;
        for (BodyNode node : body) {
            if (head == null) {
                head = node;
                continue;
            }
            if (node.x == head.x && node.y == head.y) {
                callback.gameOver();
                return;
            }
        }
    }

    @Override public boolean checkCollision(int x, int y) {
        for (BodyNode node : body) {
            if (node.x == x && node.y == y) {
                return true;
            }
        }
        return false;
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
    boolean checkCollision(int x, int y);
}

interface Callback {
    void gameOver();
    Color getBackColor();
    Color getForeColor();
}
