package com.grow.shapeshifters;

import static android.content.Context.MODE_PRIVATE;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Base64;
import android.util.Log;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

/**
 * DatabaseHelper is a helper class that manages database creation and version management.
 * It provides methods to create a tables and insert new values into the database.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    // Database Info
    private static final String DATABASE_NAME = "shapeshifters_dev.db";
    private static final int DATABASE_VERSION = 1;

    // Table Names
    private static final String TABLE_USERS = "users";

    // User Table Columns
    private static final String KEY_USER_ID = "id";
    private static final String KEY_USER_FIRSTNAME = "firstname";
    private static final String KEY_USER_LASTNAME = "lastname";
    private static final String KEY_USER_EMAIL = "email";
    private static final String KEY_USER_PASSWORD = "password";
    private static final String KEY_USER_ACCOUNT_TYPE = "account_type";

    private Context context;

    /**
     * Constructs a new instance of {@link DatabaseHelper}.
     *
     * @param context the context to use for opening or creating the database.
     */
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    /**
     * Called when the database is created for the first time. This is where the
     * creation of tables and the initial population of the tables should happen.
     *
     * @param db The database.
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_USERS_TABLE = "CREATE TABLE " + TABLE_USERS +
                "(" +
                KEY_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                KEY_USER_FIRSTNAME + " TEXT NOT NULL," +
                KEY_USER_LASTNAME + " TEXT NOT NULL," +
                KEY_USER_EMAIL + " TEXT NOT NULL," +
                KEY_USER_PASSWORD + " TEXT NOT NULL," +
                KEY_USER_ACCOUNT_TYPE + " INTEGER NOT NULL" +
                ")";

        db.execSQL(CREATE_USERS_TABLE);
    }

    /**
     * Called when the database needs to be upgraded. The implementation
     * should use this method to drop tables, add tables, or do anything else it
     * needs to upgrade to the new schema version.
     *
     * @param db The database.
     * @param oldVersion The old database version.
     * @param newVersion The new database version.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Implement later.
    }

    /**
     * Adds a new user to the users table.
     *
     * @param firstname The first name of the user.
     * @param lastname The last name of the user.
     * @param email The email address of the user.
     * @param password The hashed password of the user.
     * @param type The account type of the user (true for trainer, false for client).
     * @return true if sign up was successful, false otherwise.
     */
    public boolean signup(String firstname, String lastname, String email, String password, boolean type) {
        // Gets the data repository in write mode
        SQLiteDatabase db = this.getWritableDatabase();

        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(KEY_USER_FIRSTNAME, firstname);
        values.put(KEY_USER_LASTNAME, lastname);
        values.put(KEY_USER_EMAIL, email);
        values.put(KEY_USER_PASSWORD, hashPassword(password));
        values.put(KEY_USER_ACCOUNT_TYPE, type ? 1 : 0);

        // Insert the new row, returning the primary key value of the new row
        long newRowId = db.insert(TABLE_USERS, null, values);

        return newRowId != -1;
    }

    /**
     * Attempts to log in a user by verifying their email and password.
     * This method queries the application's SQLite database to retrieve the stored hash of
     * the user's password associated with the provided email address. If a match is found,
     * it compares the input password (after hashing) with the stored password hash to
     * authenticate the user.
     *
     * @param email The email address input, used as the unique identifier for the account.
     * @param inputPassword The password input, to be verified against the stored hash.
     * @return true if the input credentials match an account in the database
     * and the password is correct, false otherwise.
     */
    public boolean login(String email, String inputPassword) {
        SQLiteDatabase db = this.getReadableDatabase();


        String query = "SELECT * " + " FROM " + TABLE_USERS + " WHERE " + KEY_USER_EMAIL + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{email});

        if (cursor.moveToFirst()) {
            // Retrieve the stored password hash from the cursor.
            String storedPasswordHash = cursor.getString(cursor.getColumnIndexOrThrow(KEY_USER_PASSWORD));
            String firstname = cursor.getString(cursor.getColumnIndexOrThrow(KEY_USER_FIRSTNAME));
            String lastname = cursor.getString(cursor.getColumnIndexOrThrow(KEY_USER_LASTNAME));
            String emails = cursor.getString(cursor.getColumnIndexOrThrow(KEY_USER_EMAIL));
            SharedPreferences sharedPreferences = context.getSharedPreferences("AppPreferences", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();

            editor.putString("Firstname", firstname);
            editor.putString("Lastname", lastname);
            editor.putString("Email", emails);
            editor.apply();

            cursor.close();
            // Verify the input password against the stored hash.
           return checkPassword(inputPassword, storedPasswordHash);
        }
        cursor.close();
        return false;
    }

    /**
     * Verifies if the provided input password matches the stored password hash.
     * This method performs password verification by decoding the stored password hash
     * (which includes the salt) from Base64, extracting the salt and the hash part,
     * hashing the input password with the extracted salt using PBKDF2 algorithm,
     * and then comparing the resulting hash with the stored hash.
     * It utilizes a strong hash function and incorporates a salt to enhance security by
     * defending against dictionary attacks and the use of rainbow tables.
     *
     * @param inputPassword The plaintext password input by the user.
     * @param storedPasswordHash The stored password hash, in Base64 encoding,
     * which includes the salt.
     * @return true if the input password, after being hashed with the stored salt,
     * matches the stored hash; false otherwise.
     */
    protected boolean checkPassword(String inputPassword, String storedPasswordHash) {
        try {
            // Decode the stored password hash from Base64
            byte[] hashWithSalt = Base64.decode(storedPasswordHash, Base64.DEFAULT);

            // Extract the salt
            byte[] salt = new byte[16];
            System.arraycopy(hashWithSalt, 0, salt, 0, salt.length);

            // Extract the stored hash part
            byte[] storedHash = new byte[hashWithSalt.length - salt.length];
            System.arraycopy(hashWithSalt, salt.length, storedHash, 0, storedHash.length);

            // Hash the input password with the extracted salt
            KeySpec spec = new PBEKeySpec(inputPassword.toCharArray(), salt, 65536, 256);
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            byte[] inputHash = factory.generateSecret(spec).getEncoded();

            // Check if the hashes match
            return java.util.Arrays.equals(inputHash, storedHash);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            Log.e("LoginActivity", "Error checking password", e);
            return false;
        }
    }

    /**
     * Hashes a password using PBKDF2 with HMAC SHA-1.
     * This method generates a secure hash of the given password by using the PBKDF2
     * (Password-Based Key Derivation Function 2) with HMAC SHA-1 hashing algorithm.
     * It first generates a random salt, then applies the hashing algorithm with the specified
     * number of iterations and key length. The resulting hash is combined with the salt (salt+hash)
     * and returned as a Base64 encoded string. This format is suitable for storage as it includes
     * the salt needed for password verification.
     *
     * @param inputPassword The password to be hashed.
     * @return A Base64 encoded string of the salted hash, or {@code null} if an error occurs
     * during hashing.
     */
    protected String hashPassword(String inputPassword) {
        try {
            // Generate a random salt
            SecureRandom random = new SecureRandom();
            byte[] salt = new byte[16];
            random.nextBytes(salt);

            // Specify the hash parameters
            KeySpec spec = new PBEKeySpec(inputPassword.toCharArray(), salt, 65536, 256);
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");

            // Generate the hash
            byte[] hash = factory.generateSecret(spec).getEncoded();

            // Prefix the hash with the salt for storage
            byte[] hashWithSalt = new byte[salt.length + hash.length];
            System.arraycopy(salt, 0, hashWithSalt, 0, salt.length);
            System.arraycopy(hash, 0, hashWithSalt, salt.length, hash.length);

            // Return a Base64 encoded string of the salted hash for easy storage
            return Base64.encodeToString(hashWithSalt, Base64.DEFAULT);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            Log.e("SignUpActivity", "Error hashing password", e);
            return null;
        }
    }


}
