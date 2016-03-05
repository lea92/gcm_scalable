package fr.scale.gcm_scalable.controller.factory;

import org.etsi.uri.gcm.api.type.GCMInterfaceType;
import org.etsi.uri.gcm.util.GCM;
import org.objectweb.fractal.api.Component;
import org.objectweb.fractal.api.Interface;
import org.objectweb.fractal.api.NoSuchInterfaceException;
import org.objectweb.fractal.api.control.IllegalLifeCycleException;
import org.objectweb.fractal.api.factory.InstantiationException;
import org.objectweb.fractal.api.type.InterfaceType;
import org.objectweb.proactive.core.component.Binding;
import org.objectweb.proactive.core.component.Constants;
import org.objectweb.proactive.core.component.ContentDescription;
import org.objectweb.proactive.core.component.ControllerDescription;
import org.objectweb.proactive.core.component.Utils;
import org.objectweb.proactive.core.component.control.PABindingController;
import org.objectweb.proactive.core.component.control.PABindingControllerImpl;
import org.objectweb.proactive.core.component.control.PAContentController;
import org.objectweb.proactive.core.component.control.PAMulticastController;
import org.objectweb.proactive.core.component.factory.PAGenericFactory;
import org.objectweb.proactive.core.component.identity.PAComponent;
import org.objectweb.proactive.core.component.type.PAGCMInterfaceType;
import org.objectweb.proactive.core.component.type.PAGCMTypeFactory;
import org.objectweb.proactive.core.node.Node;
import org.objectweb.proactive.extensions.autonomic.controllers.monitoring.MonitorController;
import org.objectweb.proactive.extensions.autonomic.controllers.remmos.Remmos;

import examples.services.Service;
import examples.services.autoadaptable.AASCST;
import fr.scale.gcm_scalable.commun.CST;
import fr.scale.gcm_scalable.controller.components.ManagerImpl;
import fr.scale.gcm_scalable.controller.components.mape.WrapScaleMetric;
import fr.scale.gcm_scalable.controller.interfaces.IScalabilityControlMulticast;
import fr.scale.gcm_scalable.controller.interfaces.IService;

public class AScalabilityFactory {



	private static Remmos remmos;

	public static void checkRemmos(PAGCMTypeFactory tf, PAGenericFactory cf) throws InstantiationException {
		if (remmos == null) {
			remmos = new Remmos(tf, cf);
		}
	}
	
	
	

	
	
	
	
	
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

	public  static void addMetric(Component father, Component child){

		String componentName;
		try {
			componentName = GCM.getNameController(child).getFcName();

			final MonitorController monitor = Remmos.getMonitorController(father);
			monitor.startGCMMonitoring();
			Thread.sleep(100);
			monitor.addMetric(CST.WRAPSCALE+componentName, new WrapScaleMetric());
			monitor.enableMetric(CST.WRAPSCALE+componentName);
			
		} catch (NoSuchInterfaceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
