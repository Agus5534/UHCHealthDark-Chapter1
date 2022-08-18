package io.github.wickeddroidmx.plugin.hooks.discord;

import io.github.wickeddroidmx.plugin.hooks.DiscordWebhook;

public class DiscordManager {

    public DiscordWebhook getDiscordHook(HookType hookType) {
        return new DiscordWebhook(hookType.getUrl());
    }
}
