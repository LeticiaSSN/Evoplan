package tarefa.ifrn.edu.br.evoplan.modelo;

public class Planta {
    public String nome;
    public String umidadeIdeal;
    public String origemDaPlanta;
    public String cuidadoDaPlanta;
    public String cuidadoDePoda;
    public String categoria;
    public Planta(){}

    public Planta(String nome, String umidadeIdeal, String origemDaPlanta, String cuidadoDaPlanta, String cuidadoDePoda, String categoria) {
        this.nome = nome;
        this.umidadeIdeal = umidadeIdeal;
        this.origemDaPlanta = origemDaPlanta;
        this.cuidadoDaPlanta = cuidadoDaPlanta;
        this.cuidadoDePoda = cuidadoDePoda;
        this.categoria = categoria;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getUmidadeIdeal() {
        return umidadeIdeal;
    }

    public void setUmidadeIdeal(String umidadeIdeal) {
        this.umidadeIdeal = umidadeIdeal;
    }

    public String getOrigemDaPlanta() {
        return origemDaPlanta;
    }

    public void setOrigemDaPlanta(String origemDaPlanta) {
        this.origemDaPlanta = origemDaPlanta;
    }

    public String getCuidadoDaPlanta() {
        return cuidadoDaPlanta;
    }

    public void setCuidadoDaPlanta(String cuidadoDaPlanta) {
        this.cuidadoDaPlanta = cuidadoDaPlanta;
    }

    public String getCuidadoDePoda() {
        return cuidadoDePoda;
    }

    public void setCuidadoDePoda(String cuidadoDePoda) {
        this.cuidadoDePoda = cuidadoDePoda;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }
}
