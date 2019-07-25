import java.io.*;
import java.lang.reflect.Array;
import java.math.*;
import java.security.*;
import java.text.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.regex.*;

public class Solution {
    private static void printBoard(List<List<Character>> board) {
        for (List<Character> line: board) {
            for (Character cell: line) {
                System.out.print(cell);
            }
            System.out.print("\n");
        }

    }

    private static List<List<Integer>> getPossibleLocations(List<List<Character>> board, String word) {
        int length = word.length();
        List<List<Integer>> locations = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {

                boolean properSlotH = true;
                boolean properSlotV = true;

                for (int k = 0; k < length; k++) {
                    if (j < 10 - length + 1) { // Horizontal
                        if (!Arrays.asList('-',word.charAt(k)).contains(board.get(i).get(j+k)) ) {
                            properSlotH = false;
                        }
                    }
                    if (i < 10 - length + 1) { // Vertical
                        if (!Arrays.asList('-',word.charAt(k)).contains(board.get(i+k).get(j))) {
                            properSlotV = false;
                        }
                    }
                }

                if (properSlotH && j<10-length+1) {
                    locations.add(Arrays.asList(i,j,0));
                }
                if (properSlotV && i<10-length+1) {
                    locations.add(Arrays.asList(i,j,1));
                }

            }
        }

        return locations;
    }

    private static void move(List<List<Character>> board, String word, List<Integer> location) {
        int i = location.get(0);
        int j = location.get(1);
        int axis = location.get(2);

        if (axis == 0) {
            for (int k = 0; k < word.length(); k++) {
                List<Character> element = board.get(i);
                element.set(j + k, word.charAt(k));
            }
        } else {
            for (int k = 0; k < word.length(); k++) {
                List<Character> element = board.get(i + k);
                element.set(j, word.charAt(k));
            }
        }

    }
    private static void rollback(List<List<Character>> board, String word, List<Integer> location) {
        int i = location.get(0);
        int j = location.get(1);
        int axis = location.get(2);

        if (axis == 0) {
            for (int k = 0; k < word.length(); k++) {
                List<Character> element = board.get(i);
                element.set(j + k, '-');
            }
        } else {
            for (int k = 0; k < word.length(); k++) {
                List<Character> element = board.get(i + k);
                element.set(j, '-');
            }
        }

    }


    // Complete the crosswordPuzzle function below.
    private static List<List<Character>> crosswordPuzzle(List<List<Character>> board, Stack<String> words) {
        if(words.size() == 0) {
            if(!finished) {
                printBoard(board);
            }
            finished = true;
            return board;
        }

        String word = words.pop();
        List<List<Integer>> locations = getPossibleLocations(board,word);

        for (List<Integer> location: locations) {
            if (!finished) {
                move(board, word, location);
            }
            if (!finished) {
                crosswordPuzzle(board, words);
            }
            if (!finished) {
                rollback(board,word,location);
            }
        }
        if (!finished) {
            words.push(word);
        }
        return board;
    }

    private static boolean finished = false;
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) throws IOException {
        String fileName;
        if (System.getenv("OUTPUT_PATH") == null) {
            fileName = "/tmp/output.txt";
        } else {
            fileName = System.getenv("OUTPUT_PATH");
        }
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(fileName));

        String[] crossword = new String[10];

        for (int i = 0; i < 10; i++) {
            String crosswordItem = scanner.nextLine();
            crossword[i] = crosswordItem;
        }

        String words = scanner.nextLine();
        Stack<String> stack = Split(words,";");
        List<List<Character>> board = toList(crossword);
        List<List<Character>> result = crosswordPuzzle(board, stack);
        String[] output = toArray(result);

        for (int i = 0; i < output.length; i++) {
            bufferedWriter.write(output[i]);

            if (i != output.length - 1) {
                bufferedWriter.write("\n");
            }
        }

        bufferedWriter.newLine();

        bufferedWriter.close();

        scanner.close();
    }
    
    private static Stack<String> Split(String words, String separator) {
        Stack<String> result = new Stack<>();
        String[] explodedWords = words.split(";");
        for (String word: explodedWords) {
            result.push(word);
        }
        return result;
    }

    private static String[] toArray(List<List<Character>> board) {
        String[] output = new String[10];
        int index = 0;
        for (List<Character> line: board) {
            StringBuilder sb = new StringBuilder();
            for (Character cell: line) {
                sb.append(cell);
            }
            String outputLine = sb.toString();
            output[index] = outputLine;
            index++;
        }

        return output;
    }

    private static List<List<Character>> toList(String[] crossword) {
        List<List<Character>> board = new ArrayList<>();
        for (String word: crossword) {
            List<Character> charList = new ArrayList<>();
            for (char c : word.toCharArray()) {
                charList.add(c);
            }
            board.add(charList);
        }
        return board;
    }

    private static List<Character> asList(final String string) {
        return new AbstractList<Character>() {
            public int size() { return string.length(); }
            public Character get(int index) { return string.charAt(index); }
        };
    }
}
