package controllers.Activities;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import services.ServiceParticipation;
import entities.Activite;
import entities.Participation;
import entities.Utilisateur;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import services.ServiceUtilisateur ;

public class PuzzleController {

    @FXML
    private Pane puzzleArea;

    @FXML
    private Button SUBMIT , mixButton, solveButton;

    @FXML
    private Text statusText;

    @FXML
    private AnchorPane root ;
    private Image mainImage;
    private PuzzlePiece[][] pieces;
    public static final int PIECE_SIZE = 200;



    @FXML
    private void closeStage() {
        Stage stage = (Stage) root.getScene().getWindow();
        stage.close();
    }
    @FXML
    private void minimizeStage() {
        Stage stage = (Stage) root.getScene().getWindow();
        stage.setIconified(true);
    }
    @FXML
    public void initialize() {
        mainImage = new Image(getClass().getResourceAsStream("/images/puzzle-vanGogh.jpg"));
        setupPuzzleArea(puzzleArea, 2, 2);
        setupPuzzle();
    }

    private void setupPuzzleArea(Pane puzzleArea ,int numOfColumns, int numOfRows) {
        double deskWidth = PIECE_SIZE * numOfColumns;
        double deskHeight = PIECE_SIZE * numOfRows;

        puzzleArea.setPrefSize(deskWidth, deskHeight);
        puzzleArea.setMaxSize(deskWidth, deskHeight);
        puzzleArea.autosize();

        Path grid = new Path();
        grid.setStroke(Color.rgb(70, 20, 200));
        puzzleArea.getChildren().add(grid);

        // Adjust grid creation for new dimensions
        for (int col = 0; col < numOfColumns - 1; col++) {
            grid.getElements().addAll(
                    new MoveTo(PIECE_SIZE * (col + 1), 0),
                    new LineTo(PIECE_SIZE * (col + 1), deskHeight)
            );
        }
        for (int row = 0; row < numOfRows - 1; row++) {
            grid.getElements().addAll(
                    new MoveTo(0, PIECE_SIZE * (row + 1)),
                    new LineTo(deskWidth, PIECE_SIZE * (row + 1))
            );
        }
    }

    private void setupPuzzle() {
        int numOfColumns = (int) (mainImage.getWidth() / PIECE_SIZE);
        int numOfRows = (int) (mainImage.getHeight() / PIECE_SIZE);
        pieces = new PuzzlePiece[numOfColumns][numOfRows];
        for (int i = 0; i < numOfColumns; i++) {
            for (int j = 0; j < numOfRows; j++) {
                Image pieceImage = new WritableImage(mainImage.getPixelReader(), i * PIECE_SIZE, j * PIECE_SIZE, PIECE_SIZE, PIECE_SIZE);
                PuzzlePiece piece = new PuzzlePiece(pieceImage, i, j);
                puzzleArea.getChildren().add(piece);
                pieces[i][j] = piece;
                piece.setOnMouseReleased(e -> {
                    checkPiecePlacement(piece);
                });
            }
        }
    }

    private void checkPiecePlacement(PuzzlePiece piece) {
        int correctCol = (int) (piece.getColumn() * PIECE_SIZE);
        int correctRow = (int) (piece.getRow() * PIECE_SIZE);
        if (Math.abs(piece.getTranslateX() - correctCol) < 10 && Math.abs(piece.getTranslateY() - correctRow) < 10) {
            piece.setTranslateX(correctCol);
            piece.setTranslateY(correctRow);
            piece.setCorrectlyPlaced(true);
            statusText.setText("Piece placed correctly!");
            checkCompletion();
        } else {
            piece.setCorrectlyPlaced(false);
        }
    }

