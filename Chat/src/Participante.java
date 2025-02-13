import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Participante implements Runnable {
	private static Logger logger = Logger.getLogger(Class.class.getSimpleName());
	
	final Socket socket;
	final Servidor servidor;
	
	PrintWriter out;
	BufferedReader in;
	String apelido, mensagem;
	
	public Participante(Socket socket, Servidor servidor) {
		super();
		this.socket = socket;
		this.servidor = servidor;
	}

	@Override
	public void run() {
		try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);

            apelido = in.readLine();
            logger.log(Level.INFO, "Novo participante {0}", apelido);

            while (true) {
            	mensagem = in.readLine();
                if (mensagem == null || mensagem.equalsIgnoreCase("##sair##")) {
                    break;
                }

                servidor.fofocar(new ServicoMensagem(apelido, mensagem, servidor));
            }
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Erro na comunicação com o participante.", e);
        } finally {
            encerrar();
        }
	}
	
	public void enviarMensagem(String mensagem) {
        out.println(mensagem);
    }

    private void encerrar() {
        try {
            servidor.removerParticipante(this);
            socket.close();
            logger.log(Level.INFO, "Participante " + apelido + " desconectado.");
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Erro ao fechar conexão com o participante.", e);
        }
    }
    
    public String getApelido() { return apelido; }
    
    @Override
    public String toString() {
        return "Participante{" + apelido + '}';
    }
}
