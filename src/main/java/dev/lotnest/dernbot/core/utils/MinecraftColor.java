package dev.lotnest.dernbot.core.utils;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.awt.Color;

@RequiredArgsConstructor
@Getter
public enum MinecraftColor {
    BLACK(new Color(0x000000), "Black", "black"),
    DARK_BLUE(new Color(0x0000AA), "Dark Blue", "dark_blue"),
    DARK_GREEN(new Color(0x00AA00), "Dark Green", "dark_green"),
    DARK_AQUA(new Color(0x00AAAA), "Dark Aqua", "dark_aqua"),
    DARK_RED(new Color(0xAA0000), "Dark Red", "dark_red"),
    DARK_PURPLE(new Color(0xAA00AA), "Dark Purple", "dark_purple"),
    GOLD(new Color(0xFFAA00), "Gold", "gold"),
    GRAY(new Color(0xAAAAAA), "Gray", "gray"),
    DARK_GRAY(new Color(0x555555), "Dark Gray", "dark_gray"),
    BLUE(new Color(0x5555FF), "Blue", "blue"),
    GREEN(new Color(0x55FF55), "Green", "green"),
    AQUA(new Color(0x55FFFF), "Aqua", "aqua"),
    RED(new Color(0xFF5555), "Red", "red"),
    LIGHT_PURPLE(new Color(0xFF55FF), "Light Purple", "light_purple"),
    YELLOW(new Color(0xFFFF55), "Yellow", "yellow"),
    WHITE(new Color(0xFFFFFF), "White", "white");

    private final Color color;
    private final String officialName;
    private final String technicalName;

    public Color getBackgroundColor() {
        return new Color(
                color.getRed() / 4,
                color.getGreen() / 4,
                color.getBlue() / 4
        );
    }
}
