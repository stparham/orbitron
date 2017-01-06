package main;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Accordion;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextArea;
import javafx.scene.control.TitledPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Popup;
import staticStorage.Animations;
import staticStorage.CellGraphics;
import supportingClasses.GameCell;
import supportingClasses.GameCellContainer;

/**
 * This class controls all of the UI elements displayed while in the game scene.
 * I apologize for the sheer size of this class because I know that a lot of the
 * methods could be cut out and maybe placed somewhere else.
 * 
 * @author Stanton Parham (stparham@ncsu.edu)
 */
public class GameUIController {
    
    /** The width of the GridPane that displays the cells; */
    private static final double GRID_WIDTH = 2250.0;
    /** The height of the GridPane that displays the cells; */
    private static final double GRID_HEIGHT = 1500.0;
    /** The number of rows in the GridPane; */
    private static final int GRID_ROWS = 10;
    /** The number of columns in the GridPane; */
    private static final int GRID_COLS = 15;
    
    /** The root node of the game UI (from FXML); */
    @FXML // root node
    public AnchorPane gameUIRoot;
    @FXML // holds all of the "permanent" nodes of the UI
    private BorderPane bdrPane;
    
    @FXML // holds grid
    private ScrollPane scrollPane;
    
    @FXML // holds the game map
    private GridPane grid;
    
    /** The chat box in the game UI (from FXML); */
    @FXML // chat
    public TextArea chatBox;
    
    /**
     * The label displaying the amount of time that has elapsed since the game
     * began (from FXML);
     */
    @FXML // timer
    public Label timer;
    
    /** The label displaying the player's current score (from FXML); */
    @FXML // score
    public Label scorer;
    
    // resource labels
    /**
     * The label displaying the number of soldiers the player has (from FXML);
     */
    @FXML
    public Label soldiersLbl;
    /**
     * The label displaying the number of building materials the player has
     * (from FXML);
     */
    @FXML
    public Label materialsLbl;
    /**
     * The label displaying the number of energy cores the player has (from
     * FXML);
     */
    @FXML
    public Label coresLbl;
    
    // buttons directly above the map
    @FXML
    private Button jumpToSpacePortBtn;
    @FXML
    private Button jumpToHotCellBtn;
    
    /** Whether or not the view is zoomed out; */
    private boolean zoomedOut = false;
    
    /** The logic for the current game; */
    private GameLogic logic;
    
    /** The Space Port for the current game; */
    private GameCell spacePort;
    
    /** The game map for the current game; */
    private GameCell[][] gameMap;
    
    /** The cell that is currently under inspection by the player; */
    private GameCell activeCell;
    
    /** The Pane that corresponds to the activeCell; */
    private GameCellContainer activeContainer;
    
    
    /** The cell currently hovered over; */
    private GameCell hoveredCell;
    /** The cell that was previously hovered over; */
    private GameCell previousHoveredCell;
    
    /** The cell that is currently set to be "hot"; */
    private GameCell hotCell;
    
    
    /** The custom context menu; */
    private Popup customContextMenu = new Popup();
    /** The details "page"; */
    private AnchorPane detailsPane = new AnchorPane();
    /** The pause screen; */
    private AnchorPane pausePane = new AnchorPane();
    /** The tutorial screen; */
    private AnchorPane tutorialPane = new AnchorPane();
    /** The page that the tutorialPane is currently showing; */
    private int currentTutorialPage;
    
    
    /**
     * Stores the instance of the class so that other classes can reference it;
     */
    private static GameUIController instance;
    
    
    /**
     * The constructor that sets the instance field to reference this;
     */
    public GameUIController() {
        instance = this;
    }
    
    /**
     * Returns the GameUIController;
     * 
     * @return the GameUIController
     */
    public static GameUIController getInstance() {
        return instance;
    }
    
    /**
     * Returns the GridPane where the GameCells are drawn;
     * 
     * @return the GridPane where the GameCells are drawn
     */
    public GridPane getGrid() {
        return this.grid;
    }
    
    /**
     * Returns the ScrollPane which holds the GridPane;
     * 
     * @return the ScrollPane which holds the GridPane
     */
    public ScrollPane getScrollPane() {
        return this.scrollPane;
    }
    
    /**
     * Sets the Space Port of the current game;
     * 
     * @param sp the Space Port of the current game
     */
    public void setSpacePort(GameCell sp) {
        this.spacePort = sp;
    }
    
    /**
     * Sets the game map for the current game;
     * 
     * @param gm the game map for the current game
     */
    public void setGameMap(GameCell[][] gm) {
        this.gameMap = gm;
    }
    
    /**
     * Sets the logic object for the current game;
     * 
     * @param g the logic object for the current game
     */
    public void setLogic(GameLogic g) {
        this.logic = g;
    }
    
    
    /**
     * Pauses the game by stopping the logic's bgThread and bringing up a pause
     * menu;
     */
    public void pauseGame() {
        if (!gameUIRoot.getChildren().contains(pausePane)) {
            logic.stopBgThread();
            // TODO add options and the ability to save and return to main menu in the pause menu
            // set up the pause pane
            pausePane.getChildren().clear();
            pausePane.setId("pausePane");
            pausePane.setPrefSize(MAIN.STAGE_WIDTH, MAIN.STAGE_HEIGHT);
            
            // the button to resume the game
            Button resumeBtn = new Button();
            resumeBtn.setText("RESUME");
            resumeBtn.setOnAction(e -> {
                logic.startBgThread();
                gameUIRoot.getChildren().remove(pausePane);
            });
            AnchorPane.setTopAnchor(resumeBtn, 25.0);
            AnchorPane.setLeftAnchor(resumeBtn, 25.0);
            
            // the title of the end game pane
            Label title = new Label();
            title.setText("Session Paused");
            title.setId("pausePaneTitle");
            title.setPrefSize(775.0, 100.0);
            AnchorPane.setTopAnchor(title, 25.0);
            AnchorPane.setLeftAnchor(title, 200.0);
            title.setAlignment(Pos.CENTER_RIGHT);
            
            pausePane.getChildren().addAll(title, resumeBtn);
            gameUIRoot.getChildren().add(pausePane);
        }
    }
    
