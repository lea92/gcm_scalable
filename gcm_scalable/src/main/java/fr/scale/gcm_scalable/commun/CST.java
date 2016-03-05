package fr.scale.gcm_scalable.commun;

import org.objectweb.proactive.core.component.type.PAGCMTypeFactory;

/**
 * This class define the constante of program
 * @author lelbeze
 *
 */
public class CST {
	

	/**
	 * @see PAGCMTypeFactory.SINGLETON_CARDINALITY
	 */
	public static final String SC   = PAGCMTypeFactory.SINGLETON_CARDINALITY;
	public static final String MC   = PAGCMTypeFactory.MULTICAST_CARDINALITY;
	public static final boolean MND = PAGCMTypeFactory.MANDATORY;
	public static final boolean CLI = PAGCMTypeFactory.CLIENT;
	public static final boolean SRV = PAGCMTypeFactory.SERVER;
	
	/**
	 * name of logger
	 */
	public static final String APP_SCALE_LOGGER = "APP_SCALE_LOGGER";

	public static final String WRAPSCALE    = "wrapscale_";
	public static final String ScalabilityControlMulticast_ITF  = "addscale_multicast-itf";
	
	public static final String MANAGER_COMP_NAME = "manager-component";
	public static final String WRAPCOMPOSITE = "wrapcomposite_";
	public static final String MANAGER_SERVICE = "manager_service-itf";
	public static final String ItfGcmScaleInst = "gcm_scale_instance-itf";
	
	
	public static class WRAPONE{
		
		public static class CMP_NF{
			public static final String CELL_NF          = "cell-NF";
			public static final String CELLS_MANAGER_NF = "cells_manager_NF";
			
		}
		
		public static class METRICS {
			public static final String NBREALINST     = "nbrealinst";
			public static final String NBEXCEPTEDINST = "nbexceptedinst";
		}
	}
}
