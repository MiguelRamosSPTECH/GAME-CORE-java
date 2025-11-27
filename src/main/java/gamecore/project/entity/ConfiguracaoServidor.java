package gamecore.project.entity;

public class ConfiguracaoServidor {

    // Colunas de Limite de Alerta
    private String alertaLeve;
    private String alertaGrave;

    // Chaves estrangeiras (IDs) que identificam a configuração
    private String nomeComponente;
    private String nomeMetrica;
    private Integer fkLayout;
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

    public String getNomeComponente() {

        return nomeComponente;
    }

    public void setNomeComponente(String nomeComponente) {
        this.nomeComponente = nomeComponente;
    }

    public String getNomeMetrica() {
        return nomeMetrica;
    }

    public void setNomeMetrica(String nomeMetrica) {
        this.nomeMetrica = nomeMetrica;
    }

    public Integer getFkLayout() {
        return fkLayout;
    }

    public void setFkLayout(Integer fkLayout) {
        this.fkLayout = fkLayout;
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
                ", nome_componente='" + nomeComponente + '\'' +
                ", nome_metrica='" + nomeMetrica + '\'' +
                ", fk_layout=" + fkLayout +
                ", id=" + id +
                '}';
    }
}
