package fr.scale.gcm.scalable.simple.masterslave;

import java.io.Serializable;
import java.util.List;

import org.objectweb.fractal.api.NoSuchInterfaceException;
import org.objectweb.fractal.api.control.BindingController;
import org.objectweb.fractal.api.control.IllegalBindingException;
import org.objectweb.fractal.api.control.IllegalLifeCycleException;


public class MasterImpl implements ItfRunner, BindingController,Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	public static String ITF_CLIENT = "itf1";
	

public Itf1Multicast i1;
//	public Itf1 i1;
	
	
	public void bindFc(String clientItfName, Object serverItf) throws NoSuchInterfaceException,
	IllegalBindingException, IllegalLifeCycleException {
		System.out.println("[LEA] >> Master Impl : binding : "+clientItfName);
		if (ITF_CLIENT.equals(clientItfName)) {
			System.out.println("binding "+(serverItf instanceof SlaveImpl));
			 i1 = (Itf1Multicast) serverItf;
			//i1 = (Itf1) serverItf;
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
	public void run(List<String> li ) {
		System.out.println("Run....");
		i1.compute(li);

		System.out.println("End Run...."+i1.getnb());
	}

	
}
