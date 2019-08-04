/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * Copyright (C) July 2019 by Kaj Wortel - all rights reserved               *
 * Contact: kaj.wortel@gmail.com                                             *
 *                                                                           *
 * This file is part of the tools project, which can be found on github:     *
 * https://github.com/Kaj0Wortel/tools                                       *
 *                                                                           *
 * It is allowed to use, (partially) copy and modify this file               *
 * in any way for private use only by using this header.                     *
 * It is not allowed to redistribute any (modifed) versions of this file     *
 * without my permission.                                                    *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

package tools;


/**
 * Provides various sorting algorithms.
 * <br>
 * This class is currently under development. <br>
 * Do not use this class, or use with care and check runtimes! <br>
 * 
 * @todo:
 * <ul>
 *   <li> Consider which alrogithms can be replaced by {@link java.util.Collections#sort},
 *        {@link java.util.Arrays#sort} and {@link tools.data.ArrayTools#sort}.
 *   <li> Add sorting algorithms:
 *     <ul>
 *       <li> Radix sort. </li>
 *       <li> Coctail sort. </li>
 *     </ul>
 * </ul>
 *
 * @version 0.0
 * @author Kaj Wortel
 */
@Deprecated
public class Sort {
    public static final int TYPE_LETTERS_ALPAHBETICAL = 0;
    public final static int TYPE_LETTERS_REVERSE_ALPHABETICAL = 1;
    public final static int TYPE_NUM_ALPHABETICAL = 2;
    public final static int TYPE_NUM_REVERSE_ALPHABETICAL = 3;
    
    public final static int HINT_OTHER_NUM_LETTERS = 4;
    public final static int HINT_OTHER_LETTERS_NUM = 5;
    public final static int HINT_NUM_OTHER_LETTERS = 6;
    public final static int HINT_LETTERS_OTHER_NUM = 7;
    public final static int HINT_NUM_LETTERS_OTHER = 8;
    public final static int HINT_LETTERS_NUM_OTHER = 9;
    
    public final static int HINT_LONG_FIRST = 10;
    public final static int HINT_SHORT_FIRST = 11;
    
    private final static int TYPE_OTHER = 0;
    private final static int TYPE_NUM = 1;
    private final static int TYPE_LETTER_CAPS = 2;
    private final static int TYPE_LETTER_NO_CAPS = 3;
    
    
    /*
     * This is supposed to be a static function only class,
     * so it only uses memory and overhead if you create an instance.
     */
    @Deprecated
    public Sort() { }
    
    /*
     * Uses bubble sort to sort the array.
     * The algorithm is in place, so the input array is modified.
     * The sorted String array is also returned.
     */
    public static String[] bubbleSort(String[] inArray,
                                      int typeLetters, int typeNum,
                                      int sortingHint, int lengthHint) {
        if (typeLetters < 0 || typeLetters > 1)
            throw new IllegalArgumentException
                ("Invallid type for letters: "+ typeLetters);
        if (typeNum < 2 || typeNum > 3)
            throw new IllegalArgumentException
                ("Invallid type for numbers: " + typeNum);
        if (sortingHint < 4 || sortingHint > 9)
            throw new IllegalArgumentException
                ("Invallid sorting hint: " + sortingHint);
        if (lengthHint < 10 || lengthHint > 11)
            throw new IllegalArgumentException
                ("Invallid length hint: " + lengthHint);
        
        synchronized(inArray) {
            String switchElem = null;
            int start = 0;
            int end = inArray.length;
            
            // true -> 0 to end,
            // false -> end to 0
            boolean direction = true;
            boolean changed = true;
            
            while (changed) {
                changed = false;

                //System.out.println(start + ", " + end);
                for (int i = (direction ? start   : end - 2);
                         (direction ? i < end - 1 : i >= start);
                         i = (direction ? i+1 : i-1)) {
                    if (!inOrder(inArray[i], inArray[i + 1], typeLetters,
                                 typeNum, sortingHint, lengthHint)) {
                        switchElem = inArray[i];
                        inArray[i] = inArray[i+1];
                        inArray[i+1] = switchElem;
                        changed = true;
                    }
                }
                
                if (direction) {
                    end--;
                    
                } else {
                    start++;
                }
                
                direction = !direction;
            }
        }
        
        return inArray;
    }
    
