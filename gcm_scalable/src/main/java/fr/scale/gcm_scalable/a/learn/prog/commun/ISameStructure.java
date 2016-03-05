package fr.scale.gcm_scalable.a.learn.prog.commun;

import org.objectweb.fractal.api.Component;

public interface ISameStructure {
	
	//creation d'un composite
	public Component createComposite();
	
	//creation master multicast
	public Component createMasterMulticast();
	
	//creation slave 1
	public Component createSlave1();
	
	//creation slave 2
	public Component createSlave2();
	
	//creation slave 2
	public Component createElmt2();
	


	public void stop(Component composite);

	public void start(Component composite);

}
