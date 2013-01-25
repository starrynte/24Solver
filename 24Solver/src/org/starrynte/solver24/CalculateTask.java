package org.starrynte.solver24;

import android.os.AsyncTask;
import android.widget.ProgressBar;
import android.widget.TextView;

public class CalculateTask extends AsyncTask<int[], Object, Void>
{
	TextView resultsView;
	ProgressBar progressBar;

	public CalculateTask(TextView resultsView, ProgressBar progressBar)
	{
		this.resultsView = resultsView;
		this.progressBar = progressBar;
	}

	@Override
	protected Void doInBackground(int[]... params)
	{
		int[] numbers = params[0];
		try
		{
			Thread.sleep(1000);
			publishProgress(500);
			Thread.sleep(2000);
		} catch (InterruptedException e)
		{
			e.printStackTrace();
		}
		publishProgress(1000, numbers.length + " numbers.");
		return null;
	}

	@Override
	//values[0] is 10 * % done (Integer)
	//values[1] (if present) is new solution found (String)
	protected void onProgressUpdate(Object... values)
	{
		progressBar.setProgress((Integer) values[0]);
		if (values.length > 1)
		{
			resultsView.append(((String) values[1]) + "\n");
		}
	}
}
