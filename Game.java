import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.effect.Light;
import javafx.scene.effect.Lighting;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Game extends Application {
   private static final int TILE_SIZE = 80;
   private static final int COLUMNS = 7;
   private static final int ROWS = 6;
   public static final int WIDTH = 640;
   public static final int HEIGHT = 690;
   private String p1 = "Player 1";
   private String p2 = "Player 2";
   private Label p1txt;
   private Label p2txt;
   private boolean player1 = true;
   private boolean red = true;
   private boolean canPlay = true;
   private Disc[][] grid = new Disc[7][6];
   private Pane discRoot = new Pane();

   public static void main(String[] args) {
      launch(args);
   }

   public void start(Stage stage) {
      stage.setScene(new Scene(this.createContent()));
      stage.setWidth((double)640.0F);
      stage.setHeight((double)690.0F);
      stage.show();
   }

   private Parent createContent() {
      Pane root = new Pane();
      root.getChildren().add(this.discRoot);
      Shape gridShape = this.makeGrid();
      root.getChildren().add(gridShape);
      root.getChildren().addAll(this.makeColumns());
      this.p1txt = new Label(this.p1);
      this.p2txt = new Label(this.p2);
      this.p1txt.setStyle("-fx-background-color: #00ff00");
      this.p1txt.setTranslateY((double)590.0F);
      this.p2txt.setTranslateY((double)590.0F);
      this.p2txt.setTranslateX((double)540.0F - this.p2txt.getWidth());
      Button redBtn = new Button("RED");
      Button yellowBtn = new Button("YELLOW");
      redBtn.setMinWidth(yellowBtn.getWidth());
      redBtn.setStyle("-fx-background-color: #ff0000");
      yellowBtn.setStyle("-fx-background-color: #ffff00");
      redBtn.setTranslateY((double)590.0F);
      redBtn.setTranslateX((double)220.0F);
      yellowBtn.setTranslateY((double)590.0F);
      yellowBtn.setTranslateX((double)420.0F);
      root.getChildren().addAll(new Node[]{this.p1txt, this.p2txt, redBtn, yellowBtn});
      redBtn.setOnMouseClicked((e) -> this.red = true);
      yellowBtn.setOnMouseClicked((e) -> this.red = false);
      this.p1txt.setOnMouseClicked((e) -> {
         TextInputDialog dialog = new TextInputDialog("Name");
         dialog.setTitle("Change player name");
         dialog.setHeaderText("Enter your name:");
         dialog.setContentText("Name:");
         Optional<String> result = dialog.showAndWait();
         result.ifPresent((name) -> {
            if (!name.equals("")) {
               this.p1 = name;
               this.p1txt.setText(this.p1);
            }

         });
      });
      this.p2txt.setOnMouseClicked((e) -> {
         TextInputDialog dialog = new TextInputDialog("Name");
         dialog.setTitle("Change player name");
         dialog.setHeaderText("Enter your name:");
         dialog.setContentText("Name:");
         Optional<String> result = dialog.showAndWait();
         result.ifPresent((name) -> {
            if (!name.equals("")) {
               this.p2 = name;
               this.p2txt.setText(this.p2);
            }

         });
      });
      return root;
   }

   private Shape makeGrid() {
      Shape shape = new Rectangle((double)640.0F, (double)560.0F);

      for(int y = 0; y < 6; ++y) {
         for(int x = 0; x < 7; ++x) {
            Circle circle = new Circle((double)40.0F);
            circle.setCenterX((double)40.0F);
            circle.setCenterY((double)40.0F);
            circle.setTranslateX((double)(x * 85 + 20));
            circle.setTranslateY((double)(y * 85 + 20));
            shape = Shape.subtract(shape, circle);
         }
      }

      Light.Distant light = new Light.Distant();
      light.setAzimuth((double)45.0F);
      light.setElevation((double)30.0F);
      Lighting lighting = new Lighting();
      lighting.setLight(light);
      lighting.setSurfaceScale((double)5.0F);
      shape.setFill(Color.BLUE);
      shape.setEffect(lighting);
      return shape;
   }

   private List<Rectangle> makeColumns() {
      List<Rectangle> list = new ArrayList();

      for(int x = 0; x < 7; ++x) {
         Rectangle rect = new Rectangle((double)80.0F, (double)560.0F);
         rect.setTranslateX((double)(x * 85 + 20));
         rect.setFill(Color.TRANSPARENT);
         rect.setOnMouseEntered((e) -> rect.setFill(Color.rgb(200, 200, 50, 0.3)));
         rect.setOnMouseExited((e) -> rect.setFill(Color.TRANSPARENT));
         rect.setOnMouseClicked((e) -> {
            if (this.canPlay) {
               this.placeDisc(new Disc(this.red, 80), x);
               this.canPlay = false;
            }

         });
         list.add(rect);
      }

      return list;
   }

   private void placeDisc(Disc disc, int column) {
      int row = 5;

      while(this.getDisc(column, row).isPresent()) {
         --row;
         if (row < 0) {
            break;
         }
      }

      if (row >= 0) {
         this.grid[column][row] = disc;
         this.discRoot.getChildren().add(disc);
         disc.setTranslateX((double)(column * 85 + 20));
         TranslateTransition animation = new TranslateTransition(Duration.seconds((double)0.5F), disc);
         animation.setToY((double)(row * 85 + 20));
         animation.setOnFinished((e) -> {
            if (this.gameEnded(column, row)) {
               this.gameOver();
            }

            this.player1 ^= true;
            this.canPlay = true;
            if (this.player1) {
               this.p2txt.setStyle("-fx-background-color: #ffffff");
               this.p1txt.setStyle("-fx-background-color: #00ff00");
            } else {
               this.p1txt.setStyle("-fx-background-color: #ffffff");
               this.p2txt.setStyle("-fx-background-color: #00ff00");
            }

         });
         animation.play();
      }
   }

   private boolean gameEnded(int column, int row) {
      List<Point2D> vertical = (List)IntStream.rangeClosed(row - 3, row + 3).mapToObj((r) -> new Point2D((double)column, (double)r)).collect(Collectors.toList());
      List<Point2D> horizontal = (List)IntStream.rangeClosed(column - 3, column + 3).mapToObj((c) -> new Point2D((double)c, (double)row)).collect(Collectors.toList());
      Point2D topLeft = new Point2D((double)(column - 3), (double)(row - 3));
      List<Point2D> diagonal1 = (List)IntStream.rangeClosed(0, 6).mapToObj((i) -> topLeft.add((double)i, (double)i)).collect(Collectors.toList());
      Point2D botLeft = new Point2D((double)(column - 3), (double)(row + 3));
      List<Point2D> diagonal2 = (List)IntStream.rangeClosed(0, 6).mapToObj((i) -> botLeft.add((double)i, (double)(-i))).collect(Collectors.toList());
      return this.checkRange(vertical) || this.checkRange(horizontal) || this.checkRange(diagonal1) || this.checkRange(diagonal2);
   }

   private boolean checkRange(List<Point2D> points) {
      int chain = 0;

      for(Point2D p : points) {
         int column = (int)p.getX();
         int row = (int)p.getY();
         Disc disc = (Disc)this.getDisc(column, row).orElse(new Disc(!this.red, 80));
         if (disc.isRed() == this.red) {
            ++chain;
            if (chain == 4) {
               return true;
            }
         } else {
            chain = 0;
         }
      }

      return false;
   }

   private void gameOver() {
      final Alert alert = new Alert(AlertType.CONFIRMATION);
      alert.setTitle("Congrats " + (this.player1 ? this.p1 : this.p2));
      alert.setContentText("Do you want to replay the game?");
      Platform.runLater(new Runnable() {
         public void run() {
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {
               Game.this.discRoot.getChildren().clear();
               Game.this.grid = new Disc[7][6];
            } else {
               System.exit(0);
            }

         }
      });
   }

   private Optional<Disc> getDisc(int column, int row) {
      return column >= 0 && column < 7 && row >= 0 && row < 6 ? Optional.ofNullable(this.grid[column][row]) : Optional.empty();
   }
}
