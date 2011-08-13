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

import com.motorrad.webapp.http.Action;
import com.motorrad.webapp.http.ActionID;
import com.motorrad.webapp.http.HttpContext;
import com.motorrad.webapp.http.toolkit.Resource;
import com.motorrad.webapp.http.views.Page;
import com.motorrad.webapp.service.Services;

import java.io.IOException;

public class HomeAction implements Action {
    public static final ActionID ID = new ActionID() {
        @Override
        public String getName() {
            return "home";
        }

        @Override
        public Action makeAction(Services services) {
            return new HomeAction();
        }
    };

    public HomeAction() {

    }


    @Override
    public Resource execute(HttpContext httpContext) throws IOException {
        return new Page("HomePage.ftl");
    }
}
