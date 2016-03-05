package fr.scale.gcm_scalable.a.learn.commun;

import java.util.ArrayList;
import java.util.List;

import org.objectweb.fractal.api.Component;
import org.objectweb.fractal.api.NoSuchInterfaceException;
import org.objectweb.fractal.api.control.IllegalBindingException;
import org.objectweb.fractal.api.control.IllegalContentException;
import org.objectweb.fractal.api.control.IllegalLifeCycleException;
import org.objectweb.proactive.core.component.Utils;
import org.objectweb.proactive.core.component.control.PABindingController;
import org.objectweb.proactive.core.component.control.PAContentController;

import fr.scale.gcm_scalable.a.learn.commun.elements.MasterImpl;
import fr.scale.gcm_scalable.a.learn.commun.elements.Runner;

public abstract class ASameStructure implements ISameStructure{

	
	public void run() throws IllegalContentException, IllegalLifeCycleException, NoSuchInterfaceException, IllegalBindingException, InterruptedException{
		Component composite = this.createComposite();

		//Create the Slave Component 
		Component slave  = this.createSlave1();
		Component slave2 = this.createSlave2();
		Component elm2   = this.createElmt2();

		// Create the Master Multicast
		Component master = this.createMasterMulticast();

		// Add slave and master components to the composite component
		PAContentController pacc = Utils.getPAContentController(composite);
		pacc.addFcSubComponent(master);
		pacc.addFcSubComponent(slave);
		pacc.addFcSubComponent(slave2);
		pacc.addFcSubComponent(elm2);

		// Do the bindings
		PABindingController pabc = Utils.getPABindingController(composite);
		pabc.bindFc("runner", master.getFcInterface("runner"));
		PABindingController pbmaster = Utils.getPABindingController(master);
		//pbmaster.bindFc(MasterImpl.ITF_CLIENT_M, slave.getFcInterface("i1"));
		//System.out.println(Utils.getPABindingController(master).lookupFc("im1"));
		Object[] it = Utils.getPABindingController(master).listFc();

		for(Object i : it){
			System.out.println(">> " + i);
		}
		slave.getFcInterface("i1");
		Utils.getPABindingController(master).bindFc(MasterImpl.ITF_CLIENT_2,elm2.getFcInterface(MasterImpl.ITF_CLIENT_2));

		Utils.getPAMulticastController(master).bindGCMMulticast(MasterImpl.ITF_CLIENT_M, slave.getFcInterface("i1"));
		Utils.getPAMulticastController(master).bindGCMMulticast(MasterImpl.ITF_CLIENT_M, slave2.getFcInterface("i1"));

		Object[] o = {slave.getFcInterface("i1")};
		System.err.println( Utils.getPAMulticastController(master).isBoundTo(MasterImpl.ITF_CLIENT_M, o));

		this.start(composite);
		CST.log("start");
		{
			Runner runner = (Runner) composite.getFcInterface("runner");
			List<String> arg = new ArrayList<String>();
			arg.add("hello");
			arg.add("world");
			runner.run(arg);
		}
		
		CST.log("stop master");
		Utils.getPAGCMLifeCycleController(master).stopFc();
		Thread.sleep(1000);
		CST.log("start master");
		Utils.getPAGCMLifeCycleController(master).startFc();
		Thread.sleep(1000);
		{
			Runner runner = (Runner) composite.getFcInterface("runner");
			List<String> arg = new ArrayList<String>();
			arg.add("hello1");
			arg.add("world1");
			runner.run(arg);
		}
		
		CST.log("stop composite");
		this.stop(composite);
		Thread.sleep(1000);
		this.start(composite);

		{
			Runner runner = (Runner) composite.getFcInterface("runner");
			List<String> arg = new ArrayList<String>();
			arg.add("hello2");
			arg.add("world2");
			runner.run(arg);
		}

		Thread.sleep(10000);
		System.exit(0);
	
	}
}
