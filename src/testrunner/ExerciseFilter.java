package testrunner;

import org.junit.runner.Description;
import org.junit.runner.manipulation.Filter;

/**
 *
 * @author mrannanj
 */
public class ExerciseFilter extends Filter {
	
	private String exercise;

	public ExerciseFilter(String exercise)
	{
		this.exercise = exercise;
	}

	@Override
	public String describe()
	{
		return "Filters tests based on exercise";
	}

	@Override
	public boolean shouldRun(Description description) {
		Exercise e = description.getAnnotation(Exercise.class);
		if (e != null && e.value().equals(this.exercise))
		{
			return true;
		}
		return false;
	}
}
