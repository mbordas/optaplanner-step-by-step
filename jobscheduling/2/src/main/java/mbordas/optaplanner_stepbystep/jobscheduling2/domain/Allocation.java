/*Copyright (c) 2010-2012, Mathieu Bordas
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

import org.optaplanner.core.api.domain.entity.PlanningEntity;
import org.optaplanner.core.api.domain.variable.PlanningVariable;

@PlanningEntity
public class Allocation implements Comparable<Allocation> {

	private static int nextId = 0;

	private int id;
	private Task task;
	private Integer start;

	public Allocation() {
		id = nextId++;
	}

	public Allocation(Task task, int start) {
		id = nextId++;
		this.task = task;
		this.start = start;
	}

	public int getId() {
		return id;
	}

	@PlanningVariable(valueRangeProviderRefs = { "taskRange" })
	public Task getTask() {
		return task;
	}

	public void setTask(Task task) {
		this.task = task;
	}

	@PlanningVariable(valueRangeProviderRefs = { "startRange" })
	public Integer getStart() {
		return start;
	}

	public void setStart(Integer start) {
		this.start = start;
	}

	public int getPenalty() {
		return task.getPenalty(start);
	}

	public int getEnd() {
		return getStart() + task.charge;
	}

	public int getCollision(int start, int end) {
		if(end <= this.start || start >= getEnd()) {
			return 0;
		} else {
			int collision = task.charge; // Maximum value
			if(end < getEnd()) {
				collision -= getEnd() - end;
			}
			if(start > this.start) {
				collision -= start - this.start;
			}
			return collision;
		}
	}

	@Override
	public String toString() {
		return String.format("alloc #%d Task #%d %dd [%d-%d[", id, task.id, task.charge, start, getEnd());
	}

	@Override
	public int compareTo(Allocation other) {
		int result = this.start - other.start;
		return result == 0 ? this.task.id - other.task.id : result;
	}
}