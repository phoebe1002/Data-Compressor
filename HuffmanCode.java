import java.io.*;
import java.util.*;

public class HuffmanCode{
   //field
   private HuffmanNode overallRoot;

   //constructors  
   //post: initialize a new HuffmanCode object using the algorithm 
   //      described for making a node from an array of frequencies.
   public HuffmanCode(int[] frequencies) {
      Queue<HuffmanNode> q = addNodesToQueue(new PriorityQueue<HuffmanNode>(), frequencies);    
      overallRoot = combineNodes(q);     
      print(overallRoot);      
   }
   
   //post: add nodes into a Priority Queue and return q
   private Queue<HuffmanNode> addNodesToQueue(Queue<HuffmanNode> q, int[] frequencies) {
      for(int i = 0; i < frequencies.length; i++) {
         if(frequencies[i] != 0){
            HuffmanNode root = new HuffmanNode(i, frequencies[i]);
            q.add(root);
         }  
      }
      return q;
   }
   
   //post: use priority queue to combine nodes based on frequency and return the final node
   private HuffmanNode combineNodes(Queue<HuffmanNode> q) {
      while(q.size() != 1) {
         HuffmanNode first = q.remove(); //first smallest
         HuffmanNode second = q.remove(); //second smallest
         HuffmanNode root = new HuffmanNode(first.freq + second.freq, first, second); //combine two nodes
         q.add(root); //add the combined node back to queue
      }  
      return q.remove();
   }

   //post: initialize a new HuffmanCode object by reading in 
   //      a previously constructed code from a .code file.
   public HuffmanCode(Scanner input) {
     while(input.hasNextLine()) {
        int asciiValue = Integer.parseInt(input.nextLine());
        String code = input.nextLine();   
        overallRoot = buildTree(overallRoot, code, asciiValue); 
      } 
   }
   
   private HuffmanNode buildTree(HuffmanNode root, String code, int asciiValue){
      //extract type from code
      char type = code.charAt(0);
      //create a leaf  
      HuffmanNode leaf = new HuffmanNode(asciiValue, 0);
      //base case - add dummy data into a branch
      if(root == null){
         root = new HuffmanNode(0, 0);
      }
      //update node.left and node.right
      if(code.length() == 1) {
         if(type == '0'){
            root.left = leaf;
         }else {
            root.right = leaf;
         }
      } 
      //recursively to examine the code
      else {     
         //update code
         code = code.substring(1);     
         if(type == '0'){
            root.left = buildTree(root.left, code, asciiValue);
         }else {
            root.right = buildTree(root.right, code, asciiValue);
         }
      }
      return root;
   }

   //methods
   //post: store the current huffman codes to the given output stream 
   //      in the standard format
   public void save(PrintStream output) {
      save(overallRoot, "", output);
   }
   
   //post: store the current huffman codes to the given output stream 
   //      in the standard format
   private void save(HuffmanNode root, String code, PrintStream output) {
      if(root != null) {
         save(root.left, code +  "0", output);
         if(root.asciiValue != 0) {
            output.println(root.asciiValue + "\n" + code);
         }
         save(root.right, code + "1", output);    
      }     
   }

   //post: read individual bits from the input stream and write 
   //      the corresponding characters the output.
   //public void translate(BitInputStream input, PrintStream output){
   public void translate(BitInputStream input, PrintStream output){    
      HuffmanNode pointer = overallRoot;
      while(input.hasNextBit()) {
         int bitType = input.nextBit();
         //System.out.print(bitType);
         //update pointer
         //find the leaf and print it
         pointer = update(pointer, bitType);
         if(pointer.left == null && pointer.right == null){
            output.print((char) pointer.asciiValue);
            pointer = overallRoot;
         }
      }
   }
   
   //post: update the tree based on bit type
   private HuffmanNode update(HuffmanNode root, int bitType){
      if(bitType == 0){
         root = root.left;
      }else {
         root = root.right;
      }
      return root;
   }

   //Class that represents a signle node in the tree
   private  static class HuffmanNode implements Comparable<HuffmanNode>  {
      //fields
      public int asciiValue; //stores ASCII value 
      public int freq; //stores its frequency
      public HuffmanNode left; //reference to left subtree
      public HuffmanNode right; //reference to right subtree
      
      //constructors       
      //post: construct an empty node with given asciiValue and freq
      public HuffmanNode(int asciiValue, int freq) {
         this(asciiValue, freq, null, null);
      }     
      
      //post: construct an node with given asciiValue, freq, and given links
      public HuffmanNode(int asciiValue, int freq, HuffmanNode left, HuffmanNode right) {
         this.asciiValue = asciiValue;
         this.freq = freq;
         this.left = left;
         this.right = right;
      }
      
      //post: constuct a node with given freq and given links
      public HuffmanNode(int freq, HuffmanNode first, HuffmanNode second) {
         this.freq = freq;
         if(first.compareTo(second) <= 0) {
            this.left = first;
            this.right = second;
         } else {
            this.left = second;
            this.right = first;
         }
      }      
      
      //post: returns a negative if this node less than the other,
      //      returns 0 if this node equals the other
      //      reutrns a positive if this node greater than the other
      public int compareTo(HuffmanNode other) {
         if(this.freq == other.freq) {
            return this.asciiValue - other.asciiValue;
         }else {
            return this.freq - other.freq;
         }
      } 
   }
   
   //post: print node in preorder for testing purpose
   private void print(HuffmanNode root){
      if (root != null){
         System.out.println(root.asciiValue);
         print(root.left);
         print(root.right);
      }
   }
   
} 