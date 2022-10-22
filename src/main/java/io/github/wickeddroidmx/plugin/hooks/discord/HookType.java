package io.github.wickeddroidmx.plugin.hooks.discord;

public enum HookType {
    POST_UHC("https://discord.com/api/webhooks/999373999098302464/-JiuuBY1s0t_EsPYuMp0PtL0j4JUVGp0hpH073GoAoLXA4pAMGyS-xyP88m8njVP2HoN"),
    LOGS("https://discord.com/api/webhooks/920007596004474930/O3f90OX8H6z3Vhqgh-AEvXFwDNzNZfLV9CwmYYrjIniSzFUKcrrVREvhZpdbZ4QYisla");

    String url;

    HookType(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }
}
