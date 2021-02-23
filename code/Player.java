import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.*;


public class Player {

    private int x, y, width, height;
    private boolean moveLeft, moveRight;
    public int speed;
    private Image image;

    public Player(int x, int y, int speed) {
        this.x = x;
        this.y = y;
        this.width = 30;
        this.height = 20;
        this.speed = 5;
        moveRight = false;
        moveLeft = false;
        try{
            image = ImageIO.read(new File("./res/Ship.png"));
        }catch (Exception e){e.printStackTrace();}
    }

    public void draw(Graphics2D g2){
//        g2.fillRect(x, y, width, height);
        g2.drawImage(image, x, y, null);
    }

    public void move(int screenWidth){
        if(moveRight){
            if(x + width <= screenWidth)
                x += speed;
        }
        if(moveLeft) {
            if (x >= 0)
                x -= speed;
        }
        }


    public void pressed(int keyCode){
        if(keyCode == KeyEvent.VK_A)
            moveLeft = true;
        else if(keyCode == KeyEvent.VK_D)
            moveRight = true;
    }

    public void released(int keyCode){
        if(keyCode == KeyEvent.VK_A)
            moveLeft = false;
        else if(keyCode == KeyEvent.VK_D)
            moveRight = false;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Rectangle getHitBox(){
        return new Rectangle(x, y , width, height);
    }
}
