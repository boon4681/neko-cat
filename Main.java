import javax.swing.*;
import engine.World;
import engine.event.EventListener;
import engine.event.MousePosition;
import engine.objects.BallBase;
import engine.objects.BallType;

public class Main {
    public static void main(String[] args) {
        JFrame frame = new JFrame("MeowKa - Game");
        frame.setSize(600, 437);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(null);

        World game = new World();
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
        frame.add(game);
        game.start();
        frame.repaint();
    }
}