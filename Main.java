import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.*;
import engine.World;
import engine.event.EventListener;
import engine.event.MousePosition;
import engine.objects.ball.BallBase;
import engine.objects.ball.BallType;
import engine.objects.base.Body;

public class Main {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Neko cat - Game");
        frame.setSize(1028, 37 + 700);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(null);
        BufferedImage bImage = Body.loadImage(new File("assets/bg.png"));

        World game = new World("game",375, 400);
        World bgWorld = new World("bg",600, 600);
        game.setLocation(110, 160);
        bgWorld.addRenderer((g) -> {
            g.drawImage(bImage, 0, 0, bgWorld.getWidth(), bgWorld.getHeight(), bgWorld);
        });
        game.addEventListener(new EventListener<MousePosition>("mouseclick", e -> {
            BallBase ball = BallType.BALL1.createBase(game);
            ball.rotationalVelocity = (Math.random() - 0.5) * 10;
            ball.pos.x = e.x;
        }));
        // game.addEventListener(new EventListener<MousePosition>("mousemove", e -> {
        // BallBase ball = BallType.BALL1.createBase(game);
        // ball.pos = new Vec2(e.x, e.y);
        // }));
        // new Circle(game, 10, 10);
        frame.add(bgWorld);
        bgWorld.add(game);
        bgWorld.start();
        game.start();
        frame.repaint();
    }
}