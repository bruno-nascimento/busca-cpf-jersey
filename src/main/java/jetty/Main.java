package jetty;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

import org.mortbay.jetty.Server;
import org.mortbay.jetty.servlet.Context;
import org.mortbay.jetty.servlet.ServletHolder;

import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.client.apache.ApacheHttpClient;
import com.sun.jersey.core.util.MultivaluedMapImpl;
import com.sun.jersey.spi.container.servlet.ServletContainer;

public class Main {

	@Path("/buscacpf/{cpf}")
	public static class TestResource {

		@GET
		@Produces(MediaType.APPLICATION_XML)
		public Response get(@PathParam("cpf") String cpf) {
			
			// parametros da requisao do cliente
			MultivaluedMap<String, String> formData = new MultivaluedMapImpl();
			formData.add("login", "teste");
			formData.add("senha", "teste");
			formData.add("cpf", cpf);

			// confs do proxy
			
			// DefaultApacheHttpClientConfig clientConfig = new DefaultApacheHttpClientConfig();
			// clientConfig.getProperties().put(DefaultApacheHttpClientConfig.PROPERTY_PROXY_URI,"http://***.com.br:***");
			// ApacheHttpClientState state = clientConfig.getState();
			// state.setProxyCredentials(AuthScope.ANY_REALM, "***.com.br", ***, "c***", "***");
			// ApacheHttpClient client = ApacheHttpClient.create(clientConfig);

			// executa o post no webservice
			ApacheHttpClient client = ApacheHttpClient.create();
			WebResource webResource = client.resource("http://ws.fontededados.com.br/consulta.asmx/SituacaoCadastralPF");
			String clientResponse = webResource.type("application/x-www-form-urlencoded").post(String.class, formData);
			
			return Response.ok(clientResponse).build();
		}
	}

	public static void main(String[] args) throws Exception {
		ServletHolder sh = new ServletHolder(ServletContainer.class);

		sh.setInitParameter("com.sun.jersey.config.property.resourceConfigClass", "com.sun.jersey.api.core.PackagesResourceConfig");
		sh.setInitParameter("com.sun.jersey.config.property.packages", "jetty");

		Server server = new Server(9999);
		Context context = new Context(server, "/", Context.SESSIONS);
		context.addServlet(sh, "/*");
		server.start();

		// server.stop();
	}
}

/** shouldReturn()...
 * <RetornoSituacaoCadastralPF> 
 * 		<MensagemErro/>
 * 		<ValorCobrado>0.10000</ValorCobrado>
 * 		<CodErro>0</CodErro>
 * 		<Nome>BRUNO CALDEIRA DO NASCIMENTO</Nome>
 * 		<Cpf>xxxxxxxxxxxx</Cpf>
 * 		<SituacaoCadastral>REGULAR</SituacaoCadastral>
 *  </RetornoSituacaoCadastralPF>
 *  ... assert(true)
 */ 
