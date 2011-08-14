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

package com.motorrad.webapp.http;

import com.motorrad.webapp.http.actions.*;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class ActionRegistry {
    private static final ActionID[] IDS = new ActionID[]{
            SnippitAction.ID,
            NewSnippitAction.ID,
            UpdateSnippitAction.ID
    };
    private final Map<String, ActionID> map;
    private final ActionID notFound;
    private final ActionID defaultAction;

    public static ActionRegistry defaultRegistry() {
        return new ActionRegistry(Arrays.asList(IDS), NotFoundAction.ID, HomeAction.ID);
    }

    public ActionRegistry(Collection<ActionID> actionIDs, ActionID notFound, ActionID defaultAction) {
        this.notFound = notFound;
        this.defaultAction = defaultAction;
        map = new HashMap<String, ActionID>();
        for (ActionID actionID : actionIDs) {
            map.put(actionID.getName(), actionID);
        }
    }

    public ActionID getDefaultAction() {
        return defaultAction;
    }

    public ActionID get(String name) {
        if (map.containsKey(name)) {
            return map.get(name);
        } else {
            return notFound;
        }
    }

}
