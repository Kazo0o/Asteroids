import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;

/**
 * 
 */

/**
 * @author Lerato Thibile
 *
 */
public abstract class PhysicsObject {
	/**
	 * 2D vectors
	 * p Position of object
	 * v Velocity of object
	 */
	Point2D p, v;
	
	/**
	 * theta Angle of object
	 * omega Angular momentum of object
	 */
	double theta, omega;
	
	Group root, transform;
	
	boolean alive;
	
	final static double TAU = Math.PI * 2;
	
	public PhysicsObject(Group parent, Point2D p0, Point2D v0, double theta0, double omega0) {
		root = new Group();
		transform = new Group();
		root.getChildren().add(transform);
		parent.getChildren().add(root);
		
		p = p0;
		v = v0;
		omega = omega0;
		theta = theta0;
		alive = true;
	}
	
	public void update(double delta) {
		p = p.add(v.multiply(delta));
		theta = (theta + omega * delta) % TAU;
		
		transform.getTransforms().clear();
		transform.getTransforms().addAll(
				new Translate(p.getX(), p.getY()),
				new Rotate(Math.toDegrees(theta))
		);
	}
	
	public void destroy(Group parent) {
		parent.getChildren().remove(root);
		alive = false;
	}
	
	abstract Shape getShapeBounds();
	
	//check for intersections between shapes
	public boolean intersects(PhysicsObject po) {
		return alive && po.alive &&
				!Shape.intersect(getShapeBounds(), po.getShapeBounds())
				.getBoundsInLocal().isEmpty();
	}
	
	public boolean leavingBounds(Rectangle bounds) {
		if(!Shape.intersect(getShapeBounds(), bounds).getBoundsInLocal().isEmpty()) {
			return false;
		}
		
		//custom math for boundary checking
		double x = p.getX();
		double y = p.getY();
		double bx = bounds.getWidth();
		double by = bounds.getHeight();
		
		return
			//if object is on the left side and moving towards left side, return true OR
			x <= 0 && v.getX() <= 0 ||
			//if object on the right side and moving towards the right side return true OR
			x >= bx && v.getX() >= 0 ||
			//if object is on the top and its moving up, return true OR
			y <= 0 && v.getY() <= 0 ||
			//if object is on the bottom and its moving down, return true
			y >= by && v.getY() >= 0;
	}
	
	public static Point2D vecAngle(double angle, double mag) {
		return new Point2D(Math.cos(angle), Math.sin(angle)).multiply(mag);
	}
	
	public static double rand(double min, double max) {
		return Math.random()*(max-min)+min;
	}
}
