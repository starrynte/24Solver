package org.starrynte.solver24;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

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
		((EditText) findViewById(R.id.number_input)).setOnEditorActionListener(new TextView.OnEditorActionListener()
		{
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event)
			{
				if (actionId == EditorInfo.IME_NULL || actionId == EditorInfo.IME_ACTION_SEND)
				{
					add(v);
					return true;
				}
				return false;
			}
		});
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
		int[] numberArray = new int[numberList.size()];
		for (int i = 0; i < numberList.size(); i++)
		{
			numberArray[i] = numberList.get(i);
		}
		Intent results = new Intent(this, ResultsActivity.class);
		results.putExtra(EXTRA_NUMBERS, numberArray);
		startActivity(results); //consider startActivityForResult
	}
}
