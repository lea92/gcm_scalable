package fr.scale.gcm_scalable.controller.components.wrapone.nf;

import org.etsi.uri.gcm.util.GCM;
import org.objectweb.fractal.api.Component;
import org.objectweb.proactive.core.component.Utils;
import org.objectweb.proactive.core.component.componentcontroller.AbstractPAComponentController;
import org.objectweb.proactive.core.component.control.PAContentController;
import org.objectweb.proactive.core.component.control.PAMembraneController;
import org.objectweb.proactive.core.component.identity.PAComponent;
import org.objectweb.proactive.extensions.autonomic.controllers.monitoring.MonitorController;
import org.objectweb.proactive.extensions.autonomic.controllers.remmos.Remmos;

import fr.scale.gcm_scalable.commun.CST;
import fr.scale.gcm_scalable.controller.interfaces.ItfCellController;
import fr.scale.gcm_scalable.controller.components.wrapone.MetricNbInstance;

public class CellControllerImpl extends  AbstractPAComponentController implements ItfCellController.Singleton {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	
	@Override
	public Boolean ping() {
		return true;
	}


	@Override
	public State getState() {
		return new State();
	}
	
	
}
