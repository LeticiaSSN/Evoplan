package tarefa.ifrn.edu.br.evoplan.modelo;

public class Usuario {
    public String nome;
    public String senha;
    public String login;
    public Usuario(){}

    public Usuario(String nome, String senha, String login){
        this.nome=nome;
        this.senha=senha;
        this.login=login;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }


}
