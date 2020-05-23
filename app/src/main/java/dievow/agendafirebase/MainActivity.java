package dievow.agendafirebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {
    EditText editText_nome, editText_tel,editText_email,editText_endereco;
    ListView listV_dados;

    //objeto de conexão com o firebase
   FirebaseDatabase firebaseDatabase;
   DatabaseReference databaseReference;
   private List<Pessoa> listPessoa = new ArrayList<Pessoa>();
   private ArrayAdapter<Pessoa> arrayAdapterPessoa;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editText_nome = (EditText) findViewById(R.id.editText_nome);
        editText_tel = (EditText) findViewById(R.id.editText_tel);
        editText_email = (EditText) findViewById(R.id.editText_email);
        editText_endereco = (EditText) findViewById(R.id.editText_endereco);
        listV_dados = (ListView) findViewById(R.id.listV_dados);

        eventoDatabase();

    }

    private void eventoDatabase() {

        databaseReference.child("Pessoa").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listPessoa.clear();
                for (DataSnapshot objSnapshot:dataSnapshot.getChildren()){
                    Pessoa p = objSnapshot.getValue(Pessoa.class);
                    listPessoa.add(p);
                }
                arrayAdapterPessoa = new ArrayAdapter<Pessoa>(MainActivity.this,android.R.layout.simple_list_item_1,listPessoa);
                listV_dados.setAdapter(arrayAdapterPessoa);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void inicializarFirebase() {
        FirebaseApp.initializeApp(MainActivity.this);
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseDatabase.setPersistenceEnabled(true);
        firebaseDatabase.setPersistenceEnabled(true);
        databaseReference = firebaseDatabase.getReference();

    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        inicializarFirebase();

        if(id == R.id.menu_novo){
            Pessoa p = new Pessoa();
            p.setId(UUID.randomUUID().toString());
            p.setNome(editText_nome.getText().toString());
            p.setTelefone(editText_tel.getText().toString());
            p.setEmail(editText_email.getText().toString());
            p.setEndereco(editText_endereco.getText().toString());
            databaseReference.child("Pessoa").child(p.getId()).setValue(p);
            limparCampos ();
        }
        return true;
    }

    //limpa campos preenchidos
    private void limparCampos() {
        editText_nome.setText("");
        editText_tel.setText("");
        editText_email.setText("");
        editText_endereco.setText("");
    }
}
