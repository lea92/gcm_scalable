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
package fr.scale.gcm_scalable.a.learn.commun.elements;

import java.util.List;

import org.objectweb.fractal.api.NoSuchInterfaceException;
import org.objectweb.fractal.api.control.BindingController;
import org.objectweb.fractal.api.control.IllegalBindingException;
import org.objectweb.fractal.api.control.IllegalLifeCycleException;


/**
 * @author The ProActive Team
 */
public class MasterImpl implements Runner, BindingController {
  //  public static String ITF_CLIENT = "i1";
    public static String ITF_CLIENT_M = "i1";
    public static String ITF_CLIENT_2 = "i2";
   // private Itf1 i1;
    private ItfMulticast im1;
    
    private fr.scale.gcm_scalable.a.learn.commun.elements.Itf2 i2;

    public void run(List<String> arg) {
        im1.compute(arg);
    }

    public void bindFc(String clientItfName, Object serverItf) throws NoSuchInterfaceException,
            IllegalBindingException, IllegalLifeCycleException {
    	System.err.println("bindFc");
        if (ITF_CLIENT_2.equals(clientItfName)) {
            i2 = (Itf2) serverItf;
        }else if (ITF_CLIENT_M.equals(clientItfName)) {
            im1 = (ItfMulticast) serverItf;
        }  else {
            throw new NoSuchInterfaceException(clientItfName);
        }
    }

    public String[] listFc() {
        return new String[] { ITF_CLIENT_M, ITF_CLIENT_2 };
    }

    public Object lookupFc(String clientItfName) throws NoSuchInterfaceException {
    	System.err.println("lookupFc");
    	 if (ITF_CLIENT_2.equals(clientItfName)) {
            return i2;
        }else if (ITF_CLIENT_M.equals(clientItfName)) {
            return im1;
        }  else {
            throw new NoSuchInterfaceException(clientItfName);
        }
    }

    public void unbindFc(String clientItfName) throws NoSuchInterfaceException, IllegalBindingException,
            IllegalLifeCycleException {
    	System.err.println("unbindFc");
        if (ITF_CLIENT_2.equals(clientItfName)) {
            i2 = null;
        }else 
    	if (ITF_CLIENT_M.equals(clientItfName)) {
            im1 = null;
        } else {
            throw new NoSuchInterfaceException(clientItfName);
        }
    }

	@Override
	public void run2(String arg) {
		i2.coucou(arg);
	}
}
