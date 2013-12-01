package net.codelab.ui.rest.index;

import net.codelab.core.entity.dto.Course;
import net.codelab.core.entity.dto.Inproceedings;
import net.codelab.core.entity.dto.ResultsDTO;
import net.codelab.core.service.index.CourseIndexService;
import net.codelab.core.service.index.InproceedingsIndexService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;


/**
 * Created by Melzarek on 21/11/13.
 */

@Component
@Path("/index/inproceedings")
public class InproceedingsRestIndex {

    @Autowired
    private InproceedingsIndexService inproceedingsIndexService;


    public InproceedingsRestIndex()
    {

    }

    @GET
    @Path("/search")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response getCourseData(@QueryParam("query") String query)
    {
        ResultsDTO<Inproceedings> result = null;
        try {
            result = inproceedingsIndexService.getInproceedingByTitle(query);
        } catch (IOException e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(result).build();
        }
        return Response.status(Response.Status.OK).entity(result).build();
    }

    @POST
    @Path("/createindex")
    @Consumes(MediaType.TEXT_PLAIN)
    public  Response createIndex()
    {
        try {
            inproceedingsIndexService.fetchAndParseXMLData();
        } catch (IOException e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
        return Response.status(Response.Status.OK).build();
    }

}
