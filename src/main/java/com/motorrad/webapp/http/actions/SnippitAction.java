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

package com.motorrad.webapp.http.actions;

import com.motorrad.entity.KickstartSnippit;
import com.motorrad.persistence.AllSnippitDbQuery;
import com.motorrad.persistence.PersistenceService;
import com.motorrad.webapp.http.Action;
import com.motorrad.webapp.http.ActionID;
import com.motorrad.webapp.http.HttpContext;
import com.motorrad.webapp.http.toolkit.Resource;
import com.motorrad.webapp.http.views.Page;
import com.motorrad.webapp.service.Services;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SnippitAction implements Action {
    public static final ActionID ID = new ActionID() {
        @Override
        public String getName() {
            return "snippit";
        }

        @Override
        public Action makeAction(Services services) {
            return new SnippitAction(services.getPersistenceService());
        }
    };

    private PersistenceService persistenceService;

    public SnippitAction(PersistenceService persistenceService) {
        this.persistenceService = persistenceService;
    }


    @Override
    public Resource execute(HttpContext httpContext) throws IOException {
        List<KickstartSnippit> snippits = persistenceService.list(new AllSnippitDbQuery());
        Map<String, List<KickstartSnippit>> pageData = new HashMap<String, List<KickstartSnippit>>();
        pageData.put("set", snippits);
        return new Page("SnippitList.ftl", pageData);
    }
}
