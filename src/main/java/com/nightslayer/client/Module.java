package com.nightslayer.client;

public class Module {
    public final String name;
    public final String description;
    private boolean enabled;

    public Module(String name, String description, boolean enabled) {
        this.name = name;
        this.description = description;
        this.enabled = enabled;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void toggle() {
        enabled = !enabled;
    }
}
