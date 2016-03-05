package fr.scale.gcm_scalable.visualisation.content;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

public class MYCanvas extends Canvas {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	//FIELDS
    public int WIDTH  = 1024;
    public int HEIGHT = WIDTH / 16 * 9;

    Queue<MyComponent> listMyComponent         = new ConcurrentLinkedQueue();
    Queue<MyLinkInterface> listMyLinkInterface = new ConcurrentLinkedQueue();
     
    //METHODS
    public void start(){
        Dimension size = new Dimension (WIDTH, HEIGHT);
        setPreferredSize(size);
        paint(null);
    }

    public void paint(Graphics g){
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, WIDTH, HEIGHT);
        for(MyComponent co : listMyComponent){
        	co.paint(g);
        }
        for(MyLinkInterface li : listMyLinkInterface){
        	li.paint(g);
        }
    }
    
    
    int xIndex = 30;
    int yIndex = 30;
    
    public void add(MyComponent my, int xindex, int yindex){
    	my.getCoord().setCoord(xindex,yindex);
    	listMyComponent.add(my);
    }
    public void add(MyComponent my){
    	my.getCoord().setCoord(xIndex,yIndex);
    	listMyComponent.add(my);
    	xIndex += my.width+30;
    	yIndex += my.height+30;
    }

	public void add(MyLinkInterface myl) {
		listMyLinkInterface.add(myl);
	}
	
/**
		private List drawables = new LinkedList();
		
		public MYCanvas(){
			this.setBackground(Color.black);
		}
		public void paint(Graphics g) {
			super.paint(g);
			for (Iterator iter = drawables.iterator(); iter.hasNext();) {
				IDrawable d = (IDrawable) iter.next();
				d.draw(g);	
			}
		}

		public void addDrawable(IDrawable d) {
			drawables.add(d);
			repaint();
		}

		public void removeDrawable(IDrawable d) {
			drawables.remove(d);
			repaint();
		}

		public void clear() {
			drawables.clear();
			repaint();
		}
		
	}
	*/
} 