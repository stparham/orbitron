package main;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Random;

import dynamicStorage.GameInfo;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import staticStorage.Animations;
import supportingClasses.GameCell;
import supportingClasses.GameCellContainer;

/**
 * Analyzes the current game map and performs calculations for running the game;
 * 
 * <pre>
 * TERMINOLOGY
 * 
 * 1) enemy spread - an event where the enemies in each hostile cell spread to
 * one random cell that is one step away; each enemy spread is accompanied by
 * objectives regenerating their enemies; occurs at different times depending on
 * the difficulty of the current game;
 * 
 * 2) step - A step is simply the distance from one cell to another. A step can be 
 * horizontal or vertical. To find the number of steps from one cell to another, count 
 * from the 1st cell's row to the 2nd cell's row and then to the 2nd cell's column.
 * 
 * In the figure below: the distance from [a] to [b] or [e] is 1 step; the
 * distance from [a] to [i], [f], or [c] is 2 steps; the distance from [a] to
 * [m], [d], [j], or [g] is 3 steps; the distance from [a] to [k] is 4 steps;
 * the distance from [a] to [p] is 6 steps.
 * 
 * [a][b][c][d] 
 * [e][f][g][h] 
 * [i][j][k][l] 
 * [m][n][o][p]
 * </pre>
 * 
 * 
 * @author Stanton Parham (stparham@ncsu.edu)
 */
public class GameLogic {
    
    
    /** The number of columns in the grid; */
    public static final int GRID_COLS = 15;
    /** The height of the grid that holds the game map; */
    public static final double GRID_HEIGHT = 1500.0;
    /** The number of rows in the grid; */
    public static final int GRID_ROWS = 10;
    /** The width of the grid that holds the game map; */
    public static final double GRID_WIDTH = 2250.0;
    
    /**
     * The amount of resources that are converted when a resource's
     * corresponding "conversion" cell is built;
     */
    private static final int CONVERSION_RATE = 50;
    /**
     * The default number of soldiers that are stationed in a cell when it is
     * captured;
     */
    private static final int DEF_STATIONED_SOLDIERS = 1;
    /**
     * The maximum number of enemies that an objective can regenerate each enemy
     * spread;
     */
    private static final int MAX_REGEN_ENEMIES = 20;
    /**
     * The minimum number of enemies that a hostile cell must have to spread its
     * enemies to an adjacent cell;
     */
    private static final int MIN_ENEMIES_TO_SPREAD = 10;
    
    /**
     * The minimum number of enemies that an objective can regenerate each enemy
     * spread;
     */
    private static final int MIN_REGEN_ENEMIES = 10;
    /**
     * The amount by which each of the player's resource maximums increase when
     * the resource's corresponding "holder" cell is built;
     */
    private static final int RESOURCE_MAX_INCREASE = 50;
    
    
    /** The cell currently being clicked on, or operated on in any way; */
    private GameCell activeCell;
    /**
     * A Service object is JavaFX's specialized form of a Thread object. This
     * thread constantly updates the in-game timer and score and spreads enemies
     * depending on the difficulty of the game.
     */
    private Service<Boolean> bgThread;
    /** The GameInfo object that the current game is based off of; */
    private GameInfo gameInfo;
    
    /** The array holding all of the cells in the current game; */
    private GameCell[][] gameMap;
    /** The GameUIController instance; */
    private GameUIController gameUI;
    
    /** One of the three objectives; */
    private GameCell obj1;
    /** One of the three objectives; */
    private GameCell obj2;
    /** One of the three objectives; */
    private GameCell obj3;
    
    /** The Space Port: the cell that you start with; */
    private GameCell spacePort;
    
    
    /**
     * Creates a new GameLogic object with the passed GameInfo object
     * 
     * @param g the GameInfo object used in this GameLogic object
     */
    public GameLogic(GameInfo g) {
        this.gameInfo = g;
        this.gameUI = GameUIController.getInstance();
        this.gameMap = this.gameInfo.getGameMap();
        this.initializeGameMap();
    }
    
    
    /**
     * Determines if the two passed in cells are within one step of each other;
     * 
     * @param gC1 the first cell to be compared
     * @param gC2 the second cell to be compared
     * 
     * @return whether or no the cells are within one step of each other
     */
    public static boolean withinOneStep(GameCell gC1, GameCell gC2) {
        int r1 = gC1.getRow();
        int c1 = gC1.getCol();
        int r2 = gC2.getRow();
        int c2 = gC2.getCol();
        
        // checks if GC2 is in one of the spots shown below in relation to GC1
        // X X X X X
        // X X 2 X X
        // X o 1 o X
        // X X 2 X X
        // X X X X X
        if (Math.abs(c1 - c2) == 0 && Math.abs(r1 - r2) <= 1) {
            return true;
        }
        
        // checks if GC2 is in one of the spots shown below in relation to GC1
        // X X X X X
        // X X o X X
        // X 2 1 2 X
        // X X o X X
        // X X X X X
        if (Math.abs(c1 - c2) <= 1 && Math.abs(r1 - r2) == 0) {
            return true;
        }
        
        return false;
    }
    
