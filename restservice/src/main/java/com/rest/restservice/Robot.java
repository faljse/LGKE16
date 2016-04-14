package com.rest.restservice;
 
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import javax.inject.Singleton;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.omilab.robot.body.Body;
import org.omilab.robot.interfaces.bodyworld.EnumMotorDirection;
import org.omilab.robot.interfaces.bodyworld.EnumWorldAccessMethod;
 
@Path("/robot")
@Singleton
public class Robot {
	private Body body;
	
	@GET
    @Path("init")
    @Produces(MediaType.TEXT_PLAIN)
    public String init(@QueryParam("AccessMethod") String accessMethod) {
    	ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos);
        PrintStream old = System.out;
        
        System.setOut(ps);
        
        body = new Body(EnumWorldAccessMethod.valueOf(accessMethod));
		
		System.out.println();
        
        System.out.flush();
        System.setOut(old);
        
        return baos.toString();
	}
	
	@GET
    @Path("test")
    @Produces(MediaType.TEXT_PLAIN)
    public String test(@QueryParam("System") String system) {
    	ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos);
        PrintStream old = System.out;
        
        System.setOut(ps);
        
        org.omilab.robot.test.Test Test = new org.omilab.robot.test.Test();
        Test.testBody(body, system);
		
		System.out.println();
        
        System.out.flush();
        System.setOut(old);
        
        return baos.toString();
	}
    
    @POST
    @Path("demo-drive")
    @Consumes(MediaType.TEXT_PLAIN)
    public String demodrive(@QueryParam("driveParamString") String s) {
    	
    	//spd1,direction1,spd2,...
    	String[] params = s.split(",");
    	
    	body.pufferMotorCommand(true);
    	body.actMotor((short) 1, EnumMotorDirection.valueOf(params[1]), Short.parseShort(params[0]));
    	body.actMotor((short) 2, EnumMotorDirection.valueOf(params[3]), Short.parseShort(params[2]));
    	body.actMotor((short) 3, EnumMotorDirection.valueOf(params[5]), Short.parseShort(params[4]));
    	body.pufferMotorCommand(false);
    	body.actMotor((short) 4, EnumMotorDirection.valueOf(params[7]), Short.parseShort(params[6]));
    	
    	return "Success";
    }
}
