package staticStorage;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.SequentialTransition;
import javafx.animation.Timeline;
import javafx.scene.control.ScrollPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import supportingClasses.GameCell;
import supportingClasses.GameCellContainer;

/**
 * This class holds all of the information needed to render all of the
 * animations for cells when they are being scouted, captured, built on, or
 * bombarded by the player and when cells are captured by enemies. This class
 * also holds the information needed to render animations such as automatically
 * scrolling to a double-clicked cell, the Space Port, or the Hot Cell. I also
 * wanted to add some animations that made the different panes in my UI appear
 * quite extravagantly. I did not have enough time to do this.
 * 
 * <pre>
 * 
 * I wanted to add a lot more diverse and in-depth animations but I ended up not
 * having enough time to do so. I originally hoped to create different
 * animations for: 
 * 1) scouting a cell 
 * 2) capturing a neutral cell 
 * 3) capturing a
 * hostile cell 
 * 4) building a structure on a cell 
 * 5) bombarding a neutral cell
 * 6) bombarding a captured cell 
 * 7) bombarding a hostile cell and the enemies
 * survive 
 * 8) bombarding a hostile cell and the enemies are defeated 
 * 9) enemies attempting to capture a cell and being defeated 
 * 10) enemies attempting to capture a cell and being victorious
 * 
 * I was only able to have animations for scouting a cell, capturing a cell
 * (generally), building on a cell, bombarding a cell, and enemies attempting to
 * capture a cell.
 * </pre>
 * 
 * @author Stanton Parham (stparham@ncsu.edu)
 */
public final class Animations {
    
    /**
     * Creates and returns the animation for scouting a cell; The animation will
     * only start playing when the .play() is called on the returned animation.
     * 
     * @param activeContainer the container for the cell where the animation is
     *            to be rendered
     * 
     * @return the animation for scouting a particular cell
     */
    public static SequentialTransition getAnimScout(GameCellContainer activeContainer) {
        // stores the width and height of the container so later code isn't so
        // bulky
        double width = activeContainer.getWidth();
        double height = activeContainer.getHeight();
        
        // the rectangle that will slowly disappear as the scouted cell is
        // revealed
        Rectangle obscuringRectangle = new Rectangle(0.0, 0.0, width, height);
        obscuringRectangle.setFill(Color.BLACK);
        obscuringRectangle.setMouseTransparent(true);
        
        KeyValue kv00 = new KeyValue(obscuringRectangle.opacityProperty(), 0.0);
        KeyValue kv01 = new KeyValue(obscuringRectangle.visibleProperty(), false);
        KeyFrame kf0 = new KeyFrame(Duration.millis(750), kv00, kv01);
        Timeline dissapear = new Timeline(kf0);
        
        SequentialTransition anim = new SequentialTransition(dissapear);
        
        // puts the activeContainer on top of the others so that any animation
        // that goes past
        // the activeContainer's normal boundaries will not be obscured by the
        // other cells
        activeContainer.toFront();
        // adds the animation nodes to the container
        activeContainer.getChildren().addAll(obscuringRectangle);
        
        // removes the animation nodes upon finishing
        anim.setOnFinished(e -> {
            activeContainer.getChildren().removeAll(obscuringRectangle);
        });
        
        // Draw the cell graphics as soon as the animation is returned because
        // they need to be
        // present for the animation to seem like the cell is being revealed. If
        // the cell graphics
        // weren't drawn immediately then the animation wouldn't even appear to
        // be playing because
        // it would just be slowly reducing the opacity of the obscuring
        // Rectangle to reveal
        // another black Rectangle which are the original cell graphics.
        CellGraphics.drawGraphics((GameCell) activeContainer.getChildren().get(0), false);
        return anim;
    }
    
