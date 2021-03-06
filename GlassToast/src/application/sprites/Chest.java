package application.sprites;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;

import application.Game;
import application.Interactable;
import application.LootTable;
import application.LootTable.LootElement;
import application.items.Gear;
import application.items.Item;
import application.items.consumables.Consumable;
import application.items.consumables.Sealant;
import application.items.weapons.Weapon;
import application.sprites.entities.Player;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

public class Chest extends Sprite implements Interactable, Comparable<Interactable>{

	public ImageView openLabel;
	private boolean opened;
	private LootTable lootTable;
	public boolean droppingLoot;
	private Timeline lootAnimation;
	private int lootIndex;
	
	public Chest(int xcord, int ycord) {
		super(xcord, ycord);
		width = 32;
		height = 32;
		opened = false;
		scale = 1;
		img.setPreserveRatio(true);
		lootAnimation = new Timeline();
		
		
		setHitBox();
		setBaseSpriteSheet("Chest.png",scale);
		generateFrameViewports(width*scale,2);
		
		game.addSprite(this);
		openLabel = new ImageView();
		openLabel.setVisible(false);
		try {
			openLabel.setImage(new Image(new FileInputStream("src/res/pics/openLabel.png")));
		} catch (FileNotFoundException e) {System.out.println("Error Loading Player");}
		game.textBoxes.getChildren().add(openLabel);
	
		lootTable = new LootTable(new LootElement(15,new Gear(Gear.Type.STEEL),1),new LootElement(25,Consumable.energyCell(),1),new LootElement(20,new Sealant(),1),new LootElement(40,new Gear(Gear.Type.BRONZE),2));
		
	
		
	}

	@Override
	protected void setHitBox() {
		hitBox = new Rectangle(x,y,width,height);
		hitBox.setFill(Color.RED);
		hitBox.setVisible(false);
	}
	
	
	
	@Override
	public void rescale() {
		
		setBaseSpriteSheet("Chest.png",scale);
		generateFrameViewports(width*scale,2);
		if(opened)
			setAnimationFrame(1);
		
		img.setScaleX(Game.scaleX * 0.7);
		img.setScaleY(Game.scaleY* 0.7);
		
		hitBox.setWidth(width*Game.scaleX);
		hitBox.setHeight(height * Game.scaleY);
		
		
		openLabel.setScaleX(Game.scaleX*0.15);
		openLabel.setScaleY(Game.scaleY*0.15);
	} 

	@Override
	public void render() {
		img.setTranslateX(x * Game.scaleX);
		img.setTranslateY(y * Game.scaleY);
		
		openLabel.setTranslateX((x) * Game.scaleX);
		openLabel.setTranslateY((y-20) * Game.scaleY);
		
	
		
	}

	private void dropAnItem(ArrayList<Item> i) {
			i.get(lootIndex).dropItem(this);
			lootIndex++;
	}
	
	@Override
	public void interact(Player p) {
		
		ArrayList<Item> lootedItems = new ArrayList<Item>();
		lootedItems.addAll(lootTable.lootItems(true));
		lootIndex = 0;
		
		lootAnimation.getKeyFrames().add(new KeyFrame(Duration.seconds(0.05), new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				dropAnItem(lootedItems);
			}
		}));
		
		opened = true;
		openLabel.setVisible(false);
	
		
		lootAnimation.setCycleCount(lootedItems.size());
		if(!lootedItems.isEmpty())
			lootAnimation.play();
		
		System.out.println(Arrays.toString(lootedItems.toArray()));
		System.out.println(p.gears);
		setAnimationFrame(1);
	}
	
	@Override
	public boolean getCollision(Sprite s) {
		if(opened)
			return false;
		if (s instanceof Player && !hitBox.intersects(hitBox.parentToLocal(s.getHitBox().getBoundsInParent()))) {
			openLabel.setVisible(false);
			((Player)s).removeInteractRequest(this);
		}

		return hitBox.intersects(hitBox.parentToLocal(s.getHitBox().getBoundsInParent()));
	}

	@Override
	public void onCollide(Sprite s) {
		if(opened)
			return;
		if(s instanceof Player) {
			if(!openLabel.isVisible()) {
				openLabel.setVisible(true);
				((Player)s).addInteractRequest(this);
			}
		}
		
	}

	@Override
	public int compareTo(Interactable i) {
		if(this.getPriorityValue() < i.getPriorityValue())
			return -1;
		else if(this.getPriorityValue() == i.getPriorityValue())
			return 0;
		return 1;
	}

	@Override
	public int getPriorityValue() {
		return 3;
	}

}