    /**
     * Determines if the two passed in cells are within two steps of each other;
     * 
     * @param gC1 the first cell to be compared
     * @param gC2 the second cell to be compared
     * 
     * @return whether or not the two cells are within two steps of each other
     */
    public static boolean withinTwoSteps(GameCell gC1, GameCell gC2) {
        int r1 = gC1.getRow();
        int c1 = gC1.getCol();
        int r2 = gC2.getRow();
        int c2 = gC2.getCol();
        
        // checks if GC2 is in one of the spots shown below in relation to GC1
        // X X X X X X X
        // X X X o X X X
        // X X 2 2 2 X X
        // X o 2 1 2 o X
        // X X 2 2 2 X X
        // X X X o X X X
        // X X X X X X X
        if (Math.abs(c1 - c2) <= 1 && Math.abs(r1 - r2) <= 1) {
            return true;
        }
        
        // checks if GC2 is in one of the spots shown below in relation to GC1
        // X X X X X X X
        // X X X 2 X X X
        // X X o 2 o X X
        // X o o 1 o o X
        // X X o 2 o X X
        // X X X 2 X X X
        // X X X X X X X
        if (Math.abs(c1 - c2) == 0 && Math.abs(r1 - r2) <= 2) {
            return true;
        }
        
        // checks if GC2 is in one of the spots shown below in relation to GC1
        // X X X X X X X
        // X X X o X X X
        // X X o o o X X
        // X 2 2 1 2 2 X
        // X X o o o X X
        // X X X o X X X
        // X X X X X X X
        if (Math.abs(c1 - c2) <= 2 && Math.abs(r1 - r2) == 0) {
            return true;
        }
        
        return false;
    }
    
    /**
     * Performs all the game logic related to bombarding (destroying) a cell;
     * 
     * @param bombardCost the intensity of the bombardment based on the number
     *            of energy cores that the player decided to use when bombarding
     * @param tutorialMode whether or not the tutorial is taking place (wherever
     *            tutorialMode is seen it means that it is needed to ensure that
     *            the tutorial does not change the player's resources, captured
     *            cells, or other stats before the game even starts)
     * 
     * @return whether or not the cell was able to be bombarded based on the
     *         player's resources
     */
    public boolean bombardCell(int bombardCost, boolean tutorialMode) {
        
        if (gameInfo.getCores() >= bombardCost || tutorialMode) {
            if (!tutorialMode) {
                // UPDATE PLAYER RESOURCE TOTALS
                gameInfo.setCores(gameInfo.getCores() - bombardCost);
            }
            /*
             * destroy the cell only if the setEnemies() returns false which
             * means that the bombard cost was greater than the enemies because
             * setEnemies() returns false when the passed in value is negative
             */
            if (!activeCell.setEnemies(activeCell.getEnemies() - bombardCost)) {
                // UPDATE CELL VALUES
                // the active cell is now destroyed
                activeCell.setDestroyed(true);
                // is no longer hostile
                activeCell.setHostile(false);
                // is no longer captured
                activeCell.setCaptured(false);
                // is now neutral
                activeCell.setNeutral(true);
                if (!tutorialMode) {
                    setPotentialStatesOf(activeCell);
                    setPotentialStatesOfCellsAround(activeCell);
                }
                /*
                 * destroy any resources held within the cell; set the cell's
                 * resources to 0 effectively "destroying" them
                 */
                activeCell.setSoldiers(0);
                activeCell.setMaterials(0);
                activeCell.setCores(0);
            }
            return true;
        }
        
        return false;
        
        
    }
    
