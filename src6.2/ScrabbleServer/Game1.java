package ScrabbleServer;

import java.io.IOException;
import java.util.ArrayList;

import org.json.*;

public class Game1 {

    private dataManager manager = null;
    public boolean voteRow = true;
    public boolean voteColumn = true;
    private JSONObject wordTable ;
    public int pass = 0;

    /**
     * @param
     * @throws IOException
     * @throws JSONException
     */
//	public static void main(String[] args) throws IOException, JSONException {
//		new Game().start();
//	}
//
//	public void start() throws IOException, JSONException {
//
//	}
    public Game1() {
        System.out.println("A new game starts");
        try {
            manager = new dataManager();
            wordTable = manager.getDataJson();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void update(String command) throws IOException, JSONException {
        String[] argument = command.split(",");
        String grid = argument[1]+","+ argument[2];
        String letter = argument[0];
        synchronized (wordTable) {
            wordTable.put(grid, letter);
            manager.write(wordTable);
        }
    }

    public int scoring(String command) throws JSONException {
        int score = 0;
        try {
            String[] argument = command.split(",");
            String ro = argument[0];
            String col = argument[1];
            String key =null; //ro+","+col;
            String direction = argument[2];
            int row = Integer.parseInt(ro);
            int column = Integer.parseInt(col);
            int count =0;       
            String result ="";
            if (direction.equals("row")) {             	
    			try {
    				key = ro+","+col;
                    while(!wordTable.getString(key).equals("null")) {
                    	count=count+1;
                    	column=column-1;
                    	ro = String.valueOf(column);
                    	key = ro+","+col;
    				}
                    count=count-1;
                    while(wordTable.has(key)) {                    
                    	count=count+1;
                    	column=column+1;
                    	ro = String.valueOf(column);
                    	key = ro+","+col;
    				}
                    
    			}catch (IndexOutOfBoundsException e) {
    				System.out.println(count);
    			}
            }
            if (direction.equals("column")) { 
    			try {
    				key = ro+","+col;
                    while(!wordTable.getString(key).equals("null")) {
                    	count=count+1;
                    	row=row-1;
                    	ro = String.valueOf(row);
                    	key = ro+","+col;
    				}
                    count=count-1;
                    while(wordTable.has(key)) {                    
                    	count=count+1;
                    	row=row+1;
                    	ro = String.valueOf(row);
                    	key = ro+","+col;
    				}
            
        } catch (NumberFormatException e) {
            //System.out.println("Error");
        }
    			}
       
    score = count;
    }finally {
    	
    }
		return score;
    
}}