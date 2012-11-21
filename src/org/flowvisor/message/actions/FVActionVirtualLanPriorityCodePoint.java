package org.flowvisor.message.actions;

import java.util.Iterator;
import java.util.List;

import org.flowvisor.classifier.FVClassifier;
import org.flowvisor.exceptions.ActionDisallowedException;
import org.flowvisor.flows.FlowEntry;
import org.flowvisor.flows.SliceAction;
import org.flowvisor.log.FVLog;
import org.flowvisor.log.LogLevel;
import org.flowvisor.openflow.protocol.FVMatch;
import org.flowvisor.slicer.FVSlicer;
import org.openflow.protocol.OFMatch;
import org.openflow.protocol.OFError.OFBadActionCode;
import org.openflow.protocol.action.OFAction;
import org.openflow.protocol.action.OFActionVirtualLanPriorityCodePoint;

public class FVActionVirtualLanPriorityCodePoint extends
		OFActionVirtualLanPriorityCodePoint implements SlicableAction {

	@Override
	public void slice(List<OFAction> approvedActions, OFMatch match,
			FVClassifier fvClassifier, FVSlicer fvSlicer)
			throws ActionDisallowedException {
		FVMatch neoMatch = new FVMatch(match);
		match.setDataLayerVirtualLanPriorityCodePoint(this.virtualLanPriorityCodePoint);
		List<FlowEntry> flowEntries = fvClassifier.getSwitchFlowMap().matches(fvClassifier.getDPID(), neoMatch);
		for (FlowEntry fe : flowEntries) {
			Iterator<OFAction> it = fe.getActionsList().iterator();
			while (it.hasNext()) {
				if (it.next() instanceof SliceAction) {
					SliceAction action = (SliceAction) it.next();
					if (action.getSliceName().equals(fvSlicer.getSliceName())) {
						FVLog.log(LogLevel.DEBUG, fvSlicer, "Approving " + this + 
								" for " + match);
						approvedActions.add(this);
					}
				}
			}
		}
		throw new ActionDisallowedException(
				"Slice " + fvSlicer.getSliceName() + " may not rewrite vlan " +
				"priority to " + this.getVirtualLanPriorityCodePoint(), 
				OFBadActionCode.OFPBAC_BAD_ARGUMENT);
	}
}

