package fr.scale.gcm_scalable.controller.components.wrapone.nf;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Stream;

import javax.swing.JOptionPane;

import org.objectweb.fractal.api.Component;
import org.objectweb.fractal.api.NoSuchInterfaceException;
import org.objectweb.fractal.api.control.BindingController;
import org.objectweb.fractal.api.control.IllegalBindingException;
import org.objectweb.fractal.api.control.IllegalLifeCycleException;
import org.objectweb.proactive.core.component.componentcontroller.AbstractPAComponentController;
import org.objectweb.proactive.core.component.identity.PAComponent;
import org.objectweb.proactive.extensions.autonomic.controllers.monitoring.MonitorController;
import org.objectweb.proactive.extensions.autonomic.controllers.remmos.Remmos;

import fr.scale.gcm_scalable.commun.CST;
import fr.scale.gcm_scalable.controller.interfaces.IScalabilityControl;
import fr.scale.gcm_scalable.controller.interfaces.ItfCellController;
import fr.scale.gcm_scalable.controller.interfaces.ItfCellsManagerController;
import fr.scale.gcm_scalable.controller.interfaces.UpdateMetricDelay;

/**
 * inside the membrane
 * @author lelbeze
 *
 */
public class CellsManagerControllerImpl extends AbstractPAComponentController implements ItfCellsManagerController, BindingController,   Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ItfCellController.Multicast cells;
	
	public long delayUpdateMetric = 30000;
	
 
	

	public void bindFc(String clientItfName, Object serverItf) throws NoSuchInterfaceException,
	IllegalBindingException, IllegalLifeCycleException {
		if (ItfCellController.Multicast.ITF_NAME.equals(clientItfName)) {
			cells = (ItfCellController.Multicast) serverItf;
		} else {
			throw new NoSuchInterfaceException(clientItfName);
		}
	}

	public Object lookupFc(String clientItfName) throws NoSuchInterfaceException {
		if (ItfCellController.Multicast.ITF_NAME.equals(clientItfName)) {
			return cells;
		} else {
			throw new NoSuchInterfaceException(clientItfName);
		}
	}

	public void unbindFc(String clientItfName) throws NoSuchInterfaceException, IllegalBindingException,
	IllegalLifeCycleException {
		if (ItfCellController.Multicast.ITF_NAME.equals(clientItfName)) {
			cells = null;
		} else {
			throw new NoSuchInterfaceException(clientItfName);
		}
	}

	@Override
	public String[] listFc() {
		return new String[]{ ItfCellController.Multicast.ITF_NAME};
	}


	@Override
	public void updateMetrics() {
		try {
			PAComponent comp = this.hostComponent;
			//IScalabilityControl itf1 = (IScalabilityControl) comp.getFcInterface(CST.ScalabilityControlMulticast_ITF);
			
			List<Boolean> testsping = cells.ping();
			int nbInstReal = 0;
			
			for(Boolean testping : testsping){
				if(testping) nbInstReal++;
			}
			final MonitorController monitor  = Remmos.getMonitorController(hostComponent);
			monitor.setMetricValue(CST.WRAPONE.METRICS.NBREALINST, nbInstReal);
		
			JOptionPane.showInputDialog(CST.WRAPONE.METRICS.NBREALINST+" "+nbInstReal);
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	/*
	@Override
	public Long getUpdateMetricDelay() {
		return delayUpdateMetric;
	}

	@Override
	public void setUpdateMetricDelay(long metricDelay) {
		delayUpdateMetric = metricDelay;
	}*/

}