    /**
     * Performs all of the game logic dealing with building a structure on a
     * cell;
     * 
     * @param option the type of structure being built on the cell
     * @param tutorialMode whether or not the tutorial is taking place (wherever
     *            tutorialMode is seen it means that it is needed to ensure that
     *            the tutorial does not change the player's resources, captured
     *            cells, or other stats before the game even starts)
     * 
     * @return whether or not the structure was able to be built based on the
     *         player's resources
     */
    public boolean build(String option, boolean tutorialMode) {
        if (!option.equals("Housing") && !option.equals("Robotics Factory")
                && !option.equals("Storage") && !option.equals("Steel Mill")
                && !option.equals("Energy Grid") && !option.equals("Solar Farm")) {
            throw new IllegalArgumentException("option must be Housing, Robotics Factory, "
                    + "Storage, Steel Mill, Energy Grid, or Solar Farm");
        }
        
        // establish different costs for building structures
        int housingCost = 20;
        int rFactoryCost = 5;
        int storageCost = 20;
        int sMillCost = 5;
        int eGridCost = 20;
        int sFarmCost = 5;
        
        if ((option.equals("Housing") && gameInfo.getMaterials() >= housingCost) || tutorialMode) {
            // set the activeCell's builtOn field
            activeCell.setBuiltOn(true);
            // set the cell type to Housing
            activeCell.setCellType("Housing");
            if (!tutorialMode) {
                // subtract costs
                gameInfo.setMaterials(gameInfo.getMaterials() - housingCost);
                
                /*
                 * perform all logic that results from building a Housing cell;
                 * increase the maximum number of soldiers that the player can
                 * have
                 */
                gameInfo.setSoldierMax(gameInfo.getSoldierMax() + RESOURCE_MAX_INCREASE);
            }
            return true;
        } else if ((option.equals("Robo Factory")
                && gameInfo.getMaterials() >= rFactoryCost + CONVERSION_RATE / 2
                && gameInfo.getCores() >= CONVERSION_RATE / 2) || tutorialMode) {
            // set the activeCell's builtOn field
            activeCell.setBuiltOn(true);
            // set the cell type to Robotics Factory
            activeCell.setCellType("Robotics Factory");
            if (!tutorialMode) {
                // subtract costs
                gameInfo.setMaterials(gameInfo.getMaterials() - rFactoryCost);
                /*
                 * perform all logic that results from building a Robotics
                 * Factory cell; convert materials and cores into soldiers
                 * subtract half of the conversion amount from both materials
                 * and cores
                 */
                gameInfo.setMaterials(gameInfo.getMaterials() - (CONVERSION_RATE / 2));
                gameInfo.setCores(gameInfo.getCores() - (CONVERSION_RATE / 2));
                // add the conversion amount to soldiers
                gameInfo.setSoldiers(gameInfo.getSoldiers() + CONVERSION_RATE);
            }
            return true;
        } else if ((option.equals("Storage") && gameInfo.getMaterials() >= storageCost)
                || tutorialMode) {
            // set the activeCell's builtOn field
            activeCell.setBuiltOn(true);
            // set the cell type to Storage
            activeCell.setCellType("Storage");
            if (!tutorialMode) {
                // subtract costs
                gameInfo.setMaterials(gameInfo.getMaterials() - storageCost);
                /*
                 * perform all logic that results from building a Storage cell;
                 * increase the maximum number of materials that the player can
                 * have
                 */
                gameInfo.setMaterialMax(gameInfo.getMaterialMax() + RESOURCE_MAX_INCREASE);
            }
            return true;
        } else if ((option.equals("Steel Mill") && gameInfo.getMaterials() >= sMillCost
                && gameInfo.getCores() >= CONVERSION_RATE / 2
                && gameInfo.getSoldiers() >= CONVERSION_RATE / 2) || tutorialMode) {
            // set the activeCell's builtOn field
            activeCell.setBuiltOn(true);
            // set the cell type to Steel Mill
            activeCell.setCellType("Steel Mill");
            if (!tutorialMode) {
                // subtract costs
                gameInfo.setMaterials(gameInfo.getMaterials() - sMillCost);
                /*
                 * perform all logic that results from building a Steel Mill
                 * cell; convert soldiers and cores into materials subtract half
                 * of the conversion amount from both soldiers and cores
                 */
                gameInfo.setSoldiers(gameInfo.getSoldiers() - (CONVERSION_RATE / 2));
                gameInfo.setCores(gameInfo.getCores() - (CONVERSION_RATE / 2));
                // add the conversion amount to materials
                gameInfo.setMaterials(gameInfo.getMaterials() + CONVERSION_RATE);
            }
            return true;
        } else if ((option.equals("Energy Grid") && gameInfo.getMaterials() >= eGridCost)
                || tutorialMode) {
            // set the activeCell's builtOn field
            activeCell.setBuiltOn(true);
            // set the cell type to Energy Grid
            activeCell.setCellType("Energy Grid");
            if (!tutorialMode) {
                // subtract costs
                gameInfo.setMaterials(gameInfo.getMaterials() - eGridCost);
                /*
                 * perform all logic that results from building a Energy Grid
                 * cell; increase the maximum number of cores that the player
                 * can have
                 */
                gameInfo.setCoreMax(gameInfo.getCoreMax() + RESOURCE_MAX_INCREASE);
            }
            return true;
        } else if ((option.equals("Solar Farm")
                && gameInfo.getMaterials() >= sFarmCost + CONVERSION_RATE / 2
                && gameInfo.getSoldiers() >= CONVERSION_RATE / 2) || tutorialMode) {
            // set the activeCell's builtOn field
            activeCell.setBuiltOn(true);
            // set the cell type to Solar Farm
            activeCell.setCellType("Solar Farm");
            if (!tutorialMode) {
                // subtract costs
                gameInfo.setMaterials(gameInfo.getMaterials() - sFarmCost);
                /*
                 * perform all logic that results from building a Solar Farm
                 * cell convert materials and soldiers into cores subtract half
                 * of the conversion amount from both materials and soldiers
                 */
                gameInfo.setMaterials(gameInfo.getMaterials() - (CONVERSION_RATE / 2));
                gameInfo.setSoldiers(gameInfo.getSoldiers() - (CONVERSION_RATE / 2));
                // add the conversion amount to cores
                gameInfo.setCores(gameInfo.getCores() + CONVERSION_RATE);
            }
            return true;
        } else {
            return false;
        }
    }
    
