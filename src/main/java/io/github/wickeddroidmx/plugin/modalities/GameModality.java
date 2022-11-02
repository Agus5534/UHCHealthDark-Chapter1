package io.github.wickeddroidmx.plugin.modalities;

import org.bukkit.Material;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface GameModality {
    String name();
    String key();
    Material material();
    ModalityType modalityType();
    boolean experimental() default false;
    String[] lore() default {""};

}
