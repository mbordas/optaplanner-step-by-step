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

package mbordas.optaplanner_stepbystep.jobscheduling.score;

import mbordas.optaplanner_stepbystep.jobscheduling.domain.Allocation;
import mbordas.optaplanner_stepbystep.jobscheduling.domain.Schedule;
import org.optaplanner.core.api.score.buildin.hardsoft.HardSoftScore;
import org.optaplanner.core.impl.score.director.easy.EasyScoreCalculator;

public class ScheduleEasyScoreCalculator implements EasyScoreCalculator<Schedule> {

	@Override
	public HardSoftScore calculateScore(Schedule schedule) {
		int hardScore = 0;
		int softScore = 0;

		for(Allocation allocation : schedule.getAllocationList()) {
			// penalty for being late is soft score
			softScore -= allocation.getPenalty();

			// simultaneous tasks makes hard score
			int start = allocation.getStart();
			int end = allocation.getEnd();
			for(Allocation otherAllocation : schedule.getAllocationList()) {
				if(allocation == otherAllocation) {
					continue;
				}
				if(otherAllocation.getTask() == allocation.getTask()) {
					hardScore -= 100;
				} else {
					int collision = otherAllocation.getCollision(start, end);
					hardScore -= collision;
				}
			}
		}

		return HardSoftScore.of(hardScore, softScore);
	}
}