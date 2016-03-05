package fr.scale.gcm_scalable.visualisation.content;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JPanel;
import javax.swing.JTextArea;

import fr.scale.gcm_scalable.visualisation.content.view.Coord;
import fr.scale.gcm_scalable.visualisation.content.view.IDrawable;

public class MyComponent implements IDrawable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	protected String name;
	protected Coord cordonate;
	protected int width;
	protected int height;
	protected int sizePoliceW = 6;
	protected int sizePoliceH = 15;
	
	public MyComponent(String name){
		this(name, 40,40);
	}
	
	public MyComponent(String name,int x, int y){
		super();
		this.name = name;
		this.cordonate= new Coord(x,y);

		this.width  = sizePoliceW*this.name.length();
		this.height = sizePoliceH;
	}
	

    public void paint(Graphics g){
        g.setColor(Color.BLUE);
        g.fillRect(cordonate.getX(), cordonate.getY(), width,height);
        g.setColor(Color.CYAN);
        g.drawString(name, cordonate.getX(), cordonate.getY()+height);
    }


	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}


	@Override
	public void setCoord(Coord c) {
		cordonate.setCoord(c);
	}

	@Override
	public Coord getCoord() {
		return cordonate;
	}

	public String getName() {
		return name;
	}
}
