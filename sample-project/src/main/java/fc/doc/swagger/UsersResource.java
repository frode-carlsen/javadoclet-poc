package fc.doc.swagger;

import java.util.UUID;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * 
 * Endpoint for User specific operations.
 */
// see:
// https://tech.homeaway.com/development/2016/06/02/generating-swagger-spec.html
@Path("/users")
@Api(value = "users")
public class UsersResource {
	@GET
	@Path("/{userUUID}")
	@Produces("application/xml")
	@ApiOperation(value = "Returns user details", notes = "Returns user details.", response = User.class)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Successful retrieval of user entity", response = User.class),
			@ApiResponse(code = 404, message = "User with given uuid does not exist"),
			@ApiResponse(code = 500, message = "Internal server error") })
	public Response getUser(@ApiParam(value = "userUUID") @PathParam("userUUID") UUID userUUID) {
		return Response.ok().build();
	}
}