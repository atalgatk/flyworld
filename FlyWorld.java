import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.awt.Color;
import java.util.Random;


/**
 * Contains information about the world (i.e., the grid of squares)<br>
 * and handles most of the game play work that is NOT GUI specific
 */

public class FlyWorld
{
    protected int numRows;
    protected int numCols;

    protected GridLocation [][] world;

    protected GridLocation start;
    protected GridLocation goal;
    
    protected Fly mosca;



    protected Frog[] frogs;
    protected Color [][] colors; 
    protected int numFrogs;

    protected Spider [] spiders;
    protected int numSpiders;
     

    /**
     * Reads a file containing information about<br>
     * the grid setup.  Initializes the grid<br>
     * and other instance variables for use by<br>
     * FlyWorldGUI and other pieces of code.
     *
     *@param fileName the file containing the world grid information
     */
    public FlyWorld(String fileName){

         try {
            File inputFile = new File(fileName);
            Scanner scan = new Scanner(inputFile);
    
            this.numRows = scan.nextInt();
            this.numCols = scan.nextInt();
            scan.nextLine();
    
            this.world = new GridLocation[numRows][numCols];
            this.colors = new Color[numRows][numCols];
            this.frogs = new Frog[numRows * numCols];
            this.numFrogs = 0;
            this.spiders = new Spider [numRows * numCols];
            this.numSpiders = 0;
            
    
            for (int i = 0; i < numRows; i++) {
                String line = scan.nextLine();
                for (int j = 0; j < numCols; j++) {
                    char symbol = line.charAt(j);
                    GridLocation location = new GridLocation(i, j);
    
                    if (symbol == 'h') {
                        colors[i][j] = Color.RED;
                        location.setBackgroundColor(Color.RED);
                        this.goal = location;
                    } else if (symbol == 's') {
                        colors[i][j] = Color.GREEN;
                        location.setBackgroundColor(Color.GREEN);
                        this.start = location;
                        this.mosca = new Fly(location, this);
                    } else if (symbol =='f') {
                        colors[i][j] = Color.WHITE;
                        if (numFrogs == frogs.length) {
                            addFrog(location, this);
                        }
                        frogs[numFrogs++] = new Frog(location, this);
                    }else if (symbol =='a') {
                        if (numSpiders == spiders.length) {
                            addSpiders(location, this);
                        }
                        spiders[numSpiders++] = new Spider(location, this);

                    } else {
                        colors[i][j] = Color.WHITE;
                    }
    
                    this.world[i][j] = location;
                }
            }
                scan.close();
    
            } catch (FileNotFoundException e) {
                System.out.println("File not found: " + e.getMessage());
            }
    
    

        // The following print statements are just here to help you know 
        // if you've done part 1 correctly.  You can comment them out or 
        // delete them before you make your final submission
        // System.out.println("numRows: " + this.numRows + "   numCols: " + this.numCols);
        // System.out.println("start: " + this.start + "   goal: " + this.goal);
        //System.out.println("Mosca: " + this.mosca.toString());
    }
    private void addFrog(GridLocation location, FlyWorld fw) {
        Frog[] newFrogs = new Frog[frogs.length * 2];
        for (int i = 0; i < numFrogs; i++) {
            newFrogs[i] = frogs[i];
        }
        frogs = newFrogs;
    }

    private void addSpiders(GridLocation location, FlyWorld fw) {
        Spider[] newSpiders = new Spider[spiders.length * 2];
        for (int i = 0; i < numSpiders; i++) {
            newSpiders[i] = spiders[i];
        }
        spiders = newSpiders;
    }
  
    /**
     * @return int, the number of rows in the world
     */
    public int getNumRows(){
        return numRows;
    }

    /**
     * @return int, the number of columns in the world
     */
    public int getNumCols(){
        return numCols;
    }

    /**
     * Deterimes if a specific row/column location is<br>
     * a valid location in the world (i.e., it is not out of bounds)
     *
     * @param r a row
     * @param c a column
     *
     * @return boolean
     */
    public boolean isValidLoc(int r, int c){
        // FILL IN
        if (r >= 0 && r < numRows && c >= 0 && c < numCols) {
            return true;
        } else {
            return false;
        }
    }
        
    /**
     * Returns a specific location based on the given row and column
     *
     * @param r the row
     * @param c the column
     *
     * @return GridLocation
     */
    public GridLocation getLocation(int r, int c){
        return world[r][c];
    }

    /**
     * @return FlyWorldLocation, the location of the fly in the world
     */
    public GridLocation getFlyLocation(){
        return mosca.getLocation();
    }

    /**
     * Moves the fly in the given direction (if possible)
     * Checks if the fly got home or was eaten
     *
     * @param direction the direction, N,S,E,W to move
     *
     * @return int, determines the outcome of moving fly<br>
     *              there are three possibilities<br>
     *              1. fly is at home, return ATHOME (defined in FlyWorldGUI)<br>
     *              2. fly is eaten, return EATEN (defined in FlyWorldGUI)<br>
     *              3. fly not at home or eaten, return NOACTION (defined in FlyWorldGUI)
     */
    public int moveFly(int direction){
        // FILL IN
        mosca.update(direction); 

        if (mosca.getLocation().equals(goal)) {
            return FlyWorldGUI.ATHOME; 
        } else {
            for (Frog frog : frogs){
                if (frog != null && frog.eatsFly()) {
                    return FlyWorldGUI.EATEN;
            }
        }
            boolean spiderAteFly = movePredators();
            if (spiderAteFly) {
                return FlyWorldGUI.EATEN;
            }
            
            return FlyWorldGUI.NOACTION; 
        }
    }

       
    /**
     * Moves all predators. After it moves a predator, checks if it eats fly
     *
     * @return boolean, return true if any predator eats fly, false otherwise
     */
    public boolean movePredators(){
        // FILL IN
        boolean ateFly = false;

        for (int i = 0; i < numFrogs; i++) {
            Frog frog = frogs[i];
            frog.update();
    
            if (frog.eatsFly()) {
                ateFly = true;
            }
        }
        
        for (int i = 0; i < numSpiders; i++) {
            Spider spider = spiders[i];
            spider.update(); 
            if (spider.eatsFly()) {
                ateFly = true; 
                
            }
        }
        return ateFly;
    }
    public boolean isOccupiedByFrog(GridLocation location)
    {
        for (Frog frog : frogs) 
        {
            if (frog != null && frog.getLocation().equals(location))
            {
                return true;
            }
        }
        return false;
    }
}