    /**
     * Performs all game logic related to capturing a cell;
     * 
     * @param tutorialMode whether or not the tutorial is taking place (wherever
     *            tutorialMode is seen it means that it is needed to ensure that
     *            the tutorial does not change the player's resources, captured
     *            cells, or other stats before the game even starts)
     * 
     * @return whether or not the cell was able to be captured based on the
     *         player's resources
     */
    public boolean captureCell(boolean tutorialMode) {
        /*
         * a soldier can defeat 2 enemies so the capture cost for a cell is the
         * number of enemies in the cell divided by 2 plus the default number of
         * soldiers to station in a cell after capturing
         */
        int capCost = activeCell.getEnemies() / 2 + DEF_STATIONED_SOLDIERS;
        
        if (gameInfo.getSoldiers() >= capCost || tutorialMode) {
            if (!tutorialMode) {
                // UPDATE PLAYER RESOURCE TOTALS
                gameInfo.setSoldiers(gameInfo.getSoldiers() - capCost);
            }
            // UPDATE CELL VALUES
            // the active cell is now captured
            activeCell.setCaptured(true);
            // is no longer hostile after being captured
            activeCell.setHostile(false);
            // is no longer neutral after being captured
            activeCell.setNeutral(false);
            
            if (!tutorialMode) {
                setPotentialStatesOf(activeCell);
                setPotentialStatesOfCellsAround(activeCell);
                // extract the resources from the cell
                // update the player's resource totals
                setPlayerSoldiers(getPlayerSoldiers() + activeCell.getSoldiers());
                setPlayerMaterials(getPlayerMaterials() + activeCell.getMaterials());
                setPlayerCores(getPlayerCores() + activeCell.getCores());
            }
            // set the cell's resources to 0 effectively "extracting" them
            activeCell.setSoldiers(0);
            activeCell.setMaterials(0);
            activeCell.setCores(0);
            
            activeCell.setStationedSoldiers(DEF_STATIONED_SOLDIERS);
            activeCell.setEnemies(0);
            
            if ((activeCell == obj1 && obj2.isCaptured() && obj3.isCaptured())
                    || (activeCell == obj2 && obj1.isCaptured() && obj3.isCaptured())
                    || (activeCell == obj3 && obj1.isCaptured() && obj2.isCaptured())) {
                endGame('W');
            }
            
            return true;
        }
        
        return false;
        
    }
    
    /**
     * Sets the number of stationed soldiers in the active cell to the passed in
     * value;
     * 
     * @param value the desired number of stationed soldiers
     * @param tutorialMode whether or not the tutorial is taking place (wherever
     *            tutorialMode is seen it means that it is needed to ensure that
     *            the tutorial does not change the player's resources, captured
     *            cells, or other stats before the game even starts)
     * 
     * @return whether or not the the desired number of soldiers could be
     *         stationed
     */
    public boolean changeStationedSoldiers(int value, boolean tutorialMode) {
        int difference = activeCell.getStationedSoldiers() - value;
        if (tutorialMode || gameInfo.setSoldiers(gameInfo.getSoldiers() + difference)) {
            activeCell.setStationedSoldiers(value);
            return true;
        }
        return false;
    }
    
