package fr.scale.gcm_scalable.visualisation.content;

import java.awt.Font;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;

public class MyWindow extends JFrame{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	MYCanvas panneau ;
	
	private static void setUIFont(javax.swing.plaf.FontUIResource f)
	{
	    java.util.Enumeration keys = UIManager.getDefaults().keys();
	    while (keys.hasMoreElements())
	    {
	        Object key = keys.nextElement();
	        Object value = UIManager.get(key);
	        if (value instanceof javax.swing.plaf.FontUIResource)
	        {
	            UIManager.put(key, f);
	        }
	    }
	}
	
	public MyWindow(){
		super("Vizualisation ADL...");
		setUIFont(new javax.swing.plaf.FontUIResource(new Font("Courier",Font.PLAIN, 12)));
		WindowListener l = new WindowAdapter() {
			public void windowClosing(WindowEvent e){
				System.exit(0);
			}
		};
		
		addWindowListener(l);
		panneau = new MYCanvas();
		this.add(panneau);
		this.repaint();

		setSize(400,400);
		setVisible(true);
	}
	
	public MYCanvas getMYCanvas(){
		return panneau;
	}

}