    /**
     * Creates and returns the animation for capturing a cell;
     * 
     * @param activeContainer the container for the cell where the animation is
     *            to be rendered
     * 
     * @return the animation for capturing a particular cell
     */
    public static SequentialTransition getAnimCapture(GameCellContainer activeContainer) {
        // the circle in the animation
        Circle circle = new Circle(activeContainer.getWidth() / 2, activeContainer.getHeight() / 2,
                activeContainer.getWidth() / 15);
        circle.setFill(Color.web("#00FF00AA"));
        circle.setMouseTransparent(true);
        
        KeyValue kv00 = new KeyValue(circle.scaleXProperty(), 5);
        KeyValue kv01 = new KeyValue(circle.scaleYProperty(), 5);
        KeyFrame kf0 = new KeyFrame(Duration.millis(250), kv00, kv01);
        Timeline scale0 = new Timeline(kf0);
        
        KeyValue kv10 = new KeyValue(circle.scaleXProperty(), 2);
        KeyValue kv11 = new KeyValue(circle.scaleYProperty(), 2);
        KeyFrame kf1 = new KeyFrame(Duration.millis(250), kv10, kv11);
        Timeline scale1 = new Timeline(kf1);
        
        KeyValue kv20 = new KeyValue(circle.scaleXProperty(), 10);
        KeyValue kv21 = new KeyValue(circle.scaleYProperty(), 10);
        KeyValue kv22 = new KeyValue(circle.visibleProperty(), false);
        KeyFrame kf2 = new KeyFrame(Duration.millis(250), kv20, kv21, kv22);
        Timeline scale2 = new Timeline(kf2);
        
        SequentialTransition anim = new SequentialTransition(scale0, scale1, scale2);
        
        // puts the activeContainer on top of the others so that any animation
        // that goes past
        // the activeContainer's normal boundaries will not be obscured by the
        // other cells
        activeContainer.toFront();
        activeContainer.getChildren().addAll(circle);
        
        // removes the animation nodes and draws the updated cell graphics upon
        // finishing
        anim.setOnFinished(e -> {
            activeContainer.getChildren().removeAll(circle);
            CellGraphics.drawGraphics((GameCell) activeContainer.getChildren().get(0), false);
        });
        return anim;
    }
    
    /**
     * Creates and returns the animation for bombarding a cell;
     * 
     * @param activeContainer the container for the cell where the animation is
     *            to be rendered
     * 
     * @return the animation for bombarding a particular cell
     */
    public static SequentialTransition getAnimBombard(GameCellContainer activeContainer) {
        
        // the circles in the animation
        Circle circle1 = new Circle(activeContainer.getWidth() / 6, activeContainer.getHeight() / 4,
                activeContainer.getWidth() / 50);
        circle1.setStrokeWidth(0.1);
        circle1.setStroke(Color.WHITE);
        circle1.setFill(Color.TRANSPARENT);
        circle1.setMouseTransparent(true);
        
        Circle circle2 = new Circle(activeContainer.getWidth() * 5 / 6,
                activeContainer.getHeight() / 2, activeContainer.getWidth() / 50);
        circle2.setStrokeWidth(0.1);
        circle2.setStroke(Color.WHITE);
        circle2.setFill(Color.TRANSPARENT);
        circle2.setMouseTransparent(true);
        
        Circle circle3 = new Circle(activeContainer.getWidth() * 2 / 3,
                activeContainer.getHeight() * 5 / 6, activeContainer.getWidth() / 50);
        circle3.setStrokeWidth(0.1);
        circle3.setStroke(Color.WHITE);
        circle3.setFill(Color.TRANSPARENT);
        circle3.setMouseTransparent(true);
        
        
        KeyValue kv00 = new KeyValue(circle1.scaleXProperty(), 25);
        KeyValue kv01 = new KeyValue(circle1.scaleYProperty(), 25);
        KeyFrame kf0 = new KeyFrame(Duration.millis(500), kv00, kv01);
        Timeline phase0 = new Timeline(kf0);
        
        KeyValue kv10 = new KeyValue(circle1.scaleXProperty(), 30);
        KeyValue kv11 = new KeyValue(circle1.scaleYProperty(), 30);
        KeyValue kv12 = new KeyValue(circle1.opacityProperty(), 0.0);
        KeyValue kv13 = new KeyValue(circle2.scaleXProperty(), 25);
        KeyValue kv14 = new KeyValue(circle2.scaleYProperty(), 25);
        KeyFrame kf1 = new KeyFrame(Duration.millis(500), kv10, kv11, kv12, kv13, kv14);
        Timeline phase1 = new Timeline(kf1);
        
        KeyValue kv20 = new KeyValue(circle2.scaleXProperty(), 30);
        KeyValue kv21 = new KeyValue(circle2.scaleYProperty(), 30);
        KeyValue kv22 = new KeyValue(circle2.opacityProperty(), 0.0);
        KeyValue kv23 = new KeyValue(circle3.scaleXProperty(), 25);
        KeyValue kv24 = new KeyValue(circle3.scaleYProperty(), 25);
        KeyFrame kf2 = new KeyFrame(Duration.millis(500), kv20, kv21, kv22, kv23, kv24);
        Timeline phase2 = new Timeline(kf2);
        
        KeyValue kv30 = new KeyValue(circle3.scaleXProperty(), 30);
        KeyValue kv31 = new KeyValue(circle3.scaleYProperty(), 30);
        KeyValue kv32 = new KeyValue(circle3.opacityProperty(), 0.0);
        KeyFrame kf3 = new KeyFrame(Duration.millis(500), kv30, kv31, kv32);
        Timeline phase3 = new Timeline(kf3);
        
        SequentialTransition anim = new SequentialTransition(phase0, phase1, phase2, phase3);
        
        // puts the activeContainer on top of the others so that any animation
        // that goes past
        // the activeContainer's normal boundaries will not be obscured by the
        // other cells
        activeContainer.toFront();
        activeContainer.getChildren().addAll(circle1, circle2, circle3);
        
        // removes the animation nodes and draws the updated cell graphics upon
        // finishing
        anim.setOnFinished(e -> {
            activeContainer.getChildren().removeAll(circle1, circle2, circle3);
            CellGraphics.drawGraphics((GameCell) activeContainer.getChildren().get(0), false);
        });
        return anim;
    }
    
