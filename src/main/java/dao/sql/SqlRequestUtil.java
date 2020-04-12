package dao.sql;

import domain.Department;
import domain.Employee;
import domain.Organization;
import domain.Task;

import javax.ejb.Stateless;
import java.util.HashMap;
import java.util.Map;

@Stateless (name = "SqlRequestUtil")
public class SqlRequestUtil {
    private final Map<Class, Map<SqlRequestType, String>> SQL_REQUESTS = new HashMap<Class, Map<SqlRequestType, String>>()
    {{
        put(Organization.class, new HashMap<SqlRequestType, String>()
            {{put(SqlRequestType.GET_ALL_QUERY, "SELECT id, name, physicalAddress, legalAddress, manager_id FROM Organization LIMIT 1");
                put(SqlRequestType.GET_BY_ID, "SELECT name, physicalAddress, legalAddress, manager_id FROM Organization WHERE id=?");
                put(SqlRequestType.PUT_QUERY, "INSERT INTO Organization (id, name, physicalAddress, legalAddress, manager_id) VALUES (?, ?, ?, ?, ?)");
                put(SqlRequestType.DELETE_QUERY, "DELETE FROM Organization WHERE id= ?");
                put(SqlRequestType.POST_QUERY, "UPDATE Organization SET id= ?, name=?, physicalAddress=?, legalAddress=?, manager_id=? WHERE id=?");
            }});
        //id column rewritten for parameters sequence equal in put and post queries
        put(Department.class, new HashMap<SqlRequestType, String>()
        {{put(SqlRequestType.GET_ALL_QUERY, "SELECT id, name, contacts, manager_id FROM Department");
            put(SqlRequestType.GET_BY_ID, "SELECT name, contacts, manager_id FROM Department WHERE id=?");
            put(SqlRequestType.PUT_QUERY, "INSERT INTO Department (id, name, contacts, manager_id) VALUES (?, ?, ?, ?)");
            put(SqlRequestType.POST_QUERY, "UPDATE Department SET id= ?, name=?, contacts=?, manager_id=? WHERE id=?");
            put(SqlRequestType.DELETE_QUERY, "DELETE FROM Department WHERE id= ?");
        }});

        put(Employee.class, new HashMap<SqlRequestType, String>()
            {{put(SqlRequestType.GET_ALL_QUERY, "SELECT id, firstName, secondName, patronymicName, function FROM Employee");
              put(SqlRequestType.GET_BY_ID, "SELECT firstName, secondName, patronymicName, function FROM Employee WHERE id=?");
              put(SqlRequestType.PUT_QUERY, "INSERT INTO Employee (id, firstName, secondName, patronymicName, function) VALUES (?, ?, ?, ?, ?)");
              put(SqlRequestType.POST_QUERY, "UPDATE Employee SET id= ?, firstName=?, secondName=?, patronymicName=?, function=? WHERE id=?");
                put(SqlRequestType.DELETE_QUERY, "DELETE FROM Employee WHERE id= ?");
            }});

        put(Task.class, new HashMap<SqlRequestType, String>()//TODO rewite sql requests for Task.class
        {{put(SqlRequestType.GET_ALL_QUERY, "SELECT id, firstName, secondName, patronymicName, function FROM Employee");
            put(SqlRequestType.GET_BY_ID, "SELECT firstName, secondName, patronymicName, function FROM Employee WHERE id=?");
            put(SqlRequestType.PUT_QUERY, "INSERT INTO Employee (id, firstName, secondName, patronymicName, function) VALUES (?, ?, ?, ?, ?)");
            put(SqlRequestType.POST_QUERY, "UPDATE Employee SET id= ?, firstName=?, secondName=?, patronymicName=?, function=? WHERE id=?");
            put(SqlRequestType.DELETE_QUERY, "DELETE FROM Employee WHERE id= ?");
        }});

    }};

    public String getSqlRequest(Class clazz, SqlRequestType type) {
        return SQL_REQUESTS.get(clazz).get(type);
    }
}
