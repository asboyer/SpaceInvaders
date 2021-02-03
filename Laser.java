import java.awt.*;

public class Laser {

    private int x, y, w, h, vy;


    public Laser(int x, int y, int vy) {
        this.x = x;
        this.y = y;
        w = 4; //TODO adjust size?
        h = 6;
        this.vy = vy;
    }

    public void draw(Graphics g2){
        g2.fillRect(x, y, w, h);
    }

    public void move(){
        y += vy;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getW() {
        return w;
    }

    public int getH() {
        return h;
    }

    public int getVy() {
        return vy;
    }

    public Rectangle getHitBox(){
        return new Rectangle(x, y , w, h);
    }
}
