package DesignPatterns.Singleton;

public class Main {
    public static void main(String[] args) {
        // Create a singleton instance of the Logger
        Logger logger = Logger.getInstance();

        // Log some messages
        logger.log("Application started");
        logger.log("Performing some operations");
        logger.log("Application finished");

        // Print the log messages
        System.out.println("Log Messages:");
        String message = logger.getLog() ;
            System.out.println(message);
        // Note: The Logger class is a singleton, so all log messages are stored in the same instance.
    }
}
