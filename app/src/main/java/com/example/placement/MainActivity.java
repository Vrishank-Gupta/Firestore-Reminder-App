package com.example.placement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.placement.util.Reminder;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.common.reflect.TypeToken;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    int RC_SIGN_IN = 0;
    SignInButton signInButton;
    GoogleSignInClient mGoogleSignInClient;
    private FirebaseAuth mAuth;
    FirebaseFirestore db;
    public static Map<String,Object> map;
    DocumentReference reference;
    Gson gson;
    public static ArrayList<Reminder> reminderArrayList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        reference = db.collection("users").document(mAuth.getCurrentUser().getUid());
        gson = new Gson();


        signInButton = findViewById(R.id.btnSignIn);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();

        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);

            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);

                firebaseAuthWithGoogle(account);


            } catch (ApiException e) {
                e.printStackTrace();
            }

        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful())
                        {

                            map = new HashMap<>();
                            fetchArrayList();
//                            map = new HashMap<>();
//                            map.put("email", mAuth.getCurrentUser().getEmail());
//                            map.put("uid",mAuth.getCurrentUser().getUid());
//                            reference.set(map).addOnSuccessListener(new OnSuccessListener<Void>() {
//                                @Override
//                                public void onSuccess(Void aVoid) {
//                                    Toast.makeText(MainActivity.this, "Pushed", Toast.LENGTH_SHORT).show();
//
//                                    fetchArrayList();
//                                }
//                            });

                        } else {
                            Toast.makeText(MainActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void fetchArrayList() {

        reference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                DocumentSnapshot snapshot = task.getResult();

                if(snapshot.exists() && snapshot!= null)
                {
                    Log.e("jsonReminder", "onComplete: " + snapshot.getData() );
                    String jsonReminder = snapshot.getString("Reminders");

                    if(jsonReminder == null || jsonReminder.equals("[]"))
                    {
                        reminderArrayList = new ArrayList<>();
                         jsonReminder = gson.toJson(reminderArrayList);
                        Log.e("jsonReminder", "onComplete: " );
                        map.put("Email",mAuth.getCurrentUser().getEmail());
                        map.put("UID",mAuth.getCurrentUser().getUid());

                        map.put("Reminders", jsonReminder);
                        reference.set(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(MainActivity.this, "Pushed", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(MainActivity.this, Main2Activity.class));
                                finish();
                            }
                        });
                    }
                    else
                    {
                        Type userListType = new TypeToken<ArrayList<Reminder>>(){}.getType();
                         reminderArrayList = gson.fromJson(jsonReminder, userListType);
                        map = snapshot.getData();
                        Log.e("JsonReminder", "onComplete: " + jsonReminder );
                        startActivity(new Intent(MainActivity.this, Main2Activity.class));
                        finish();
                    }
                }
            }
        });

    }


//    @Override
//    protected void onStart() {
//        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
//        if(account != null) {
//            startActivity(new Intent(MainActivity.this, Main2Activity.class));
//            finish();
//        }
//        super.onStart();
//    }
}
