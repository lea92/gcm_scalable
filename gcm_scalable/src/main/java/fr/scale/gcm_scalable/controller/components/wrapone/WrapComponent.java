package fr.scale.gcm_scalable.controller.components.wrapone;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.JOptionPane;

import org.etsi.uri.gcm.util.GCM;
import org.objectweb.fractal.api.Component;
import org.objectweb.fractal.api.NoSuchInterfaceException;
import org.objectweb.fractal.api.control.IllegalBindingException;
import org.objectweb.fractal.api.control.IllegalContentException;
import org.objectweb.fractal.api.control.IllegalLifeCycleException;
import org.objectweb.fractal.api.factory.InstantiationException;
import org.objectweb.fractal.api.type.ComponentType;
import org.objectweb.fractal.api.type.InterfaceType;
import org.objectweb.fractal.api.type.TypeFactory;
import org.objectweb.proactive.core.component.Constants;
import org.objectweb.proactive.core.component.ContentDescription;
import org.objectweb.proactive.core.component.ControllerDescription;
import org.objectweb.proactive.core.component.Utils;
import org.objectweb.proactive.core.component.control.PABindingController;
import org.objectweb.proactive.core.component.control.PAContentController;
import org.objectweb.proactive.core.component.control.PAGCMLifeCycleController;
import org.objectweb.proactive.core.component.control.PAMembraneController;
import org.objectweb.proactive.core.component.exceptions.NoSuchComponentException;
import org.objectweb.proactive.core.component.factory.PAGenericFactory;
import org.objectweb.proactive.core.component.identity.PAComponent;
import org.objectweb.proactive.core.component.type.PAGCMInterfaceType;
import org.objectweb.proactive.core.component.type.PAGCMTypeFactory;
import org.objectweb.proactive.core.node.Node;
import org.objectweb.proactive.extensions.autonomic.controllers.monitoring.EventControl;
import org.objectweb.proactive.extensions.autonomic.controllers.monitoring.MonitorController;
import org.objectweb.proactive.extensions.autonomic.controllers.remmos.Remmos;

import fr.scale.gcm_scalable.commun.CST;
import fr.scale.gcm_scalable.controller.components.ManagerImpl;
import fr.scale.gcm_scalable.controller.components.WrapScaleImpl;
import fr.scale.gcm_scalable.controller.components.wrapone.nf.CellControllerImpl;
import fr.scale.gcm_scalable.controller.components.wrapone.nf.CellsManagerControllerImpl;
import fr.scale.gcm_scalable.controller.factory.AutonomousCompositeScalable;
import fr.scale.gcm_scalable.controller.interfaces.IService;
import fr.scale.gcm_scalable.controller.interfaces.ItfCellController;
import fr.scale.gcm_scalable.controller.interfaces.ItfCellsManagerController;

public class WrapComponent {


	protected static final String COMPONENT_CONTROLLER_CONFIG = 
			"/org/objectweb/proactive/core/component/componentcontroller/config/default-component-controller-config.xml";

	public static AutonomousCompositeScalable ACS;

	public WrapComponent(){

	}

