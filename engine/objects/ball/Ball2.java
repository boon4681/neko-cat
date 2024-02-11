package engine.objects.ball;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

import engine.World;
import engine.objects.base.Body;

public class Ball2 extends BallBase {
    private static BufferedImage image = Body.loadImage(new File("assets/c2.png"));

    public Ball2(World world) {
        super(world, 20, 12);
    }

    @Override
    public Color getColor() {
        return Color.YELLOW;
    }

    @Override
    public BufferedImage getImage() {
        return image;
    }
}
