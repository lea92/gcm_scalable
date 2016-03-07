package fr.scale.gcm_scalable.a.learn.oleksandra.p.classes;

import fr.scale.gcm_scalable.a.learn.oleksandra.p.types.*;
import fr.scale.gcm_scalable.a.learn.oleksandra.p.interfaces.*;




import java.util.List;
import java.io.Serializable;
import org.objectweb.proactive.Body;
import org.objectweb.proactive.core.component.body.ComponentRunActive;
import org.objectweb.fractal.api.control.AttributeController;
import org.objectweb.proactive.multiactivity.component.ComponentMultiActiveService;
import org.objectweb.proactive.core.component.Utils;

public class CompositeDefaultClass implements Serializable, ComponentRunActive, AttributeController{




 	@Override
	public void runComponentActivity(Body body) {
		ComponentMultiActiveService multiActiveService = new ComponentMultiActiveService(body);
		while (body.isActive()) {
			multiActiveService.multiActiveServing();
		};
	}



}

