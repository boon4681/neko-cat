package game.events;

import engine.event.Event;

public class GameOver extends Event<Boolean> {

	public GameOver(Boolean v) {
		super("gameover", v);
	}
}
