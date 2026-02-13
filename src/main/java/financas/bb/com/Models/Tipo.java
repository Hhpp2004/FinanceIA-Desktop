package financas.bb.com.Models;

public enum Tipo {
    ENTRADA(1,"Entrada"),
    SAIDA(2, "Saida");

    private int id;
    private String nome;
    
    public int getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    private Tipo(int id, String nome) {
        this.id = id;
        this.nome = nome;
    }
}
