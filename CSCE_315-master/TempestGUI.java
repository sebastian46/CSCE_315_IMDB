import javax.swing.*;      // Needed for Swing classes
import java.awt.event.*;   // Needed for ActionListener Interface
import java.awt.*;
import java.sql.*;
import java.io.*;
import java.lang.reflect.Array;
import java.util.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream; 
import java.util.Collections;

//********************************************************************
// GUI - Area

public class TempestGUI extends JFrame
{
   private final int WINDOW_WIDTH = 750;  // Window width
   private final int WINDOW_HEIGHT = 750; // Window height

   JFrame f;

   //Question 1
   JTextField input_01, input_02;

   //Question 2
   JTextField input_year_1, input_year_2, input_year_actor;

   //Question 3

   //Question 4
   JTextField input_genre_q4, input_year_q4, input_actor_q4;

   public TempestGUI(){
      f = new JFrame("Tempest GUI");
      f.setSize(WINDOW_WIDTH,WINDOW_HEIGHT);
      f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

      //********************************************************************
      //Question 1

      input_01 = new JTextField("Actor or Director");
      input_01.setBounds(150,50, 200,30); // x , y , width, height
      f.add(input_01);

      input_02 = new JTextField("Actor or Director");
      input_02.setBounds(390,50, 200,30); // x , y , width, height
      f.add(input_02);

      JLabel to_link = new JLabel("to");
      to_link.setBounds(365,40,50,50); // x , y , width, height
      f.add(to_link);

      JButton search_b = new JButton("search");
      search_b.addActionListener(new search_button());
      search_b.setBounds(320,100,90,30); // x , y , width, height
      f.add(search_b);

      //********************************************************************
      //Question 2

      input_year_1 = new JTextField("Year value #1");
      input_year_1.setBounds(100,200, 200,30); // x , y , width, height
      f.add(input_year_1);

      input_year_2 = new JTextField("Year value #2");
      input_year_2.setBounds(340,200, 200,30); // x , y , width, height
      f.add(input_year_2);

      input_year_actor = new JTextField("Actor / Director");
      input_year_actor.setBounds(550,200, 200,30); // x , y , width, height
      f.add(input_year_actor);

      JButton year_search_b = new JButton("search");
      year_search_b.addActionListener(new year_search_button());
      year_search_b.setBounds(320,250,90,30); // x , y , width, height
      f.add(year_search_b);

      //********************************************************************
      //Question 3


      //********************************************************************
      //Question 4

      input_genre_q4 = new JTextField("Genre");
      input_genre_q4.setBounds(50,350, 200,30); // x , y , width, height
      f.add(input_genre_q4);

      input_year_q4 = new JTextField("Year");
      input_year_q4.setBounds(290,350, 200,30); // x , y , width, height
      f.add(input_year_q4);

      input_actor_q4 = new JTextField("Favorite Actor");
      input_actor_q4.setBounds(530,350, 200,30); // x , y , width, height
      f.add(input_actor_q4);

      JButton year_search_d = new JButton("search");
      year_search_d.addActionListener(new q4_search_button());
      year_search_d.setBounds(320,400,90,30); // x , y , width, height
      f.add(year_search_d);

      //********************************************************************

      f.setLayout(null);
      f.setVisible(true);
   }

   
   public static void BFS(String start, String end){
      
      //variables
      //used for testing
      start = "nm0000102";
      end = "nm0115524";
      LinkedList list = new LinkedList();
      LinkedList buffer_movie = new LinkedList();
      LinkedList buffer_people = new LinkedList();

      Connection conn = null;
      String index = start;
      int index_check = start.compareTo( end );

      //start the tree
      list.add(start, null);
      Node parentnode = list.gethead();
      

      try{
         conn = DriverManager.getConnection("jdbc:postgresql://db-315.cse.tamu.edu/TempestDB",dbSetup.user, dbSetup.pswd);

         //repeats loop until actor found
         while(index_check != 0){

            //call for all movies with the actor
            buffer_people = null;
            Statement stmt = conn.createStatement();
            String sqlStatement = "select tconst from movies where movies.nconst ='"+index+"';";
            System.out.print("Movies : ");
            System.out.println(sqlStatement);
            ResultSet rs = stmt.executeQuery(sqlStatement);

            //used to grab the titles from the column
            while(rs.next()){
               String title = rs.getString("tconst");
               list.add(title, parentnode);
               buffer_movie.add(title, buffer_movie.gethead());
            }

            //call all actors from movies
            Node buffer_index = buffer_movie.gethead();
            System.out.println("Test 02:");
            while(buffer_index.getparent() != null){
               index = buffer_index.getdata();
               stmt = conn.createStatement();
               sqlStatement = "select nconst from movies where movies.tconst ='"+index+"';";
               System.out.print("People : ");
               System.out.println(sqlStatement);
               rs = stmt.executeQuery(sqlStatement);
               System.out.println("Test 03:");
               //used to grab the names from the column
               while(rs.next()){
                  String people = rs.getString("nconst");
                  parentnode = buffer_index.getparent();
                  list.add(people, parentnode);
                  if(people == end){
                     System.out.println("Found Actor and Breaking loop:");
                     index = people; //since the person should be the stored in people used as another check
                     break;
                  }
               }
               System.out.println("Test 04:");
               //interate to parent
               buffer_index = buffer_index.getparent();
            }
            buffer_movie = null;


            
            //check if found actor
            index_check = index.compareTo( end );
         }
      }
      catch(Exception d){
         System.out.println("Error with BFS function:");
      }
   }
   

