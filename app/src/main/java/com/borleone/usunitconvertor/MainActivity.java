package com.borleone.usunitconvertor;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Random;

public class MainActivity extends Activity implements OnClickListener {

	int ex = 0;
	Button w, v, d, t, con1, con2;
	EditText e1, e2, e3, e4;
	TextView t1, t2, t3;
	RelativeLayout layout;
	TableLayout tlayout;
	View line;
	double usd, inr, a, b;
	int i, j;
	String currencySelected;
	double[][] dist = { { 25.4, 2.54, 0.0254, 0.0000254, 1, 0.0833333, 0.0277777, 0.00001578 }, { 304.8, 30.48, 0.3048, 0.0003048, 12, 1, 0.3333333, 0.0001894 },
			{ 914.4, 91.44, 0.9144, 0.0009144, 36, 3, 1, 0.0005682 }, { 1609344, 160934.4, 1609.344, 1.609344, 63360, 5280, 1760, 1 } };
	double[][] vol = { { 29.5735295, 0.0295735, 0.00002957, 1, 0.0625, 0.0078125, 0.000248 }, { 473.176473, 0.4731765, 0.00047317, 16, 1, 0.125, 0.00396825 },
			{ 3785.411784, 3.7854118, 0.0037854, 128, 8, 1, 0.031746 }, { 119240.47119, 119.2404712, 0.1192404, 4032, 252, 31.5, 1 } };
	double[][] wt = { { 453592.37, 453.59237, 0.45359237, 1, 16, 0.000453592, 0.0005 }, { 28349.523125, 28.349523, 0.0283495, 0.0625, 1, 0.00002835, 0.00003125 },
			{ 1000000000, 1000000, 1000, 2204.6226218, 35273.961949, 1, 1.1023113 }, { 907184740, 907184.74, 907.18474, 2000, 32000, 0.90718474, 1 } };
	Spinner s1, s2, s3;
	static float rate;
	NumberFormat formatter = new DecimalFormat("#0.00");
	NumberFormat formatters = new DecimalFormat("#0.000000");
	public Boolean flag;

	// InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
	// imm.hideSoftInputFromWindow(layout.getWindowToken(), 0);

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		layout = new RelativeLayout(this);
		tlayout = new TableLayout(this);
		w = new Button(this);
		v = new Button(this);
		d = new Button(this);
		t = new Button(this);
		con1 = new Button(this);
		con2 = new Button(this);
		e1 = new EditText(this);
		e2 = new EditText(this);
		e3 = new EditText(this);
		e4 = new EditText(this);
		s1 = new Spinner(this);
		s2 = new Spinner(this);
		s3 = new Spinner(this);
		t3 = new TextView(this);
		line = new View(this);

		layout = (RelativeLayout) findViewById(R.id.layout);
		tlayout = (TableLayout) findViewById(R.id.tableLayout1);
		w = (Button) findViewById(R.id.b1);
		v = (Button) findViewById(R.id.b2);
		d = (Button) findViewById(R.id.b3);
		t = (Button) findViewById(R.id.b4);
		con1 = (Button) findViewById(R.id.convert1);
		con2 = (Button) findViewById(R.id.convert2);
		e1 = (EditText) findViewById(R.id.editText1);
		e2 = (EditText) findViewById(R.id.editText2);
		e3 = (EditText) findViewById(R.id.editText11);
		e4 = (EditText) findViewById(R.id.editText22);
		t2 = (TextView) findViewById(R.id.textView1);
		line = (View) findViewById(R.id.line);

		flag = true;

		Random rand = new Random(System.currentTimeMillis());

		int r, g, b;
		r = rand.nextInt(256);
		g = rand.nextInt(256);
		b = rand.nextInt(256);
		String color = "#";
		if (r < 16)
			color += "0" + Integer.toHexString(r);
		else
			color += Integer.toHexString(r);
		if (g < 16)
			color += "0" + Integer.toHexString(g);
		else
			color += Integer.toHexString(g);
		if (b < 16)
			color += "0" + Integer.toHexString(b);
		else
			color += Integer.toHexString(b);

