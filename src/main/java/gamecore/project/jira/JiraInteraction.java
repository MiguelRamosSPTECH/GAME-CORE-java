package gamecore.project.jira;
import io.github.cdimascio.dotenv.Dotenv;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Base64;

public class JiraInteraction {
    private static final String DOMINIO_JIRA = "gamecore.atlassian.net";
    private static final String JIRA_EMAIL = "miguel.ramos@sptech.school";

    private static final String START_DATE_CUSTOM_FIELD = "customfield_10015";

    //transformando em base64 para jogar no Basic Auth
    private static String base64Auth() {
        Dotenv dotenv = Dotenv.load();
        String tokenJira = dotenv.get("TOKEN_JIRA");
        String credenciais = JIRA_EMAIL+":"+tokenJira;
        return "Basic "+Base64.getEncoder().encodeToString(credenciais.getBytes());
    }

    //pedi pra ia criar um ngc para tratar as strings "especiais"
    private static String escapeJsonString(String value) {
        if (value == null) {
            return "";
        }
        return value.replace("\\", "\\\\") // Escapa a própria barra invertida primeiro
                .replace("\"", "\\\"")  // Escapa aspas duplas
                .replace("\n", "\\n")   // Escapa quebras de linha
                .replace("\r", "");     // Remove retornos de carro
    }

    public static void criarTicket(
            String tituloTicket,
            String descricaoTicket,
            String prioridadeTicket,
            String startDateTicket
    ) {
        String url = "https://"+DOMINIO_JIRA+"/rest/api/3/issue";

        if(prioridadeTicket.equals("Grave")) {
            prioridadeTicket = "Highest";
        } else {
            prioridadeTicket = "Medium";
        }
        // *** PASSO CRÍTICO: Escapando as strings de entrada ***
        String tituloEscapado = escapeJsonString(tituloTicket);
        String descricaoEscapada = escapeJsonString(descricaoTicket);

        String bodyJson = String.format("""
                {
                  "fields": {
                    "project": {
                      "key": "KAN"
                    },
                    "summary": "%s",
                    "description": {
                      "type": "doc",
                      "version": 1,
                      "content": [
                        {
                          "type": "paragraph",
                          "content": [
                            {
                              "type": "text",
                              "text": "%s"
                            }
                          ]
                        }
                      ]
                    },
                    "issuetype": {
                      "name": "Task"
                    },
                    "priority": {
                        "name":"%s"
                    },
                    "%s": "%s"
                  }
                }
                """, tituloEscapado, descricaoEscapada, prioridadeTicket, START_DATE_CUSTOM_FIELD, startDateTicket);

        HttpClient client = HttpClient.newHttpClient();

        //monta requisicao
        HttpRequest request = HttpRequest.newBuilder() //esse newBuilder() é para poder encadear varias configs
                .uri(URI.create(url)) //define url destino
                .header("Content-Type","application/json") //o body vai receber um json
                .header("Authorization", base64Auth()) //passo o token de autorização, tipo o Basic Auth
                .POST(HttpRequest.BodyPublishers.ofString(bodyJson)) //para bytes p ir p net
                .build(); //finaliza montagem
        try {
            //envia resposta p net e usa o HttpResponse.BodyHandlers para transformar o que vier em string
            HttpResponse<String> resposta = client.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println("STATUS CODE TICKET: "+resposta.statusCode());
            if(resposta.statusCode() == 201) {
                System.out.println("TICKET CRIADO!");
            } else {
                System.out.println("TICKET NAO CRIADO! Verifique as credenciais e tente novamente!");
                System.out.println(resposta.body());
            }
        } catch (Exception e) {
            System.out.println("ERRO AO TENTAR LANÇAR UM TICKET (Rede)" + e.getMessage());
            e.printStackTrace();
        }

    }
}
