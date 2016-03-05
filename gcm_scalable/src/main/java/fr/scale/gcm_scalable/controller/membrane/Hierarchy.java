package fr.scale.gcm_scalable.controller.membrane;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.etsi.uri.gcm.util.GCM;
import org.objectweb.fractal.api.Component;
import org.objectweb.fractal.api.NoSuchInterfaceException;
import org.objectweb.fractal.api.type.InterfaceType;
import org.objectweb.proactive.core.component.Constants;
import org.objectweb.proactive.core.component.Utils;
import org.objectweb.proactive.core.component.control.PAContentController;
import org.objectweb.proactive.core.component.identity.PAComponent;

import fr.scale.gcm_scalable.controller.membrane.Hierarchy.Node.Root;

public class Hierarchy {
	
	public static class Link {
		public static Map<String,List<Root>> mapItfClient = new HashMap<String,List<Root>>();
		public static Map<String,List<Root>> mapItfServer = new HashMap<String,List<Root>>();
		
		public static List<Link> maplink = new ArrayList<Link>();
		
		protected Root client; public Root getClient(){ return client;}
		protected Root server; public Root getServer(){ return server;}
		protected String link; public String getLink(){ return link;}
		
		public static String DELIMETER = "::";
		
		public Link(Root client,Root server,String link){
			this.client = client;
			this.server = server;
			this.link = link;
			maplink.add(this);
		}
		
		public String toString(){
			return client.getName()+"-- "+link+" --> "+server.getName();
		}
		/**
		 * add the link
		 * @return
		 */
		public static String strLink(){
			StringBuilder strb = new StringBuilder();
			for(Entry<String, List<Root>> entry : mapItfServer.entrySet()){
				String link = entry.getKey();
				List<Root> servers = entry.getValue();

				if(!Link.mapItfClient.containsKey(link)) continue;
				List<Root> clients = Link.mapItfClient.get(link);
				
				for(Root client :  clients){
					for(Root server :  servers){
						strb.append(new Link(client,server,link).toString()+"\n");
					}
				}
				
			}

			return strb.toString();
		}
	}
	
	
	public static class Node {
		List<Node> children = new ArrayList<Node>();
		Root root;
		Node parent = null;

		public int level = 0;
		public int num = 0;

		public static class Root {


			PAComponent component;

			

			

			public Root(Component component2) {
				this.component = (PAComponent) component2;
			}

			public String getName() {
				try {
					return GCM.getNameController(component).getFcName();
				} catch (NoSuchInterfaceException e) {
					e.printStackTrace();
				}
				return "NoName";
			}

			public String toString(){
				return toString(0);
			}
			public String toString(int level){
				StringBuilder strb = new StringBuilder();
				strb.append(getName()+" \n ");

				for(int i=0;i< level;i++) strb.append("\t");
				strb.append("\n Itf Client :");
				for(InterfaceType cl : this.component.getComponentParameters().getClientInterfaceTypes()){
					strb.append(cl.getFcItfName()+" : "+cl.getFcItfSignature()+" ; ");
					List<Root> li = (Link.mapItfClient.containsKey(cl.getFcItfName())) ?
							Link.mapItfClient.get(cl.getFcItfName()) : 
								new ArrayList<Root>();
					li.add(this);
							
					Link.mapItfClient.put(cl.getFcItfName(), li);
				}

				strb.append("\n Itf Server :");
				for(InterfaceType cl : this.component.getComponentParameters().getServerInterfaceTypes()){
					strb.append(cl.getFcItfName()+" : "+cl.getFcItfSignature()+" ; ");
					List<Root> li = (Link.mapItfServer.containsKey(cl.getFcItfName())) ?
							Link.mapItfServer.get(cl.getFcItfName()) : 
								new ArrayList<Root>();
					li.add(this);
					Link.mapItfServer.put(cl.getFcItfName(), li);
				}

				return strb.toString();
			}
		}

		public Node(Component component){
			newInstance(component);
		}

		public Node(Component component, int level, int ind, Node parent){
			this.level = level + 1;
			this.num = ind;
			this.parent  = parent;
			newInstance(component);
		}

		private void newInstance(Component component){
			root = new Root(component);
			hierachy(root.component);
		}


		public List<Node> hierachy(Component component){
			if(Constants.COMPOSITE.equals(((PAComponent) component).getComponentParameters().getHierarchicalType())) {

				PAContentController contentController;
				try {
					contentController = Utils.getPAContentController(component);

					int ind = 0;
					for ( Component subComponent : contentController.getFcSubComponents()) {
						children.add(new Node(subComponent,level,ind++,this ));
					}
				} catch (NoSuchInterfaceException e) {
					e.printStackTrace();
				}

			}
			return children;
		}

		public String toString(){
			StringBuilder strb = new StringBuilder();
			strb.append(level+" ");
			for(int i=0;i< level;i++) strb.append("\t");
			strb.append(" | ");
			strb.append(root.toString(level)+"\n");


			for(Node n : children){
				strb.append(n.toString());
			}

			return strb.toString();
		}

		
		// GETTER
		
		public Root getRoot() {
			return root;
		}

		public List<Node> getChildren() {
			return children;
		}

		public boolean hasChildren() {
			return !children.isEmpty();
		}
		
		public boolean hasNodeParent() {
			return parent != null;
		}

		public Node getParent() {
			return parent;
		}
	}

}
