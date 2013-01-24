package org.starrynte.solver24;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity
{
	public final static String EXTRA_NUMBERS = "org.starrynte.solver24.NUMBERS";
	private final static String BUNDLE_NUMBERS = "numbers";
	ArrayAdapter<Integer> adapter;
	ArrayList<Integer> numberList;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		numberList = (savedInstanceState != null) ? savedInstanceState.getIntegerArrayList(BUNDLE_NUMBERS) : new ArrayList<Integer>();
		adapter = new ArrayAdapter<Integer>(this, R.layout.item_number, numberList);
		GridView gridView = (GridView) findViewById(R.id.number_list);
		gridView.setAdapter(adapter);
		gridView.setOnItemClickListener(new AdapterView.OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id)
			{
				showNumberEditDialog(position);
			}
		});
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

	@Override
	protected void onSaveInstanceState(Bundle outState)
	{
		super.onSaveInstanceState(outState);
		outState.putIntegerArrayList(BUNDLE_NUMBERS, numberList);
	}

	public void add(View view)
	{
		EditText editText = (EditText) findViewById(R.id.number_input);
		try
		{
			int number = Integer.valueOf(editText.getText().toString());
			adapter.add(number);
			editText.setText("");
		} catch (NumberFormatException e)
		{
			Toast.makeText(this, R.string.number_invalid, Toast.LENGTH_SHORT).show();
		}
	}

	public void calculate(View view)
	{
		if (numberList.size() == 0)
		{
			Toast.makeText(this, R.string.no_numbers, Toast.LENGTH_SHORT).show();
		}
		int[] numberArray = new int[numberList.size()];
		for (int i = 0; i < numberList.size(); i++)
		{
			numberArray[i] = numberList.get(i);
		}
		Intent results = new Intent(this, ResultsActivity.class);
		results.putExtra(EXTRA_NUMBERS, numberArray);
		startActivity(results); //consider startActivityForResult
	}

	private void showNumberEditDialog(final int index)
	{
		final EditText editText = (EditText) getLayoutInflater().inflate(R.layout.number_edit, null);

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setView(editText)
				.setPositiveButton(R.string.number_delete, new DialogInterface.OnClickListener()
				{
					@Override
					public void onClick(DialogInterface dialog, int which)
					{
						numberList.remove(index);
						adapter.notifyDataSetChanged();
					}
				})
				.setNegativeButton(R.string.number_save, new DialogInterface.OnClickListener()
				{
					@Override
					public void onClick(DialogInterface dialog, int which)
					{
						numberList.set(index, Integer.parseInt(editText.getText().toString()));
						adapter.notifyDataSetChanged();
					}
				});

		final Dialog dialog = builder.create();
		dialog.show();

		editText.setText(String.valueOf(numberList.get(index)));
		editText.setOnEditorActionListener(new TextView.OnEditorActionListener()
		{
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event)
			{
				if (actionId == EditorInfo.IME_NULL || actionId == EditorInfo.IME_ACTION_SEND)
				{
					numberList.set(index, Integer.parseInt(v.getText().toString()));
					adapter.notifyDataSetChanged();
					dialog.dismiss();
					return true;
				}
				return false;
			}
		});
	}
}
