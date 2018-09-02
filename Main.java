/// Space Invaders 

import java.awt.*;
import java.awt.event.*;
import javax.swing.*; 
import java.util.Vector; 
import java.util.concurrent.ThreadLocalRandom; 

class Entity {
    public Boolean collision; 
    public Boolean shoot; 
    public int speed; 

    public int x;
    public int y; 

    public Bullet bullet; 

    public Entity() {
        this.x = 32;
        this.y = 32;
        this.collision = false; 
    }
    public void createBullet() { 
        bullet = new Bullet(); 
        bullet.x = this.x; 
        bullet.y = this.y; 
    }    
} 

class Bullet extends Entity { 
    public Bullet() { 
        this.speed = 4; 
    }
}

class Invader extends Entity { 
    public Boolean canMove, canMoveRight; 
    public int stepsTaken; 

    public void Invader() { 
        this.speed = 2; 
        this.canMove = true; 
        this.canMoveRight = true; 
        this.stepsTaken = 0;
    } 
}

class Player extends Entity { 
    public Player() { 
        this.speed = 4; 
        this.x = 150; 
        this.y = 330; 
        this.shoot = false; 
    } 

    public void moveRight() { 
        this.x += this.speed; 
    }
    public void moveLeft() { 
        this.x -= this.speed; 
    } 
} 

public class Main extends JFrame {
    private Image dbImage;
    private Graphics dbg;

    public Player player; 
    public Invader invader; 

    public Vector<Invader> invaders = new Vector<Invader>(32);  

    public class AL extends KeyAdapter {
        public void keyPressed(KeyEvent e) {
            int keyCode = e.getKeyCode();
            if (keyCode == e.VK_LEFT) {
                player.moveLeft(); 
            }
            if (keyCode == e.VK_RIGHT) {
                player.moveRight(); 
            } 
            
            if (keyCode == e.VK_SPACE) { 
                player.createBullet(); 
            }
        }
        public void keyReleased(KeyEvent e) {
           
        }
    }
    public Main() {
        this.addKeyListener(new AL());
        this.setTitle("Game");
        this.setSize(300, 400);
        this.setResizable(false);
        this.setVisible(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        player = new Player(); 
        for (int i = 0; i < 4; i++) { 
            invaders.add(new Invader()); 
            invaders.get(i).x = i * 64; 
            invaders.get(i).y = 68; 
        } 
    } 
   
    public void paint(Graphics g) {
        this.dbImage = createImage(getWidth(), getHeight());
        this.dbg = this.dbImage.getGraphics();
        this.paintComponent(this.dbg);
        g.drawImage(this.dbImage, 0, 0, this);
        if (invaders.size() >= 0) { 
            for (int i = 0; i < invaders.size(); i++) { 
                if (invaders.get(i).canMove) { 
                    if (player.bullet != null) { 
                        if (player.bullet.x >= invaders.get(i).x - 16 && 
                            player.bullet.x <= invaders.get(i).x + 16 && 
                            player.bullet.y <= invaders.get(i).y + 16 && 
                            player.bullet.y >= invaders.get(i).y - 16
                        ) { 
                            player.bullet = null; 
                            invaders.remove(i); 
                            System.out.println("Collision"); 
                        } 
                    } 

                    if (invaders.get(i).stepsTaken >= 200) { 
                        invaders.get(i).canMoveRight = false; 
                        invaders.get(i).y += 4; 
                    } else if (invaders.get(i).stepsTaken <= 0) { 
                        invaders.get(i).canMoveRight = true; 
                        invaders.get(i).y += 4; 
                    } 

                    if (invaders.get(i).canMoveRight) { 
                        invaders.get(i).x += 1; 
                        invaders.get(i).stepsTaken += 1; 
                    } else if (!invaders.get(i).canMoveRight) { 
                        invaders.get(i).x -= 1; 
                        invaders.get(i).stepsTaken -= 1; 
                    }
                    invaders.get(i).canMove = false; 
                } 
            } 
        } 
    }
 
    public void paintComponent(Graphics g) {
        if (invaders.size() >= -1) { 
            for (int i = 0; i < invaders.size(); i++) { 
                g.fillRect(invaders.get(i).x, invaders.get(i).y, 32, 32); 
                int j = 0; 
                while (j < 12000) { 
                    j++; 
                    invaders.get(i).canMove = true; 
                } 
            }

            int randomNum = ThreadLocalRandom.current().nextInt(-10, invaders.size());
            if (randomNum >= invaders.size() / 2) { 
                    invaders.get(randomNum).createBullet(); 
            } 
            for (int i = 0; i < invaders.size(); i++) { 
                if (invaders.get(i).bullet != null) { 
                    invaders.get(i).bullet.y += invaders.get(i).bullet.speed; 
                    g.fillRect(invaders.get(i).bullet.x, invaders.get(i).bullet.y, 16, 16); 

                    if (invaders.get(i).bullet.x >= player.x - 16 && 
                        invaders.get(i).x <= player.x + 16 && 
                        invaders.get(i).bullet.y <= player.y + 16 && 
                        invaders.get(i).bullet.y >= player.y - 16
                    ) { 
                        invaders.get(i).bullet = null; 
                        player = null;  
                        System.out.println("GAME OVER"); 
                    } 
                } 
            } 
        } 

        if (player.bullet != null) { 
            player.bullet.y -= player.bullet.speed; 
            g.fillRect(player.bullet.x, player.bullet.y, 16, 16);
        }
        g.fillRect(player.x, player.y, 32, 32);
        this.repaint();
    }
   
    public static void main(String[] args) { 
        new Main();
    }
} 

