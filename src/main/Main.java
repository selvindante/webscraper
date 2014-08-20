package main;

import scraper.Scraper;

/**
 * Created by Selvin
 * on 11.08.2014.
 */
public class Main {
    public static void main(String[] args) throws Exception {

        /**
         * Checking of number of input arguments
         * 1) URL or path to txt file
         * 2) Searching words
         * 3) ... 6) Command flags (-v -w -c -e), minimum one.
         */
        if (args.length > 2 && args.length < 7) {
            String[] flags = new String[4];
            System.arraycopy(args, 2, flags, 0, args.length - 2);
            new Scraper(args[0], args[1], flags);
        }
        else System.out.println("Wrong number of input arguments.");
    }
}
