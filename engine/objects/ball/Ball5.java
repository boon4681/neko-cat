package engine.objects.ball;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

import engine.World;
import engine.objects.base.Body;

public class Ball5 extends BallBase {
    private static BufferedImage image = Body.loadImage(new File("assets/c5.png"));

    public Ball5(World world) {
        super(world, 36, 20);
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
