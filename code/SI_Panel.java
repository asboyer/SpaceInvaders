import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.Scanner;

public class SI_Panel extends JPanel {

    private Timer timer;
    private ArrayList<Alien> aliens;
    private int alienVx;
    private Player player;
    private ArrayList<Laser> playerLasers, alienLasers;
    private boolean powerUp;
    private boolean og;
    private boolean wasOg;
    private double laserProbability;
	private StringBuilder sb;
    private int playerLaserSpeed;
    private final int baseSpeed;
    private final int playerSpeed;
    private boolean lose;
    private boolean win;
    private int x;
    private int level;
    private int y;
    private int z;
    private int f;
    private int highScore;
    private boolean hard;
    private boolean cheat;
    private String name;
    private String highScoreName;
    private int times;
    private int laserDelay, laserCounter; //delay -> frames between shots, counter counts frames
    private int score, lives;

    public SI_Panel(int width, int height) {

        setBounds(0,0,width, height);
        playerSpeed = 5;
        baseSpeed = -12;
        restart();
        setupKeyListener();
        timer = new Timer(1000/100, e->update());
        timer.start();
    }

    public void restart(){
        System.out.println("\n--- NEW GAME ---\n");
        // Scanner input = new Scanner(System.in);
        // System.out.println("Player name: ");
        // String name = input.next();
        aliens = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 10; j++) {
                aliens.add(new Alien(j*40, i*40, 20));
            } 
        }
