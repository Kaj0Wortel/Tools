/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * Copyright (C) 2018 by Kaj Wortel - all rights reserved                *
 * Contact: <kaj.wortel@gmail.com>                                       *
 *                                                                       *
 * This file is part of the tools project.                               *
 *                                                                       *
 * It is allowed to use, (partially) copy and modify this file           *
 * in any way for private use only.                                      *
 * It is not allowed to redistribute any (modifed) versions of this file *
 * without my permission.                                                *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

package tools;


// Java packages
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.io.File;

import java.lang.reflect.Array;
//import java.lang.reflect.GenericDeclaration;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class MultiTool {
    
    /* 
     * Simple encoding algorithm.
     * Reeeeaaally old. No idea what it does.
     */
    public static String[] encode(int[][] input) throws IllegalArgumentException {
        return encoder(input);
    }
    
    private static String[] encoder(int[][] input) throws IllegalArgumentException {
        String[] output = new String[input.length];
        String line;
        for (int row = 0; row < input.length; row++) {
            line = "";
            for (int col = 0; col < input[row].length; col++) {
                line += Integer.toHexString(input[row][col]);
            }
            output[row] = line;
        }
        return output;
    }
    
    
    /* 
     * Simple decoding algorithm.
     * Reeeeaaally old. No idea what it does.
     */
    public static int[][] decode(String[] input) throws IllegalArgumentException {
        return decoder(input);
    }
    public static int[][] decode(String input) throws IllegalArgumentException {
        String[] inputArray = input.split("#");
        return decoder(inputArray);
    }
    
    private static int[][] decoder(String[] input) throws IllegalArgumentException {
        // check whether the input is not null
        if (input == null) {
            throw new IllegalArgumentException();
        }
        
        // It is known here that the input != null. It is not known whether
        // every row has the same length, but this wil be checked later.
        int[][] output = new int[input.length][input[0].length()];
        
        for (int row = 0; row < input.length; row++) {
            // checks whether every row has the same length
            if (input[row] == null
                    ||
                input[0].length() != input[row].length()) {
                throw new IllegalArgumentException();
            }
            // the decoding
            for (int col = 0; col < input[row].length(); col++) {
                String letter = "0x" + input[row].charAt(col);
                output[row][col] = Long.decode(letter).intValue();
            }
        }
        return output;
    }
    
    /* 
     * Converts a decimal integer to a 32 based number String.
     * 
     * @param dHex a decimal number.
     * @return a String containing the 32 based representation of {@code dHex}.
     */
    public static String dHexToString(int dHex) throws NumberFormatException {
        
        if (dHex < 0) throw new NumberFormatException();
        
        String answer = "";
        int heighestDHexNumber = 1;
        
        while (Math.pow(32, heighestDHexNumber) - 1 < dHex) {
            heighestDHexNumber += 1;
        }
        
        for (int counter = 0; counter < heighestDHexNumber; counter++) {
            int part = (int)((double)(dHex) / Math.pow(32, heighestDHexNumber - (counter+1)));
            dHex -= part * Math.pow(32, heighestDHexNumber - (counter+1));
            answer += singleDHexToString(part);
        }
        
        return answer;
    }
    
    /* 
     * Converts a 32 based number String to a decimal integer.
     * 
     * @param dHex 32 based number String.
     * @return an integer represented by the 32 based number {@code dHex}.
     */
    public static int stringToDHex(String dHex) throws NumberFormatException {
        
        if (dHex == null) throw new NumberFormatException();
        
        dHex = dHex.toUpperCase();
        int answer = 0;
        int length = dHex.length();
        if (length == 0) throw new NumberFormatException();
        
        for (int counter = 0; counter < length; counter++) {
            answer += Math.pow(32, length - (counter+1)) * singleStringToDHex(dHex.charAt(counter));
        }
        return answer;
    }
    
    /* 
     * Converts a single decimal number to a hexadecimal char.
     * 
     * @param dHex a decimal number (0 <= dHex < 32).
     * @return the 32 based representation of {@code dHex}.
     */
    private static char singleDHexToString(int dHex) throws NumberFormatException {
        if (dHex == 0) return '0';
        else if (dHex == 1) return '1';
        else if (dHex == 2) return '2';
        else if (dHex == 3) return '3';
        else if (dHex == 4) return '4';
        else if (dHex == 5) return '5';
        else if (dHex == 6) return '6';
        else if (dHex == 7) return '7';
        else if (dHex == 8) return '8';
        else if (dHex == 9) return '9';
        else if (dHex == 10) return 'A';
        else if (dHex == 11) return 'B';
        else if (dHex == 12) return 'C';
        else if (dHex == 13) return 'D';
        else if (dHex == 14) return 'E';
        else if (dHex == 15) return 'F';
        else if (dHex == 16) return 'G';
        else if (dHex == 17) return 'H';
        else if (dHex == 18) return 'I';
        else if (dHex == 19) return 'J';
        else if (dHex == 20) return 'K';
        else if (dHex == 21) return 'L';
        else if (dHex == 22) return 'M';
        else if (dHex == 23) return 'N';
        else if (dHex == 24) return 'O';
        else if (dHex == 25) return 'P';
        else if (dHex == 26) return 'Q';
        else if (dHex == 27) return 'R';
        else if (dHex == 28) return 'S';
        else if (dHex == 29) return 'T';
        else if (dHex == 30) return 'U';
        else if (dHex == 31) return 'V';
        else throw new NumberFormatException();
    }
    
    /* 
     * Converts a single 32 based char to a decimal number.
     * 
     * @param dHex a 32 based number ('0' <= dHex <= '9' || 'A' <= dHex <= 'V').
     * @return the decimal representation of {@code dHex}.
     */
    private static int singleStringToDHex(char dHex)  throws NumberFormatException {
        if (dHex == '0') return 0;
        else if (dHex == '1') return 1;
        else if (dHex == '2') return 2;
        else if (dHex == '3') return 3;
        else if (dHex == '4') return 4;
        else if (dHex == '5') return 5;
        else if (dHex == '6') return 6;
        else if (dHex == '7') return 7;
        else if (dHex == '8') return 8;
        else if (dHex == '9') return 9;
        else if (dHex == 'A') return 10;
        else if (dHex == 'B') return 11;
        else if (dHex == 'C') return 12;
        else if (dHex == 'D') return 13;
        else if (dHex == 'E') return 14;
        else if (dHex == 'F') return 15;
        else if (dHex == 'G') return 16;
        else if (dHex == 'H') return 17;
        else if (dHex == 'I') return 18;
        else if (dHex == 'J') return 19;
        else if (dHex == 'K') return 20;
        else if (dHex == 'L') return 21;
        else if (dHex == 'M') return 22;
        else if (dHex == 'N') return 23;
        else if (dHex == 'O') return 24;
        else if (dHex == 'P') return 25;
        else if (dHex == 'Q') return 26;
        else if (dHex == 'R') return 27;
        else if (dHex == 'S') return 28;
        else if (dHex == 'T') return 29;
        else if (dHex == 'U') return 30;
        else if (dHex == 'V') return 31;
        else throw new NumberFormatException();
    }
    
    
    
    
    /* 
     * Lists all files in a dir.
     * @param rootDir the root dir from which the files will be listed.
     * @param listDirs whether the directories should be listed or not.
     * @param pathSoFar the path that has been traversed so far.
     * 
     * @return an ArrayList containing an array of File objects of length 2 for which holds:
     *     - The first element contains the full path of the file.
     *     - The second element contains the path of the file relative to the given rootDir
     *       (excl. the rootdir and the file itself).
     * 
     * @throws IllegalArgumentException if the root dir is not a directory.
     * @throws IllegalStateException if a file found in a certain directory, is not located in that directory.
     * 
     * Furthermore is guarenteed that:
     * - For every directory that all its children (sub-dirs included) are listed
     *     directly below its own entry.
     * - When the files X in directory dirX and Y NOT in dirX are listed consecutively,
     *     then all children (including sub-children) of dirX are listed.
     * - No other assumptions regarding file-order can be made on the output.
     * 
     * Note: ONLY use the THIRD function when you know what you are doing!
     */
    public static ArrayList<File[]> listFilesAndPathsFromRootDir(File rootDir)
        throws IllegalArgumentException, IllegalStateException {
        return listFilesAndPathsFromRootDir(rootDir, "", true);
    }
    
    public static ArrayList<File[]> listFilesAndPathsFromRootDir(File rootDir, boolean listDirs)
        throws IllegalArgumentException, IllegalStateException {
        return listFilesAndPathsFromRootDir(rootDir, "", listDirs);
    }
    
    public static ArrayList<File[]> listFilesAndPathsFromRootDir(File rootDir, String pathSoFar, boolean listDirs)
        throws IllegalArgumentException, IllegalStateException {
        
        if (rootDir.isFile()) {
            new IllegalArgumentException("The file \"" + rootDir.getPath() + "\" is no dir.");
        }
        
        pathSoFar = (pathSoFar == null ? "" : pathSoFar);
        
        ArrayList<File[]> output = new ArrayList<File[]>();
        String root = rootDir.getPath();
        File[] listOfFiles = rootDir.listFiles();
        
        if (listDirs && !pathSoFar.equals("")) {
            output.add(new File[] {rootDir, new File(pathSoFar)});
        }
        
        for (int i = 0; i < listOfFiles.length; i++) {
            if (!root.equals(listOfFiles[i].getPath().substring(0, root.length()))) {
                throw new IllegalStateException("File \"" + listOfFiles[i] + "\" could not be found in dir \"" + root + "\"");
            }
            
            if (listOfFiles[i].isFile()) {
                output.add(new File[] {listOfFiles[i],
                    new File(pathSoFar + listOfFiles[i].getParent().substring(root.length()))});
                
            } else {
                output.addAll
                    (listFilesAndPathsFromRootDir
                         (listOfFiles[i], pathSoFar + listOfFiles[i].getPath().substring(root.length()), listDirs));
            }
        }
        
        return output;
    }
    
    
    /* 
     * Checks if a given number is in the array.
     * 
     * @param array array to look in.
     * @param number number to look for.
     * @return true iff the number is in the array.
     */
    public static boolean isInArray(int[] array, int number) {
        for (int i = array.length - 1; i >= 0; i--) {
            if (array[i] == number) {
                return true;
            }
        }
        
        return false;
    }
    
    /* 
     * Checks if a given value is in the array.
     * Generic version of the function above.
     * 
     * @param array array to look in.
     * @param number number to look for.
     * @return true iff the number is in the array.
     */
    public static <T> boolean isInArray(T[] array, T value) {
        for (T entry : array) {
            if (entry.equals(value)) return true;
        }
        
        return false;
    }
    
    /* 
     * Converts an ArrayList to an array.
     * 
     * @param array the array to be converted.
     * @return an ArrayList containing every element of {@code array} and in the same order.
     */
    public static <T> ArrayList<T> toArrayList(T[] array) {
        if (array == null) return null;
        
        ArrayList<T> list = new ArrayList<T>(array.length);
        
        for (int i = 0; i < array.length; i++) {
            list.add(array[i]);
        }
        
        return list;
    }
    
    
    /* 
     * Converts a List to an array.
     * 
     * @param list the input list
     * @param classValue the input/output class type
     * @param start the first element that will be put in the array.
     * @param end the last element that will NOT be put in the array.
     * @return the elements from the output array in the same order as in the input List.
     *     returns null iff the given list or class are null.
     * @throws IllegalArgumentException iff start < end.
     */
    @SuppressWarnings("unchecked")
    public static <A, B extends A> A[] listToArray(List<B> list, Class<B> classValue) {
        return listToArray(list, classValue, 0);
    }
    
    @SuppressWarnings("unchecked")
    public static <A, B extends A> A[] listToArray(List<B> list, Class<B> classValue, int start) {
        return listToArray(list, classValue, start, list.size());
    }
    
    @SuppressWarnings("unchecked")
    public static <A, B extends A> A[] listToArray(List<B> list, Class<B> classValue, int start, int end)
        throws IllegalArgumentException {
        if (list == null || classValue == null) return null;
        if (start >= end) throw new IllegalArgumentException("start(" + start + ") > end(" + end + ").");
        
        A[] array = (A[]) Array.newInstance(classValue, end - start);
        
        for (int i = start; i < list.size() && i < end; i++) {
            array[i - start] = list.get(i);
        }
        
        return (A[]) array;
    }
    
    /* 
     * Converts any array to an ArrayList
     * 
     * @param array the input array
     * @param classValue the input/output class type
     * @return the elements from the output ArrayList in the same order as in the input array.
     *     Returns null iff the given array or class are null.
     * 
     * WARNING! THIS FUNCTION HAS NOT BEEN EXTENSIVLY TESTED!
     * If you get class cast exceptions (e.g. cannot convert/cast Object[] to XXX[]), here's you problem.
     */
    @SuppressWarnings("unchecked")
    public static <A, B extends A> ArrayList<A> arrayToArrayList(B[] array, Class<B> classValue) {
        if (array == null) return null;
        
        List<A> list = new ArrayList<A>(array.length);
        
        for (int i = 0; i < array.length; i++) {
            list.add((A) array[i]);
        }
        
        return (ArrayList<A>) list;
    }
    
    /* 
     * Makes a copy of an arrayList.
     * 
     * @param list ArrayList to copy
     * WARNING! THIS FUNCTION HAS NOT BEEN EXTENSIVLY TESTED!
     */
    public static <T> ArrayList<T> copyArrayList(ArrayList<T> list) {
        if (list == null) return null;
        
        ArrayList<T> newList = new ArrayList<T>(list.size());
        
        for (int i = 0; i < list.size(); i++) {
            newList.add(list.get(i));
        }
        
        return newList;
    }
    
    /* 
     * Makes a copy of an array.
     * 
     * @param array array to copy.
     * WARNING! THIS FUNCTION HAS NOT BEEN EXTENSIVLY TESTED!
     */
    @SuppressWarnings("unchecked")
    public static <T> T[] copyArray(T[] array) {
        if (array == null) return null;
        
        T[] newArray = (T[]) Array.newInstance(array.getClass().getComponentType(), array.length);
        
        for (int i = 0; i < array.length; i++) {
            newArray[i] = (T) array[i];
        }
        
        return (T[]) newArray;
    }
    
    /*
     * Sleeps the current thread.
     * Mainly used to avoid the annoying catch statement.
     * 
     * @param time time in ms that the thread sleeps.
     */
    public static void sleepThread(long time) {
        try {
            Thread.sleep(time);
            
        } catch (InterruptedException e) {
            System.err.println(e);
        }   
    }
    
    /* 
     * Fires an ActionEvent for all ActionListeners in the array.
     * Uses as time the current time stamp.
     * 
     * See fireActioneEvents(String, long, int, ActionListener[]) for more info.
     */
    public static void fireActionEvents(Object source, String command, int modifiers, ActionListener[] als) {
        fireActionEvents(source, command, System.currentTimeMillis(), modifiers, als);
    }
    
    /* 
     * Fires an ActionEvent for all ActionListeners currently listening.
     * Uses another thread for execution.
     * 
     * @param source the source of the event.
     * @param command the command used for the event.
     * @param when the time when the event occured.
     * @param modifiers the modifiers for the event.
     * @param als array containing the ActionListeners that need to be notified of the event.
     */
    public static void fireActionEvents(final Object source, final String command,
                                        final long when, final int modifiers, final ActionListener[] als) {
        if (als == null) return;
        
        new Thread(source.getClass().getName() + " ActionEvent") {
            public void run() {
                ActionEvent e = new ActionEvent(source,
                                                ActionEvent.ACTION_PERFORMED,
                                                command, when, modifiers);
                
                for (int i = 0; i < als.length; i++) {
                    if (als[i] == null) continue;
                    
                    als[i].actionPerformed(e);
                }
            }
        }.start();
    }
    
    /* 
     * Converts a double to a String, having 'decimals' decimals.
     * 
     * @param num the number to be converted.
     * @param the number of decimals.
     * @return String representation of a double, having 'decimals' decimals.
     */
    public static String doubleToStringDecimals(double num, int decimals) {
        if (decimals < 0) throw new IllegalArgumentException("Number of decimals was negative: " + decimals);
        
        String number = Double.toString(num);
        for (int i = 0; i < number.length(); i++) {
            if (number.charAt(i) == '.') {
                if (decimals == 0) {
                    return number.substring(0, i);
                    
                } else if (number.length() > i + decimals) {
                    return number.substring(0, i + decimals + 1);
                    
                } else {
                    while (number.length() < i + decimals + 1) {
                        number += "0";
                    }
                }
                
                return number;
            }
        }
        
        // '.' was not found
        number += ".";
        for (int i = 0; i < decimals; i++) {
            number += "0";
        }
        
        return number;
    }
    
    /* 
     * Converts an Integer to a String, with zero's filled till the n'th position.
     * 
     * @param i number to be converted.
     * @param n the length of the number + number of leading zeroes.
     * 
     * If the length of the number is bigger then n, then the full number is returned.
     */
    public static String fillZero(int i, int n) throws NumberFormatException {
        String number = Integer.toString(i);
        
        while (number.length() < n) {
            number = "0" + number;
        }
        
        return number;
    }
    
    /* 
     * Adds spaces to the left of the given text till the total length
     * of the text is equal to the given size.
     * If the initial length of the text is longer then the given size,
     * no action is taken.
     * 
     * @param text text to process.
     * @param size length of the text.
     */
    public static String fillSpaceLeft(String text, int size) {
        for (int i = text.length(); i < size; i++) {
            text = " " + text;
        }
        
        return text;
    }
    
    /* 
     * Adds spaces to the right of the given text till the total length
     * of the text is equal to the given size.
     * If the initial length of the text is longer then the given size,
     * no action is taken.
     * 
     * @param text text to process.
     * @param size length of the text.
     */
    public static String fillSpaceRight(String text, int size) {
        for (int i = text.length(); i < size; i++) {
            text = text + " ";
        }
        
        return text;
    }
    
    /* 
     * Converts all spaces in the input String to spaces that are visible in html.
     * 
     * @param text text to process.
     */
    public static String toHTMLSpace(String text) {
        return text.replaceAll(" ", "" + ((char) 0x00A0));
    }
    
    /*
     * Calculates the power of a base.
     * Has a much higher accuracy compared to the function
     * java.lang.Math.pow(double, double), but only accepts integer powers.
     * Takes significantly less time calculating powers less then 500, but more
     * when calculating higher powers compared to the same method.
     * 
     * @param base the base used for the power.
     * @param pow the power used for the power.
     * @result the result of base ^ pow.
     */
    public static double intPow(double base, int pow) {
        double result = 1;
        
        for (int i = 0; i < pow; i++) {
            result *= base;
        }
        
        for (int i = -1; i >= pow; i--) {
            result /= base;
        }
        
        return result;
    }
    
    /* 
     * Checks if a given object is an array.
     * Returns true if so, false otherwise.
     * 
     * @param obj the Object to be tested.
     * @return true if {@code obj} is an array. False otherwise.
     */
    public static boolean isArray(Object obj) {
        if (obj == null) return false;
        return obj.getClass().isArray();
    }
    
    /* 
     * Randomly shuffles an array.
     * 
     * @param in the input array.
     * @param rnd the used Random object.
     * 
     * Copy-pasted from:
     * "http://grepcode.com/file/repository.grepcode.com/java/root/jdk/openjdk/8u40-b25/java/util/Collections.java#Collections.shuffle%28java.util.List%2Cjava.util.Random%29"
     */
    public static <V> V[] shuffleArray(V[] in) {
        return shuffleArray(in, new Random());
    }
    
    public static <V> V[] shuffleArray(V[] in, Random rnd) {
        for (int i = in.length; i > 1; i--) {
            swap(in, i - 1, rnd.nextInt(i));
        }
        
        return in;
    }
    
    /* 
     * Makes a copy of a Class array, keeping the type variable.
     * 
     * @param array Class array with type to copy.
     * WARNING! THIS FUNCTION HAS NOT BEEN EXTENSIVLY TESTED YET!
     */
    @SuppressWarnings("unchecked")
    public static <T> Class<T>[] copyArray(Class<T>[] array) {
        if (array == null) return null;
        
        Class<T>[] newArray = (Class<T>[]) new Class[array.length];
        
        for (int i = 0; i < array.length; i++) {
            newArray[i] = array[i];
        }
        
        return newArray;
    }
    
    /* 
     * Performs a swap between two elements of an array.
     * 
     * @param arr the array where the swap occurs.
     * @param i the first element of the swap.
     * @param j the second element of the swap.
     * @return arr, but then with the elements i and j swapped.
     */
    public static <V> V[] swap(V[] arr, int i, int j) {
        V tmp = arr[i];
        arr[i] = arr[j];
        arr[j] = tmp;
        return (V[]) arr;
    }
    
    /* 
     * Calculates a hashValue for an object with the given dependant objects.
     */
    public static int calcHashCode(Object[] objArr) {
        int result = 41;
        
        for (Object obj : objArr) {
            int c;
            
            if (obj == null) {
                c = 0;
                
            } else if (obj instanceof Boolean) {
                c = ((Boolean) obj ? 1 : 0);
                
            } else if (obj instanceof Byte ||
                       obj instanceof Character ||
                       obj instanceof Short ||
                       obj instanceof Integer) {
                c = (int) obj;
                
            } else if (obj instanceof Long) {
                c = (int) Math.pow((Long) obj, (Long) obj >>> 32);
                
            } else if (obj instanceof Float) {
                c = Float.floatToIntBits((Float) obj);
                
            } else if (obj instanceof Double) {
                Long value = Double.doubleToLongBits((Double) obj);
                c = (int) Math.pow(value, value >>> 32);
                
            } else if (isArray(obj)) {
                c = calcHashCode((Object[]) obj);
                
            } else {
                c = obj.hashCode();
            }
            
            result = 37 * result + c;
        }
        
        return result;
    }
    
}