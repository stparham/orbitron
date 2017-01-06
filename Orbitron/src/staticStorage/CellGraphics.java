package staticStorage;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import supportingClasses.GameCell;

/**
 * This class holds all of the information needed to draw all of the game cells.
 * By using a GameCell's GraphicsContext, this class draws the cell's graphics.
 * 
 * @author Stanton Parham (stparham@ncsu.edu)
 *
 */
public class CellGraphics {
    
    
    /**
     * One of the main colors that I use throughout the game;
     */
    private static final Color FF8300 = Color.web("#FF8300");
    
    
    /**
     * The GameCell that is being drawn;
     */
    private static GameCell gameCell;
    /**
     * The type of cell being drawn;
     */
    private static String cellType;
    /**
     * The graphics context of the GameCell;
     */
    private static GraphicsContext gc;
    /**
     * The width of the GameCell;
     */
    private static double canvasWidth;
    /**
     * The height of the GameCell;
     */
    private static double canvasHeight;
    /**
     * The main color that should be used when drawing the cell;
     */
    private static Color mainColor;
    /**
     * The font used when drawing the cell;
     */
    private static Font mainFont;
    /**
     * Whether or not the hovered-over version of the cell should be drawn;
     */
    private static boolean drawHoverCell;
    /**
     * Whether or not the cell being drawn is "special"; The "special" cells
     * are: "Space Port", "Objective", "Housing", "Robotics Factory", "Storage",
     * "Steel Mill", "Energy Grid", and "Solar Farm".
     */
    private static boolean specialCell;
    
    
    /**
     * Draws the graphics for the passed in GameCell;
     * 
     * @param cell the cell's whose graphics are to drawn
     * @param hover whether or not to draw the cell in its "hovered-over" form
     */
    public static void drawGraphics(GameCell cell, boolean hover) {
        initializeFields(cell, hover);
        // establish some useful measurement variables for all cells
        double roadWidth = canvasHeight / 14; // the width of the "roads" that
                                              // surround each cell
        double canCen = canvasHeight / 2; // center of canvas both hor. and ver.
                                          // given the canvas is a square
        double outSqrWidth = canvasHeight * 6 / 7; // the width of the large
                                                   // square in every cell
        double onePix = canvasHeight / 150; // 1 pixel for a canvas of size 150
                                            // x 150
        double fivePix = canvasHeight / 30; // 5 pixels for a canvas of size 150
                                            // x 150
        // clear anything already on the canvas
        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, canvasWidth, canvasHeight);
        // set default settings for the GraphicsContext
        setGraphicsSettings(fivePix, onePix, mainColor, mainColor);
        
        
        if (!gameCell.isScouted()) { // draw "fog of war" if cell is not scouted
            drawUnscoutedCell(canCen, onePix, fivePix, roadWidth);
        } else if (gameCell.isDestroyed()) { // draw destroyed cell if cell has
                                             // been bombarded
            drawDestroyedCell(canCen, onePix, fivePix);
        } else { // draw the cell types graphics
            drawCellBasics(canCen, outSqrWidth, onePix, fivePix, roadWidth);
            setGraphicsSettings(fivePix, onePix, mainColor, mainColor);
            
            if (specialCell) {
                switch (cellType) {
                case "Space Port":
                    drawSpacePort(canCen, outSqrWidth, onePix, fivePix, roadWidth);
                    break;
                case "Objective":
                    drawObjective(canCen, outSqrWidth, onePix, fivePix, roadWidth);
                    break;
                default:
                    drawStructure(canCen, outSqrWidth, onePix, fivePix, roadWidth);
                    break;
                }
            } else {
                drawRegularCell(canCen, outSqrWidth, onePix, fivePix, roadWidth);
            }
            
        }
    }
    
    /**
     * Initializes the fields of this class; This method isn't really necessary
     * but I originally wrote this class to be constructable, and I didn't want
     * to rewrite all of the fields as variables in the methods.
     * 
     * @param cell the game cell's whose graphics are to be drawn
     * @param hover whether or not to draw the "hovered-over" form of this cell
     */
    private static void initializeFields(GameCell cell, boolean hover) {
        gameCell = cell;
        drawHoverCell = hover;
        cellType = gameCell.getCellType();
        gc = gameCell.getGraphicsContext2D();
        canvasHeight = gameCell.getHeight();
        canvasWidth = gameCell.getWidth();
        mainFont = Font.loadFont(
                CellGraphics.class.getResource("/res/fonts/Orbitron.ttf").toExternalForm(),
                canvasHeight / 12.5);
        
        // determine if a special cell
        if (cellType.equals("Space Port") || cellType.equals("Objective")
                || cellType.equals("Housing") || cellType.equals("Robotics Factory")
                || cellType.equals("Storage") || cellType.equals("Steel Mill")
                || cellType.equals("Energy Grid") || cellType.equals("Solar Farm")) {
            specialCell = true;
        } else {
            specialCell = false;
        }
        
        // determine color for drawing
        if (gameCell.isScouted() && !gameCell.isDestroyed()) { // only assign a
                                                               // color if the
                                                               // cell
                                                               // has been
                                                               // scouted and is
                                                               // not destroyed
            if (gameCell.isCaptured()) {
                mainColor = Color.web("#00FF00");
            } else if (gameCell.isHostile()) {
                mainColor = Color.RED;
            } else { // cell is neutral
                mainColor = Color.WHITE;
            }
        } else {
            mainColor = Color.BLACK;
        }
        
        if (drawHoverCell) {
            mainColor = Color.web("#FF8300");
        }
    }
    
    /**
     * Sets the cell's GraphicsContext's settings to the passed in values;
     * 
     * @param lineDashes the line dash pattern
     * @param lineWidth the width of the lines that are drawn
     * @param fillColor the color that the graphics' fills are drawn in
     * @param strokeColor the color that the graphics' strokes are drawn in
     */
    private static void setGraphicsSettings(double lineDashes, double lineWidth, Color fillColor,
            Color strokeColor) {
        gc.setFont(mainFont);
        gc.setLineDashes(lineDashes);
        gc.setLineWidth(lineWidth);
        gc.setFill(fillColor);
        gc.setStroke(strokeColor);
    }
    
    
    /**
     * Draws the basics of every cell that is scouted and not destroyed;
     * 
     * @param canCen center of canvas both hor. and ver. given the canvas is a
     *            square
     * @param outSqrWidth the width of the large square in every cell
     * @param onePix 1 pixel for a canvas of size 150 x 150
     * @param fivePix 5 pixels for a canvas of size 150 x 150
     * @param roadWidth the width of the "roads" that surround each cell
     */
    private static void drawCellBasics(double canCen, double outSqrWidth, double onePix,
            double fivePix, double roadWidth) {
        /* Every Cell Has An Outer Square */
        gc.strokeRect(canCen - outSqrWidth / 2, canCen - outSqrWidth / 2, outSqrWidth, outSqrWidth);
        
        /* Every Cell Has Bordering Lines */
        
        gc.setLineDashes(null);
        gc.setLineWidth(onePix);
        gc.setStroke(Color.YELLOW);
        // left line
        gc.strokeLine(0, roadWidth, 0, canvasHeight - roadWidth);
        // top line
        gc.strokeLine(roadWidth, 0, canvasHeight - roadWidth, 0);
        // right line
        gc.strokeLine(canvasHeight - 0, roadWidth, canvasHeight - 0, canvasHeight - roadWidth);
        // bottom line
        gc.strokeLine(roadWidth, canvasHeight - 0, canvasHeight - roadWidth, canvasHeight - 0);
        
    }
    
    /**
     * Draws all of the regular cells (all of the ones that are not special:
     * "Space Port", "Objective", "Housing", "Robotics Factory", "Storage",
     * "Steel Mill", "Energy Grid", "Solar Farm");
     * 
     * @param canCen center of canvas both hor. and ver. given the canvas is a
     *            square
     * @param outSqrWidth the width of the large square in every cell
     * @param onePix 1 pixel for a canvas of size 150 x 150
     * @param fivePix 5 pixels for a canvas of size 150 x 150
     * @param roadWidth the width of the "roads" that surround each cell
     */
    private static void drawRegularCell(double canCen, double outSqrWidth, double onePix,
            double fivePix, double roadWidth) {
        // surround with try/catch block in case the passed in cell has a cell
        // type that is not recognized
        try {
            String url = CTInfo.getCTImgURLs()[gameCell.getCellTypeIndex()];
            Image pic = new Image(url, 50, 50, false, false);
            gc.drawImage(pic, outSqrWidth - fivePix * 10, outSqrWidth - fivePix * 10, fivePix * 10,
                    fivePix * 10);
            
            gc.fillText(cellType.replace(' ', '\n').toUpperCase(), roadWidth + onePix * 2,
                    roadWidth + gc.getFont().getSize());
            // only draw resource indicators if the cell hasn't been captured
            // yet
            if (!gameCell.isCaptured()) {
                gc.setFill(FF8300);
                gc.fillText("S: " + gameCell.getSoldiers(), roadWidth + onePix * 2,
                        roadWidth + gc.getFont().getSize() * 3 + onePix * 4);
                
                gc.setFill(Color.FIREBRICK);
                gc.fillText("M: " + gameCell.getMaterials(), roadWidth + onePix * 2,
                        roadWidth + gc.getFont().getSize() * 4 + onePix * 6);
                
                gc.setFill(Color.CYAN);
                gc.fillText("C: " + gameCell.getCores(), roadWidth + onePix * 2,
                        roadWidth + gc.getFont().getSize() * 5 + onePix * 8);
                
                if (gameCell.isHostile()) {
                    gc.setFill(Color.RED);
                    gc.fillText("E: " + gameCell.getEnemies(), roadWidth + onePix * 2,
                            roadWidth + gc.getFont().getSize() * 7 + onePix * 10);
                }
            } else { // if the cell is captured then draw the number of
                     // stationed soldiers in that cell
                gc.fillText("Stationed: " + gameCell.getStationedSoldiers(), roadWidth + onePix * 2,
                        roadWidth + gc.getFont().getSize() * 3 + onePix * 4);
            }
        } catch (IndexOutOfBoundsException e) {
            // catches the exception that would result if the cell type did not
            // exist
            // and draws the default cell in that case
            drawDefault();
        }
    }
    
    /**
     * Draws a default cell that is easily distinguishable from others; If this
     * cell is drawn, then there is most likely not a draw cell type method
     * defined within this class that corresponds to the given cell type.
     */
    private static void drawDefault() {
        gc.setFill(FF8300);
        gc.fillOval(0, 0, canvasWidth, canvasHeight);
        gc.setFill(Color.BLACK);
        gc.fillText("DEFAULT", canvasWidth / 2, canvasHeight / 2);
    }
    
    
    /**
     * Draws the Space Port cell's graphics;
     * 
     * @param canCen center of canvas both hor. and ver. given the canvas is a
     *            square
     * @param outSqrWidth the width of the large square in every cell
     * @param onePix 1 pixel for a canvas of size 150 x 150
     * @param fivePix 5 pixels for a canvas of size 150 x 150
     */
    private static void drawSpacePort(double canCen, double outSqrWidth, double onePix,
            double fivePix, double roadWidth) {
        String url = CTInfo.getCTImgURLs()[gameCell.getCellTypeIndex()];
        Image pic = new Image(url, 150, 150, false, false);
        gc.drawImage(pic, canCen - outSqrWidth / 2, canCen - outSqrWidth / 2, outSqrWidth,
                outSqrWidth);
        
        
        // green circle in the middle of the Space Port image
        gc.fillOval(canCen - fivePix * 2.5, canCen - fivePix * 2.5, fivePix * 5, fivePix * 5);
    }
    
    
    /**
     * Draws the Objective cell's graphics;
     * 
     * @param canCen center of canvas both hor. and ver. given the canvas is a
     *            square
     * @param outSqrWidth the width of the large square in every cell
     * @param onePix 1 pixel for a canvas of size 150 x 150
     * @param fivePix 5 pixels for a canvas of size 150 x 150
     */
    private static void drawObjective(double canCen, double outSqrWidth, double onePix,
            double fivePix, double roadWidth) {
        // cell specific measurement variables
        double circRadius = canvasHeight * 8 / 21;
        
        String url = CTInfo.getCTImgURLs()[gameCell.getCellTypeIndex()];
        Image pic = new Image(url, 190, 190, false, false);
        gc.drawImage(pic, canCen - outSqrWidth / 2, canCen - outSqrWidth / 2, outSqrWidth,
                outSqrWidth);
        
        
        gc.setLineDashes(null);
        gc.setLineWidth(onePix);
        gc.strokeOval(canCen - circRadius, canCen - circRadius, circRadius * 2, circRadius * 2);
        
        gc.setLineDashes(fivePix);
        gc.strokeRect(canCen - outSqrWidth / 2, canCen - outSqrWidth / 2, outSqrWidth, outSqrWidth);
    }
    
    /**
     * Draws the graphics of any of the structure's that can be built by the
     * player; Structures: Housing, Robotics Factory, Storage, Steel Mill,
     * Energy Grid, Solar Farm;
     * 
     * @param canCen center of canvas both hor. and ver. given the canvas is a
     *            square
     * @param outSqrWidth the width of the large square in every cell
     * @param onePix 1 pixel for a canvas of size 150 x 150
     * @param fivePix 5 pixels for a canvas of size 150 x 150
     */
    private static void drawStructure(double canCen, double outSqrWidth, double onePix,
            double fivePix, double roadWidth) {
        
        // establish url as just the cellPics directory
        String url = "res/cellPics/";
        for (int i = 0; i < cellType.length(); i++) {
            if (cellType.charAt(i) != ' ') {
                // progressively add each letter in cell type if it's not a
                // space
                url += cellType.charAt(i);
            }
        }
        // add file extension
        url += ".png";
        
        Image pic = new Image(url, 160, 160, false, false);
        gc.drawImage(pic, canCen - outSqrWidth / 4, canCen - outSqrWidth / 4, outSqrWidth / 2,
                outSqrWidth / 2);
        
    }
    
    
    /**
     * Draws a "fog of war" (essentially a blank square);
     * 
     * @param canCen center of canvas both hor. and ver. given the canvas is a
     *            square
     * @param onePix 1 pixel for a canvas of size 150 x 150
     * @param fivePix 5 pixels for a canvas of size 150 x 150
     */
    private static void drawUnscoutedCell(double canCen, double onePix, double fivePix,
            double roadWidth) {
        setGraphicsSettings(fivePix, onePix, Color.BLACK, Color.YELLOW);
        gc.fillRect(0, 0, canvasWidth, canvasHeight);
        /* Bordering Lines */
        gc.setLineDashes(null);
        gc.setLineWidth(onePix);
        // left line
        gc.strokeLine(0, roadWidth, 0, canvasHeight - roadWidth);
        // top line
        gc.strokeLine(roadWidth, 0, canvasHeight - roadWidth, 0);
        // right line
        gc.strokeLine(canvasHeight - 0, roadWidth, canvasHeight - 0, canvasHeight - roadWidth);
        // bottom line
        gc.strokeLine(roadWidth, canvasHeight - 0, canvasHeight - roadWidth, canvasHeight - 0);
        if (drawHoverCell) {
            setGraphicsSettings(fivePix, onePix, mainColor, mainColor);
            gc.strokeLine(0, 0, canvasHeight, canvasHeight);
            gc.strokeLine(canvasHeight, 0, 0, canvasHeight);
            
        }
    }
    
    /**
     * Draws a cracked (not really haha) pattern as the cell's graphics;
     * 
     * @param canCen center of canvas both hor. and ver. given the canvas is a
     *            square
     * @param onePix 1 pixel for a canvas of size 150 x 150
     * @param fivePix 5 pixels for a canvas of size 150 x 150
     */
    private static void drawDestroyedCell(double canCen, double onePix, double fivePix) {
        gc.setStroke(Color.WHITE);
        gc.strokeLine(0, 0, canvasWidth, canvasWidth);
        gc.strokeLine(0, canvasHeight, canvasWidth, 0);
        gc.strokeLine(canCen, 0, canCen, canvasHeight);
        gc.strokeLine(0, canCen, canvasWidth, canCen);
    }
    
}
