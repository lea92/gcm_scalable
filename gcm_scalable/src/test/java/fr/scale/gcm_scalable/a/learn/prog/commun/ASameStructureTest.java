package fr.scale.gcm_scalable.a.learn.prog.commun;

import static org.junit.Assert.*;

import org.junit.Test;
import org.objectweb.fractal.adl.ADLException;
import org.objectweb.fractal.adl.Factory;
import org.objectweb.fractal.adl.FactoryFactory;
import org.objectweb.fractal.api.Component;
import org.objectweb.fractal.api.NoSuchInterfaceException;
import org.objectweb.fractal.api.control.IllegalBindingException;
import org.objectweb.fractal.api.control.IllegalContentException;
import org.objectweb.fractal.api.control.IllegalLifeCycleException;
import org.objectweb.proactive.core.component.Utils;
import org.objectweb.proactive.core.component.control.PABindingController;
import org.objectweb.proactive.core.component.control.PAContentController;

import fr.scale.gcm_scalable.a.learn.prog.adl.nomembrane.NoMembStruct;
import fr.scale.gcm_scalable.a.learn.prog.commun.elements.MasterImpl;
import fr.scale.gcm_scalable.a.learn.prog.commun.elements.Runner;
import fr.scale.gcm_scalable.a.learn.prog.commun.elements.SlaveImpl;

import org.junit.Test;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ASameStructureTest{

	private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
	private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();

	@Before
	public void setUpStreams() {
	    System.setOut(new PrintStream(outContent));
	    System.setErr(new PrintStream(errContent));
	}

	@After
	public void cleanUpStreams() {
	    System.setOut(null);
	    System.setErr(null);
	}

	public boolean isAPI(){
		return true;
	}
	
	
	@Test 
	public void existcomposite() throws ADLException{
		Component composite = null ;
		
		Map<String, Object> context = new HashMap<String, Object>();
		String adl = "fr.scale.gcm_scalable.a.learn.prog.commun.elements.adl.Composite";
		Factory f = FactoryFactory.getFactory();
		composite = (Component) f.newComponent(adl ,context);
		System.out.println(composite);	
		
		
		assertNotNull(composite);
	}
	@Test
	public void run() throws IllegalContentException, IllegalLifeCycleException, NoSuchInterfaceException, IllegalBindingException, InterruptedException{
		
		ISameStructure struct = new NoMembStruct("fr.scale.gcm_scalable.a.learn.prog.commun.elements.adl.Composite");
		Component composite = struct.createComposite();

		//Create the Slave Component 
		Component slave  = struct.createSlave1();
		Component slave2 = struct.createSlave2();
		Component elm2   = struct.createElmt2();


		CST.log("Create the Master Multicast");
		// Create the Master Multicast
		Component master = struct.createMasterMulticast();
		CST.log(" Add slave and master components to the composite component");
		// Add slave and master components to the composite component
		if(struct.isAPI()){
			PAContentController pacc = Utils.getPAContentController(composite);
			pacc.addFcSubComponent(master);
			pacc.addFcSubComponent(slave);
			pacc.addFcSubComponent(slave2);
			pacc.addFcSubComponent(elm2);


			CST.log(" Do the bindings");
			// Do the bindings
			PABindingController pabc = Utils.getPABindingController(composite);
			pabc.bindFc("runner", master.getFcInterface("runner"));


			Utils.getPABindingController(master).bindFc(MasterImpl.ITF_CLIENT_2,elm2.getFcInterface(MasterImpl.ITF_CLIENT_2));

			Utils.getPAMulticastController(master).bindGCMMulticast(MasterImpl.ITF_CLIENT_M, slave.getFcInterface("i1"));
			Utils.getPAMulticastController(master).bindGCMMulticast(MasterImpl.ITF_CLIENT_M, slave2.getFcInterface("i1"));

			Object[] o = {slave.getFcInterface("i1")};
			System.err.println( Utils.getPAMulticastController(master).isBoundTo(MasterImpl.ITF_CLIENT_M, o));
		}

		struct.start(composite);
		CST.log("start composite");
		{
			Runner runner = (Runner) composite.getFcInterface("runner");
			List<String> arg = new ArrayList<String>();
			arg.add("test 1 : ");
			arg.add("start composite");
			List<String> res = runner.run(arg);

			SlaveImpl si = new SlaveImpl();
			String r = si.compute(arg);
			
		    assertEquals(r, res.get(0));
		    assertEquals(r, res.get(1));
		}

		CST.log("start stop master");
		struct.stopstart(master);
		{
			Runner runner = (Runner) composite.getFcInterface("runner");
			List<String> arg = new ArrayList<String>();
			arg.add("test 2: ");
			arg.add("start stop master");
			List<String> res = runner.run(arg);
			

			SlaveImpl si = new SlaveImpl();
			String r = si.compute(arg);
			
		    assertEquals(r, res.get(0));
		    assertEquals(r, res.get(1));
		}

		CST.log("stop and start composite");
		struct.stopstart(composite);

		{
			Runner runner = (Runner) composite.getFcInterface("runner");
			List<String> arg = new ArrayList<String>();
			arg.add("test 3 : ");
			arg.add("stop and start composite");
			runner.run(arg);
		}

		Thread.sleep(1000);

		CST.log("bind and unbind");
		struct.unbindbindmulticast(composite,master,slave);

		{
			Runner runner = (Runner) composite.getFcInterface("runner");
			List<String> arg = new ArrayList<String>();
			arg.add("test 4 : ");
			arg.add("bind and unbind");
			runner.run(arg);
		}
		

		{
			Runner runner = (Runner) composite.getFcInterface("runner");
			
			String arg = "run interface 2";
			runner.run2(arg );
		}

		struct.unbindbindsingleton(composite,master,elm2);
		

		{
			Runner runner = (Runner) composite.getFcInterface("runner");
			
			String arg = " run interface 2 after unbind";
			runner.run2(arg );
		}

		Thread.sleep(10000);

		System.exit(0);

	}

	
}
