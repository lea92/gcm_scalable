package fr.scale.gcm_scalable.controller.interfaces;

import java.util.List;

import fr.scale.gcm_scalable.controller.components.wrapone.nf.State;

public interface ItfCellController {
	
	public static final String ITF_NAME = "itf_cell_controller";
	
	
	
	
	public static interface Singleton extends ItfCellController {
		
		public static final String ITF_NAME = ItfCellController.ITF_NAME+"_singleton";

		public Boolean ping();
		
		public State getState();
	}
	
	public static interface Multicast extends ItfCellController {

		public static final String ITF_NAME = ItfCellController.ITF_NAME+"_multicast";
		
		public List<Boolean> ping();
		
		public List<State> getState();
		
	}
}
