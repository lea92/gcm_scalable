/*
 * ################################################################
 *
 * ProActive Parallel Suite(TM): The Java(TM) library for
 *    Parallel, Distributed, Multi-Core Computing for
 *    Enterprise Grids & Clouds
 *
 * Copyright (C) 1997-2012 INRIA/University of
 *                 Nice-Sophia Antipolis/ActiveEon
 * Contact: proactive@ow2.org or contact@activeeon.com
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Affero General Public License
 * as published by the Free Software Foundation; version 3 of
 * the License.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
 * USA
 *
 * If needed, contact us to obtain a release under GPL Version 2 or 3
 * or a different license than the AGPL.
 *
 *  Initial developer(s):               The ProActive Team
 *                        http://proactive.inria.fr/team_members.htm
 *  Contributor(s):
 *
 * ################################################################
 * $$PROACTIVE_INITIAL_DEV$$
 */
package org.objectweb.proactive.examples.userguide.components.api.interfaces;

import java.util.ArrayList;
import java.util.List;

import org.etsi.uri.gcm.api.type.GCMTypeFactory;
import org.etsi.uri.gcm.util.GCM;
import org.objectweb.fractal.api.Component;
import org.objectweb.fractal.api.control.BindingController;
import org.objectweb.fractal.api.control.ContentController;
import org.objectweb.fractal.api.factory.GenericFactory;
import org.objectweb.fractal.api.factory.InstantiationException;
import org.objectweb.fractal.api.type.ComponentType;
import org.objectweb.fractal.api.type.InterfaceType;
import org.objectweb.fractal.api.type.TypeFactory;
import org.objectweb.proactive.core.component.Constants;
import org.objectweb.proactive.core.component.ContentDescription;
import org.objectweb.proactive.core.component.ControllerDescription;
import org.objectweb.proactive.core.component.Utils;
import org.objectweb.proactive.core.component.factory.PAGenericFactory;
import org.objectweb.proactive.core.component.identity.PAComponent;
import org.objectweb.proactive.core.component.type.PAGCMInterfaceType;
import org.objectweb.proactive.core.component.type.PAGCMTypeFactory;
import org.objectweb.proactive.core.node.Node;
import org.objectweb.proactive.extensions.autonomic.controllers.remmos.Remmos;

import fr.scale.gcm.scalable.simple.masterslave.ItfRunner;
import fr.scale.gcm_scalable.commun.CST;
import fr.scale.gcm_scalable.controller.components.ManagerImpl;
import fr.scale.gcm_scalable.controller.components.wrapone.nf.CellsManagerControllerImpl;
import fr.scale.gcm_scalable.controller.interfaces.IService;
import fr.scale.gcm_scalable.controller.interfaces.ItfCellController;
import fr.scale.gcm_scalable.controller.interfaces.ItfCellsManagerController;


/**
 * @author The ProActive Team
 */
public class Main {

	
	
