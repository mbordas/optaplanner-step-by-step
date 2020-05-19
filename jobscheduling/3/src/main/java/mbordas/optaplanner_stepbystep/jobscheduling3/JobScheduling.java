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

package mbordas.optaplanner_stepbystep.jobscheduling3;

import mbordas.optaplanner_stepbystep.jobscheduling3.domain.Schedule;
import mbordas.optaplanner_stepbystep.jobscheduling3.domain.Task;
import mbordas.optaplanner_stepbystep.jobscheduling3.domain.Worker;
import org.optaplanner.core.api.solver.Solver;
import org.optaplanner.core.api.solver.SolverFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class JobScheduling {

	static Task addNewTask(Map<Integer, Task> map, int charge, int dueDelay, int latePenalty, String capabilities) {
		Task newTask = new Task(charge, dueDelay, latePenalty, capabilities);
		map.put(newTask.getId(), newTask);
		return newTask;
	}

	public static void main(String[] args) {
		// Build the Solver
		SolverFactory<Schedule> solverFactory = SolverFactory.createFromXmlResource(
				"mbordas/optaplanner_stepbystep/jobscheduling3/score/scheduleCalculatorConfig.xml");
		Solver<Schedule> solver = solverFactory.buildSolver();

		Map<Integer, Task> tasks = new TreeMap<>();

		// Building preliminary tasks with no late penalty
		Task t0 = addNewTask(tasks, 4, 5, 0, Task.CAPABILITY_1);
		Task t1 = addNewTask(tasks, 3, 4, 0, Task.CAPABILITY_1);
		Task t2 = addNewTask(tasks, 5, 6, 4, Task.CAPABILITY_1); // Higher priority
		Task t3 = addNewTask(tasks, 6, 10, 3, Task.CAPABILITY_2); // Lower priority

		// Building tasks that depend on preliminary tasks
		Task t4 = addNewTask(tasks, 5,12,3, Task.CAPABILITY_2).setDependencies(t0);
		Task t5 = addNewTask(tasks, 4,14,2, Task.CAPABILITY_1).setDependencies(t4,t1);
		Task t6 = addNewTask(tasks, 4, 14, 2, Task.CAPABILITY_1);
		Task t7 = addNewTask(tasks, 5, 14, 3, Task.CAPABILITY_2);

		Worker worker1 = new Worker("W1", Task.CAPABILITY_1);
		Worker worker2 = new Worker("W2", Task.CAPABILITY_1, Task.CAPABILITY_2);
		Worker worker3 = new Worker("W3", Task.CAPABILITY_2);
		List<Worker> workers = new ArrayList<>();
		workers.add(worker1);
		workers.add(worker2);
		workers.add(worker3);

		worker2.addDaysOff(0);
		worker3.addDaysOff(2,3);

		Schedule unsolvedSchedule = new Schedule(new ArrayList<>(tasks.values()), workers);
		unsolvedSchedule.init();
		unsolvedSchedule.forceAllocation(t1, 0, worker1);

		// Solve the problem
		Schedule solvedSchedule = solver.solve(unsolvedSchedule);

		// Display the result
		System.out.println("\nSolution:\n" + solvedSchedule.toString());
	}
}
