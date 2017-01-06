package main;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Optional;

import dynamicStorage.PrefsHighScores;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

//TODO TASKS
/* 
* 1) add sounds and music
* 
* 2) add tool tips to all controls (some buttons already have tool tips)
* 
* 3) add keyboard controls for controlling game flow more quickly
*/
//TODO TASKS

//TODO KNOWN GLITCHES
/*
* 1) if the context menu of a cell is open while an enemy captures it, the player can still 
*    choose whichever options were available before the cell became hostile
*    
* 2) game does not end properly when enemies capture Space Port (Loss)
* 
* 3) after a game ends (either by Win or Forfeit) the resources of a new game are initialized to 
*    the values of the previous game
*    
* 4) This is more of a game exploit than a glitch: To prevent the possibility of losing the game 
*    by having the Space Port captured by enemies, the player can capture a few cells around the 
*    Space Port, then bombard the four cells to the top, bottom, left, and right of the Space Port 
*    thus cutting it off from any enemy attacks but leaving the already captured cells open to the 
*    rest of the map.
*/
//TODO KNOWN GLITCHES

/**
 * Starts the program and loads the scenes and user preferences and high scores
 * for the game;
 * 
 * @author Stanton Parham (stparham@ncsu.edu)
 *
 */
public class MAIN extends Application {
    
    /** The width of the program window; */
    public static final double STAGE_WIDTH = 1000.0;
    /** The height of the program window; */
    public static final double STAGE_HEIGHT = 593.0;
    
    
    /**
     * Stores the stage of the window so that other classes can reference it;
     */
    public static Stage mainStage;
    /** Stores the menu scene for easy reference; */
    public static Scene mainMenuScene;
    /** Stores the game scene for easy reference; */
    public static Scene gameUIScene;
    /** Stores the PrefsHighScores object */
    public static PrefsHighScores prefsHighScores;
    
    /**
     * Stores the instance of the class so that other classes can reference it;
     */
    private static MAIN instance;
    
    
    /**
     * The constructor that sets the instance field to reference this;
     */
    public MAIN() {
        instance = this;
    }
    
    /**
     * Returns the instance of the MAIN class;
     * 
     * @return a reference to the MAIN instance;
     */
    public static MAIN getInstance() {
        return instance;
    }
    
    /**
     * Every JavaFX application begins with a start() method;
     */
    @Override
    public void start(Stage stage) {
        // catches any errors that from loading the scenes
        try {
            // assign fields
            mainStage = stage;
            mainMenuScene = new Scene(FXMLLoader.load(getClass().getResource("MainMenuUI.fxml")));
            gameUIScene = new Scene(FXMLLoader.load(getClass().getResource("GameUI.fxml")));
            
            /*
             * Catches any exceptions from loading the user preferences object;
             * I put this try/catch inside of the outer try/catch because I want
             * this one to do something whereas I want the outer one to stop
             * execution.
             */
            try {
                // recover game info from file
                FileInputStream fileIn = new FileInputStream(
                        "serialized-objects/PrefsHighScores.ser");
                ObjectInputStream in = new ObjectInputStream(fileIn);
                prefsHighScores = (PrefsHighScores) in.readObject();
                in.close();
                fileIn.close();
            } catch (IOException e) {
                /*
                 * Create a new PrefsHighScores object if one cannot be read
                 * from file
                 */
                prefsHighScores = new PrefsHighScores();
                /*
                 * And yet another try/catch block to catch any exceptions when
                 * saving the user preferences to file
                 */
                try {
                    FileOutputStream fileOut = new FileOutputStream(
                            "serialized-objects/PrefsHighScores.ser");
                    ObjectOutputStream out = new ObjectOutputStream(fileOut);
                    out.writeObject(prefsHighScores);
                    out.close();
                    fileOut.close();
                } catch (IOException e2) {
                    e2.printStackTrace();
                }
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            
            // settings for mainStage
            stage.setOnCloseRequest(e -> {
                // only show close confirmation box if in the gameUIScene
                if (stage.getScene() == gameUIScene) {
                    confirmClose(e);
                }
            });
            stage.setResizable(false);
            stage.setTitle("Orbitron");
            Image windowIcon = new Image("/res/imgs/MainSymbol.png", 32, 32, false, false);
            stage.getIcons().add(windowIcon);
            
            // load the initial scene and corresponding resources
            stage.setScene(mainMenuScene);
            // Sets the font for this JavaFX application
            Font.loadFont(getClass().getResourceAsStream("/res/fonts/Orbitron.ttf"), 12);
            mainMenuScene.getStylesheets()
                    .add(getClass().getResource("Orbitron.css").toExternalForm());
            gameUIScene.getStylesheets()
                    .add(getClass().getResource("Orbitron.css").toExternalForm());
            
            // display the stage
            stage.show();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Shows a confirmation window when the window's exit button is pressed;
     * 
     * @param e the close event produced from clicking the "X" on the actual
     *            program window;
     */
    private void confirmClose(WindowEvent e) {
        Alert closeConfirmation = new Alert(Alert.AlertType.CONFIRMATION,
                "Are you sure you want to close the program?\n\n"
                        + "If you are currently in a session, then your progress will not be saved!");
        Button exitButton = (Button) closeConfirmation.getDialogPane().lookupButton(ButtonType.OK);
        exitButton.setText("Close");
        closeConfirmation.setHeaderText("Confirm Close");
        closeConfirmation.initModality(Modality.APPLICATION_MODAL);
        closeConfirmation.initOwner(mainStage);
        
        Optional<ButtonType> closeResponse = closeConfirmation.showAndWait();
        if (!ButtonType.OK.equals(closeResponse.get())) {
            e.consume();
        }
    }
    
    /**
     * This is just a fail-safe in case the start method does not work; The
     * documentation on JavaFX has more info about this.
     * 
     * @param args NONE
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