    /*
     * Compares the two Strings using the sorting hint.
     * Returns true iff str1 <= str2.
     * False otherwise.
     */
    public static boolean inOrder(String str1, String str2,
                                  int typeLetters, int typeNum,
                                  int sortingHint, int lengthHint) {
        // Check for equality
        if (str1.equals(str2)) {
            return true;
        }
        
        // Calculate the smallest length of the two Strings
        int minSize = str1.length();
        if (minSize > str2.length()) {
            minSize = str2.length();
        }
        
        // Determine the order
        int[] key = new int[2];
        for (int i = 0; i < minSize; i++) {
            key[0] = str1.charAt(i);
            key[1] = str2.charAt(i);
            if (key[0] == key[1]) continue;
            
            // Determine the type
            int[] type = {TYPE_OTHER, TYPE_OTHER};
            for (int j = 0; j < 2; j++) {
                if (key[j] >= '0' && key[j] <= '9') {
                    type[j] = TYPE_NUM;
                    
                } else if (key[j] >= 'a' && key[j] <= 'z') {
                    type[j] = TYPE_LETTER_NO_CAPS;
                    
                } else if (key[j] >= 'A' && key[j] <= 'Z') {
                    type[j] = TYPE_LETTER_CAPS;
                }
            }
            
            
            if (type[0] == type[1]) {
                if (type[0] == TYPE_OTHER) {
                    return  (key[0] <= key[1]);
                    
                } else if (type[0] == TYPE_NUM) {
                    if (typeNum == TYPE_NUM_REVERSE_ALPHABETICAL) {
                        return  (key[0] <= key[1]);
                        
                    } else { // typeNum == TYPE_NUM_ALPHABETICAL
                        return  (key[0] >= key[1]);
                    }
                    
                } else if (type[0] == TYPE_LETTER_CAPS ||
                           type[0] == TYPE_LETTER_NO_CAPS) {
                    if (typeLetters == TYPE_LETTERS_ALPAHBETICAL) {
                        return  (key[0] <= key[1]);
                        
                    } else { // typeLetters == TYPE_LETTERS_REVERSE_ALPHABETICAL
                        return  (key[0] >= key[1]);
                    }
                }
                
            } else {
                if (type[0] == TYPE_NUM) {
                    if (sortingHint == HINT_NUM_OTHER_LETTERS ||
                        sortingHint == HINT_NUM_LETTERS_OTHER) {
                        return true;
                        
                    } else if (sortingHint == HINT_OTHER_LETTERS_NUM ||
                               sortingHint == HINT_LETTERS_OTHER_NUM) {
                        return false;
                        
                    } else if (sortingHint == HINT_LETTERS_NUM_OTHER) {
                        return (type[1] == TYPE_OTHER);
                        
                    } else { // sortingHint == HINT_OTHER_NUM_LETTERS
                        return (type[1] != TYPE_OTHER);
                    }
                    
                } else if (type[0] == TYPE_LETTER_CAPS ||
                           type[0] == TYPE_LETTER_NO_CAPS) {
                    // Note that type[0] != type[1]
                    if (type[1] == TYPE_LETTER_NO_CAPS) { // type[0] == TYPE_LETTER_CAPS
                        if (typeLetters == TYPE_LETTERS_ALPAHBETICAL) {
                            return  (key[0] - 'A' <= key[1] - 'a');
                            
                        } else { // typeLetters == TYPE_LETTERS_REVERSE_ALPHABETICAL
                            return  (key[0] - 'A' >= key[1] - 'a');
                        }
                        
                    } else if (type[1] == TYPE_LETTER_CAPS) { // type[0] == TYPE_LETTER_NO_CAPS
                        if (typeLetters == TYPE_LETTERS_ALPAHBETICAL) {
                            return  (key[0] - 'a' < key[1] - 'A');
                            
                        } else { // typeLetters == TYPE_LETTERS_REVERSE_ALPHABETICAL
                            return  (key[0] - 'a' > key[1] - 'A');
                        }
                        
                    } else {
                        if (sortingHint == HINT_LETTERS_OTHER_NUM ||
                            sortingHint == HINT_LETTERS_NUM_OTHER) {
                            return true;
                            
                        } else if (sortingHint == HINT_OTHER_NUM_LETTERS ||
                                   sortingHint == HINT_NUM_OTHER_LETTERS) {
                            return false;
                            
                        } else if (sortingHint == HINT_OTHER_LETTERS_NUM) {
                            return (type[1] == TYPE_NUM);
                            
                        } else { // sortingHint == HINT_NUM_LETTERS_OTHER
                            return (type[1] == TYPE_OTHER);
                        }
                    }
                    
                } else { // type[0] == TYPE_OTHER
                    if (sortingHint == HINT_OTHER_NUM_LETTERS ||
                        sortingHint == HINT_OTHER_LETTERS_NUM) {
                        return true;
                        
                    } else if (sortingHint == HINT_NUM_LETTERS_OTHER ||
                               sortingHint == HINT_LETTERS_NUM_OTHER) {
                        return false;
                        
                    } else if (sortingHint == HINT_NUM_OTHER_LETTERS) {
                        return (type[1] != TYPE_NUM);
                        
                    } else { // sortingHint == HINT_LETTERS_OTHER_NUM
                        return (type[1] == TYPE_NUM);
                    }
                }
            }
        }
        
        if (lengthHint == HINT_LONG_FIRST) {
            return (str1.length() >= str2.length());
            
        } else { // lengthHint == HINT_SHORT_FIRST
            return (str1.length() <= str2.length());
        }
    }
    
    /*
    public static void main(String[] args) {
        //String[] array = new String[] {"a", "b", "A", "B", "A", "1", "2", "#", "Ab", "AB", "Aa"};
        //String[] array = new String[] {"aa", "a", "AA", "A", "bb", "b", "BB", "B", "15", "14", "13", "13", "12", "11", "2", "##", "$$"};
        //String[] array = new String[] {"1", "#"};
        /*
        String[] array = new String[10000];
        int min = 48;
        int max = 122 - min + 1;
        for (int i = 0; i < array.length; i++) {
            array[i] = "" + ((char) (min + Math.floorMod(i, max)))
                + ((char) (min + Math.floorMod(i / max, max)));
        }*//*
        
        String[] array = {"b", "a", "12", "11", "$"};
        String[] sorted = null;
        long time1 = System.currentTimeMillis();
        for (int i = 0; i < 1; i++) {
            sorted = bubbleSort(array, TYPE_LETTERS_ALPAHBETICAL, TYPE_NUM_REVERSE_ALPHABETICAL, HINT_OTHER_NUM_LETTERS, HINT_SHORT_FIRST);
        }
        long time2 = System.currentTimeMillis();
        System.out.println("Time taken: " + (time2 - time1));

        for (int i = 0; i < array.length; i++) {
            System.out.println(array[i]);
        }
    }*/
    
    
}
