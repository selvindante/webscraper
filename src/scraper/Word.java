package scraper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Selvin
 * on 12.08.2014.
 */
public class Word {
    private String value;
    private int count = 0;
    private List<String> sentences = new ArrayList<>();

    public Word(String word) {
        this.value = word;
    }

    public String getValue() {
        return value;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<String> getSentences() {
        return sentences;
    }

    public void addSentence(String sentence) {
        this.sentences.add(sentence);
    }

    public void clearSent() {
        sentences.clear();
    }
}
