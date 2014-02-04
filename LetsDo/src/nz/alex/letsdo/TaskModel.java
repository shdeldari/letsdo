package nz.alex.letsdo;

public class TaskModel {

	protected String title, category, assignee, description, 
										dateDue, dateCreated;
	public TaskModel(String tTitle, String tCategory, String tAssignee, String tDescription, String tDateDue){
		setTask(tTitle, tCategory, tAssignee, tDescription, tDateDue);
	}

	public TaskModel(String tTitle, String tCategory, String tAssignee, String tDescription){
		setTask(tTitle, tCategory, tAssignee, tDescription, null);
	}

	public void setTask(String tTitle, String tCategory, String tAssignee, String tDescription, String tDateDue){
		title = tTitle;
		category = tCategory;
		assignee = tAssignee;
		description = tDescription;
		dateDue = tDateDue;
	}
	
	public String toString(){
		return title;
	}
}
