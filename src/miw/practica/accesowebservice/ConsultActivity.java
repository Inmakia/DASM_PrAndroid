package miw.practica.accesowebservice;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class ConsultActivity extends Activity {
	
	private ArrayList<Registro> registros;
	private int contador = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_consult);

		Bundle extras = getIntent().getExtras();
		JSONArray resultado;
		try {
			resultado = new JSONArray(extras.get("registro").toString());

			this.registros = new ArrayList<Registro>();
			Registro reg;
			for (int i = 1; i <= resultado.getJSONObject(0).getInt("NUMREG"); i++) {
				reg = new Registro(resultado.getJSONObject(i));
				registros.add(reg);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		showRegistro();
	}
	
	public void previous (View v) {
		if (this.contador != 0) {
			this.contador = this.contador - 1;
			showRegistro();
		}
	}
	
	public void next (View v) {
		if (this.contador != this.registros.size()-1) {
			this.contador = this.contador + 1;
			showRegistro();
		}
	}
	
	public void goFirst (View v) {
		this.contador = 0;
		showRegistro();
	}
	
	public void goLast (View v) {
		this.contador = this.registros.size()-1;
		showRegistro();
	}
	
	private void showRegistro(){
		TextView regCount = (TextView) findViewById(R.id.contador);
		regCount.setText("registro "+(contador+1) +" de "+this.registros.size());
		EditText dni = (EditText) findViewById(R.id.dniField);
		EditText nombre = (EditText) findViewById(R.id.nameField);
		EditText apellidos = (EditText) findViewById(R.id.lastnameField);
		EditText direccion = (EditText) findViewById(R.id.addressField);
		EditText telefono = (EditText) findViewById(R.id.phoneField);
		EditText equipo = (EditText) findViewById(R.id.teamField);
		dni.setText(this.registros.get(contador).getDni());
		nombre.setText(this.registros.get(contador).getNombre());
		apellidos.setText(this.registros.get(contador).getApellidos());
		direccion.setText(this.registros.get(contador).getDireccion());
		telefono.setText(this.registros.get(contador).getTelefono());
		equipo.setText(this.registros.get(contador).getEquipo());
	}
	
	@Override
	public void onBackPressed() {
		setResult(RESULT_CANCELED);
		super.onBackPressed();
	}
}
