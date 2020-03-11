/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package videogame;

import java.awt.image.BufferedImage;

/**
 *
 * @author antoniomejorado
 */
public class Assets {
    public static BufferedImage background; // to store background image
    public static BufferedImage player;     // to store the player image
    public static BufferedImage enemy;     // to store the enemy image
    public static BufferedImage ally;     // to store the ally image
    public static BufferedImage gameOver; // to store the game over image

    /**
     * initializing the images of the game
     */
    public static void init() {
        background = ImageLoader.loadImage("/images/background.png");
        player = ImageLoader.loadImage("/images/mario.png");
        enemy = ImageLoader.loadImage("/images/enemy.png");
        ally = ImageLoader.loadImage("/images/ally.png");
        gameOver = ImageLoader.loadImage("/images/game-over.png");
    }
    
}
