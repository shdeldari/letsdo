package nz.alex.letsdo;

enum TaskStatus {
	OPENED, CLOSED;
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
	
	public String toString(){
		return title;
	}
	
	public void openTask(){
		status = TaskStatus.OPENED;
	}
	
	public void closeTask(){
		status = TaskStatus.CLOSED;
	}

	public TaskStatus getStatus(){
		return status;
	}

	public void setStatus(TaskStatus aStatus){
		status = aStatus;
	}
}