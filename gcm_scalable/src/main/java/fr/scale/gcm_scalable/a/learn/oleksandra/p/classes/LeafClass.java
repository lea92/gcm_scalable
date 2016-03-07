package fr.scale.gcm_scalable.a.learn.oleksandra.p.classes;

import fr.scale.gcm_scalable.a.learn.oleksandra.p.types.*;
import fr.scale.gcm_scalable.a.learn.oleksandra.p.interfaces.*;

import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.Arrays;

import org.objectweb.fractal.api.NoSuchInterfaceException;
import org.objectweb.fractal.api.control.BindingController;
import org.objectweb.fractal.api.control.IllegalBindingException;
import org.objectweb.fractal.api.control.IllegalLifeCycleException;
import org.objectweb.proactive.core.util.wrapper.IntWrapper;
import org.objectweb.proactive.core.util.wrapper.BooleanWrapper;


import org.objectweb.proactive.core.component.componentcontroller.AbstractPAComponentController;
import org.etsi.uri.gcm.util.GCM;

import java.util.List;
import java.io.Serializable;
import org.objectweb.proactive.Body;
import org.objectweb.proactive.core.component.body.ComponentRunActive;
import org.objectweb.fractal.api.control.AttributeController;
import org.objectweb.proactive.multiactivity.component.ComponentMultiActiveService;
import org.objectweb.proactive.core.component.Utils;

public class LeafClass implements Serializable, BindingController, LeafClassAC, 
ComponentRunActive, ISingle{

	public int myId;

	@Override
	public void bindFc(String myClientItf, Object serverItf) throws NoSuchInterfaceException, IllegalBindingException, IllegalLifeCycleException {
		switch(myClientItf) {
		default:
			throw new NoSuchInterfaceException(myClientItf);
		}
	}


	@Override
	public String[] listFc() {
		return new String[0];
	}
	@Override
	public Object lookupFc(String myClientItf) throws NoSuchInterfaceException {
		switch(myClientItf) {
		default:
			throw new NoSuchInterfaceException(myClientItf);
		}
	}


	@Override
	public void unbindFc(String myClientItf) throws NoSuchInterfaceException {
		switch(myClientItf) {
		default:
			throw new NoSuchInterfaceException(myClientItf);
		}
	}


 	@Override
	public void runComponentActivity(Body body) {
		ComponentMultiActiveService multiActiveService = new ComponentMultiActiveService(body);
		while (body.isActive()) {
			multiActiveService.multiActiveServing();
		};
	}

	public  void set_myId(int newId) {
		this.myId = newId;
	}
	
	public  int get_myId() {
		return this.myId;
	}
	
	public  void addSubcomp(Integer parentId) {
		System.out.println("You accessed a component with ID: " + this.myId + " with an argument " + parentId);
		//return new BooleanWrapper(true);
	}

}

