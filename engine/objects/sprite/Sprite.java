package engine.objects.sprite;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.swing.JPanel;

import engine.World;
import engine.objects.base.Body;
import engine.objects.base.BodyType;
import engine.objects.base.ShapeType;
import engine.vector.Vec2;

public class Sprite extends Body {
    private int tw;
    private int th;
    private int w;
    private int h;
    private int selected_tile = 0;
    private double scale_x = 0;
    private double scale_y = 0;

    public Vec2 pos;
    private BufferedImage image;

    public Sprite(World world, String source, Vec2 pos, int tw, int th, int w, int h) {
        super(world, pos, new Vec2(), new Vec2(), new Vec2(), 0);
        this.tw = tw;
        this.th = th;
        this.w = w;
        this.h = h;
        this.pos = pos;
        image = Body.loadImage(new File(source));
        this.scale_x = (((double) image.getWidth()) * ((double) w / (double) tw));
        this.scale_y = (((double) image.getHeight()) * ((double) h / (double) th));
    }

    public void setSprite(int i) {
        selected_tile = i;
    }

    @Override
    public void render(Graphics2D g) {
        int tile_p_x = (int) (image.getWidth() / tw);
        Graphics2D g2d = (Graphics2D) g.create((int) this.pos.x, (int) this.pos.y, w, h);
        g2d.drawImage(image, (int) ((selected_tile % tile_p_x) * w), (int) (-(selected_tile / tile_p_x) * h),
                (int) scale_x,
                (int) scale_y, null);
        g2d.dispose();
    }

    @Override
    public void update(double dt) {
    }

    @Override
    public void updatePosition(double dt) {
    }

    @Override
    public ShapeType getShapeType() {
        return null;
    }

    @Override
    public double getX() {
        return this.pos.x;
    }

    @Override
    public double getY() {
        return this.pos.y;
    }

    @Override
    public BodyType getBodyType() {
        return null;
    }
}
