package io.ilyahaker.sokobanserver.database.api.result;

import lombok.Data;

@Data
public class Field {
    private final Class clazz;
    private final Object value;
}
