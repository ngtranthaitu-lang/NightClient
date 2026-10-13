package com.nightslayer.client;

import java.util.ArrayList;
import java.util.List;

public final class Modules {
    public static final Module FPS = new Module("FPS", "Hien thi FPS", true);
    public static final Module CPS = new Module("CPS", "Hien thi so click moi giay", true);
    public static final Module PING = new Module("Ping", "Hien thi do tre mang", true);
    public static final Module COORDS = new Module("Toa do", "Hien thi toa do XYZ", true);
    public static final Module DIRECTION = new Module("Huong nhin", "Hien thi huong N / S / E / W", false);
    public static final Module ARMOR = new Module("Do ben giap", "Hien thi do ben giap dang mac", true);
    public static final Module KEYSTROKES = new Module("Phim bam", "Hien thi phim W A S D", false);

    public static final Module TOTEMS = new Module("Dem totem", "So totem va ngoc trai con trong tui", true);
    public static final Module TARGET = new Module("Muc tieu", "Ten, mau, khoang cach muc tieu dang ngam", true);
    public static final Module COOLDOWN = new Module("Hoi chieu danh", "Thanh hoi chieu don danh duoi tam ngam", true);
    public static final Module LOWHP = new Module("Canh bao mau", "Bao khi mau xuong thap", true);

    public static final Module ZOOM = new Module("Zoom", "Giu phim C de phong to", true);
    public static final Module FULLBRIGHT = new Module("Fullbright", "Nhin ro trong bong toi (chi o may ban)", false);

    public static final List<Module> HUD = List.of(FPS, CPS, PING, COORDS, DIRECTION, ARMOR, KEYSTROKES);
    public static final List<Module> PVP = List.of(TOTEMS, TARGET, COOLDOWN, LOWHP);
    public static final List<Module> VISUAL = List.of(ZOOM, FULLBRIGHT);

    public static final List<Module> ALL = buildAll();

    public static final int[] THEMES = {0xFF8B7CFF, 0xFF3DDC97, 0xFFFF5A5A, 0xFF4DA3FF, 0xFFFFB347};
    public static final String[] THEME_NAMES = {"Tim", "Xanh la", "Do", "Xanh duong", "Cam"};
    public static int theme = 0;

    private static List<Module> buildAll() {
        List<Module> all = new ArrayList<>();
        all.addAll(HUD);
        all.addAll(PVP);
        all.addAll(VISUAL);
        return List.copyOf(all);
    }

    public static int accent() {
        return THEMES[theme];
    }

    private Modules() {
    }
}
