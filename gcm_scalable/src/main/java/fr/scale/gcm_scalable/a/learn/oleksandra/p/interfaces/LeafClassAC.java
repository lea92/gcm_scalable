package fr.scale.gcm_scalable.a.learn.oleksandra.p.interfaces;

import org.objectweb.fractal.api.control.AttributeController;

public interface LeafClassAC extends AttributeController {
	public void set_myId(int newId);
	public int get_myId();
}
