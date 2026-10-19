import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class Sprite {
   private Image image;
   private double positionX = (double)0.0F;
   private double positionY = (double)0.0F;
   private double velocityX = (double)0.0F;
   private double velocityY = (double)0.0F;
   private double width;
   private double height;

   public void setImage(Image i) {
      this.image = i;
      this.width = i.getWidth();
      this.height = i.getHeight();
   }

   public void setImage(String filename) {
      Image i = new Image(filename);
      this.setImage(i);
   }

   public void setPosition(double x, double y) {
      this.positionX = x;
      this.positionY = y;
   }

   public double getWidth() {
      return this.width;
   }

   public double getHeight() {
      return this.height;
   }

   public void setVelocity(double x, double y) {
      this.velocityX = x;
      this.velocityY = y;
   }

   public void addVelocity(double x, double y) {
      this.velocityX += x;
      this.velocityY += y;
   }

   public void update(double time) {
      this.positionX += this.velocityX * time;
      this.positionY += this.velocityY * time;
   }

   public void render(GraphicsContext gc) {
      gc.drawImage(this.image, this.positionX, this.positionY);
   }

   public Rectangle2D getBoundary() {
      return new Rectangle2D(this.positionX, this.positionY, this.width, this.height);
   }

   public boolean intersects(Sprite s) {
      return s.getBoundary().intersects(this.getBoundary());
   }

   public String toString() {
      return " Position: [" + this.positionX + "," + this.positionY + "] Velocity: [" + this.velocityX + "," + this.velocityY + "]";
   }
}
