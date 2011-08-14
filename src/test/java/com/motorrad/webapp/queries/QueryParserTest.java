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

package com.motorrad.webapp.queries;

import com.motorrad.BaseTestCase;
import com.motorrad.Fallible;
import com.motorrad.util.Bag;
import org.junit.Test;

public class QueryParserTest extends BaseTestCase {
    @Test
    public void testRequiredString() throws Exception {
        final QueryParser<String> parser = new QueryParser<String>() {
            @Override
            public String parse(Bag<String, String> parameters) throws InvalidQueryException {
                return requiredString(Key.id, parameters);
            }

            public Bag<String, String> toParameters(String tee) {
                return new Bag<String, String>();
            }
        };

        assertFailure(InvalidQueryException.class, new Fallible() {
            public void execute() throws Exception {
                parser.parse(new Bag<String, String>());
            }
        });
    }
}