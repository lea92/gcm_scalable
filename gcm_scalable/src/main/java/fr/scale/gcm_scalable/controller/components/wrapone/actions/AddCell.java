package fr.scale.gcm_scalable.controller.components.wrapone.actions;

import org.etsi.uri.gcm.util.GCM;
import org.objectweb.fractal.api.Component;
import org.objectweb.proactive.core.component.Utils;
import org.objectweb.proactive.core.component.control.PAGCMLifeCycleController;
import org.objectweb.proactive.core.component.factory.PAGenericFactory;
import org.objectweb.proactive.core.component.type.PAGCMTypeFactory;
import org.objectweb.proactive.core.node.Node;
import org.objectweb.proactive.extensions.autonomic.controllers.execution.Action;

import examples.services.autoadaptable.AASCST;
import examples.services.autoadaptable.AASFactory;
import examples.services.autoadaptable.components.MasterAttributes;
import fr.scale.gcm_scalable.commun.CST;

public class AddCell extends Action{
	private Node node;


	public AddCell(Node node) {
		this.node = node;
	}

	@Override
	public Object execute(Component wrapone, PAGCMTypeFactory typeFactory, PAGenericFactory genericFactory) {

		// NOTE: assumed to be executed on Solver composite component

		try {
			Component slave = AASFactory.createSlave(node, typeFactory, genericFactory);
			Component ws    = Action.getBindComponent(wrapone, CST.WRAPSCALE);

			System.out.println("Stopping cell");
			PAGCMLifeCycleController lc = Utils.getPAGCMLifeCycleController(wrapone);
			lc.stopFc();

			System.out.println("add slave to solver");
			Utils.getPAContentController(wrapone).addFcSubComponent(slave);
			System.out.println("bind slave to master");
			Utils.getPABindingController(ws).bindFc(AASCST.SLAVE_MULTICAST, slave.getFcInterface(AASCST.SLAVE));
			System.out.println("setting the attributes");
		
			/*MasterAttributes masterAttr = (WSAttributes) GCM.getAttributeController(ws);

			long cellsNumber = (long) (masterAttr.getCellsNumber() + 1);
			masterAttr.setCellsNumber(cellsNumber);
			lc.startFc();*/

			return true;

		} catch (Exception e) {
			e.printStackTrace();
		}

		return false;
	}
}