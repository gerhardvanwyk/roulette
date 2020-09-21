package org.example;

public class Application {

    static {
        String path = Roulette.class.getClassLoader()
                .getResource("logging.properties")
                .getFile();
        System.setProperty("java.util.logging.config.file", path);
    }

    public static void main(String[] args) {
        Roulette runner = new Roulette();
        runner.run(args);

    }

}
