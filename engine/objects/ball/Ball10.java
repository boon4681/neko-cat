package engine.objects.ball;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

import engine.World;
import engine.objects.base.Body;

public class Ball10 extends BallBase {
    private static BufferedImage image = Body.loadImage(new File("assets/c10.png"));

    public Ball10(World world) {
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