	/**
	 * creation of the wrap. This wrap allow the first instance to get after more instance of the source
	 * @param node
	 * @param tf
	 * @param cf
	 * @param subcomponent
	 * @return
	 * @throws Exception
	 */
	public static PAComponent createWrap(Node node, PAGCMTypeFactory tf, PAGenericFactory cf,PAComponent subcomponent) throws Exception {

		AutonomousCompositeScalable.checkRemmos(tf, cf);

		String componentName = GCM.getNameController(subcomponent).getFcName();


		/**
		 * add interface to allow increasing of component. the name of this interface is 
		 * CST.ADDSCALE_ITF and the interface is 
		 */
		PAGCMInterfaceType[] fTypes ={
				AutonomousCompositeScalable.createMulticastItf(tf,PAGCMTypeFactory.SERVER)
		};


		String name = CST.WRAPSCALE+componentName;

		PAComponent composite = (PAComponent) AutonomousCompositeScalable.remmos.newFcInstance(
				AutonomousCompositeScalable.remmos.createFcType(fTypes, Constants.COMPOSITE),
				new ControllerDescription(name, Constants.COMPOSITE),
				null, node);


		AutonomousCompositeScalable.addMAPE((PAComponent) composite);

		PAComponent ws_codemetier = (PAComponent) AutonomousCompositeScalable.remmos.newFcInstance(
				AutonomousCompositeScalable.remmos.createFcType(fTypes, Constants.PRIMITIVE),
				new ControllerDescription(name+"_code_metier", Constants.PRIMITIVE),
				new ContentDescription(WrapScaleImpl.class.getName()), node);


		//add non-functionel component
		Component componentCellsManager = createItfCellsManager(tf,cf,node);
		{
			PAMembraneController membranecellManager = Utils.getPAMembraneController(composite);
			membranecellManager.stopMembrane();
			membranecellManager.nfAddFcSubComponent(componentCellsManager);
		}
		AutonomousCompositeScalable.addMAPE((PAComponent) ws_codemetier);
		final PAContentController	cc = Utils.getPAContentController(composite);

		createCell(composite, subcomponent, tf, cf, node);
		//CST.ItfGcmScaleInst
		//
		//cc.addFcSubComponent(subcomponent);
		cc.addFcSubComponent(ws_codemetier);
		final PABindingController	bc = Utils.getPABindingController(composite);
		bc.bindFc(CST.ScalabilityControlMulticast_ITF, ws_codemetier.getFcInterface(CST.ScalabilityControlMulticast_ITF));

		PAMembraneController membranecomposite = Utils.getPAMembraneController(composite);

		PAGCMLifeCycleController lifeCycle = Utils.getPAGCMLifeCycleController(composite);
		States oldStates = stopMembraneAndLifeCycle(membranecomposite,lifeCycle);
		Remmos.enableMonitoring(composite);
		Thread.sleep(1000);
		
		MonitorController monitor        = Remmos.getMonitorController(composite);
		monitor.addMetric(CST.WRAPONE.METRICS.NBREALINST, new MetricNbInstance());
		monitor.addMetric(CST.WRAPONE.METRICS.NBEXCEPTEDINST, new MetricNbInstance());
		monitor.enableMetric(CST.WRAPONE.METRICS.NBREALINST);
		monitor.enableMetric(CST.WRAPONE.METRICS.NBEXCEPTEDINST);

		startMembraneAndLifeCycle(oldStates, membranecomposite, lifeCycle);

		ItfCellsManagerController cm = (ItfCellsManagerController) membranecomposite.nfGetFcSubComponent(CST.WRAPONE.CMP_NF.CELLS_MANAGER_NF).getFcInterface(ItfCellsManagerController.ITF_NAME);
		cm.updateMetrics();
		return composite;
	}


	private static void createCell(Component compWrap, Component subcomponent, PAGCMTypeFactory tf, PAGenericFactory cf, Node node) throws NoSuchInterfaceException, NoSuchComponentException, IllegalLifeCycleException, IllegalContentException, IllegalBindingException {
		{
			
			PAMembraneController membranecellManager = Utils.getPAMembraneController(compWrap);
			Component componentCellsManager = membranecellManager.nfGetFcSubComponent(CST.WRAPONE.CMP_NF.CELLS_MANAGER_NF);
			membranecellManager.stopMembrane();
			//Add a fc sub component ItfGsInst

			Component componentcell = createItfCell(tf,cf,node);
			PAMembraneController membrane = Utils.getPAMembraneController(subcomponent);
			membrane.stopMembrane();
			membrane.nfAddFcSubComponent(componentcell);


			GCM.getBindingController(componentCellsManager).bindFc(ItfCellController.Multicast.ITF_NAME
					,componentcell.getFcInterface(ItfCellController.Singleton.ITF_NAME));

			//Utils.getPABindingController(componentCellsManager).isBoundTo(component)
			membrane.startMembrane();
			membranecellManager.startMembrane();

		}
	}
	
	

	


	public static class States implements Serializable {
		protected static final long serialVersionUID = 1L;
		protected String membrane, lifeCycle;
		States(String membraneState, String lifeCycleState) {
			membrane = membraneState;
			lifeCycle = lifeCycleState;
		}
		String getMembraneState() { return membrane; }
		String getLifeCycleState() { return lifeCycle; }
	}

