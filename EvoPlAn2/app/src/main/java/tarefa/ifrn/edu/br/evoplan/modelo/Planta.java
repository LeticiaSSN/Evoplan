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
}
