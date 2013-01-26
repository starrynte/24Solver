package org.starrynte.solver24;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.os.AsyncTask;
import android.widget.ProgressBar;
import android.widget.TextView;

public class CalculateTask extends AsyncTask<int[], Object, Void>
{
	private final static double[] ITERATIONS = { 1, 8, 192, 7680, 430080, 30965760 };
	private final static byte OPER_ADD = 0, OPER_SUB = 1, OPER_MUL = 2, OPER_DIV = 3;
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
		int[] temp = params[0];
		int nTemp = temp.length - 1;
		double[] numbers = new double[temp.length];
		for (int i = 0; i <= nTemp; i++)
			numbers[i] = temp[i];
		Arrays.sort(numbers);

		int counter = 0;
		do
		{
			int dyck = (1 << (2 * nTemp - 1)) | (((1 << (nTemp - 1)) - 1) << (nTemp - 1)); //1 0 1^(t-1) 0^(t-1), t = nNumbers - 1
			do
			{
				byte[] operations = new byte[nTemp];
				do
				{
					List<Double> stack = new ArrayList<Double>();
					stack.add(0, numbers[0]);
					int numberIndex = 1, operIndex = 0;
					for (int i = 31 - Integer.numberOfLeadingZeros(dyck); i >= 0; i--)
					{
						if ((dyck & (1 << i)) == 0)
						{
							double a = stack.remove(1), b = stack.get(0);
							switch (operations[operIndex++])
							{
							case OPER_ADD:
								stack.set(0, a + b);
								break;
							case OPER_SUB:
								stack.set(0, a - b);
								break;
							case OPER_MUL:
								stack.set(0, a * b);
								break;
							case OPER_DIV:
								stack.set(0, a / b);
								break;
							}
						} else
						{
							stack.add(0, numbers[numberIndex++]);
						}
					}
					if (Math.abs(stack.remove(0) - 24) < 1E-4)
					{
						publishProgress((int) (1000 * counter / ITERATIONS[nTemp]), getString(numbers, dyck, operations));
					}

					counter++;
					if (counter % 100 == 0)
						publishProgress((int) (1000 * counter / ITERATIONS[nTemp]));
				} while (nextOperations(operations));
			} while ((dyck = nextDyck(dyck)) != 0);
		} while (nextPermutation(numbers));
		publishProgress(1000);
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

	//algorithm taken from wikipedia
	private boolean nextPermutation(double[] numbers)
	{
		//step 1
		int k = -1;
		for (int i = numbers.length - 2; i >= 0; i--)
		{
			if (numbers[i] < numbers[i + 1])
			{
				k = i;
				break;
			}
		}
		if (k == -1)
			return false;

		//step 2
		int l = numbers.length - 1;
		while (numbers[k] >= numbers[l])
			l--;

		//step 3
		swap(numbers, k, l);

		//step 4
		for (int i = 1; i <= (numbers.length - (k + 1)) / 2; i++)
		{
			swap(numbers, i + k, numbers.length - i);
		}
		return true;
	}

	private void swap(double[] numbers, int a, int b)
	{
		double temp = numbers[a];
		numbers[a] = numbers[b];
		numbers[b] = temp;
	}

	//algorithm taken from Table 1 of http://webhome.csc.uvic.ca/~haron/coolKat.pdf
	private int nextDyck(int dyck)
	{
		int i = 0, j = 0;
		for (int bit = Integer.highestOneBit(dyck); bit > 0; bit >>>= 1)
		{
			if (j == 0)
			{
				if ((dyck & bit) == bit)
					i++;
				else
					j++;
			} else
			{
				if ((dyck & bit) == 0)
				{
					j++;
				} else
				{
					int temp = 31 - Integer.numberOfLeadingZeros(dyck);
					if ((dyck & (bit >>>= 1)) == bit || (i == j))
					{
						return swap(dyck, temp - i, temp - (i + j));
					} else
					{
						return swap(swap(dyck, temp - 1, temp - i), temp - (i + j), temp - (i + j + 1));
					}
				}
			}
		}
		return 0;
	}

	//algorithm taken from http://graphics.stanford.edu/~seander/bithacks.html#SwappingBitsXOR
	private int swap(int dyck, int a, int b)
	{
		int x = ((dyck >> a) ^ (dyck >> b)) & 1;
		return dyck ^ ((x << a) | (x << b));
	}

	private boolean nextOperations(byte[] operations)
	{
		for (int i = 0; i < operations.length; i++)
		{
			if (++operations[i] <= OPER_DIV)
			{
				return true;
			} else
			{
				operations[i] = 0;
			}
		}
		return false;
	}

	private String getString(double[] numbers, int dyck, byte[] operations)
	{
		List<String> stack = new ArrayList<String>();
		stack.add(0, String.valueOf(numbers[0]));
		int numberIndex = 1, operIndex = 0;
		for (int i = 31 - Integer.numberOfLeadingZeros(dyck); i >= 0; i--)
		{
			if ((dyck & (1 << i)) == 0)
			{
				String a = stack.remove(1), b = stack.get(0);
				switch (operations[operIndex++])
				{
				case OPER_ADD:
					stack.set(0, "(" + a + " + " + b + ")");
					break;
				case OPER_SUB:
					stack.set(0, "(" + a + " - " + b + ")");
					break;
				case OPER_MUL:
					stack.set(0, "(" + a + " * " + b + ")");
					break;
				case OPER_DIV:
					stack.set(0, "(" + a + " / " + b + ")");
					break;
				}
			} else
			{
				stack.add(0, String.valueOf(numbers[numberIndex++]));
			}
		}
		return stack.remove(0);
	}

}
