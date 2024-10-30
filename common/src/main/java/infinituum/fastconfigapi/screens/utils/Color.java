package infinituum.fastconfigapi.screens.utils;

// TODO: Move Color and Components to Void Lib
public final class Color {
    private final int a;
    private final int r;
    private final int g;
    private final int b;

    private Color(int a, int r, int g, int b) {
        this.a = inRangeARGB(a);
        this.r = inRangeARGB(r);
        this.g = inRangeARGB(g);
        this.b = inRangeARGB(b);
    }

    private static int inRangeARGB(double value) {
        return (int) Math.max(0, Math.min(0xFF, value));
    }

    public static Color white() {
        return Color.of(0xFFFFFF);
    }

    public static Color of(int argb) {
        int a = parseAlpha(argb);
        int r = parseRed(argb);
        int g = parseGreen(argb);
        int b = parseBlue(argb);

        return new Color(a, r, g, b);
    }

    private static int parseAlpha(int argb) {
        if (argb <= 0xFFFFFF) {
            return 0xFF;
        }

        return (argb >> 24) & 0xFF;
    }

    private static int parseRed(int argb) {
        return (argb >> 16) & 0xFF;
    }

    private static int parseGreen(int argb) {
        return (argb >> 8) & 0xFF;
    }

    private static int parseBlue(int argb) {
        return (argb) & 0xFF;
    }

    public static Color black() {
        return Color.of(0x000000);
    }

    public static Color red() {
        return Color.of(0xFF0000);
    }

    public static Color green() {
        return Color.of(0x00FF00);
    }

    public static Color blue() {
        return Color.of(0x0000FF);
    }

    public Color withLuminance(double factor) {
        return new Color(a, inRangeARGB(r * factor), inRangeARGB(g * factor), inRangeARGB(b * factor));
    }

    public int toDecimal() {
        int alpha = (a << 24) & 0xFF000000;
        int red = (r << 16) & 0xFF0000;
        int green = (g << 8) & 0xFF00;
        int blue = (b) & 0xFF;

        return alpha | red | green | blue;
    }

    public int withAlpha() {
        return a;
    }

    public Color withAlpha(int a) {
        return new Color(inRangeARGB(a), r, g, b);
    }

    public int withRed() {
        return r;
    }

    public Color withRed(int r) {
        return new Color(a, inRangeARGB(r), g, b);
    }

    public int withGreen() {
        return g;
    }

    public Color withGreen(int g) {
        return new Color(a, r, inRangeARGB(g), b);
    }

    public int withBlue() {
        return b;
    }

    public Color withBlue(int b) {
        return new Color(a, r, g, inRangeARGB(b));
    }
}