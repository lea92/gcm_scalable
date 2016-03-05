package org.objectweb.proactive.examples.userguide.components.api.interfaces;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.objectweb.fractal.api.NoSuchInterfaceException;
import org.objectweb.proactive.core.component.componentcontroller.AbstractPAComponentController;
import org.objectweb.proactive.core.component.identity.PAComponent;


public class Itf2Impl extends  AbstractPAComponentController implements  Itf2,Serializable{

	@Override
	public void doNothing() {
		System.out.println("nonfonc");
		PAComponent comp = this.hostComponent;
		try{
		Runner2 r = (Runner2) comp.getFcInterface("runner");
		List<String> li = new ArrayList<String>();
		li.add("lea");
		r.run(li);
		}catch  ( NoSuchInterfaceException e) {
			e.printStackTrace();
		}
	}

}
