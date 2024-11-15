import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.util.Random;

public class Spider {
    protected static final String imgFile = "spider.png";

    protected GridLocation location;

    protected FlyWorld world;

    protected BufferedImage image;

    public Spider(GridLocation loc, FlyWorld fw)
    {
        location = loc;
        world = fw;

        try
        {
            image = ImageIO.read(new File(imgFile));
        }
        catch (IOException ioe)
        {
            System.out.println("Unable to read image file: " + imgFile);
            ioe.printStackTrace();
            System.exit(0);
        }
        location.setSpider(this);
    }
    public BufferedImage getImage() {
        return image;
    }

    public GridLocation getLocation() {
        return location;
    }

    public boolean isPredator() {
        return true;
    }

    public String toString() {
        return "Spider at location (" + location.getRow() + ", " + location.getCol() + ")";
    }
    public GridLocation[] generateLegalMoves() {
        GridLocation[] legalMoves = new GridLocation[4]; 
        int moves = 0;
        int flyRow = world.getFlyLocation().getRow();
        int flyCol = world.getFlyLocation().getCol();
        int spiderRow = location.getRow();
        int spiderCol = location.getCol();

        if (spiderRow < flyRow) {
            if (world.isValidLoc(spiderRow + 1, spiderCol) &&
                !world.isOccupiedByFrog(world.getLocation(spiderRow + 1, spiderCol))) {
                legalMoves[moves++] = world.getLocation(spiderRow + 1, spiderCol); 
            }
        } else if (spiderRow > flyRow) {
            if (world.isValidLoc(spiderRow - 1, spiderCol) &&
                !world.isOccupiedByFrog(world.getLocation(spiderRow - 1, spiderCol))) {
                legalMoves[moves++] = world.getLocation(spiderRow - 1, spiderCol); 
            }
        }

        if (spiderCol < flyCol) {
            if (world.isValidLoc(spiderRow, spiderCol + 1) &&
                !world.isOccupiedByFrog(world.getLocation(spiderRow, spiderCol + 1))) {
                legalMoves[moves++] = world.getLocation(spiderRow, spiderCol + 1); 
            }
        } else if (spiderCol > flyCol) {
            if (world.isValidLoc(spiderRow, spiderCol - 1) &&
                !world.isOccupiedByFrog(world.getLocation(spiderRow, spiderCol - 1))) {
                legalMoves[moves++] = world.getLocation(spiderRow, spiderCol - 1); 
            }
        }

        GridLocation[] result = new GridLocation[moves];
        for (int i = 0; i < moves; i++) {
            result[i] = legalMoves[i];
        }

        return result;
    }
   
    public void update() {
        GridLocation[] legalMoves = generateLegalMoves();
        if (legalMoves.length > 0) {
            Random rand = new Random();
            GridLocation newLocation = legalMoves[rand.nextInt(legalMoves.length)];
            if (!world.isOccupiedByFrog(newLocation)) {
                location.removeSpider();
                newLocation.setSpider(this);
                location = newLocation;
            }
        }
    }

    public boolean eatsFly() {
        return location.equals(world.getFlyLocation());
    }
}
