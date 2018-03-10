/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.netbeans.rest.application.config;

import java.util.Set;
import javax.ws.rs.core.Application;

/**
 *
 * @author or
 */
@javax.ws.rs.ApplicationPath("/api")
public class ApplicationConfig extends Application {

    @Override
    public Set<Class<?>> getClasses() {

        Set<Class<?>> resources = new java.util.HashSet<>();
        addRestResourceClasses(resources);
        return resources;
    }

    /**
     * Do not modify addRestResourceClasses() method.
     * It is automatically populated with
     * all resources defined in the project.
     * If required, comment out calling this method in getClasses().
     */
    private void addRestResourceClasses(Set<Class<?>> resources) {
        resources.add(UnitTests.UnitTestsResources.class);
        resources.add(resources.ApartmentDetailsResources.class);
        resources.add(resources.AuthResource.class);
        resources.add(resources.ErrorLogResource.class);
        resources.add(resources.FavoritesResource.class);
        resources.add(resources.MapsResource.class);
        resources.add(resources.MatchResources.class);
        resources.add(resources.MessagesResource.class);
        resources.add(resources.OffererPrefResources.class);
        resources.add(resources.PostResources.class);
        resources.add(resources.RoomDetailsResources.class);
        resources.add(resources.SeekerPrefResources.class);
        resources.add(resources.UserAboutResource.class);
        resources.add(resources.UserResource.class);
        resources.add(resources.UserSettingsResources.class);
    }
    
}
