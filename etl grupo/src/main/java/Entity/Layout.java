package Entity;

public class Layout {
    private Integer id;
    private String nome;
    private Integer emUso;
    private Integer fk_empresa_layout;

    public Layout() {

    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Integer getEmUso() {
        return emUso;
    }

    public void setEmUso(Integer emUso) {
        this.emUso = emUso;
    }

    public Integer getFk_empresa_layout() {
        return fk_empresa_layout;
    }

    public void setFk_empresa_layout(Integer fk_empresa_layout) {
        this.fk_empresa_layout = fk_empresa_layout;
    }

    @Override
    public String toString() {
        return "Layout{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", emUso=" + emUso +
                ", fk_empresa_layout=" + fk_empresa_layout +
                '}';
    }
}
