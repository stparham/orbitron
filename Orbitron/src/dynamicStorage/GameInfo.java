package dynamicStorage;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import staticStorage.CellGraphics;
import supportingClasses.GameCell;
import supportingClasses.MapGenerator;

/**
 * This class holds all of the information needed to create or load a game and
 * is constantly updated throughout game play. This class is serialized into a
 * save file every time that the game is saved.
 * 
 * @author Stanton Parham (stparham@ncsu.edu)
 */
public class GameInfo implements Serializable {
    
    /** The number of columns in the grid; */
    public static final int GRID_COLS = 15;
    /** The height of the grid that holds the game map; */
    public static final double GRID_HEIGHT = 1500.0;
    /** The number of rows in the grid; */
    public static final int GRID_ROWS = 10;
    /** The width of the grid that holds the game map; */
    public static final double GRID_WIDTH = 2250.0;
    
    /** The spread rate of the enemies for the EASY difficulty in nanoseconds; */
    private static final long EASY_RATE = 30 * 1000000000L; // every 30 seconds
    /** The spread rate of the enemies for the CHALLENGING difficulty in nanoseconds; */
    private static final long CHALLENGING_RATE = 15 * 1000000000L; // every 15 seconds
    /** The spread rate of the enemies for the HARD difficulty in nanoseconds; */
    private static final long HARD_RATE = 8 * 1000000000L; // every 8 seconds
    /** The spread rate of the enemies for the INSANE difficulty in nanoseconds; */
    private static final long INSANE_RATE = 2 * 1000000000L; // every 2 seconds
    
    /**
     * The initial maximum number of cores that the player begins the game with;
     * If the player attempts to collect more cores than this value without
     * increasing their maximum capacity, then the player's number of cores will
     * be kept at this value.
     */
    private static final int INIT_CORE_MAX = 100;
    /** The initial number of cores that the player begins the game with; */
    private static final int INIT_CORES = 10;
    
    /**
     * The initial maximum number of materials that the player begins the game
     * with; If the player attempts to collect more materials than this value
     * without increasing their maximum capacity, then the player's number of
     * materials will be kept at this value.
     */
    private static final int INIT_MATERIAL_MAX = 100;
    /** The initial number of materials that the player begins the game with; */
    private static final int INIT_MATERIALS = 10;
    
    /**
     * The initial maximum number of soldiers that the player begins the game
     * with; If the player attempts to collect more soldiers than this value
     * without increasing their maximum capacity, then the player's number of
     * soldiers will be kept at this value.
     */
    private static final int INIT_SOLDIER_MAX = 100;
    /** The initial number of soldiers that the player begins the game with; */
    private static final int INIT_SOLDIERS = 15;
    
    
    /**
     * The class's serial ID number; (I just made it the date that I made this
     * class serializable.)
     */
    private static final long serialVersionUID = 041616L;
    
    
    /**
     * The maximum amount of energy cores that the player can have;
     */
    private int coreMax;
    /**
     * The number of energy cores that the player currently has;
     */
    private int cores;
    /**
     * A property that formats cores and coresMax in the form: 000/000
     * (cores/coresMax);
     */
    private transient StringProperty coresString = new SimpleStringProperty(this, "coresString",
            "");
    
    /**
     * The difficulty level of this game;
     */
    private String difficulty;
    
    /**
     * Represents what type of end game took place in this game; ' ' means that
     * the game has not ended; 'W' means that the game was won; 'L' means that
     * the game was lost; 'F' means that the player forfeited;
     */
    private char endGame;
    /**
     * An array of all the game cells in the map;
     */
    private GameCell[][] gameMap;
    /**
     * The maximum amount of building materials that the player can have;
     */
    private int materialMax;
    /**
     * The number of building materials that the player currently has;
     */
    private int materials;
    
    /**
     * A property that formats materials and materialsMax in the form: 000/000
     * (materials/materialsMax);
     */
    private transient StringProperty materialsString = new SimpleStringProperty(this,
            "materialsString", "");
    
    /**
     * The player's name;
     */
    private String playerName;
    /**
     * The save slot that the game will save to;
     */
    private int saveSlot;
    /**
     * The player's current score;
     */
    private int score;
    
    /**
     * The maximum amount of soldiers that the player can have;
     */
    private int soldierMax;
    /**
     * The number of soldiers that the player currently has;
     */
    private int soldiers;
    /**
     * A property that formats soldiers and soldiersMax in the form: 000/000
     * (soldiers/soldiersMax);
     */
    private transient StringProperty soldiersString = new SimpleStringProperty(this,
            "soldiersString", "");
    
