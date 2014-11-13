package miw.practica.accesowebservice;

import org.json.JSONException;
import org.json.JSONObject;

public class Registro {
	
	private String dni, nombre, apellidos, direccion, telefono, equipo;
	
	public Registro(JSONObject registro){
		try {
			this.dni = registro.get("DNI").toString();
			this.nombre = registro.get("Nombre").toString();
			this.apellidos = registro.get("Apellidos").toString();
			this.direccion = registro.get("Direccion").toString();
			this.telefono = registro.get("Telefono").toString();
			this.equipo = registro.get("Equipo").toString();
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	public Registro(){
	}

	public String getDni() {
		return dni;
	}

	public void setDni(String dni) {
		this.dni = dni;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getApellidos() {
		return apellidos;
	}

	public void setApellidos(String apellidos) {
		this.apellidos = apellidos;
	}

	public String getDireccion() {
		return direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public String getEquipo() {
		return equipo;
	}

	public void setEquipo(String equipo) {
		this.equipo = equipo;
	}
	
	public JSONObject doRegistroJSON(){
		JSONObject registro = new JSONObject();
		try {
			registro.put("DNI", this.dni);
			registro.put("Nombre", this.nombre);
			registro.put("Apellidos", this.apellidos);
			registro.put("Direccion", this.direccion);
			registro.put("Telefono", this.telefono);
			registro.put("Equipo", this.equipo);
		} catch (JSONException e) {
			e.printStackTrace();
		}	
		return registro;
	}
}
