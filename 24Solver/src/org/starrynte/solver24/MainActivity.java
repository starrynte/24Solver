package org.starrynte.solver24;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends Activity
{
	public final static String EXTRA_NUMBERS = "org.starrynte.solver24.NUMBERS";

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	public void calculate(View view)
	{
		EditText numberList = (EditText) findViewById(R.id.number_list);
		String[] numbersRaw = numberList.getText().toString().trim().split("\\s+");
		int[] numbers = new int[numbersRaw.length];
		for (int i = 0; i < numbersRaw.length; i++)
		{
			numbers[i] = Integer.parseInt(numbersRaw[i]);
		}
		Intent results = new Intent(this, ResultsActivity.class);
		results.putExtra(EXTRA_NUMBERS, numbers);
		startActivity(results); //consider startActivityForResult
	}

}
