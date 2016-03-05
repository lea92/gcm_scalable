package fr.scale.gcm_scalable.visualisation.content;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;

import fr.scale.gcm_scalable.visualisation.content.model.MyLink;
import fr.scale.gcm_scalable.visualisation.content.view.Coord;
import fr.scale.gcm_scalable.visualisation.content.view.IDrawable;

public class MyLinkInterface extends Canvas{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	protected String name;
	
	protected Coord coordSource = new Coord();

	protected Coord coordDest   = new Coord();
	
	protected MyLink mylink;
	
	public MyLinkInterface(String name){
		super();
		this.name = name;
	}
	
	
	public void paint(Graphics g){
        g.setColor(Color.YELLOW);
        g.drawString(name, coordSource.getX(), coordSource.getY());
        g.drawLine(coordSource.getX(), coordSource.getY(), coordDest.getX(), coordDest.getY());
        int radius = 6;
        g.fillOval(coordSource.getX()-radius/2, coordSource.getY()-radius/2, radius, radius);
        //g.drawLine(xE, yE, xE+30, yE+30 );
        //g.drawLine(xE, yE, xE-30, yE-30 );
    }
	
	
	public void setCoord(int xB, int yB, int xE, int yE){
		setCoordBegin(xB,yB);
		setCoordEnd(xE,yE);
	}
	
	public void setCoordBegin(int xB, int yB){
		coordSource.setCoord(xB, yB);
	}
	
	public void setCoordEnd(int xE, int yE){
		coordDest.setCoord(xE, yE);
	}
	
	public MyLink MyLink(){
		return mylink;
	}


	public void setSource(IDrawable myComponent) {
		Coord c = myComponent.getCoord();
		this.coordSource.setX(c.getX() + myComponent.getWidth());
		this.coordSource.setY(c.getY() + myComponent.getHeight());
	}


	public void setDestination(IDrawable myComponent) {
		this.coordDest.setCoord(myComponent.getCoord());
	}

}
