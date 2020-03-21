import java.io.*;

public class LinkedList{
    
   Node head;
   Node parent;
   Node temp_ptr;

   public void add(String data, Node parent){
      Node node = new Node();
      node.data = data;
      node.parent = parent;

      //set head to new node since once actor found it needs to be the head of the list
      head = node;
   }

   public Node gethead(){
      return head;
   }

   public void deletelist(){
      head = null;
   }


   public void showlist(){
      Node node = head;

      while(node.parent != null){
         System.out.print(node.data+" -> ");
         node = node.parent;
      }
      System.out.print(node.data);
      System.out.println("");
   }
}