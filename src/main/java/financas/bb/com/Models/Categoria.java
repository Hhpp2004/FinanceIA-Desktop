package financas.bb.com.Models;

public enum Categoria {
    ALIMENTACAO(1,"Alimentação"),
    TRANSPORTE(2, "Transporte"),
    INVESTIMENTOS(3, "Investimentos"),
    SAUDE(4, "Saúde"),
    MORADIA(5, "Moradia"),
    LAZER(6, "Lazer"),
    OUTROS(7, "Outros");

    private int id;
    private String nome;
    
    public int getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    private Categoria(int id, String nome) {
        this.id = id;
        this.nome = nome;
    } 
}
