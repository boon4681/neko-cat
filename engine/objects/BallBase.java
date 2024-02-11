package engine.objects;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

import engine.World;
import engine.objects.base.Circle;

public abstract class BallBase extends Circle {
    public abstract Color getColor();

    public abstract BufferedImage getImage();

    public BallBase(World world, int radius, int mass) {
        super(world, radius, mass);
    }

    @Override
    public void render(Graphics2D g) {
        g = (Graphics2D) g.create();
        g.setColor(this.getColor());
        g.rotate(Math.toRadians(this.rotation), (int) this.getX() + this.radius, (int) this.getY() + this.radius);
        g.drawImage(this.getImage(), (int) this.getX(), (int) this.getY(), this.radius * 2, this.radius * 2,
                getWorld());
        g.dispose();
        // g.fillOval((int) this.getX(), (int) this.getY(), this.radius * 2, this.radius
        // * 2);
    }
}
