/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package videogame;

import java.awt.image.BufferedImage;

/**
 * @author antoniomejorado
 */
public class Assets {
    public static BufferedImage background;             // to store background image
    public static BufferedImage playerSprites[];        // to store the player image
    public static BufferedImage player[][];             // to store the player image
    public static BufferedImage enemy;                  // to store the enemy image
    public static BufferedImage ally;                   // to store the ally image
    public static BufferedImage gameOver;               // to store the game over image

    /**
     * initializing the images of the game
     */
    public static void init() {
        background = ImageLoader.loadImage("/images/background.png");
        enemy = ImageLoader.loadImage("/images/enemy.png");
        ally = ImageLoader.loadImage("/images/ally.png");
        gameOver = ImageLoader.loadImage("/images/game-over.png");

        playerSprites = new BufferedImage[5];
        playerSprites[0] = ImageLoader.loadImage("/images/sprites/player/stay.png");
        playerSprites[1] = ImageLoader.loadImage("/images/sprites/player/up.png");
        playerSprites[2] = ImageLoader.loadImage("/images/sprites/player/right.png");
        playerSprites[3] = ImageLoader.loadImage("/images/sprites/player/down.png");
        playerSprites[4] = ImageLoader.loadImage("/images/sprites/player/left.png");

        player = new BufferedImage[5][9];

        for (int i = 0; i < 5; i++) {
            player[i] = new BufferedImage[9];
        }

        SpriteSheet playerSpriteSheets[] = new SpriteSheet[5];
        for (int i = 0; i < 5; i++) {
            playerSpriteSheets[i] = new SpriteSheet(playerSprites[i]);
        }

        for(int i = 0; i < 5; i++){
            for (int j = 0; j < 9; j++) {
                player[i][j] = playerSpriteSheets[i].crop(j * 64, 0, 64, 64);
            }
        }
    }

}
