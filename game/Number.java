package game;

import java.awt.Graphics2D;
import java.util.ArrayList;

import engine.World;
import engine.objects.base.UIComponent;
import engine.objects.sprite.Sprite;
import engine.vector.Vec2;

public class Number extends UIComponent {
    private Sprite[] nums = new Sprite[10];
    private int num = 0;
    private boolean changed = true;
    private boolean center = true;

    public Number(World world, String sprite, Vec2 pos, Vec2 size, boolean center) {
        this(world, sprite, pos, size);
        this.center = center;
    }

    public Number(World world, String sprite, Vec2 pos, Vec2 size) {
        super(world, pos);
        for (int i = 0; i < 10; i++) {
            nums[i] = new Sprite(world, sprite, new Vec2(0, 0), 192, 216, 24, 27);
        }
        this.renderer = (g) -> {
            if (!changed)
                return;
            changed = false;
            String s = "" + num;
            for (int i = 0; i < nums.length; i++) {
                Sprite d = this.nums[i];
                d.visible = false;
            }
            int l = 0;
            for (int i = nums.length - 1; i > nums.length - s.length() - 1; i--) {
                l++;
                Sprite d = this.nums[i];
                int v = s.charAt(s.length() - l) - '0';
                d.visible = true;
                if (center) {
                    d.pos.x = pos.x + (size.x / 2) + (s.length() - l) * 24 - s.length() * 12;
                } else {
                    d.pos.x = pos.x + (size.x / 2) - l * 24;
                }
                d.pos.y = pos.y;
                d.setSprite(v);
                d.render(g);
            }
        };
    }

    public void setValue(int i) {
        if (i != this.num) {
            changed = true;
        }
        this.num = i;
    }

    public int getValue() {
        return this.num;
    }
}
