import java.util.List;
import org.sql2o.*;
import java.text.ParseException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Task {
  private int id;
  private String description;
  private int categoryId;
  private String duedate;

  public Task(String description, int categoryId, String duedate) {
    this.description = description;
    this.categoryId = categoryId;
    this.duedate = duedate;
  }

  public String getDescription() {
    return description;
  }

  public int getId() {
    return id;
  }

  public int getCategoryId() {
    return categoryId;
  }
  public String getDueDate() {
    return duedate;
  }
  public String formatDueDate(){
    SimpleDateFormat fromUser = new SimpleDateFormat("yyyy-MM-dd");
    SimpleDateFormat myFormat = new SimpleDateFormat("E, MMM dd yyyy");
    try {
      String reformattedStr = myFormat.format(fromUser.parse(duedate));
      return reformattedStr;
    } catch (ParseException e) {
      return "broken";
    }

  }

  public static List<Task> all() {
    String sql = "SELECT id, description, categoryId FROM tasks";
    try(Connection con = DB.sql2o.open()) {
      return con.createQuery(sql).executeAndFetch(Task.class);
    }
  }

  @Override
  public boolean equals(Object otherTask){
    if (!(otherTask instanceof Task)) {
      return false;
    } else {
      Task newTask = (Task) otherTask;
      return this.getDescription().equals(newTask.getDescription()) &&
             this.getId() == newTask.getId() &&
             this.getCategoryId() == newTask.getCategoryId();
    }
  }

  public void save() {
    try(Connection con = DB.sql2o.open()) {
      String sql = "INSERT INTO tasks(description, categoryId, duedate) VALUES (:description, :categoryId, :duedate)";
      this.id = (int) con.createQuery(sql, true)
        .addParameter("description", this.description)
        .addParameter("categoryId", this.categoryId)
        .addParameter("duedate", this.duedate)
        .executeUpdate()
        .getKey();
    }
  }

  public static Task find(int id) {
    try(Connection con = DB.sql2o.open()) {
      String sql = "SELECT * FROM tasks where id=:id";
      Task task = con.createQuery(sql)
        .addParameter("id", id)
        .executeAndFetchFirst(Task.class);
      return task;
    }
  }
}
