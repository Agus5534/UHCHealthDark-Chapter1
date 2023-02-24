package io.github.wickeddroidmx.plugin.modalities.configurable;

import java.util.HashMap;

public interface ValueConfig extends ConfigurableModality {
    HashMap<Integer, Object> optionsValues();
    void set(Integer option, Object value);
    void remove(Integer option);
    Object get(Integer option);
}
