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
package fr.scale.gcm_scalable.a.learn.api.withmembrane;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.etsi.uri.gcm.api.type.GCMTypeFactory;
import org.etsi.uri.gcm.util.GCM;
import org.objectweb.fractal.api.Component;
import org.objectweb.fractal.api.control.BindingController;
import org.objectweb.fractal.api.control.ContentController;
import org.objectweb.fractal.api.control.NameController;
import org.objectweb.fractal.api.factory.GenericFactory;
import org.objectweb.fractal.api.factory.InstantiationException;
import org.objectweb.fractal.api.type.ComponentType;
import org.objectweb.fractal.api.type.InterfaceType;
import org.objectweb.fractal.api.type.TypeFactory;
import org.objectweb.proactive.core.component.Constants;
import org.objectweb.proactive.core.component.ContentDescription;
import org.objectweb.proactive.core.component.ControllerDescription;
import org.objectweb.proactive.core.component.Utils;
import org.objectweb.proactive.core.component.adl.FactoryFactory;
import org.objectweb.proactive.core.component.control.PABindingController;
import org.objectweb.proactive.core.component.control.PAContentController;
import org.objectweb.proactive.core.component.control.PAGCMLifeCycleController;
import org.objectweb.proactive.core.component.control.PAInterceptorController;
import org.objectweb.proactive.core.component.control.PAMembraneController;
import org.objectweb.proactive.core.component.factory.PAGenericFactory;
import org.objectweb.proactive.core.component.type.PAGCMInterfaceType;
import org.objectweb.proactive.core.component.type.PAGCMTypeFactory;
import org.objectweb.proactive.core.node.Node;
import org.objectweb.proactive.examples.userguide.components.api.interfaces.Itf2;
import org.objectweb.proactive.examples.userguide.components.api.interfaces.Itf2Impl;

import examples.services.autoadaptable.AASCST;
import examples.services.autoadaptable.components.MasterAttributes;
import examples.services.autoadaptable.components.SlaveMulticast;
import examples.services.autoadaptable.components.Solver;
import fr.scale.gcm_scalable.a.learn.commun.MainCommun;
import fr.scale.gcm_scalable.a.learn.commun.elements.Itf1;
import fr.scale.gcm_scalable.a.learn.commun.elements.ItfMulticast;
import fr.scale.gcm_scalable.a.learn.commun.elements.MasterImpl;
import fr.scale.gcm_scalable.a.learn.commun.elements.Runner;
import fr.scale.gcm_scalable.a.learn.commun.elements.SlaveImpl;



/**
 * we want to create with the help of api
 * one composite, one master, two slaves
 * the composite has an interface runner
 * the multicast master is binding to two slaves
 * thanks to interface i1
 * 
 * We want start launch the compute stop and start launch the compute
 * to verify if it works...
 * 
 * @author The ProActive Team
 */
public class Main extends MainCommun{

	public static void main(String[] args) throws Exception {
		WithMembraneStructure struct = new WithMembraneStructure();
		struct.run();
	}


}
