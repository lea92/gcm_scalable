package fr.scale.gcm_scalable.a.learn.adl;

import java.util.Map;

import org.objectweb.fractal.adl.ADLException;
import org.objectweb.fractal.api.Component;
import org.objectweb.proactive.core.component.adl.PABasicFactory;

public class ABasicFactory extends PABasicFactory implements AFactory {

	@Override
	@SuppressWarnings("rawtypes")
	public Object newComponent(String name, Map context) throws ADLException {
		Component comp = (Component) super.newComponent(name, context);
		return comp;
	}

	

	
}

