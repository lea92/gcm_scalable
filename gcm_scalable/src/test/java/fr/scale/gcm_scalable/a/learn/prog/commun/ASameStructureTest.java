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


	Component composite = null ;

	@Before
	public void before() throws ADLException {

		Map<String, Object> context = new HashMap<String, Object>();
		String adl = "fr.scale.gcm_scalable.a.learn.prog.commun.elements.adl.Composite";
		Factory f = org.objectweb.proactive.core.component.adl.FactoryFactory.getFactory();
		composite = (Component) f.newComponent(adl ,context);
		System.out.println(composite);	

	}

	@After
	public void after() {
	}


	@Test 
	public void existcomposite() throws ADLException{
		assertNotNull(composite);
	}
	
	@Test 
	public void start() throws ADLException{
		assertNotNull(composite);
	}


}
