package io.github.wickeddroidmx.plugin.hooks.discord;

public enum HookType {
    POST_UHC("https://canary.discord.com/api/webhooks/1062464782302728242/UCpc2s5c7tKVBLSTs6KFJyHyhgprftAbndQWq3xR2lDpMB-QSrFEZt8jxTJkqN7CJZbb");

    String url;

    HookType(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }
}
