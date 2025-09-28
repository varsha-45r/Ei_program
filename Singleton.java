public class Singleton {
    public static void main(String[] args) {
        Logger logger1 = Logger.getInstance();
        Logger logger2 = Logger.getInstance();

        logger1.log("System started.");
        logger2.log("User logged in.");

        System.out.println("Same instance? " + (logger1 == logger2));
    }
}


class Logger {
    private static Logger instance;

    public static Logger getInstance() {
        if (instance == null) {
            instance = new Logger();
        }
        return instance;
    }

    private Logger() {}

    public void log(String msg) {
        System.out.println("[LOG]: " + msg);
    }
}
