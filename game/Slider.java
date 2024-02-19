package game;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;

import engine.World;
import engine.event.EventListener;
import engine.event.mouse.MousePosition;
import engine.objects.base.Body;
import engine.objects.base.UIComponent;
import engine.vector.Vec2;

public class Slider extends UIComponent {
    private int min = 0;
    private int max = 0;
    private int value = 0;
    private static BufferedImage image = Body.loadImage(new File("assets/slider_thumb.png"));
    private Vec2 thumb_pos = new Vec2();
    private boolean pressed = false;
    private OnChange onChange;

    public static interface OnChange {
        public void on(int v);
    }

    public Slider(World world, Vec2 pos, Vec2 size, int min, int max, int val) {
        super(world, pos);
        this.size = size;
        this.min = min;
        this.max = max;
        this.value = val;
        this.renderer = (g) -> {
            g.setColor(new Color(158, 141, 98));
            g.fillOval((int) pos.x - (int) (size.y / 2), (int) pos.y, (int) size.y, (int) size.y);
            g.fillOval((int) (pos.x + size.x - size.y*1.5), (int) pos.y, (int) size.y, (int) size.y);
            g.fillRect((int) pos.x, (int) pos.y, (int) (size.x - size.y), (int) size.y);

            g.setColor(new Color(80, 71, 48));
            g.fillOval((int) pos.x - (int) (size.y / 2), (int) pos.y, (int) size.y, (int) size.y);
            // g.fillOval((int) (pos.x + (size.x * ((double) value / (double) (max -
            // min)))), (int) pos.y, (int) size.y,
            // (int) size.y);

            g.fillRect(
                    (int) pos.x,
                    (int) pos.y,
                    (int) (size.x * ((double) value / (double) (max - min))),
                    (int) size.y);
            int h = (int) (((double) 27 / 11) * size.y);
            int w = (int) (((double) 16 / 11) * size.y);
            int tx = (int) (pos.x + (size.x * ((double) value / (double) (max - min)))) - w / 2;
            int ty = (int) (pos.y - (double) (h / 2) + size.y / 2);
            g.drawImage(image, tx, ty, w, h, null);
            thumb_pos.x = tx;
            thumb_pos.y = ty;
            if (pressed) {
                int x = (int) (world.mx - this.pos.x);
                int v = Math.min(this.max,
                        Math.max(this.min, (int) ((double) (x / size.x) * (this.max - this.min))));
                if (this.value != v) {
                    this.value = v;
                    this.onChange.on(v);
                }
            }
        };

        this.onClick((g) -> {
            pressed = true;
        });

        world.addEventListener(new EventListener<MousePosition>("mouseup", e -> {
            pressed = false;
            return true;
        }));
    }

    public void setValue(int value) {
        this.value = value;
    }

    public void onChange(OnChange onChange) {
        this.onChange = onChange;
    }
}
