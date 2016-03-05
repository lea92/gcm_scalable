package fr.scale.gcm_scalable.visualisation.content.view;

import java.awt.Graphics;
import java.awt.Rectangle;

public interface IDrawable {
	
	//public  void draw(Graphics g);

	//public Rectangle getRectangle();
	
	public  void setCoord(Coord c);
	
	public  Coord getCoord();

	public int getWidth();

	public int getHeight();
}