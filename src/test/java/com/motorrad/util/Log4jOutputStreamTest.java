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

package com.motorrad.util;

import com.motorrad.BaseTestCase;
import org.junit.Test;

import java.io.PrintWriter;

public class Log4jOutputStreamTest extends BaseTestCase {
    @Test
    public void testLog() throws Exception {
        Log4jOutputStream out = new Log4jOutputStream(this.getClass());
        PrintWriter pw = new PrintWriter(out);
        pw.println("Hello, world");
        pw.println("Testing logging");
        pw.flush();
    }
}