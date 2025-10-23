import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.dao.EmptyResultDataAccessException;

public class ConfiguracaoServidorDAO {

    private JdbcTemplate jdbcTemplate;

    public ConfiguracaoServidorDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public ConfiguracaoServidor buscarConfiguracaoPorMac(
            String macaddresServidor,
            String nomeComponente,
            String nomeMedida
    ) {

        String sql = "SELECT cs.alertaLeve, cs.alertaGrave " +
                "FROM configuracaoservidor cs " +
                "JOIN layout l ON cs.fk_layout_int = l.id " +
                "JOIN empresa_servidor es ON l.fk_empresa_layout = es.fk_empresa_servidor " +
                "JOIN servidor s ON es.fk_servidor = s.id " +
                "JOIN componente comp ON cs.fk_componente_cs = comp.id " +
                "JOIN metrica m ON cs.fk_metrica_cs = m.id " +
                "WHERE s.macaddres = ? " +
                "AND comp.nome = ? " +
                "AND m.unidadeMedida = ?";

        try {
            return jdbcTemplate.queryForObject(
                    sql,
                    new BeanPropertyRowMapper<>(ConfiguracaoServidor.class),
                    macaddresServidor, nomeComponente, nomeMedida
            );

        } catch (EmptyResultDataAccessException e) {
            System.err.println("Atenção: Configuração de alerta não encontrada para o MAC: " + macaddresServidor);
            return null;
        }
    }
}