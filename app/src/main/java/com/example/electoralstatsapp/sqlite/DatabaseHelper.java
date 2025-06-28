package com.example.electoralstatsapp.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.electoralstatsapp.sqlite.models.BureauDeVote;
import com.example.electoralstatsapp.sqlite.models.Candidat;
import com.example.electoralstatsapp.sqlite.models.CentreDeVote;
import com.example.electoralstatsapp.sqlite.models.Circonscription;
import com.example.electoralstatsapp.sqlite.models.Election;
import com.example.electoralstatsapp.sqlite.models.Resultat;
import com.example.electoralstatsapp.sqlite.models.ResultatView;
import com.example.electoralstatsapp.sqlite.models.User;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "electoral_stats.db";
    private static final int DATABASE_VERSION = 3;

    // Table Names
    public static final String TABLE_USERS = "users";
    public static final String TABLE_ELECTIONS = "elections";
    public static final String TABLE_CANDIDATS = "candidats";
    public static final String TABLE_CIRCONSCRIPTIONS = "circonscriptions";
    public static final String TABLE_CENTRES_VOTE = "centres_vote";
    public static final String TABLE_BUREAUX_VOTE = "bureaux_vote";
    public static final String TABLE_RESULTATS = "resultats";
    public static final String TABLE_NOTIFICATIONS = "notifications";
    public static final String TABLE_RAPPORTS = "rapports";

    // Common column names
    public static final String KEY_ID = "id";

    // USERS Table - column names
    public static final String KEY_USER_EMAIL = "email";
    public static final String KEY_USER_PASSWORD = "password";
    public static final String KEY_USER_FULL_NAME = "full_name";
    public static final String KEY_USER_ROLE = "role";

    // ELECTION Table
    public static final String KEY_ELECTION_TYPE = "type";
    public static final String KEY_ELECTION_DATE = "date_scrutin";
    public static final String KEY_ELECTION_STATUT = "statut";
    public static final String KEY_ELECTION_NB_TOURS = "nb_tours";

    // CANDIDAT Table
    public static final String KEY_CANDIDAT_NOM = "nom";
    public static final String KEY_CANDIDAT_PRENOM = "prenom";
    public static final String KEY_CANDIDAT_PARTI = "parti";
    public static final String KEY_CANDIDAT_BIO = "biographie";
    public static final String KEY_CANDIDAT_PHOTO = "photo";
    public static final String KEY_CANDIDAT_SAISI_PAR = "saisi_par";

    // CIRCONSCRIPTION Table
    public static final String KEY_CIRCONSCRIPTION_NOM = "nom";
    public static final String KEY_CIRCONSCRIPTION_REGION = "region";
    public static final String KEY_CIRCONSCRIPTION_NB_INSCRITS = "nb_inscrits";

    // CENTRE_VOTE Table
    public static final String KEY_CENTRE_NOM = "nom";
    public static final String KEY_CENTRE_ADRESSE = "adresse";
    public static final String KEY_CENTRE_LATITUDE = "latitude";
    public static final String KEY_CENTRE_LONGITUDE = "longitude";
    public static final String KEY_CENTRE_CIRCONSCRIPTION_ID = "circonscription_id";

    // BUREAU_VOTE Table
    public static final String KEY_BUREAU_NUMERO = "numero";
    public static final String KEY_BUREAU_NB_INSCRITS = "nb_inscrits";
    public static final String KEY_BUREAU_NB_VOTANTS = "nb_votants";
    public static final String KEY_BUREAU_CENTRE_ID = "centre_vote_id";

    // RESULTAT Table
    public static final String KEY_RESULTAT_NB_VOIX = "nb_voix";
    public static final String KEY_RESULTAT_POURCENTAGE = "pourcentage";
    public static final String KEY_RESULTAT_DATE_SAISIE = "date_saisie";
    public static final String KEY_RESULTAT_ELECTION_ID = "election_id";
    public static final String KEY_RESULTAT_CANDIDAT_ID = "candidat_id";
    public static final String KEY_RESULTAT_BUREAU_ID = "bureau_vote_id";

    // CREATE TABLE statements
    private static final String CREATE_TABLE_USERS = "CREATE TABLE " + TABLE_USERS + "("
            + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + KEY_USER_FULL_NAME + " TEXT,"
            + KEY_USER_EMAIL + " TEXT UNIQUE NOT NULL,"
            + KEY_USER_PASSWORD + " TEXT NOT NULL,"
            + KEY_USER_ROLE + " TEXT" + ")";

    private static final String CREATE_TABLE_ELECTIONS = "CREATE TABLE " + TABLE_ELECTIONS + "("
            + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + KEY_ELECTION_TYPE + " TEXT,"
            + KEY_ELECTION_DATE + " INTEGER," // Stored as timestamp
            + KEY_ELECTION_STATUT + " TEXT,"
            + KEY_ELECTION_NB_TOURS + " INTEGER" + ")";

    private static final String CREATE_TABLE_CANDIDATS = "CREATE TABLE " + TABLE_CANDIDATS + "("
            + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + KEY_CANDIDAT_NOM + " TEXT,"
            + KEY_CANDIDAT_PRENOM + " TEXT,"
            + KEY_CANDIDAT_PARTI + " TEXT,"
            + KEY_CANDIDAT_BIO + " TEXT,"
            + KEY_CANDIDAT_PHOTO + " TEXT,"
            + KEY_CANDIDAT_SAISI_PAR + " TEXT" + ")";

    private static final String CREATE_TABLE_CIRCONSCRIPTIONS = "CREATE TABLE " + TABLE_CIRCONSCRIPTIONS + "("
            + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + KEY_CIRCONSCRIPTION_NOM + " TEXT,"
            + KEY_CIRCONSCRIPTION_REGION + " TEXT,"
            + KEY_CIRCONSCRIPTION_NB_INSCRITS + " INTEGER" + ")";

    private static final String CREATE_TABLE_CENTRES_VOTE = "CREATE TABLE " + TABLE_CENTRES_VOTE + "("
            + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + KEY_CENTRE_NOM + " TEXT,"
            + KEY_CENTRE_ADRESSE + " TEXT,"
            + KEY_CENTRE_LATITUDE + " REAL,"
            + KEY_CENTRE_LONGITUDE + " REAL,"
            + KEY_CENTRE_CIRCONSCRIPTION_ID + " INTEGER,"
            + "FOREIGN KEY(" + KEY_CENTRE_CIRCONSCRIPTION_ID + ") REFERENCES " + TABLE_CIRCONSCRIPTIONS + "(" + KEY_ID + ")" + ")";

    private static final String CREATE_TABLE_BUREAUX_VOTE = "CREATE TABLE " + TABLE_BUREAUX_VOTE + "("
            + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + KEY_BUREAU_NUMERO + " TEXT,"
            + KEY_BUREAU_NB_INSCRITS + " INTEGER,"
            + KEY_BUREAU_NB_VOTANTS + " INTEGER,"
            + KEY_BUREAU_CENTRE_ID + " INTEGER,"
            + "FOREIGN KEY(" + KEY_BUREAU_CENTRE_ID + ") REFERENCES " + TABLE_CENTRES_VOTE + "(" + KEY_ID + ")" + ")";

    private static final String CREATE_TABLE_RESULTATS = "CREATE TABLE " + TABLE_RESULTATS + "("
            + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + KEY_RESULTAT_NB_VOIX + " INTEGER,"
            + KEY_RESULTAT_POURCENTAGE + " REAL,"
            + KEY_RESULTAT_DATE_SAISIE + " INTEGER,"
            + KEY_RESULTAT_ELECTION_ID + " INTEGER,"
            + KEY_RESULTAT_CANDIDAT_ID + " INTEGER,"
            + KEY_RESULTAT_BUREAU_ID + " INTEGER,"
            + "FOREIGN KEY(" + KEY_RESULTAT_ELECTION_ID + ") REFERENCES " + TABLE_ELECTIONS + "(" + KEY_ID + "),"
            + "FOREIGN KEY(" + KEY_RESULTAT_CANDIDAT_ID + ") REFERENCES " + TABLE_CANDIDATS + "(" + KEY_ID + "),"
            + "FOREIGN KEY(" + KEY_RESULTAT_BUREAU_ID + ") REFERENCES " + TABLE_BUREAUX_VOTE + "(" + KEY_ID + ")" + ")";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        db.setForeignKeyConstraintsEnabled(true);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 3) {
            db.execSQL("ALTER TABLE " + TABLE_CANDIDATS + " ADD COLUMN " + KEY_CANDIDAT_SAISI_PAR + " TEXT");
        }
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RESULTATS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BUREAUX_VOTE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CENTRES_VOTE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CIRCONSCRIPTIONS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CANDIDATS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ELECTIONS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        onCreate(db);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_USERS);
        db.execSQL(CREATE_TABLE_ELECTIONS);
        db.execSQL(CREATE_TABLE_CANDIDATS);
        db.execSQL(CREATE_TABLE_CIRCONSCRIPTIONS);
        db.execSQL(CREATE_TABLE_CENTRES_VOTE);
        db.execSQL(CREATE_TABLE_BUREAUX_VOTE);
        db.execSQL(CREATE_TABLE_RESULTATS);
        addInitialUsers(db);
    }

    private void addInitialUsers(SQLiteDatabase db) {
        if (getUserCount(db) == 0) {
            addUser(db, new User("admin", "admin@gmail.com", "admin123", "Administrateur"));
            addUser(db, new User("superviseur", "superviseur@gmail.com", "sup123", "superviseur"));
            addUser(db, new User("operateur", "operateur@gmail.com", "op123", "opérateur"));
        }
    }

    public void checkAndAddInitialUsers() {
        SQLiteDatabase db = this.getWritableDatabase();
        addInitialUsers(db);
        db.close();
    }
    
    public long addUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        long result = addUser(db, user);
        db.close();
        return result;
    }

    private long addUser(SQLiteDatabase db, User user) {
        ContentValues cv = new ContentValues();
        cv.put(KEY_USER_FULL_NAME, user.getFullName());
        cv.put(KEY_USER_EMAIL, user.getEmail());
        cv.put(KEY_USER_PASSWORD, user.getPassword());
        cv.put(KEY_USER_ROLE, user.getRole());
        return db.insert(TABLE_USERS, null, cv);
    }

    public Map<String, String> checkUser(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Map<String, String> userDetails = null;
        String[] columns = {KEY_ID, KEY_USER_ROLE, KEY_USER_FULL_NAME};
        String selection = KEY_USER_EMAIL + " = ? AND " + KEY_USER_PASSWORD + " = ?";
        String[] selectionArgs = {email, password};

        Cursor cursor = db.query(TABLE_USERS, columns, selection, selectionArgs, null, null, null);

        if (cursor.moveToFirst()) {
            userDetails = new HashMap<>();
            userDetails.put(KEY_ID, cursor.getString(cursor.getColumnIndexOrThrow(KEY_ID)));
            userDetails.put(KEY_USER_ROLE, cursor.getString(cursor.getColumnIndexOrThrow(KEY_USER_ROLE)));
            userDetails.put(KEY_USER_FULL_NAME, cursor.getString(cursor.getColumnIndexOrThrow(KEY_USER_FULL_NAME)));
        }
        cursor.close();
        db.close();
        return userDetails;
    }

    public String getUserRole(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        String role = null;
        String[] columns = {KEY_USER_ROLE};
        String selection = KEY_USER_EMAIL + " = ? AND " + KEY_USER_PASSWORD + " = ?";
        String[] selectionArgs = {email, password};

        Cursor cursor = db.query(TABLE_USERS, columns, selection, selectionArgs, null, null, null);

        if (cursor.moveToFirst()) {
            role = cursor.getString(cursor.getColumnIndexOrThrow(KEY_USER_ROLE));
        }
        cursor.close();
        db.close();
        return role;
    }

    public int getUserCount() {
        SQLiteDatabase db = this.getReadableDatabase();
        int count = getUserCount(db);
        db.close();
        return count;
    }
    
    private int getUserCount(SQLiteDatabase db) {
        String query = "SELECT COUNT(*) FROM " + TABLE_USERS;
        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        int count = cursor.getInt(0);
        cursor.close();
        return count;
    }

    // #################### "elections" table methods ####################

    public long addElection(Election election) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ELECTION_TYPE, election.getType());
        values.put(KEY_ELECTION_DATE, election.getDateScrutin().getTime()); // Store date as timestamp
        values.put(KEY_ELECTION_STATUT, election.getStatut());
        values.put(KEY_ELECTION_NB_TOURS, election.getNbTours());

        long electionId = db.insert(TABLE_ELECTIONS, null, values);
        return electionId;
    }

    public List<Election> getAllElections() {
        List<Election> elections = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + TABLE_ELECTIONS;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                Election election = new Election();
                election.setId(c.getLong(c.getColumnIndexOrThrow(KEY_ID)));
                election.setType(c.getString(c.getColumnIndexOrThrow(KEY_ELECTION_TYPE)));
                election.setDateScrutin(new Date(c.getLong(c.getColumnIndexOrThrow(KEY_ELECTION_DATE))));
                election.setStatut(c.getString(c.getColumnIndexOrThrow(KEY_ELECTION_STATUT)));
                election.setNbTours(c.getInt(c.getColumnIndexOrThrow(KEY_ELECTION_NB_TOURS)));

                elections.add(election);
            } while (c.moveToNext());
        }
        c.close();
        return elections;
    }

    public int updateElection(Election election) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_ELECTION_TYPE, election.getType());
        values.put(KEY_ELECTION_DATE, election.getDateScrutin().getTime());
        values.put(KEY_ELECTION_STATUT, election.getStatut());
        values.put(KEY_ELECTION_NB_TOURS, election.getNbTours());
        int rows = db.update(TABLE_ELECTIONS, values, KEY_ID + " = ?", new String[]{String.valueOf(election.getId())});
        db.close();
        return rows;
    }

    public int deleteElection(long electionId) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rows = db.delete(TABLE_ELECTIONS, KEY_ID + " = ?", new String[]{String.valueOf(electionId)});
        db.close();
        return rows;
    }

    // #################### "candidats" table methods ####################

    public long addCandidat(Candidat candidat) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_CANDIDAT_NOM, candidat.getNom());
        values.put(KEY_CANDIDAT_PRENOM, candidat.getPrenom());
        values.put(KEY_CANDIDAT_PARTI, candidat.getParti());
        values.put(KEY_CANDIDAT_BIO, candidat.getBiographie());
        values.put(KEY_CANDIDAT_PHOTO, candidat.getPhoto());
        values.put(KEY_CANDIDAT_SAISI_PAR, candidat.getSaisiPar());
        long id = db.insert(TABLE_CANDIDATS, null, values);
        db.close();
        return id;
    }

    public List<Candidat> getAllCandidats() {
        List<Candidat> candidats = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + TABLE_CANDIDATS;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        if (c.moveToFirst()) {
            do {
                Candidat candidat = new Candidat();
                candidat.setId(c.getLong(c.getColumnIndexOrThrow(KEY_ID)));
                candidat.setNom(c.getString(c.getColumnIndexOrThrow(KEY_CANDIDAT_NOM)));
                candidat.setPrenom(c.getString(c.getColumnIndexOrThrow(KEY_CANDIDAT_PRENOM)));
                candidat.setParti(c.getString(c.getColumnIndexOrThrow(KEY_CANDIDAT_PARTI)));
                candidat.setBiographie(c.getString(c.getColumnIndexOrThrow(KEY_CANDIDAT_BIO)));
                candidat.setPhoto(c.getString(c.getColumnIndexOrThrow(KEY_CANDIDAT_PHOTO)));
                candidat.setSaisiPar(c.getString(c.getColumnIndexOrThrow(KEY_CANDIDAT_SAISI_PAR)));
                candidats.add(candidat);
            } while (c.moveToNext());
        }
        c.close();
        return candidats;
    }

    // #################### "circonscriptions" table methods ####################

    public List<Circonscription> getAllCirconscriptions() {
        List<Circonscription> circonscriptions = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + TABLE_CIRCONSCRIPTIONS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        if (c.moveToFirst()) {
            do {
                Circonscription circ = new Circonscription();
                circ.setId(c.getLong(c.getColumnIndexOrThrow(KEY_ID)));
                circ.setNom(c.getString(c.getColumnIndexOrThrow(KEY_CIRCONSCRIPTION_NOM)));
                circ.setRegion(c.getString(c.getColumnIndexOrThrow(KEY_CIRCONSCRIPTION_REGION)));
                circ.setNbInscrits(c.getInt(c.getColumnIndexOrThrow(KEY_CIRCONSCRIPTION_NB_INSCRITS)));
                circonscriptions.add(circ);
            } while (c.moveToNext());
        }
        c.close();
        return circonscriptions;
    }

    // #################### "centres_vote" table methods ####################
    public List<CentreDeVote> getCentresByCirconscription(long circonscriptionId) {
        List<CentreDeVote> centres = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT * FROM " + TABLE_CENTRES_VOTE + " WHERE " + KEY_CENTRE_CIRCONSCRIPTION_ID + " = ?";
        Cursor c = db.rawQuery(selectQuery, new String[]{String.valueOf(circonscriptionId)});

        if (c.moveToFirst()) {
            do {
                CentreDeVote centre = new CentreDeVote();
                centre.setId(c.getLong(c.getColumnIndexOrThrow(KEY_ID)));
                centre.setNom(c.getString(c.getColumnIndexOrThrow(KEY_CENTRE_NOM)));
                centre.setAdresse(c.getString(c.getColumnIndexOrThrow(KEY_CENTRE_ADRESSE)));
                centre.setLatitude(c.getDouble(c.getColumnIndexOrThrow(KEY_CENTRE_LATITUDE)));
                centre.setLongitude(c.getDouble(c.getColumnIndexOrThrow(KEY_CENTRE_LONGITUDE)));
                centre.setCirconscriptionId(c.getLong(c.getColumnIndexOrThrow(KEY_CENTRE_CIRCONSCRIPTION_ID)));
                centres.add(centre);
            } while (c.moveToNext());
        }
        c.close();
        return centres;
    }

    // #################### "bureaux_vote" table methods ####################
    public List<BureauDeVote> getBureauxByCentre(long centreId) {
        List<BureauDeVote> bureaux = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT * FROM " + TABLE_BUREAUX_VOTE + " WHERE " + KEY_BUREAU_CENTRE_ID + " = ?";
        Cursor c = db.rawQuery(selectQuery, new String[]{String.valueOf(centreId)});
        if (c.moveToFirst()) {
            do {
                BureauDeVote bureau = new BureauDeVote();
                bureau.setId(c.getLong(c.getColumnIndexOrThrow(KEY_ID)));
                bureau.setNumero(c.getString(c.getColumnIndexOrThrow(KEY_BUREAU_NUMERO)));
                bureau.setNbInscrits(c.getInt(c.getColumnIndexOrThrow(KEY_BUREAU_NB_INSCRITS)));
                bureau.setNbVotants(c.getInt(c.getColumnIndexOrThrow(KEY_BUREAU_NB_VOTANTS)));
                bureau.setCentreDeVoteId(c.getLong(c.getColumnIndexOrThrow(KEY_BUREAU_CENTRE_ID)));
                bureaux.add(bureau);
            } while (c.moveToNext());
        }
        c.close();
        return bureaux;
    }

    // #################### "resultats" table methods ####################

    public long addResultat(Resultat resultat) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_RESULTAT_ELECTION_ID, resultat.getElectionId());
        values.put(KEY_RESULTAT_CANDIDAT_ID, resultat.getCandidatId());
        values.put(KEY_RESULTAT_BUREAU_ID, resultat.getBureauVoteId());
        values.put(KEY_RESULTAT_NB_VOIX, resultat.getNbVoix());
        values.put(KEY_RESULTAT_POURCENTAGE, resultat.getPourcentage());
        values.put(KEY_RESULTAT_DATE_SAISIE, resultat.getDateSaisie().getTime());

        long id = db.insert(TABLE_RESULTATS, null, values);
        return id;
    }

    public List<ResultatView> getResultViews() {
        List<ResultatView> resultatViews = new ArrayList<>();
        String query = "SELECT " +
                "e." + KEY_ELECTION_TYPE + " AS election_type, " +
                "c." + KEY_CANDIDAT_PRENOM + " || ' ' || c." + KEY_CANDIDAT_NOM + " AS candidat_name, " +
                "b." + KEY_BUREAU_NUMERO + " AS bureau_numero, " +
                "r." + KEY_RESULTAT_NB_VOIX + " AS nb_voix " +
                "FROM " + TABLE_RESULTATS + " r " +
                "JOIN " + TABLE_ELECTIONS + " e ON r." + KEY_RESULTAT_ELECTION_ID + " = e." + KEY_ID + " " +
                "JOIN " + TABLE_CANDIDATS + " c ON r." + KEY_RESULTAT_CANDIDAT_ID + " = c." + KEY_ID + " " +
                "JOIN " + TABLE_BUREAUX_VOTE + " b ON r." + KEY_RESULTAT_BUREAU_ID + " = b." + KEY_ID + " " +
                "ORDER BY e." + KEY_ELECTION_TYPE + ", r." + KEY_RESULTAT_NB_VOIX + " DESC";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(query, null);

        if (c.moveToFirst()) {
            do {
                ResultatView rv = new ResultatView();
                rv.setElectionType(c.getString(c.getColumnIndexOrThrow("election_type")));
                rv.setCandidatFullName(c.getString(c.getColumnIndexOrThrow("candidat_name")));
                rv.setBureauNumero(c.getString(c.getColumnIndexOrThrow("bureau_numero")));
                rv.setNbVoix(c.getInt(c.getColumnIndexOrThrow("nb_voix")));
                resultatViews.add(rv);
            } while (c.moveToNext());
        }
        c.close();
        return resultatViews;
    }

    // #################### "users" table methods (listing) ####################

    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_USERS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);
        if (c.moveToFirst()) {
            do {
                User user = new User();
                user.setId(c.getLong(c.getColumnIndexOrThrow(KEY_ID)));
                user.setFullName(c.getString(c.getColumnIndexOrThrow(KEY_USER_FULL_NAME)));
                user.setEmail(c.getString(c.getColumnIndexOrThrow(KEY_USER_EMAIL)));
                user.setPassword(c.getString(c.getColumnIndexOrThrow(KEY_USER_PASSWORD)));
                user.setRole(c.getString(c.getColumnIndexOrThrow(KEY_USER_ROLE)));
                users.add(user);
            } while (c.moveToNext());
        }
        c.close();
        return users;
    }

    public List<User> getUsersByRole(String role) {
        List<User> users = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_USERS + " WHERE " + KEY_USER_ROLE + " = ?";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, new String[]{role});
        if (c.moveToFirst()) {
            do {
                User user = new User();
                user.setId(c.getLong(c.getColumnIndexOrThrow(KEY_ID)));
                user.setFullName(c.getString(c.getColumnIndexOrThrow(KEY_USER_FULL_NAME)));
                user.setEmail(c.getString(c.getColumnIndexOrThrow(KEY_USER_EMAIL)));
                user.setPassword(c.getString(c.getColumnIndexOrThrow(KEY_USER_PASSWORD)));
                user.setRole(c.getString(c.getColumnIndexOrThrow(KEY_USER_ROLE)));
                users.add(user);
            } while (c.moveToNext());
        }
        c.close();
        return users;
    }

    public int updateUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_USER_FULL_NAME, user.getFullName());
        values.put(KEY_USER_EMAIL, user.getEmail());
        values.put(KEY_USER_PASSWORD, user.getPassword());
        values.put(KEY_USER_ROLE, user.getRole());
        int rows = db.update(TABLE_USERS, values, KEY_ID + " = ?", new String[]{String.valueOf(user.getId())});
        db.close();
        return rows;
    }

    public int deleteUser(long userId) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rows = db.delete(TABLE_USERS, KEY_ID + " = ?", new String[]{String.valueOf(userId)});
        db.close();
        return rows;
    }

    public int getTotalInscrits() {
        int total = 0;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT SUM(" + KEY_BUREAU_NB_INSCRITS + ") FROM " + TABLE_BUREAUX_VOTE, null);
        if (c.moveToFirst()) {
            total = c.getInt(0);
        }
        c.close();
        return total;
    }

    public int getTotalVotants() {
        int total = 0;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT SUM(" + KEY_BUREAU_NB_VOTANTS + ") FROM " + TABLE_BUREAUX_VOTE, null);
        if (c.moveToFirst()) {
            total = c.getInt(0);
        }
        c.close();
        return total;
    }

    // Retourne le pourcentage réel de voix pour un candidat donné (toutes élections confondues)
    public float getPourcentageForCandidat(long candidatId) {
        float pourcentage = 0f;
        SQLiteDatabase db = this.getReadableDatabase();

        // Nombre de voix pour ce candidat
        Cursor c1 = db.rawQuery("SELECT SUM(nb_voix) FROM resultats WHERE candidat_id = ?", new String[]{String.valueOf(candidatId)});
        int voixCandidat = 0;
        if (c1.moveToFirst()) {
            voixCandidat = c1.getInt(0);
        }
        c1.close();

        // Nombre total de voix exprimées
        Cursor c2 = db.rawQuery("SELECT SUM(nb_voix) FROM resultats", null);
        int totalVoix = 0;
        if (c2.moveToFirst()) {
            totalVoix = c2.getInt(0);
        }
        c2.close();

        if (totalVoix > 0) {
            pourcentage = (voixCandidat * 100f) / totalVoix;
        }
        return pourcentage;
    }

    // Retourne le nombre de votes saisis par jour de la semaine
    public Map<String, Integer> getVotesParJour() {
        Map<String, Integer> votes = new LinkedHashMap<>();
        votes.put("L", 0); votes.put("M", 0); votes.put("M", 0); votes.put("J", 0); votes.put("V", 0); votes.put("S", 0); votes.put("D", 0);
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT " + KEY_RESULTAT_DATE_SAISIE + " FROM " + TABLE_RESULTATS, null);
        if (c.moveToFirst()) {
            do {
                long dateMillis = c.getLong(0);
                Calendar cal = Calendar.getInstance();
                cal.setTimeInMillis(dateMillis);
                int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
                String dayLetter = "L";
                switch (dayOfWeek) {
                    case Calendar.MONDAY: dayLetter = "L"; break;
                    case Calendar.TUESDAY: dayLetter = "M"; break;
                    case Calendar.WEDNESDAY: dayLetter = "M"; break;
                    case Calendar.THURSDAY: dayLetter = "J"; break;
                    case Calendar.FRIDAY: dayLetter = "V"; break;
                    case Calendar.SATURDAY: dayLetter = "S"; break;
                    case Calendar.SUNDAY: dayLetter = "D"; break;
                }
                votes.put(dayLetter, votes.get(dayLetter) + 1);
            } while (c.moveToNext());
        }
        c.close();
        return votes;
    }

    // Retourne le nombre de votes par région
    public Map<String, Integer> getVotesParRegion() {
        Map<String, Integer> votes = new LinkedHashMap<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT circonscriptions.region, SUM(resultats.nb_voix) " +
                       "FROM resultats " +
                       "JOIN bureaux_vote ON resultats.bureau_vote_id = bureaux_vote.id " +
                       "JOIN centres_vote ON bureaux_vote.centre_vote_id = centres_vote.id " +
                       "JOIN circonscriptions ON centres_vote.circonscription_id = circonscriptions.id " +
                       "GROUP BY circonscriptions.region";
        Cursor c = db.rawQuery(query, null);
        if (c.moveToFirst()) {
            do {
                String region = c.getString(0);
                int nbVoix = c.getInt(1);
                votes.put(region, nbVoix);
            } while (c.moveToNext());
        }
        c.close();
        return votes;
    }

    // Retourne le nombre de votes par parti
    public Map<String, Integer> getVotesParParti() {
        Map<String, Integer> votes = new LinkedHashMap<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT candidats.parti, SUM(resultats.nb_voix) " +
                       "FROM resultats " +
                       "JOIN candidats ON resultats.candidat_id = candidats.id " +
                       "GROUP BY candidats.parti";
        Cursor c = db.rawQuery(query, null);
        if (c.moveToFirst()) {
            do {
                String parti = c.getString(0);
                int nbVoix = c.getInt(1);
                votes.put(parti, nbVoix);
            } while (c.moveToNext());
        }
        c.close();
        return votes;
    }

    // Retourne le candidat avec le plus haut pourcentage (optionnellement pour une élection donnée)
    public Candidat getCandidatEnTete(Long electionId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT c.*, SUM(r." + KEY_RESULTAT_POURCENTAGE + ") as total_pct " +
                "FROM " + TABLE_CANDIDATS + " c " +
                "JOIN " + TABLE_RESULTATS + " r ON c." + KEY_ID + " = r." + KEY_RESULTAT_CANDIDAT_ID + " ";
        if (electionId != null) {
            query += "WHERE r." + KEY_RESULTAT_ELECTION_ID + " = " + electionId + " ";
        }
        query += "GROUP BY c." + KEY_ID + " ORDER BY total_pct DESC LIMIT 1";
        Cursor c = db.rawQuery(query, null);
        Candidat candidat = null;
        if (c.moveToFirst()) {
            candidat = new Candidat();
            candidat.setId(c.getLong(c.getColumnIndexOrThrow(KEY_ID)));
            candidat.setNom(c.getString(c.getColumnIndexOrThrow(KEY_CANDIDAT_NOM)));
            candidat.setPrenom(c.getString(c.getColumnIndexOrThrow(KEY_CANDIDAT_PRENOM)));
            candidat.setParti(c.getString(c.getColumnIndexOrThrow(KEY_CANDIDAT_PARTI)));
            candidat.setBiographie(c.getString(c.getColumnIndexOrThrow(KEY_CANDIDAT_BIO)));
            candidat.setPhoto(c.getString(c.getColumnIndexOrThrow(KEY_CANDIDAT_PHOTO)));
        }
        c.close();
        return candidat;
    }

    public int updateBureau(BureauDeVote bureau) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_BUREAU_NUMERO, bureau.getNumero());
        values.put(KEY_BUREAU_NB_INSCRITS, bureau.getNbInscrits());
        values.put(KEY_BUREAU_NB_VOTANTS, bureau.getNbVotants());
        values.put(KEY_BUREAU_CENTRE_ID, bureau.getCentreDeVoteId());
        int rows = db.update(TABLE_BUREAUX_VOTE, values, KEY_ID + " = ?", new String[]{String.valueOf(bureau.getId())});
        db.close();
        return rows;
    }

    public int deleteBureau(long bureauId) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rows = db.delete(TABLE_BUREAUX_VOTE, KEY_ID + " = ?", new String[]{String.valueOf(bureauId)});
        db.close();
        return rows;
    }

    public int updateCentre(CentreDeVote centre) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_CENTRE_NOM, centre.getNom());
        values.put(KEY_CENTRE_ADRESSE, centre.getAdresse());
        values.put(KEY_CENTRE_LATITUDE, centre.getLatitude());
        values.put(KEY_CENTRE_LONGITUDE, centre.getLongitude());
        values.put(KEY_CENTRE_CIRCONSCRIPTION_ID, centre.getCirconscriptionId());
        int rows = db.update(TABLE_CENTRES_VOTE, values, KEY_ID + " = ?", new String[]{String.valueOf(centre.getId())});
        db.close();
        return rows;
    }

    public int deleteCentre(long centreId) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rows = db.delete(TABLE_CENTRES_VOTE, KEY_ID + " = ?", new String[]{String.valueOf(centreId)});
        db.close();
        return rows;
    }

    public int updateCirconscription(Circonscription circ) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_CIRCONSCRIPTION_NOM, circ.getNom());
        values.put(KEY_CIRCONSCRIPTION_REGION, circ.getRegion());
        values.put(KEY_CIRCONSCRIPTION_NB_INSCRITS, circ.getNbInscrits());
        int rows = db.update(TABLE_CIRCONSCRIPTIONS, values, KEY_ID + " = ?", new String[]{String.valueOf(circ.getId())});
        db.close();
        return rows;
    }

    public int deleteCirconscription(long circId) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rows = db.delete(TABLE_CIRCONSCRIPTIONS, KEY_ID + " = ?", new String[]{String.valueOf(circId)});
        db.close();
        return rows;
    }

    public long addCirconscription(Circonscription circ) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_CIRCONSCRIPTION_NOM, circ.getNom());
        values.put(KEY_CIRCONSCRIPTION_REGION, circ.getRegion());
        values.put(KEY_CIRCONSCRIPTION_NB_INSCRITS, circ.getNbInscrits());
        long id = db.insert(TABLE_CIRCONSCRIPTIONS, null, values);
        db.close();
        return id;
    }

    // Récupère tous les bureaux de vote
    public List<BureauDeVote> getAllBureaux() {
        List<BureauDeVote> bureaux = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM " + TABLE_BUREAUX_VOTE, null);
        if (c.moveToFirst()) {
            do {
                BureauDeVote bureau = new BureauDeVote();
                bureau.setId(c.getLong(c.getColumnIndexOrThrow(KEY_ID)));
                bureau.setNumero(c.getString(c.getColumnIndexOrThrow(KEY_BUREAU_NUMERO)));
                bureau.setNbInscrits(c.getInt(c.getColumnIndexOrThrow(KEY_BUREAU_NB_INSCRITS)));
                bureau.setNbVotants(c.getInt(c.getColumnIndexOrThrow(KEY_BUREAU_NB_VOTANTS)));
                bureau.setCentreDeVoteId(c.getLong(c.getColumnIndexOrThrow(KEY_BUREAU_CENTRE_ID)));
                bureaux.add(bureau);
            } while (c.moveToNext());
        }
        c.close();
        return bureaux;
    }

    // Ajoute un bureau de vote
    public long addBureau(BureauDeVote bureau) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_BUREAU_NUMERO, bureau.getNumero());
        values.put(KEY_BUREAU_NB_INSCRITS, bureau.getNbInscrits());
        values.put(KEY_BUREAU_NB_VOTANTS, bureau.getNbVotants());
        values.put(KEY_BUREAU_CENTRE_ID, bureau.getCentreDeVoteId());
        long id = db.insert(TABLE_BUREAUX_VOTE, null, values);
        db.close();
        return id;
    }

    // Récupère tous les centres de vote
    public List<CentreDeVote> getAllCentres() {
        List<CentreDeVote> centres = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM " + TABLE_CENTRES_VOTE, null);
        if (c.moveToFirst()) {
            do {
                CentreDeVote centre = new CentreDeVote();
                centre.setId(c.getLong(c.getColumnIndexOrThrow(KEY_ID)));
                centre.setNom(c.getString(c.getColumnIndexOrThrow(KEY_CENTRE_NOM)));
                centre.setAdresse(c.getString(c.getColumnIndexOrThrow(KEY_CENTRE_ADRESSE)));
                centre.setLatitude(c.getDouble(c.getColumnIndexOrThrow(KEY_CENTRE_LATITUDE)));
                centre.setLongitude(c.getDouble(c.getColumnIndexOrThrow(KEY_CENTRE_LONGITUDE)));
                centre.setCirconscriptionId(c.getLong(c.getColumnIndexOrThrow(KEY_CENTRE_CIRCONSCRIPTION_ID)));
                centres.add(centre);
            } while (c.moveToNext());
        }
        c.close();
        return centres;
    }

    // Ajoute un centre de vote
    public long addCentre(CentreDeVote centre) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_CENTRE_NOM, centre.getNom());
        values.put(KEY_CENTRE_ADRESSE, centre.getAdresse());
        values.put(KEY_CENTRE_LATITUDE, centre.getLatitude());
        values.put(KEY_CENTRE_LONGITUDE, centre.getLongitude());
        values.put(KEY_CENTRE_CIRCONSCRIPTION_ID, centre.getCirconscriptionId());
        long id = db.insert(TABLE_CENTRES_VOTE, null, values);
        db.close();
        return id;
    }
} 