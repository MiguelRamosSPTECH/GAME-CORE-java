package Entity;

public class Servidor {
    private Integer id;
    private String apelido;
    private String macadress;
    private String localizacao;
    private Integer fk_empresa_servidor;
    private Integer fk_layout;

    public Servidor() {

    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getApelido() {
        return apelido;
    }

    public void setApelido(String apelido) {
        this.apelido = apelido;
    }

    public String getMacadress() {
        return macadress;
    }

    public void setMacadress(String macadress) {
        this.macadress = macadress;
    }

    public String getLocalizacao() {
        return localizacao;
    }

    public void setLocalizacao(String localizacao) {
        this.localizacao = localizacao;
    }

    public Integer getFk_empresa_servidor() {
        return fk_empresa_servidor;
    }

    public void setFk_empresa_servidor(Integer fk_empresa_servidor) {
        this.fk_empresa_servidor = fk_empresa_servidor;
    }

    public Integer getFk_layout() {
        return fk_layout;
    }

    public void setFk_layout(Integer fk_layout) {
        this.fk_layout = fk_layout;
    }

    @Override
    public String
    toString() {
        return "Servidor{" +
                "id=" + id +
                ", apelido='" + apelido + '\'' +
                ", macadress='" + macadress + '\'' +
                ", localizacao='" + localizacao + '\'' +
                ", fk_empresa_servidor=" + fk_empresa_servidor +
                ", fk_layout=" + fk_layout +
                '}';
    }
}
