package engine.objects.ball;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

import engine.World;
import engine.objects.base.Body;

public class Ball7 extends BallBase {
    private static BufferedImage image = Body.loadImage(new File("assets/c7.png"));

    public Ball7(World world) {
        super(world, 70, 20);
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
