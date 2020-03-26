/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package videogame;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * @author Ernesto García
 */
public class Ally extends Item {
    private int speed;
    private Animation[] animation;
    private BufferedImage ally;

    public Ally(int x, int y, int width, int height, int speed) {
        super(x, y, width, height);
        this.speed = speed;

        this.animation = new Animation[1];
        for (int i = 0; i < 1; i++) {
            this.animation[i] = new Animation(Assets.ally[i], 100);
        }
    }

    @Override
    public String toString() {
        return "[a] speed:" + speed + " " + "x:" + x + " " + "y:" + y + " " + "width:" + width + " " + "height:" + height;
    }

    @Override
    public void tick() {
        for (int i = 0; i < 1; i++) {
            this.animation[i].tick();
        }

        ally = this.animation[0].getCurrentFrame();

        this.setX(this.getX() + speed);
    }

    @Override
    public void render(Graphics g) {
        g.drawImage(ally, getX(), getY(), getWidth(), getHeight(), null);
    }
}
