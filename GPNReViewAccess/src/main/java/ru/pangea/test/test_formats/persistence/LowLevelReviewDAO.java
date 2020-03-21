/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.pangea.test.test_formats.persistence;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.sql.DataSource;
import org.sql2o.Connection;
import org.sql2o.Sql2o;
import ru.pangea.test.test_formats.entities.Attribute;
import ru.pangea.test.test_formats.entities.Container;
import ru.pangea.test.test_formats.entities.ContainerWithAttributes;
import ru.pangea.test.test_formats.entities.DoubleAttribute;
import ru.pangea.test.test_formats.entities.IntAttribute;
import ru.pangea.test.test_formats.entities.PointAttribute;
import ru.pangea.test.test_formats.entities.RefWithParameterAttribute;
import ru.pangea.test.test_formats.entities.StringAttribute;

/**
 *
 * @author efremov
 */
@Stateless
@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
public class LowLevelReviewDAO {

    @Resource(lookup = "java:comp/env/jdbc/PANGEA")
    DataSource ds;

    private Map<String, String> columnNamesMapping = new HashMap<>();
    Connection connection;

    private Map<Class<?>, String> sqlQueries = new HashMap<>();

    public static class ContainerNotFound extends RuntimeException {

        public ContainerNotFound() {
        }

        public ContainerNotFound(String message) {
            super(message);
        }
    }

    @PostConstruct
    public void init() {
        LOG.info("Initializing connection");
        setupDaoInstance();
        Sql2o sql2oWrapper = new Sql2o(ds);
        sql2oWrapper.setDefaultColumnMappings(columnNamesMapping);
        connection = sql2oWrapper.open();
    }

    @PreDestroy
    public void cleanup() {
        LOG.info("Cleaning up");
        if (connection != null) {
            connection.close();
            connection = null;
        }
    }

    public Container getContainerById(long containerId) {
        List<Container> tmp = connection.createQuery(SQL_GET_CONTAINERBYID)
                .addParameter("containerId", containerId)
                .executeAndFetch(Container.class);
        if (tmp.size() > 1) {
            throw new RuntimeException("Inconsistent DB: multiple containers with the same id " + containerId);
        } else if (tmp.isEmpty()) {
            throw new ContainerNotFound("Not found: id " + containerId);
        } else {
            return tmp.get(0);
        }
    }

    public ContainerWithAttributes getContainerByIdWithAttributes(long containerId) {
        return new ContainerWithAttributes(getContainerById(containerId), getContainerAttributesAsMap(containerId));
    }

    public List<Container> getContainersByParentType(long parentId, String type) {
        return connection.createQuery(SQL_GET_CONTAINERS)
                .addParameter("linkUp", parentId)
                .addParameter("type", type)
                .executeAndFetch(Container.class);
    }

    public List<Container> getSubContainers(long parentId) {
        return connection.createQuery(SQL_GET_SUBCONTAINERS)
                .addParameter("linkUp", parentId)
                .executeAndFetch(Container.class);
    }

    public List<StringAttribute> getContainerStringAttributes(long containerId) {
        return connection.createQuery(SQL_GET_STRING_ATTRIBUTES)
                .addParameter("containerId", containerId)
                .executeAndFetch(StringAttribute.class);
    }

    public List<IntAttribute> getContainerIntAttributes(long containerId) {
        return connection.createQuery(SQL_GET_INT_ATTRIBUTES)
                .addParameter("containerId", containerId)
                .executeAndFetch(IntAttribute.class);
    }

    public <T> List<T> getGenericContainerAttributes(long containerId, Class<T> typeArg) {
        return connection.createQuery(sqlQueries.get(typeArg))
                .addParameter("containerId", containerId)
                .executeAndFetch(typeArg);
    }

    public List<Attribute> getContainerAttributes(long containerId) {
        List<StringAttribute> sa = getContainerStringAttributes(containerId);
        List<IntAttribute> si = getContainerIntAttributes(containerId);
        List<PointAttribute> sp = getGenericContainerAttributes(containerId, PointAttribute.class);
        List<DoubleAttribute> sd = getGenericContainerAttributes(containerId, DoubleAttribute.class);
        List<RefWithParameterAttribute> sdr = getGenericContainerAttributes(containerId, RefWithParameterAttribute.class);
        List<Attribute> res = new ArrayList<>(sa);
        res.addAll(si);
        res.addAll(sp);
        res.addAll(sd);
        res.addAll(sdr);
        return res;
    }

    public Map<String, List<Attribute>> getContainerAttributesAsMap(long containerID) {
        List<Attribute> tmp = getContainerAttributes(containerID);
        return tmp.stream().collect(Collectors.groupingBy(Attribute::getAttributeName));
    }

    public List<ContainerWithAttributes> getSubcontainersOfTypeWithAttributes(long parentId, String type) {
        return getContainersByParentType(parentId, type)
                .stream()
                .map(c -> new ContainerWithAttributes(c, getContainerAttributesAsMap(c.getId())))
                .collect(Collectors.toList());
    }

