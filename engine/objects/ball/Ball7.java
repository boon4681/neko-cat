package engine.objects.ball;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;


import engine.World;
import engine.objects.base.Body;
import engine.vector.Vec2;

public class Ball7 extends BallBase {
    private static BufferedImage image = Body.loadImage(new File("assets/cat/c7.png"));

    public Ball7(World world, Vec2 pos) {
        super(world, 62, 32);
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
