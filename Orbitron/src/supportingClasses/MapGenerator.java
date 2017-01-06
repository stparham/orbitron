package supportingClasses;

import java.util.Random;

import main.GameLogic;
import staticStorage.CTInfo;
import staticStorage.CellGraphics;

/**
 * This class randomly generates a new game map based on the values that it is
 * constructed with (it's pretty much hard-coded for a map size of 10 rows by 15
 * columns though). The random map generation process conforms to some very
 * strict limitations so that every map produced by this class is in every way
 * playable by the user.
 * 
 * <pre>
 * 
 * 
 * GENERIC MAP STRUCTURE CREATED (Map Size: 10 rows, 15 columns)
 * 
 * s's represent the area of the game map where a Space Port can be established.
 * The "S" is the cell at which a Space Port is established if there isn't already one.
 * 
 * +'s represent the area where only random cells can be established. However, random cells 
 * can be in any position.
 * 
 * O's represent the area where the Objectives can be established.
 * The "A" is the cell at which the first Objective is established if there isn't already one.
 * The "B" is the cell at which the second Objective is established if there aren't already two.
 * The "C" is the cell at which the third Objective is established if there aren't already three.
 *
 *                   C O L #
 *           0 1 2 3 4 5 6 7 8 9 0 1 2 3 4
 *         0 s s s s + + + + O O O O O O O 
 *         1 s s s s + + + + O O O O O O O 
 *      R  2 s s s s + + + + O O O O O O O 
 *      O  3 s s s S + + + + O O O O O O A 
 *      W  4 + + + + + + + + O O O O O O O 
 *      #  5 + + + + + + + + O O O O O O O 
 *         6 + + + + + + + + O O O B O O O 
 *         7 + + + + + + + + O O O O O O O 
 *         8 O O O O O O O O O O O O O O O 
 *         9 O O O O O C O O O O O O O O O
 * </pre>
 * 
 * 
 * @author Stanton Parham (stparham@ncsu.edu)
 */
public class MapGenerator {
    /**
     * The maximum number of each cell type that can be created in the game map;
     * For example if there were 4 Gas Stations already established, a 5th Gas
     * Station would be able to be established, however, no more Gas Stations
     * may be established after that.
     */
    private static final int CELL_TYPE_MAX_NUM = 5;
    /** The default location of the space port; */
    private static final GameCell DEF_SPACE_PORT = new GameCell(3, 3);
    /** The default location of the first objective; */
    private static final GameCell DEF_OBJ_A = new GameCell(3, 14);
    /** The default location of the second objective; */
    private static final GameCell DEF_OBJ_B = new GameCell(6, 11);
    /** The default location of the third objective; */
    private static final GameCell DEF_OBJ_C = new GameCell(9, 5);
    
    /** The number of rows in the game map to be created; */
    private int rows;
    /** The number of columns in the game map to be created; */
    private int columns;
    /** The width (in pixels) of the game map to be created; */
    private double width;
    /** The height (in pixels) of the game map to be created; */
    private double height;
    
    /** The master array for the cell types; */
    private static String[] cellTypes = CTInfo.getCTNames();
    
