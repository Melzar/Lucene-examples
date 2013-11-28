package net.codelab.ui.rest.index;

import net.codelab.core.entity.dto.Course;
import net.codelab.core.lucene.index.ResultsDTO;
import net.codelab.core.service.index.CourseIndexService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;


/**
 * Created by Melzarek on 21/11/13.
 */

@Component
@Path("/index/course")
public class CourseRestIndex {

    @Autowired
    private CourseIndexService courseIndexService;


    public CourseRestIndex()
    {

    }

    @GET
    @Path("/search")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response getCourseData(@QueryParam("query") String query)
    {
        ResultsDTO<Course> result = courseIndexService.getCourseIndexData(query);
        return Response.status(Response.Status.OK).entity(result).build();
    }

    @POST
    @Path("/createindex")
    @Consumes(MediaType.TEXT_PLAIN)
    public  Response createIndex()
    {
        courseIndexService.reciveCourseData();
        return Response.status(Response.Status.OK).build();
    }
}