    /**
     * The rate at which the enemies in the game will spread in nanoseconds; Changing this is
     * like changing the difficulty;
     */
    private long spreadRate;
    /**
     * The amount of time that has elapsed during gameplay in nanoseconds;
     */
    private long timeElapsed;
    /**
     * The amount of time since the last enemy spread occurred in nanoseconds;
     */
    private long timeOfLastSpread;
    
    
    /**
     * Creates a new GameInfo object in saveSlot 1 with the playerName "DEFAULT"
     * and "EASY" difficulty; Generates a new random game map; Sets timeElapsed
     * to 0; Sets score to 0; Sets resource values and resource max values to
     * class constant values;
     */
    public GameInfo() {
        this(1, "DEFAULT", "EASY");
    }
    
    /**
     * Creates a new GameInfo object with the passed in saveSlot number,
     * playerName, and difficulty; Generates a new random game map; Sets
     * timeElapsed to 0; Sets score to 0; Sets resource values and resource max
     * values to class constant values;
     * 
     * @param saveSlot the save slot in which this GameInfo object will be saved
     *            (1, 2, or 3)
     * @param playerName the name of the player for this game
     * @param difficulty a String representing the difficulty of the game
     */
    public GameInfo(int saveSlot, String playerName, String difficulty) {
        this.setSaveSlot(saveSlot);
        this.setPlayerName(playerName);
        this.setDifficulty(difficulty);
        this.generateGameMap();
        this.setTimeElapsed(0);
        this.setTimeOfLastSpread(0);
        // set difficulty sets the spreadRate
        this.setScore(0);
        this.setEndGame(' ');
        this.setSoldierMax(INIT_SOLDIER_MAX);
        this.setSoldiers(INIT_SOLDIERS);
        this.setMaterialMax(INIT_MATERIAL_MAX);
        this.setMaterials(INIT_MATERIALS);
        this.setCoreMax(INIT_CORE_MAX);
        this.setCores(INIT_CORES);
    }
    
    
    /**
     * Converts nanoseconds into a more readable String format: mm:ss (m =
     * minutes / s = seconds);
     * 
     * @param time the nanoseconds to be converted
     * @return a String representation of the nanoseconds in the format mm:ss
     */
    public static String convertTimeToString(long time) {
        long diff = TimeUnit.NANOSECONDS.toSeconds(time);
        long minsL = diff / 60;
        String mins = "0" + minsL;
        String secs = "0" + (diff - (minsL * 60));
        return mins.substring(mins.length() - 2) + ":" + secs.substring(secs.length() - 2);
    }
    
    /**
     * Generates the game map for this game;
     */
    public void generateGameMap() {
        MapGenerator mapGen = new MapGenerator(GRID_ROWS, GRID_COLS, GRID_HEIGHT, GRID_WIDTH);
        gameMap = mapGen.generateGameMap();
    }
    
    /**
     * Returns the difficulty of the current game;
     * 
     * @return the difficulty of the current game
     */
    public String getDifficulty() {
        return difficulty;
    }
    
    /**
     * Returns the type of end game that occurred (Win, Loss, Forfeit);
     * 
     * @return the type of end game that occurred
     */
    public char getEndGame() {
        return endGame;
    }
    
    /**
     * Returns the current game map;
     * 
     * @return the current game map
     */
    public GameCell[][] getGameMap() {
        return gameMap;
    }
    
    /**
     * Returns the current max number of energy cores the player can have;
     * 
     * @return the current max number of energy cores the player can have
     */
    public int getCoreMax() {
        return coreMax;
    }
    
    /**
     * Returns the current number of energy cores the player has;
     * 
     * @return the current number of energy cores the player has
     */
    public int getCores() {
        return cores;
    }
    
    /**
     * Returns a String representing cores and coresMax in the form: 000/000
     * (cores/coresMax);
     * 
     * @return a String representing cores and coresMax in the form: 000/000
     *         (cores/coresMax)
     */
    public String getCoresString() {
        return coresString.get();
    }
    
    /**
     * Returns the property that formats cores and coresMax in the form: 000/000
     * (cores/coresMax);
     * 
     * @return the property that formats cores and coresMax in the form: 000/000
     *         (cores/coresMax)
     */
    public StringProperty coresStringProperty() {
        return coresString;
    }
    
