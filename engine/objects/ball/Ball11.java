package engine.objects.ball;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;

import engine.World;
import engine.objects.base.Body;
import engine.vector.Vec2;

public class Ball11 extends BallBase {
    private static BufferedImage image = Body.loadImage(new File("assets/cat/c11.png"));

    public Ball11(World world, Vec2 pos) {
        super(world, 130, 70);
        this.pos = pos;
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
