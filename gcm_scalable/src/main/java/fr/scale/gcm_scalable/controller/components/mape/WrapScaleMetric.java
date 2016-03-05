package fr.scale.gcm_scalable.controller.components.mape;

import org.objectweb.proactive.extensions.autonomic.controllers.monitoring.metrics.Metric;



/**
 * this class allows to keep the number of instance of one metric
 * @author lelbeze
 *
 */
public class WrapScaleMetric extends Metric<Integer>{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	protected static Integer nbInstance = 0;
	
	public WrapScaleMetric(){
		this.nbInstance++;
	}

	@Override
	public Integer calculate() {
		return nbInstance;
	}

	@Override
	public Integer getValue() {
		return nbInstance;
	}

	@Override
	public void setValue(Integer value) {
		nbInstance = value;
		
	}
}
