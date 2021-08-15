package io.ilyahaker.sokobanserver.database.api.result;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

public class SelectResult {

    @Getter
    @Setter
    private List<Column> columns;

    @Getter
    @Setter
    private List<Row> rows;
}
