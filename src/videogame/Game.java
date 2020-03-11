/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package videogame;

import javafx.util.Duration;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * @author Ernesto García
 */
public class Game implements Runnable {
    private BufferStrategy bs;              // to have several buffers when displaying
    private Graphics g;                     // to paint objects
    private Display display;                // to display in the game
    String title;                           // title of the window
    private int width;                      // width of the window
    private int height;                     // height of the window
    private Thread thread;                  // thread to create the game
    private boolean running;                // to set the game
    private Player player;                  // to use a player
    private KeyManager keyManager;          // to manage the keyboard
    private LinkedList<Enemy> enemyList;    // enemies
    private LinkedList<Ally> allyList;     // allies
    private int maxEnemies;                 // max enemy quantity
    private int maxAllies;                  // max enemy quantity
    private int lastEnemySpawn;             // time since last enemy spawn
    private int lastAllySpawn;              // time since last ally spawn
    private int enemiesCollide;             // enemy collision qty

    /**
     * to create title, width and height and set the game is still not running
     *
     * @param title  to set the title of the window
     * @param width  to set the width of the window
     * @param height to set the height of the window
     */
    public Game(String title, int width, int height) {
        this.title = title;
        this.width = width;
        this.height = height;
        running = false;
        keyManager = new KeyManager();
    }

    /**
     * To get the width of the game window
     *
     * @return an <code>int</code> value with the width
     */
    public int getWidth() {
        return width;
    }

    /**
     * To get the height of the game window
     *
     * @return an <code>int</code> value with the height
     */
    public int getHeight() {
        return height;
    }

    /**
     * To get quantity of collided enemies
     *
     * @return an <code>int</code> value with the enemy collide quantity
     */
    public int getEnemiesCollide() {
        return enemiesCollide;
    }

    /**
     * To get the player of the game
     *
     * @return an <code>Player</code> value with the player
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Set lastEnemySpawn value
     *
     * @param lastEnemySpawn to modify
     */
    public void setLastEnemySpawn(int lastEnemySpawn) {
        this.lastEnemySpawn = lastEnemySpawn;
    }

    /**
     * Set lastAllySpawn value
     *
     * @param lastAllySpawn to modify
     */
    public void setLastAllySpawn(int lastAllySpawn) {
        this.lastAllySpawn = lastAllySpawn;
    }

    /**
     * Set enemiesCollide value
     *
     * @param enemiesCollide to modify
     */
    public void setEnemiesCollide(int enemiesCollide) {
        this.enemiesCollide = enemiesCollide;
    }

    /**
     * initializing the display window of the game
     */
    private void init() {
        display = new Display(title, getWidth(), getHeight());
        Assets.init();
        Sounds.initialize();
        enemyList = new LinkedList<>();
        allyList = new LinkedList<>();
        enemiesCollide = 0;
        maxEnemies = (int) (Math.random() * 3) + 8;
        maxAllies = (int) (Math.random() * 6) + 10;
        player = new Player(getWidth() / 2 - 50, getHeight() / 2 - 50, 1, 100, 100, (int) (Math.random() * 3) + 3, 5, this);
        display.getJframe().addKeyListener(keyManager);
    }

    @Override
    public void run() {
        init();
        // frames per second
        int fps = 50;
        // time for each tick in nano seconds
        double timeTick = 1000000000 / fps;
        // initializing delta
        double delta = 0;
        // define now to use inside the loop
        long now;
        // initializing last time to the computer time in nanoseconds
        long lastTime = System.nanoTime();
        while (running) {
            // setting the time now to the actual time
            now = System.nanoTime();
            // Accumulating to delta the difference between times in timeTick units
            delta += (now - lastTime) / timeTick;
            // updating the last time
            lastTime = now;

            // if delta is positive we tick the game
            if (delta >= 1) {
                tick();
                render();
                delta--;
            }
        }
    }

    public KeyManager getKeyManager() {
        return keyManager;
    }

    private void tick() {
        keyManager.tick();
        player.tick();

        if (lastEnemySpawn > 0) {
            setLastEnemySpawn(lastEnemySpawn - 1);
        }

        if (lastAllySpawn > 0) {
            setLastAllySpawn(lastAllySpawn - 1);
        }

        if (enemyList.size() < maxEnemies && Math.random() > 0.95 && lastEnemySpawn == 0) {
            enemyList.add(new Enemy(getWidth(), (int) (Math.random() * getHeight()), 50, 50, (int) (Math.random() * 3) + 3));
            setLastEnemySpawn(30);
        }

        if (allyList.size() < maxAllies && Math.random() > 0.95 && lastAllySpawn == 0) {
            allyList.add(new Ally(0, (int) (Math.random() * getHeight()), 50, 50, (int) (Math.random() * 3) + 1));
            setLastAllySpawn(30);
        }

        Iterator<Enemy> enemyIter = enemyList.iterator();
        Iterator<Ally> allyIter = allyList.iterator();

        while (enemyIter.hasNext()) {
            Enemy enemy = enemyIter.next();
            enemy.tick();
            if (player.collision(enemy)) {
                Sounds.hitPlayer.seek(Duration.ZERO);
                Sounds.hitPlayer.play();
                enemyIter.remove();
                setEnemiesCollide(getEnemiesCollide() + 1);
                if (getEnemiesCollide() == 1) {
                    player.setLives(player.getLives() - 1);
                    if (player.getLives() == 0) {
                        Sounds.gameOverPlayer.play();
                        stop();
                    }
                    setEnemiesCollide(0);
                }
            } else if (enemy.getX() <= 0) {
                enemyIter.remove();
            }
        }

        while (allyIter.hasNext()) {
            Ally ally = allyIter.next();
            ally.tick();
            if (player.collision(ally)) {
                Sounds.pointPlayer.seek(Duration.ZERO);
                Sounds.pointPlayer.play();
                allyIter.remove();
                player.setPoints(player.getPoints() + 5);
            } else if (ally.getX() > getWidth()) {
                allyIter.remove();
            }
        }
    }

    private void render() {
        // get the buffer strategy from the display
        bs = display.getCanvas().getBufferStrategy();
        /* if it is null, we define one with 3 buffers to display images of
        the game, if not null, then we display every image of the game but
        after clearing the Rectangle, getting the graphic object from the
        buffer strategy element. 
        show the graphic and dispose it to the trash system
        */
        if (bs == null) {
            display.getCanvas().createBufferStrategy(3);
        } else {
            g = bs.getDrawGraphics();
            g.drawImage(Assets.background, 0, 0, width, height, null);
            g.setColor(Color.WHITE);
            Font font = new Font("Agency FB", Font.BOLD, 22);
            g.setFont(font);
            g.drawString("Vidas: " + player.getLives(), 10, getHeight() - 20);
            g.drawString("Puntos: " + player.getPoints(), getWidth() - 100, getHeight() - 20);
            player.render(g);

            for (Enemy enemy : enemyList) {
                enemy.render(g);
            }

            for (Ally ally : allyList) {
                ally.render(g);
            }


            if (!running) {
                g.drawImage(Assets.gameOver, getWidth() / 2 - 235, getHeight() / 2 - 64, 470, 128, null);
            }

            bs.show();
            g.dispose();
        }
    }

    /**
     * setting the thead for the game
     */
    public synchronized void start() {
        if (!running) {
            running = true;
            thread = new Thread(this);
            thread.start();
        }
    }

    /**
     * stopping the thread
     */
    public synchronized void stop() {
        if (running) {
            running = false;
            render();
        }
    }


}
