package org.flowvisor.exceptions;

public class FlowEntryNotFound extends FVException {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	public FlowEntryNotFound(int id) {
		super(String.valueOf(id));
		// TODO Auto-generated constructor stub
	}

}
