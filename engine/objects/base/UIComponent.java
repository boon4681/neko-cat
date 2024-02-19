package engine.objects.base;

import java.awt.Graphics2D;

import engine.World;
import engine.event.mouse.MousePosition;
import engine.vector.Vec2;

public class UIComponent extends StaticBody {

    public static interface IRenderer {
        public void render(Graphics2D g);
    }

    protected IRenderer renderer;
    private IRenderer on_hover;
    private IRenderer on_leave;
    private IRenderer on_click;
    private IRenderer after_renderer;
    protected boolean is_hover = false;
    protected boolean is_leave = false;
    protected boolean is_click = false;
    protected MousePosition mouseclickPos = new MousePosition(0, 0);
    public boolean stopPropagation = false;

    public UIComponent(World world, Vec2 pos) {
        super(world, pos, new Vec2(), new Vec2(), new Vec2(), 0);
        this.renderer = null;
    }

    public UIComponent(World world, Vec2 pos, IRenderer renderer) {
        super(world, pos, new Vec2(), new Vec2(), new Vec2(), 0);
        this.renderer = renderer;
    }

    public void onHover(IRenderer renderer) {
        on_hover = renderer;
    }

    public void onAfterRender(IRenderer renderer) {
        after_renderer = renderer;
    }

    public void onLeave(IRenderer renderer) {
        on_leave = renderer;
    }

    public void onClick(IRenderer renderer) {
        on_click = renderer;
    }

    public final void hover(boolean hover) {
        if (is_hover && !hover) {
            is_leave = true;
        }
        is_hover = hover;
    }

    public boolean click(MousePosition p) {
        is_click = true;
        this.mouseclickPos = p;
        return !stopPropagation;
    }

    @Override
    public void render(Graphics2D g) {
        if (this.is_hover && this.on_hover != null) {
            this.on_hover.render(g);
        }
        if (this.is_leave && this.on_leave != null) {
            this.on_leave.render(g);
            this.is_leave = false;
        }
        if (this.is_click && this.on_click != null) {
            this.on_click.render(g);
            this.is_click = false;
        }
        this.renderer.render(g);
        if (this.after_renderer != null) {
            this.after_renderer.render(g);
        }
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

}
