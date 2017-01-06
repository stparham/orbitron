package supportingClasses;

import java.io.Serializable;

import javafx.scene.canvas.Canvas;
import staticStorage.CTInfo;

/**
 * This class is the basis for all game play. These hold in-game information and
 * display graphics.
 * 
 * @author Stanton Parham (stparham@ncsu.edu)
 */
public class GameCell extends Canvas implements Serializable {
    
    /**
     * The class's serial ID number; (I just made it the date that I made this
     * class serializable.)
     */
    private static final long serialVersionUID = 041616L;
    
    
    /**
     * The maximum number of enemies allowed to occupy any one cell;
     */
    public static final int MAX_ENEMIES = 100;
    
    
    /* BASIC INFO */
    // hold the basic info of the cell
    /**
     * The cell type represented as a String;
     */
    private String cellType;
    /**
     * The index of the cell's type in CTInfo.CTVals;
     */
    private int cellTypeIndex;
    /**
     * The cell's row number in the map grid;
     */
    private int row;
    /**
     * The cell's column number in the map grid;
     */
    private int col;
    
    /* CURRENT STATE BOOLEANS */
    // hold the state of the cell
    /**
     * Whether or not the cell is captured;
     */
    private boolean captured;
    /**
     * Whether or not the cell has been scouted;
     */
    private boolean scouted;
    /**
     * Whether or not the cell is neutral;
     */
    private boolean neutral;
    /**
     * Whether or not the cell is hostile;
     */
    private boolean hostile;
    /**
     * Whether or not the cell has been destroyed;
     */
    private boolean destroyed;
    /**
     * Whether or not a building has been built on this cell;
     */
    private boolean builtOn;
    
    /* POTENTIAL STATE BOOLEANS */
    // tell whether or not a cell can change its state to another
    /**
     * Whether or not the cell can be captured;
     */
    private boolean capturable;
    /**
     * Whether or not the cell can be scouted;
     */
    private boolean scoutable;
    /**
     * Whether or not the cell can be destroyed;
     */
    private boolean destroyable;
    
    /* RESOURCE/ENEMY VALS */
    // hold the values for the number of resources and enemies in the cell
    /**
     * The number of soldiers that the cell currently holds;
     */
    private int soldiers;
    /**
     * The number of building materials that the cell currently holds;
     */
    private int materials;
    /**
     * The number of energy cores that the cell currently holds;
     */
    private int cores;
    /**
     * The number of enemies currently in the cell;
     */
    private int enemies;
    /**
     * The number of soldiers stationed in this cell that can fend off enemies;
     */
    private int stationedSoldiers;
    
    /* CELL CONVERSION BOOLEANS */
    /**
     * Whether or not the cell can be converted into a housing cell;
     */
    private boolean housingCompatible = false;
    /**
     * Whether or not the cell can be converted into a robotics factory cell;
     */
    private boolean rFactoryCompatible = false;
    /**
     * Whether or not the cell can be converted into a storage cell;
     */
    private boolean storageCompatible = false;
    /**
     * Whether or not the cell can be converted into a steel mill cell;
     */
    private boolean sMillCompatible = false;
    /**
     * Whether or not the cell can be converted into an energy grid cell;
     */
    private boolean eGridCompatible = false;
    /**
     * Whether or not the cell can be converted into a solar farm cell;
     */
    private boolean sFarmCompatible = false;
    
    
    /**
     * Creates a new GameCell object using the given cell type, row, and column;
     * This should be the main constructor used (especially when generating
     * maps);
     * 
     * @param cellType the type of game cell
     * @param row the cell's row in a grid layout
     * @param col the cell's column in a grid layout
     * @param captured whether or not the GameCell is initially captured
     * @param scouted whether or not the GameCell is initially scouted
     * @param neutral whether or not the GameCell is initially neutral
     * @param hostile whether or not the GameCell is initially hostile
     * @param destroyed whether or not the GameCell is initially destroyed
     * @param soldiers the number of soldiers that the cell initially contains
     * @param materials the number of building materials that the cell initially
     *            contains
     * @param cores the number of energy cores that the cell initially contains
     * @param enemies the number of enemies that the cell initially contains
     * @param capturable whether or not this cell is capturable
     * @param scoutable whether or not this cell is scoutable
     * @param destroyable whether or not this cell is destroyable
     */
    public GameCell(String cellType, int row, int col, boolean captured, boolean scouted,
            boolean neutral, boolean hostile, boolean destroyed, int soldiers, int materials,
            int cores, int enemies, boolean capturable, boolean scoutable, boolean destroyable) {
        super();
        this.cellType = cellType;
        this.cellTypeIndex = CTInfo.getCTIndex(cellType);
        this.row = row;
        this.col = col;
        this.setCaptured(captured);
        this.setScouted(scouted);
        this.setNeutral(neutral);
        this.setHostile(hostile);
        this.setDestroyed(destroyed);
        this.setCapturable(capturable);
        this.setScoutable(scoutable);
        this.setDestroyable(destroyable);
        this.setSoldiers(soldiers);
        this.setMaterials(materials);
        this.setCores(cores);
        this.setEnemies(enemies);
        
        this.housingCompatible = CTInfo.isHousingCompatible(cellType);
        this.rFactoryCompatible = CTInfo.isRFactoryCompatible(cellType);
        this.storageCompatible = CTInfo.isStorageCompatible(cellType);
        this.sMillCompatible = CTInfo.isSMillCompatible(cellType);
        this.eGridCompatible = CTInfo.isEGridCompatible(cellType);
        this.sFarmCompatible = CTInfo.isSFarmCompatible(cellType);
    }
    