    public Container getContainerByNameType(long parentId, String name, String type) {
        List<Container> tmp = connection.createQuery(SQL_GET_CONTAINERBYNAMETYPE)
                .addParameter("parentId", parentId)
                .addParameter("name", name)
                .addParameter("type", type)
                .executeAndFetch(Container.class);
        if (tmp.size() > 1) {
            throw new RuntimeException("Inconsistent DB: multiple containers with the same name subcontainers of " + parentId);
        } else if (tmp.isEmpty()) {
            throw new ContainerNotFound("Not found: container " + name + " subcontainer of " + parentId + " of type " + type);
        } else {
            return tmp.get(0);
        }

    }

    void setupDaoInstance() {
        specifyColumnMapping();
        setupSqlQueries();
    }

    private void specifyColumnMapping() {
        columnNamesMapping.put("CodeContainer", "id");
        columnNamesMapping.put("ContainerType", "type");
        columnNamesMapping.put("ContainerName", "name");
        columnNamesMapping.put("KeyWord", "attributeName");
        columnNamesMapping.put("CodeValue", "id");
        columnNamesMapping.put("ValueIndex", "index");
        columnNamesMapping.put("DataValue", "value");
        columnNamesMapping.put("DataValueX", "x");
        columnNamesMapping.put("DataValueY", "y");
        columnNamesMapping.put("DataValueZ", "z");
        columnNamesMapping.put("DataValueD", "value");
        columnNamesMapping.put("DataValueR", "ref");
    }

    private void setupSqlQueries() {
        sqlQueries.put(StringAttribute.class, SQL_GET_STRING_ATTRIBUTES);
        sqlQueries.put(IntAttribute.class, SQL_GET_INT_ATTRIBUTES);
        sqlQueries.put(DoubleAttribute.class, SQL_GET_DOUBLE_ATTRIBUTES);
        sqlQueries.put(PointAttribute.class, SQL_GET_POINT_ATTRIBUTES);
        sqlQueries.put(RefWithParameterAttribute.class, SQL_GET_REFWPARAM_ATTRIBUTES);
    }

    private static final Logger LOG = Logger.getLogger(LowLevelReviewDAO.class.getName());
    static final String SQL_GET_CONTAINERS = "select * from Containers where LinkUp = :linkUp and ContainerType = :type and Status='Actual'";
    static final String SQL_GET_CONTAINERBYID = "select * from Containers where CodeContainer = :containerId and Status='Actual'";
    static final String SQL_GET_SUBCONTAINERS = "select * from Containers where LinkUp = :linkUp and Status='Actual'";
    private static final String SQL_GET_SUBCONTAINERS_BY_NAME_TYPE = "select * from Containers where LinkUp = :linkUp"
            + " and ContainerType = :type and ContainerName = :name and Status='Actual'";
    private static final String SQL_GET_SUBCONTAINERS_BY_TYPE = "select * from Containers where LinkUp = :linkUp"
            + " and ContainerType = :type and ContainerName = :name and Status='Actual'";
    static final String SQL_GET_STRING_ATTRIBUTES = "select a.CodeValue, a.LinkContainer, a.LinkMetaData, a.ValueIndex, a.Status, a.DataValue, m.KeyWord from DataValuesC a "
            + "join MetaData m on a.LinkMetaData = m.CodeData where a.LinkContainer = :containerId and a.Status='Actual'";
    static final String SQL_GET_INT_ATTRIBUTES = "select a.CodeValue, a.LinkContainer, a.LinkMetaData, a.ValueIndex, a.Status, a.DataValue, m.KeyWord from DataValuesI a "
            + "join MetaData m on a.LinkMetaData = m.CodeData where a.LinkContainer = :containerId and a.Status='Actual'";
    static final String SQL_GET_DOUBLE_ATTRIBUTES = "select a.CodeValue, a.LinkContainer, a.LinkMetaData, a.ValueIndex, a.Status, a.DataValue, m.KeyWord from DataValuesD a "
            + "join MetaData m on a.LinkMetaData = m.CodeData where a.LinkContainer = :containerId and a.Status='Actual'";
    static final String SQL_GET_POINT_ATTRIBUTES = "select a.CodeValue, a.LinkContainer, a.LinkMetaData, a.ValueIndex, a.Status, a.DataValueX, a.DataValueY, a.DataValueZ, m.KeyWord from DataValuesP a "
            + "join MetaData m on a.LinkMetaData = m.CodeData where a.LinkContainer = :containerId and a.Status='Actual'";
    static final String SQL_GET_REFWPARAM_ATTRIBUTES = "select a.CodeValue, a.LinkContainer, a.LinkMetaData, a.ValueIndex, a.Status, a.DataValueD, a.DataValueR, c.ContainerName as refContainerName, m.KeyWord "
            + " from DataValuesX a join Containers c on a.DataValueR = c.CodeContainer join MetaData m on a.LinkMetaData = m.CodeData "
            + "where a.Status='Actual' and c.Status='Actual' and a.LinkContainer = :containerId";
    static final String SQL_GET_CONTAINERBYNAMETYPE = "select * from Containers where LinkUp = :parentId and ContainerName = :name and ContainerType = :type and Status='Actual'";
}
