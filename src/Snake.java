import java.awt.Color;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;


public class Snake extends Frame{
    int x = 40,y=50;

    Snake(){
        this.setSize(800,700);
        this.setLocationRelativeTo(null);
        this.setBackground(Color.GREEN);
        this.setVisible(true);
        addWindowListener(new WindowAdapter() {
            @Override public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
    }
    public static void main(String[] args) {
        new Snake();
    }
    Image offScreenImage = null;
    public void update(Graphics g) { //双缓冲
        if(offScreenImage == null) {
            offScreenImage = this.createImage(800, 600);
        }
        Graphics gOffScreen = offScreenImage.getGraphics();
        Color c = gOffScreen.getColor();
        gOffScreen.setColor(Color.GREEN);
        gOffScreen.fillRect(0, 0, 800, 600);
        gOffScreen.setColor(c);
        paint(gOffScreen);
        g.drawImage(offScreenImage, 0, 0, null);
    }
    public void paint(Graphics g){
        super.paint(g);
        g.setColor(Color.RED);
        g.fillOval(x, y, 20, 20);
        y++;
        repaint();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
// TODO 自动生成的 catch 块
            e.printStackTrace();
        }
    }
}