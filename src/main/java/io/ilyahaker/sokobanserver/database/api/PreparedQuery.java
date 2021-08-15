package io.ilyahaker.sokobanserver.database.api;

public interface PreparedQuery<R> {

    R execute(Object... args);

}
