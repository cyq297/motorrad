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


import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.motorrad.entity.KickstartSnippit;
import com.motorrad.entity.KickstartSnippitType;
import com.motorrad.persistence.PersistenceService;
import com.motorrad.webapp.service.Services;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Singleton
@Path("/snippit")
public class SnippitApi {

    private PersistenceService persistenceService;

    @Inject
    public SnippitApi(Services services) {
        this.persistenceService = services.getPersistenceService();
    }

    @POST
    @Path("/new")
    @Produces(MediaType.TEXT_PLAIN)
    public String newSnippit(@FormParam("name") String name, @FormParam("snippit") String snippitText, @FormParam("type") KickstartSnippitType type) {
        KickstartSnippit snippit = new KickstartSnippit();

        snippit.setName(name);
        snippit.setSnippit(snippitText);
        snippit.setType(type);

        persistenceService.hibernate(snippit);
        return "Done! Snippit name: " + snippit.getName() + " snippit: " + snippit.getSnippit() + " type: " + snippit.getType();
    }

    @POST
    @Path("/update/{id}")
    @Produces(MediaType.TEXT_PLAIN)
    public String updateSnippit(@PathParam("id") long id, @FormParam("name") String name, @FormParam("snippit") String snippitText, @FormParam("type") KickstartSnippitType type) {
        KickstartSnippit snippit = persistenceService.get(KickstartSnippit.class, id);

        snippit.setName(name);
        snippit.setSnippit(snippitText);
        snippit.setType(type);

        persistenceService.update(snippit);

        return "Done! Updated Snippit name: " + snippit.getName() + " snippit:" + snippit.getSnippit();
    }

    @POST
    @Path("/delete/{id}")
    @Produces(MediaType.TEXT_PLAIN)
    public String deleteSnippit(@PathParam("id") long id) {
        if (persistenceService.delete(KickstartSnippit.class, id)) {
            return "Done deleted id: " + id;
        } else {
            return "problem deleting id: " + id;
        }
    }
}
