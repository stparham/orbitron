package main;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

import dynamicStorage.GameInfo;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 * This class controls all of the UI elements displayed while in the main menu
 * scene.
 * 
 * @author Stanton Parham (stparham@ncsu.edu)
 */
public class MainMenuUIController {
    
    /* FXML identifiable objects */
    @FXML // root node
    private AnchorPane mainMenuRoot;
    
    @FXML // menu buttons
    private Button newSessBtn;
    @FXML
    private Button loadSessBtn;
    @FXML
    private Button extrasBtn;
    @FXML
    private Button exitBtn;
    
    @FXML // corresponding menu labels
    private Label newSessLbl;
    @FXML
    private Label loadSessLbl;
    @FXML
    private Label extrasLbl;
    @FXML
    private Label exitLbl;
    
    
    /*
     * below are panes that are displayed depending on which menu buttons are
     * pressed
     */
    /** Displays all of the components needed to load or create a save */
    private AnchorPane saveSelector = new AnchorPane();
    
    /**
     * Displays all of the components needed to change the over-arching options
     * for the game, to see the high scores for the game, and to show the
     * aboutPane;
     */
    private AnchorPane extrasMenu = new AnchorPane();
    
    /**
     * Displays all of the components needed to see information about the game
     */
    private AnchorPane aboutPane = new AnchorPane();
    
