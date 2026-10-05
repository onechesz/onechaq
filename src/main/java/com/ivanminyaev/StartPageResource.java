package com.ivanminyaev;

import io.quarkus.qute.Template;
import io.quarkus.qute.TemplateInstance;
import io.smallrye.common.annotation.Blocking;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/")
public class StartPageResource {

    @Inject
    Template index;

    @Inject
    OnlinePresence presence;

    @GET
    @Blocking
    @Transactional
    @Produces(MediaType.TEXT_HTML)
    public TemplateInstance index() {
        return index.data("messages", Message.find("order by createdAt nulls first, id").list())
                .data("online", presence.count());
    }
}
