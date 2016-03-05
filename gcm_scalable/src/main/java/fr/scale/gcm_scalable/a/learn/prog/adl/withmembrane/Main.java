package fr.scale.gcm_scalable.a.learn.prog.adl.withmembrane;

import java.util.HashMap;
import java.util.Map;

import org.etsi.uri.gcm.api.type.GCMTypeFactory;
import org.etsi.uri.gcm.util.GCM;
import org.objectweb.fractal.adl.Factory;
import org.objectweb.fractal.api.Component;
import org.objectweb.fractal.api.Interface;
import org.objectweb.fractal.api.factory.GenericFactory;
import org.objectweb.fractal.fscript.FScript;
import org.objectweb.fractal.fscript.FScriptEngine;
import org.objectweb.fractal.fscript.ScriptLoader;
import org.objectweb.proactive.core.component.Utils;
import org.objectweb.proactive.core.component.adl.FactoryFactory;
import org.objectweb.proactive.core.component.factory.PAGenericFactory;
import org.objectweb.proactive.core.component.type.PAGCMTypeFactory;
import org.objectweb.proactive.extensions.autonomic.adl.ABasicFactory;
import org.objectweb.proactive.extensions.autonomic.controllers.execution.ExecutorControllerImpl;
import org.objectweb.proactive.extra.component.fscript.GCMScript;
import org.objectweb.proactive.extra.component.fscript.model.GCMNodeFactory;

import fr.scale.gcm_scalable.a.learn.adl.AFactoryFactory;
import fr.scale.gcm_scalable.a.learn.prog.commun.CST;
import fr.scale.gcm_scalable.a.learn.prog.commun.MainCommun;

// I want to create a composite with membrane

//-Djava.security.manager -Djava.security.policy=/Users/lelbeze/Desktop/allPerm.policy  -Dgcm.provider=org.objectweb.proactive.core.component.Fractive
public class Main extends MainCommun{

	/**
	 * I want just see if without autonomic I can start/stop bind and unbind. And if it's work
	 * @param args
	 * @throws Exception
	 */
	public static void runwithoutunomic(String[] args) throws Exception {
		//main1(args);

		System.out.println("Main");
		PAGCMTypeFactory typeFactory;
		Factory factory = AFactoryFactory.getAFactory();
		Component boot = Utils.getBootstrapComponent();
        PAGCMTypeFactory tf = Utils.getPAGCMTypeFactory(boot);
        PAGenericFactory gf = Utils.getPAGenericFactory(boot);

		
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
	        //Utils.getPAMembraneController(composite).nfAddFcSubComponent(notfonc(tf,gf,null));
		}
		Utils.getPAGCMLifeCycleController(composite).startFc();
		
		Utils.getPAMembraneController(composite).startMembrane();

		Thread.sleep(1000);

		CST.log("Demande 1");
		test(composite,"test 1");

		Utils.getPAMembraneController(composite).stopMembrane();

		CST.log("Demande 2 apres stop membrane");

		test(composite,"test 2");
		
		Component slave = null;
		Component master = null;
		

		CST.log("Stop life cycle");

		Utils.getPAGCMLifeCycleController(composite).stopFc();
		Thread.sleep(1000);

		CST.log("Start life cycle + membrane");

		Utils.getPAMembraneController(composite).startMembrane();
		Thread.sleep(1000);
		
		Utils.getPAGCMLifeCycleController(composite).startFc();
		Thread.sleep(1000);
		
		
		test(composite,"test 3");
		
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

	
	public static void main(String[] args) {
		try {
			runwithoutunomic(args);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