    /**
     * Ends the game logically and tells the game UI to bring up the end game
     * pane;
     * 
     * @param option the type of end game that occurred: 'W' means the player
     *            won by capturing all 3 objectives; 'L' means the player lost
     *            because the enemies captured the Space Port; 'F' means the
     *            player forfeited by bombarding their own Space Port
     */
    public void endGame(char option) {
        if (option != 'L' && option != 'W' && option != 'F') {
            throw new IllegalArgumentException("option must be 'L', 'W', or 'F' ");
        }
        
        stopBgThread();
        
        gameInfo.setEndGame(option);
        gameUI.showEndGamePane(option, gameInfo.getPlayerName(), gameInfo.getDifficulty(),
                gameInfo.getTimeElapsedString(), gameInfo.getScore(), gameInfo.getSoldiers(),
                gameInfo.getMaterials(), gameInfo.getCores(), gameInfo.getNumCellsCapped(),
                gameInfo.getNumObjsCapped(),
                MAIN.prefsHighScores.addHighScore(gameInfo.getPlayerName(), gameInfo.getScore()));
    }
    
    /**
     * Returns the GameCell currently being clicked on;
     * 
     * @return the GameCell currently being clicked on
     */
    public GameCell getActiveCell() {
        return activeCell;
    }
    
    /**
     * Returns the current game map as a 2D array of GameCells;
     * 
     * @return the current game map as a 2D array of GameCells
     */
    public GameCell[][] getGameMap() {
        return gameMap;
    }
    
    /**
     * Returns the number of energy cores that the player has;
     * 
     * @return the number of energy cores that the player has
     */
    public int getPlayerCores() {
        return gameInfo.getCores();
    }
    
    /**
     * Returns the number of building materials that the player has;
     * 
     * @return the number of building materials that the player has
     */
    public int getPlayerMaterials() {
        return gameInfo.getMaterials();
    }
    
    /**
     * Returns the number of soldiers that the player has;
     * 
     * @return the number of soldiers that the player has
     */
    public int getPlayerSoldiers() {
        return gameInfo.getSoldiers();
    }
    
    /**
     * Returns the Space Port in the current game map;
     * 
     * @return the Space Port in the current game map
     */
    public GameCell getSpacePort() {
        return spacePort;
    }
    
