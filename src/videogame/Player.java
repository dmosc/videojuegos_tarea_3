/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package videogame;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

/**
 * @author antoniomejorado
 */
public class Player extends Item {

    private int direction;
    private int lives;
    private int points;
    private int speed;
    private Game game;
    private Animation animation[];
    private BufferedImage player;

    public Player(int x, int y, int direction, int width, int height, int lives, int speed, Game game) {
        super(x, y, width, height);
        this.direction = direction;
        this.lives = lives;
        this.speed = speed;
        this.game = game;
        this.points = 0;

        this.animation = new Animation[5];
        for (int i = 0; i < 5; i++) {
            this.animation[i] = new Animation(Assets.player[i], 100);
        }
    }

    /**
     * Get direction value
     *
     * @return direction
     */
    public int getDirection() {
        return direction;
    }

    /**
     * Get points value
     *
     * @return points
     */
    public int getPoints() {
        return points;
    }

    /**
     * Get lives value
     *
     * @return lives
     */
    public int getLives() {
        return lives;
    }

    /**
     * Set direction value
     *
     * @param direction to modify
     */
    public void setDirection(int direction) {
        this.direction = direction;
    }

    /**
     * Set lives value
     *
     * @param lives to modify
     */
    public void setLives(int lives) {
        this.lives = lives;
    }

    /**
     * Set points value
     *
     * @param points to modify
     */
    public void setPoints(int points) {
        this.points = points;
    }

    @Override
    public void tick() {
        for (int i = 0; i < 5; i++) {
            this.animation[i].tick();
        }

        player = this.animation[0].getCurrentFrame();

        // moving player depending on flags
        if (game.getKeyManager().UP) {
            setY(getY() - speed);
            player = this.animation[1].getCurrentFrame();
        }

        if (game.getKeyManager().RIGHT) {
            setX(getX() + speed);
            player = this.animation[2].getCurrentFrame();
        }

        if (game.getKeyManager().DOWN) {
            setY(getY() + speed);
            player = this.animation[3].getCurrentFrame();
        }

        if (game.getKeyManager().LEFT) {
            setX(getX() - speed);
            player = this.animation[4].getCurrentFrame();
        }

        // reset x position and y position if collision
        if (getX() + 60 >= game.getWidth()) {
            setX(game.getWidth() - 60);
        } else if (getX() <= -30) {
            setX(-30);
        }
        if (getY() + 80 >= game.getHeight()) {
            setY(game.getHeight() - 80);
        } else if (getY() <= -20) {
            setY(-20);
        }
    }

    @Override
    public void render(Graphics g) {
        g.drawImage(player, getX(), getY(), getWidth(), getHeight(), null);
    }
}
