package org.paidaki.scrambledwords.app;


import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ScrambledWords {

    private HashMap<Character, ArrayList<String>> words = new HashMap<>();

    public boolean loadDictionary(File filePath) {
        HashMap<Character, ArrayList<String>> map = new HashMap<>();

        try (Stream<String> stream = Files.lines(filePath.toPath())) {
            Iterator<String> iter = stream.iterator();

            if (!iter.hasNext()) return false;

            while (iter.hasNext()) {
                String s = iter.next();

                if (!s.matches("\\p{Lu}+")) return false;

                map.putIfAbsent(s.charAt(0), new ArrayList<>());
                map.get(s.charAt(0)).add(s);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        words.forEach((character, list) -> Collections.sort(list));
        words.clear();
        words.putAll(map);

        return true;
    }

    public boolean searchWord(String word) {
        return !(word == null || word.isEmpty() || !words.keySet().contains(word.charAt(0)))
                && Collections.binarySearch(words.get(word.charAt(0)), word) >= 0;
    }

    public List<GridWord> searchWords(List<GridWord> words) {
        List<GridWord> foundWords = words.stream()
                .filter(word -> searchWord(word.getWord()))
                .collect(Collectors.toCollection(ArrayList::new));

        Set<GridWord> toBeRemoved = new HashSet<>();

        foundWords.forEach(word -> foundWords.forEach(w -> {
            if (word != w && word.getCells().containsAll(w.getCells())) {
                if (word.getCells().size() != w.getCells().size()) {
                    toBeRemoved.add(w);
                } else {
                    if (w.isReverse()) {
                        if (!word.getWord().equals(w.getWord())) word.setReverseWord(w.getWord());
                        toBeRemoved.add(w);
                    }
                }
            }
        }));

        foundWords.removeAll(toBeRemoved);
        Collections.sort(foundWords, GridWord.WORD_COMPARATOR);
        return foundWords;
    }
}
