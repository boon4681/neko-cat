package game;

import java.awt.Cursor;
import java.io.File;

import engine.Window;
import engine.World;
import engine.animation.Transition;
import engine.objects.base.StaticImage;
import engine.sound.Sound;
import engine.tick.Ticker;
import engine.vector.Vec2;

public class HoverButton extends StaticImage {
    private Transition wTransition;
    private Transition hTransition;
    private Transition xTransition;
    private Transition yTransition;
    private boolean enter;

    public interface OnClick {
        public void click();
    }

    public HoverButton(Ticker ticker, Window window, World world, Vec2 pos, Vec2 size, String path, OnClick onClick) {
        super(world, pos, size, path);
        this.wTransition = new Transition(ticker, size.x, size.x * 0.9, 100, true);
        this.hTransition = new Transition(ticker, size.y, size.y * 0.9, 100, true);
        this.xTransition = new Transition(ticker, pos.x, pos.x + size.x * 0.1 * 0.5, 100, true);
        this.yTransition = new Transition(ticker, pos.y, pos.y + size.y * 0.1 * 0.5, 100, true);
        this.stopPropagation = true;
        this.onHover((g) -> {
            wTransition.start();
            hTransition.start();
            xTransition.start();
            yTransition.start();
            window.setCursor(new Cursor(Cursor.HAND_CURSOR));
            if (!enter) {
                Sounds.SFX.enterSound.play();
                enter = true;
            }
        });
        this.onLeave((g) -> {
            wTransition.stop();
            hTransition.stop();
            xTransition.stop();
            yTransition.stop();
            window.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            enter = false;
        });

        this.onAfterRender((g) -> {
            this.size.x = wTransition.value();
            this.size.y = hTransition.value();
            this.pos.x = xTransition.value();
            this.pos.y = yTransition.value();
        });

        this.onClick((g) -> {
            wTransition.stop();
            hTransition.stop();
            xTransition.stop();
            yTransition.stop();
            Sounds.SFX.clickSound.play();
            window.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            onClick.click();
        });
    }
}
