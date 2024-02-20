package game;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

import engine.Window;
import engine.World;
import engine.animation.Transition;
import engine.event.EventListener;
import engine.event.mouse.MousePosition;
import engine.event.world.MergeScore;
import engine.objects.ball.BallBase;
import engine.objects.ball.BallType;
import engine.objects.base.Body;
import engine.objects.base.StaticImage;
import engine.scene.Scene;
import engine.sound.Sound;
import engine.sound.SoundManager;
import engine.tick.Tickable;
import engine.tick.Ticker;
import engine.vector.Vec2;

public class Game {
    private PhysicWorld game;
    private Number score;
    private Number highscore;
    private Number gameOverScore;
    private StaticImage nextImage;
    private ArrayList<BallType> nexts = new ArrayList<>();
    private boolean canClick = true;
    private boolean isClicked = false;
    private Tickable delayer = new Tickable() {
        public double counter = 0;

        @Override
        public void tick(double dt) {
            if (counter > 10) {
                canClick = true;
                isClicked = false;
                counter = 0;
            }
            if (isClicked) {
                counter += dt;
                canClick = false;
            }
        }
    };

    public Game() {
        Window window = new Window("Neko Cat - Game");
        ImageIcon logo = new ImageIcon("assets/logo.png");
        window.setSize(1028, 37 + 600);
        window.setVisible(true);
        window.setBackground(Color.BLACK);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setLayout(null);
        window.setResizable(false);
        window.setIconImage(logo.getImage());
        Ticker ticker = new Ticker(window);

        Scene gameover = new Scene(window);
        Scene menu = new Scene(window);
        Scene setting = new Scene(window);
        Scene start = new Scene(window);
        Scene gameScene = new Scene(window);
        Scene blackScene = new Scene(window);
        Sound[] popSounds = new Sound[] { Sounds.SFX.pop, Sounds.SFX.pop2, Sounds.SFX.pop3 };
        Sound[] dropSounds = new Sound[] { Sounds.SFX.drop, Sounds.SFX.drop2 };

        Config.load();
        Sounds.MUSIC.bgm.play();
        Sounds.MUSIC.bgm.setVolume(Config.music);

        SoundManager.setVolume(Sounds.SFX.class, Config.sfx);
        reset();

        gameover.setup((w) -> {
            BufferedImage bg = Body.loadImage(new File("assets/game_over.png"));
            DarkOverlay overlay = new DarkOverlay(window, "overlay", window.getWidth(), window.getHeight());
            World world = new World(window, "menu", 358, 476);
            world.setLocation(320, 39);
            world.addBeforeRenderer((g) -> {
                g.drawImage(bg, 0, 0, world.getWidth(), world.getHeight(), world);
                return g;
            });
            gameOverScore = new Number(world, "assets/number_sprite_2.png", new Vec2(80, 190), new Vec2(200, 27),
                    true);
            HoverButton restart = new HoverButton(ticker, window, world, new Vec2(35, 240), new Vec2(292, 85),
                    "assets/restart.png", () -> {
                        gameover.setDisplay(false);
                        reset();
                    });
            HoverButton mainMenu = new HoverButton(ticker, window, world, new Vec2(35, 350), new Vec2(292, 85),
                    "assets/main_menu.png", () -> {
                        gameover.setDisplay(false);
                        setting.setDisplay(false);
                        gameScene.setDisplay(false);
                        start.setDisplay(true);
                        blackScene.setDisplay(true);
                        reset();
                    });

            w.add(world);
            w.add(overlay);
        });
        gameover.setDisplay(false);

        menu.setup((w) -> {
            BufferedImage bg = Body.loadImage(new File("assets/menu_bg.png"));
            DarkOverlay overlay = new DarkOverlay(window, "overlay", window.getWidth(), window.getHeight());
            World world = new World(window, "menu", 358, 476);
            world.setLocation(320, 39);
            world.addBeforeRenderer((g) -> {
                g.drawImage(bg, 0, 0, world.getWidth(), world.getHeight(), world);
                return g;
            });

            HoverButton resume = new HoverButton(ticker, window, world, new Vec2(35, 140), new Vec2(292, 85),
                    "assets/resume.png", () -> {
                        menu.setDisplay(false);
                    });
            HoverButton mainMenu = new HoverButton(ticker, window, world, new Vec2(35, 250), new Vec2(292, 85),
                    "assets/main_menu.png", () -> {
                        menu.setDisplay(false);
                        setting.setDisplay(false);
                        gameScene.setDisplay(false);
                        start.setDisplay(true);
                        blackScene.setDisplay(true);
                        reset();
                    });
            HoverButton exit = new HoverButton(ticker, window, world, new Vec2(35, 360), new Vec2(292, 85),
                    "assets/exit.png", () -> {
                        ticker.stop();
                        System.exit(0);
                    });

            w.add(world);
            w.add(overlay);
        });
        menu.setDisplay(false);
        setting.setup((w) -> {
            BufferedImage bg = Body.loadImage(new File("assets/setting.png"));
            BufferedImage close_bg = Body.loadImage(new File("assets/close_bg.png"));
            DarkOverlay overlay = new DarkOverlay(window, "overlay", window.getWidth(), window.getHeight());
            World world = new World(window, "setting", 500, 476);
            world.setLocation(320, 39);
            world.addBeforeRenderer((g) -> {
                g.drawImage(close_bg, 320, 0, 91, 54, world);
                g.drawImage(bg, 0, 0, 358, 476, world);
                return g;
            });

            HoverButton close = new HoverButton(ticker, window, world,
                    new Vec2(365, 11),
                    new Vec2(33, 33),
                    "assets/X.png", () -> {
                        setting.setDisplay(false);
                    });

            StaticImage popcat = new StaticImage(world, new Vec2(40, 130), new Vec2(40, 40), "assets/pop.png");
            StaticImage note = new StaticImage(world, new Vec2(40, 210), new Vec2(40, 32), "assets/note.png");
            StaticImage music_title = new StaticImage(world, new Vec2(40, 103), new Vec2(115, 19), "assets/MUSIC.png");
            StaticImage sfx_title = new StaticImage(world, new Vec2(40, 183), new Vec2(65, 19), "assets/SFX.png");
            Number bgmLevel = new Number(world, "assets/number_sprite_2.png", new Vec2(200, 100), new Vec2(200, 27),
                    false);
            Number sfxLevel = new Number(world, "assets/number_sprite_2.png", new Vec2(200, 180), new Vec2(200, 27),
                    false);

            bgmLevel.setValue(Sounds.MUSIC.bgm.getVolume());
            sfxLevel.setValue(100);

            Slider bgmSlider = new Slider(world, new Vec2(100, 145), new Vec2(200, 10), 0, 100, Config.music);
            bgmSlider.onChange(v -> {
                Sounds.MUSIC.bgm.setVolume(v);
                bgmLevel.setValue(v);
                Config.music = v;
                Config.save();
            });
            Slider sfxSlider = new Slider(world, new Vec2(100, 225), new Vec2(200, 10), 0, 100, Config.sfx);
            sfxSlider.onChange(v -> {
                SoundManager.setVolume(Sounds.SFX.class, (int) ((double) v * 1.2d));
                sfxLevel.setValue(v);
                Config.sfx = v;
                Config.save();
            });
            w.add(world);
            w.add(overlay);
        });
        setting.setDisplay(false);
        start.setup((w) -> {
            BufferedImage bg = Body.loadImage(new File("assets/startBg.png"));
            World world = new World(window, "bg", 600, 600);
            world.setLocation(200, 0);
            world.addBeforeRenderer((g) -> {
                g.drawImage(bg, 0, 0, world.getWidth(), world.getHeight(), world);
                return g;
            });

            Transition rot = new Transition(ticker, -5, 5, 1000, false);

            StaticImage title = new StaticImage(world, new Vec2(132, 52), new Vec2(342, 175), "assets/title.png");
            title.rotateAtCenter = true;
            rot.start();
            title.onAfterRender((g) -> {
                title.rotation = rot.value();
            });
            HoverButton staButton = new HoverButton(ticker, window, world, new Vec2(152, 323), new Vec2(292, 85),
                    "assets/startBTN.png", () -> {
                        start.setDisplay(false);
                        blackScene.setDisplay(false);
                        gameScene.setDisplay(true);
                    });
            HoverButton settingButton = new HoverButton(ticker, window, world, new Vec2(152, 420), new Vec2(292, 75),
                    "assets/settingBtn.png", () -> {
                        setting.setDisplay(true);
                    });
            w.add(world);
        });
        gameScene.setup((w) -> {
            BufferedImage bg = Body.loadImage(new File("assets/bg.png"));
            World pipeWorld = new World(window, "pipe", 600, 600);
            pipeWorld.setLocation(0, 0);

            StaticImage pipe = new StaticImage(pipeWorld, new Vec2(300 - 42, -40 - 40), new Vec2(85, 219),
                    "assets/pipe.png");
            StaticImage preview = new StaticImage(pipeWorld, new Vec2(pipe.pos.x, 90), new Vec2(30, 30));

            pipe.onAfterRender((g) -> {
                preview.setImage(nexts.get(0).getImage());
                int radius = nexts.get(0).getRadius();
                preview.size = new Vec2(radius * 2, radius * 2);
                preview.pos.x = pipe.pos.x + pipe.size.x / 2 - radius;
            });
            game = new PhysicWorld(window, "game", 375, 540);
            game.setLocation(110, 20);
            StaticImage left = new StaticImage(w, new Vec2(0, 0), new Vec2(274, 600), "assets/extend_left.png");
            StaticImage right = new StaticImage(w, new Vec2(800, 0), new Vec2(214, 600), "assets/extend_right.png");

            World GUI = new World(window, "GUI", window.getWidth(), window.getHeight());
            GUI.setLocation(0, 0);
            // StaticImage nu = new StaticImage(GUI, new Vec2(), new Vec2(48, 540),
            // "assets/number_sprite.png");

            StaticImage title = new StaticImage(GUI, new Vec2(40, 20), new Vec2(227, 110), "assets/title.png");
            StaticImage next = new StaticImage(GUI, new Vec2(40, 150), new Vec2(224, 200), "assets/next.png");

            StaticImage scoreBg = new StaticImage(GUI, new Vec2(40, 380), new Vec2(224, 86), "assets/score.png");
            StaticImage highBg = new StaticImage(GUI, new Vec2(40, 480), new Vec2(224, 86), "assets/high.png");
            StaticImage catolution = new StaticImage(GUI, new Vec2(740, 80), new Vec2(224, 496),
                    "assets/catolution.png");
            nextImage = new StaticImage(GUI, new Vec2(110, 220), new Vec2(80, 80));
            nextImage.setImage(nexts.get(1).getImage());
            score = new Number(GUI, "assets/number_sprite.png", new Vec2(45, 420), new Vec2(200, 27));
            highscore = new Number(GUI, "assets/number_sprite.png", new Vec2(45, 520), new Vec2(200, 27));
            highscore.setValue(Config.highscore);
            HoverButton menuButton = new HoverButton(ticker, window, GUI, new Vec2(765, 20), new Vec2(50, 50),
                    "assets/menu.png", () -> {
                        menu.setDisplay(true);
                        GUI.mx = menu.getWorld().mx;
                        GUI.my = menu.getWorld().my;
                    });
            HoverButton resetButton = new HoverButton(ticker, window, GUI, new Vec2(825, 20), new Vec2(50, 50),
                    "assets/reset.png", () -> {
                        reset();
                    });

            HoverButton settingButton = new HoverButton(ticker, window, GUI, new Vec2(885, 20), new Vec2(50, 50),
                    "assets/cog.png", () -> {
                        setting.setDisplay(true);
                    });
            // for (int i = 0; i < 1000; i++) {
            // BallType.BALL1.createBase(game, new Vec2(Math.random() * 100 + 200,
            // Math.random() * 100));
            // }
            GUI.addEventListener(new EventListener<MousePosition>("mousemove", e -> {
                e.x = Math.max(Math.min(485, e.x - 200), 110);
                pipe.pos.x = e.x - pipe.size.x / 2;
                return true;
            }));
            GUI.addEventListener(new EventListener<MousePosition>("mouseclick", e -> {
                isClicked = true;
                if (!canClick)
                    return true;
                pipe.pos.x = Math.max(Math.min(485, e.x - 200), 110) - pipe.size.x / 2;
                e.x = e.x - 110 - 200;
                Vec2 pos = new Vec2(e.x, 120);
                BallBase ball = nexts.remove(0).createBase(game, pos);
                int p = (int) Math.floor(Math.random() * 2);
                dropSounds[p].play();
                ball.rotationalVelocity = (Math.random() - 0.5) * 10;
                nexts.add(randomBall());
                nextImage.setImage(nexts.get(1).getImage());
                return true;
            }));
            game.addEventListener(new EventListener<MergeScore>("world.circle_merge", e -> {
                int p = (int) Math.floor(Math.random() * 3);
                popSounds[p].play();
                score.setValue(score.getValue() + e.score());
                Config.highscore = Math.max(highscore.getValue(), score.getValue());
                Config.save();
                highscore.setValue(Math.max(highscore.getValue(), score.getValue()));
                return true;
            }));
            game.addEventListener(new EventListener<Boolean>("gameover", e -> {
                gameover.setDisplay(true);
                gameOverScore.setValue(score.getValue());
                return true;
            }));
            World bgWorld = new World(window, "bg", 600, 600);
            bgWorld.setLocation(200, 0);
            bgWorld.addBeforeRenderer((g) -> {
                g.drawImage(bg, 0, 0, bgWorld.getWidth(), bgWorld.getHeight(), bgWorld);
                return g;
            });
            w.add(GUI);
            bgWorld.add(pipeWorld);
            bgWorld.add(game);
            w.add(bgWorld);
        });
        blackScene.setup((w) -> {
            World blackScreen = new World(window, "blackScreen", window.getWidth(), window.getHeight());
            blackScreen.setBackground(Color.BLACK);
            blackScreen.setOpaque(true);
            w.add(blackScreen);
        });
        gameScene.setDisplay(false);
        ticker.tickables.add(delayer);
        ticker.start();
        window.repaint();
    }

    private BallType randomBall() {
        double[] weights = { 1.2, 0.6, 0.3 };
        double sumOfWeights = 0.0;
        for (double weight : weights) {
            sumOfWeights += weight;
        }
        Random random = new Random();
        double randomNumber = random.nextDouble() * sumOfWeights;
        double cumulativeProbability = 0.0;
        for (int i = 0; i < weights.length; i++) {
            cumulativeProbability += weights[i];
            if (randomNumber < cumulativeProbability) {
                return BallType.values()[i];
            }
        }
        return BallType.values()[0];
    }

    private void reset() {
        if (nexts.size() > 0)
            nexts.remove(0);
        if (nexts.size() > 0)
            nexts.remove(0);
        nexts.add(randomBall());
        nexts.add(randomBall());
        if (game != null) {
            game.restart();
            // for (int i = 0; i < 1000; i++) {
            // BallType.BALL1.createBase(game, new Vec2(Math.random() * 100, Math.random() *
            // 20));
            // }
        }
        if (score != null)
            score.setValue(0);
        if (nextImage != null)
            nextImage.setImage(nexts.get(1).getImage());
    }
}
