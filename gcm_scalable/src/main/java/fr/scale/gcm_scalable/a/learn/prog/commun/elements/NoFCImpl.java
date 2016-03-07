package fr.scale.gcm_scalable.a.learn.prog.commun.elements;

import org.objectweb.proactive.core.component.componentcontroller.AbstractPAComponentController;

public class NoFCImpl  extends  AbstractPAComponentController implements NoFCItf {

	@Override
	public void dosomething() {
		System.err.println("nothing");
	}

}
