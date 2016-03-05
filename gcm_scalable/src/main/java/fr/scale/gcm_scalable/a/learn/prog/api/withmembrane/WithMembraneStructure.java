package fr.scale.gcm_scalable.a.learn.prog.api.withmembrane;

import org.objectweb.fractal.api.Component;
import org.objectweb.fractal.api.NoSuchInterfaceException;
import org.objectweb.fractal.api.control.IllegalLifeCycleException;
import org.objectweb.fractal.api.control.NameController;
import org.objectweb.fractal.api.factory.InstantiationException;
import org.objectweb.fractal.api.type.ComponentType;
import org.objectweb.fractal.api.type.InterfaceType;
import org.objectweb.fractal.api.type.TypeFactory;
import org.objectweb.proactive.core.component.Constants;
import org.objectweb.proactive.core.component.ContentDescription;
import org.objectweb.proactive.core.component.ControllerDescription;
import org.objectweb.proactive.core.component.Utils;
import org.objectweb.proactive.core.component.control.PABindingController;
import org.objectweb.proactive.core.component.control.PAGCMLifeCycleController;
import org.objectweb.proactive.core.component.control.PAInterceptorController;
import org.objectweb.proactive.core.component.control.PAMembraneController;
import org.objectweb.proactive.core.component.factory.PAGenericFactory;
import org.objectweb.proactive.core.component.type.PAGCMInterfaceType;
import org.objectweb.proactive.core.component.type.PAGCMTypeFactory;
import org.objectweb.proactive.core.node.Node;
import fr.scale.gcm_scalable.a.learn.prog.commun.ASameStructure;
import fr.scale.gcm_scalable.a.learn.prog.commun.CST;
import fr.scale.gcm_scalable.a.learn.prog.commun.elements.Elmt2Impl;
import fr.scale.gcm_scalable.a.learn.prog.commun.elements.Itf1;
import fr.scale.gcm_scalable.a.learn.prog.commun.elements.ItfMulticast;
import fr.scale.gcm_scalable.a.learn.prog.commun.elements.MasterImpl;
import fr.scale.gcm_scalable.a.learn.prog.commun.elements.NoFCImpl;
import fr.scale.gcm_scalable.a.learn.prog.commun.elements.NoFCItf;
import fr.scale.gcm_scalable.a.learn.prog.commun.elements.Runner;
import fr.scale.gcm_scalable.a.learn.prog.commun.elements.SlaveImpl;

public class WithMembraneStructure extends ASameStructure{

	PAGCMTypeFactory tf;
	PAGenericFactory gf;