    /**
     * Creates and returns the animation for building on a cell;
     * 
     * @param activeContainer the container for the cell where the animation is
     *            to be rendered
     * 
     * @return the animation for building on a particular cell
     */
    public static SequentialTransition getAnimBuild(GameCellContainer activeContainer) {
        double width = activeContainer.getWidth();
        double height = activeContainer.getHeight();
        
        // the rectangle that slowly obscures the cell
        Rectangle obscuringRectangle = new Rectangle(0.0, 0.0, width, height);
        obscuringRectangle.setFill(Color.BLACK);
        obscuringRectangle.setOpacity(0.0);
        obscuringRectangle.setMouseTransparent(true);
        
        KeyValue kv00 = new KeyValue(obscuringRectangle.opacityProperty(), 1.0);
        KeyFrame kf0 = new KeyFrame(Duration.millis(750), kv00);
        Timeline appear = new Timeline(kf0);
        
        SequentialTransition anim = new SequentialTransition(appear);
        
        // puts the activeContainer on top of the others so that any animation
        // that goes past
        // the activeContainer's normal boundaries will not be obscured by the
        // other cells
        activeContainer.toFront();
        activeContainer.getChildren().addAll(obscuringRectangle);
        
        // removes the animation nodes and draws the updated cell graphics upon
        // finishing
        anim.setOnFinished(e -> {
            activeContainer.getChildren().removeAll(obscuringRectangle);
            CellGraphics.drawGraphics((GameCell) activeContainer.getChildren().get(0), false);
            // calls the second phase of build so that the new structure is
            // revealed
            getAnimBuildPhase2(activeContainer).play();
        });
        return anim;
    }
    
    /**
     * Creates and returns the 2nd phase of the building animation;
     * 
     * @param activeContainer the container for the cell where the animation is
     *            to be rendered
     * 
     * @return the 2nd phase of the building animation
     */
    private static SequentialTransition getAnimBuildPhase2(GameCellContainer activeContainer) {
        double width = activeContainer.getWidth();
        double height = activeContainer.getHeight();
        
        // the rectangle that slowly disappears as the new structure is revealed
        Rectangle obscuringRectangle = new Rectangle(0.0, 0.0, width, height);
        obscuringRectangle.setFill(Color.BLACK);
        obscuringRectangle.setMouseTransparent(true);
        
        KeyValue kv00 = new KeyValue(obscuringRectangle.opacityProperty(), 0.0);
        KeyValue kv01 = new KeyValue(obscuringRectangle.visibleProperty(), false);
        KeyFrame kf0 = new KeyFrame(Duration.millis(750), kv00, kv01);
        Timeline dissapear = new Timeline(kf0);
        
        
        SequentialTransition anim = new SequentialTransition(dissapear);
        
        /*
         * puts the activeContainer on top of the others so that any animation
         * that goes past the activeContainer's normal boundaries will not be
         * obscured by the other cells
         */
        activeContainer.toFront();
        // adds the animation nodes to the container
        activeContainer.getChildren().addAll(obscuringRectangle);
        
        // removes the animation nodes upon finishing
        anim.setOnFinished(e -> {
            activeContainer.getChildren().removeAll(obscuringRectangle);
        });
        
        return anim;
    }
    