    /**
     * Stores the instance of the class so that other classes can reference it;
     */
    private static MainMenuUIController instance;
    
    
    /**
     * The constructor that sets the instance field to reference this;
     */
    public MainMenuUIController() {
        instance = this;
    }
    
    
    /**
     * Returns the instance of the MainMenuUIController class;
     * 
     * @return a reference to the MainMenuUIController instance;
     */
    public static MainMenuUIController getInstance() {
        return instance;
    }
    
    
    /**
     * Shows the extras pane that has several options, a button for seeing
     * information about the game, and the high scores for the game;
     * 
     * 
     * @param e the ActionEvent from the Options/Extras Button on the main menu
     */
    public void showExtrasMenu(ActionEvent e) {
        extrasMenu.getChildren().clear();
        
        // sets up the extras menu pane
        extrasMenu.setId("mainMenuPane");
        extrasMenu.setPrefSize(800.0, 500.0);
        AnchorPane.setTopAnchor(extrasMenu, 48.0);
        AnchorPane.setLeftAnchor(extrasMenu, 102.0);
        
        // creates a label for the title of the extras menu
        Label titleLbl = new Label("Options / Extras");
        titleLbl.setId("paneLabel");
        AnchorPane.setRightAnchor(titleLbl, 25.0);
        AnchorPane.setTopAnchor(titleLbl, 25.0);
        
        // creates a button that returns the user to the main menu
        Button backBtn = new Button("BACK");
        backBtn.setOnAction(e2 -> {
            mainMenuRoot.getChildren().remove(extrasMenu);
        });
        AnchorPane.setTopAnchor(backBtn, 25.0);
        AnchorPane.setLeftAnchor(backBtn, 25.0);
        
        // creates a HIGH SCORES title
        Label highScoreTitle = new Label("HIGH SCORES");
        highScoreTitle.setId("paneLabel");
        // creates the high score labels
        // high score 1
        Label hs1Name = new Label(MAIN.prefsHighScores.getHS1Name());
        hs1Name.setPrefWidth(200.0);
        hs1Name.setId("highScoreLabel");
        Label hs1Score = new Label("" + MAIN.prefsHighScores.getHS1Score());
        hs1Score.setId("highScoreLabel");
        HBox highScore1 = new HBox(25);
        highScore1.setAlignment(Pos.CENTER);
        highScore1.getChildren().addAll(hs1Name, hs1Score);
        // high score 2
        Label hs2Name = new Label(MAIN.prefsHighScores.getHS2Name());
        hs2Name.setPrefWidth(200.0);
        hs2Name.setId("highScoreLabel");
        Label hs2Score = new Label("" + MAIN.prefsHighScores.getHS2Score());
        hs2Score.setId("highScoreLabel");
        HBox highScore2 = new HBox(25);
        highScore2.setAlignment(Pos.CENTER);
        highScore2.getChildren().addAll(hs2Name, hs2Score);
        // high score 3
        Label hs3Name = new Label(MAIN.prefsHighScores.getHS3Name());
        hs3Name.setPrefWidth(200.0);
        hs3Name.setId("highScoreLabel");
        Label hs3Score = new Label("" + MAIN.prefsHighScores.getHS3Score());
        hs3Score.setId("highScoreLabel");
        HBox highScore3 = new HBox(25);
        highScore3.setAlignment(Pos.CENTER);
        highScore3.getChildren().addAll(hs3Name, hs3Score);
        // creates a container to hold the high score labels
        VBox highScoresContainer = new VBox(25);
        highScoresContainer.setAlignment(Pos.CENTER);
        AnchorPane.setLeftAnchor(highScoresContainer, 100.0);
        AnchorPane.setTopAnchor(highScoresContainer, 150.0);
        highScoresContainer.getChildren().addAll(highScoreTitle, highScore1, highScore2, highScore3);
        
        // creates a title for the options
        Label optionsTitle = new Label("OPTIONS");
        optionsTitle.setId("paneLabel");
        // creates a check box for the music
        CheckBox musicCheck = new CheckBox("MUSIC");
        musicCheck.setOnAction(e2 -> MAIN.prefsHighScores.setMusicOn(musicCheck.isSelected()));
        // creates a check box for the sound effects
        CheckBox sfxCheck = new CheckBox("SFX");
        sfxCheck.setOnAction(e2 -> MAIN.prefsHighScores.setSFXOn(sfxCheck.isSelected()));
        // creates a container to hold the options
        VBox options = new VBox(25);
        options.setAlignment(Pos.CENTER_RIGHT);
        AnchorPane.setRightAnchor(options, 100.0);
        AnchorPane.setTopAnchor(options, 150.0);
        options.getChildren().addAll(optionsTitle, musicCheck, sfxCheck);
        
        // creates a button that shows info about the game
        Button aboutBtn = new Button("ABOUT THE GAME");
        aboutBtn.setOnAction(e2 -> {
            mainMenuRoot.getChildren().remove(extrasMenu);
            showAboutPane();
        });
        AnchorPane.setBottomAnchor(aboutBtn, 25.0);
        AnchorPane.setLeftAnchor(aboutBtn, 25.0);
        
        // add the title, back button, and save buttons to the save selector
        extrasMenu.getChildren().addAll(titleLbl, highScoresContainer, options, aboutBtn, backBtn);
        // shows the save selector by adding it to the main menu
        mainMenuRoot.getChildren().add(extrasMenu);
    }
    
    
    /**
     * Shows the save selector pane and all of its components; The save selector
     * pane allows the user to either overwrite a previous save and create a new
     * game or load a previous save depending on the button that they press in
     * the main menu.
     * 
     * @param e the ActionEvent from the Button that was pressed in the main
     *            menu
     */
    public void showSaveSelector(ActionEvent e) {
        saveSelector.getChildren().clear();
        
        // sets up the save selector pane
        saveSelector.setId("mainMenuPane");
        saveSelector.setPrefSize(800.0, 500.0);
        AnchorPane.setTopAnchor(saveSelector, 48.0);
        AnchorPane.setLeftAnchor(saveSelector, 102.0);
        
        // creates a label for the title of the save selector
        // the text will be set later
        Label titleLbl = new Label();
        titleLbl.setId("paneLabel");
        AnchorPane.setRightAnchor(titleLbl, 25.0);
        AnchorPane.setTopAnchor(titleLbl, 25.0);
        
        // creates a button that returns the user to the main menu
        Button backBtn = new Button("BACK");
        backBtn.setOnAction(e2 -> {
            mainMenuRoot.getChildren().remove(saveSelector);
        });
        AnchorPane.setTopAnchor(backBtn, 25.0);
        AnchorPane.setLeftAnchor(backBtn, 25.0);
        
        // creates 3 buttons that represent the saves
        Button save1Btn = new Button("SAVE 1");
        save1Btn.setPrefSize(250.0, 50.0);
        Button save2Btn = new Button("SAVE 2");
        save2Btn.setPrefSize(250.0, 50.0);
        Button save3Btn = new Button("SAVE 3");
        save3Btn.setPrefSize(250.0, 50.0);
        
        // adds the save buttons to a container
        VBox btnContainer = new VBox();
        btnContainer.setSpacing(50.0);
        btnContainer.getChildren().addAll(save1Btn, save2Btn, save3Btn);
        AnchorPane.setTopAnchor(btnContainer, 180.0);
        AnchorPane.setLeftAnchor(btnContainer, 45.0);
        
        // add the title, back button, and save buttons to the save selector
        saveSelector.getChildren().addAll(titleLbl, backBtn, btnContainer);
        // shows the save selector by adding it to the main menu
        mainMenuRoot.getChildren().add(saveSelector);
        
        // option specific code that chooses what to do based on if a new game
        // is being created
        // or if a previous game is being loaded
        if (e.getSource() == newSessBtn) {
            // sets the title of the save selector to indicate creating a new
            // game
            titleLbl.setText("Start a new screening session");
            
            // creates nodes that will get info for creating a new game
            Label saveTitleLbl = new Label();
            saveTitleLbl.setPrefSize(350.0, 50.0);
            saveTitleLbl.setId("paneLabel");
            Label nameLbl = new Label("Name: ");
            nameLbl.setPrefSize(350.0, 50.0);
            nameLbl.setId("newSaveLabel");
            // for the player to type their name
            TextField nameFld = new TextField();
            nameFld.setId("nameField");
            nameFld.setPrefSize(250.0, 50.0);
            nameFld.setPromptText("Enter your name");
            Label diffLbl = new Label("Difficulty: ");
            diffLbl.setPrefSize(350.0, 50.0);
            diffLbl.setId("newSaveLabel");
            // for the player to choose the difficulty
            ChoiceBox<String> diffChooser = new ChoiceBox<>();
            diffChooser.setId("diffChooser");
            diffChooser.setPrefSize(250.0, 50.0);
            diffChooser.getItems().addAll("EASY", "CHALLENGING", "HARD", "INSANE");
            diffChooser.setValue("CHALLENGING");
            Button beginGameBtn = new Button("BEGIN");
            beginGameBtn.setPrefSize(100.0, 30.0);
            
            VBox gameSetUpContainer = new VBox();
            gameSetUpContainer.setVisible(false);
            gameSetUpContainer.setSpacing(10.0);
            gameSetUpContainer.getChildren().addAll(saveTitleLbl, nameLbl, nameFld, diffLbl, diffChooser, beginGameBtn);
            AnchorPane.setTopAnchor(gameSetUpContainer, 100.0);
            AnchorPane.setLeftAnchor(gameSetUpContainer, 400.0);
            // add the game set up nodes to the save selector but they don't
            // show up
            // because gameSetUpContainer is set to not visible
            saveSelector.getChildren().add(gameSetUpContainer);
            
            Label overwriteLbl = new Label();
            overwriteLbl.setPrefSize(470.0, 50.0);
            overwriteLbl.setId("newSaveLabel");
            
            save1Btn.setOnAction(e2 -> {
                saveSelector.getChildren().remove(overwriteLbl);
                saveTitleLbl.setText("SAVE 1");
                gameSetUpContainer.setVisible(true);
                beginGameBtn.setOnAction(e3 -> {
                    String playerName = nameFld.getText();
                    String difficulty = diffChooser.getValue();
                    createNewSave(1, playerName, difficulty);
                });
            });
            save1Btn.setOnMouseEntered(e2 -> {
                if (!gameSetUpContainer.isVisible()) {
                    AnchorPane.setTopAnchor(overwriteLbl, 180.0);
                    AnchorPane.setLeftAnchor(overwriteLbl, 325.0);
                    overwriteLbl.setText("Overwrite Save 1 and Start a New Session");
                    saveSelector.getChildren().add(overwriteLbl);
                }
            });
            save1Btn.setOnMouseExited(e2 -> {
                saveSelector.getChildren().remove(overwriteLbl);
            });
            
            save2Btn.setOnAction(e2 -> {
                saveSelector.getChildren().remove(overwriteLbl);
                saveTitleLbl.setText("SAVE 2");
                gameSetUpContainer.setVisible(true);
                beginGameBtn.setOnAction(e3 -> {
                    String playerName = nameFld.getText();
                    String difficulty = diffChooser.getValue();
                    createNewSave(2, playerName, difficulty);
                });
            });
            save2Btn.setOnMouseEntered(e2 -> {
                if (!gameSetUpContainer.isVisible()) {
                    AnchorPane.setTopAnchor(overwriteLbl, 280.0);
                    AnchorPane.setLeftAnchor(overwriteLbl, 325.0);
                    overwriteLbl.setText("Overwrite Save 2 and Start a New Session");
                    saveSelector.getChildren().add(overwriteLbl);
                }
            });
            save2Btn.setOnMouseExited(e2 -> {
                saveSelector.getChildren().remove(overwriteLbl);
            });
            
            save3Btn.setOnAction(e2 -> {
                saveSelector.getChildren().remove(overwriteLbl);
                saveTitleLbl.setText("SAVE 3");
                gameSetUpContainer.setVisible(true);
                beginGameBtn.setOnAction(e3 -> {
                    String playerName = nameFld.getText();
                    String difficulty = diffChooser.getValue();
                    createNewSave(3, playerName, difficulty);
                });
            });
            save3Btn.setOnMouseEntered(e2 -> {
                if (!gameSetUpContainer.isVisible()) {
                    AnchorPane.setTopAnchor(overwriteLbl, 380.0);
                    AnchorPane.setLeftAnchor(overwriteLbl, 325.0);
                    overwriteLbl.setText("Overwrite Save 3 and Start a New Session");
                    saveSelector.getChildren().add(overwriteLbl);
                }
            });
            save3Btn.setOnMouseExited(e2 -> {
                saveSelector.getChildren().remove(overwriteLbl);
            });
            
            
        } else {
            // sets the title of the save selector to indicate loading a
            // previous game
            titleLbl.setText("Load a previous screening session");
            
            // creates several labels that display information about the save
            // that is about to be loaded
            Label saveTitleLbl = new Label();
            saveTitleLbl.setPrefSize(350.0, 50.0);
            saveTitleLbl.setId("paneLabel");
            Label playerNameLbl = new Label();
            playerNameLbl.setPrefSize(350.0, 50.0);
            playerNameLbl.setId("loadSaveLabel");
            Label difficultyLbl = new Label();
            difficultyLbl.setPrefSize(350.0, 50.0);
            difficultyLbl.setId("loadSaveLabel");
            Label timeElapsedLbl = new Label();
            timeElapsedLbl.setPrefSize(350.0, 50.0);
            timeElapsedLbl.setId("loadSaveLabel");
            Label scoreLbl = new Label();
            scoreLbl.setPrefSize(350.0, 50.0);
            scoreLbl.setId("loadSaveLabel");
            
            // adds the information labels to a container
            VBox lblContainer = new VBox();
            lblContainer.setVisible(false);
            lblContainer.setSpacing(30.0);
            lblContainer.getChildren().addAll(saveTitleLbl, playerNameLbl, difficultyLbl, timeElapsedLbl, scoreLbl);
            AnchorPane.setTopAnchor(lblContainer, 100.0);
            AnchorPane.setLeftAnchor(lblContainer, 400.0);
            // add the info labels to the save selector but they don't show up
            // because lblContainer is set to not visible
            saveSelector.getChildren().add(lblContainer);
            
            // sets the behavior of the first save button
            save1Btn.setOnAction(e2 -> loadSave(1));
            save1Btn.setOnMouseEntered(e2 -> {
                showSaveInformation(1, saveTitleLbl, playerNameLbl, difficultyLbl, timeElapsedLbl, scoreLbl,
                        lblContainer);
            });
            save1Btn.setOnMouseExited(e2 -> {
                // removes the information labels from view when the mouse
                // leaves the button
                lblContainer.setVisible(false);
            });
            
            // sets the behavior of the second save button
            save2Btn.setOnAction(e2 -> loadSave(2));
            save2Btn.setOnMouseEntered(e2 -> {
                showSaveInformation(2, saveTitleLbl, playerNameLbl, difficultyLbl, timeElapsedLbl, scoreLbl,
                        lblContainer);
            });
            save2Btn.setOnMouseExited(e2 -> {
                // removes the information labels from view when the mouse
                // leaves the button
                lblContainer.setVisible(false);
            });
            
            // sets the behavior of the third save button
            save3Btn.setOnAction(e2 -> loadSave(3));
            save3Btn.setOnMouseEntered(e2 -> {
                showSaveInformation(3, saveTitleLbl, playerNameLbl, difficultyLbl, timeElapsedLbl, scoreLbl,
                        lblContainer);
            });
            save3Btn.setOnMouseExited(e2 -> {
                // removes the information labels from view when the mouse
                // leaves the button
                lblContainer.setVisible(false);
            });
        }
    }
    
    
    /**
     * Shows the menu buttons' labels when the mouse enters their region;
     * 
     * @param e the onEnter MouseEvent from the button
     */
    public void showLabel(MouseEvent e) {
        if (e.getSource().equals(newSessBtn)) {
            newSessLbl.setVisible(true);
        } else if (e.getSource().equals(loadSessBtn)) {
            loadSessLbl.setVisible(true);
        } else if (e.getSource().equals(extrasBtn)) {
            extrasLbl.setVisible(true);
        } else if (e.getSource().equals(exitBtn)) {
            exitLbl.setVisible(true);
        }
    }
    
