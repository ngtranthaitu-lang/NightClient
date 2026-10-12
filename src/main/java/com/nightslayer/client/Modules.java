package com.nightslayer.client;

import java.util.List;

public final class Modules {
    public static final Module FPS = new Module("FPS", "Hien thi FPS", true);
    public static final Module CPS = new Module("CPS", "Hien thi so click moi giay", true);
    public static final Module COORDS = new Module("Toa do", "Hien thi toa do XYZ", true);
    public static final Module ARMOR = new Module("Do ben giap", "Hien thi do ben giap dang mac", true);
    public static final Module KEYSTROKES = new Module("Phim bam", "Hien thi phim W A S D", false);
    public static final Module ZOOM = new Module("Zoom", "Giu phim C de phong to", true);
    public static final Module FULLBRIGHT = new Module("Fullbright", "Nhin ro trong bong toi (chi o may ban)", false);

    public static final List<Module> ALL = List.of(FPS, CPS, COORDS, ARMOR, KEYSTROKES, ZOOM, FULLBRIGHT);

    private Modules() {
    }
}