	/** Stops the Membran and LifeCycle Controllers */
	protected static States stopMembraneAndLifeCycle(PAMembraneController membrane, PAGCMLifeCycleController lifeCycle)
			throws IllegalLifeCycleException, NoSuchInterfaceException {
		// check that membrane is started (needed to check lifeCycle state)
		String membraneState = membrane.getMembraneState();
		if (membraneState.equals(PAMembraneController.MEMBRANE_STOPPED)) {
			membrane.startMembrane(); // 
		}
		// stop lifeCycle
		String lifeCycleState = lifeCycle.getFcState();
		if (lifeCycleState.equals(PAGCMLifeCycleController.STARTED)) {
			lifeCycle.stopFc();
		}
		// stop membrane
		if (membrane.getMembraneState().equals(PAMembraneController.MEMBRANE_STARTED)) {
			membrane.stopMembrane();
		}
		return new States(membraneState, lifeCycleState);
	}

	protected static void startMembraneAndLifeCycle(States oldStates, PAMembraneController membrane,
			PAGCMLifeCycleController lifeCycle) throws IllegalLifeCycleException {
		if(oldStates.getMembraneState().equals(PAMembraneController.MEMBRANE_STARTED)) {
			membrane.startMembrane();
		}
		if(oldStates.getLifeCycleState().equals(PAGCMLifeCycleController.STARTED)) {
			lifeCycle.startFc();
		}
	}

	/**
	 * Creates the NF Record Store component
	 * @param patf
	 * @param pagf
	 * @param bcm 
	 * @param logStoreClass
	 * @return
	 */
	protected static Component createItfCell(PAGCMTypeFactory patf, PAGenericFactory pagf, Node node) {
		try {
			InterfaceType[] recordStoreItfType = new InterfaceType[] {
					patf.createGCMItfType(ItfCellController.Singleton.ITF_NAME, ItfCellController.Singleton.class.getName(), TypeFactory.SERVER, TypeFactory.MANDATORY, PAGCMTypeFactory.SINGLETON_CARDINALITY)
			};
			Component res =  pagf.newNfFcInstance(patf.createFcType(recordStoreItfType), 
					new ControllerDescription(CST.WRAPONE.CMP_NF.CELL_NF, Constants.PRIMITIVE, COMPONENT_CONTROLLER_CONFIG), 
					new ContentDescription(CellControllerImpl.class.getName()), node);
			return res;
		} catch (InstantiationException e) {
			e.printStackTrace();
			return null;
		}
	}


	/**
	 * Creates the NF Record Store component
	 * @param patf
	 * @param pagf
	 * @param logStoreClass
	 * @return
	 */
	protected static Component createItfCellsManager(PAGCMTypeFactory patf, PAGenericFactory pagf, Node node) {
		try {
			InterfaceType[] itfType = new InterfaceType[] {
					patf.createGCMItfType(ItfCellsManagerController.ITF_NAME, ItfCellsManagerController.class.getName(), TypeFactory.SERVER, TypeFactory.OPTIONAL, PAGCMTypeFactory.SINGLETON_CARDINALITY),

					patf.createGCMItfType(ItfCellController.Multicast.ITF_NAME, ItfCellController.Multicast.class.getName(), TypeFactory.CLIENT, TypeFactory.OPTIONAL, PAGCMTypeFactory.MULTICAST_CARDINALITY)
			};

			ComponentType t = patf.createFcType(itfType);

			return pagf.newNfFcInstance(t, 
					new ControllerDescription(CST.WRAPONE.CMP_NF.CELLS_MANAGER_NF, 
							Constants.PRIMITIVE, COMPONENT_CONTROLLER_CONFIG), 
					new ContentDescription(CellsManagerControllerImpl.class.getName()), node);
		} catch (InstantiationException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	
	/**
	 * add Interceptor of date in entry
	 * @param componentComposite
	 * @return
	 */
	protected static Component addInterceptorIn(Component componentComposite){
		return componentComposite;
	}


}
