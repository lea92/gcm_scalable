package fr.scale.gcm.scalable.simple.masterslave;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.etsi.uri.gcm.api.control.MulticastController;
import org.etsi.uri.gcm.api.type.GCMTypeFactory;
import org.etsi.uri.gcm.util.GCM;
import org.inria.scale.streams.runners.BaseApplicationRunner;
import org.objectweb.fractal.adl.Factory;
import org.objectweb.fractal.api.Component;
import org.objectweb.fractal.api.Interface;
import org.objectweb.fractal.api.NoSuchInterfaceException;
import org.objectweb.fractal.api.Type;
import org.objectweb.fractal.api.control.BindingController;
import org.objectweb.fractal.api.control.ContentController;
import org.objectweb.fractal.api.control.IllegalBindingException;
import org.objectweb.fractal.api.control.IllegalContentException;
import org.objectweb.fractal.api.control.IllegalLifeCycleException;
import org.objectweb.fractal.api.control.NameController;
import org.objectweb.fractal.api.factory.GenericFactory;
import org.objectweb.fractal.api.factory.InstantiationException;
import org.objectweb.fractal.api.type.ComponentType;
import org.objectweb.fractal.api.type.InterfaceType;
import org.objectweb.fractal.api.type.TypeFactory;
import org.objectweb.fractal.fscript.FScript;
import org.objectweb.fractal.fscript.FScriptEngine;
import org.objectweb.fractal.fscript.ScriptLoader;
import org.objectweb.proactive.api.PADeployment;
import org.objectweb.proactive.core.component.Constants;
import org.objectweb.proactive.core.component.ContentDescription;
import org.objectweb.proactive.core.component.ControllerDescription;
import org.objectweb.proactive.core.component.PAInterface;
import org.objectweb.proactive.core.component.Utils;
import org.objectweb.proactive.core.component.adl.FactoryFactory;
import org.objectweb.proactive.core.component.control.PABindingController;
import org.objectweb.proactive.core.component.control.PAContentController;
import org.objectweb.proactive.core.component.control.PAGCMLifeCycleController;
import org.objectweb.proactive.core.component.control.PAMembraneController;
import org.objectweb.proactive.core.component.control.PAMulticastController;
import org.objectweb.proactive.core.component.control.PASuperController;
import org.objectweb.proactive.core.component.exceptions.NoSuchComponentException;
import org.objectweb.proactive.core.component.factory.PAGenericFactory;
import org.objectweb.proactive.core.component.identity.PAComponent;
import org.objectweb.proactive.core.component.type.PAGCMInterfaceType;
import org.objectweb.proactive.core.component.type.PAGCMTypeFactory;
import org.objectweb.proactive.core.descriptor.data.ProActiveDescriptor;
import org.objectweb.proactive.core.node.Node;
import org.objectweb.proactive.examples.userguide.components.api.interfaces.Itf2;
import org.objectweb.proactive.examples.userguide.components.api.interfaces.Itf2Impl;
import org.objectweb.proactive.extensions.autonomic.adl.AFactory;
import org.objectweb.proactive.extensions.autonomic.adl.AFactoryFactory;
import org.objectweb.proactive.extensions.autonomic.controllers.ACConstants;
import org.objectweb.proactive.extensions.autonomic.controllers.analysis.AnalyzerController;
import org.objectweb.proactive.extensions.autonomic.controllers.execution.ExecutorController;
import org.objectweb.proactive.extensions.autonomic.controllers.execution.ExecutorControllerImpl;
import org.objectweb.proactive.extensions.autonomic.controllers.monitoring.MonitorController;
import org.objectweb.proactive.extensions.autonomic.controllers.monitoring.MonitorControllerMulticast;
import org.objectweb.proactive.extensions.autonomic.controllers.planning.PlannerController;
import org.objectweb.proactive.extensions.autonomic.controllers.remmos.Remmos;
import org.objectweb.proactive.extensions.autonomic.controllers.remmos.Remmos.States;
import org.objectweb.proactive.extra.component.fscript.GCMScript;
import org.objectweb.proactive.extra.component.fscript.model.GCMNodeFactory;

import examples.services.Service;
import examples.services.performance.PSCST;
import fr.scale.gcm_scalable.a.learn.prog.commun.MainCommun;
import fr.scale.gcm_scalable.commun.CST;
import fr.scale.gcm_scalable.controller.factory.AutonomousCompositeScalable;
import fr.scale.gcm_scalable.controller.interfaces.IService;
import fr.scale.gcm_scalable.visualisation.factory.AVisuFactory;