    /**
     * Tells the game logic to save the game and depending on if the save is
     * successful or not, this method will append an appropriate message to
     * KAL's chat;
     */
    public void saveGame() {
        if (logic.saveGame()) {
            chatBox.appendText("\n--Session saved successfully!!");
            
        } else {
            chatBox.appendText("\n--Session failed to save :(");
        }
    }
    
    
    /**
     * Changes the view either from "zoomed-in" to "overview" or vice-versa;
     */
    public void changeView() {
        
        if (!zoomedOut) { // if map is full size
            grid.setMaxHeight(scrollPane.getHeight() - 18);
            grid.setMaxWidth(scrollPane.getWidth() - 18);
            grid.setMaxHeight(scrollPane.getHeight() - 18);
            grid.setMaxWidth(scrollPane.getWidth() - 18);
            zoomedOut = true;
            
            // iterate through row
            for (int i = 0; i < gameMap.length; i++) {
                // iterate through column
                for (int j = 0; j < gameMap[i].length; j++) {
                    // set size of each GameCell
                    gameMap[i][j].setHeight((scrollPane.getHeight() - 18) / GRID_ROWS);
                    gameMap[i][j].setWidth((scrollPane.getWidth() - 18) / GRID_COLS);
                    
                    /* REDRAW GRAPHICS FOR EACH GAME CELL */
                    /*
                     * CellDrawer drawer = new CellDrawer(gameMap[i][j], false);
                     * drawer.drawGraphics();
                     */
                    CellGraphics.drawGraphics(gameMap[i][j], false);
                }
            }
        } else if (zoomedOut) { // if map is compressed
            // resize GridPane
            grid.setMaxHeight(grid.getPrefHeight());
            grid.setMaxWidth(grid.getPrefWidth());
            grid.setMaxHeight(grid.getPrefHeight());
            grid.setMaxWidth(grid.getPrefWidth());
            zoomedOut = false;
            
            // resize GameCells
            // iterate through row
            for (int i = 0; i < gameMap.length; i++) {
                // iterate through column
                for (int j = 0; j < gameMap[i].length; j++) {
                    // set size of each GameCell
                    gameMap[i][j].setHeight(GRID_HEIGHT / GRID_ROWS);
                    gameMap[i][j].setWidth(GRID_WIDTH / GRID_COLS);
                    
                    /* REDRAW GRAPHICS FOR EACH GAME CELL */
                    /*
                     * CellDrawer drawer = new CellDrawer(gameMap[i][j], false);
                     * drawer.drawGraphics();
                     */
                    CellGraphics.drawGraphics(gameMap[i][j], false);
                    
                } // end of inner for loop
            } // end of outer for loop
        } // end of else if
    }
    
