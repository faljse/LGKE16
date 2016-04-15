package com.rest.restservice;
 

import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;

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
import org.omilab.robot.test.Test;

@Path("/robot")
@Singleton
public class Robot {
    private static final Logger log= Logger.getLogger( Robot.class.getName() );

    private Body body;
	
	@GET
    @Path("init")
    @Produces(MediaType.TEXT_PLAIN)
    public String init(@QueryParam("AccessMethod") String accessMethod) {
    	try {
            body = new Body(EnumWorldAccessMethod.valueOf(accessMethod));
        } catch (IOException e){
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            log.log(Level.SEVERE, "init failed", e);
            return sw.toString();
        }
        return "";
	}
	
	@GET
    @Path("test")
    @Produces(MediaType.TEXT_PLAIN)
    public String test(@QueryParam("System") String system) {

    	ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos);
        PrintStream old = System.out;
        
        System.setOut(ps);
        
        Test test = new Test();
        try {
            test.testBody(body, system);
        }   catch (IOException e){
            e.printStackTrace();
        }
		
		System.out.println();
        System.out.flush();
        System.setOut(old);
        
        return baos.toString();
	}
    
    @POST
    @Path("demo-drive")
    @Consumes(MediaType.TEXT_PLAIN)
    public String demodrive(@QueryParam("driveParamString") String s) {
        try {
    	//spd1,direction1,spd2,...
    	String[] params = s.split(",");
    	
    	body.pufferMotorCommand(true);
        body.actMotor((short) 1, EnumMotorDirection.valueOf(params[1]), Short.parseShort(params[0]));
        body.actMotor((short) 2, EnumMotorDirection.valueOf(params[3]), Short.parseShort(params[2]));
    	body.actMotor((short) 3, EnumMotorDirection.valueOf(params[5]), Short.parseShort(params[4]));
    	body.pufferMotorCommand(false);
    	body.actMotor((short) 4, EnumMotorDirection.valueOf(params[7]), Short.parseShort(params[6]));}
        catch (IOException e) {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            log.log(Level.SEVERE, "demodrive failed", e);
            return sw.toString();
        }
    	return "Success";
    }
}
