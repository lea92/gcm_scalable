package fr.scale.gcm_scalable.controller.interfaces;

import org.objectweb.fractal.api.control.AttributeController;

public interface UpdateMetricDelay extends AttributeController {
	
	public Long getUpdateMetricDelay();
	
	public void setUpdateMetricDelay(long metricDelay);
}
