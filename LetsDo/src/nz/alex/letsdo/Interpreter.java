package nz.alex.letsdo;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Interpreter {

	private static String DELIMETER = " :";

	private Pattern pattern;
	private Matcher matcher;

	public Interpreter() {
		// TODO Auto-generated constructor stub
	}

	private String stuff(String str){
		pattern = Pattern.compile("\\\\");
		matcher = pattern.matcher(str);
		String retStr = matcher.replaceAll("\\\\\\\\");

		pattern = Pattern.compile(":");
		matcher = pattern.matcher(retStr);
		retStr = matcher.replaceAll("\\\\:");

		return retStr;
	}

	private String deStuff(String str){
		pattern = Pattern.compile("\\\\:");
		matcher = pattern.matcher(str);
		String retStr = matcher.replaceAll(":");

		pattern = Pattern.compile("\\\\\\\\");
		matcher = pattern.matcher(retStr);
		retStr = matcher.replaceAll("\\\\");

		return retStr;
	}

	private String[] split(String str){
		pattern = Pattern.compile(DELIMETER);
		return pattern.split(str);
	}

	public String compile(Task task){
		return stuff(task.getCategory()) + DELIMETER	+ stuff(task.getDescription()) + DELIMETER + task.getStatus(); 
	}

	public TaskModel deCompile(String str) {
		String [] splits = split(str);
		TaskModel task = new TaskModel(null, deStuff(splits[0]), null, deStuff(splits[1]));
		if (splits[3].equals(TaskStatus.OPENED.toString()))
			task.status = TaskStatus.OPENED;
		else
			task.status = TaskStatus.CLOSED;
		return task; 
	}
}
