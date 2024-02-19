package engine.objects.base;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;

import engine.World;
import engine.vector.Vec2;

public class StaticImage extends UIComponent {

    private BufferedImage image;

    public StaticImage(World world, Vec2 pos, Vec2 size) {
        super(world, pos);
        this.pos = pos;
        this.size = size;
        this.createRenderer();
    }

    public StaticImage(World world, Vec2 pos, Vec2 size, String path) {
        this(world, pos, size, Body.loadImage(new File(path)));
    }

    private StaticImage(World world, Vec2 pos, Vec2 size, BufferedImage image) {
        super(world, pos);
        this.size = size;
        this.image = image;
        this.createRenderer();
    }

    private void createRenderer() {
        this.renderer = (g) -> {
            g = (Graphics2D) g.create();
            if (this.rotateAtCenter) {
                g.rotate(Math.toRadians(this.rotation), (int) this.getX() + this.size.x / 2,
                        (int) this.getY() + this.size.y / 2);
            } else {
                g.rotate(Math.toRadians(this.rotation), (int) this.getX(), (int) this.getY());
            }
            g.drawImage(this.image, (int) pos.x, (int) pos.y, (int) this.size.x, (int) this.size.y, null);
            g.dispose();
        };
    }

    public void setImage(BufferedImage image) {
        this.image = image;
    }
}
