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

	private List<String> skills = new ArrayList<>();

	public Worker(String name, String... skills) {
		this.name = name;
		for(String skill : skills) {
			this.skills.add(skill);
		}
	}

	public boolean hasSkill(String skill) {
		return skills.contains(skill);
	}

	public String getName() {
		return name;
	}

	@Override
	public int compareTo(Worker worker) {
		return this.name.compareTo(worker.name);
	}
}