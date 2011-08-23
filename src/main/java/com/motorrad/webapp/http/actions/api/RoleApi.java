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

package com.motorrad.webapp.http.actions.api;

import com.google.inject.Singleton;
import com.motorrad.entity.KickstartSnippit;
import com.motorrad.entity.Role;
import com.motorrad.persistence.PersistenceService;
import com.motorrad.webapp.service.Services;

import javax.inject.Inject;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Singleton
@Path("/role")
public class RoleApi {

    private PersistenceService persistenceService;

    @Inject
    public RoleApi(Services services) {
        this.persistenceService = services.getPersistenceService();
    }

    @POST
    @Path("/new")
    @Produces(MediaType.TEXT_PLAIN)
    public String newRole(@FormParam("name") String name, @FormParam("pre_snippits")List<KickstartSnippit> pre_snippits){
        Role role = new Role();
        Set<KickstartSnippit> allSnippits = new HashSet<KickstartSnippit> ();

        allSnippits.addAll(pre_snippits);

        role.setName(name);
        role.setSnippits(allSnippits);

        persistenceService.hibernate(role);

        return "Done!";
    }
}
