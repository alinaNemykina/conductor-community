package com.netflix.conductor.sdk.testing;

import com.netflix.conductor.client.kotlin.http.TaskClient;
import com.netflix.conductor.client.kotlin.http.jersey.JerseyTaskClient;
import com.netflix.conductor.sdk.workflow.executor.WorkflowExecutor;
import com.netflix.conductor.sdk.workflow.executor.task.AnnotatedWorkerExecutor;

public class WorkflowTestRunner {

    private LocalServerRunner localServerRunner;

    private final AnnotatedWorkerExecutor annotatedWorkerExecutor;

    private final WorkflowExecutor workflowExecutor;

    public WorkflowTestRunner(String serverApiUrl) {

        TaskClient taskClient = new JerseyTaskClient();
        taskClient.setRootURI(serverApiUrl);
        this.annotatedWorkerExecutor = new AnnotatedWorkerExecutor(taskClient);

        this.workflowExecutor = new WorkflowExecutor(serverApiUrl);
    }

    public WorkflowTestRunner(int port, String conductorVersion) {

        localServerRunner = new LocalServerRunner(port, conductorVersion);

        TaskClient taskClient = new JerseyTaskClient();
        taskClient.setRootURI(localServerRunner.getServerAPIUrl());
        this.annotatedWorkerExecutor = new AnnotatedWorkerExecutor(taskClient);

        this.workflowExecutor = new WorkflowExecutor(localServerRunner.getServerAPIUrl());
    }

    public WorkflowExecutor getWorkflowExecutor() {
        return workflowExecutor;
    }

    public void init(String basePackages) {
        if (localServerRunner != null) {
            localServerRunner.startLocalServer();
        }
        annotatedWorkerExecutor.initWorkers(basePackages);
    }

    public void shutdown() {
        localServerRunner.shutdown();
        annotatedWorkerExecutor.shutdown();
        workflowExecutor.shutdown();
    }
}
