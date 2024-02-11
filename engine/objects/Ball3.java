package engine.objects;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

import engine.World;
import engine.objects.base.Body;

public class Ball3 extends BallBase {
    private static BufferedImage image = Body.loadImage(new File("assets/c3.png"));

    public Ball3(World world) {
        super(world, 100, 40);
    }

    @Override
    public Color getColor() {
        return Color.GREEN;
    }

    @Override
    public BufferedImage getImage() {
        return image;
    }
}
