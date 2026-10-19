import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class Disc extends Circle {
   private final boolean red;

   public Disc(boolean red, int size) {
      super((double)(size / 2), red ? Color.RED : Color.YELLOW);
      this.red = red;
      this.setCenterX((double)(size / 2));
      this.setCenterY((double)(size / 2));
   }

   public boolean isRed() {
      return this.red;
   }
}
