package fr.scale.gcm_scalable.controller.components.wrapone;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.objectweb.proactive.extensions.autonomic.controllers.monitoring.metrics.Metric;
import org.objectweb.proactive.extensions.autonomic.controllers.utils.Wrapper;

public class MetricNbInstance extends Metric<Integer>{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	int nbInstance = 0;
	
	

	@Override
	public Integer getValue() {
		return nbInstance;
	}

	@Override
	public void setValue(Integer value) {
		nbInstance = value;
	}

	@Override
	public Integer calculate() {
	//Nothing
		return nbInstance;
	}

}
