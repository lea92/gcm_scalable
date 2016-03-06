package fr.scale.gcm_scalable.a.learn.prog.commun;

import java.util.ArrayList;
import java.util.List;

import org.etsi.uri.gcm.util.GCM;
import org.objectweb.fractal.api.Component;
import org.objectweb.fractal.api.NoSuchInterfaceException;
import org.objectweb.fractal.api.control.IllegalBindingException;
import org.objectweb.fractal.api.control.IllegalContentException;
import org.objectweb.fractal.api.control.IllegalLifeCycleException;
import org.objectweb.proactive.core.component.Utils;
import org.objectweb.proactive.core.component.control.PABindingController;
import org.objectweb.proactive.core.component.control.PAContentController;

import fr.scale.gcm_scalable.a.learn.prog.commun.elements.MasterImpl;
import fr.scale.gcm_scalable.a.learn.prog.commun.elements.Runner;

public abstract class ASameStructure implements ISameStructure{


	public boolean isAPI(){
		return true;
	}

	public void run() throws IllegalContentException, IllegalLifeCycleException, NoSuchInterfaceException, IllegalBindingException, InterruptedException{
		Component composite = this.createComposite();

		//Create the Slave Component 
		Component slave  = this.createSlave1();
		Component slave2 = this.createSlave2();
		Component elm2   = this.createElmt2();


		CST.log("Create the Master Multicast");
		// Create the Master Multicast
		Component master = this.createMasterMulticast();
		CST.log(" Add slave and master components to the composite component");
		// Add slave and master components to the composite component
		if(this.isAPI()){
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

		this.start(composite);
		CST.log("start composite");
		{
			Runner runner = (Runner) composite.getFcInterface("runner");
			List<String> arg = new ArrayList<String>();
			arg.add("test 1 : ");
			arg.add("start composite");
			runner.run(arg);
		}

		CST.log("start stop master");
		this.stopstart(master);
		{
			Runner runner = (Runner) composite.getFcInterface("runner");
			List<String> arg = new ArrayList<String>();
			arg.add("test 2: ");
			arg.add("start stop master");
			runner.run(arg);
		}

		CST.log("stop and start composite");
		this.stopstart(composite);

		{
			Runner runner = (Runner) composite.getFcInterface("runner");
			List<String> arg = new ArrayList<String>();
			arg.add("test 3 : ");
			arg.add("stop and start composite");
			runner.run(arg);
		}

		Thread.sleep(1000);

		CST.log("bind and unbind");
		this.unbindbindmulticast(composite,master,slave);

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

		this.unbindbindsingleton(composite,master,elm2);
		

		{
			Runner runner = (Runner) composite.getFcInterface("runner");
			
			String arg = " run interface 2 after unbind";
			runner.run2(arg );
		}

		Thread.sleep(10000);

		System.exit(0);

	}

	public void unbindbindmulticast(Component composite, Component master, Component slave) {
		{
			try {

				CST.log("unbind");
				Utils.getPAMulticastController(master).unbindGCMMulticast(MasterImpl.ITF_CLIENT_M, slave.getFcInterface(MasterImpl.ITF_CLIENT_M));		
				Thread.sleep(1000);

				CST.log("stop master");
				this.stop(master);
				Thread.sleep(1000);

				CST.log("bind");
				Utils.getPAMulticastController(master).bindGCMMulticast("i1",slave.getFcInterface("i1"));
				this.start(master);

				System.out.println("master is it binded to slave ?"+ Utils.getPAMulticastController(master).isBoundTo("i1", new Object[]{slave.getFcInterface("i1")}));
			} catch (IllegalLifeCycleException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NoSuchInterfaceException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalBindingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public void unbindbindsingleton(Component composite, Component master, Component elm2)
	{
		try {

			
			CST.log("stop master");
			this.stop(master);
			Thread.sleep(1000);

			CST.log("unbind");
			Utils.getPABindingController(master).unbindFc(MasterImpl.ITF_CLIENT_2);		
			Thread.sleep(1000);

			
			CST.log("bind");
			Utils.getPABindingController(master).bindFc(MasterImpl.ITF_CLIENT_2,elm2.getFcInterface(MasterImpl.ITF_CLIENT_2));
			this.start(master);

			System.out.println("master is it binded to slave ?"+ Utils.getPABindingController(master).isBoundTo(elm2));
		} catch (IllegalLifeCycleException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchInterfaceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalBindingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