    /**
     * Returns the current max number of building materials the player can have;
     * 
     * @return the current max number of building materials the player can have
     */
    public int getMaterialMax() {
        return materialMax;
    }
    
    /**
     * Returns the current number of building materials the player has;
     * 
     * @return the current number of building materials the player has
     */
    public int getMaterials() {
        return materials;
    }
    
    /**
     * Returns the property that formats materials and materialsMax in the form:
     * 000/000 (materials/materialsMax);
     * 
     * @return the property that formats materials and materialsMax in the form:
     *         000/000 (materials/materialsMax)
     */
    public StringProperty materialsStringProperty() {
        return materialsString;
    }
    
    /**
     * Returns a String representing materials and materialsMax in the form:
     * 000/000 (materials/materialsMax);
     * 
     * @return a String representing materials and materialsMax in the form:
     *         000/000 (materials/materialsMax)
     */
    public String getMaterialsString() {
        return materialsString.get();
    }
    
    /**
     * Returns the current max number of soldiers the player can have;
     * 
     * @return the current max number of soldiers the player can have
     */
    public int getSoldierMax() {
        return soldierMax;
    }
    
    /**
     * Returns the current number of soldiers the player has;
     * 
     * @return the current number of soldiers the player has
     */
    public int getSoldiers() {
        return soldiers;
    }
    
    /**
     * Returns the property that formats soldiers and soldiersMax in the form:
     * 000/000 (soldiers/soldiersMax);
     * 
     * @return the property that formats soldiers and soldiersMax in the form:
     *         000/000 (soldiers/soldiersMax)
     */
    public StringProperty soldiersStringProperty() {
        return soldiersString;
    }
    
    /**
     * Returns a String representing soldiers and soldiersMax in the form:
     * 000/000 (soldiers/soldiersMax);
     * 
     * @return a String representing soldiers and soldiersMax in the form:
     *         000/000 (soldiers/soldiersMax)
     */
    public String getSoldiersString() {
        return soldiersString.get();
    }
    
    /**
     * Returns the number of cells that have been captured by the player;
     * 
     * @return the number of cells that have been captured by the player
     */
    public int getNumCellsCapped() {
        int count = 0;
        // get the number of cells/objectives that have been captured
        for (int i = 0; i < gameMap.length; i++) {
            for (int j = 0; j < gameMap[i].length; j++) {
                if (gameMap[i][j].isCaptured()) {
                    count++;
                }
            }
        }
        return count;
    }
    
    /**
     * Returns the number of objectives that have been captured by the player;
     * 
     * @return the number of objectives that have been captured by the player
     */
    public int getNumObjsCapped() {
        int count = 0;
        // get the number of cells/objectives that have been captured
        for (int i = 0; i < gameMap.length; i++) {
            for (int j = 0; j < gameMap[i].length; j++) {
                if (gameMap[i][j].isCaptured() && gameMap[i][j].getCellType().equals("Objective")) {
                    count++;
                }
            }
        }
        return count;
    }
    
    /**
     * Returns the player's name;
     * 
     * @return the player's name
     */
    public String getPlayerName() {
        return playerName;
    }
    
    /**
     * Returns the save slot that this game is saved in;
     * 
     * @return the save slot that this game is saved in
     */
    public int getSaveSlot() {
        return saveSlot;
    }
    
    /**
     * Returns the player's score;
     * 
     * @return the player's score
     */
    public int getScore() {
        calculateScore();
        return score;
    }
    
    /**
     * Returns the spread rate of enemies in nanoseconds (determined by difficulty);
     * 
     * @return the spread rate of enemies in nanoseconds
     */
    public long getSpreadRate() {
        return spreadRate;
    }
    
    /**
     * Returns the time elapsed in the game in nanoseconds;
     * 
     * @return the time elapsed in the game in nanoseconds
     */
    public long getTimeElapsed() {
        return timeElapsed;
    }
    
    /**
     * Returns the time elapsed in the game as a formatted String;
     * 
     * @return the time elapsed in the game as a formatted String
     */
    public String getTimeElapsedString() {
        return convertTimeToString(timeElapsed);
    }
    
    /**
     * Returns the time the enemies last spread in nanoseconds;
     * 
     * @return the time the enemies last spread in nanoseconds
     */
    public long getTimeOfLastSpread() {
        return timeOfLastSpread;
    }
    