   // Class for Question 1
   private class search_button implements ActionListener{
      public void actionPerformed(ActionEvent e){
         Connection conn = null;
         String start = input_01.getText();
         //String index = start; //index string used for BFS
         String end = input_02.getText();
         try{
            //open connection to database
            Class.forName("org.postgresql.Driver");
            conn = DriverManager.getConnection("jdbc:postgresql://db-315.cse.tamu.edu/TempestDB",dbSetup.user, dbSetup.pswd);

            // Checks if input is equal to itself first
            int var_str = start.compareTo( end );
            System.out.println("str1 & str2 comparison: "+var_str);
            if(var_str == 0){
               System.out.println(start+" has a rank number of 0:");
               System.out.println("The actor is connected to itself:");
            }

            // If start and end not equal must perform a BFS
            else{
               System.out.println("Got to the BFS function call:");
               BFS(start,end);
            }

            //close connection after done
            conn.close();
         }
         catch(Exception d){
            System.out.println("Some Error Occured in Main");
         }
         System.out.println("End of Search Button Action:");
      }
   }

   // Class for Question 2
   // List an actor who performed in a movie every year in the given range of years.
   private class year_search_button implements ActionListener{
      public void actionPerformed(ActionEvent e){
         // converts string to int
         int begin_year = 0;
         int end_year = 0;
         String begin_year_string = input_year_1.getText();
         String end_year_string = input_year_2.getText();
         try{
            begin_year = Integer.parseInt(begin_year_string);
            end_year = Integer.parseInt(end_year_string);
         } catch (NumberFormatException d){
            System.out.println("Please enter a valid year");
         }
         int year_range = end_year - begin_year + 1;
         // create array with range of years
         int[] year_range_arr = new int[year_range];
         for(int i = 0; i < year_range_arr.length; i++){
            year_range_arr[i] = i + begin_year;
         }
         Connection conn = null;
         try{
            //open connection to database
            Class.forName("org.postgresql.Driver");
            conn = DriverManager.getConnection("jdbc:postgresql://db-315.cse.tamu.edu/TempestDB",dbSetup.user, dbSetup.pswd);

            Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            String sqlStatement = "SELECT nconst, startyear, primarytitle, primaryname FROM movies WHERE startyear IS NOT NULL ORDER BY nconst, startyear ASC;";
            ResultSet rs = stmt.executeQuery(sqlStatement);
            List<String> nconst_name = new ArrayList<>();
            List<String> primarytitle_list = new ArrayList<>();
            // keep track of nconst
            rs.next();
            String prev_nconst = rs.getString(1);
            String prev_name = rs.getString(4);
            String primarytitle_str = "";
            rs.beforeFirst();
            while(rs.next()){
               String nconst_ = rs.getString(1);
               // while it's the same actor
               if (nconst_.equals(prev_nconst)){
                  // converts year to int
                  int year_int = Integer.parseInt(rs.getString(2));
                  if (year_int >= begin_year && year_int <= end_year){
                     primarytitle_str += "(" + rs.getString(3) + "), ";
                     // update array to check off year
                     // if array is empty at the end, then we found a movie for all years
                     for(int i = 0; i < year_range_arr.length; i++){
                        if(year_int == year_range_arr[i] && year_range_arr[i] != 0){
                           year_range_arr[i] = 0;
                        }
                     }
                  }
               } else {
                  // updating values once a different person is looked at
                  int sum = 0;
                  for(int i = 0; i < year_range_arr.length; i++){
                     sum += year_range_arr[i];
                  }
                  if (sum == 0){
                     primarytitle_list.add(primarytitle_str);
                     nconst_name.add(prev_name);
                  }
                  prev_name = rs.getString(4);
                  primarytitle_str = "";
                  for(int i = 0; i < year_range_arr.length; i++){
                     year_range_arr[i] = i + begin_year;
                  }
                  prev_nconst = nconst_;


                  // start new search
                  int year_int = Integer.parseInt(rs.getString(2));
                  if (year_int >= begin_year && year_int <= end_year){
                     primarytitle_str += rs.getString(3) + ", ";
                     // update array to check off year
                     // if array is empty at the end, then we found a movie for all years
                     for(int i = 0; i < year_range_arr.length; i++){
                        if(year_int == year_range_arr[i] && year_range_arr[i] != 0){
                           year_range_arr[i] = 0;
                        }
                     }
                  }
               }
            }
            if(nconst_name.size() > 0){
               Random rand = new Random();
               int rand_int = rand.nextInt(nconst_name.size());
               System.out.println("Actor or Director: " + nconst_name.get(rand_int));
               System.out.println("Credited movies: " + primarytitle_list.get(rand_int));
            } else {
               System.out.println("No matches found!");
            }
         }
         catch(Exception d){
            System.out.println("Some Error Occured");
            System.out.println(d);
         }
         try{
            //close connection after done
            conn.close();
            System.out.println("Connection closed.");
         } catch (Exception b){
            System.out.println("Connection NOT closed");
         }
         System.out.println("End of Search Button Action:");
         
      }
   }

