/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package videogame;

import java.awt.*;

/**
 * @author Ernesto García
 */
public class Ally extends Item {
    private int speed;

    public Ally(int x, int y, int width, int height, int speed) {
        super(x, y, width, height);
        this.speed = speed;
    }

    @Override
    public String toString() {
        return "[a] speed:" + speed + " " + "x:" + x + " " + "y:" + y + " " + "width:" + width + " " + "height:" + height;
    }

    @Override
    public void tick() {
        this.setX(this.getX() + speed);
    }

    @Override
    public void render(Graphics g) {
        g.drawImage(Assets.ally, getX(), getY(), getWidth(), getHeight(), null);
    }
}
