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

import com.motorrad.util.Bag;

public class LongQueryParser extends QueryParser<Long> {
    private Key key;

    public LongQueryParser(Key key) {
        this.key = key;
    }

    @Override
    public Bag<String, String> toParameters(Long tee) {
        Bag<String, String> bag = new Bag<String, String>();
        bag.put(key.name(), Long.toString(tee));
        return bag;
    }

    @Override
    public Long parse(Bag<String, String> parameters) throws InvalidQueryException {
        return requiredLong(key, parameters);
    }
}
