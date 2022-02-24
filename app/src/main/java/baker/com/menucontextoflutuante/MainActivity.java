package baker.com.menucontextoflutuante;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;

public class MainActivity extends AppCompatActivity {

    private ListView listViewNomes;
    private EditText editTextNome;
    private ImageButton imageButton1, imageButton2;

    private ArrayList<String> lista;
    private ArrayAdapter<String> adapter; //responsável por gerenciar o ArrayList no ListView

    private int posicaoAlteracao = -1; //guarda a posicao para edição de dados

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listViewNomes = findViewById(R.id.listViewNomes);
        editTextNome = findViewById(R.id.editTextNome);

        imageButton1 = findViewById(R.id.imageButton);
        imageButton2 = findViewById(R.id.imageButton2);

        imageButton2.setVisibility(View.INVISIBLE);

        popularLista();

        //marca que o listView responde a eventos de menu de contexto
        registerForContextMenu(listViewNomes);
    }

    private void popularLista(){
        lista = new ArrayList<>();

        adapter = new ArrayAdapter<>(this,
                                    android.R.layout.simple_list_item_1,
                                    lista);

        listViewNomes.setAdapter(adapter);
    }

    public void adicionar(View view){
        String frase = editTextNome.getText().toString();

        if(frase.isEmpty()){
            return;
        }
        editTextNome.setText(null);

        if(posicaoAlteracao == -1){
            lista.add(frase);
        } else {
            lista.remove(posicaoAlteracao);
            lista.add(posicaoAlteracao, frase); //adiciona o valor alterado na mesma posição

            imageButton1.setImageResource(android.R.drawable.ic_input_add);
            imageButton2.setVisibility(View.INVISIBLE);

            posicaoAlteracao = -1;

            listViewNomes.setEnabled(true);
        }

        Collections.sort(lista); //ordena nosso arraylist usando o padrão básico de ordenação

        adapter.notifyDataSetChanged();
    }

    //cancelar edição
    public void cancelar(View view){
        editTextNome.setText(null);

        imageButton1.setImageResource(android.R.drawable.ic_input_add);
        imageButton2.setVisibility(View.INVISIBLE);

        listViewNomes.setEnabled(true);

        posicaoAlteracao = -1;
    }

    private void alterar(int posicao){
        String frase = lista.get(posicao);

        editTextNome.setText(frase);
        editTextNome.setSelection(editTextNome.getText().length()); //colocar o cursor no final

        imageButton1.setImageResource(android.R.drawable.ic_menu_save);
        imageButton2.setVisibility(View.VISIBLE);

        listViewNomes.setEnabled(false); //bloqueia para interações com o usuário enquanto edita

        posicaoAlteracao = posicao;
    }

    private void excluir(int posicao){
        lista.remove(posicao);

        adapter.notifyDataSetChanged();
    }

    //usado para inflar o menu, só é chamado na primeira vez que o menu é exibido
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        getMenuInflater().inflate(R.menu.principal_menu_contexto, menu);
    }

    //é chamado quando um item é clicado
    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {

        //permite guardar informações sobre o menu de contexto
        AdapterView.AdapterContextMenuInfo info;

        //usado para ter informações em que situação está no momento do click
        info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        switch (item.getItemId()){
            case R.id.menuItemAlterar:
                    alterar(info.position);
                return true;
            case R.id.menuItemExcluir:
                    excluir(info.position); //devolve a posição no adapter
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }
}