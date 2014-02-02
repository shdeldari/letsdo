package nz.alex.letsdo;

public class TaskModel {

	protected String title, category, assignee, description;

	public TaskModel(String tTitle, String tCategory, String tAssignee, String tDescription){
		setTask(tTitle, tCategory, tAssignee, tDescription);
	}

	public void setTask(String tTitle, String tCategory, String tAssignee, String tDescription){
		title = tTitle;
		category = tCategory;
		assignee = tAssignee;
		description = tDescription;
	}
	
	public String getTitle(){
		return title;
	}
}
