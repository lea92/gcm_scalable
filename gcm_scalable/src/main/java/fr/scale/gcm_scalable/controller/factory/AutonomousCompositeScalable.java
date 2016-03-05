package fr.scale.gcm_scalable.controller.factory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.etsi.uri.gcm.api.type.GCMInterfaceType;
import org.etsi.uri.gcm.util.GCM;
import org.objectweb.fractal.api.Component;
import org.objectweb.fractal.api.Interface;
import org.objectweb.fractal.api.NoSuchInterfaceException;
import org.objectweb.fractal.api.control.IllegalBindingException;
import org.objectweb.fractal.api.control.IllegalContentException;
import org.objectweb.fractal.api.control.IllegalLifeCycleException;
import org.objectweb.fractal.api.factory.InstantiationException;
import org.objectweb.fractal.api.type.InterfaceType;
import org.objectweb.proactive.core.component.Binding;
import org.objectweb.proactive.core.component.Constants;
import org.objectweb.proactive.core.component.ContentDescription;
import org.objectweb.proactive.core.component.ControllerDescription;
import org.objectweb.proactive.core.component.Utils;
import org.objectweb.proactive.core.component.control.PABindingController;
import org.objectweb.proactive.core.component.control.PAContentController;
import org.objectweb.proactive.core.component.control.PAMulticastController;
import org.objectweb.proactive.core.component.factory.PAGenericFactory;
import org.objectweb.proactive.core.component.identity.PAComponent;
import org.objectweb.proactive.core.component.type.PAGCMInterfaceType;
import org.objectweb.proactive.core.component.type.PAGCMTypeFactory;
import org.objectweb.proactive.core.node.Node;
import org.objectweb.proactive.extensions.autonomic.controllers.monitoring.MonitorController;
import org.objectweb.proactive.extensions.autonomic.controllers.remmos.Remmos;

import fr.scale.gcm.scalable.simple.masterslave.Itf1Multicast;
import fr.scale.gcm.scalable.simple.masterslave.MasterImpl;
import fr.scale.gcm_scalable.commun.CST;
import fr.scale.gcm_scalable.controller.components.ManagerImpl;
import fr.scale.gcm_scalable.controller.components.wrapone.WrapComponent;
import fr.scale.gcm_scalable.controller.interfaces.IScalabilityControl;
import fr.scale.gcm_scalable.controller.interfaces.IScalabilityControlMulticast;
import fr.scale.gcm_scalable.controller.interfaces.IService;

/**
 * it transform a sime application in a application scalable
 * @author lelbeze
 *
 */
public class AutonomousCompositeScalable {

	protected PAComponent compositeContainer;

	protected PAComponent compositeContaining;

	protected PAComponent manager;

	protected  PAGCMTypeFactory patf;

	protected  PAGenericFactory pagf;

	protected Node node = null;

	public static Remmos remmos;

	protected Map<String,Component> map_wrapper = new HashMap<String,Component>();
	protected Map<String,Component> map_containing = new HashMap<String,Component>();

	public static void checkRemmos(PAGCMTypeFactory tf, PAGenericFactory cf) throws InstantiationException {
		if (remmos == null) {
			remmos = new Remmos(tf, cf);
		}
	}

	public AutonomousCompositeScalable(Component compositeContaining){
		this.compositeContaining = (PAComponent) compositeContaining;
		try{

			Component boot = Utils.getBootstrapComponent();
			patf = Utils.getPAGCMTypeFactory(boot);
			pagf = Utils.getPAGenericFactory(boot);
			checkRemmos(patf,pagf);

			this.createMapContaining();
			this.createManager();
			this.createWrapComposite();

		}catch(Exception e){
			System.err.println("Impossible to create a factory");
			e.printStackTrace();
		}


		//first createManager;
	}

	/**
	 * create the map of component of level 1
	 * @throws NoSuchInterfaceException
	 */
	private void createMapContaining() throws NoSuchInterfaceException {
		final PAContentController	contentController = Utils.getPAContentController(compositeContaining);
		Component[] subComponents = contentController.getFcSubComponents();

		for ( Component subComponent : subComponents ) {
			String componentName = GCM.getNameController(subComponent).getFcName();
			map_containing.put(componentName, subComponent);
		}
	}