    /**
     * Returns the time the enemies last spread as a formatted String;
     * 
     * @return the time the enemies last spread as a formatted String
     */
    public String getTimeOfLastSpreadString() {
        return convertTimeToString(timeOfLastSpread);
    }
    
    /**
     * Redraws the map's cells; THIS METHOD SHOULD ONLY BE USED WHEN LOADING A
     * PREVIOUS GAME.
     */
    public void redrawGameMap() {
        for (int i = 0; i < gameMap.length; i++) {
            for (int j = 0; j < gameMap[i].length; j++) {
                CellGraphics.drawGraphics(gameMap[i][j], false);
            }
        }
    }
    
    /**
     * Regenerates the string properties used by the game UI; They need to be
     * regenerated because they are not reloaded along with the rest of the
     * GameInfo object when read from the save file because properties aren't
     * serializable. THIS METHOD SHOULD ONLY BE USED WHEN LOADING A PREVIOUS
     * GAME.
     */
    public void regenerateStringProperties() {
        this.soldiersString = new SimpleStringProperty(this, "soldiersString", "");
        updateSoldiersStringProperty();
        this.materialsString = new SimpleStringProperty(this, "materialsString", "");
        updateMaterialsStringProperty();
        this.coresString = new SimpleStringProperty(this, "coresString", "");
        updateCoresStringProperty();
    }
    
    /**
     * Sets the max number of energy cores the player can have;
     * 
     * @param value the max number of energy cores the player can have
     */
    public void setCoreMax(int value) {
        if (value < INIT_CORE_MAX) {
            throw new IllegalArgumentException("coreMax must be at least " + INIT_CORE_MAX);
        }
        
        this.coreMax = value;
        updateCoresStringProperty();
    }
    
    /**
     * Sets the number of energy cores the player has;
     * 
     * @param value the number of energy cores the player has
     * @return whether or not the cores were successfully set
     */
    public boolean setCores(int value) {
        if (value < 0) {
            return false; // cores must be non-negative
        }
        
        if (value > coreMax) {
            this.cores = coreMax;
            updateCoresStringProperty();
            return false; // cores cannot be greater than core max
        } else {
            this.cores = value;
            updateCoresStringProperty();
            return true;
        }
    }
    
    /**
     * Sets a String representing cores and coresMax in the form: 000/000
     * (cores/coresMax);
     * 
     * @param value a String representing cores and coresMax in the form:
     *            000/000 (cores/coresMax)
     */
    public void setCoresString(String value) {
        this.coresString.set(value);
    }
    
    /**
     * Sets the difficulty of the current game;
     * 
     * @param value the difficulty of the current game
     */
    public void setDifficulty(String value) {
        if (value.equals("EASY")) {
            this.setSpreadRate(EASY_RATE);
        } else if (value.equals("CHALLENGING")) {
            this.setSpreadRate(CHALLENGING_RATE);
        } else if (value.equals("HARD")) {
            this.setSpreadRate(HARD_RATE);
        } else if (value.equals("INSANE")) {
            this.setSpreadRate(INSANE_RATE);
        } else {
            throw new IllegalArgumentException(
                    "difficulty must be EASY, CHALLENGING, HARD, or INSANE");
        }
        this.difficulty = value;
    }
    
    /**
     * Sets the type of end game that occurred (Win, Loss, Forfeit);
     * 
     * @param value the type of end game that occurred
     */
    public void setEndGame(char value) {
        if (value != 'W' && value != 'L' && value != 'F' && value != ' ') {
            throw new IllegalArgumentException("endGame must be 'W', 'L', 'F', or ' '");
        }
        
        this.endGame = value;
    }
    
    /**
     * Sets the map for the current game;
     * 
     * @param value the map for the current game
     */
    public void setGameMap(GameCell[][] value) {
        this.gameMap = value;
    }
    
    /**
     * Sets the max number of building materials the player can have;
     * 
     * @param value the max number of building materials the player can have
     */
    public void setMaterialMax(int value) {
        if (value < INIT_MATERIAL_MAX) {
            throw new IllegalArgumentException("materialMax must be at least " + INIT_MATERIAL_MAX);
        }
        
        this.materialMax = value;
        updateMaterialsStringProperty();
    }
    
    /**
     * Sets the number of building materials the player has;
     * 
     * @param value the number of building materials the player has
     * @return whether or not the materials were successfully set
     */
    public boolean setMaterials(int value) {
        if (value < 0) {
            return false; // materials must be non-negative
        }
        
        if (value > materialMax) {
            this.materials = materialMax;
            updateMaterialsStringProperty();
            return false; // materials cannot be greater than material max
        } else {
            this.materials = value;
            updateMaterialsStringProperty();
            return true;
        }
    }
    
