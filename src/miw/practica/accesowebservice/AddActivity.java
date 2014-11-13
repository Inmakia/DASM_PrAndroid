package miw.practica.accesowebservice;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

public class AddActivity extends Activity {
	
	private Registro registro;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add);
		
		Bundle extras = getIntent().getExtras();
		this.registro = new Registro();
		this.registro.setDni(extras.get("registro").toString());
		EditText dni = (EditText) findViewById(R.id.dniField);
		dni.setText(this.registro.getDni());
	}
	
	public JSONObject crearRegistro(){
		EditText nombre = (EditText) findViewById(R.id.nameField);
		EditText apellidos = (EditText) findViewById(R.id.lastnameField);
		EditText direccion = (EditText) findViewById(R.id.addressField);
		EditText telefono = (EditText) findViewById(R.id.phoneField);
		EditText equipo = (EditText) findViewById(R.id.teamField);
		this.registro.setNombre(nombre.getText().toString());
		this.registro.setApellidos(apellidos.getText().toString());
		this.registro.setDireccion(direccion.getText().toString());
		this.registro.setTelefono(telefono.getText().toString());
		this.registro.setEquipo(equipo.getText().toString());
		return this.registro.doRegistroJSON();
	}
	
	public void add(View v){
		String res = crearRegistro().toString();
		new InsercionBD().execute(res);
	}
	
	private class InsercionBD extends AsyncTask <String, Void, String> {
		
		private ProgressDialog pDialog;
		private final String URL = "http://demo.calamar.eui.upm.es/dasmapi/v1/miw25/fichas";
		
		protected void onPreExecute(){
			super.onPreExecute();
			pDialog = new ProgressDialog(AddActivity.this);
			pDialog.setMessage(getString(R.string.progress_title));
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();
		}
	
		@Override
		protected String doInBackground(String... params) {
			String respuesta = getString(R.string.sin_respuesta);
			try {
				AndroidHttpClient httpclient = AndroidHttpClient.newInstance("AndroidHttpClient");
				HttpPost httppost = new HttpPost(URL);
				StringEntity entity = new StringEntity(params[0]);
				httppost.setEntity(entity);
				httppost.setHeader("Accept", "application/json");
				httppost.setHeader("Content-type", "application/json");
				HttpResponse response = httpclient.execute(httppost);
				respuesta = EntityUtils.toString(response.getEntity());
				httpclient.close();
			} catch (Exception e){
				Log.e(getString(R.string.app_name), e.toString());
			}
			return respuesta;
		}
		
		@Override
		protected void onPostExecute(String respuesta){
			pDialog.dismiss();
			try {
				JSONArray resultado = new JSONArray(respuesta);
				int numreg = resultado.getJSONObject(0).getInt("NUMREG");
				if (numreg == 0 || numreg == 1){
					Intent intent = new Intent();
					setResult(RESULT_OK, intent);
			    	finish();
				}
				else if (numreg == -1) {
				 	Intent intent = new Intent();
				 	setResult(RESULT_CANCELED, intent);
			    	finish();
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}
	
	@Override
	public void onBackPressed() {
		setResult(RESULT_CANCELED);
		super.onBackPressed();
	}
}