	/**
	 * creation of manager
	 */
	protected void createManager(){
		try {
			PAGCMInterfaceType[] fTypes = new PAGCMInterfaceType[] {
					(PAGCMInterfaceType) patf.createGCMItfType(CST.MANAGER_SERVICE, IService.class.getName(), PAGCMTypeFactory.SERVER, PAGCMTypeFactory.OPTIONAL, PAGCMTypeFactory.SINGLETON_CARDINALITY),

					createMulticastItf(patf,PAGCMTypeFactory.CLIENT)
			};

			manager = (PAComponent) remmos.newFcInstance(
					remmos.createFcType(fTypes, Constants.PRIMITIVE),
					new ControllerDescription(CST.MANAGER_COMP_NAME, Constants.PRIMITIVE),
					new ContentDescription(ManagerImpl.class.getName()),
					node);

			addMAPE(manager);

		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	
	

	/**
	 * creation of manager
	 * @throws NoSuchInterfaceException 
	 */
	protected PAComponent createCopyWithoutBinding(PAComponent comp) throws NoSuchInterfaceException{
		PAComponent res = null;
		Object[] itfs = comp.getFcInterfaces();
		PAGCMInterfaceType[] fTypes = new PAGCMInterfaceType[itfs.length] ; 
		for(int i=0;i<itfs.length;i++){
			fTypes[i] = (PAGCMInterfaceType) itfs[i];
		}
		
		String name = GCM.getNameController(comp).getFcName();
		//ControllerDescription ctrldesc = Utils.getPAContentController(comp).;
	
		
		try {

			res = (PAComponent) remmos.newFcInstance(
					remmos.createFcType(fTypes, Constants.PRIMITIVE),
					new ControllerDescription(name, Constants.PRIMITIVE),
					new ContentDescription(ManagerImpl.class.getName()),
					node);

			addMAPE(res);

		}catch(Exception e){
			e.printStackTrace();
		}
		return res;
	}
	
	
	

	/**
	 * Add the mape to one component
	 * @param comp
	 */
	public static void addMAPE(PAComponent comp){
		try {
			Utils.getPAMembraneController(comp).startMembrane();
			Remmos.addMonitoring(comp);
			Remmos.addAnalysis(comp);
			Remmos.addPlannerController(comp);
			Remmos.addExecutorController(comp);
		} catch (IllegalLifeCycleException e) {
			e.printStackTrace();
		} catch (NoSuchInterfaceException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	/**
	 * create the container of job
	 * @throws Exception
	 */
	public  void createWrapComposite() throws Exception {

		InterfaceType[] inter = ((PAComponent) compositeContaining).getComponentParameters().getServerInterfaceTypes();

		InterfaceType[] intermanager = manager.getComponentParameters().getServerInterfaceTypes();


		/**
		 * add interface to allow increasing of component. the name of this interface is 
		 * CST.ADDSCALE_ITF and the interface is 
		 */
		PAGCMInterfaceType[] fTypes = new PAGCMInterfaceType[inter.length +intermanager.length];
		for(int i = 0; i< inter.length;i++){
			fTypes[i] = (PAGCMInterfaceType)  (inter[i]);
		}


		for(int i = 0; i< intermanager.length;i++){
			fTypes[i+inter.length] = (PAGCMInterfaceType)  (intermanager[i]);
		}


		compositeContainer = (PAComponent) remmos.newFcInstance(
				remmos.createFcType(fTypes, Constants.COMPOSITE),
				new ControllerDescription(CST.WRAPCOMPOSITE, Constants.COMPOSITE),
				null, node);

		final PAContentController cc = Utils.getPAContentController(compositeContainer);
		cc.addFcSubComponent(compositeContaining);
		//cc.addFcSubComponent(manager);

		final PABindingController bc = Utils.getPABindingController(compositeContainer);
		for(int i = 0; i< inter.length;i++){
			PAGCMInterfaceType pa = (PAGCMInterfaceType)  (inter[i]);
			bc.bindFc(pa.getFcItfName(),
					compositeContaining.getFcInterface(pa.getFcItfName()));
		}

		cc.addFcSubComponent(manager);

		for(int i = 0; i< intermanager.length;i++){
			PAGCMInterfaceType pa = (PAGCMInterfaceType)  (intermanager[i]);
			bc.bindFc(pa.getFcItfName(),
					manager.getFcInterface(pa.getFcItfName()));
		}

		addMAPE(compositeContainer);
	}


	/**
	 * add for one component the capacity to scale
	 * @param componentname
	 * @throws Exception
	 */
	public void addPowerScalability(String componentname) throws Exception{

		final PAContentController	contentController = Utils.getPAContentController(compositeContaining);

		Component[] subComponents = contentController.getFcSubComponents();
		// wrap component scalable
		final PABindingController bcmanager = Utils.getPABindingController(manager);

		if(map_containing.containsKey(componentname)) {
			PAComponent subComponent = (PAComponent) map_containing.get(componentname); 
			//remove a component and his binding
			//suppression component from basis composite
			contentController.removeFcSubComponent(subComponent);
			PAComponent wrap = WrapComponent.createWrap(null, patf, pagf,(PAComponent) subComponent);

			Object o = subComponent.getFcInterface("itf1");
			Component[] allsubcomps = Utils.getPAContentController(this.getCompositeContaining()).getFcSubComponents();

			
			for(Component comp : allsubcomps){
				
				String name = GCM.getNameController(comp).getFcName();
				System.err.println(name);
				
				try{
					PABindingController pac = Utils.getPABindingController(comp);
					if(pac.isBoundTo(subComponent)){
						if(comp.getFcInterface("itf1")!= null){
							//pac.unbindFc("itf1");
							//pac.bindFc("itf1", serverItf);
						}
					}
				}catch(NoSuchInterfaceException e){

				}
			}/**/
			//updateBindingCompClientsToBindToWrap(subComponent,wrap);
			map_wrapper.put(GCM.getNameController(wrap).getFcName(), wrap);
			contentController.addFcSubComponent(wrap);
			bcmanager.bindFc(CST.ScalabilityControlMulticast_ITF, wrap.getFcInterface(CST.ScalabilityControlMulticast_ITF));
		}else {
			throw new Exception("Error, no component with name "+componentname+"...");
		}
	}


	public Map<PAComponent,List<Interface>> updateBindingCompClientsToBindToWrap(
			PAComponent subComponent, PAComponent wrap) throws NoSuchInterfaceException{
		Map<PAComponent,List<Interface>> res = new HashMap<PAComponent,List<Interface>>();
		Component[] allsubcomps = Utils.getPAContentController(this.getCompositeContaining()).getFcSubComponents();
		Interface[] serversitfs = filterServerItfs(subComponent.getFcInterfaces());
		//final PABindingController pacSubComponent = Utils.getPABindingController(subComponent);
		PABindingController pacwrap = Utils.getPABindingController(wrap);


		for(Component comp : allsubcomps){
			try{
				PABindingController pac = Utils.getPABindingController(comp);
				if(pac.isBoundTo(subComponent)){
					ArrayList<Interface> newListItfs = new ArrayList<Interface>();
					for (int i = 0; i < serversitfs.length; i++) {

						String nameitf = serversitfs[i].getFcItfName();
						if(!nameitf.endsWith("controller")){
							try{
								if(pac.lookupFc(serversitfs[i].getFcItfName())!=null){
									//on suprime
									/*GCM.getGCMLifeCycleController(comp).
								GCM.getGCMLifeCycleController(comp).stopFc();

								pac.unbindFc(serversitfs[i].getFcItfName());
								newListItfs.add(serversitfs[i]);
								pacwrap.bindFc(nameitf, serversitfs[i]);*/
								}


							}catch(NoSuchInterfaceException e){
								//ce n'etait pas la bonne interface client
								//on laisse couler
								/*	} catch (IllegalBindingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IllegalLifeCycleException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();*/
							}
						}

					}

					res.put((PAComponent) comp,newListItfs);
				}
			}catch(NoSuchInterfaceException e){
				//no binding-controller on laisse couler
			}
		}
		return res;
	}

	private static Interface[] filterServerItfs(Object[] itfs) {
		ArrayList<Interface> newListItfs = new ArrayList<Interface>();
		for (int i = 0; i < itfs.length; i++) {
			Interface curItf = (Interface) itfs[i];
			if (!((GCMInterfaceType) curItf.getFcItfType()).isFcClientItf()){
				newListItfs.add(curItf);
			}
		}
		return newListItfs.toArray(new Interface[] {});
	}

	/*
	public static Boolean isBoundTo(PAComponent compclient, PAComponent compserver) {
		PABindingController pac = Utils.getPABindingController(compclient);

        Object[] serverItfsComponent = filterServerItfs(compserver.getFcInterfaces());
        Object[] itfs = compclient.getFcInterfaces();
        for (int i = 0; i < itfs.length; i++) {
            Interface curItf = (Interface) itfs[i];
            if (!((GCMInterfaceType) curItf.getFcItfType()).isGCMMulticastItf()) {
                for (int j = 0; j < serverItfsComponent.length; j++) {
                    Interface curServerItf = (Interface) serverItfsComponent[j];
                    Binding binding = (Binding) getBinding(curItf.getFcItfName());
                    if ((binding != null) &&
                        binding.getServerInterface().getFcItfOwner().equals(curServerItf.getFcItfOwner()) &&
                        binding.getServerInterface().getFcItfType().equals(curServerItf.getFcItfType())) {
                        return Boolean.valueOf(true);
                    }
                }
            } else {
                try {
                    PAMulticastController mc = Utils.getPAMulticastController(getFcItfOwner());
                    if (mc.isBoundTo(curItf.getFcItfName(), serverItfsComponent))
                        return Boolean.valueOf(true);
                } catch (NoSuchInterfaceException e) {
                    // TODO: handle exception
                }
            }
        }
        return Boolean.valueOf(false);
    }
	 */

	public static PAGCMInterfaceType createMulticastItf(PAGCMTypeFactory tf, boolean isclient) throws InstantiationException{
		boolean isOptional = true;//!(isclient);
		String cardinality = !(isclient)? PAGCMTypeFactory.SINGLETON_CARDINALITY : PAGCMTypeFactory.MULTICAST_CARDINALITY;
		String theclass = !(isclient)?  IScalabilityControl.class.getName() : IScalabilityControlMulticast.class.getName() ;
		return (PAGCMInterfaceType) tf.createGCMItfType(CST.ScalabilityControlMulticast_ITF, theclass, 
				isclient, isOptional, cardinality);
	}



	// GETTER AND SETTER

	public PAComponent getWrapComposite() {
		return compositeContainer;
	}

	public PAComponent getManager() {
		return manager;
	}

	public PAComponent getCompositeContaining() {
		return compositeContaining;
	}

	public PAGCMTypeFactory getPatf() {
		return patf;
	}

	public PAGenericFactory getPagf() {
		return pagf;
	}

	public Map<String, Component> getMapWrap(){
		return map_wrapper;
	}

}
