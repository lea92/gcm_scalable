package fr.scale.gcm_scalable.visualisation.factory;

import org.etsi.uri.gcm.util.GCM;
import org.objectweb.fractal.api.Component;
import org.objectweb.fractal.api.factory.InstantiationException;
import org.objectweb.fractal.api.type.InterfaceType;
import org.objectweb.proactive.core.component.Constants;
import org.objectweb.proactive.core.component.ContentDescription;
import org.objectweb.proactive.core.component.ControllerDescription;
import org.objectweb.proactive.core.component.Utils;
import org.objectweb.proactive.core.component.control.PAContentController;
import org.objectweb.proactive.core.component.factory.PAGenericFactory;
import org.objectweb.proactive.core.component.identity.PAComponent;
import org.objectweb.proactive.core.component.type.PAGCMInterfaceType;
import org.objectweb.proactive.core.component.type.PAGCMTypeFactory;
import org.objectweb.proactive.core.node.Node;
import org.objectweb.proactive.extensions.autonomic.controllers.monitoring.MonitorController;
import org.objectweb.proactive.extensions.autonomic.controllers.remmos.Remmos;

import fr.scale.gcm_scalable.controller.components.mape.WrapScaleMetric;
import fr.scale.gcm_scalable.controller.factory.AScalabilityFactory;

public class AVisuFactory {
	
	
	private static Remmos remmos;

	public static void checkRemmos(PAGCMTypeFactory tf, PAGenericFactory cf) throws InstantiationException {
		if (remmos == null) {
			remmos = new Remmos(tf, cf);
		}
	}

	
	static final String controllerDescriptionName = "VisualisationComposant";
	
	/** Service composite **/
	public static Component createComponentVisu(Node node, PAGCMTypeFactory tf, PAGenericFactory cf,PAComponent componentwrapper) throws Exception {

		checkRemmos(tf, cf);

		String componentName = GCM.getNameController(componentwrapper).getFcName();
		InterfaceType[] inter = componentwrapper.getComponentParameters().getInterfaceTypes();

		PAGCMInterfaceType[] fTypes = {};//new PAGCMInterfaceType[inter.length];
		/*for(int i = 0; i< inter.length;i++){
			fTypes[i] = (PAGCMInterfaceType)  (inter[i]);
		}*/
		
		Object o [] = {componentwrapper};
		
		
		Component comp = remmos.newFcInstance(
				remmos.createFcType(fTypes, Constants.PRIMITIVE),
				new ControllerDescription(controllerDescriptionName, Constants.PRIMITIVE),
				new ContentDescription(PACompVizuImpl.class.getName(),o), node);

		Utils.getPAMembraneController(comp).startMembrane();
		Remmos.addMonitoring(comp);
		Remmos.addAnalysis(comp);
		Remmos.addPlannerController(comp);
		Remmos.addExecutorController(comp);
		
		
		
		return comp;
	}
}
