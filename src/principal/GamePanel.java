package principal;

import javax.swing.JPanel;

import entity.Player;
import tile.TileManeger;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

public class GamePanel extends JPanel implements Runnable{

	//screen setings 
	final int originalTilesSize = 16;// 16px X 16px
	final int scale = 3;
	
	public final int tileSize = originalTilesSize * scale;
    public final int maxScreenCols = 16;
    public final int maxScreenRows = 12;
    public final int screenWith = tileSize * maxScreenCols;//768px
    public final int screenHeight = tileSize * maxScreenRows;//576px

    //World setings
    public final int maxWorldCol = 50;
    public final int maxWorldRow = 50;
    public final int worldWith = tileSize * maxWorldCol;//800px
    public final int worldHeight = tileSize * maxWorldRow;//800px


    //FPS
    int FPS = 60;
    TileManeger tileM = new TileManeger(this); 
    KeyHandler keyH = new KeyHandler();
    Thread gameThread;  
    public CollisionChecker cChecker = new CollisionChecker(this);
    public Player player = new Player(this,keyH);

    //constructor======================
    public GamePanel() {
     this.setPreferredSize(new Dimension(screenWith, screenHeight));
     this.setBackground(new Color(0,0,0));
     this.setDoubleBuffered(true);
     this.addKeyListener(keyH);
     this.setFocusable(true);
    }

    public void startGameThread(){
        if(gameThread == null){
            gameThread = new Thread(this);
            gameThread.start();
        }
    }

    @Override
    public void run(){  
        double drawInterval = 1000000000/FPS;
        double delta = 0; 
        long lastTime = System.nanoTime();
        long currentTime;
        long timer = 0;
        int drawCount = 0;

        while(gameThread != null) {
            currentTime = System.nanoTime();
            delta += (currentTime - lastTime) / drawInterval;
            timer += (currentTime - lastTime);
            lastTime = currentTime;

            if(delta >= 1){
                update();
                repaint();
                delta--;  
                drawCount++;
            }   

            if(timer >= 1000000000){
                System.out.println("FPS: " + drawCount);
                drawCount = 0;
                timer = 0;
            }
        }
    }

    public void update(){
      
    	player.update();
    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D)g;

        tileM.draw(g2);

        player.draw(g2);

        g2.dispose();
    }
}