	public WithMembraneStructure(){
		Component boot;
		try {
			boot = Utils.getBootstrapComponent();
			tf = Utils.getPAGCMTypeFactory(boot);
			gf = Utils.getPAGenericFactory(boot);

		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (NoSuchInterfaceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	//create composite with membrane
	@Override
	public Component createComposite() {
		try{
			InterfaceType[] nfItfTypes = new InterfaceType[] {
					tf.createFcItfType(Constants.BINDING_CONTROLLER, PABindingController.class
							.getName(), TypeFactory.SERVER, TypeFactory.MANDATORY, TypeFactory.SINGLE),
					tf.createFcItfType(Constants.LIFECYCLE_CONTROLLER,
							PAGCMLifeCycleController.class.getName(), TypeFactory.SERVER, TypeFactory.MANDATORY,
							TypeFactory.SINGLE),
					tf.createFcItfType(Constants.NAME_CONTROLLER, NameController.class.getName(),
							TypeFactory.SERVER, TypeFactory.MANDATORY, TypeFactory.SINGLE),
					tf.createFcItfType(Constants.MEMBRANE_CONTROLLER, PAMembraneController.class
							.getName(), TypeFactory.SERVER, TypeFactory.MANDATORY, TypeFactory.SINGLE),
					tf.createFcItfType(Constants.INTERCEPTOR_CONTROLLER,
							PAInterceptorController.class.getName(), TypeFactory.SERVER, TypeFactory.MANDATORY,
							TypeFactory.SINGLE)
			};

			ComponentType tComposite = tf.createFcType(new InterfaceType[] { tf.createFcItfType("runner",
					Runner.class.getName(), TypeFactory.SERVER, TypeFactory.MANDATORY, TypeFactory.SINGLE) },nfItfTypes);


			Component composite = gf.newFcInstance(tComposite, new ControllerDescription("composite",
					Constants.COMPOSITE), null);
			Utils.getPAMembraneController(composite).nfAddFcSubComponent(notfonc(tf,gf,null));
			return composite;
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}

	protected static final String COMPONENT_CONTROLLER_CONFIG = 
			"/org/objectweb/proactive/core/component/componentcontroller/config/default-component-controller-config.xml";

	protected static Component notfonc(PAGCMTypeFactory patf, PAGenericFactory pagf, Node node) {
		try {
			InterfaceType[] itfType = new InterfaceType[] {
					patf.createGCMItfType("nofci1", NoFCItf.class.getName(), TypeFactory.SERVER, TypeFactory.OPTIONAL, PAGCMTypeFactory.SINGLETON_CARDINALITY),
			};

			ComponentType t = patf.createFcType(itfType);

			return pagf.newNfFcInstance(t, 
					new ControllerDescription("nofci1", 
							Constants.PRIMITIVE, COMPONENT_CONTROLLER_CONFIG), 
					new ContentDescription(NoFCImpl.class.getName()), node);
		} catch (InstantiationException e) {
			e.printStackTrace();
			return null;
		}
	}


	///fin create composite

	@Override
	public Component createMasterMulticast() {
		Component master = null;
		try{
			ComponentType tMaster = tf.createFcType
					(new PAGCMInterfaceType[] {
							(PAGCMInterfaceType) tf.createGCMItfType(MasterImpl.ITF_CLIENT_M, ItfMulticast.class.getName(), CST.CLI, CST.MND, CST.MC),
							(PAGCMInterfaceType) tf.createGCMItfType("runner", Runner.class.getName(), CST.SRV, CST.MND,CST.SC),
							(PAGCMInterfaceType) tf.createGCMItfType(MasterImpl.ITF_CLIENT_2, fr.scale.gcm_scalable.a.learn.prog.commun.elements.Itf2.class.getName(), CST.CLI, CST.MND, CST.SC),
					});
			master = gf.newFcInstance(tMaster, new ControllerDescription("slave", Constants.PRIMITIVE),
					MasterImpl.class.getName());
		}catch(Exception e){
			e.printStackTrace();
		}

		return master;
	}

	@Override
	public Component createSlave1() {
		return createSlave();
	}

	@Override
	public Component createSlave2() {
		return createSlave();
	}


	private Component createSlave(){
		ComponentType tSlave;
		Component slave = null;
		try {
			tSlave = tf.createFcType(new InterfaceType[] { tf.createFcItfType("i1", Itf1.class
					.getName(), TypeFactory.SERVER, TypeFactory.MANDATORY, TypeFactory.SINGLE) });
			slave = gf.newFcInstance(tSlave, new ControllerDescription("slave", Constants.PRIMITIVE),
					SlaveImpl.class.getName());
		} catch (InstantiationException e) {
			e.printStackTrace();
		}
		return slave;

	}


	@Override
	public Component createElmt2() {
		ComponentType tSlave;
		Component slave = null;
		try {
			tSlave = tf.createFcType(new InterfaceType[] { 
					tf.createFcItfType(MasterImpl.ITF_CLIENT_2, fr.scale.gcm_scalable.a.learn.prog.commun.elements.Itf2.class
					.getName(), TypeFactory.SERVER, TypeFactory.MANDATORY, TypeFactory.SINGLE) });
			slave = gf.newFcInstance(tSlave, new ControllerDescription("elm2", Constants.PRIMITIVE),
					Elmt2Impl.class.getName());
		} catch (InstantiationException e) {
			e.printStackTrace();
		}
		return slave;

	}


	@Override
	public void start(Component composite) {
		try {
			CST.log("start membrane");    

			Utils.getPAMembraneController(composite).startMembrane();

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
	public void stop(Component composite) {
		try {
			System.out.println("stopmb");    
			Utils.getPAMembraneController(composite).stopMembrane();
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
}