public class Main extends MainCommun{

	/**
	 * la formule magique pour arreteret demarrer un composite
	 */
	public static void stop_startCompositeAutonimic(PAComponent owner){

		try {
			PAGCMLifeCycleController lcc = Utils.getPAGCMLifeCycleController(owner);
			PAMembraneController mc = Utils.getPAMembraneController(owner);
			States oldStates = Remmos.stopMembraneAndLifeCycle(mc, lcc);

			//TODO what do you want

			//Remmos.startMembraneAndLifeCycle(oldStates, mc, lcc);
		} catch (NoSuchInterfaceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalLifeCycleException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void run1(String[] args)  {
		try{

			final String adl = "fr.scale.gcm.scalable.simple.masterslave.adl.Composite";
			final String deploymentFilename = args.length > 1 ? args[1] : null;

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

			Component componentADL = (Component) factory.newAutonomicComponent(adl, context);


			Thread.sleep(1000);

			//ON PERMET MONITORING ET ON DEMARRE
			//Remmos.enableMonitoring(componentADL); // [!] too much important, maybe merge with MonitoController.startMontioring?


			//final MonitorController monitor = Remmos.getMonitorController(componentADL);
			//monitor.startGCMMonitoring();
			//Thread.sleep(2000);

			PAMembraneController mc = Utils.getPAMembraneController(componentADL);


			System.out.println("[LEA] >> etat de membrane "+mc.getMembraneState());


			Utils.getPAGCMLifeCycleController(componentADL).startFc();
			Component[] subs = mc.nfGetFcSubComponents();


			System.out.println("[LEA] >>1er demande de calcul");
			{
				ItfRunner i1 = (ItfRunner) componentADL.getFcInterface("runner");

				// TODO: Call the compute method
				List<String> li = Arrays.asList("Michaela","Yael","Deborah","Nathanael","Eden","Lea","Yonathan");
				//itf1.compute(li);
				i1.run(li);

			}

			for(Component ca : subs){

				String name = GCM.getNameController(ca).getFcName();

				System.out.println("[LEA] >> Membrane "+name+" "+Utils.getPAGCMLifeCycleController(ca).getFcState());
			}



			Thread.sleep(1000);
			try {
				Remmos.checkFactories();
				PAGCMLifeCycleController lcc = Utils.getPAGCMLifeCycleController(componentADL);
				mc = Utils.getPAMembraneController(componentADL);

				System.out.println("[LEA] >>Stop le composite");
				//States oldStates = Remmos.stopMembraneAndLifeCycle(mc, lcc);
				//mc.startMembrane();
				//mc.stopMembrane();
				lcc.stopFc();
				System.out.println("[LEA] >> etat de lcc "+lcc.getFcState());
				PAContentController pac = Utils.getPAContentController(componentADL);
				for(Component ca : pac.getFcSubComponents()){
					String name = GCM.getNameController(ca).getFcName();

					System.out.println("[LEA] >> "+name+" "+Utils.getPAGCMLifeCycleController(ca).getFcState());
				}


				for(Component ca : subs){

					String name = GCM.getNameController(ca).getFcName();

					System.out.println("[LEA] >> membranne "+name+" "+Utils.getPAGCMLifeCycleController(ca).getFcState());
				}



				/*if (lcc.getFcState().equals(PAGCMLifeCycleController.STARTED)) {

					System.out.println("[LEA] >>Stop le composite lcc");
					lcc.stopFc();
				}
				 */

				Thread.sleep(1000);

				//mc.startMembrane();
				System.out.println("[LEA] >>Start le composite");
				lcc.startFc();

				System.out.println("[LEA] >> etat de lcc "+lcc.getFcState());
				for(Component ca : pac.getFcSubComponents()){
					String name = GCM.getNameController(ca).getFcName();

					System.out.println("[LEA] >> "+name+" "+Utils.getPAGCMLifeCycleController(ca).getFcState());
				}

				//Remmos.startMembraneAndLifeCycle(oldStates, mc, lcc);
			} catch (Exception e) {
				e.printStackTrace();
			}
			Thread.sleep(1000);


			System.out.println("[LEA] >>2eme demande de calcul");
			{
				ItfRunner i1 = (ItfRunner) componentADL.getFcInterface("runner");
				List<String> li = Arrays.asList("Toto");
				i1.run(li);
			}
			Thread.sleep(1000);

			/*System.out.println("[LEA] >>Stop le composite");
			Utils.getPAGCMLifeCycleController(componentADL).stopFc();
			Thread.sleep(1000);*/

			//GCM.getGCMLifeCycleController(componentADL).stopFc();
			//GCM.getBindingController(componentADL).unbindFc("runner");

			Component slave = null;
			Component master = null;

			//Thread.sleep(1000);
			for(Component ca : Utils.getPAContentController(componentADL).getFcSubComponents()){
				String name = GCM.getNameController(ca).getFcName();
				if(name.equals("Slave")){
					slave = ca;
				}
				if(name.equals("Master")){
					master = ca;
				}

			};

			System.out.println("service is binded to ?"+ Utils.getPABindingController(componentADL).isBoundTo(master));


			/*
			for(Object o : master.getFcInterfaces()){
				Interface curItf = (Interface) o;
				System.err.println(curItf.getFcItfName());
			}*/
			//GCM.getGCMLifeCycleController(master).stopFc();
			//Utils.getPAGCMLifeCycleController(master).stopFc();
			Thread.sleep(1000);
			//Remmos.unbind("itf1");
			String[] fc = GCM.getBindingController(master).listFc();
			for(String st : fc){
				System.out.println(st);
			}
			//Utils.getPAMulticastController(master).bindGCMMulticast("itf1", slave.getFcInterface("itf1"));

			/*
			System.out.println(">>>> UNBIND AND REBIND");
			Utils.getPAGCMLifeCycleController(master).stopFc();
			Utils.getPAMulticastController(master).unbindGCMMulticast("itf1", "itf1");		
			Utils.getPABindingController(master).bindFc("itf1",slave.getFcInterface("itf1"));
			Utils.getPAGCMLifeCycleController(master).startFc();
			 */
			System.out.println("master is it binded to slave ?"+ Utils.getPABindingController(master).isBoundTo(slave));

			Thread.sleep(1000);
			test(componentADL,"test unbind and rebind");
			System.exit(-1);

			AutonomousCompositeScalable autonomous = new AutonomousCompositeScalable(componentADL);

			PAComponent component = autonomous.getWrapComposite();

			autonomous.addPowerScalability("Slave");
			autonomous.addPowerScalability("Slave2");

			Remmos.enableMonitoring(component);

			Component compVizu = AVisuFactory.createComponentVisu(null, autonomous.getPatf(), autonomous.getPagf(),(PAComponent) component);
			final PAContentController contentController = Utils.getPAContentController(component);
			contentController.addFcSubComponent(compVizu);

			GCM.getGCMLifeCycleController(component).startFc();



			// TODO: Get the interface i1
			ItfRunner i1 = (ItfRunner) component.getFcInterface("runner");

			// TODO: Call the compute method
			List<String> li = Arrays.asList("Michaela","Yael","Deborah","Nathanael","Eden","Lea","Yonathan");
			//itf1.compute(li);
			i1.run(li);
			IService itf =  (IService) component.getFcInterface(CST.MANAGER_SERVICE);
			itf.run();

			//	boolean ok = true;
			//	final MonitorController monitormanager = Remmos.getMonitorController(autonomous.getManager());

			Collection<Component> collectionsComp = autonomous.getMapWrap().values();

			Component val = collectionsComp.iterator().next();
			final MonitorController mon = Remmos.getMonitorController(val);

			/*
			//while(ok){
				Wrapper<HashSet<String>> list =
						mon.getMetricList("/addscale_multicast-itf");
				for(String str : list.getValue()){
					System.out.println(mon.getMonitoredComponentName()+" >>> Metric >>> "+str);
				}

				mon.calculateMetric("MetricNbInstance_wrapscale_Slave2");
				System.out.println("nb instance = "+mon.getMetricValue("MetricNbInstance_wrapscale_Slave2").getValue().toString());

			//}*/
			// TODO: Stop Component
			Thread.sleep(100000);

			GCM.getGCMLifeCycleController(component).stopFc();

			System.exit(-1);

		}catch(Exception e){
			e.printStackTrace();
		}
	}


	/**
	 * I want just see if without autonomic I can start/stop bind and unbind. And if it's work
	 * @param args
	 * @throws Exception
	 */
	public static void runwithoutunomic(String[] args) throws Exception {
		//main1(args);

		System.out.println("Main");
		Factory factory = FactoryFactory.getFactory();

		final String adl = "fr.scale.gcm.scalable.simple.masterslave.adl.Composite";
		Map<String, Object> context = new HashMap<String, Object>();
		Component composite = (Component) factory.newComponent(
				adl, context);


		String defaultFcProvider = System.getProperty("fractal.provider");
		System.setProperty("fractal.provider", "org.objectweb.fractal.julia.Julia");
		Component gcmScript = GCMScript.newEngineFromAdl(ExecutorControllerImpl.AGCMSCRIPT_ADL);
		ScriptLoader loader = FScript.getScriptLoader(gcmScript);
		FScriptEngine engine = FScript.getFScriptEngine(gcmScript);
		engine.setGlobalVariable("this", ((GCMNodeFactory) FScript.getNodeFactory(gcmScript))
				.createGCMComponentNode(composite));

		if(defaultFcProvider !=null) System.setProperty("fractal.provider", defaultFcProvider);

		// engine.execute("unbind(bindings-to($this/Master/itf1));");
		/*{
        	Object result = engine.execute("$this");
        	 System.out.println(result);
        }
        {
        	Object result = engine.execute("$this/descendant::Slave/interface::itf1)");
        	engine.setGlobalVariable("slave", result);
        	Object result2 = engine.execute("$slave");

        	 System.out.println(result2);
        }

        {
        	Object result = engine.execute("unbind(bindings-to($this/descendant::Slave/interface::itf1))");
        	 System.out.println(result);
        }

		 */

		{

			Component boot = Utils.getBootstrapComponent();
			PAGCMTypeFactory tf = Utils.getPAGCMTypeFactory(boot);
			PAGenericFactory gf = Utils.getPAGenericFactory(boot);

			Utils.getPAMembraneController(composite).nfAddFcSubComponent(notfonc(tf,gf,null));
		}
		GCM.getGCMLifeCycleController(composite).startFc();
		Utils.getPAMembraneController(composite).startMembrane();

		Thread.sleep(1000);

		//test(composite,"test adl");


		Component slave = null;
		Component master = null;

		//Thread.sleep(1000);
		for(Component ca : GCM.getContentController(composite).getFcSubComponents()){
			String name = GCM.getNameController(ca).getFcName();
			if(name.equals("Slave")){
				slave = ca;
			}
			if(name.equals("Master")){
				master = ca;
			}
		};

		//Component slave2 = createSlave("Slave2");

		//                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                          vjuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuk;,,,,,,,,,,                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    	GCM.getContentController(composite).addFcSubComponent(slave2);
		//                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                          vjuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuk;,,,,,,,,,,                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    	GCM.getContentController(composite).addFcSubComponent(slave2);
		//	GCM.getBindingController(master).bindFc("itf1",slave2.getFcInterface("itf1"));



		System.out.println(">>>> STOP AND START");

		GCM.getGCMLifeCycleController(composite).stopFc();

		Thread.sleep(1000);

		GCM.getGCMLifeCycleController(composite).startFc();
		Thread.sleep(1000);
		System.out.println(Utils.getPABindingController(master).isBoundTo(slave));
		for(Object o : master.getFcInterfaces()){
			Interface i = (Interface) o;
			System.out.println(">>>>"+i.getFcItfName());
		}


		System.out.println(">>>> UNBIND AND REBIND");
		GCM.getGCMLifeCycleController(master).stopFc();
		Utils.getPAMulticastController(master).unbindGCMMulticast("itf1", "itf1");		
		Utils.getPABindingController(master).bindFc("itf1",slave.getFcInterface("itf1"));
		GCM.getGCMLifeCycleController(master).startFc();

		System.out.println("master is it binded to slave ?"+ Utils.getPABindingController(master).isBoundTo(slave));

		Thread.sleep(1000);
		test(composite,"test unbind and rebind");

		GCM.getGCMLifeCycleController(composite).stopFc();

		System.exit(0);
	}



	public static void runwithoutscale(String[] args) throws Exception {
		//main1(args);

		System.out.println("Main");


		final String adl = "fr.scale.gcm.scalable.simple.masterslave.adl.Composite";
		final String deploymentFilename = args.length > 1 ? args[1] : null;

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

		Component composite = (Component) factory.newAutonomicComponent(adl, context);
		Utils.getPAGCMLifeCycleController(composite).startFc();

		Remmos.enableMonitoring(composite); // [!] too much important, maybe merge with MonitoController.startMontioring?
		final MonitorController monitor = Remmos.getMonitorController(composite);
		monitor.startGCMMonitoring();
		Thread.sleep(2000);


		Thread.sleep(1000);
		monitor.stopGCMMonitoring();

		Utils.getPAGCMLifeCycleController(composite).stopFc();
		Utils.getPAGCMLifeCycleController(composite).startFc();
		monitor.startGCMMonitoring();
		Thread.sleep(2000);

		// TODO: Get the interface i1
		ItfRunner i1 = (ItfRunner) composite.getFcInterface("runner");

		// TODO: Call the compute method
		List<String> li = Arrays.asList("Michaela","Yael","Deborah","Nathanael","Eden","Lea","Yonathan");
		//itf1.compute(li);
		i1.run(li);



		Thread.sleep(1000);
		GCM.getGCMLifeCycleController(composite).stopFc();

		System.exit(0);
	}



	public static void runwithoutadl(String[] args) throws Exception {
		System.out.println("Main runwithoutadl");

		Component composite = createComplet();

		GCM.getGCMLifeCycleController(composite).startFc();

		ItfRunner i1 = (ItfRunner) composite.getFcInterface("runner");

		// TODO: Call the compute method
		List<String> li = Arrays.asList("Michaela","Yael","Deborah","Nathanael","Eden","Lea","Yonathan");
		//itf1.compute(li);
		//i1.run(li);



		Thread.sleep(1000);
		GCM.getGCMLifeCycleController(composite).stopFc();

		System.exit(0);
	}

	
	public static PAGCMInterfaceType[] createMonitorableNFType(PAGCMTypeFactory pagcmTf, PAGCMInterfaceType[] fItfType, String hierarchy) {

		ArrayList<PAGCMInterfaceType> typeList = new ArrayList<PAGCMInterfaceType>();
		
		// Normally, the NF interfaces mentioned here should be those that are going to be implemented by NF components,
		// and the rest of the NF interfaces (that are going to be implemented by object controller) should be in a ControllerDesc file.
		// But the PAComponentImpl ignores the NFType if there is a ControllerDesc file specified :(,
		// so I better put all the NF interfaces here.
		// That means that I need another method to add the object controllers for the (not yet created) controllers.
		try {
			// Object controller-managed server interfaces.
			typeList.add((PAGCMInterfaceType) pagcmTf.createGCMItfType(Constants.CONTENT_CONTROLLER, PAContentController.class.getName(), TypeFactory.SERVER, TypeFactory.MANDATORY, PAGCMTypeFactory.SINGLETON_CARDINALITY));
			typeList.add((PAGCMInterfaceType) pagcmTf.createGCMItfType(Constants.BINDING_CONTROLLER, PABindingController.class.getName(), TypeFactory.SERVER, TypeFactory.MANDATORY, PAGCMTypeFactory.SINGLETON_CARDINALITY));
			typeList.add((PAGCMInterfaceType) pagcmTf.createGCMItfType(Constants.LIFECYCLE_CONTROLLER, PAGCMLifeCycleController.class.getName(), TypeFactory.SERVER, TypeFactory.MANDATORY, PAGCMTypeFactory.SINGLETON_CARDINALITY));
			typeList.add((PAGCMInterfaceType) pagcmTf.createGCMItfType(Constants.SUPER_CONTROLLER, PASuperController.class.getName(), TypeFactory.SERVER, TypeFactory.MANDATORY, PAGCMTypeFactory.SINGLETON_CARDINALITY));
			typeList.add((PAGCMInterfaceType) pagcmTf.createGCMItfType(Constants.NAME_CONTROLLER, NameController.class.getName(), TypeFactory.SERVER, TypeFactory.MANDATORY, PAGCMTypeFactory.SINGLETON_CARDINALITY));
			typeList.add((PAGCMInterfaceType) pagcmTf.createGCMItfType(Constants.MEMBRANE_CONTROLLER, PAMembraneController.class.getName(), TypeFactory.SERVER, TypeFactory.MANDATORY, PAGCMTypeFactory.SINGLETON_CARDINALITY));
			typeList.add((PAGCMInterfaceType) pagcmTf.createGCMItfType(Constants.MULTICAST_CONTROLLER, PAMulticastController.class.getName(), TypeFactory.SERVER, TypeFactory.OPTIONAL, PAGCMTypeFactory.SINGLETON_CARDINALITY));
			String itfName;
			
			// external client Monitoring interfaces
			// add one client Monitoring interface for each client F interface
			// Support client-singleton, and client-multicast interfaces
			for(PAGCMInterfaceType itfType : fItfType) {
				if (!itfType.isFcClientItf()) continue;
				itfName = itfType.getFcItfName() + ACConstants.EXTERNAL_CLIENT_SUFFIX;
				if ((itfType.isGCMSingletonItf() && !itfType.isGCMCollectiveItf()) || itfType.isGCMGathercastItf()) {
					// add a client-singleton interface
					typeList.add((PAGCMInterfaceType) pagcmTf.createGCMItfType(itfName, MonitorController.class.getName(), TypeFactory.CLIENT, TypeFactory.OPTIONAL, PAGCMTypeFactory.SINGLETON_CARDINALITY));
				} else if(itfType.isGCMMulticastItf()) {
					// add a multicast client interface
					typeList.add((PAGCMInterfaceType) pagcmTf.createGCMItfType(itfName, MonitorControllerMulticast.class.getName(), TypeFactory.CLIENT, TypeFactory.OPTIONAL, PAGCMTypeFactory.MULTICAST_CARDINALITY));
				}
			}
			
			// composites have also internal client and server bindings
			if(Constants.COMPOSITE.equals(hierarchy)) {
				// one client internal Monitoring interface for each server binding
				// collective and multicast/gathercast interfaces not supported (yet)
				for(PAGCMInterfaceType itfType : fItfType) {
					// only server-singleton supported ... others ignored
					if(!itfType.isFcClientItf() && itfType.isGCMSingletonItf() && !itfType.isGCMCollectiveItf()) {
						itfName = itfType.getFcItfName() + ACConstants.INTERNAL_CLIENT_SUFFIX;
						typeList.add((PAGCMInterfaceType) pagcmTf.createGCMItfType(itfName, MonitorController.class.getName(), TypeFactory.CLIENT, TypeFactory.OPTIONAL, PAGCMTypeFactory.SINGLETON_CARDINALITY, PAGCMTypeFactory.INTERNAL));
					}
				}
				// one server internal Monitoring interface in each composite
				itfName = ACConstants.INTERNAL_SERVER_NFITF;
				typeList.add((PAGCMInterfaceType) pagcmTf.createGCMItfType(itfName, MonitorController.class.getName(), TypeFactory.SERVER, TypeFactory.OPTIONAL, PAGCMTypeFactory.SINGLETON_CARDINALITY, PAGCMTypeFactory.INTERNAL));
			}

		} catch (InstantiationException e) {
			e.printStackTrace();
		}

		return (PAGCMInterfaceType[]) typeList.toArray(new PAGCMInterfaceType[typeList.size()]);
	}

	public static void runwithoutadlwithoutautonimc(String[] args)  {
		try{
			Component boot = Utils.getBootstrapComponent();
			PAGCMTypeFactory tf = Utils.getPAGCMTypeFactory(boot);
			PAGenericFactory gf = Utils.getPAGenericFactory(boot);
			ComponentType tComposite = tf.createFcType(new InterfaceType[] { tf.createFcItfType("runner",
					ItfRunner.class.getName(), TypeFactory.SERVER, TypeFactory.MANDATORY, TypeFactory.SINGLE) });
			
			PAGCMInterfaceType[] tMasters = new PAGCMInterfaceType[] {
					(PAGCMInterfaceType) tf.createGCMItfType(MasterImpl.ITF_CLIENT, Itf1Multicast.class.getName(), CLI, MND, MC),
					(PAGCMInterfaceType) tf.createGCMItfType("runner", ItfRunner.class.getName(), SRV, MND, SC),
				};
			
			//Type tMaster = tf.createFcType(tMasters,createMonitorableNFType(tf,tMasters,Constants.PRIMITIVE));
			Type tMaster = tf.createFcType(new InterfaceType[] { 
		
		tf.createFcItfType("runner", ItfRunner.class.getName(), TypeFactory.SERVER, TypeFactory.MANDATORY, 
				TypeFactory.SINGLE),
		tf.createFcItfType("itf1", Itf1.class.getName(), TypeFactory.CLIENT, TypeFactory.MANDATORY, 
				TypeFactory.COLLECTION)
		});
			
			ComponentType tSlave = tf.createFcType(new InterfaceType[] { 
					tf.createFcItfType("itf1", Itf1.class.getName(), TypeFactory.SERVER, TypeFactory.MANDATORY, 
							TypeFactory.SINGLE) });

			Component slave  = gf.newFcInstance(tSlave, new ControllerDescription("slave", Constants.PRIMITIVE), SlaveImpl.class.getName());
			
			// TODO: Create the Master Component
			Component master = gf.newFcInstance(tMaster,
					new ControllerDescription("master", Constants.PRIMITIVE), 
					MasterImpl2.class.getName());

			
			Component composite = gf.newFcInstance(tComposite, new ControllerDescription("composite", Constants.COMPOSITE), null);
			// TODO: Add slave and master components to the composite component
			ContentController cc = GCM.getContentController(composite); 
			cc.addFcSubComponent(slave);
			cc.addFcSubComponent(master);
			// TODO: Do the bindings

			BindingController bcComposite = GCM.getBindingController(composite);
			bcComposite.bindFc("runner", master.getFcInterface("runner"));
			BindingController bcMaster = //Utils.getPAMulticastController(master);
			 GCM.getBindingController(master); 
			 Object it = slave.getFcInterface(MasterImpl.ITF_CLIENT);
			 for(Object o : master.getFcInterfaces()){
					Interface curItf = (Interface) o;
					System.err.println("interface ==>"+curItf.getFcItfName());
			 }
			 //bcMaster.bindGCMMulticast(MasterImpl.ITF_CLIENT, it);
			bcMaster.bindFc(MasterImpl.ITF_CLIENT, it);


			GCM.getGCMLifeCycleController(composite).startFc();
			Thread.sleep(1000);

			ItfRunner runner = (ItfRunner) composite.getFcInterface("runner"); 
			List<String> arg = new ArrayList<String>();
			arg.add("hello");
			arg.add("world");
			runner.run(arg);
			Thread.sleep(1000);
			System.err.println("end");
			GCM.getGCMLifeCycleController(composite).stopFc();

			/**/
			System.exit(0);
		}catch(Exception e){
			e.printStackTrace();
		}
	}


	static boolean auto = false;

	/**
	 * creation of manager
	 * @throws NoSuchInterfaceException 
	 * @throws IllegalLifeCycleException 
	 * @throws IllegalContentException 
	 * @throws IllegalBindingException 
	 */
	protected static  Component   createComplet() throws IllegalContentException, IllegalLifeCycleException, NoSuchInterfaceException, IllegalBindingException{
		Component comp = createComposite();
		Component master = createMaster();

		ContentController pac = GCM.getContentController(comp);
		pac.addFcSubComponent(master);
		/* 
		 * Component slave = createSlave("Slave1");
		Utils.getPAContentController(comp).addFcSubComponent(slave);
		Component slave2 = createSlave("Slave2");
		Utils.getPAContentController(comp).addFcSubComponent(slave2);

		PABindingController bind = Utils.getPABindingController(comp);
		bind.bindFc("runner", master.getFcInterface("runner"));
		Utils.getPABindingController(master).bindFc("itf1", slave.getFcInterface("itf1"));
		Utils.getPABindingController(master).bindFc("itf1", slave2.getFcInterface("itf1"));
		 */
		return comp;

	}



	/**
	 * creation of manager
	 */
	protected static Component   createComposite(){

		try {
			Component boot = Utils.getBootstrapComponent();
			PAGCMTypeFactory patf = Utils.getPAGCMTypeFactory(boot);
			PAGenericFactory pagf = Utils.getPAGenericFactory(boot);
			Remmos remmos = new Remmos(patf, pagf);


			PAGCMInterfaceType[] fTypes = new PAGCMInterfaceType[] {
					(PAGCMInterfaceType) patf.createGCMItfType("runner", ItfRunner.class.getName(), PAGCMTypeFactory.SERVER, PAGCMTypeFactory.OPTIONAL, PAGCMTypeFactory.SINGLETON_CARDINALITY),

			};
			Component  comp = null;



			if(auto){
				comp = (PAComponent) remmos.newFcInstance(
						remmos.createFcType(fTypes, Constants.COMPOSITE),
						new ControllerDescription("Composite", Constants.COMPOSITE),
						null,
						null);
				Utils.getPAMembraneController(comp).startMembrane();
				Remmos.addMonitoring(comp);
				Remmos.addAnalysis(comp);
				Remmos.addPlannerController(comp);
				Remmos.addExecutorController(comp);
			}else{
				comp = (PAComponent) pagf.newFcInstance(
						remmos.createFcType(fTypes, Constants.COMPOSITE),
						new ControllerDescription("Composite", Constants.COMPOSITE),
						null,
						null);
			}
			return comp;

		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * creation of master
	 */
	protected  static  Component createMaster(){
		try {
			Component boot = Utils.getBootstrapComponent();
			PAGCMTypeFactory patf = Utils.getPAGCMTypeFactory(boot);
			PAGenericFactory pagf = Utils.getPAGenericFactory(boot);
			Remmos remmos = new Remmos(patf, pagf);


			PAGCMInterfaceType[] fTypes = new PAGCMInterfaceType[] {
					(PAGCMInterfaceType) patf.createGCMItfType("runner", ItfRunner.class.getName(), PAGCMTypeFactory.SERVER, PAGCMTypeFactory.OPTIONAL, PAGCMTypeFactory.SINGLETON_CARDINALITY),

					(PAGCMInterfaceType) patf.createGCMItfType("itf1", Itf1Multicast.class.getName(), PAGCMTypeFactory.CLIENT, PAGCMTypeFactory.OPTIONAL, PAGCMTypeFactory.MULTICAST_CARDINALITY),

			};

			Component  comp = null;
			if(auto){
				comp = (PAComponent) remmos.newFcInstance(
						remmos.createFcType(fTypes, Constants.PRIMITIVE),
						new ControllerDescription("Master2", Constants.PRIMITIVE),
						new ContentDescription(MasterImpl.class.getName()),
						null);
				Utils.getPAMembraneController(comp).startMembrane();
				Remmos.addMonitoring(comp);
				Remmos.addAnalysis(comp);
				Remmos.addPlannerController(comp);
				Remmos.addExecutorController(comp);
			}else{
				comp = (PAComponent) pagf.newFcInstance(
						remmos.createFcType(fTypes, Constants.PRIMITIVE),
						new ControllerDescription("Master2", Constants.PRIMITIVE),
						new ContentDescription(MasterImpl.class.getName()),
						null);
			}
			return comp;

		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}


	/**
	 * creation of slave
	 * @param string 
	 */
	protected static  Component   createSlave(String string){
		try {
			Component boot = Utils.getBootstrapComponent();
			PAGCMTypeFactory patf = Utils.getPAGCMTypeFactory(boot);
			PAGenericFactory pagf = Utils.getPAGenericFactory(boot);
			Remmos remmos = new Remmos(patf, pagf);


			PAGCMInterfaceType[] fTypes = new PAGCMInterfaceType[] {
					(PAGCMInterfaceType) patf.createGCMItfType("itf1", Itf1.class.getName(), PAGCMTypeFactory.SERVER, PAGCMTypeFactory.OPTIONAL, PAGCMTypeFactory.SINGLETON_CARDINALITY),

			};
			Component  comp = null;

			if(auto){
				comp = (PAComponent) remmos.newFcInstance(
						remmos.createFcType(fTypes, Constants.PRIMITIVE),
						new ControllerDescription(string, Constants.PRIMITIVE),
						new ContentDescription(SlaveImpl.class.getName()),
						null);
				Utils.getPAMembraneController(comp).startMembrane();
				Remmos.addMonitoring(comp);
				Remmos.addAnalysis(comp);
				Remmos.addPlannerController(comp);
				Remmos.addExecutorController(comp);
			}else{
				comp = (PAComponent) pagf.newFcInstance(
						remmos.createFcType(fTypes, Constants.PRIMITIVE),
						new ControllerDescription(string, Constants.PRIMITIVE),
						new ContentDescription(SlaveImpl.class.getName()),
						null);
			}
			return comp;

		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}


	public static void main(final String... args) {
		try {
			//runwithoutunomic(args); //test sans autonic juste pour verifier le stop/start unbind bind ect...
			//run1(args);
			runwithoutadlwithoutautonimc(args);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}


	protected static final String COMPONENT_CONTROLLER_CONFIG = 
			"/org/objectweb/proactive/core/component/componentcontroller/config/default-component-controller-config.xml";

	protected static Component notfonc(PAGCMTypeFactory patf, PAGenericFactory pagf, Node node) {
		try {
			InterfaceType[] itfType = new InterfaceType[] {
					patf.createGCMItfType("i2", Itf2.class.getName(), TypeFactory.SERVER, TypeFactory.OPTIONAL, PAGCMTypeFactory.SINGLETON_CARDINALITY),
			};

			ComponentType t = patf.createFcType(itfType);

			return pagf.newNfFcInstance(t, 
					new ControllerDescription("i2", 
							Constants.PRIMITIVE, COMPONENT_CONTROLLER_CONFIG), 
					new ContentDescription(Itf2Impl.class.getName()), node);
		} catch (InstantiationException e) {
			e.printStackTrace();
			return null;
		}
	}
	private static void echo(String str){
		System.out.println(str);
	}

}
