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
package fr.scale.gcm_scalable.controller.components.wrapone.nf;

import java.io.Serializable;
import java.lang.management.ManagementFactory;
import java.lang.reflect.Field;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Date;


public class State implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Val commitedMemory =
			new Val("Commited memory",
					ManagementFactory.getMemoryMXBean().getHeapMemoryUsage().getCommitted(),
					"bytes");

	private Val initMemory =
			new Val("Initial memory requested",ManagementFactory.getMemoryMXBean().getHeapMemoryUsage().getInit()
					,"bytes");

	private Val maxMemory =
			new Val("Maximum memory available",ManagementFactory.getMemoryMXBean().getHeapMemoryUsage().getMax(),"bytes")
			;

	private Val usedMemory =
			new Val("Used memory",ManagementFactory.getMemoryMXBean().getHeapMemoryUsage().getUsed(),"bytes");

	private Val  operatingsystem = 
			new Val("Operating System",
					ManagementFactory.getOperatingSystemMXBean().getArch()+" "+
							ManagementFactory.getOperatingSystemMXBean().getVersion()+" "+
							ManagementFactory.getOperatingSystemMXBean().getAvailableProcessors()
							,"");


	private Val liveThreads = 
			new Val("Processors", ManagementFactory.getThreadMXBean().getThreadCount(),"");
	private Val startedThreads =
			new Val("Current live threads",ManagementFactory.getThreadMXBean().getTotalStartedThreadCount(),"");
	private Val peakThreads = 
			new Val("Peak number of live threads",ManagementFactory.getThreadMXBean().getPeakThreadCount(),"");
	private Val deamonThreads = 
			new Val("Current daemon threads", ManagementFactory.getThreadMXBean().getDaemonThreadCount(),"");

	private Date timePoint = new Date();
	private String hostname;
	{
		try {
			hostname = InetAddress.getLocalHost().toString();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}




	public State() {
	}


	public static class Val{
		String name;
		Object val;
		String measure;

		public Val(String name, Object val, String measure){
			this.name = name;
			this.val  = val;
			this.measure = measure;
		}
	}

	public String toString() {

		StringBuilder res = new StringBuilder("");
		try {
			int maxsize = 0;
			Field[] fields = this.getClass().getDeclaredFields();
			ArrayList<Val> listval = new ArrayList<Val>();
			
			for(Field f : fields){
				Object o;
				o = f.get(this);

				if(o instanceof Val){
					Val v = (Val) o;
					int tmp = v.name.length();
					if(maxsize<tmp) maxsize = tmp;
					
					listval.add(v);
				}
			}
			
			int maxsizestr = 0;
			ArrayList<String> liststr = new ArrayList<String>();
			for(Val val : listval){
				int taillestr = val.name.length();
				String space = "";
				int nbspace = maxsize-taillestr;
				for(int i=0;i<nbspace;i++){
					space += " ";
				}
				String str = val.name+space+" | "+val.val.toString()+" "+val.measure;
				if(maxsizestr<str.length()) maxsizestr = str.length();
				liststr.add(str);
				
			}
			
			String trait = "";
			for(int i=0;i<maxsizestr;i++){
				trait += "-";
			}
			res.append(" "+trait+"\n");
			for(String str : liststr){

				String space = "";
				int nbspace = maxsizestr-str.length();
				for(int i=0;i<nbspace;i++){
					space += " ";
				}
				res.append("|"+str+space+"|\n");
				
				res.append("|"+trait+"|\n");
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return res.toString();
	}

	public static void main(String ...strings ){
		System.out.println(new State().toString());
	}
}
