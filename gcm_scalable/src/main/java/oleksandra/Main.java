package oleksandra;

import java.util.ArrayList;
import java.util.List;

import org.etsi.uri.gcm.api.type.GCMTypeFactory;
import org.etsi.uri.gcm.util.GCM;
import org.objectweb.fractal.api.Component;
import org.objectweb.fractal.api.control.BindingController;
import org.objectweb.fractal.api.control.ContentController;
import org.objectweb.fractal.api.factory.GenericFactory;
import org.objectweb.fractal.api.factory.InstantiationException;
import org.objectweb.fractal.api.type.ComponentType;
import org.objectweb.fractal.api.type.InterfaceType;
import org.objectweb.fractal.api.type.TypeFactory;
import org.objectweb.proactive.core.component.Constants;
import org.objectweb.proactive.core.component.ControllerDescription;
import org.objectweb.proactive.core.component.Utils;

import fr.scale.gcm.scalable.simple.masterslave.Itf1;
import fr.scale.gcm.scalable.simple.masterslave.ItfRunner;
import fr.scale.gcm.scalable.simple.masterslave.MasterImpl;
import fr.scale.gcm.scalable.simple.masterslave.SlaveImpl;

public class Main {

	public static void main(String[] args) throws Exception {
		Component boot = Utils.getBootstrapComponent();
		GCMTypeFactory tf = GCM.getGCMTypeFactory(boot);
		GenericFactory gf = GCM.getGenericFactory(boot);
		ComponentType tComposite = tf.createFcType(new InterfaceType[] { tf.createFcItfType("runner",
		ItfRunner.class.getName(), TypeFactory.SERVER, TypeFactory.MANDATORY, TypeFactory.SINGLE) });
		// TODO: Create the Master Component type
		
		ComponentType tMaster = tf.createFcType(new InterfaceType[] {
				
				tf.createFcItfType("itf1", Itf1.class.getName(), TypeFactory.CLIENT, TypeFactory.MANDATORY,
						TypeFactory.SINGLE) ,
				tf.createFcItfType("runner", ItfRunner.class.getName(), TypeFactory.SERVER,
		TypeFactory.MANDATORY, TypeFactory.SINGLE)
				});
		
		ComponentType tSlave = tf.createFcType(new InterfaceType[] { 
				tf.createFcItfType("itf1", Itf1.class.getName(), TypeFactory.SERVER, TypeFactory.MANDATORY, 
						TypeFactory.SINGLE) });
		
		Component slave  = gf.newFcInstance(tSlave, new ControllerDescription("slave", Constants.PRIMITIVE), SlaveImpl.class.getName());
		// TODO: Create the Master Component
		Component master = gf.newFcInstance(tMaster,
		new ControllerDescription("master", Constants.PRIMITIVE), 
		MasterImpl.class.getName());
		
		Component composite = gf.newFcInstance(tComposite, new ControllerDescription("composite", Constants.COMPOSITE), null);
		// TODO: Add slave and master components to the composite component
		ContentController cc = GCM.getContentController(composite); 
		cc.addFcSubComponent(slave);
		cc.addFcSubComponent(master);
		// TODO: Do the bindings

		BindingController bcMaster = GCM.getBindingController(master); 
		bcMaster.bindFc("itf1", slave.getFcInterface("itf1"));
		master.getFcInterface("itf1");
		
		BindingController bcComposite = GCM.getBindingController(composite); 
		bcComposite.bindFc("runner", master.getFcInterface("runner"));
		
		GCM.getGCMLifeCycleController(composite).startFc();
		Thread.sleep(1000);

		GCM.getGCMLifeCycleController(composite).stopFc();
		bcMaster.unbindFc("itf1");
		
		Component slave2  = gf.newFcInstance(tSlave, new ControllerDescription("slave", Constants.PRIMITIVE), SlaveImpl.class.getName());
		cc.addFcSubComponent(slave2);
		
		bcMaster.bindFc("itf1", slave2.getFcInterface("itf1"));
		
		
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
		System.exit(0);
	}

}
