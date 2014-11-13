package miw.practica.accesowebservice;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity {
	
	private final int ACTIVIDAD_CONSULTA = 001;
	private final int ACTIVIDAD_INSERCION = 002;
	private final int ACTIVIDAD_MODIFICACION = 003;
	private final int ACTIVIDAD_BORRADO = 004;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    
    public void consult(View v) {
		String busqueda = ((EditText)findViewById(R.id.main_dni)).getText().toString();
    	new ConsultaBD().execute(busqueda);
    }
    
    public void add (View v) {
    	String insercion = ((EditText)findViewById(R.id.main_dni)).getText().toString();
    	if (insercion.equals(getString(R.string.sin_respuesta))) {
			Toast.makeText(getBaseContext(), R.string.no_dni, Toast.LENGTH_LONG).show();
    	}
    	new InsercionBD().execute(insercion);
    }
    
    public void modify (View v) {
    	String modificacion = ((EditText)findViewById(R.id.main_dni)).getText().toString();
    	if (modificacion.equals(getString(R.string.sin_respuesta))) {
			Toast.makeText(getBaseContext(), R.string.no_dni, Toast.LENGTH_LONG).show();
    	}
    	new ModificacionBD().execute(modificacion);
    }
    
    public void delete (View v) {
    	String borrado =((EditText)findViewById(R.id.main_dni)).getText().toString();
    	if (borrado.equals(getString(R.string.sin_respuesta))) {
			Toast.makeText(getBaseContext(), R.string.no_dni, Toast.LENGTH_LONG).show();
    	}
    	new BorradoBD().execute(borrado);
    }

	private class ConsultaBD extends AsyncTask <String, Void, String> {
		
		private ProgressDialog pDialog;
		private final String URL = "http://demo.calamar.eui.upm.es/dasmapi/v1/miw25/fichas";
		
		protected void onPreExecute(){
			super.onPreExecute();
			pDialog = new ProgressDialog(MainActivity.this);
			pDialog.setMessage(getString(R.string.progress_title));
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();
		}
	
		@Override
		protected String doInBackground(String... params) {
			String respuesta = getString(R.string.sin_respuesta);
			String url = URL;
			if(!params[0].equals("")){
				url +="/"+ params[0];
			}
			try {
				AndroidHttpClient httpclient = AndroidHttpClient.newInstance("AndroidHttpClient");
				HttpGet httpget = new HttpGet(url);
				HttpResponse response = httpclient.execute(httpget);
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
				if (resultado.getJSONObject(0).get("NUMREG").equals(0)){
					Toast.makeText(getBaseContext(), R.string.no_reg, Toast.LENGTH_LONG).show();
				}
				else if (resultado.getJSONObject(0).getInt("NUMREG") > 0) {
				 	Intent intent = new Intent(MainActivity.this, ConsultActivity.class);
			    	intent.putExtra("registro", respuesta);
			    	startActivityForResult(intent, ACTIVIDAD_CONSULTA);
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}
		
	private class InsercionBD extends AsyncTask <String, Void, String> {
			
			private ProgressDialog pDialog;
			private final String URL = "http://demo.calamar.eui.upm.es/dasmapi/v1/miw25/fichas";
			
			protected void onPreExecute(){
				super.onPreExecute();
				pDialog = new ProgressDialog(MainActivity.this);
				pDialog.setMessage(getString(R.string.progress_title));
				pDialog.setIndeterminate(false);
				pDialog.setCancelable(true);
				pDialog.show();
			}
		
			@Override
			protected String doInBackground(String... params) {
				String respuesta = getString(R.string.sin_respuesta);
				String url = URL + "/"+ params[0];
				try {
					AndroidHttpClient httpclient = AndroidHttpClient.newInstance("AndroidHttpClient");
					HttpGet httpget = new HttpGet(url);
					HttpResponse response = httpclient.execute(httpget);
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
					if (resultado.getJSONObject(0).getInt("NUMREG") > 0){
						Toast.makeText(getBaseContext(), R.string.exist_dni, Toast.LENGTH_LONG).show();
					}
					else if (resultado.getJSONObject(0).getInt("NUMREG") == 0) {
					 	Intent intent = new Intent(MainActivity.this, AddActivity.class);
				    	intent.putExtra("registro", ((EditText)findViewById(R.id.main_dni)).getText().toString());
				    	startActivityForResult(intent, ACTIVIDAD_INSERCION);
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
	}

	private class BorradoBD extends AsyncTask <String, Void, String> {
		
		private ProgressDialog pDialog;
		private final String URL = "http://demo.calamar.eui.upm.es/dasmapi/v1/miw25/fichas";
		
		protected void onPreExecute(){
			super.onPreExecute();
			pDialog = new ProgressDialog(MainActivity.this);
			pDialog.setMessage(getString(R.string.progress_title));
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();
		}
	
		@Override
		protected String doInBackground(String... params) {
			String respuesta = getString(R.string.sin_respuesta);
			String url = URL + "/"+ params[0];
			try {
				AndroidHttpClient httpclient = AndroidHttpClient.newInstance("AndroidHttpClient");
				HttpGet httpget = new HttpGet(url);
				HttpResponse response = httpclient.execute(httpget);
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
				if (resultado.getJSONObject(0).getInt("NUMREG") == 0){
					Toast.makeText(getBaseContext(), R.string.no_reg, Toast.LENGTH_LONG).show();
				}
				else if (resultado.getJSONObject(0).getInt("NUMREG") != 0) {
				 	Intent intent = new Intent(MainActivity.this, DeleteActivity.class);
			    	intent.putExtra("registro", respuesta);
			    	startActivityForResult(intent, ACTIVIDAD_BORRADO);
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
}
	
	private class ModificacionBD extends AsyncTask <String, Void, String> {
		
		private ProgressDialog pDialog;
		private final String URL = "http://demo.calamar.eui.upm.es/dasmapi/v1/miw25/fichas";
		
		protected void onPreExecute(){
			super.onPreExecute();
			pDialog = new ProgressDialog(MainActivity.this);
			pDialog.setMessage(getString(R.string.progress_title));
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();
		}
	
		@Override
		protected String doInBackground(String... params) {
			String respuesta = getString(R.string.sin_respuesta);
			String url = URL + "/"+ params[0];
			try {
				AndroidHttpClient httpclient = AndroidHttpClient.newInstance("AndroidHttpClient");
				HttpGet httpget = new HttpGet(url);
				HttpResponse response = httpclient.execute(httpget);
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
				if (resultado.getJSONObject(0).getInt("NUMREG") == 0){
					Toast.makeText(getBaseContext(), R.string.no_reg, Toast.LENGTH_LONG).show();
				}
				else if (resultado.getJSONObject(0).getInt("NUMREG") != 0) {
				 	Intent intent = new Intent(MainActivity.this, ModifyActivity.class);
			    	intent.putExtra("registro", respuesta);
			    	startActivityForResult(intent, ACTIVIDAD_MODIFICACION);
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
}
	
	@Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
    	String newMensaje = "Operación cancelada";
    	if (requestCode==ACTIVIDAD_CONSULTA) {
    		if (resultCode==RESULT_CANCELED) {
    			newMensaje = "Consulta finalizada";
    		}
    	} else if (requestCode==ACTIVIDAD_INSERCION) {
    		if (resultCode==RESULT_OK) {
    			newMensaje = "Inserción realizada";
    		} else if (resultCode==RESULT_CANCELED) {
    			newMensaje = "Inserción cancelada";
    		}
    	} else if (requestCode==ACTIVIDAD_MODIFICACION) {
    		if (resultCode==RESULT_OK) {
    			newMensaje = "Actualización realizada";
    		} else if (resultCode==RESULT_CANCELED) {
    			newMensaje = "Actualización cancelada";
    		}
    	} else if (requestCode==ACTIVIDAD_BORRADO) {
    		if (resultCode==RESULT_OK) {
    			newMensaje = "Borrado realizado";
    		} else if (resultCode==RESULT_CANCELED) {
    			newMensaje = "Borrado cancelado";
    		}
    	}
		Toast.makeText(this, newMensaje, Toast.LENGTH_LONG).show();
    }
}