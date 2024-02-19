package engine.objects.ball;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;

import engine.World;
import engine.objects.base.Body;
import engine.vector.Vec2;

public class Ball2 extends BallBase {
    private static BufferedImage image = Body.loadImage(new File("assets/cat/c2.png"));

    public Ball2(World world, Vec2 pos) {
        super(world, 22, 7);
        this.pos = pos;
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
