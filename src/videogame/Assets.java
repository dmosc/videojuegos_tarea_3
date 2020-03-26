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
    public static BufferedImage[] playerSprites;        // to store the player image
    public static BufferedImage[] enemySprites;                // to store the enemy image
    public static BufferedImage[] allySprites;                // to store the enemy image
    public static BufferedImage[][] player;             // to store the player image
    public static BufferedImage[][] enemy;                  // to store the enemy image
    public static BufferedImage[][] ally;                   // to store the ally image
    public static BufferedImage gameOver;               // to store the game over image

    /**
     * initializing the images of the game
     */
    public static void init() {
        background = ImageLoader.loadImage("/images/background.png");
        gameOver = ImageLoader.loadImage("/images/game-over.png");

        loadPlayer();
        loadEnemy();
        loadAlly();
    }

    private static void loadPlayer(){
        int animationQty = 5;
        playerSprites = new BufferedImage[animationQty];
        playerSprites[0] = ImageLoader.loadImage("/images/sprites/player/stay.png");
        playerSprites[1] = ImageLoader.loadImage("/images/sprites/player/up.png");
        playerSprites[2] = ImageLoader.loadImage("/images/sprites/player/right.png");
        playerSprites[3] = ImageLoader.loadImage("/images/sprites/player/down.png");
        playerSprites[4] = ImageLoader.loadImage("/images/sprites/player/left.png");

        player = new BufferedImage[animationQty][9];

        for (int i = 0; i < animationQty; i++) {
            player[i] = new BufferedImage[9];
        }

        SpriteSheet[] playerSpriteSheets = new SpriteSheet[animationQty];
        for (int i = 0; i < animationQty; i++) {
            playerSpriteSheets[i] = new SpriteSheet(playerSprites[i]);
        }

        for (int i = 0; i < animationQty; i++) {
            for (int j = 0; j < 9; j++) {
                player[i][j] = playerSpriteSheets[i].crop(j * 64, 0, 64, 64);
            }
        }
    }

    private static void loadEnemy(){
        int animationQty = 1;
        enemySprites = new BufferedImage[animationQty];
        enemySprites[0] = ImageLoader.loadImage("/images/sprites/enemy/left.png");

        enemy = new BufferedImage[animationQty][9];

        for (int i = 0; i < animationQty; i++) {
            enemy[i] = new BufferedImage[9];
        }

        SpriteSheet[] enemySpriteSheets = new SpriteSheet[animationQty];
        for (int i = 0; i < animationQty; i++) {
            enemySpriteSheets[i] = new SpriteSheet(enemySprites[i]);
        }

        for (int i = 0; i < animationQty; i++) {
            for (int j = 0; j < 9; j++) {
                enemy[i][j] = enemySpriteSheets[i].crop(j * 64, 0, 64, 64);
            }
        }
    }

    private static void loadAlly(){
        int animationQty = 1;
        allySprites = new BufferedImage[animationQty];
        allySprites[0] = ImageLoader.loadImage("/images/sprites/ally/right.png");

        ally = new BufferedImage[animationQty][9];

        for (int i = 0; i < animationQty; i++) {
            ally[i] = new BufferedImage[9];
        }

        SpriteSheet[] allySpriteSheets = new SpriteSheet[animationQty];
        for (int i = 0; i < animationQty; i++) {
            allySpriteSheets[i] = new SpriteSheet(allySprites[i]);
        }

        for (int i = 0; i < animationQty; i++) {
            for (int j = 0; j < 9; j++) {
                ally[i][j] = allySpriteSheets[i].crop(j * 64, 0, 64, 64);
            }
        }
    }


}
