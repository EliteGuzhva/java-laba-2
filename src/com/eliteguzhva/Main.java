package com.eliteguzhva;

public class Main {
    public static void main(String[] args) {
        // Get filename from command line arguments
        String filename = "";

        if (args.length == 0) {
            System.out.println("Please provide a filename");
            System.exit(0);
        } else {
            if (args[0].endsWith(".ini")) {
                filename = args[0];
            } else {
                System.out.println("File should have .ini extension");
                System.exit(0);
            }
        }

        // Init parser
        IniParser parser = new IniParser();

        boolean didRead = parser.read(filename);
        if (!didRead)
            System.exit(0);

        parser.printContent();
    }
}
