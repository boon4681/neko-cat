package engine.objects.ball;

import java.awt.Color;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;

import engine.World;
import engine.objects.base.Body;
import engine.vector.Vec2;

public class Ball1 extends BallBase {
    private static BufferedImage image = Body.loadImage(new File("assets/cat/c1c.png"));

    public Ball1(World world, Vec2 pos) {
        super(world, 20, 5);
        this.pos = pos;
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