    /**
     * Returns to the main menu by resetting the scene;
     * 
     * @param e the ActionEvent produced by a button that called this method
     */
    public void returnToMainMenu(ActionEvent e) {
        logic.stopBgThread();
        saveGame();
        chatBox.clear();
        chatBox.appendText(
                "--Hi! I'm KAL, your personal A.I.!! I'll help you throughout your screening session.");
        gameUIRoot.getChildren().remove(pausePane);
        MAIN.mainStage.setScene(MAIN.mainMenuScene);
    }
    
    
    /**
     * Immediately centers view on the Space Port;
     * 
     * @param e an ActionEvent from the button above the map that has the game
     *            logo
     */
    public void jumpToSpacePort(ActionEvent e) {
        if (zoomedOut) {
            changeView();
        }
        Animations.getAnimJumpToCell(spacePort, scrollPane).play();
    }
    
    
    /**
     * Immediately centers the view on the hot cell corresponding to which
     * "Hot Cell" button was pressed;
     * 
     * @param e an ActionEvent from one of the three "Hot Cell" buttons above
     *            the map;
     */
    public void jumpToHotCell(ActionEvent e) {
        if (hotCell != null) {
            if (zoomedOut) {
                changeView();
            }
            Animations.getAnimJumpToCell(hotCell, scrollPane).play();
        }
    }
    
    
    /**
     * Shows the "game over" screen to the player;
     * 
     * @param option the type of end game that occurred
     * @param playerName the player's name
     * @param difficulty the difficulty of the game
     * @param timeElapsed the time elapsed while playing the game
     * @param score the final score for the game
     * @param numSoldiers the final number of soldiers that the player had
     * @param numMaterials the final number of materials that the player had
     * @param numCores the final number of cores that the player had
     * @param numCellsCapped the overall number of cells that are capped when
     *            the game ends
     * @param numObjsCapped the number of Objectives that are capped when the
     *            game ends
     * @param newHighScore whether or not the player achieved a new high score
     */
    public void showEndGamePane(char option, String playerName, String difficulty,
            String timeElapsed, int score, int numSoldiers, int numMaterials, int numCores,
            int numCellsCapped, int numObjsCapped, boolean newHighScore) {
        if (option != 'L' && option != 'W' && option != 'F') {
            throw new IllegalArgumentException("option must be 'L', 'W', or 'F' ");
        }
        
        // clear the context menu from the screen
        customContextMenu.getContent().clear();
        customContextMenu.hide();
        
        // initialize the end game pane
        AnchorPane endGamePane = new AnchorPane();
        endGamePane.setPrefSize(MAIN.STAGE_WIDTH, MAIN.STAGE_HEIGHT);
        endGamePane.setId("endGamePane");
        
        // the back button at the top-left of the screen
        Button backBtn = new Button("MAIN MENU");
        AnchorPane.setTopAnchor(backBtn, 25.0);
        AnchorPane.setLeftAnchor(backBtn, 25.0);
        backBtn.setOnAction(e2 -> {
            gameUIRoot.getChildren().remove(endGamePane);
            returnToMainMenu(e2);
        });
        
        
        // the title of the end game pane
        Label title = new Label();
        title.setText("YOU ARE DEFEATED");
        if (option == 'W') {
            title.setText("YOU ARE VICTORIOUS");
        } else if (option == 'F') {
            title.setText("YOU FORFEITED");
        }
        title.setId("endGamePaneTitle");
        title.setPrefSize(775.0, 100.0);
        AnchorPane.setTopAnchor(title, 25.0);
        AnchorPane.setLeftAnchor(title, 200.0);
        title.setAlignment(Pos.CENTER_RIGHT);
        
        
        // creates several labels that display information about the game
        // player name
        Label playerNameLbl = new Label("Player: " + playerName);
        playerNameLbl.setPrefSize(450.0, 50.0);
        playerNameLbl.setId("endGameLabel");
        // difficulty
        Label difficultyLbl = new Label("Difficulty: " + difficulty);
        difficultyLbl.setPrefSize(450.0, 50.0);
        difficultyLbl.setId("endGameLabel");
        // time elapsed
        Label timeElapsedLbl = new Label("Time Elapsed: " + timeElapsed);
        timeElapsedLbl.setPrefSize(450.0, 50.0);
        timeElapsedLbl.setId("endGameLabel");
        // final score
        Label scoreLbl = new Label("Score: " + score);
        scoreLbl.setPrefSize(450.0, 50.0);
        scoreLbl.setId("endGameLabel");
        // displayed if the player achieved a new high score
        Label newHighScoreLbl = new Label("NEW HIGH SCORE");
        newHighScoreLbl.setPrefSize(450.0, 50.0);
        newHighScoreLbl.setId("endGameLabel");
        newHighScoreLbl.setVisible(newHighScore);
        // holds the labels on the left side of the screen
        VBox leftContainer = new VBox();
        leftContainer.setAlignment(Pos.CENTER);
        leftContainer.setSpacing(30);
        AnchorPane.setTopAnchor(leftContainer, 150.0);
        AnchorPane.setLeftAnchor(leftContainer, 25.0);
        leftContainer.getChildren().addAll(playerNameLbl, difficultyLbl, timeElapsedLbl, scoreLbl,
                newHighScoreLbl);
        
        // creates several more labels that display information about the game
        // the final number of soldiers that the player had
        soldiersLbl = new Label("Soldiers: " + numSoldiers);
        soldiersLbl.setPrefSize(450.0, 50.0);
        soldiersLbl.setId("endGameLabel");
        // the final number of materials that the player had
        materialsLbl = new Label("Materials: " + numMaterials);
        materialsLbl.setPrefSize(450.0, 50.0);
        materialsLbl.setId("endGameLabel");
        // the final number of cores that the player had
        coresLbl = new Label("Cores: " + numCores);
        coresLbl.setPrefSize(450.0, 50.0);
        coresLbl.setId("endGameLabel");
        // the overall number of cells capped at the end of the game
        Label cellsCappedLbl = new Label("Cells Captured: " + numCellsCapped);
        cellsCappedLbl.setPrefSize(450.0, 50.0);
        cellsCappedLbl.setId("endGameLabel");
        // the number of Objectives capped at the end of the game
        Label objsCappedLbl = new Label("Objectives Captured: " + numObjsCapped + "/3");
        objsCappedLbl.setPrefSize(450.0, 50.0);
        objsCappedLbl.setId("endGameLabel");
        // holds all of the labels on the right side of the screen
        VBox rightContainer = new VBox();
        rightContainer.setAlignment(Pos.CENTER);
        rightContainer.setSpacing(30);
        AnchorPane.setTopAnchor(rightContainer, 150.0);
        AnchorPane.setLeftAnchor(rightContainer, 525.0);
        rightContainer.getChildren().addAll(soldiersLbl, materialsLbl, coresLbl, cellsCappedLbl,
                objsCappedLbl);
        
        // add everything to the endGamePane
        endGamePane.getChildren().addAll(backBtn, title, leftContainer, rightContainer);
        
        // add the endGamePane to the gameUIRoot
        gameUIRoot.getChildren().add(endGamePane);
        
    }
    
    
    /**
     * Highlights the cell that is currently hovered over with the mouse in
     * orange;
     * 
     * @param e the MouseEvent
     */
    public void addHighlight(MouseEvent e) {
        hoveredCell = (GameCell) e.getSource();
        CellGraphics.drawGraphics(hoveredCell, true);
        previousHoveredCell = hoveredCell;
    }
    
    
    /**
     * Redraws the previously hovered over cell with its correct color;
     * 
     * @param e the MouseEvent
     */
    public void removeHighlight(MouseEvent e) {
        CellGraphics.drawGraphics(previousHoveredCell, false);
    }
    
    
    /**
     * Jumps to a cell if it has been double-clicked on; Shows a customized
     * context menu if a cell is right-clicked on;
     * 
     * @param e the MouseEvent
     * @param tutorialMode whether or not the tutorial is taking place (wherever
     *            tutorialMode is seen it means that it is needed to ensure that
     *            the tutorial does not change the player's resources, captured
     *            cells, or other stats before the game even starts)
     */
    public void handleGameCellClicks(MouseEvent e, boolean tutorialMode) {
        // determine the cell that was clicked on
        determineActiveCell(e);
        
        // jump to the activeCell if double-clicked
        if (e.getButton().equals(MouseButton.PRIMARY)) {
            
            // checks to make sure that the tutorial isn't being played before
            // attempting to
            // traverse to that cell in the scrollPane
            if (e.getClickCount() == 2 && !tutorialMode) {
                if (zoomedOut) {
                    changeView();
                }
                Animations.getAnimJumpToCell(activeCell, scrollPane).play();
            }
        }
        
        // produce the context menu if right-clicked
        if (e.getButton().equals(MouseButton.SECONDARY)) {
            showContextMenu(e, tutorialMode);
            
        }
    }
    
    
    /**
     * Shows the tutorial pane while pausing the game;
     */
    public void showTutorialPane() {
        logic.stopBgThread();
        // remove any extra panes that may be on the screen
        gameUIRoot.getChildren().remove(1, gameUIRoot.getChildren().size());
        // resets the tutorialPane
        tutorialPane.getChildren().clear();
        currentTutorialPage = 0;
        
        // the back button at the top-left of the screen
        Button endBtn = new Button("END TUTORIAL");
        AnchorPane.setTopAnchor(endBtn, 25.0);
        AnchorPane.setLeftAnchor(endBtn, 25.0);
        endBtn.setOnAction(e2 -> {
            logic.startBgThread();
            gameUIRoot.getChildren().remove(tutorialPane);
        });
        
        // the title of the tutorialPane
        Label title = new Label();
        title.setText("Tutorial");
        title.setId("tutorialPaneTitle");
        title.setPrefSize(775.0, 100.0);
        AnchorPane.setTopAnchor(title, 25.0);
        AnchorPane.setLeftAnchor(title, 200.0);
        title.setAlignment(Pos.CENTER_RIGHT);
        
        // create the different "pages" that will explain the game
        // page 0
        AnchorPane page0 = new AnchorPane();
        page0.setPrefSize(900.0, 350.0);
        Label page0Title = new Label("ORBITRON BASICS");
        page0Title.setId("tutorialPageTitle");
        // create text area for description
        TextArea page0Text = new TextArea();
        page0Text.setEditable(false);
        page0Text.setWrapText(true);
        page0Text.setPrefSize(400.0, 350.0);
        AnchorPane.setTopAnchor(page0Text, 50.0);
        page0Text.setText("Welcome to the Orbitron Superior Biological Entity Screening "
                + "Program! As always, the first order of business is getting you up to speed "
                + "on what you'll be doing here. You're in command of a satellite in "
                + "geosynchronous orbit around the Earth! You are tasked with protecting your "
                + "fellow humans down on the ground, and to do so, you need to protect the Space "
                + "Port while attempting to capture all 3 Objectives!");
        // create space port
        GameCell page0SpacePort = new GameCell("Space Port", 0, 0, true, true, false, false, false,
                0, 0, 0, 0, false, false, false);
        page0SpacePort.setHeight(GameLogic.GRID_HEIGHT / GameLogic.GRID_ROWS);
        page0SpacePort.setWidth(GameLogic.GRID_WIDTH / GameLogic.GRID_COLS);
        CellGraphics.drawGraphics(page0SpacePort, false);
        AnchorPane.setTopAnchor(page0SpacePort, 50.0);
        AnchorPane.setRightAnchor(page0SpacePort, 0.0);
        // create space port label
        Label spacePortLabel = new Label("SPACE PORT");
        AnchorPane.setTopAnchor(spacePortLabel, 50.0);
        AnchorPane.setRightAnchor(spacePortLabel, 175.0);
        // create objective
        GameCell page0Objective = new GameCell("Objective", 0, 1, false, true, false, true, false,
                0, 0, 0, 100, false, false, false);
        page0Objective.setHeight(GameLogic.GRID_HEIGHT / GameLogic.GRID_ROWS);
        page0Objective.setWidth(GameLogic.GRID_WIDTH / GameLogic.GRID_COLS);
        CellGraphics.drawGraphics(page0Objective, false);
        AnchorPane.setTopAnchor(page0Objective, 225.0);
        AnchorPane.setRightAnchor(page0Objective, 0.0);
        // create objective label
        Label objectiveLabel = new Label("OBJECTIVE");
        AnchorPane.setTopAnchor(objectiveLabel, 225.0);
        AnchorPane.setRightAnchor(objectiveLabel, 175.0);
        // add everything to page0
        page0.getChildren().addAll(page0Title, page0Text, page0SpacePort, spacePortLabel,
                page0Objective, objectiveLabel);
        
        // page 1
        AnchorPane page1 = new AnchorPane();
        page1.setPrefSize(900.0, 350.0);
        Label page1Title = new Label("SCOUTING/CAPTURING");
        page1Title.setId("tutorialPageTitle");
        // create text area for description
        TextArea page1Text = new TextArea();
        page1Text.setEditable(false);
        page1Text.setWrapText(true);
        page1Text.setPrefSize(400.0, 350.0);
        AnchorPane.setTopAnchor(page1Text, 50.0);
        page1Text.setText("When you first set out from your humble Space Port, you need "
                + "to scout and capture cells to gather resources. Without resources you can't "
                + "do anything! The different resources are SOLDIERS, BUILDING MATERIALS, and "
                + "ENERGY CORES. You need SOLDIERS to scout and capture cells. To scout a cell, "
                + "simply right-click on any cell that is blacked out and click the scout button "
                + "from the Operations section. To capture a cell, simply right-click on any "
                + "cell that has been scouted and click the capture button from the Operations "
                + "section. Try to scout and capture the cell to the right!");
        // create a generic cell
        GameCell page1Cell = new GameCell("Apartments", 0, 0, false, false, true, false, false, 0,
                0, 0, 0, false, true, false);
        page1Cell.setOnMouseClicked(e -> handleGameCellClicks(e, true));
        page1Cell.setHeight(GameLogic.GRID_HEIGHT / GameLogic.GRID_ROWS);
        page1Cell.setWidth(GameLogic.GRID_WIDTH / GameLogic.GRID_COLS);
        CellGraphics.drawGraphics(page1Cell, false);
        // create a GameCellContainer for the cell
        GameCellContainer page1CellContainer = new GameCellContainer(0, 0, page1Cell);
        AnchorPane.setTopAnchor(page1CellContainer, 150.0);
        AnchorPane.setRightAnchor(page1CellContainer, 200.0);
        // add everything to page1
        page1.getChildren().addAll(page1Title, page1Text, page1CellContainer);
        
        // page2
        AnchorPane page2 = new AnchorPane();
        page2.setPrefSize(900.0, 350.0);
        Label page2Title = new Label("BUILDING");
        page2Title.setId("tutorialPageTitle");
        // create text area for description
        TextArea page2Text = new TextArea();
        page2Text.setEditable(false);
        page2Text.setWrapText(true);
        page2Text.setPrefSize(400.0, 350.0);
        AnchorPane.setTopAnchor(page2Text, 50.0);
        page2Text.setText("After capturing a cell, you can build structures on it as long as it "
                + "is not a Space Port or Objective cell. Building structures requires BUILDING "
                + "MATERIALS. Some structures require other resources as well. The structures "
                + "that you can build on a cell are: HOUSING, ROBOTICS FACTORY, STORAGE, STEEL "
                + "MILL, ENERGY GRID, and SOLAR FARM. Try to build on the cell to the right!");
        // create a generic cell
        GameCell page2Cell = new GameCell("Apartments", 0, 0, true, true, false, false, false, 0, 0,
                0, 0, false, false, false);
        page2Cell.setOnMouseClicked(e -> handleGameCellClicks(e, true));
        page2Cell.setHeight(GameLogic.GRID_HEIGHT / GameLogic.GRID_ROWS);
        page2Cell.setWidth(GameLogic.GRID_WIDTH / GameLogic.GRID_COLS);
        CellGraphics.drawGraphics(page2Cell, false);
        // create a GameCellContainer for the cell
        GameCellContainer page2CellContainer = new GameCellContainer(0, 0, page2Cell);
        AnchorPane.setTopAnchor(page2CellContainer, 150.0);
        AnchorPane.setRightAnchor(page2CellContainer, 200.0);
        // add everything to page2
        page2.getChildren().addAll(page2Title, page2Text, page2CellContainer);
        
        // page 3
        AnchorPane page3 = new AnchorPane();
        page3.setPrefSize(900.0, 350.0);
        Label page3Title = new Label("DEFENDING");
        page3Title.setId("tutorialPageTitle");
        // create text area for description
        TextArea page3Text = new TextArea();
        page3Text.setEditable(false);
        page3Text.setWrapText(true);
        page3Text.setPrefSize(400.0, 350.0);
        AnchorPane.setTopAnchor(page3Text, 50.0);
        page3Text.setText("After capturing a cell, you can also station soldiers in the cell! "
                + "These soldiers will defend your cell from enemy attacks. If these soldiers "
                + "are defeated, then your cell will be captured by the enemy. One great tip "
                + "for stationing soldiers is to immediately station a LOT of soldiers in an "
                + "Objective when you capture it! This way the enemies won't recapture it and "
                + "continue to regenerate new enemies. Try to station soldiers in the cell to "
                + "the right!");
        // create a generic cell
        GameCell page3Cell = new GameCell("Apartments", 0, 0, true, true, false, false, false, 0, 0,
                0, 0, false, false, false);
        page3Cell.setOnMouseClicked(e -> handleGameCellClicks(e, true));
        page3Cell.setHeight(GameLogic.GRID_HEIGHT / GameLogic.GRID_ROWS);
        page3Cell.setWidth(GameLogic.GRID_WIDTH / GameLogic.GRID_COLS);
        CellGraphics.drawGraphics(page3Cell, false);
        // create a GameCellContainer for the cell
        GameCellContainer page3CellContainer = new GameCellContainer(0, 0, page3Cell);
        AnchorPane.setTopAnchor(page3CellContainer, 150.0);
        AnchorPane.setRightAnchor(page3CellContainer, 200.0);
        // add everything to page3
        page3.getChildren().addAll(page3Title, page3Text, page3CellContainer);
        
        // page 4
        AnchorPane page4 = new AnchorPane();
        page4.setPrefSize(900.0, 350.0);
        Label page4Title = new Label("ENEMIES");
        page4Title.setId("tutorialPageTitle");
        // create text area for description
        TextArea page4Text = new TextArea();
        page4Text.setEditable(false);
        page4Text.setWrapText(true);
        page4Text.setPrefSize(400.0, 350.0);
        AnchorPane.setTopAnchor(page4Text, 50.0);
        page4Text.setText("In order to perform well in your screening session, you need to "
                + "'Know Thy Enemy'. Enemies inhabit Objectives and will continue to pour "
                + "out of them until you capture them. It's pretty hard to capture an Objective, "
                + "but they are packed with resources! When encoutering enemies in other cells, "
                + "you can bombard them with a kinetic strike from your satellite! Depending on "
                + "how many energy cores you use when bombarding, you will either thin out the "
                + "enemies in that cell or completely destroy the cell! When a cell is destroyed, "
                + "neither you nor the enemy can move into the cell. Try bombarding the cell to "
                + "the right! (If you want to see the different effects of varying the amount of "
                + "energy cores that you use, then reload the tutorial page.)");
        // create a generic cell
        GameCell page4Cell = new GameCell("Apartments", 0, 0, false, true, false, true, false, 0, 0,
                0, 5, false, false, true);
        page4Cell.setOnMouseClicked(e -> handleGameCellClicks(e, true));
        page4Cell.setHeight(GameLogic.GRID_HEIGHT / GameLogic.GRID_ROWS);
        page4Cell.setWidth(GameLogic.GRID_WIDTH / GameLogic.GRID_COLS);
        CellGraphics.drawGraphics(page4Cell, false);
        // create a GameCellContainer for the cell
        GameCellContainer page4CellContainer = new GameCellContainer(0, 0, page4Cell);
        AnchorPane.setTopAnchor(page4CellContainer, 150.0);
        AnchorPane.setRightAnchor(page4CellContainer, 200.0);
        // add everything to page4
        page4.getChildren().addAll(page4Title, page4Text, page4CellContainer);
        
        // page 5
        AnchorPane page5 = new AnchorPane();
        page5.setPrefSize(900.0, 350.0);
        Label page5Title = new Label("THE END GAME");
        page5Title.setId("tutorialPageTitle");
        // create text area for description
        TextArea page5Text = new TextArea();
        page5Text.setEditable(false);
        page5Text.setWrapText(true);
        page5Text.setPrefSize(800.0, 350.0);
        AnchorPane.setTopAnchor(page5Text, 50.0);
        page5Text.setText("Finally! We're at the end of the tutorial! Oh wait, I still need "
                + "to tell you about how you can end the game. There are 3 'end games'. You can "
                + "win by capturing all 3 Objectives, lose by allowing the enemies to capture the "
                + "Space Port, or forfeit by bombarding the Space Port yourself!! I'm sure that "
                + "there are a lot of details that I've left out, but I'm confident that you'll "
                + "be able to figure everything else out on your own! Best of luck in showing "
                + "that you are, in fact, the most Superior Biological Entity!");
        // add everything to page5
        page5.getChildren().addAll(page5Title, page5Text);
        
        // create a container that will hold the current tutorial page
        AnchorPane pageContainer = new AnchorPane();
        AnchorPane.setTopAnchor(pageContainer, 125.0);
        AnchorPane.setLeftAnchor(pageContainer, 25.0);
        pageContainer.getChildren().add(page0);
        // add all of the pages to an array
        AnchorPane[] pages = { page0, page1, page2, page3, page4, page5 };
        
        
        // the navigation buttons at the bottom of the screen
        Button prevBtn = new Button("PREVIOUS");
        Button nextBtn = new Button("NEXT");
        // for prevBtn
        prevBtn.setDisable(true); // initially disabled because already on first
                                  // page
        prevBtn.setPrefWidth(105.0);
        AnchorPane.setTopAnchor(prevBtn, 25.0);
        AnchorPane.setLeftAnchor(prevBtn, 25.0);
        prevBtn.setOnAction(e2 -> {
            pageContainer.getChildren().remove(pages[currentTutorialPage]);
            currentTutorialPage--;
            pageContainer.getChildren().add(pages[currentTutorialPage]);
            nextBtn.setDisable(false);
            if (currentTutorialPage == 0) {
                prevBtn.setDisable(true);
            }
        });
        // for nextBtn
        nextBtn.setPrefWidth(105.0);
        AnchorPane.setTopAnchor(nextBtn, 25.0);
        AnchorPane.setLeftAnchor(nextBtn, 25.0);
        nextBtn.setOnAction(e2 -> {
            pageContainer.getChildren().remove(pages[currentTutorialPage]);
            currentTutorialPage++;
            pageContainer.getChildren().add(pages[currentTutorialPage]);
            prevBtn.setDisable(false);
            if (currentTutorialPage == pages.length - 1) {
                nextBtn.setDisable(true);
            }
        });
        // create HBox for navigation buttons
        HBox navBtns = new HBox(10);
        navBtns.setAlignment(Pos.CENTER);
        AnchorPane.setBottomAnchor(navBtns, 25.0);
        AnchorPane.setLeftAnchor(navBtns, 25.0);
        navBtns.getChildren().addAll(prevBtn, nextBtn);
        
        
        // sets up the tutorialPane
        tutorialPane.setPrefSize(MAIN.STAGE_WIDTH, MAIN.STAGE_HEIGHT);
        tutorialPane.setId("tutorialPane");
        tutorialPane.getChildren().addAll(title, endBtn, pageContainer, navBtns);
        gameUIRoot.getChildren().add(tutorialPane);
    }
    
    
    /**
     * Clears the "fog of war" from the cell and draws the cell's normal
     * graphics;
     * 
     * @param e the ActionEvent
     * @param tutorialMode whether or not the tutorial is taking place (wherever
     *            tutorialMode is seen it means that it is needed to ensure that
     *            the tutorial does not change the player's resources, captured
     *            cells, or other stats before the game even starts)
     */
    private void scoutCell(ActionEvent e, boolean tutorialMode) {
        // close the context menu
        customContextMenu.hide();
        customContextMenu.getContent().clear();
        
        // perform game logic for scouting a cell
        if (logic.scoutCell(tutorialMode)) {
            // play scout animation if logic is successful
            Animations.getAnimScout(activeContainer).play();
        } else {
            chatBox.appendText("\n--You need at least one soldier to scout a cell!");
        }
    }
    
    
    /**
     * Captures the cell and re-draws it;
     * 
     * @param e the ActionEvent
     * @param tutorialMode whether or not the tutorial is taking place (wherever
     *            tutorialMode is seen it means that it is needed to ensure that
     *            the tutorial does not change the player's resources, captured
     *            cells, or other stats before the game even starts)
     */
    private void captureCell(ActionEvent e, boolean tutorialMode) {
        // close and clear the context menu
        customContextMenu.hide();
        customContextMenu.getContent().clear();
        
        // perform game logic for capturing a cell
        if (logic.captureCell(tutorialMode)) {
            // play capture animation if logic is successful
            Animations.getAnimCapture(activeContainer).play();
        } else {
            chatBox.appendText("\n--You need more soldiers to capture that cell!");
        }
    }
    
    
    /**
     * Destroys the cell and re-draws the cell as a cracked cell;
     * 
     * @param e the ActionEvent
     * @param bombardCost the number of cores that the player wants to use to
     *            bombard the cell
     * @param tutorialMode whether or not the tutorial is taking place (wherever
     *            tutorialMode is seen it means that it is needed to ensure that
     *            the tutorial does not change the player's resources, captured
     *            cells, or other stats before the game even starts)
     */
    private void bombardCell(ActionEvent e, int bombardCost, boolean tutorialMode) {
        // close the context menu
        customContextMenu.hide();
        customContextMenu.getContent().clear();
        
        // perform game logic for bombarding a cell
        if (logic.bombardCell(bombardCost, tutorialMode)) {
            // play bombard animation if logic is successful
            Animations.getAnimBombard(activeContainer).play();
        } else {
            chatBox.appendText("\n--You need more cores to bombard that cell!");
        }
    }
    
