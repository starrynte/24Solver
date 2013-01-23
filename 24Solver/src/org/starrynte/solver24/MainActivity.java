package org.starrynte.solver24;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

public class MainActivity extends Activity
{
	public final static String EXTRA_NUMBERS = "org.starrynte.solver24.NUMBERS";
	ArrayAdapter<Integer> adapter;
	List<Integer> numberList;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		numberList = new ArrayList<Integer>();
		adapter = new ArrayAdapter<Integer>(this, R.layout.item_number, numberList);
		((ListView) findViewById(R.id.number_list)).setAdapter(adapter);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	public void add(View view)
	{
		EditText number = (EditText) findViewById(R.id.number_input);
		adapter.add(Integer.valueOf(number.getText().toString()));
		number.setText("");
	}

	public void calculate(View view)
	{
		/*
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
		*/
	}

}
