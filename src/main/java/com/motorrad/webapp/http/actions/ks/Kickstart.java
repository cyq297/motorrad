package com.motorrad.webapp.http.actions.ks;

import com.google.inject.Singleton;
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

    @Inject
    public Kickstart(final Services services) {
    }

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String test(@Context HttpServletRequest req){
        return "hello " + req.getRemoteAddr();
    }

    @GET
    @Path("/roles/{role}")
    @Produces(MediaType.TEXT_PLAIN)
    public String getRole(@PathParam("role") String role){
        return "role is: " + role;
    }
}
