/*
 * Copyright 2017 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.jbpm.services.task.wih;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.drools.core.impl.EnvironmentFactory;
import org.jbpm.services.task.HumanTaskServiceFactory;
import org.jbpm.services.task.test.TestStatefulKnowledgeSession;
import org.jbpm.test.persistence.util.PersistenceUtil;
import org.junit.After;
import org.junit.Before;
import org.kie.api.runtime.process.WorkItemHandler;
import org.kie.internal.task.api.InternalTaskService;
import org.kie.test.util.db.PoolingDataSourceWrapper;

public class HTWorkItemHandlerTest extends HTWorkItemHandlerBaseTest {

    private EntityManagerFactory emf;
    private PoolingDataSourceWrapper pds;
    
    @Before
    public void setUp() throws Exception {
    	pds = setupPoolingDataSource();
        emf = Persistence.createEntityManagerFactory("org.jbpm.services.task");
        ksession = new TestStatefulKnowledgeSession();
        env = EnvironmentFactory.newEnvironment();
        ksession.setEnvironment(env);
        this.taskService = HumanTaskServiceFactory.newTaskServiceConfigurator()
				.entityManagerFactory(emf)
				.getTaskService();
        WorkItemHandler htWorkItemHandler = new NonManagedLocalHTWorkItemHandler(ksession, taskService);
 
        setHandler(htWorkItemHandler);
    }

    @After
    public void tearDown() throws Exception {
        ((InternalTaskService)taskService).removeAllTasks();
		if (emf != null) {
			emf.close();
		}
		if (pds != null) {
			pds.close();
		}
    }

    protected PoolingDataSourceWrapper setupPoolingDataSource() {
        return PersistenceUtil.setupPoolingDataSource("jdbc/jbpm-ds");
    }
}