    /**
     * Sets a String representing materials and materialsMax in the form: 000/000
     * (materials/materialsMax);
     * 
     * @param value a String representing materials and materialsMax in the form:
     *            000/000 (materials/materialsMax)
     */
    public void setMaterialsString(String value) {
        this.materialsString.set(value);
    }
    
    /**
     * Sets the player's name for the current game;
     * 
     * @param value the player's name the current game
     */
    public void setPlayerName(String value) {
        this.playerName = value;
    }
    
    /**
     * Sets the save slot for the current game;
     * 
     * @param value the save slot for the current game
     */
    public void setSaveSlot(int value) {
        this.saveSlot = value;
    }
    
    /**
     * Sets the max number of soldiers the player can have;
     * 
     * @param value the max number of soldiers the player can have
     */
    public void setSoldierMax(int value) {
        if (value < INIT_SOLDIER_MAX) {
            throw new IllegalArgumentException("soldierMax must be at least " + INIT_SOLDIER_MAX);
        }
        
        this.soldierMax = value;
        updateSoldiersStringProperty();
    }
    
    /**
     * Sets the number of soldiers the player has;
     * 
     * @param value the number of soldiers the player has
     * @return whether or not the soldiers were successfully set
     */
    public boolean setSoldiers(int value) {
        if (value < 0) {
            return false; // soldiers must be non-negative
        }
        
        if (value > soldierMax) {
            this.soldiers = soldierMax;
            updateSoldiersStringProperty();
            return false; // soldiers cannot be greater than soldier max
        } else {
            this.soldiers = value;
            updateSoldiersStringProperty();
            return true;
        }
    }
    
    /**
     * Sets a String representing soldiers and soldiersMax in the form: 000/000
     * (soldiers/soldiersMax);
     * 
     * @param value a String representing soldiers and soldiersMax in the form:
     *            000/000 (soldiers/soldiersMax)
     */
    public void setSoldiersString(String value) {
        this.soldiersString.set(value);
    }
    
    /**
     * Sets the spread rate of enemies in nanoseconds;
     * 
     * @param value the spread rate of enemies in nanoseconds
     */
    public void setSpreadRate(long value) {
        this.spreadRate = value;
    }
    
    /**
     * Sets the time elapsed in nanoseconds;
     * 
     * @param value the time elapsed in nanoseconds
     */
    public void setTimeElapsed(long value) {
        if (value < 0) {
            throw new IllegalArgumentException("time must be non-negative");
        }
        this.timeElapsed = value;
    }
    
    /**
     * Sets the time since enemies last spread in nanoseconds;
     * 
     * @param value the time since enemies last spread in nanoseconds
     */
    public void setTimeOfLastSpread(long value) {
        this.timeOfLastSpread = value;
    }
    
    
    /**
     * Calculates the score based on many of the factors present in the game;
     */
    private void calculateScore() {
        int numObjsCapped = getNumObjsCapped();
        int numCellsCapped = getNumCellsCapped();
        
        // add up soldiers, materials, cores, capped objectives, capped cells
        // in their respective proportions
        double result = soldiers / 5 + materials / 10 + cores / 10 + numObjsCapped * 100
                + numCellsCapped;
        
        // add in any end of game bonuses/penalties
        if (endGame == 'W') {
            result += 150;
        } else if (endGame == 'L') {
            result -= 10;
        } else if (endGame == 'F') {
            result -= 50;
        }
        
        // subtract one point for every 10 seconds of play time
        result -= timeElapsed / 10000000000L;
        
        if (result > 0) {// only multiply the result by the difficulty if it's
                         // positive
            // multiply the score by 1 over the spreadRate in decaseconds (10 seconds)
            result *= 1 / ((double) spreadRate / 10000000000L);
        }
        // set the score
        score = (int) result;
    }
    
    
    private void setScore(int value) {
        this.score = value;
    }
    
    private void updateCoresStringProperty() {
        coresString.set(cores + "/" + coreMax);
    }
    
    
    private void updateMaterialsStringProperty() {
        materialsString.set(materials + "/" + materialMax);
    }
    
    
    private void updateSoldiersStringProperty() {
        soldiersString.set(soldiers + "/" + soldierMax);
    }
    
    
}
