/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package videogame;

import javafx.util.Duration;

import java.awt.*;
import java.awt.image.BufferStrategy;
import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;

import static java.lang.Integer.parseInt;

/**
 * @author Ernesto García
 */
public class Game implements Runnable {
    public static final String saveFileName = "latestState.txt";
    String title;                           // title of the window
    private BufferStrategy bs;              // to have several buffers when displaying
    private Graphics g;                     // to paint objects
    private Display display;                // to display in the game
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
     * Set enemiesCollide value
     *
     * @param enemiesCollide to modify
     */
    public void setEnemiesCollide(int enemiesCollide) {
        this.enemiesCollide = enemiesCollide;
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
        if (keyManager.P) {
            return;
        }
        if (keyManager.G) {
            saveState();
        }
        if (keyManager.C) {
            loadState();
        }

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
            g.drawString("Vidas: " + player.getLives(), 100, getHeight() - 20);
            g.drawString("Puntos: " + player.getPoints(), getWidth() - 400, getHeight() - 20);
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

    public void saveState() {
        try {
            PrintWriter writer = new PrintWriter(new FileWriter(saveFileName));
            writer.println("[g] maxEnemies:" + maxEnemies);
            writer.println("[g] maxAllies:" + maxAllies);
            writer.println("[g] enemiesCollide:" + enemiesCollide);
            writer.println(player.toString());

            Iterator<Enemy> enemyIter = enemyList.iterator();
            Iterator<Ally> allyIter = allyList.iterator();

            while (enemyIter.hasNext()) {
                Enemy enemy = enemyIter.next();
                writer.println(enemy.toString());
            }

            while (allyIter.hasNext()) {
                Ally ally = allyIter.next();
                writer.println(ally.toString());
            }

            writer.close();
        } catch (IOException ioe) {
            System.out.println("File Not found CALL 911");
        }
    }

    public void loadState() {
        try {
            FileReader file = new FileReader(saveFileName);
            BufferedReader reader = new BufferedReader(file);
            String line;
            String attribute, typeofObject;
            int i, x, y, width, height, direction, lives, points, speed;
            String[] attributes;
            ArrayList<String> enemyListData = new ArrayList<String>(), allyListData = new ArrayList<String>();

            while ((line = reader.readLine()) != null) {
                attributes = line.split(" ");
                typeofObject = attributes[0];

                switch (typeofObject) {
                    case "[g]": // Reload global variables
                        attribute = attributes[1].substring(0, attributes[1].indexOf(":"));
                        switch (attribute) {
                            case "maxEnemies":
                                maxEnemies = parseInt(attributes[1].substring(attributes[1].indexOf(":") + 1));
                                break;
                            case "maxAllies":
                                maxAllies = parseInt(attributes[1].substring(attributes[1].indexOf(":") + 1));
                                break;
                            case "enemiesCollide":
                                enemiesCollide = parseInt(attributes[1].substring(attributes[1].indexOf(":") + 1));
                                break;
                        }
                        break;
                    case "[p]": // Reload player state
                        direction = parseInt(attributes[1].substring(attributes[1].indexOf(":") + 1));
                        lives = parseInt(attributes[2].substring(attributes[2].indexOf(":") + 1));
                        points = parseInt(attributes[3].substring(attributes[3].indexOf(":") + 1));
                        speed = parseInt(attributes[4].substring(attributes[4].indexOf(":") + 1));
                        x = parseInt(attributes[5].substring(attributes[5].indexOf(":") + 1));
                        y = parseInt(attributes[6].substring(attributes[6].indexOf(":") + 1));
                        width = parseInt(attributes[7].substring(attributes[7].indexOf(":") + 1));
                        height = parseInt(attributes[8].substring(attributes[8].indexOf(":") + 1));

                        player = new Player(x, y, direction, width, height, lives, speed, this);
                        player.setPoints(points);
                        break;
                    case "[e]":
                        enemyListData.add(line);
                        break;
                    case "[a]":
                        allyListData.add(line);
                        break;
                }
            }

            Iterator<Enemy> enemyIter = enemyList.iterator();
            Iterator<Ally> allyIter = allyList.iterator();

            i = 0;
            while (enemyIter.hasNext() && i < enemyListData.size()) { // Reload enemies state
                attributes = enemyListData.get(i).split(" ");
                speed = parseInt(attributes[1].substring(attributes[1].indexOf(":") + 1));
                x = parseInt(attributes[2].substring(attributes[2].indexOf(":") + 1));
                y = parseInt(attributes[3].substring(attributes[3].indexOf(":") + 1));
                width = parseInt(attributes[4].substring(attributes[4].indexOf(":") + 1));
                height = parseInt(attributes[5].substring(attributes[5].indexOf(":") + 1));

                enemyList.set(i, new Enemy(x, y, width, height, speed));
                i++;
                enemyIter.next();
            }

            i = 0;
            while (allyIter.hasNext() && i < allyListData.size()) { // Reload allies state
                attributes = allyListData.get(i).split(" ");
                speed = parseInt(attributes[1].substring(attributes[1].indexOf(":") + 1));
                x = parseInt(attributes[2].substring(attributes[2].indexOf(":") + 1));
                y = parseInt(attributes[3].substring(attributes[3].indexOf(":") + 1));
                width = parseInt(attributes[4].substring(attributes[4].indexOf(":") + 1));
                height = parseInt(attributes[5].substring(attributes[5].indexOf(":") + 1));

                allyList.set(i, new Ally(x, y, width, height, speed));
                i++;
                allyIter.next();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
