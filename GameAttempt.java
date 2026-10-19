import java.util.ArrayList;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class GameAttempt extends Application {
   public static final int WIDTH = 640;
   public static final int HEIGHT = 477;

   public static void main(String[] args) {
      launch(args);
   }

   public void start(Stage stage) {
      stage.setTitle("Connect 4++");
      Group root = new Group();
      Scene scene = new Scene(root);
      stage.setScene(scene);
      stage.setResizable(false);
      Canvas canvas = new Canvas((double)640.0F, (double)477.0F);
      root.getChildren().add(canvas);
      final GraphicsContext gc = canvas.getGraphicsContext2D();
      final ArrayList<Sprite> chips = new ArrayList();
      scene.setOnMousePressed(new EventHandler<MouseEvent>() {
         public void handle(MouseEvent event) {
            Sprite chip = new Sprite();
            chip.setImage("img/red_chip.png");
            chip.setPosition(event.getX() - chip.getWidth() / (double)2.0F, event.getY() - chip.getHeight() / (double)2.0F);
            chip.setVelocity((double)0.0F, (double)0.0F);
            chips.add(chip);
         }
      });
      scene.setOnMouseReleased(new EventHandler<MouseEvent>() {
         public void handle(MouseEvent event) {
            ((Sprite)chips.get(chips.size() - 1)).addVelocity((double)0.0F, (double)100.0F);
         }
      });
      final LongValue lastNanoTime = new LongValue(System.nanoTime());
      (new AnimationTimer() {
         public void handle(long now) {
            double elapsedTime = (double)(now - lastNanoTime.value) / (double)1.0E9F;
            lastNanoTime.value = now;

            for(Sprite chip : chips) {
               for(Sprite s : chips) {
                  if (chip.intersects(s)) {
                     chip.setVelocity((double)0.0F, (double)0.0F);
                     break;
                  }
               }
            }

            for(Sprite chip : chips) {
               chip.update(elapsedTime);
            }

            gc.clearRect((double)0.0F, (double)0.0F, (double)640.0F, (double)477.0F);

            for(Sprite chip : chips) {
               chip.render(gc);
            }

         }
      }).start();
      stage.show();
   }
}
