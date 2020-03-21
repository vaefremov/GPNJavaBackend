/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.pangea.test.test_formats.boundary;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import ru.pangea.test.test_formats.entities.Attribute;
import ru.pangea.test.test_formats.entities.Container;
import ru.pangea.test.test_formats.entities.ContainerWithAttributes;
import ru.pangea.test.test_formats.persistence.LowLevelReviewDAO;

/**
 *
 * @author efremov
 */
@Path("review_low")
public class ReViewInterface {
    @Inject
    private LowLevelReviewDAO rv;
    @Inject
    private UserObjectsDAO dao;
    
    @GET
    @Path("containers/{parentId}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public List<ContainerWithAttributes> getContainersByTypeWithAttrs(@PathParam("parentId") long parentId, @QueryParam("type") String type) {
        List<ContainerWithAttributes> res = rv.getSubcontainersOfTypeWithAttributes(parentId, type);
        return res;
    }
    
    @GET
    @Path("container/{id}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Container getContainerById(@PathParam("id") long containerId) {
        Container res = rv.getContainerById(containerId);
        return res;
    }
    
    @GET
    @Path("container_ext/{id}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public ContainerWithAttributes getContainerByIdWithAttributes(@PathParam("id") long containerId) {
        ContainerWithAttributes res = rv.getContainerByIdWithAttributes(containerId);
        return res;
    }

    @GET
    @Path("container_ext1/{id}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Container getContainerByIdWithAttributes(@PathParam("id") long parentId, 
            @QueryParam("name") String name, @QueryParam("type") String containerType) {
        Container res = rv.getContainerByNameType(parentId, name, containerType);
        return res;
    }

    
    @GET
    @Path("subcontainers/{parentId}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public List<Container> getContainers(@PathParam("parentId") long parentId, @QueryParam("type") String containerType) {
        List<Container> res;
        if(containerType == null) {
             res = rv.getSubContainers(parentId);
        } else {
            res = rv.getSubContainers(parentId).stream()
                    .filter(c -> containerType.equals(c.getType()))
                    .collect(Collectors.toList());
        }
        return res;
    }
    
    @GET
    @Path("attributes/{containerId}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public List<Attribute> getAttributes(@PathParam("containerId") long containerId) {
        List<Attribute> res = rv.getContainerAttributes(containerId);
        return res;
    }
    
    @GET
    @Path("attributes_ext/{containerId}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Map<String, List<Attribute>> getAttributesExt(@PathParam("containerId") long containerId) {
        return rv.getContainerAttributesAsMap( containerId);
    }
}
