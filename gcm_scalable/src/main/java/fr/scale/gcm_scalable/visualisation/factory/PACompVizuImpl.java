package fr.scale.gcm_scalable.visualisation.factory;

import java.util.HashMap;
import java.util.Map;

import org.objectweb.fractal.api.Component;
import org.objectweb.proactive.core.component.identity.PAComponent;

import fr.scale.gcm_scalable.controller.membrane.Hierarchy;
import fr.scale.gcm_scalable.controller.membrane.Hierarchy.Link;
import fr.scale.gcm_scalable.controller.membrane.Hierarchy.Node;
import fr.scale.gcm_scalable.visualisation.content.MYCanvas;
import fr.scale.gcm_scalable.visualisation.content.MyComponent;
import fr.scale.gcm_scalable.visualisation.content.MyLinkInterface;
import fr.scale.gcm_scalable.visualisation.content.MyWindow;

public class PACompVizuImpl {
	
	
	public PACompVizuImpl(){
		
	}
	
	public PACompVizuImpl(Component compositeWrapper){
		MyWindow w = new MyWindow();
		MYCanvas canvas = w.getMYCanvas();
		
		System.out.println("Compinents");

		Map<String,MyComponent> mp = new HashMap<String,MyComponent>();

		Node node = new Hierarchy.Node(compositeWrapper);

		int ind  = 0;
		java.util.Stack<Node> queue = new java.util.Stack();
		queue.add(node);
		while(!queue.isEmpty()){
			Node child = queue.pop();
			MyComponent coc = new MyComponent(child.getRoot().getName());

			canvas.add(coc,child.level*(100*(child.num 
					+((child.hasNodeParent())? child.getParent().num : 0)
					)), child.level*100);
			mp.put(coc.getName(),coc);
			System.err.println(coc.getName());
			if(child.hasChildren()){
				queue.addAll(child.getChildren());
			}
			ind++;
		}

		echo(node.toString());
		echo(Hierarchy.Link.strLink());


		for(Link myl : Link.maplink){
			MyLinkInterface alinki = new MyLinkInterface(myl.getLink());
			System.err.println(myl.getClient().getName());
			if(mp.containsKey(myl.getClient().getName())){
				alinki.setSource(mp.get(myl.getClient().getName()));
				alinki.setDestination(mp.get(myl.getServer().getName()));
				canvas.add(alinki);
			}
		}

		w.repaint();
		System.out.println("Fin");

	}
	

	private static void echo(String str){
		System.out.println(str);
	}

}