		Log.d("random color", color);
		// t2.setTextColor(Color.parseColor("#" + Integer.toHexString(r) + Integer.toHexString(g) + Integer.toHexString(b)));
		// t2.setTextColor(Color.parseColor(color));
		layout.setBackgroundColor(Color.parseColor(color));
		tlayout.setBackgroundColor(Color.parseColor(color));

		t3 = (TextView) findViewById(R.id.rate);
		e1.setText("1");
		s1 = (Spinner) findViewById(R.id.US);
		s2 = (Spinner) findViewById(R.id.IN);
		s3 = (Spinner) findViewById(R.id.currency);
		s1.setBackgroundResource(R.drawable.gradient_bg);
		s2.setBackgroundResource(R.drawable.gradient_bg);
		s3.setBackgroundResource(R.drawable.gradient_bg);

		// ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, R.array.currency);
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.currency, R.layout.spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		s3.setAdapter(adapter);

		SharedPreferences currency = getSharedPreferences("CurrentRate", MODE_PRIVATE);
		i = currency.getInt("cur", 0);
		currencySelected = currency.getString("currency", "");
		s3.setSelection(i);

		// LayoutInflater linflater = null; // =new LayoutInflater(this);
		// View view = linflater.inflate(R.layout.spinner_item, null);
		// TextView tv = (TextView) view.findViewById(android.R.id.text1);
		// tv.setSingleLine();
		// tv.setEllipsize(TruncateAt.MARQUEE);
		// tv.setMarqueeRepeatLimit(-1);
		// tv.setSelected(true);

		DisplayMetrics displayMetrics = this.getResources().getDisplayMetrics();
		float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
		int Height = displayMetrics.heightPixels;

		line.getLayoutParams().width = (int) ((dpWidth - 50) * (displayMetrics.density));
		System.out.println(dpWidth);
		if (dpWidth > 360) {
			w.setText("Weight");
			v.setText("Volume");
			d.setText("Distance");
			t.setText("Temp");
		} else if (dpWidth < 330) {
			s1.setGravity(Gravity.LEFT);
			s2.setGravity(Gravity.LEFT);
			s3.getLayoutParams().width -= 10;
		} else if (dpWidth < 300) {
			s1.setGravity(Gravity.LEFT);
			s2.setGravity(Gravity.LEFT);
			s3.getLayoutParams().width -= 10;
			w.setText("Wt");
			v.setText("Vol");
			d.setText("Dist");
		}
		Log.d("ht", layout.getLayoutParams().width + " " + Height + " " + displayMetrics.widthPixels + " " + displayMetrics.density);

		int result = 0;
		int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
		if (resourceId > 0) {
			result = getResources().getDimensionPixelSize(resourceId);
		}

		if (layout.getLayoutParams().height < Height)
			layout.getLayoutParams().height = Height - result;

		w.setOnClickListener(this);
		v.setOnClickListener(this);
		d.setOnClickListener(this);
		t.setOnClickListener(this);
		con1.setOnClickListener(this);
		con2.setOnClickListener(this);
		// s1.setOnItemSelectedListener(this);
		// s2.setOnItemSelectedListener(this);

		e3.setVisibility(View.INVISIBLE);
		e4.setVisibility(View.INVISIBLE);
		t3.setVisibility(View.INVISIBLE);

		if (android.os.Build.VERSION.SDK_INT > 9) {
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
			StrictMode.setThreadPolicy(policy);
		}

