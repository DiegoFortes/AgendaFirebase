package dievow.agendafirebase;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
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

    private List<Pessoa> listPessoa = new ArrayList<Pessoa>();
    private ArrayAdapter<Pessoa> arrayAdapterPessoa;

    //objeto de conexão com o firebase
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    EditText editText_nome, editText_tel,editText_email,editText_endereco;
    ListView listV_dados;
    Pessoa pessoaSelecionada;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        inicializarFirebase();
        eventoDatabase();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editText_nome = (EditText) findViewById(R.id.editText_nome);
        editText_tel = (EditText) findViewById(R.id.editText_tel);
        editText_email = (EditText) findViewById(R.id.editText_email);
        editText_endereco = (EditText) findViewById(R.id.editText_endereco);
        listV_dados = (ListView) findViewById(R.id.listV_dados);

        listV_dados.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                pessoaSelecionada = (Pessoa)parent.getItemAtPosition(position);
                editText_nome.setText(pessoaSelecionada.getNome());
                editText_tel.setText(pessoaSelecionada.getTelefone());
                editText_email.setText(pessoaSelecionada.getEmail());
                editText_endereco.setText(pessoaSelecionada.getEndereco());
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();



        if(id == R.id.menu_novo){
            if(editText_nome.getText().length() == 0) {

                editText_nome.setError("campo obrigatório");
            }else{
                AlertDialog.Builder alerta = new AlertDialog.Builder(MainActivity.this);
                alerta.setMessage("Novo Registro Incluido!");
                alerta.show();
                inserirRegistro();
            }
        }else if(id == R.id.menu_editar){
            if(editText_nome.getText().length() == 0) {
                AlertDialog.Builder alerta = new AlertDialog.Builder(MainActivity.this);
                alerta.setMessage("Nenhum registro foi selecionado");
            }else{
            AlertDialog.Builder alerta = new AlertDialog.Builder(MainActivity.this);
            alerta.setMessage("Registro atualizado com sucesso!");
            alerta.show();
            editarRegistro();
            }
        }else if(id == R.id.menu_deletar){
            AlertDialog.Builder alerta = new AlertDialog.Builder(MainActivity.this);
            alerta.setTitle("Excluir Produto");
            alerta.setIcon( android.R.drawable.ic_delete );
            alerta.setMessage("Confirma a exclusão do registro:  " + pessoaSelecionada.getNome() + "de sua agenda?");
            alerta.setNeutralButton("Não", null);
            alerta.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    apagarRegistro();
                }
            });
            alerta.show();
        }
        return true;
    }

    //Função inserir registro
    private void inserirRegistro() {
        Pessoa p = new Pessoa();
        p.setId(UUID.randomUUID().toString());
        p.setNome(editText_nome.getText().toString());
        p.setTelefone(editText_tel.getText().toString());
        p.setEmail(editText_email.getText().toString());
        p.setEndereco(editText_endereco.getText().toString());
        databaseReference.child("Pessoa").child(p.getId()).setValue(p);
        limparCampos ();
    }

    //Função editar registro
    private void editarRegistro() {
        Pessoa p = new Pessoa();
        p.setId(pessoaSelecionada.getId());
        p.setNome(editText_nome.getText().toString().trim());
        p.setTelefone(editText_tel.getText().toString().trim());
        p.setEmail(editText_email.getText().toString().trim());
        p.setEndereco(editText_endereco.getText().toString().trim());
        databaseReference.child("Pessoa").child(p.getId()).setValue(p);
        limparCampos();
    }

    //Funcão apagar registro
    private void apagarRegistro() {
        Pessoa p = new Pessoa();
        p.setId(pessoaSelecionada.getId());
        databaseReference.child("Pessoa").child(p.getId()).removeValue();
        limparCampos();
    }

    //Inicializa conectividade do Firebase
    private void inicializarFirebase() {
        FirebaseApp.initializeApp(MainActivity.this);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
    }

    //limpa campos preenchidos
    private void limparCampos() {
        editText_nome.setText("");
        editText_tel.setText("");
        editText_email.setText("");
        editText_endereco.setText("");
    }

    //Exibi Registros do Firebase no List View
    private void eventoDatabase() {

        databaseReference.child("Pessoa").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                listPessoa.clear();
                for (DataSnapshot objSnapshot:dataSnapshot.getChildren()){
                    Pessoa p = objSnapshot.getValue(Pessoa.class);
                    listPessoa.add(p);
                }
                arrayAdapterPessoa = new ArrayAdapter<Pessoa>(MainActivity.this,
                        android.R.layout.simple_list_item_1,listPessoa);
                listV_dados.setAdapter(arrayAdapterPessoa);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
