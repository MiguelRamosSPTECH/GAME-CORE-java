package gamecore.project.entity;

public class ConfiguracaoServidor {

    // Colunas de Limite de Alerta
    private String alertaLeve;
    private String alertaGrave;

    // Chaves estrangeiras (IDs) que identificam a configuração
    private String nome_componente;
    private String nome_metrica;
    private Integer fk_layout;
    private Integer id;

    public ConfiguracaoServidor() {
    }

    public String getAlertaLeve() {
        return alertaLeve;
    }

    public void setAlertaLeve(String alertaLeve) {
        this.alertaLeve = alertaLeve;
    }

    public String getAlertaGrave() {
        return alertaGrave;
    }

    public void setAlertaGrave(String alertaGrave) {
        this.alertaGrave = alertaGrave;
    }

    public String getNome_componente() {

        return nome_componente;
    }

    public void setNome_componente(String nome_componente) {
        this.nome_componente = nome_componente;
    }

    public String getNome_metrica() {
        return nome_metrica;
    }

    public void setNome_metrica(String nome_metrica) {
        this.nome_metrica = nome_metrica;
    }

    public Integer getFk_layout() {
        return fk_layout;
    }

    public void setFk_layout(Integer fk_layout) {
        this.fk_layout = fk_layout;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "ConfiguracaoServidor{" +
                "alertaLeve='" + alertaLeve + '\'' +
                ", alertaGrave='" + alertaGrave + '\'' +
                ", nome_componente='" + nome_componente + '\'' +
                ", nome_metrica='" + nome_metrica + '\'' +
                ", fk_layout=" + fk_layout +
                ", id=" + id +
                '}';
    }
}
