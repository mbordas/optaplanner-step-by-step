/*
 * Copyright (c) 2012-2020 Smart Grid Energy
 * All Right Reserved
 * http://www.smartgridenergy.fr
 */

package mbordas.optaplanner_stepbystep.jobscheduling2.domain;

import java.util.ArrayList;
import java.util.List;

public class Worker implements Comparable<Worker> {

	private String name;

	private List<String> capabilities = new ArrayList<>();

	public Worker(String name, String...capabilities) {
		this.name = name;
		for(String capability : capabilities) {
			this.capabilities.add(capability);
		}
	}

	public boolean hasCapability(String capability) {
		return capabilities.contains(capability);
	}

	public String getName() {
		return name;
	}

	@Override
	public int compareTo(Worker worker) {
		return this.name.compareTo(worker.name);
	}
}