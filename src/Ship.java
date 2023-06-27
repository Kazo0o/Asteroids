import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Shape;
import javafx.scene.transform.Scale;

/**
 * 
 */

/**
 * @author Lerato Thibile
 *
 */
public class Ship extends PhysicsObject {
	//speed of the ship
	double thrust = 150;
	
	//ship's shape
	Polygon pgon;

	/**
	 * @param parent
	 * @param p0
	 * @param v0
	 * @param theta0
	 * @param omega0
	 */
	public Ship(Group parent, Point2D p0) {
		super(parent, p0, Point2D.ZERO, 0, 0);
		//triangle
		pgon = new Polygon(0.7, 0, -0.7, -0.4, -0.7, 0.4);
		
		transform.getChildren().add(pgon);
		
		pgon.setStroke(Color.rgb(196, 237, 253));
		pgon.setStrokeWidth(0.1);
		pgon.getTransforms().add(new Scale(30, 30));
		
		update(0, 0, 0);
	}

	public void update(double delta, double omega, double throttle) {
		if(throttle != 0) {
			Point2D acc = vecAngle(theta, thrust * throttle);
			v = v.add(acc.multiply(delta));
		}else {
			v = v.multiply(1-0.2*delta);
		}
		
		this.omega = omega;
		super.update(delta);
	}
	
	@Override
	public Shape getShapeBounds() {
		return pgon;
	}

}
