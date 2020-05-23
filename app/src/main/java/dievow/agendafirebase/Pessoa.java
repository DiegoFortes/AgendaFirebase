package dievow.agendafirebase;

//classe pessoa que contém os dados requeridos na agenda
public class Pessoa {

    private String id;
    private String nome;
    private String telefone;
    private String email;
    private String endereco;

    //metodo construtor
    public Pessoa() {
    }

    //metodo getter and setter
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getEmail(){
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEndereco() {

        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    @Override
    public String toString() {
        return nome;
    }

}