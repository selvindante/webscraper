package scraper;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Selvin
 * on 12.08.2014.
 */
public class Scraper {
    boolean vFlag = false, wFlag = false, cFlag = false, eFlag = false;
    private List<String> urlList = new ArrayList<>();
    private List<Word> wordList = new ArrayList<>();
    private int totalLength = 0;
    private long totalTimeout = 0;
    private int sentLength = 200;

    public Scraper(String url, String words, String[] flags) {
        setFlags(flags);
        fillUrlList(url);
        fillWordList(words);
        if(vFlag || wFlag || cFlag || eFlag) scrape();
    }

    // Method for setting flags
    private void setFlags(String[] flags) {
        for(String flag: flags) {
            if(flag != null) {
                switch (flag) {
                    case "-v":
                        vFlag = true;
                        System.out.println("Flag \"-v\" is on.");
                        break;
                    case "-w":
                        wFlag = true;
                        System.out.println("Flag \"-w\" is on.");
                        break;
                    case "-c":
                        cFlag = true;
                        System.out.println("Flag \"-c\" is on.");
                        break;
                    case "-e":
                        eFlag = true;
                        System.out.println("Flag \"-e\" is on.");
                        break;
                    default:
                        break;
                }
            }
        }
    }

    // Method for filling of URL list
    private void fillUrlList(String path) {
        String partUrl = "http://";
        FileReader fr;
        BufferedReader br;
        // if URL...
        if(path.length() > partUrl.length() && path.contains(partUrl)) {
            urlList.add(path);
        }
        // if path to txt file
        else {
            try {
                fr = new FileReader(path);
                br = new BufferedReader(fr);
                String urlLine = br.readLine();
                while(urlLine != null) {
                    urlList.add(urlLine);
                    urlLine = br.readLine();
                }
                br.close();
            } catch (IOException e) {
                System.out.println("Url or path to the url list is not recognized.");
                e.printStackTrace();
            }
        }
    }

    // Method for filling of word list
    private void fillWordList(String words) {
        String[] mas = words.split(",");
        for(String str: mas) {
            wordList.add(new Word(str));
        }
    }

    private void scrape() {
        Document doc;
        String text = "";
        for(String url: urlList) {
            System.out.println("Data processing results for resource: " + url);
            long timeout = System.currentTimeMillis();
            // parsing of resource with jsoup html-parser
            try {
                doc = Jsoup.connect(url).get();
                text = doc.text();
                totalLength += text.length();
                //System.out.println(text);
            } catch (IOException e) {
                e.printStackTrace();
            }
            // Info for wFlag
            if(wFlag) {
                for (Word word : wordList) {
                    int count = check(text, word);
                    word.setCount(word.getCount() + count);
                    System.out.println("Count number of word \"" + word.getValue() + "\" in this resource is: " + count);
                }
            }
            // Info for vFlag
            if(vFlag) {
                long urlTimeout = System.currentTimeMillis() - timeout;
                totalTimeout += urlTimeout;
                System.out.println("Time spend on data scraping and data processing in this resource is: " + urlTimeout + " ms");
            }
            // Info for cFlag
            if(cFlag) System.out.println("Count number of characters in this resource is: " + text.length());
            // Info for eFlag
            if(eFlag) {
                System.out.println("Extracted sentences from all resources:");
                for (Word word : wordList) {
                    System.out.println("----------------------------------------------------------------------");
                    System.out.println("Sentences for word \"" + word.getValue() + "\":");
                    for(String sentence : word.getSentences()) {
                        System.out.println(sentence);
                    }
                    word.clearSent();
                }
            }
            System.out.println("======================================================================\n");
        }
        System.out.println("Total statistic for all resources:");
        // Total info for wFlag
        if(wFlag) {
            for (Word word : wordList) {
                System.out.println("Count number of word \"" + word.getValue() + "\" in all resources is: " + word.getCount());
            }
        }
        // Total info for vFlag
        if(vFlag) {
            System.out.println("Time spend on data scraping and data processing in all resources is: " + totalTimeout + " ms");
        }
        // Total info for cFlag
        if(cFlag) {
            System.out.println("Count number of characters in all resources is: " + totalLength);
        }
    }

    // Method to search for words
    private int check(String text, Word word) {
        int count = 0;
        for (int i = 0; i <= text.length() - word.getValue().length(); i++) {
            if (text.substring(i, i + word.getValue().length()).equals(word.getValue())) {
                count++;
                // Checking of source text bounds
                int startSent = i < sentLength/2 ? 0 : i - sentLength/2;
                int endSent = i > text.length() - sentLength/2 ? text.length() : i + sentLength/2;
                word.addSentence("..." + text.substring(startSent, endSent) + "...");
            }
        }
        /*Pattern p = Pattern.compile("((\\d+\\.\\s*)*[А-ЯA-Z\\@]((т.п.|т.д.|пр.|т.е.|т.о.)|[^?!.\\(]|\\([^\\)]*\\))*[.?!])");
        Matcher m = p.matcher(text);
        while (m.find()){
            if(m.group().contains(word.getValue())) word.addSentence(m.group());
        }*/
        return count;
    }
}