    /**
     * Hides the menu buttons' labels when the mouse exits their region;
     * 
     * @param e the onExit MouseEvent from the button
     */
    public void hideLabel(MouseEvent e) {
        if (e.getSource().equals(newSessBtn)) {
            newSessLbl.setVisible(false);
        } else if (e.getSource().equals(loadSessBtn)) {
            loadSessLbl.setVisible(false);
        } else if (e.getSource().equals(extrasBtn)) {
            extrasLbl.setVisible(false);
        } else if (e.getSource().equals(exitBtn)) {
            exitLbl.setVisible(false);
        }
    }
    
    
    /**
     * Just a way to quickly exit the program;
     * 
     * @param e an ActionEvent from the exitBtn in the FXML
     */
    public void closeProgram(ActionEvent e) {
        System.exit(0);
    }
    
    
    /**
     * Shows the pane that has information about me and how I made the game;
     */
    private void showAboutPane() {
        aboutPane.getChildren().clear();
        
        // sets up the about pane
        aboutPane.setId("mainMenuPane");
        aboutPane.setPrefSize(800.0, 500.0);
        AnchorPane.setTopAnchor(aboutPane, 48.0);
        AnchorPane.setLeftAnchor(aboutPane, 102.0);
        
        // creates a button that returns the user to the main menu
        Button backBtn = new Button("BACK");
        backBtn.setOnAction(e2 -> {
            mainMenuRoot.getChildren().remove(aboutPane);
        });
        AnchorPane.setTopAnchor(backBtn, 25.0);
        AnchorPane.setLeftAnchor(backBtn, 25.0);
        
        Label title = new Label("About the game");
        title.setId("paneLabel");
        AnchorPane.setRightAnchor(title, 25.0);
        AnchorPane.setTopAnchor(title, 25.0);
        
        TextArea info = new TextArea();
        info.setEditable(false);
        info.setWrapText(true);
        info.setPrefSize(600.0, 350.0);
        AnchorPane.setLeftAnchor(info, 25.0);
        AnchorPane.setTopAnchor(info, 100.0);
        info.setText("In this very spot, I was going to put a bunch of stuff about the game "
                + "(like a little bit of context on why it's all spacey themed and why that "
                + "super long phrase is underneath the title) and also how I made the game "
                + "but I ended up not having as "
                + "much time as I had hoped, so I've settled for this: I'm going to put a "
                + "little splurge about the game on my NCSU website (once I get that up and "
                + "running as well) and then I'll probably put here at some point as well.  "
                + "If you're one of the lucky people that happened to get a hold of this game *wink wink*, "
                + "then be sure to check back with me if I ever decide to update it.  You can "
                + "reach me by email at <stparham@ncsu.edu>.  Oh yeah!  My name is Stanton Parham.");
        
        aboutPane.getChildren().addAll(backBtn, title, info);
        
        mainMenuRoot.getChildren().add(aboutPane);
    }
    
    
    /**
     * Recovers and shows the save information for a particular save game on the
     * given labels;
     * 
     * @param saveSlot the save file from which to get the save information
     * @param saveTitleLbl the label to display the save's title on
     * @param playerNameLbl the label to display the save's player name on
     * @param difficultyLbl the label to display the save's difficulty on
     * @param timeElapsedLbl the label to display the save's elapsed time on
     * @param scoreLbl the label to display the save's score on
     * @param lblContainer the container for the labels
     */
    private void showSaveInformation(int saveSlot, Label saveTitleLbl, Label playerNameLbl, Label difficultyLbl,
            Label timeElapsedLbl, Label scoreLbl, VBox lblContainer) {
        // recover GameInfo object
        GameInfo info = null;
        try {
            FileInputStream fileIn = new FileInputStream("serialized-objects/save" + saveSlot + ".ser");
            ObjectInputStream in = new ObjectInputStream(fileIn);
            info = (GameInfo) in.readObject();
            in.close();
            fileIn.close();
            
            // sets the text of the information labels based on the GameInfo
            // object
            saveTitleLbl.setText("SAVE " + saveSlot);
            playerNameLbl.setText("Player: " + info.getPlayerName());
            difficultyLbl.setText("Difficulty: " + info.getDifficulty());
            timeElapsedLbl.setText("Time Elapsed: " + info.getTimeElapsedString());
            scoreLbl.setText("Score: " + info.getScore());
            lblContainer.setVisible(true);
        } catch (IOException ex) {
            // don't do anything because there is no save file
            System.out.println("No Save File");
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        }
    }
    
