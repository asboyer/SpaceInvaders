import java.awt.*;
import java.util.*;

public class Alien {

    private int x, y, size;
    private Image[] images;
    private int imageCounter;

    public Alien(int x, int y, int size, Image[] images) {
        this.x = x;
        this.y = y;
        this.size = size;
        this.images = images;
    }

    public void draw(Graphics2D g2){
//        g2.fillRect(x, y, size, size);
        g2.drawImage(images[(imageCounter/30)%2], x, y, null);
        imageCounter++;
    }

    public void move(int vx){
        x += vx;
    }

    public void shiftDown(){
        y += size;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getSize() {
        return size;
    }

    public Rectangle getHitBox(){
        return new Rectangle(x, y , size, size);
    }
}
