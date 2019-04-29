package application;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import application.entities.Player;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Chest extends Sprite implements Interactable{

	public ImageView openLabel;
	
	public Chest(int xcord, int ycord,Game g) {
		super(xcord, ycord,g);
		width = 56;
		height = 56;
		try {
			img.setImage(new Image(new FileInputStream("src/res/pics/Chest.png")));
		} catch (FileNotFoundException e) {System.out.println("Error Loading Pic");}
		img.setScaleX(1);
		img.setScaleY(1);
		setHitBox();
		game.addSprite(this);
		openLabel = new ImageView();
		openLabel.setVisible(false);
		try {
			openLabel.setImage(new Image(new FileInputStream("src/res/pics/openLabel.png")));
		} catch (FileNotFoundException e) {System.out.println("Error Loading Player");}
		game.textBoxes.getChildren().add(openLabel);
		

		
	}

	@Override
	protected void setHitBox() {
		hitBox = new Rectangle(x,y,width,height);
		hitBox.setFill(Color.RED);
		
	}
	
	
	
	@Override
	public void rescale() {
		img.setScaleX(Game.scaleX*0.55);
		img.setScaleY(Game.scaleY*0.55);
		
		hitBox.setWidth(width*Game.scaleX*0.55);
		hitBox.setHeight(height * Game.scaleY*0.55);
		
		
		openLabel.setScaleX(Game.scaleX*0.15);
		openLabel.setScaleY(Game.scaleY*0.15);
	}

	@Override
	public void render() {
		img.setTranslateX(x * Game.scaleX);
		img.setTranslateY(y * Game.scaleY);
		
		
		
		openLabel.setTranslateX((x+1) * Game.scaleX);
		openLabel.setTranslateY((y-20) * Game.scaleY);
		
	}
	@Override
	public Rectangle2D getHitBox() {
		return new Rectangle2D((x-5)* Game.scaleX,y * Game.scaleY,width*Game.scaleX * 0.55,height * Game.scaleY * 0.55);
	}

	@Override
	public void interact() {
		
		
		System.out.println("Interacted!");
	}
	
	@Override
	public boolean getCollision(Sprite s) {
		if (s instanceof Player && !this.getHitBox().intersects(s.getHitBox())) {
			openLabel.setVisible(false);
			((Player)s).removeInteractRequest(this);
		}
		return this.getHitBox().intersects(s.getHitBox());
	}

	@Override
	public void onCollide(Sprite s) {
		if(s instanceof Player) {
			if(!openLabel.isVisible()) {
				openLabel.setVisible(true);
				((Player)s).addInteractRequest(this);
			}
		}
		
	}

}