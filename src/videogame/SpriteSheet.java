package videogame;

import java.awt.image.BufferedImage;

/**
 * @author Ernesto García
 */
public class SpriteSheet {
    private BufferedImage sheet;

    /**
     * Create a new spritesheet
     *
     * @param sheet the <code>image</code> with the sprites
     */
    public SpriteSheet(BufferedImage sheet) {
        this.sheet = sheet;
    }

    /**
     * Crop image from the spritesheet
     *
     * @param x      an <code>int</code> value with the x coordinate
     * @param y      an <code>int</code> value with the y coordinate
     * @param width  an <code>int</code> value with the sprite width
     * @param height an <code>int</code> value with the sprite height
     * @return
     */
    public BufferedImage crop(int x, int y, int width, int height) {
        return sheet.getSubimage(x, y, width, height);
    }
}
