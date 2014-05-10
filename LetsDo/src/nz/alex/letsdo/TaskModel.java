package nz.alex.letsdo;

enum TaskStatus {
	OPENED, CLOSED;
}

class Task{
	private int taskId;
	private TaskModel taskModel;
	
	public Task(int id, TaskModel model){
		taskId = id;
		taskModel = model;
	}
	
	public int getId(){
		return taskId;
	}	

	public TaskModel getTaskModel(){
		return taskModel;
	}	

	public void setStatus(TaskStatus aStatus){
		taskModel.status = aStatus;
	}

	public TaskStatus getStatus(){
		return taskModel.status;
	}

	public String toString(){
		return taskModel.title;
	}
	
	public String getCategory(){
		return taskModel.category;
	}	

	public String getAssignee(){
		return taskModel.assignee;
	}
	
	public boolean isOpen(){
		if (taskModel.status == TaskStatus.OPENED)
			return true;
		return false;
	}
}

public class TaskModel {
		
	protected String title, category, assignee, description, 
										dateDue;
	protected TaskStatus status;
	
	public TaskModel(String tTitle, String tCategory, String tAssignee, String tDescription, String tDateDue, TaskStatus tStatus){
		setTask(tTitle, tCategory, tAssignee, tDescription, tDateDue, tStatus);
	}

	public TaskModel(String tTitle, String tCategory, String tAssignee, String tDescription, String tDateDue){
		setTask(tTitle, tCategory, tAssignee, tDescription, tDateDue, TaskStatus.OPENED);
	}

	public TaskModel(String tTitle, String tCategory, String tAssignee, String tDescription){
		setTask(tTitle, tCategory, tAssignee, tDescription, null, TaskStatus.OPENED);
	}

	public void setTask(String tTitle, String tCategory, String tAssignee, String tDescription, String tDateDue, TaskStatus tStatus){
		title = tTitle;
		category = tCategory;
		assignee = tAssignee;
		description = tDescription;
		dateDue = tDateDue;
		status = tStatus;
	}	
}