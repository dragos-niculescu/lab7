package ro.pub.cs.pdsd.lab7;

import java.util.ArrayList;
import java.util.List;

//import android.R;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends Activity {

	
	DataB  ang_db; 
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		ang_db = new DataB(getBaseContext());
		
		TextView text = (TextView)findViewById(R.id.text1);
		text.setText(ang_db.getAngajati().toString()); 
	}

	



	public class Angajati {
		private int _id;
		private String _nume;
		private String _departament;

		public Angajati(){}

		// constructor
		public Angajati(int id, String nume, String _departament){
			this._id = id;
			this._nume = nume;
			this._departament = _departament;
		}

		// constructor
		public Angajati(String nume, String _departament){
			this._nume = nume;
			this._departament = _departament;
		}

		public int get_id() {
			return _id;
		}

		public void set_id(int _id) {
			this._id = _id;
		}

		public String get_nume() {
			return _nume;
		}

		public void set_nume(String _nume) {
			this._nume = _nume;
		}

		public String get_departament() {
			return _departament;
		}

		public void set_departament(String _departament) {
			this._departament = _departament;
		}
		
		@Override
		public String toString(){
			return get_id() + ": " + get_nume() + " " + get_departament();
		}
	}


	public class DataB extends SQLiteOpenHelper {
		// Versiune BD
		private static final int VERSIUNE_BD = 1;

		// Nume BD
		private static final String NUME_BD = "Angajati_Firma";

		// Tabela Angajati
		private static final String TABELA_ANGAJATI = "angajati";

		// coloanele tabelei Angajati
		private static final String ID_KEY = "id";
		private static final String NUME_KEY = "nume";
		private static final String DEPARTAMENT_KEY = "departament";


		public DataB(Context context) {
			super(context, NUME_BD, null, VERSIUNE_BD);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			String CREATE_EMPLOYERS_TABLE = "CREATE TABLE " + TABELA_ANGAJATI + "("
					+ ID_KEY + " INTEGER PRIMARY KEY, " + NUME_KEY + " TEXT,"
					+ DEPARTAMENT_KEY + " TEXT" + ")";
			db.execSQL(CREATE_EMPLOYERS_TABLE);
		}	

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			db.execSQL("DROP TABLE IF EXISTS " + TABELA_ANGAJATI);

			// Crează din nou tabela
			onCreate(db);

		}

		// Adaugare angajat
		public void addAngajat(Angajati angajat) {
			SQLiteDatabase db = this.getWritableDatabase();

			ContentValues valori = new ContentValues();
			valori.put(NUME_KEY, angajat.get_nume()); // Nume Angajat
			valori.put(DEPARTAMENT_KEY, angajat.get_departament()); // Departament angajat

			// Inserare linie
			db.insert(TABELA_ANGAJATI, null, valori);
			//Inchiderea conexiunii cu baza de date 
			db.close(); 
		}
		
		Angajati getAngajat(int id) {
			SQLiteDatabase db = this.getReadableDatabase();

			Cursor cursor = db.query(TABELA_ANGAJATI, new String[] { ID_KEY,
					NUME_KEY, DEPARTAMENT_KEY }, ID_KEY + "=?",
					new String[] { String.valueOf(id) }, null, null, null, null);

			if (cursor != null)
				cursor.moveToFirst();

			Angajati angajat = new Angajati(Integer.parseInt(cursor.getString(0)),
					cursor.getString(1), cursor.getString(2));
			//Returneaza angajat
			return angajat;
		}
		
		//Toti angajatii
		public List<Angajati> getAngajati() {
			List<Angajati> angajatiList = new ArrayList<Angajati>();

			// interogare
			String selectQuery = "SELECT  * FROM " + TABELA_ANGAJATI;

			SQLiteDatabase db = this.getWritableDatabase();
			Cursor cursor = db.rawQuery(selectQuery, null);

			// face loop printre linii si adauga in lista
			if (cursor.moveToFirst()) { 
				do {
					Angajati angajat = new Angajati();
					angajat.set_id(Integer.parseInt(cursor.getString(0)));
					angajat.set_nume(cursor.getString(1));
					angajat.set_departament(cursor.getString(2));


					// Adauga unui angajat in lista
					angajatiList.add(angajat);
				} while (cursor.moveToNext());
			}

			// returneaza lista de angajati
			return angajatiList;
		}

		// Update angajat
		public int updateAngajat(Angajati angajat) {
			SQLiteDatabase db = this.getWritableDatabase();

			ContentValues values = new ContentValues();
			values.put(NUME_KEY, angajat.get_nume());
			values.put(DEPARTAMENT_KEY, angajat.get_departament());

			// update linie
			return db.update(TABELA_ANGAJATI, values, ID_KEY + " = ?",
					new String[] { String.valueOf(angajat.get_id()) });
		}


		public void stergeAngajat(Angajati angajat) {
			SQLiteDatabase db = this.getWritableDatabase();
			db.delete(TABELA_ANGAJATI, ID_KEY + " = ?",
					new String[] { String.valueOf(angajat.get_id()) });
			db.close();
		}
	    
	    

	}// DATAB

} // ACTIVITY