    /**
     * Tracks the number of cells of each type that have already been generated;
     */
    private int[] numOfTypes = new int[cellTypes.length];
    
    
    /**
     * Creates a new MapGenerator that generates on the basis of the parameters;
     * 
     * @param numOfRows the number of rows in the grid
     * @param numOfColumns the number of columns in the grid
     * @param gridHeight the height of the grid in pixels
     * @param gridWidth the width of the grid in pixels
     */
    public MapGenerator(int numOfRows, int numOfColumns, double gridHeight, double gridWidth) {
        rows = numOfRows;
        columns = numOfColumns;
        height = gridHeight;
        width = gridWidth;
    }
    
    
    /**
     * Performs all of the logic in generating a new game map and then returns
     * it;
     * 
     * @return a newly generated game map
     */
    public GameCell[][] generateGameMap() {
        Random randNumGen = new Random();
        GameCell[][] map = new GameCell[rows][columns];
        GameCell spacePort = null;
        // iterate through rows
        for (int i = 0; i < map.length; i++) {
            // iterate through columns
            for (int j = 0; j < map[i].length; j++) {
                /* BEGIN: variables used in do-while loop */
                // variable that holds a random index for the MasterCTArray
                int randIndex = -1;
                
                // variable that converts the random index to an actual cell
                // type
                String randCellType = "";
                
                // these variables are a way of identifying certain important
                // indexes
                int indexOfObjective = CTInfo.getCTIndex("Objective");
                int indexOfSpacePort = CTInfo.getCTIndex("Space Port");
                
                // ensures that the cell type is not used too many times
                boolean cellTypeUsedTooMuch = false;
                // ensures that not more than 1 Space Port is established
                boolean spacePortEstablished = false;
                // ensures that not more than 3 Objectives are established
                boolean objectivesEstablished = false;
                // these boolean tests ensure that there are no cells of the
                // same type close to
                // each other
                boolean cellAboveIsSame = false;
                boolean cellToLeftIsSame = false;
                boolean cellToTopLeftIsSame = false;
                boolean cellToTopRightIsSame = false;
                
                // these boolean tests ensure that the Space Port is established
                // and in the
                // correct area
                boolean outOfSpacePortBounds = false;
                boolean noEstablishedSpacePort = (i == DEF_SPACE_PORT.getRow()
                        && j == DEF_SPACE_PORT.getRow()) && (numOfTypes[indexOfSpacePort] == 0);
                
                // these boolean tests ensure that the Objectives are
                // established and in the
                // correct area
                boolean outOfObjectiveBounds = false;
                boolean noEstablishedObjectiveA = (i == DEF_OBJ_A.getRow()
                        && j == DEF_OBJ_A.getCol()) && (numOfTypes[indexOfObjective] == 0);
                boolean noEstablishedObjectiveB = (i == DEF_OBJ_B.getRow()
                        && j == DEF_OBJ_B.getCol()) && (numOfTypes[indexOfObjective] == 1);
                boolean noEstablishedObjectiveC = (i == DEF_OBJ_C.getRow()
                        && j == DEF_OBJ_C.getRow()) && (numOfTypes[indexOfObjective] == 2);
                
                /* END: variables used in do-while loop */
                
                /*
                 * @formatter:off (turn code formatter off for this comment)
                 * 
                 * @formatter:on (turn code formatter on for rest of code)
                 * 
                 * DO-WHILE LOOP BELOW GENERATES AN APPROPRIATE CELL TYPE FOR
                 * THE CURRENT CELL resets randIndex if: 1) corresponding
                 * cellType has already been used too much; 2) three Objectives
                 * have already been established; 3) Space Port has already been
                 * established; 4) cell above current cell has the same cell
                 * type; 5) cell to the left of current cell has the same cell
                 * type; 6) cell to the top-left of current cell has the same
                 * cell type; 7) cell to the top-right of current cell has the
                 * same cell type;
                 * 
                 * WARNING: can create infinite loop if the number of different
                 * cellTypes cannot accommodate the map size with the specified
                 * max number of each cellType
                 */
                do {
                    /*
                     * ensures that there is a Space Port by the time that map
                     * generation reaches row 3 col 3
                     */
                    if (noEstablishedSpacePort) {
                        randIndex = indexOfSpacePort;
                        // convert randIndex to a cell type
                        randCellType = cellTypes[randIndex];
                        break;
                    }
                    /*
                     * ensures there is at least one Objective by the time that
                     * map generation reaches row 3 col 14
                     */
                    if (noEstablishedObjectiveA) {
                        randIndex = indexOfObjective;
                        // convert randIndex to a cell type
                        randCellType = cellTypes[randIndex];
                        break;
                    }
                    /*
                     * ensures there are at least two Objectives by the time
                     * that map generation reaches row 6 col 11
                     */
                    if (noEstablishedObjectiveB) {
                        randIndex = indexOfObjective;
                        // convert randIndex to a cell type
                        randCellType = cellTypes[randIndex];
                        break;
                    }
                    /*
                     * ensures there are at least three Objectives by the time
                     * that map generation reaches row 9 col 5
                     */
                    if (noEstablishedObjectiveC) {
                        randIndex = indexOfObjective;
                        // convert randIndex to a cell type
                        randCellType = cellTypes[randIndex];
                        break;
                    }
                    
                    // set randIndex to an actual random number
                    randIndex = randNumGen.nextInt(cellTypes.length);
                    // convert randIndex to a cell type
                    randCellType = cellTypes[randIndex];
                    // set boolean variables to their respective tests
                    /*
                     * In the following assignments for the boolean variables,
                     * the i's represent the current row and the j's represent
                     * the current column.
                     */
                    cellTypeUsedTooMuch = numOfTypes[randIndex] >= CELL_TYPE_MAX_NUM;
                    spacePortEstablished = randIndex == indexOfSpacePort
                            && numOfTypes[indexOfSpacePort] == 1;
                    objectivesEstablished = randIndex == indexOfObjective
                            && numOfTypes[indexOfObjective] == 3;
                    cellAboveIsSame = (i > 0) && (randCellType.equals(map[i - 1][j].getCellType()));
                    cellToLeftIsSame = (j > 0)
                            && (randCellType.equals(map[i][j - 1].getCellType()));
                    cellToTopLeftIsSame = (i > 0 && j > 0)
                            && (randCellType.equals(map[i - 1][j - 1].getCellType()));
                    cellToTopRightIsSame = (i > 0 && j < columns - 1)
                            && (randCellType.equals(map[i - 1][j + 1].getCellType()));
                    outOfSpacePortBounds = randIndex == indexOfSpacePort && !(i <= 3 && j <= 3);
                    outOfObjectiveBounds = randIndex == indexOfObjective
                            && (!(j >= 8 && i < 8) || (i >= 8));
                    
                    // continue generating a different random value if any of
                    // these tests are true
                } while (cellTypeUsedTooMuch || spacePortEstablished || objectivesEstablished
                        || cellAboveIsSame || cellToLeftIsSame || cellToTopLeftIsSame
                        || cellToTopRightIsSame || outOfSpacePortBounds || outOfObjectiveBounds);
                
                
                // AFTER AN ACCEPTABLE randCellType IS GENERATED:
                
                
                // add GameCell for each column in each row with different cell
                // type
                map[i][j] = new GameCell(randCellType, i, j, false, false, true, false, false, 0, 0,
                        0, 0, false, false, false);
                map[i][j].setHeight(height / rows);
                map[i][j].setWidth(width / columns);
                
                // sets all cells to be neutral
                map[i][j].setNeutral(true);
                
                // establish random values for a cell's resources based on the
                // cell type's
                // individual resource maxs and mins
                int cellSoldierMax = CTInfo.getCTSoldierMaxs()[randIndex];
                int cellSoldierMin = CTInfo.getCTSoldierMins()[randIndex];
                int randNumSoldiers = randNumGen.nextInt((cellSoldierMax - cellSoldierMin) + 1)
                        + cellSoldierMin;
                
                int cellMaterialMax = CTInfo.getCTMaterialMaxs()[randIndex];
                int cellMaterialMin = CTInfo.getCTMaterialMins()[randIndex];
                int randNumMaterials = randNumGen.nextInt((cellMaterialMax - cellMaterialMin) + 1)
                        + cellMaterialMin;
                
                int cellCoreMax = CTInfo.getCTCoreMaxs()[randIndex];
                int cellCoreMin = CTInfo.getCTCoreMins()[randIndex];
                int randNumCores = randNumGen.nextInt((cellCoreMax - cellCoreMin) + 1)
                        + cellCoreMin;
                
                map[i][j].setSoldiers(randNumSoldiers);
                map[i][j].setMaterials(randNumMaterials);
                map[i][j].setCores(randNumCores);
                
                // sets the Space Port to scouted and captured because it is
                // where you start
                if (randCellType.equals("Space Port")) {
                    spacePort = map[i][j];
                    map[i][j].setScouted(true);
                    map[i][j].setCaptured(true);
                    map[i][j].setNeutral(false);
                }
                
                // sets the Objectives to hostile because they are what you need
                // to capture to win
                if (randCellType.equals("Objective")) {
                    map[i][j].setHostile(true);
                    map[i][j].setNeutral(false);
                    map[i][j].setEnemies(15);
                }
                
///////// TODO/////////////FOR
////////////////////////// TESTING
////////////////////////// PURPOSES
////////////////////////// ONLY!!
////////////////////////// DO NOT LEAVE THIS IN!!!!!!!!!!
// This allows the whole map to be visible when the game starts
// map[i][j].setScouted(true);
// map[i][j].setCapturable(true);
////////////////////////// END TESTING CODE
                
                // draw the GameCell's graphics
                CellGraphics.drawGraphics(map[i][j], false);
                
                // keep track of each cell type already created
                numOfTypes[randIndex]++;
                
            } // end of inner for-loop
        } // end of outer for-loop
        
        // set the cells around the spacePort to be scoutable
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[i].length; j++) {
                if (GameLogic.withinTwoSteps(map[i][j], spacePort)) {
                    map[i][j].setScoutable(true);
                }
            }
        }
        return map;
    }
    
    
    
    
    
    
}