//        for(Alien alien: aliens){
//            System.out.println(alien.getY());
//        }
        alienVx = 2;
        player = new Player(getWidth()/2 - 15, getHeight() - 80, playerSpeed); //??
        x = 1;
        y = 1;
        z = 1;
        f = 1;
        level = 1;
        hard = false;
        sb = new StringBuilder();
        win = false;
        lose = false;
        cheat = false;
        times = 1;
        playerLasers = new ArrayList<>();
        alienLasers = new ArrayList<>();
        laserDelay = 20; //TODO: balance this?
        laserCounter = laserDelay;
        laserProbability = 0.03;
        powerUp = false;
        og = false;
        wasOg = false;
        playerLaserSpeed = baseSpeed;
        lives = 3;
        score = 0;
    }
    public void nextLevel(){
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 10; j++) {
                aliens.add(new Alien(j*40, i*40, 20));
            }
        }
        level ++;
        alienVx = 2;
        alienLasers = new ArrayList<>();
        boolean hitEdge = false;
    }
    public void update(){
        if (aliens.size() == 0){
            nextLevel();
        }
        if (score > highScore && !cheat)
            highScore = score;
            // highScoreName = name;
        if(lives > 0){
            laserCounter ++;
            updateAliens();
            player.move(getWidth());
            updatePlayerLaser();
            updateAlienLaser();
            repaint();
            }
        else{
            while(z == 1){
                z ++;
                System.out.println("GAME OVER!");
                System.out.println("Score: " + score);
                lose = true;
                }
            }
    }

    public void updateAliens(){
        boolean hitEdge = false;
        for(Alien alien: aliens){
            alien.move(alienVx);

            if(alien.getX() + alien.getSize() >= getWidth())
                hitEdge = true;
            else if(alien.getX() <= 0)
                hitEdge = true;
        }
        if(hitEdge){
            alienVx *= -1;
            for(Alien alien: aliens){
                alien.shiftDown();
            }

        }
        //aliens shooting laser beams...
        if(Math.random() < laserProbability){
            //pick a random alien, have that alien fire
            //make a laser at that aliens location
//            Alien shooter = aliens.get(10 + (int)((Math.random() * (30-10) + x)));
//            x ++;
//
//            aliens.remove(shooter);
            Alien shooter = aliens.get((int)(Math.random() * aliens.size()));
            Laser laser = new Laser(shooter.getX(), shooter.getY(), (int)(baseSpeed*-1 * 0.5));
            alienLasers.add(laser);
        }
    }

    public void updatePlayerLaser(){
        for(int i = 0; i < playerLasers.size(); i++){
            Laser laser = playerLasers.get(i);
            laser.move();
            if(laser.getY() < -10){
                playerLasers.remove(i);
                i--;
            }
        }

        //check for laser/alien collision
        for (int i = 0; i < playerLasers.size(); i++) {
            Laser laser = playerLasers.get(i);
            for (int j = 0; j < aliens.size(); j++){
                Alien alien = aliens.get(j);
                if(laser.getHitBox().intersects(alien.getHitBox())){
                    playerLasers.remove(i);
                    aliens.remove(j);
                    if (hard)
                        score += 200;
                    else if (og)
                        score += 100;
                    else
                        score += 50;
                    j = aliens.size();
                    i--;
                }
            }

        }
    }
    public void updateAlienLaser() {
        for (int i = 0; i < alienLasers.size(); i++) {
            Laser laser = alienLasers.get(i);
            laser.move();
            if (laser.getY() > getHeight() + 10) {
                alienLasers.remove(i);
                i--;
            }
        }

        for (int i = 0; i < alienLasers.size(); i++) {
            Laser laser = alienLasers.get(i);
            for (int j = 0; j < aliens.size(); j++){
                Alien alien = aliens.get(j);
                if(laser.getHitBox().intersects(player.getHitBox())){
                    alienLasers.remove(i);
                    lives --;
                    System.out.println("Lost life!\nLives: " + lives);
                    j = aliens.size();
                    i--;
                }
            }

        }
    }

    public void setupKeyListener(){
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                sb.append(e.getKeyChar());
//                System.out.println(sb.toString());
                if(sb.toString().contains("baller") && x == 1){ //if enter baller, turn on powerup, enter to clear command
                    System.out.println("Power up activated!");
                    x ++;
                    cheat = true;
                    player.speed = playerSpeed * 2;
                    powerUp = true;
                    if (og){
                        wasOg = true;
                    }
                    else{
                        og = true;
                    }
//                    playerLaserSpeed = baseSpeed / 2;
                }
                else if(sb.toString().contains("life") && times == 1){
                    lives ++;
                    times ++;
                    System.out.println("Gained a life!");
                    System.out.println("Lives: " +  lives);
                }
                else if(sb.toString().contains("hard") && f == 1){
                    System.out.println("Hard mode: double the points!");
                    laserProbability = 0.2;
                    hard = true;
                    f ++;
                }
                else if(sb.toString().contains("norm") && f == 2){
                    System.out.println("Ahhh, back to normal!");
                    laserProbability = 0.03;
                    hard = false;
                    f --;
                }
                else if(sb.toString().contains("game")){
                    if (score >= highScore){
                        System.out.println("NEW HIGH SCORE!");
                        System.out.println("Final score: " + score);
                    }
                    else
                        System.out.println("Final score: " + score);
                    System.out.println("Wave reached: " + level);
                    // System.out.println("Player name " + name);
                    restart();
                }
                else if(sb.toString().contains("stop") && x == 2){ //if stop, turn off power up
                    System.out.println("Power up stopped!");
                    x --;
                    player.speed = playerSpeed;
                    powerUp = false;
                    if(!wasOg)
                        og = false;
                    playerLaserSpeed = baseSpeed;
//                    System.out.println("done");
                }
                else if(sb.toString().contains("new") && y == 2){
                    System.out.println("New mode activated");
                    og = false;
                    y --;
                }
                else if(sb.toString().contains("og") && y == 1){
                    System.out.println("Reverted back to OG");
                    og = true;
                    y ++;
                }
            }

            @Override
            public void keyPressed(KeyEvent e) {
                player.pressed(e.getKeyCode()); //notify player that key is down
                if(e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_SPACE){
                    Laser laser = new Laser(player.getX() + player.getWidth()/2, player.getY(), playerLaserSpeed); //TODO: new speed?
                     //TODO: test this, add power up
                    if(powerUp) {
                        playerLasers.add(laser);
                    }
                    else{
                        if(og) {
                            if(playerLasers.size() < 1)
                                playerLasers.add(laser);
                        }
                        else{
                            if(laserCounter >= laserDelay){
                                playerLasers.add(laser);
                                laserCounter = 0;
                            }
                        }
                    }
                }
                if(e.getKeyCode() == KeyEvent.VK_ENTER) {
                    sb = new StringBuilder();
                    times = 1;
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                player.released(e.getKeyCode()); //notify player that key is down
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(Color.black);
        if(hard)
            g2.setColor(Color.ORANGE);
        for(Alien alien: aliens){
            alien.draw(g2);
        }
        g2.setColor(new Color(61,131,66));
        for(Laser laser: playerLasers){
            laser.draw(g2);
        }
        g2.setColor(Color.RED);
        for(Laser laser: alienLasers)
            laser.draw(g2);
        if(lives == 3){
            g2.setColor(new Color(44,168,34));
        }
        else if(lives > 3)
            g2.setColor(Color.darkGray);
        else if(lives == 2){
            g2.setColor(new Color(255,153,154));
        }
        else if(lives == 1){
            g2.setColor(new Color( 255,93,93));
        }
        if (powerUp)
            g2.setColor(Color.BLUE);
        if(lives == 0)
            g2.setColor(Color.RED);
        player.draw(g2);
        g2.setColor(Color.black);
        // g2.drawString("High score: " + highScore + " (" + highScoreName + " )", 7, 10);
        g2.drawString("High score: " + highScore, 7, 10);
        g2.drawString("Score: " + score, 7, 25);
        g2.drawString("Lives: " + lives, 7, 40);
        g2.drawString("Wave: " + level, 7, 55);
        if(win){
            g2.drawString("YOU WIN", 400, 400);
        }
        if(lose){
            g2.drawString("GAME OVER", 400, 400);
        }

    }

}
