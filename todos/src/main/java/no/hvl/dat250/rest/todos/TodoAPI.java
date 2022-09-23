package no.hvl.dat250.rest.todos;

import static spark.Spark.*;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import com.google.gson.Gson;

/**
 * Rest-Endpoint.
 */
public class TodoAPI {
	
	// Simulating DB
	static Map<Long, Todo> todos = null;
	private final static AtomicLong counter = new AtomicLong();

    public static void main(String[] args) {
        if (args.length > 0) {
            port(Integer.parseInt(args[0]));
        } else {
            port(8080);
        }
        
        todos = new ConcurrentHashMap<Long, Todo>();

        after((req, res) -> res.type("application/json"));

        // TODO: Implement API, such that the testcases succeed.
        
        // GET Requests		
        get("/todos", (req, res) -> new Gson().toJson(todos.values()));
 
        get("/todos/:id", (req, res) -> {
        	Long id;
        	try {
        		id = Long.parseLong(req.params(":id"));
        	}catch(NumberFormatException e) {
        		return String.format("The id \"%s\" is not a number!", req.params(":id"));
        	}
        	Todo todo = todos.get(id);
        	if(todo != null) {
        		return todo.toJson();
        	}
        	return String.format("Todo with the id  \"%s\" not found!", id);
        });
        
        // POST Requests
        post("/todos", (req, res) -> {
        	Todo responseTodo = new Gson().fromJson(req.body(), Todo.class);
        	Long id = counter.incrementAndGet();
        	Todo newTodo = new Todo(id, responseTodo.getSummary(), responseTodo.getDescription());
            todos.put(id, newTodo);
            return todos.get(id).toJson();
        });
        
        // PUT Requests
        put("/todos/:id", (req, res) -> {
        	Long id;
        	try {
        		id = Long.parseLong(req.params(":id"));
        	}catch(NumberFormatException e) {
        		return String.format("The id \"%s\" is not a number!", req.params(":id"));
        	}
        	Todo todo = todos.get(id);
        	if(todo != null) {
        		Todo updatedTodo = new Gson().fromJson(req.body(), Todo.class);
            	todo = new Todo((long)id, updatedTodo.getSummary(), updatedTodo.getDescription());
            	todos.put(id, todo);
            	return todos.get(id).toJson();
        	}
        	return String.format("Todo with the id  \"%s\" not found!", id);
        });
        
        // DELETE Requests
        delete("/todos/:id", (req, res) -> {
        	Long id;
        	try {
        		id = Long.parseLong(req.params(":id"));
        	}catch(NumberFormatException e) {
        		return String.format("The id \"%s\" is not a number!", req.params(":id"));
        	}
        	Todo todo = todos.get(id);
        	if(todo != null) {
            	return todos.remove(id).toJson();
        	}
        	return String.format("Todo with the id  \"%s\" not found!", id);
        });
        
    }
    
}
