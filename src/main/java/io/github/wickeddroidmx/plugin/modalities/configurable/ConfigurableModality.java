package io.github.wickeddroidmx.plugin.modalities.configurable;

import java.util.ArrayList;
import java.util.List;

public interface ConfigurableModality {
    List<Integer> optionList();
    List<Integer> options = new ArrayList<>();
    void setOption(Integer option);
    Integer getOption();

    Integer enabledOption = null;
}
