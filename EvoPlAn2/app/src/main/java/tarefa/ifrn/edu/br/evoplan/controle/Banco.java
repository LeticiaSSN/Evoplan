package tarefa.ifrn.edu.br.evoplan.controle;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import tarefa.ifrn.edu.br.evoplan.modelo.Planta;
import tarefa.ifrn.edu.br.evoplan.modelo.Usuario;

public class Banco{

    public Statement stmt;
    public Connection conn;

    public Banco() {

        String url = "jdbc:mysql://ifrnsc.cswhts9hb6hj.us-west-2.rds.amazonaws.com:3306/evoplan";
        String usr = "ifrnsc";
        String pas = "elasvaopassar";
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection(url, usr, pas);
            stmt = conn.createStatement();
        } catch (Exception e) {
            System.out.println("Erro" + e.getMessage());
        }
    }

    public Connection getConn(){
        return this.conn;
    }

    public void inserirPlanta(Planta p) throws SQLException { /*inserir planta*/
        stmt.executeUpdate("INSERT INTO planta (`nome`,`umidadeIdeal`, `origemDaPlanta`, `cuidadoDaPlanta`, `cuidadoDePoda`, `umidadeIdeal`, `categoria`) VALUES ('"+p.nome+"', '"+p.umidadeIdeal+"','"+p.origemDaPlanta+"', '"+p.cuidadoDaPlanta+"' ,'"+p.cuidadoDePoda+"' ,'"+ 1 +"', '"+p.categoria+"');");
    }

    public void inserirUsuario(Usuario u) throws SQLException { /*inserir usuario*/
        stmt.executeUpdate("INSERT INTO usuario(`nome`,`senha`, `nivelDeAcesso`, `login`)  VALUES ('"+u.nome+"', '"+u.senha+"','"+ 1 +"', '"+u.login+"');");
    }

    public ResultSet getVerificarLogin(String login) throws SQLException { /*verificar login*/
        return stmt.executeQuery("SELECT senha FROM usuario WHERE login = '" + login + "' ");
    }

    public ResultSet getPlantas(Planta p) throws SQLException { //listar plantas
        return stmt.executeQuery("select * from planta");
    }

    public ResultSet getSenha(String senha) throws SQLException { //listar plantas
        return stmt.executeQuery("select senha from usuario WHERE senha = '" +senha+"'");
    }

    public ResultSet getUsuario(String login) throws SQLException { //listar plantas
        return stmt.executeQuery("select * from usuario WHERE login = '" +login+"'");
    }

    public ResultSet getPlantas(String nome, String cuidadoDaPlanta, String cuidadoDePoda, String categoria, String origemDaPlanta, String umidadeIdeal) throws SQLException { //listar plantas
        return stmt.executeQuery("select nome, cuidadoDaPlanta, cuidadoDePoda, categoria, origemDaPlanta, umidadeIdeal from planta where nome='" + nome + "'");
    }

    public ResultSet getUsuario(String nome, String senha, String login) throws SQLException { /*inserir usuario*/
        return stmt.executeQuery("select nome, login, senha from usuario where nome = '"+ nome +"'");
    }

    public ResultSet getBuscarCuidadoPlanta(String nome) throws SQLException { //buscar cuidado da planta
        return stmt.executeQuery("select cuidadoDaPlanta from planta where nome=?");
    }

    public ResultSet getBuscarCuidadoPoda(String cuidadoDaPoda) throws SQLException { //buscar cuidado da poda
        return stmt.executeQuery("select cuidadoDaPoda from planta where id=?");
    }

    public ResultSet getBuscarUmidadeIdeal(String plantaSeleciona) throws SQLException { //buscar umidade ideal
        return stmt.executeQuery("select umidadeIdeal from planta where nome ='"+plantaSeleciona+"'");
    }

    /*usando para imagem
    public ResultSet getPlantas(String nome, String cuidadoDaPlanta, String cuidadoDePoda, int id) throws SQLException { //listar plantas
        return stmt.executeQuery("select nome, cuidadoDaPlanta, cuidadoDePoda from planta where id='" + id + "'");
    }*/

}
