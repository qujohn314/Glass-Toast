package application.sprites.entities;

import application.Game;
import application.sprites.Sprite;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public abstract class Entity extends Sprite{
	
	protected double handX;
	protected double handY;
	
	public Entity(int xcord, int ycord,double hX, double hY) {
		super(xcord, ycord);
		handX = hX;
		handY = hY;
	}

	public double getHandX() {
		return handX; 
	}
	
	public double getHandY() {
		return handY;
	}
	
	@Override
	protected void setHitBox() {
		hitBox = new Rectangle(x,y,width,height);
		hitBox.setFill(Color.RED);
	}
	
	@Override
	public void render() {	
		
		img.setTranslateX(x);
		img.setTranslateY(y);
	}
}
