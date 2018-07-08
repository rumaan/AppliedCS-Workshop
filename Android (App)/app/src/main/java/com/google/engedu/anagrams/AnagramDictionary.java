package com.google.engedu.anagrams;

import android.util.Log;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;

public class AnagramDictionary {

  private static final int MIN_NUM_ANAGRAMS = 5;
  private static final int DEFAULT_WORD_LENGTH = 4;
      // FIXME: word of length 3 is crashing the app. 4 and above works fine :) .
  private static final int MAX_WORD_LENGTH = 7;

  // Random number generator
  private Random random = new Random();

  private ArrayList<String> hashKeys = new ArrayList<>();
  private HashSet<String> wordSet = new HashSet<>();

  private HashMap<String, ArrayList<String>> lettersToWord = new HashMap<>();
  private HashMap<Integer, ArrayList<String>> sizeToWords = new HashMap<>();

  private int wordLength = DEFAULT_WORD_LENGTH;

  public AnagramDictionary(InputStream wordListStream) throws IOException {
    BufferedReader in = new BufferedReader(new InputStreamReader(wordListStream));
    String line;
    while ((line = in.readLine()) != null) {
      String word = line.trim();
      wordSet.add(word);
      int currentWordLength = word.length();
      ArrayList<String> temp = new ArrayList<String>();
      if (sizeToWords.containsKey(currentWordLength)) {
        temp = sizeToWords.get(currentWordLength);
        temp.add(word);
        sizeToWords.put(currentWordLength, temp);
      } else {
        temp.add(word);
        sizeToWords.put(currentWordLength, temp);
      }
      String sortedWord = sortLetters(word);
      ArrayList<String> values = new ArrayList<String>();
      if (lettersToWord.containsKey(sortedWord)) {
        values = lettersToWord.get(sortedWord);
        values.add(word);
        lettersToWord.put(sortedWord, values);
      } else {
        hashKeys.add(sortedWord);
        values.add(word);
        lettersToWord.put(sortedWord, values);
      }
    }
  }

  public boolean isGoodWord(String word, String base) {
    if (wordSet.contains(word) && !word.contains(base)) {
      return true;
    }
    return false;
  }

  private String sortLetters(String word) {
    char[] chararray = word.toCharArray();
    Arrays.sort(chararray);
    return new String(chararray);
  }

  public ArrayList<String> getAnagrams(String targetWord) {
    //ArrayList<String> result = new ArrayList<String>();
    //for(String dictWord : wordList) {
    //    String sortedDictWord = sortLetters(dictWord);
    //    if (sortedDictWord.length() == sortedTargetWord.length() && sortedDictWord == sortedTargetWord)
    //    {
    //        Log.d("result arraylist", dictWord);
    //        result.add(dictWord);
    //    }
    // }
    String sortedTargetWord = sortLetters(targetWord);
    ArrayList<String> result = lettersToWord.get(sortedTargetWord);
    Log.d("getAnagrams", "ArrayList Result Returned");
    return result;
  }

  public ArrayList<String> getAnagramsWithOneMoreLetter(String currentWord) {
    ArrayList<String> result = new ArrayList<>();
    ArrayList<String> temp;
    for (int i = 0; i < 26; i++) {
      /* Add characters to current word */
      String newWord = currentWord + (char) (97 + i);
      String sortedNewWord = sortLetters(newWord);
      if (lettersToWord.containsKey(sortedNewWord)) {
        temp = lettersToWord.get(sortedNewWord);
        for (String tword : temp) {
          // Find non Substring words, redundant but required
          if (!tword.contains(currentWord)) {
            result.add(tword);
          }
        }
      }
    }
    return result;
  }

  public String pickGoodStarterWord() {
    ArrayList<String> minAnagramWords = new ArrayList<String>();
    ArrayList<String> temp;
    for (String key : sizeToWords.get(wordLength)) {
      if ((lettersToWord.get(sortLetters(key))).size() >= MIN_NUM_ANAGRAMS) {
        temp = lettersToWord.get(sortLetters(key));
        minAnagramWords.addAll(temp);
      }
    }
    /* Randomly pick a word from the Anagrams list */
    String randomString = minAnagramWords.get(random.nextInt(minAnagramWords.size()));
    if (wordLength <= MAX_WORD_LENGTH) {
      wordLength++;
    }
    return randomString;
  }
}