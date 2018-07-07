package AppliedCS;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;

import static AppliedCS.AnagramsTest.Helper.print;

/* Java CLI program to test the working of Anagrams */
public class AnagramsTest {
  public static void main(String[] args) {
    try {
      File file = new File(
          "/Users/rumaankhalander/IdeaProjects/Anagrams/src/AppliedCS/words.txt");
      InputStream inputStream = new FileInputStream(file);

      AnagramDictionary anagramDictionary = new AnagramDictionary(inputStream);
      String currentWord = anagramDictionary.getInitialWord();
      print("Word: " + currentWord);
      print("\nPossible Anagrams: \n");
      ArrayList<String> anagrams = anagramDictionary.getAnagramsWithOneMoreLetter(currentWord);
      for (String anagram : anagrams) {
        print(anagram + "\n");
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /* helper methods class */
  static class Helper {
    public static void print(String s) {
      System.out.print(s);
    }
  }
}

class AnagramDictionary {

  /* Default word length Anagrams */
  private static final int DEFAULT_WORD_LENGTH = 4;

  /* Maximum word length of Anagrams */
  private static final int MAXIMUM_WORD_LENGTH = 7;

  /* Minimum Number of Anagrams */
  private static final int MINIMUM_ANAGRAMS = 5;

  /* Variable word length */
  private static int wordLength = DEFAULT_WORD_LENGTH;

  /* HashSet of all the words obtained from the file words.txt*/
  private HashSet<String> wordSet = new HashSet<>();

  /* HashMap of String --> {String, String ...} */
  private HashMap<String, ArrayList<String>> letterToWords = new HashMap<>();

  /* HashMap of Int --> {String, String ...} */
  private HashMap<Integer, ArrayList<String>> sizeToWords = new HashMap<>();

  AnagramDictionary(InputStream inputStream) throws IOException {
    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
    /* Read lines from the file */
    String line;
    while ((line = bufferedReader.readLine()) != null) {
      /* Get the word from each line by trimming trailing spaces */
      String word = line.trim();

      /* Add the word to the words HashSet */
      wordSet.add(word);

      int wordLength = word.length();

      /* If Int --> {String, String, ...} hash map
       * contains the current word length as the key
       * */
      ArrayList<String> temp = new ArrayList<>();
      if (sizeToWords.containsKey(wordLength)) {
        /* Add the word to HashMap */
        temp = sizeToWords.get(wordLength);
        temp.add(word);
        sizeToWords.put(wordLength, temp);
      } else {
        temp.add(word);
        sizeToWords.put(wordLength, temp);
      }

      /* Sort the word by characters */
      String sortedWord = sortByLetters(word);

      /* If String --> {String, String, ...} hash map
       * contains the current word length as the key
       * */
      ArrayList<String> values = new ArrayList<>();
      if (letterToWords.containsKey(sortedWord)) {
        /* Add the word to HashMap */
        values = letterToWords.get(sortedWord);
        /* Add the current word to list and not sortedWord */
        values.add(word);
        letterToWords.put(sortedWord, values);
      } else {
        values.add(word);
        letterToWords.put(sortedWord, values);
      }
    }
  }

  public ArrayList<String> getAnagramsWithOneMoreLetter(String currentWord) {
    ArrayList<String> result = new ArrayList<>();
    ArrayList<String> temp;
    for (int i = 0; i < 26; i++) {
      /* add character to new word [a-z] */
      String newWord = currentWord + (char) (97 + i);

      /* Sort the word based on characters */
      String sortedWord = sortByLetters(newWord);

      if (letterToWords.containsKey(sortedWord)) {
        temp = letterToWords.get(sortedWord);
        for (String word : temp) {
          /* Avoid Substrings. */
          if (!word.contains(currentWord)) {
            result.add(word);
          }
        }
      }
    }

    return result;
  }

  public String getInitialWord() {
    ArrayList<String> minimumAnagramWordsList = new ArrayList<>();
    ArrayList<String> temp;
    /* Loop through strings in the list obtained from Int --> {String, String, ...} HashMap. */
    for (String key : sizeToWords.get(wordLength)) {
      /* We want at least 5 Anagram words */
      if (letterToWords.get(sortByLetters(key)).size() >= MINIMUM_ANAGRAMS) {
        temp = letterToWords.get(sortByLetters(key));
        for (String word : temp) {
          /* Add anagrams with word length to this list */
          minimumAnagramWordsList.add(word);
        }
      }
    }

    /* Randomly pick one word from Anagrams list */
    int randomIndex = new Random().nextInt(minimumAnagramWordsList.size());
    String randomWord = minimumAnagramWordsList.get(randomIndex);

    /* Change the Anagram Challenge word length at every play till max. length is reached */
    if (wordLength <= MAXIMUM_WORD_LENGTH) {
      wordLength++;
    }

    return randomWord;
  }

  /**
   * @param word Word that is be checked.
   * @param baseWord Word that will be checked against.
   * @return true if word isn't the substring of itself and present in words list else false
   */
  public boolean isGoodWord(String word, String baseWord) {
    return wordSet.contains(word) && !word.contains(baseWord);
  }

  /**
   * @param word String to be Sorted.
   * Sorts the string by Characters.
   * @return Sorted String.
   */
  private String sortByLetters(String word) {
    char[] charArray = word.toCharArray();
    Arrays.sort(charArray);
    return new String(charArray);
  }
}
