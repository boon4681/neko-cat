package engine.animation;

import engine.tick.Ticker;

public class Transition {
    private enum State {
        IN,
        OUT;
    }

    public static interface Interpolate {
        public double run(double from, double to, double duration, double time);
    }

    private final double from;
    private final double to;
    private double time = 0;
    private double duration;
    private boolean running;
    private State state = State.IN;
    private boolean InOut;
    public final Interpolate interpolate;

    public Transition(Ticker ticker, double from, double to, double duration, boolean InOut) {
        this(ticker, from, to, duration, InOut, (f, t, d, T) -> {
            double s = T / d;
            return (1 - s) * f + s * t;
        });
    }

    public Transition(Ticker ticker, double from, double to, double duration, boolean InOut, Interpolate interpolate) {
        ticker.transitions.add(this);
        this.from = from;
        this.to = to;
        this.InOut = InOut;
        this.duration = duration;
        this.interpolate = interpolate;
    }

    public final double value() {
        if (this.running) {
            return this.interpolate.run(from, to, duration, time);
        } else {
            return this.from;
        }
    }

    public void start() {
        if (this.running)
            return;
        this.state = State.IN;
        this.running = true;
        this.time = 0;
    }

    public void stop() {
        if (this.InOut) {
            this.state = State.OUT;
        } else {
            this.running = false;
        }
    }

    public boolean isRunning() {
        return running;
    }

    public void update(double dt) {
        if (this.isRunning()) {
            if (this.InOut) {
                switch (state.name()) {
                    case "IN": {
                        if (this.time <= this.duration) {
                            this.time += dt * 60;
                        }
                        break;
                    }
                    case "OUT": {
                        if (this.time > 0) {
                            this.time -= dt * 60;
                        } else {
                            this.running = false;
                        }
                        break;
                    }
                }
            } else {
                switch (state.name()) {
                    case "IN": {
                        if (this.time <= this.duration) {
                            this.time += dt * 60;
                        } else {
                            state = State.OUT;
                        }
                        break;
                    }
                    case "OUT": {
                        if (this.time > 0) {
                            this.time -= dt * 60;
                        } else {
                            state = State.IN;
                        }
                        break;
                    }
                }
            }
        }
        if(this.time < 0) this.time = 0;
    }
}
