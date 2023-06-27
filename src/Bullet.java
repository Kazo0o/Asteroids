import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Shape;

/**
 * 
 */

/**
 * @author Lerato Thibile
 *
 */
public class Bullet extends PhysicsObject {
	Circle circle;
	static double muzzle = 400;

	/**
	 * @param parent
	 * @param p0
	 * @param v0
	 * @param theta0
	 * @param omega0
	 */
	public Bullet(Group parent, Point2D p, Point2D vShip, double angle) {
		super(parent, p, vecAngle(angle, muzzle).add(vShip), angle, 0);
		// TODO Auto-generated constructor stub
		circle = new Circle(3, Color.rgb(252, 255, 219));
		transform.getChildren().add(circle);
	}

	@Override
	Shape getShapeBounds() {
		// TODO Auto-generated method stub
		return circle;
	}

}
