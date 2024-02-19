package engine.event.world;

import java.awt.Dimension;

import engine.event.Event;

public class CircleMerge extends Event<MergeScore> {
    public CircleMerge(MergeScore v) {
        super("world.circle_merge", v);
    }
}
