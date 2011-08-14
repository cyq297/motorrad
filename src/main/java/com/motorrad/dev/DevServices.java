/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.motorrad.dev;

import com.motorrad.persistence.HibernateConfiguration;
import com.motorrad.persistence.HibernatePersistenceService;
import com.motorrad.persistence.PersistenceService;
import com.motorrad.util.Configuration;
import com.motorrad.webapp.service.Services;

import java.io.IOException;

public class DevServices implements Services {
    private Configuration configuration;
    private PersistenceService persistenceService;

    public DevServices(Configuration configuration) throws IOException {
        this.configuration = configuration;
        this.persistenceService = new HibernatePersistenceService(new HibernateConfiguration());
    }

    @Override
    public Configuration getConfiguration() {
        return configuration;
    }

    @Override
    public PersistenceService getPersistenceService() {
        return persistenceService;
    }
}
