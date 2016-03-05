package fr.scale.gcm_scalable.a.learn.prog.api.withmape;

import org.objectweb.fractal.api.Component;
import org.objectweb.fractal.api.NoSuchInterfaceException;
import org.objectweb.fractal.api.control.IllegalLifeCycleException;
import org.objectweb.fractal.api.factory.InstantiationException;
import org.objectweb.fractal.api.type.ComponentType;
import org.objectweb.proactive.core.component.Constants;
import org.objectweb.proactive.core.component.ContentDescription;
import org.objectweb.proactive.core.component.ControllerDescription;
import org.objectweb.proactive.core.component.Utils;
import org.objectweb.proactive.core.component.factory.PAGenericFactory;
import org.objectweb.proactive.core.component.type.PAGCMInterfaceType;
import org.objectweb.proactive.core.component.type.PAGCMTypeFactory;
import org.objectweb.proactive.core.node.Node;
import org.objectweb.proactive.extensions.autonomic.controllers.remmos.Remmos;
import examples.services.autoadaptable.AASCST;
import fr.scale.gcm_scalable.a.learn.prog.commun.ASameStructure;
import fr.scale.gcm_scalable.a.learn.prog.commun.CST;
import fr.scale.gcm_scalable.a.learn.prog.commun.elements.Elmt2Impl;
import fr.scale.gcm_scalable.a.learn.prog.commun.elements.Itf1;
import fr.scale.gcm_scalable.a.learn.prog.commun.elements.Itf2;
import fr.scale.gcm_scalable.a.learn.prog.commun.elements.ItfMulticast;
import fr.scale.gcm_scalable.a.learn.prog.commun.elements.MasterImpl;
import fr.scale.gcm_scalable.a.learn.prog.commun.elements.Runner;
import fr.scale.gcm_scalable.a.learn.prog.commun.elements.SlaveImpl;

public class MapeStructure extends ASameStructure{


	Remmos remmos;
	PAGCMTypeFactory tf;
	PAGenericFactory gf ;

	Node node;

	
	public MapeStructure(){
		try {
			Component boot = Utils.getBootstrapComponent();
			tf = Utils.getPAGCMTypeFactory(boot);
			gf = Utils.getPAGenericFactory(boot);

			remmos = new Remmos(tf,gf);
			remmos.checkFactories();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (NoSuchInterfaceException e) {
			e.printStackTrace();
		}

	}

	@Override
	public Component createComposite() {
		Component comp = null;
		PAGCMInterfaceType[] fTypes;
		try {
			fTypes = new PAGCMInterfaceType[] {
					(PAGCMInterfaceType) tf.createGCMItfType("runner", Runner.class.getName(), CST.SRV, CST.MND, CST.SC),
			};

			comp = remmos.newFcInstance(
					remmos.createFcType(fTypes, Constants.COMPOSITE),
					new ControllerDescription(AASCST.SERVICE_COMP_NAME, Constants.COMPOSITE),
					null, node);

			addMAPE(comp);
		}catch (InstantiationException e1) {
			e1.printStackTrace();
		}catch(Exception e){
			e.printStackTrace();
		}
		return comp;
	}


	//-----------------------------------------------------------------------
	//MASTER MULTICAST
	//-----------------------------------------------------------------------

	@Override
	public Component createMasterMulticast() {
		Component master = null;
		ComponentType tMaster;
		try {
			tMaster = remmos.createFcType
					(new PAGCMInterfaceType[] {
							(PAGCMInterfaceType) tf.createGCMItfType(MasterImpl.ITF_CLIENT_M, ItfMulticast.class.getName(), CST.CLI, CST.MND, CST.MC),
							(PAGCMInterfaceType) tf.createGCMItfType(MasterImpl.ITF_CLIENT_2, fr.scale.gcm_scalable.a.learn.prog.commun.elements.Itf2.class.getName(), CST.CLI, CST.MND, CST.SC),
							(PAGCMInterfaceType) tf.createGCMItfType("runner", Runner.class.getName(), CST.SRV, CST.MND, CST.SC),
					}, Constants.PRIMITIVE);

			master = remmos.newFcInstance(tMaster, 
					new ControllerDescription("slave", Constants.PRIMITIVE),
					new ContentDescription(MasterImpl.class.getName()) , node);
		
		    addMAPE(master);
		} catch (InstantiationException e) {
			e.printStackTrace();
		}
		return master;
	}

	//-----------------------------------------------------------------------
	//DEBUT SLAVE 
	//-----------------------------------------------------------------------

	@Override
	public Component createSlave1() {
		return createSlave("slave1");
	}

	@Override
	public Component createSlave2() {
		return createSlave("slave2");
	}

	private Component createSlave(String name){
		Component slave = null;
		PAGCMInterfaceType[] ftypes;
		try {
			ftypes = new PAGCMInterfaceType[] {
					(PAGCMInterfaceType) tf.createGCMItfType("i1", Itf1.class
							.getName(), CST.SRV,CST.MND,CST.SC) };
			slave = remmos.newFcInstance(
					remmos.createFcType(ftypes, Constants.PRIMITIVE), 
					new ControllerDescription(name, Constants.PRIMITIVE),
					new ContentDescription(SlaveImpl.class.getName()), node);
			addMAPE(slave); 
		} catch (InstantiationException e) {
			e.printStackTrace();
		}
		return slave;

	}

	//-----------------------------------------------------------------------
	//fin SLAVE
	//-----------------------------------------------------------------------

	private void addMAPE(Component comp) {
		try {
			Utils.getPAMembraneController(comp).startMembrane();
			Remmos.addMonitoring(comp);
			Remmos.addAnalysis(comp);
			Remmos.addPlannerController(comp);
			Remmos.addExecutorController(comp);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public Component createElmt2() {
		Component slave = null;
		PAGCMInterfaceType[] ftypes;
		try {
			ftypes = new PAGCMInterfaceType[] {
					(PAGCMInterfaceType) tf.createGCMItfType(MasterImpl.ITF_CLIENT_2,
							Itf2.class
							.getName(), CST.SRV,CST.MND,CST.SC) };
			slave = remmos.newFcInstance(
					remmos.createFcType(ftypes, Constants.PRIMITIVE), 
					new ControllerDescription("elm2", Constants.PRIMITIVE),
					new ContentDescription(Elmt2Impl.class.getName()), node);
			addMAPE(slave); 
		} catch (InstantiationException e) {
			e.printStackTrace();
		}
		return slave;
	}

	@Override
	public void stop(Component composite) {
		try {
			Utils.getPAGCMLifeCycleController(composite).stopFc();
		} catch (IllegalLifeCycleException e) {
			e.printStackTrace();
		} catch (NoSuchInterfaceException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void start(Component composite) {

		try {

	        CST.log("start membrane");     
	        Remmos.enableMonitoring(composite); // [!] too much important, maybe merge with MonitoController.startMontioring?
			
	        CST.log("start composite");     
	        
	        Utils.getPAGCMLifeCycleController(composite).startFc();
	        
		} catch (IllegalLifeCycleException e) {
			e.printStackTrace();
		} catch (NoSuchInterfaceException e) {
			e.printStackTrace();
		}
	}

}
