package io.github.wickeddroidmx.plugin.modalities.configurable;

import java.util.HashMap;

public interface ToggleConfig extends ConfigurableModality {
    HashMap<Integer, Boolean> optionsValues();
    void toggle(Integer option);
    void remove(Integer option);
    boolean get(Integer option);
}