    /**
     * Regenerates the enemies in each of the objectives as long as the
     * objective is still hostile;
     */
    public void regenObjectiveEnemies() {
        for (int i = 0; i < gameMap.length; i++) {
            for (int j = 0; j < gameMap[i].length; j++) {
                Random rand = new Random();
                if (!obj1.isCaptured() && obj1.getEnemies() < GameCell.MAX_ENEMIES) {
                    int regeneratedEnemies = rand.nextInt(MAX_REGEN_ENEMIES - MIN_REGEN_ENEMIES)
                            + MIN_REGEN_ENEMIES;
                    obj1.setEnemies(obj1.getEnemies() + regeneratedEnemies);
                }
                if (!obj2.isCaptured() && obj2.getEnemies() < GameCell.MAX_ENEMIES) {
                    int regeneratedEnemies = rand.nextInt(MAX_REGEN_ENEMIES - MIN_REGEN_ENEMIES)
                            + MIN_REGEN_ENEMIES;
                    obj2.setEnemies(obj2.getEnemies() + regeneratedEnemies);
                }
                if (!obj3.isCaptured() && obj3.getEnemies() < GameCell.MAX_ENEMIES) {
                    int regeneratedEnemies = rand.nextInt(MAX_REGEN_ENEMIES - MIN_REGEN_ENEMIES)
                            + MIN_REGEN_ENEMIES;
                    obj3.setEnemies(obj3.getEnemies() + regeneratedEnemies);
                }
            }
        }
    }
    
    
    /**
     * Saves the current GameInfo object into its respective save file;
     * 
     * @return whether or not the game was saved successfully
     */
    public boolean saveGame() {
        try {
            FileOutputStream fileOut = new FileOutputStream(
                    "serialized-objects/save" + gameInfo.getSaveSlot() + ".ser");
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(gameInfo);
            out.close();
            fileOut.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Performs all game logic related to scouting a cell;
     * 
     * @param tutorialMode whether or not the tutorial is taking place (wherever
     *            tutorialMode is seen it means that it is needed to ensure that
     *            the tutorial does not change the player's resources, captured
     *            cells, or other stats before the game even starts)
     * @return whether or not the cell was successfully scouted
     */
    public boolean scoutCell(boolean tutorialMode) {
        int scoutCost = 1;
        
        if (gameInfo.getSoldiers() >= scoutCost || tutorialMode) {
            if (!tutorialMode) {
                // UPDATE PLAYER RESOURCE TOTALS
                gameInfo.setSoldiers(gameInfo.getSoldiers() - scoutCost);
                
                // UPDATE CELL VALUES
                // the active cell is now scouted
                activeCell.setScouted(true);
                setPotentialStatesOf(activeCell);
            } else {
                activeCell.setScouted(true);
                activeCell.setCapturable(true);
            }
            return true;
        }
        return false;
    }
    
    /**
     * Sets the GameCell that is currently being clicked on;
     * 
     * @param activeCell the GameCell that is currently being clicked on
     */
    public void setActiveCell(GameCell activeCell) {
        this.activeCell = activeCell;
    }
    
    /**
     * Sets the game map with a 2D array of GameCells;
     * 
     * @param gameMap a 2D array of GameCells representing a game map
     */
    public void setGameMap(GameCell[][] gameMap) {
        this.gameMap = gameMap;
    }
    
    /**
     * Sets the number of energy cores that the player has;
     * 
     * @param value the number of energy cores that the player has
     */
    public void setPlayerCores(int value) {
        this.gameInfo.setCores(value);
    }
    
    /**
     * Sets the number of building materials that the player has
     * 
     * @param value the number of building materials that the player has
     */
    public void setPlayerMaterials(int value) {
        this.gameInfo.setMaterials(value);
    }
    
    /**
     * Sets the number of soldiers that the player has
     * 
     * @param value the number of soldiers that the player has
     */
    public void setPlayerSoldiers(int value) {
        this.gameInfo.setSoldiers(value);
    }
    
    
    /**
     * Sets the potential states of the passed in cell by looking at surrounding
     * cells;
     * 
     * @param gc the cell whose potential states are to be set
     */
    public void setPotentialStatesOf(GameCell gc) {
        /*
         * These booleans keep track of if the potential states have already
         * been confirmed by a previous cell in the for-loop iteration. If these
         * booleans weren't used, then all of the potential states would be
         * based off of the last cell in the for-loop iteration. So if the last
         * cell wasn't captured, but a previous cell was, then the potential
         * states would be wrong without these booleans.
         */
        boolean capturabilityConfirmed = false;
        boolean scoutabilityConfirmed = false;
        boolean destroyabilityConfirmed = false;
        /*
         * set the potential states of the passed in cell based on the
         * surrounding cells' states
         */
        for (int i = gc.getRow() - 2; i <= gc.getRow() + 2; i++) {
            for (int j = gc.getCol() - 2; j <= gc.getCol() + 2; j++) {
                try {
                    boolean isCapturable = gameMap[i][j].isCaptured()
                            && withinOneStep(gc, gameMap[i][j]) && !gc.isCaptured()
                            && gc.isScouted();
                    boolean isScoutable = gameMap[i][j].isCaptured()
                            && withinTwoSteps(gc, gameMap[i][j]) && !gc.isScouted();
                    boolean isDestroyable = gc.isScouted();
                    
                    if (!capturabilityConfirmed) {
                        // check capturability
                        if (isCapturable) {
                            gc.setCapturable(true);
                            capturabilityConfirmed = true;
                        } else {
                            gc.setCapturable(false);
                        }
                    }
                    
                    if (!scoutabilityConfirmed) {
                        // check scoutability
                        if (isScoutable) {
                            gc.setScoutable(true);
                            scoutabilityConfirmed = true;
                        } else {
                            gc.setScoutable(false);
                        }
                    }
                    
                    if (!destroyabilityConfirmed) {
                        // check destroyability
                        if (isDestroyable) {
                            gc.setDestroyable(true);
                            destroyabilityConfirmed = true;
                        } else {
                            gc.setDestroyable(false);
                        }
                    }
                    
                } catch (IndexOutOfBoundsException ex) {
                    // don't do anything
                }
            }
        }
    }
    
    /**
     * Sets the Space Port in the current game map;
     * 
     * @param spacePort the Space Port in the current game map
     */
    public void setSpacePort(GameCell spacePort) {
        this.spacePort = spacePort;
    }
    
    /**
     * Spreads any enemies in hostile cells to the cells around them;
     * 
     * @return whether or not the enemies captured the Space Port during this
     *         spread;
     */
    public boolean spreadEnemies() {
        Random rand = new Random();
        for (int i = 0; i < gameMap.length; i++) {
            for (int j = 0; j < gameMap[i].length; j++) {
                /*
                 * if a cell has 8 or more enemies then spread enemies to an
                 * adjacent cell
                 */
                if (gameMap[i][j].isHostile()
                        && (gameMap[i][j].getEnemies() >= MIN_ENEMIES_TO_SPREAD)) {
                    // the cell that is spreading the enemies
                    GameCell spreader = gameMap[i][j];
                    // the cell that is receiving enemies
                    GameCell other = null;
                    
                    /*
                     * generate a random direction to spread enemies 0 is left,
                     * 1 is up, 2 is right, 3 is down
                     */
                    int randDir = rand.nextInt(4);
                    // generate a random number of enemies to spread
                    int randEn = rand.nextInt(spreader.getEnemies() / 2) + 1;
                    
                    try {
                        if (randDir == 0) {
                            // other is cell to the left of spreader
                            other = gameMap[i][j - 1];
                        } else if (randDir == 1) {
                            // other is cell above spreader
                            other = gameMap[i - 1][j];
                        } else if (randDir == 2) {
                            // other is cell to the right of spreader
                            other = gameMap[i][j + 1];
                        } else {
                            // other is cell below spreader
                            other = gameMap[i + 1][j];
                        }
                        // if other is not destroyed, then spread enemies to it
                        if (!other.isDestroyed()) {
                            // subtract enemies from spreader
                            spreader.setEnemies(spreader.getEnemies() - randEn);
                            
                            if (other.isCaptured()) {
                                /*
                                 * set the number of soldiers after battle to be
                                 * the cell's soldiers times 2 minus the enemies
                                 * attacking because a soldier can defeat 2
                                 * enemies
                                 */
                                int soldiersAfterBattle = other.getStationedSoldiers() * 2 - randEn;
                                if (soldiersAfterBattle < 0) { /*
                                                                * the captured
                                                                * cell was
                                                                * defeated by
                                                                * enemies
                                                                */
                                    /*
                                     * remove stationed soldiers (they were
                                     * defeated)
                                     */
                                    other.setStationedSoldiers(0);
                                    
                                    /*
                                     * add enemies to other (soldiersAfterBattle
                                     * will be negative so the enemies left to
                                     * occupy the cell will be the absolute
                                     * value of soldiersAfterBattle)
                                     */
                                    other.setEnemies(Math.abs(soldiersAfterBattle));
                                    other.setHostile(true);
                                    other.setNeutral(false);
                                    other.setCaptured(false);
                                    setPotentialStatesOf(other);
                                    setPotentialStatesOfCellsAround(other);
                                    
                                    // play the animation for enemies spreading
                                    Animations.getAnimEnemySpread(
                                            (GameCellContainer) other.getParent()).play();
                                    
                                    gameUI.chatBox.appendText("\n" + gameInfo.getTimeElapsedString()
                                            + "--One of your cells has been captured!");
                                    
                                    /*
                                     * end the game if the space port has just
                                     * been captured
                                     */
                                    if (other == spacePort) {
                                        return true;
                                    }
                                    
                                } else { /*
                                          * the captured cell defeated the
                                          * enemies
                                          */
                                    other.setStationedSoldiers(soldiersAfterBattle);
                                    
                                    // play the animation for enemies spreading
                                    Animations.getAnimEnemySpread(
                                            (GameCellContainer) other.getParent()).play();
                                }
                                
                            } else if (other.isNeutral()) {
                                // add enemies to other
                                other.setEnemies(randEn);
                                other.setHostile(true);
                                other.setNeutral(false);
                                
                                // play the animation for enemies spreading
                                Animations.getAnimEnemySpread((GameCellContainer) other.getParent())
                                        .play();
                                
                            } else { // cell is already hostile
                                other.setEnemies(other.getEnemies() + randEn);
                            }
                            
                        }
                    } catch (IndexOutOfBoundsException e) {
                        /*
                         * don't do anything cuz the cell just didn't happen to
                         * spread this time
                         */
                    } // end catch
                    
                } // end if
                
            } // end j for-loop
            
        } // end i for-loop
        return false;
    } // end method
    
    /**
     * Starts the timer and enemy spreading;
     */
    public void startBgThread() {
        // create the thread
        bgThread = new Service<Boolean>() {
            @Override
            protected Task<Boolean> createTask() {
                return new Task<Boolean>() {
                    @Override
                    protected Boolean call() throws Exception {
                        /*
                         *  initialize value to false meaning that the game has not ended in loss
                         */
                        updateValue(false);
                        
                        Random rand = new Random();
                        // establishes a start time from which to measure the
                        // timer
                        long start = System.nanoTime() - gameInfo.getTimeElapsed();
                        
                        // the time until the next enemy spread takes place
                        long timeUntilNextSpread = gameInfo.getSpreadRate();
                        while (!isCancelled()) {
                            /* BEGIN enemy spread code */
                            long timeSinceLastSpread = System.nanoTime()
                                    - gameInfo.getTimeOfLastSpread();
                            
                            if (timeSinceLastSpread >= timeUntilNextSpread) {
                                System.out.println("Enemies spreading...");
                                if (spreadEnemies()) {
                                    bgThread.cancel();
                                    updateValue(true);
                                }
                                regenObjectiveEnemies();
                                gameInfo.setTimeOfLastSpread(
                                        gameInfo.getTimeOfLastSpread() + timeSinceLastSpread);
                                /* 
                                 * generate a time between the spread rate - 1sec and spread rate + 1sec, 
                                 * then convert to nanosecond
                                 */
                                long diff = (rand.nextInt(3) - 1) * 1000000000L;
                                timeUntilNextSpread = gameInfo.getSpreadRate() + diff;
                            }
                            /* END enemy spread code */
                            
                            /* BEGIN timer code */
                            // updates gameInfo's timeElapsed
                            gameInfo.setTimeElapsed(System.nanoTime() - start);
                            // update the message property with the time's
                            // String form
                            updateMessage(gameInfo.getTimeElapsedString());
                            // update the title property with the current score
                            updateTitle("" + gameInfo.getScore());
                            /* END timer code */
                            
                            
                            // sleep for half a second
                            Thread.sleep(500);
                        }
                        return null;
                    }
                }; // END task creation
            }
        }; // END timerThread creation
           // only set onCancelled code because the thread should never end
           // unless cancelled
        bgThread.setOnCancelled(e -> {
            gameUI.timer.textProperty().unbind();
            gameUI.scorer.textProperty().unbind();
            if (bgThread.getValue().booleanValue()) {
                endGame('L');
            }
        });
        
        // sets the timer's text to the thread's message
        gameUI.timer.textProperty().bind(bgThread.messageProperty());
        gameUI.scorer.textProperty().bind(bgThread.titleProperty());
        
        // begins the thread
        bgThread.restart();
    }
    
    
    /**
     * Stops the timer if it is running;
     */
    public void stopBgThread() {
        if (bgThread != null && bgThread.isRunning()) {
            bgThread.cancel();
        }
    }
    
    /**
     * Initializes the gameMap with cell behaviors and determines space port and
     * objectives;
     */
    private void initializeGameMap() {
        // iterate through rows
        for (int i = 0; i < gameMap.length; i++) {
            // iterate through columns
            for (int j = 0; j < gameMap[i].length; j++) {
                // establish easy reference to Space Port cell
                if (gameMap[i][j].getCellType().equals("Space Port")) {
                    setSpacePort(gameMap[i][j]);
                }
                // establish easy reference to objectives
                if (gameMap[i][j].getCellType().equals("Objective")) {
                    if (obj1 == null) {
                        obj1 = gameMap[i][j];
                    } else if (obj2 == null) {
                        obj2 = gameMap[i][j];
                    } else {
                        obj3 = gameMap[i][j];
                    }
                }
                
                // sets the action of each GameCell
                gameMap[i][j].setOnMouseClicked(e -> gameUI.handleGameCellClicks(e, false));
                gameMap[i][j].setOnMouseEntered(e -> gameUI.addHighlight(e));
                gameMap[i][j].setOnMouseExited(e -> gameUI.removeHighlight(e));
                
                // wraps the GameCell in a container that is more practical for
                // animations
                GameCellContainer container = new GameCellContainer(i, j, gameMap[i][j]);
                // add GameCell to GridPane
                gameUI.getGrid().add(container, j, i);
                
                // set GameCells to correct size
                gameMap[i][j].setHeight(GRID_HEIGHT / GRID_ROWS);
                gameMap[i][j].setWidth(GRID_WIDTH / GRID_COLS);
                
            } // end of inner for loop
        } // end of outer for loop
        
        // sync this GameLogic's values with any needed by the GameUIController
        syncGameUI();
        
        // bring view over Space Port
        gameUI.jumpToSpacePort(null);
    }
    
    
    /**
     * Sets the potential states of the cells around the passed in cell by
     * calling the setPotentialStatesOf() on each of the cells;
     * 
     * @param gc the cell around which other cells' potential states will be set
     */
    private void setPotentialStatesOfCellsAround(GameCell gc) {
        // progressively set the surrounding cells' potential states
        for (int i = gc.getRow() - 2; i <= gc.getRow() + 2; i++) {
            for (int j = gc.getCol() - 2; j <= gc.getCol() + 2; j++) {
                try {
                    setPotentialStatesOf(gameMap[i][j]);
                } catch (IndexOutOfBoundsException e) {
                    // don't do anything; it's okay!
                }
            }
        }
    }
    
    /**
     * Sets certain values in the GameUIController with the GameLogic values;
     */
    private void syncGameUI() {
        gameUI.setGameMap(gameMap);
        gameUI.setSpacePort(spacePort);
        
        /* 
         * sets the resource labels in the game UI to GameInfo's corresponding properties
         */
        gameUI.soldiersLbl.textProperty().bind(gameInfo.soldiersStringProperty());
        gameUI.materialsLbl.textProperty().bind(gameInfo.materialsStringProperty());
        gameUI.coresLbl.textProperty().bind(gameInfo.coresStringProperty());
    }
    
}