   // Question 4
   // Get top 50 list genre?
   // filter out years (+/- 5 years from given)
   // filter out movies which include favorite actor
   // look at title crew

   // look at random movie from actor with corresponding genre and year range
   // look up directors/writers from that movie
   // look at knownfortitles 

   // top movies in genre
   // SELECT DISTINCT primarytitle, tconst, genres, startyear, averagerating, numvotes FROM movies WHERE genres LIKE '%Action%' AND numvotes>10000 AND startyear::integer BETWEEN 1975 AND 1985 ORDER BY averagerating DESC LIMIT 50;

   // movies by actor which include genre, within range year given
   // SELECT DISTINCT tconst, genres, startyear, primarytitle FROM movies WHERE primaryname='Arnold Schwarzenegger' AND genres LIKE '%Action%' AND numvotes>10000 AND startyear::integer BETWEEN 1975 AND 1985 ORDER BY startyear ASC;

   private class q4_search_button implements ActionListener{
      public void actionPerformed(ActionEvent e){
         String input_genre = input_genre_q4.getText();
         int input_year = Integer.parseInt(input_year_q4.getText());
         String input_actor = input_actor_q4.getText();
         // range of years to look through
         int low_year = input_year - 3;
         int high_year = input_year + 3;

         Connection conn = null;
         try{
            //open connection to database
            Class.forName("org.postgresql.Driver");
            conn = DriverManager.getConnection("jdbc:postgresql://db-315.cse.tamu.edu/TempestDB",dbSetup.user, dbSetup.pswd);

            Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            // look for relevant movies by actor in date range
            String sqlStatement = "SELECT DISTINCT tconst FROM movies WHERE primaryname='" + input_actor + "' AND genres LIKE '%" + input_genre + "%' AND numvotes>10000 AND startyear::integer BETWEEN " + low_year + " AND " + high_year + ";";
            ResultSet rs = stmt.executeQuery(sqlStatement);

            // store tconst for lookup later
            List<String> tconst_actor = new ArrayList<>();
            while(rs.next()){
               tconst_actor.add(rs.getString(1));
            }

            // store list of directors and writers from actor's relevant movies
            List<String> nconst_directors = new ArrayList<>();
            for(int i = 0; i < tconst_actor.size(); i++){
               sqlStatement = "SELECT DISTINCT directors, writers FROM title_crew WHERE tconst='" + tconst_actor.get(i) + "';";
               rs = stmt.executeQuery(sqlStatement);
               while(rs.next()){
                  String dir_ = rs.getString(1);
                  String[] split_dir = dir_.split("\\s*,\\s*");
                  String writer_ = rs.getString(2);
                  String[] split_writer = writer_.split("\\s*,\\s*");
                  for(int j = 0; j < split_dir.length; j++){
                     nconst_directors.add(split_dir[j]);
                  }
                  for(int k = 0; k < split_writer.length; k++){
                     nconst_directors.add(split_writer[k]);
                  }                  
               }
            }

            // store all movies that the actor searched is NOT in
            sqlStatement = "SELECT DISTINCT nconst FROM name_basics WHERE primaryname='" + input_actor + "';";
            rs = stmt.executeQuery(sqlStatement);
            String nconst_actor = "";
            if(rs.next()){
               nconst_actor = rs.getString(1);
            }
            // look up directors and writers' movies who fit genre and year range
            List<String> movie_recommend = new ArrayList<>();
            List<String> director_recommended = new ArrayList<>();
            for(int i = 0; i < nconst_directors.size(); i++){
               sqlStatement = "SELECT DISTINCT tconst, primarytitle FROM movies WHERE numvotes>1000 AND nconst='" + nconst_directors.get(i) +"' AND genres LIKE '%" + input_genre + "%' AND startyear::integer BETWEEN " + low_year + " AND " + high_year + ";";
               rs = stmt.executeQuery(sqlStatement);
               while(rs.next()){
                  String temp_tconst = rs.getString(1);
                  Statement stmt_new = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                  sqlStatement = "SELECT nconst FROM title_principals WHERE tconst='" + temp_tconst + "';";
                  ResultSet rs_new = stmt_new.executeQuery(sqlStatement);
                  boolean actor_in = false;
                  while(rs_new.next()){
                     String nconst_check = rs_new.getString(1);
                     if(nconst_check.equals(nconst_actor)){
                        actor_in = true;
                     }
                  }
                  if(actor_in == false){
                     boolean is_dup = false;
                     String temp_rs = rs.getString(2);
                     for(int w = 0; w < movie_recommend.size(); w++){
                        if(temp_rs.equals(movie_recommend.get(w))){
                           is_dup = true;
                        }
                     }
                     if(is_dup == false){
                        movie_recommend.add(rs.getString(2));
                        director_recommended.add(nconst_directors.get(i));
                     }
                  }
                  stmt_new.close();
                  rs_new.close();
               }
            }
            // print out
            List<String> director_name = new ArrayList<>();
            for(int i = 0; i < director_recommended.size(); i++){
               sqlStatement = "SELECT primaryname FROM name_basics WHERE nconst='" + director_recommended.get(i) + "';";
               rs = stmt.executeQuery(sqlStatement);
               while(rs.next()){
                  director_name.add(rs.getString(1));
               }
            }
            stmt.close();
            rs.close();
            if(movie_recommend.size() > 4){
               for(int i = 0; i < movie_recommend.size(); i++){
                  Random rand = new Random();
                  int rand_int = rand.nextInt(movie_recommend.size());
                  int temp_int = i + 1;
                  System.out.println("Recommendation " + temp_int + " from " + director_name.get(rand_int) + ":" + movie_recommend.get(rand_int));
                  movie_recommend.remove(rand_int);
                  director_name.remove(rand_int);
               }
            } else {
               System.out.println("Not enough matches found!");
            }
         }
         catch(Exception d){
            System.out.println("Some Error Occured");
            System.out.println(d);
         }
         try{
            //close connection after done
            conn.close();
            System.out.println("Connection closed.");
         } catch (Exception b){
            System.out.println("Connection NOT closed");
         }
         System.out.println("End of Search Button Action:");
      }
   }

   public static void main(String[] args)
   {
      new TempestGUI();
   }
}