package com.endava.automation.atf.utils;

import com.endava.automation.atf.manager.FileReaderManager;
import lombok.extern.log4j.Log4j2;

@Log4j2
public final class JUnitPropertiesManager {

    private JUnitPropertiesManager() {
        // utility class
    }

    public static void configureParallelExecution() {

        var cfg =
                FileReaderManager.getInstance()
                        .getConfigFileReader();

        boolean parallelExecution =
                cfg.isParallelExecution();

        int threadCount =
                cfg.getThreadCount();

        // =================================================
        // ENABLE / DISABLE PARALLEL EXECUTION
        // =================================================

        System.setProperty(
                "cucumber.execution.parallel.enabled",
                String.valueOf(parallelExecution)
        );

        // =================================================
        // PARALLEL STRATEGY
        // =================================================

        System.setProperty(
                "cucumber.execution.parallel.config.strategy",
                "fixed"
        );

        // =================================================
        // THREAD COUNT
        // =================================================

        System.setProperty(
                "cucumber.execution.parallel.config.fixed.parallelism",
                String.valueOf(threadCount)
        );

        // =================================================
        // EXECUTION MODE
        // =================================================
        // parallel     -> scenarios/features run in parallel
        // same_thread  -> sequential execution
        // =================================================

        System.setProperty(
                "cucumber.execution.execution-mode.scenario",
                parallelExecution
                        ? "parallel"
                        : "same_thread"
        );

        // =================================================
        // OPTIONAL:
        // Scenario-level parallel execution
        // Uncomment if needed later
        // =================================================

        /*
        System.setProperty(
                "cucumber.execution.execution-mode.scenario",
                parallelExecution
                        ? "parallel"
                        : "same_thread"
        );
        */

        // =================================================
        // JUNIT PARALLEL EXECUTION
        // =================================================

        System.setProperty(
                "junit.jupiter.execution.parallel.enabled",
                String.valueOf(parallelExecution)
        );

        // =================================================
        // LOG CONFIGURATION
        // =================================================

        log.info("""
                
                =====================================================
                JUNIT / CUCUMBER PARALLEL CONFIGURATION
                =====================================================
                Parallel Execution : {}
                Thread Count       : {}
                Execution Mode     : {}
                =====================================================
                """,
                parallelExecution,
                threadCount,
                parallelExecution
                        ? "parallel"
                        : "same_thread"
        );
    }
}