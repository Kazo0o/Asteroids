import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.stage.Stage;
import javafx.scene.*;
import javafx.scene.control.Label;
import javafx.scene.input.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.geometry.*;
import javafx.animation.*;
import java.util.HashSet;
import java.util.Set; 
import java.util.List;
import java.util.LinkedList;

/**
 * 
 */

/**
 * @author Lerato Thibile
 *
 */
public class Main extends Application{
	//size of window
	Point2D size = new Point2D(800, 700);
	
	//set of keys that are being held down
	Set<KeyCode> keysDown = new HashSet<>();
	
	int key(KeyCode k) {
		return keysDown.contains(k) ? 1 : 0;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		launch(args);
	}

	@Override
	public void start(Stage stage) throws Exception {
		// TODO Auto-generated method stub
		Group gRoot = new Group();
		Scene scene = new Scene(gRoot, size.getX(), size.getY());
		
		stage.setScene(scene);
		stage.setTitle("Asteroids");
		scene.setFill(Paint.valueOf("black"));
		
		Label fpsLabel = new Label();
		fpsLabel.setTranslateX(2);
		fpsLabel.setTextFill(Paint.valueOf("White"));
		
		Group gGame = new Group();
		Group gAsteroids = new Group();
		Group gBullets = new Group();
		gGame.getChildren().addAll(gAsteroids, gBullets);
		
		Ship ship = new Ship(gGame, size.multiply(0.5));
		
		Label loseLabel = new Label();
		loseLabel.setTranslateX(size.getX()/2-200);
		loseLabel.setTranslateY(size.getY()/2-100);
		loseLabel.setTextFill(Color.WHITE);
		loseLabel.setFont(Font.font(30));
		loseLabel.setVisible(false);
		
		List<Asteroid> asteroids = new LinkedList<>();
		List<Bullet> bullets = new LinkedList<>();
		
		gRoot.getChildren().addAll(gGame, loseLabel, fpsLabel);
		
		//input handling
		scene.setOnKeyPressed(new EventHandler<KeyEvent>() {

			public void handle(KeyEvent event) {
				keysDown.add(event.getCode());
			}
			
		});
		
		scene.setOnKeyReleased(new EventHandler<KeyEvent>() {

			public void handle(KeyEvent event) {
				keysDown.remove(event.getCode());
			}
			
		});
		
		//SETUP
		
		Rectangle bounds = new Rectangle(0, 0, size.getX(), size.getY());
		bounds.setVisible(false);
		gRoot.getChildren().add(bounds);
		
		AnimationTimer loop = new AnimationTimer() {
			double old = -1;
			double elapsed = 0;
			
			double bulletWait = 0.3;
			double bulletTimer = 0;
			
			int score = 0;
			boolean died = false;
			
			int asteroidCount = 15;
			
			public void handle(long nano) {
				if(old < 0) {
					old = nano;
				}
				double delta = (nano - old) / 1e9;
				old = nano;
				elapsed += delta;
				
				fpsLabel.setText(String.format("%.2f %.2f", 1/delta, elapsed));
				
				for(Asteroid aster : asteroids) {
					//basic collision handling
					for(Bullet bullet: bullets) {
						if(aster.intersects(bullet)) {
							aster.destroy(gAsteroids);
							bullet.destroy(gBullets);
							score++;
							break;
						}
					}
					
					//if the asteroid is already 'dead'
					if(!aster.alive) {
						continue;
					}
					
					if(aster.intersects(ship)) {
						ship.destroy(gGame);
						aster.destroy(gAsteroids);
						loseLabel.setText(String.format("You blew up!\nYou survived %.2f seconds\nand destroyed %d asteroids",
								elapsed, score));
						loseLabel.setVisible(true);
						died = true;
					}
					//remove from scene
					if(aster.leavingBounds(bounds)) {
						aster.destroy(gAsteroids);
					}else {
						aster.update(delta);
					}
				}
				for(Bullet b : bullets) {
					//remove from scene
					if(b.leavingBounds(bounds)) {
						b.destroy(gBullets);
					}else {
						b.update(delta);
					}
				}
				
				//delete from actual game logic
				asteroids.removeIf(a -> !a.alive);
				bullets.removeIf(b -> !b.alive);
				
				//add asteroids to scene
				while(asteroids.size() < asteroidCount) {
					asteroids.add(Asteroid.make(gAsteroids, size));
				}
				
				if(!died) {
					if(key(KeyCode.SPACE) == 1 && bulletTimer <= 0) {
						Bullet b = new Bullet(gBullets, ship.p, ship.v, ship.theta);
						bullets.add(b);
						bulletTimer = bulletWait;
					}
					
					
					bulletTimer -= delta;
					
					if(ship.leavingBounds(bounds)) {
						ship.destroy(gGame);
						loseLabel.setText(String.format("You flew away!\nYou survived %.2f seconds\nand destroyed %d asteroids",
								elapsed, score));
						loseLabel.setVisible(true);
						died = true;
					}else {
					//ship movement
					//helps with handling direction of rotation
					double rot = 4 * (key(KeyCode.D)-key(KeyCode.A));
					ship.update(delta, rot, key(KeyCode.W));
				}
			}
		}
	};
		
		loop.start();
		
		stage.show();
	}

}
