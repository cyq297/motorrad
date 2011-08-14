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

public abstract class QueryParser<T> {
    public abstract Bag<String, String> toParameters(T tee);

    public abstract T parse(Bag<String, String> parameters) throws InvalidQueryException;

    protected final Long requiredLong(Key key, Bag<String, String> parameters) throws InvalidQueryException {
        if (!parameters.containsKey(key.name())) {
            throw new InvalidQueryException(key);
        }

        return Long.parseLong(parameters.get(key.name()));
    }
}
