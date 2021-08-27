package io.ilyahaker.sokobanserver.levels;

import io.ilyahaker.sokobanserver.FillingStrategy;
import io.ilyahaker.sokobanserver.objects.DecorationObject;
import io.ilyahaker.sokobanserver.objects.GameObject;
import io.ilyahaker.sokobanserver.objects.Material;
import lombok.Getter;

import java.util.List;
import java.util.Map;

public class FinalDisplay {
    @Getter
    private final GameObject[][] display;

    public FinalDisplay(List<String> lore) {
        display = new FillingStrategy(Map.of('p',
                new DecorationObject(Material.WHITE_STAINED_GLASS_PANE, "Congratulations!", lore)),
                List.of("ppppppppp",
                        "ppppppppp",
                        "ppppppppp",
                        "ppppppppp",
                        "ppppppppp",
                        "ppppppppp")).getObjects();
    }
}
