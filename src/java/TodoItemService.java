
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author athola
 */




// Define TodoItemService for database operations
@Path("/todos")
class TodoItemService {
    private static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    private static final String DB_URL = "jdbc:mysql://localhost/todolist_db";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "";



    @Context
    private UriInfo context;
    
//// Enable CORS
//header("Access-Control-Allow-Origin: *");
//header("Access-Control-Allow-Methods: GET, POST, PUT, DELETE");
//header("Access-Control-Allow-Headers: Content-Type, Authorization");
    // Create a new to-do item
    @POST
    
    @Path("/create")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createTodoItem(TodoItem todoItem) {
        try {
            Class.forName(JDBC_DRIVER);
            Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);

            PreparedStatement stmt = conn.prepareStatement("INSERT INTO todo_item (title, description, due_date) VALUES (?, ?, ?)");
            stmt.setString(1, todoItem.getTitle());
            stmt.setString(2, todoItem.getDescription());
            stmt.setString(3, todoItem.getDueDate());

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected == 1) {
                return Response.status(Response.Status.CREATED).build();
            } else {
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Get all to-do items
    @GET
    @Path("/get_all")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllTodos() {
        try {
            Class.forName(JDBC_DRIVER);
            Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);

            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM todo_item");

            List<TodoItem> todoItems = new ArrayList<>();
            while (rs.next()) {
                TodoItem todoItem = new TodoItem();
                todoItem.setId(rs.getInt("id"));
                todoItem.setTitle(rs.getString("title"));
                todoItem.setDescription(rs.getString("description"));
                todoItem.setDueDate(rs.getString("due_date"));
                todoItems.add(todoItem);
            }

            return Response.ok(todoItems).build();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Get a specific to-do item by ID
    @GET
    @Path("/get/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getTodoById(@PathParam("id") int id) {
        try {
            Class.forName(JDBC_DRIVER);
            Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);

            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM todo_item WHERE id = ?");
            stmt.setInt(1, id);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                TodoItem todoItem = new TodoItem();
                todoItem.setId(rs.getInt("id"));
                todoItem.setTitle(rs.getString("title"));
                todoItem.setDescription(rs.getString("description"));
                todoItem.setDueDate(rs.getString("due_date"));

                return Response.ok(todoItem).build();
            } else {
                return Response.status(Response.Status.NOT_FOUND).build();
            }
        } catch (ClassNotFoundException | SQLException e ){
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
        
    }
    
    // Delete a to-do item
    @DELETE
    @Path("/delete/{id}")
    public Response deleteTodoItem(@PathParam("id") int id) {
        // Perform database deletion
         try {
            Class.forName(JDBC_DRIVER);
            Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);

            PreparedStatement stmt = conn.prepareStatement("DELETE FROM todo_item WHERE id = ? ");
            stmt.setInt(1, id);
            
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected == 1) {
                return Response.status(Response.Status.CREATED).build();
            } else {
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    
    
    // edit to-do item
    @PUT
    
    @Path("/edit")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateTodoItem(TodoItem todoItem) {
        try {
            Class.forName(JDBC_DRIVER);
            Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);

            PreparedStatement stmt = conn.prepareStatement("UPDATE todo_item set title=?, description=?, due_date=? WHERE id= ?)");
            stmt.setString(1, todoItem.getTitle());
            stmt.setString(2, todoItem.getDescription());
            stmt.setString(3, todoItem.getDueDate());
            stmt.setInt(4, todoItem.getId());

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected == 1) {
                return Response.status(Response.Status.CREATED).build();
            } else {
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }
}
    
    