    /**
     * Constructs a new GameCell with the given row and column number (only used
     * for storing a row and col value for MapGenerator);
     * 
     * @param row the row of the cell
     * @param col the column of the cell
     */
    protected GameCell(int row, int col) {
        this(null, row, col, false, false, false, false, false, 0, 0, 0, 0, false, false, false);
    }
    
    /**
     * Returns the type of this cell;
     * 
     * @return the type of this cell
     */
    public String getCellType() {
        return cellType;
    }
    
    /**
     * Returns the index of this cell's type in CTInfo.CTVals;
     * 
     * @return the index of this cell's type in CTInfo.CTVals
     */
    public int getCellTypeIndex() {
        return cellTypeIndex;
    }
    
    /**
     * Returns the row of this cell;
     * 
     * @return the row of this cell
     */
    public int getRow() {
        return row;
    }
    
    /**
     * Returns the column of this cell;
     * 
     * @return the column of this cell
     */
    public int getCol() {
        return col;
    }
    
    /**
     * Returns whether or not this cell is captured;
     * 
     * @return whether or not this cell is captured
     */
    public boolean isCaptured() {
        return captured;
    }
    
    /**
     * Returns whether or not this cell is scouted;
     * 
     * @return whether or not this cell is scouted
     */
    public boolean isScouted() {
        return scouted;
    }
    
    /**
     * Returns whether or not this cell is neutral;
     * 
     * @return whether or not this cell is neutral
     */
    public boolean isNeutral() {
        return neutral;
    }
    
    /**
     * Returns whether or not this cell is hostile;
     * 
     * @return whether or not this cell is hostile
     */
    public boolean isHostile() {
        return hostile;
    }
    
    /**
     * Returns whether or not this cell is destroyed;
     * 
     * @return whether or not this cell is destroyed
     */
    public boolean isDestroyed() {
        return destroyed;
    }
    
    /**
     * Returns whether or not this cell is able to be captured;
     * 
     * @return whether or not this cell is able to be captured
     */
    public boolean isCapturable() {
        return capturable;
    }
    
    /**
     * Returns whether or not this cell is able to be scouted;
     * 
     * @return whether or not this cell is able to be scouted
     */
    public boolean isScoutable() {
        return scoutable;
    }
    
    /**
     * Returns whether or not this cell is able to be destroyed;
     * 
     * @return whether or not this cell is able to be destroyed
     */
    public boolean isDestroyable() {
        return destroyable;
    }
    
    /**
     * Returns whether or not this cell is built on;
     * 
     * @return whether or not this cell is built on
     */
    public boolean isBuiltOn() {
        return builtOn;
    }
    
    /**
     * Returns the number of soldiers held in this cell (as a resource);
     * 
     * @return the number of soldiers held in this cell (as a resource)
     */
    public int getSoldiers() {
        return soldiers;
    }
    
    /**
     * Returns the number of building materials held in this cell;
     * 
     * @return the number of building materials held in this cell
     */
    public int getMaterials() {
        return materials;
    }
    
    /**
     * Returns the number of energy cores held in this cell;
     * 
     * @return the number of energy cores held in this cell
     */
    public int getCores() {
        return cores;
    }
    
    /**
     * Returns the number of enemies in this cell;
     * 
     * @return the number of enemies in this cell
     */
    public int getEnemies() {
        return enemies;
    }
    
    /**
     * Returns the number of stationed soldiers in this cell;
     * 
     * @return the number of stationed soldiers in this cell
     */
    public int getStationedSoldiers() {
        return stationedSoldiers;
    }
    
    /**
     * Returns whether or not this cell can have Housing built on it;
     * 
     * @return whether or not this cell can have Housing built on it
     */
    public boolean isHousingCompatible() {
        return housingCompatible;
    }
    
    /**
     * Returns whether or not this cell can have a Robotics Factory built on it;
     * 
     * @return whether or not this cell can have a Robotics Factory built on it
     */
    public boolean isRFactoryCompatible() {
        return rFactoryCompatible;
    }
    
    /**
     * Returns whether or not this cell can have Storage built on it;
     * 
     * @return whether or not this cell can have Storage built on it
     */
    public boolean isStorageCompatible() {
        return storageCompatible;
    }
    
