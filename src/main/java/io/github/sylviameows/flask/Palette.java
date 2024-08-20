package io.github.sylviameows.flask;

import net.kyori.adventure.text.format.TextColor;

/**
 * Flask's custom palette, to create consistency.
 */
public interface Palette {
    TextColor WHITE = TextColor.color(0xFFFFFF); // default
    TextColor GRAY = TextColor.color(0xAAAAAA); // default
    TextColor DARK_GRAY = TextColor.color(0x555555); // default
    TextColor BLACK = TextColor.color(0x000000); // default

    TextColor RED_DARK = TextColor.color(0xAA0000); // default
    TextColor RED = TextColor.color(0xFF0000);
    TextColor RED_LIGHT = TextColor.color(0xFF5555); // default

    TextColor GOLD = TextColor.color(0xFFAA00); // default

    TextColor YELLOW = TextColor.color(0xFFFF00); // default

    TextColor LIME = TextColor.color(0xAAFF55);

    TextColor GREEN_DARK = TextColor.color(0x00AA00); // default
    TextColor GREEN = TextColor.color(0x55FF55); // default

    TextColor MINT = TextColor.color(0x87ffdf);

    TextColor AQUA_DARK = TextColor.color(0x00AAAA); // default
    TextColor AQUA = TextColor.color(0x55FFFF); // default

    TextColor BLUE_DARK = TextColor.color(0x0000AA); // default
    TextColor BLUE = TextColor.color(0x5555FF); // default

    TextColor PURPLE = TextColor.color(0xAA00AA); // default
    TextColor PURPLE_LIGHT = TextColor.color(0xFF55FF); // default

}