    /**
     * Builds whichever structure on a cell depending on the button pressed;
     * 
     * @param e the ActionEvent from the button in the context menu
     * @param tutorialMode whether or not the tutorial is taking place (wherever
     *            tutorialMode is seen it means that it is needed to ensure that
     *            the tutorial does not change the player's resources, captured
     *            cells, or other stats before the game even starts)
     */
    private void build(ActionEvent e, boolean tutorialMode) {
        // close the context menu
        customContextMenu.hide();
        customContextMenu.getContent().clear();
        
        
        if (((Button) e.getSource()).getText().equals("Housing")) {
            System.out.println("Housing Built");
            if (logic.build("Housing", tutorialMode)) {
                // play build animation if logic is successful
                Animations.getAnimBuild(activeContainer).play();
            } else {
                chatBox.appendText("\n--You need more materials to build that!");
            }
        } else if (((Button) e.getSource()).getText().equals("Robo Factory")) {
            System.out.println("Robotics Factory Built");
            if (logic.build("Robotics Factory", tutorialMode)) {
                // play build animation if logic is successful
                Animations.getAnimBuild(activeContainer).play();
            } else {
                chatBox.appendText("\n--You either need more materials to build that or "
                        + "you don't have enough cores and materials to convert to soldiers!");
            }
        } else if (((Button) e.getSource()).getText().equals("Storage")) {
            System.out.println("Storage Built");
            if (logic.build("Storage", tutorialMode)) {
                // play build animation if logic is successful
                Animations.getAnimBuild(activeContainer).play();
            } else {
                chatBox.appendText("\n--You need more materials to build that!");
            }
        } else if (((Button) e.getSource()).getText().equals("Steel Mill")) {
            System.out.println("Steel Mill Built");
            if (logic.build("Steel Mill", tutorialMode)) {
                // play build animation if logic is successful
                Animations.getAnimBuild(activeContainer).play();
            } else {
                chatBox.appendText("\n--You either need more materials to build that or "
                        + "you don't have enough cores and soldiers to convert to materials!");
            }
        } else if (((Button) e.getSource()).getText().equals("Energy Grid")) {
            System.out.println("Energy Grid Built");
            if (logic.build("Energy Grid", tutorialMode)) {
                // play build animation if logic is successful
                Animations.getAnimBuild(activeContainer).play();
            } else {
                chatBox.appendText("\n--You need more materials to build that!");
            }
        } else if (((Button) e.getSource()).getText().equals("Solar Farm")) {
            System.out.println("Solar Farm Built");
            if (logic.build("Solar Farm", tutorialMode)) {
                // play build animation if logic is successful
                Animations.getAnimBuild(activeContainer).play();
            } else {
                chatBox.appendText("\n--You either need more materials to build that or "
                        + "you don't have enough soldiers and materials to convert to cores!");
            }
        }
    }
    
    
    /**
     * Shows the custom context menu and all of the options for the active cell;
     * 
     * @param e the OnClick MouseEvent from the active cell
     * @param tutorialMode whether or not the tutorial is taking place (wherever
     *            tutorialMode is seen it means that it is needed to ensure that
     *            the tutorial does not change the player's resources, captured
     *            cells, or other stats before the game even starts)
     */
    private void showContextMenu(MouseEvent e, boolean tutorialMode) {
        // clear context menu before generating again
        customContextMenu.getContent().clear();
        
        // BELOW ARE THE ITEMS TO BE INCLUDED IN VARIOUS SECTIONS
        /* INFO Buttons */
        // create a VBox that contains the info buttons
        VBox info = new VBox();
        info.setAlignment(Pos.CENTER);
        info.setPadding(new Insets(2));
        info.setSpacing(2);
        
        
        if (!tutorialMode) {
            // add a Details button for all cells
            Button details = new Button("Details");
            details.setOnAction(e2 -> showDetailsPane(e2));
            info.getChildren().addAll(details);
            
            // add set as Hot Cell option if activeCell is any cell but the
            // Space Port
            if (!activeCell.equals(spacePort)) {
                Button setHotCellBtn = new Button();
                setHotCellBtn.setText("Hot Cell");
                setHotCellBtn.setOnAction(e2 -> {
                    hotCell = activeCell;
                    customContextMenu.hide();
                    customContextMenu.getContent().clear();
                });
                info.getChildren().addAll(setHotCellBtn);
            }
        }
        /* END of INFO Buttons */
        
        
        /* OPERATIONS Buttons */
        // create a VBox that contains the operations buttons
        VBox operations = new VBox();
        operations.setAlignment(Pos.CENTER);
        operations.setPadding(new Insets(2));
        operations.setSpacing(2);
        
        // only add operations if the cell is not destroyed
        if (!activeCell.isDestroyed()) {
            // add scout option if cell is not scouted
            if (!activeCell.isScouted() && activeCell.isScoutable()) {
                Button scout = new Button("Scout");
                scout.setOnAction(e2 -> scoutCell(e2, tutorialMode));
                operations.getChildren().add(scout);
            }
            // add capture option if cell is scouted and is not destroyed or
            // captured
            if (activeCell.isScouted() && !activeCell.isCaptured() && activeCell.isCapturable()) {
                Button capture = new Button("Capture");
                capture.setOnAction(e2 -> captureCell(e2, tutorialMode));
                operations.getChildren().add(capture);
            }
            // add set stationed soldiers option if the cell is captured
            if (activeCell.isCaptured()) {
                HBox stationedSoldiersContainer = new HBox();
                stationedSoldiersContainer.setAlignment(Pos.CENTER);
                stationedSoldiersContainer.setSpacing(2);
                
                Label stationedLbl = new Label("Stationed: ");
                stationedLbl.setTextFill(Color.web("#00FF00"));
                stationedLbl.setPrefWidth(80.0);
                
                Spinner<Integer> stationedSpin = new Spinner<Integer>(0, 99,
                        activeCell.getStationedSoldiers());
                stationedSpin.setPrefWidth(68.0);
                
                Image btnIcon = new Image("res/icons/check.png", 16, 16, false, false);
                Button stationedBtn = new Button("", new ImageView(btnIcon));
                stationedBtn.setOnAction(e2 -> {
                    logic.changeStationedSoldiers(stationedSpin.getValue().intValue(),
                            tutorialMode);
                    CellGraphics.drawGraphics(activeCell, false);
                });
                
                stationedSoldiersContainer.getChildren().addAll(stationedLbl, stationedSpin,
                        stationedBtn);
                operations.getChildren().add(stationedSoldiersContainer);
            }
            // add bombard option if cell is: scouted, not destroyed, and not a
            // Objective
            // (add forfeit option if the cell is the Space Port)
            if (activeCell.isScouted() && !activeCell.getCellType().equals("Objective")
                    && activeCell.isDestroyable()) {
                if (activeCell.getCellType().equals("Space Port")) {
                    Button forfeit = new Button("Forfeit");
                    forfeit.setOnAction(e2 -> logic.endGame('F'));
                    operations.getChildren().add(forfeit);
                } else {
                    HBox bombardContainer = new HBox();
                    bombardContainer.setAlignment(Pos.CENTER);
                    bombardContainer.setSpacing(2);
                    
                    Label bombardLbl = new Label("Bombard: ");
                    bombardLbl.setTextFill(Color.CYAN);
                    bombardLbl.setPrefWidth(80.0);
                    
                    // added this variable and if statement to work around when
                    // the number of
                    // enemies in a cell is zero which would make the spinner
                    // throw infinite
                    // exceptions because its upper value would be less than its
                    // lower value
                    int spinnerUpperValue = activeCell.getEnemies();
                    if (spinnerUpperValue < 1) {
                        spinnerUpperValue = 1;
                    }
                    Spinner<Integer> bombardSpin = new Spinner<Integer>(1, spinnerUpperValue,
                            activeCell.getEnemies() + 1);
                    bombardSpin.setPrefWidth(68.0);
                    
                    Image btnIcon = new Image("res/icons/check.png", 16, 16, false, false);
                    Button bombardBtn = new Button("", new ImageView(btnIcon));
                    bombardBtn.setOnAction(e2 -> {
                        bombardCell(e2, bombardSpin.getValue().intValue(), tutorialMode);
                    });
                    
                    bombardContainer.getChildren().addAll(bombardLbl, bombardSpin, bombardBtn);
                    operations.getChildren().add(bombardContainer);
                }
            }
        } else { // display message if cell is destroyed, a Space Port, or a
                 // captured Objective
            Label noOpsAvailMsg = new Label("No Operations\nAvailable");
            operations.getChildren().add(noOpsAvailMsg);
        }
        /* END of OPERATIONS Buttons */
        
        
        /* BUILD Buttons */
        // create a VBox that contains the build buttons
        VBox build = new VBox();
        build.setAlignment(Pos.CENTER);
        build.setPadding(new Insets(2));
        build.setSpacing(2);
        if (activeCell.isCaptured() && !activeCell.isBuiltOn()) {
            // add build housing option if cell is housing compatible
            if (activeCell.isHousingCompatible()) {
                Button hou = new Button("Housing");
                hou.setOnAction(e2 -> build(e2, tutorialMode));
                build.getChildren().add(hou);
            }
            // add build robotics factory option if cell is robotics factory
            // compatible
            if (activeCell.isRFactoryCompatible()) {
                Button rFa = new Button("Robo Factory");
                rFa.setOnAction(e2 -> build(e2, tutorialMode));
                build.getChildren().add(rFa);
            }
            // add build storage option if cell is storage compatible
            if (activeCell.isStorageCompatible()) {
                Button sto = new Button("Storage");
                sto.setOnAction(e2 -> build(e2, tutorialMode));
                build.getChildren().add(sto);
            }
            // add build steel mill option if cell is steel mill compatible
            if (activeCell.isSMillCompatible()) {
                Button sMi = new Button("Steel Mill");
                sMi.setOnAction(e2 -> build(e2, tutorialMode));
                build.getChildren().add(sMi);
            }
            // add build energy grid option if cell is energy grid compatible
            if (activeCell.isEGridCompatible()) {
                Button eGr = new Button("Energy Grid");
                eGr.setOnAction(e2 -> build(e2, tutorialMode));
                build.getChildren().add(eGr);
            }
            // add build solar farm option if cell is solar farm compatible
            if (activeCell.isSFarmCompatible()) {
                Button sFa = new Button("Solar Farm");
                sFa.setOnAction(e2 -> build(e2, tutorialMode));
                build.getChildren().add(sFa);
            }
        } else {
            Label noBuildsAvailMsg = new Label("No Build\nOptions\nAvailable");
            build.getChildren().add(noBuildsAvailMsg);
        }
        /* END of BUILD Buttons */
        
        
        /* ACCORDION PANE (ROOT) */
        // show "Hidden" as the cell name if the cell has not been scouted
        String t1Title = "Hidden";
        if (activeCell.isScouted()) {
            // show actual cell name if the cell has been scouted
            t1Title = activeCell.getCellType();
        }
        if (activeCell.isDestroyed()) {
            // show "Destroyed" as the cell name if the cell has been destroyed
            t1Title = "Distroyed";
        }
        TitledPane infoContainer = new TitledPane(t1Title, info);
        TitledPane operationsContainer = new TitledPane("Operations", operations);
        TitledPane buildContainer = new TitledPane("Build", build);
        // adds everything to an accordion pane
        Accordion accordion = new Accordion();
        accordion.getPanes().addAll(infoContainer, operationsContainer, buildContainer);
        /* END ACCORDION PANE */
        
        // adds accordion to context menu
        customContextMenu.getContent().add(accordion);
        // shows the context menu
        customContextMenu.setX(e.getScreenX());
        customContextMenu.setY(e.getScreenY());
        customContextMenu.setAutoHide(true);
        customContextMenu.show(MAIN.mainStage);
    }
    
    
    /**
     * Brings up the details pane which tells the player more about a particular
     * cell;
     * 
     * @param e the ActionEvent
     */
    private void showDetailsPane(ActionEvent e) {
        // stop the game timer
        logic.stopBgThread();
        
        // hide the context menu
        customContextMenu.hide();
        customContextMenu.getContent().clear();
        // clear the detailsPane of previous details
        detailsPane.getChildren().clear();
        
        detailsPane.setPrefSize(MAIN.STAGE_WIDTH, MAIN.STAGE_HEIGHT);
        detailsPane.setId("detailsPane");
        
        // the back button at the top-left of the screen
        Button backBtn = new Button("BACK");
        AnchorPane.setTopAnchor(backBtn, 25.0);
        AnchorPane.setLeftAnchor(backBtn, 25.0);
        backBtn.setOnAction(e2 -> {
            logic.startBgThread();
            gameUIRoot.getChildren().remove(detailsPane);
        });
        
        // the title of the details pane
        Label title = new Label();
        title.setText("Hidden");
        if (activeCell.isScouted()) {
            title.setText(activeCell.getCellType());
        }
        if (activeCell.isDestroyed()) {
            title.setText("Destroyed");
        }
        title.setId("detailsPaneTitle");
        title.setPrefSize(775.0, 100.0);
        AnchorPane.setTopAnchor(title, 25.0);
        AnchorPane.setLeftAnchor(title, 200.0);
        title.setAlignment(Pos.CENTER_RIGHT);
        
        TextArea description = new TextArea();
        description.setEditable(false);
        description.setWrapText(true);
        description.setPrefSize(400.0, 400.0);
        AnchorPane.setTopAnchor(description, 125.0);
        AnchorPane.setLeftAnchor(description, 25.0);
        description.appendText("I planned to put a whole myriad of information here, "
                + "but unfortunately, I ran out of time.  All of the details needed "
                + "to play the game are displayed on the cells themselves though!!");
        
        
        detailsPane.getChildren().addAll(title, backBtn, description);
        
        gameUIRoot.getChildren().add(detailsPane);
    }
    
    
    /**
     * Sets the value of the activeCell (the one that the player is clicking on)
     * 
     * @param event the onClick MouseEvent from the activeCell
     */
    private void determineActiveCell(MouseEvent event) {
        logic.setActiveCell((GameCell) event.getSource());
        this.activeCell = logic.getActiveCell();
        this.activeContainer = (GameCellContainer) activeCell.getParent();
//////////////////// FOR DEBUGGING ONLY\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
        /*
         * System.out.println(); System.out.print("cellType: " +
         * activeCell.getCellType() + " | "); System.out.print("row: " +
         * activeCell.getRow() + " | "); System.out.print("column: " +
         * activeCell.getCol() + " | "); System.out.print("capturable: " +
         * activeCell.isCapturable() + " | "); System.out.print("scoutable: " +
         * activeCell.isScoutable() + " | "); System.out.println("destroyable: "
         * + activeCell.isDestroyable() + " | "); System.out.print("captured: "
         * + activeCell.isCaptured() + " | "); System.out.print("scouted: " +
         * activeCell.isScouted() + " | "); System.out.print("neutral: " +
         * activeCell.isNeutral() + " | "); System.out.print("hostile: " +
         * activeCell.isHostile() + " | "); System.out.println("destroyed: " +
         * activeCell.isDestroyed() + " | "); System.out.print("hou com: " +
         * activeCell.isHousingCompatible() + " | "); System.out.print(
         * "rFa com: " + activeCell.isRFactoryCompatible() + " | ");
         * System.out.print("sto com: " + activeCell.isStorageCompatible() +
         * " | "); System.out.print("sMi com: " + activeCell.isSMillCompatible()
         * + " | "); System.out.print("eGr com: " +
         * activeCell.isEGridCompatible() + " | "); System.out.println(
         * "sFa com: " + activeCell.isSFarmCompatible() + " | ");
         * System.out.print("soldiers: " + activeCell.getSoldiers() + " | ");
         * System.out.print("materials: " + activeCell.getMaterials() + " | ");
         * System.out.print("cores: " + activeCell.getCores() + " | ");
         * System.out.print("enemies: " + activeCell.getEnemies() + " | ");
         * System.out.print("stationSoldiers: " +
         * activeCell.getStationedSoldiers() + " | "); System.out.println();
         */
//////////////////// END DEBUGGING\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
    
    }
    
    
}
