package com.ivanminyaev;

import io.smallrye.common.annotation.Blocking;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/online")
public class OnlineResource {

    @Inject
    OnlinePresence presence;

    @GET
    @Blocking
    @Produces(MediaType.TEXT_PLAIN)
    public int online() {
        return presence.count();
    }
}
