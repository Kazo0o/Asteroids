import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Shape;

/**
 * 
 */

/**
 * @author Kazo0o
 *
 */
public class Asteroid extends PhysicsObject {
	Circle circle;

	/**
	 * @param parent
	 * @param p0
	 * @param v0
	 * @param theta0
	 * @param omega0
	 */
	public Asteroid(Group parent, double radius, Point2D p0, Point2D v0, double omega) {
		super(parent, p0, v0, 0, omega);
		
		circle = new Circle(radius, Color.TRANSPARENT);
		circle.setStroke(Color.WHITE);
		circle.setStrokeWidth(3);
		
		transform.getChildren().add(circle);
		
		update(0);
	}

	@Override
	Shape getShapeBounds() {
		// TODO Auto-generated method stub
		return circle;
	}
	
	public static Asteroid make(Group parent, Point2D size) {
		double angle = Math.random() * TAU;
		double radius = rand(20, 30);
		double omega = rand(-2, 2);
		double distCenter = size.magnitude() / 2 * rand(1,2);
		Point2D p = vecAngle(angle, distCenter).add(size.multiply(0.5));
		Point2D v = vecAngle(Math.PI + angle + rand(-0.2,0.2), rand(50, 100));
		
		return new Asteroid(parent, radius, p, v, omega);
	}

}
