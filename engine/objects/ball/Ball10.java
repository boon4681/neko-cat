package engine.objects.ball;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;

import engine.World;
import engine.objects.base.Body;
import engine.vector.Vec2;

public class Ball10 extends BallBase {
    private static BufferedImage image = Body.loadImage(new File("assets/cat/c10.png"));

    public Ball10(World world, Vec2 pos) {
        super(world, 110, 59);
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