    /**
     * Creates and returns the animation for enemies spreading to a cell; I
     * actually wasn't able to create an animation for this because I would be
     * altering the UI from a Thread other than the JavaFX Application Thread.
     * 
     * @param activeContainer the container for the cell where the animation is
     *            to be rendered
     * 
     * @return the animation for enemies spreading to a particular cell
     */
    public static SequentialTransition getAnimEnemySpread(GameCellContainer activeContainer) {
        /*
         * @formatter:off (turn code formatter off for this comment)
         * 
         * @formatter:on (turn code formatter on for rest of code)
         * 
         * //the circle in the animation Circle circle = new
         * Circle(activeContainer.getWidth() / 2, activeContainer.getHeight() /
         * 2, activeContainer.getWidth() / 15);
         * circle.setFill(Color.web("#FF0000AA"));
         * circle.setMouseTransparent(true);
         * 
         * KeyValue kv00 = new KeyValue(circle.scaleXProperty(), 5); KeyValue
         * kv01 = new KeyValue(circle.scaleYProperty(), 5); KeyFrame kf0 = new
         * KeyFrame(Duration.millis(250), kv00, kv01); Timeline scale0 = new
         * Timeline(kf0);
         * 
         * KeyValue kv10 = new KeyValue(circle.scaleXProperty(), 2); KeyValue
         * kv11 = new KeyValue(circle.scaleYProperty(), 2); KeyFrame kf1 = new
         * KeyFrame(Duration.millis(250), kv10, kv11); Timeline scale1 = new
         * Timeline(kf1);
         * 
         * KeyValue kv20 = new KeyValue(circle.scaleXProperty(), 10); KeyValue
         * kv21 = new KeyValue(circle.scaleYProperty(), 10); KeyValue kv22 = new
         * KeyValue(circle.visibleProperty(), false); KeyFrame kf2 = new
         * KeyFrame(Duration.millis(250), kv20, kv21, kv22); Timeline scale2 =
         * new Timeline(kf2);
         * 
         * SequentialTransition anim = new SequentialTransition(scale0, scale1,
         * scale2);
         * 
         * //puts the activeContainer on top of the others so that any animation
         * that goes past //the activeContainer's normal boundaries will not be
         * obscured by the other cells activeContainer.toFront();
         * activeContainer.getChildren().addAll(circle);
         */
        
        /*
         * I could not add the actual animation of enemies spreading to a cell
         * above because I would essentially be changing the UI from a Thread
         * other than the JavaFX thread. The fact that you are not allowed to do
         * that has held me back from doing so many things in this game,
         * including making an animation that occurs because of another Thread.
         */
        KeyFrame blah2 = new KeyFrame(Duration.millis(88));
        Timeline blah = new Timeline(blah2);
        SequentialTransition anim = new SequentialTransition(blah);
        // removes the animation nodes and draws the updated cell graphics upon finishing
        anim.setOnFinished(e -> {
            // activeContainer.getChildren().removeAll(circle);
            CellGraphics.drawGraphics((GameCell) activeContainer.getChildren().get(0), false);
        });
        return anim;
    }
    
    
    /**
     * The animation for moving the view from one cell to another automatically;
     * 
     * @param activeCell the cell to center the view on
     * @param scrollPane the ScrollPane object that holds the grid of cells
     * @return the animation for moving from one cell to another automatically
     */
    public static SequentialTransition getAnimJumpToCell(GameCell activeCell,
            ScrollPane scrollPane) {
        
        double hValue = ((scrollPane.getHmax() / 10) * activeCell.getCol()
                - (scrollPane.getHmax() / 10) * 2);
        double vValue = ((scrollPane.getVmax() / 6.75) * activeCell.getRow()
                - (scrollPane.getVmax() / 6.75));
        
        KeyValue kv1 = new KeyValue(scrollPane.hvalueProperty(), hValue);
        
        KeyValue kv2 = new KeyValue(scrollPane.vvalueProperty(), vValue);
        
        KeyFrame kf1 = new KeyFrame(Duration.millis(1000), kv1, kv2);
        
        Timeline moveTo = new Timeline();
        moveTo.getKeyFrames().add(kf1);
        
        SequentialTransition anim = new SequentialTransition(moveTo);
        return anim;
    }
    
    
}
