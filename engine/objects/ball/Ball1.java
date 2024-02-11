package engine.objects.ball;

import java.awt.Color;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

import engine.World;
import engine.objects.base.Body;

public class Ball1 extends BallBase {
    private static BufferedImage image = Body.loadImage(new File("assets/c1c.png"));

    public Ball1(World world) {
        super(world, 16, 5);
    }

    @Override
    public Color getColor() {
        return Color.RED;
    }

    @Override
    public BufferedImage getImage() {
        return image;
    }
}
