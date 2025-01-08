package app.global;

public class AppConfig {
    private static String mode;

    static {
        setDevMode();
    }

    public static void setProdMode() {
        mode = "prod";
    }

    public static void setDevMode() {
        mode = "dev";
    }

    public static void setTestMode() {
        mode = "test";
    }

    public static boolean isProd() {
        return mode.equals("prod");
    }

    public static boolean isDev() {
        return mode.equals("dev");
    }

    public static boolean isTest() {
        return mode.equals("test");
    }

    public static String getDbPath() {
        return "db/" + mode;
    }
}
