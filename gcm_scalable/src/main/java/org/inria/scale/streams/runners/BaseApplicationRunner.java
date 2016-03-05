package org.inria.scale.streams.runners;

import java.util.HashMap;
import java.util.Map;

import org.objectweb.fractal.api.Component;
import org.objectweb.proactive.api.PADeployment;
import org.objectweb.proactive.core.component.Utils;
import org.objectweb.proactive.core.component.control.PAContentController;
import org.objectweb.proactive.core.component.identity.PAComponent;
import org.objectweb.proactive.core.descriptor.data.ProActiveDescriptor;
import org.objectweb.proactive.extensions.autonomic.adl.AFactory;
import org.objectweb.proactive.extensions.autonomic.adl.AFactoryFactory;
import org.objectweb.proactive.extensions.autonomic.controllers.execution.ExecutorController;
import org.objectweb.proactive.extensions.autonomic.controllers.monitoring.MonitorController;
import org.objectweb.proactive.extensions.autonomic.controllers.remmos.Remmos;

/**
 * Allows users to execute their applications in a simple way, passing as an
 * argument the ADL and, optionally, the deployment configuration of the system.
 *
 * @author moliva
 *
 */
public class BaseApplicationRunner {

	protected Component compositeWrapper;
	
	
	/**
	 * <p>
	 * Main method for a simple application based on ADL.
	 * </p>
	 * <p>
	 * <b>Important remark:</b> Do not forget to pass the following parameters
	 * when executing the application:<br />
	 * <code>-Djava.security.manager -Djava.security.policy=src/test/resources/allPerm.policy  -Dgcm.provider=org.objectweb.proactive.core.component.Fractive</code>
	 * </p>
	 *
	 * @param args
	 *          The parameters accepted currently are an ADL file
	 *          <b>(required)</b> and secondly, an XML deployment file as optional
	 * @throws Exception
	 */
	public  Component main(final String... args) throws Exception {
		// verify parameter length
		if (args.length < 1) {
			System.err.println("At least an ADL package and filename should be passed as a parameter");
			System.err.println("Optionally a deployment file can be passed too");
			System.exit(1);
		}

		// handle arguments
		 String adl = args[0];
		//adl = "/"+adl.replace(".", "/");
		//adl = BaseApplicationRunner.class.getResource(adl).getPath().toString();
		final String deploymentFilename = args.length > 1 ? args[1] : null;

		// get the component Factory allowing component creation from ADL
		final AFactory factory = (AFactory) AFactoryFactory.getAFactory();
		final Map<String, Object> context = new HashMap<String, Object>();

		// retrieve the deployment descriptor
		if (deploymentFilename != null) {
			final String deploymentFilePath = BaseApplicationRunner.class.getClassLoader().getResource(deploymentFilename)
					.getPath();
			final ProActiveDescriptor deploymentDescriptor = PADeployment.getProactiveDescriptor(deploymentFilePath);
			context.put("deployment-descriptor", deploymentDescriptor);
			deploymentDescriptor.activateMappings();
		}

		compositeWrapper = (Component) factory.newAutonomicComponent(adl, context);

		Remmos.enableMonitoring(compositeWrapper);
		final MonitorController monitor = Remmos.getMonitorController(compositeWrapper);
		monitor.startGCMMonitoring();

		// METRICS
		

		/*
		final PAContentController contentController = Utils.getPAContentController(compositeWrapper);
		for (final Component subComponent : contentController.getFcSubComponents()) {
			Remmos.getMonitorController(subComponent).setRecordStoreCapacity(16);
		}

		// RULE
		
		// PLAN
		
		// EXECUTOR
		final ExecutorController executor = Remmos.getExecutorController(compositeWrapper);

		Utils.getPAGCMLifeCycleController(compositeWrapper).startFc();

		System.out.println("*\n*\n* App ready: " + ((PAComponent) compositeWrapper).getID().toString() + "\n*\n*");
		*/
		return compositeWrapper;
	}

	//-----------------------------------------------------------------------------------------
	// GETTER AND SETTER
	//-----------------------------------------------------------------------------------------
	

	public Component getComponent(){ return compositeWrapper;}
}