    /**
     * Creates a new game in the given save slot;
     * 
     * @param saveSlot the save slot in which to create a new game
     * @param playerName the player's name for the new game
     * @param difficulty the difficulty for the new game
     */
    private void createNewSave(int saveSlot, String playerName, String difficulty) {
        // create GameInfo object which stores all of the game's data in the
        // given save slot
        GameInfo info = new GameInfo(saveSlot, playerName, difficulty);
        // create a GameLogic object which runs off of the GameInfo object
        GameLogic logic = new GameLogic(info);
        // load the GameLogic object into the game UI
        GameUIController.getInstance().setLogic(logic);
        // close the saveSelector pane
        mainMenuRoot.getChildren().remove(saveSelector);
        // set the scene to display the game UI
        MAIN.mainStage.setScene(MAIN.gameUIScene);
        // start tutorial
        GameUIController.getInstance().showTutorialPane();
    }
    
    /**
     * Loads a previous game at the from the given save slot;
     * 
     * @param saveSlot the save slot from which to load a game;
     */
    private void loadSave(int saveSlot) {
        // recover GameInfo object from the given save slot
        GameInfo info = null;
        try {
            // recover game info from file
            FileInputStream fileIn = new FileInputStream("serialized-objects/save" + saveSlot + ".ser");
            ObjectInputStream in = new ObjectInputStream(fileIn);
            info = (GameInfo) in.readObject();
            in.close();
            fileIn.close();
            
            // only load the game if the game has not ended (a value of ' '
            // means that the game has not yet ended)
            if (info.getEndGame() == ' ') {
                // regenerate the GameInfo's string properties because they
                // aren't serializable
                // and thus unable to be saved
                info.regenerateStringProperties();
                // create a GameLogic object which runs off of the GameInfo
                // object
                GameLogic logic = new GameLogic(info);
                // redraw the game map
                // this has to be done after creating the GameLogic
                info.redrawGameMap();
                // load the GameLogic object into the game UI
                GameUIController.getInstance().setLogic(logic);
                // close the saveSelector pane
                mainMenuRoot.getChildren().remove(saveSelector);
                // set the scene to display the game UI
                MAIN.mainStage.setScene(MAIN.gameUIScene);
                // start game timer
                logic.startBgThread();
            }
        } catch (IOException e) {
            // don't do anything because there is no save file
            System.out.println("No Save File");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        
    }
    
}
