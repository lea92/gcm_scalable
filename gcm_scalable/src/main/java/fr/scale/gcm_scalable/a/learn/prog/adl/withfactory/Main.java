package fr.scale.gcm_scalable.a.learn.prog.adl.withfactory;

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

import fr.scale.gcm_scalable.a.learn.adl.MyFactoryFactory;
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
		System.out.println("Main with adl and factory");
		MembFacStruct struct = new MembFacStruct();
		struct.run();
	}

	
	public static void main(String[] args) {
		try {
			runwithoutunomic(args);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	

}
