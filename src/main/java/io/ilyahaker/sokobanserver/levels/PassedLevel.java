package io.ilyahaker.sokobanserver.levels;

import lombok.Data;

@Data
public class PassedLevel {

    private Level level;
    private int steps;
    private int lastSteps;

    public PassedLevel(Level level, int steps, int lastSteps) {
        this.level = level;
        this.steps = steps;
        this.lastSteps = lastSteps;
    }

}
