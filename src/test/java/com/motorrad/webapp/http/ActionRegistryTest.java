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

import com.google.common.collect.Lists;
import com.motorrad.BaseTestCase;
import org.junit.Test;

import java.util.ArrayList;

import static org.mockito.Mockito.mock;

public class ActionRegistryTest extends BaseTestCase {
    @Test
    public void testResolve() throws Exception {
        ActionID foo = makeActionId("foo");
        ActionID bar = makeActionId("bar");
        ActionID notFound = makeActionId("404");
        ActionRegistry actionRegistry = new ActionRegistry(Lists.newArrayList(foo, bar), notFound, mock(ActionID.class));
        assertTrue(actionRegistry.get("foo") == foo);
        assertTrue(actionRegistry.get("bar") == bar);
        assertTrue(actionRegistry.get("bafoon") == notFound);
    }

    @Test
    public void testDefaultAction() throws Exception {
        ActionID defaultAction = makeActionId("foo");
        ActionRegistry actionRegistry = new ActionRegistry(new ArrayList<ActionID>(), mock(ActionID.class), defaultAction);
        assertTrue(defaultAction == actionRegistry.getDefaultAction());
    }
}