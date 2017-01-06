package dynamicStorage;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * This class holds all of the player's preferred settings and the high scores
 * for the particular program that the player is using.
 * 
 * @author Stanton Parham (stparham@ncsu.edu)
 */
public class PrefsHighScores implements Serializable {
    
    /**
     * The class's serial ID number; (I just made it the date that I made this
     * class serializable.)
     */
    private static final long serialVersionUID = 042416L;
    
    
    /**
     * The player's name associated with the first high score;
     */
    private String hS1Name;
    
    /**
     * The actual score associated with the first high score;
     */
    private int hS1Score;
    
    /**
     * The player's name associated with the second high score;
     */
    private String hS2Name;
    
    /**
     * The actual score associated with the second high score;
     */
    private int hS2Score;
    
    /**
     * The player's name associated with the third high score;
     */
    private String hS3Name;
    
    /**
     * The actual score associated with the third high score;
     */
    private int hS3Score;
    
    /**
     * Whether or not music should play;
     */
    private boolean musicOn;
    
    /**
     * Whether or not sound effects should play;
     */
    private boolean sFXOn;
    
    
    /**
     * Creates a new PrefsHighScores object; THIS SHOULD ONLY BE USED IF THERE
     * IS NOT ALREADY ONE OF THESE OBJECTS PRESENT! AFTER CREATING THIS OBJECT
     * IT WILL BE SERIALIZED INTO A FILE AND RELOADED EACH TIME THE PROGRAM RUNS
     * SO IT WILL NEVER NEED TO BE CONSTRUCTED AGAIN.
     */
    public PrefsHighScores() {
        this.musicOn = true;
        this.sFXOn = true;
        this.hS1Name = "ME";
        this.hS1Score = 3;
        this.hS2Name = "MYSELF";
        this.hS2Score = 2;
        this.hS3Name = "I";
        this.hS3Score = 1;
    }
    
    
    /**
     * Attempts to add a passed in score to the high scores if the score is
     * greater than one of the current high scores;
     * 
     * @param playerName the player's name that achieved the passed in score;
     * @param score the score to be set
     * 
     * @return whether or not the score passed in was set as a new high score;
     */
    public boolean addHighScore(String playerName, int score) {
        if (score > hS1Score) {
            hS3Name = hS2Name;
            hS3Score = hS2Score;
            
            hS2Name = hS1Name;
            hS2Score = hS1Score;
            
            hS1Name = playerName;
            hS1Score = score;
            saveThis();
            return true;
        }
        if (score > hS2Score) {
            hS3Name = hS2Name;
            hS3Score = hS2Score;
            
            hS2Name = playerName;
            hS2Score = score;
            saveThis();
            return true;
        }
        if (score > hS3Score) {
            hS3Name = playerName;
            hS3Score = score;
            saveThis();
            return true;
        }
        // the passed in score was not greater than any of the current high
        // scores
        // so it was not set as a high score
        return false;
    }
    
    /**
     * Returns the player's name associated with the first high score;
     * 
     * @return the player's name associated with the first high score
     */
    public String getHS1Name() {
        return hS1Name;
    }
    
    /**
     * Returns the actual score associated with the first high score;
     * 
     * @return the actual score associated with the first high score
     */
    public int getHS1Score() {
        return hS1Score;
    }
    
    /**
     * Returns the player's name associated with the second high score;
     * 
     * @return the player's name associated with the second high score
     */
    public String getHS2Name() {
        return hS2Name;
    }
    
    /**
     * Returns the actual score associated with the second high score;
     * 
     * @return the actual score associated with the second high score
     */
    public int getHS2Score() {
        return hS2Score;
    }
    
    /**
     * Returns the player's name associated with the third high score;
     * 
     * @return the player's name associated with the third high score
     */
    public String getHS3Name() {
        return hS3Name;
    }
    
    /**
     * Returns the actual score associated with the third high score;
     * 
     * @return the actual score associated with the third high score
     */
    public int getHS3Score() {
        return hS3Score;
    }
    
    /**
     * Returns whether or not music is on;
     * 
     * @return whether or not music is on
     */
    public boolean isMusicOn() {
        return musicOn;
    }
    
    /**
     * Returns whether or not sound effects are on;
     * 
     * @return whether or not sound effects are on
     */
    public boolean isSFXOn() {
        return sFXOn;
    }
    
    /**
     * Sets whether or not music is on;
     * 
     * @param value whether or not music is on
     */
    public void setMusicOn(boolean value) {
        this.musicOn = value;
        saveThis();
    }
    
    /**
     * Sets whether or not sound effects is on;
     * 
     * @param value whether or not sound effects is on
     */
    public void setSFXOn(boolean value) {
        this.sFXOn = value;
        saveThis();
    }
    
    /**
     * Saves this object into a save file by serializing it;
     */
    private void saveThis() {
        try {
            FileOutputStream fileOut = new FileOutputStream("serialized-objects/PrefsHighScores.ser");
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(this);
            out.close();
            fileOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    
}
