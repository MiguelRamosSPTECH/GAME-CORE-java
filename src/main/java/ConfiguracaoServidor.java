public class ConfiguracaoServidor {

    // Colunas de Limite de Alerta
    private String alertaLeve;
    private String alertaGrave;

    // Chaves estrangeiras (IDs) que identificam a configuração
    private Integer fkMetricaCs;
    private Integer fkComponenteCs;
    private Integer fkLayoutInt;
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



    @Override
    public String toString() {
        return "ConfiguracaoServidor{" +
                "alertaLeve='" + alertaLeve + '\'' +
                ", alertaGrave='" + alertaGrave + '\'' +
                // ... incluir outros atributos para debug
                '}';
    }
}
