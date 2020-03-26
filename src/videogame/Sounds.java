package videogame;

import javafx.scene.media.MediaPlayer;

/**
 * @author Ernesto García
 */
public class Sounds {
    public static MediaPlayer hitPlayer;
    public static MediaPlayer pointPlayer;
    public static MediaPlayer gameOverPlayer;

    /**
     * initializing sounds
     */
    public static void initialize() {
        hitPlayer = SoundLoader.loadSound("/sounds/hurt.mp3");
        pointPlayer = SoundLoader.loadSound("/sounds/point.mp3");
        gameOverPlayer = SoundLoader.loadSound("/sounds/game-over.mp3");
    }
}
