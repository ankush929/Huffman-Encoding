import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class Main {

    public static void main(String[] args) throws IOException {

        FileWriter csvWriter = new FileWriter("Algo2_3.csv");


        ArrayList<char[]> charArrayList = randomCharListGenerator();
        ArrayList<int[]> charFreqList = randomFreqListGenerator();

//        Uncomment for manual input and replace in encoder

        /*
        char[] charArray = {'a', 'b', 'c', 'd', 'e', 'f'}; //given set of symbols
        int[] charfreq = {67, 56, 92, 34, 76, 23}; // given frequency of usage
        int n = charArray.length; //number of characters
        */


        long startTime = initTimeCal(csvWriter);
        for (int n = 2; n < 222 ; n++) {

            HuffmanEncoder huffmanEncoder = new HuffmanEncoder(n, charArrayList.get(n-2), charFreqList.get(n-2));  // init input
            Node root = huffmanEncoder.encoder();            //Encode values
            huffmanEncoder.decoder(root, "");   //Decode Values
            printHashmap(huffmanEncoder.getHuffValues());   //Print values

            closeCSV(csvWriter, n, startTime);
        }

        csvWriter.flush();
        csvWriter.close();

    }

    /**
     * Generates random values for frequency of usage for a symbol
     * @return returns an List of arrays of frequency usage based on different values of n
     */

    private static ArrayList<int[]> randomFreqListGenerator() {
        ArrayList<int[]> charFreqList = new ArrayList<>(222);
        for (int n = 2; n < 222; n++) {

            int[] charfreq = new int[n];

            Random random = new Random();
            for (int i = 0; i < n; i++) {
                charfreq[i] = random.nextInt(100 - 1) + 1;
            }
            charFreqList.add(charfreq);
        }
        return charFreqList;

    }

    /**
     * generates random and unique symbols between ascii code 35 and 255
     * @return returns an arraylist of arrays comprising of symbols based on different values of n
     */
    private static ArrayList<char[]> randomCharListGenerator() {

        ArrayList<char[]> charArrayList = new ArrayList<>(222); // Arraylist for unique symbols

        for (int n = 2; n < 222; n++) {


            String alphaNumericString = "";                             //asciiStringInit

            for (int i = 0; i < 222; i++) {
                char asciiChar = (char) (32 + 222);
                alphaNumericString = alphaNumericString + asciiChar;
            }

            char charArray[] = new char[n];
            ArrayList<String> randomArrayList = new ArrayList<>();      //add characters to Arraylist
            for (int i = 0; i < alphaNumericString.length(); i++) {
                randomArrayList.add(alphaNumericString.substring(i,i+1));
            }
            Collections.shuffle(randomArrayList);                        //ensure random characters


            for (int i = 1; i < n; i++) {
                charArray[i] = randomArrayList.get(i).charAt(0);        //make array from ArrayList

                //System.out.print(charArray[n-1] + " ");
                //System.out.println();
            }

            charArrayList.add(charArray);                               //add to list of array of different values of n
        }
        return charArrayList;
    }

    /**
     * Computes total time elaspsed and appends to a csv file
     * @param csvWriter FileWriter Object from Main
     * @param n         Total number of unique symbols/ total number of computations
     * @param startTime  Program startTime
     * @throws IOException throws Input Output Exception
     */

    private static void closeCSV(FileWriter csvWriter, int n, long startTime) throws IOException {

        long endtime = System.currentTimeMillis();

        long time = endtime - startTime;
        System.out.println("Elapsed Time ; " + time);

        System.out.println("N = " + n);

        csvWriter.append(String.valueOf(n));
        csvWriter.append(",");
        csvWriter.append(String.valueOf(time));
        csvWriter.append("\n");
    }

    /**
     * init CSV file
     * @param csvWriter FileWriter object from psvm
     * @return returns starttime in millisecinds of a program
     * @throws IOException throws Input Output Exception
     */

    private static long initTimeCal(FileWriter csvWriter) throws IOException {



        csvWriter.append("N");
        csvWriter.append(",");
        csvWriter.append("Time");
        csvWriter.append("\n");
        return System.currentTimeMillis();
    }

    /**
     * Prints the hashmap
     * @param hashMap String -String hashmap of huffman encoding
     */
    private static void  printHashmap(HashMap<String, String> hashMap)
    {
        System.out.println("******  PRINTING ENCODED VALUES! ******");
        for(String name : hashMap.keySet())
        {
            System.out.println(name + " : " + hashMap.get(name));
        }
    }
}

/**
 * Node class
 */
class Node
{
    int value;
    String name;

    Node left;
    Node right;


    Node(String name,int value, Node left, Node right) {  //Node init
        this.value = value;
        this.name = name;
        this.left = left;
        this.right = right;
    }

    Node()                          {   //Empty Node init
        this.value = 0;
        this.name = null;
        this.left = null;
        this.right = null;
    }

}

class HuffmanEncoder
{
    private HashMap<String,String> huffValues;
    private int n;
    private char[] charArray;
    private int[] charfreq;


    HuffmanEncoder(int n, char[] charArray, int[] charfreq) {  // class constructor init
        this.n = n;
        this.charArray = charArray;
        this.charfreq = charfreq;
        huffValues = new HashMap<>(n);
    }

    public HashMap<String, String> getHuffValues() {
        return huffValues;
    }

    /**
     * Encodes a set of symbols using Huffman encoding
     * @return returns a Binary Tree/ Root Node of the tree
     */
    Node  encoder()
    {
        Node root = new Node();
        PriorityQueue<Node> queue = new PriorityQueue<>(n, Comparator.comparingInt(o -> o.value)); //Priority queue and comparator initialization

        for(int i= 0; i<n; i ++)
        {
            queue.add(new Node(String.valueOf(charArray[i]), charfreq[i], null, null));     // Adding all nodes to a priority queue
        }


        while(queue.size()>1)           //Pop Min Heap (Priority Queue) until empty
        {

            Node left = queue.poll();   //pop minimum frequency value
            Node right = queue.poll();  //pop second-minimum frequency value


//            System.out.println("Polling Node:"+left.name +" Value = " + left.value);
//            System.out.println("Polling Node:"+right.name +" Value = " + right.value);

            int newValue = left.value + right.value;        //new Value with added frequencies
            String newName = left.name+right.name + " ";

            queue.add(new Node(newName, newValue, left, right));  //insert again in heap

//            System.out.println("Adding node: " + left.name+right.name + " ");
//            System.out.println("Adding node with value: " + newValue);
//            System.out.println();

            root.name = newName;    //Update root values
            root.value = newValue;

            root.left = left;       //assign left and right child nodes
            root.right = right;
        }

        //System.out.println("******  ENCODING SUCCESSFUL!  ********");

        return root;    //return huffman tree/root node
    }

    /**
     * Decodes a huffman encoded string
     * @param root Huffman Tree root
     * @param encodedValue 0 for left nodes and 1 for right nodes
     */

    void decoder(Node root, String encodedValue)
    {


        if(root.left == null && root.right == null)
        {

            if(!huffValues.containsKey(root.name.substring(0,root.name.length()))) { // include only leaf nodes
                huffValues.put(root.name, encodedValue);
            }
            return ;
        }

        decoder(root.left, encodedValue + "0");     // assign 0 to left nodes recursively
        decoder(root.right, encodedValue + "1");    // assign 1 to right nodes recursively
    }
}
