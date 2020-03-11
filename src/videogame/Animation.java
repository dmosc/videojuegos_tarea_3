package videogame;

import java.awt.image.BufferedImage;
import java.nio.Buffer;

/**
 * @author Ernesto García
 */
public class Animation {
    private int speed;              // for the speed of every frame
    private int index;              // to get the index of next frame to pain
    private long lastTime;          // to save the previous time of the animation
    private long timer;             // to accumulate the time of animation
    private BufferedImage[] frames; // to store every image/frame

    /**
     * Creating the animation with all the frames and the speed for each
     *
     * @param frames an <code>array</code> of images
     * @param speed  an <code>array</code> value for the speed of every frame
     */
    public Animation(BufferedImage[] frames, int speed) {
        this.frames = frames;
        this.speed = speed;
        index = 0;
        timer = 0;
        lastTime = System.currentTimeMillis();
    }

    /**
     * Getting the current frame to paint
     * @return the <code>BufferedImage</code> to the corresponding frame to paint
     */
    public BufferedImage getCurrentFrame(){
        return frames[index];
    }

    public void tick(){
        timer += System.currentTimeMillis() - lastTime;
        lastTime = System.currentTimeMillis();

        if(timer > speed){
            index++;
            timer = 0;
            if(index >= frames.length){
                index = 0;
            }
        }
    }
}
