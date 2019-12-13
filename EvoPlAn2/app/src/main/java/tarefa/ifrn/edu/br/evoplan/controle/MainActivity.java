package tarefa.ifrn.edu.br.evoplan.controle;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;

import tarefa.ifrn.edu.br.evoplan.R;
import tarefa.ifrn.edu.br.evoplan.modelo.Planta;
import tarefa.ifrn.edu.br.evoplan.modelo.Usuario;

public class

MainActivity extends AppCompatActivity {

    private String nomePlanta = "";

    public String login = "";
    public static String plantaSelecionada="";
    public static int umidadeAtuals=0;
    public static int umidadeAtual =0;
    public static int diferenca=0;
    public static int umidadeIdeal=0;

    String Nome2;
    String Senha;
    String Login;

    String Nome;
    String CuidadoDaPlanta ;
    String CuidadoDePoda;
    String Categoria;
    String OrigemDaPlanta;
    String UmidadeIdeal;


    Banco b = new Banco();
    Usuario usuario = new Usuario();
    Planta p = new Planta(Nome, UmidadeIdeal, OrigemDaPlanta, CuidadoDaPlanta, CuidadoDePoda, Categoria);
    Usuario u = new Usuario (Nome2, Senha, Login);

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.tela_login);
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(MainActivity.this,MainActivity.class);
        startActivity(intent);
        finish();

    }

    // CÓDIGO DA CONEXÃO DO  MODULO BLUETOOTH
    private static final int REQUEST_ENABLE_BT = 1;

    BluetoothAdapter bluetoothAdapter;
    ArrayList<BluetoothDevice> pairedDeviceArrayList;

    TextView textInfo, textStatus, textDados;
    ListView listViewPairedDevice;
    LinearLayout inputPane;
    EditText inputField;
    Button btnSend;
    ImageView grafico;

    ArrayAdapter<BluetoothDevice> pairedDeviceAdapter;
    private UUID myUUID;
    private final String UUID_STRING_WELL_KNOWN_SPP =
            "00001101-0000-1000-8000-00805F9B34FB"; //testa o que é isso

    ThreadConnectBTdevice myThreadConnectBTdevice;
    ThreadConnected myThreadConnected;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(myThreadConnectBTdevice!=null){
            myThreadConnectBTdevice.cancel();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==REQUEST_ENABLE_BT){
            if(resultCode == Activity.RESULT_OK){
                setup();
            }else{
                Toast.makeText(this,
                        "Bluetooth não conectado",
                        Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    private void startThreadConnected(BluetoothSocket socket){

        myThreadConnected = new ThreadConnected(socket);
        myThreadConnected.start();
    }

    private class ThreadConnectBTdevice extends Thread {

        private BluetoothSocket bluetoothSocket = null;
        private final BluetoothDevice bluetoothDevice;

        private ThreadConnectBTdevice(BluetoothDevice device) {
            bluetoothDevice = device;

            try {
                bluetoothSocket = device.createRfcommSocketToServiceRecord(myUUID);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            boolean success = false;
            try {
                bluetoothSocket.connect();
                success = true;
            } catch (IOException e) {
                e.printStackTrace();

                final String eMessage = e.getMessage();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                    }
                });

                try {
                    bluetoothSocket.close();
                } catch (IOException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
            }

            if(success){
                runOnUiThread(new Runnable(){

                    @Override
                    public void run() {
                    }});
                startThreadConnected(bluetoothSocket);
            }
        }

        public void cancel() {
            Toast.makeText(getApplicationContext(),
                    "close bluetoothSocket",
                    Toast.LENGTH_LONG).show();

            try {
                bluetoothSocket.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    private class ThreadConnected extends Thread {
        private final BluetoothSocket connectedBluetoothSocket;
        private final InputStream connectedInputStream;
        private final OutputStream connectedOutputStream;

        public ThreadConnected(BluetoothSocket socket) {
            connectedBluetoothSocket = socket;
            InputStream in = null;
            OutputStream out = null;

            try {
                in = socket.getInputStream();
                out = socket.getOutputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }
            connectedInputStream = in;
            connectedOutputStream = out;
        }

        @Override
        public void run() {
            byte[] buffer = new byte[256];
            int bytes;

            while (true) {
                try {
                    bytes = connectedInputStream.read(buffer);
                    final String strReceived = new String(buffer, 0, bytes);

                    final int umidadeIdealf=umidadeIdeal;
                    final int umidadeAtualf=somenteDigitos(strReceived);
                    final int diferencaf =umidadeIdealf-umidadeAtualf;

                    runOnUiThread(new Runnable(){

                        @Override
                        public void run() {
                            if(diferencaf >11){
                                grafico = (ImageView)findViewById(R.id.grafico);
                                grafico.setImageResource(R.drawable.grafico_seco);
                            }
                            else if(diferencaf >=-10 && diferencaf<=10){
                                grafico = (ImageView)findViewById(R.id.grafico);
                                grafico.setImageResource(R.drawable.grafico_umido);
                            }
                            else if (diferencaf<=-11 ){
                                grafico = (ImageView)findViewById(R.id.grafico);
                                grafico.setImageResource(R.drawable.grafico_muito_umido);
                            }
                        }});

                } catch (Exception e){
                    e.printStackTrace();
                }}}

        public void write(byte[] buffer) {
            try {
                connectedOutputStream.write(buffer);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        public void cancel() {
            try {
                connectedBluetoothSocket.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    //funcao que converte o valor do sensor de umidade
    public static int somenteDigitos(String strReceived) {
        String digitos = "";
        char[] letras  = strReceived.toCharArray();
        for (char letra : letras) {
            if(Character.isDigit(letra)) {
                digitos += letra;
            }
        }
        return Integer.parseInt(digitos);
    }

    public void setup() {
        Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();
        if (pairedDevices.size() > 0) {
            pairedDeviceArrayList = new ArrayList<BluetoothDevice>();

            for (BluetoothDevice device : pairedDevices) {
                pairedDeviceArrayList.add(device);
            }
            ///testa isso
            pairedDeviceAdapter = new ArrayAdapter<BluetoothDevice>(this,
                    android.R.layout.simple_list_item_1, pairedDeviceArrayList);

            listViewPairedDevice.setAdapter(pairedDeviceAdapter);

            listViewPairedDevice.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {
                    BluetoothDevice device =
                            (BluetoothDevice) parent.getItemAtPosition(position);
                    Toast.makeText(MainActivity.this,
                            "Nome do Bluetooth selecionado: " + device.getName() + "\n"
                            ,
                            Toast.LENGTH_LONG).show();
                    myThreadConnectBTdevice = new ThreadConnectBTdevice(device);
                    myThreadConnectBTdevice.start();
                }
            });
        }
    }

    public void butaoSend(){
        if (myThreadConnected != null) {
            byte[] bytesToSend = inputField.getText().toString().getBytes();
            myThreadConnected.write(bytesToSend);
        }
    }

    public void login(View v) {
        setContentView(R.layout.tela_login);
    }

    public void cadastro(View v) {
        setContentView(R.layout.tela_cadastro);
    }

    public void menuPrincipal(View v) {
        setContentView(R.layout.tela_menu_principal);
    }

    public void confirmarPlanta(View v) {
        plantaSelecionada=nomePlanta;
        setContentView(R.layout.tela_planta_do_usuario);
    }

    public void adicionarNovaEspecie(View v) { setContentView(R.layout.tela_adicionar_nova_especie);}

    public void maisInformacoesSobreaPlanta(View v) { setContentView(R.layout.tela_mais_informacoes_sobre_a_planta);}

    public void informacoes(View v) { setContentView(R.layout.tela_informacoes);}

    public void plantaDoUsuario(View v) { setContentView(R.layout.tela_planta_do_usuario);}

    //TELA QUE MOSTRAR TODAS AS ESPECIES DE PLANTAS QUE EXISTEM
    public void todasEspecies(View v) {
        setContentView(R.layout.tela_todas_especies);
        Nome = "";
        CuidadoDaPlanta = "";
        CuidadoDePoda = "";
        Categoria = "";
        OrigemDaPlanta = "";
        UmidadeIdeal ="";
        EditText plantas = (EditText) findViewById(R.id.listarPlantassss);

        try {
            ResultSet especies = b.getPlantas(p);
            String texto = "";
            while (especies.next()) {
                texto += especies.getString("nome") + "\n";
            }
            plantas.setText(texto);
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    // TELA TODAS AS ESPECIES, QUE APÓS SER ESCRITO ALGUMA DAS PLANTAS QUE POSSUI NO BD, VAI PARA A TELA MAIS INFORMAÇÕES SOBRE A PLANTA
    public void pesquisarPlanta(View v) {
        SearchView plantas = (SearchView) findViewById(R.id.pesquisarPlanta2222);
        nomePlanta = plantas.getQuery().toString();
        setContentView(R.layout.tela_mais_informacoes_sobre_a_planta);

        Nome = "";
        CuidadoDaPlanta = "";
        CuidadoDePoda = "";
        Categoria = "";
        OrigemDaPlanta = "";
        UmidadeIdeal ="";

        try {
            ResultSet retorno = b.getPlantas(p);
            while (retorno.next()) {
                Nome = retorno.getString("nome") + "\n";
                CuidadoDaPlanta = retorno.getString("cuidadoDaPlanta") + "\n";
                CuidadoDePoda = retorno.getString("cuidadoDePoda") + "\n";
                Categoria = retorno.getString("categoria") + "\n";
                OrigemDaPlanta = retorno.getString("origemDaPlanta") + "\n";
                UmidadeIdeal =retorno.getInt("umidadeIdeal") + "\n";
            }

            //titulo nome da planta
            TextView nomePlantaTela = (TextView) findViewById(R.id.nomePlanta);
            nomePlantaTela.setText(Nome);

            //cuidado da planta
            TextView cuidadoDaPlanta = (TextView) findViewById(R.id.caixaTextoPlanta);
            cuidadoDaPlanta.setText(CuidadoDaPlanta);

            //cuidado de poda
            TextView cuidadoDePoda = (TextView) findViewById(R.id.caixaTextoCuidados);
            cuidadoDePoda.setText(CuidadoDePoda);

            //categoria
            TextView categoria2 = (TextView) findViewById(R.id.caixaTextoCategoria);
            categoria2.setText(Categoria);

            //origem
            TextView origem = (TextView) findViewById(R.id.caixaTextoOrigem2);
            origem.setText(OrigemDaPlanta);

            //umidade ideal
            TextView umidadeIdeal2 = (TextView) findViewById(R.id.caixaTextoUmidadeIdeal);
            umidadeIdeal2.setText(UmidadeIdeal);

        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    //TELA DE CADASTRO DO USUARIO
    public void cadastrarPessoa(View v) {
        EditText nome = (EditText) findViewById(R.id.txNome);
        EditText senha = (EditText) findViewById(R.id.senha_digitar);
        EditText confirmarSenha = (EditText) findViewById(R.id.confirmar_senha_digitar2);
        EditText login = (EditText) findViewById(R.id.txLogin);

        String Nome = nome.getText().toString();
        String Senha = senha.getText().toString();
        String Login = login.getText().toString();
        String ConfirmarSenha = confirmarSenha.getText().toString();

        if (Nome.equals("") ||Senha.equals("") || Login.equals("") || ConfirmarSenha.equals("")){
            Toast.makeText(getApplicationContext(), "É necessário preencher todos os campos.", Toast.LENGTH_LONG).show();
        }
        else if (Senha.equals(ConfirmarSenha)) {
            try {
                b.inserirUsuario (u);
                Toast.makeText(getApplicationContext(), "Cadastro realizado com sucesso!", Toast.LENGTH_LONG).show();
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
        } else {
            Toast.makeText(getApplicationContext(), "As senhas não correspondem.", Toast.LENGTH_LONG).show();
        }

    }

    // VERIFICANDO LOGIN E SENHA NA TELA DE LOGIN
    public void temCadastro(View v) {
        EditText login = (EditText) findViewById(R.id.Login);
        EditText confirmarSenha = (EditText) findViewById(R.id.confirmar_senha);
        String senhaDB = "vazio";

        String Login = login.getText().toString();
        String ConfirmarSenha = confirmarSenha.getText().toString();

        try {
            ResultSet resultado = b.getVerificarLogin(u);
            while (resultado.next()) {
                senhaDB = resultado.getString("senha");
            }
            if (senhaDB.equals(ConfirmarSenha)) {
                this.login = u.login;
                setContentView(R.layout.tela_menu_principal);
            } else {
                Toast.makeText(getApplicationContext(), "Login e/ou senha incorreta.", Toast.LENGTH_LONG).show();
            }

        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    //TELA DE CADASTRO DA PLANTA
    public void cadastrarPlanta(View v) {
        EditText nome = (EditText) findViewById(R.id.nome);
        EditText umidadeIdeal = (EditText) findViewById(R.id.umidadeIdeal);
        EditText origemDaPlanta = (EditText) findViewById(R.id.origemDaPlanta);
        EditText cuidadoDaPlanta = (EditText) findViewById(R.id.cuidadoDaPlanta);
        EditText cuidadoDePoda = (EditText) findViewById(R.id.cuidadoDePoda);
        EditText categoria = (EditText) findViewById(R.id.categoria);

        String Nome = nome.getText().toString();
        String UmidadeIdeal = umidadeIdeal.getText().toString();
        String OrigemDaPlanta = origemDaPlanta.getText().toString();
        String CuidadoDaPlanta = cuidadoDaPlanta.getText().toString();
        String CuidadoDePoda = cuidadoDePoda.getText().toString();
        String Categoria = categoria.getText().toString();

        try {
            if (Nome.equals("") || UmidadeIdeal.equals("") || OrigemDaPlanta.equals(" ") || CuidadoDaPlanta.equals(" ") || CuidadoDePoda.equals(" ")|| Categoria.equals(" ")){
                Toast.makeText(getApplicationContext(), "É necessário preencher todos os campos. ", Toast.LENGTH_LONG).show();
            }
            else{
                b.inserirPlanta(p);
                Toast.makeText(getApplicationContext(), "Cadastro realizado com sucesso!", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    // TELA PLANTA DO USUARIO QUE FAZ CONEXÃO COM O MÓDULO BLUETOOTH
    public void chamarBlue(View v) throws SQLException{
        int umidade =0;
        ResultSet rs = b.getBuscarUmidadeIdeal(plantaSelecionada);
        while (rs.next()){
            umidade = Integer.parseInt(rs.getString("umidadeIdeal"));
        }
        Toast.makeText(this,
                "Umidade "+umidade,Toast.LENGTH_LONG).show();

        umidadeIdeal= umidade;

        textDados = (TextView) findViewById(R.id.dados);
        textInfo = (TextView) findViewById(R.id.info);
        textStatus = (TextView) findViewById(R.id.status);
        listViewPairedDevice = (ListView) findViewById(R.id.pairedlist);
        inputPane = (LinearLayout) findViewById(R.id.inputpane);
        btnSend = (Button) findViewById(R.id.send);
        grafico =(ImageView)findViewById(R.id.grafico);
        btnSend.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (myThreadConnected != null) {
                    byte[] bytesToSend = inputField.getText().toString().getBytes();
                    myThreadConnected.write(bytesToSend);
                }
            }
        });

        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH)) {
            Toast.makeText(this,
                    "FEATURE_BLUETOOTH NOT support",
                    Toast.LENGTH_LONG).show();
            finish();
            return;
        }
        myUUID = UUID.fromString(UUID_STRING_WELL_KNOWN_SPP);
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter == null) {
            Toast.makeText(this,
                    "Bluetooth não suporta",
                    Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        String stInfo = bluetoothAdapter.getName() + "\n" +bluetoothAdapter.getAddress();
        textInfo.setText(stInfo);
        setup();
    }
}
