/*
 * ################################################################
 *
 * ProActive Parallel Suite(TM): The Java(TM) library for
 *    Parallel, Distributed, Multi-Core Computing for
 *    Enterprise Grids & Clouds
 *
 * Copyright (C) 1997-2012 INRIA/University of
 *                 Nice-Sophia Antipolis/ActiveEon
 * Contact: proactive@ow2.org or contact@activeeon.com
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Affero General Public License
 * as published by the Free Software Foundation; version 3 of
 * the License.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
 * USA
 *
 * If needed, contact us to obtain a release under GPL Version 2 or 3
 * or a different license than the AGPL.
 *
 *  Initial developer(s):               The ProActive Team
 *                        http://proactive.inria.fr/team_members.htm
 *  Contributor(s):
 *
 * ################################################################
 * $$PROACTIVE_INITIAL_DEV$$
 */
package fr.scale.gcm_scalable.visualisation;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import javax.swing.JOptionPane;

import org.etsi.uri.gcm.util.GCM;
import org.inria.scale.streams.runners.BaseApplicationRunner;
import org.objectweb.fractal.api.Component;
import org.objectweb.proactive.api.PADeployment;
import org.objectweb.proactive.core.component.Utils;
import org.objectweb.proactive.core.component.control.PABindingController;
import org.objectweb.proactive.core.component.control.PAContentController;
import org.objectweb.proactive.core.component.factory.PAGenericFactory;
import org.objectweb.proactive.core.component.identity.PAComponent;
import org.objectweb.proactive.core.component.type.PAGCMInterfaceType;
import org.objectweb.proactive.core.component.type.PAGCMTypeFactory;
import org.objectweb.proactive.core.descriptor.data.ProActiveDescriptor;
import org.objectweb.proactive.core.node.Node;
import org.objectweb.proactive.extensions.autonomic.adl.AFactory;
import org.objectweb.proactive.extensions.autonomic.adl.AFactoryFactory;
import org.objectweb.proactive.extensions.autonomic.controllers.execution.ExecutorController;
import org.objectweb.proactive.extensions.autonomic.controllers.monitoring.MonitorController;
import org.objectweb.proactive.extensions.autonomic.controllers.remmos.Remmos;

import fr.scale.gcm_scalable.commun.CST;
import fr.scale.gcm_scalable.controller.factory.AScalabilityFactory;
import fr.scale.gcm_scalable.visualisation.factory.AVisuFactory;


/**
 * @author The ProActive Team
 */
public class Main {

	public static void run(final String... args)  {
		try{
			Component compositeWrapper;
			// verify parameter length
			/*if (args.length < 1) {
			System.err.println("At least an ADL package and filename should be passed as a parameter");
			System.err.println("Optionally a deployment file can be passed too");
			System.exit(1);
		}
			 */
			// handle arguments
			String adl = "fr.scale.gcm_scalable.adl.Composite";//args[0];
			adl = "fr.scale.gcm.scalable.simple.masterslave.adl.Composite";
			
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
			final PAContentController contentController = Utils.getPAContentController(compositeWrapper);

			Remmos.enableMonitoring(compositeWrapper);
			final MonitorController monitor = Remmos.getMonitorController(compositeWrapper);
			
			Component boot = Utils.getBootstrapComponent();
			PAGCMTypeFactory patf = Utils.getPAGCMTypeFactory(boot);
			PAGenericFactory pagf = Utils.getPAGenericFactory(boot);
			Node no = null;
			Component manager = AScalabilityFactory.createManager(no, patf, pagf);

			contentController.addFcSubComponent(manager);
			final PABindingController bcmanager = Utils.getPABindingController(manager);

			
			for ( Component subComponent : contentController.getFcSubComponents()) {
				String componentName = GCM.getNameController(subComponent).getFcName();
				if((!componentName.equals("Slave") //&& !componentName.equals("Slave2")
						)){ 
					System.out.println(":: "+componentName); continue;
				}

				contentController.removeFcSubComponent(subComponent);

				//final PAContentController subcontentController = Utils.getPAContentController(subComponent);

				final Component wrap = 
						AScalabilityFactory.createWrap(null, patf, pagf,(PAComponent) subComponent);
				contentController.addFcSubComponent(wrap);
				
				bcmanager.bindFc(CST.ScalabilityControlMulticast_ITF, wrap.getFcInterface(CST.ScalabilityControlMulticast_ITF));

				//IScalabilityControl itf =  (IScalabilityControl) wrap.getFcInterface(CST.ScalabilityControl_ITF);
				//itf.increase();
				//AScalabilityFactory.bindScalability(manager,wrap);
				
				
				//Add a metric to component
				
				AScalabilityFactory.addMetric(compositeWrapper, wrap);
				final MonitorController monitor2 = Remmos.getMonitorController(wrap);

				HashSet<String> hl = monitor2.getMetricList().getValue();
				for(String val : hl){
					System.out.println(">> "+val);
				}
				
				break;
			}
			
		
			System.out.println("hello");
			// METRICS
			//monitor.addMetric(Cst.SIZEFILE, new SizeFileMetric());
			//monitor.enableMetric(Cst.SIZEFILE);


//start
			Component compVizu = AVisuFactory.createComponentVisu(null, patf, pagf,(PAComponent) compositeWrapper);
			contentController.addFcSubComponent(compVizu);
			
			Utils.getPAGCMLifeCycleController(compositeWrapper).startFc();

			monitor.startGCMMonitoring();
			Thread.sleep(100);
			
			System.out.println("hello");

			/*
			// RULE
			 */
			Remmos.getAnalyzerController(compositeWrapper).addRule("always", new SizeFileRule());
			// PLAN*/

			Remmos.getPlannerController(compositeWrapper).setPlan(new SizeFilePlan());

			// EXECUTOR
			final ExecutorController executor = Remmos.getExecutorController(compositeWrapper);




			Runner2 ru = (Runner2) compositeWrapper.getFcInterface("runner");
			ru.run();
			//IScalabilityControl itf =  (IScalabilityControl) manager.getFcInterface(CST.ScalabilityControlMulticast_ITF);
			//itf.increase();
			 

			
			
		}catch(Exception e){
			e.printStackTrace();
		}

	}



	public static void main(final String... args) {
		run(args);

	}

	private static void echo(String str){
		System.out.println(str);
	}
}
