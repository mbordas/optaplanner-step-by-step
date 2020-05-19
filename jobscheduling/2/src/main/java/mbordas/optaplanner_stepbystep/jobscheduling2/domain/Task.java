/*Copyright (c) 2020-2020, Mathieu Bordas
All rights reserved.

Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:

1- Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
2- Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.
3- Neither the name of the copyright holder nor the names of its contributors may be used to endorse or promote products derived from this software without specific prior written permission.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package mbordas.optaplanner_stepbystep.jobscheduling2.domain;

import java.util.ArrayList;
import java.util.List;

public class Task {

	public static final String CAPABILITY_1 = "C1";
	public static final String CAPABILITY_2 = "C2";

	private static int nextId = 0;

	int id;
	int duration;
	int dueDelay;
	int latePenalty;

	List<Task> dependencies = new ArrayList<>();
	List<String> requiredCapabilities = new ArrayList<>();

	public Task(int duration, int dueDelay, int latePenalty, String...requiredCapabilities) {
		this.id = nextId++;
		this.duration = duration;
		this.dueDelay = dueDelay;
		this.latePenalty = latePenalty;
		for(String capability : requiredCapabilities) {
			this.requiredCapabilities.add(capability);
		}
	}

	public int getId() {
		return id;
	}

	public Task setDependencies(Task...tasks) {
		for(Task task : tasks) {
			dependencies.add(task);
		}
		return this;
	}

	public boolean dependsOn(Task other) {
		return dependencies.contains(other);
	}

	public List<Task> getDependencies() {
		return dependencies;
	}

	public int getDuration() {
		return duration;
	}

	public int dueDelay() {
		return dueDelay;
	}

	public int getPenalty(int start) {
		return Math.max(0, start + duration - dueDelay) * latePenalty;
	}

	public boolean hasRequiredCapabilities(Worker worker) {
		for(String capability : requiredCapabilities) {
			if(!worker.hasCapability(capability)) {
				return false;
			}
		}
		return true;
	}

	@Override
	public String toString() {
		String result = String.format("#%d %dd !%d", id, duration, dueDelay);

		result += " cap:";
		for(String requiredCapability : requiredCapabilities) {
			result += requiredCapability + ",";
		}

		result += " dep:";
		for(Task task : dependencies) {
			result += "#" + task.id + ",";
		}

		return result;
	}

}