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

package com.motorrad.webapp.http.actions.ks;

import com.google.inject.Singleton;
import com.motorrad.entity.Role;
import com.motorrad.persistence.PersistenceService;
import com.motorrad.webapp.service.Services;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

@Singleton
@Path("/")
public class Kickstart {

    PersistenceService persistenceService;

    @Inject
    public Kickstart(final Services services) {
        persistenceService = services.getPersistenceService();
    }

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String test(@Context HttpServletRequest req) {
        return "hello " + req.getRemoteAddr();
    }

    @GET
    @Path("/roles/{id}")
    @Produces(MediaType.TEXT_PLAIN)
    public String getRole(@PathParam("id") long id) {
        Role role = persistenceService.get(Role.class, id);
        return role.toString();
    }
}