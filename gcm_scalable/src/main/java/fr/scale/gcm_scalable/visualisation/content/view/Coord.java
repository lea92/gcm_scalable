package fr.scale.gcm_scalable.visualisation.content.view;

public class Coord {
	int x; 
	int y;
	
	public Coord(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public Coord() {
		this(0,0);
	}
	
	public int getX(){ return x;}
	public int getY(){ return y;}

	public void setX(int x){ this.x = x;}
	public void setY(int y){ this.y = y;}
	
	public void setCoord(int x, int y){
		this.x = x;
		this.y = y;
	}

	public void setCoord(Coord coord) {
		this.x = coord.x;
		this.y = coord.y;
	}
}
