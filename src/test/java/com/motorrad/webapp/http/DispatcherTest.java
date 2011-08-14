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
import com.motorrad.util.Bag;
import com.motorrad.webapp.http.toolkit.Resource;
import com.motorrad.webapp.service.Services;
import org.junit.Before;
import org.junit.Test;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.mock;


public class DispatcherTest extends BaseTestCase {
    @Before
    public void setUP() throws Exception {
        super.setUp();
    }

    @Test
    public void testDispatch() throws Exception {
        ActionRegistry actionRegistry = new ActionRegistry(Lists.newArrayList(makeMockActionID("testaction")), null, null);
        Dispatcher dispatcher = new Dispatcher(actionRegistry, mock(Services.class));
        dispatcher.dispatch("testaction", makeMockContext(), makeMockResponse());
        assertEventsSoFar(
                "creating action testaction",
                "execute action testaction",
                "setting content-type text/plain",
                "setting status code 200",
                "setting header foo to bar",
                "getting output stream",
                "rendering resource"
        );


    }

    @Test
    public void testDispatch404() throws Exception {
        ActionRegistry actionRegistry = new ActionRegistry(new ArrayList<ActionID>(), makeMockActionID("404"), null);
        Dispatcher dispatcher = new Dispatcher(actionRegistry, mock(Services.class));
        dispatcher.dispatch("unknown", makeMockContext(), makeMockResponse());
        assertEventsSoFar(
                "creating action 404",
                "execute action 404",
                "setting content-type text/plain",
                "setting status code 200",
                "setting header foo to bar",
                "getting output stream",
                "rendering resource"
        );

    }

    @Test
    public void testDispatchDefaulAction() throws Exception {
        ActionRegistry actionRegistry = new ActionRegistry(new ArrayList<ActionID>(), null, makeMockActionID("default"));
        Dispatcher dispatcher = new Dispatcher(actionRegistry, mock(Services.class));
        dispatcher.dispatch("", makeMockContext(), makeMockResponse());
        assertEventsSoFar(
                "creating action default",
                "execute action default",
                "setting content-type text/plain",
                "setting status code 200",
                "setting header foo to bar",
                "getting output stream",
                "rendering resource"
        );
    }

    // Mocks of actions and other resources

    private ActionID makeMockActionID(final String name) {
        return new ActionID() {

            @Override
            public String getName() {
                return name;
            }

            @Override
            public Action makeAction(Services services) {
                addEvent("creating action " + name);
                return makeMockAction(name);
            }
        };
    }

    private Action makeMockAction(final String name) {
        return new Action() {

            @Override
            public Resource execute(HttpContext httpContext) {
                addEvent("execute action " + name);
                return makeMockResource();
            }
        };
    }

    private HttpContext makeMockContext() {
        return new HttpContext(new Bag<String, String>(), "", null);
    }


    private Resource makeMockResource() {
        return new Resource() {

            @Override
            public String getContentType() {
                return "text/plain";
            }

            @Override
            public int getHttpStatus() {
                return HttpServletResponse.SC_OK;
            }

            @Override
            public void renderWithoutClosing(OutputStream out) throws IOException {
                addEvent("rendering resource");
            }

            @Override
            public Map<String, String> extraHeaders() {
                Map<String, String> map = new HashMap<String, String>();
                map.put("foo", "bar");
                return map;
            }
        };
    }

    private FlyweightHttpResponse makeMockResponse() {
        return new FlyweightHttpResponse(null) {

            @Override
            public void setContentType(String contentType) {
                addEvent("setting content-type " + contentType);
            }

            @Override
            public void setStatusCode(int statusCode) {
                addEvent("setting status code " + statusCode);
            }

            @Override
            public OutputStream getOutputStream() throws IOException {
                addEvent("getting output stream");
                return mock(OutputStream.class);
            }

            @Override
            public void setHeader(String key, String value) {
                addEvent("setting header " + key + " to " + value);
            }

        };
    }
}
