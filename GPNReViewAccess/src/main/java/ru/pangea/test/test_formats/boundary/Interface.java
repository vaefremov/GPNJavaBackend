/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.pangea.test.test_formats.boundary;

import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.ejb.EJBException;
import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import ru.pangea.test.test_formats.entities.Boundaries;
import ru.pangea.test.test_formats.entities.MethodInfo;
import ru.pangea.test.test_formats.entities.Project;
import ru.pangea.test.test_formats.entities.Well;
import ru.pangea.test.test_formats.entities.WellSet;
import ru.pangea.test.test_formats.persistence.LowLevelReviewDAO;

/**
 *
 * @author efremov
 */
@Path("yandex")
public class Interface {

    @Inject
//    WellDAO dao;
    UserObjectsDAO dao;

    @Context
    HttpServletResponse resp;

    @Path("projects")
    @GET
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Project> getProjects() {
        return dao.getProjects();
    }

    @Path("wells/{project}")
    @GET
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public WellSet getWells(@PathParam("project") String project, 
            @QueryParam("name") String name) {
        LOG.info("Getting wells for " + project);
        try {
            WellSet ans = dao.getWells(project);
            if (name == null)
                return ans;
            else
            {
                List<Well> tmp = ans.getWells().stream()
                        .filter(w -> w.getName().equalsIgnoreCase(name))
                        .collect(Collectors.toList());
                return new WellSet(tmp);
            }
        } catch (EJBException ex) {
            signalError(ex);
            return null;
        }
    }

    @Path("methods/{project}/{well}")
    @GET
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<MethodInfo> getMethods(@PathParam("project") String project,
            @PathParam("well") String well, @QueryParam("methodType") String methodType) {
        LOG.info("Getting methods for " + project + " " + well + " of type " + methodType);
        try {
            if (methodType == null) {
                return dao.getMethods(project, well);
            } else {
                return dao.getMethods(project, well).stream()
                        .filter(m -> methodType.equals(m.getMethodType()))
                        .collect(Collectors.toList());
            }
        } catch (EJBException ex) {
            signalError(ex);
            return null;
        }
    }

    @Path("method/{project}/{well}/{method}")
    @GET
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Boundaries getMethod(@PathParam("project") String project,
            @PathParam("well") String well, @PathParam("method") String method) {
        try {
            return dao.getMethod(project, well, method);
        } catch (EJBException ex) {
            signalError(ex);
            return null;
        }
    }

    private void signalError(EJBException ex) {
        if (ex.getCause().getCause() instanceof LowLevelReviewDAO.ContainerNotFound) {
            LOG.severe(ex.getMessage());
            try {
                resp.sendError(Response.Status.NOT_FOUND.getStatusCode(), ex.getMessage());
            } catch (IOException ex1) {
                throw new RuntimeException("Error when forming responce", ex1);
            }
        } else {
            throw ex;
        }
    }

    private static final Logger LOG = Logger.getLogger(Interface.class.getName());
}
