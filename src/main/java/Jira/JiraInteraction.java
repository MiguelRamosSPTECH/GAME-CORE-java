package Jira;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Base64;

public class JiraInteraction {
    private static final String DOMINIO_JIRA = "gamecore.atlassian.net";
    private static final String JIRA_EMAIL = "miguel.ramos@sptech.school";
    private static final String API_TOKEN = "ATATT3xFfGF0ertxdgZtjP9PREf-TZslW-1pQxaNHcC-VDCFSWPwadrCIe1ZMi98G2toJZpXDBuUu8--9DDgJP0MgMUIahoOXwfk1JuDjK69SeqQiV56Knd3T9ESp4OQndFeXH5wdse0ZyXdTilh0yo1cGwMbczfHXKbaX2gGGisOEMnWxNmm2Y=DF189327";

    //transformando em base64 para jogar no Basic Auth
    private static String base64Auth() {
        String credenciais = JIRA_EMAIL+":"+API_TOKEN;
        return "Basic "+Base64.getEncoder().encodeToString(credenciais.getBytes());
    }

    public static void criarTicket(
            String tituloTicket,
            String descricaoTicket,
            String prioridadeTicket,
            String dataResolverTicket
    ) {
        String url = "https://"+DOMINIO_JIRA+"/rest/api/3/issue";

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
                    "duedate": "%s"
                  }
                }
                """, tituloTicket, descricaoTicket, prioridadeTicket, dataResolverTicket);

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

            if(resposta.statusCode() == 201) {
                System.out.println("TICKET CRIADO!");
            } else {
                System.out.println("TICKET NAO CRIADO! Verifique as credenciais e tente novamente!");
            }
        } catch (Exception e) {
            System.out.println("ERRO AO TENTAR LANÇAR UM TICKET (Rede)" + e.getMessage());
            e.printStackTrace();
        }

    }
}