    /**
     * Returns whether or not this cell can have a Steel Mill built on it;
     * 
     * @return whether or not this cell can have a Steel Mill built on it
     */
    public boolean isSMillCompatible() {
        return sMillCompatible;
    }
    
    /**
     * Returns whether or not this cell can have an Energy Grid built on it;
     * 
     * @return whether or not this cell can have an Energy Grid built on it
     */
    public boolean isEGridCompatible() {
        return eGridCompatible;
    }
    
    /**
     * Returns whether or not this cell can have a Solar Farm built on it;
     * 
     * @return whether or not this cell can have a Solar Farm built on it
     */
    public boolean isSFarmCompatible() {
        return sFarmCompatible;
    }
    
    /**
     * Sets the cell's cellType field; WARNING: this method should only be
     * called when building structures on cells;
     * 
     * @param value the cell type to be changed to
     */
    public void setCellType(String value) {
        this.cellType = value;
    }
    
    /**
     * Sets whether or not this cell is captured;
     * 
     * @param value whether or not this cell is captured
     */
    public void setCaptured(boolean value) {
        captured = value;
        recalcPotentialStates();
    }
    
    /**
     * Sets whether or not this cell is scouted;
     * 
     * @param value whether or not this cell is scouted
     */
    public void setScouted(boolean value) {
        scouted = value;
        recalcPotentialStates();
    }
    
    /**
     * Sets whether or not this cell is neutral;
     * 
     * @param value whether or not this cell is neutral
     */
    public void setNeutral(boolean value) {
        neutral = value;
    }
    
    /**
     * Sets whether or not this cell is hostile;
     * 
     * @param value whether or not this cell is hostile
     */
    public void setHostile(boolean value) {
        hostile = value;
    }
    
    /**
     * Sets whether or not this cell is destroyed;
     * 
     * @param value whether or not this cell is destroyed
     */
    public void setDestroyed(boolean value) {
        destroyed = value;
        recalcPotentialStates();
    }
    
    /**
     * Sets whether or not this cell is able to be captured;
     * 
     * @param value whether or not this cell is able to be captured
     */
    public void setCapturable(boolean value) {
        capturable = value;
    }
    
    /**
     * Sets whether or not this cell is able to be scouted;
     * 
     * @param value whether or not this cell is able to be scouted
     */
    public void setScoutable(boolean value) {
        scoutable = value;
    }
    
    /**
     * Sets whether or not this cell is able to be destroyed;
     * 
     * @param value whether or not this cell is able to be destroyed
     */
    public void setDestroyable(boolean value) {
        destroyable = value;
    }
    
    /**
     * Sets whether or not this cell is built on;
     * 
     * @param value whether or not this cell is built on
     */
    public void setBuiltOn(boolean value) {
        builtOn = value;
    }
    
    /**
     * Sets the number of soldiers held in this cell (as resources);
     * 
     * @param value the number of soldiers held in this cell (as resources)
     */
    public void setSoldiers(int value) {
        soldiers = value;
    }
    
    /**
     * Sets the number of building materials held in this cell;
     * 
     * @param value the number of building materials held in this cell
     */
    public void setMaterials(int value) {
        materials = value;
    }
    
    /**
     * Sets the number of energy cores held in this cell;
     * 
     * @param value the number of energy cores held in this cell
     */
    public void setCores(int value) {
        cores = value;
    }
    
    /**
     * Sets the number of enemies in this cell;
     * 
     * @param value the number of enemies
     * @return whether or not enemies were set to the passed in value or to 0 or
     *         the max (true means value was set; false means either 0 or max
     *         was set)
     */
    public boolean setEnemies(int value) {
        if (value < 0) {
            enemies = 0;
            return false;
        }
        
        if (value > MAX_ENEMIES) {
            enemies = MAX_ENEMIES;
            return false;
        } else {
            enemies = value;
            return true;
        }
    }
    
    /**
     * Sets the number of stationed soldiers in this cell;
     * 
     * @param value the number of stationed soldiers
     * 
     * @throws IllegalArgumentException if value is negative
     * 
     * @return whether or not stationedSoldiers was successfully set to the
     *         passed in value
     */
    public boolean setStationedSoldiers(int value) {
        if (value < 0) {
            throw new IllegalArgumentException("stationedSoldiers must be non-negative");
        }
        
        stationedSoldiers = value;
        return true;
    }
    
    
    /**
     * Determines the potential states of the cell based on the current state of
     * the cell;
     */
    private void recalcPotentialStates() {
        if (this.captured) { // if captured (and by definition also scouted)
            this.capturable = false;
            this.scoutable = false;
            this.destroyable = true;
        } else if (this.scouted) { // if just scouted
            /*
             * when a cell is scouted, its potential for being captured is still
             * dependent on other factors so I'm not including a capturable
             * assignment here
             */
            this.scoutable = false;
            this.destroyable = true;
        } else if (this.destroyed) { // if cell has been destroyed
            this.capturable = false;
            this.scoutable = false;
            this.destroyable = false;
        }
    }
    
}