		convert(1);
		e1.requestFocus();
	}

	@Override
	public void onClick(View view) {
		flag = false;

		switch (view.getId()) {
		case R.id.convert1: {

			if (ex == 0)
				convert(1);
			else if (ex == 1) {
				a = Float.parseFloat(e3.getText().toString());
				i = s1.getSelectedItemPosition();
				j = s2.getSelectedItemPosition();
				b = a * wt[i][j];
				e4.setText(formatters.format(b)); // String.valueOf()
				convert(1);
				if (t3.getVisibility() == View.INVISIBLE)
					t3.setVisibility(View.VISIBLE);

				// if (currencySelected.equals("INR"))
				// t3.setText("Rs." + (Float.valueOf(e2.getText().toString()) / Float.valueOf(e4.getText().toString())) + "/" + s2.getSelectedItem().toString());
				// else
				t3.setText(currencySelected + " " + formatter.format((Float.valueOf(e2.getText().toString()) / Float.valueOf(e4.getText().toString()))) + "/"
						+ s2.getSelectedItem().toString());
			} else if (ex == 2) {
				a = Float.parseFloat(e3.getText().toString());
				i = s1.getSelectedItemPosition();
				j = s2.getSelectedItemPosition();
				b = a * vol[i][j];
				e4.setText(formatters.format(b));
				convert(1);
				if (t3.getVisibility() == View.INVISIBLE)
					t3.setVisibility(View.VISIBLE);

				// if (currencySelected.equals("INR"))
				// t3.setText("Rs." + (Float.valueOf(e2.getText().toString()) / Float.valueOf(e4.getText().toString())) + "/" + s2.getSelectedItem().toString());
				// else
				t3.setText(currencySelected + " " + formatter.format((Float.valueOf(e2.getText().toString()) / Float.valueOf(e4.getText().toString()))) + "/"
						+ s2.getSelectedItem().toString());
			} else if (ex == 3) {
				if (t3.getVisibility() == View.VISIBLE)
					t3.setVisibility(View.INVISIBLE);

				a = Float.parseFloat(e3.getText().toString());
				i = s1.getSelectedItemPosition();
				j = s2.getSelectedItemPosition();
				b = a * dist[i][j];
				e4.setText(formatters.format(b));
				convert(1);
			} else if (ex == 4) {
				if (t3.getVisibility() == View.VISIBLE)
					t3.setVisibility(View.INVISIBLE);

				a = Float.parseFloat(e3.getText().toString());
				b = (a - 32) * 5 / 9;
				e4.setText(formatter.format(b).toString());
				convert(1);
			}

			InputMethodManager imm = (InputMethodManager) getSystemService(this.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(layout.getWindowToken(), 0);

			break;
		}

		case R.id.convert2: {

			if (ex == 0)
				convert(2);
			else if (ex == 1) {
				a = Float.parseFloat(e4.getText().toString());
				i = s1.getSelectedItemPosition();
				j = s2.getSelectedItemPosition();
				b = a * 1 / wt[i][j];
				e3.setText(formatters.format(b));
				convert(2);
				if (t3.getVisibility() == View.INVISIBLE)
					t3.setVisibility(View.VISIBLE);

				// if (currencySelected.equals("INR"))
				// t3.setText("Rs." + (Float.valueOf(e2.getText().toString()) / Float.valueOf(e4.getText().toString())) + "/" + s2.getSelectedItem().toString());
				// else
				t3.setText(currencySelected + " " + formatter.format((Float.valueOf(e2.getText().toString()) / Float.valueOf(e4.getText().toString()))) + "/"
						+ s2.getSelectedItem().toString());
			} else if (ex == 2) {
				a = Float.parseFloat(e4.getText().toString());
				i = s1.getSelectedItemPosition();
				j = s2.getSelectedItemPosition();
				b = a * 1 / vol[i][j];
				e3.setText(formatters.format(b));
				convert(2);
				if (t3.getVisibility() == View.INVISIBLE)
					t3.setVisibility(View.VISIBLE);

				// if (currencySelected.equals("INR"))
				// t3.setText("Rs." + (Float.valueOf(e2.getText().toString()) / Float.valueOf(e4.getText().toString())) + "/" + s2.getSelectedItem().toString());
				// else
				t3.setText(currencySelected + " " + formatter.format((Float.valueOf(e2.getText().toString()) / Float.valueOf(e4.getText().toString()))) + "/"
						+ s2.getSelectedItem().toString());
			} else if (ex == 3) {
				if (t3.getVisibility() == View.VISIBLE)
					t3.setVisibility(View.INVISIBLE);

				a = Float.parseFloat(e4.getText().toString());
				i = s1.getSelectedItemPosition();
				j = s2.getSelectedItemPosition();
				b = a * 1 / dist[i][j];
				e3.setText(formatters.format(b));
				convert(2);
			} else if (ex == 4) {
				if (t3.getVisibility() == View.VISIBLE)
					t3.setVisibility(View.INVISIBLE);

				a = Float.parseFloat(e4.getText().toString());
				b = a * 9 / 5 + 32;
				e3.setText(formatter.format(b).toString());
				convert(2);
			}

			InputMethodManager imm = (InputMethodManager) getSystemService(this.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(layout.getWindowToken(), 0);

			break;
		}

		case R.id.b1: {
			// w.setGravity(Gravity.CENTER_HORIZONTAL);
			// v.setGravity(Gravity.CENTER);
			// d.setGravity(Gravity.CENTER);
			// t.setGravity(Gravity.CENTER);

			e3.setVisibility(View.VISIBLE);
			e4.setVisibility(View.VISIBLE);
			ex = 1;

			ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(this, R.array.wt1, R.layout.spinner_item1);
			adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			s1.setAdapter(adapter1);
			adapter1 = ArrayAdapter.createFromResource(this, R.array.wt2, R.layout.spinner_item1);
			adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			s2.setAdapter(adapter1);
			s2.setSelection(1);
			e3.setText("1");

			a = Float.parseFloat(e3.getText().toString());
			b = a * 453.59237;
			e4.setText(formatters.format(b));

			if (t3.getVisibility() == View.INVISIBLE)
				t3.setVisibility(View.VISIBLE);

			t3.setText(currencySelected + " " + formatter.format((Float.valueOf(e2.getText().toString()) / Float.valueOf(e4.getText().toString()))) + "/"
					+ s2.getSelectedItem().toString());

			break;
		}

		case R.id.b2: {
			// v.setGravity(Gravity.CENTER_HORIZONTAL);
			// w.setGravity(Gravity.CENTER);
			// d.setGravity(Gravity.CENTER);
			// t.setGravity(Gravity.CENTER);

			e3.setVisibility(View.VISIBLE);
			e4.setVisibility(View.VISIBLE);
			ex = 2;

			ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(this, R.array.vol1, R.layout.spinner_item1);
			adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			s1.setAdapter(adapter1);
			adapter1 = ArrayAdapter.createFromResource(this, R.array.vol2, R.layout.spinner_item1);
			adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			s2.setAdapter(adapter1);
			s2.setSelection(1);
			s1.setSelection(2);
			e3.setText("1");

			a = Float.parseFloat(e3.getText().toString());
			b = a * 3.78541;
			e4.setText(formatters.format(b));

			if (t3.getVisibility() == View.INVISIBLE)
				t3.setVisibility(View.VISIBLE);

			t3.setText(currencySelected + " " + formatter.format((Float.valueOf(e2.getText().toString()) / Float.valueOf(e4.getText().toString()))) + "/"
					+ s2.getSelectedItem().toString());
			break;
		}

		case R.id.b3: {
			// d.setGravity(Gravity.CENTER_HORIZONTAL);
			// v.setGravity(Gravity.CENTER);
			// w.setGravity(Gravity.CENTER);
			// t.setGravity(Gravity.CENTER);

			if (t3.getVisibility() == View.VISIBLE)
				t3.setVisibility(View.INVISIBLE);

			e3.setVisibility(View.VISIBLE);
			e4.setVisibility(View.VISIBLE);
			ex = 3;

			ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(this, R.array.dist1, R.layout.spinner_item1);
			adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			s1.setAdapter(adapter1);
			adapter1 = ArrayAdapter.createFromResource(this, R.array.dist2, R.layout.spinner_item1);
			adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			s2.setAdapter(adapter1);
			s2.setSelection(3);
			s1.setSelection(3);
			e3.setText("1");

			a = Float.parseFloat(e3.getText().toString());
			b = a * 1.609344;
			e4.setText(formatters.format(b));
			break;
		}

		case R.id.b4: {
			// t.setGravity(Gravity.CENTER_HORIZONTAL);
			// v.setGravity(Gravity.CENTER);
			// d.setGravity(Gravity.CENTER);
			// w.setGravity(Gravity.CENTER);

			if (t3.getVisibility() == View.VISIBLE)
				t3.setVisibility(View.INVISIBLE);

			e3.setVisibility(View.VISIBLE);
			e4.setVisibility(View.VISIBLE);
			ex = 4;

			ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(this, R.array.temperature1, R.layout.spinner_item1);
			adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			s1.setAdapter(adapter1);
			ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this, R.array.temperature2, R.layout.spinner_item1);
			adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			s2.setAdapter(adapter2);
			e3.setText("1");

			a = Float.parseFloat(e3.getText().toString());
			b = (a - 32) * 5 / 9;
			e4.setText(formatter.format(b).toString());
			break;
		}

		}
	}

	// private class fetch extends AsyncTask<String, Void, String> {

	public String getJson(String url) throws ClientProtocolException, IOException {

		StringBuilder build = new StringBuilder();
		HttpClient client = new DefaultHttpClient();
		HttpGet httpGet = new HttpGet(url);

		// Check if net speed is slow
		HttpParams params = client.getParams();
		HttpConnectionParams.setConnectionTimeout(params, 3000);
		HttpConnectionParams.setSoTimeout(params, 3000);

		HttpResponse response = client.execute(httpGet);
		HttpEntity entity = response.getEntity();
		InputStream content = entity.getContent();
		BufferedReader reader = new BufferedReader(new InputStreamReader(content));
		String con;
		while ((con = reader.readLine()) != null) {
			build.append(con);
		}
		return build.toString();
	}

	public void convert(int a) {

		i = s3.getSelectedItemPosition();
		currencySelected = String.format("%.3s", s3.getSelectedItem().toString());
		Log.d("cur", currencySelected);

		try {
			String s = getJson("http://query.yahooapis.com/v1/public/yql?q=select%20*%20from%20yahoo.finance.xchange%20where%20pair%20in%20(%22USD" + currencySelected
					+ "%22)&format=json&diagnostics=true&env=store%3A%2F%2Fdatatables.org%2Falltableswithkeys&callback=");
			JSONObject jObj;
			jObj = new JSONObject(s);
			String exResult = jObj.getJSONObject("query").getJSONObject("results").getJSONObject("rate").getString("Rate");
			inr = Float.parseFloat(exResult);
			rate = (float) inr;
			SharedPreferences currency = getSharedPreferences("CurrentRate", MODE_PRIVATE);
			SharedPreferences.Editor currEditor = currency.edit();
			currEditor.putFloat("USDtoINR", rate);
			currEditor.putInt("cur", i);
			currEditor.putString("currency", currencySelected);
			currEditor.commit();

			Log.d("exResult", exResult + rate);
			if (a == 1) {
				String u = e1.getText().toString();
				try {
					usd = Float.parseFloat(u);
				} catch (NumberFormatException e) {
					usd = 0;
				}
				inr *= usd;
				e2.setText(formatter.format(inr).toString());
			} else {
				String u = e2.getText().toString();
				try {
					usd = Float.parseFloat(u);
				} catch (NumberFormatException e) {
					usd = 0;
				}
				usd /= inr;
				e1.setText(formatter.format(usd).toString());
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.d("error1", e.toString());
			if (flag == true)
				Toast.makeText(getApplicationContext(), "Internet not connected", Toast.LENGTH_SHORT).show();

			SharedPreferences currency = getSharedPreferences("CurrentRate", MODE_PRIVATE);
			if (a == 1) {
				rate = currency.getFloat("USDtoINR", 60);
				String u = e1.getText().toString();
				try {
					usd = Float.parseFloat(u);
				} catch (NumberFormatException e1) {
					usd = 0;
				}
				rate *= usd;
				e2.setText(formatter.format(rate).toString());
			} else {
				rate = currency.getFloat("USDtoINR", 60);
				String u = e2.getText().toString();
				try {
					usd = Float.parseFloat(u);
				} catch (NumberFormatException e1) {
					usd = 0;
				}
				usd /= inr;
				e1.setText(formatter.format(usd).toString());
			}
		}
	}

	// @Override
	// protected String doInBackground(String... params) {
	// // TODO Auto-generated method stub
	// return null;
	// }
	// }
}
