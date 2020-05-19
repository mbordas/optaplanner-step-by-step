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

package mbordas.optaplanner_stepbystep.jobscheduling3.domain;

import org.optaplanner.core.api.domain.solution.PlanningEntityCollectionProperty;
import org.optaplanner.core.api.domain.solution.PlanningScore;
import org.optaplanner.core.api.domain.solution.PlanningSolution;
import org.optaplanner.core.api.domain.solution.drools.ProblemFactCollectionProperty;
import org.optaplanner.core.api.domain.valuerange.CountableValueRange;
import org.optaplanner.core.api.domain.valuerange.ValueRangeFactory;
import org.optaplanner.core.api.domain.valuerange.ValueRangeProvider;
import org.optaplanner.core.api.score.buildin.hardsoft.HardSoftScore;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@PlanningSolution
public class Schedule {

	private List<Task> taskList = new ArrayList<>();
	private List<Allocation> allocationList = new ArrayList<>();
	private List<Worker> workerList = new ArrayList<>();

	private List<Allocation> lockedAllocationList = new ArrayList<>();

	private HardSoftScore score;

	public Schedule() {
	}

	public Schedule(List<Task> taskList, List<Worker> workers) {
		this.taskList = taskList;
		this.workerList = workers;
	}

	public void init() {
		allocationList.clear();
		Worker firstWorker = workerList.get(0);
		for(Task task : taskList) {
			allocationList.add(new Allocation(task, 0, firstWorker));
		}
	}

	public void forceAllocation(Task task, int start, Worker worker) {
		lockedAllocationList.add(new Allocation(task, start, worker));
	}

	@PlanningEntityCollectionProperty
	public List<Allocation> getAllocationList() {
		return allocationList;
	}

	public void setAllocationList(List<Allocation> allocationList) {
		this.allocationList = allocationList;
	}

	@ProblemFactCollectionProperty
	public List<Allocation> getLockedAllocationList() {
		return lockedAllocationList;
	}

	@ProblemFactCollectionProperty
	@ValueRangeProvider(id = "taskRange")
	public List<Task> getTaskList() {
		return taskList;
	}

	public void setTaskList(List<Task> taskList) {
		this.taskList = taskList;
	}

	@ProblemFactCollectionProperty
	@ValueRangeProvider(id = "workerRange")
	public List<Worker> getWorkerList(){
		return workerList;
	}

	@ValueRangeProvider(id = "startRange")
	public CountableValueRange<Integer> getStartRange() {
		int max = 0;
		for(Task task : getTaskList()) {
			max += task.duration;
		}
		return ValueRangeFactory.createIntValueRange(0, max * 2);
	}

	@PlanningScore
	public HardSoftScore getScore() {
		return score;
	}

	public void setScore(HardSoftScore score) {
		this.score = score;
	}

	@Override
	public String toString() {
		StringBuilder result = new StringBuilder();

		// Printing input summary
		result.append("Input\n");
		for(Task task : this.getTaskList()) {
			result.append(task.toString() + "\n");
		}

		List<Allocation> allocations = new ArrayList<>();
		allocations.addAll(allocationList);
		allocations.addAll(lockedAllocationList);

		int firstDay = Integer.MAX_VALUE;
		int lastDay = Integer.MIN_VALUE;
		for(Allocation allocation : allocations) {
			firstDay = Math.min(firstDay, allocation.getStart());
			lastDay = Math.max(lastDay, allocation.getEnd());
		}
		final int days = lastDay - firstDay;

		// Grouping allocations by worker
		Map<Worker, Task[]> tasksByWorker = new TreeMap<>();
		for(Allocation allocation : allocations) {
			Worker worker = allocation.getWorker();
			Task[] workerTasks = tasksByWorker.get(worker);
			if(workerTasks == null) {
				workerTasks = new Task[days];
				tasksByWorker.put(worker, workerTasks);
			}

			for(int day = allocation.getStart(); day < allocation.getEnd(); day++) {
				if(worker.worksThatDay(day)) {
					workerTasks[day - firstDay] = allocation.getTask();
				}
			}
		}

		result.append(String.format("\nBest solution takes %d days\n", days));
		for(Map.Entry<Worker, Task[]> entry : tasksByWorker.entrySet()) {
			Worker worker = entry.getKey();
			Task[] tasks = entry.getValue();
			StringBuilder line = new StringBuilder(worker.getName() + "\t");
			for(int i = 0; i < tasks.length; i++) {
				Task task = tasks[i];
				if(!worker.worksThatDay(i)) {
					if(task == null) {
						line.append(" x ");
					} else {
						line.append(" ! "); // A task is assigned a day off
					}
				} else {
					if(task == null) {
						line.append("   ");
					} else {
						line.append(String.format(" %d ", task.id));
					}
				}
			}
			result.append(line.toString() + "\n");
		}

		return result.toString();
	}
}
