package controllers.Activities;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

public class PuzzlePiece extends ImageView {
    public static final double SIZE = 250.0; // Adjust the size according to your puzzle piece size

    private boolean correctlyPlaced;
    private int column;
    private int row;
    private double mouseAnchorX;
    private double mouseAnchorY;
    private double initialTranslateX;
    private double initialTranslateY;

    public PuzzlePiece(Image image, int column, int row) {
        super(image);
        this.column = column;
        this.row = row;

        // Create a jigsaw piece clip
      /*  Path clip = createJigsawClip();*/
       /* this.setClip(clip);*/

        // Event handlers for mouse dragging
        this.setOnMousePressed(this::onPiecePressed);
        this.setOnMouseDragged(this::onPieceDragged);
        this.setOnMouseReleased(this::onPieceReleased);
    }
/*
    private Path createJigsawClip() {
        Path path = new Path();
        // Start from one corner of the puzzle piece
        path.getElements().add(new MoveTo(0, 0));
        // Define edges and curves of the puzzle piece
        // Example for standard jigsaw edges, replace with actual values
        path.getElements().add(new LineTo(25, 0)); // Move to the start of curve
        path.getElements().add(new CubicCurveTo(35, -10, 45, 10, 55, 0)); // Example curve
        path.getElements().add(new LineTo(100, 0)); // Finish top edge
        path.getElements().add(new LineTo(100, 25)); // Move down to start of right curve
        // Add more LineTo and CubicCurveTo commands for the other sides
        path.getElements().add(new LineTo(0, 100)); // Finish left edge, back to start
        return path;
    }


*/
    public boolean isCorrectlyPlaced() {
        return correctlyPlaced;
    }

    public void setCorrectlyPlaced(boolean correctlyPlaced) {
        this.correctlyPlaced = correctlyPlaced;
    }

    public int getColumn() {
        return column;
    }

    public int getRow() {
        return row;
    }

    private void onPiecePressed(MouseEvent event) {
        mouseAnchorX = event.getSceneX();
        mouseAnchorY = event.getSceneY();
        initialTranslateX = getTranslateX();
        initialTranslateY = getTranslateY();
    }

    private void onPieceDragged(MouseEvent event) {
        double deltaX = event.getSceneX() - mouseAnchorX;
        double deltaY = event.getSceneY() - mouseAnchorY;
        double newTranslateX = initialTranslateX + deltaX;
        double newTranslateY = initialTranslateY + deltaY;
        setTranslateX(newTranslateX);
        setTranslateY(newTranslateY);
    }

    private void onPieceReleased(MouseEvent event) {
        // Implement logic for piece placement check here
    }
}
