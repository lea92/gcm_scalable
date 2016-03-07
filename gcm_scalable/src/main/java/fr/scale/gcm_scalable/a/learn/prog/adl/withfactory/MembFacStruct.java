package fr.scale.gcm_scalable.a.learn.prog.adl.withfactory;

import java.util.HashMap;
import java.util.Map;

import org.etsi.uri.gcm.util.GCM;
import org.objectweb.fractal.adl.ADLException;
import org.objectweb.fractal.adl.Factory;
import org.objectweb.fractal.api.Component;
import org.objectweb.fractal.api.NoSuchInterfaceException;
import org.objectweb.fractal.api.Type;
import org.objectweb.fractal.api.control.IllegalBindingException;
import org.objectweb.fractal.api.control.IllegalLifeCycleException;
import org.objectweb.proactive.core.component.Constants;
import org.objectweb.proactive.core.component.Utils;
import org.objectweb.proactive.core.component.adl.FactoryFactory;
import org.objectweb.proactive.core.component.factory.PAGenericFactory;
import org.objectweb.proactive.core.component.identity.PAComponent;
import org.objectweb.proactive.core.component.type.PAGCMTypeFactory;

import fr.scale.gcm_scalable.a.learn.adl.MyFactoryFactory;
import fr.scale.gcm_scalable.a.learn.prog.commun.ASameStructure;
import fr.scale.gcm_scalable.a.learn.prog.commun.CST;
import fr.scale.gcm_scalable.a.learn.prog.commun.elements.MasterImpl;

public class MembFacStruct extends ASameStructure{

	Component composite, slave1, slave2, master,elm2;

	public boolean isAPI(){
		return false;
	}

	public MembFacStruct(){
		try {
			Factory factory = MyFactoryFactory.getAFactory();

			final String adl = "fr.scale.gcm_scalable.a.learn.prog.commun.elements.adl.Composite";
			Map<String, Object> context = new HashMap<String, Object>();
			composite = (Component) factory.newComponent(
					adl, context);

			for(Component ca : GCM.getContentController(composite).getFcSubComponents()){
				String name = GCM.getNameController(ca).getFcName();
				if(name.equals("Slave")){
					slave1 = ca;
				}else if(name.equals("Master")){
					master = ca;
				}else if(name.equals("Slave2")){
					slave2 = ca;
				}else if(name.equals("Elm2")){
					elm2 = ca;
				}
			};

		} catch (ADLException e) {
			e.printStackTrace();
		} catch (NoSuchInterfaceException e) {
			e.printStackTrace();
		}
	}

	@Override
	public Component createComposite() {
		return composite;
	}

	@Override
	public Component createMasterMulticast() {
		return master;
	}

	@Override
	public Component createSlave1() {
		return slave1;
	}

	@Override
	public Component createSlave2() {
		return slave2;
	}

	@Override
	public Component createElmt2() {
		return elm2;
	}

	@Override
	public void start(Component comp) {
		try {
			CST.log("start all ");    
			PAComponent composite = (PAComponent) comp;
			Utils.getPAMembraneController(composite).startMembrane();
			Type type = composite.getFcType();

			if(composite.getComponentParameters().getHierarchicalType().equals(Constants.COMPOSITE)){
				for( Component sub : Utils.getPAContentController(composite).getFcSubComponents()){
					Utils.getPAMembraneController(sub).startMembrane();
				}
			}
			CST.log("start");     
			Utils.getPAGCMLifeCycleController(composite).startFc();
		} catch (IllegalLifeCycleException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchInterfaceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	@Override
	public void stop(Component comp) {
		try {

			PAComponent composite = (PAComponent) comp;
			System.out.println("stopmb");    
			Utils.getPAMembraneController(composite).stopMembrane();
			if(composite.getComponentParameters().getHierarchicalType().equals(Constants.COMPOSITE)){
				for( Component sub : Utils.getPAContentController(composite).getFcSubComponents()){
					Utils.getPAMembraneController(sub).stopMembrane();
				}
			}
			System.out.println("stop");     
			Utils.getPAGCMLifeCycleController(composite).stopFc();
		} catch (IllegalLifeCycleException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchInterfaceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	@Override
	public void stopstart(Component composite) {
		stop(composite);
		try {
			Thread.sleep(1000);

			start(composite);

			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}


	public void unbindbindmulticast(Component composite, Component master, Component slave) {
		{
			try {
				
				CST.log("stop master");
				//Utils.getPAGCMLifeCycleController(master).stopFc();
				Thread.sleep(1000);

				CST.log("unbind");
				Utils.getPAMulticastController(master).unbindGCMMulticast(MasterImpl.ITF_CLIENT_M, slave.getFcInterface(MasterImpl.ITF_CLIENT_M));		
				Thread.sleep(1000);

				this.stop(master);
				Thread.sleep(1000);
				
				CST.log("bind");
				Utils.getPAMulticastController(master).bindGCMMulticast("i1",slave.getFcInterface("i1"));
				
				Thread.sleep(1000);
				
				this.start(master);

				System.out.println("master is it binded to slave ?"+ Utils.getPABindingController(master).isBoundTo(slave));
			}catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}


		}

	}

}