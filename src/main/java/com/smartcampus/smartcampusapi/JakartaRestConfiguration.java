package com.smartcampus.smartcampusapi;

import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;

/**
 * Configures Jakarta RESTful Web Services for the application.
 * @author Jolen
 */
@ApplicationPath("/api/v1") // application path points to /api/v1
public class JakartaRestConfiguration extends Application {
    
}
