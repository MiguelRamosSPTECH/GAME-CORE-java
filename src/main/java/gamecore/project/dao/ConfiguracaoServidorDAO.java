package gamecore.project.dao;

import gamecore.project.entity.ConfiguracaoServidor;
import gamecore.project.entity.Layout;
import gamecore.project.entity.Servidor;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.dao.EmptyResultDataAccessException;

import java.util.List;

public class ConfiguracaoServidorDAO {

    private JdbcTemplate jdbcTemplate;

    public ConfiguracaoServidorDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Servidor buscarServidorPorMac(String macaddresServidor) {
        try {
            String querySql = "SELECT * FROM servidor WHERE macadress = ?";
            return jdbcTemplate.queryForObject(
                    querySql,
                    new BeanPropertyRowMapper<>(Servidor.class),
                    macaddresServidor
            );
        } catch (EmptyResultDataAccessException e) {
            System.err.println("Servidor não encontrado! - " + macaddresServidor);
            return null;
        }
    }

    public Layout buscarLayoutPorFkEmpresa(Integer fkEmpresa) {
        try {
            String querySql = "SELECT * FROM layout WHERE fk_empresa_layout = ? AND emUso = 1";
            return jdbcTemplate.queryForObject(
                    querySql,
                    new BeanPropertyRowMapper<>(Layout.class),
                    fkEmpresa
            );
        } catch (EmptyResultDataAccessException e) {
            System.err.println("Layout em uso nao encontrado! - PARA EMPRESA DO ID" + fkEmpresa);
            return null;
        }
    }
    public List<ConfiguracaoServidor> buscarConfiguracaoLayout(Integer fkLayout) {
        try {
            String querySql =
                    "select cs.alertaLeve," +
                            "cs.alertaGrave, " +
                            "componente.nome as nomeComponente, " +
                            "metrica.unidadeMedida as nomeMetrica " +
                    "from configuracaoservidor cs\n" +
                    "inner join \n" +
                    "componente on \n" +
                    "cs.fk_componente_cs = componente.id\n" +
                    "inner join metrica on\n" +
                    "cs.fk_metrica_cs = metrica.id\n" +
                    "where fk_layout = ?;";
            return jdbcTemplate.query(
                    querySql,
                    new BeanPropertyRowMapper<>(ConfiguracaoServidor.class),
                    fkLayout
            );
        } catch (EmptyResultDataAccessException e) {
            System.err.println("NAO EXISTEM COMPONENTES E METRICAS PARA ESSE LAYOUT! FK DO LAYOUT " + fkLayout);
            return null;
        }
    }

    public Layout buscarLayoutPorFkLayout(Integer fkLayout) {
        try {
            String querySql = "SELECT * FROM layout where id = ?;";
            return jdbcTemplate.queryForObject(
                    querySql,
                    new BeanPropertyRowMapper<>(Layout.class),
                    fkLayout
            );
        } catch (EmptyResultDataAccessException e) {
            System.err.println("NAO EXISTEM COMPONENTES E METRICAS PARA ESSE LAYOUT! FK DO LAYOUT " + fkLayout);
            return null;
        }
    }

}