    public static void main(String[] args) throws Exception {
        Component boot = Utils.getBootstrapComponent();

        PAGCMTypeFactory tf = Utils.getPAGCMTypeFactory(boot);
        PAGenericFactory gf = Utils.getPAGenericFactory(boot);
        Remmos remmos = new Remmos(tf, gf); 
        
        PAGCMInterfaceType[] tcomposite = new PAGCMInterfaceType[] {
				(PAGCMInterfaceType)tf.createGCMItfType("runner", Runner2.class.getName(), PAGCMTypeFactory.SERVER,
						PAGCMTypeFactory.MANDATORY, 
						PAGCMTypeFactory.SINGLETON_CARDINALITY),

		};

        PAComponent composite = (PAComponent) remmos.newFcInstance(
				remmos.createFcType(tcomposite, Constants.COMPOSITE),
				new ControllerDescription("composite", Constants.COMPOSITE),
				null,
				null);
       
        ComponentType tMaster = remmos.createFcType(new PAGCMInterfaceType[] {
        		(PAGCMInterfaceType)tf.createGCMItfType("runner", Runner2.class.getName(), PAGCMTypeFactory.SERVER,
        				PAGCMTypeFactory.MANDATORY, PAGCMTypeFactory.SINGLETON_CARDINALITY),
        		(PAGCMInterfaceType)tf.createGCMItfType("i1", Itf1.class.getName(), PAGCMTypeFactory.CLIENT, PAGCMTypeFactory.MANDATORY,
        				PAGCMTypeFactory.SINGLETON_CARDINALITY)//,
                //tf.createFcItfType("i2", Itf2.class.getName(), TypeFactory.CLIENT, TypeFactory.MANDATORY,
                //        TypeFactory.SINGLE)
                //TODO: Add the new client interface
                }, Constants.PRIMITIVE);
        ComponentType tSlave = remmos.createFcType(new PAGCMInterfaceType[] {
        		(PAGCMInterfaceType)tf.createGCMItfType("i1", Itf1.class.getName(), PAGCMTypeFactory.SERVER, PAGCMTypeFactory.MANDATORY,
        				PAGCMTypeFactory.SINGLETON_CARDINALITY)
                //,
                //tf.createFcItfType("i2", Itf2.class.getName(), TypeFactory.SERVER, TypeFactory.MANDATORY,
                //        TypeFactory.SINGLE)
                //TODO: Add the new server interface
                }, Constants.PRIMITIVE);
        PAComponent slave =(PAComponent) remmos.newFcInstance(tSlave, 
        		new ControllerDescription("slave", Constants.PRIMITIVE),
                new ContentDescription(SlaveImpl.class.getName()),null);
        Component master = remmos.newFcInstance(tMaster,
                new ControllerDescription("master", Constants.PRIMITIVE),
                new ContentDescription(MasterImpl.class.getName()),null);
        



        ContentController cc = Utils.getPAContentController(composite);
        cc.addFcSubComponent(slave);
        cc.addFcSubComponent(master);

        Utils.getPABindingController(composite).bindFc("runner", master.getFcInterface("runner"));
        Utils.getPABindingController(master).bindFc("i1", slave.getFcInterface("i1"));
        //bcMaster.bindFc("i2", slave.getFcInterface("i2"));
        // TODO: Do the binding for the new interface
        Utils.getPAMembraneController(composite).nfAddFcSubComponent(notfonc(tf,gf,null));
        
        //start the membrane before start job
        Utils.getPAMembraneController(master).startMembrane();
        Utils.getPAMembraneController(slave).startMembrane();
        Utils.getPAMembraneController(composite).startMembrane();
        

        System.out.println("before start");
        Utils.getPAGCMLifeCycleController(composite).startFc();

        System.out.println("after start");
        Runner2 runner = (Runner2) composite.getFcInterface("runner");
        
        Itf1 i1 = (Itf1) slave.getFcInterface("i1");
        List<String> arg = new ArrayList<String>();
        arg.add("hello");
        arg.add("world");
        i1.compute(arg);
        
        //called non fonc
        Itf2 i2 = (Itf2) Utils.getPAMembraneController(composite).nfGetFcSubComponent("i2").getFcInterface("i2");
        i2.doNothing();

       // GCM.getGCMLifeCycleController(master).stopFc();
        Thread.sleep(1000); 
        GCM.getGCMLifeCycleController(composite).stopFc();
        
        System.exit(0);
    }
    

	protected static final String COMPONENT_CONTROLLER_CONFIG = 
			"/org/objectweb/proactive/core/component/componentcontroller/config/default-component-controller-config.xml";

    
    protected static Component notfonc(PAGCMTypeFactory patf, PAGenericFactory pagf, Node node) {
		try {
			InterfaceType[] itfType = new InterfaceType[] {
					patf.createGCMItfType("i2", Itf2.class.getName(), TypeFactory.SERVER, TypeFactory.OPTIONAL, PAGCMTypeFactory.SINGLETON_CARDINALITY),
			};

			ComponentType t = patf.createFcType(itfType);

			return pagf.newNfFcInstance(t, 
					new ControllerDescription("i2", 
							Constants.PRIMITIVE, COMPONENT_CONTROLLER_CONFIG), 
					new ContentDescription(Itf2Impl.class.getName()), node);
		} catch (InstantiationException e) {
			e.printStackTrace();
			return null;
		}
	}
}