    @FXML
    private void handleMixAction() {
        ArrayList<PuzzlePiece> shufflePieces = new ArrayList<>();
        for (PuzzlePiece[] row : pieces) {
            Collections.addAll(shufflePieces, row);
        }
        Collections.shuffle(shufflePieces);
        int index = 0;
        for (int i = 0; i < pieces.length; i++) {
            for (int j = 0; j < pieces[i].length; j++) {
                PuzzlePiece piece = shufflePieces.get(index++);
                piece.setLayoutX(i * PIECE_SIZE);
                piece.setLayoutY(j * PIECE_SIZE);
                piece.toFront(); // Ensure the piece is moved to the front when mixed
            }
            statusText.setText("Mixed!");
        }
    }
    @FXML
    private void handleSolveAction() {
        statusText.setText("Solving...");

        for (int i = 0; i < pieces.length; i++) {
            for (int j = 0; j < pieces[i].length; j++) {
                PuzzlePiece piece = pieces[i][j];
                int correctCol = (int) (piece.getColumn() * PIECE_SIZE);
                int correctRow = (int) (piece.getRow() * PIECE_SIZE);
                piece.setLayoutX(correctCol);
                piece.setLayoutY(correctRow);
                piece.setTranslateX(0); // Reset translate values
                piece.setTranslateY(0);
                piece.toBack(); // Place the piece behind others when solved
            }
        }
        checkCompletion(); // Check if the puzzle is completed after solving
    }
    @FXML
    private void handleSubmitAction() {
        // Check if the puzzle is completed when the submit button is clicked
        checkCompletion();
    }


    @FXML
    private void checkCompletion() {


        boolean isComplete = true;
        for (PuzzlePiece[] row : pieces) {
            for (PuzzlePiece piece : row) {
                if (!piece.isCorrectlyPlaced()) {
                    isComplete = false;
                    break; // Exit loop early if any piece is not correctly placed
                }
            }
            if (!isComplete) break; // Exit the outer loop early if any piece is not correctly placed
        }
        if (isComplete) {
            statusText.setText("Congratulations! Puzzle completed!");
          //  int userId;
          //  handlePuzzleCompletion( userId, activityId);
           // handlePuzzleCompletion();
         //   int userId = getUserIdFromDatabase(); // Retrieve the user's ID
          /*  try {
                handlePuzzleCompletion(userId); // Update the user's score
            } catch (SQLException e) {
                e.printStackTrace(); // Handle the exception according to your application's error handling strategy
            }
*/
        } else {
            statusText.setText("The puzzle is incomplete.");
        }
    }
/*
    private int getUserIdFromDatabase() {
        // Initialize the UtilisateurService or UtilisateurDao object
        ServiceUtilisateur utilisateurService = new ServiceUtilisateur(); // Assuming you have a UtilisateurService class

        // Implement the logic to retrieve the user's ID from the database
        // For example:
        try {
            // Call your method to retrieve the user by ID
            Utilisateur utilisateur = utilisateurService.recupererById();
            if (utilisateur != null) {
                return utilisateur.getUtilisateurId(); // Return the user's ID if found
            } else {
                System.out.println("User not found.");
                return -1; // Return -1 if the user is not found
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Handle the exception according to your application's error handling strategy
            return -1; // Return -1 if an exception occurs
        }
    }
*/
    private void handlePuzzleCompletion(int userID) throws SQLException {
        // Update the user's score when puzzle is completed
        // String updateQuery = "UPDATE Participation SET score = 2500 WHERE utilisateur_id = ? AND id_activite = ?";
        // Assuming you have a method to execute SQL queries in your ServiceParticipation class
        ServiceParticipation.updateUserScoreInParticipation( userID , 2500);
    }
/*
    @FXML
    private void handle9() {
        // Set new number of rows and columns
        int newRows = 3;
        int newColumns = 3;

        // Clear existing puzzle pieces
        puzzleArea.getChildren().clear();

        // Update the pieces array and setup the new puzzle area
        setupPuzzleArea(puzzleArea, newColumns, newRows);
        setupPuzzle(newColumns, newRows); // Make sure you update 'setupPuzzle' method to accept rows and columns
    }*/

}
