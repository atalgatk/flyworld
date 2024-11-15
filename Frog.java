import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

import java.util.Arrays;
import java.util.Random;



/**
 * Handles display, movement, and fly eating capabalities for frogs
 */
public class Frog
{
    protected static final String imgFile = "frog.png";

    protected GridLocation location;

    protected FlyWorld world;

    protected BufferedImage image;

    

    /**
     * Creates a new Frog object.<br>
     * The image file for a frog is frog.jpg<br>
     *
     * @param loc a GridLocation
     * @param fw the FlyWorld the frog is in
     */
    public Frog(GridLocation loc, FlyWorld fw)
    {
    // FILL IN
        location = loc; // object in the grid game
        world = fw; 
        try
            {
                image = ImageIO.read(new File(imgFile));
            }
            catch (IOException ioe)
            {
                System.out.println("Unable to read image file: " + imgFile);
                System.exit(0);
            }
            location.setFrog(this);
        }

    /**
     * @return BufferedImage the image of the frog
     */
    public BufferedImage getImage()
    {
    return image;
    }

    /**
     * @return GridLocation the location of the frog
     */
    public GridLocation getLocation()
    {
    return location;
    }

    /**
     * @return boolean, always true
     */
    public boolean isPredator()
    {
    return true;
    }

    /**
    * Returns a string representation of this Frog showing
    * the location coordinates and the world.
    *
    * @return the string representation
    */
    public String toString(){
        String s = "Frog in world:  " + this.world + "  at location (" + this.location.getRow() + ", " + this.location.getCol() + ")";
        return s;
    }

    /**
     * Generates a list of <strong>ALL</strong> possible legal moves<br>
     * for a frog.<br>
     * You should select all possible grid locations from<br>
     * the <strong>world</strong> based on the following restrictions<br>
     * Frogs can move one space in any of the four cardinal directions but<br>
     * 1. Can not move off the grid<br>
     * 2. Can not move onto a square that already has frog on it<br>
     * GridLocation has a method to help you determine if there is a frog<br>
     * on a location or not.<br>
     *
     * @return GridLocation[] a collection of legal grid locations from<br>
     * the <strong>world</strong> that the frog can move to
     */
    public GridLocation[] generateLegalMoves()
    {
        // FILL IN
        //checking moving directions
        GridLocation[]legalMoves = new GridLocation[4];
        int moves = 0;

        if (world.isValidLoc(location.getRow() - 1, location.getCol()) &&
        !world.getLocation(location.getRow() - 1, location.getCol()).hasPredator()) {
            legalMoves[moves++] = world.getLocation(location.getRow() - 1, location.getCol());
        } //moving NORTH

        if (world.isValidLoc(location.getRow() + 1, location.getCol()) &&
        !world.getLocation(location.getRow() + 1, location.getCol()).hasPredator()) {
            legalMoves[moves++] = world.getLocation(location.getRow() + 1, location.getCol());
        } // moving SOUTH

        if (world.isValidLoc(location.getRow(), location.getCol() + 1) &&
        !world.getLocation(location.getRow(), location.getCol() + 1).hasPredator()) {
            legalMoves[moves++] = world.getLocation(location.getRow(), location.getCol() + 1);
        } // moving EAST

        
        if (world.isValidLoc(location.getRow(), location.getCol() - 1) &&
        !world.getLocation(location.getRow(), location.getCol() - 1).hasPredator()) {
            legalMoves[moves++] = world.getLocation(location.getRow(), location.getCol() - 1);
        } // moving WEST
        GridLocation[] result = new GridLocation[moves];
        for (int i = 0; i < moves; i++) {
            result[i] = legalMoves[i];
        }
    
        return result;
    }

    /**
     * This method updates the frog's position.<br>
     * It should randomly select one of the legal locations(if there any)<br>
     * and set the frog's location to the chosen updated location.
     */
    public void update()
    {
        // FILL IN
        GridLocation[] legalMoves = generateLegalMoves();
        if (legalMoves.length > 0) { 
            Random rand = new Random();
            GridLocation newLocation = legalMoves[rand.nextInt(legalMoves.length)];

            while (world.isOccupiedByFrog(newLocation)) {
                newLocation = legalMoves[rand.nextInt(legalMoves.length)];
            }

            location.removeFrog();
            newLocation.setFrog(this);
            location = newLocation;
    }
}

    /**
     * This method helps determine if a frog is in a location<br>
     * where it can eat a fly or not. A frog can eat the fly if it<br>
     * is on the same square as the fly or 1 spaces away in<br>
     * one of the cardinal directions
     *
     * @return boolean true if the fly can be eaten, false otherwise
     */ 
    public boolean eatsFly()
    {
        // FILL IN
        GridLocation flyLocation = world.getFlyLocation();
        int flyRow = flyLocation.getRow();
        int flyCol = flyLocation.getCol();
        int frogRow = location.getRow();
        int frogCol = location.getCol();

       
        if (frogRow == flyRow && frogCol == flyCol) {
            return true;
        }

        
        return (Math.abs(frogRow - flyRow) == 1 && frogCol == flyCol) ||  
            (frogRow == flyRow && Math.abs(frogCol - flyCol) == 1);    
    }
}