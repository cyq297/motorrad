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

package com.motorrad;

import com.google.common.base.Joiner;
import com.motorrad.util.Log;
import junit.framework.TestCase;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class BaseTestCase extends TestCase {

    private List<String> events;

    public void setUp() throws Exception {
        events = new ArrayList<String>();
        System.setProperty("log4j.configuration", "log4jdev-console.properties");
    }

    protected final void addEvent(String s) {
        events.add(s);
    }

    protected final void assertEventsSoFar(String... expected) {
        assertEquals("Did not get expected events", Joiner.on("\n").join(expected), Joiner.on("\n").join(this.events));
    }

    protected final void assertFailure(Class<? extends Throwable> expectedException, Fallible fallible) {
        try {
            fallible.execute();
        } catch (Throwable t) {
            if (!expectedException.isAssignableFrom(t.getClass())) {
                fail("Got a " + t.getClass().getSimpleName() + ", should have gotten a " + expectedException.getSimpleName());
            }

            return;
        }

        fail("Should have gotten an exception!");
    }

    protected static void assertNotEquals(Object left, Object right) {
        if (left.equals(right)) {
            fail("Arguments are expected to not be equal");
        }
    }

    protected final void assertEventsSoFarAndClear(String... expected) {
        assertEquals("Did not get expected events", Joiner.on("\n").join(expected), Joiner.on("\n").join(this.events));
        this.events = new ArrayList<String>();
    }


    protected final void log(String message) {
        Log.info(this, message);
    }

    @Test
    public void testBTC() {
        assertTrue(true);
    }
}
