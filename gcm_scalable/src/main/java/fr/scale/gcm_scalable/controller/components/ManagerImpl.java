package fr.scale.gcm_scalable.controller.components;

import java.io.Serializable;
import java.util.List;

import org.objectweb.fractal.api.NoSuchInterfaceException;
import org.objectweb.fractal.api.control.BindingController;
import org.objectweb.fractal.api.control.IllegalBindingException;
import org.objectweb.fractal.api.control.IllegalLifeCycleException;

import fr.scale.gcm.scalable.simple.masterslave.Itf1Multicast;
import fr.scale.gcm.scalable.simple.masterslave.SlaveImpl;
import fr.scale.gcm_scalable.commun.CST;
import fr.scale.gcm_scalable.controller.interfaces.IScalabilityControlMulticast;
import fr.scale.gcm_scalable.controller.interfaces.IService;
import fr.scale.gcm_scalable.controller.interfaces.ManagerAttributes;

public class ManagerImpl implements  IService, ManagerAttributes, BindingController, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public static String ITF_CLIENT = CST.ScalabilityControlMulticast_ITF;
	
	public IScalabilityControlMulticast i1;
	
	public void bindFc(String clientItfName, Object serverItf) throws NoSuchInterfaceException,
	IllegalBindingException, IllegalLifeCycleException {
		System.out.println("binding : "+clientItfName);
		if (ITF_CLIENT.equals(clientItfName)) {
			System.out.println("binding "+(serverItf instanceof SlaveImpl));
			i1 = (IScalabilityControlMulticast) serverItf;
		} else {
			throw new NoSuchInterfaceException(clientItfName);
		}
	}
	
	public Object lookupFc(String clientItfName) throws NoSuchInterfaceException {
		if (ITF_CLIENT.equals(clientItfName)) {
			return i1;
		} else {
			throw new NoSuchInterfaceException(clientItfName);
		}
	}

	public void unbindFc(String clientItfName) throws NoSuchInterfaceException, IllegalBindingException,
	IllegalLifeCycleException {
		if (ITF_CLIENT.equals(clientItfName)) {
			i1 = null;
		} else {
			throw new NoSuchInterfaceException(clientItfName);
		}
	}

	@Override
	public String[] listFc() {
		return new String[]{ ITF_CLIENT};
	}

	@Override
	public void run() {
		System.out.println(">> Manager Run....");
		i1.increase();
		
		i1.getInfo();
		
		System.out.println(">> Manager End Run....");
	}
	
}
