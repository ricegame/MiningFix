package ricedotwho.mf.hud;

public class titleClass {
    static boolean running = false;
    public static String titleText = "";
    public static void createTitle(String text, long duration) {
        if(running) return;
        new Thread(() -> {
            try {
                running = true;
                titleText = text;
                Thread.sleep(duration);
                running = false;
                titleText = "";
            } catch(InterruptedException e) {
                throw new RuntimeException(e);
            }
        }).start();
    }
    public static void overrideTitle(String text, long duration) {
        new Thread(() -> {
            try {
                running = true;
                titleText = text;
                Thread.sleep(duration);
                running = false;
            } catch(InterruptedException e) {
                throw new RuntimeException(e);
            }
        }).start();
    }
}
