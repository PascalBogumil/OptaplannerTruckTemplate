<?xml version="1.0" encoding="UTF-8"?>
<plannerBenchmark>
  <benchmarkDirectory>local/data/cheaptime</benchmarkDirectory>
  <!--<parallelBenchmarkCount>AUTO</parallelBenchmarkCount>-->

  <inheritedSolverBenchmark>
    <problemBenchmarks>
      <solutionFileIOClass>org.optaplanner.examples.cheaptime.persistence.CheapTimeSolutionFileIO</solutionFileIOClass>
      <inputSolutionFile>data/cheaptime/import/instance00</inputSolutionFile>
      <inputSolutionFile>data/cheaptime/import/instance01</inputSolutionFile>
      <inputSolutionFile>data/cheaptime/import/instance02</inputSolutionFile>
      <inputSolutionFile>data/cheaptime/import/instance03</inputSolutionFile>
      <inputSolutionFile>data/cheaptime/import/instance04</inputSolutionFile>
      <inputSolutionFile>data/cheaptime/import/instance05</inputSolutionFile>
      <inputSolutionFile>data/cheaptime/import/instance06</inputSolutionFile>
      <inputSolutionFile>data/cheaptime/import/instance07</inputSolutionFile>
      <inputSolutionFile>data/cheaptime/import/instance08</inputSolutionFile>
      <inputSolutionFile>data/cheaptime/import/instance09</inputSolutionFile>
      <inputSolutionFile>data/cheaptime/import/instance10</inputSolutionFile>
      <inputSolutionFile>data/cheaptime/import/instance11</inputSolutionFile>
      <inputSolutionFile>data/cheaptime/import/instance12</inputSolutionFile>
      <inputSolutionFile>data/cheaptime/import/instance13</inputSolutionFile>
      <inputSolutionFile>data/cheaptime/import/instance14</inputSolutionFile>
      <inputSolutionFile>data/cheaptime/import/instance15</inputSolutionFile>
      <inputSolutionFile>data/cheaptime/import/instance16</inputSolutionFile>
      <inputSolutionFile>data/cheaptime/import/instance17</inputSolutionFile>
      <inputSolutionFile>data/cheaptime/import/instance18</inputSolutionFile>
      <inputSolutionFile>data/cheaptime/import/instance19</inputSolutionFile>
      <inputSolutionFile>data/cheaptime/import/instance20</inputSolutionFile>
      <inputSolutionFile>data/cheaptime/import/instance21</inputSolutionFile>
      <inputSolutionFile>data/cheaptime/import/instance22</inputSolutionFile>
      <inputSolutionFile>data/cheaptime/import/instance23</inputSolutionFile>
      <inputSolutionFile>data/cheaptime/import/instance24</inputSolutionFile>
      <inputSolutionFile>data/cheaptime/import/instance25</inputSolutionFile>
      <inputSolutionFile>data/cheaptime/import/instance26</inputSolutionFile>
      <inputSolutionFile>data/cheaptime/import/instance27</inputSolutionFile>
      <inputSolutionFile>data/cheaptime/import/instance28</inputSolutionFile>
      <inputSolutionFile>data/cheaptime/import/instance29</inputSolutionFile>
      <inputSolutionFile>data/cheaptime/import/instance30</inputSolutionFile>
      <inputSolutionFile>data/cheaptime/import/instance31</inputSolutionFile>
      <inputSolutionFile>data/cheaptime/import/instance32</inputSolutionFile>
      <inputSolutionFile>data/cheaptime/import/instance33</inputSolutionFile>
      <inputSolutionFile>data/cheaptime/import/instance34</inputSolutionFile>
      <inputSolutionFile>data/cheaptime/import/instance35</inputSolutionFile>
      <inputSolutionFile>data/cheaptime/import/instance36</inputSolutionFile>
      <inputSolutionFile>data/cheaptime/import/instance37</inputSolutionFile>
      <inputSolutionFile>data/cheaptime/import/instance38</inputSolutionFile>
      <inputSolutionFile>data/cheaptime/import/instance39</inputSolutionFile>
      <inputSolutionFile>data/cheaptime/import/instance40</inputSolutionFile>
      <inputSolutionFile>data/cheaptime/import/instance41</inputSolutionFile>
      <inputSolutionFile>data/cheaptime/import/instance42</inputSolutionFile>
      <inputSolutionFile>data/cheaptime/import/instance43</inputSolutionFile>
      <inputSolutionFile>data/cheaptime/import/instance44</inputSolutionFile>
      <inputSolutionFile>data/cheaptime/import/instance45</inputSolutionFile>
      <inputSolutionFile>data/cheaptime/import/instance46</inputSolutionFile>
      <inputSolutionFile>data/cheaptime/import/instance47</inputSolutionFile>
      <inputSolutionFile>data/cheaptime/import/instance48</inputSolutionFile>
      <inputSolutionFile>data/cheaptime/import/instance49</inputSolutionFile>
    </problemBenchmarks>

    <solver>
      <solutionClass>org.optaplanner.examples.cheaptime.domain.CheapTimeSolution</solutionClass>
      <entityClass>org.optaplanner.examples.cheaptime.domain.TaskAssignment</entityClass>
      <scoreDirectorFactory>
        <incrementalScoreCalculatorClass>org.optaplanner.examples.cheaptime.score.CheapTimeIncrementalScoreCalculator</incrementalScoreCalculatorClass>
        <initializingScoreTrend>ONLY_DOWN</initializingScoreTrend>
      </scoreDirectorFactory>
      <termination>
        <minutesSpentLimit>5</minutesSpentLimit>
      </termination>
    </solver>
  </inheritedSolverBenchmark>

  <solverBenchmark>
    <name>FFFD - LA (400) TS (0.3)</name>
    <solver>
      <constructionHeuristic>
        <constructionHeuristicType>FIRST_FIT_DECREASING</constructionHeuristicType>
        <forager>
          <pickEarlyType>FIRST_FEASIBLE_SCORE_OR_NON_DETERIORATING_HARD</pickEarlyType>
        </forager>
      </constructionHeuristic>
      <localSearch>
        <unionMoveSelector>
          <changeMoveSelector>
            <valueSelector variableName="startPeriod"/>
          </changeMoveSelector>
          <changeMoveSelector>
            <valueSelector variableName="machine"/>
          </changeMoveSelector>
          <swapMoveSelector/>
        </unionMoveSelector>
        <acceptor>
          <lateAcceptanceSize>400</lateAcceptanceSize>
          <entityTabuRatio>0.3</entityTabuRatio>
        </acceptor>
        <forager>
          <acceptedCountLimit>2</acceptedCountLimit>
        </forager>
      </localSearch>
    </solver>
  </solverBenchmark>
</plannerBenchmark>
