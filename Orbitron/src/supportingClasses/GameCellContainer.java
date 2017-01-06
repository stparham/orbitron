package supportingClasses;

import javafx.scene.Node;
import javafx.scene.layout.Pane;

/**
 * Provides a stable area where animations can be rendered on top of game cells;
 * I created this class because adding animations to an individual GameCell was
 * not practical because I would have had to change the entire structure of each
 * GameCell so that it extended a pane which had a canvas embedded within it. I
 * essentially did the same thing here, but with a fraction of the work.
 * Rendering the animations to the entire GridPane that holds all of the
 * GameCells was not working either because the animation was always stuck at
 * the top left corner of the grid for some reason.
 * 
 * @author Stanton Parham (stparham@ncsu.edu)
 */
public class GameCellContainer extends Pane {
    
    
    /** The column of the game cell that this GameCellContainer holds; */
    private int col;
    
    /** The row of the game cell that this GameCellContainer holds; */
    private int row;
    
    /**
     * Constructs a GameCellContainer with the given row and column while
     * passing the children to it's super class, Pane;
     * 
     * @param row the row of this GameCellContainer in the game map
     * @param col the column of this GameCellContainer in the game map
     * @param children the GameCell that this GameCellContainer will hold
     */
    public GameCellContainer(int row, int col, Node... children) {
        super(children);
        this.row = row;
        this.col = col;
    }
    
    /**
     * Returns the column of this GameCellContainer;
     * 
     * @return the column of this GameCellContainer
     */
    public int getCol() {
        return this.col;
    }
    
    
    /**
     * Returns the row of this GameCellContainer;
     * 
     * @return the row of this GameCellContainer
     */
    public int getRow() {
        return this.row;
    }
    
    
}
